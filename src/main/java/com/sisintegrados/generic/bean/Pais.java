package com.sisintegrados.generic.bean;

import com.vaadin.server.ThemeResource;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author Allan Gil
 */
@Data
@NoArgsConstructor(access=AccessLevel.PUBLIC, force=true)
@RequiredArgsConstructor
public class Pais {
    private final Integer paisId;
    private final String nombre;
    private final String codigo;
    private final String monedaSimbolo;
    private final String volSimbolo;
    private final String estado;
    private String creadoPor;
    private Date creadoEl;
    private String modificadoPor;
    private Date modificadoEl;
    private final boolean selected;
    private ThemeResource flag;
}
