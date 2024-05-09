package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.User;
import com.mbarca.vete.domain.Vaccine;
import com.mbarca.vete.domain.VaccineNotification;
import com.mbarca.vete.dto.request.VaccineRequestDto;
import com.mbarca.vete.dto.response.VaccineResponseDto;
import com.mbarca.vete.repository.VaccineRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
@Repository
public class VaccineRepositoryImpl implements VaccineRepository {
    private String CREATE_VACCINE = "INSERT INTO Vaccines (name, date, notes, pet_id) VALUES (?,?,?,?)";
    private String GET_VACCINES_BY_PET_ID = "SELECT * FROM Vaccines WHERE pet_id = ?";
    private String DELETE_VACCINE = "DELETE FROM vaccines WHERE id = ?";
    private String GET_TODAY_VACCINES = "SELECT c.name AS client_name, c.phone AS client_phone, " +
            "p.name AS pet_name, v.name AS vaccine_name " +
            "FROM Vaccines v " +
            "JOIN ClientPets cp ON v.pet_id = cp.pet_id " +
            "JOIN Clients c ON cp.client_id = c.id " +
            "JOIN Pets p ON cp.pet_id = p.id " +
            "WHERE v.date = ?";
    JdbcTemplate jdbcTemplate;

    public VaccineRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createVaccine(Vaccine vaccine) {
        return jdbcTemplate.update(CREATE_VACCINE,
                vaccine.getName(),
                vaccine.getDate(),
                vaccine.getNotes(),
                vaccine.getPetId());
    }

    @Override
    public List<Vaccine> getVaccinesById(Long petId) {
        Object[] params = {petId};
        int[] types = {1};
        return jdbcTemplate.query(GET_VACCINES_BY_PET_ID, params, types, new VaccineRowMapper());
    }

    @Override
    public List<VaccineNotification> getTodayVaccines() {
        LocalDate currentDate = LocalDate.now();

        return jdbcTemplate.query(GET_TODAY_VACCINES, new Object[]{currentDate}, (rs, rowNum) ->
                new VaccineNotification(
                        rs.getString("client_name"),
                        rs.getString("client_phone"),
                        rs.getString("pet_name"),
                        rs.getString("vaccine_name")));
    }

    @Override
    public Integer deleteVaccine(Long id) {
        return jdbcTemplate.update(DELETE_VACCINE, id);
    }

    static class VaccineRowMapper implements RowMapper<Vaccine> {
        @Override
        public Vaccine mapRow(ResultSet rs, int rowNum) throws SQLException {
            Vaccine vaccine = new Vaccine();
            vaccine.setId(rs.getLong("id"));
            vaccine.setName(rs.getString("name"));
            vaccine.setDate(rs.getDate("date"));
            vaccine.setNotes(rs.getString("notes"));
            vaccine.setPetId(rs.getLong("pet_id"));
            return vaccine;
        }
    }
}
