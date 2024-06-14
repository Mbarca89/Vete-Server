package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.PaginatedResults;
import com.mbarca.vete.domain.Pet;
import com.mbarca.vete.exceptions.PetNotFoundException;
import com.mbarca.vete.repository.PetRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class PetRepositoryImpl implements PetRepository {

    private final String CREATE_PET = "INSERT INTO Pets (name, race, gender, species, weight, born, photo, thumbnail) VALUES (?,?,?,?,?,?,?,?)";
    private final String GET_PET_BY_ID = "SELECT Pets.*, CONCAT(Clients.name, ' ', Clients.surname) AS owner_name FROM Pets JOIN ClientPets ON Pets.id = ClientPets.pet_id JOIN Clients ON ClientPets.client_id = Clients.id WHERE Pets.id = ?";


    private final JdbcTemplate jdbcTemplate;

    public PetRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createPet(Pet pet, Long clientId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(CREATE_PET, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pet.getName());
            ps.setString(2, pet.getRace());
            ps.setString(3, pet.getGender());
            ps.setString(4,pet.getSpecies());
            ps.setDouble(5, pet.getWeight());
            java.util.Date utilDate = pet.getBorn();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            ps.setDate(6, sqlDate);
            ps.setBytes(7, pet.getPhoto());
            ps.setBytes(8, pet.getThumbnail());
            return ps;
        }, keyHolder);

        Long petId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        String RELATE_PET = "INSERT INTO ClientPets (client_id, pet_id) VALUES (?, ?)";
        return jdbcTemplate.update(RELATE_PET, clientId, petId);
    }

    @Override
    public Integer getPetCount() {
    return null;
    }

    @Override
    public PaginatedResults<Pet> getAllPets(int limit, int offset) throws Exception {
        String GET_PET_COUNT = "SELECT COUNT(*) FROM pets";
        Integer totalCount = jdbcTemplate.queryForObject(GET_PET_COUNT, Integer.class);
        String GET_ALL_PETS = "SELECT p.*, CONCAT(c.name, ' ', c.surname) AS owner_name " +
                "FROM Pets p " +
                "INNER JOIN ClientPets cp ON p.id = cp.pet_id " +
                "INNER JOIN Clients c ON cp.client_id = c.id " +
                "LIMIT ? OFFSET ?";
        List<Pet> pets = jdbcTemplate.query(GET_ALL_PETS, new Object[]{limit, offset}, new PetRowMapper(true, false));
        return new PaginatedResults(pets, totalCount != null ? totalCount:0);
    }

    @Override
    public List<Pet> getPetsFromClient(Long clientId) {
        Object[] params = {clientId};
        String GET_PETS_FROM_CLIENT = "SELECT p.*, CONCAT(c.name, ' ', c.surname) AS owner_name " +
                "FROM Pets p " +
                "JOIN ClientPets cp ON p.id = cp.pet_id " +
                "JOIN Clients c ON cp.client_id = c.id " +
                "WHERE cp.client_id = ?";
        return jdbcTemplate.query(GET_PETS_FROM_CLIENT, params, new PetRowMapper(true, false));
    }

    @Override
    public PaginatedResults<Pet> getPetsByName(String name, String species, int limit, int offset) {
        String GET_PET_COUNT_WITH_SPECIES = "SELECT COUNT(*) FROM pets WHERE LOWER(name) LIKE LOWER(?) AND species = ?";
        String GET_PET_COUNT = "SELECT COUNT(*) FROM pets WHERE LOWER(name) LIKE LOWER(?)";
        Integer totalCount;
        String searchTerm = "%" + name + "%";
        if(!Objects.equals(species, "")) totalCount = jdbcTemplate.queryForObject(GET_PET_COUNT_WITH_SPECIES, new Object[]{searchTerm, species}, Integer.class);
        else totalCount = jdbcTemplate.queryForObject(GET_PET_COUNT, new Object[]{searchTerm}, Integer.class);
        String GET_PET_BY_NAME = "SELECT * FROM Pets WHERE LOWER(name) LIKE LOWER(?) LIMIT ? OFFSET ?";
        String GET_PET_BY_NAME_AND_SPECIES = "SELECT * FROM Pets WHERE LOWER(name) LIKE LOWER(?) AND species = ? LIMIT ? OFFSET ?";
        List<Pet> pets;
        if(!Objects.equals(species, "")) pets = jdbcTemplate.query(GET_PET_BY_NAME_AND_SPECIES, new Object[]{searchTerm, species, limit, offset}, new PetRowMapper(false, false));
        else pets = jdbcTemplate.query(GET_PET_BY_NAME, new Object[]{searchTerm, limit, offset}, new PetRowMapper(false, false));
        return new PaginatedResults<Pet>(pets, totalCount != null ? totalCount:0);
    }

    @Override
    public Pet getPetById(Long petId) {
        Object[] params = {petId};
        int[] types = {1};
        return jdbcTemplate.queryForObject(GET_PET_BY_ID, params, types, new PetRowMapper(true, true));
    }

    @Override
    public Integer deletePet(Long petId) {
        String DELETE_PET_CLIENT = "DELETE FROM ClientPets WHERE pet_id = ?";
        jdbcTemplate.update(DELETE_PET_CLIENT, petId);
        String DELETE_PET = "DELETE FROM Pets WHERE id = ?";
        return jdbcTemplate.update(DELETE_PET, petId);
    }

    @Override
    public Integer editPet(Pet newPet) throws PetNotFoundException {
        Pet editPet = new Pet();
        Long petId = newPet.getId();

        Object[] params = {petId};
        int[] types = {1};
        Pet currentPet = jdbcTemplate.queryForObject(GET_PET_BY_ID, params, types, new PetRowMapper(false, true));

        if (currentPet == null) {
            throw new PetNotFoundException("Mascota no encontrada!");
        }

        editPet.setName(newPet.getName());
        if (!newPet.getRace().isEmpty()) editPet.setRace(newPet.getRace()); else editPet.setRace(currentPet.getRace());
        if (newPet.getGender() != null && !newPet.getGender().isEmpty()) editPet.setGender(newPet.getGender()); else editPet.setGender(currentPet.getGender());
        if (newPet.getSpecies() != null && !newPet.getSpecies().isEmpty()) editPet.setSpecies(newPet.getSpecies()); else editPet.setSpecies(currentPet.getSpecies());
        if (newPet.getWeight() != 0) editPet.setWeight(newPet.getWeight()); else editPet.setWeight(currentPet.getWeight());
        if (newPet.getBorn() != null) editPet.setBorn(newPet.getBorn()); else editPet.setBorn(currentPet.getBorn());
        if (newPet.getPhoto() != null) editPet.setPhoto(newPet.getPhoto()); else editPet.setPhoto(currentPet.getPhoto());
        if (newPet.getThumbnail() != null) editPet.setThumbnail(newPet.getThumbnail()); else editPet.setThumbnail(currentPet.getThumbnail());

        String EDIT_PET = "UPDATE pets SET name = ?, race = ?, gender = ?, species = ?, weight = ?, born = ?, photo = ?, thumbnail = ? WHERE id = ?";
        return jdbcTemplate.update(EDIT_PET, editPet.getName(), editPet.getRace(), editPet.getGender(), editPet.getSpecies(), editPet.getWeight(), editPet.getBorn(), editPet.getPhoto(), editPet.getThumbnail(), newPet.getId());
    }

    static class PetRowMapper implements RowMapper<Pet> {
        boolean includeOwner;
        boolean includePhoto;

        public PetRowMapper(boolean includeOwner, boolean includePhoto) {
            this.includeOwner = includeOwner;
            this.includePhoto = includePhoto;
        }


        @Override
        public Pet mapRow(ResultSet rs, int rowNum) throws SQLException {
            Pet pet = new Pet();
            pet.setId(rs.getLong("id"));
            pet.setName(rs.getString("name"));
            pet.setRace(rs.getString("race"));
            pet.setGender(rs.getString("gender"));
            pet.setSpecies(rs.getString("species"));
            pet.setWeight(rs.getDouble("weight"));
            pet.setBorn(rs.getDate("born"));
            if(includePhoto) pet.setPhoto(rs.getBytes("photo"));
            pet.setThumbnail(rs.getBytes("thumbnail"));
            if(includeOwner) pet.setOwnerName(rs.getString("owner_name"));
            return pet;
        }
    }
}
