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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author m
 */
public class SvcComVenLubricantes extends Dao{
    private String query;    
    
    public List<ComVenLubricantes> getComVenLub(int countryId, int brandId, int productId, Date fecha) {
        List<ComVenLubricantes> result = new ArrayList();
        ResultSet rst = null;        
        Calendar ayer = Calendar.getInstance();
        ayer.setTime(fecha);
        ayer.add(Calendar.DATE, -1);
        try {                        
            query = "SELECT p.producto_id,p.nombre,m.id_marca, c.pais_id, c.PAIS_ID "
                    + "FROM lubricanteprecio l, pais c, producto p, marca m "
                    + "WHERE l.pais_id = c.pais_id AND p.id_marca = m.id_marca and p.TIPO_ID=2 "
                    + "AND l.producto_id = p.producto_id and p.ESTADO='A' AND l.pais_id = ? "
                    + "AND p.id_marca = ? and l.producto_id= ? ";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, countryId);
            pst.setInt(2, brandId);
            pst.setInt(3, productId);
            rst = pst.executeQuery();
            Double valFinal = valorFinal(countryId, brandId, productId, ayer.getTime());
            System.out.println("valFinal "+valFinal);
            ComVenLubricantes lub = new ComVenLubricantes();
            while (rst.next()) {                
                lub.setProductoId(rst.getInt(1));
                lub.setProductoNombre(rst.getString(2));
                lub.setMarcaId(rst.getInt(3));
                lub.setPaisId(rst.getInt(4));
                lub.setInvInicial(valFinal);
                lub.setFecha(fecha);
                result.add(lub);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }
    
    public List<ComVenLubricantes> getComVenLubAnterior(int countryId, int brandId, int productId, Date fecha) {
        List<ComVenLubricantes> result = new ArrayList();
        try {
            query = "select l.PRODUCTO_ID, p.NOMBRE, l.FECHA, "
                    + "l.INV_INICIAL, l.COMPRA, l.VENTA, l.INV_FINAL, l.PAIS_ID "
                    + "from COMPRA_VENTA_LUBRICANTE l, PRODUCTO p "
                    + "where l.PRODUCTO_ID = p.PRODUCTO_ID "
                    + "AND l.PAIS_ID=? and l.MARCA_ID=? and l.PRODUCTO_ID=? "
                    + "and l.FECHA = to_date(?,'dd/mm/yyyy') ";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, countryId);
            pst.setInt(2, brandId);
            pst.setInt(3, productId);
            pst.setString(4, Constant.SDF_ddMMyyyy.format(fecha));
            ResultSet rst = pst.executeQuery();
            ComVenLubricantes lub = new ComVenLubricantes();
            while (rst.next()) {                
                lub.setProductoId(rst.getInt(1));
                lub.setProductoNombre(rst.getString(2));
                lub.setFecha(rst.getDate(3));
                lub.setInvInicial(rst.getDouble(4));
                lub.setCompra(rst.getDouble(5));
                lub.setVenta(rst.getDouble(6));
                lub.setInvfinal(rst.getDouble(7));
                lub.setPaisId(rst.getInt(8));
                result.add(lub);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }
    
    public ComVenLubricantes insertCompra(ComVenLubricantes lub) {
        ComVenLubricantes result = new ComVenLubricantes();
        ResultSet rst = null;  
        Calendar ayer = Calendar.getInstance();
        ayer.setTime(lub.getFecha());
        ayer.add(Calendar.DATE, -1);
        try {
            int val = countLub(lub.getProductoId(),lub.getFecha(),lub.getPaisId());
            if(val>0){
                miQuery = "UPDATE COMPRA_VENTA_LUBRICANTE "
                    + "SET INV_INICIAL=?,COMPRA=?, VENTA=?, INV_FINAL=?, MODIFICADO_POR=?, MODIFICADO_EL=SYSDATE "
                    + "where PRODUCTO_ID=? and FECHA=to_date(?,'dd/mm/yyyy') ";
                pst = getConnection().prepareStatement(miQuery);
                pst.setDouble(1, lub.getInvInicial());
                pst.setDouble(2, lub.getCompra());
                pst.setDouble(3, lub.getVenta());
                pst.setDouble(4, lub.getInvfinal());
                pst.setString(5, lub.getModificadopor());
                pst.setInt(6, lub.getProductoId());
                pst.setDate(7, new java.sql.Date(lub.getFecha().getTime()));
                pst.executeUpdate();
                result = lub;
            }else{
                miQuery = "INSERT INTO COMPRA_VENTA_LUBRICANTE(COMPRA_ID, MARCA_ID, PRODUCTO_ID, "
                    + "FECHA, INV_INICIAL, COMPRA, INV_FINAL, CREADO_POR, PAIS_ID, CREADO_EL) "
                    + "VALUES(COMPRA_VENTA_LUBRICANTE_SEQ.NEXTVAL, ?, ?, ?,?, ?, ?, ?, ?, SYSDATE)";
                pst = getConnection().prepareStatement(miQuery);
                pst.setInt(1, lub.getMarcaId());
                pst.setInt(2, lub.getProductoId());
                pst.setDate(3, new java.sql.Date(lub.getFecha().getTime()));
                pst.setDouble(4, lub.getInvInicial());
                pst.setDouble(5, lub.getCompra());
                pst.setDouble(6, lub.getInvfinal());
                pst.setString(7, lub.getCreadopor());
                pst.setInt(8, lub.getPaisId());
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
    
    public boolean insertVenta(int productoId, int paisId, Double venta, Date fecha) {
        boolean result = true;
        ResultSet rst = null;  
        Calendar ayer = Calendar.getInstance();
        ayer.setTime(fecha);
        ayer.add(Calendar.DATE, -1);
        try {
            int idMarca = recuperaMarca(paisId);
            int val = countLub(productoId,fecha,paisId);
            Double valFinal = valorFinal(paisId, idMarca, productoId, fecha);
            Double valFinalAnt = valorFinal(paisId, idMarca, productoId, ayer.getTime());
            Double compra = compra(paisId, idMarca, productoId, fecha);
            if(val>0){                
                miQuery = "UPDATE COMPRA_VENTA_LUBRICANTE "
                    + "SET VENTA=?, INV_FINAL=?, MODIFICADO_EL=SYSDATE "
                    + "where PRODUCTO_ID=? and FECHA=to_date(?,'dd/mm/yyyy')";
                System.out.println("update venta "+miQuery);
                System.out.println("venta "+venta);
                System.out.println("valFinal "+valFinal);
                System.out.println("compra "+compra);
                pst = getConnection().prepareStatement(miQuery);
                pst.setDouble(1, venta);
                pst.setDouble(2, valFinal + compra - venta);
                pst.setInt(3,productoId);
                pst.setString(4, Constant.SDF_ddMMyyyy.format(fecha));
                pst.executeUpdate();
            }else{               
                miQuery = "INSERT INTO COMPRA_VENTA_LUBRICANTE(COMPRA_ID, MARCA_ID, PRODUCTO_ID, "
                    + "FECHA, INV_INICIAL, VENTA, INV_FINAL, CREADO_POR, PAIS_ID, CREADO_EL) "
                    + "VALUES(COMPRA_VENTA_LUBRICANTE_SEQ.NEXTVAL, ?, ?, ?,?, ?, ?, ?, ?, SYSDATE)";
                pst = getConnection().prepareStatement(miQuery);
                pst.setInt(1, idMarca);
                pst.setInt(2, productoId);
                pst.setDate(3, new java.sql.Date(fecha.getTime()));
                pst.setDouble(4, valFinalAnt);
                pst.setDouble(5, venta);
                pst.setDouble(6, (valFinalAnt-venta));
                pst.setInt(7, paisId);
                pst.executeQuery();
            }
            
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }  
    
    public int countLub(int idProducto, Date fecha, int idPais) {
        int result = 0;
        String dateString = Constant.SDF_ddMMyyyy.format(fecha);
        try {
            miQuery = "SELECT count(*) FROM COMPRA_VENTA_LUBRICANTE where PRODUCTO_ID="+idProducto+" "
                    + "and trunc(FECHA) =  to_date('"+dateString+"','dd/mm/yyyy') and PAIS_ID= "+idPais;
            System.out.println("validacion 0 o 1 "+miQuery);
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
    private Double valorFinal(int countryId, int brandId, int productId, Date fecha){        
        ResultSet rst = null;
        Double valor = 0.00;
            try{        
                query = "select l.INV_INICIAL "
                        + "from COMPRA_VENTA_LUBRICANTE l, PRODUCTO p "
                        + "where l.PRODUCTO_ID = p.PRODUCTO_ID "
                        + "AND l.PAIS_ID=? and l.MARCA_ID=? and l.PRODUCTO_ID=? "
                        + "and l.FECHA = to_date(?,'dd/mm/yyyy') ";
                System.out.println("query "+query);
                System.out.println("const "+Constant.SDF_ddMMyyyy.format(fecha));
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, countryId);
                pst.setInt(2, brandId);
                pst.setInt(3, productId);
                pst.setString(4, Constant.SDF_ddMMyyyy.format(fecha));
                rst = pst.executeQuery();            
                while (rst.next()) {         
                    System.out.println("fsdfsd "+rst.getDouble(1));
                    valor = rst.getDouble(1);
                }
            }catch(Exception exc){
                exc.printStackTrace();
            }
        return valor;
    }
    
    private Double compra(int countryId, int brandId, int productId, Date fecha){        
        ResultSet rst = null;
        Double valor = 0.00;
            try{        
                query = "select l.COMPRA "
                        + "from COMPRA_VENTA_LUBRICANTE l, PRODUCTO p "
                        + "where l.PRODUCTO_ID = p.PRODUCTO_ID "
                        + "AND l.PAIS_ID=? and l.MARCA_ID=? and l.PRODUCTO_ID=? "
                        + "and l.FECHA = to_date(?,'dd/mm/yyyy') ";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, countryId);
                pst.setInt(2, brandId);
                pst.setInt(3, productId);
                pst.setString(4, Constant.SDF_ddMMyyyy.format(fecha));
                rst = pst.executeQuery();            
                while (rst.next()) {         
                    System.out.println("fsdfsd "+rst.getDouble(1));
                    valor = rst.getDouble(1);
                }
            }catch(Exception exc){
                exc.printStackTrace();
            }
        return valor;
    }
    
    private int recuperaMarca(int productId){        
        ResultSet rst = null;
        int valor = 0;
            try{        
                query = "select ID_MARCA from PRODUCTO where producto_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, productId);
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
