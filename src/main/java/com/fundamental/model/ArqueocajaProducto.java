package com.fundamental.model;

/**
 * @author Henry Barrientos
 */
public class ArqueocajaProducto {

    private Integer arqueocajaId;
    private Integer productoId;
    private Double monto;
    private String descError;
    //Adicionales
    private String nombreProducto;

    public ArqueocajaProducto(Integer arqueocajaId, Integer productoId, Double monto) {
        this.arqueocajaId = arqueocajaId;
        this.productoId = productoId;
        this.monto = monto;
    }

    public ArqueocajaProducto() {
    }

    public Integer getArqueocajaId() {
        return arqueocajaId;
    }

    public void setArqueocajaId(Integer arqueocajaId) {
        this.arqueocajaId = arqueocajaId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
}
