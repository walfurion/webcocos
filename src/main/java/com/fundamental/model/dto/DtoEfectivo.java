package com.fundamental.model.dto;

import com.fundamental.model.Mediopago;

/**
 * @author Henry Barrientos
 */
public class DtoEfectivo {

    private Integer id;
    private Mediopago medioPago;
    private Double noDocto;
    private Double value;
    private Integer intValue;
    private Double tasa;
    private Double monExtranjera;

    public DtoEfectivo(Integer id, Double noDocto, Double value) {
        this.id = id;
        this.noDocto = noDocto;
        this.value = value;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getNoDocto() {
        return noDocto;
    }

    public void setNoDocto(Double noDocto) {
        this.noDocto = noDocto;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Mediopago getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(Mediopago medioPago) {
        this.medioPago = medioPago;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public Double getTasa() {
        return tasa;
    }

    public void setTasa(Double tasa) {
        this.tasa = tasa;
    }

    public Double getMonExtranjera() {
        return monExtranjera;
    }

    public void setMonExtranjera(Double monExtranjera) {
        this.monExtranjera = monExtranjera;
    }

}
