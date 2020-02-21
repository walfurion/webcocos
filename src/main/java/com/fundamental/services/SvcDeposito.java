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
import com.vaadin.data.util.BeanContainer;
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

    public List<GenericBeanMedioPago> getAllMediosPago(boolean includeInactives, int idpais) {
        List<GenericBeanMedioPago> result = new ArrayList();
        try {
            String query = (includeInactives) ? "" : " AND m.estado = 'A' ";
            query = "SELECT m.mediopago_id, m.nombre "
                    + "FROM mediopago m, pais p "
                    + "WHERE m.pais_id = p.pais_id "
                    + " and and p.PAIS_ID = " + idpais
                    + " and m.tipo = 2 "
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

    public double getTasaCambio(int paisid) {
        double result = 0;
        String query = "";
        try {

            query = "select TASA FROM TASACAMBIO "
                    + "WHERE "
                    + "PAIS_ID = " + paisid;
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();

            while (rst.next()) {
                result = rst.getDouble(1);
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

    public int deleteDepositoByDate(int estacionid, Date dia) {
        int respuesta = 0;
        try {
            miQuery = "delete DEPOSITO_DET "
                    + "    where ESTACION_ID = ? "
                    + "    and FECHA = ? ";
            pst = getConnection().prepareStatement(miQuery);

            java.sql.Date sqlDateIni = new java.sql.Date(dia.getTime());

            /*Envio parametros necesarios*/
            pst.setString(1, String.valueOf(estacionid));
            pst.setDate(2, sqlDateIni);
            pst.executeUpdate();
            closePst();
            System.out.println("delete exitoso");
            respuesta = 1;
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (Exception ignore) {
            }
        }
        
        return respuesta;

    }

    public BeanContainer<Integer, GenericMedioPago> insertDepositoDetalle(BeanContainer<Integer, GenericMedioPago> bcrDeposito, double tasacambio, int estacionid, Date fechaCierre) {
        int respuesta = 0;
        String query = "";
        System.out.println("ingresa metodo guardar");
        java.sql.Date sqlDateIni = new java.sql.Date(fechaCierre.getTime());
        try {

            for (Integer itemId : bcrDeposito.getItemIds()) {
                //pst = getConnection().prepareStatement(query);
                double montoDolar = 0;
                double totalDolar = 0;
                montoDolar = bcrDeposito.getItem(itemId).getBean().getMontousd();
                if(montoDolar!=0){
                    totalDolar = montoDolar * tasacambio;
                    bcrDeposito.getItem(itemId).getBean().setMonto(totalDolar);
                }
                query = "INSERT INTO DEPOSITO_DET (IDDEPOSITODET, FECHA, ESTACION_ID, MEDIOPAGO_ID, NOBOLETA, COMENTARIOS, MONTO, MONTOUSD) "
                        + " VALUES (seq_deposito_det.nextval, "
                        + " to_date('" +sqlDateIni+ "', 'yyyy-mm-dd'),"
                        //+ sqlDateIni + ","
                        + estacionid + ","
                        + bcrDeposito.getItem(itemId).getBean().getMediopago().getMediopagoid() + ","
                        + "'" + bcrDeposito.getItem(itemId).getBean().getNoboleta() + "',"
                        + "'" + bcrDeposito.getItem(itemId).getBean().getComentarios() + "',"
                        + bcrDeposito.getItem(itemId).getBean().getMonto() + ","
                        + montoDolar + ")";                               
                System.out.println("query,.,.,. "+query);
                pst = getConnection().prepareStatement(query);
                pst.executeUpdate();
            }
            respuesta = 1;

        } catch (Exception e) {
            e.printStackTrace();
            respuesta = 0;
        }

        return bcrDeposito;
    }
    
    public List<GenericBeanMedioPago> getAllMediosPago(boolean includeInactives) {
        List<GenericBeanMedioPago> result = new ArrayList();
        try {
            String query = (includeInactives) ? "" : " AND m.estado = 'A' ";
            query = "SELECT m.mediopago_id, m.nombre "
                    + "FROM mediopago m, pais p "
                    + "WHERE m.pais_id = p.pais_id "
                    //+ " and and p.PAIS_ID = " + idpais
                    + " and m.tipo = 2 "
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
