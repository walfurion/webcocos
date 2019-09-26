/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import com.vaadin.server.ThemeResource;
import java.util.Date;

/**
 *
 * @author walfu
 */
public class PaisBK {

    private Integer paisId;
    private String nombre;
    private String codigo;
    private String monedaSimbolo;
    private String volSimbolo;
    private String estado;
    private String creadoPor;
    private Date creadoEl;
    private String modificadoPor;
    private Date modificadoEl;
    private boolean selected = false;
    private ThemeResource flag;
    
        public PaisBK(Integer paisId, String nombre, String codigo, String monedaSimbolo, String volSimbolo, String estado, String creadoPor) {
        this.paisId = paisId;
        this.nombre = nombre;
        this.codigo = codigo;
        this.monedaSimbolo = monedaSimbolo;
        this.volSimbolo = volSimbolo;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.flag = new ThemeResource("img/"+codigo.toLowerCase()+".gif");
    }
    
    public PaisBK(Integer paisId, String nombre, String codigo, String monedaSimbolo, String volSimbolo, String estado, String creadoPor,boolean selected) {
        this.paisId = paisId;
        this.nombre = nombre;
        this.codigo = codigo;
        this.monedaSimbolo = monedaSimbolo;
        this.volSimbolo = volSimbolo;
        this.estado = estado;
        this.creadoPor = creadoPor;
        //Images from https://demo.vaadin.com/sampler-for-vaadin6/VAADIN/themes/sampler/flags/gt.gif
        this.flag = new ThemeResource("img/"+codigo.toLowerCase()+".gif");
        this.selected = selected;
    }

    public PaisBK() {
    }

    public Integer getPaisId() {
        return paisId;
    }

    public void setPaisId(Integer paisId) {
        this.paisId = paisId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getMonedaSimbolo() {
        return monedaSimbolo;
    }

    public void setMonedaSimbolo(String monedaSimbolo) {
        this.monedaSimbolo = monedaSimbolo;
    }

    public ThemeResource getFlag() {
        return flag;
    }

    public void setFlag(ThemeResource flag) {
        this.flag = flag;
    }

    public String getVolSimbolo() {
        return volSimbolo;
    }

    public void setVolSimbolo(String volSimbolo) {
        this.volSimbolo = volSimbolo;
    }
}
