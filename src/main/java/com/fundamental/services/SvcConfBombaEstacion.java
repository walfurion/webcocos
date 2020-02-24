package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.fundamental.model.EstacionConf;
import com.fundamental.model.EstacionConfHead;
import com.fundamental.utils.Constant;
import com.sisintegrados.daoimp.DaoImp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Henry Barrientos
 */
public class SvcConfBombaEstacion extends DaoImp {

    String query;

    public SvcConfBombaEstacion() {
    }

    public EstacionConfHead doAction(String action, EstacionConfHead conf) {
        EstacionConfHead result = new EstacionConfHead();
        ResultSet rst = null;
        Connection cnn = null;
        try {
//            getConnection().setAutoCommit(false);
              cnn = getConnection();
              cnn.setAutoCommit(false);
            if (action.equals(ACTION_ADD)) {
                query = "SELECT estacion_conf_head_seq.NEXTVAL FROM DUAL";
                pst = cnn.prepareStatement(query);
                rst = pst.executeQuery();
                conf.setEstacionconfheadId((rst.next()) ? rst.getInt(1) : 0);
                rst.close();
                pst.close();
                

                query = "INSERT INTO estacion_conf_head (estacionconfhead_id, nombre, estacion_id, estado, creado_por, creado_el, hora_inicio, hora_fin) "
                        + "VALUES (?, ?, ?, ?, ?, SYSDATE, ?, ?)";
                pst = cnn.prepareStatement(query);
                pst.setInt(1, conf.getEstacionconfheadId());
                pst.setString(2, conf.getNombre());
//                pst.setInt(3, conf.getEstacionId());
                pst.setObject(3, null);
                pst.setString(4, "A");
                pst.setString(5, conf.getCreadoPor());
                pst.setObject(6, conf.getHoraInicio());
                pst.setObject(7, conf.getHoraFin());
                pst.executeUpdate();
                closePst();

                int tipoDespacho;
                for (EstacionConf ecf : conf.getEstacionConf()) {
                    query = "INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por, creado_el, estacion_id) "
                            + "VALUES (?, ?, ?, ?, SYSDATE, ?)";
                    pst = cnn.prepareStatement(query);
                    pst.setInt(1, conf.getEstacionconfheadId());
                    pst.setInt(2, ecf.getBombaId());
                    tipoDespacho = ecf.getTypeServ().getId();   //(ecf.getAutoService()) ? 1 : 2;
                    pst.setInt(3, tipoDespacho);
                    pst.setString(4, conf.getCreadoPor());
                    pst.setInt(5, ecf.getEstacionId());
                    pst.executeUpdate();
                    closePst();
                }
                result = conf;
            } else if (action.equals(ACTION_UPDATE)) {

                query = "UPDATE estacion_conf_head "
                        + "SET nombre = ?, estado = ?, modificado_por = ?, modificado_el = SYSDATE, hora_inicio = ?, hora_fin = ? "
                        + "WHERE estacionconfhead_id = ?";// AND estacion_id = ?";
                pst = cnn.prepareStatement(query);
                pst.setString(1, conf.getNombre());
                pst.setString(2, conf.getEstado());
                pst.setString(3, conf.getCreadoPor());
                pst.setObject(4, conf.getHoraInicio());
                pst.setObject(5, conf.getHoraFin());
                pst.setInt(6, conf.getEstacionconfheadId());
//                pst.setInt(7, conf.getEstacionId());
//                pst.setObject(7, null);
                pst.executeUpdate();
                closePst();

                int tipoDespacho;

                query = "select * from estacion_conf where estacionconfhead_id = " + conf.getEstacionconfheadId() + " AND estacion_id = " + conf.getEstacionConf().get(0).getEstacionId();
                pst = cnn.prepareStatement(query);
                rst = pst.executeQuery();
                tipoDespacho = (rst.next()) ? 1 : 0;
                rst.close();
                pst.close();

                if (tipoDespacho == 1) {  //ya tiene datos

                    for (EstacionConf ecf : conf.getEstacionConf()) {
                        query = "UPDATE estacion_conf "
                                + "SET tipodespacho_id = ? "
                                + "WHERE estacionconfhead_id = ? AND bomba_id = ? AND estacion_id = ?";
                        pst = cnn.prepareStatement(query);
                        tipoDespacho = ecf.getTypeServ().getId(); //(ecf.getAutoService()) ? 1 : 2;
                        pst.setInt(1, tipoDespacho);
                        pst.setInt(2, conf.getEstacionconfheadId());
                        pst.setInt(3, ecf.getBombaId());
                        pst.setInt(4, ecf.getEstacionId());
                        pst.executeUpdate();
                        closePst();
                    }
                    result = conf;
                } else {
                    for (EstacionConf ecf : conf.getEstacionConf()) {
                        query = "INSERT INTO estacion_conf (estacionconfhead_id, bomba_id, tipodespacho_id, creado_por, creado_el, estacion_id) "
                                + "VALUES (?, ?, ?, ?, SYSDATE, ?)";
                        pst = cnn.prepareStatement(query);
                        pst.setInt(1, conf.getEstacionconfheadId());
                        pst.setInt(2, ecf.getBombaId());
                        tipoDespacho = ecf.getTypeServ().getId();   //(ecf.getAutoService()) ? 1 : 2;
                        pst.setInt(3, tipoDespacho);
                        pst.setString(4, conf.getCreadoPor());
                        pst.setInt(5, ecf.getEstacionId());
                        pst.executeUpdate();
                        closePst();
                    }
                    result = conf;
                }
//            } else if (action.equals(Dao.ACTION_DELETE)) {
//                query = "UPDATE estacion_conf_head "
//                        + "SET estado = ?, modificado_por = ?, modificado_el = SYSDATE "
//                        + "WHERE estacionconfhead_id = ?";
//                pst = getConnection().prepareStatement(query);
//                pst.setString(1, "I");
//                pst.setString(2, conf.getModificadoPor());
//                pst.setInt(3, conf.getEstacionconfheadId());
//                pst.executeUpdate();
//                result = conf;
            }
//            getConnection().commit();
        } catch (Exception exc) {
            try {
                cnn.rollback();
            } catch (Exception ignore) {
            }
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                closePst();
                closeConnections(); //asg
                cnn.close(); //asg
            } catch (SQLException ex) {
            }
        }
        return result;
    }

}
