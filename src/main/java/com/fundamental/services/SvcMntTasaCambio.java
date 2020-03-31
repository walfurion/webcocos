package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.fundamental.model.TasaCambio;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.generic.bean.Pais;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ifred
 */
public class SvcMntTasaCambio extends DaoImp {

    private String query;

    public List<TasaCambio> getAllRates2(Integer paisid) {
        List<TasaCambio> result = new ArrayList();
        ResultSet rst = null;
        query = "SELECT t.tasacambio_id, t.pais_id, t.fecha_inicio, t.fecha_fin, t.tasa, t.creado_por, p.nombre "
                + "FROM tasacambio t, pais p WHERE t.pais_id = p.pais_id AND t.fecha_inicio >= ?"
                + " AND p.pais_id = " + paisid;
        try {
            Calendar todayMinusSix = Calendar.getInstance();
            todayMinusSix.add(Calendar.MONTH, -6);
            pst = getConnection().prepareStatement(query);
            pst.setDate(1, new java.sql.Date(todayMinusSix.getTimeInMillis()));
            rst = pst.executeQuery();
            TasaCambio cambio;
            while (rst.next()) {
                cambio = new TasaCambio(rst.getInt(1), rst.getInt(2), new java.util.Date(rst.getDate(3).getTime()), new java.util.Date(rst.getDate(4).getTime()), rst.getDouble(5), rst.getString(6));
                cambio.setPaisNombre(rst.getString(7));
                result.add(cambio);
            }
            rst.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
                closePst();
                closeConnections(); //asg
        }
        return result;
    }

    public List<TasaCambio> getAllRates() {
        List<TasaCambio> result = new ArrayList();
        query = "SELECT t.tasacambio_id, t.pais_id, t.fecha_inicio, t.fecha_fin, t.tasa, t.creado_por, p.nombre "
                + "FROM tasacambio t, pais p WHERE t.pais_id = p.pais_id AND t.fecha_inicio >= ?";
        ResultSet rst = null;
        try {
            Calendar todayMinusSix = Calendar.getInstance();
            todayMinusSix.add(Calendar.MONTH, -6);
            pst = getConnection().prepareStatement(query);
            pst.setDate(1, new java.sql.Date(todayMinusSix.getTimeInMillis()));
            rst = pst.executeQuery();
            TasaCambio cambio;
            while (rst.next()) {
                cambio = new TasaCambio(rst.getInt(1), rst.getInt(2), new java.util.Date(rst.getDate(3).getTime()), new java.util.Date(rst.getDate(4).getTime()), rst.getDouble(5), rst.getString(6));
                cambio.setPaisNombre(rst.getString(7));
                result.add(cambio);
            }
            rst.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
                closePst();
                closeConnections(); //asg
        }
        return result;
    }

    public List<Pais> getAllPaises2(Integer idusuario) {
        List<Pais> result = new ArrayList<Pais>();
        miQuery = "SELECT a.pais_id, a.nombre, a.codigo, a.moneda_simbolo, a.estado, a.vol_simbolo \n"
                + "FROM pais a, \n"
                + "estacion b, \n"
                + "estacion_usuario c \n"
                + "WHERE a.pais_id = b.pais_id \n"
                + "and b.ESTACION_ID = c.ESTACION_ID \n"
                + "and c.USUARIO_ID = " + idusuario;
        ResultSet rst = null;
        try {
            rst = getConnection().prepareStatement(miQuery).executeQuery();
            while (rst.next()) {
                result.add(new Pais(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(6), rst.getString(5), null));
            }
            rst.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
            closeConnections(); //asg
        }
        return result;
    }

//    public List<TasaCambio> getNewCambios(int year, int month) {
//        List<TasaCambio> result = new ArrayList();
//        query = "SELECT t.anio, t.mes, t.pais_id, t.tasa, p.nombre "
//                + "FROM pais p, tasacambio t "
//                + "WHERE p.pais_id = t.pais_id "
//                + "AND ( (t.anio = ? AND t.mes >= ?) "
//                + "OR (t.anio = ? AND t.mes <= ?) ) "
//                + "ORDER BY p.nombre, t.anio, t.mes ASC";
//        try {
//            int startMonth = 1 + (month - 3), startYear = year;
//            switch (month) {
//                case 2: {   //febrero
//                    startMonth = 12;
//                    startYear = year - 1;
//                    break;
//                }
//                case 1: {   //enero
//                    startMonth = 11;
//                    startYear = year - 1;
//                    break;
//                }
//            }
//            pst = getConnection().prepareStatement(query);
//            pst.setInt(1, startYear);
//            pst.setInt(2, startMonth);
//            pst.setInt(3, year);
//            pst.setInt(4, month);
//
//            ResultSet rst = pst.executeQuery();
//            TasaCambio cambio;
//            int indexId = 1;
//            while (rst.next()) {
//                cambio = new TasaCambio(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getDouble(4), null, null);
//                cambio.setPaisNombre(rst.getString(5));
//                cambio.setId(indexId++);
//                result.add(cambio);
//            }
//        } catch (Exception exc) {
//            exc.printStackTrace();
//        } finally {
//            try { pst.close(); } catch (Exception ignore) { }
//        }
//        return result;
//    }
    public TasaCambio doAction(String action, TasaCambio tasa) {
        TasaCambio result = new TasaCambio();
        ResultSet rst = null;
        try {
            if (action.equals(ACTION_ADD)) {
                query = "SELECT tasacambio_seq.NEXTVAL FROM DUAL";
                pst = getConnection().prepareStatement(query);
                rst = pst.executeQuery();
                int changeRateId = (rst.next()) ? rst.getInt(1) : 0;
                tasa.setTasacambioId(changeRateId);
                rst.close();
                closePst();
                closeConnections();

                query = "INSERT INTO tasacambio (tasa, creado_por, creado_el, pais_id, tasacambio_id, fecha_inicio, fecha_fin) "
                        + "VALUES (?, ?, SYSDATE, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setDouble(1, tasa.getTasa());
                pst.setString(2, tasa.getCreadoPor());
                pst.setInt(3, tasa.getPaisId());
                pst.setInt(4, changeRateId);
                pst.setDate(5, new java.sql.Date(tasa.getFechaInicio().getTime()));
                pst.setDate(6, new java.sql.Date(tasa.getFechaFin().getTime()));
                pst.executeUpdate();
                result = tasa;
                closePst();
                closeConnections();
            } else if (action.equals(ACTION_UPDATE)) {
                query = "UPDATE tasacambio "
                        + "SET tasa = ?, modificado_por = ?, pais_id = ?, modificado_el = SYSDATE, fecha_inicio = ?, fecha_fin = ? "
                        + "WHERE tasacambio_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setDouble(1, tasa.getTasa());
                pst.setString(2, tasa.getModificadoPor());
                pst.setInt(3, tasa.getPaisId());
                pst.setDate(4, new java.sql.Date(tasa.getFechaInicio().getTime()));
                pst.setDate(5, new java.sql.Date(tasa.getFechaFin().getTime()));
                pst.setInt(6, tasa.getTasacambioId());
                pst.executeUpdate();
                result = tasa;
                closePst();
                closeConnections();
            } else if (action.equals(ACTION_DELETE)) {
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            tasa.setDescError(exc.getMessage());
        } finally {
            closePst();
            closeConnections(); //asg
        }
        return result;
    }
}
