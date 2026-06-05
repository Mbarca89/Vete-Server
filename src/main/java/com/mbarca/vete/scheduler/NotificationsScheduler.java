package com.mbarca.vete.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbarca.vete.domain.Reminder;
import com.mbarca.vete.domain.VaccineNotification;
import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.domain.WebOrderItem;
import com.mbarca.vete.repository.MessagesRepository;
import com.mbarca.vete.repository.ReminderRepository;
import com.mbarca.vete.repository.VaccineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class NotificationsScheduler {

    VaccineRepository vaccineRepository;
    ReminderRepository reminderRepository;
    MessagesRepository messagesRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationsScheduler(VaccineRepository vaccineRepository, ReminderRepository reminderRepository, MessagesRepository messagesRepository) {
        this.vaccineRepository = vaccineRepository;
        this.reminderRepository = reminderRepository;
        this.messagesRepository = messagesRepository;
    }

    @Value("${frontend.base-url}")
    private String frontendBaseUrl;


    @Scheduled(cron = "0 00 10 * * *")
    public void checkVaccineRecords() {
        List<VaccineNotification> vaccineNotifications = vaccineRepository.getTodayVaccines();
        for (VaccineNotification vaccineNotification : vaccineNotifications) {
            String message = buildVaccineMessage(vaccineNotification);
            SendResult result = sendWhatsappDetailed(vaccineNotification.getClientPhone(), message);

            vaccineNotification.setSent(result.sent());
            vaccineNotification.setFailureReason(result.failureReason());
            vaccineRepository.updateNotificationStatus(vaccineNotification);
            messagesRepository.saveMessage(vaccineNotification);
        }
    }

    @Scheduled(cron = "0 01 10 * * *")
    public void checkReminders() {
        List<Reminder> reminders = reminderRepository.getTodayReminder();
        for (Reminder reminder : reminders) {
            String message = buildReminderMessage(reminder);
            SendResult result = sendWhatsappDetailed(reminder.getPhone(), message);

            reminder.setSent(result.sent());
            reminder.setFailureReason(result.failureReason());
            reminderRepository.updateNotificationStatus(reminder);
            messagesRepository.saveReminder(reminder);
        }
    }

    private String buildVaccineMessage(VaccineNotification vaccineNotification) {
        return "Hola " + vaccineNotification.getClientName() + " 👋\n\n"
                + "Te recordamos que hoy " + vaccineNotification.getPetName() + " tiene un turno en *Veterinaria del Parque*.\n\n"
                + "Motivo: *" + vaccineNotification.getVaccineName() + "*\n\n"
                + "Si necesitás reprogramarlo, respondé este mensaje y te ayudamos.";
    }

    private String buildReminderMessage(Reminder reminder) {
        return "Hola 👋\n\n"
                + "Desde *Veterinaria del Parque* te recordamos que hoy tenés un turno pendiente.\n\n"
                + "Motivo: *" + reminder.getName() + "*\n\n"
                + "Si necesitás reprogramarlo, respondé este mensaje y coordinamos un nuevo horario.";
    }

    public void sendOrderConfirmation(WebOrder order, List<WebOrderItem> items) {

        StringBuilder message = new StringBuilder();
        message.append("Hola ").append(order.getCustomerName()).append(" 👋\n\n")
                .append("✅ *Pago aprobado*\n")
                .append("Gracias por tu compra en *Veterinaria del Parque* 🐾\n\n")
                .append("🧾 *Detalle del pedido:*\n");

        for (WebOrderItem item : items) {
            message.append("- ")
                    .append(item.getProductName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append("\n");
        }

        message.append("\n💰 *Total:* $")
                .append(order.getTotalAmount())
                .append("\n\n")
                .append("📍 En breve nos comunicamos para coordinar la entrega.\n")
                .append("¡Gracias por confiar en nosotros! ❤️");

        sendWhatsapp(order.getCustomerPhone(), message.toString());
    }

    public boolean sendPetPublicProfile(
            String petName,
            String ownerName,
            String ownerPhone,
            UUID publicId
    ) {

        final String publicProfileUrl = frontendBaseUrl + "/mascotas/" + publicId;

        StringBuilder message = new StringBuilder();

        message.append("Hola ").append(ownerName).append(" 👋\n\n")
                .append("🐾 *Ficha médica de ").append(petName).append("*\n\n")
                .append("Ya podés acceder a la información de tu mascota desde el siguiente enlace:\n\n")
                .append("🔗 ").append(publicProfileUrl).append("\n\n")
                .append("📋 Ahí vas a encontrar datos importantes como:\n")
                .append("• Información general\n")
                .append("• Historial médico\n")
                .append("• Vacunas y controles\n\n")
                .append("Gracias por confiar en *Veterinaria del Parque* ❤️");

        return sendWhatsapp(ownerPhone, message.toString());
    }


    record WsSendReq(String number, String message) {}
    record SendResult(boolean sent, String failureReason) {}

    private String normalizePhone(String phone) {
        if (phone == null) return "";
        return phone.replaceAll("[^0-9]", "");
    }

    private boolean sendWhatsapp(String phone, String message) {
        return sendWhatsappDetailed(phone, message).sent();
    }

    private SendResult sendWhatsappDetailed(String phone, String message) {
        try {
            String number = normalizePhone(phone);
            if (number.isBlank()) {
                return new SendResult(false, "No se pudo enviar: telefono vacio o invalido");
            }

            String json = objectMapper.writeValueAsString(new WsSendReq(number, message));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:3001/ws/send"))
                    .header("Content-Type", "application/json")
                    .version(HttpClient.Version.HTTP_1_1)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("WS status={} body={}", response.statusCode(), response.body());
            log.info("WS payload -> number='{}' len(message)={} url={}",
                    phone, message != null ? message.length() : 0, "http://127.0.0.1:3001/ws/send");

            if (response.statusCode() == 200) {
                return new SendResult(true, null);
            }

            return new SendResult(false, "WS respondio HTTP " + response.statusCode() + ": " + truncate(response.body()));

        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Error enviando WhatsApp", e);
            return new SendResult(false, "Error enviando WhatsApp: " + truncate(e.getMessage()));
        }
    }

    private String truncate(String value) {
        if (value == null || value.isBlank()) {
            return "sin detalle";
        }
        return value.length() <= 250 ? value : value.substring(0, 250);
    }

}
