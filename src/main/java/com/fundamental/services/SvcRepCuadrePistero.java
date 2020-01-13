/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.sisintegrados.generic.bean.Estacion;
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

    public Integer getTotCalibraTurnoEmpleado(Integer turnoid, Integer empleadid) {
        Integer total = 0;
        ResultSet rst = null;
        try {
            query = "select nvl(sum(calibracion),0) Calibracion \n"
                    + "from lectura a,\n"
                    + "     lectura_detalle b\n"
                    + "where a.lectura_id = b.lectura_id \n"
                    + "and b.tipo = 'E' \n"
                    + "and a.turno_id = "+turnoid
                    + " and a.empleado_id = "+empleadid;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                total = rst.getInt(1);
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
        return total;
    }
}
