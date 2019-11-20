package com.sisintegrados.generic.bean;

import com.fundamental.model.Arqueocaja;
import com.fundamental.model.Bomba;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Allan G.
 */

@Data
@NoArgsConstructor
                                                                                                                                       
public class Tarjeta {

    private Integer mediopago_id;
    private String nombre;
    private Integer is_tcredito;
    private Integer pais_id;
    private String estado;
    private String creado_por;
    private Date creado_el;

    public Tarjeta(Integer mediopago_id, String nombre) {
        this.mediopago_id = mediopago_id;
        this.nombre = nombre;
    }
    
}
