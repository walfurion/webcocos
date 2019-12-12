/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.fundamental.utils.Constant;
import com.sisintegrados.generic.bean.GenericEstacion;
import com.sisintegrados.generic.bean.GenericMTD;
import com.sisintegrados.generic.bean.GenericRprControlMediosPago;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.themes.ValoTheme;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Mery
 */
public class SvcReporteControlMediosPago extends Dao {

    private String query;

    public ArrayList<GenericEstacion> getCheckEstacionesM(Integer idpais) {
        ArrayList<GenericEstacion> result = new ArrayList<GenericEstacion>();
        GenericEstacion gnestacion = new GenericEstacion();
        try {
            query = "Select estacion_id,nombre from estacion where pais_id ="+idpais;
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
    
    public ArrayList<GenericRprControlMediosPago> getCtlMediosPago() {
        ArrayList<GenericRprControlMediosPago> result = new ArrayList<GenericRprControlMediosPago>();
        GenericRprControlMediosPago genctl = new GenericRprControlMediosPago();
        try {
            query = "Select FECHA, LOTE, MONTO_BRUTO, COMISION, \n" +
"                MONTO_NETO, COMENTARIOS "
                    + "from CTRL_Medios_pago order by fecha asc";
            System.out.println("QUERY "+query);
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                genctl = new GenericRprControlMediosPago(rst.getDate(1), rst.getDouble(2),
                        rst.getDouble(3),
                        rst.getDouble(4),
                        rst.getDouble(5),
                        rst.getString(6));
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

    public void generar_datacrt(Date fecha_ini, Date fecha_fin, int estaciones, String paisid) throws SQLException {
        String query = "{call rep (?,?,?,?)}";
        CallableStatement cst = getConnection().prepareCall(query);
String sfecha_ini = Constant.SDF_ddMMyyyy.format(fecha_ini);
String sfecha_fin = Constant.SDF_ddMMyyyy.format(fecha_fin);

//        java.sql.Date sqlDateIni = new java.sql.Date(fecha_ini.getTime());
       // java.sql.Date sqlDateFin = new java.sql.Date(fecha_fin.getTime());
        
        System.out.println("PROCEDIMIENTO "+ query);

        /*Envio parametros necesarios*/
        cst.setString(1, sfecha_ini);
        cst.setString(2, sfecha_fin);
        cst.setInt(3, estaciones);
        cst.setString(4, paisid);
        cst.execute();
    }

  }
