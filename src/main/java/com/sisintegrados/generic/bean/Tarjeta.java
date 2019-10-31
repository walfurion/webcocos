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

    private Integer tarjeta_Id;
    private String nombre;
    private String creado_por;
    private Date creado_el;

    public Tarjeta(Integer tarjeta_Id, String nombre) {
        this.tarjeta_Id = tarjeta_Id;
        this.nombre = nombre;
    }
    
}
