/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import java.util.Date;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author m
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class ComInventarioFisico {
    private int numero;
    private int inventario_id;
    private int producto_id;
    private Date fecha;
    private Double inv_final;
    private Double unidad_fis_tienda;
    private Double unidad_fis_bodega;
    private Double unidad_fis_pista;
    private Double total_unidad_fisica;
    private Double diferencia_inv;
    private String comentario;
    private String creado_por;
    private Date creado_el;
    private String modificado_por;
    private Date modificado_el;
    private String productoNombre;
    private String presentacion;
    private Double precio;

    public ComInventarioFisico(int producto_id, int numero, Double inv_final, String productoNombre, String presentacion, Double precio) {
        this.producto_id = producto_id;
        this.numero = numero;
        this.inv_final = inv_final;
        this.productoNombre = productoNombre;
        this.presentacion = presentacion;
        this.precio = precio;
    }

    
}
