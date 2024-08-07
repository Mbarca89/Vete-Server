package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.Vaccine;
import com.mbarca.vete.dto.request.VaccineRequestDto;
import com.mbarca.vete.dto.response.ReminderResponseDto;
import com.mbarca.vete.dto.response.VaccineResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.repository.VaccineRepository;
import com.mbarca.vete.service.VaccineService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VaccineServiceImpl implements VaccineService {
    VaccineRepository vaccineRepository;

    public VaccineServiceImpl(VaccineRepository vaccineRepository) {
        this.vaccineRepository = vaccineRepository;
    }

    @Override
    public String createVaccine(VaccineRequestDto vaccineRequestDto) throws NoSuchAlgorithmException, MissingDataException {
        if (vaccineRequestDto.getDate() == null ||
                vaccineRequestDto.getName() == null ||
                Objects.equals(vaccineRequestDto.getName(), "")) {
            throw new MissingDataException("Faltan datos!");
        }

        Vaccine vaccine = mapDtoToVaccine(vaccineRequestDto);
        Integer response = vaccineRepository.createVaccine(vaccine);
        if (response.equals(0)) {
            return "Error al crear el evento!";
        } else {
            return "Evento creado correctamente!";
        }
    }

    @Override
    public String deleteVaccine(Long id) {
        Integer response = vaccineRepository.deleteVaccine(id);
        if (response.equals(0)) {
            throw new EmptyResultDataAccessException(1);
        }
        return "Evento eliminado correctamente";
    }

    @Override
    public List<VaccineResponseDto> getVaccinesById(Long petId) {
        List<Vaccine> vaccines = vaccineRepository.getVaccinesById(petId);
        return vaccines.stream().map(this::mapVaccineToDto).collect(Collectors.toList());
    }

    @Override
    public List<ReminderResponseDto> getVaccinesByDate(Date date) {
        List<Vaccine> vaccines = vaccineRepository.getVaccinesByDate(date);
        return vaccines.stream().map(this::mapVaccineNotificationToReminderDto).collect(Collectors.toList());
    }

    @Override
    public String editVaccine(VaccineRequestDto vaccineRequestDto) throws NoSuchAlgorithmException {
        Integer res = vaccineRepository.editVaccine(mapDtoToVaccine(vaccineRequestDto));
        if (res.equals(0)) {
            return "Error al editar el evento!";
        } else {
            return "Evento editado correctamente!";
        }
    }

    @Override
    public void deletePetVaccines(Long petId) {
        Integer response = vaccineRepository.deletePetVaccines(petId);
        if (response.equals(0)) {
            throw new EmptyResultDataAccessException(1);
        }
    }

    private Vaccine mapDtoToVaccine(VaccineRequestDto vaccineRequestDto) throws NoSuchAlgorithmException {
        Vaccine vaccine = new Vaccine();
        if (vaccineRequestDto.getId() != null) vaccine.setId(vaccineRequestDto.getId());
        vaccine.setName(vaccineRequestDto.getName());
        vaccine.setDate(vaccineRequestDto.getDate());
        vaccine.setNotes(vaccineRequestDto.getNotes());
        vaccine.setPetId(vaccineRequestDto.getPetId());
        return vaccine;
    }

    private VaccineResponseDto mapVaccineToDto(Vaccine vaccine) {
        VaccineResponseDto vaccineResponseDto = new VaccineResponseDto();
        vaccineResponseDto.setId(vaccine.getId());
        vaccineResponseDto.setName(vaccine.getName());
        vaccineResponseDto.setDate(vaccine.getDate());
        vaccineResponseDto.setNotes(vaccine.getNotes());
        vaccineResponseDto.setPetId(vaccine.getPetId());
        return vaccineResponseDto;
    }

    private ReminderResponseDto mapVaccineNotificationToReminderDto(Vaccine vaccine) {
        ReminderResponseDto response = new ReminderResponseDto();
        response.setName(vaccine.getName());
        response.setDate(vaccine.getDate());
        response.setId(vaccine.getId());
        response.setPetId(vaccine.getPetId());
        return response;
    }

}
