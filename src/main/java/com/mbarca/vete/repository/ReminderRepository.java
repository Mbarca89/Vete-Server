package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Reminder;

import java.util.Date;
import java.util.List;

public interface ReminderRepository {
    Integer createReminder (Reminder reminder);
    Integer deleteReminder (Long reminderId);
    List<Reminder> getReminders (Date date);
    Reminder getReminderById (Long reminderId);
    List<Reminder> getTodayReminder ();
}
