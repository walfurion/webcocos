/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Allan G.
 */
@Data
@NoArgsConstructor
public class GenericDetalleFM {
    private Integer iddet;
    private Estacion estacion;
    private GenericBeanMedioPago mediopago;
    private GenericLote genlote;
    private String cliente;
    private Double venta;
    private String comentario;

    public GenericDetalleFM(Integer iddet, Estacion estacion, GenericBeanMedioPago mediopago, GenericLote genlote, String cliente, Double venta, String comentario) {
        this.iddet = iddet;
        this.estacion = estacion;
        this.mediopago = mediopago;
        this.genlote = genlote;
        this.cliente = cliente;
        this.venta = venta;
        this.comentario = comentario;
    }
}
