/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import java.util.Date;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author m
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class InventarioRec {
    private Date fecha;
    private Integer estacionId;
    private Integer productoId;
    private Double inicial;
    private Double finall;
    private Double compras;
    private Double lecturaVeederRoot;
    private Double inventarioFisico; 
    private Double diferencia;
    private Double varianza;
    private String creadoPor;
    private String creadoPersona;
    private String modificadoPor;
    private String modificadoPersona;
    private Integer volFacturado;
    private Integer galonesCisterna;
    private String compartimiento;
    private String pulgadas; 
    private Double ventas; 
    private Double calibracion; 

    public InventarioRec(Date fecha, Integer estacionId, Integer productoId, Double inicial, Double finall, Double compras, String creadoPor, String creadoPersona) {
        this.fecha = fecha;
        this.estacionId = estacionId;
        this.productoId = productoId;
        this.inicial = inicial;
        this.finall = finall;
        this.compras = compras;
        this.creadoPor = creadoPor;
        this.creadoPersona = creadoPersona;
    }
}
