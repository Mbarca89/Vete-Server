package com.mbarca.vete.dto.response;

import com.mbarca.vete.domain.MedicalHistory;
import com.mbarca.vete.domain.Pet;
import com.mbarca.vete.domain.Vaccine;

import java.util.List;

public class PublicPetProfileResponseDto {
    private Pet pet;
    private List<MedicalHistory> medicalHistory;
    private List<Vaccine> vaccine;

    public PublicPetProfileResponseDto() {
    }

    public PublicPetProfileResponseDto(Pet pet, List<MedicalHistory> medicalHistory, List<Vaccine> vaccine) {
        this.pet = pet;
        this.medicalHistory = medicalHistory;
        this.vaccine = vaccine;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public List<MedicalHistory> getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(List<MedicalHistory> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public List<Vaccine> getVaccine() {
        return vaccine;
    }

    public void setVaccine(List<Vaccine> vaccine) {
        this.vaccine = vaccine;
    }
}
