package com.sisintegrados.generic.bean;

import com.fundamental.model.Arqueocaja;
import com.fundamental.model.Bomba;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Allan G.
 */

@Data
@NoArgsConstructor

public class Empleado {

    private int empleadoId;
    private String nombre, estado, creadoPor, creadoEl;
    private String modificadoPor, modificadoEl;
    private List<Bomba> bombas;
    private Arqueocaja arqueo;

    public Empleado(int empleadoId, String nombre) {
        this.empleadoId = empleadoId;
        this.nombre = nombre;
    }
    
    public Empleado(int empleadoId, String nombre, String estado) {
        this.empleadoId = empleadoId;
        this.nombre = nombre;
        this.estado = estado;
    }
}
