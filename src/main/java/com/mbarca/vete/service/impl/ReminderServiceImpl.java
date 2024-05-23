package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.Reminder;
import com.mbarca.vete.dto.request.ReminderRequestDto;
import com.mbarca.vete.dto.response.ReminderResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.repository.ReminderRepository;
import com.mbarca.vete.service.ReminderService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReminderServiceImpl implements ReminderService {
    ReminderRepository reminderRepository;

    public ReminderServiceImpl(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Override
    public String createReminder(ReminderRequestDto reminderRequestDto) throws MissingDataException, NoSuchAlgorithmException {
        if (reminderRequestDto.getDate() == null ||
                reminderRequestDto.getName() == null ||
                Objects.equals(reminderRequestDto.getName(), "")){
            throw new MissingDataException("Faltan datos!");
        }
        Reminder reminder = mapDtoToReminder(reminderRequestDto);
        Integer response = reminderRepository.createReminder(reminder);
        if (response.equals(0)) {
            return "Error al crear el recordatorio!";
        }
        return "Recordatorio creado correctamente!";
    }

    @Override
    public String deleteReminder(Long reminderId) {
        Integer response = reminderRepository.deleteReminder(reminderId);
        if (response.equals(0)) {
            throw new EmptyResultDataAccessException(1);
        }
        return "Recordatorio eliminado correctamente";
    }

    @Override
    public List<ReminderResponseDto> getReminders(Date date) {
        List<Reminder> reminders = reminderRepository.getReminders(date);
        return reminders.stream().map(this::mapReminderToDto).collect(Collectors.toList());
    }

    @Override
    public ReminderResponseDto getReminderById(Long reminderId) {
        Reminder reminder = reminderRepository.getReminderById(reminderId);
        return mapReminderToDto(reminder);
    }

    private Reminder mapDtoToReminder(ReminderRequestDto reminderRequestDto) throws NoSuchAlgorithmException {
        Reminder reminder = new Reminder();
        if (reminderRequestDto.getId() != null) reminder.setId(reminderRequestDto.getId());
        reminder.setName(reminderRequestDto.getName());
        reminder.setDate(reminderRequestDto.getDate());
        reminder.setNotes(reminderRequestDto.getNotes());
        reminder.setPhone(reminderRequestDto.getPhone());
        return reminder;
    }

    private ReminderResponseDto mapReminderToDto(Reminder reminder) {
        ReminderResponseDto reminderResponseDto = new ReminderResponseDto();
        reminderResponseDto.setId(reminder.getId());
        reminderResponseDto.setName(reminder.getName());
        reminderResponseDto.setDate(reminder.getDate());
        reminderResponseDto.setNotes(reminder.getNotes());
        return reminderResponseDto;
    }
}
