/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author jjosue
 */

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class GenericProduct {
    
    private Integer productoid;
    private String nombre;
    private String codigo;
    private Double precio;
    private Integer idmarca;

    
    

    public GenericProduct(Integer productoid, String nombre, String codigo, Double precio, Integer idmarca) {
        this.productoid = productoid;
        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.idmarca = idmarca;
    }
    
       public GenericProduct(Integer productoId, String nombre, String codigo) {
        this.productoid = productoid;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    

    
}
