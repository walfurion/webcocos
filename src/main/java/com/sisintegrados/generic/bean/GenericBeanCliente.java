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
 * @author jjosue
 */
@Data
@NoArgsConstructor
public class GenericBeanCliente {

    private Integer clienteid;
    private Integer codigo;
    private String nombre;

    public GenericBeanCliente(Integer clienteid, Integer codigo, String nombre) {
        this.clienteid = clienteid;
        this.codigo = codigo;
        this.nombre = nombre;
    }
    
    

}
