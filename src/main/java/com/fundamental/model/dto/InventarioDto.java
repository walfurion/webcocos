package com.fundamental.model.dto;

import com.fundamental.model.Inventario;
import java.util.Date;

/**
 * @author Henry Barrientos
 */
public class InventarioDto extends Inventario {
        
    private Integer idDto;
    private String productoNombre;
    private Double calibracion, ventas, ventasCons, diferencia;
    private String estado = "        ";
    //utiles para la tabla
    private Double inicialDto;
    private Double finallDto;
    private Double comprasDto;
    private Boolean esNuevo;
    
//    private Integer inventarioFisico, lecturaVeederRoot, volFacturado, galones;
//            private String compartimiento, pulgadas;

    public InventarioDto(Integer idDto, String productoNombre, Double calibracion, Date fecha, Integer estacionId, Integer productoId, Double inicial, Double finall, Double compras, String creadoPor, String creadoPersona) {
        super(fecha, estacionId, productoId, inicial, finall, compras, creadoPor, creadoPersona);
        this.idDto = idDto;
        this.productoNombre = productoNombre;
        this.calibracion = calibracion;
    }

    public Integer getIdDto() {
        return idDto;
    }

    public void setIdDto(Integer idDto) {
        this.idDto = idDto;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public Double getCalibracion() {
        return calibracion;
    }

    public void setCalibracion(Double calibracion) {
        this.calibracion = calibracion;
    }

    public Double getVentas() {
        return ventas;
    }

    public void setVentas(Double ventas) {
        this.ventas = ventas;
    }

    public Double getVentasCons() {
        return ventasCons;
    }

    public void setVentasCons(Double ventasCons) {
        this.ventasCons = ventasCons;
    }

    public Double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Double diferencia) {
        this.diferencia = diferencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getInicialDto() {
        return inicialDto;
    }

    public void setInicialDto(Double inicialDto) {
        this.inicialDto = inicialDto;
    }

    public Double getFinallDto() {
        return finallDto;
    }

    public void setFinallDto(Double finallDto) {
        this.finallDto = finallDto;
    }

    public Double getComprasDto() {
        return comprasDto;
    }

    public void setComprasDto(Double comprasDto) {
        this.comprasDto = comprasDto;
    }

    public Boolean getEsNuevo() {
        return esNuevo;
    }

    public void setEsNuevo(Boolean esNuevo) {
        this.esNuevo = esNuevo;
    }

//    public Integer getInventarioFisico() {
//        return inventarioFisico;
//    }
//
//    public void setInventarioFisico(Integer inventarioFisico) {
//        this.inventarioFisico = inventarioFisico;
//    }
//
//    public Integer getLecturaVeederRoot() {
//        return lecturaVeederRoot;
//    }
//
//    public void setLecturaVeederRoot(Integer lecturaVeederRoot) {
//        this.lecturaVeederRoot = lecturaVeederRoot;
//    }
//
//    public Integer getVolFacturado() {
//        return volFacturado;
//    }
//
//    public void setVolFacturado(Integer volFacturado) {
//        this.volFacturado = volFacturado;
//    }
//
//    public Integer getGalones() {
//        return galones;
//    }
//
//    public void setGalones(Integer galones) {
//        this.galones = galones;
//    }
//
//    public String getCompartimiento() {
//        return compartimiento;
//    }
//
//    public void setCompartimiento(String compartimiento) {
//        this.compartimiento = compartimiento;
//    }
//
//    public String getPulgadas() {
//        return pulgadas;
//    }
//
//    public void setPulgadas(String pulgadas) {
//        this.pulgadas = pulgadas;
//    }
//



}
