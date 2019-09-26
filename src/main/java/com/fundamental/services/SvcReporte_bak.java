package com.fundamental.services;

import com.fundamental.model.Mediopago;
import com.fundamental.model.Producto;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Henry Barrientos
 */
public class SvcReporte_bak extends Dao {

    String query;
    SimpleDateFormat sdf_ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat decFmt2 = new DecimalFormat("### ###,##0.00;-#");

    private String getMTDTurnosValidos(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        String result = "";
        try {
            query = "SELECT d.fecha, t.turno_id "
                    + "FROM dia d, turno t "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id "
                    + "AND d.estacion_id = " + estacionId + " AND d.fecha BETWEEN TO_DATE('" + sdf_ddmmyyyy.format(fechaInicial) + "', 'dd/mm/yyyy') AND TO_DATE('" + sdf_ddmmyyyy.format(fechaFinal) + "', 'dd/mm/yyyy') "
                    + "ORDER BY d.fecha, turno_id";

            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            String uFecha = "", lFecha = "", lTurno = "";
            while (rst.next()) {
                if (!uFecha.equals(rst.getString(1))) {
                    result += (result.isEmpty()) ? rst.getString(2) : ", ".concat(rst.getString(2));    //solo el primero de cada dia
                    uFecha = rst.getString(1);
                }
                lFecha = rst.getString(1);
                lTurno = rst.getString(2);
            }
            if (!uFecha.equals(lFecha)) {
                result += (result.isEmpty()) ? lTurno : ", ".concat(lTurno);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<Producto> getMTDProductos(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        List<Producto> result = new ArrayList();
        try {
            String turnos = getMTDTurnosValidos(fechaInicial, fechaFinal, estacionId);
            query = "SELECT DISTINCT pro.producto_id, pro.nombre, pro.codigo, pro.estado, pro.orden_pos "
                    + "FROM dia d, turno t, precio p, producto pro, estacion e "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND t.turno_id = p.turno_id AND p.producto_id = pro.producto_id "
                    + "AND d.estacion_id = e.estacion_id AND  d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND t.turno_id IN (" + turnos + ") "
                    + "ORDER BY pro.producto_id";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            Producto product;
            while (rst.next()) {
                product = new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), null, rst.getInt(5));
                product.setPriceAS(0D);
                product.setPriceSC(0D);
                result.add(product);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDPrecios(Date fechaInicial, Date fechaFinal, Integer estacionId, String productsIds, Integer tipodespachoId) {
        List<String[]> result = new ArrayList();
        try {
            String turnos = getMTDTurnosValidos(fechaInicial, fechaFinal, estacionId);
            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, e.nombre, pro.nombre, p.precio, pro.producto_id "
                    + "FROM dia d, turno t, precio p, producto pro, estacion e "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND t.turno_id = p.turno_id AND p.producto_id = pro.producto_id AND e.estacion_id = d.estacion_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "AND t.turno_id IN (" + turnos + ") "
                    + "AND p.tipodespacho_id =  " + tipodespachoId
                    + " UNION "
                    + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, e.nombre, pro.nombre, 0, pro.producto_id "
                    + "FROM dia d, turno t, producto pro, estacion e  "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND e.estacion_id = d.estacion_id  "
                    + "AND NOT EXISTS (SELECT * FROM precio p where t.turno_id = p.turno_id AND p.producto_id = pro.producto_id AND p.tipodespacho_id = " + tipodespachoId + " ) "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "AND t.turno_id IN (" + turnos + ") AND pro.producto_id in (" + productsIds + ") "
                    + "ORDER BY 1, 5";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new String[]{
                    rst.getString(1), //fecha
                    rst.getString(2), //nombreEstacion
                    rst.getString(3), //
                    rst.getString(4) //precio
                });
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDVolumenes(Date fechaInicial, Date fechaFinal, Integer estacionId, String productsIds) {
        List<String[]> result = new ArrayList();
        try {
            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, pro.producto_id, pro.nombre, SUM(ld.lectura_finaL - ld.lectura_inicial - ld.calibracion) "
                    + "FROM dia d, turno t, producto pro, lectura l, lectura_detalle ld, estacion e "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id  AND d.estacion_id = e.estacion_id AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id AND ld.producto_id = pro.producto_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), pro.producto_id, pro.nombre "
                    //                    + "ORDER BY 1, pro.producto_id"
                    + " UNION "
                    + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, pro.producto_id, pro.nombre, 0 "
                    + "FROM dia d, turno t, producto pro, lectura l, estacion e "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id  AND d.estacion_id = e.estacion_id AND t.turno_id = l.turno_id "
                    + "AND NOT EXISTS (SELECT * FROM lectura_detalle ld WHERE l.lectura_id = ld.lectura_id AND ld.producto_id = pro.producto_id) "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND pro.producto_id in (" + productsIds + ") "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), pro.producto_id, pro.nombre "
                    + "ORDER BY 1, 2";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            Double total = 0D, lastTotal = 0D, diff = 0D;
            int itemNum = 0;
            String fecha = "";
            List<String> tmplist = new ArrayList();
            while (rst.next()) {
                if (!fecha.isEmpty() && !fecha.equals(rst.getString(1))) {
                    tmplist.add(0, fecha);
                    tmplist.add(total.toString());
                    diff = (total > 0 && lastTotal > 0) ? (total - lastTotal) / lastTotal : 0D;
                    tmplist.add(decFmt2.format(diff));
                    result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
                    fecha = rst.getString(1);
                    tmplist = new ArrayList();
                    lastTotal = total;
                    total = 0D;
                } else if (fecha.isEmpty()) {
                    fecha = rst.getString(1);
                }
                tmplist.add(rst.getString(4));
                total += rst.getDouble(4);
            }
            if (!fecha.isEmpty()) { //si entro en el while
                tmplist.add(0, fecha);
                tmplist.add(decFmt2.format(total));
                diff = (total > 0 && lastTotal > 0) ? (total - lastTotal) / lastTotal : 0D;
                tmplist.add(decFmt2.format(diff));
                result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDMonetario(Date fechaInicial, Date fechaFinal, Integer estacionId, String productsIds) {
        List<String[]> result = new ArrayList();
        try {
            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, pro.producto_id, pro.nombre, SUM(p.precio * (ld.lectura_final - ld.lectura_inicial - ld.calibracion)) "
                    + "FROM dia d, turno t, producto pro, lectura l, lectura_detalle ld, precio p "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id AND ld.producto_id = pro.producto_id "
                    + "AND p.turno_id = t.turno_id AND p.producto_id = ld.producto_id AND p.tipodespacho_id = ld.tipodespacho_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), pro.producto_id, pro.nombre "
                    //                    + "ORDER BY 1, pro.producto_id"
                    + " UNION "
                    + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, pro.producto_id, pro.nombre, 0 "
                    + "FROM dia d, turno t, producto pro, lectura l, estacion e, precio p "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id  AND d.estacion_id = e.estacion_id AND t.turno_id = l.turno_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND pro.producto_id in (" + productsIds + ") "
                    + "AND NOT EXISTS (SELECT * FROM lectura_detalle ld WHERE l.lectura_id = ld.lectura_id AND ld.producto_id = pro.producto_id) "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), pro.producto_id, pro.nombre "
                    + "ORDER BY 1, 2";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new String[]{
                    rst.getString(1), //fecha
                    rst.getString(2), //productoId
                    rst.getString(3), //productoNombre
                    rst.getString(4) //ventasDinero
                });
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDVentasMiscelaneos(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        List<String[]> result = new ArrayList();
        try {
            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, SUM(ap.monto) "
                    + "FROM dia d, producto p, estacion_producto ep, arqueocaja a, arqueocaja_producto ap "
                    + "WHERE d.fecha = a.fecha AND a.arqueocaja_id = ap.arqueocaja_id AND ap.producto_id = p.producto_id AND ep.estacion_id = a.estacion_id AND ep.producto_id = p.producto_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND p.tipo_id = 2 " //tipo 2, son los miscelaneos
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                    //                    + "ORDER BY 1"
                    + " UNION "
                    + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, 0 "
                    + "FROM dia d "
                    + "WHERE d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "AND NOT EXISTS (SELECT * FROM arqueocaja a, arqueocaja_producto ap, producto p WHERE a.arqueocaja_id = ap.arqueocaja_id AND a.fecha = d.fecha AND ap.producto_id = p.producto_id AND p.tipo_id = 2) "
                    + "ORDER BY 1";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new String[]{
                    rst.getString(1), //fecha
                    rst.getString(2) //ventas
                });
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    //Venta exenta
    public List<String[]> getMTDVentaByProducto(Date fechaInicial, Date fechaFinal, Integer estacionId, Integer productoId) {
        List<String[]> result = new ArrayList();
        try {
            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, SUM(ap.monto) "
                    + "FROM dia d, producto p, estacion_producto ep, arqueocaja a, arqueocaja_producto ap "
                    + "WHERE d.fecha = a.fecha AND a.arqueocaja_id = ap.arqueocaja_id AND ap.producto_id = p.producto_id AND ep.estacion_id = a.estacion_id AND ep.producto_id = p.producto_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND p.producto_id = ? "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                    //                    + "ORDER BY 1"
                    + " UNION "
                    + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, 0 "
                    + "FROM dia d "
                    + "WHERE d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "AND NOT EXISTS (SELECT * FROM arqueocaja a, arqueocaja_producto ap WHERE a.arqueocaja_id = ap.arqueocaja_id AND a.fecha = d.fecha AND ap.producto_id = ?) "
                    + "ORDER BY 1";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, productoId);
            pst.setInt(5, estacionId);
            pst.setString(6, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(7, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(8, productoId);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new String[]{
                    rst.getString(1), //fecha
                    rst.getString(2) //ventas
                });
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDVentaOtrosProductos(Date fechaInicial, Date fechaFinal, Integer estacionId, boolean totalXdia) {
        List<String[]> result = new ArrayList();
        try {
            List<Producto> otrosProductos = getOtrosProductosByEstacionid(fechaInicial, fechaFinal, estacionId);
            String otrosProductosIds = "";
            for (Producto p : otrosProductos) {
                otrosProductosIds += (otrosProductosIds.isEmpty()) ? p.getProductoId() : ",".concat(p.getProductoId().toString());
            }
            Double diff = 0D, totalAnt = 0D;

            if (totalXdia && !otrosProductosIds.isEmpty()) {
                query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, SUM(ap.monto) "
                        + "FROM dia d, turno t, producto p, estacion_producto ep, arqueocaja a, arqueocaja_producto ap "
                        + "WHERE d.fecha = a.fecha AND a.arqueocaja_id = ap.arqueocaja_id AND ap.producto_id = p.producto_id AND ep.estacion_id = a.estacion_id AND ep.producto_id = p.producto_id "
                        + "AND d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND t.turno_id = a.turno_id "
                        + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND p.producto_id IN (" + otrosProductosIds + ") "
                        + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                        //                    + "ORDER BY 1"
                        + " UNION "
                        + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, 0 "
                        + "FROM dia d "
                        + "WHERE d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                        + "AND NOT EXISTS (SELECT * FROM arqueocaja a, arqueocaja_producto ap WHERE a.arqueocaja_id = ap.arqueocaja_id AND a.fecha = d.fecha AND ap.producto_id IN (" + otrosProductosIds + ") ) "
                        + "ORDER BY 1";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, estacionId);
                pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
                pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
                pst.setInt(4, estacionId);
                pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
                pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
                ResultSet rst = pst.executeQuery();
                while (rst.next()) {
                    diff = (totalAnt > 0 && rst.getDouble(2) > 0) ? (rst.getDouble(2) - totalAnt) / totalAnt : 0D;
                    result.add(new String[]{
                        rst.getString(1), //fecha
                        rst.getString(2), //ventas
                        ((diff == 0D) ? null : diff.toString())
                    });
                    totalAnt = rst.getDouble(2);
                }
            } else if (!totalXdia && !otrosProductosIds.isEmpty()) {
                query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, p.producto_id, SUM(ap.monto) "
                        + "FROM dia d, turno t, producto p, estacion_producto ep, arqueocaja a, arqueocaja_producto ap "
                        + "WHERE d.fecha = a.fecha AND a.arqueocaja_id = ap.arqueocaja_id AND ap.producto_id = p.producto_id AND ep.estacion_id = a.estacion_id AND ep.producto_id = p.producto_id "
                        + "AND d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND t.turno_id = a.turno_id "
                        + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND p.producto_id IN (" + otrosProductosIds + ") "
                        + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), p.producto_id "
                        + //"--ORDER BY 1, p.producto_id                    " +
                        " UNION "
                        + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, p.producto_id, 0 "
                        + "FROM dia d, producto p "
                        + "WHERE d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                        + "AND p.producto_id IN (" + otrosProductosIds + ")"
                        + "AND NOT EXISTS (SELECT * FROM arqueocaja a, arqueocaja_producto ap WHERE a.arqueocaja_id = ap.arqueocaja_id AND a.fecha = d.fecha AND ap.producto_id = p.producto_id ) "
                        + "ORDER BY 1, 2";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, estacionId);
                pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
                pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
                pst.setInt(4, estacionId);
                pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
                pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
                ResultSet rst = pst.executeQuery();
                List<String> datamap = new ArrayList<String>();
                Map<Integer, Double> mapTotal = new HashMap();
                String uFecha = "";
                while (rst.next()) {
                    if (!uFecha.isEmpty() && !uFecha.equals(rst.getString(1))) {
                        datamap.add(0, uFecha);
                        result.add(Arrays.copyOf(datamap.toArray(), datamap.size(), String[].class));
                        datamap = new ArrayList();
                        uFecha = rst.getString(1);
                    } else if (uFecha.isEmpty()) {
                        uFecha = rst.getString(1);
                    }
                    totalAnt = mapTotal.get(rst.getInt(2));
                    diff = (totalAnt != null && totalAnt > 0 && rst.getDouble(3) > 0) ? (rst.getDouble(3) - totalAnt) / totalAnt : 0D;
                    datamap.add(decFmt2.format(rst.getDouble(3)));
                    datamap.add((diff == 0D) ? null : decFmt2.format(diff));
                    mapTotal.put(rst.getInt(2), rst.getDouble(3));
                }
                if (!uFecha.isEmpty()) {
                    datamap.add(0, uFecha);
                    result.add(Arrays.copyOf(datamap.toArray(), datamap.size(), String[].class));
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<Producto> getOtrosProductosByEstacionid(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        List<Producto> result = new ArrayList();
        try {
            //los distintos de tipo combustible (1)
            miQuery = "SELECT DISTINCT p.producto_id, p.nombre, p.codigo, p.estado, p.creado_por, p.orden_pos "
                    + "FROM dia d, turno t, producto p, estacion_producto ep, arqueocaja a, arqueocaja_producto ap "
                    + "WHERE d.fecha = a.fecha AND a.arqueocaja_id = ap.arqueocaja_id AND ap.producto_id = p.producto_id AND ep.estacion_id = a.estacion_id AND ep.producto_id = p.producto_id "
                    + "AND d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND t.turno_id = a.turno_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "ORDER BY 1";
            pst = getConnection().prepareStatement(miQuery);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getInt(6)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public Integer getCantidadTurnos(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        Integer result = 0;
        try {
            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, COUNT(t.turno_id) "
                    + "FROM dia d, turno t "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                    + "ORDER BY 2 DESC";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            result = (rst.next()) ? rst.getInt(2) : 0;
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    //Venta por turno
    public List<String[]> getMTDVentaTurnoByFechaEstacion(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        List<String[]> result = new ArrayList();
        try {
            int cantTurnos = getCantidadTurnos(fechaInicial, fechaFinal, estacionId);

            List<Producto> otrosProductos = getOtrosProductosByEstacionid(fechaInicial, fechaFinal, estacionId);
            String otrosProductosIds = "";
            for (Producto p : otrosProductos) {
                otrosProductosIds += (otrosProductosIds.isEmpty()) ? p.getProductoId() : ",".concat(p.getProductoId().toString());
            }
            otrosProductosIds = (otrosProductosIds.isEmpty()) ? "-1" : otrosProductosIds;

            query = "SELECT virtual.fecha, virtual.turno_id, SUM(virtual.total) "
                    + "FROM ("
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, t.turno_id, SUM(p.precio * (ld.lectura_final - ld.lectura_inicial - ld.calibracion)) total  "
                    + "    FROM dia d, turno t, lectura l, lectura_detalle ld, precio p, producto pro  "
                    + "    WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND l.turno_id = t.turno_id AND l.estacion_id = t.estacion_id  "
                    + "    AND ld.lectura_id = l.lectura_id AND ld.estacion_id = l.estacion_id "
                    + "    AND p.turno_id = t.turno_id AND p.producto_id = ld.producto_id AND p.tipodespacho_id = ld.tipodespacho_id "
                    + "    AND ld.producto_id = pro.producto_id  "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy')  "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), t.turno_id  "
                    + "    UNION "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, t.turno_id, SUM(ap.monto) total "
                    + "    FROM dia d, turno t, producto p, estacion_producto ep, arqueocaja a, arqueocaja_producto ap "
                    + "    WHERE d.fecha = a.fecha AND a.arqueocaja_id = ap.arqueocaja_id AND ap.producto_id = p.producto_id AND ep.estacion_id = a.estacion_id AND ep.producto_id = p.producto_id "
                    + "    AND d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND t.turno_id = a.turno_id "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND p.producto_id IN (" + otrosProductosIds + ") "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), t.turno_id "
                    + ") virtual "
                    + "GROUP BY fecha, turno_id "
                    + "ORDER BY 1, 2";

            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));

            ResultSet rst = pst.executeQuery();
            String fecha = "";
            List<String> tmplist = new ArrayList();
            int mysize = 0;
            while (rst.next()) {
                if (!fecha.isEmpty() && !fecha.equals(rst.getString(1))) {
                    mysize = tmplist.size();
                    for (int i = 0; i < cantTurnos - mysize; i++) {
                        tmplist.add("0");
                    }
                    tmplist.add(0, fecha);
                    result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
                    fecha = rst.getString(1);
                    tmplist = new ArrayList();
                } else if (fecha.isEmpty()) {
                    fecha = rst.getString(1);
                }
                tmplist.add(rst.getString(3));
            }
            if (!fecha.isEmpty()) { //si entro en el while
                mysize = tmplist.size();
                for (int i = 0; i < cantTurnos - mysize; i++) {
                    tmplist.add("0");
                }
                tmplist.add(0, fecha);
                result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public String getMTDIslasIds(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        String result = "";
        try {
            query = "SELECT distinct b.isla "
                    + "FROM dia d, turno t, lectura l, lectura_detalle ld, estacion e, bomba b "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id  AND d.estacion_id = e.estacion_id AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id AND ld.bomba_id = b.bomba_id "
                    + "AND d.estacion_id = " + estacionId + " AND d.fecha BETWEEN TO_DATE('" + sdf_ddmmyyyy.format(fechaInicial) + "', 'dd/mm/yyyy') AND TO_DATE('" + sdf_ddmmyyyy.format(fechaFinal) + "', 'dd/mm/yyyy') "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), b.isla "
                    + "ORDER BY 1";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result += (result.isEmpty()) ? rst.getString(1) : ", ".concat(rst.getString(1));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDVentaIslaByFechaEstacion(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        List<String[]> result = new ArrayList();
        try {
            String islasIds = getMTDIslasIds(fechaInicial, fechaFinal, estacionId);
            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, b.isla, SUM(ld.lectura_finaL - ld.lectura_inicial - ld.calibracion) "
                    + "FROM dia d, turno t, lectura l, lectura_detalle ld, estacion e, bomba b "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id  AND d.estacion_id = e.estacion_id AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id "
                    + "AND ld.bomba_id = b.bomba_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), b.isla  "
                    + " UNION "
                    + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, b.isla, 0 "
                    + "FROM dia d, turno t, estacion e, bomba b "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id  AND d.estacion_id = e.estacion_id "
                    + "AND NOT EXISTS (SELECT * FROM lectura_detalle ld, lectura l WHERE l.lectura_id = ld.lectura_id AND ld.bomba_id = b.bomba_id AND t.turno_id = l.turno_id) "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "AND b.isla IN (" + islasIds + ") "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), b.isla "
                    + "ORDER BY 1, 2";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            String fecha = "";
            List<String> tmplist = new ArrayList();
            while (rst.next()) {
                if (!fecha.isEmpty() && !fecha.equals(rst.getString(1))) {
                    tmplist.add(0, fecha);
                    result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
                    fecha = rst.getString(1);
                    tmplist = new ArrayList();
                } else if (fecha.isEmpty()) {
                    fecha = rst.getString(1);
                }
                tmplist.add(rst.getString(3));
            }
            if (!fecha.isEmpty()) { //si entro en el while
                tmplist.add(0, fecha);
                result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<Mediopago> getMTDMediospagoByFechaEstacion_distintos(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        List<Mediopago> result = new ArrayList();
        try {
            query = "SELECT DISTINCT m.mediopago_id, m.nombre, m.tipo "
                    + "FROM arqueocaja_detalle ad, dia d, arqueocaja a, mediopago m "
                    + "WHERE d.fecha = a.fecha AND a.arqueocaja_id = ad.arqueocaja_id AND d.estacion_id = a.estacion_id AND ad.mediopago_id = m.mediopago_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "ORDER BY 2";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Mediopago(rst.getInt(1), rst.getString(2), rst.getInt(3), null, 0, null, false, null));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<Mediopago> getMTDMediospagoEfectivoByFechaEstacion_distintos(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        List<Mediopago> result = new ArrayList();
        try {
            query = "SELECT DISTINCT m.mediopago_id, m.nombre, m.tipo "
                    + "FROM efectivo ad, dia d, arqueocaja a, mediopago m "
                    + "WHERE d.fecha = a.fecha AND a.arqueocaja_id = ad.arqueocaja_id AND d.estacion_id = a.estacion_id AND ad.mediopago_id = m.mediopago_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "ORDER BY 2";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Mediopago(rst.getInt(1), rst.getString(2), rst.getInt(3), null, 0, null, false, null));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    private Map<String, Double> getTotalxDia(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        Map<String, Double> result = new HashMap();
        try {
            //determinar monto total por dia
            query = "SELECT fecha, SUM(monto) "
                    + "FROM ( "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, SUM(ad.monto) monto "
                    + "    FROM dia d, arqueocaja a, arqueocaja_detalle ad "
                    + "    WHERE d.fecha = a.fecha AND a.arqueocaja_id = ad.arqueocaja_id AND d.estacion_id = a.estacion_id "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy')  "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                    + "        UNION "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, 0 monto "
                    + "    FROM dia d "
                    + "    WHERE NOT EXISTS (SELECT * FROM arqueocaja a, arqueocaja_detalle ad WHERE d.fecha = a.fecha AND a.arqueocaja_id = ad.arqueocaja_id AND d.estacion_id = a.estacion_id ) "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "        UNION "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, SUM(e.monto) monto  "
                    + "    FROM dia d, arqueocaja a, efectivo e "
                    + "    WHERE d.fecha = a.fecha AND a.arqueocaja_id = e.arqueocaja_id AND d.estacion_id = a.estacion_id "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy')  "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                    + "        UNION "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, 0 monto "
                    + "    FROM dia d "
                    + "    WHERE NOT EXISTS (SELECT * FROM arqueocaja a, efectivo e WHERE d.fecha = a.fecha AND a.arqueocaja_id = e.arqueocaja_id AND d.estacion_id = a.estacion_id ) "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "    ) tmptab "
                    + "GROUP BY fecha ORDER BY 1";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));

            pst.setInt(7, estacionId);
            pst.setString(8, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(9, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(10, estacionId);
            pst.setString(11, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(12, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.put(rst.getString(1), rst.getDouble(2));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDMediospagoByFechaEstacion(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        List<String[]> result = new ArrayList();
        try {
            List<Mediopago> mediospago = getMTDMediospagoByFechaEstacion_distintos(fechaInicial, fechaFinal, estacionId);
            String mediospagoIds = "";
            for (Mediopago m : mediospago) {
                mediospagoIds += (mediospagoIds.isEmpty()) ? m.getMediopagoId() : ",".concat(m.getMediopagoId().toString());
            }
            ResultSet rst;
            Map<String, Double> totalXdia = getTotalxDia(fechaInicial, fechaFinal, estacionId);
            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, m.nombre, SUM(ad.monto) "
                    + "FROM dia d, arqueocaja a, arqueocaja_detalle ad, mediopago m, mediopago_pais mp, estacion e "
                    + "WHERE d.fecha = a.fecha AND a.arqueocaja_id = ad.arqueocaja_id AND ad.mediopago_id = m.mediopago_id AND d.estacion_id = e.estacion_id AND a.estacion_id = e.estacion_id AND mp.pais_id = e.pais_id AND mp.mediopago_id = m.mediopago_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), m.nombre "
                    + " UNION "
                    + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, m.nombre, 0 "
                    + "FROM dia d, mediopago m  "
                    + "WHERE d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "AND m.mediopago_id IN (" + mediospagoIds + ") "
                    + "AND NOT EXISTS ( SELECT * FROM arqueocaja_detalle ad, arqueocaja a WHERE ad.mediopago_id = m.mediopago_id AND a.arqueocaja_id = ad.arqueocaja_id AND d.fecha = a.fecha AND d.estacion_id = a.estacion_id) "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), m.nombre "
                    + "ORDER BY 1, 2";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
            rst = pst.executeQuery();
            String fecha = "", tmpString;
            List<String> tmplist = new ArrayList();
            Double percent;
            while (rst.next()) {
                if (!fecha.isEmpty() && !fecha.equals(rst.getString(1))) {
                    tmpString = (totalXdia.get(fecha) != null) ? totalXdia.get(fecha).toString() : "0";
                    tmplist.add(0, tmpString);    //total x dia
                    tmplist.add(0, fecha);
                    result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
                    fecha = rst.getString(1);
                    tmplist = new ArrayList();
                } else if (fecha.isEmpty()) {
                    fecha = rst.getString(1);
                }
                percent = (totalXdia.get(fecha) != null && totalXdia.get(fecha) > 0) ? Double.parseDouble(rst.getString(3)) / totalXdia.get(fecha) : 0D;
                tmplist.add(rst.getString(3));
                tmplist.add(decFmt2.format(percent));    //porcentaje del mp respecto totalDia
            }
            if (!fecha.isEmpty()) { //si entro en el while
                tmpString = (totalXdia.get(fecha) != null) ? totalXdia.get(fecha).toString() : "0";
                tmplist.add(0, tmpString);    //total x dia
                tmplist.add(0, fecha);
                result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDEfectivoByFechaEstacion(Date fechaInicial, Date fechaFinal, Integer estacionId) {
        List<String[]> result = new ArrayList();
        try {
            List<Mediopago> mediospagoE = getMTDMediospagoEfectivoByFechaEstacion_distintos(fechaInicial, fechaFinal, estacionId);
            String mediospagoIds = "";
            for (Mediopago m : mediospagoE) {
                mediospagoIds += (mediospagoIds.isEmpty()) ? m.getMediopagoId() : ",".concat(m.getMediopagoId().toString());
            }
            //determinar monto total por dia
            Map<String, Double> totalXdia = getTotalxDia(fechaInicial, fechaFinal, estacionId);

            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, m.nombre, SUM(ad.monto) "
                    + "FROM dia d, arqueocaja a, efectivo ad, mediopago m, mediopago_pais mp, estacion e "
                    + "WHERE d.fecha = a.fecha AND a.arqueocaja_id = ad.arqueocaja_id AND ad.mediopago_id = m.mediopago_id AND d.estacion_id = e.estacion_id AND a.estacion_id = e.estacion_id AND mp.pais_id = e.pais_id AND mp.mediopago_id = m.mediopago_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), m.nombre "
                    + " UNION "
                    + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, m.nombre, 0 "
                    + "FROM dia d, mediopago m  "
                    + "WHERE d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "AND m.mediopago_id IN (" + mediospagoIds + ") "
                    + "AND NOT EXISTS ( SELECT * FROM efectivo ad, arqueocaja a WHERE ad.mediopago_id = m.mediopago_id AND a.arqueocaja_id = ad.arqueocaja_id AND d.fecha = a.fecha AND d.estacion_id = a.estacion_id ) "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), m.nombre "
                    + "ORDER BY 1, 2";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            String fecha = "", tmpString;
            List<String> tmplist = new ArrayList();
            Double percent;
            while (rst.next()) {
                if (!fecha.isEmpty() && !fecha.equals(rst.getString(1))) {
//                    tmpString = (totalXdia.get(fecha) != null) ? totalXdia.get(fecha).toString() : "0";
//                    tmplist.add(0, tmpString);    //total x dia
                    tmplist.add(0, fecha);
                    result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
                    fecha = rst.getString(1);
                    tmplist = new ArrayList();
                } else if (fecha.isEmpty()) {
                    fecha = rst.getString(1);
                }
                percent = (totalXdia.get(fecha) != null && totalXdia.get(fecha) > 0) ? Double.parseDouble(rst.getString(3)) / totalXdia.get(fecha) : 0D;
                tmplist.add(rst.getString(3));
                tmplist.add(decFmt2.format(percent));    //porcentaje del mp respecto totalDia
            }
            if (!fecha.isEmpty()) { //si entro en el while
//                tmpString = (totalXdia.get(fecha) != null) ? totalXdia.get(fecha).toString() : "0";
//                tmplist.add(0, tmpString);    //total x dia
                tmplist.add(0, fecha);
                result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDInventarioByFechaEstacion(Date fechaInicial, Date fechaFinal, Integer estacionId, String invType) {
        List<String[]> result = new ArrayList();
        try {
            String productosIds = "";
            List<Producto> productos = getMTDProductos(fechaInicial, fechaFinal, estacionId);
            for (Producto p : productos) {
                productosIds += (productosIds.isEmpty()) ? p.getProductoId().toString() : ",".concat(p.getProductoId().toString());
            }
            if (invType.equals("FINAL")) {
                query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, ic.producto_id, ic.final ";
            } else if (invType.equals("COMPRAS")) {
                query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, ic.producto_id, ic.compras ";
            }
            query = query
                    + "FROM inventario_coco ic, dia d "
                    + "WHERE d.fecha = ic.fecha AND d.estacion_id = ic.estacion_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + " UNION "
                    + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, p.producto_id, 0 "
                    + "FROM dia d, producto p "
                    + "WHERE NOT EXISTS ( SELECT * FROM inventario_coco ic WHERE d.fecha = ic.fecha AND d.estacion_id = ic.estacion_id AND ic.producto_id = p.producto_id) "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND p.producto_id IN (" + productosIds + ") "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), p.producto_id "
                    + "ORDER BY 1, 2";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            String fecha = "";
            List<String> tmplist = new ArrayList();
            Double total = 0D, diff = 0D, lastTotal = 0D;
            while (rst.next()) {
                if (!fecha.isEmpty() && !fecha.equals(rst.getString(1))) {
                    tmplist.add(0, fecha);
                    tmplist.add(total.toString());
                    diff = (total > 0 && lastTotal > 0) ? (total - lastTotal) / lastTotal : 0D;
                    tmplist.add(decFmt2.format(diff));
                    result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
                    fecha = rst.getString(1);
                    tmplist = new ArrayList();
                    lastTotal = total;
                    total = 0D;
                } else if (fecha.isEmpty()) {
                    fecha = rst.getString(1);
                }
                tmplist.add(rst.getString(3));
                total += rst.getDouble(3);
            }
            if (!fecha.isEmpty()) { //si entro en el while
                tmplist.add(0, fecha);
                tmplist.add(total.toString());
                diff = (total > 0 && lastTotal > 0) ? (total - lastTotal) / lastTotal : 0D;
                tmplist.add(decFmt2.format(diff));
                result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDCalibraciones(Date fechaInicial, Date fechaFinal, Integer estacionId, String productsIds) {
        List<String[]> result = new ArrayList();
        try {
            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, pro.producto_id, SUM(ld.calibracion) "
                    + "FROM dia d, turno t, producto pro, lectura l, lectura_detalle ld, estacion e "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id  AND d.estacion_id = e.estacion_id AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id AND ld.producto_id = pro.producto_id "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), pro.producto_id "
                    //                    + "ORDER BY 1, pro.producto_id"
                    + " UNION "
                    + "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, pro.producto_id, 0 "
                    + "FROM dia d, turno t, producto pro, lectura l, estacion e "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id  AND d.estacion_id = e.estacion_id AND t.turno_id = l.turno_id "
                    + "AND NOT EXISTS (SELECT * FROM lectura_detalle ld WHERE l.lectura_id = ld.lectura_id AND ld.producto_id = pro.producto_id) "
                    + "AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND pro.producto_id in (" + productsIds + ") "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), pro.producto_id "
                    + "ORDER BY 1, 2";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            String fecha = "";
            List<String> tmplist = new ArrayList();
            Double total = 0D, diff = 0D, lastTotal = 0D;
            while (rst.next()) {
                if (!fecha.isEmpty() && !fecha.equals(rst.getString(1))) {
                    tmplist.add(0, fecha);
                    tmplist.add((total == 0) ? "0" : total.toString());
                    diff = (total > 0 && lastTotal > 0) ? (total - lastTotal) / lastTotal : 0D;
                    tmplist.add((diff == 0) ? "0" : decFmt2.format(diff));
                    result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
                    fecha = rst.getString(1);
                    tmplist = new ArrayList();
                    lastTotal = total;
                    total = 0D;
                } else if (fecha.isEmpty()) {
                    fecha = rst.getString(1);
                }
                tmplist.add(rst.getString(3));
                total += rst.getDouble(3);
            }
            if (!fecha.isEmpty()) { //si entro en el while
                tmplist.add(0, fecha);
                tmplist.add((total == 0) ? "0" : total.toString());
                diff = (total > 0 && lastTotal > 0) ? (total - lastTotal) / lastTotal : 0D;
                tmplist.add((diff == 0) ? "0" : decFmt2.format(diff));
                result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDFaltanteSobrante(Date fechaInicial, Date fechaFinal, Integer estacionId, String productsIds) {
        List<String[]> result = new ArrayList();
        try {
            //Ventas
            query = "SELECT dummy.fecha, SUM(dummy.monto)  "
                    + "FROM ( "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, SUM(p.precio * (ld.lectura_final - ld.lectura_inicial - ld.calibracion)) monto  "
                    + "    FROM dia d, turno t, precio p, lectura l, lectura_detalle ld  "
                    + "    WHERE d.estacion_id = t.estacion_id AND d.fecha = t.fecha AND t.turno_id = p.turno_id AND l.estacion_id = t.estacion_id AND l.turno_id = t.turno_id "
                    + "    AND ld.lectura_id = l.lectura_id AND ld.estacion_id = d.estacion_id AND ld.producto_id = p.producto_id AND ld.tipodespacho_id = p.tipodespacho_id "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                    + "        UNION "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, SUM(ap.monto) monto "
                    + "    FROM dia d, turno t, arqueocaja a, arqueocaja_producto ap  "
                    + "    WHERE d.estacion_id = t.estacion_id AND d.fecha = t.fecha AND a.estacion_id = t.estacion_id AND a.turno_id = t.turno_id AND a.fecha = t.fecha "
                    + "    AND a.arqueocaja_id = ap.arqueocaja_id   "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                    + "        UNION "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, 0 monto  "
                    + "    FROM dia d "
                    + "    WHERE NOT EXISTS( SELECT * FROM turno t WHERE d.estacion_id = t.estacion_id AND d.fecha = t.fecha ) "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                    + ") dummy "
                    + "GROUP BY dummy.fecha "
                    + "ORDER BY 1";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(7, estacionId);
            pst.setString(8, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(9, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            Map<String, Double> ventas = new HashMap();
            while (rst.next()) {
                ventas.put(rst.getString(1), rst.getDouble(2));
            }
            try { pst.close(); } catch (Exception ignore) { }

            //Ingresos
            query = "SELECT dtable.fecha, SUM(dtable.monto) "
                    + "FROM ( "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, SUM (ad.monto) monto "
                    + "    FROM dia d, turno t, arqueocaja a, arqueocaja_detalle ad  "
                    + "    WHERE d.estacion_id = t.estacion_id AND d.fecha = t.fecha AND a.estacion_id = t.estacion_id AND a.turno_id = t.turno_id AND a.fecha = t.fecha "
                    + "    AND a.arqueocaja_id = ad.arqueocaja_id   "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                    + "        UNION "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, SUM(e.monto) monto "
                    + "    FROM dia d, turno t, arqueocaja a, efectivo e  "
                    + "    WHERE d.estacion_id = t.estacion_id AND d.fecha = t.fecha AND a.estacion_id = t.estacion_id AND a.turno_id = t.turno_id AND a.fecha = t.fecha "
                    + "    AND a.arqueocaja_id = e.arqueocaja_id  "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                    + "        UNION "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, 0 monto "
                    + "    FROM dia d  "
                    + "    WHERE NOT EXISTS ( SELECT * FROM turno t WHERE d.estacion_id = t.estacion_id AND d.fecha = t.fecha )  "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy') "
                    + ") dtable "
                    + "GROUP BY dtable.fecha "
                    + "ORDER BY 1";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(7, estacionId);
            pst.setString(8, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(9, sdf_ddmmyyyy.format(fechaFinal));
            rst = pst.executeQuery();
            Map<String, Double> ingresos = new HashMap();
            while (rst.next()) {
                ingresos.put(rst.getString(1), rst.getDouble(2));
            }

            Double diferencia, lastTotal = 0D;
            String[] data;
            for (Entry<String, Double> item : ventas.entrySet()) {
                diferencia = ingresos.get(item.getKey()) - item.getValue();
                lastTotal = (diferencia != 0 && lastTotal != 0) ? (diferencia - lastTotal) / lastTotal : 0D;
                if (diferencia > 0) {
                    data = new String[]{item.getKey(), decFmt2.format(diferencia), null, decFmt2.format(diferencia), decFmt2.format(lastTotal)};
                } else {
                    data = new String[]{item.getKey(), null, decFmt2.format(diferencia), decFmt2.format(diferencia), decFmt2.format(lastTotal)};
                }
                lastTotal = diferencia;
                result.add(data);
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getMTDFaltanteSobranteVol(Date fechaInicial, Date fechaFinal, Integer estacionId, String productsIds) {
        List<String[]> result = new ArrayList();
        try {
            //La calibracion NO se toma en cuenta en la primera parte, porque se anula cuando se integra con el inventario
            query = "SELECT dummy.fecha, dummy.producto_id, SUM(dummy.monto) "
                    + "FROM ( "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, pro.producto_id, SUM(ld.lectura_finaL - ld.lectura_inicial "
                    + "- ld.calibracion"
                    + "         ) monto  "
                    + "    FROM dia d, turno t, producto pro, lectura l, lectura_detalle ld  "
                    + "    WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id AND ld.producto_id = pro.producto_id  "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), pro.producto_id  "
                    + "        UNION "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, pro.producto_id, 0 monto  "
                    + "    FROM dia d, producto pro  "
                    + "    WHERE NOT EXISTS (SELECT * FROM lectura_detalle ld, turno t, lectura l WHERE l.lectura_id = ld.lectura_id AND ld.producto_id = pro.producto_id AND d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND t.turno_id = l.turno_id)  "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND pro.producto_id IN (" + productsIds + ")  "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), pro.producto_id  "
                    + "        UNION "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, ic.producto_id, -SUM(ic.inicial + NVL(ic.compras, 0) - ic.final "
                    //                    + "- <calibracion> "
                    + "         ) monto  "
                    + "    FROM dia d, inventario_coco ic "
                    + "    WHERE d.fecha = ic.fecha AND d.estacion_id = ic.estacion_id  "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), ic.producto_id  "
                    + "        UNION "
                    + "    SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, p.producto_id, -0 monto  "
                    + "    FROM dia d, producto p "
                    + "    WHERE NOT EXISTS ( SELECT * FROM inventario_coco ic WHERE d.fecha = ic.fecha AND d.estacion_id = ic.estacion_id )  "
                    + "    AND d.estacion_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') AND p.producto_id IN (" + productsIds + ") "
                    + "    GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), p.producto_id  "
                    + ") dummy "
                    + "GROUP BY dummy.fecha, dummy.producto_id  "
                    + "ORDER BY 1, 2";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(4, estacionId);
            pst.setString(5, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(6, sdf_ddmmyyyy.format(fechaFinal));

            pst.setInt(7, estacionId);
            pst.setString(8, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(9, sdf_ddmmyyyy.format(fechaFinal));
            pst.setInt(10, estacionId);
            pst.setString(11, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(12, sdf_ddmmyyyy.format(fechaFinal));

            ResultSet rst = pst.executeQuery();
            String fecha = "";
            Map<Integer, Double> prodVals = new HashMap();
            List<String> tmplist = new ArrayList();
            Double diff = 0D;
            while (rst.next()) {
                if (!fecha.isEmpty() && !fecha.equals(rst.getString(1))) {
                    tmplist.add(0, fecha);
                    result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
                    fecha = rst.getString(1);
                    tmplist = new ArrayList();
                } else if (fecha.isEmpty()) {
                    fecha = rst.getString(1);
                }
                tmplist.add(rst.getString(3));
                diff = (prodVals.containsKey(rst.getInt(2))) ? prodVals.get(rst.getInt(2)) : 0D;
                diff = (diff != 0 && rst.getDouble(3) != 0) ? (rst.getDouble(3) - diff) / diff : 0D;
                prodVals.put(rst.getInt(2), rst.getDouble(3));
                tmplist.add(decFmt2.format(diff));
            }
            if (!fecha.isEmpty()) { //si entro en el while
                tmplist.add(0, fecha);
                result.add(Arrays.copyOf(tmplist.toArray(), tmplist.size(), String[].class));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getDataMovimiento(Date fechaInicial, Date fechaFinal, Integer paisId) {
        List<String[]> result = new ArrayList();
        try {
//            List<String[]> dataVolumen = getMTDVolumenes(fechaInicial, fechaFinal, estacionId, productsIds);
            List<String[]> dataVolumen = new ArrayList();
            query = "SELECT TO_CHAR(d.fecha, 'dd/mm/yyyy') fecha, e.nombre, pro.producto_id, SUM(ld.lectura_finaL - ld.lectura_inicial - ld.calibracion) "
                    + "FROM dia d, turno t, producto pro, lectura l, lectura_detalle ld, estacion e "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id  AND d.estacion_id = e.estacion_id AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id AND ld.producto_id = pro.producto_id "
                    + "AND e.pais_id = ? AND d.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "GROUP BY TO_CHAR(d.fecha, 'dd/mm/yyyy'), e.nombre, pro.producto_id "
                    + "ORDER BY 2, pro.producto_id, 1";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, paisId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                //fecha; estacion; producto_id; venta
                dataVolumen.add(new String[]{rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4)});
            }
            try { pst.close(); } catch (Exception ignore) { }

            query = "SELECT e.deposito, e.nombre, p.nombre, TO_CHAR(ic.fecha, 'dd/mm/yyyy'), ic.inicial, ic.compras, 0, ic.final"
                    + ", p.producto_id "
                    + "FROM inventario_coco ic, estacion e, producto p "
                    + "WHERE ic.estacion_id = e.estacion_id AND ic.producto_id = p.producto_id "
                    + "AND e.pais_id = ? AND ic.fecha BETWEEN TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') "
                    + "ORDER BY 2, p.producto_id, ic.fecha";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, paisId);
            pst.setString(2, sdf_ddmmyyyy.format(fechaInicial));
            pst.setString(3, sdf_ddmmyyyy.format(fechaFinal));
            rst = pst.executeQuery();
            String fecha, estacion, prodId, venta;
            Double invContable, merma;
            while (rst.next()) {
                fecha = rst.getString(4);
                estacion = rst.getString(2);
                prodId = rst.getString(9);
                venta = "0";
                for (String[] dvn : dataVolumen) {
                    if (fecha.equals(dvn[0]) && estacion.equals(dvn[1]) && prodId.equals(dvn[2])) {
                        venta = dvn[3];
                        break;
                    }
                }
                invContable = rst.getDouble(5) + rst.getDouble(6) - Double.parseDouble(venta);
                merma = rst.getDouble(8) - invContable;
                result.add(new String[]{rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6), venta, rst.getString(8), invContable.toString(), merma.toString()});
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

}
