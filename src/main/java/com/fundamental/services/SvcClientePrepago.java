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
public class SvcClientePrepago extends Dao {

    private String query;

    public SvcClientePrepago() {
    }

    public boolean CreaClienteDetalle(Integer idarqueocaja, BeanContainer<Integer, DtoProducto> bcrPrepaid, String usuario) throws SQLException {
        boolean result = false;
        PreparedStatement pst = null;

        /*Elimina antes de volver asignar*/
        try {
            query = "DELETE FROM arqueocaja_det_cxcprep WHERE arqueocaja_id = " + idarqueocaja;
            pst = getConnection().prepareStatement(query);
            pst.executeUpdate();
            closePst();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*Asigna detalle clientes prepago al arqueo*/
        try {
            query = "INSERT INTO arqueocaja_det_cxcprep (idpk,arqueocaja_id, cliente_id, monto, creado_por,creado_el) "
                    + "VALUES (arqueo_cli_prepago.nextval,?, ?, ?, ?,sysdate)";

            for (Integer itemId : bcrPrepaid.getItemIds()) {
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, idarqueocaja);
                pst.setInt(2, bcrPrepaid.getItem(itemId).getBean().getCliente().getClienteId());
                pst.setDouble(3, bcrPrepaid.getItem(itemId).getBean().getValor());
                pst.setString(4, usuario);
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

    public BeanContainer<Integer, DtoProducto> getDetallePrepago(Integer idarqueocaja) {
        BeanContainer<Integer, DtoProducto> bcrPrepaid = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
        ResultSet rst = null;
        PreparedStatement pst = null;
        int id = 0;
        try {
            query = "select a.arqueocaja_id,a.monto,a.creado_por,a.creado_el, "
                    + "b.cliente_id,b.codigo,b.nombre,b.estacion_id,b.estado,b.creado_por,b.creado_el,b.tipo,b.codigo_envoy,b.cedula_juridica "
                    + "from arqueocaja_det_cxcprep a, "
                    + "cliente b "
                    + "where a.cliente_id = b.cliente_id "
                    + "and a.arqueocaja_id = " + idarqueocaja;

            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                id++;
                DtoProducto dto = new DtoProducto();
                dto.setValor(rst.getDouble(2));
                dto.setCliente(new Cliente(rst.getInt(5),rst.getString(6),rst.getString(7),rst.getInt(8),rst.getString(9),rst.getString(10),rst.getDate(11),rst.getString(12),rst.getString(13),rst.getString(14)));
                bcrPrepaid.addItem(id, dto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bcrPrepaid;
    }
    
  
    public boolean CreaClienteDetalleCredito(Integer idarqueocaja, BeanContainer<Integer, DtoProducto> bcrClientes, String usuario) throws SQLException {
        boolean result = false;
        PreparedStatement pst = null;

        /*Elimina antes de volver asignar*/
        try {
            query = "DELETE FROM ARQUEOCAJA_DET_CLI_CR WHERE arqueocaja_id = " + idarqueocaja;
            pst = getConnection().prepareStatement(query);
            pst.executeUpdate();
            closePst();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*Asigna detalle clientes prepago al arqueo*/
        try {
            query = "INSERT INTO ARQUEOCAJA_DET_CLI_CR (idpk,arqueocaja_id, cliente_id, monto, creado_por,creado_el) "
                    + "VALUES (seq_ARQUEOCAJA_DET_CLI_CR.nextval,?, ?, ?, ?,sysdate)";

            for (Integer itemId : bcrClientes.getItemIds()) {
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, idarqueocaja);
                pst.setInt(2, bcrClientes.getItem(itemId).getBean().getCliente().getClienteId());
                pst.setDouble(3, bcrClientes.getItem(itemId).getBean().getValor());
                pst.setString(4, usuario);
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
    
    public BeanContainer<Integer, DtoProducto> getDetalleCredito(Integer idarqueocaja) {
        BeanContainer<Integer, DtoProducto> bcrClientes = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
        ResultSet rst = null;
        PreparedStatement pst = null;
        int id = 0;
        try {
            query = "select a.arqueocaja_id,a.monto,a.creado_por,a.creado_el, "
                    + "b.cliente_id,b.codigo,b.nombre,b.estacion_id,b.estado,b.creado_por,b.creado_el,b.tipo,b.codigo_envoy,b.cedula_juridica "
                    + "from ARQUEOCAJA_DET_CLI_CR a, "
                    + "cliente b "
                    + "where a.cliente_id = b.cliente_id "
                    + "and a.arqueocaja_id = " + idarqueocaja;

            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                id++;
                DtoProducto dto = new DtoProducto();
                dto.setValor(rst.getDouble(2));
                dto.setCliente(new Cliente(rst.getInt(5),rst.getString(6),rst.getString(7),rst.getInt(8),rst.getString(9),rst.getString(10),rst.getDate(11),rst.getString(12),rst.getString(13),rst.getString(14)));
                bcrClientes.addItem(id, dto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bcrClientes;
    }   
}
