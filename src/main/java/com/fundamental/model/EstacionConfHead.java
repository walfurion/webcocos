package com.fundamental.model;

import java.util.List;

/**
 * @author Henry Barrientos
 */
public class EstacionConfHead {

    
    private Integer estacionconfheadId;
    private String nombre;
    private Integer estacionId;
    private String estado, creadoPor, modificadoPor, horaInicio, horaFin;
    //Adicionales
    private List<EstacionConf> estacionConf;
    private String descError;

    public EstacionConfHead(Integer estacionconfheadId, String nombre, Integer estacionId, String estado, String creadoPor, String horaInicio, String horaFin) {
        this.estacionconfheadId = estacionconfheadId;
        this.nombre = nombre;
        this.estacionId = estacionId;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public EstacionConfHead() {
    }

    public Integer getEstacionconfheadId() {
        return estacionconfheadId;
    }

    public void setEstacionconfheadId(Integer estacionconfheadId) {
        this.estacionconfheadId = estacionconfheadId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(Integer estacionId) {
        this.estacionId = estacionId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public List<EstacionConf> getEstacionConf() {
        return estacionConf;
    }

    public void setEstacionConf(List<EstacionConf> estacionConf) {
        this.estacionConf = estacionConf;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

}
