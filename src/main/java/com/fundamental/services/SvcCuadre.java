package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.fundamental.model.Bomba;
import com.fundamental.model.Cliente;
import com.sisintegrados.generic.bean.Empleado;
import com.fundamental.model.Producto;
import com.fundamental.model.TasaCambio;
import com.fundamental.utils.Constant;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.generic.bean.GenericProduct;
import com.vaadin.ui.CheckBox;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Henry Barrientos
 */
public class SvcCuadre extends DaoImp {

    private String query;

    public SvcCuadre() {
    }

    public List<Bomba> getBombasCuadradasByTurnoid(Integer turnoId) {
        List<Bomba> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = "SELECT b.bomba_id, b.nombre, b.estado "
                    + "FROM arqueocaja_bomba acb, bomba b "
                    + "WHERE acb.bomba_id = b.bomba_id AND acb.turno_id = " + turnoId;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Bomba(rst.getInt(1), rst.getString(2), rst.getString(3), null, null, new CheckBox("", false)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public TasaCambio getTasacambioByPaisFecha(Integer paisId, Date myDate) {
        TasaCambio result = new TasaCambio();
        ResultSet rst = null;
        try {
            query = "SELECT t.tasacambio_id, t.pais_id, t.fecha_inicio, t.fecha_fin, t.tasa, t.creado_por "
                    + "FROM tasacambio t "
                    + "WHERE t.pais_id = ? AND t.fecha_inicio <= TO_DATE(?, 'dd/mm/yyyy') AND TO_DATE(?, 'dd/mm/yyyy') <= t.fecha_fin";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, paisId);
//            pst.setDate(2, new java.sql.Date(myDate.getTime()));
//            pst.setDate(3, new java.sql.Date(myDate.getTime()));
            pst.setString(2, Constant.SDF_ddMMyyyy.format(myDate));
            pst.setString(3, Constant.SDF_ddMMyyyy.format(myDate));
            rst = pst.executeQuery();
            result = (rst.next()) ? new TasaCambio(rst.getInt(1), rst.getInt(2), new java.util.Date(rst.getDate(3).getTime()), new java.util.Date(rst.getDate(4).getTime()), rst.getDouble(5), rst.getString(6)) : result;
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                closePst();
                closeConnections(); //asg
            } catch (SQLException ex) {
            }
        }
        return result;
    }

