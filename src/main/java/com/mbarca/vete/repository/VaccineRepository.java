package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Vaccine;
import com.mbarca.vete.dto.request.VaccineRequestDto;
import com.mbarca.vete.dto.response.VaccineResponseDto;

import java.util.List;

public interface VaccineRepository {
    Integer createVaccine(Vaccine vaccine);
    List<Vaccine> getVaccinesById(Long petId);
    Integer deleteVaccine(Long id);
}
