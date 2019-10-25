package com.fundamental.services;

import com.fundamental.model.Acceso;
import com.fundamental.model.Arqueocaja;
import com.fundamental.model.Bomba;
import com.fundamental.model.BombaEstacion;
import com.fundamental.model.Dia;
import com.fundamental.model.Empleado;
import com.fundamental.model.Estacion;
import com.fundamental.model.EstacionConf;
import com.fundamental.model.EstacionConfHead;
import com.fundamental.model.Horario;
import com.fundamental.model.Lecturafinal;
import com.fundamental.model.Marca;
import com.fundamental.model.Mediopago;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Parametro;
import com.fundamental.model.Precio;
import com.fundamental.model.Producto;
import com.fundamental.model.Rol;
import com.fundamental.model.Turno;
import com.fundamental.model.dto.DtoArqueo;
import com.fundamental.model.dto.DtoGenericBean;
import com.fundamental.utils.Constant;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.util.ListSet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.CheckBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author Henry Barrientos..
 */
public class Dao {

//    public static final String JNDI_NAME = "jdbc/cocos_dev";    //DEVELOPMENT
//    public static final String JNDI_NAME = "jdbc/cocos";      //PRODUCTION
    public static final String JNDI_NAME = "java:/comp/env/jdbc/cocos_dev";      //TomeeDesa

    public static final String ACTION_ADD = "ADD";
    public static final String ACTION_UPDATE = "UPDATE";
    public static final String ACTION_DELETE = "DELETE";
    public String miQuery, tmpString;

    protected Connection conn = null;
    protected PreparedStatement pst = null;
    protected PreparedStatement pst2 = null;

    public Connection getConnection() {
        try {
//            conn = (conn == null || conn.isClosed()) ? ((DataSource) new InitialContext().lookup(JNDI_NAME)).getConnection() : conn;

            if (VaadinSession.getCurrent().getAttribute(Constant.SESSION_CONNECTION) == null
                    || ((Connection) VaadinSession.getCurrent().getAttribute(Constant.SESSION_CONNECTION)).isClosed()) {

                conn = (conn == null || conn.isClosed()) ? ((DataSource) new InitialContext().lookup(JNDI_NAME)).getConnection() : conn;
                VaadinSession.getCurrent().setAttribute(Constant.SESSION_CONNECTION, conn);

            } else {
                conn = (Connection) VaadinSession.getCurrent().getAttribute(Constant.SESSION_CONNECTION);
            }

        } catch (Exception exc) {
            System.out.println("EXCEPTION: DaoAccess.getConnection - " + exc.getMessage());
        }
        return conn;
    }

