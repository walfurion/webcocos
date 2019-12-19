/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.GenericBeanMedioPago;
import com.sisintegrados.generic.bean.GenericDetalleFM;
import com.sisintegrados.generic.bean.GenericLote;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Allan G.
 */
public class SvcDetalleTcClientes extends Dao {

    private String query;

    public List<GenericDetalleFM> getDetalleByMedioPago(Integer idestacion, Date date, Integer mediopagoid) {
        List<GenericDetalleFM> result = new ArrayList();

        try {
            query = "SELECT A.IDDET,A.CLIENTE,A.VENTA,A.COMENTARIO,       \n"
                    + "       B.ESTACION_ID,B.NOMBRE,\n"
                    + "       C.MEDIOPAGO_ID,C.NOMBRE,\n"
                    + "       D.LOTE_ID,D.LOTE\n"
                    + "  FROM TARJETA_DETALLE_FM  A, ESTACION B, MEDIOPAGO C,\n"
                    + "       (SELECT x.LOTE LOTE_ID, x.lote FROM arqueocaja_tc x, mediopago y WHERE x.TARJETA_ID = y.MEDIOPAGO_ID AND x.tarjeta_id = ? AND x.arqueocaja_id IN\n"
                    + "                          (SELECT arqueocaja_id FROM arqueocaja WHERE turno_id IN \n"
                    + "                                      (SELECT turno_id FROM turno WHERE estacion_id = ? AND fecha =  ?))\n"
                    + "         GROUP BY x.tarjeta_id, y.NOMBRE, x.lote) D\n"
                    + " WHERE     A.IDESTACION = B.ESTACION_ID\n"
                    + "       AND A.IDMEDIOPAGO = C.MEDIOPAGO_ID\n"
                    + "       AND A.LOTE = D.LOTE_ID\n"
                    + "       AND C.MEDIOPAGO_ID = ?"
                    + "       AND A.IDESTACION = ?\n"
                    + "       AND A.fecha = ?";
            pst = getConnection().prepareStatement(query);

            java.sql.Date sqlDateIni = new java.sql.Date(date.getTime());

            /*Envio parametros necesarios*/
            pst.setInt(1, mediopagoid);
            pst.setInt(2, idestacion);
            pst.setDate(3, sqlDateIni);
            pst.setInt(4, mediopagoid);
            pst.setInt(5, idestacion);
            pst.setDate(6, sqlDateIni);
            ResultSet rst = pst.executeQuery();

            while (rst.next()) {
                result.add(new GenericDetalleFM(rst.getInt(1), new Estacion(rst.getInt(5), rst.getString(6)), new GenericBeanMedioPago(rst.getInt(7), rst.getString(8)), new GenericLote(rst.getInt(9), rst.getInt(10)), rst.getString(2), rst.getDouble(3), rst.getString(4)));
            }
            closePst();
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

    public List<Estacion> getAllEstaciones(boolean includeInactive,Integer paisid) {
        List<Estacion> result = new ArrayList();
        //String statusName;
        ResultSet rst = null;
        try {
            query = (includeInactive) ? "" : " AND e.estado = 'A' ";
            query = "SELECT e.estacion_id, e.nombre "
                    + "FROM estacion e, pais p "
                    + "WHERE e.pais_id = p.pais_id "
                    + "AND e.pais_id = "+paisid
                    + query
                    + " ORDER BY p.nombre ";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            Estacion estacion;
            while (rst.next()) {
                estacion = new Estacion(rst.getInt(1), rst.getString(2));
                result.add(estacion);
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
        return result;
    }

    public List<GenericLote> getAllLotesbyMedioPago(Integer mediopagoid, Integer estacionid, Date date) {
        List<GenericLote> result = new ArrayList();
        //String statusName;
        ResultSet rst = null;
        try {
            query = "  SELECT a.LOTE LOTE_ID, a.lote\n"
                    + "    FROM arqueocaja_tc a, mediopago b\n"
                    + "   WHERE     a.TARJETA_ID = b.MEDIOPAGO_ID\n"
                    + "         AND a.tarjeta_id = ?\n"
                    + "         AND a.arqueocaja_id IN\n"
                    + "                 (SELECT arqueocaja_id\n"
                    + "                    FROM arqueocaja\n"
                    + "                   WHERE turno_id IN\n"
                    + "                             (SELECT turno_id\n"
                    + "                                FROM turno\n"
                    + "                               WHERE     estacion_id = ?\n"
                    + "                                     AND fecha = ?))\n"
                    + "GROUP BY a.tarjeta_id, b.NOMBRE, a.lote";

            pst = getConnection().prepareStatement(query);

            java.sql.Date sqlDateIni = new java.sql.Date(date.getTime());

            /*Envio parametros necesarios*/
            pst.setInt(1, mediopagoid);
            pst.setInt(2, estacionid);
            pst.setDate(3, sqlDateIni);

            rst = pst.executeQuery();

            GenericLote lote;
            while (rst.next()) {
                lote = new GenericLote(rst.getInt(1), rst.getInt(2));
                result.add(lote);
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
        return result;
    }

    public List<GenericBeanMedioPago> getAllMediosPago(boolean includeInactives,Integer paisid) {
        List<GenericBeanMedioPago> result = new ArrayList();
        try {
            query = (includeInactives) ? "" : " AND m.estado = 'A' ";
            query = "SELECT m.mediopago_id, m.nombre "
                    + "FROM mediopago m, pais p "
                    + "WHERE m.pais_id = p.pais_id "
                    + " AND m.pais_id = "+paisid
                    + query
                    + " ORDER BY m.nombre";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            GenericBeanMedioPago mediopago;
            while (rst.next()) {
                mediopago = new GenericBeanMedioPago(rst.getInt(1), rst.getString(2));
                result.add(mediopago);
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
