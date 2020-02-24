/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.fundamental.model.Producto;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.fundamental.model.dto.DtoProducto;
import com.sisintegrados.daoimp.DaoImp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjosue
 */
public class SvcDetalleLubricantes extends DaoImp {

    private String query;

    public SvcDetalleLubricantes() {
    }

    public boolean CreaProductoDetalle(Integer idarqueocaja, BeanContainer<Integer, DtoProducto> bcrLubs, String usuario) throws SQLException {
        boolean result = false;
        PreparedStatement pst = null;

        /*Elimina antes de volver asignar*/
        try {
            query = "DELETE FROM arqueocaja_det_lub WHERE arqueocaja_id = " + idarqueocaja;
            pst = getConnection().prepareStatement(query);
            pst.executeUpdate();
            closePst();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            pst.close();
            closeConnections(); //asg
        }

        /*Asigna detalle productos al arqueo*/
        try {
            query = "INSERT INTO arqueocaja_det_lub (idpk,arqueocaja_id, producto_id, cantidad, precio, creado_por,creado_el) "
                    + "VALUES (arqueo_det_lub.nextval,?, ?, ?, ?, ? ,sysdate)";

            for (Integer itemId : bcrLubs.getItemIds()) {
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, idarqueocaja);
                pst.setInt(2, bcrLubs.getItem(itemId).getBean().getProducto().getProductoId());
                pst.setDouble(3, bcrLubs.getItem(itemId).getBean().getCantidad());
                pst.setDouble(4, bcrLubs.getItem(itemId).getBean().getValor());
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
                closeConnections(); //asg
            }
        }
        return result;
    }

    public BeanContainer<Integer, DtoProducto> getDetalleProducto(Integer idarqueocaja) {
        BeanContainer<Integer, DtoProducto> bcrLubs = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
        ResultSet rst = null;
        PreparedStatement pst = null;
        int id = 0;
        try {
            query = "   select a.arqueocaja_id,a.precio,a.cantidad, a.creado_por,a.creado_el, \n"
                    + "                    b.producto_id, b.nombre, b.id_marca \n"
                    + "                    from arqueocaja_det_lub a, producto b \n"
                    + "                    where a.producto_id = b.producto_id \n"
                    + "                    and a.arqueocaja_id = " + idarqueocaja;

            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                id++;
                DtoProducto dto = new DtoProducto();
                dto.setValor(rst.getDouble(2));
                dto.setCantidad(rst.getInt(3)); //ASG CANTIDAD
                dto.setTotal(dto.getCantidad() * dto.getValor()); //ASG TOTAL
                dto.setIdmarca(rst.getInt(8)); //ASG
                System.out.println("ID MARCA SVCDETALLELUBRICANTES " + rst.getInt(8));
                dto.setProducto(new Producto(rst.getInt(6), rst.getString(7), null, rst.getInt(8), null, rst.getDouble(2), null));
                bcrLubs.addItem(id, dto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg;
            } catch (SQLException ex) {
                Logger.getLogger(SvcDetalleLubricantes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return bcrLubs;
    }

}
