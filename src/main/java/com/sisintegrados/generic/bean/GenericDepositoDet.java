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


public class GenericDepositoDet {
    
    private Integer idGenerico;
    private GenericMedioPago mediopago;
    private Double monto;
    private String observaciones;
    private String numeroboleta;

    public GenericDepositoDet(Integer idGenerico, GenericMedioPago mediopago, Double monto, String observaciones, String numeroboleta) {
        this.idGenerico = idGenerico;
        this.mediopago = mediopago;
        this.monto = monto;
        this.observaciones = observaciones;
        this.numeroboleta = numeroboleta;
    }
    
    
}
