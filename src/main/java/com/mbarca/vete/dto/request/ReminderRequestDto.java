package com.mbarca.vete.dto.request;

import java.util.Date;

public class ReminderRequestDto {
    private Long id;
    private String name;
    private Date date;
    private String notes;
    private String phone;

    public ReminderRequestDto() {
    }

    public ReminderRequestDto(Long id, String name, Date date, String notes) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.notes = notes;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ReminderRequestDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", notes='" + notes + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
