package com.mbarca.vete.service;

import com.mbarca.vete.domain.VaccineNotification;
import com.mbarca.vete.dto.response.MessageResponseDto;

import java.util.Date;
import java.util.List;

public interface MessageService {
    Void saveMessage(VaccineNotification message);
    List<MessageResponseDto> getMessages(Date date);
    String forceMessage ();
    String forceReminders ();
}
