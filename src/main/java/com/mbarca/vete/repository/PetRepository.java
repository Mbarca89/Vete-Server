package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Pet;
import com.mbarca.vete.domain.User;
import com.mbarca.vete.exceptions.PetNotFoundException;

import java.util.List;

public interface PetRepository {
    Integer createPet (Pet pet, Long clientId);
    Integer getPetCount ();
    List<Pet> getAllPets (int limit, int offset);
    List<Pet> getPetsFromClient (Long clientId);
    List<Pet> getPetsByName (String name, int limit, int offset);
    Pet getPetById (Long petId);
    Integer deletePet (Long petId);
    Integer editPet(Pet pet) throws PetNotFoundException;
}
