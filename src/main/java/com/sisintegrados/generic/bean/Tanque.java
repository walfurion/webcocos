/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import com.fundamental.model.Producto;
import com.fundamental.model.dto.DtoProducto;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author jjosue
 */

@Data
@NoArgsConstructor

public class Tanque {
    
    private int idtanque;
    private Producto producto;
    private Estacion estacion;
    private String descripcion;
    private String usuarioCreacion;
    private Date fechaCreacion;
    private String usuarioModificacion;
    private Date fechaModificacion;
    private String descProducto;
    private String descEstacion;
    
    public Tanque(int idtanque, Producto producto, Estacion estacion, String descripcion) {
        this.idtanque = idtanque;
        this.producto = producto;
        this.estacion = estacion;
        this.descripcion = descripcion;
    }

    public Tanque(int idtanque, Producto producto, Estacion estacion, String descripcion, String descProducto, String descEstacion) {
        this.idtanque = idtanque;
        this.producto = producto;
        this.estacion = estacion;
        this.descripcion = descripcion;
        this.descProducto = descProducto;
        this.descEstacion = descEstacion;
    }
    
    
    
    

    public Tanque(int idtanque, String descripcion) {
        this.idtanque = idtanque;
        this.descripcion = descripcion;
    }

    public Tanque(int idtanque, Producto producto, Estacion estacion, String descripcion, String usuarioCreacion, Date fechaCreacion, String usuarioModificacion, Date fechaModificacion) {
        this.idtanque = idtanque;
        this.producto = producto;
        this.estacion = estacion;
        this.descripcion = descripcion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
    }

    public Tanque(int idtanque, Producto producto, Estacion estacion, String descripcion, String usuarioCreacion, Date fechaCreacion, String usuarioModificacion, Date fechaModificacion, String descProducto, String descEstacion) {
        this.idtanque = idtanque;
        this.producto = producto;
        this.estacion = estacion;
        this.descripcion = descripcion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
        this.descProducto = descProducto;
        this.descEstacion = descEstacion;
    }
    
    
    
    
    
    
    
    
}
