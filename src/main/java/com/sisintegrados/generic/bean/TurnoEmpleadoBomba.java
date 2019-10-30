package com.sisintegrados.generic.bean;

import com.fundamental.model.Bomba;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Henry Barrientos
 */
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)

public class TurnoEmpleadoBomba {

    //required
    private Integer fakeId, turnoId, empleadoId, bombaId;
    private String creadoPor;
    //optional
    private String creadoEl, modificadoPor, modificadoEl;
    //
    private Empleado employee;
    private Bomba pump;
    private String nombreEmpleado;

    public TurnoEmpleadoBomba(int fakeId, int turnoId, int empleadoId, int bombaId, String creadoPor) {
        this.fakeId = fakeId;
        this.turnoId = turnoId;
        this.empleadoId = empleadoId;
        this.bombaId = bombaId;
        this.creadoPor = creadoPor;
    }

    public TurnoEmpleadoBomba(int bombaid, String nombre) {
        this.nombreEmpleado = nombre;
        this.bombaId = bombaid;
    }
}
