/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Henry Barrientos
 */
@Entity
@Table(name = "ESTADO", catalog = "", schema = "ECOCO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estado.findAll", query = "SELECT e FROM Estado e"),
    @NamedQuery(name = "Estado.findByEstadoId", query = "SELECT e FROM Estado e WHERE e.estadoId = :estadoId"),
    @NamedQuery(name = "Estado.findByNombre", query = "SELECT e FROM Estado e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Estado.findByDescripcion", query = "SELECT e FROM Estado e WHERE e.descripcion = :descripcion"),
    @NamedQuery(name = "Estado.findByCreadoPor", query = "SELECT e FROM Estado e WHERE e.creadoPor = :creadoPor"),
    @NamedQuery(name = "Estado.findByCreadoEl", query = "SELECT e FROM Estado e WHERE e.creadoEl = :creadoEl"),
    @NamedQuery(name = "Estado.findByModificadoPor", query = "SELECT e FROM Estado e WHERE e.modificadoPor = :modificadoPor"),
    @NamedQuery(name = "Estado.findByModificadoEl", query = "SELECT e FROM Estado e WHERE e.modificadoEl = :modificadoEl")})
public class Estado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO_ID", nullable = false)
    private Short estadoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NOMBRE", nullable = false, length = 30)
    private String nombre;
    @Size(max = 100)
    @Column(name = "DESCRIPCION", length = 100)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "CREADO_POR", nullable = false, length = 40)
    private String creadoPor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREADO_EL", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date creadoEl;
    @Size(max = 40)
    @Column(name = "MODIFICADO_POR", length = 40)
    private String modificadoPor;
    @Column(name = "MODIFICADO_EL")
    @Temporal(TemporalType.DATE)
    private Date modificadoEl;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoId")
    private List<Turno> turnoList;

    public Estado() {
    }

    public Estado(Short estadoId) {
        this.estadoId = estadoId;
    }

    public Estado(Short estadoId, String nombre, String creadoPor, Date creadoEl) {
        this.estadoId = estadoId;
        this.nombre = nombre;
        this.creadoPor = creadoPor;
        this.creadoEl = creadoEl;
    }

    public Short getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Short estadoId) {
        this.estadoId = estadoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    @XmlTransient
    public List<Turno> getTurnoList() {
        return turnoList;
    }

    public void setTurnoList(List<Turno> turnoList) {
        this.turnoList = turnoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estadoId != null ? estadoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Estado)) {
            return false;
        }
        Estado other = (Estado) object;
        if ((this.estadoId == null && other.estadoId != null) || (this.estadoId != null && !this.estadoId.equals(other.estadoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fundamental.model.Estado[ estadoId=" + estadoId + " ]";
    }
    
}
