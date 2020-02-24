/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.generic.bean.GenericEstacion;
import com.sisintegrados.generic.bean.GenericRprControlMediosPago;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Mery
 */
public class SvcReporteControlMediosPago extends DaoImp {

    private String query;

    public ArrayList<GenericEstacion> getCheckEstacionesM(Integer idpais) {
        ArrayList<GenericEstacion> result = new ArrayList<GenericEstacion>();
        GenericEstacion gnestacion = new GenericEstacion();
        ResultSet rst = null;
        try {
            query = "Select estacion_id,nombre from estacion where pais_id =" + idpais;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
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
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public String getEstacion(Integer idestacion) {
        String result = "";
        ResultSet rst = null;
        
        query = "SELECT NOMBRE FROM ESTACION WHERE ESTACION_ID =" + idestacion;
        try {
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result = rst.getString(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public ArrayList<GenericRprControlMediosPago> getCtlMediosPago() {
        ArrayList<GenericRprControlMediosPago> result = new ArrayList<GenericRprControlMediosPago>();
        GenericRprControlMediosPago genctl = new GenericRprControlMediosPago();
        ResultSet rst = null;
        try {
            query = "Select FECHA, MEDIOPAGO_ID, LOTE, MONTO_BRUTO, COMISION, \n"
                    + "                MONTO_NETO, COMENTARIOS, CODIGO, ESTACION, BANCO, NODEPOSITO, MONTOCH, MONTOUSD, CLIENTE, COD_CLIENTE, TIPO_CLIENTE "
                    + "from CTRL_MEDIOS_PAGO order by fecha asc";
//            System.out.println("QUERY " + query);
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            Integer ii=0;
            while (rst.next()) {
                genctl = new GenericRprControlMediosPago(ii,rst.getDate(1), rst.getDouble(2),
                        rst.getDouble(3),
                        rst.getDouble(4),
                        rst.getDouble(5),
                        rst.getDouble(6),
                        rst.getString(7),
                        rst.getString(8),
                        rst.getString(9),
                        rst.getString(10),
                        rst.getString(11),
                        rst.getDouble(12),
                        rst.getDouble(13),
                        rst.getString(14),
                        rst.getString(15),
                        rst.getString(16));
                result.add(genctl);
                ii++;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public void generar_datacrt(Date fecha_ini, Date fecha_fin, String estaciones, String paisid) throws SQLException {
        String query = "{call REPORT_CTRL_MEDIO_PAGO (?,?,?,?)}";
        CallableStatement cst = getConnection().prepareCall(query);
//        String sfecha_ini = Constant.SDF_ddMMyyyy.format(fecha_ini);
//        String sfecha_fin = Constant.SDF_ddMMyyyy.format(fecha_fin);

        java.sql.Date sqlDateIni = new java.sql.Date(fecha_ini.getTime());
        java.sql.Date sqlDateFin = new java.sql.Date(fecha_fin.getTime());
//        System.out.println("PROCEDIMIENTO " + query);

        /*Envio parametros necesarios*/
        cst.setDate(1, sqlDateIni);
        cst.setDate(2, sqlDateFin);
        cst.setString(3, estaciones);
        cst.setString(4, paisid);
        cst.execute();
        cst.close();
        closeConnections();
    }

    public String getPaisId(Integer idestacion) {
        String result = "";
        ResultSet rst = null;
        query = "SELECT PAIS_ID FROM ESTACION WHERE ESTACION_ID = " + idestacion;
        try {
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result = rst.getString(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }
}
