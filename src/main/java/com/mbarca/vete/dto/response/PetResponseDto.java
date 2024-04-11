package com.mbarca.vete.dto.response;

import java.util.Arrays;
import java.sql.Date;

public class PetResponseDto {
    private Long id;
    private String name;
    private String race;
    private Double weight;
    private Date born;
    private byte[] photo;

    public PetResponseDto() {
    }

    public PetResponseDto(Long id, String name, String race, Double weight, Date born, byte[] photo) {
        this.id = id;
        this.name = name;
        this.race = race;
        this.weight = weight;
        this.born = born;
        this.photo = photo;
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

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    @Override
    public String toString() {
        return "PetResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }

}
