package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.Pet;
import com.mbarca.vete.domain.Product;
import com.mbarca.vete.dto.request.PetRequestDto;
import com.mbarca.vete.dto.response.PetResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.repository.PetRepository;
import com.mbarca.vete.service.PetService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public String createPet(PetRequestDto petRequestDto, byte[] compressedImage, Long clientId) throws MissingDataException {
        if (Objects.equals(petRequestDto.getName(),"")) throw new MissingDataException("Faltan datos!");

        Pet pet = mapDtoToPet(petRequestDto);
        pet.setPhoto(compressedImage);
        Integer response = petRepository.createPet(pet, clientId);
        if (response.equals(0)) {
            return "Error al crear la mascota!";
        }
        return "Mascota creada correctamente!";
    }

    @Override
    public Integer getPetCount() {
        return petRepository.getPetCount();
    }

    @Override
    public List<PetResponseDto> getAllPets(int page, int size) {
        int offset = (page -1) * size;
        List<Pet> pets = petRepository.getAllPets(size, offset);
        return pets.stream().map(this::mapPetToDto).collect(Collectors.toList());
    }

    @Override
    public List<PetResponseDto> getPetsFromClient(Long clientId) {
        List<Pet> pets = petRepository.getPetsFromClient(clientId);
        return pets.stream().map(this::mapPetToDto).collect(Collectors.toList());
    }

    @Override
    public List<PetResponseDto> getPetsByName(String name, int page, int size) {
        int offset = (page - 1) * size;
        List<Pet> pets = petRepository.getPetsByName(name, size, offset);
        return pets.stream().map(this::mapPetToDto).collect(Collectors.toList());
    }

    @Override
    public PetResponseDto getPetById (Long petId) {
        return mapPetToDto(petRepository.getPetById(petId));
    }

    @Override
    public String deletePet(Long petId) {
        Integer response = petRepository.deletePet(petId);
        if (response.equals(0)) {
            throw new EmptyResultDataAccessException(1);
        }
        return "Mascota eliminada correctamente";
    }

    @Override
    public byte[] compressImage(byte[] imageData) throws IOException, MaxUploadSizeExceededException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(ImageIO.read(new ByteArrayInputStream(imageData)), "jpg", outputStream);
        return outputStream.toByteArray();
    }

    private Pet mapDtoToPet(PetRequestDto petRequestDto) {
        Pet pet = new Pet();
        pet.setName(petRequestDto.getName());
        pet.setRace(petRequestDto.getRace());
        pet.setWeight(petRequestDto.getWeight());
        pet.setBorn(petRequestDto.getBorn());

        return pet;
    }

    private PetResponseDto mapPetToDto (Pet pet) {
        PetResponseDto petResponseDto = new PetResponseDto();
        petResponseDto.setId(pet.getId());
        petResponseDto.setName(pet.getName());
        petResponseDto.setRace(pet.getRace());
        petResponseDto.setWeight(pet.getWeight());
        petResponseDto.setBorn(pet.getBorn());
        petResponseDto.setPhoto(pet.getPhoto());
        return petResponseDto;
    }
}
