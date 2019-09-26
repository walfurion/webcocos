package com.fundamental.model;

/**
 * @author Henry Barrientos
 */
public class BombaEmpleadoEstacion {

    //required
    private int estacionId, empleadoId, bombaId;
    private String creadoPor;
    //optional
    private String estado, creadoEl, modificadoPor, modificadoEl;

    public BombaEmpleadoEstacion(int estacionId, int empleadoId, int bombaId, String creadoPor) {
        this.estacionId = estacionId;
        this.empleadoId = empleadoId;
        this.bombaId = bombaId;
        this.creadoPor = creadoPor;
    }

    public int getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(int estacionId) {
        this.estacionId = estacionId;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public int getBombaId() {
        return bombaId;
    }

    public void setBombaId(int bombaId) {
        this.bombaId = bombaId;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCreadoEl() {
        return creadoEl;
    }

    public void setCreadoEl(String creadoEl) {
        this.creadoEl = creadoEl;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public String getModificadoEl() {
        return modificadoEl;
    }

    public void setModificadoEl(String modificadoEl) {
        this.modificadoEl = modificadoEl;
    }

}
