/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.LitroCalibracion;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Allan G.
 */
public class SvcRepCuadrePistero extends Dao {

    private String query;

    public String getBombaByTurnoEmpleado(Integer turnoid, Integer empleadid) {
        String bomba = "";
        ResultSet rst = null;
        try {
            query = "Select distinct(substr(b.nombre,0,8)) Bomba \n"
                    + "from turno_empleado_bomba a,\n"
                    + "     bomba b\n"
                    + "where a.bomba_id = b.bomba_id \n"
                    + "and a.turno_id = \n" + turnoid
                    + " and a.empleado_id = " + empleadid;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                bomba = rst.getString(1);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return bomba;
    }

    public ArrayList<LitroCalibracion> getTotCalibraTurnoEmpleado(Integer turnoid, Integer empleadid) {
        ArrayList<LitroCalibracion> calibracion = new ArrayList<LitroCalibracion>();
        ResultSet rst = null;
        try {
            query = "select d.nombre,\n"
                    + "       c.precio,\n"
                    + "       nvl(sum(calibracion),0) caliLT, \n"
                    + "       nvl(sum(calibracion),0) * c.precio caliVT\n"
                    + "from lectura a,\n"
                    + "     lectura_detalle b,\n"
                    + "     precio c,\n"
                    + "     producto d\n"
                    + "where a.lectura_id = b.lectura_id\n"
                    + "and b.producto_id  = c.producto_id\n"
                    + "and c.turno_id = a.turno_id\n"
                    + "and d.producto_id = c.producto_id\n"
                    + "and b.tipo = 'E'\n"
                    + "and a.turno_id = \n" + turnoid
                    + " and a.empleado_id = \n" + empleadid
                    + "group by d.nombre,c.precio";

            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                calibracion.add(new LitroCalibracion(rst.getString(1), rst.getDouble(2), rst.getDouble(3), rst.getDouble(4)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return calibracion;
    }
}
