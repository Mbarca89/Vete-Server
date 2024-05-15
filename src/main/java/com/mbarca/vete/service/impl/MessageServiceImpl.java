package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.VaccineNotification;
import com.mbarca.vete.dto.response.MessageResponseDto;
import com.mbarca.vete.repository.MessagesRepository;
import com.mbarca.vete.service.MessageService;
import com.mbarca.vete.service.NotificationsScheduler;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    MessagesRepository messagesRepository;
    NotificationsScheduler notificationsScheduler;

    public MessageServiceImpl(MessagesRepository messagesRepository, NotificationsScheduler notificationsScheduler) {
        this.messagesRepository = messagesRepository;
        this.notificationsScheduler = notificationsScheduler;
    }


    @Override
    public Void saveMessage(VaccineNotification message) {
        Integer response = messagesRepository.saveMessage(message);
        if (response.equals(0)) {
            throw new EmptyResultDataAccessException(1);
        }
        return null;
    }

    @Override
    public List<MessageResponseDto> getMessages(Date date) {
        List<VaccineNotification> messages = messagesRepository.getMessages(date);
        return messages.stream().map(this::mapMessageToDto).collect(Collectors.toList());
    }

    @Override
    public String forceMessage() {
        try {
        notificationsScheduler.checkVaccineRecords();
        return "Mensajes enviados!";
        } catch (Exception e) {
           return "Error al enviar mensajes: " + e.getMessage() ;
        }
    }

    private MessageResponseDto mapMessageToDto(VaccineNotification message) {
        MessageResponseDto messageResponseDto = new MessageResponseDto();
        messageResponseDto.setClientName(message.getClientName());
        messageResponseDto.setClientPhone(message.getClientPhone());
        messageResponseDto.setPetName(message.getPetName());
        messageResponseDto.setVaccineName(message.getVaccineName());
        messageResponseDto.setSent(message.getSent());
        return messageResponseDto;
    }
}
