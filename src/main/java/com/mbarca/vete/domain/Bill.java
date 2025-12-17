package com.mbarca.vete.domain;

import java.util.Date;
import java.util.List;

public class Bill {
    Long id;
    Date fecha;
    String tipo;
    Long numero;
    Integer tipoDocumento;
    Long documento;


    String nombre;
    Double importeTotal;
    Double importeNoGravado;
    Double importeGravado;
    Double importeIva;
    String cae;
    String caeFchVto;
    String estado;
    List<AfipResponseObject> errors;
    List<AfipResponseObject> observations;
    List<BillProduct> billProducts;
    String condicionIvaDescripcion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCondicionIvaDescripcion() {
        return condicionIvaDescripcion;
    }

    public void setCondicionIvaDescripcion(String condicionIvaDescripcion) {
        this.condicionIvaDescripcion = condicionIvaDescripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public Double getImporteNoGravado() {
        return importeNoGravado;
    }

    public void setImporteNoGravado(Double importeNoGravado) {
        this.importeNoGravado = importeNoGravado;
    }

    public Double getImporteGravado() {
        return importeGravado;
    }

    public void setImporteGravado(Double importeGravado) {
        this.importeGravado = importeGravado;
    }

    public Double getImporteIva() {
        return importeIva;
    }

    public void setImporteIva(Double importeIva) {
        this.importeIva = importeIva;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public List<BillProduct> getBillProducts() {
        return billProducts;
    }

    public void setBillProducts(List<BillProduct> billProducts) {
        this.billProducts = billProducts;
    }
}
