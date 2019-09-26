package com.fundamental.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Henry Barrientos
 */
@Entity
@Table(name = "PARAMETRO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parametro.findAll", query = "SELECT p FROM Parametro p")})
public class Parametro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PARAMETRO_ID", nullable = false)
    private Short parametroId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 350)
    @Column(name = "VALOR", nullable = false, length = 350)
    private String valor;
    @Size(max = 350)
    @Column(name = "DESCRIPCION", length = 350)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ESTADO", nullable = false, length = 1)
    private String estado;
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

    public Parametro() {
    }

    public Parametro(Short parametroId) {
        this.parametroId = parametroId;
    }

    public Parametro(Short parametroId, String nombre, String valor, String estado, String creadoPor, Date creadoEl) {
        this.parametroId = parametroId;
        this.nombre = nombre;
        this.valor = valor;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.creadoEl = creadoEl;
    }

    public Short getParametroId() {
        return parametroId;
    }

    public void setParametroId(Short parametroId) {
        this.parametroId = parametroId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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

    public Date getCreadoEl() {
        return creadoEl;
    }

    public void setCreadoEl(Date creadoEl) {
        this.creadoEl = creadoEl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (parametroId != null ? parametroId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parametro)) {
            return false;
        }
        Parametro other = (Parametro) object;
        if ((this.parametroId == null && other.parametroId != null) || (this.parametroId != null && !this.parametroId.equals(other.parametroId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fundamental.model.Parametro[ parametroId=" + parametroId + " ]";
    }
    
}
