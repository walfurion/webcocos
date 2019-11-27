/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.fundamental.model.Precio;
import com.sisintegrados.generic.bean.GenericEstacion;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.themes.ValoTheme;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Allan G.
 */
public class SvcMtd extends Dao {
    
    private String query;
    
    public ArrayList<GenericEstacion> getCheckEstaciones(Integer idpais) {
        ArrayList<GenericEstacion> result = new ArrayList<GenericEstacion>();
        GenericEstacion genestacion = new GenericEstacion();
        CheckBox check = new CheckBox();
        try {
            query = "Select estacion_id,nombre from estacion where pais_id ="+idpais;
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                genestacion = new GenericEstacion();
                genestacion.setEstacionid(rst.getInt(1));
                genestacion.setNombre(rst.getString(2));
                check = new CheckBox(genestacion.getNombre());
                check.setId(genestacion.getNombre());
                check.setStyleName(ValoTheme.CHECKBOX_SMALL);
                genestacion.setCheck(check);
                result.add(genestacion);
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
