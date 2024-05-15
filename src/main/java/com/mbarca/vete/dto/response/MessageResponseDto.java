package com.mbarca.vete.dto.response;

public class MessageResponseDto {
    private String clientName;
    private String clientPhone;
    private String petName;
    private String vaccineName;
    private Boolean sent;

    public MessageResponseDto() {
    }

    public MessageResponseDto(String clientName, String clientPhone, String petName, String vaccineName, Boolean sent) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.petName = petName;
        this.vaccineName = vaccineName;
        this.sent = sent;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }
}
