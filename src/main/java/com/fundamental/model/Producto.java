package com.fundamental.model;

import com.fundamental.model.dto.DtoGenericBean;
import java.util.Date;
import org.vaadin.ui.NumberField;

/**
 * @author Henry Barrientos
 */
public class Producto {

    private Integer productoId;
    private String nombre, codigo;
    private Integer tipoId, ordenPos, idMarca, codigoNumerico;
    private String estado,sku;
    private String creadoPor;
    private Date creadoEl, modificadoEl;
    private String modificadoPor, codigoEnvoy, codigoBarras, presentacion, descError;
    //Adicionales
    private NumberField precioAS;    //Autoservicio
    private NumberField precioSC;   //Servicio completo
    private Double value, precio;
    private boolean selected;
    private Double priceAS;
    private Double priceSC;
    private Marca marca;
    private Tipoproducto typeProd;
    private DtoGenericBean status;

    public Producto(Integer productoId, String nombre, String codigo, String estado, String creadoPor, Integer ordenPos) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.codigo = codigo;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.ordenPos = ordenPos;
    }

    public Producto(Integer productoId, String nombre, String codigo, Integer idMarca, String estado, Double precio, String presentacion ) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.codigo = codigo;
        this.idMarca = idMarca;
        this.estado = estado;
        this.precio = precio;
        this.presentacion = presentacion;
    }

    

    public Producto(Integer productoId, String nombre, String codigo, Integer tipoId, Integer ordenPos, String estado, Integer codigoNumerico, String presentacion, String codigoBarras, Integer idMarca, String codigoEnvoy, String sku) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.codigo = codigo;
        this.tipoId = tipoId;
        this.ordenPos = ordenPos;
        this.idMarca = idMarca;
        this.codigoNumerico = codigoNumerico;
        this.estado = estado;
        this.codigoEnvoy = codigoEnvoy;
        this.codigoBarras = codigoBarras;
        this.presentacion = presentacion;
        this.sku = sku;
    }

    public Producto() {
    }
    
    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
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

    public Date getCreadoEl() {
        return creadoEl;
    }

    public void setCreadoEl(Date creadoEl) {
        this.creadoEl = creadoEl;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public Date getModificadoEl() {
        return modificadoEl;
    }

    public void setModificadoEl(Date modificadoEl) {
        this.modificadoEl = modificadoEl;
    }

    public NumberField getPrecioAS() {
        return precioAS;
    }

    public void setPrecioAS(NumberField precioAS) {
        this.precioAS = precioAS;
    }

    public NumberField getPrecioSC() {
        return precioSC;
    }

    public void setPrecioSC(NumberField precioSC) {
        this.precioSC = precioSC;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getOrdenPos() {
        return ordenPos;
    }

    public void setOrdenPos(Integer ordenPos) {
        this.ordenPos = ordenPos;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Double getPriceAS() {
        return priceAS;
    }

    public void setPriceAS(Double priceAS) {
        this.priceAS = priceAS;
    }

    public Double getPriceSC() {
        return priceSC;
    }

    public void setPriceSC(Double priceSC) {
        this.priceSC = priceSC;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Integer getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public Integer getCodigoNumerico() {
        return codigoNumerico;
    }

    public void setCodigoNumerico(Integer codigoNumerico) {
        this.codigoNumerico = codigoNumerico;
    }

    public String getCodigoEnvoy() {
        return codigoEnvoy;
    }

    public void setCodigoEnvoy(String codigoEnvoy) {
        this.codigoEnvoy = codigoEnvoy;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public Tipoproducto getTypeProd() {
        return typeProd;
    }

    public void setTypeProd(Tipoproducto typeProd) {
        this.typeProd = typeProd;
    }

    public DtoGenericBean getStatus() {
        return status;
    }

    public void setStatus(DtoGenericBean status) {
        this.status = status;
    }

    public Integer getTipoId() {
        return tipoId;
    }

    public void setTipoId(Integer tipoId) {
        this.tipoId = tipoId;
    }

    public String getDescError() {
        return descError;
    }

    public void setDescError(String descError) {
        this.descError = descError;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    
}