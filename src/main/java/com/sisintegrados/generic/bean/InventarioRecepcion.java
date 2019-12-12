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
public class InventarioRecepcion extends InventarioRec {
    private Integer idDto;
    private String productoNombre;
    private Double calibracion;
    private Double ventas;
    private Double ventasCons;
    private Double diferencia;
    private String estado = "        ";
    //utiles para la tabla
    private Double inicialDto;
    private Double finallDto;
    private Double comprasDto;
    private Boolean esNuevo;
    private String tanque;
    
    public InventarioRecepcion(Integer idDto, String productoNombre, Double calibracion, Date fecha, Integer estacionId, Integer productoId, Double inicial, Double finall, Double compras, String creadoPor, String creadoPersona) {
        super(fecha, estacionId, productoId, inicial, finall, compras, creadoPor, creadoPersona);
        this.idDto = idDto;
        this.productoNombre = productoNombre;
        this.calibracion = calibracion;
    }
}
