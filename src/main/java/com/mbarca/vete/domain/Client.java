package com.mbarca.vete.domain;

public class Client {
    private Long id;
    private String name;
    private Long petId;
    private String petName;

    public Client() {
    }

    public Client(String name, Long petId, String petName) {
        this.name = name;
        this.petId = petId;
        this.petName = petName;
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

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", petId=" + petId +
                ", petName='" + petName + '\'' +
                '}';
    }
}
