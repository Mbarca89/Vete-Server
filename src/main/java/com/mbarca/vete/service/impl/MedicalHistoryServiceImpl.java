package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.MedicalHistory;
import com.mbarca.vete.dto.request.MedicalHistoryRequestDto;
import com.mbarca.vete.dto.response.MedicalHistoryResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.repository.MedicalHistoryRepository;
import com.mbarca.vete.service.MedicalHistoryService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Date;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {
    MedicalHistoryRepository medicalHistoryRepository;

    public MedicalHistoryServiceImpl(MedicalHistoryRepository medicalHistoryRepository) {
        this.medicalHistoryRepository = medicalHistoryRepository;
    }

    @Override
    public String createMedicalHistory(MedicalHistoryRequestDto medicalHistoryRequestDto) throws MissingDataException {
        if (medicalHistoryRequestDto.getType() == null ||
                Objects.equals(medicalHistoryRequestDto.getType(), "")
        ) {
            throw new MissingDataException("Faltan datos!");
        }

        MedicalHistory medicalHistory = mapDtoToMedicalHistory(medicalHistoryRequestDto);
        Integer response = medicalHistoryRepository.createMedicalHistory(medicalHistory);
        if (response.equals(0)) {
            return "Error al crear la Historia clínica!";
        }
        return "Historia clínica creada correctamente!";
    }

    @Override
    public List<MedicalHistoryResponseDto> getMedicalHistoryForPet(Long petId) {
        List<MedicalHistory> medicalHistories = medicalHistoryRepository.getMedicalHistoryForPet(petId);
        return medicalHistories.stream().map(this::mapMedicalHistoryToDto).collect(Collectors.toList());
    }

    @Override
    public MedicalHistoryResponseDto getMedicalHistoryById(Long medicalHistoryId) {
        return mapMedicalHistoryToDto(medicalHistoryRepository.getMedicalHistoryById(medicalHistoryId));
    }

    @Override
    public String deleteMedicalHistory(Long medicalHistoryId) {
        Integer response = medicalHistoryRepository.deleteMedicalHistory(medicalHistoryId);
        if (response.equals(0)) {
            throw new EmptyResultDataAccessException(1);
        }
        return "Historia clínica eliminada correctamente";
    }

    private MedicalHistory mapDtoToMedicalHistory (MedicalHistoryRequestDto medicalHistoryRequestDto) {
        LocalDate currentDate = LocalDate.now();
        Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setDate(date);
        medicalHistory.setType(medicalHistoryRequestDto.getType());
        medicalHistory.setNotes(medicalHistoryRequestDto.getNotes());
        medicalHistory.setDescription(medicalHistoryRequestDto.getDescription());
        medicalHistory.setMedicine(medicalHistoryRequestDto.getMedicine());
        medicalHistory.setPetId(medicalHistoryRequestDto.getPetId());
        return medicalHistory;
    }

    private MedicalHistoryResponseDto mapMedicalHistoryToDto (MedicalHistory medicalHistory) {
        MedicalHistoryResponseDto medicalHistoryResponseDto = new MedicalHistoryResponseDto();
        medicalHistoryResponseDto.setId(medicalHistory.getId());
        medicalHistoryResponseDto.setDate(medicalHistory.getDate());
        medicalHistoryResponseDto.setType(medicalHistory.getType());
        medicalHistoryResponseDto.setNotes(medicalHistory.getNotes());
        medicalHistoryResponseDto.setDescription(medicalHistory.getDescription());
        medicalHistoryResponseDto.setMedicine(medicalHistory.getMedicine());
        return medicalHistoryResponseDto;
    }
}
