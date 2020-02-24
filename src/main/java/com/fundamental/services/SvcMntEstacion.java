package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.fundamental.model.Bomba;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.Producto;
import com.fundamental.model.Rol;
import com.sisintegrados.daoimp.DaoImp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Henry Barrientos
 */
public class SvcMntEstacion extends DaoImp {

    private String query;

    String tmpString;

    public List<Producto> getAllProductos(boolean includeInactive) {
        List<Producto> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = (includeInactive) ? "" : " WHERE p.estado = 'A' ";
            query = "SELECT p.producto_id, p.nombre, p.codigo, p.estado, p.creado_por, p.orden_pos "
                    //                    + ", p.tipo_id "
                    + "FROM producto p "
                    + query
                    + " ORDER BY tipo_id";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getInt(6)));
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

    public Estacion doAction(String action, Estacion estacion) {
        Estacion result = new Estacion();
        Connection cnn = null;
        cnn = getConnection();
        ResultSet rst = null;
        try {
            cnn.setAutoCommit(false);
            if (action.equals(ACTION_ADD)) {
                query = "SELECT estacion_seq.NEXTVAL FROM DUAL";
                pst = cnn.prepareStatement(query);
                rst = pst.executeQuery();
                Integer estacionId = (rst.next()) ? rst.getInt(1) : null;
                estacion.setEstacionId(estacionId);
                closePst();
                rst.close();
                query = "INSERT INTO estacion (nombre, codigo, pais_id, creado_por, estacion_id, fact_electronica, bu, deposito, codigo_envoy, id_marca) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = cnn.prepareStatement(query);
                pst.setString(1, estacion.getNombre());
                pst.setString(2, estacion.getCodigo());
                pst.setInt(3, estacion.getPaisId());
                pst.setString(4, estacion.getCreadoPor());
                pst.setInt(5, estacion.getEstacionId());
                pst.setString(6, estacion.getFactElectronica());
                pst.setString(7, estacion.getBu());
                pst.setString(8, estacion.getDeposito());
                pst.setString(9, estacion.getCodigoEnvoy());
                pst.setInt(10, estacion.getIdMarca());
            } else if (action.equals(ACTION_UPDATE)) {
                query = "UPDATE estacion "
                        + "SET nombre = ?, codigo = ?, pais_id = ?,  modificado_por = ?, modificado_el = SYSDATE, fact_electronica = ?, bu = ?, deposito = ?, estado = ?, codigo_envoy = ?, id_marca = ? "
                        + "WHERE estacion_id = ?";
                pst = cnn.prepareStatement(query);
                pst.setString(1, estacion.getNombre());
                pst.setString(2, estacion.getCodigo());
                pst.setInt(3, estacion.getPaisId());
                pst.setString(4, estacion.getModificadoPor());
                pst.setString(5, estacion.getFactElectronica());
                pst.setString(6, estacion.getBu());
                pst.setString(7, estacion.getDeposito());
                pst.setString(8, estacion.getEstado());
                pst.setString(9, estacion.getCodigoEnvoy());
                pst.setInt(10, estacion.getIdMarca());
                pst.setInt(11, estacion.getEstacionId());
            }
            pst.executeUpdate();
            rst.close();
            closePst();

            if (action.equals(ACTION_ADD)) {
                query = "INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por, corr_pista) "
                        + "VALUES (?, ?, ?, ?)";
                for (Bomba b : estacion.getBombas()) {
                    pst = cnn.prepareStatement(query);
                    pst.setInt(1, b.getId());
                    pst.setInt(2, estacion.getEstacionId());
                    pst.setString(3, estacion.getCreadoPor());
                    pst.setInt(4, b.getCorrPista());
                    pst.executeUpdate();
                    closePst();
                }

                query = "INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) "
                        + "VALUES (?, ?, ?)";
                for (Producto p : estacion.getProductos()) {
                    pst = cnn.prepareStatement(query);
                    pst.setInt(1, estacion.getEstacionId());
                    pst.setInt(2, p.getProductoId());
                    pst.setString(3, estacion.getCreadoPor());
                    pst.executeUpdate();
                    closePst();
                }
            } else if (action.equals(ACTION_UPDATE)) {
                String itemIds = "";
                for (Bomba b : estacion.getBombas()) {
                    itemIds = (itemIds.isEmpty()) ? b.getId().toString() : itemIds + ", " + b.getId();
                }
//                query = "DELETE FROM bomba_estacion WHERE estacion_id = ? AND bomba_id NOT IN (" + itemIds + ")";
                query = "DELETE FROM bomba_estacion WHERE estacion_id = ?";
                pst = cnn.prepareStatement(query);
                pst.setInt(1, estacion.getEstacionId());
                pst.executeUpdate();    //Elimina los que ya no son necesarios
                closePst();
                query = "INSERT INTO bomba_estacion (bomba_id, estacion_id, creado_por, corr_pista) "
                        + "VALUES (?, ?, ?, ?)";
                for (Bomba b : estacion.getBombas()) {
                    pst = cnn.prepareStatement(query);
                    pst.setInt(1, b.getId());
                    pst.setInt(2, estacion.getEstacionId());
                    pst.setString(3, estacion.getCreadoPor());
                    pst.setInt(4, b.getCorrPista());
                    try {   //Inserta las nuevas asociaciones, las ya existentes, simplemente se ignoran.
                        pst.executeUpdate();
                    } catch (Exception ignore) {
                    }
                    closePst();
                }

                itemIds = "";
                for (Producto p : estacion.getProductos()) {
                    itemIds = (itemIds.isEmpty()) ? p.getProductoId().toString() : itemIds + ", " + p.getProductoId();
                }
                query = "DELETE FROM estacion_producto WHERE estacion_id = ? AND producto_id NOT IN (" + itemIds + ")";
                pst = cnn.prepareStatement(query);
                pst.setInt(1, estacion.getEstacionId());
                pst.executeUpdate();    //Elimina los que ya no son necesarios
                closePst();
                query = "INSERT INTO estacion_producto (estacion_id, producto_id, creado_por) "
                        + "VALUES (?, ?, ?)";
                for (Producto p : estacion.getProductos()) {
                    pst = cnn.prepareStatement(query);
                    pst.setInt(1, estacion.getEstacionId());
                    pst.setInt(2, p.getProductoId());
                    pst.setString(3, estacion.getCreadoPor());
                    try {   //Inserta las nuevas asociaciones, las ya existentes, simplemente se ignoran.
                        pst.executeUpdate();
                    } catch (Exception exc) {
                    }
                    closePst();
                }

            } else if (action.equals(ACTION_DELETE)) {
                query = "DELETE FROM bomba_estacion WHERE estacion_id = " + estacion.getEstacionId();
                pst = cnn.prepareStatement(query);
                pst.executeUpdate();
                closePst();

                query = "DELETE FROM estacion_producto WHERE estacion_id = " + estacion.getEstacionId();
                pst = cnn.prepareStatement(query);
                pst.executeUpdate();
                closePst();
            }

            result = estacion;
            cnn.commit();
        } catch (Exception exc) {
            try {
                cnn.rollback();
            } catch (Exception ignore) {
            }
            result.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                closePst();
                closeConnections(); //asg
            } catch (SQLException ex) {
                Logger.getLogger(SvcMntEstacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public boolean existeBU(String BU) {
        boolean result = false;
        ResultSet rst = null;
        try {
            miQuery = "SELECT bu "
                    + "FROM ESTACION "
                    + "WHERE bu = '" + BU + "'";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            result = rst.next();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                closePst();
                closeConnections(); //asg
            } catch (SQLException ex) {
                Logger.getLogger(SvcMntEstacion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    public boolean existeCodEnvoy(String CODIGO_ENVOY) {
        boolean result = false;
        ResultSet rst = null;
        try {
            miQuery = "SELECT CODIGO_ENVOY "
                    + "FROM ESTACION "
                    + "WHERE CODIGO_ENVOY = '" + CODIGO_ENVOY + "'";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            result = rst.next();
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
}
