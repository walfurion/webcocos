package com.fundamental.model.dto;

/**
 * @author Henry Barrientos
 */
public class DtoLectura {

    private Integer bombaIdDto;
    private Integer bombaId;
    private String bomba;
    private Integer productoId;
    private String producto;
    private Integer tipodespachoId;
    private String em, creadoPersona, nombrePistero, nombreJefe;
    //electronicas
    private Double eInicial, eFinal, eVolumen, eCalibracion;
    //manuales
    private Double mInicial, mFinal, mTotal, mDiferencia, mCalibracion;
    private Boolean esNueva;
    private Integer lecturaId;
    
    
    public static final String[] columnsTable = new String[]{"bomba", "producto", "lecIni", "lecFin", "lecTotal", "diferencia"};
    public static final String[] columnsTableElec = new String[]{"bomba", "producto", "lecVolIni", "lecFin", "lecVolTotal"};

    public DtoLectura(Integer bombaIdDto, Integer bombaId, String bomba, Integer productoId, String producto) {
        this.bombaIdDto = bombaIdDto;
        this.bombaId = bombaId;
        this.bomba = bomba;
        this.productoId = productoId;
        this.producto = producto;
    }

    public Integer getBombaId() {
        return bombaId;
    }

    public void setBombaId(Integer bombaId) {
        this.bombaId = bombaId;
    }

    public String getBomba() {
        return bomba;
    }

    public void setBomba(String bomba) {
        this.bomba = bomba;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getBombaIdDto() {
        return bombaIdDto;
    }

    public void setBombaIdDto(Integer bombaIdDto) {
        this.bombaIdDto = bombaIdDto;
    }

    public Integer getTipodespachoId() {
        return tipodespachoId;
    }

    public void setTipodespachoId(Integer tipodespachoId) {
        this.tipodespachoId = tipodespachoId;
    }

    public Double geteInicial() {
        return eInicial;
    }

    public void seteInicial(Double eInicial) {
        this.eInicial = eInicial;
    }

    public Double geteFinal() {
        return eFinal;
    }

    public void seteFinal(Double eFinal) {
        this.eFinal = eFinal;
    }

    public Double geteVolumen() {
        return eVolumen;
    }

    public void seteVolumen(Double eVolumen) {
        this.eVolumen = eVolumen;
    }

    public Double getmInicial() {
        return mInicial;
    }

    public void setmInicial(Double mInicial) {
        this.mInicial = mInicial;
    }

    public Double getmFinal() {
        return mFinal;
    }

    public void setmFinal(Double mFinal) {
        this.mFinal = mFinal;
    }

    public Double getmTotal() {
        return mTotal;
    }

    public void setmTotal(Double mTotal) {
        this.mTotal = mTotal;
    }

    public Double getmDiferencia() {
        return mDiferencia;
    }

    public void setmDiferencia(Double mDiferencia) {
        this.mDiferencia = mDiferencia;
    }

    public String getEm() {
        return em;
    }

    public void setEm(String em) {
        this.em = em;
    }

    public String getCreadoPersona() {
        return creadoPersona;
    }

    public void setCreadoPersona(String creadoPersona) {
        this.creadoPersona = creadoPersona;
    }

    public Double getmCalibracion() {
        return mCalibracion;
    }

    public void setmCalibracion(Double mCalibracion) {
        this.mCalibracion = mCalibracion;
    }

    public Boolean getEsNueva() {
        return esNueva;
    }

    public void setEsNueva(Boolean esNueva) {
        this.esNueva = esNueva;
    }

    public Integer getLecturaId() {
        return lecturaId;
    }

    public void setLecturaId(Integer lecturaId) {
        this.lecturaId = lecturaId;
    }

    public String getNombrePistero() {
        return nombrePistero;
    }

    public void setNombrePistero(String nombrePistero) {
        this.nombrePistero = nombrePistero;
    }

    public String getNombreJefe() {
        return nombreJefe;
    }

    public void setNombreJefe(String nombreJefe) {
        this.nombreJefe = nombreJefe;
    }

    public Double geteCalibracion() {
        return eCalibracion;
    }

    public void seteCalibracion(Double eCalibracion) {
        this.eCalibracion = eCalibracion;
    }

}
