package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Reminder;
import com.mbarca.vete.domain.VaccineNotification;
import com.mbarca.vete.repository.MessagesRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
@Repository
public class MessagesRepositoryImpl implements MessagesRepository {

    private final String SAVE_MESSAGE = "INSERT INTO Messages (client_name, client_phone, pet_name, vaccine, sent) VALUES (?,?,?,?,?)";
    private final String GET_MESSAGES = "SELECT * FROM Messages WHERE date = ?";
    JdbcTemplate jdbcTemplate;

    public MessagesRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer saveMessage(VaccineNotification message) {
        return jdbcTemplate.update(SAVE_MESSAGE,
                message.getClientName(),
                message.getClientPhone(),
                message.getPetName(),
                message.getVaccineName(),
                message.getSent());
    }

    @Override
    public List<VaccineNotification> getMessages(Date date) {
        Object[] params = {date};
        return jdbcTemplate.query(GET_MESSAGES, params, new MessageRowMapper());
    }

    @Override
    public Integer saveReminder(Reminder message) {
        return jdbcTemplate.update(SAVE_MESSAGE,
                "Recordatorio manual",
                message.getPhone(),
                "Recordatorio manual",
                message.getName(),
                message.isSent());
    }

    static class MessageRowMapper implements RowMapper<VaccineNotification> {
        @Override
        public VaccineNotification mapRow(ResultSet rs, int rowNum) throws SQLException {
            VaccineNotification message = new VaccineNotification();
            message.setClientName(rs.getString("client_name"));
            message.setClientPhone(rs.getString("client_phone"));
            message.setPetName(rs.getString("pet_name"));
            message.setVaccineName(rs.getString("vaccine"));
            message.setSent(rs.getBoolean("sent"));
            return message;
        }
    }
}
