package com.mbarca.vete.dto.request;

public class BillRequestDto {
    public String tipo;
    public Long numero;
    public Integer tipoDocumento;
    public Long documento;
    public Double importeTotal;
    public Double importeNoGravado;
    public Double importeGravado;
    public Double importeIva;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Integer getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Integer tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public Double getImporteNoGravado() {
        return importeNoGravado;
    }

    public void setImporteNoGravado(Double importeNoGravado) {
        this.importeNoGravado = importeNoGravado;
    }

    public Double getImporteIva() {
        return importeIva;
    }

    public void setImporteIva(Double importeIva) {
        this.importeIva = importeIva;
    }

    public Double getImporteGravado() {
        return importeGravado;
    }

    public void setImporteGravado(Double importeGravado) {
        this.importeGravado = importeGravado;
    }
}