    public List<Producto> getCombustiblesByEstacionidPOS(Integer estacionId) {
        List<Producto> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = "SELECT p.producto_id, p.nombre, p.codigo, p.estado, p.orden_pos "
                    + "FROM estacion_producto ep, producto p "
                    + "WHERE ep.producto_id = p.producto_id AND p.tipo_id = 1 AND ep.estacion_id = " + estacionId
                    + " ORDER BY p.orden_pos";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), null, rst.getInt(5)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Producto> getProdAdicionalesByEstacionid(Integer estacionId) {
        List<Producto> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = "SELECT p.producto_id, p.nombre, p.codigo, p.estado, p.orden_pos "
                    + "FROM estacion_producto ep, producto p "
                    //                    + "WHERE ep.producto_id = p.producto_id AND p.tipo_id != 1 AND ep.estacion_id = "+estacionId;
                    + "WHERE ep.producto_id = p.producto_id AND p.tipo_id = 3 AND ep.estacion_id = " + estacionId;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), null, rst.getInt(5)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Cliente> getCustomersByStationidType(Integer estacionId, String type) {
        List<Cliente> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = "SELECT cliente_id, codigo, nombre, estacion_id, estado, creado_por, creado_el, codigo_envoy, cedula_juridica "
                    + "FROM cliente "
                    + "WHERE estacion_id = " + estacionId + " AND tipo = '" + type + "'";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Cliente(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5), rst.getString(6), null, null, rst.getString(8), rst.getString(9)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Producto> getLubricantsByCountryStation(boolean onlyActive, Integer countryId, Integer stationId) {
        List<Producto> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = Constant.SDF_ddMMyyyy.format(new java.util.Date());
            query = (onlyActive) ? " AND TO_DATE('" + query + "', 'dd/mm/yyyy') BETWEEN TRUNC(fecha_inicio) AND TRUNC(fecha_fin) " : "";
            query = "SELECT p.producto_id, p.nombre, p.codigo, p.tipo_id, p.orden_pos, p.estado, p.creado_por, p.creado_el, p.codigo_num, p.presentacion, p.codigo_barras, NVL(lub.precio, 0), p.ID_MARCA "
                    + "FROM producto p, estacion_producto pe, lubricanteprecio lub "
                    + "WHERE p.producto_id = pe.producto_id "//+ " AND pe.estacion_id = lub.estacion_id "
                    + "AND pe.producto_id = lub.producto_id "
                    + "AND p.tipo_id = 2 AND lub.pais_id = " + countryId
                    //+ " AND lub.estacion_id = " + stationId 
                    + " AND pe.estacion_id = " + stationId
                    + query
                    + " ORDER BY p.orden_pos";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            Producto producto;
            while (rst.next()) {
                producto = new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(6), rst.getString(7), rst.getInt(5));
                producto.setPrecio(rst.getDouble(12));
                result.add(producto);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Cliente> getCreditCards() {
        List<Cliente> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = "select mediopago_id, '1', nombre, 0, 'A', 'user', sysdate FROM mediopago WHERE estado = 'A' AND is_tcredito = 1";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Cliente(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5), rst.getString(6), null, null, null, null));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                closePst();
                closeConnections(); //asg
            } catch (SQLException ex) {
            }
        }
        return result;
    }

    public String[] getEmpleadoByEstacionTurnoEmpleado(int estacionId, int turnoId, int empleadoId) {
        String[] result = new String[]{"", ""};
        ResultSet rst = null;
        try {
            query = "SELECT l.nombre_pistero, l.nombre_jefe FROM lectura l WHERE l.estacion_id = ? AND l.turno_id = ? AND l.empleado_id = ?";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setInt(2, turnoId);
            pst.setInt(3, empleadoId);
            rst = pst.executeQuery();
            if (rst.next()) {
                result = new String[]{rst.getString(1), rst.getString(2)};
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                closePst();
                closeConnections(); //asg
            } catch (SQLException ex) {
            }
        }
        return result;
    }

    public List<Producto> getLubricantsGenericsCountryStation(Integer countryId, Integer stationId) {
        List<Producto> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = Constant.SDF_ddMMyyyy.format(new java.util.Date());
            //query = (onlyActive) ? " AND TO_DATE('" + query + "', 'dd/mm/yyyy') BETWEEN TRUNC(fecha_inicio) AND TRUNC(fecha_fin) " : "";
            query = "SELECT p.producto_id,\n"
                    + "         p.nombre,\n"
                    + "         p.codigo,\n"
                    + "         p.id_marca,\n"
                    + "         p.estado,\n"
                    + "         NVL (pr.precio, 0) precio\n"
                    + "    FROM producto         p,\n"
                    + "         lubricanteprecio pr,\n"
                    + "         estacion_producto ep,\n"
                    + "         estacion         e\n"
                    + "   WHERE     p.producto_id = pr.producto_id\n"
                    + "         AND e.pais_id = pr.pais_id\n"
                    + "         AND ep.estacion_id = e.estacion_id\n"
                    + "         AND ep.producto_id IN (9, 10)\n"
                    + "         AND TRUNC (SYSDATE) <= TRUNC (pr.fecha_fin)\n"
                    + "         AND e.estacion_id = " + stationId + " \n"
                    + "         AND p.ESTADO = 'A'\n"
                    + "         AND p.TIPO_ID = 2\n"
                    + "         AND e.pais_id = " + countryId + " \n"
                    + "GROUP BY p.producto_id,\n"
                    + "         p.nombre,\n"
                    + "         p.codigo,\n"
                    + "         p.estado,\n"
                    + "         NVL (pr.precio, 0),\n"
                    + "         p.id_marca ";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            Producto prod;
            while (rst.next()) {
                prod = new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5), rst.getDouble(6), null);
                prod.setPrecio(rst.getDouble(6));
                result.add(prod);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public int recuperaDisponibilidadInventario(Date fecha, int productoId) {
        ResultSet rst = null;
        int valor = 0;
        String fecha1 = Constant.SDF_ddMMyyyy.format(fecha);
        String fecha2 = Constant.SDF_ddMMyyyy.format(fecha);
        fecha1 = fecha2.substring(3, 5);
        fecha2 = fecha2.substring(6);
        try {
            query = " select inv_final DISPONIBILIDAD\n"
                    + " from compra_venta_lubricante\n"
                    + " where compra_id =(\n"
                    + " select max(compra_id) PRODUCTO\n"
                    + " from compra_venta_lubricante \n"
                    + " where to_char(fecha,'mm') = '" + fecha1 + "'\n"
                    + " and to_char(fecha,'yyyy') = '" + fecha2 + "'\n"
                    + " and producto_id = " + productoId + ") ";
            System.out.println("query " + query);
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                valor = rst.getInt(1);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (SQLException ex) {
            }
        }
        return valor;
    }
}
