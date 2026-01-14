package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.MedicalHistory;
import com.mbarca.vete.domain.Pet;
import com.mbarca.vete.domain.Vaccine;
import com.mbarca.vete.dto.response.PublicPetProfileResponseDto;
import com.mbarca.vete.repository.MedicalHistoryRepository;
import com.mbarca.vete.repository.PetRepository;
import com.mbarca.vete.repository.VaccineRepository;
import com.mbarca.vete.scheduler.NotificationsScheduler;
import com.mbarca.vete.service.FileStorageService;
import com.mbarca.vete.service.PublicPetProfileService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PublicPetProfileServiceImpl implements PublicPetProfileService {

    private final PetRepository petRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final VaccineRepository vaccineRepository;
    private final NotificationsScheduler notificationsScheduler;
    private final FileStorageService fileStorageService;

    public PublicPetProfileServiceImpl(PetRepository petRepository, MedicalHistoryRepository medicalHistoryRepository, VaccineRepository vaccineRepository, NotificationsScheduler notificationsScheduler, FileStorageService fileStorageService) {
        this.petRepository = petRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.vaccineRepository = vaccineRepository;
        this.notificationsScheduler = notificationsScheduler;
        this.fileStorageService = fileStorageService;
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

    @Override
    public ResponseEntity<Resource> downloadMedicalHistoryFile(UUID publicId, Long medicalHistoryId) {
        Pet pet = petRepository.getPetByPublicId(publicId);
        if (pet == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mascota no encontrada");

        MedicalHistory mh = medicalHistoryRepository.getMedicalHistoryByIdAndPetId(medicalHistoryId, pet.getId());
        if (mh == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro médico no encontrado");

        String filePath = mh.getFile();
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El registro no tiene archivo");
        }

        // En DB guardás "/otp/fileStorage/20250513...pdf"
        // FileStorageService resuelve relativo a rootLocation, así que pasamos sólo el nombre del archivo
        String filename = Paths.get(filePath).getFileName().toString();

        Resource resource;
        try {
            resource = fileStorageService.loadAsResource(filename);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo no disponible");
        }

        // Para PDF lo podemos setear más lindo:
        MediaType contentType = filename.toLowerCase().endsWith(".pdf")
                ? MediaType.APPLICATION_PDF
                : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .cacheControl(CacheControl.noStore())
                .body(resource);
    }
}
