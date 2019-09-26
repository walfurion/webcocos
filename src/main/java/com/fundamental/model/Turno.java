package com.fundamental.model;

import java.util.Date;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class Turno {

    private Integer     turnoId;
    private Integer     estacionId;
    private Integer     usuarioId;
//    private Integer horaFin;
    private Integer     estadoId;
//    private Integer horarioEstado;
//    private Integer horarioId;
    private String      turnoFusion;
    private Integer     estacionconfheadId;
    private String      creadoPor, creadoPersona, modificadoPor, modificadoPersona;
    private Date        creadoEl, modificadoEl, fecha;
    //Adicionales
    private List<BombaestacionTurno> bombaestacionTurno;
    private String      descError;
    private String      nombre;
    private Boolean selected;
    private Integer horarioId;
    private Horario horario;

    public Turno(Integer turnoId, Integer estacionId, Integer usuarioId, Integer estadoId, Date fecha, String creadoPor, String creadoPersona) {
        this.turnoId = turnoId;
        this.estacionId = estacionId;
        this.usuarioId = usuarioId;
        this.estadoId = estadoId;
        this.fecha = fecha;
        this.creadoPor = creadoPor;
        this.creadoPersona = creadoPersona;
    }
    
    public Turno(Integer turnoId, Integer estacionId, Integer usuarioId, Integer estadoId, String creadoPor, Date creadoEl, String creadoPersona) {
        this.turnoId = turnoId;
        this.estacionId = estacionId;
        this.usuarioId = usuarioId;
        this.estadoId = estadoId;
        this.creadoPor = creadoPor;
        this.creadoEl = creadoEl;
        this.creadoPersona = creadoPersona;
    }

    public Turno() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(Integer turnoId) {
        this.turnoId = turnoId;
    }

    public Integer getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(Integer estacionId) {
        this.estacionId = estacionId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
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
    
    public Date getCreadoEl(){
        return creadoEl;
    }
    
    public void setCreadoEl(Date creadoEl){
        this.creadoEl = creadoEl;
    }

    public Date getModificadoEl() {
        return modificadoEl;
    }

    public void setModificadoEl(Date modificadoEl) {
        this.modificadoEl = modificadoEl;
    }

    public String getCreadoPersona() {
        return creadoPersona;
    }

    public void setCreadoPersona(String creadoPersona) {
        this.creadoPersona = creadoPersona;
    }

    public List<BombaestacionTurno> getBombaestacionTurno() {
        return bombaestacionTurno;
    }

    public void setBombaestacionTurno(List<BombaestacionTurno> bombaestacionTurno) {
        this.bombaestacionTurno = bombaestacionTurno;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModificadoPersona() {
        return modificadoPersona;
    }

    public void setModificadoPersona(String modificadoPersona) {
        this.modificadoPersona = modificadoPersona;
    }

    public String getTurnoFusion() {
        return turnoFusion;
    }

    public void setTurnoFusion(String turnoFusion) {
        this.turnoFusion = turnoFusion;
    }

    public Integer getEstacionconfheadId() {
        return estacionconfheadId;
    }

    public void setEstacionconfheadId(Integer estacionconfheadId) {
        this.estacionconfheadId = estacionconfheadId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Integer getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(Integer horarioId) {
        this.horarioId = horarioId;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

}
