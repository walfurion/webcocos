/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author m
 */
@Data
@NoArgsConstructor
public class GenericRptWetStock {
    private Date fecha;
    private int producto_id;
    private Double inicial;
    private Double compras;
    private Double ventas;
    private String ajuste;
    private Double inv_fisico;
    private String nivel;
    private String piloto;
    private String unidad;
    private String compratimiento;
    private String factura;
    private Double comprasFact;
    private Double inv_teorico;
    private Double varianza;
    private Double diferencia;

    public GenericRptWetStock(Date fecha, int producto_id, Double inicial, Double compras, Double ventas, Double inv_fisico, String piloto, String unidad, String compratimiento, String factura, Double comprasFact, Double inv_teorico, Double varianza, Double diferencia) {
        this.fecha = fecha;
        this.producto_id = producto_id;
        this.inicial = inicial;
        this.compras = compras;
        this.ventas = ventas;
        this.inv_fisico = inv_fisico;
        this.piloto = piloto;
        this.unidad = unidad;
        this.compratimiento = compratimiento;
        this.factura = factura;
        this.comprasFact = comprasFact;
        this.inv_teorico = inv_teorico;
        this.varianza = varianza;
        this.diferencia = diferencia;
    }
    
    
}
