package com.mbarca.vete.dto.response;

import java.util.Date;

public class ReminderResponseDto {
    private Long id;
    private String name;
    private Date date;
    private String notes;

    public ReminderResponseDto(Long id, String name, Date date, String notes) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.notes = notes;
    }

    public ReminderResponseDto() {
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
}
