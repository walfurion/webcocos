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
public class compraGeneric {

    Integer inv_inicial;
    Integer inv_final;
    Integer compra;
    Integer venta;

    public compraGeneric(Integer invInicial, Integer invFinal, Integer compra, Integer venta) {
        this.inv_inicial = invInicial;
        this.inv_final = invFinal;
        this.compra = compra;
        this.venta = venta;
    }
}
