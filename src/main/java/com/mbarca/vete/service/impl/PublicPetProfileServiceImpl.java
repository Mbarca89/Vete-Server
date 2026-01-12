package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.MedicalHistory;
import com.mbarca.vete.domain.Pet;
import com.mbarca.vete.domain.Vaccine;
import com.mbarca.vete.dto.response.PublicPetProfileResponseDto;
import com.mbarca.vete.repository.MedicalHistoryRepository;
import com.mbarca.vete.repository.PetRepository;
import com.mbarca.vete.repository.VaccineRepository;
import com.mbarca.vete.scheduler.NotificationsScheduler;
import com.mbarca.vete.service.PublicPetProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PublicPetProfileServiceImpl implements PublicPetProfileService {

    private final PetRepository petRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final VaccineRepository vaccineRepository;
    private final NotificationsScheduler notificationsScheduler;

    public PublicPetProfileServiceImpl(PetRepository petRepository, MedicalHistoryRepository medicalHistoryRepository, VaccineRepository vaccineRepository, NotificationsScheduler notificationsScheduler) {
        this.petRepository = petRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.vaccineRepository = vaccineRepository;
        this.notificationsScheduler = notificationsScheduler;
    }

    @Override
    public PublicPetProfileResponseDto getPetProfile(UUID publicId) {
        Pet pet = petRepository.getPetByPublicId(publicId);
        List<MedicalHistory> medicalHistoryList = medicalHistoryRepository.getMedicalHistoryForPet(pet.getId());
        List<Vaccine> vaccineList = vaccineRepository.getVaccinesById(pet.getId());

        return new PublicPetProfileResponseDto(pet, medicalHistoryList, vaccineList);
    }

    @Override
    public String sendPublicProfile(UUID publicId) {
        Pet pet = petRepository.getPetByPublicId(publicId);
        boolean sent = notificationsScheduler.sendPetPublicProfile(pet.getName(), pet.getOwnerName(), pet.getOwnerPhone(), pet.getPublicId());
        return sent ? "Mensaje enviado correctamente" : "Error al enviar el mensaje";
    }
}
