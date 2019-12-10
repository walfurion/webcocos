/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author m
 */
@Data
@NoArgsConstructor
public class RecepcionInventario {
    private Integer invrecepcion_id;
    private Date fecha;
    private Integer pais_id;
    private Integer estacion_id;
    private String piloto;
    private String unidad;
    private String factura;
    private String creado_por;
    private Date creado_el;
    
    public RecepcionInventario(Integer invrecepcion_id, Date fecha, Integer pais_id, Integer estacion_id, String piloto, String unidad, String factura) {
        this.invrecepcion_id = invrecepcion_id;
        this.fecha = fecha;
        this.pais_id = pais_id;
        this.estacion_id = estacion_id;
        this.piloto = piloto;
        this.unidad = unidad;
        this.factura = factura;
    }
}
