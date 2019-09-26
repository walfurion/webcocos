///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package com.fundamental.utils;

import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Allan_Gil
 */
public class CreateComponents {
    
    HorizontalLayout horizontal = new HorizontalLayout();
    VerticalLayout vertical     = new VerticalLayout();
    CssLayout cssLayout         = new CssLayout();
    Notification notificacion;
    
    /**
     * Crea componente HorizontalLayout con lista de componentes enviados
     * @param style Stilo css
     * @param size Tipo size que se requiere
     * @param spacing Espacio entre componentes
     * @param margin Marguen de componente HorizontalLayout
     * @param responsive Hacer responsive
     * @param comp Lista de componentes a agregar en componente HorizontalLayout
     * @return 
     */
    public Component createHorizontal(String style,String size,boolean spacing,boolean margin,boolean responsive,Component[] comp){        
        horizontal = new HorizontalLayout();
        horizontal = (HorizontalLayout) validaSize(size, horizontal);           
        horizontal.addStyleName(style);
        horizontal.setSpacing(spacing);
        horizontal.setMargin(margin);
        if(responsive){
            Responsive.makeResponsive(horizontal);
        }
        readComponents(comp,horizontal);
        return horizontal;
    }
    /**
     * Crea componente VerticalLayout con lista de componentes enviados
     * @param style Stilo css
     * @param size Tipo size que se requiere
     * @param spacing Espacio entre componentes
     * @param margin Marguen de componente VerticalLayout
     * @param responsive Hacer responsive
     * @param comp Lista de componentes a agregar en componente VerticalLayout
     * @return 
     */
    public Component createVertical(String style,String size,boolean spacing,boolean margin,boolean responsive,Component[] comp){        
        vertical = new VerticalLayout();
        vertical = (VerticalLayout) validaSize(size, vertical);           
        vertical.addStyleName(style);
        vertical.setSpacing(spacing);
        vertical.setMargin(margin);
        if(responsive){
            Responsive.makeResponsive(vertical);
        }
        readComponents(comp,vertical);
        return vertical;
    }  
    
    /**
     * Crea componente CssLayout con lista de componentes enviados
     * @param style Stilo css
     * @param size Tipo size que se requiere
     * @param spacing Espacio entre componentes
     * @param margin Marguen de componente CssLayout
     * @param responsive Hacer responsive
     * @param comp Lista de componentes a agregar en componente CssLayout
     * @return 
     */
    public Component createCssLayout(String style,String size,boolean spacing,boolean margin,boolean responsive,Component[] comp){     
        cssLayout = new CssLayout();
        cssLayout = (CssLayout) validaSize(size, cssLayout);     
        cssLayout.addStyleName(style);
        if(responsive){
            Responsive.makeResponsive(cssLayout);
        }
        readComponents(comp,cssLayout);
        return cssLayout;
    }
    
    public Notification crearNotificacion(String mensaje){
        notificacion = new Notification(mensaje);
        notificacion.setDelayMsec(3000);
        notificacion.setStyleName("bar success small");
        notificacion.setPosition(Position.TOP_CENTER);
        notificacion.show(Page.getCurrent());
        return notificacion;
    }
    
    public Notification crearPublicidad(String mensaje){
        notificacion = new Notification(mensaje);
        notificacion.setDelayMsec(10000);
        notificacion.setStyleName("bar success small");
        notificacion.setPosition(Position.BOTTOM_CENTER);
        notificacion.show(Page.getCurrent());
        return notificacion;
    }
    
    /**
     * Valida tipo se size del componente enviado
     * @param size tipo de size
     * @param comp componente a cambiar size
     * @return 
     */
    public Component validaSize(String size,Component comp){
        if("FULL".equals(size)){
            comp.setSizeFull();
        }else if ("UNDEFINED".equals(size)){
            comp.setSizeUndefined();
        }else if(!"".equals(size)){
            comp.setWidth(size);
        }  
        return comp;
    }
    
    /**
     * Metodo encargado de leer los tipos de componentes
     * @param comps
     * @param comp 
     */
    public void readComponents(Component[] comps,Component comp){
        if(comps!=null){
            for(Component c:comps){
                if(comp instanceof VerticalLayout){
                    vertical.addComponent(c);
                }else if (comp instanceof HorizontalLayout){
                    horizontal.addComponent(c);
                }else if (comp instanceof CssLayout){
                    cssLayout.addComponent(c);
                }
            }
        }
    }
    
    
}
