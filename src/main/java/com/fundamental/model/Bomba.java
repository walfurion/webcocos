package com.fundamental.model;

import com.fundamental.model.dto.DtoGenericBean;
import com.vaadin.ui.CheckBox;
import java.util.Date;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class Bomba {

    private Integer id;
    private String nombre, estado, creadoPor;
    private Date creadoEl;
    private String modificadoPor;
    private Date modificadoEl;
    //Adicionales
    private CheckBox checkbox;
    private List<BombaEstacion> bombaEstacion;
    private String tipoDespachoName;
    private Integer tipoDespachoId;
    private Boolean selected = false;
    private int corrPista = 0;

    public Bomba(Integer id, String nombre, String estado, String creadoPor, Date creadoEl, CheckBox checkbox) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.creadoEl = creadoEl;
        this.checkbox = checkbox;
    }
    
    public Bomba() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
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

    public List<BombaEstacion> getBombaEstacion() {
        return bombaEstacion;
    }

    public void setBombaEstacion(List<BombaEstacion> bombaEstacion) {
        this.bombaEstacion = bombaEstacion;
    }

    public String getTipoDespachoName() {
        return tipoDespachoName;
    }

    public void setTipoDespachoName(String tipoDespachoName) {
        this.tipoDespachoName = tipoDespachoName;
    }

    public Integer getTipoDespachoId() {
        return tipoDespachoId;
    }

    public void setTipoDespachoId(Integer tipoDespachoId) {
        this.tipoDespachoId = tipoDespachoId;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public int getCorrPista() {
        return corrPista;
    }

    public void setCorrPista(int corrPista) {
        this.corrPista = corrPista;
    }

}
