package com.fundamental.model;

/**
 * @author Henry Barrientos
 */
public class Lecturafinal {

    private Integer estacionId;
    private Integer bombaId;
    private Integer productoId;
    private String tipo;
    private Double lecturaInicial;
    private Double lecturaFinal;
    private String modificadoPor;
    private String modificadoPersona;

    public Lecturafinal(Integer estacionId, Integer bombaId, Integer productoId, String tipo, Double lecturaInicial, Double lecturaFinal, String modificadoPor, String modificadoPersona) {
        this.estacionId = estacionId;
        this.bombaId = bombaId;
        this.productoId = productoId;
        this.tipo = tipo;
        this.lecturaInicial = lecturaInicial;
        this.lecturaFinal = lecturaFinal;
        this.modificadoPor = modificadoPor;
        this.modificadoPersona = modificadoPersona;
    }

    public Lecturafinal() {
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

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getLecturaFinal() {
        return lecturaFinal;
    }

    public void setLecturaFinal(Double lecturaFinal) {
        this.lecturaFinal = lecturaFinal;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public String getModificadoPersona() {
        return modificadoPersona;
    }

    public void setModificadoPersona(String modificadoPersona) {
        this.modificadoPersona = modificadoPersona;
    }

    public Double getLecturaInicial() {
        return lecturaInicial;
    }

    public void setLecturaInicial(Double lecturaInicial) {
        this.lecturaInicial = lecturaInicial;
    }

}
