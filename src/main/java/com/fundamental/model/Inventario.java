package com.fundamental.model;

import java.util.Date;

/**
 * @author Henry Barrientos
 */
public class Inventario {

    private Date fecha;
    private Integer estacionId, productoId;
    private Double inicial, finall, compras;
    private String creadoPor, creadoPersona, modificadoPor, modificadoPersona;
    //
        private Integer inventarioFisico, lecturaVeederRoot, volFacturado, galonesCisterna;
            private String compartimiento, pulgadas;


    public Inventario() {
    }

    public Inventario(Date fecha, Integer estacionId, Integer productoId, Double inicial, Double finall, Double compras, String creadoPor, String creadoPersona) {
        this.fecha = fecha;
        this.estacionId = estacionId;
        this.productoId = productoId;
        this.inicial = inicial;
        this.finall = finall;
        this.compras = compras;
        this.creadoPor = creadoPor;
        this.creadoPersona = creadoPersona;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(Integer estacionId) {
        this.estacionId = estacionId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Double getInicial() {
        return inicial;
    }

    public void setInicial(Double inicial) {
        this.inicial = inicial;
    }

    public Double getFinall() {
        return finall;
    }

    public void setFinall(Double finall) {
        this.finall = finall;
    }

    public Double getCompras() {
        return compras;
    }

    public void setCompras(Double compras) {
        this.compras = compras;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
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

    public String getModificadoPersona() {
        return modificadoPersona;
    }

    public void setModificadoPersona(String modificadoPersona) {
        this.modificadoPersona = modificadoPersona;
    }

    public Integer getInventarioFisico() {
        return inventarioFisico;
    }

    public void setInventarioFisico(Integer inventarioFisico) {
        this.inventarioFisico = inventarioFisico;
    }

    public Integer getLecturaVeederRoot() {
        return lecturaVeederRoot;
    }

    public void setLecturaVeederRoot(Integer lecturaVeederRoot) {
        this.lecturaVeederRoot = lecturaVeederRoot;
    }

    public Integer getVolFacturado() {
        return volFacturado;
    }

    public void setVolFacturado(Integer volFacturado) {
        this.volFacturado = volFacturado;
    }

    public Integer getGalonesCisterna() {
        return galonesCisterna;
    }

    public void setGalonesCisterna(Integer galonesCisterna) {
        this.galonesCisterna = galonesCisterna;
    }

    public String getCompartimiento() {
        return compartimiento;
    }

    public void setCompartimiento(String compartimiento) {
        this.compartimiento = compartimiento;
    }

    public String getPulgadas() {
        return pulgadas;
    }

    public void setPulgadas(String pulgadas) {
        this.pulgadas = pulgadas;
    }

}
