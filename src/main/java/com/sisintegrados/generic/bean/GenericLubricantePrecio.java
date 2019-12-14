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
public class GenericLubricantePrecio {
    
    @ExcelColumn("Pais")
    private String PAIS;
    @ExcelColumn("Marca")
    private String MARCA;
    @ExcelColumn("Producto")
    private String PRODUCTO;
    @ExcelColumn("Inicio")
    private String INICIO;
    @ExcelColumn("Fin")
    private String FIN;
    @ExcelColumn("Precio")
    private String PRECIO;
    @ExcelColumn("Historial")
    private String HISTORIAL;

    public GenericLubricantePrecio() {
    }

    public GenericLubricantePrecio(String PAIS, String MARCA, String PRODUCTO, String INICIO, String FIN, String PRECIO, String HISTORIAL) {
        this.PAIS = PAIS;
        this.MARCA = MARCA;
        this.PRODUCTO = PRODUCTO;
        this.INICIO = INICIO;
        this.FIN = FIN;
        this.PRECIO = PRECIO;
        this.HISTORIAL = HISTORIAL;
    }

    public String getPAIS() {
        return PAIS;
    }

    public void setPAIS(String PAIS) {
        this.PAIS = PAIS;
    }

    public String getMARCA() {
        return MARCA;
    }

    public void setMARCA(String MARCA) {
        this.MARCA = MARCA;
    }

    public String getPRODUCTO() {
        return PRODUCTO;
    }

    public void setPRODUCTO(String PRODUCTO) {
        this.PRODUCTO = PRODUCTO;
    }

    public String getINICIO() {
        return INICIO;
    }

    public void setINICIO(String INICIO) {
        this.INICIO = INICIO;
    }

    public String getFIN() {
        return FIN;
    }

    public void setFIN(String FIN) {
        this.FIN = FIN;
    }

    public String getPRECIO() {
        return PRECIO;
    }

    public void setPRECIO(String PRECIO) {
        this.PRECIO = PRECIO;
    }

    public String getHISTORIAL() {
        return HISTORIAL;
    }

    public void setHISTORIAL(String HISTORIAL) {
        this.HISTORIAL = HISTORIAL;
    }
    
    
    
}
