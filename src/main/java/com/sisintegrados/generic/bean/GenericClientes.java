/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import com.vaadin.addon.excel.ExcelColumn;

/**
 *
 * @author jjosue
 */
public class GenericClientes {
    
    @ExcelColumn("Pais")
    private String pais;
    @ExcelColumn("Estacion")
    private String estacion;
    @ExcelColumn("Tipo")
    private String tipo;
    @ExcelColumn("Código E1")
    private String codigo_e1;
    @ExcelColumn("Código envoy")
    private String codigoEnvoy;
    @ExcelColumn("Estado")
    private String estado;
    @ExcelColumn("Nombre")
    private String nombre;

    public GenericClientes() {
    }

    public GenericClientes(String pais, String estacion, String tipo, String codigo_e1, String codigoEnvoy, String estado, String nombre) {
        this.pais = pais;
        this.estacion = estacion;
        this.tipo = tipo;
        this.codigo_e1 = codigo_e1;
        this.codigoEnvoy = codigoEnvoy;
        this.estado = estado;
        this.nombre = nombre;
    }
    
    

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigo_e1() {
        return codigo_e1;
    }

    public void setCodigo_e1(String codigo_e1) {
        this.codigo_e1 = codigo_e1;
    }

    public String getCodigoEnvoy() {
        return codigoEnvoy;
    }

    public void setCodigoEnvoy(String codigoEnvoy) {
        this.codigoEnvoy = codigoEnvoy;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    
}
