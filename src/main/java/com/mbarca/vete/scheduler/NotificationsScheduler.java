package com.mbarca.vete.scheduler;

import com.mbarca.vete.domain.Reminder;
import com.mbarca.vete.domain.VaccineNotification;
import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.domain.WebOrderItem;
import com.mbarca.vete.repository.MessagesRepository;
import com.mbarca.vete.repository.ReminderRepository;
import com.mbarca.vete.repository.VaccineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
@Slf4j
public class NotificationsScheduler {

    VaccineRepository vaccineRepository;
    ReminderRepository reminderRepository;
    MessagesRepository messagesRepository;

    public NotificationsScheduler(VaccineRepository vaccineRepository, ReminderRepository reminderRepository, MessagesRepository messagesRepository) {
        this.vaccineRepository = vaccineRepository;
        this.reminderRepository = reminderRepository;
        this.messagesRepository = messagesRepository;
    }


    @Scheduled(cron = "0 00 10 * * *")
    public void checkVaccineRecords() throws Exception {
        List<VaccineNotification> vaccineNotifications = vaccineRepository.getTodayVaccines();
        for (VaccineNotification vaccineNotification : vaccineNotifications) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("http://localhost:3001/ws/send"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString("{\"number\": \"" + vaccineNotification.getClientPhone() + "\",\"message\": \"" + "Hola " + vaccineNotification.getClientName() + "! Te recordamos que hoy tenés un turno para " + vaccineNotification.getPetName() + ". Motivo: " + vaccineNotification.getVaccineName() + "\" }"))
                        .version(HttpClient.Version.HTTP_1_1)
                        .build();
                HttpResponse<String> response;
                try (HttpClient http = HttpClient.newHttpClient()) {
                    response = http.send(request, HttpResponse.BodyHandlers.ofString());
                }
                if(response.statusCode() == 200) vaccineNotification.setSent(true);
                messagesRepository.saveMessage(vaccineNotification);
            } catch (URISyntaxException | IOException | InterruptedException e) {
                throw new Exception(e.getMessage());
            }
        }
    }

    @Scheduled(cron = "0 01 10 * * *")
    public void checkReminders() throws Exception {
        List<Reminder> reminders = reminderRepository.getTodayReminder();
        for (Reminder reminder : reminders) {
            if (reminder.getPhone() != null) {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:3001/ws/send"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString("{\"number\": \"" + reminder.getPhone() + "\",\"message\": \"" + "Hola! Veterinaria del Parque te recuerda que hoy tenés un turno para " + reminder.getName() + "\" }"))
                            .version(HttpClient.Version.HTTP_1_1)
                            .build();
                    HttpResponse<String> response;
                    try (HttpClient http = HttpClient.newHttpClient()) {
                        response = http.send(request, HttpResponse.BodyHandlers.ofString());
                    }
                    if (response.statusCode() == 200) reminder.setSent(true);
                    messagesRepository.saveReminder(reminder);
                } catch (URISyntaxException | IOException | InterruptedException e) {
                    throw new Exception(e.getMessage());
                }
            }
        }
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

    private void sendWhatsapp(String phone, String message) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:3001/ws/send"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{\"number\":\"" + phone + "\",\"message\":\"" + message + "\"}"
                    ))
                    .build();

            try (HttpClient client = HttpClient.newHttpClient()) {
                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                   log.warn("Error enviando WhatsApp: {}", response.body());
                }
            }
        } catch (Exception e) {
            log.error("Error enviando WhatsApp", e);
        }
    }
}

