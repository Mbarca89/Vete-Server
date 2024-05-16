package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Reminder;
import com.mbarca.vete.domain.VaccineNotification;

import java.util.Date;
import java.util.List;

public interface MessagesRepository {
    Integer saveMessage(VaccineNotification message);
    Integer saveReminder(Reminder message);
    List<VaccineNotification> getMessages(Date date);
}
