/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.fundamental.model.Cliente;
import com.fundamental.model.Dia;
import com.fundamental.model.Precio;
import com.fundamental.model.Producto;
import com.fundamental.model.Turno;
import com.fundamental.model.dto.DtoProducto;
import com.sisintegrados.generic.bean.EmpleadoBombaTurno;
import com.sisintegrados.generic.bean.GenericTarjeta;
import com.sisintegrados.generic.bean.Tarjeta;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Allan G.
 */
public class SvcTarjetaCredito extends Dao {

    private String query;

    public SvcTarjetaCredito() {
    }

    public List<Tarjeta> getTarjetas() {
        List<Tarjeta> result = new ArrayList();
        try {
            miQuery = "select tarjeta_id, nombre from TARJETA";
            pst = getConnection().prepareStatement(miQuery);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Tarjeta(rst.getInt(1), rst.getString(2)));
            }
            closePst();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public boolean CreaDetalleTarjetaCredito(Integer idarqueocaja, BeanContainer<Integer, GenericTarjeta> bcrCreditC, String usuario) throws SQLException {
        boolean result = false;
        PreparedStatement pst = null;

        /*Elimina antes de volver asignar*/
        try {
            query = "DELETE FROM arqueocaja_tc WHERE arqueocaja_id = " + idarqueocaja;
            pst = getConnection().prepareStatement(query);
            pst.executeUpdate();
            closePst();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*Asigna detalle clientes prepago al arqueo*/
        try {
            query = "INSERT INTO arqueocaja_tc (arqueocaja_id, tarjeta_id, lote, monto, creado_por,creado_el) "
                    + "VALUES (?,?, ?, ?, ?,sysdate)";

            for (Integer itemId : bcrCreditC.getItemIds()) {
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, idarqueocaja);
                pst.setInt(2, bcrCreditC.getItem(itemId).getBean().getTarjeta().getTarjeta_Id());
                pst.setInt(3, bcrCreditC.getItem(itemId).getBean().getLote());
                pst.setDouble(4, bcrCreditC.getItem(itemId).getBean().getMonto());
                pst.setString(5, usuario);
                pst.executeUpdate();
                closePst();
            }
            result = true;
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
        return result;
    }

    public BeanContainer<Integer, GenericTarjeta> getDetalleTarjetaCredito(Integer idarqueocaja) {
        BeanContainer<Integer, GenericTarjeta> bcrTarjetaCredito = new BeanContainer<Integer, GenericTarjeta>(GenericTarjeta.class);
        ResultSet rst = null;
        PreparedStatement pst = null;
        int id = 0;
        try {
            query = "select a.NOMBRE,a.TARJETA_ID, "
                    + "b.LOTE, b.MONTO "
                    + "from TARJETA a, "
                    + "arqueocaja_tc b "
                    + "where a.TARJETA_ID = b.TARJETA_ID "
                    + "and b.ARQUEOCAJA_ID = " + idarqueocaja;

            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                id++;
                GenericTarjeta dto = new GenericTarjeta();
                dto.setLote(rst.getInt(3));
                dto.setMonto(rst.getDouble(4));
                dto.setTarjeta(new Tarjeta(rst.getInt(2), rst.getString(1)));
                bcrTarjetaCredito.addItem(id, dto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bcrTarjetaCredito;
    }
}
