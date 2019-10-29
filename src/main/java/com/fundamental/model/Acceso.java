package com.fundamental.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Henry Barrientos
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Acceso {

    private Integer accesoId;
    private String titulo;
    private Integer padre;
    private Integer orden;
    private String recursoInterno, descripcion, estado, creadoPor, modificadoPor, nombrePadre;
    //Adicionales
    private List<Acceso> accesos;
    private boolean selected;
    private boolean ver;
    private boolean cambiar;
    private boolean agregar;
    private boolean eliminar;
}
