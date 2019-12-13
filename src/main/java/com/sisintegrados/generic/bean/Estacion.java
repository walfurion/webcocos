package com.sisintegrados.generic.bean;

import com.fundamental.model.Bomba;
import com.fundamental.model.EstacionConfHead;
import com.fundamental.model.Marca;
import com.fundamental.model.Producto;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.dto.DtoGenericBean;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Allan G.
 */

@Data
@NoArgsConstructor
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

    public Estacion(Integer estacionId, String nombre) {
        this.estacionId = estacionId;
        this.nombre = nombre;
    }
    
    
}
