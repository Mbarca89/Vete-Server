package com.mbarca.vete.service;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.mbarca.vete.domain.PaginatedResults;
import com.mbarca.vete.dto.request.PetRequestDto;
import com.mbarca.vete.dto.request.UserRequestDto;
import com.mbarca.vete.dto.response.PetResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.exceptions.PetNotFoundException;
import com.mbarca.vete.exceptions.UserNotFoundException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface PetService {
    String createPet (PetRequestDto petRequestDto, byte[] compressedImage, Long clientId) throws MissingDataException;
    Integer getPetCount();
    PaginatedResults<PetResponseDto> getAllPets(int page, int size) throws Exception;
    List<PetResponseDto> getPetsFromClient(Long clientId);
    List<PetResponseDto> getPetsByName(String name, int page, int size);
    PetResponseDto getPetById (Long petId);
    String deletePet (Long petId);
    String editPet (PetRequestDto petRequestDto, byte[] compressedImage) throws MissingDataException, NoSuchAlgorithmException, UserNotFoundException, PetNotFoundException;
    byte[] compressImage(byte[] imageData) throws IOException, ImageProcessingException, ImageProcessingException, MetadataException;
}
