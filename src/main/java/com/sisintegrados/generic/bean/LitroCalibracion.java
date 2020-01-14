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
public class LitroCalibracion {

    String nombre;
    Double precio;
    Double litro;
    Double ventaColones;

    public LitroCalibracion(String nombre, Double precio, Double litro, Double ventaColones) {
        this.nombre = nombre;
        this.precio = precio;
        this.litro = litro;
        this.ventaColones = ventaColones;
    }

}
