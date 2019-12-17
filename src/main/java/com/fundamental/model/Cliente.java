package com.fundamental.model;

import java.util.Date;

/**
 * @author Henry Barrientos
 */
public class Cliente {

    private int clienteId;
    private String codigo, nombre;
    private int estacionId;
    private String estado, creadoPor, tipo, codigoEnvoy, cedulaJuridica;
    private Date creadoEl;
    //
    private int paisId;
    private String paisNombre, estacionNombre;    

    public Cliente(int clienteId, String codigo, String nombre, int estacionId, String estado, String creadoPor, Date creadoEl, String tipo, String codigoEnvoy, String cedulaJuridica) {
        this.clienteId = clienteId;
        this.codigo = codigo;
        this.nombre = nombre;
        this.estacionId = estacionId;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.creadoEl = creadoEl;
        this.tipo = tipo;
        this.codigoEnvoy = codigoEnvoy;
        this.cedulaJuridica = cedulaJuridica;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public int getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(int estacionId) {
        this.estacionId = estacionId;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPaisNombre() {
        return paisNombre;
    }

    public void setPaisNombre(String paisNombre) {
        this.paisNombre = paisNombre;
    }

    public String getEstacionNombre() {
        return estacionNombre;
    }

    public void setEstacionNombre(String estacionNombre) {
        this.estacionNombre = estacionNombre;
    }

    public int getPaisId() {
        return paisId;
    }

    public void setPaisId(int paisId) {
        this.paisId = paisId;
    }

    public String getCodigoEnvoy() {
        return codigoEnvoy;
    }

    public void setCodigoEnvoy(String codigoEnvoy) {
        this.codigoEnvoy = codigoEnvoy;
    }

    public String getCedulaJuridica() {
        return cedulaJuridica;
    }

    public void setCedulaJuridica(String cedulaJuridica) {
        this.cedulaJuridica = cedulaJuridica;
    }

    @Override
    public String toString() {
        return "Cliente{" + "clienteId=" + clienteId + ", codigo=" + codigo + ", nombre=" + nombre + ", estacionId=" + estacionId + ", estado=" + estado + ", creadoPor=" + creadoPor + ", tipo=" + tipo + ", codigoEnvoy=" + codigoEnvoy + ", cedulaJuridica=" + cedulaJuridica + ", creadoEl=" + creadoEl + ", paisId=" + paisId + ", paisNombre=" + paisNombre + ", estacionNombre=" + estacionNombre + '}';
    }
    
    
}
