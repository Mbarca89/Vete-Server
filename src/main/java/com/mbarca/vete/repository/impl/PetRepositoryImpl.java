package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Pet;
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

    private final String CREATE_PET = "INSERT INTO Pets (name, race, weight, born, photo) VALUES (?,?,?,?,?)";
    private final String GET_PET_COUNT = "SELECT COUNT(*) FROM pets";
    private final String GET_ALL_PETS = "SELECT * FROM Pets LIMIT ? OFFSET ?";
    private final String DELETE_PET = "DELETE FROM Pets WHERE id = ?";
    private final String GET_PET_BY_NAME = "SELECT * FROM Pets WHERE name LIKE ? LIMIT ? OFFSET ?";
    private final String GET_PETS_FROM_CLIENT = "SELECT p.* FROM Pets p JOIN ClientPets cp ON p.id = cp.pet_id WHERE cp.client_id = ?";
    private final String GET_PET_BY_ID = "SELECT * FROM Pets WHERE id = ?";
private final String RELATE_PET = "INSERT INTO ClientPets (client_id, pet_id) VALUES (?, ?)";
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
            ps.setDouble(3, pet.getWeight());
            ps.setDate(4, pet.getBorn());
            ps.setBytes(5, pet.getPhoto());
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
    public List<Pet> getPetsByName (String name, int limit, int offset) {
        String searchTerm = "%" + name + "%";
        List<Pet> pets = jdbcTemplate.query(GET_PET_BY_NAME, new Object[]{searchTerm, limit, offset}, new PetRowMapper());
        return pets;
    }

    @Override
    public Pet getPetById (Long petId) {
        Object[] params = {petId};
        int[] types = {1};
        return jdbcTemplate.queryForObject(GET_PET_BY_ID, params, types, new PetRowMapper());
    }

    @Override
    public Integer deletePet(Long petId) {
        return jdbcTemplate.update(DELETE_PET, petId);
    }

    static class PetRowMapper implements RowMapper<Pet> {
        @Override
        public Pet mapRow(ResultSet rs, int rowNum) throws SQLException {
            Pet pet = new Pet();
            pet.setId(rs.getLong("id"));
            pet.setName(rs.getString("name"));
            pet.setRace(rs.getString("race"));
            pet.setWeight(rs.getDouble("weight"));
            pet.setBorn(rs.getDate("born"));
            pet.setPhoto(rs.getBytes("photo"));
            return pet;
        }
    }
}
