package com.mbarca.vete.dto.response;

import java.util.Date;

public class MedicalHistoryResponseDto {
    private Long id;
    private Date date;
    private String type;
    private String notes;
    private String medicine;

    public MedicalHistoryResponseDto() {
    }

    public MedicalHistoryResponseDto(Long id, Date date, String type, String notes, String medicine) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.notes = notes;
        this.medicine = medicine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
