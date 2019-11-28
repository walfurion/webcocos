package com.fundamental.view;

import com.fundamental.model.Dia;
import com.fundamental.model.Turno;
import com.fundamental.model.Utils;
import com.fundamental.services.SvcMtd;
import com.fundamental.services.SvcTurno;
import com.fundamental.services.SvcUsuario;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import com.fundamental.services.SvcReporteControlMediosPago;
import com.sisintegrados.generic.bean.GenericEstacion;
import java.util.Locale;

/**
 *
 * @author Mery
 */
public class RptControDeMediosDePago extends Panel implements View {

CreateComponents components = new CreateComponents();
    Label lblUltimoDia = new Label();
    Label lblUltimoTurno = new Label();
    ComboBox cmbPais = new ComboBox();
    DateField cmbFechaInicio = new DateField("Fecha Inicio:");
    DateField cmbFechaFin = new DateField("Fecha Fin:");
    Usuario usuario = new Usuario();
    Dia ultimoDia;
    Turno ultimoTurno;
    Utils utils = new Utils();
    SvcTurno dao = new SvcTurno();
    SvcReporteControlMediosPago SvcReporteControlMediosPago = new SvcReporteControlMediosPago();
    BeanItemContainer<Pais> contPais = new BeanItemContainer<Pais>(Pais.class);
    Button btnGenerar = new Button("Generar Reporte");
    Button btnExportar = new Button("Exportar a Excel", FontAwesome.EDIT);
    //traer estaciones con su checkbox
    ArrayList<GenericEstacion> checkestacionesm = new ArrayList<GenericEstacion>();
    CheckBox chkEstacion1 = new CheckBox();

    public RptControDeMediosDePago() {
        super.setLocale(VaadinSession.getCurrent().getAttribute(Locale.class));
        super.addStyleName(ValoTheme.PANEL_BORDERLESS);
        super.setSizeFull();
        DashboardEventBus.register(this);
        usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
        super.setContent(components.createVertical(Constant.styleTransactions, "100%", false, true, true, new Component[]{buildForm()}));
        cargaInfoSesion();
    }

    private Component buildForm() {
        return components.createVertical(Constant.styleLogin, "100%", false, false, true, new Component[]{buildTitle(), buildHeader(), /*buildToolbar2(),*/ buildButtons()});
    }

    private Component buildTitle() {
        Label title = new Label("Control de medios de pago");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        Component toolBar = components.createHorizontal(Constant.styleToolbar, Constant.sizeUndefined, true, false, false, new Component[]{buildToolbar()});
        return components.createHorizontal(Constant.styleViewheader, Constant.sizeUndefined, true, false, false, new Component[]{title, toolBar});
    }

    private Component buildHeader() {
        contPais = new BeanItemContainer<Pais>(Pais.class);
        contPais.addAll(dao.getAllPaises());
        //cargaUltTurnoDia();
        cmbPais = new ComboBox("País:", contPais);
        cmbPais.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        cmbPais.setItemCaptionPropertyId("nombre");
        cmbPais.setItemIconPropertyId("flag");
        cmbPais.setWidth("165px");
        cmbPais.setRequired(true);
        cmbPais.setNullSelectionAllowed(false);
        cmbPais.setRequiredError("Debe seleccionar un país");
        cmbPais.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cmbPais.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                if (cmbPais.getValue() != null) {
                    Pais pais = new Pais();
                    pais = (Pais) cmbPais.getValue();
                    checkestacionesm = new ArrayList<GenericEstacion>();
                    checkestacionesm = SvcReporteControlMediosPago.getCheckEstacionesM(pais.getPaisId());
                    toolbarContainerTables.removeAllComponents();
                    VerticalLayout vl = new VerticalLayout();
                    vl.setSpacing(true);
                    vl.setResponsive(true);
                    Label lblestacion = new Label("Estaciones");
                    vl.addComponent(lblestacion);
                    lblestacion.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
                    for (GenericEstacion checkestacion : checkestacionesm) {
                        vl.addComponent(checkestacion.getCheck());
                    }
                    toolbarContainerTables.addComponent(vl);
                }
            }
        });

        cmbFechaInicio.setWidth("135px");
        cmbFechaInicio.setDateFormat("dd/MM/yyyy");
        cmbFechaInicio.setRangeEnd(Date.from(Instant.now()));
        cmbFechaInicio.setLocale(new Locale("es", "ES"));
        cmbFechaInicio.setLenient(true);
        cmbFechaInicio.addStyleName(ValoTheme.DATEFIELD_SMALL);
        cmbFechaInicio.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
            }
        });

        cmbFechaFin.setWidth("135px");
        cmbFechaFin.setDateFormat("dd/MM/yyyy");
        cmbFechaFin.setRangeEnd(Date.from(Instant.now()));
        cmbFechaFin.setLocale(new Locale("es", "ES"));
        cmbFechaFin.setLenient(true);
        cmbFechaFin.addStyleName(ValoTheme.DATEFIELD_SMALL);
        cmbFechaFin.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
            }
        });
        Component toolBar = components.createCssLayout(Constant.styleToolbar, Constant.sizeFull, false, false, true, new Component[]{utils.vlContainer(cmbPais),/* utils.vlContainer(cmbEstacion),*/ utils.vlContainer(cmbFechaInicio), utils.vlContainer(cmbFechaFin), utils.vlContainer(buildToolbar2())});
        VerticalLayout v = new VerticalLayout(toolBar);
        return components.createHorizontal(Constant.styleViewheader, Constant.sizeFull, false, false, true, new Component[]{v});
    }
    private CssLayout toolbarContainerTables;

    private Component buildToolbar2() {
        toolbarContainerTables = new CssLayout();
//        return toolbarContainerTables;
        return components.createHorizontal(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{utils.vlContainer(toolbarContainerTables)});
    }
    private CssLayout toolBar2;

    private Component buildToolbar() {
        toolBar2 = new CssLayout();
        VerticalLayout v = new VerticalLayout(toolBar2);
        return components.createHorizontal(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{v});
    }

    private Component buildButtons() {
        btnGenerar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnGenerar.setIcon(FontAwesome.SAVE);
        btnGenerar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if (ultimoDia.getFecha() != null && ultimoDia.getEstadoId() == 2 && ultimoDia.getFecha().equals(cmbFechaInicio.getValue())) {
                    Notification.show("ERROR:", "Para la fecha elegida, existe un día cerrado.", Notification.Type.ERROR_MESSAGE);
                    return;
                }

                boolean everythingOk = false;  //SOLO EJEMPLO 
                if (everythingOk) {

                } else {
                    Notification.show("ERROR:", "Ocurrió un error al guardar el precio.\n", Notification.Type.ERROR_MESSAGE);
                    return;
                }
            }
        });

        btnExportar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnExportar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                for (Component component : toolbarContainerTables) {
                    System.out.println("Q ES CAPTION "+component.getCaption());
                    System.out.println("Q ES DESCRIPCION "+component.getDescription());
                    System.out.println("Q ES ID "+component.getId());
                    
                }
