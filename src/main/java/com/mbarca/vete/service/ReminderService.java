package com.mbarca.vete.service;

import com.mbarca.vete.domain.Reminder;
import com.mbarca.vete.dto.request.ReminderRequestDto;
import com.mbarca.vete.dto.response.ReminderResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

public interface ReminderService {
    String createReminder (ReminderRequestDto reminderRequestDto) throws MissingDataException, NoSuchAlgorithmException;
    String deleteReminder (Long reminderId);
    List<ReminderResponseDto> getReminders (Date date);
    ReminderResponseDto getReminderById (Long reminderId);
}
