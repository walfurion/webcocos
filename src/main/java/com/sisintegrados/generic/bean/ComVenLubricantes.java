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
public class ComVenLubricantes {
    private int compraId;
    private int marcaId;
    private int productoId;
    private Date fecha;
    private Double invInicial;
    private Double compra;
    private Double venta;
    private Double invfinal;
    private String creadopor;
    private Date creadoel;
    private String modificadopor;
    private Date modificadoel;
    
    private String productoNombre;
}
