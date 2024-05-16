package com.mbarca.vete.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.mbarca.vete.domain.PaginatedResults;
import com.mbarca.vete.domain.Pet;
import com.mbarca.vete.domain.Product;
import com.mbarca.vete.domain.User;
import com.mbarca.vete.dto.request.PetRequestDto;
import com.mbarca.vete.dto.request.UserRequestDto;
import com.mbarca.vete.dto.response.PetResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.exceptions.PetNotFoundException;
import com.mbarca.vete.exceptions.UserNotFoundException;
import com.mbarca.vete.repository.PetRepository;
import com.mbarca.vete.service.PetService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
    public PaginatedResults<PetResponseDto> getAllPets(int page, int size) throws Exception {
        int offset = (page -1) * size;
        PaginatedResults<Pet> pets = petRepository.getAllPets(size, offset);
        List<PetResponseDto> petsResponse = pets.getData().stream().map(this::mapPetToDto).toList();
        return new PaginatedResults<PetResponseDto>(petsResponse, pets.getTotalCount());
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
    public String editPet(PetRequestDto petRequestDto, byte[] compressedImage) throws MissingDataException, NoSuchAlgorithmException, UserNotFoundException, PetNotFoundException {

        if (petRequestDto.getName() == null || Objects.equals(petRequestDto.getName(), "")) {
            throw new MissingDataException("Faltan datos!");
        }

        Pet pet = mapDtoToPet(petRequestDto);
        if(compressedImage != null) pet.setPhoto(compressedImage);
        Integer response = petRepository.editPet(pet);

        if (response.equals(0)) {
            return "Error al editar la mascota!";
        }
        return "Mascota editada correctamente!";
    }

    @Override
    public byte[] compressImage(byte[] imageData) throws IOException, MaxUploadSizeExceededException, ImageProcessingException, MetadataException {
        Metadata metadata = ImageMetadataReader.readMetadata(new ByteArrayInputStream(imageData));
        ExifIFD0Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        int orientation = exifDirectory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        if (orientation != 1) {
            AffineTransform transform = new AffineTransform();
            switch (orientation) {
                case 6:
                    transform.translate(originalImage.getHeight(), 0);
                    transform.rotate(Math.toRadians(90));
                    break;
                case 3:
                    transform.translate(originalImage.getWidth(), originalImage.getHeight());
                    transform.rotate(Math.toRadians(180));
                    break;
                case 8:
                    transform.translate(0, originalImage.getWidth());
                    transform.rotate(Math.toRadians(270));
                    break;
            }
            originalImage = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)
                    .filter(originalImage, null);
        }
        BufferedImage outputImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        outputImage.createGraphics().drawImage(originalImage, 0, 0, Color.WHITE, null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "jpg", outputStream);
        return outputStream.toByteArray();
    }

    private Pet mapDtoToPet(PetRequestDto petRequestDto) {
        Pet pet = new Pet();
        pet.setId(petRequestDto.getId());
        pet.setName(petRequestDto.getName());
        pet.setRace(petRequestDto.getRace());
        pet.setGender(petRequestDto.getGender());
        pet.setSpecies(petRequestDto.getSpecies());
        pet.setWeight(petRequestDto.getWeight());
        pet.setBorn(petRequestDto.getBorn());

        return pet;
    }

    private PetResponseDto mapPetToDto (Pet pet) {
        PetResponseDto petResponseDto = new PetResponseDto();
        petResponseDto.setId(pet.getId());
        petResponseDto.setName(pet.getName());
        petResponseDto.setRace(pet.getRace());
        petResponseDto.setGender(pet.getGender());
        petResponseDto.setSpecies(pet.getSpecies());
        petResponseDto.setWeight(pet.getWeight());
        petResponseDto.setBorn(pet.getBorn());
        petResponseDto.setPhoto(pet.getPhoto());
        petResponseDto.setOwnerName(pet.getOwnerName());
        return petResponseDto;
    }
}
