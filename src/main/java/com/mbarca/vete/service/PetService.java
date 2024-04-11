package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.PetRequestDto;
import com.mbarca.vete.dto.response.PetResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;

import java.io.IOException;
import java.util.List;

public interface PetService {
    String createPet (PetRequestDto petRequestDto, byte[] compressedImage, Long clientId) throws MissingDataException;
    Integer getPetCount();
    List<PetResponseDto> getAllPets(int page, int size);
    List<PetResponseDto> getPetsFromClient(Long clientId);
    String deletePet (Long petId);
    byte[] compressImage(byte[] imageData) throws IOException;
}
