package com.mbarca.vete.dto.response;

import java.util.Arrays;

public class PetResponseDto {
    private Long id;
    private String name;
    private byte[] photo;

    public PetResponseDto(Long id, String name, byte[] photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    public PetResponseDto() {
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

    @Override
    public String toString() {
        return "PetResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }
}
