package com.fundamental.services;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Henry Barrientos
 */
public class SvcProducto extends Dao {

    private String query;
    public SvcProducto() {
    }

    public Map<Integer, String> getProductosByEstacionidMap(Integer estacionId) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        try {
            query = "SELECT p.producto_id, p.nombre "
                    + "FROM estacion_producto ep, producto p "
                    + "WHERE ep.producto_id = p.producto_id AND ep.estacion_id = "+estacionId;
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.put(rst.getInt(1), rst.getString(2));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

    
}
