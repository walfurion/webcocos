/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.sisintegrados.generic.bean.GenericEstacion;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.themes.ValoTheme;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Mery
 */
public class SvcReporteControlMediosPago extends Dao {

    private String query;

    public ArrayList<GenericEstacion> getCheckEstacionesM(Integer idpais) {
        ArrayList<GenericEstacion> result = new ArrayList<GenericEstacion>();
        GenericEstacion gnestacion = new GenericEstacion();
        CheckBox check = new CheckBox();
        try {
            query = "Select estacion_id,nombre from estacion where pais_id ="+idpais;
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                gnestacion = new GenericEstacion();
                gnestacion.setEstacionid(rst.getInt(1));
                gnestacion.setNombre(rst.getString(2));
                check = new CheckBox(gnestacion.getNombre());
                check.setId(gnestacion.getNombre());
                check.setStyleName(ValoTheme.CHECKBOX_SMALL);
                gnestacion.setCheck(check);
                result.add(gnestacion);
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
  }
