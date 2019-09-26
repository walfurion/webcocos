package com.fundamental.model;

import java.util.Date;

/**
 * @author Henry Barrientos
 */
public class BombaEstacion {

//    private Integer bombaestacionId;
    private Integer bombaId;
    private Integer estacionId;
//    private Integer tipoDespachoId;
//    private String estado;
    private String creadoPor;
    private String modificadoPor;
    private Date modificadoEl;

    public BombaEstacion(//Integer bombaestacionId, 
            Integer bombaId, Integer estacionId, String creadoPor) {
//        this.bombaestacionId = bombaestacionId;
        this.bombaId = bombaId;
        this.estacionId = estacionId;
//        this.tipoDespachoId = tipoDespachoId;
        this.creadoPor = creadoPor;
    }

//    public Integer getBombaestacionId() {
//        return bombaestacionId;
//    }
//
//    public void setBombaestacionId(Integer bombaestacionId) {
//        this.bombaestacionId = bombaestacionId;
//    }

    public Integer getBombaId() {
        return bombaId;
    }

    public void setBombaId(Integer bombaId) {
        this.bombaId = bombaId;
    }

    public Integer getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(Integer estacionId) {
        this.estacionId = estacionId;
    }

//    public Integer getTipoDespachoId() {
//        return tipoDespachoId;
//    }
//
//    public void setTipoDespachoId(Integer tipoDespachoId) {
//        this.tipoDespachoId = tipoDespachoId;
//    }
//
//    public String getEstado() {
//        return estado;
//    }
//
//    public void setEstado(String estado) {
//        this.estado = estado;
//    }

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

    public Date getModificadoEl() {
        return modificadoEl;
    }

    public void setModificadoEl(Date modificadoEl) {
        this.modificadoEl = modificadoEl;
    }

}
