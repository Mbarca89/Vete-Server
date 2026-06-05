package com.mbarca.vete.dto.response;

import java.util.Date;

public class VaccineResponseDto {
    private Long id;
    private Long petId;
    private Date date;
    private String name;
    private String notes;
    private boolean sent;
    private String failureReason;

    public VaccineResponseDto() {
    }

    public VaccineResponseDto(Long id, Long petId, Date date, String name, String notes) {
        this.id = id;
        this.petId = petId;
        this.date = date;
        this.name = name;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
