package com.fundamental.services;

import com.fundamental.model.Arqueocaja;
import com.fundamental.model.ArqueocajaDetalle;
import com.fundamental.model.ArqueocajaProducto;
import com.fundamental.model.Bomba;
import com.fundamental.model.Efectivo;
import com.fundamental.model.Inventario;
import com.fundamental.model.Mediopago;
import com.fundamental.model.Producto;
import com.fundamental.model.dto.InventarioDto;
import com.fundamental.model.dto.RecepcionDto;
import com.fundamental.utils.Constant;
import com.sisintegrados.generic.bean.ArqueoTC;
import com.vaadin.ui.CheckBox;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class SvcTurnoCierre extends Dao {

    private String query;

    public SvcTurnoCierre() {
//        super();
    }

//    public List<Bomba> getBombasByArquoid(Integer arqueoId) {
    public List<Bomba> getBombasByArquoid(String arqueosIds) {
        List<Bomba> result = new ArrayList();
        try {
            query = "SELECT b.bomba_id, b.nombre, b.estado, b.creado_por "
                    + "FROM arqueocaja_bomba ab, bomba b "
                    + "WHERE ab.bomba_id = b.bomba_id AND ab.arqueocaja_id IN (" + arqueosIds + ") ORDER BY b.bomba_id";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Bomba(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), null, new CheckBox()));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<Producto> getProductoByArqueoid(String arqueoIds) {
        List<Producto> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = "SELECT p.producto_id, p.nombre, p.codigo, p.estado, p.creado_por, p.orden_pos, SUM(ap.monto) "
                    + "FROM arqueocaja_producto ap, producto p "
                    + "WHERE ap.producto_id = p.producto_id AND ap.arqueocaja_id in (" + arqueoIds + ") "
                    + "GROUP BY p.producto_id, p.nombre, p.codigo, p.estado, p.creado_por, p.orden_pos "
                    + "ORDER BY p.producto_id";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            Producto producto;
            while (rst.next()) {
                producto = new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), null, rst.getInt(6));
                producto.setValue(rst.getDouble(7));
                result.add(producto);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { rst.close(); pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<Mediopago> getMediopagoByArqueoid(String arqueoIds) {
        List<Mediopago> result = new ArrayList();
        try {
            query = "SELECT m.mediopago_id, m.nombre, m.tipo, SUM(ad.doctos), SUM(ad.monto) "
                    + "FROM arqueocaja_detalle ad, mediopago m "
                    + "WHERE ad.mediopago_id = m.mediopago_id AND ad.arqueocaja_id IN (" + arqueoIds + ") "
                    + "GROUP BY m.mediopago_id, m.nombre, m.tipo "
                    + "ORDER BY m.mediopago_id";
            System.out.println("query medio pago "+query);
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Mediopago mediopago;
            while (rst.next()) {
                mediopago = new Mediopago(rst.getInt(1), rst.getString(2), rst.getInt(3), null, 0, null, false, null);
                mediopago.setCantidad(rst.getInt(4));
                mediopago.setValue(rst.getDouble(5));
                result.add(mediopago);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<Mediopago> getEfectivoByArqueoid(String arqueoIds) {
        List<Mediopago> result = new ArrayList();
        try {
            query = "SELECT m.mediopago_id, m.nombre, m.tipo, e.orden, SUM(e.monto), e.efectivo_id "
                    + "FROM efectivo e, mediopago m "
                    + "WHERE e.mediopago_id = m.mediopago_id AND e.arqueocaja_id IN (" + arqueoIds + ") "
                    + "GROUP BY m.mediopago_id, m.nombre, m.tipo, e.orden, e.efectivo_id "
                    + "ORDER BY efectivo_id, m.mediopago_id";
            System.out.println("query efectivo "+query);
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Mediopago mediopago;
            while (rst.next()) {
                mediopago = new Mediopago(rst.getInt(1), rst.getString(2), rst.getInt(3), null, 0, null, false, null);
                mediopago.setCantidad(rst.getInt(4));
                mediopago.setValue(rst.getDouble(5));
                mediopago.setEfectivoId(rst.getInt(6));
                result.add(mediopago);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }
    
    public List<ArqueoTC> getArqueoTC(String arqueocajaId) {
        List<ArqueoTC> result = new ArrayList();
        try {
            query = "select ARQ.ARQUEOCAJA_ID, ARQ.LOTE, SUM(ARQ.MONTO), TAR.NOMBRE, TAR.TARJETA_ID "
                    + "from ARQUEOCAJA_TC ARQ, TARJETA TAR "
                    + "where ARQ.TARJETA_ID=TAR.TARJETA_ID and ARQUEOCAJA_ID IN (" + arqueocajaId + ") "
                    + "group by ARQ.ARQUEOCAJA_ID, ARQ.LOTE, TAR.NOMBRE, TAR.TARJETA_ID ";
            pst = getConnection().prepareStatement(query);
            System.out.println("entra a la tarjeta "+query);
            ResultSet rst = pst.executeQuery();
            ArqueoTC atc;
            while (rst.next()) {
                atc = new ArqueoTC(rst.getInt(1), rst.getString(2), rst.getDouble(3), rst.getString(4), rst.getInt(5));
                result.add(atc);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<ArqueocajaProducto> getArqueocajaProductos(Integer arqueocajaId) {
        List<ArqueocajaProducto> result = new ArrayList();
        try {
            query = "SELECT ap.arqueocaja_id, ap.producto_id, ap.monto, p.nombre "
                    + "FROM arqueocaja_producto ap, producto p "
                    + "WHERE ap.producto_id = p.producto_id AND ap.arqueocaja_id = " + arqueocajaId;
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            ArqueocajaProducto apo;
            while (rst.next()) {
                apo = new ArqueocajaProducto(rst.getInt(1), rst.getInt(2), rst.getDouble(3));
                apo.setNombreProducto(rst.getString(4));
                result.add(apo);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public Arqueocaja getArqueoByHorarioid(Integer horarioId) {
        Arqueocaja result = new Arqueocaja();
        try {
            query = "SELECT arqueocaja_id, estado_id "
                    + "FROM arqueocaja "
                    + "WHERE horario_id = " + horarioId;
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
//            if (rst.next()) {
//                result = new Arqueocaja(rst.getInt(1), rst.getInt(2), null, null);
//            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<ArqueocajaDetalle> getArqueoDetalleByHorarioid(Integer horarioId) {
        List<ArqueocajaDetalle> result = new ArrayList();
        try {
            query = "SELECT ad.arqueocaja_id, ad.mediopago_id, ad.doctos, ad.monto, mp.nombre "
                    + "FROM arqueocaja_detalle ad, mediopago mp, arqueocaja ac "
                    + "WHERE ad.mediopago_id = mp.mediopago_id AND ad.arqueocaja_id = ac.arqueocaja_id AND ac.horario_id = " + horarioId;
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            ArqueocajaDetalle ad;
            int count = 0;
            while (rst.next()) {
                ad = new ArqueocajaDetalle(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getDouble(4), null);
                ad.setNombreMedioPago(rst.getString(5));
                ad.setIdTable(count++);
                result.add(ad);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<Efectivo> getEfectivoByHorarioid(Integer horarioId) {
        List<Efectivo> result = new ArrayList();
        try {
            query
                    = //                "--SELECT ef.arqueocaja_id, ef.mediopago_id, ef.orden, ef.monto \n" +
                    "SELECT null, ef.mediopago_id, NULL, SUM(ef.monto), mp.nombre "
                    + "FROM efectivo ef, mediopago mp, arqueocaja ac "
                    + "WHERE ef.mediopago_id = mp.mediopago_id AND ac.arqueocaja_id = ef.arqueocaja_id AND ac.horario_id = " + horarioId
                    + " GROUP BY 1, ef.mediopago_id, 3, mp.nombre";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Efectivo efe;
            int count = 0;
            while (rst.next()) {
                efe = new Efectivo(null, rst.getInt(2), null, rst.getDouble(4));
                efe.setIdTable(count++);
                efe.setNombreMedioPago(rst.getString(5));
                result.add(efe);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<Producto> getProductoByTurnoid(String turnosIds) {
        List<Producto> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = "SELECT p.producto_id, p.nombre, p.codigo, p.estado, p.creado_por, p.orden_pos, SUM(ap.monto) "
                    + "FROM arqueocaja_producto ap, producto p, arqueocaja a "
                    + "WHERE ap.producto_id = p.producto_id AND ap.arqueocaja_id = a.arqueocaja_id AND a.turno_id IN (" + turnosIds + ") "
                    + "GROUP BY p.producto_id, p.nombre, p.codigo, p.estado, p.creado_por, p.orden_pos "
                    + "ORDER BY p.producto_id";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            Producto producto;
            while (rst.next()) {
                producto = new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), null, rst.getInt(6));
                producto.setValue(rst.getDouble(7));
                result.add(producto);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { rst.close(); pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<Mediopago> getMediopagoByTurnosid(String turnosIds) {
        List<Mediopago> result = new ArrayList();
        try {
            query = "SELECT m.mediopago_id, m.nombre, m.tipo, SUM(ad.doctos), SUM(ad.monto) "
                    + "FROM arqueocaja_detalle ad, mediopago m, arqueocaja a "
                    + "WHERE ad.mediopago_id = m.mediopago_id AND ad.arqueocaja_id = a.arqueocaja_id AND a.turno_id IN (" + turnosIds + ") "
                    + "GROUP BY m.mediopago_id, m.nombre, m.tipo "
                    + "ORDER BY m.mediopago_id";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Mediopago mediopago;
            while (rst.next()) {
                mediopago = new Mediopago(rst.getInt(1), rst.getString(2), rst.getInt(3), null, 0, null, false, null);
                mediopago.setCantidad(rst.getInt(4));
                mediopago.setValue(rst.getDouble(5));
                result.add(mediopago);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<Mediopago> getEfectivoByTurnosid(String turnosIds) {
        List<Mediopago> result = new ArrayList();
        try {
            query = "SELECT m.mediopago_id, m.nombre, m.tipo, e.orden, SUM(e.monto), e.efectivo_id "
                    + "FROM efectivo e, mediopago m, arqueocaja a "
                    + "WHERE e.mediopago_id = m.mediopago_id AND e.arqueocaja_id = a.arqueocaja_id AND a.turno_id IN (" + turnosIds + ") "
                    + "GROUP BY m.mediopago_id, m.nombre, m.tipo, e.orden, e.efectivo_id "
                    + "ORDER BY efectivo_id, m.mediopago_id";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Mediopago mediopago;
            while (rst.next()) {
                mediopago = new Mediopago(rst.getInt(1), rst.getString(2), rst.getInt(3), null, 0, null, false, null);
                mediopago.setCantidad(rst.getInt(4));
                mediopago.setValue(rst.getDouble(5));
                mediopago.setEfectivoId(rst.getInt(6));
                result.add(mediopago);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<String[]> getCalibracionByFechaEstacion(Date fecha, Integer estacionId) {
        List<String[]> result = new ArrayList();
        try {
            //calibraciones por dia
            query = "SELECT p.producto_id, SUM(ld.calibracion), SUM(ld.lectura_final - ld.lectura_inicial) "
                    + "FROM dia d, turno t, lectura l, lectura_detalle ld, producto p "
                    + "WHERE d.fecha = t.fecha AND d.estacion_id = t.estacion_id AND t.estacion_id = l.estacion_id AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id AND ld.producto_id = p.producto_id "
                    + "AND d.fecha = TO_DATE(?, 'dd/mm/yyyy') AND d.estacion_id = ? "
                    + "GROUP BY p.producto_id ORDER BY p.producto_id";
            pst = getConnection().prepareStatement(query);
            pst.setString(1, Constant.SDF_ddMMyyyy.format(fecha));
            pst.setInt(2, estacionId);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new String[]{rst.getString(1), rst.getString(2), rst.getString(3)});
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public List<InventarioDto> getInventarioByFechaEstacion(Date fecha, Integer estacionId) {
        List<InventarioDto> result = new ArrayList();
        try {
            //calibraciones por dia
            List<String[]> calibraciones = getCalibracionByFechaEstacion(fecha, estacionId);

            query = "SELECT 'rownum', p.nombre, i.fecha, i.estacion_id, i.producto_id, i.inicial, i.final, i.compras "
                    + "FROM inventario_coco i, producto p "
                    + "WHERE i.producto_id = p.producto_id AND i.fecha = TO_DATE(?, 'dd/mm/yyyy') AND i.estacion_id = ? "
                    + "ORDER BY p.producto_id";
            pst = getConnection().prepareStatement(query);
            pst.setString(1, Constant.SDF_ddMMyyyy.format(fecha));
            pst.setInt(2, estacionId);
            ResultSet rst = pst.executeQuery();
            InventarioDto invdto;
            int rownum = 0;
            while (rst.next()) {
                invdto = new InventarioDto(rownum++, rst.getString(2), 0D, rst.getDate(3), rst.getInt(4), rst.getInt(5), rst.getDouble(6), rst.getDouble(7), rst.getDouble(8), null, null);
                invdto.setInicialDto(invdto.getInicial());
                invdto.setFinallDto(invdto.getFinall());
                invdto.setComprasDto(invdto.getCompras());
                for (String[] item : calibraciones) {
                    if (item[0].equals(rst.getString(5))) {
                        invdto.setCalibracion(Double.parseDouble(item[1]));
                        invdto.setVentasCons(Double.parseDouble(item[2]));
                        break;
                    }
                }
                result.add(invdto);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    public Inventario doActionInventario(String action, Inventario inventario) {
        Inventario result = new Inventario();
        try {
            if (action.equals(Dao.ACTION_ADD)) {
                query = "INSERT INTO inventario_coco (inicial, final, compras, creado_por, creado_persona, creado_el, fecha, estacion_id, producto_id"
                        + ", inv_fisico, lectura_veeder, compartimiento, vol_facturado, pulgadas, galones_cisterna) "
                        + "VALUES (?, ?, ?, ?, ?, SYSDATE, ?, ?, ?"
                        + ", ?, ?, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setDouble(1, inventario.getInicial());
                pst.setDouble(2, inventario.getFinall());
                pst.setObject(3, inventario.getCompras());
                pst.setString(4, inventario.getCreadoPor());
                pst.setString(5, inventario.getCreadoPersona());
                pst.setDate(6, (java.sql.Date) inventario.getFecha());
                pst.setInt(7, inventario.getEstacionId());
                pst.setInt(8, inventario.getProductoId());
pst.setObject(9, inventario.getInventarioFisico());
pst.setObject(10, inventario.getLecturaVeederRoot());
pst.setObject(11, inventario.getCompartimiento());
pst.setObject(12, inventario.getVolFacturado());
pst.setObject(13, inventario.getPulgadas());
pst.setObject(14, inventario.getGalonesCisterna());
                pst.executeUpdate();
            } else if (action.equals(Dao.ACTION_UPDATE)) {
                query = "UPDATE inventario_coco "
                        + "SET final = ?, compras = ?, modificado_por = ?, modificado_persona = ?, modificado_el = SYSDATE "
                        + " , inv_fisico = ?, lectura_veeder = ?, compartimiento = ?, vol_facturado = ?, pulgadas = ?, galones_cisterna = ? "
                        + "WHERE fecha = ? AND estacion_id = ? AND producto_id = ?";
                pst = getConnection().prepareStatement(query);
//                pst.setDouble(1, inventario.getInicial());
                pst.setDouble(1, inventario.getFinall());
                pst.setObject(2, inventario.getCompras());
                pst.setString(3, inventario.getModificadoPor());
                pst.setString(4, inventario.getModificadoPersona());
pst.setObject(5, inventario.getInventarioFisico());
pst.setObject(6, inventario.getLecturaVeederRoot());
pst.setObject(7, inventario.getCompartimiento());
pst.setObject(8, inventario.getVolFacturado());
pst.setObject(9, inventario.getPulgadas());
pst.setObject(10, inventario.getGalonesCisterna());
                //pk
                pst.setDate(11, (java.sql.Date) inventario.getFecha());
                pst.setInt(12, inventario.getEstacionId());
                pst.setInt(13, inventario.getProductoId());
                pst.executeUpdate();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }
    
    public RecepcionDto doActionInvRecepcion(String action, RecepcionDto invrecep) {
        RecepcionDto result = new RecepcionDto();
        try {
            if (action.equals(Dao.ACTION_ADD)) {
                query = "INSERT INTO inv_recepcion (invrecepcion_id, fecha, pais_id, estacion_id, piloto, unidad, factura, creado_por) "
                        + "VALUES (inv_recepcion_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, new java.sql.Date(invrecep.getFecha().getTime()));
                pst.setObject(2, invrecep.getPais_id());
                pst.setObject(3, invrecep.getEstacion_id());
                pst.setObject(4, invrecep.getPiloto());
                pst.setObject(5, invrecep.getUnidad());
                pst.setObject(6, invrecep.getFactura());
                pst.setObject(7, invrecep.getCreado_por());
                pst.executeUpdate();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }
    
    
}
