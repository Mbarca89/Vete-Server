package com.mbarca.vete.repository;

import com.mbarca.vete.domain.PaginatedResults;
import com.mbarca.vete.domain.Pet;
import com.mbarca.vete.domain.User;
import com.mbarca.vete.exceptions.PetNotFoundException;

import java.util.List;

public interface PetRepository {
    Integer createPet (Pet pet, Long clientId);
    Integer getPetCount ();
    PaginatedResults<Pet> getAllPets (int limit, int offset) throws Exception;
    List<Pet> getPetsFromClient (Long clientId);
    PaginatedResults<Pet> getPetsByName (String name, String species, int limit, int offset);
    Pet getPetById (Long petId);
    Integer deletePet (Long petId);
    Integer editPet(Pet pet) throws PetNotFoundException;
}
