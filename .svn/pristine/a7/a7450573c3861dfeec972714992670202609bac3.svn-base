package com.fundamental.services;

import com.fundamental.model.Bomba;
import com.fundamental.model.Empleado;
import com.fundamental.model.Estacion;
import static com.fundamental.services.Dao.ACTION_ADD;
import static com.fundamental.services.Dao.ACTION_UPDATE;
import com.fundamental.model.Lectura;
import com.fundamental.model.LecturaDetalle;
import com.fundamental.model.Lecturafinal;
import com.fundamental.model.dto.DtoLectura;
import com.fundamental.model.dto.DtoPrecio;
import com.fundamental.utils.Constant;
import com.vaadin.ui.CheckBox;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class SvcReading extends Dao {

    private String query;

    public SvcReading() {
    }

    public List<Bomba> getBombasConLectura(Integer estacionId, Integer turnoId) {
        List<Bomba> result = new ArrayList<Bomba>();
        try {
            query = "SELECT DISTINCT b.bomba_id, b.nombre "
                    + "FROM estacion e, bomba_estacion be, bomba b, estacion_conf_head ech, estacion_conf ec, tipodespacho td, turno t, lectura l, lectura_detalle ld "
                    + "WHERE e.estacion_id = be.estacion_id AND b.bomba_id = be.bomba_id "
                    + "AND ech.estacion_id = e.estacion_id AND ec.tipodespacho_id = td.tipodespacho_id AND t.estacion_id = ech.estacion_id AND ec.bomba_id = be.bomba_id "
                    + "AND ech.estacionconfhead_id = ec.estacionconfhead_id AND l.estacion_id = e.estacion_id AND l.turno_id = t.turno_id AND l.lectura_id = ld.lectura_id AND ld.bomba_id = be.bomba_id "
                    + "AND e.estacion_id = ? AND t.turno_id = ? "
                    + "ORDER BY b.bomba_id";

            pst = getConnection().prepareStatement(query);
            pst.setObject(1, estacionId);
            pst.setObject(2, turnoId);
            ResultSet rst = pst.executeQuery();
            Bomba bomba;
            while (rst.next()) {
                bomba = new Bomba(rst.getInt(1), rst.getString(2), null, null, null, new CheckBox("", false));
                result.add(bomba);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    /**
     * Ultimo set de precios activos en una estacion.
     */
    public List<DtoPrecio> getPrecioByTurnoid(Integer turnoId) {
        List<DtoPrecio> result = new ArrayList();
        try {
            ResultSet rst;
            query = "SELECT pro.producto_id, pro.nombre, tp.tipodespacho_id, tp.nombre, p.precio "
                    + "FROM precio p, producto pro, tipodespacho tp "
                    + "WHERE p.producto_id = pro.producto_id AND p.tipodespacho_id = tp.tipodespacho_id AND p.turno_id = " + turnoId
                    + " ORDER BY tp.nombre, pro.producto_id";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            int key = 0;
            boolean exists;
            while (rst.next()) {
                exists = false;
                for (DtoPrecio dp : result) {
                    if (dp.getProductoId().equals(rst.getInt(1)) && rst.getInt(3) == Constant.AUTOSERVICIO) {   //auto
                        dp.setPrecioAS(rst.getDouble(5));
                        exists = true;
                        break;
                    } else if (dp.getProductoId().equals(rst.getInt(1)) && rst.getInt(3) == Constant.SERVICIO_COMPLETO) {   //completo
                        dp.setPrecioSC(rst.getDouble(5));
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    DtoPrecio dpo = new DtoPrecio(key++, rst.getInt(1), rst.getString(2), rst.getInt(3), rst.getString(2));
                    dpo.setPrecioAS(0D);
                    dpo.setPrecioSC(0D);
                    if (rst.getInt(3) == Constant.AUTOSERVICIO) {
                        dpo.setPrecioAS(rst.getDouble(5));
                    } else if (rst.getInt(3) == Constant.SERVICIO_COMPLETO) {
                        dpo.setPrecioSC(rst.getDouble(5));
                    }
                    result.add(dpo);
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public List<DtoLectura> getLecturasByTurnoid(Integer turnoId, String tipo) {
        List<DtoLectura> result = new ArrayList();
        try {
            query = "SELECT ld.bomba_id, ld.producto_id, ld.tipo, ld.lectura_inicial, ld.lectura_final, ld.total, ld.tipodespacho_id, l.creado_persona, ld.calibracion, ld.lectura_id, l.nombre_pistero, l.nombre_jefe "
                    + "FROM turno t, lectura l, lectura_detalle ld "
                    + "WHERE l.lectura_id = ld.lectura_id AND l.turno_id = t.turno_id AND t.estacion_id = l.estacion_id AND t.turno_id = ? AND ld.tipo = ?";
            pst = getConnection().prepareStatement(query);
            pst.setObject(1, turnoId);  //puede ser null
            pst.setString(2, tipo);
            ResultSet rst = pst.executeQuery();
            DtoLectura dla;
            while (rst.next()) {
                dla = new DtoLectura(0, rst.getInt(1), null, rst.getInt(2), null);
                dla.setEm(rst.getString(3));
                dla.setmInicial(rst.getDouble(4));
                dla.setmFinal(rst.getDouble(5));
                dla.setmTotal(rst.getDouble(6));
                dla.setTipodespachoId(rst.getInt(7));
                dla.setCreadoPersona(rst.getString(8));
                dla.setmCalibracion(rst.getDouble(9));
                dla.setLecturaId(rst.getInt(10));
                dla.setNombrePistero(rst.getString(11));
                dla.setNombreJefe(rst.getString(12));
                result.add(dla);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public Lectura doActionLectura(String action, Lectura lectura) {
        Lectura result = new Lectura();
        try {
            if (action.equals(ACTION_ADD)) {
                query = "SELECT lectura_seq.NEXTVAL FROM DUAL";
                pst = getConnection().prepareStatement(query);
                ResultSet rst = pst.executeQuery();
                Integer lecturaId = (rst.next()) ? rst.getInt(1) : 0;
                lectura.setLecturaId(lecturaId);
                closePst();
                query = "INSERT INTO lectura (lectura_id, estacion_id, turno_id, creado_por, creado_persona, nombre_pistero, nombre_jefe, empleado_id) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, lectura.getLecturaId());
                pst.setInt(2, lectura.getEstacionId());
                pst.setInt(3, lectura.getTurnoId());
                pst.setString(4, lectura.getCreadoPor());
                pst.setString(5, lectura.getCreadoPersona());
                pst.setString(6, lectura.getNombrePistero());
                pst.setString(7, lectura.getNombreJefe());
                pst.setInt(8, lectura.getEmpleadoId());
                pst.executeUpdate();
                closePst();
                result = lectura;
            } else if (action.equals(ACTION_UPDATE)) {
//TODO: se debiese actualizar los nombres de pistero y jefe.
                query = "UPDATE lectura SET nombre_pistero = ?, nombre_jefe = ?, empleado_id = ? WHERE estacion_id = ? AND turno_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setString(1, lectura.getNombrePistero());
                pst.setString(2, lectura.getNombreJefe());
                pst.setInt(3, lectura.getEmpleadoId());
                pst.setInt(4, lectura.getEstacionId());
                pst.setInt(5, lectura.getTurnoId());
                pst.executeUpdate();
                closePst();

//                query = "SELECT lectura_id FROM lectura WHERE estacion_id = ? AND turno_id = ?";
//                pst = getConnection().prepareStatement(query);
//                pst.setInt(1, lectura.getEstacionId());
//                pst.setInt(2, lectura.getTurnoId());
//                ResultSet rst = pst.executeQuery();
//                lectura.setLecturaId((rst.next()) ? rst.getInt(1) : 0);
//                for (LecturaDetalle ld : lectura.getLecturaDetalle()) {
//                    query = "UPDATE lectura_detalle "
//                            + "SET lectura_inicial = ?, lectura_final = ?, total = ?, calibracion = ? "
//                            + "WHERE lectura_id = ? AND estacion_id = ? AND bomba_id = ? AND producto_id = ? AND tipodespacho_id = ? AND tipo = ? ";
//                    pst = getConnection().prepareStatement(query);
//                    pst.setDouble(1, ld.getLecturaInicial());
//                    pst.setDouble(2, ld.getLecturaFinal());
//                    pst.setDouble(3, ld.getLecturaTotal());
//                    pst.setObject(4, ld.getCalibracion()); //puede venir null
//                    pst.setInt(5, lectura.getLecturaId());
//                    pst.setInt(6, ld.getEstacionId());
//                    pst.setInt(7, ld.getBombaId());
//                    pst.setInt(8, ld.getProductoId());
//                    pst.setInt(9, ld.getTipodespachoId());
//                    pst.setString(10, ld.getTipo());
//                    pst.executeUpdate();
////TODO: replicar esta modificacion de precio en una tabla de historico.
//                }
                result = lectura;
            }
        } catch (Exception exc) {
            result.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public LecturaDetalle doActionLecturaDetalle(String action, LecturaDetalle lecturaDet) {
        LecturaDetalle result = new LecturaDetalle();
        try {
            if (action.equals(ACTION_ADD)) {
                //TODO: En esta tabla se deben guardar el dato de la configuracion que tiene en ese momento la bomba, basado en la que se eligio al crear el turno.
                query = "INSERT INTO lectura_detalle (lectura_id, estacion_id, bomba_id, producto_id, lectura_inicial, lectura_final, total, tipo, tipodespacho_id, calibracion) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, lecturaDet.getLecturaId());
                pst.setInt(2, lecturaDet.getEstacionId());
                pst.setInt(3, lecturaDet.getBombaId());
                pst.setInt(4, lecturaDet.getProductoId());
                pst.setDouble(5, lecturaDet.getLecturaInicial());
                pst.setDouble(6, lecturaDet.getLecturaFinal());
                pst.setDouble(7, lecturaDet.getLecturaTotal());
                pst.setString(8, lecturaDet.getTipo());
                pst.setInt(9, lecturaDet.getTipodespachoId());
                pst.setObject(10, (lecturaDet.getCalibracion() == null) ? 0 : lecturaDet.getCalibracion()); //puede venir null
                pst.executeUpdate();
                result = lecturaDet;
            } else if (action.equals(ACTION_UPDATE)) {
                query = "UPDATE lectura_detalle "
                        + "SET lectura_inicial = ?, lectura_final = ?, total = ?, calibracion = ? "
                        + "WHERE lectura_id = ? AND estacion_id = ? AND bomba_id = ? AND producto_id = ? AND tipodespacho_id = ? AND tipo = ? ";
                pst = getConnection().prepareStatement(query);
                pst.setDouble(1, lecturaDet.getLecturaInicial());
                pst.setDouble(2, lecturaDet.getLecturaFinal());
                pst.setDouble(3, lecturaDet.getLecturaTotal());
                pst.setObject(4, lecturaDet.getCalibracion()); //puede venir null
                pst.setInt(5, lecturaDet.getLecturaId());
                pst.setInt(6, lecturaDet.getEstacionId());
                pst.setInt(7, lecturaDet.getBombaId());
                pst.setInt(8, lecturaDet.getProductoId());
                pst.setInt(9, lecturaDet.getTipodespachoId());
                pst.setString(10, lecturaDet.getTipo());
                pst.executeUpdate();
            } else if (action.equals(ACTION_DELETE)) {
                query = "DELETE FROM lectura_detalle "
                        + "WHERE lectura_id = ? AND estacion_id = ? AND bomba_id = ? AND producto_id = ? AND tipo = ? AND tipodespacho_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, lecturaDet.getLecturaId());
                pst.setInt(2, lecturaDet.getEstacionId());
                pst.setInt(3, lecturaDet.getBombaId());
                pst.setInt(4, lecturaDet.getProductoId());
                pst.setString(5, lecturaDet.getTipo());
                pst.setInt(6, lecturaDet.getTipodespachoId());
                pst.executeUpdate();
                result = lecturaDet;
                closePst();

                //Se borran las lecturas que ya no tienen detalle
                query = "SELECT COUNT(*) FROM lectura l, lectura_detalle ld WHERE l.lectura_id = ld.lectura_id AND l.lectura_id = " + lecturaDet.getLecturaId();
                pst = getConnection().prepareStatement(query);
                ResultSet rst = pst.executeQuery();
                if (rst.next() && rst.getInt(1) > 0) {
                    query = "DELETE FROM lectura l WHERE l.lectura_id = " + lecturaDet.getLecturaId();
                    pst = getConnection().prepareStatement(query);
                    pst.executeQuery();
                    closePst();
                }

            }
        } catch (Exception exc) {
            result.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public Lecturafinal doActionLecturaFinal(String action, Lecturafinal objeto) {
        Lecturafinal result = new Lecturafinal();
        try {
            if (action.equals(ACTION_ADD)) {
                query = "INSERT INTO lecturafinal (estacion_id, bomba_id, producto_id, tipo, lectura_final, modificado_por, modificado_el, modificado_persona, lectura_inicial) "
                        + "VALUES (?, ?, ?, ?, ?, ?, SYSDATE, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, objeto.getEstacionId());
                pst.setInt(2, objeto.getBombaId());
                pst.setInt(3, objeto.getProductoId());
                pst.setString(4, objeto.getTipo());
                pst.setDouble(5, objeto.getLecturaFinal());
                pst.setString(6, objeto.getModificadoPor());
                pst.setString(7, objeto.getModificadoPersona());
                pst.setDouble(8, objeto.getLecturaInicial());
                pst.executeUpdate();
                result = objeto;
            } else if (action.equals(ACTION_UPDATE)) {
                query = "UPDATE lecturafinal "
                        + "SET lectura_inicial = ?, lectura_final = ?, modificado_por = ?, modificado_el = SYSDATE, modificado_persona = ? "
                        + "WHERE estacion_id = ? AND bomba_id = ? AND producto_id = ? AND tipo = ?";
                pst = getConnection().prepareStatement(query);
                pst.setDouble(1, objeto.getLecturaInicial());
                pst.setDouble(2, objeto.getLecturaFinal());
                pst.setString(3, objeto.getModificadoPor());
                pst.setString(4, objeto.getModificadoPersona());

                pst.setInt(5, objeto.getEstacionId());
                pst.setInt(6, objeto.getBombaId());
                pst.setInt(7, objeto.getProductoId());
                pst.setString(8, objeto.getTipo());
                pst.executeUpdate();
                result = objeto;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public String existsLecturasForBombas(Integer estacionId, Integer turnoId, String bombas) {
        String result = "";
        query = "SELECT DISTINCT(ld.bomba_id) FROM lectura la, lectura_detalle ld "
                + "WHERE la.lectura_id = ld.lectura_id AND la.estacion_id = ld.estacion_id "
                + "AND la.estacion_id = ? AND la.turno_id = ? AND ld.bomba_id IN (" + bombas + ") ";
        try {
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setInt(2, turnoId);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result += (result.isEmpty()) ? rst.getString(1) : ", " + rst.getString(1);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public Estacion getStationById(Integer estacionId) {
        Estacion result = new Estacion();
        try {
            query = "SELECT estacion_id, nombre, codigo, pais_id, estado, fact_electronica "
                    + "FROM estacion "
                    + "WHERE estacion_id = " + estacionId;
            ResultSet rst = getConnection().prepareStatement(query).executeQuery();
            if (rst.next()) {
                result = new Estacion(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5), rst.getString(6));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public LecturaDetalle getLecturaDetalleSiguiente(Integer estacionId, Integer lecturaId, Integer bombaId, Integer productoId) {
        LecturaDetalle result = new LecturaDetalle();
        try {
            query = "SELECT ld.lectura_id, ld.estacion_id, ld.bomba_id, ld.producto_id, ld.tipodespacho_id, ld.tipo, ld.lectura_inicial, ld.lectura_final, ld.total, ld.calibracion "
                    + "FROM lectura l, lectura_detalle ld "
                    + "WHERE l.lectura_id = ld.lectura_id AND l.estacion_id = ? AND l.lectura_id > ? AND ld.bomba_id = ? AND producto_id = ? "
                    + "ORDER BY l.lectura_id";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, estacionId);
            pst.setInt(2, lecturaId);
            pst.setInt(3, bombaId);
            pst.setInt(4, productoId);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                result = new LecturaDetalle(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getInt(4), rst.getInt(5), rst.getString(6), rst.getDouble(7), rst.getDouble(8), rst.getDouble(9), rst.getDouble(10));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

}
