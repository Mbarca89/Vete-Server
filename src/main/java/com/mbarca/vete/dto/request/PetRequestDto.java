package com.mbarca.vete.dto.request;

import java.sql.Blob;

public class PetRequestDto {
    private String name;

    public PetRequestDto(String name) {
        this.name = name;
    }

    public PetRequestDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PetRequestDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
