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
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Pais {

    private Integer paisId;
    private String nombre;
    private String codigo;
    private String monedaSimbolo;
    private String volSimbolo;
    private String estado;
    private String creadoPor;
    private Date creadoEl;
    private String modificadoPor;
    private Date modificadoEl;
    private boolean selected;
    private ThemeResource flag;

    public Pais(Integer paisId, String nombre, String codigo, String monedaSimbolo, String volSimbolo, String estado, String creadoPor) {
        this.paisId = paisId;
        this.nombre = nombre;
        this.codigo = codigo;
        this.monedaSimbolo = monedaSimbolo;
        this.volSimbolo = volSimbolo;
        this.estado = estado;
        this.creadoPor = creadoPor;
        //Images from https://demo.vaadin.com/sampler-for-vaadin6/VAADIN/themes/sampler/flags/gt.gif
        this.flag = new ThemeResource("img/" + codigo.toLowerCase() + ".gif");
    }

    public Pais(Integer paisId, String nombre, String codigo, String monedaSimbolo, String volSimbolo, String estado, String creadoPor, boolean selected) {
        this.paisId = paisId;
        this.nombre = nombre;
        this.codigo = codigo;
        this.monedaSimbolo = monedaSimbolo;
        this.volSimbolo = volSimbolo;
        this.estado = estado;
        this.creadoPor = creadoPor;
        //Images from https://demo.vaadin.com/sampler-for-vaadin6/VAADIN/themes/sampler/flags/gt.gif
        this.flag = new ThemeResource("img/" + codigo.toLowerCase() + ".gif");
        this.selected = selected;
    }

}
