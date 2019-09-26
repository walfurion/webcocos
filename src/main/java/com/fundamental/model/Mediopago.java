package com.fundamental.model;

import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.dto.DtoGenericBean;
import java.util.Date;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class Mediopago {

    private Integer mediopagoId, tipo;
    private String nombre;
    private String creadoPor, modificadoPor, estado;
    private Date creadoEl, modificadoEl;
    private int orden, paisId, tipoprodId;
    private Double partidacontPor;
    private boolean partidacont, isTCredito;
    //Adicionales
    private Double value;
//    private Double cantidad;
    private Integer cantidad;
    private List<Pais> paises;
    private String descError, nombrePais;
    private Integer efectivoId;
    private DtoGenericBean status;
    private Pais country;
    
    public Mediopago() {
    }

    public Mediopago(Integer mediopago_id, String nombre, Integer tipo, String creadoPor, int orden, String nombrePais, boolean isTCredito, String estado) {
        this.mediopagoId = mediopago_id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.creadoPor = creadoPor;
        this.orden = orden;
        this.nombrePais = nombrePais;
        this.isTCredito = isTCredito;
        this.estado = estado;
    }

    public Integer getMediopagoId() {
        return mediopagoId;
    }

    public void setMediopagoId(Integer mediopago_id) {
        this.mediopagoId = mediopago_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getCantidad() {
        return cantidad;
    }

//    public Double getCantidad() {
//        return cantidad;
//    }
//
//    public void setCantidad(Double cantidad) {
//        this.cantidad = cantidad;
//    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public List<Pais> getPaises() {
        return paises;
    }

    public void setPaises(List<Pais> paises) {
        this.paises = paises;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getEfectivoId() {
        return efectivoId;
    }

    public void setEfectivoId(Integer efectivoId) {
        this.efectivoId = efectivoId;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getPaisId() {
        return paisId;
    }

    public void setPaisId(int paisId) {
        this.paisId = paisId;
    }

    public int getTipoprodId() {
        return tipoprodId;
    }

    public void setTipoprodId(int tipoprodId) {
        this.tipoprodId = tipoprodId;
    }

    public Double getPartidacontPor() {
        return partidacontPor;
    }

    public void setPartidacontPor(Double partidacontPor) {
        this.partidacontPor = partidacontPor;
    }

    public boolean isPartidacont() {
        return partidacont;
    }

    public void setPartidacont(boolean partidacont) {
        this.partidacont = partidacont;
    }

    public boolean isIsTCredito() {
        return isTCredito;
    }

    public void setIsTCredito(boolean isTCredito) {
        this.isTCredito = isTCredito;
    }

    public DtoGenericBean getStatus() {
        return status;
    }

    public void setStatus(DtoGenericBean status) {
        this.status = status;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Pais getCountry() {
        return country;
    }

    public void setCountry(Pais country) {
        this.country = country;
    }

}