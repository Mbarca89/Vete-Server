package com.mbarca.vete.service;

import com.mbarca.vete.dto.response.PublicPetProfileResponseDto;

import java.util.UUID;

public interface PublicPetProfileService {
    PublicPetProfileResponseDto getPetProfile(UUID publicId);
    String sendPublicProfile (UUID publicId);
}
