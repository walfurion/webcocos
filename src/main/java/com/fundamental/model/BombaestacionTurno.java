package com.fundamental.model;

/**
 * @author Henry Barrientos
 */
public class BombaestacionTurno {

    private Integer estacionId;
    private Integer bombaId;
    private Integer turno_id;

    public BombaestacionTurno(Integer estacionId, Integer bombaId, Integer turno_id) {
        this.estacionId = estacionId;
        this.bombaId = bombaId;
        this.turno_id = turno_id;
    }

    public Integer getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(Integer estacionId) {
        this.estacionId = estacionId;
    }

    public Integer getBombaId() {
        return bombaId;
    }

    public void setBombaId(Integer bombaId) {
        this.bombaId = bombaId;
    }

    public Integer getTurno_id() {
        return turno_id;
    }

    public void setTurno_id(Integer turno_id) {
        this.turno_id = turno_id;
    }

}
