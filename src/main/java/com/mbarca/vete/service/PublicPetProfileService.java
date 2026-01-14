package com.mbarca.vete.service;

import com.mbarca.vete.dto.response.PublicPetProfileResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface PublicPetProfileService {
    PublicPetProfileResponseDto getPetProfile(UUID publicId);
    String sendPublicProfile (UUID publicId);
    ResponseEntity<Resource> downloadMedicalHistoryFile(UUID publicId, Long medicalHistoryId);

}
