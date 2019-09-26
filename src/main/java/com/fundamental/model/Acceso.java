package com.fundamental.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Henry Barrientos
 */
public class Acceso {

    private Integer accesoId;
    private String titulo;
    private Integer padre;
    private Integer orden;
    private String recursoInterno, descripcion, estado, creadoPor, modificadoPor, nombrePadre;
    //Adicionales
    private List<Acceso> accesos;
    private boolean selected;

    public Acceso(Integer accesoId, String titulo, Integer padre, Integer orden, String recursoInterno, String descripcion, String estado) {
        this.accesoId = accesoId;
        this.titulo = titulo;
        this.padre = padre;
        this.orden = orden;
        this.recursoInterno = recursoInterno;
        this.descripcion = descripcion;
        this.estado = estado;
        this.accesos = new ArrayList();
    }

    public Integer getAccesoId() {
        return accesoId;
    }

    public void setAccesoId(Integer accesoId) {
        this.accesoId = accesoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getPadre() {
        return padre;
    }

    public void setPadre(Integer padre) {
        this.padre = padre;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getRecursoInterno() {
        return recursoInterno;
    }

    public void setRecursoInterno(String recursoInterno) {
        this.recursoInterno = recursoInterno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public List<Acceso> getAccesos() {
        return accesos;
    }

    public void setAccesos(List<Acceso> accesos) {
        this.accesos = accesos;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getNombrePadre() {
        return nombrePadre;
    }

    public void setNombrePadre(String nombrePadre) {
        this.nombrePadre = nombrePadre;
    }
    
}
