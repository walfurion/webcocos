/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.LitroCalibracion;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Allan G.
 */
public class SvcRepCuadrePistero extends DaoImp {

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
            rst.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
                closePst();
                closeConnections(); //asg
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
            rst.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
            closeConnections(); //asg
        }
        return calibracion;
    }

    public Integer getTolerancia(Integer idparametro) {
        Integer result = 0;
        ResultSet rst = null;
        try {
            query = "Select valor from parametro where parametro_id = " + idparametro;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result = rst.getInt(1);
            }
            rst.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
                closePst();
                closeConnections(); //asg
        }

        return result;
    }

    public Integer getIdRol(Integer iduser) {
        Integer result = 0;
        ResultSet rst = null;
        try {
            query = "select b.rol_id,username \n"
                    + "from usuario a, \n"
                    + "     rol_usuario b \n"
                    + "where a.USUARIO_ID = b.USUARIO_ID \n"
                    + "and a.usuario_id = " + iduser;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result = rst.getInt(1);
            }
            rst.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
                closePst();
                closeConnections(); //asg
        }

        return result;
    }

    public Integer getStadoDia(Date fecha, Integer idestacion) {
        Integer result = 0;
        ResultSet rst = null;

        try {
            java.sql.Date sqlDateIni = new java.sql.Date(fecha.getTime());

            query = "Select count(*) cantidad\n"
                    + "from dia \n"
                    + "where estado_id = 2 \n"
                    + " and fecha = TO_DATE('" + sqlDateIni + "','yyyy/mm/dd') and estacion_id = " + idestacion;

            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result = rst.getInt(1);
            }
            rst.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
                closePst();
                closeConnections(); //asg
        }
        return result;
    }
}
