package com.fundamental.model;

import com.sisintegrados.generic.bean.Pais;
import java.util.Date;

public class TasaCambio {

    private Integer tasacambioId, paisId;
    private Date fechaInicio, fechaFin;
    private Double tasa;
    private String creadoPor, modificadoPor;
    //Adicional
    private String paisNombre;
    private String descError;
    private Pais pais;

//    public TasaCambio(Integer anio, Integer mes, Integer paisId, Double tasa, String creadoPor, String modificadoPor) {
//        this.anio = anio;
//        this.mes = mes;
//        this.paisId = paisId;
//        this.tasa = tasa;
//        this.creadoPor = creadoPor;
//        this.modificadoPor = modificadoPor;
//    }
    public TasaCambio(Integer tasacambioId, int paisId, Date fechaInicio, Date fechaFin, Double tasa, String creadoPor) {
        this.tasacambioId = tasacambioId;
        this.paisId = paisId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tasa = tasa;
        this.creadoPor = creadoPor;
    }

    public TasaCambio() {
    }

    public Integer getTasacambioId() {
        return tasacambioId;
    }

    public void setTasacambioId(Integer tasacambioId) {
        this.tasacambioId = tasacambioId;
    }

    public Integer getPaisId() {
        return paisId;
    }

    public void setPaisId(Integer paisId) {
        this.paisId = paisId;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Double getTasa() {
        return tasa;
    }

    public void setTasa(Double tasa) {
        this.tasa = tasa;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public String getPaisNombre() {
        return paisNombre;
    }

    public void setPaisNombre(String paisNombre) {
        this.paisNombre = paisNombre;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

}