//                if (bcrPrecios.getItemIds().size() == counter) {
//                    Notification notif = new Notification("ÉXITO:", "El registro se ha actualizado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
//                    notif.setDelayMsec(3000);
//                    notif.setPosition(Position.MIDDLE_CENTER);
//                    notif.show(Page.getCurrent());
//                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PR_TURN.getViewName());
//                } else {
//                    Notification.show("ERROR:", "Ocurrió un error al actualizar el registro.\n", Notification.Type.ERROR_MESSAGE);
//                    return;
//                }
            }
        });

        HorizontalLayout footer = (HorizontalLayout) components.createHorizontal(ValoTheme.WINDOW_BOTTOM_TOOLBAR, "", true, false, false, new Component[]{btnGenerar, btnExportar});

        footer.setComponentAlignment(btnGenerar, Alignment.TOP_RIGHT);
        footer.setWidth(
                100.0f, Unit.PERCENTAGE);
        return footer;
    }

//    private void cargaUltTurnoDia() {
//        SvcUsuario svu = new SvcUsuario();
//        usuario = svu.getLastTurnLastDay(usuario);
//        if (usuario.getDia() == null) {
//            lblUltimoDia.setValue("SIN DATOS REGISTRADOS");
//            lblUltimoTurno.setValue("SIN DATOS REGISTRADOS");
//        } else {
//            lblUltimoDia.setValue("Último día: " + usuario.getDia().getDia() + " (" + usuario.getDia().getEstado() + ")");
//            lblUltimoTurno.setValue("Último turno: " + usuario.getTurno().getTurno() + " (" + usuario.getTurno().getEstado() + ")");
//        }
//
//        lblUltimoDia.addStyleName(ValoTheme.LABEL_BOLD);
//        lblUltimoDia.addStyleName(ValoTheme.LABEL_H3);
//        lblUltimoDia.addStyleName(ValoTheme.LABEL_COLORED);
//        lblUltimoDia.setWidth("35%");
//
//        lblUltimoTurno.addStyleName(ValoTheme.LABEL_BOLD);
//        lblUltimoTurno.addStyleName(ValoTheme.LABEL_H3);
//        lblUltimoTurno.addStyleName(ValoTheme.LABEL_COLORED);
//        lblUltimoTurno.setSizeUndefined();
//
//        toolBar2.removeAllComponents();
//        toolBar2.addComponent(utils.vlContainer2(lblUltimoDia));
//        toolBar2.addComponent(utils.vlContainer2(lblUltimoTurno));
//    }

    private void cargaInfoSesion() {
        if (usuario.getPaisId() != null) {
            int i = 0;
            for (i = 0; i < contPais.size(); i++) {
                Pais hh = new Pais();
                hh = contPais.getIdByIndex(i);
                if (usuario.getPaisId().toString().trim().equals(hh.getPaisId().toString().trim())) {
                    cmbPais.setValue(contPais.getIdByIndex(i));
                }
            }
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}