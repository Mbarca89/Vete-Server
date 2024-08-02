package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Vaccine;
import com.mbarca.vete.domain.VaccineNotification;

import java.util.Date;
import java.util.List;

public interface VaccineRepository {
    Integer createVaccine(Vaccine vaccine);
    List<Vaccine> getVaccinesById(Long petId);
    Integer deleteVaccine(Long id);
    List<VaccineNotification> getTodayVaccines();
    List<Vaccine> getVaccinesByDate(Date date);
    Integer editVaccine (Vaccine vaccine);
    Integer deletePetVaccines(Long petId);
}
