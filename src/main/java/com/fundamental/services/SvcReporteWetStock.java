/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.fundamental.utils.Constant;
import com.sisintegrados.generic.bean.GenericEstacion;
import com.sisintegrados.generic.bean.GenericRptWetStock;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author m
 */
public class SvcReporteWetStock extends Dao {
    private String query;
    public ArrayList<GenericEstacion> getCheckEstaciones(Integer idpais) {
        ArrayList<GenericEstacion> result = new ArrayList<GenericEstacion>();
        GenericEstacion gnestacion = new GenericEstacion();
        try {
            query = "Select estacion_id,nombre from estacion where pais_id =" + idpais;
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                gnestacion = new GenericEstacion();
                gnestacion.setEstacionid(rst.getInt(1));
                gnestacion.setNombre(rst.getString(2));
                result.add(gnestacion);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public String getEstacion(Integer idestacion) {
        String result = "";
        query = "SELECT NOMBRE FROM ESTACION WHERE ESTACION_ID =" + idestacion;
        try {
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result = rst.getString(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }
    
    public ArrayList<GenericRptWetStock> getCtlWetStock(Date fec1, Date fec2) {
        ArrayList<GenericRptWetStock> result = new ArrayList<GenericRptWetStock>();
        String fecha1 = Constant.SDF_ddMMyyyy.format(fec1);
        String fecha2 = Constant.SDF_ddMMyyyy.format(fec2);
        GenericRptWetStock genctl = new GenericRptWetStock();
        try {
            query = "select r.fecha, t.producto_id, r.inicial, r.compras, r.ventas, '' ajustes, r.inv_fisico, " +
                    " '' nivel, p.piloto, p.unidad, r.compartimiento, p.factura, r.compras comprasFact, r.lectura_veeder, r.varianza, r.diferencia " +
                    "from RECEPCION_INVENTARIO_DETALLE r, tanque t, recepcion_inventario p\n" +
                    "where t.producto_id=r.producto_id and r.invrecepcion_id = p.invrecepcion_id and\n" +
                    "r.fecha >= to_date('"+fecha1+"','dd/mm/yyyy') and r.fecha <= to_date('"+fecha2+"','dd/mm/yyyy')\n" +
                    "order by r.fecha, t.PRODUCTO_ID";
            System.out.println("QUERY " + query);
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                genctl = new GenericRptWetStock(rst.getDate(1), rst.getInt(2), rst.getDouble(3), rst.getDouble(4), rst.getDouble(5), rst.getDouble(7), rst.getString(9), rst.getString(10), rst.getString(11), rst.getString(12), rst.getDouble(13), rst.getDouble(14), rst.getDouble(15), rst.getDouble(16));
                result.add(genctl);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }
}
