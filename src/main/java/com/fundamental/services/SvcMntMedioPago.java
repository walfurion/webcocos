package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.fundamental.model.Mediopago;
import com.sisintegrados.daoimp.DaoImp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Henry Barrientos
 */
public class SvcMntMedioPago extends DaoImp {

    private String query;

    public Mediopago doAction(String action, Mediopago mediopago) {
        Mediopago result = new Mediopago();
        ResultSet rst = null;
        try {
            if (action.equals(ACTION_ADD)) {
                query = "SELECT mediopago_seq.NEXTVAL FROM DUAL";
                pst = getConnection().prepareStatement(query);
                rst = pst.executeQuery();
                Integer mpId = (rst.next()) ? rst.getInt(1) : null;
                mediopago.setMediopagoId(mpId);
                closePst();

                query = "INSERT INTO mediopago (nombre, tipo, creado_por, mediopago_id, pais_id "
                        + ", orden, tipoprod_id, partidacont_por, partidacont, is_tcredito) "
                        + "VALUES (?, ?, ?, ?, ?"
                        + ", ?, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setString(1, mediopago.getNombre());
                pst.setInt(2, mediopago.getTipo());
                pst.setString(3, mediopago.getCreadoPor());
                pst.setInt(4, mediopago.getMediopagoId());
                pst.setInt(5, mediopago.getCountry().getPaisId());
                pst.setInt(6, mediopago.getOrden());
                pst.setInt(7, mediopago.getTipoprodId());
                pst.setDouble(8, mediopago.getPartidacontPor());
                pst.setInt(9, (mediopago.isPartidacont() ? 1 : 0));
                pst.setInt(10, (mediopago.isIsTCredito() ? 1 : 0));
                pst.executeUpdate();
            } else if (action.equals(ACTION_UPDATE)) {
                query = "UPDATE mediopago "
                        + "SET nombre = ?, tipo = ?, modificado_por = ?, modificado_el = SYSDATE, estado = ?"
                        + ", pais_id = ?, orden = ?, tipoprod_id = ?, partidacont_por = ?, partidacont = ?, is_tcredito = ? "
                        + "WHERE mediopago_id = ? ";
                pst = getConnection().prepareStatement(query);
                pst.setString(1, mediopago.getNombre());
                pst.setInt(2, mediopago.getTipo());
                pst.setString(3, mediopago.getModificadoPor());
                pst.setString(4, mediopago.getEstado());
                pst.setInt(5, mediopago.getCountry().getPaisId());
                pst.setInt(6, mediopago.getOrden());
                pst.setInt(7, mediopago.getTipoprodId());
                pst.setDouble(8, mediopago.getPartidacontPor());
                pst.setInt(9, (mediopago.isPartidacont() ? 1 : 0));
                pst.setInt(10, (mediopago.isIsTCredito() ? 1 : 0));
                pst.setInt(11, mediopago.getMediopagoId());
                pst.executeUpdate();
            } else if (action.equals(ACTION_DELETE)) {
                query = "UPDATE mediopago "
                        + "SET estado = 'I', modificado_por = ?, modificado_el = SYSDATE "
                        + "WHERE mediopago_id = ? ";
                pst = getConnection().prepareStatement(query);
                pst.setString(1, mediopago.getModificadoPor());
                pst.setInt(2, mediopago.getMediopagoId());
                pst.executeUpdate();
                try {
                    pst.close();
                } catch (Exception ignore) {
                }
            }
            result = mediopago;
        } catch (Exception exc) {
            mediopago.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                closePst();
                closeConnections(); //asg
            } catch (SQLException ex) {
                Logger.getLogger(SvcMntMedioPago.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
