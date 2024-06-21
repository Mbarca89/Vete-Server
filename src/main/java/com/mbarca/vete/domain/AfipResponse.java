package com.mbarca.vete.domain;

import java.util.ArrayList;
import java.util.List;

public class AfipResponse {
    private List<AfipResponseObject> errors;
    private List<AfipResponseObject> observations;
    private String cae;
    private String caeFchVto;
    private String status;
    private String message;

    public AfipResponse(List<AfipResponseObject> errors, List<AfipResponseObject> observations, String cae, String caeFchVto, String status) {
        this.errors = errors != null ? errors : new ArrayList<>();
        this.observations = observations != null ? observations : new ArrayList<>();
        this.cae = cae;
        this.caeFchVto = caeFchVto;
        this.status = status;
    }

    public AfipResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public List<AfipResponseObject> getErrors() {
        return errors;
    }

    public void setErrors(List<AfipResponseObject> errors) {
        this.errors = errors;
    }

    public List<AfipResponseObject> getObservations() {
        return observations;
    }

    public void setObservations(List<AfipResponseObject> observations) {
        this.observations = observations;
    }

    public String getCae() {
        return cae;
    }

    public void setCae(String cae) {
        this.cae = cae;
    }

    public String getCaeFchVto() {
        return caeFchVto;
    }

    public void setCaeFchVto(String caeFchVto) {
        this.caeFchVto = caeFchVto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
