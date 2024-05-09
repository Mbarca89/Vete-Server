package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Reminder;
import com.mbarca.vete.domain.Vaccine;
import com.mbarca.vete.domain.VaccineNotification;
import com.mbarca.vete.repository.ReminderRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Repository
public class ReminderRepositoryImpl implements ReminderRepository {

    private final String CREATE_REMINDER = "INSERT INTO Reminders (name, date, notes) VALUES (?,?,?)";
    private final String DELETE_REMINDER = "DELETE FROM Reminders WHERE id = ?";
    private final String GET_REMINDERS = "SELECT * FROM Reminders WHERE date = ?";
    private final String GET_REMINDER_BY_ID = "SELECT * FROM Reminders WHERE id = ?";
    JdbcTemplate jdbcTemplate;
    public ReminderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createReminder(Reminder reminder) {
        return jdbcTemplate.update(CREATE_REMINDER,
                reminder.getName(),
                reminder.getDate(),
                reminder.getNotes());
    }

    @Override
    public Integer deleteReminder(Long reminderId) {
        return jdbcTemplate.update(DELETE_REMINDER, reminderId);
    }

    @Override
    public List<Reminder> getReminders(Date date) {
        return jdbcTemplate.query(GET_REMINDERS, new Object[]{date}, new ReminderRowMapper());
    }
    @Override
    public Reminder getReminderById(Long reminderId) {
        Object[] params = {reminderId};
        int[] types = {1};
        return jdbcTemplate.queryForObject(GET_REMINDER_BY_ID, params, types, new ReminderRowMapper());
    }

    static class ReminderRowMapper implements RowMapper<Reminder> {
        @Override
        public Reminder mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reminder reminder = new Reminder();
            reminder.setId(rs.getLong("id"));
            reminder.setName(rs.getString("name"));
            reminder.setDate(rs.getDate("date"));
            reminder.setNotes(rs.getString("notes"));
            return reminder;
        }
    }
}
