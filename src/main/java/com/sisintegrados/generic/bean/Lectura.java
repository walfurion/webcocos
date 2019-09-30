package com.sisintegrados.generic.bean;

import com.fundamental.model.LecturaDetalle;
import com.fundamental.model.Lecturafinal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Henry Barrientos
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Lectura {

    private Integer lecturaId;
    private Integer estacionId;
    private Integer turnoId;
    private String creadoPor;
    private Date creadoEl;
    private String creadoPersona;
    private String modificadoPor;
    private String descError;
    private String nombrePistero;
    private String nombreJefe;
    private Date modificadoEl;
    private List<LecturaDetalle> lecturaDetalle = new ArrayList();
    private List<Lecturafinal> lecturafinal = new ArrayList();
    private Integer empleadoId;

    public Lectura(Integer lecturaId, Integer estacionId, Integer turnoId,
            String creadoPor, String creadoPersona, String nombrePistero, String nombreJefe) {
        this.lecturaId = lecturaId;
        this.estacionId = estacionId;
        this.turnoId = turnoId;
        this.creadoPor = creadoPor;
        this.creadoPersona = creadoPersona;
        this.nombrePistero = nombrePistero;
        this.nombreJefe = nombreJefe;
    }

    public Lectura(Integer lecturaId, Integer estacionId, Integer turnoId,
            String creadoPor, String creadoPersona) {
        this.lecturaId = lecturaId;
        this.estacionId = estacionId;
        this.turnoId = turnoId;
        this.creadoPor = creadoPor;
        this.creadoPersona = creadoPersona;
        this.nombrePistero = nombrePistero;
        this.nombreJefe = nombreJefe;
    }
}
