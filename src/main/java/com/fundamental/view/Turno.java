///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.fundamental.view;
//
//import com.fundamental.utils.Constant;
//import com.fundamental.utils.CreateComponents;
//import com.vaadin.demo.dashboard.event.DashboardEventBus;
//import com.vaadin.navigator.View;
//import com.vaadin.navigator.ViewChangeListener;
//import com.vaadin.server.VaadinSession;
//import com.vaadin.ui.ComboBox;
//import com.vaadin.ui.Component;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.Panel;
//import com.vaadin.ui.themes.ValoTheme;
//import java.text.SimpleDateFormat;
//import java.util.Locale;
//
///**
// *
// * @author Allan G.
// */
//public class Turno extends Panel implements View {
//
//    CreateComponents components = new CreateComponents();
//    Label lblUltimoDia = new Label();
//    Label lblUltimoTurno = new Label();
//    ComboBox cmbPais = new ComboBox();
//    ComboBox cmbEstacion = new ComboBox();
//    ComboBox cmbHorario = new ComboBox();
//    ComboBox cmbTurno = new ComboBox();
//
//    public Turno() {
//        super.setLocale(VaadinSession.getCurrent().getAttribute(Locale.class));
//        super.addStyleName(ValoTheme.PANEL_BORDERLESS);
//        super.setSizeFull();
//        DashboardEventBus.register(this);
//        super.setContent(components.createVertical(Constant.styleTransactions, "100%", false, true, true, new Component[]{buildForm()}));
//    }
//
//    private Component buildForm() {
//        return components.createVertical(Constant.styleLogin, "100%", false, false, true, new Component[]{buildTitle(), buildHeader(), buildToolBar2(), buildTable()});
//    }
//
//    private Component buildTitle() {
//        Label title = new Label("Turno");
//        title.setSizeUndefined();
//        title.addStyleName(ValoTheme.LABEL_H1);
//        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
//        Component toolBar = components.createHorizontal(Constant.styleToolbar, Constant.sizeUndefined, true, false, false, new Component[]{buildToolbar()});
//        return components.createHorizontal(Constant.styleViewheader, Constant.sizeUndefined, true, false, false, new Component[]{title, toolBar});
//    }
//
//    private HorizontalLayout toolbarContainerLabels;
//
//    private Component buildToolbar() {
//        toolbarContainerLabels = new HorizontalLayout();
//        return components.createHorizontal(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{toolbarContainerLabels});
//    }
//
//    private void buildLabelInfo() {
//        SimpleDateFormat sdf_ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
//        String fechaString = "";
//        String estadoName = "";
//        fechaString = (ultimoDia.getFecha() != null) ? sdf_ddmmyyyy.format(ultimoDia.getFecha()) : "INEXISTENTE";
//        estadoName = (ultimoDia.getEstadoId() != null && ultimoDia.getEstadoId() == 1) ? "ABIERTO" : ((ultimoDia.getEstadoId() != null && ultimoDia.getEstadoId() == 2) ? "CERRADO" : "SIN ESTADO");
//        lblUltimoDía.setValue("Último día: " + fechaString + " (" + estadoName + ")");
//        lblUltimoDía.addStyleName(ValoTheme.LABEL_BOLD);
//        lblUltimoDía.addStyleName(ValoTheme.LABEL_H3);
//        lblUltimoDía.addStyleName(ValoTheme.LABEL_COLORED);
//        lblUltimoDía.setWidth("35%");
//
//        fechaString = (ultimoTurno.getTurnoId() != null) ? "Turno ".concat(ultimoTurno.getTurnoId().toString()) : "INEXISTENTE";
//        estadoName = (ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 1) ? "ABIERTO" : ((ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 2) ? "CERRADO" : "SIN ESTADO");
//        lblUltimoTurno.setValue("Último turno: " + fechaString + " (" + estadoName + ")");
//        lblUltimoTurno.addStyleName(ValoTheme.LABEL_BOLD);
//        lblUltimoTurno.addStyleName(ValoTheme.LABEL_H3);
//        lblUltimoTurno.addStyleName(ValoTheme.LABEL_COLORED);
//        lblUltimoTurno.setSizeUndefined();
//
//        lblHoraConf.addStyleName(ValoTheme.LABEL_BOLD);
//        lblHoraConf.addStyleName(ValoTheme.LABEL_SUCCESS);
//
//        toolbarContainerLabels.removeAllComponents();
//        toolbarContainerLabels.addComponent(components.createHorizontal(Constant.styleToolbar, Constant.sizeUndefined, true, false, true, new Component[]{lblUltimoDía, lblUltimoTurno}));
//    }
//
//    @Override
//    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//}
