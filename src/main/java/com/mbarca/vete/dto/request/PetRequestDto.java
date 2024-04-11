package com.mbarca.vete.dto.request;

import java.sql.Date;

public class PetRequestDto {
    private String name;
    private String race;
    private Double weight;
    private Date born;

    public PetRequestDto() {
    }

    public PetRequestDto(String name, String race, Double weight, Date born) {
        this.name = name;
        this.race = race;
        this.weight = weight;
        this.born = born;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "PetRequestDto{" +
                "name='" + name + '\'' +
                '}';
    }

}
