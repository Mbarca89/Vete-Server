package com.mbarca.vete.dto.response;

public class CategoryResponseDto {
    private Long id;
    private String name;

    public CategoryResponseDto(String name, Long id) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "CategoryResponseDto{" +
                "name='" + name + '\'' +
                '}';
    }

}
