package com.fundamental.model;

import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.dto.DtoGenericBean;
import java.util.Date;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class Horario {

    private int horarioId;
    private String nombre, horaInicio, horaFin, estado, creadoPor;
//
    private String descripcion, modificadoPor, descError, nombreHoras;
    private Date creadoEl, modificadoEl, horaIDate, horaFDate;
    private DtoGenericBean status;
    private List<Estacion> listStations;
    private int estacionconfheadId;

    public Horario(int horarioId, String nombre, String horaInicio, String horaFin, String estado, String descripcion) {
        this.horarioId = horarioId;
        this.nombre = nombre;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.descripcion = descripcion;
    }

    public Horario() {
    }

    public int getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(int horarioId) {
        this.horarioId = horarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public Date getCreadoEl() {
        return creadoEl;
    }

    public void setCreadoEl(Date creadoEl) {
        this.creadoEl = creadoEl;
    }

    public Date getModificadoEl() {
        return modificadoEl;
    }

    public void setModificadoEl(Date modificadoEl) {
        this.modificadoEl = modificadoEl;
    }

    public DtoGenericBean getStatus() {
        return status;
    }

    public void setStatus(DtoGenericBean status) {
        this.status = status;
    }

    public List<Estacion> getListStations() {
        return listStations;
    }

    public void setListStations(List<Estacion> listStations) {
        this.listStations = listStations;
    }

    public Date getHoraIDate() {
        return horaIDate;
    }

    public void setHoraIDate(Date horaIDate) {
        this.horaIDate = horaIDate;
    }

    public Date getHoraFDate() {
        return horaFDate;
    }

    public void setHoraFDate(Date horaFDate) {
        this.horaFDate = horaFDate;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public int getEstacionconfheadId() {
        return estacionconfheadId;
    }

    public void setEstacionconfheadId(int estacionconfheadId) {
        this.estacionconfheadId = estacionconfheadId;
    }

    public String getNombreHoras() {
        return nombreHoras;
    }

    public void setNombreHoras(String nombreHoras) {
        this.nombreHoras = nombreHoras;
    }

}
