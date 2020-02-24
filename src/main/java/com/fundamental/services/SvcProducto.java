package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.sisintegrados.daoimp.DaoImp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Henry Barrientos
 */
public class SvcProducto extends DaoImp {

    private String query;
    public SvcProducto() {
    }

    public Map<Integer, String> getProductosByEstacionidMap(Integer estacionId) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        ResultSet rst = null;
        try {
            query = "SELECT p.producto_id, p.nombre "
                    + "FROM estacion_producto ep, producto p "
                    + "WHERE ep.producto_id = p.producto_id AND ep.estacion_id = "+estacionId;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.put(rst.getInt(1), rst.getString(2));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                closePst();
                closeConnections();
            } catch (SQLException ex) {
                Logger.getLogger(SvcProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
