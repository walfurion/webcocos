package com.fundamental.services;

import com.fundamental.model.Arqueocaja;
import com.fundamental.model.ArqueocajaBomba;
import com.fundamental.model.ArqueocajaDetalle;
import com.fundamental.model.ArqueocajaProducto;
import com.fundamental.model.Bomba;
import com.fundamental.model.Dia;
import com.fundamental.model.Efectivo;
import com.sisintegrados.generic.bean.Empleado;
import com.fundamental.model.FactelectronicaPos;
import com.fundamental.model.Horario;
import com.fundamental.model.Precio;
import com.fundamental.model.Turno;
import com.sisintegrados.generic.bean.TurnoEmpleadoBomba;
import com.fundamental.utils.Constant;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.generic.bean.EmpleadoBombaTurno;
import com.sisintegrados.generic.bean.Pais;
import com.vaadin.data.util.BeanItemContainer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Henry Barrientos
 */
public class SvcTurno extends DaoImp {

    private String query;

    public SvcTurno() {
    }

    public Boolean puedeCrearTurnoEnHorario(Integer horafin) {
        Boolean result = false;
        query = "SELECT * FROM turno "
                + "WHERE hora_fin = ? AND TRUNC(creado_el) = TO_DATE(?, 'dd/mm/yyyy') AND estado_id = ?";
        ResultSet rst = null;
        try {
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, horafin);
            pst.setString(2, new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            pst.setInt(3, 1);   //Estado abierto
            rst = pst.executeQuery();
            result = !rst.next();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public Arqueocaja saveArqueo(String action, Arqueocaja arqueo) {
        Arqueocaja result = new Arqueocaja();
        ResultSet rst = null;
        try {
            if (action.equals(ACTION_ADD)) {
                query = "SELECT arqueocaja_seq.NEXTVAL FROM DUAL";
                pst = getConnection().prepareStatement(query);
                rst = pst.executeQuery();
                Integer arqueoId = (rst.next()) ? rst.getInt(1) : 0;
                arqueo.setArqueocajaId(arqueoId);
                try {
                    rst.close();
                    pst.close();
                    closeConnections(); //asg
                } catch (Exception ignore) {
                }

                query = "INSERT INTO arqueocaja (arqueocaja_id, estado_id, creado_por, creado_persona, estacion_id, turno_id, fecha, empleado_id, nombre_jefe, nombre_pistero) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, arqueo.getArqueocajaId());
                pst.setInt(2, arqueo.getEstado_id());
                pst.setString(3, arqueo.getCreado_por());
                pst.setString(4, arqueo.getCreado_persona());
                pst.setInt(5, arqueo.getEstacionId());
                pst.setInt(6, arqueo.getTurnoId());
                pst.setDate(7, new java.sql.Date(arqueo.getFecha().getTime()));
                pst.setInt(8, arqueo.getEmpleadoId());
                pst.setString(9, arqueo.getNombreJefe());
                pst.setString(10, arqueo.getNombrePistero());
                pst.executeUpdate();
                result = arqueo;
                closePst();
                closeConnections(); //asg
            } else if (action.equals(ACTION_UPDATE)) {
                query = "UPDATE arqueocaja "
                        + "SET estado_id = ?, modificado_por = ?, modificado_persona = ?, estacion_id = ?, turno_id = ?, modificado_el = SYSDATE, nombre_jefe = ?, nombre_pistero = ? "
                        + "WHERE arqueocaja_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, arqueo.getEstado_id());
                pst.setString(2, arqueo.getModificadoPor());
                pst.setString(3, arqueo.getModificadoPersona());
                pst.setInt(4, arqueo.getEstacionId());
                pst.setInt(5, arqueo.getTurnoId());
                pst.setString(6, arqueo.getNombreJefe());
                pst.setString(7, arqueo.getNombrePistero());
                pst.setInt(8, arqueo.getArqueocajaId());
                pst.executeUpdate();
                result = arqueo;
                closePst();
                closeConnections(); //asg
            }
        } catch (Exception exc) {
            arqueo.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public ArqueocajaDetalle saveArqueoDetalle(String action, ArqueocajaDetalle arqueod) {
        ArqueocajaDetalle result = new ArqueocajaDetalle();
        try {
            if (action.equals(ACTION_ADD)) {
                query = "INSERT INTO arqueocaja_detalle (arqueocaja_id, mediopago_id, doctos, monto, creado_por) "
                        + "VALUES (?, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, arqueod.getArqueocajaId());
                pst.setInt(2, arqueod.getMediopagoId());
                pst.setInt(3, arqueod.getDoctos());
                pst.setDouble(4, arqueod.getMonto());
                pst.setString(5, arqueod.getCreadoPor());
                pst.executeUpdate();
                result = arqueod;
                closePst();
                closeConnections(); //asg
            } else if (action.equals(ACTION_UPDATE)) {
                query = "UPDATE arqueocaja_detalle "
                        + "SET doctos = ?, monto = ?, modificado_por = ?, modificado_el = SYSDATE "
                        + "WHERE arqueocaja_id = ? AND mediopago_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, arqueod.getDoctos());
                pst.setDouble(2, arqueod.getMonto());
                pst.setString(3, arqueod.getModificadoPor());
                pst.setInt(4, arqueod.getArqueocajaId());
                pst.setInt(5, arqueod.getMediopagoId());
                pst.executeUpdate();
                result = arqueod;
                closePst();
                closeConnections(); //asg
            } else if (action.equals(ACTION_DELETE)) {
                query = "DELETE FROM arqueocaja_detalle "
                        + "WHERE arqueocaja_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, arqueod.getArqueocajaId());
                pst.executeUpdate();
                result = arqueod;
                closePst();
                closeConnections(); //asg
            }
        } catch (Exception exc) {
            arqueod.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {
            try {
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public ArqueocajaBomba saveArqueoBomba(String action, ArqueocajaBomba objeto) {
        ArqueocajaBomba result = new ArqueocajaBomba();
        try {
            if (action.equals(ACTION_ADD)) {
                query = "INSERT INTO arqueocaja_bomba (arqueocaja_id, bomba_id, turno_id) "
                        + "VALUES (?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, objeto.getArqueocajaId());
                pst.setInt(2, objeto.getBombaId());
                pst.setInt(3, objeto.getTurnoId());
                pst.executeUpdate();
                result = objeto;
                closePst();
                closeConnections(); //asg
            } else if (action.equals(ACTION_UPDATE)) {
                //Nothing to do here.
            } else if (action.equals(ACTION_DELETE)) {
                if (objeto.getArqueocajaId() != null) {
                    query = "DELETE FROM arqueocaja_bomba "
                            + "WHERE arqueocaja_id = ?";
                    pst = getConnection().prepareStatement(query);
                    pst.setInt(1, objeto.getArqueocajaId());
                } else {
                    query = "DELETE FROM arqueocaja_bomba "
                            + "WHERE turno_id = ? AND bomba_id = ?";
                    pst = getConnection().prepareStatement(query);
                    pst.setObject(1, objeto.getTurnoId());
                    pst.setObject(2, objeto.getBombaId());
                }
                pst.executeUpdate();
                result = objeto;
                closePst();
                closeConnections(); //asg
            }
        } catch (Exception exc) {
            objeto.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {
            try {
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public ArqueocajaProducto saveArqueoProducto(String action, ArqueocajaProducto arqueoProd) {
        ArqueocajaProducto result = new ArqueocajaProducto();
        ResultSet rst = null;
        try {
            if (action.equals(ACTION_ADD)) {
                query = "INSERT INTO arqueocaja_producto (arqueocaja_id, producto_id, monto) "
                        + "VALUES (?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, arqueoProd.getArqueocajaId());
                pst.setInt(2, arqueoProd.getProductoId());
                pst.setDouble(3, arqueoProd.getMonto());
                pst.executeUpdate();
                result = arqueoProd;
                closePst();
                closeConnections(); //asg
            } else if (action.equals(ACTION_UPDATE)) {
                query = "UPDATE arqueocaja_producto "
                        + "SET monto = ? "
                        + "WHERE arqueocaja_id = ? AND producto_id = ? ";
                pst = getConnection().prepareStatement(query);
                pst.setDouble(1, arqueoProd.getMonto());
                pst.setInt(2, arqueoProd.getArqueocajaId());
                pst.setInt(3, arqueoProd.getProductoId());
                pst.executeUpdate();
                result = arqueoProd;
                closePst();
                closeConnections(); //asg
            } else if (action.equals(ACTION_DELETE)) {
                query = "DELETE FROM arqueocaja_producto "
                        + "WHERE arqueocaja_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, arqueoProd.getArqueocajaId());
                pst.executeUpdate();
                result = arqueoProd;
                closePst();
                closeConnections(); //asg
            }
        } catch (Exception exc) {
            arqueoProd.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {
            try {
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public Efectivo doActionEfectivo(String action, Efectivo efectivo) {
        Efectivo result = new Efectivo();
        ResultSet rst = null;
        try {
            if (action.equals(ACTION_ADD)) {
                query = "INSERT INTO efectivo (efectivo_id, arqueocaja_id, mediopago_id, orden, monto, tasa, mon_extranjera) "
                        + "VALUES (efectivo_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, efectivo.getArqueocajaId());
                pst.setInt(2, efectivo.getMediopagoId());
                pst.setInt(3, efectivo.getOrden());
                pst.setDouble(4, efectivo.getMonto());
                pst.setObject(5, efectivo.getTasa());
                pst.setObject(6, efectivo.getMonExtranjera());
                pst.executeUpdate();
                result = efectivo;
                closePst();
                closeConnections(); //asg
            } //            else if (action.equals(ACTION_UPDATE)) {
            //                query = "UPDATE efectivo "
            //                        + "SET orden = ?, monto = ? "
            //                        + "WHERE arqueocaja_id = ? AND mediopago_id = ?";
            //                pst = getConnection().prepareStatement(query);
            //                pst.setInt(1, efectivo.getOrden());
            //                pst.setDouble(2, efectivo.getMonto());
            //                pst.setInt(3, efectivo.getArqueocajaId());
            //                pst.setInt(4, efectivo.getMediopagoId());
            //                pst.executeUpdate();
            //                result = efectivo;
            //            } 
            else if (action.equals(ACTION_DELETE)) {
                query = "DELETE FROM efectivo "
                        + "WHERE arqueocaja_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, efectivo.getArqueocajaId());
                pst.executeUpdate();
                result = efectivo;
                closePst();
                closeConnections(); //asg
            }
        } catch (Exception exc) {
            result.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {
            try {
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public FactelectronicaPos doActionFactelectronicaPos(String action, FactelectronicaPos fepos) {
        FactelectronicaPos result = new FactelectronicaPos();
        ResultSet rst = null;
        try {
            if (action.equals(ACTION_ADD)) {
                query = "INSERT INTO factelectronica_pos (arqueocaja_id, producto_id, galones, monto) "
                        + "VALUES (?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, fepos.getArqueocajaId());
                pst.setInt(2, fepos.getProducto_id());
                pst.setDouble(3, fepos.getGalones());
                pst.setDouble(4, fepos.getMonto());
                pst.executeUpdate();
                result = fepos;
                rst.close();
                closePst();
                closeConnections(); //asg
            } else if (action.equals(ACTION_UPDATE)) {
                //nothing to do here
            }
        } catch (Exception exc) {
            result.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<Precio> getPreciosByTurnoid(Integer turnoId) {
        List<Precio> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = "SELECT turno_id, producto_id, tipodespacho_id, precio "
                    + "FROM precio "
                    + "WHERE turno_id = " + turnoId;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Precio(rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getDouble(4), null, null));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<TurnoEmpleadoBomba> getTurnoEmpBombaByTurnoid(Integer turnoId) {
        List<TurnoEmpleadoBomba> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = "SELECT teb.turno_id, teb.empleado_id, teb.bomba_id, teb.creado_por, e.nombre, b.nombre "
                    + "FROM turno_empleado_bomba teb, empleado e, bomba b "
                    + "WHERE teb.bomba_id = b.bomba_id AND teb.empleado_id = e.empleado_id AND teb.turno_id = " + turnoId;
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            int fakeId = 1;
            TurnoEmpleadoBomba item;
            while (rst.next()) {
                item = new TurnoEmpleadoBomba(fakeId++, rst.getInt(1), rst.getInt(2), rst.getInt(3), rst.getString(4));
                item.setEmployee(new Empleado(rst.getInt(2), rst.getString(5)));
                item.setPump(new Bomba(rst.getInt(3), rst.getString(6), null, null, null, null));
                result.add(item);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
                closeConnections(); //asg
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public List<EmpleadoBombaTurno> getTurnoEmpBombaByTurnoid2(Integer turnoId) {
        List<EmpleadoBombaTurno> result = new ArrayList();
        ResultSet rst = null;
        Integer maxBombas = 0;
        try {
            query = "SELECT teb.turno_id,e.empleado_id,e.nombre "
                    + "FROM turno_empleado_bomba teb, empleado e, bomba b "
                    + "WHERE teb.bomba_id = b.bomba_id AND teb.empleado_id = e.empleado_id AND teb.turno_id = " + turnoId
                    + " GROUP BY e.nombre,e.empleado_id,TEB.TURNO_ID ORDER BY e.empleado_id";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();

//            rst.close();
//            closePst();
//            closeConnections(); //asg

            EmpleadoBombaTurno item;

            String Vquery = "SELECT max(teb.bomba_id) MaxBomba "
                    + "FROM turno_empleado_bomba teb, empleado e, bomba b "
                    + "WHERE teb.bomba_id = b.bomba_id AND teb.empleado_id = e.empleado_id AND teb.turno_id = " + turnoId
                    + " ORDER BY e.empleado_id";

            PreparedStatement pstC = null;
            ResultSet rstC = null;
            pstC = getConnection().prepareStatement(Vquery);
            rstC = pstC.executeQuery();

            while (rstC.next()) {
                maxBombas = rstC.getInt(1);
            }

            while (rst.next()) {
                item = new EmpleadoBombaTurno(rst.getInt(1), rst.getInt(2), rst.getString(3));
                PreparedStatement pstB = null;
                ResultSet rstB = null;

                String query2 = "SELECT teb.turno_id,e.empleado_id,e.nombre,teb.bomba_id "
                        + "FROM turno_empleado_bomba teb, empleado e, bomba b "
                        + "WHERE teb.bomba_id = b.bomba_id AND teb.empleado_id = e.empleado_id AND teb.turno_id = " + turnoId
                        + " AND e.empleado_id = " + item.getEmpleadoid()
                        + " ORDER BY e.empleado_id";
                pstB = getConnection().prepareStatement(query2);
                rstB = pstB.executeQuery();

                while (rstB.next()) {
                    if (rstB.getInt(4) == 1) {
                        item.setBomba1(true);
                    }
                    if (rstB.getInt(4) == 2) {
                        item.setBomba2(true);
                    }
                    if (rstB.getInt(4) == 3) {
                        item.setBomba3(true);
                    }
                    if (rstB.getInt(4) == 4) {
                        item.setBomba4(true);
                    }
                    if (rstB.getInt(4) == 5) {
                        item.setBomba5(true);
                    }
                    if (rstB.getInt(4) == 6) {
                        item.setBomba6(true);
                    }
                    if (rstB.getInt(4) == 7) {
                        item.setBomba7(true);
                    }
                    if (rstB.getInt(4) == 8) {
                        item.setBomba8(true);
                    }
                    if (rstB.getInt(4) == 9) {
                        item.setBomba9(true);
                    }
                    if (rstB.getInt(4) == 10) {
                        item.setBomba10(true);
                    }
                    if (rstB.getInt(4) == 11) {
                        item.setBomba11(true);
                    }
                    if (rstB.getInt(4) == 12) {
                        item.setBomba12(true);
                    }
                }
                item.setMaxBombas(maxBombas);
                pstC.close();
                rstC.close();
                result.add(item);
                rstB.close();
                pstB.close();
//                closeConnections(); //asg
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }

                if (rst != null) {
                    rst.close();
                }
                closePst();
                closeConnections(); //asg
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public List<Empleado> getEmpleados(boolean onlyActive) {
        List<Empleado> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = (onlyActive) ? " AND e.estado = 'A' " : "";
            miQuery = "SELECT e.empleado_id, e.nombre, e.estado "
                    + "FROM empleado e "
                    + "WHERE 1 = 1 " + miQuery;
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Empleado(rst.getInt(1), rst.getString(2)));
            }
            closePst();
            closeConnections(); //asg
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

    public List<Empleado> getEmpleados2(boolean onlyActive) {
        List<Empleado> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = (onlyActive) ? " AND e.estado = 'A' " : "";
            miQuery = "SELECT e.empleado_id, e.nombre, e.estado "
                    + "FROM empleado e "
                    + "WHERE 1 = 1 " + miQuery;
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Empleado(rst.getInt(1), rst.getString(2), rst.getString(3)));
            }
            closePst();
            closeConnections(); //asg
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

    public List<Bomba> getBombasByEstacionConfheadId(int estacionConfheadId, int idstation) {
        List<Bomba> result = new ArrayList();
        ResultSet rst = null;
        try {
            miQuery = "SELECT b.bomba_id, b.nombre, b.estado, b.creado_por, b.creado_el "
                    + "FROM estacion_conf ec, bomba b "
                    + "WHERE ec.bomba_id = b.bomba_id AND ec.estacionconfhead_id = " + estacionConfheadId
                    + "AND ec.estacion_id = " + idstation; //ASG   
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Bomba(rst.getInt(1), rst.getString(2), null, null, null, null));
            }
            closePst();
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

    public boolean doCreateTurn2(boolean crearDia, Dia dia, Turno turno, List<Precio> listPrice, BeanItemContainer<EmpleadoBombaTurno> listTurnoEmpPump, String creadoPor) {
        boolean result = false;
        ArrayList<EmpleadoBombaTurno> listAsigna = new ArrayList<EmpleadoBombaTurno>();
        ResultSet rst = null;
        Connection cnn = null;
        cnn = getConnection();
        try {
            cnn.setAutoCommit(false);
            if (crearDia) {
                miQuery = "INSERT INTO dia (estado_id, creado_por, creado_persona, estacion_id, fecha, creado_el) "
                        + "VALUES (?, ?, ?, ?, ?, SYSDATE)";
                pst = cnn.prepareStatement(miQuery);
                pst.setInt(1, dia.getEstadoId());
                pst.setString(2, dia.getCreadoPor());
                pst.setString(3, dia.getCreadoPersona());
                pst.setInt(4, dia.getEstacionId());
                pst.setDate(5, new java.sql.Date(dia.getFecha().getTime()));
                pst.executeUpdate();
                closePst();
            }

            miQuery = "SELECT turno_seq.NEXTVAL FROM DUAL";
            pst = cnn.prepareStatement(miQuery);
            rst = pst.executeQuery();
            Integer turnoId = (rst.next()) ? rst.getInt(1) : 0;
            closePst();
            miQuery = "INSERT INTO turno (turno_id, estacion_id, usuario_id, estado_id, creado_por, creado_persona, turno_fusion, estacionconfhead_id, fecha, horario_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = cnn.prepareStatement(miQuery);
            pst.setInt(1, turnoId);
            pst.setInt(2, turno.getEstacionId());
            pst.setInt(3, turno.getUsuarioId());
            pst.setInt(4, turno.getEstadoId());
            pst.setString(5, turno.getCreadoPor());
            pst.setString(6, turno.getCreadoPersona());
            pst.setString(7, turno.getTurnoFusion());
            pst.setObject(8, turno.getEstacionconfheadId());    //setObject, puesto que puede ser NULL.
            pst.setDate(9, new java.sql.Date(turno.getFecha().getTime()));
            pst.setInt(10, turno.getHorarioId());
            pst.executeUpdate();
            closePst();

            miQuery = "INSERT INTO precio (turno_id, producto_id, tipodespacho_id, precio, creado_por, creado_persona, creado_el) "
                    + "VALUES (?, ?, ?, ?, ?, ?, SYSDATE)";

            for (Precio precio : listPrice) {
                pst = cnn.prepareStatement(miQuery);
                pst.setInt(1, turnoId);
                pst.setInt(2, precio.getProductoId());
                pst.setInt(3, precio.getTipodespachoId());
                pst.setDouble(4, precio.getPrecio());
                pst.setString(5, precio.getCreadoPor());
                pst.setString(6, precio.getCreadoPersona());
                pst.executeUpdate();
                closePst();
            }

            /*Desgloza las asignaciones por bomba*/
            for (EmpleadoBombaTurno rstB : listTurnoEmpPump.getItemIds()) {
                if (rstB.isBomba1()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 1));
                }
                if (rstB.isBomba2()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 2));
                }
                if (rstB.isBomba3()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 3));
                }
                if (rstB.isBomba4()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 4));
                }
                if (rstB.isBomba5()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 5));
                }
                if (rstB.isBomba6()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 6));
                }
                if (rstB.isBomba7()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 7));
                }
                if (rstB.isBomba8()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 8));
                }
                if (rstB.isBomba9()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 9));
                }
                if (rstB.isBomba10()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 10));
                }
                if (rstB.isBomba11()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 11));
                }
                if (rstB.isBomba12()) {
                    listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 12));
                }
            }

            miQuery = "INSERT INTO turno_empleado_bomba (turno_id, empleado_id, bomba_id, creado_por) "
                    + "VALUES (?, ?, ?, ?)";
            for (EmpleadoBombaTurno item : listAsigna) {
                pst = cnn.prepareStatement(miQuery);
                pst.setInt(1, turnoId);
                pst.setInt(2, item.getEmpleadoid());
                pst.setInt(3, item.getBombaid());
                pst.setString(4, creadoPor);
                pst.executeUpdate();
                closePst();
            }

            result = true;
            cnn.commit();
        } catch (Exception exc) {
            try {
                cnn.rollback();
            } catch (SQLException ex) {
//                Logger.getLogger(SvcTurno.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    rst.close();
                    closePst();
                    cnn.close();
                    closeConnections(); //asg
                } catch (SQLException ex) {
                }
            }
            exc.printStackTrace();
        }
        return result;
    }

    public void modificarTurno(Integer idturno, BeanItemContainer<EmpleadoBombaTurno> listTurnoEmpPump, String usuario) throws SQLException {
        ArrayList<EmpleadoBombaTurno> listAsigna = new ArrayList<EmpleadoBombaTurno>();

        PreparedStatement pst = null;
        ResultSet rst = null;
        
        /*Elimina antes de volver asignar*/
        try {
            miQuery = "DELETE FROM turno_empleado_bomba WHERE turno_id = " + idturno;
            pst = getConnection().prepareStatement(miQuery);
            pst.executeUpdate();
            rst.close();
            closePst();
            closeConnections(); //asg
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*Desgloza las asignaciones por bomba*/
        for (EmpleadoBombaTurno rstB : listTurnoEmpPump.getItemIds()) {
            if (rstB.isBomba1()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 1));
            }
            if (rstB.isBomba2()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 2));
            }
            if (rstB.isBomba3()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 3));
            }
            if (rstB.isBomba4()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 4));
            }
            if (rstB.isBomba5()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 5));
            }
            if (rstB.isBomba6()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 6));
            }
            if (rstB.isBomba7()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 7));
            }
            if (rstB.isBomba8()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 8));
            }
            if (rstB.isBomba9()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 9));
            }
            if (rstB.isBomba10()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 10));
            }
            if (rstB.isBomba11()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 11));
            }
            if (rstB.isBomba12()) {
                listAsigna.add(new EmpleadoBombaTurno(rstB.getTurnoid(), rstB.getEmpleadoid(), "", 12));
            }
        }

        miQuery = "INSERT INTO turno_empleado_bomba (turno_id, empleado_id, bomba_id, creado_por) "
                + "VALUES (?, ?, ?, ?)";
        for (EmpleadoBombaTurno item : listAsigna) {
            pst = getConnection().prepareStatement(miQuery);
            pst.setInt(1, idturno);
            pst.setInt(2, item.getEmpleadoid());
            pst.setInt(3, item.getBombaid());
            pst.setString(4, usuario);
            pst.executeUpdate();
            closePst();
            closeConnections(); //asg
        }
    }

    public boolean doCreateTurn(boolean crearDia, Dia dia, Turno turno, List<Precio> listPrice, List<TurnoEmpleadoBomba> listTurnoEmpPump) {
        boolean result = false;
        ResultSet rst = null;
        Connection cnn = null;
        cnn = getConnection();
              
        try {
            cnn.setAutoCommit(false);
            if (crearDia) {
                miQuery = "INSERT INTO dia (estado_id, creado_por, creado_persona, estacion_id, fecha, creado_el) "
                        + "VALUES (?, ?, ?, ?, ?, SYSDATE)";
                pst = cnn.prepareStatement(miQuery);
                pst.setInt(1, dia.getEstadoId());
                pst.setString(2, dia.getCreadoPor());
                pst.setString(3, dia.getCreadoPersona());
                pst.setInt(4, dia.getEstacionId());
                pst.setDate(5, new java.sql.Date(dia.getFecha().getTime()));
                pst.executeQuery();
                closePst();
            }

            miQuery = "SELECT turno_seq.NEXTVAL FROM DUAL";
            pst = cnn.prepareStatement(miQuery);
            rst = pst.executeQuery();
            Integer turnoId = (rst.next()) ? rst.getInt(1) : 0;
            closePst();
            miQuery = "INSERT INTO turno (turno_id, estacion_id, usuario_id, estado_id, creado_por, creado_persona, turno_fusion, estacionconfhead_id, fecha, horario_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = cnn.prepareStatement(miQuery);
            pst.setInt(1, turnoId);
            pst.setInt(2, turno.getEstacionId());
            pst.setInt(3, turno.getUsuarioId());
            pst.setInt(4, turno.getEstadoId());
            pst.setString(5, turno.getCreadoPor());
            pst.setString(6, turno.getCreadoPersona());
            pst.setString(7, turno.getTurnoFusion());
            pst.setObject(8, turno.getEstacionconfheadId());    //setObject, puesto que puede ser NULL.
            pst.setDate(9, new java.sql.Date(turno.getFecha().getTime()));
            pst.setInt(10, turno.getHorarioId());
            pst.executeUpdate();
            closePst();

            miQuery = "INSERT INTO precio (turno_id, producto_id, tipodespacho_id, precio, creado_por, creado_persona, creado_el) "
                    + "VALUES (?, ?, ?, ?, ?, ?, SYSDATE)";
            for (Precio precio : listPrice) {
                pst = cnn.prepareStatement(miQuery);
                pst.setInt(1, turnoId);
                pst.setInt(2, precio.getProductoId());
                pst.setInt(3, precio.getTipodespachoId());
                pst.setDouble(4, precio.getPrecio());
                pst.setString(5, precio.getCreadoPor());
                pst.setString(6, precio.getCreadoPersona());
                pst.executeUpdate();
                closePst();
            }

            miQuery = "INSERT INTO turno_empleado_bomba (turno_id, empleado_id, bomba_id, creado_por) "
                    + "VALUES (?, ?, ?, ?)";
            for (TurnoEmpleadoBomba item : listTurnoEmpPump) {
                pst = cnn.prepareStatement(miQuery);
                pst.setInt(1, turnoId);
                pst.setInt(2, item.getEmployee().getEmpleadoId());
                pst.setInt(3, item.getPump().getId());
                pst.setString(4, item.getCreadoPor());
                pst.executeUpdate();
                closePst();
            }

            result = true;
            cnn.commit();
        } catch (Exception exc) {
            try {
                cnn.rollback();
            } catch (SQLException ex) {
//                Logger.getLogger(SvcTurno.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                try {
                    rst.close();
                    closePst();
                    cnn.close();
                    closeConnections(); //asg
                } catch (SQLException ex) {
                    Logger.getLogger(SvcTurno.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            exc.printStackTrace();
        }
        return result;
    }

    public List<Horario> getHorarioByEstacionid(int estationId) {
        List<Horario> result = new ArrayList();
        query = "SELECT h.horario_id, h.nombre, h.hora_inicio, h.hora_fin, h.estado, h.descripcion, eh.estacionconfhead_id "
                + "FROM estacion_horario eh, horario h "
                + "WHERE eh.horario_id = h.horario_id AND h.estado = 'A' AND eh.estacion_id = " + estationId
                + " ORDER BY h.hora_inicio";
        ResultSet rst = null;
        try {
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            Horario schedule;
            while (rst.next()) {
                schedule = new Horario(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6));
                schedule.setEstacionconfheadId(rst.getInt(7));
                schedule.setNombreHoras(rst.getString(3) + " - " + rst.getString(4));
                result.add(schedule);
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

    public List<Horario> getHorarioByEstacionid2(int estationId, int idpais) {
        List<Horario> result = new ArrayList();
        query = "SELECT h.horario_id, h.nombre, h.hora_inicio, h.hora_fin, h.estado, h.descripcion, eh.estacionconfhead_id "
                + "FROM estacion_horario eh, horario h "
                + "WHERE eh.horario_id = h.horario_id AND h.estado = 'A' AND eh.estacion_id = " + estationId
                + "and eh.paisestacion_id = " + idpais
                + " ORDER BY h.hora_inicio";
        ResultSet rst = null;
        try {
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            Horario schedule;
            while (rst.next()) {
                schedule = new Horario(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6));
                schedule.setEstacionconfheadId(rst.getInt(7));
                schedule.setNombreHoras(rst.getString(3) + " - " + rst.getString(4));
                result.add(schedule);
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

    public Pais getPais(Integer paisid) throws SQLException {
        Pais pais = new Pais();
        query = "Select pais_id,nombre,codigo,moneda_simbolo,estado,creado_por,creado_el,modificado_por,modificado_el,vol_simbolo \n"
                + "from pais\n"
                + "where pais_id = " + paisid;

        ResultSet rst = null;

        try {
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            if (rst.next()) {
                pais.setPaisId(rst.getInt(1));
                pais.setNombre(rst.getString(2));
                pais.setCodigo(rst.getString(3));
                pais.setMonedaSimbolo(rst.getString(4));
                pais.setEstado(rst.getString(5));
                pais.setCreadoPor(rst.getString(6));
                pais.setCreadoEl(rst.getDate(7));
                pais.setModificadoPor(rst.getString(8));
                pais.setModificadoEl(rst.getDate(9));
                pais.setVolSimbolo(rst.getString(10));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (rst != null) {
                rst.close();
            }
            rst.close();
            closePst();
            closeConnections(); //asg
        }
        return pais;
    }

    public Integer validaTurnodia(Integer idestacion, Integer idconfhead, Date Fecha) {
        Integer cant = 0;
        String dateString = Constant.SDF_ddMMyyyy.format(Fecha);
        query = "select count(*) cantidad "
                + "from turno "
                + "where fecha = to_date('" + dateString + "','dd/mm/yyyy') "
                + "and estacion_id = " + idestacion
                + " and estacionconfhead_id = " + idconfhead;

        ResultSet rst = null;
        try {
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            if (rst.next()) {
                cant = rst.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rst != null) {
                    rst.close();
                }
                rst.close();
                closePst();
                closeConnections(); //asg
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cant;
    }

    public Integer getIdHorario(Integer idturno) {
        Integer id = 0;
        query = "select horario_id "
                + "from turno "
                + "where turno_id = " + idturno;

        ResultSet rst = null;
        try {
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            if (rst.next()) {
                id = rst.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rst != null) {
                    rst.close();
                }
                closePst();
                closeConnections(); //asg
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return id;
    }

    public Integer getnext() {
        Integer id = 0;
        query = "SELECT seq_empleado.nextval \n"
                + "FROM DUAL";

        ResultSet rst = null;
        try {
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            if (rst.next()) {
                id = rst.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rst != null) {
                    rst.close();
                }
                closePst();
                closeConnections(); //asg
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return id;
    }

    public Integer getExistHorario(Integer idestacion, Integer idconfhead, Integer idhorario, String fecha) {
        Integer id = -1;
        query = "select count(*) from turno where estacion_id = " + idestacion
                + " and estacionconfhead_id = " + idconfhead
                + " and HORARIO_ID = " + idhorario
                + " and fecha = to_date('" + fecha + "','dd/mm/yyyy')";
        ResultSet rst = null;
        try {
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            if (rst.next()) {
                id = rst.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rst != null) {
                    rst.close();
                }
                closePst();
                closeConnections(); //asg
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return id;
    }

    public int countTurnOpen(int idEstacion) {
        int result = 0;
        ResultSet rst = null;
        try {
            miQuery = "SELECT count(*) FROM dia "
                    + "WHERE estado_id =1 AND estacion_id = " + idEstacion;
            System.out.println("mi ueryryry " + miQuery);
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            if (rst.next()) {
                result = rst.getInt(1);
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
