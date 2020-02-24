package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.sisintegrados.daoimp.DaoImp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Henry Barrientos
 */
public class SvcArqueo extends DaoImp {

    private String query;

    public SvcArqueo() {
    }

    public boolean bombaTieneLecturaByTurnoid(Integer turnoId, Integer bombaId) {
        boolean result = false;
        ResultSet rst = null;
        try {
            query = "SELECT count(*) "
                    + "FROM lectura l, lectura_detalle ld "
                    + "WHERE l.lectura_id = ld.lectura_id AND l.turno_id = ? AND ld.bomba_id = ?";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, turnoId);
            pst.setInt(2, bombaId);
            rst = pst.executeQuery();
            rst.next();
            result = (rst.getInt(1) > 0);
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                pst.close();
                rst.close();
                closeConnections(); //asg
            } catch (SQLException ex) {
            }
        }
        return result;
    }

}
