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
@AllArgsConstructor
                                                                                                                                       
public class GenericTarjeta {
private Integer idGenerico;
private  Tarjeta tarjeta;
private Integer lote;
private Double monto;
    
}
