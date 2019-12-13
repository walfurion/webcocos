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
 * @author jjosu
 */
@Data
@NoArgsConstructor
public class GenericMedioPago {
    private Integer mediopagoid;
    private String nombre;
    
    public GenericMedioPago(Integer mediopagoid, String nombre) {
        this.mediopagoid = mediopagoid;
        this.nombre = nombre;
    }
}
