/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.fundamental.model.Cliente;
import com.fundamental.model.Dia;
import com.fundamental.model.Precio;
import com.fundamental.model.Producto;
import com.fundamental.model.Turno;
import com.fundamental.model.dto.DtoProducto;
import com.sisintegrados.generic.bean.EmpleadoBombaTurno;
import com.sisintegrados.generic.bean.GenericTarjeta;
import com.sisintegrados.generic.bean.Tarjeta;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mery
 */
public class SvcReporteControlMediosPago extends Dao {

    private String query;

//    public ArrayList<GenericEstacion> getCheckEstaciones() {
//        ArrayList<GenericEstacion> result = new ArrayList<GenericEstacion>();
//        GenericEstacion genestacion = new GenericEstacion;
//        CheckBox check = new CheckBox();
//        try {
//            query = "select estacion_id, nombre from estacion";
//            pst = getConnection().prepareStatement(query);
//            ResultSet rst = pst.executeQuery();
//            while (rst.next()) {
//                genestacion = new GenericEstacion();
//                genestacion 
//                genestacion 
//            }
//                  }
//        } catch (Exception exc) {
//            exc.printStackTrace();
//        } finally {
//            try {
//                pst.close();
//            } catch (Exception ignore) {
//            }
//        }
//        return result;

    }
