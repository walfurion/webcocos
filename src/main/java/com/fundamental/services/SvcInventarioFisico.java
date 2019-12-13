/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.fundamental.utils.Constant;
import com.sisintegrados.generic.bean.ComInventarioFisico;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author m
 */
public class SvcInventarioFisico extends Dao {
    private String query;
    public List<ComInventarioFisico> getLubricantes(int countryId, int brandId, Date fecha) {
        List<ComInventarioFisico> result = new ArrayList();
        
        try {
//            Date fecha2 = recuperaFecha(countryId,brandId)
            query = "select rownum as numero,p.NOMBRE,p.PRESENTACION,lp.PRECIO, "
                    + "l.INV_FINAL, l.PAIS_ID, l.PRODUCTO_ID "
                    + "from COMPRA_VENTA_LUBRICANTE l, PRODUCTO p, LUBRICANTEPRECIO lp "
                    + "where l.PRODUCTO_ID = p.PRODUCTO_ID and l.PRODUCTO_ID = lp.PRODUCTO_ID "
                    + "AND l.PAIS_ID=? and l.MARCA_ID=? ";
            System.out.println("query "+query);
            System.out.println("countryId "+countryId);
            System.out.println("brandId "+brandId);
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, countryId);
            pst.setInt(2, brandId);
//            pst.setString(3, Constant.SDF_ddMMyyyy.format(fecha));
            ResultSet rst = pst.executeQuery();
            ComInventarioFisico inv;
            while (rst.next()) {    
                inv = new ComInventarioFisico(rst.getInt(7), rst.getInt(1), rst.getDouble(5),rst.getString(2), rst.getString(3), rst.getDouble(4));
                result.add(inv);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }
    private int recuperaFecha(int paisId,int marcaId,int productId, Date fecha){
        ResultSet rst = null;
        int valor = 0;
            try{        
                query = "select FECHA from COMPRA_VENTA_LUBRICANTE "
                      + "where PAIS_ID=? and MARCA_ID=? and PRODUCTO_ID=?  "
                      + "and FECHA >= trunc((to_date(?,'dd/mm/yyyy')),'month') and FECHA <= last_day(to_date(?,'dd/mm/yyyy')) "
                      + "and rownum=1 order by COMPRA_ID desc ";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, productId);
                pst.setInt(2, marcaId);
                pst.setInt(3, productId);
                pst.setString(4, Constant.SDF_ddMMyyyy.format(fecha));
                pst.setString(5, Constant.SDF_ddMMyyyy.format(fecha));
                rst = pst.executeQuery();            
                while (rst.next()) {         
                    valor = rst.getInt(1);
                }
            }catch(Exception exc){
                exc.printStackTrace();
            }
        return valor;
    }
}
