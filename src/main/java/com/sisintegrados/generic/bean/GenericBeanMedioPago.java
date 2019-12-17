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
public class GenericBeanMedioPago {
    private Integer mediopagoid;
    private String nombre;

    public GenericBeanMedioPago(Integer mediopagoid, String nombre) {
        this.mediopagoid = mediopagoid;
        this.nombre = nombre;
    }
    
}
