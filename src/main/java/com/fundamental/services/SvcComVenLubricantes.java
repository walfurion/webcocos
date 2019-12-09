/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.fundamental.utils.Constant;
import com.sisintegrados.generic.bean.ComVenLubricantes;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author m
 */
public class SvcComVenLubricantes extends Dao{
    private String query;
    
    public List<ComVenLubricantes> getComVenLub(int countryId, int brandId, int productId) {
        List<ComVenLubricantes> result = new ArrayList();
        try {
            query = "SELECT p.producto_id,p.nombre,m.id_marca, c.pais_id "
                    + "FROM lubricanteprecio l, pais c, producto p, marca m "
                    + "WHERE l.pais_id = c.pais_id AND p.id_marca = m.id_marca and p.TIPO_ID=2 "
                    + "AND l.producto_id = p.producto_id and p.ESTADO='A' AND l.pais_id = ? "
                    + "AND p.id_marca = ? and l.producto_id= ? ";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, countryId);
            pst.setInt(2, brandId);
            pst.setInt(3, productId);
            ResultSet rst = pst.executeQuery();
            ComVenLubricantes lub = new ComVenLubricantes();
            while (rst.next()) {                
                lub.setProductoId(rst.getInt(1));
                lub.setProductoNombre(rst.getString(2));
                lub.setMarcaId(rst.getInt(3));
                result.add(lub);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }
    
    public List<ComVenLubricantes> getComVenLubAnterior(int countryId, int brandId, int productId) {
        List<ComVenLubricantes> result = new ArrayList();
        try {
            query = "select l.PRODUCTO_ID, p.NOMBRE, l.FECHA, "
                    + "l.INV_INICIAL, l.COMPRA, l.VENTA, l.INV_FINAL "
                    + "from COMPRA_VENTA_LUBRICANTE l, PRODUCTO p "
                    + "AND l.producto_id = p.producto_id and p.ESTADO='A' AND l.pais_id = ? "
                    + "AND p.id_marca = ? and l.producto_id= ? ";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, countryId);
            pst.setInt(2, brandId);
            pst.setInt(3, productId);
            ResultSet rst = pst.executeQuery();
            ComVenLubricantes lub = new ComVenLubricantes();
            while (rst.next()) {                
                lub.setProductoId(rst.getInt(1));
                lub.setProductoNombre(rst.getString(2));
                lub.setMarcaId(rst.getInt(3));
                result.add(lub);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }
    
    public ComVenLubricantes insertLub(ComVenLubricantes lub) {
        ComVenLubricantes result = new ComVenLubricantes();
        ResultSet rst = null;
        try {
                miQuery = "INSERT INTO COMPRA_VENTA_LUBRICANTE(COMPRA_ID, PRODUCTO_ID, "
                        + "FECHA, INV_INICIAL, COMPRA, INV_FINAL, CREADO_POR, CREADO_EL) "
                        + "VALUES(COMPRA_VENTA_LUBRICANTE_SEQ.NEXTVAL, 1, SYSDATE, 28.00, 33.00, 22.00, 'jmeng', SYSDATE)";
                pst = getConnection().prepareStatement(miQuery);
//                pst.setInt(1, lub.getProductoId());
//                pst.setDate(2, (java.sql.Date) lub.getFecha());
//                pst.setDouble(3, lub.getInvInicial());
//                pst.setDouble(4, lub.getCompra());
//                pst.setDouble(5, lub.getInvfinal());
//                pst.setString(1, lub.getCreadopor());
                pst.executeQuery();
                result = lub;
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
    public ComVenLubricantes updateLub(ComVenLubricantes lub ) {
        ComVenLubricantes result = new ComVenLubricantes();
        ResultSet rst = null;
        try {
            miQuery = "UPDATE COMPRA_VENTA_LUBRICANTE "
                    + "SET INV_INICIAL=?,COMPRA=?, VENTA=?, INV_FINAL=?, MODIFICADO_POR=?, MODIFICADO_EL=SYSDATE "
                    + "where PRODUCTO_ID=?";
            pst = getConnection().prepareStatement(miQuery);
            pst.setDouble(1, lub.getInvInicial());
            pst.setDouble(2, lub.getCompra());
            pst.setDouble(3, lub.getVenta());
            pst.setDouble(4, lub.getInvfinal());
            pst.setString(5, lub.getModificadopor());
            pst.setInt(6, lub.getProductoId());
            pst.executeUpdate();
            result = lub;
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
    public int countLub(int idProducto, Date fecha) {
        int result = 0;
        String dateString = Constant.SDF_ddMMyyyy.format(fecha);
        try {
            miQuery = "SELECT count(*) FROM COMPRA_VENTA_LUBRICANTE where PRODUCTO_ID="+idProducto+" "
                    + "and FECHA >  to_date('"+dateString+"','dd/mm/yyyy') ";
            pst = getConnection().prepareStatement(miQuery);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                result = rst.getInt(1);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }
}
