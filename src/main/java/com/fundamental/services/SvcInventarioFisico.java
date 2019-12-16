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
    public List<ComInventarioFisico> getLubricantes(int brandId, Date fecha) {
        List<ComInventarioFisico> result = new ArrayList();
        String fecha1 = Constant.SDF_ddMMyyyy.format(fecha);
        String fecha2 = Constant.SDF_ddMMyyyy.format(fecha);
        fecha1 = fecha2.substring(3,5);
        fecha2 = fecha2.substring(6);   
        System.out.println("fecha1 "+fecha1);
        System.out.println("fecha2 "+fecha2);
        try {
//            Date fecha2 = recuperaFecha(countryId,brandId)
            query = "select rownum as numero,p.NOMBRE,p.PRESENTACION,lp.PRECIO, "
                    + "l.INV_FINAL, l.PAIS_ID, l.PRODUCTO_ID, " +
                    "(select INV_FINAL from INVENTARIO_FISICO_LUB where PRODUCTO_ID=p.PRODUCTO_ID and FECHA = to_date('01/10/2019','mm/yyyy')) as invFin, " +
                    "(select UNIDAD_FIS_TIENDA from INVENTARIO_FISICO_LUB where PRODUCTO_ID=p.PRODUCTO_ID and FECHA = to_date('01/10/2019','mm/yyyy')) as uniTienda, " +
                    "(select UNIDAD_FIS_BODEGA from INVENTARIO_FISICO_LUB where PRODUCTO_ID=p.PRODUCTO_ID and FECHA = to_date('01/10/2019','mm/yyyy')) as uniBodega, " +
                    "(select UNIDAD_FIS_PISTA from INVENTARIO_FISICO_LUB where PRODUCTO_ID=p.PRODUCTO_ID and FECHA = to_date('01/10/2019','mm/yyyy')) as uniPista, " +
                    "(select DIFERENCIA_INV from INVENTARIO_FISICO_LUB where PRODUCTO_ID=p.PRODUCTO_ID and FECHA = to_date('01/10/2019','mm/yyyy')) as difInv, " +
                    "(select COMENTARIO from INVENTARIO_FISICO_LUB where PRODUCTO_ID=p.PRODUCTO_ID and FECHA = to_date('01/10/2019','mm/yyyy')) as Comentario "
                    + "from COMPRA_VENTA_LUBRICANTE l, PRODUCTO p, LUBRICANTEPRECIO lp "
                    + "where l.PRODUCTO_ID = p.PRODUCTO_ID and l.PRODUCTO_ID = lp.PRODUCTO_ID "
                    + "and l.MARCA_ID="+brandId+" "
                    + "and FECHA = (select max(b.fecha) from COMPRA_VENTA_LUBRICANTE b where b.MARCA_ID="+brandId+" and "+fecha1+"=extract(MONTH from b.fecha)" 
                    + "AND "+fecha2+"=extract(YEAR from b.fecha))  order by FECHA desc ";
            System.out.println("query "+query);
            System.out.println("brandId "+brandId);
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            ComInventarioFisico inv;
            while (rst.next()) {    
                inv = new ComInventarioFisico(rst.getInt(7), rst.getInt(1), rst.getDouble(5),rst.getString(2), rst.getString(3), rst.getDouble(4));
                inv.setUnidad_fis_tienda(rst.getDouble(8));
                inv.setUnidad_fis_bodega(rst.getDouble(9));
                inv.setUnidad_fis_pista(rst.getDouble(10));
                inv.setDiferencia_inv(rst.getDouble(11));
                inv.setComentario(rst.getString(12));
                result.add(inv);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }
    
    public ComInventarioFisico insertCompra(ComInventarioFisico lub) {
        ComInventarioFisico result = new ComInventarioFisico();
        ResultSet rst = null;  
        try {
            int val = countLub(lub.getProducto_id(),lub.getFecha());
            if(val>0){
                miQuery = "UPDATE INVENTARIO_FISICO_LUB "
                    + "SET INV_FINAL=?,UNIDAD_FIS_TIENDA=?, UNIDAD_FIS_BODEGA=?,UNIDAD_FIS_PISTA=?, TOTAL_UNIDAD_FISICA=?, "
                    + "DIFERENCIA_INV=?, COMENTARIO=?, MODIFICADO_POR=?, MODIFICADO_EL=SYSDATE "
                    + "where PRODUCTO_ID=? and FECHA=to_date(?,'dd/mm/yyyy') ";
                pst = getConnection().prepareStatement(miQuery);
                pst.setDouble(1, lub.getInv_final());
                pst.setDouble(2, lub.getUnidad_fis_tienda());
                pst.setDouble(3, lub.getUnidad_fis_bodega());
                pst.setDouble(4, lub.getUnidad_fis_pista());
                pst.setDouble(5, lub.getTotal_unidad_fisica());
                pst.setDouble(6, lub.getDiferencia_inv());
                pst.setString(7, lub.getComentario());
                pst.setString(7, lub.getModificado_por());
                pst.setInt(8, lub.getProducto_id());
                pst.setString(9,Constant.SDF_ddMMyyyy.format(lub.getFecha()));
                pst.executeUpdate();
                result = lub;
            }else{
                miQuery = "INSERT INTO COMPRA_VENTA_LUBRICANTE(INVENTARIO_ID,PRODUCTO_ID,FECHA,INV_FINAL,UNIDAD_FIS_TIENDA,UNIDAD_FIS_BODEGA, "
                    + "UNIDAD_FIS_PISTA,TOTAL_UNIDAD_FISICA,DIFERENCIA_INV,COMENTARIO,CREADO_POR,CREADO_EL) "
                    + "VALUES(INVENTARIO_FISICO_LUB_SEQ.NEXTVAL, ?, ?, ?,?, ?, ?, ?, ?, SYSDATE)";
                pst = getConnection().prepareStatement(miQuery);
                pst.setInt(1, lub.getProducto_id());                
                pst.setDate(2, new java.sql.Date(lub.getFecha().getTime()));
                pst.setDouble(3, lub.getInv_final());
                pst.setDouble(4, lub.getUnidad_fis_tienda());
                pst.setDouble(5, lub.getUnidad_fis_bodega());
                pst.setDouble(6, lub.getUnidad_fis_pista());
                pst.setDouble(7, lub.getTotal_unidad_fisica());
                pst.setDouble(8, lub.getDiferencia_inv());
                pst.setString(9, lub.getComentario());
                pst.setString(10, lub.getCreado_por());
                pst.executeQuery();
                result = lub;
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
    
    public int countLub(int idProducto, Date fecha) {
        int result = 0;
        String dateString = Constant.SDF_ddMMyyyy.format(fecha);
        try {
            miQuery = "SELECT count(*) FROM INVENTARIO_FISICO_LUB where PRODUCTO_ID="+idProducto+" "
                    + "and trunc(FECHA) =  to_date('"+dateString+"','dd/mm/yyyy')";
            System.out.println("mi ueryryry "+miQuery);
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
