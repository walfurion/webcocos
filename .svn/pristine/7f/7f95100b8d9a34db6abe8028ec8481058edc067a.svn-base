package com.fundamental.services;

import com.fundamental.model.Arqueocaja;
import com.fundamental.model.ArqueocajaBomba;
import com.fundamental.model.LecturaDetalle;
import com.fundamental.model.dto.DtoLectura;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class SvcChangeLastRead extends Dao {

    private String query;
    
    public SvcChangeLastRead() {
    }

    public List<ArqueocajaBomba> getArqueocajaBomba(Integer estacionId, Integer bombaId, Integer arqueoEstadoId) {
        List<ArqueocajaBomba> result = new ArrayList();
        try {
            query = (arqueoEstadoId!=null) ? " AND a.estado_id = " + arqueoEstadoId : "";
            query = "SELECT ab.arqueocaja_id, ab.bomba_id, ab.turno_id " +
                    "FROM turno t, arqueocaja a, arqueocaja_bomba ab " +
                    "WHERE t.turno_id = a.turno_id AND a.arqueocaja_id = ab.arqueocaja_id AND t.estacion_id = ? AND ab.bomba_id = ? "
//                    + "AND t.estado_id = 1 "
                    + query;
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setInt(2, bombaId);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new ArqueocajaBomba(rst.getInt(1), rst.getInt(2), rst.getInt(3)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }
    
    public List<LecturaDetalle> getUltimaLecturaByEstacionTurnoBomba(Integer estacionId, Integer turnoId, Integer bombaId) {
        List<LecturaDetalle> result = new ArrayList();
        try {
            query = "SELECT ld.lectura_id, ld.estacion_id, ld.bomba_id, ld.producto_id, ld.tipodespacho_id, ld.tipo, ld.lectura_inicial, ld.lectura_final, ld.total, ld.calibracion, p.nombre, b.nombre "
                    + "FROM lectura_detalle ld, producto p, lectura l, turno t, bomba b "
                    + "WHERE ld.producto_id = p.producto_id AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id AND ld.bomba_id = b.bomba_id "
//                    + "AND t.estado_id = ? "
                    + "AND ld.estacion_id = ? AND t.turno_id = ? AND ld.bomba_id = ? AND ld.tipo = 'M'";
            pst = getConnection().prepareStatement(query);
//            pst.setInt(1, turnoEstadoId);
            pst.setInt(1, estacionId);
            pst.setInt(2, turnoId);
            pst.setInt(3, bombaId);
            ResultSet rst = pst.executeQuery();
            LecturaDetalle lde;
            while (rst.next()) {
                lde = new LecturaDetalle(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getInt(4), rst.getInt(5),
                        rst.getString(6), rst.getDouble(7), rst.getDouble(8), rst.getDouble(9), rst.getDouble(10));
                lde.setNombreProducto(rst.getString(11));
                lde.setNombreBomba(rst.getString(12));
                result.add(lde);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

}
