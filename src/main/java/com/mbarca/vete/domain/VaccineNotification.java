package com.mbarca.vete.domain;

public class VaccineNotification {
    private Long vaccineId;
    private String clientName;
    private String clientPhone;
    private String petName;
    private String vaccineName;
    private Boolean sent;
    private String failureReason;

    public VaccineNotification() {
    }

    public VaccineNotification(Long vaccineId, String clientName, String clientPhone, String petName, String vaccineName, Boolean sent, String failureReason) {
        this.vaccineId = vaccineId;
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.petName = petName;
        this.vaccineName = vaccineName;
        this.sent = sent;
        this.failureReason = failureReason;
    }

    public Long getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(Long vaccineId) {
        this.vaccineId = vaccineId;
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

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @Override
    public String toString() {
        return "VaccineNotification{" +
                "vaccineId=" + vaccineId +
                ", clientName='" + clientName + '\'' +
                ", clientPhone='" + clientPhone + '\'' +
                ", petName='" + petName + '\'' +
                ", vaccineName='" + vaccineName + '\'' +
                ", sent=" + sent +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }

}
