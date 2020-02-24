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
public class SvcTipoDespacho extends DaoImp {

    private String query;
    public SvcTipoDespacho() {
    }

    public Map<Integer, String> getTiposDespachoMap() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        ResultSet rst = null;
        try {
            query = "SELECT tipodespacho_id, nombre FROM tipodespacho";
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
                closeConnections(); //asg
            } catch (SQLException ex) {
            }
        }
        return result;
    }

}
