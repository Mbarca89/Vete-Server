package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.MedicalHistoryRequestDto;
import com.mbarca.vete.dto.response.MedicalHistoryResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;

import java.util.List;

public interface MedicalHistoryService {
    String createMedicalHistory(MedicalHistoryRequestDto medicalHistoryRequestDto, String filePath) throws MissingDataException;
    List<MedicalHistoryResponseDto> getMedicalHistoryForPet (Long petId);
    MedicalHistoryResponseDto getMedicalHistoryById (Long medicalHistoryId);
    String deleteMedicalHistory(Long medicalHistoryId);
}
