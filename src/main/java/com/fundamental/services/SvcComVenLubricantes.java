/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.fundamental.utils.Constant;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.generic.bean.ComVenLubricantes;
import com.sisintegrados.generic.bean.compraGeneric;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m
 */
public class SvcComVenLubricantes extends DaoImp {

    private String query;

    public List<ComVenLubricantes> getComVenLub(int countryId, int brandId, Date fecha, Integer ESTACIONID) {
        List<ComVenLubricantes> result = new ArrayList();
        Calendar ayer = Calendar.getInstance();
        ayer.setTime(fecha);
        ayer.add(Calendar.DATE, -1);
        ResultSet rst = null;
        try {
            query = "SELECT p.producto_id,p.nombre,m.id_marca, c.pais_id, (select inv_inicial from compra_venta_lubricante "
                    + "where producto_id=p.PRODUCTO_ID and pais_id=c.PAIS_ID and FECHA=to_date('" + Constant.SDF_ddMMyyyy.format(fecha) + "','dd/mm/yyyy') AND ESTACION_ID = " + ESTACIONID + ") as inv_inicial, "
                    + "(select compra from compra_venta_lubricante "
                    + "where producto_id=p.PRODUCTO_ID and pais_id=c.PAIS_ID and FECHA=to_date('" + Constant.SDF_ddMMyyyy.format(fecha) + "','dd/mm/yyyy') AND ESTACION_ID = " + ESTACIONID + ") as compra "
                    + "FROM lubricanteprecio l, pais c, producto p, marca m "
                    + "WHERE l.pais_id = c.pais_id AND p.id_marca = m.id_marca and p.TIPO_ID=2 "
                    + "AND l.producto_id = p.producto_id and p.ESTADO='A' AND l.pais_id = " + countryId + " "
                    + "AND p.id_marca = " + brandId + "";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            ComVenLubricantes lub;
            while (rst.next()) {
                Double valFinal = valorFinal(countryId, brandId, fecha, rst.getInt(1), ESTACIONID); //ASG ESTACION
                int count = countLub(rst.getInt(1), fecha, countryId, ESTACIONID, false);
                if (valFinal != null && count == 0) {
                    valFinal = valFinal;
                } else {
                    valFinal = rst.getDouble(5);
                }
                lub = new ComVenLubricantes(rst.getInt(3), rst.getInt(1), rst.getInt(4), fecha, valFinal, rst.getString(2), ESTACIONID);
                lub.setCompra(rst.getDouble(6));
                result.add(lub);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
            } catch (SQLException ex) {
                Logger.getLogger(SvcComVenLubricantes.class.getName()).log(Level.SEVERE, null, ex);
            }
            closePst();
            closeConnections(); //asg
        }
        return result;
    }

