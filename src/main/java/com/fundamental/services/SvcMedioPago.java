package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.fundamental.model.Mediopago;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.generic.bean.GenericMedioPago;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Henry Barrientos
 */
public class SvcMedioPago extends DaoImp {

    private String query;

    public List<String[]> getMediospagoReporte(Date fechaInicial, Date fechaFinal, Integer paisId) {
        List<String[]> result = new ArrayList();
        ResultSet rst = null;
        try {
            SimpleDateFormat sdf_ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
            query = (paisId != null) ? " AND e.pais_id = " + paisId : "";
            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy'), e.codigo, e.bu, e.deposito, e.nombre, m.mediopago_id, m.nombre, SUM(ad.monto) "
                    + "FROM arqueocaja_detalle ad, mediopago m, arqueocaja a, estacion e, dia d, turno t "
                    + "WHERE ad.mediopago_id = m.mediopago_id AND a.estacion_id = e.estacion_id AND a.arqueocaja_id = ad.arqueocaja_id "
                    + "AND t.turno_id = a.turno_id AND t.fecha = d.fecha AND d.estacion_id = e.estacion_id "
                    + "AND d.fecha BETWEEN TO_DATE('" + sdf_ddmmyyyy.format(fechaInicial) + "', 'dd/mm/yyyy') AND TO_DATE('" + sdf_ddmmyyyy.format(fechaFinal) + "', 'dd/mm/yyyy') "
                    + query
                    + " GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), e.codigo, e.bu, e.deposito, e.nombre, m.mediopago_id, m.nombre "
                    //                    + "ORDER BY 1, e.nombre"
                    + " UNION "
                    + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy'), e.codigo, e.bu, e.deposito, e.nombre, m.mediopago_id, m.nombre, SUM(ef.monto) "
                    + "FROM efectivo ef, mediopago m, arqueocaja a, estacion e, dia d, turno t "
                    + "WHERE ef.mediopago_id = m.mediopago_id AND a.estacion_id = e.estacion_id AND a.arqueocaja_id = ef.arqueocaja_id "
                    + "AND t.turno_id = a.turno_id AND t.fecha = d.fecha AND d.estacion_id = e.estacion_id "
                    + "AND d.fecha BETWEEN TO_DATE('" + sdf_ddmmyyyy.format(fechaInicial) + "', 'dd/mm/yyyy') AND TO_DATE('" + sdf_ddmmyyyy.format(fechaFinal) + "', 'dd/mm/yyyy') "
                    + query
                    + " GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), e.codigo, e.bu, e.deposito, e.nombre, m.mediopago_id, m.nombre "
                    + "ORDER BY 1, 5"
                    ;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new String[]{
                    rst.getString(1), //fecha
                    rst.getString(2), //codigoEstacion
                    rst.getString(3), //BU
                    rst.getString(4), //deposito
                    rst.getString(5), //estacionNombre
                    rst.getString(6), //mediopagoId
                    rst.getString(7), //mediopagoNombre
                    rst.getString(8) //monto
                });
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                closePst();
                closeConnections(); //asg
            } catch (SQLException ex) {
            }
        }
        return result;
    }

    public List<String[]> getVolumenesReporte(Date fechaInicial, Date fechaFinal, Integer paisId) {
        List<String[]> result = new ArrayList();
        ResultSet rst = null;
        try {
            SimpleDateFormat sdf_ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
            query = (paisId != null) ? " AND e.pais_id = " + paisId : "";
            query = "SELECT e.codigo, e.bu, e.deposito, e.nombre, TO_CHAR(d.fecha, 'dd/mm/yyyy'), p.codigo_num, p.codigo, p.nombre, "
                    + "     SUM(ld.lectura_final - ld.lectura_inicial - ld.calibracion) vol, "
                    + "     SUM(pre.precio * (ld.lectura_final - ld.lectura_inicial - ld.calibracion)) monto "
                    + "FROM dia d, turno t, lectura l, lectura_detalle ld,    estacion e, producto p, precio pre "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id "
                    + "AND t.estacion_id = e.estacion_id AND ld.producto_id = p.producto_id "
                    + "AND pre.turno_id = t.turno_id AND pre.producto_id = p.producto_id AND pre.tipodespacho_id = ld.tipodespacho_id "
                    + "AND d.fecha >= TO_DATE('" + sdf_ddmmyyyy.format(fechaInicial) + "', 'dd/mm/yyyy') AND d.fecha <= TO_DATE('" + sdf_ddmmyyyy.format(fechaFinal) + "', 'dd/mm/yyyy') "
                    + query
                    + " GROUP BY e.codigo, e.bu, e.deposito, e.nombre, d.fecha, p.codigo_num, p.codigo, p.nombre "
                    //                    + "ORDER BY e.nombre, d.fecha, p.nombre DESC";
                    + " UNION "
                    + "SELECT e.codigo, e.bu, e.deposito, e.nombre, TO_CHAR(d.fecha, 'dd/mm/yyyy'), p.producto_id, '', p.nombre, 0, SUM(ap.monto) "
                    + "FROM arqueocaja_producto ap, arqueocaja a, estacion e, dia d, turno t, producto p "
                    + "WHERE a.estacion_id = e.estacion_id AND a.arqueocaja_id = ap.arqueocaja_id AND t.turno_id = a.turno_id AND t.fecha = d.fecha AND d.estacion_id = e.estacion_id AND p.producto_id = ap.producto_id "
                    + "AND d.fecha >= TO_DATE('" + sdf_ddmmyyyy.format(fechaInicial) + "', 'dd/mm/yyyy') AND d.fecha <= TO_DATE('" + sdf_ddmmyyyy.format(fechaFinal) + "', 'dd/mm/yyyy') "
                    + query
                    + " GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), e.codigo, e.bu, e.deposito, e.nombre, p.producto_id, p.nombre "
                    + "ORDER BY 4, 5, 8 DESC"
                    ;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new String[]{
                    rst.getString(1), //
                    rst.getString(2), //
                    rst.getString(3), //
                    rst.getString(4), //
                    rst.getString(5), //dia
                    rst.getString(6), //
                    rst.getString(7), //
                    rst.getString(8), //
                    rst.getString(9), //volumen
                    rst.getString(10) //monto
                });
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                closePst();
                closeConnections(); //asg
            } catch (SQLException ex) {
            }
        }
        return result;
    }
}
