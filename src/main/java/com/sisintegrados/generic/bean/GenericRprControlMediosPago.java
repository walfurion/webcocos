/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Mery
 */
@Data
@NoArgsConstructor

public class GenericRprControlMediosPago {
    private Date fecha;
    private Double mediopago_id;
    private Double lote;
    private Double monto_bruto;
    private Double comision;
    private Double monto_neto;
    private String comentarios;
    private String codigo;
    private String estacion;
    private String banco;
    private String nodeposito;
    private Double montoch;
    private Double montousd;

    public GenericRprControlMediosPago(Date fecha, Double mediopago_id, Double lote, Double monto_bruto, Double comision, Double monto_neto, String comentarios, String codigo, String estacion, String banco, String nodeposito, Double montoch, Double montousd) {
        this.fecha = fecha;
        this.mediopago_id = mediopago_id;
        this.lote = lote;
        this.monto_bruto = monto_bruto;
        this.comision = comision;
        this.monto_neto = monto_neto;
        this.comentarios = comentarios;
        this.codigo = codigo;
        this.estacion = estacion;
        this.banco = banco;
        this.nodeposito = nodeposito;
        this.montoch = montoch;
        this.montousd = montousd;
    }

    
}
