package com.fundamental.model;

import java.util.Date;

/**
 * @author Henry Barrientos
 */
public class Precio {

//    private Integer precioId;
    private Integer turnoId;
    private Integer productoId;
    private Integer tipodespachoId;
    private Double precio;
    private String creadoPor;
    private Date creadoEl;
    private String creadoPersona;
    private String modificadoPor;
    private Date modificadoEl;
    private String modificadoPersona;
    private String descError;

    public Precio(Integer turnoId, Integer productoId, Integer tipodespachoId, Double precio, String creadoPor, String creadoPersona) {
        this.turnoId = turnoId;
        this.productoId = productoId;
        this.tipodespachoId = tipodespachoId;
        this.precio = precio;
        this.creadoPor = creadoPor;
        this.creadoPersona = creadoPersona;
    }
    
    public Precio() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    public Integer getPrecioId() {
//        return precioId;
//    }
//
//    public void setPrecioId(Integer precioId) {
//        this.precioId = precioId;
//    }

    public Integer getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(Integer turnoId) {
        this.turnoId = turnoId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getTipodespachoId() {
        return tipodespachoId;
    }

    public void setTipodespachoId(Integer tipodespachoId) {
        this.tipodespachoId = tipodespachoId;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Date getCreadoEl() {
        return creadoEl;
    }

    public void setCreadoEl(Date creadoEl) {
        this.creadoEl = creadoEl;
    }

    public String getCreadoPersona() {
        return creadoPersona;
    }

    public void setCreadoPersona(String creadoPersona) {
        this.creadoPersona = creadoPersona;
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

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public String getModificadoPersona() {
        return modificadoPersona;
    }

    public void setModificadoPersona(String modificadoPersona) {
        this.modificadoPersona = modificadoPersona;
    }

}
