/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.sisintegrados.dao.Dao;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.generic.bean.Empleado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 50230
 */
public class SvcEmpleado extends DaoImp {

    private String query;

    public SvcEmpleado() {
    }

    public List<Empleado> getEmpleados() {
        List<Empleado> result = new ArrayList();
        ResultSet rst = null;
        try {
            query = "SELECT empleado_id, nombre, estado "
                    + "FROM EMPLEADO";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Empleado(rst.getInt(1), rst.getString(2), rst.getString(3)));
            }
        } catch (Exception exc) {
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

    public Empleado doAction(String action, Empleado empleado) {
        ResultSet rst = null;
        try {
            if (action.equals(ACTION_ADD)) {
                query = "SELECT empleado_seq.NEXTVAL FROM DUAL";
                pst = getConnection().prepareStatement(query);
                rst = pst.executeQuery();
                Integer estacionId = (rst.next()) ? rst.getInt(1) : null;
                empleado.setEmpleadoId(estacionId);
                rst.close();
                closePst();
                closeConnections(); //asg

                query = "INSERT INTO empleado (empleado_id, nombre, estado, creado_por, creado_el) "
                        + "VALUES (?, ?, ?, ?,SYSDATE)";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, empleado.getEmpleadoId());
                pst.setString(2, empleado.getNombre());
                pst.setString(3, empleado.getEstado());
                pst.setString(4, empleado.getCreadoPor());
                pst.executeUpdate();
                pst.close();
                closeConnections(); //asg
            } else if (action.equals(ACTION_UPDATE)) {
                query = "UPDATE empleado "
                        + "SET nombre = ?, estado = ?, modificado_por = ?, modificado_el = SYSDATE "
                        + "WHERE empleado_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setString(1, empleado.getNombre());
                pst.setString(2, empleado.getEstado());
                pst.setString(3, empleado.getCreadoPor());
                pst.setInt(4, empleado.getEmpleadoId());
                pst.executeUpdate();
                pst.close();
                closeConnections(); //asg
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                closePst();
                closeConnections(); //asg
            } catch (SQLException ex) {
                Logger.getLogger(SvcEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return empleado;
    }

    public void insert(String nombre, String estado, String usuario) throws SQLException {
        PreparedStatement pst = null;
        query = "INSERT INTO empleado (empleado_id, nombre, estado, creado_por, creado_el) "
                + "VALUES (seq_empleado.nextval, ?, ?, ?,SYSDATE)";
        try {
            pst = getConnection().prepareStatement(query);
            pst.setString(1, nombre);
            pst.setString(2, estado);
            pst.setString(3, usuario);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SvcEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                pst.close();
                closeConnections(); //asg
            }
        }
    }

    public void update(Integer idempleado, String nombre, String estado, String usuario) throws SQLException {
        PreparedStatement pst = null;
        query = "UPDATE empleado set nombre = '" + nombre + "', estado = '" + estado + "', creado_por = '" + usuario + "', creado_el = sysdate where empleado_id = " + idempleado;
        try {
            pst = getConnection().prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SvcEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pst != null) {
                pst.close();
                closeConnections(); //asg
            }
        }
    }

}
