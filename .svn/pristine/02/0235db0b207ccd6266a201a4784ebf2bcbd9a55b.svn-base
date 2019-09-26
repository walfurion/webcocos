package com.fundamental.services;

import com.fundamental.model.dto.DtoLectura;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class SvcBomba extends Dao {

    private String query;
    
    public SvcBomba() {
    }
 
    public List<DtoLectura> getUltimaLecturaByBomba(String tipo, Integer bombaId, Integer estacionId, Integer turnoId) {
        List<DtoLectura> result = new ArrayList();
        try {
            query = "SELECT b.bomba_id, b.nombre, p.producto_id, p.nombre, ld.lectura_inicial, ld.lectura_final, ld.total, ld.tipodespacho_id "
                    + "FROM lectura_detalle ld, bomba b, producto p, lectura l "
                    + "WHERE b.bomba_id = ld.bomba_id AND p.producto_id = ld.producto_id AND l.lectura_id = ld.lectura_id AND "
                    + "ld.tipo = ? AND ld.bomba_id = ? AND ld.estacion_id = ? AND l.turno_id = ?";
            pst = getConnection().prepareStatement(query);
            pst.setString(1, tipo);
            pst.setInt(2, bombaId);
            pst.setInt(3, estacionId);
            pst.setInt(4, turnoId);
            ResultSet rst = pst.executeQuery();
            DtoLectura dtoLec;
            int count = 0;
            while (rst.next()) {
                dtoLec = new DtoLectura(count++, rst.getInt(1), rst.getString(2), rst.getInt(3), rst.getString(4));
                if (tipo.equals("E")) {
                    dtoLec.seteInicial(rst.getDouble(5));
                    dtoLec.seteFinal(rst.getDouble(6));
                    dtoLec.seteVolumen(rst.getDouble(7));
                } else if (tipo.equals("M")) {
                    dtoLec.setmInicial(rst.getDouble(5));
                    dtoLec.setmFinal(rst.getDouble(6));
                    dtoLec.setmTotal(rst.getDouble(7));
                }
                dtoLec.setTipodespachoId(rst.getInt(8));
                result.add(dtoLec);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }

}
