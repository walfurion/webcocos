package com.fundamental.model;

import com.fundamental.model.dto.DtoGenericBean;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class Rol {

    private Integer rolId;
    private String nombre, descripcion;
    private Integer rolpadreId;
    private String estado, creadoPor, modificadoPor, descError;
    //Adicionales
    private boolean selected, canSave;
    private DtoGenericBean status;
    private List<Acceso> accesos;

    public Rol(Integer rolId, String nombre, String descripcion, Integer rolpadreId, String estado) {
        this.rolId = rolId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rolpadreId = rolpadreId;
        this.estado = estado;
    }

    public Rol() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getRolpadreId() {
        return rolpadreId;
    }

    public void setRolpadreId(Integer rolpadreId) {
        this.rolpadreId = rolpadreId;
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

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public DtoGenericBean getStatus() {
        return status;
    }

    public void setStatus(DtoGenericBean status) {
        this.status = status;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public List<Acceso> getAccesos() {
        return accesos;
    }

    public void setAccesos(List<Acceso> accesos) {
        this.accesos = accesos;
    }

    public boolean isCanSave() {
        return canSave;
    }

    public void setCanSave(boolean canSave) {
        this.canSave = canSave;
    }

}
