package com.mbarca.vete.domain;

import javax.persistence.Lob;
import java.sql.Date;

public class Pet {
    private Long id;
    private String name;
    private String race;
    private Double weight;
    private Date born;
    @Lob
    private byte[] photo;
    MedicalHistory medicalHistory;

    public Pet() {
    }

    public Pet(Long id, String name, String race, Double weight, Date born, byte[] photo, MedicalHistory medicalHistory) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.weight = weight;
        this.born = born;
        this.photo = photo;
        this.medicalHistory = medicalHistory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(MedicalHistory medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public java.sql.Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }
}
