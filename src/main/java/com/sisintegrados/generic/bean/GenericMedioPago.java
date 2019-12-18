/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import com.fundamental.model.Mediopago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author jjosu
 */
@Data
@NoArgsConstructor
public class GenericMedioPago {

    private Integer idgeneric;
    private Estacion estacion;
    private GenericBeanMedioPago mediopago;
    private String noboleta;
    private String comentarios;
    private Double monto;
    private Double montousd;
    public GenericMedioPago(Integer idgeneric,Estacion estacion, GenericBeanMedioPago mediopago, String noboleta, String comentarios, Double monto, Double montousd) {
        this.idgeneric = idgeneric;
        this.estacion = estacion;
        this.mediopago = mediopago;
        this.noboleta = noboleta;
        this.comentarios = comentarios;
        this.monto = monto;
        this.montousd = montousd;
    }
}
