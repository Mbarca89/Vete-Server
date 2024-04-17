package com.mbarca.vete.dto.request;

import java.util.Date;

public class MedicalHistoryRequestDto {
    private Date date;
    private String type;
    private String notes;
    private String description;
    private String medicine;

    public MedicalHistoryRequestDto() {
    }

    public MedicalHistoryRequestDto(Date date, String type, String notes, String description, String medicine) {
        this.date = date;
        this.type = type;
        this.notes = notes;
        this.description = description;
        this.medicine = medicine;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
