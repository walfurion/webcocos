/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author jjosue
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositoDet {

    private Integer idepositodet;
    private Integer fecha;
    private Double estacion_id;
    private String mediopago_id;
    private String noboleta;
    private String comentarios;
    private String monto;
    

    public DepositoDet(Integer idepositodet) {
        this.idepositodet = idepositodet;
    }

    
    
    
    

}
