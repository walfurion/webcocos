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
 * @author m
 */
@Data
@NoArgsConstructor
public class GenericSMTP {
    private String host;
    private String port;
    private String tipoSMTP;
    private String userAuth;
    private String passAuth;
    private String mailFrom;
    private String llave;
    private String valor;

    public GenericSMTP(String llave, String valor) {
        this.llave = llave;
        this.valor = valor;
    }
}
