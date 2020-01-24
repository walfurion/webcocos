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
public class UnoConfiguraciones {

    private Long idconfiguracion;
    private String llave;
    private String valor;

    public UnoConfiguraciones(Long idconfiguracion, String llave, String valor) {
        this.idconfiguracion = idconfiguracion;
        this.llave = llave;
        this.valor = valor;
    }
}
