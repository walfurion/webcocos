/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.fundamental.model.Empleado;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 50230
 */
public class SvcEmpleado extends Dao {
    
    private String query;
    public SvcEmpleado() {
    }

    public List<Empleado> getEmpleados() {
        List<Empleado> result = new ArrayList();
        try {
            query = "SELECT empleado_id, nombre, estado "
                    + "FROM EMPLEADO";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Empleado(rst.getInt(1),rst.getString(2),rst.getString(3)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }
    
    public Empleado doAction(String action, Empleado empleado) {
        try {
            if (action.equals(Dao.ACTION_ADD)) {
                query = "SELECT empleado_seq.NEXTVAL FROM DUAL";
                pst = getConnection().prepareStatement(query);
                ResultSet rst = pst.executeQuery();
                Integer estacionId = (rst.next()) ? rst.getInt(1) : null;
                empleado.setEmpleadoId(estacionId);
                closePst();
                query = "INSERT INTO empleado (empleado_id, nombre, estado, creado_por, creado_el) "
                        + "VALUES (?, ?, ?, ?,SYSDATE)";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, empleado.getEmpleadoId());
                pst.setString(2, empleado.getNombre());
                pst.setString(3, empleado.getEstado());
                pst.setString(4, empleado.getCreadoPor());
                pst.executeUpdate();
            } else if (action.equals(Dao.ACTION_UPDATE)) {
                query = "UPDATE empleado "
                        + "SET nombre = ?, estado = ?, modificado_por = ?, modificado_el = SYSDATE "
                        + "WHERE empleado_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setString(1, empleado.getNombre());
                pst.setString(2, empleado.getEstado());
                pst.setString(3, empleado.getCreadoPor());
                pst.setInt(4, empleado.getEmpleadoId());
                pst.executeUpdate();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return empleado;
    }
    
}
