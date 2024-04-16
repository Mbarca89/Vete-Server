package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Pet;

import java.util.List;

public interface PetRepository {
    Integer createPet (Pet pet, Long clientId);
    Integer getPetCount ();
    List<Pet> getAllPets (int limit, int offset);
    List<Pet> getPetsFromClient (Long clientId);
    List<Pet> getPetsByName (String name, int limit, int offset);
    Pet getPetById (Long petId);
    Integer deletePet (Long petId);
}
