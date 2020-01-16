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
    private Integer genericid;
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
    private String cliente;
    
    private String cod_cliente;
    private String tipo_cliente;

    public GenericRprControlMediosPago(Integer genericid,Date fecha, Double mediopago_id, Double lote, Double monto_bruto, Double comision, Double monto_neto, String comentarios, String codigo, String estacion, String banco, String nodeposito, Double montoch, Double montousd, String cliente,  String cod_cliente, String tipo_cliente) {
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
        this.cliente = cliente;
        this.genericid = genericid;
        
        this.cod_cliente = cod_cliente;
        this.tipo_cliente = tipo_cliente;
    }
}
