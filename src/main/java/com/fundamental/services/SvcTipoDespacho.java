package com.fundamental.services;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Henry Barrientos
 */
public class SvcTipoDespacho extends Dao {

    private String query;
    public SvcTipoDespacho() {
    }

    public Map<Integer, String> getTiposDespachoMap() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        try {
            query = "SELECT tipodespacho_id, nombre FROM tipodespacho";
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
