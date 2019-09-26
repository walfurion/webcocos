package com.fundamental.services;

import java.sql.ResultSet;

/**
 * @author Henry Barrientos
 */
public class SvcArqueo extends Dao {

    private String query;
    
    public SvcArqueo() {
    }
    
    public boolean bombaTieneLecturaByTurnoid(Integer turnoId, Integer bombaId) {
        boolean result = false;
        try {
            query = "SELECT count(*) "
                    + "FROM lectura l, lectura_detalle ld "
                    + "WHERE l.lectura_id = ld.lectura_id AND l.turno_id = ? AND ld.bomba_id = ?";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, turnoId);
            pst.setInt(2, bombaId);
            ResultSet rst = pst.executeQuery();
            rst.next();
            result = (rst.getInt(1) > 0);
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

}
