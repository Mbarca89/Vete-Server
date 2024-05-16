package com.mbarca.vete.service;

import com.mbarca.vete.domain.Reminder;
import com.mbarca.vete.domain.VaccineNotification;
import com.mbarca.vete.repository.MessagesRepository;
import com.mbarca.vete.repository.ReminderRepository;
import com.mbarca.vete.repository.VaccineRepository;
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
}

