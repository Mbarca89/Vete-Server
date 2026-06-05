package com.mbarca.vete.domain;

import java.util.Date;

public class Vaccine {
    private Long id;
    private String name;
    private Date date;
    private String notes;
    private Long petId;
    private boolean sent;
    private String failureReason;

    public Vaccine() {
    }

    public Vaccine(Long id, String name, Date date, String notes, Long petId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.notes = notes;
        this.petId = petId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
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
