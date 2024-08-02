package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.MedicalHistory;
import com.mbarca.vete.domain.Pet;
import com.mbarca.vete.repository.MedicalHistoryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MedicalHistoryRepositoryImpl implements MedicalHistoryRepository {

    private final String CREATE_MEDICAL_HISTORY = "INSERT INTO medical_history (`date`, `type`, notes, description, medicine, pet_id) VALUES (?,?,?,?,?,?)";
    private final String GET_MEDICAL_HISTORY_FOR_PET = "SELECT * FROM medical_history WHERE pet_id = ?";
    private final String GET_MEDICAL_HISTORY_BY_ID = "SELECT * FROM medical_history WHERE id = ?";
    private final String DELETE_MEDICAL_HISTORY = "DELETE FROM medical_history WHERE pet_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public MedicalHistoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createMedicalHistory(MedicalHistory medicalHistory) {
        return jdbcTemplate.update(CREATE_MEDICAL_HISTORY,
                medicalHistory.getDate(),
                medicalHistory.getType(),
                medicalHistory.getNotes(),
                medicalHistory.getDescription(),
                medicalHistory.getMedicine(),
                medicalHistory.getPetId());
    }

    @Override
    public List<MedicalHistory> getMedicalHistoryForPet(Long petId) {
        Object[] params = {petId};
        int[] types = {1};
        return jdbcTemplate.query(GET_MEDICAL_HISTORY_FOR_PET, params, types, new MedicalHistoryRowMapper());
    }

    @Override
    public MedicalHistory getMedicalHistoryById(Long medicalHistoryId) {
        Object[] params = {medicalHistoryId};
        int[] types = {1};
        return jdbcTemplate.queryForObject(GET_MEDICAL_HISTORY_BY_ID, params, types, new MedicalHistoryRowMapper());
    }

    @Override
    public Integer deleteMedicalHistory(Long petId) {
        return jdbcTemplate.update(DELETE_MEDICAL_HISTORY, petId);
    }

    static class MedicalHistoryRowMapper implements RowMapper<MedicalHistory> {
        @Override
        public MedicalHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
            MedicalHistory medicalHistory = new MedicalHistory();
            medicalHistory.setId(rs.getLong("id"));
            medicalHistory.setDate(rs.getDate("date"));
            medicalHistory.setType(rs.getString("type"));
            medicalHistory.setNotes(rs.getString("notes"));
            medicalHistory.setDescription(rs.getString("description"));
            medicalHistory.setMedicine(rs.getString("medicine"));
            return medicalHistory;
        }
    }
}
