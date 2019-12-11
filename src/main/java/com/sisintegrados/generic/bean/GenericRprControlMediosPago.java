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
    private Double lote;
    private Double monto_bruto;
    private Double comision;
    private Double monto_neto;
    private String comentarios;

    public GenericRprControlMediosPago(Date fecha, Double lote, Double monto_bruto, Double comision, Double monto_neto, String comentarios) {
        this.fecha = fecha;
        this.lote = lote;
        this.monto_bruto = monto_bruto;
        this.comision = comision;
        this.monto_neto = monto_neto;
        this.comentarios = comentarios;
    }
}