    public void setOwnAutocommit(boolean autoCommit) {
        try {
            conn.setAutoCommit(autoCommit);
        } catch (SQLException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeConnections() {
        try {
            if (pst2 != null) {
                pst.close();
            }
            if (pst != null) {
                pst.close();
            }
//            if (conn != null) {
//                conn.commit();
//                conn.close();
//            }
        } catch (Exception exc) {
            System.out.println("EXCEPTION DaoAccess.closeConnection - " + exc.getMessage());
        } finally {
            System.gc();
        }
    }

    public void closePst() {
        try {
            if (pst2 != null) {
                pst.close();
            }
            if (pst != null) {
                pst.close();
            }
        } catch (Exception exc) {
            System.out.println("EXCEPTION DaoAccess.closePst - " + exc.getMessage());
        }
    }

    /*
    * FUNCIONES COMUNES A TODOS LOS SERVICIOS *
     */
    protected List<Rol> getRolesByUserid(Integer userId) {
        List<Rol> result = new ArrayList();
        ResultSet rst = null;
        SvcMaintenance maintenace = new SvcMaintenance();
        try {
            miQuery = "SELECT r.rol_id, r.nombre, r.descripcion, r.rolpadre_id, r.estado "
                    + "FROM rol r, rol_usuario ru "
                    + "WHERE r.rol_id = ru.rol_id AND ru.usuario_id = " + userId;
            pst = getConnection().prepareStatement(miQuery);
            System.out.println("getRoles "+miQuery);
            rst = pst.executeQuery();
            while (rst.next()) {
                System.out.println("next");
                Rol rl = new Rol(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5));
                try{
                List<Acceso> accesos = maintenace.getAccessByRolid(rst.getInt(1));
                rl.setAccesos(accesos);
                }catch(Exception e){
                    e.printStackTrace();
                }
                result.add(rl);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Bomba> getBombasByEstacionid(Integer estacionId) {
        List<Bomba> result = new ArrayList<Bomba>();
        ResultSet rst = null;
        try {
            miQuery = "SELECT b.bomba_id, b.nombre, b.estado, be.corr_pista "
                    + "FROM estacion e, bomba_estacion be, bomba b "
                    + "WHERE e.estacion_id = be.estacion_id AND b.bomba_id = be.bomba_id AND e.estacion_id = " + estacionId;
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            Bomba bomba;
            while (rst.next()) {
                bomba = new Bomba(rst.getInt(1), rst.getString(2), rst.getString(3), null, null, new CheckBox("", false));
                bomba.setCorrPista(rst.getInt(4));
//                bomba.setBombaEstacion(getBombaEstacionByEstacionid(estacionId));
                result.add(bomba);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<DtoArqueo> getArqueo(String turnosIds, String bombasIds, String tipo) {
        List<DtoArqueo> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = (bombasIds == null) ? "" : " AND ec.bomba_id IN (" + bombasIds + ") ";
            miQuery = "SELECT pro.nombre, td.nombre, SUM(ld.lectura_final-ld.lectura_inicial-ld.calibracion) galones, SUM((ld.lectura_final-ld.lectura_inicial-ld.calibracion)*p.precio) venta, pro.producto_id, ld.tipodespacho_id, SUM(ld.calibracion*p.precio) cal "
                    + "FROM turno t, precio p, estacion_conf_head ech, estacion_conf ec, producto pro, tipodespacho td, lectura l, lectura_detalle ld "
                    + "WHERE t.turno_id = p.turno_id AND t.estacionconfhead_id = ech.estacionconfhead_id AND ech.estacionconfhead_id = ec.estacionconfhead_id "
                    + " AND ec.tipodespacho_id = p.tipodespacho_id "
                    + "AND p.producto_id = pro.producto_id AND p.tipodespacho_id = td.tipodespacho_id "
                    + "AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id "
                    + "AND ld.bomba_id = ec.bomba_id AND ld.producto_id = p.producto_id AND ld.tipodespacho_id = p.tipodespacho_id "
                    + "AND ec.ESTACION_ID = l.ESTACION_ID"
                    + miQuery + " AND t.turno_id IN (" + turnosIds + ") AND ld.tipo = '" + tipo + "' "
                    + "GROUP BY td.nombre, pro.nombre, pro.producto_id, ld.tipodespacho_id "
                    + "ORDER BY td.nombre, pro.producto_id";

            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            int count = 0;
            DtoArqueo daro;
            while (rst.next()) {
                daro = new DtoArqueo(count++, rst.getString(1), rst.getString(2), rst.getDouble(3), 0D, rst.getDouble(4));
                daro.setProductoId(rst.getInt(5));
                daro.setTipodespachoId(rst.getInt(6));
                daro.setDiferencia(0D);
                daro.setCalibracion(rst.getDouble(7));
                result.add(daro);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

//    public List<Turno> getTurnosActivosByEstacionid(Integer estacionId) {
//        List<Turno> result = new ArrayList();
//        ResultSet rst = null; ResultSet rst = null; try {
//            miQuery = "SELECT turno_id, estacion_id, usuario_id, estado_id, creado_persona "
//                    + "FROM turno "
//                    + "WHERE estado_id = 1 AND estacion_id = " + estacionId
//                    + " ORDER BY turno_id DESC";
//            pst = getConnection().prepareStatement(miQuery);
//            rst = pst.executeQuery();
//            while (rst.next()) {
//                result.add(new Turno(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getInt(4), null, rst.getString(5)));
//            }
//        } catch (Exception exc) {
//            exc.printStackTrace();
//        }
//        return result;
//    }
//    public List<Turno> getTurnosByHorarioid(Integer horarioId) {
//        List<Turno> result = new ArrayList();
//        ResultSet rst = null; ResultSet rst = null; try {
//            miQuery = "SELECT turno_id, estacion_id, usuario_id, estado_id, creado_persona "
//                    + "FROM turno "
//                    + "WHERE horario_id = " + horarioId
//                    + " ORDER BY turno_id DESC";
//            pst = getConnection().prepareStatement(miQuery);
//            rst = pst.executeQuery();
////            result = (rst.next()) ? new Turno(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getInt(6), rst.getInt(4), null, rst.getString(5)) : result;
//            while (rst.next()) {
//                result.add(new Turno(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getInt(4), null, rst.getString(5)));
//            }
//        } catch (Exception exc) {
//            exc.printStackTrace();
//        }
//        return result;
//    }
//    public Horario getHorarioActivoByEstacionid(Integer stationId) {
//        Horario result = new Horario();
//        ResultSet rst = null; try {
//            miQuery = "SELECT horario_id, estacion_id, hora_fin, fecha, estado_id "
//                    + "FROM horario "
//                    + "WHERE estado_id = 1 AND estacion_id = " + stationId
//                    + " ORDER BY horario_id DESC";
//            pst = getConnection().prepareStatement(miQuery);
//            rst = pst.executeQuery();
//            result = (rst.next()) ? new Horario(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getDate(4), rst.getInt(5)) : result;
//        } catch (Exception exc) {
//            exc.printStackTrace();
//        } finally {
//            
//                try { rst.close(); pst.close(); } catch (Exception ignore) {
//            }
//        }
//        return result;
//    }
//    public Horario doActionHorario(String action, Horario horario) {
//        Horario result = new Horario();
//        ResultSet rst = null; try {
//            if (action.equals(ACTION_ADD)) {
//                miQuery = "SELECT horario_seq.NEXTVAL FROM DUAL";
//                pst = getConnection().prepareStatement(miQuery);
//                rst = pst.executeQuery();
//                int horarioId = (rst.next()) ? rst.getInt(1) : 0;
//                horario.setHorarioId(horarioId);
//
//                miQuery = "INSERT INTO horario (horario_id, estacion_id, hora_fin, fecha, estado_id, creado_por, creado_persona) "
//                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
//                pst = getConnection().prepareStatement(miQuery);
//                pst.setInt(1, horario.getHorarioId());
//                pst.setInt(2, horario.getEstacionId());
//                pst.setInt(3, horario.getHoraFin());
//                pst.setDate(4, new java.sql.Date(horario.getFecha().getTime()));
//                pst.setInt(5, horario.getEstadoId());
//                pst.setString(6, horario.getCreadoPor());
//                pst.setString(7, horario.getCreadoPersona());
//                pst.executeUpdate();
//                result = horario;
//            } else if (action.equals(ACTION_UPDATE)) {
//                miQuery = "UPDATE horario "
//                        + "SET estado_id = ?, modificado_por = ?, modificado_persona = ?, modificado_el = SYSDATE "
//                        + "WHERE horario_id = " + horario.getHorarioId();
//                pst = getConnection().prepareStatement(miQuery);
//                pst.setInt(1, horario.getEstadoId());
//                pst.setString(2, horario.getModificadoPor());
//                pst.setString(3, horario.getModificadoPersona());
//                pst.executeUpdate();
//                result = horario;
//            }
//        } catch (Exception exc) {
//            result.setDescError(exc.getMessage());
//            exc.printStackTrace();
//        }
//        return result;
//    }
    public List<Pais> getAllPaises() {
        List<Pais> result = new ArrayList<Pais>();
        miQuery = "SELECT pais_id, nombre, codigo, moneda_simbolo, estado, vol_simbolo "
                + "FROM pais";
        ResultSet rst = null;
        try {
            rst = getConnection().prepareStatement(miQuery).executeQuery();
            while (rst.next()) {
                result.add(new Pais(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(6), rst.getString(5),false));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Estacion> getStationsByCountry(Integer paisId) {
        List<Estacion> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = "SELECT estacion_id, nombre, codigo, pais_id, estado, fact_electronica "
                    + "FROM estacion "
                    + "WHERE pais_id = " + paisId;
            rst = getConnection().prepareStatement(miQuery).executeQuery();
            while (rst.next()) {
                result.add(new Estacion(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5), rst.getString(6)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Estacion> getStationsByCountry(Integer paisId, boolean onlyActive) {
        List<Estacion> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = (onlyActive) ? " AND estado = 'A' " : "";
            miQuery = "SELECT estacion_id, nombre, codigo, pais_id, estado, fact_electronica "
                    + "FROM estacion "
                    + "WHERE pais_id = " + paisId
                    + miQuery;
            rst = getConnection().prepareStatement(miQuery).executeQuery();
            while (rst.next()) {
                result.add(new Estacion(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5), rst.getString(6)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    /* Ultimo turno del dia, es util para lecturas y cuadre, se necesita el ultimo del dia*/
    public Turno getTurnoActivoByEstacionid(Integer estacionId) {
        Turno result = new Turno();
        ResultSet rst = null;
        try {
            miQuery = "SELECT t.turno_id, t.estacion_id, t.usuario_id, t.estado_id, t.fecha, t.creado_persona, t.estacionconfhead_id "
                    + ", h.horario_id, h.nombre, h.hora_inicio, h.hora_fin, h.estado, h.descripcion "
                    + "FROM turno t, horario h "
                    + "WHERE t.horario_id = h.horario_id AND t.estado_id = 1 AND t.estacion_id = " + estacionId
                    + " ORDER BY t.turno_id DESC";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            if (rst.next()) {
                result = new Turno(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getInt(4), rst.getDate(5), null, rst.getString(6));
                result.setEstacionconfheadId(rst.getInt(7));
                Horario h = new Horario(rst.getInt(8), rst.getString(9), rst.getString(10), rst.getString(11), rst.getString(12), rst.getString(13));
                h.setNombreHoras(rst.getString(10) + " - " + rst.getString(11));
                result.setHorario(h);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    /* Ultimo turno registrado para la estacion, no importa si de ayer o hoy */
    public Turno getUltimoTurnoByEstacionid(Integer estacionId) {
        Turno result = new Turno();
        ResultSet rst = null;
        try {
            miQuery = "SELECT t.turno_id, t.estacion_id, t.usuario_id, t.estado_id, t.fecha, t.creado_persona, t.estacionconfhead_id "
                    + ", h.horario_id, h.nombre, h.hora_inicio, h.hora_fin, h.estado, h.descripcion "
                    + "FROM turno t, horario h "
                    + "WHERE t.horario_id = h.horario_id AND t.estacion_id = ? "
                    + " ORDER BY t.turno_id DESC";
            pst = getConnection().prepareStatement(miQuery);
            pst.setObject(1, estacionId);
            rst = pst.executeQuery();
            if (rst.next()) {
                result = new Turno(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getInt(4), rst.getDate(5), null, rst.getString(6));
                result.setEstacionconfheadId(rst.getInt(7));
                Horario h = new Horario(rst.getInt(8), rst.getString(9), rst.getString(10), rst.getString(11), rst.getString(12), rst.getString(13));
                h.setNombreHoras(rst.getString(10) + " - " + rst.getString(11));
                result.setHorario(h);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Bomba> getBombasByEstacionidTurnoid(Integer estacionId, Integer turnoId) {
        List<Bomba> result = new ArrayList<Bomba>();
        ResultSet rst = null;
        try {
//TODO: modificar el query siguiente para que se tomen las bombas del turno desde la tabla> TURNO_EMPLEADO_BOMBA
            miQuery = "SELECT b.bomba_id, b.nombre, b.estado, td.nombre_corto, ec.tipodespacho_id "
                    + "FROM bomba b, bomba_estacion be, estacion e, turno t, estacion_conf_head ech, estacion_conf ec, tipodespacho td "
                    + "WHERE b.bomba_id = be.bomba_id AND e.estacion_id = be.estacion_id AND e.estacion_id = t.estacion_id AND t.estacion_id = ec.estacion_id "// AND t.estacion_id = ech.estacion_id "
                    + "AND t.estacionconfhead_id = ech.estacionconfhead_id AND ech.estacionconfhead_id = ec.estacionconfhead_id AND ec.estacion_id = e.estacion_id "//AND ech.estacion_id = e.estacion_id "
                    + "AND ec.bomba_id = be.bomba_id AND ec.tipodespacho_id = td.tipodespacho_id "
                    + "AND e.estacion_id = ? AND t.turno_id = ? "
                    + "ORDER BY b.bomba_id";
            pst = getConnection().prepareStatement(miQuery);
            pst.setObject(1, estacionId);
            pst.setObject(2, turnoId);
            rst = pst.executeQuery();
            Bomba bomba;
            while (rst.next()) {
                bomba = new Bomba(rst.getInt(1), rst.getString(2), rst.getString(3), null, null, new CheckBox("", false));
                bomba.setBombaEstacion(getBombaEstacionByEstacionid(estacionId));
                bomba.setTipoDespachoName(rst.getString(4));
                bomba.setTipoDespachoId(rst.getInt(5));
                result.add(bomba);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    protected List<BombaEstacion> getBombaEstacionByEstacionid(Integer estacionId) {
        List<BombaEstacion> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = "SELECT bomba_id, estacion_id "
                    + "FROM bomba_estacion "
                    + "WHERE estacion_id = " + estacionId;
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new BombaEstacion(rst.getInt(1), rst.getInt(2), null));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Producto> getCombustiblesByEstacionid(Integer estacionId) {
        List<Producto> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = "SELECT p.producto_id, p.nombre, p.codigo, p.estado, p.orden_pos "
                    + "FROM estacion_producto ep, producto p "
                    + "WHERE ep.producto_id = p.producto_id AND p.tipo_id = 1 AND ep.estacion_id = " + estacionId
                    + " ORDER BY p.producto_id";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            Producto product;
            while (rst.next()) {
                product = new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), null, rst.getInt(5));
                product.setPriceAS(0D);
                product.setPriceSC(0D);
                result.add(product);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Lecturafinal> getLecturasfinales(Integer estacionId, String tipoLectura) {
        List<Lecturafinal> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = "SELECT estacion_id, bomba_id, producto_id, tipo, lectura_inicial, lectura_final, modificado_por, modificado_el, modificado_persona "
                    + "FROM lecturafinal "
                    + "WHERE estacion_id = ? AND tipo = ?";
            pst = getConnection().prepareStatement(miQuery);
            pst.setObject(1, estacionId);
            pst.setString(2, tipoLectura);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Lecturafinal(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getString(4), rst.getDouble(5), rst.getDouble(6), null, null));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public Turno doActionTurno(String action, Turno turno) {
        Turno result = new Turno();
        ResultSet rst = null;
        try {
            if (action.equals(ACTION_ADD)) {
//                miQuery = "SELECT turno_seq.NEXTVAL FROM DUAL";
//                pst = getConnection().prepareStatement(miQuery);
//                rst = pst.executeQuery();
//                Integer turnoId = (rst.next()) ? rst.getInt(1) : 0;
//                turno.setTurnoId(turnoId);
//                miQuery = "INSERT INTO turno (turno_id, estacion_id, usuario_id, estado_id, creado_por, creado_persona, turno_fusion, estacionconfhead_id, fecha) "
//                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//                pst = getConnection().prepareStatement(miQuery);
//                pst.setInt(1, turno.getTurnoId());
//                pst.setInt(2, turno.getEstacionId());
//                pst.setInt(3, turno.getUsuarioId());
//                pst.setInt(4, turno.getEstadoId());
//                pst.setString(5, turno.getCreadoPor());
//                pst.setString(6, turno.getCreadoPersona());
//                pst.setString(7, turno.getTurnoFusion());
//                pst.setObject(8, turno.getEstacionconfheadId());    //setObject, puesto que puede ser NULL.
//                pst.setDate(9, new java.sql.Date(turno.getFecha().getTime()));
//                pst.executeUpdate();

//                for (BombaestacionTurno bet : turno.getBombaestacionTurno()) {
//                    miQuery = "INSERT INTO bombaestacion_turno(estacion_id, bomba_id, turno_id) VALUES (?, ?, ?)";
//                    pst = getConnection().prepareStatement(miQuery);
//                    pst.setInt(1, bet.getEstacionId());
//                    pst.setInt(2, bet.getBombaId());
//                    pst.setInt(3, turnoId);
//                    pst.executeUpdate();
//                }
                result = turno;
            } else if (action.equals(ACTION_UPDATE)) {
                miQuery = "UPDATE turno "
                        + "SET "
                        //                        + "estacion_id = ?, usuario_id = ?, "
                        + "estado_id = ?, modificado_por = ?, modificado_persona = ?, modificado_el = SYSDATE " //, nombre_pistero = ? "
                        + "WHERE turno_id = ?";
                pst = getConnection().prepareStatement(miQuery);
//                pst.setInt(1, turno.getEstacionId());
//                pst.setInt(2, turno.getUsuarioId());
                pst.setInt(1, turno.getEstadoId());
                pst.setString(2, turno.getModificadoPor());
                pst.setString(3, turno.getModificadoPersona());
//                pst.setString(4, turno.getn);
                pst.setInt(4, turno.getTurnoId());
                pst.executeUpdate();
                result = turno;
            } else if (action.equals(Dao.ACTION_DELETE)) {
                miQuery = "DELETE FROM turno WHERE turno_id = ?";
                pst = getConnection().prepareStatement(miQuery);
                pst.setInt(1, turno.getTurnoId());
                pst.executeUpdate();
                result = null;
            }
        } catch (Exception exc) {
            turno.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Mediopago> getMediospagoByPaisidTipoid(Integer paisId, Integer tipoId) {
        List<Mediopago> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = (tipoId != null) ? " AND m.tipo = " + tipoId : "";
            miQuery = "SELECT m.mediopago_id, m.nombre, m.tipo, m.orden, p.nombre, m.partidacont_por, m.partidacont, m.is_tcredito "
                    + "FROM mediopago m, pais p "
                    + "WHERE m.pais_id = p.pais_id AND m.pais_id = " + paisId
                    + miQuery
                    + " ORDER BY m.orden";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            Mediopago mediopago;
            while (rst.next()) {
                mediopago = new Mediopago(rst.getInt(1), rst.getString(2), rst.getInt(3), null, rst.getInt(4), rst.getString(5), rst.getBoolean(7), null);
                mediopago.setPartidacontPor(rst.getDouble(6));
                mediopago.setPartidacont(rst.getBoolean(7));
                result.add(mediopago);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    //SvcTurnoCierre
    public List<Arqueocaja> getArqueocajaByTurnoid(Integer turnoId) {
        List<Arqueocaja> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = "SELECT arqueocaja_id, estacion_id, turno_id, fecha, estado_id, creado_por, creado_persona, empleado_id, nombre_pistero, nombre_jefe "
                    + "FROM arqueocaja "
                    + "WHERE turno_id = " + turnoId
                    + " ORDER BY arqueocaja_id";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            Arqueocaja acaja;
            int indexId = 1;
            while (rst.next()) {
                acaja = new Arqueocaja(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getDate(4), rst.getInt(5), rst.getString(6), rst.getString(7), rst.getInt(8), rst.getString(9), rst.getString(10));
                acaja.setNombre("Cuadre " + indexId++);
                result.add(acaja);
            }

            miQuery = "M";
            miQuery
                    = "SELECT tabl.arqueocaja_id, SUM(tabl.monto) "
                    + "FROM ( "
                    + "     SELECT a.arqueocaja_id, SUM((ld.lectura_final-ld.lectura_inicial-ld.calibracion)*p.precio) monto "
                    + "     FROM turno t, precio p, estacion_conf_head ech, estacion_conf ec, lectura l, lectura_detalle ld, arqueocaja a, arqueocaja_bomba ab "
                    + "     WHERE t.turno_id = p.turno_id AND t.estacionconfhead_id = ech.estacionconfhead_id AND ech.estacionconfhead_id = ec.estacionconfhead_id  AND ec.tipodespacho_id = p.tipodespacho_id "
                    + "     AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id AND ld.bomba_id = ec.bomba_id "
                    + "     AND ld.producto_id = p.producto_id AND ld.tipodespacho_id = p.tipodespacho_id "
                    + "     AND a.turno_id = t.turno_id AND a.arqueocaja_id = ab.arqueocaja_id AND ab.bomba_id = ec.bomba_id AND ab.turno_id = t.turno_id "
                    + "     AND t.turno_id = " + turnoId + " AND ld.tipo = '" + miQuery + "' "
                    + "     GROUP BY a.arqueocaja_id "
                    + "UNION "
                    + "     SELECT a.arqueocaja_id, SUM(ap.monto) monto  "
                    + "     FROM arqueocaja a, arqueocaja_producto ap  "
                    + "     WHERE a.arqueocaja_id = ap.arqueocaja_id AND a.turno_id = " + turnoId
                    + "     GROUP BY a.arqueocaja_id "
                    + "UNION "
                    + "     SELECT a.arqueocaja_id, -SUM(ad.monto) monto "
                    + "     FROM arqueocaja a, arqueocaja_detalle ad "
                    + "     WHERE a.arqueocaja_id = ad.arqueocaja_id AND a.turno_id = " + turnoId
                    + "     GROUP BY a.arqueocaja_id "
                    + "UNION "
                    + "     SELECT a.arqueocaja_id, -SUM(ae.monto) monto "
                    + "     FROM arqueocaja a, efectivo ae "
                    + "     WHERE a.arqueocaja_id = ae.arqueocaja_id AND a.turno_id = " + turnoId
                    + "     GROUP BY a.arqueocaja_id "
                    + ") tabl "
                    + "GROUP BY tabl.arqueocaja_id "
                    + "ORDER BY tabl.arqueocaja_id";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            while (rst.next()) {
                for (Arqueocaja arq : result) {
                    if (arq.getArqueocajaId() == rst.getInt(1)) {
                        arq.setDiferencia(rst.getDouble(2));
                    }
                }
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Arqueocaja> getArqueocajaByTurnoidDia(Integer turnoId, Date fecha) {
        List<Arqueocaja> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = "SELECT a.arqueocaja_id, a.estacion_id, a.turno_id, a.fecha, a.estado_id, a.creado_por, a.creado_persona, a.empleado_id, a.nombre_pistero, a.nombre_jefe "
                    + "FROM arqueocaja a, turno t "
                    + "WHERE a.turno_id = t.turno_id AND a.turno_id = ? AND t.fecha = ? "
                    + " ORDER BY a.arqueocaja_id DESC";
            pst = getConnection().prepareStatement(miQuery);
            pst.setObject(1, turnoId);
            pst.setDate(2, (fecha == null) ? null : new java.sql.Date(fecha.getTime()));
            rst = pst.executeQuery();
            Arqueocaja acaja;
            while (rst.next()) {
                acaja = new Arqueocaja(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getDate(4), rst.getInt(5), rst.getString(6), rst.getString(7), rst.getInt(8), rst.getString(9), rst.getString(10));
                acaja.setNombre("Cuadre " + rst.getInt(1));
                result.add(acaja);
            }

            miQuery = "E";
            miQuery
                    = "SELECT tabl.arqueocaja_id, SUM(tabl.monto) "
                    + "FROM ( "
                    + "     SELECT a.arqueocaja_id, SUM(ad.monto) monto "
                    + "     FROM arqueocaja a, arqueocaja_detalle ad "
                    + "     WHERE a.arqueocaja_id = ad.arqueocaja_id AND a.turno_id = " + turnoId
                    + "     GROUP BY a.arqueocaja_id "
                    + "UNION "
                    + "     SELECT a.arqueocaja_id, SUM(ae.monto) monto "
                    + "     FROM arqueocaja a, efectivo ae "
                    + "     WHERE a.arqueocaja_id = ae.arqueocaja_id AND a.turno_id = " + turnoId
                    + "     GROUP BY a.arqueocaja_id "
                    + "UNION "
                    + "     SELECT a.arqueocaja_id, -SUM((ld.lectura_final-ld.lectura_inicial-ld.calibracion)*p.precio) monto "
                    + "     FROM turno t, precio p, estacion_conf_head ech, estacion_conf ec, lectura l, lectura_detalle ld, arqueocaja a, arqueocaja_bomba ab "
                    + "     WHERE t.turno_id = p.turno_id AND t.estacionconfhead_id = ech.estacionconfhead_id AND ech.estacionconfhead_id = ec.estacionconfhead_id  AND ec.tipodespacho_id = p.tipodespacho_id "
                    + "     AND t.turno_id = l.turno_id AND l.lectura_id = ld.lectura_id AND ld.bomba_id = ec.bomba_id "
                    + "     AND ld.producto_id = p.producto_id AND ld.tipodespacho_id = p.tipodespacho_id "
                    + "     AND a.turno_id = t.turno_id AND a.arqueocaja_id = ab.arqueocaja_id AND ab.bomba_id = ec.bomba_id AND ab.turno_id = t.turno_id "
                    + "     AND t.estacion_id = ec.estacion_id "
                    + "     AND t.turno_id = " + turnoId + " AND ld.tipo = '" + miQuery + "' "
                    + "     GROUP BY a.arqueocaja_id "
                    + "UNION "
                    + "     SELECT a.arqueocaja_id, -SUM(ap.monto) monto  "
                    + "     FROM arqueocaja a, arqueocaja_producto ap  "
                    + "     WHERE a.arqueocaja_id = ap.arqueocaja_id AND a.turno_id = " + turnoId
                    + "     GROUP BY a.arqueocaja_id "
                    + ") tabl "
                    + "GROUP BY tabl.arqueocaja_id "
                    + "ORDER BY tabl.arqueocaja_id";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            while (rst.next()) {
                for (Arqueocaja arq : result) {
                    if (arq.getArqueocajaId() == rst.getInt(1)) {
                        arq.setDiferencia(rst.getDouble(2));
                    }
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Turno> getTurnosByEstacionid(Integer estacionId) {
        List<Turno> result = new ArrayList();
        ResultSet rst = null;
        try {
            //Se valida que el turno tenga lectura para que aparezca en el combobox
            miQuery = "SELECT DISTINCT t.turno_id, t.estacion_id, t.usuario_id, t.estado_id, t.fecha, t.creado_por, t.creado_persona "
                    + "FROM turno t, lectura l "
                    + "WHERE t.turno_id = l.turno_id AND t.estacion_id = " + estacionId
                    + " ORDER BY t.turno_id";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            Turno turno;
            short count = 1;
            while (rst.next()) {
                turno = new Turno(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getInt(4), rst.getDate(5), rst.getString(6), rst.getString(7));
                turno.setNombre("Turno " + count++);
                result.add(turno);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Turno> getTurnosByEstacionidDiaNolectura(Integer estacionId, Date fecha) {
        List<Turno> result = new ArrayList();
        ResultSet rst = null;
        try {
            //Se valida que el turno tenga lectura para que aparezca en el combobox
            miQuery = "SELECT DISTINCT t.turno_id, t.estacion_id, t.usuario_id, t.estado_id, t.fecha, t.creado_por, t.creado_persona, t.estacionconfhead_id "
                    + "FROM turno t "
                    + "WHERE t.estacion_id = ? AND t.fecha = ? "
                    + "ORDER BY turno_id";
            pst = getConnection().prepareStatement(miQuery);
            pst.setObject(1, estacionId);
            pst.setDate(2, (fecha == null) ? null : new java.sql.Date(fecha.getTime()));
            rst = pst.executeQuery();
            Turno turno;
            short count = 1;
            while (rst.next()) {
                turno = new Turno(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getInt(4), rst.getDate(5), rst.getString(6), rst.getString(7));
                turno.setNombre("Turno " + count++);
                turno.setEstacionconfheadId(rst.getInt(8));
                result.add(turno);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Turno> getTurnosByEstacionidDia_lectura(Integer estacionId, Date fecha) {
        List<Turno> result = new ArrayList();
        ResultSet rst = null;
        try {
            //Se valida que el turno tenga lectura para que aparezca en el combobox
            miQuery = "SELECT DISTINCT t.turno_id, t.estacion_id, t.usuario_id, t.estado_id, t.fecha, t.creado_por, t.creado_persona "
                    + "FROM turno t, lectura l "
                    + "WHERE t.turno_id = l.turno_id AND t.estacion_id = ? AND t.fecha = ? "
                    + "ORDER BY t.turno_id";
            pst = getConnection().prepareStatement(miQuery);
            pst.setObject(1, estacionId);
            pst.setDate(2, (fecha == null) ? null : new java.sql.Date(fecha.getTime()));
            rst = pst.executeQuery();
            Turno turno;
            short count = 1;
            while (rst.next()) {
                turno = new Turno(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getInt(4), rst.getDate(5), rst.getString(6), rst.getString(7));
                turno.setNombre("Turno " + count++);
                result.add(turno);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public Dia doActionDia(String action, Dia dia) {
        Dia result = new Dia();
        ResultSet rst = null;
        try {
//            if (action.equals(ACTION_ADD)) {
//                miQuery = "INSERT INTO dia (estado_id, creado_por, creado_persona, estacion_id, fecha, creado_el) "
//                        + "VALUES (?, ?, ?, ?, ?, SYSDATE)";
//                pst = getConnection().prepareStatement(miQuery);
//                pst.setInt(1, dia.getEstadoId());
//                pst.setString(2, dia.getCreadoPor());
//                pst.setString(3, dia.getCreadoPersona());
//                pst.setInt(4, dia.getEstacionId());
//                pst.setDate(5, new java.sql.Date(dia.getFecha().getTime()));
//                pst.executeQuery();
//                result = dia;
//            } else 
            if (action.equals(ACTION_UPDATE)) {
                miQuery = "UPDATE dia SET estado_id = ?, modificado_por = ?, modificado_persona = ?, modificado_el = SYSDATE "
                        + "WHERE estacion_id = ? AND fecha = ?";
                pst = getConnection().prepareStatement(miQuery);
                pst.setInt(1, 2);
                pst.setString(2, dia.getModificadoPor());
                pst.setString(3, dia.getModificadoPersona());
                pst.setInt(4, dia.getEstacionId());
                pst.setDate(5, new java.sql.Date(dia.getFecha().getTime()));
                pst.executeQuery();
                result = dia;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public Precio doActionPrecio(String action, Precio precio) {
        Precio result = new Precio();
        ResultSet rst = null;
        try {
//            if (action.equals(ACTION_ADD)) {
////                miQuery = "SELECT precio_seq.NEXTVAL FROM DUAL";
////                pst = getConnection().prepareStatement(miQuery);
////                rst = pst.executeQuery();
////                Integer precioId = (rst.next()) ? rst.getInt(1) : 0;
////                precio.setPrecioId(precioId);
//                miQuery = "INSERT INTO precio (turno_id, producto_id, tipodespacho_id, precio, creado_por, creado_persona, creado_el) "
//                        + "VALUES (?, ?, ?, ?, ?, ?, SYSDATE)";
//                pst = getConnection().prepareStatement(miQuery, Statement.RETURN_GENERATED_KEYS);
////                pst.setInt(1, precio.getPrecioId());
//                pst.setInt(1, precio.getTurnoId());
//                pst.setInt(2, precio.getProductoId());
//                pst.setInt(3, precio.getTipodespachoId());
//                pst.setDouble(4, precio.getPrecio());
//                pst.setString(5, precio.getCreadoPor());
//                pst.setString(6, precio.getCreadoPersona());
//                pst.executeUpdate();
//                rst = pst.getGeneratedKeys();
//                result = (rst.next()) ? precio : null;
//            } else 
            if (action.equals(ACTION_UPDATE)) {
                miQuery = "UPDATE precio "
                        + "SET precio = ?, modificado_por = ?, modificado_persona = ?, modificado_el = SYSDATE "
                        + "WHERE turno_id = ? AND producto_id = ? AND tipodespacho_id = ?";
                pst = getConnection().prepareStatement(miQuery);
                pst.setDouble(1, precio.getPrecio());
                pst.setString(2, precio.getModificadoPor());
                pst.setString(3, precio.getModificadoPersona());
                pst.setInt(4, precio.getTurnoId());
                pst.setInt(5, precio.getProductoId());
                pst.setInt(6, precio.getTipodespachoId());
                pst.executeUpdate();
                result = precio;
            }
        } catch (Exception exc) {
            result.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public Dia getDiaActivoByEstacionid(Integer estacionId) {
        Dia result = new Dia();
        ResultSet rst = null;
        try {
            miQuery = "SELECT estacion_id, fecha, estado_id, creado_por, creado_persona "
                    + "FROM dia "
                    + "WHERE estado_id = 1 AND estacion_id = " + estacionId;
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            result = (rst.next()) ? new Dia(rst.getInt(1), rst.getDate(2), rst.getInt(3), rst.getString(4), rst.getString(5)) : result;
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    /*Esta funcion es basicamente para obtener el estado de un dia seleccionado en un control de fecha.*/
    public Dia getDiaByEstacionidFecha(Integer estacionId, Date fecha) {
        Dia result = new Dia();
        ResultSet rst = null;
        try {
            miQuery = "SELECT estacion_id, fecha, estado_id, creado_por, creado_persona "
                    + "FROM dia "
                    + "WHERE estacion_id = ? AND fecha = ? "
                    + " ORDER BY fecha DESC";
            pst = getConnection().prepareStatement(miQuery);
            pst.setObject(1, estacionId);
            pst.setDate(2, (fecha == null) ? null : new java.sql.Date(fecha.getTime()));
            rst = pst.executeQuery();
            result = (rst.next()) ? new Dia(rst.getInt(1), rst.getDate(2), rst.getInt(3), rst.getString(4), rst.getString(5)) : result;
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public Dia getUltimoDiaByEstacionid(Integer estacionId) {
        Dia result = new Dia();
        ResultSet rst = null;
        try {
            miQuery = "SELECT estacion_id, fecha, estado_id, creado_por, creado_persona "
                    + "FROM dia "
                    + "WHERE estacion_id = " + estacionId
                    + " ORDER BY fecha DESC";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            result = (rst.next()) ? new Dia(rst.getInt(1), rst.getDate(2), rst.getInt(3), rst.getString(4), rst.getString(5)) : result;
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Acceso> getAccesosByUsuarioid(Integer usuarioId, boolean isSysadmin) {
        List<Acceso> result = new ArrayList();
        List<Acceso> tempList = new ArrayList();
        ResultSet rst = null;
        try {
            isSysadmin = false;
            //Si el usuario es SYSADMIN, trae todos los accesos.
            /*modificado por cambios de seguridad anterior
            String isNotSysadmin = (isSysadmin) ? " WHERE a.estado = 'A' " : ", acceso_rol ar, rol_usuario ru, usuario u WHERE a.estado = 'A' AND a.acceso_id = ar.acceso_id AND ar.rol_id = ru.rol_id AND ru.usuario_id = u.usuario_id AND u.usuario_id = " + usuarioId;
            
             miQuery = "SELECT DISTINCT a.acceso_id, a.titulo, a.padre, a.orden, a.recurso_interno, a.descripcion, a.estado "
                    + " ,ar.ver,ar.cambiar,ar.agregar,ar.eliminar "
                    + "FROM acceso a " + isNotSysadmin
                    + " CONNECT BY PRIOR a.acceso_id = a.padre "
                    + " START WITH a.padre IS NULL "
                    + " ORDER BY NVL(a.padre, 0), a.orden";
            System.out.println("valida user = "+miQuery);
            */
            String isNotSysadmin = (isSysadmin) ? " WHERE a.estado = 'A' " : ", acceso_rol ar, rol_usuario ru, usuario u WHERE a.estado = 'A' AND a.acceso_id = ar.acceso_id AND ar.rol_id = ru.rol_id AND ru.usuario_id = u.usuario_id AND u.usuario_id = " + usuarioId;
            miQuery = "SELECT DISTINCT a.acceso_id, a.titulo, a.padre, a.orden, a.recurso_interno, a.descripcion, a.estado "
//                    + " ,ar.ver,ar.cambiar,ar.agregar,ar.eliminar "
                    + "FROM acceso a " + isNotSysadmin
                    + " CONNECT BY PRIOR a.acceso_id = a.padre "
                    + " ORDER BY NVL(a.padre, 0), a.orden";
            System.out.println("valida user = "+miQuery);
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            Acceso access;
            while (rst.next()) {
                access = new Acceso();
                access.setAccesoId(rst.getInt(1));
                access.setTitulo(rst.getString(2));
                access.setPadre(rst.getInt(3));
                access.setOrden(rst.getInt(4));
                access.setRecursoInterno(rst.getString(5));
                access.setDescripcion(rst.getString(6));
                access.setEstado(rst.getString(7));
                access.setAccesos(new ArrayList<Acceso>());
//                access.setVer(rst.getInt(8)==1?true:false);
//                access.setCambiar(rst.getInt(9)==1?true:false);
//                access.setAgregar(rst.getInt(10)==1?true:false);
//                access.setEliminar(rst.getInt(11)==1?true:false);
                
                if (rst.getString(3) == null || rst.getInt(3) == 0) { //padres
                    result.add(access);
////                    result.add(new Acceso(rst.getInt(1), rst.getString(2), rst.getInt(3), rst.getInt(4), rst.getString(5), rst.getString(6), rst.getString(7)));
                } else { //hijos
//                    tempList.add(new Acceso(rst.getInt(1), rst.getString(2), rst.getInt(3), rst.getInt(4), rst.getString(5), rst.getString(6), rst.getString(7)));
                    tempList.add(access);
                }
            }
            result = getParents();
            for (Acceso a : result) {
                for (Acceso acc : tempList) {
                    if (a.getAccesoId().equals(acc.getPadre())) {
                        a.getAccesos().add(acc);
                    }
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Estacion> getAllEstaciones(boolean includeInactive) {
        List<Estacion> result = new ArrayList();
        String statusName;
        ResultSet rst = null;
        try {
            miQuery = (includeInactive) ? "" : " AND e.estado = 'A' ";
            miQuery = "SELECT e.estacion_id, e.nombre, e.codigo, "
                    + "e.pais_id, e.estado, p.nombre, "
                    + "e.fact_electronica, p.codigo, e.bu, e.deposito, e.codigo_envoy"
                    + ", e.id_marca, m.nombre " //12,13
                    + "FROM estacion e, pais p, marca m "
                    + "WHERE e.pais_id = p.pais_id AND e.id_marca = m.id_marca "
                    + miQuery
                    + " ORDER BY p.nombre, e.codigo ";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            Estacion estacion;
            while (rst.next()) {
                estacion = new Estacion(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5), rst.getString(7));
                estacion.setPaisNombre(rst.getString(6));
                estacion.setPais(new Pais(rst.getInt(4), rst.getString(6), rst.getString(8), "", "", "",false));
                estacion.setBombas(new ArrayList());
                estacion.setProductos(new ArrayList());
                estacion.getBombas().addAll(getBombasByEstacionid(rst.getInt(1)));
                estacion.getProductos().addAll(getProductosByEstacionid(rst.getInt(1)));
                estacion.setBu(rst.getString(9));
                estacion.setDeposito(rst.getString(10));
                statusName = (rst.getString(5).equals("A")) ? "Activo" : "Inactivo";
                estacion.setStatus(new DtoGenericBean(rst.getString(5), statusName));
                estacion.setCodigoEnvoy(rst.getString(11));
                estacion.setIdMarca(rst.getInt(12));
                estacion.setBrand(new Marca(rst.getInt(12), rst.getString(13), "A"));
                result.add(estacion);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Producto> getProductosByEstacionid(Integer estacionId) {
        List<Producto> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = "SELECT p.producto_id, p.nombre, p.codigo, p.estado, p.creado_por, p.orden_pos "
                    //                    + ", p.tipo_id "
                    + "FROM estacion_producto ep, producto p "
                    + "WHERE ep.producto_id = p.producto_id AND ep.estacion_id = " + estacionId
                    + " ORDER BY producto_id";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getInt(6)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {

            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<EstacionConfHead> getConfiguracionHeadByEstacionid(Integer estacionId, boolean addInactive) {
        List<EstacionConfHead> result = new ArrayList();
        ResultSet rst = null;
        try {
            String query = addInactive ? "" : " AND estado = 'A' ";
            query = "SELECT estacionconfhead_id, nombre, estacion_id, estado, creado_por, hora_inicio, hora_fin "
                    + "FROM estacion_conf_head "
                    + "WHERE estacion_id = " + estacionId
                    + query
                    + " ORDER BY nombre";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            EstacionConfHead estacionConfHead;
            while (rst.next()) {
                estacionConfHead = new EstacionConfHead(rst.getInt(1), rst.getString(2), rst.getInt(3), rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7));
                estacionConfHead.setEstacionConf(getConfiguracionByEstconfhead(rst.getInt(1)));
                result.add(estacionConfHead);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<EstacionConfHead> getAllConfiguracionHead(boolean addInactive) {
        List<EstacionConfHead> result = new ArrayList();
        ResultSet rst = null;
        try {
            String query = addInactive ? "" : " AND estado = 'A' ";
            query = "SELECT estacionconfhead_id, nombre, estacion_id, estado, creado_por, hora_inicio, hora_fin "
                    + "FROM estacion_conf_head "
                    + "WHERE estacion_id IS NULL "
                    + query
                    + " ORDER BY nombre";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            EstacionConfHead estacionConfHead;
            while (rst.next()) {
                estacionConfHead = new EstacionConfHead(rst.getInt(1), rst.getString(2), rst.getInt(3), rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7));
                estacionConfHead.setEstacionConf(getConfiguracionByEstconfhead(rst.getInt(1)));
                result.add(estacionConfHead);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<EstacionConf> getConfiguracionByEstconfhead(Integer estconfhead) {
        List<EstacionConf> result = new ArrayList();
        ResultSet rst = null;
        try {
            String query = "SELECT ec.estacionconfhead_id, ec.bomba_id, ec.tipodespacho_id, b.nombre, ec.estacion_id "
                    + "FROM estacion_conf ec, bomba b "
                    + "WHERE ec.bomba_id = b.bomba_id AND ec.estacionconfhead_id = " + estconfhead;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            EstacionConf ecf;
            int index = 1;
            while (rst.next()) {
                ecf = new EstacionConf(rst.getInt(1), rst.getInt(2), rst.getInt(3), null);
                ecf.setIdDto(index++);
                ecf.setBombaNombre(rst.getString(4));
                ecf.setAutoService(rst.getInt(3) == 1);   //1:Auto, 2:Full
                ecf.setEstacionId(rst.getInt(5));
                tmpString = (rst.getInt(3) == 1) ? "Auto" : "Full";
                ecf.setTypeServ(new DtoGenericBean(rst.getInt(3), tmpString));
                result.add(ecf);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public Parametro getParameterByName(String name) {
        Parametro result = null;
        miQuery = "SELECT parametro_id, nombre, valor, descripcion, estado, creado_por, creado_el "
                + "FROM parametro "
                + "WHERE nombre = ?";
        try {
            pst = getConnection().prepareStatement(miQuery);
            pst.setString(1, name);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                result = new Parametro(rst.getShort(1), rst.getString(2), rst.getString(3), rst.getString(5), rst.getString(6), new java.util.Date(rst.getDate(7).getTime()));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return result;
    }

    public List<Empleado> getEmpleadosByTurnoid(int turnoId) {
        List<Empleado> result = new ArrayList();
        try {
            miQuery = "SELECT DISTINCT e.empleado_id, e.nombre, e.estado, a.arqueocaja_id, a.estacion_id, a.turno_id, a.fecha, a.estado_id, a.nombre_pistero, a.nombre_jefe "
                    + "FROM empleado e, turno_empleado_bomba teb "
                    + "LEFT JOIN arqueocaja a ON teb.empleado_id = a.empleado_id AND teb.turno_id = a.turno_id "
                    + "WHERE teb.empleado_id = e.empleado_id AND teb.turno_id = " + turnoId
                    + " ORDER BY e.nombre";
            pst = getConnection().prepareStatement(miQuery);
            ResultSet rst = pst.executeQuery();
            Empleado emp;
            while (rst.next()) {
                emp = new Empleado(rst.getInt(1), rst.getString(2));
                if (rst.getString(4) != null) {
                    emp.setArqueo(new Arqueocaja(rst.getInt(4), rst.getInt(5), rst.getInt(6), rst.getDate(7), rst.getInt(8), null, null, null, rst.getString(9), rst.getString(10)));
                }
                result.add(emp);
            }
            closePst();
            for (Empleado item : result) {
                item.setBombas(new ArrayList());
                miQuery = "SELECT b.bomba_id, b.nombre, b.estado, b.creado_por, b.creado_el, b.isla "
                        + "FROM turno_empleado_bomba bee, empleado e, bomba b "
                        + "WHERE bee.bomba_id = b.bomba_id AND bee.empleado_id = e.empleado_id AND bee.turno_id = " + turnoId + " AND bee.empleado_id = " + item.getEmpleadoId();
                pst = getConnection().prepareStatement(miQuery);
                rst = pst.executeQuery();
                while (rst.next()) {
                    item.getBombas().add(new Bomba(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), new java.util.Date(rst.getDate(5).getTime()), null));
                }
                closePst();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }
    
    public List<Bomba> getAllBombas(boolean includeInactive) {
        List<Bomba> result = new ArrayList();
        try {
            miQuery = (includeInactive) ? "" : " WHERE estado = 'A' ";
            miQuery = "SELECT bomba_id, nombre, estado "
                    + "FROM bomba "
                    + miQuery
                    + " ORDER BY isla, bomba_id";
            pst = getConnection().prepareStatement(miQuery);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Bomba(rst.getInt(1), rst.getString(2), rst.getString(3), null, null, null));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<EstacionConfHead> getConfiguracionHeadByEstacionidHorario(Integer estacionId, int horarioId) {
        List<EstacionConfHead> result = new ArrayList();
        ResultSet rst = null;
        try {
            String query = "";// = addInactive ? "" : " AND ech.estado = 'A' ";
            query = "SELECT DISTINCT ech.estacionconfhead_id, ech.nombre, ech.estacion_id, ech.estado, ech.creado_por, h.hora_inicio, h.hora_fin "
                    + "FROM estacion_conf_head ech, horario h, estacion_horario eh "
                    + "WHERE h.horario_id = eh.horario_id AND eh.estacionconfhead_id = ech.estacionconfhead_id AND eh.estacion_id = " + estacionId + " AND h.horario_id = " + horarioId
                    + query
                    + " ORDER BY ech.nombre";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            EstacionConfHead estacionConfHead;
            while (rst.next()) {
                estacionConfHead = new EstacionConfHead(rst.getInt(1), rst.getString(2), rst.getInt(3), rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7));
                estacionConfHead.setEstacionConf(getConfiguracionByEstconfhead(rst.getInt(1)));
                result.add(estacionConfHead);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Producto> getAllProductosByTypeMarca(int type, boolean includeInactive, Integer brandId) {
        List<Producto> result = new ArrayList();
        ResultSet rst = null;
        try {
            tmpString = (brandId == null) ? "" : " AND p.id_marca = " + brandId;
            miQuery = (includeInactive) ? "" : " AND p.estado = 'A' ";
            miQuery = "SELECT p.producto_id, p.nombre, p.codigo, p.estado, p.creado_por, p.orden_pos, NVL(m.id_marca, 0), NVL(m.nombre, '') "
                    //                    + ", p.tipo_id "
                    + "FROM producto p "
                    + "LEFT JOIN marca m ON p.id_marca = m.id_marca "
                    + "WHERE p.tipo_id = " + type
                    + miQuery + tmpString
                    + " ORDER BY p.tipo_id";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            Producto producto;
            while (rst.next()) {
                producto = new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getInt(6));
                producto.setMarca(new Marca(rst.getInt(7), rst.getString(8), null));
                result.add(producto);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Producto> getAllProductosByCountryTypeBrand(Integer countryId, int type, Integer brandId, boolean includeInactive) {
        List<Producto> result = new ArrayList();
        ResultSet rst = null;
        try {
            tmpString = (brandId == null) ? "" : " AND p.id_marca = " + brandId;
            miQuery = (includeInactive) ? "" : " AND p.estado = 'A' ";
            miQuery = "SELECT p.producto_id, p.nombre, p.codigo, p.estado, p.creado_por, p.orden_pos, NVL(m.id_marca, 0), NVL(m.nombre, '') "
                    + "FROM producto p "
                    + "LEFT JOIN estacion_producto ep ON ep.producto_id = p.producto_id "
                    + "LEFT JOIN marca m ON p.id_marca = m.id_marca "
                    + "LEFT JOIN estacion e ON e.estacion_id = ep.estacion_id AND e.pais_id = " + countryId
                    + " WHERE p.tipo_id = " + type
                    + miQuery
                    + tmpString
                    + " ORDER BY m.nombre, p.nombre";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            Producto producto;
            while (rst.next()) {
                producto = new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getInt(6));
                producto.setMarca(new Marca(rst.getInt(7), rst.getString(8), null));
                result.add(producto);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public List<Marca> getAllBrands(boolean includeInactive) {
        List<Marca> result = new ArrayList();
        miQuery = (includeInactive) ? "" : " WHERE estado = 'A' ";
        try {
            miQuery = "SELECT id_marca, nombre, estado FROM marca" + miQuery;
            pst = getConnection().prepareStatement(miQuery);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Marca(rst.getInt(1), rst.getString(2), rst.getString(3)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public List<Estacion> getStationsByCountryUser(Integer paisId, Integer userId) {
        List<Estacion> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = "SELECT e.estacion_id, e.nombre, e.codigo, e.pais_id, e.estado, e.fact_electronica "
                    + "FROM estacion e, estacion_usuario eu "
                    + "WHERE e.estacion_id = eu.estacion_id AND e.pais_id = " + paisId + " AND eu.usuario_id = " + userId;
            rst = getConnection().prepareStatement(miQuery).executeQuery();
            while (rst.next()) {
                result.add(new Estacion(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5), rst.getString(6)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public List<EstacionConfHead> getAllConfiguracionheadPais() {
        List<EstacionConfHead> result = new ArrayList();
        ResultSet rst = null;
        try {
            String query = "SELECT DISTINCT ech.estacionconfhead_id, ech.nombre, ech.estado, ec.estacion_id "
                    + "FROM estacion_conf_head ech, estacion_conf ec "
                    + "WHERE ech.estado = 'A' AND ech.estacionconfhead_id = ec.estacionconfhead_id";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            EstacionConfHead estacionConfHead;
            while (rst.next()) {
                estacionConfHead = new EstacionConfHead(rst.getInt(1), rst.getString(2), rst.getInt(4), rst.getString(3), null, null, null);
//                estacionConfHead.setEstacionConf(getConfiguracionByEstconfhead(rst.getInt(1)));
                result.add(estacionConfHead);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public String[] getUniqueStation(int userId) {
        String[] result = null;
        try {
            miQuery = "SELECT COUNT(*) FROM estacion_usuario eu, estacion e, pais p WHERE eu.estacion_id = e.estacion_id AND e.pais_id = p.pais_id AND eu.usuario_id = " + userId;
            pst = getConnection().prepareStatement(miQuery);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                if (rst.getInt(1) == 1) {
                    closePst();
                    miQuery = "SELECT e.pais_id, e.estacion_id FROM estacion_usuario eu, estacion e, pais p WHERE eu.estacion_id = e.estacion_id AND e.pais_id = p.pais_id AND eu.usuario_id = " + userId;
                    pst = getConnection().prepareStatement(miQuery);
                    rst = pst.executeQuery();
                    if (rst.next()) {
                        result = new String[]{"", ""};  //countryId, stationId
                        result[0] = rst.getString(1);
                        result[1] = rst.getString(2);
                    }
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }
    
     public List<Acceso> getParents() {
        List<Acceso> p = new ArrayList();
        ResultSet rst = null;
        try {
            String query =  " select ACCESO_ID,TITULO,PADRE,ORDEN,DESCRIPCION " +
                            " from acceso where PADRE is null and upper(estado)='A' order by orden";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
             Acceso access;
            while (rst.next()) {
                access = new Acceso();
                access.setAccesoId(rst.getInt(1));
                access.setTitulo(rst.getString(2));
                access.setPadre(rst.getInt(3));
                access.setOrden(rst.getInt(4));
                access.setDescripcion(rst.getString(5));
                access.setAccesos(new ArrayList<Acceso>());
                p.add(access);
                
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return p;
    }
    public Acceso getAccess(String screen){
        Acceso acceso = new Acceso();
         Usuario user = ((Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName()));
                for (Acceso a : user.getRoles().get(0).getAccesos()) {
                    if (a.getRecursoInterno().trim().toUpperCase().equals(screen)) {
                        System.out.println(screen + " - " + a.getTitulo() + " ACCIONES  " 
                                + a.isVer() + " " + a.isCambiar() + " - " + a.isAgregar());
                        acceso.setVer(true);
                        acceso.setCambiar(a.isCambiar());
                        acceso.setEliminar(a.isEliminar());
                        acceso.setAgregar(a.isAgregar());
                        return acceso;
                    }
                }
                return acceso;
    }    
}
