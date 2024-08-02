package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.VaccineRequestDto;
import com.mbarca.vete.dto.response.ReminderResponseDto;
import com.mbarca.vete.dto.response.VaccineResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

public interface VaccineService {
    String createVaccine (VaccineRequestDto vaccineRequestDto) throws NoSuchAlgorithmException, MissingDataException;
    String deleteVaccine (Long id);
    List<VaccineResponseDto> getVaccinesById (Long petId);
    List<ReminderResponseDto> getVaccinesByDate (Date date);
    String editVaccine (VaccineRequestDto vaccineRequestDto) throws NoSuchAlgorithmException;
    void deletePetVaccines (Long petId);
}
