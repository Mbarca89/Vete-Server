package com.mbarca.vete.domain;

public class VaccineNotification {
    private String clientName;
    private String clientPhone;
    private String petName;
    private String vaccineName;

    public VaccineNotification() {
    }

    public VaccineNotification(String clientName, String clientPhone, String petName, String vaccineName) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.petName = petName;
        this.vaccineName = vaccineName;
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
}
