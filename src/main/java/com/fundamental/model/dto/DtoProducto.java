package com.fundamental.model.dto;

import com.fundamental.model.Cliente;
import com.fundamental.model.Producto;

/**
 * @author Henry Barrientos
 */
public class DtoProducto {

    private Integer productoId;
    private String nombre;
    private String codigo;
    private Double valor, total;
    //Adicional
    private Double galones;
    private Double galonesFS;
    private Double valorFS;
    private Double galonesDif;
    private Double valorDif;
    private String facturar, presentacion, codigoBarras;
    private Cliente cliente;
    private Producto producto;
    private int cantidad;
    private int idmarca;

    public DtoProducto(Integer productoId, String nombre, String codigo) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.codigo = codigo;
    }
    
    public void initValorGalones(){
        this.galones = 0D;
        this.valor = 0D;
        this.galonesDif = 0D;
        this.valorDif = 0D;
        this.galonesFS = 0D;
        this.valorFS = 0D;
    }

    public DtoProducto() {
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getGalones() {
        return galones;
    }

    public void setGalones(Double galones) {
        this.galones = galones;
    }

    public Double getGalonesFS() {
        return galonesFS;
    }

    public void setGalonesFS(Double galonesFS) {
        this.galonesFS = galonesFS;
    }

    public Double getValorFS() {
        return valorFS;
    }

    public void setValorFS(Double valorFS) {
        this.valorFS = valorFS;
    }

    public Double getGalonesDif() {
        return galonesDif;
    }

    public void setGalonesDif(Double galonesDif) {
        this.galonesDif = galonesDif;
    }

    public Double getValorDif() {
        return valorDif;
    }

    public void setValorDif(Double valorDif) {
        this.valorDif = valorDif;
    }

    public String getFacturar() {
        return facturar;
    }

    public void setFacturar(String facturar) {
        this.facturar = facturar;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getIdmarca() {
        return idmarca;
    }

    public void setIdmarca(int idmarca) {
        this.idmarca = idmarca;
    }

}
