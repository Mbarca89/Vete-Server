package com.mbarca.vete.dto.response;

public class CategoryResponseDto {
    private String name;

    public CategoryResponseDto(String name) {
        this.name = name;
    }

    public CategoryResponseDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryResponseDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
