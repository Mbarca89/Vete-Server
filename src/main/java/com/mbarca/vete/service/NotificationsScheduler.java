//package com.mbarca.vete.service;
//
//import com.mbarca.vete.domain.VaccineNotification;
//import com.mbarca.vete.repository.VaccineRepository;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class NotificationsScheduler {
//
//    VaccineRepository vaccineRepository;
//
//    public NotificationsScheduler(VaccineRepository vaccineRepository) {
//        this.vaccineRepository = vaccineRepository;
//    }
//
//    @Scheduled(cron = "0 59 11 * * *")
//    public void checkVaccineRecords() {
//        List<VaccineNotification> vaccineNotifications = vaccineRepository.getTodayVaccines();
//        for (VaccineNotification vaccineNotification : vaccineNotifications) {
//            try {
//                HttpRequest request = HttpRequest.newBuilder()
//                        .uri(new URI("https://graph.facebook.com/v13.0/302138896318143/messages"))
//                        .header("Authorization", "Bearer EAAUaJ4XFv2UBOZBbRun6c7oQWvDPOnHZAhZA8jf5WgkfCidbVV5lUMK7jr9rtNY6sM4zyM1jB0IMBCQZCDybCINdfJFxslDSe4X29UfezNIGNSS9LQDyzzGENEGkB0ZCnQwZAGQtzHINkCZAX6XIJ44Q1APvZALvq3vUvNtFVS9UBN0sSDEMFyPh8nuDkZBhoI6R8OeAg7Rq6riERQTqBoyRv")
//                        .header("Content-Type", "application/json")
//                        .POST(HttpRequest.BodyPublishers.ofString("{ \"messaging_product\": \"whatsapp\", \"to\": \"" + vaccineNotification.getClientPhone() + "\", \"type\": \"template\", \"template\": { \"name\": \"recordatorio_servicio\", \"language\": { \"code\": \"es_AR\" }, \"components\": [{ \"type\": \"body\", \"parameters\": [{ \"type\": \"text\", \"text\": \"" + vaccineNotification.getClientName() + "\" }, { \"type\": \"text\", \"text\": \"" + vaccineNotification.getPetName() + "\" }, { \"type\": \"text\", \"text\": \"" + vaccineNotification.getVaccineName() + "\" }] }] } }"))
//                        .build();
//                HttpClient http = HttpClient.newHttpClient();
//                HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
//                System.out.println(response.body());
//
//            } catch (URISyntaxException | IOException | InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
//
