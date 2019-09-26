package com.fundamental.model.dto;

/**
 * @author Henry Barrientos
 */
public class DtoArqueo {

    private Integer id;
    private String nombreProducto;
    private String nombreDespacho;
    private Double volumen;
    private Double precio;
    private Double venta;
    private Double diferencia;
    //Adicionales
    private Integer productoId;
    private Integer tipodespachoId;
    private String diferenciaString;
    private String ventaString;
    private Double calibracion;

    public DtoArqueo(Integer id, String nombreProducto, String nombreDespacho, Double volumen, Double precio, Double venta) {
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.nombreDespacho = nombreDespacho;
        this.volumen = volumen;
        this.precio = precio;
        this.venta = venta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getNombreDespacho() {
        return nombreDespacho;
    }

    public void setNombreDespacho(String nombreDespacho) {
        this.nombreDespacho = nombreDespacho;
    }

    public Double getVolumen() {
        return volumen;
    }

    public void setVolumen(Double volumen) {
        this.volumen = volumen;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getVenta() {
        return venta;
    }

    public void setVenta(Double venta) {
        this.venta = venta;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getTipodespachoId() {
        return tipodespachoId;
    }

    public void setTipodespachoId(Integer tipodespachoId) {
        this.tipodespachoId = tipodespachoId;
    }

    public Double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Double diferencia) {
        this.diferencia = diferencia;
    }

    public String getDiferenciaString() {
        return diferenciaString;
    }

    public void setDiferenciaString(String diferenciaString) {
        this.diferenciaString = diferenciaString;
    }

    public String getVentaString() {
        return ventaString;
    }

    public void setVentaString(String ventaString) {
        this.ventaString = ventaString;
    }

    public Double getCalibracion() {
        return calibracion;
    }

    public void setCalibracion(Double calibracion) {
        this.calibracion = calibracion;
    }

}
