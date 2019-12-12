/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.fundamental.model.Mediopago;
import com.sisintegrados.generic.bean.GenericDepositoDet;
import com.sisintegrados.generic.bean.GenericMedioPago;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jjosu
 */
public class SvcDeposito extends Dao {

    private String miQuery;

    public SvcDeposito() {
    }

    public List<GenericDepositoDet> getDepositoByEstacion(Integer idestacion) {
        List<GenericDepositoDet> result = new ArrayList();
        result = null;
        try {
            miQuery = "select IDDEPOSITODET, MEDIOPAGO_ID, MONTO, COMENTARIOS, NOBOLETA "
                    + " FROM DEPOSITO_DET "
                    + " WHERE ESTACION_ID = " + idestacion;
            System.out.println("miQuery  " + miQuery);
            pst = getConnection().prepareStatement(miQuery);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                GenericMedioPago mp = new GenericMedioPago();
                mp.setMediopagoid(rst.getInt(2));
                result.add(new GenericDepositoDet(rst.getInt(1), mp, rst.getDouble(3), rst.getString(4), rst.getString(5)));
            }
            closePst();
            System.out.println("tamaño de result   --- " + result.size());
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public List<GenericDepositoDet> getDepositoByEstacion(String idestacion, String date) {
        List<GenericDepositoDet> result = new ArrayList();
        //result = null;

        try {
            miQuery = "select IDDEPOSITODET, MEDIOPAGO_ID, MONTO, COMENTARIOS, NOBOLETA "
                    + " FROM DEPOSITO_DET "
                    + " WHERE ESTACION_ID = " + idestacion + " and to_char(FECHA, 'YYYY-MM-DD') = '" + date + "'";
            System.out.println("MiQuery getDepositoByEstacion    " + miQuery);
            pst = getConnection().prepareStatement(miQuery);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                GenericMedioPago mp = new GenericMedioPago();
                mp.setMediopagoid(rst.getInt(2));
                result.add(new GenericDepositoDet(rst.getInt(1), mp, rst.getDouble(3), rst.getString(4), rst.getString(5)));
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
}