    public List<ComVenLubricantes> getComVenLubAnterior(int countryId, int brandId, Date fecha, Integer ESTACIONID) {
        List<ComVenLubricantes> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = "select l.PRODUCTO_ID, p.NOMBRE, l.FECHA, "
                    + "l.INV_INICIAL, l.COMPRA, l.VENTA, l.INV_FINAL, l.PAIS_ID "
                    + "from COMPRA_VENTA_LUBRICANTE l, PRODUCTO p "
                    + "where l.PRODUCTO_ID = p.PRODUCTO_ID "
                    + "AND l.PAIS_ID=" + countryId + " and l.MARCA_ID=" + brandId + " "
                    + "and l.FECHA = to_date('" + Constant.SDF_ddMMyyyy.format(fecha) + "','dd/mm/yyyy') AND l.ESTACION_ID = " + ESTACIONID; //ASG ESTACION
            System.out.println("query,.,.,. " + query);
//            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            ComVenLubricantes lub;
            while (rst.next()) {
                lub = new ComVenLubricantes(rst.getInt(1), rst.getInt(8), rst.getDate(3), rst.getDouble(4), rst.getDouble(5), rst.getDouble(6), rst.getDouble(7), rst.getString(2));
                result.add(lub);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
            } catch (SQLException ex) {
            }
            closeConnections(); //asg
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
            int val = countLub(lub.getProductoId(), lub.getFecha(), lub.getPaisId(), lub.getEstacionid(), true);//ASG ESTACION
            /*New ASG*/
            compraGeneric comp = compraAct(lub.getProductoId(), lub.getFecha(), lub.getPaisId(), lub.getEstacionid(), true);
            Double invFinalTotal = 0.00;
            if (val > 0) {
                miQuery = "UPDATE COMPRA_VENTA_LUBRICANTE "
                        + "SET INV_INICIAL=?,COMPRA=?, INV_FINAL=?, MODIFICADO_POR=?, MODIFICADO_EL=SYSDATE "
                        + "where PRODUCTO_ID=? and FECHA=to_date(?,'dd/mm/yyyy') AND ESTACION_ID = ?"; //ASG ESTACION
                pst = getConnection().prepareStatement(miQuery);
                /*New ASG*/
                if (lub.getInvInicial() > 0) {
                    invFinalTotal = lub.getInvInicial();
                    pst.setDouble(1, invFinalTotal);
                }else{
                    invFinalTotal = Double.valueOf(comp.getInv_final());
                    pst.setDouble(1, comp.getInv_inicial());
                }

                pst.setDouble(2, lub.getCompra()+comp.getCompra());
//                pst.setDouble(3, lub.getInvfinal());
                pst.setDouble(3, invFinalTotal+lub.getCompra()+comp.getCompra()); //ASG
                pst.setString(4, lub.getModificadopor());
                pst.setInt(5, lub.getProductoId());
                pst.setString(6, Constant.SDF_ddMMyyyy.format(lub.getFecha()));
                pst.setInt(7, lub.getEstacionid()); ///ASG ESTACION
                pst.executeUpdate();
                result = lub;
            } else {
                miQuery = "INSERT INTO COMPRA_VENTA_LUBRICANTE(COMPRA_ID, MARCA_ID, PRODUCTO_ID, "
                        + "FECHA, INV_INICIAL, COMPRA, INV_FINAL, CREADO_POR, PAIS_ID, CREADO_EL, ESTACION_ID) " /*ASG ESTACION*/
                        + "VALUES(COMPRA_VENTA_LUBRICANTE_SEQ.NEXTVAL, ?, ?, ?,?, ?, ?, ?, ?, SYSDATE,?)";
                pst = getConnection().prepareStatement(miQuery);
                pst.setInt(1, lub.getMarcaId());
                pst.setInt(2, lub.getProductoId());
                pst.setDate(3, new java.sql.Date(lub.getFecha().getTime()));
                pst.setDouble(4, lub.getInvInicial());
                pst.setDouble(5, lub.getCompra());
                pst.setDouble(6, lub.getInvfinal());
                pst.setString(7, lub.getCreadopor());
                pst.setInt(8, lub.getPaisId());
                pst.setInt(9, lub.getEstacionid()); //ASG ESTACION
                pst.executeUpdate();
                result = lub;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
            } catch (Exception ignore) {
            }
            closePst();
            closeConnections(); //asg
        }
        return result;
    }

    public boolean insertVenta(int productoId, int paisId, Double venta, Date fecha, Integer ESTACIONID) {
        boolean result = true;
        ResultSet rst = null;
        Calendar ayer = Calendar.getInstance();
        ayer.setTime(fecha);
        ayer.add(Calendar.DATE, -1);
        try {
            int idMarca = recuperaMarca(productoId);
            Date fec = fechaMax(productoId, fecha, paisId, ESTACIONID);
//            Double valFinal = valorFinal(paisId, idMarca, fecha, productoId);
            Double valFinalAnt = valorFinal(paisId, idMarca, ayer.getTime(), productoId, ESTACIONID);
            Double ventAcu = valorVenta(paisId, idMarca, productoId, fecha, ESTACIONID);  //ASG
            Double compra = compra(paisId, idMarca, productoId, fecha, ESTACIONID);
            Double valInicial = valorInicial(paisId, idMarca, productoId, fecha, ESTACIONID);
            if (fec != null) {
                miQuery = "UPDATE COMPRA_VENTA_LUBRICANTE "
                        + "SET VENTA=?, INV_FINAL=?, MODIFICADO_EL=SYSDATE "
                        + "where PRODUCTO_ID=? and FECHA=to_date(?,'dd/mm/yyyy') AND ESTACION_ID = " + ESTACIONID; //ASG ESTACION
                pst = getConnection().prepareStatement(miQuery);
                pst.setDouble(1, ventAcu + venta); //asg
//                pst.setDouble(1, venta);
//                pst.setDouble(2, valInicial + compra - venta);//ASG
                pst.setDouble(2, valInicial - venta); //asg
                pst.setInt(3, productoId);
                pst.setString(4, Constant.SDF_ddMMyyyy.format(fec));
                pst.executeUpdate();
            } else {
                miQuery = "INSERT INTO COMPRA_VENTA_LUBRICANTE(COMPRA_ID, MARCA_ID, PRODUCTO_ID, "
                        + "FECHA, INV_INICIAL, VENTA, INV_FINAL, PAIS_ID, CREADO_EL, ESTACION_ID) "
                        + "VALUES(COMPRA_VENTA_LUBRICANTE_SEQ.NEXTVAL, ?, ?, ?,?, ?, ?, ?, SYSDATE, ?)";
                pst = getConnection().prepareStatement(miQuery);
                pst.setInt(1, idMarca);
                pst.setInt(2, productoId);
                pst.setDate(3, new java.sql.Date(fecha.getTime()));
                pst.setDouble(4, valFinalAnt);
                pst.setDouble(5, venta);
                pst.setDouble(6, (valFinalAnt - venta));
                pst.setInt(7, paisId);
                pst.setInt(8, ESTACIONID);  //ASG ESTACION
                pst.executeUpdate();
            }

        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        } finally {

            try {
                rst.close();
            } catch (Exception ignore) {
            }
            closePst();
            closeConnections(); //asg
        }
        return result;
    }

    public boolean reversarVenta(int productoId, int paisId, Double venta, Date fecha, Integer ESTACIONID) {
        boolean result = true;
        ResultSet rst = null;
        Calendar ayer = Calendar.getInstance();
        ayer.setTime(fecha);
        ayer.add(Calendar.DATE, -1);
        try {
            Date fec = fechaMax(productoId, fecha, paisId, ESTACIONID);
            int idMarca = recuperaMarca(productoId);
//            Double valFinalAnt = valorFinal(paisId, idMarca, ayer.getTime(), productoId, ESTACIONID);  /*ASG*/
//            Double compra = compra(paisId, idMarca, productoId, fecha, ESTACIONID);  /*ASG*/
            Double ventAcu = valorVenta(paisId, idMarca, productoId, fecha, ESTACIONID);  //ASG
            Double valInicial = valorInicial(paisId, idMarca, productoId, fecha, ESTACIONID);
            miQuery = "UPDATE COMPRA_VENTA_LUBRICANTE "
                    + "SET VENTA=?, INV_FINAL=?, MODIFICADO_EL=SYSDATE "
                    + "where PRODUCTO_ID=? and FECHA=to_date(?,'dd/mm/yyyy') AND ESTACION_ID = " + ESTACIONID;  //ASG ESTACION ID
            System.out.println("REVERSA VENTA " + miQuery);
            System.out.println("VENTA  " + ventAcu + "  " + venta);
            pst = getConnection().prepareStatement(miQuery);
            pst.setDouble(1, ventAcu - Math.abs(venta));//asg
            pst.setDouble(2, valInicial + Math.abs(venta));//asg   
//            if (ventAcu > Math.abs(venta)) {
//                pst.setDouble(2, valInicial + Math.abs(venta));//asg                
//            } else {
//                pst.setDouble(2, valInicial + (ventAcu - Math.abs(venta)));//asg
//            }
//            pst.setDouble(1, valInicial + compra);
            pst.setInt(3, productoId);
            pst.setString(4, Constant.SDF_ddMMyyyy.format(fec));
            pst.executeUpdate();
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        } finally {

            try {
                rst.close();
            } catch (Exception ignore) {
            }
            closePst();
            closeConnections(); //asg
        }
        return result;
    }

    public compraGeneric compraAct(int idProducto, Date fecha, int idPais, Integer ESTACIONID, boolean CerrarConexion) {
        compraGeneric result = new compraGeneric();
        String dateString = Constant.SDF_ddMMyyyy.format(fecha);
        ResultSet rst = null;
        try {
            miQuery = "SELECT INV_INICIAL,INV_FINAL,COMPRA,VENTA FROM COMPRA_VENTA_LUBRICANTE where PRODUCTO_ID=" + idProducto + " "
                    + "and trunc(FECHA) =  to_date('" + dateString + "','dd/mm/yyyy') and PAIS_ID= " + idPais + " AND ESTACION_ID = " + ESTACIONID;  //ASG ESTACION
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            if (rst.next()) {
                result = new compraGeneric(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getInt(4));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
            } catch (SQLException ex) {
            }
            closePst();
            if (CerrarConexion) {
                System.out.println("ENTRA A CERRAR LA CONEXION " + CerrarConexion);
                closeConnections(); //asg
            }
        }
        return result;
    }

    public int countLub(int idProducto, Date fecha, int idPais, Integer ESTACIONID, boolean CerrarConexion) {
        int result = 0;
        String dateString = Constant.SDF_ddMMyyyy.format(fecha);
        ResultSet rst = null;
        try {
            miQuery = "SELECT count(*) FROM COMPRA_VENTA_LUBRICANTE where PRODUCTO_ID=" + idProducto + " "
                    + "and trunc(FECHA) =  to_date('" + dateString + "','dd/mm/yyyy') and PAIS_ID= " + idPais + " AND ESTACION_ID = " + ESTACIONID;  //ASG ESTACION
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            if (rst.next()) {
                result = rst.getInt(1);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
            } catch (SQLException ex) {
            }
            closePst();
            if (CerrarConexion) {
                System.out.println("ENTRA A CERRAR LA CONEXION " + CerrarConexion);
                closeConnections(); //asg
            }
        }
        return result;
    }

    public Date fechaMax(int idProducto, Date fecha, int idPais, Integer ESTACIONID) {
        Date result = null;
        String fecha1 = Constant.SDF_ddMMyyyy.format(fecha);
        String fecha2 = Constant.SDF_ddMMyyyy.format(fecha);
        String fecha3 = Constant.SDF_ddMMyyyy.format(fecha);
        fecha1 = fecha2.substring(3, 5);
        fecha2 = fecha2.substring(6);
        fecha3 = fecha3.substring(0, 2);

        System.out.println("FECHA1 " + fecha1);
        System.out.println("FECHA2 " + fecha2);
        ResultSet rst = null;
        try {
            miQuery = "SELECT fecha FROM COMPRA_VENTA_LUBRICANTE "
                    + "where PRODUCTO_ID=" + idProducto + " and PAIS_ID=" + idPais + " AND ESTACION_ID = " + ESTACIONID
                    + /*ASG ESTACION*/ " and FECHA = (select max(b.fecha) from COMPRA_VENTA_LUBRICANTE b where b.PAIS_ID=" + idPais + " "
                    + "and b.producto_id=" + idProducto + " and " + fecha1 + "=extract(MONTH from b.fecha) "
                    + "AND " + fecha3 + "=extract(DAY FROM b.fecha) " /*ASG*/
                    + "AND " + fecha2 + "=extract(YEAR from b.fecha) AND b.ESTACION_ID = " + ESTACIONID + ")  order by FECHA desc ";
            System.out.println("QUERY FEXMAX " + miQuery);
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            if (rst.next()) {
                result = rst.getDate(1);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
            } catch (SQLException ex) {
            }
            closePst();
            closeConnections(); //asg            
        }
        return result;
    }

    private Double valorFinal(int countryId, int brandId, Date fecha, int productId, Integer ESTACIONID) {
        ResultSet rst = null;
        Double valor = 0.00;
        String fecha1 = Constant.SDF_ddMMyyyy.format(fecha);
        String fecha2 = Constant.SDF_ddMMyyyy.format(fecha);

        String fecha3 = Constant.SDF_ddMMyyyy.format(fecha);

        fecha1 = fecha2.substring(3, 5);
        fecha2 = fecha2.substring(6);

        fecha3 = fecha3.substring(0, 2);

        try {
            query = "select INV_FINAL "
                    + "from COMPRA_VENTA_LUBRICANTE "
                    + "where PAIS_ID=" + countryId + " and MARCA_ID=" + brandId + " and producto_id=" + productId + " AND ESTACION_ID = " + ESTACIONID /*ASG ESTACION*/
                    + " and FECHA = (select max(b.fecha) from COMPRA_VENTA_LUBRICANTE b where b.PAIS_ID=" + countryId + " "
                    + "and b.MARCA_ID=" + brandId + " and b.producto_id=" + productId + " and " + fecha1 + "=extract(MONTH from b.fecha) "
                    + "AND " + fecha3 + "=extract(DAY from b.fecha) " /*ASG*/
                    + "AND " + fecha2 + "=extract(YEAR from b.fecha) AND b.ESTACION_ID = " + ESTACIONID + ")  order by FECHA desc ";
            System.out.println("query VALOR FINAL " + query);
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                valor = rst.getDouble(1);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
//                closeConnections(); //asg
            } catch (SQLException ex) {
            }
            closePst();
        }
        return valor;
    }

    private Double valorVenta(int countryId, int brandId, int productId, Date fecha, Integer ESTACIONID) {
        ResultSet rst = null;
        Double valor = 0.00;
        String fecha1 = Constant.SDF_ddMMyyyy.format(fecha);
        String fecha2 = Constant.SDF_ddMMyyyy.format(fecha);
        String fecha3 = Constant.SDF_ddMMyyyy.format(fecha);
        fecha1 = fecha2.substring(3, 5);
        fecha2 = fecha2.substring(6);
        fecha3 = fecha3.substring(0, 2);
        try {
//            query = "select l.INV_INICIAL " //ASG
            query = "select l.venta "
                    + "from COMPRA_VENTA_LUBRICANTE l, PRODUCTO p "
                    + "where l.PRODUCTO_ID = p.PRODUCTO_ID AND l.ESTACION_ID = " + ESTACIONID /*asg estacion*/
                    + " AND l.PAIS_ID=" + countryId + " and l.MARCA_ID=" + brandId + " and l.PRODUCTO_ID=" + productId + " "
                    + "and FECHA = (select max(b.fecha) from COMPRA_VENTA_LUBRICANTE b where b.PAIS_ID=" + countryId + " "
                    + "and b.producto_id=" + productId + " and " + fecha1 + "=extract(MONTH from b.fecha) "
                    + "AND " + fecha3 + "=extract(DAY from b.fecha) " /*ASG*/
                    + "AND " + fecha2 + "=extract(YEAR from b.fecha) AND b.ESTACION_ID = " + ESTACIONID + ")  order by FECHA desc ";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();

            System.out.println("QUERY VALOR VENTA " + query);
            while (rst.next()) {
                valor = rst.getDouble(1);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
            } catch (SQLException ex) {
            }
            closePst();
            closeConnections(); //asg
        }
        return valor;
    }

    private Double valorInicial(int countryId, int brandId, int productId, Date fecha, Integer ESTACIONID) {
        ResultSet rst = null;
        Double valor = 0.00;
        String fecha1 = Constant.SDF_ddMMyyyy.format(fecha);
        String fecha2 = Constant.SDF_ddMMyyyy.format(fecha);
        String fecha3 = Constant.SDF_ddMMyyyy.format(fecha);
        fecha1 = fecha2.substring(3, 5);
        fecha2 = fecha2.substring(6);
        fecha3 = fecha3.substring(0, 2);
        try {
//            query = "select l.INV_INICIAL " //ASG
            query = "select l.inv_final "
                    + "from COMPRA_VENTA_LUBRICANTE l, PRODUCTO p "
                    + "where l.PRODUCTO_ID = p.PRODUCTO_ID AND l.ESTACION_ID = " + ESTACIONID /*asg estacion*/
                    + " AND l.PAIS_ID=" + countryId + " and l.MARCA_ID=" + brandId + " and l.PRODUCTO_ID=" + productId + " "
                    + "and FECHA = (select max(b.fecha) from COMPRA_VENTA_LUBRICANTE b where b.PAIS_ID=" + countryId + " "
                    + "and b.producto_id=" + productId + " and " + fecha1 + "=extract(MONTH from b.fecha) "
                    + "AND " + fecha3 + "=extract(DAY from b.fecha) "
                    + "AND " + fecha2 + "=extract(YEAR from b.fecha) AND b.ESTACION_ID = " + ESTACIONID + ")  order by FECHA desc ";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();

            System.out.println("QUERY VALOR INICIAL " + query);
            while (rst.next()) {
                valor = rst.getDouble(1);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
            } catch (SQLException ex) {
            }
            closePst();
            closeConnections(); //asg
        }
        return valor;
    }

    private Double compra(int countryId, int brandId, int productId, Date fecha, Integer ESTACIONID) {
        ResultSet rst = null;
        Double valor = 0.00;
        String fecha1 = Constant.SDF_ddMMyyyy.format(fecha);
        String fecha2 = Constant.SDF_ddMMyyyy.format(fecha);
        String fecha3 = Constant.SDF_ddMMyyyy.format(fecha);
        fecha1 = fecha2.substring(3, 5);
        fecha2 = fecha2.substring(6);
        fecha3 = fecha3.substring(0, 2);
        try {
            query = "select l.COMPRA "
                    + "from COMPRA_VENTA_LUBRICANTE l, PRODUCTO p "
                    + "where l.PRODUCTO_ID = p.PRODUCTO_ID AND l.ESTACION_ID = " + ESTACIONID /*asg estacion*/
                    + " AND l.PAIS_ID=" + countryId + " and l.MARCA_ID=" + brandId + " and l.PRODUCTO_ID=" + productId + " "
                    + "and FECHA = (select max(b.fecha) from COMPRA_VENTA_LUBRICANTE b where b.PAIS_ID=" + countryId + " "
                    + "and b.producto_id=" + productId + " and " + fecha1 + "=extract(MONTH from b.fecha) "
                    + "AND " + fecha3 + "=extract(DAY from b.fecha) " /*ASG*/
                    + "AND " + fecha2 + "=extract(YEAR from b.fecha) AND b.ESTACION_ID = " + ESTACIONID + ")";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            System.out.println("query COMPRA " + query);
            while (rst.next()) {
                System.out.println("fsdfsd " + rst.getDouble(1));
                valor = rst.getDouble(1);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
            } catch (SQLException ex) {
            }
            closePst();
            closeConnections(); //asg
        }
        return valor;
    }

    private int recuperaMarca(int productId) {
        ResultSet rst = null;
        int valor = 0;
        try {
            query = "select ID_MARCA from PRODUCTO where producto_id = ?";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, productId);
            rst = pst.executeQuery();
            while (rst.next()) {
                valor = rst.getInt(1);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
            } catch (SQLException ex) {
            }
            closePst();
            closeConnections(); //asg
        }
        return valor;
    }
}
