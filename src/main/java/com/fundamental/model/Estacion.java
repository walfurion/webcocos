package com.fundamental.model;

import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.dto.DtoGenericBean;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class Estacion {

    private Integer estacionId;
    private String nombre;
    private String codigo, bu, deposito;
    private Integer paisId, idMarca;
//    private String datosConexion, claveConexion;
    private String factElectronica, estado, codigoEnvoy, creadoPor, modificadoPor;
    
    //Adicionales
    private String paisNombre;
    private Pais pais;
    private List<Bomba> bombas;
    private List<Producto> productos;
    private String descError;
    private DtoGenericBean status;
    private boolean selected;
    private EstacionConfHead estacionConfHead;  //para mant horario
    private Marca brand;

    public Estacion(Integer estacionId, String nombre, String codigo, Integer paisId, String estado, String factElectronica) {
        this.estacionId = estacionId;
        this.nombre = nombre;
        this.codigo = codigo;
        this.paisId = paisId;
        this.estado = estado;
        this.factElectronica = factElectronica;
    }

    public Estacion() {
    }

    public Integer getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(Integer estacionId) {
        this.estacionId = estacionId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getPaisId() {
        return paisId;
    }

    public void setPaisId(Integer paisId) {
        this.paisId = paisId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public String getPaisNombre() {
        return paisNombre;
    }

    public void setPaisNombre(String paisNombre) {
        this.paisNombre = paisNombre;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

//    public String getDatosConexion() {
//        return datosConexion;
//    }
//
//    public void setDatosConexion(String datosConexion) {
//        this.datosConexion = datosConexion;
//    }

    public List<Bomba> getBombas() {
        return bombas;
    }

    public void setBombas(List<Bomba> bombas) {
        this.bombas = bombas;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public String getFactElectronica() {
        return factElectronica;
    }

    public void setFactElectronica(String factElectronica) {
        this.factElectronica = factElectronica;
    }

//    public String getClaveConexion() {
//        return claveConexion;
//    }
//
//    public void setClaveConexion(String claveConexion) {
//        this.claveConexion = claveConexion;
//    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

    public String getCodigoEnvoy() {
        return codigoEnvoy;
    }

    public void setCodigoEnvoy(String codigoEnvoy) {
        this.codigoEnvoy = codigoEnvoy;
    }

    public DtoGenericBean getStatus() {
        return status;
    }

    public void setStatus(DtoGenericBean status) {
        this.status = status;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public EstacionConfHead getEstacionConfHead() {
        return estacionConfHead;
    }

    public void setEstacionConfHead(EstacionConfHead estacionConfHead) {
        this.estacionConfHead = estacionConfHead;
    }

    public Integer getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public Marca getBrand() {
        return brand;
    }

    public void setBrand(Marca brand) {
        this.brand = brand;
    }

}
