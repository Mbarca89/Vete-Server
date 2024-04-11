package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Pet;

import java.util.List;

public interface PetRepository {
    Integer createPet (Pet pet, Long clientId);
    Integer getPetCount ();
    List<Pet> getAllPets (int limit, int offset);
    List<Pet> getPetsFromClient (Long clientId);
    Integer deletePet (Long petId);
}
