/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import com.fundamental.model.Mediopago;
import com.fundamental.model.Producto;
import com.fundamental.model.dto.DtoArqueo;
import com.fundamental.model.dto.DtoEfectivo;
import com.vaadin.data.util.BeanContainer;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Allan G.
 */
@Data
@NoArgsConstructor
public class RepCuadrePistero {
    String empleado;
    String bomba;
    String turno;
    Integer calibracion;
    BeanContainer<Integer, DtoArqueo> arqueo;
    BeanContainer<Integer, DtoEfectivo> efectivo;
    BeanContainer<Integer, Mediopago> mediopago;
    BeanContainer<Integer, Producto> prodadicionales;
    Date fecha;

    public RepCuadrePistero(String empleado, String bomba, String turno, Integer calibracion, BeanContainer<Integer, DtoArqueo> arqueo, BeanContainer<Integer, DtoEfectivo> efectivo, BeanContainer<Integer, Mediopago> mediopago, BeanContainer<Integer, Producto> prodadicionales,Date fecha) {
        this.empleado = empleado;
        this.bomba = bomba;
        this.turno = turno;
        this.calibracion = calibracion;
        this.arqueo = arqueo;
        this.efectivo = efectivo;
        this.mediopago = mediopago;
        this.prodadicionales = prodadicionales;
        this.fecha = fecha;
    }
}
