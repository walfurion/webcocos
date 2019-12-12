package com.fundamental.model;

import com.sisintegrados.generic.bean.Pais;
import java.util.Date;

/**
 * @author Henry Barrientos
 */
public class Lubricanteprecio implements Cloneable{

    private int lubricanteprecio, paisId, //estacionId, 
            productoId;
    private Date fechaInicio, fechaFin, creadoEl;
    private double precio;
    private String creadoPor;
//
    private Date modificadoEl;
    private String modificadoPor, paisNombre, estacionNombre, productoNombre, marcaNombre, descError;
    private Marca marca;
    private Producto producto;
    private Pais pais;
    public Object clone(){
        Lubricanteprecio obj=null;
        try{
            obj=(Lubricanteprecio)super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }
//    public Lubricanteprecio(int lubricanteprecio, int paisId, int estacionId, int productoId, Date fechaInicio, Date fechaFin, double precio, String creadoPor) {
    public Lubricanteprecio(int lubricanteprecio, int paisId, int productoId, Date fechaInicio, Date fechaFin, double precio, String creadoPor) {
        this.lubricanteprecio = lubricanteprecio;
        this.paisId = paisId;
//        this.estacionId = estacionId;
        this.productoId = productoId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.precio = precio;
        this.creadoPor = creadoPor;
    }

    public Lubricanteprecio() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getLubricanteprecio() {
        return lubricanteprecio;
    }

    public void setLubricanteprecio(int lubricanteprecio) {
        this.lubricanteprecio = lubricanteprecio;
    }

    public int getPaisId() {
        return paisId;
    }

    public void setPaisId(int paisId) {
        this.paisId = paisId;
    }

//    public int getEstacionId() {
//        return estacionId;
//    }
//
//    public void setEstacionId(int estacionId) {
//        this.estacionId = estacionId;
//    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getCreadoEl() {
        return creadoEl;
    }

    public void setCreadoEl(Date creadoEl) {
        this.creadoEl = creadoEl;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Date getModificadoEl() {
        return modificadoEl;
    }

    public void setModificadoEl(Date modificadoEl) {
        this.modificadoEl = modificadoEl;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
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

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getMarcaNombre() {
        return marcaNombre;
    }

    public void setMarcaNombre(String marcaNombre) {
        this.marcaNombre = marcaNombre;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    @Override
    public String toString() {
        return "Lubricanteprecio{" + "lubricanteprecio=" + lubricanteprecio + ", paisId=" + paisId + ", productoId=" + productoId + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", creadoEl=" + creadoEl + ", precio=" + precio + ", creadoPor=" + creadoPor + ", modificadoEl=" + modificadoEl + ", modificadoPor=" + modificadoPor + ", paisNombre=" + paisNombre + ", estacionNombre=" + estacionNombre + ", productoNombre=" + productoNombre + ", marcaNombre=" + marcaNombre + ", descError=" + descError + ", marca=" + marca + ", producto=" + producto + ", pais=" + pais + '}';
    }

}
