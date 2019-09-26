package com.fundamental.model;

/**
 * @author Henry Barrientos
 */
public class ArqueocajaDetalle {

    private Integer arqueocajaId;
    private Integer mediopagoId;
    private Integer doctos;
    private Double monto;
    private String creadoPor;
    private String modificadoPor;
    private String descError;
//Adicionales
    private String nombreMedioPago;
    private int idTable;

    public ArqueocajaDetalle(Integer arqueocajaId, Integer mediopagoId, Integer doctos, Double monto, String creadoPor) {
        this.arqueocajaId = arqueocajaId;
        this.mediopagoId = mediopagoId;
        this.doctos = doctos;
        this.monto = monto;
        this.creadoPor = creadoPor;
    }

    public ArqueocajaDetalle() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getArqueocajaId() {
        return arqueocajaId;
    }

    public void setArqueocajaId(Integer arqueocajaId) {
        this.arqueocajaId = arqueocajaId;
    }

    public Integer getMediopagoId() {
        return mediopagoId;
    }

    public void setMediopagoId(Integer mediopagoId) {
        this.mediopagoId = mediopagoId;
    }

    public Integer getDoctos() {
        return doctos;
    }

    public void setDoctos(Integer doctos) {
        this.doctos = doctos;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public String getNombreMedioPago() {
        return nombreMedioPago;
    }

    public void setNombreMedioPago(String nombreMedioPago) {
        this.nombreMedioPago = nombreMedioPago;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

}
