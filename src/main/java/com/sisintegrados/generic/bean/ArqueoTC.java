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
public class ArqueoTC {
    private Integer arqueocajaId;
    private String nombre;
    private String lote;
    private Double value;
    private Integer tarjetaId;
    
    public ArqueoTC(Integer arqueocajaId, String lote, Double value, String nombre, Integer tarjetaId) {
        this.arqueocajaId = arqueocajaId;
        this.lote = lote;
        this.value = value;
        this.nombre = nombre;
        this.tarjetaId = tarjetaId;
    }
}