package com.fundamental.model.dto;

import java.util.Date;

/**
 * @author Henry Barrientos
 */
public class RecepcionDto {

    private Integer invrecepcion_id;
    private Date fecha;
    private Integer pais_id;
    private Integer estacion_id;
    private String piloto;
    private String unidad;
    private String factura;
    private String creado_por;
    private Date creado_el;

    public RecepcionDto(Integer invrecepcion_id, Date fecha, Integer pais_id, Integer estacion_id, String piloto, String unidad, String factura) {
        this.invrecepcion_id = invrecepcion_id;
        this.fecha = fecha;
        this.pais_id = pais_id;
        this.estacion_id = estacion_id;
        this.piloto = piloto;
        this.unidad = unidad;
        this.factura = factura;
    }

    public RecepcionDto() {
    }

    public Integer getInvrecepcion_id() {
        return invrecepcion_id;
    }

    public void setInvrecepcion_id(Integer invrecepcion_id) {
        this.invrecepcion_id = invrecepcion_id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getPais_id() {
        return pais_id;
    }

    public void setPais_id(Integer pais_id) {
        this.pais_id = pais_id;
    }

    public Integer getEstacion_id() {
        return estacion_id;
    }

    public void setEstacion_id(Integer estacion_id) {
        this.estacion_id = estacion_id;
    }

    public String getPiloto() {
        return piloto;
    }

    public void setPiloto(String piloto) {
        this.piloto = piloto;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getCreado_por() {
        return creado_por;
    }

    public void setCreado_por(String creado_por) {
        this.creado_por = creado_por;
    }

    public Date getCreado_el() {
        return creado_el;
    }

    public void setCreado_el(Date creado_el) {
        this.creado_el = creado_el;
    }

}
