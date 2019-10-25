package com.fundamental.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class Arqueocaja {

    private Integer arqueocajaId;
    private Integer estacionId;
    private Integer turnoId;
    private Date fecha;
    private Integer estado_id, empleadoId;
    private String creado_por, creado_persona, modificadoPor, modificadoPersona, descError, nombrePistero, nombreJefe;
    //Adicionales
    private List<ArqueocajaBomba> arqueocajaBomba;
    private Boolean selected;
    private String nombre;
    private Double diferencia;
    
//    public Arqueocaja(Integer arqueocajaId, Integer estado_id, String creado_por, String creado_persona) {
//        this.arqueocajaId = arqueocajaId;
////        this.horarioId = horarioId;
//        this.estado_id = estado_id;
//        this.creado_por = creado_por;
//        this.creado_persona = creado_persona;
//    }

    public Arqueocaja(Integer arqueocajaId, Integer estacionId, Integer turnoId, Date fecha, Integer estado_id, String creado_por, String creado_persona, Integer empleadoId, String nombrePistero, String nombreJefe) {
        this.arqueocajaId = arqueocajaId;
        this.estacionId = estacionId;
        this.turnoId = turnoId;
        this.fecha = fecha;
        this.estado_id = estado_id;
        this.creado_por = creado_por;
        this.creado_persona = creado_persona;
        this.empleadoId = empleadoId;
        this.nombrePistero = nombrePistero;
        this.nombreJefe = nombreJefe;
    }
    
    public Arqueocaja(Integer arqueocajaId, Integer estacionId, Integer turnoId, Date fecha, Integer estado_id, String creado_por, String creado_persona, Integer empleadoId) {
        this.arqueocajaId = arqueocajaId;
        this.estacionId = estacionId;
        this.turnoId = turnoId;
        this.fecha = fecha;
        this.estado_id = estado_id;
        this.creado_por = creado_por;
        this.creado_persona = creado_persona;
        this.empleadoId = empleadoId;
    }

    public Arqueocaja() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getArqueocajaId() {
        return arqueocajaId;
    }

    public void setArqueocajaId(Integer arqueocajaId) {
        this.arqueocajaId = arqueocajaId;
    }

//    public Integer getHorarioId() {
//        return horarioId;
//    }
//
//    public void setHorarioId(Integer horarioId) {
//        this.horarioId = horarioId;
//    }
    public Integer getEstado_id() {
        return estado_id;
    }

    public void setEstado_id(Integer estado_id) {
        this.estado_id = estado_id;
    }

    public String getCreado_por() {
        return creado_por;
    }

    public void setCreado_por(String creado_por) {
        this.creado_por = creado_por;
    }

    public String getCreado_persona() {
        return creado_persona;
    }

    public void setCreado_persona(String creado_persona) {
        this.creado_persona = creado_persona;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public Integer getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(Integer estacionId) {
        this.estacionId = estacionId;
    }

    public Integer getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(Integer turnoId) {
        this.turnoId = turnoId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<ArqueocajaBomba> getArqueocajaBomba() {
        return arqueocajaBomba;
    }

    public void setArqueocajaBomba(List<ArqueocajaBomba> arqueocajaBomba) {
        this.arqueocajaBomba = arqueocajaBomba;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Double diferencia) {
        this.diferencia = diferencia;
    }

    public Integer getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Integer empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getNombrePistero() {
        return nombrePistero;
    }

    public void setNombrePistero(String nombrePistero) {
        this.nombrePistero = nombrePistero;
    }

    public String getNombreJefe() {
        return nombreJefe;
    }

    public void setNombreJefe(String nombreJefe) {
        this.nombreJefe = nombreJefe;
    }
}
