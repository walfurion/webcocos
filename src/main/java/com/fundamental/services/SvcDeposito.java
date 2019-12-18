/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.fundamental.model.Mediopago;
import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.GenericBeanMedioPago;
import com.sisintegrados.generic.bean.GenericDepositoDet;
import com.sisintegrados.generic.bean.GenericMedioPago;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jjosu
 */
public class SvcDeposito extends Dao {

    private String miQuery;

    public SvcDeposito() {
    }

//    public List<GenericDepositoDet> getDepositoByEstacion(Integer idestacion) {
//        List<GenericDepositoDet> result = new ArrayList();
//        result = null;
//        try {
//            miQuery = "select IDDEPOSITODET, MEDIOPAGO_ID, MONTO, COMENTARIOS, NOBOLETA "
//                    + " FROM DEPOSITO_DET "
//                    + " WHERE ESTACION_ID = " + idestacion;
//            System.out.println("miQuery  " + miQuery);
//            pst = getConnection().prepareStatement(miQuery);
//            ResultSet rst = pst.executeQuery();
//            while (rst.next()) {
//                GenericMedioPago mp = new GenericMedioPago();
//                mp.setMediopagoid(rst.getInt(2));
//                result.add(new GenericDepositoDet(rst.getInt(1), mp, rst.getDouble(3), rst.getString(4), rst.getString(5)));
//            }
//            closePst();
//            System.out.println("tamaño de result   --- " + result.size());
//        } catch (Exception exc) {
//            exc.printStackTrace();
//        } finally {
//            closePst();
//        }
//        return result;
//    }
    public List<GenericMedioPago> getDepositoByEstacion(String idestacion, Date date) {
        List<GenericMedioPago> result = new ArrayList();

        try {
            miQuery = "SELECT A.IDDEPOSITODET, A.MONTO, A.COMENTARIOS, A.NOBOLETA,\n"
                    + "        B.ESTACION_ID,B.NOMBRE,\n"
                    + "        C.MEDIOPAGO_ID,C.NOMBRE,A.MONTOUSD\n"
                    + "                    FROM DEPOSITO_DET A,\n"
                    + "                         ESTACION B,\n"
                    + "                         MEDIOPAGO C\n"
                    + "                    WHERE A.ESTACION_ID = B.ESTACION_ID\n"
                    + "                    AND A.MEDIOPAGO_ID = C.MEDIOPAGO_ID \n"
                    + "                    AND A.ESTACION_ID = ? and A.fecha = ?";
            pst = getConnection().prepareStatement(miQuery);

            java.sql.Date sqlDateIni = new java.sql.Date(date.getTime());

            /*Envio parametros necesarios*/
            pst.setString(1, idestacion);
            pst.setDate(2, sqlDateIni);
            ResultSet rst = pst.executeQuery();

            while (rst.next()) {
                result.add(new GenericMedioPago(rst.getInt(1), new Estacion(rst.getInt(5), rst.getString(6)), new GenericBeanMedioPago(rst.getInt(7), rst.getString(8)), rst.getString(4), rst.getString(3), rst.getDouble(2), rst.getDouble(9)));
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

//    public Tanque doActionTanque(String action, Tanque tanque) {
//        Tanque result = new Tanque();
//        try {
//            if (action.equals(Dao.ACTION_ADD)) {
//                System.out.println("ingresa metodo guardar");
//                query = "SELECT seq_tanque.nextval FROM DUAL";
//                pst = getConnection().prepareStatement(query);
//                ResultSet rst = pst.executeQuery();
//                tanque.setIdtanque((rst.next()) ? rst.getInt(1) : 0);
//                closePst();
//                query = "INSERT INTO tanque (idtanque, producto_id, estacion_id, descripcion, usuario_creacion, fecha_creacion) "
//                        + "VALUES (?, ?, ?, ?, ?, SYSDATE)";
//                
//                pst = getConnection().prepareStatement(query);
//                pst.setObject(1, tanque.getIdtanque());
//                pst.setObject(2, tanque.getProducto().getProductoId());
//                pst.setObject(3, tanque.getEstacion().getEstacionId());
//                pst.setObject(4, tanque.getDescripcion());
//                pst.setObject(5, tanque.getUsuarioCreacion());
//                //pst.setObject(6, tanque.getFechaCreacion());
//
//                pst.executeUpdate();
//                result = tanque;
//            } else if (action.equals(Dao.ACTION_UPDATE)) {
//                System.out.println("ingresa metodo actualizar");
//                query = "UPDATE tanque "
//                        + "SET producto = ?, estacion = ?, descripcion = ?"
//                        + "  usuario_modificacion = ?, fecha_modificacion = SYSDATE"
//                        + "WHERE idtanque = ?";
//                pst = getConnection().prepareStatement(query);
//                pst.setObject(1, tanque.getProducto().getProductoId());
//                pst.setObject(2, tanque.getEstacion().getEstacionId());
//                pst.setObject(3, tanque.getDescripcion());
//                pst.setObject(4, tanque.getUsuarioModificacion());
//                pst.setObject(5, tanque.getIdtanque());
//
//                pst.executeUpdate();
//                result = tanque;
//            }
//        } catch (Exception exc) {
//            // result.setDescError(exc.getMessage());
//            exc.printStackTrace();
//        } finally {
//            closePst();
//        }
//        return result;
//    }
    public List<Estacion> getAllEstaciones(boolean includeInactive) {
        List<Estacion> result = new ArrayList();
        //String statusName;
        ResultSet rst = null;
        try {
            miQuery = (includeInactive) ? "" : " AND e.estado = 'A' ";
            miQuery = "SELECT e.estacion_id, e.nombre "
                    + "FROM estacion e, pais p "
                    + "WHERE e.pais_id = p.pais_id "
                    + miQuery
                    + " ORDER BY p.nombre ";
            pst = getConnection().prepareStatement(miQuery);
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

    public List<GenericBeanMedioPago> getAllMediosPago(boolean includeInactives) {
        List<GenericBeanMedioPago> result = new ArrayList();
        try {
            String query = (includeInactives) ? "" : " AND m.estado = 'A' ";
            query = "SELECT m.mediopago_id, m.nombre "
                    + "FROM mediopago m, pais p "
                    + "WHERE m.pais_id = p.pais_id "
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
