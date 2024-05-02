package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Pet;
import com.mbarca.vete.domain.User;
import com.mbarca.vete.exceptions.PetNotFoundException;
import com.mbarca.vete.exceptions.UserNotFoundException;
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

    private final String CREATE_PET = "INSERT INTO Pets (name, race, gender, species, weight, born, photo) VALUES (?,?,?,?,?,?,?)";
    private final String GET_PET_COUNT = "SELECT COUNT(*) FROM pets";
    private final String GET_ALL_PETS = "SELECT * FROM Pets LIMIT ? OFFSET ?";
    private final String DELETE_PET = "DELETE FROM Pets WHERE id = ?";
    private final String DELETE_PET_CLIENT = "DELETE FROM ClientPets WHERE pet_id = ?";
    private final String GET_PET_BY_NAME = "SELECT * FROM Pets WHERE name LIKE ? LIMIT ? OFFSET ?";
    private final String GET_PETS_FROM_CLIENT = "SELECT p.* FROM Pets p JOIN ClientPets cp ON p.id = cp.pet_id WHERE cp.client_id = ?";
    private final String GET_PET_BY_ID = "SELECT * FROM Pets WHERE id = ?";
    private final String RELATE_PET = "INSERT INTO ClientPets (client_id, pet_id) VALUES (?, ?)";
    private final String EDIT_PET = "UPDATE pets SET name = ?, race = ?, gender = ?, species = ?, weight = ?, born = ?, photo = ? WHERE id = ?";

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
            ps.setDate(6, pet.getBorn());
            ps.setBytes(7, pet.getPhoto());
            return ps;
        }, keyHolder);

        Long petId = keyHolder.getKey().longValue();

        return jdbcTemplate.update(RELATE_PET, clientId, petId);
    }

    @Override
    public Integer getPetCount() {
        return jdbcTemplate.queryForObject(GET_PET_COUNT, Integer.class);
    }

    @Override
    public List<Pet> getAllPets(int limit, int offset) {
        return jdbcTemplate.query(GET_ALL_PETS, new Object[]{limit, offset}, new PetRowMapper());
    }

    @Override
    public List<Pet> getPetsFromClient(Long clientId) {
        Object[] params = {clientId};
        return jdbcTemplate.query(GET_PETS_FROM_CLIENT, params, new PetRowMapper());
    }

    @Override
    public List<Pet> getPetsByName(String name, int limit, int offset) {
        String searchTerm = "%" + name + "%";
        List<Pet> pets = jdbcTemplate.query(GET_PET_BY_NAME, new Object[]{searchTerm, limit, offset}, new PetRowMapper());
        return pets;
    }

    @Override
    public Pet getPetById(Long petId) {
        Object[] params = {petId};
        int[] types = {1};
        return jdbcTemplate.queryForObject(GET_PET_BY_ID, params, types, new PetRowMapper());
    }

    @Override
    public Integer deletePet(Long petId) {
        jdbcTemplate.update(DELETE_PET_CLIENT, petId);
        return jdbcTemplate.update(DELETE_PET, petId);
    }

    @Override
    public Integer editPet(Pet newPet) throws PetNotFoundException, PetNotFoundException {
        Pet editPet = new Pet();

        Long petId = newPet.getId();

        System.out.println(petId);

        Object[] params = {petId};
        int[] types = {1};
        Pet currentPet = jdbcTemplate.queryForObject(GET_PET_BY_ID, params, types, new PetRowMapper());

        if (currentPet == null) {
            throw new PetNotFoundException("Mascota no encontrada!");
        }

        editPet.setName(newPet.getName());
        if (!newPet.getRace().isEmpty()) editPet.setRace(newPet.getRace()); else editPet.setRace(currentPet.getRace());
        if (!newPet.getGender().isEmpty()) editPet.setRace(newPet.getGender()); else editPet.setGender(currentPet.getGender());
        if (!newPet.getSpecies().isEmpty()) editPet.setSpecies(newPet.getSpecies()); else editPet.setSpecies(currentPet.getSpecies());
        if (newPet.getWeight() != 0) editPet.setWeight(newPet.getWeight()); else editPet.setWeight(currentPet.getWeight());
        if (newPet.getBorn() != null) editPet.setBorn(newPet.getBorn()); else editPet.setBorn(currentPet.getBorn());
        if (newPet.getPhoto() != null) editPet.setPhoto(newPet.getPhoto()); else editPet.setPhoto(currentPet.getPhoto());

        return jdbcTemplate.update(EDIT_PET, editPet.getName(), editPet.getRace(), editPet.getGender(), editPet.getSpecies(), editPet.getWeight(), editPet.getBorn(), editPet.getPhoto(), newPet.getId());
    }

    static class PetRowMapper implements RowMapper<Pet> {
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
            pet.setPhoto(rs.getBytes("photo"));
            return pet;
        }
    }
}
