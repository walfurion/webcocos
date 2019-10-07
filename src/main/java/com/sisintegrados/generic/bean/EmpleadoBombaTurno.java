/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Allan G.
 */
@Data
@NoArgsConstructor
public class EmpleadoBombaTurno {

    private int turnoid;
    private int empleadoid;
    private String nombre;
    private boolean bomba1 = false;
    private boolean bomba2 = false;
    private boolean bomba3 = false;
    private boolean bomba4 = false;
    private boolean bomba5 = false;
    private boolean bomba6 = false;
    private boolean bomba7 = false;
    private boolean bomba8 = false;
    private boolean bomba9 = false;
    private boolean bomba10 = false;
    private boolean bomba11 = false;
    private boolean bomba12 = false;

    public EmpleadoBombaTurno(int turnoid,int empleadoid, String nombre) {
        this.turnoid = turnoid;
        this.empleadoid = empleadoid;
        this.nombre = nombre;
    }

    public String toString() {
        return "EmpleadoTurno: { turnoid = "+turnoid+", empleadoid ="+empleadoid+", nombre = "+nombre+", bomba1 = "+bomba1+", bomba2 = "+bomba2+", bomba3 = "+bomba3+", bomba4 = "+bomba4+", bomba5 = "+bomba5+", bomba6 = "+bomba6+", bomba7 = "+bomba7+", bomba8 = "+bomba8+", bomba9 = "+bomba9+", bomba10 = "+bomba10+", bomba11 = "+bomba11+", bomba12 = "+bomba12+" }";
    }
}
