package com.fundamental.view;

import com.fundamental.model.Acceso;
import com.fundamental.model.Bomba;
import com.fundamental.services.Dao;
import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.Lectura;
import com.fundamental.model.LecturaDetalle;
import com.fundamental.model.Lecturafinal;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Turno;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.services.SvcChangeLastRead;
import com.fundamental.services.SvcReading;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
public class PrChangeLastRead extends Panel implements View {

    ComboBox cbxPais, cbxEstacion;
    Table tableBombas, tableLecturas;
    TextArea txtaComentario;
    Button btnSave;
    Acceso acceso = new Acceso();

    BeanContainer<Integer, Bomba> bcBombas;
    BeanContainer<Integer, LecturaDetalle> bcLecturas;
    List<Pais> paises;
    List<Lecturafinal> lecturasManualesActuales;
    Estacion estacion;

    //template
    private final VerticalLayout vlRoot;
    private CssLayout content = new CssLayout();
    Utils utils = new Utils();
    Usuario user;

    public PrChangeLastRead() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        content.addStyleName("dashboard-panels");
        Responsive.makeResponsive(content);
        vlRoot = utils.buildVerticalR("vlRoot", true, false, true, "dashboard-view");
        setContent(vlRoot);
        Responsive.makeResponsive(vlRoot);
        vlRoot.addComponent(utils.buildHeaderR("Cambio de última lectura"));
        vlRoot.addComponent(utils.buildSeparator());
        vlRoot.addComponent(content);
        vlRoot.setExpandRatio(content, 1);
        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        getAllData();
        //template

//        buildControls();
        buildActions();
        buildTableBombas();
        buildTableLecturas();
        buildFields();
        buildActions();

        CssLayout filterFields = new CssLayout(utils.vlContainer(cbxPais), utils.vlContainer(cbxEstacion));
        filterFields.setId("filterFields");
        Responsive.makeResponsive(filterFields);

        HorizontalLayout hlTop = new HorizontalLayout(filterFields);
        hlTop.setSizeUndefined();
        hlTop.setWidth(100f, Unit.PERCENTAGE);
        hlTop.setId("hlTop");
        Responsive.makeResponsive(hlTop);

        CssLayout cltTables = new CssLayout(utils.vlContainer(tableBombas), utils.vlContainer(tableLecturas), utils.vlContainer(txtaComentario), btnSave);
        HorizontalLayout hlBottom = new HorizontalLayout(cltTables);
        hlBottom.setSizeUndefined();
        hlBottom.setId("hlBottom");
        Responsive.makeResponsive(hlBottom);

        content.addComponents(hlTop, hlBottom);

//        Component loginForm = buildLoginForm();
//        addComponent(loginForm);
//        setExpandRatio(loginForm, 1);
//        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
//        cbxPais.focus();
//        Notification notification = new Notification(
//                "Welcome to Dashboard Demo");
//        notification
//                .setDescription("<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
//        notification.setHtmlContentAllowed(true);
//        notification.setStyleName("tray dark small closable login-help");
//        notification.setPosition(Position.BOTTOM_CENTER);
//        notification.setDelayMsec(20000);
//        notification.show(Page.getCurrent());
    }

    private void getAllData() {
        if (user.getPaisLogin() != null) {
            paises = new ArrayList();
            paises.add(user.getPaisLogin());
            estacion = user.getEstacionLogin();
        } else {
            SvcChangeLastRead svcCLR = new SvcChangeLastRead();
            paises = svcCLR.getAllPaises();
            svcCLR.closeConnections();
        }

        bcBombas = new BeanContainer<Integer, Bomba>(Bomba.class);
        bcBombas.setBeanIdProperty("id");

        bcLecturas = new BeanContainer<Integer, LecturaDetalle>(LecturaDetalle.class);
        bcLecturas.setBeanIdProperty("id");
    }

    VerticalLayout vlActions;

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setMargin(false);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        txtaComentario = new TextArea("Comentario:");
        txtaComentario.setSizeFull();

//        loginPanel.addComponent(buildLabels());
//        loginPanel.addComponent(buildFields());
//        VerticalLayout 
        vlActions = new VerticalLayout();
        vlActions.setSpacing(true);
        vlActions.setSizeUndefined();
        vlActions.addComponents(txtaComentario, btnSave);
        vlActions.setComponentAlignment(txtaComentario, Alignment.TOP_RIGHT);
        vlActions.setComponentAlignment(txtaComentario, Alignment.BOTTOM_RIGHT);

        HorizontalLayout hlTables = new HorizontalLayout();
        hlTables.setSizeFull();
        hlTables.setSpacing(true);
        hlTables.addComponents(tableBombas, tableLecturas, vlActions);
        hlTables.setExpandRatio(tableBombas, 0.15f);
        hlTables.setExpandRatio(tableLecturas, 0.70f);
        hlTables.setExpandRatio(vlActions, 0.15f);

        loginPanel.addComponent(hlTables);

        return loginPanel;
    }

    private void buildTableBombas() {
        tableBombas = utils.buildTable("Bombas:", 100f, 100f, bcBombas,
                new String[]{"nombre"},
                new String[]{"Nombre"});
        tableBombas.setColumnAlignments(Table.Align.LEFT);
        tableBombas.setSizeUndefined();
        tableBombas.setHeight("350px");
        tableBombas.setSelectable(true);
        tableBombas.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                int estacionId = ((Estacion) cbxEstacion.getValue()).getEstacionId();
                int bombaId = Integer.parseInt(event.getItem().getItemProperty("id").getValue().toString());
                boolean puedeEditar = true;
                SvcChangeLastRead svcCLT = new SvcChangeLastRead();
//                Turno turno = svcCLT.getTurnoActivoByEstacionid(estacion.getEstacionId());
                Turno turno = svcCLT.getUltimoTurnoByEstacionid(estacion.getEstacionId());

                if (estacion.getPaisId() != 320) {   //NO Guatemala
                    if (user.getRolLogin().equals("SUPERVISOR") && turno.getTurnoId() != null) {   //supervisor
//                        puedeEditar = svcCLT.getArqueocajaBomba(estacionId, bombaId, null).isEmpty();
                        puedeEditar = true;
                    } else if (user.getRolLogin().equals("OTRO")) {   //administrativo de oficina, rol superior a supervisor
                        //4 es el estado del cierre si el supervisor ya hizo cuadre sobre la bomba.
                        puedeEditar = !svcCLT.getArqueocajaBomba(estacionId, bombaId, null).isEmpty();
                    }
                }

                List<LecturaDetalle> lecturas = (puedeEditar)
                        ? svcCLT.getUltimaLecturaByEstacionTurnoBomba(estacionId, turno.getTurnoId(), bombaId) : new ArrayList();
                lecturasManualesActuales = svcCLT.getLecturasfinales(estacionId, "M");
                svcCLT.closeConnections();

                int count = 0;
                for (LecturaDetalle ld : lecturas) {
                    ld.setId(count++);
                    ld.setNewFinalReading(ld.getLecturaFinal());
                    ld.setNewInitialReading(ld.getLecturaInicial());
                }
                bcLecturas.removeAllItems();
                bcLecturas.addAll(lecturas);

                if (lecturas.isEmpty()) {
                    Notification.show("AVISO:", "No existen lecturas para un turno abierto.", Notification.Type.WARNING_MESSAGE);
                    return;
                }
//                tableLecturas.setSizeUndefined();
//                vlActions.setSizeUndefined();
            }
        });
    }

    private void buildTableLecturas() {
        tableLecturas = utils.buildTable("Lecturas:", 100f, 100f, bcLecturas,
                new String[]{"nombreBomba"},
                new String[]{"Bomba"});
        tableLecturas.addGeneratedColumn("colNuevaInicial", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("newInitialReading");  //Atributo del bean
                final TextField nfNuevaInicial = new TextField();
                nfNuevaInicial.setPropertyDataSource(utils.getPropertyFormatterDouble(pro));
                nfNuevaInicial.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfNuevaInicial.addStyleName("align-right");
                nfNuevaInicial.setWidth(130f, Unit.PIXELS);
                nfNuevaInicial.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        String value = nfNuevaInicial.getValue();
                        value = (value != null) ? value.replaceAll(",", "").trim() : value;
                        if (value != null && !value.isEmpty() && value.matches("(\\d+(\\.\\d+)?)|(\\.\\d+)")) {
                            //Nothing to do here
                        } else if (value != null) {
                            bcLecturas.getItem(itemId).getItemProperty("newInitialReading").setValue(0D);
                        }
                    }
                });
                return nfNuevaInicial;
            }
        });
        tableLecturas.addGeneratedColumn("colNuevaFinal", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("newFinalReading");  //Atributo del bean
                final TextField nfNuevaFinal = new TextField();
                nfNuevaFinal.setPropertyDataSource(utils.getPropertyFormatterDouble(pro));
                nfNuevaFinal.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfNuevaFinal.addStyleName("align-right");
                nfNuevaFinal.setWidth(130f, Unit.PIXELS);
                nfNuevaFinal.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        String value = nfNuevaFinal.getValue();
                        value = (value != null) ? value.replaceAll(",", "").trim() : value;
                        if (value != null && !value.isEmpty() && value.matches("(\\d+(\\.\\d+)?)|(\\.\\d+)")) {
                            //Nothing to do here
                        } else if (value != null) {
                            bcLecturas.getItem(itemId).getItemProperty("newFinalReading").setValue(0D);
                        }
                    }
                });
                return nfNuevaFinal;
            }
        });
        tableLecturas.setVisibleColumns(new Object[]{"nombreBomba", "nombreProducto", "colNuevaInicial", "colNuevaFinal"});
        tableLecturas.setColumnHeaders(new String[]{"Bomba", "Producto", "Lectura inicial", "Lectura final"});
        tableLecturas.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT, Table.Align.RIGHT, Table.Align.RIGHT);
        tableLecturas.setSizeUndefined();
        tableLecturas.setHeight("400px");
        tableLecturas.setStyleName(ValoTheme.TABLE_COMPACT);
        tableLecturas.setStyleName(ValoTheme.TABLE_SMALL);

    }

    private void buildFields() {
        cbxPais = new ComboBox("País:", new ListContainer<Pais>(Pais.class, paises));
        cbxPais.setItemCaptionPropertyId("nombre");
        cbxPais.setItemIconPropertyId("flag");
        cbxPais.setNullSelectionAllowed(false);
        cbxPais.setSizeUndefined();
        cbxPais.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                List<Estacion> estaciones = new ArrayList();
                if (user.getEstacionLogin() != null) {
                    estaciones.add(user.getEstacionLogin());
                } else {
                    SvcChangeLastRead svcCLR = new SvcChangeLastRead();
                    estaciones = svcCLR.getStationsByCountry(((Pais) cbxPais.getValue()).getPaisId(), true);
                    svcCLR.closeConnections();
                }
                cbxEstacion.setContainerDataSource(new ListContainer<Estacion>(Estacion.class, estaciones));
                cbxEstacion.setValue((user.getEstacionLogin() != null) ? user.getEstacionLogin() : null);
            }
        });

        cbxEstacion = new ComboBox("Estación:");
        cbxEstacion.setItemCaptionPropertyId("nombre");
        cbxEstacion.setSizeUndefined();
        cbxEstacion.setNullSelectionAllowed(false);
        cbxEstacion.setWidth("250px");
        cbxEstacion.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                estacion = ((Estacion) cbxEstacion.getValue());
                SvcChangeLastRead svcCLR = new SvcChangeLastRead();
                List<Bomba> bombas = svcCLR.getBombasByEstacionid(((Estacion) cbxEstacion.getValue()).getEstacionId());
                bcBombas.removeAllItems();
                bcBombas.addAll(bombas);
                svcCLR.closeConnections();
            }
        });

        txtaComentario = new TextArea("Comentario");
        txtaComentario.setSizeFull();

        if (paises.size() == 1) {
            cbxPais.setValue(user.getPaisLogin());
        }
    }

    private void buildActions() {
        btnSave = new Button("Guardar", FontAwesome.SAVE);
        btnSave.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnSave.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                if (txtaComentario.getValue().trim().isEmpty() || bcLecturas.getItemIds().isEmpty()) {
                    Notification.show("ERROR:", "Todos los campos son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                SvcReading svcLectura = new SvcReading();
                Turno turno = svcLectura.getTurnoActivoByEstacionid(estacion.getEstacionId());
                Lectura lectura = new Lectura(null, estacion.getEstacionId(), turno.getTurnoId(), null, null, null/*,null, null*/);
                lectura.setLecturaDetalle(new ArrayList<LecturaDetalle>());
                LecturaDetalle ldetail;
                boolean existe = false;
                for (Object itemId : bcLecturas.getItemIds()) {
                    ldetail = (LecturaDetalle) bcLecturas.getItem(itemId).getBean();
//                    ldetail.setLecturaTotal(ldetail.getNewFinalReading() - ldetail.getNewInitialReading());
                    ldetail.setLecturaInicial(ldetail.getNewInitialReading());
                    ldetail.setLecturaFinal(ldetail.getNewFinalReading());
                    lectura.getLecturaDetalle().add(ldetail);

                    svcLectura.doActionLecturaDetalle(Dao.ACTION_UPDATE, ldetail);

                    lectura.setLecturaId(ldetail.getLecturaId());
                    lectura.getLecturafinal().add(
                            new Lecturafinal(estacion.getEstacionId(), ldetail.getBombaId(), ldetail.getProductoId(),
                                    "M", ldetail.getLecturaInicial(), ldetail.getLecturaFinal(), user.getUsername(), user.getNombreLogin())
                    );
                }
//                lectura = svcLectura.doActionLectura(Dao.ACTION_UPDATE, lectura);

                for (Lecturafinal lfl : lectura.getLecturafinal()) {
                    for (Lecturafinal lflu : lecturasManualesActuales) {
                        if (lfl.getBombaId().equals(lflu.getBombaId()) && lfl.getProductoId().equals(lflu.getProductoId())
                                && lfl.getTipo().equals(lflu.getTipo())) {
                            existe = true;
                            break;
                        }
                    }
                    if (existe) {
                        svcLectura.doActionLecturaFinal(Dao.ACTION_UPDATE, lfl);
                    } else {
                        svcLectura.doActionLecturaFinal(Dao.ACTION_ADD, lfl);
                    }
                }

                svcLectura.closeConnections();
                if (lectura.getLecturaId() != null) {
//                    Notification.show("Operación realizada con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    Notification notif = new Notification("ÉXITO:", "Operación realizada con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    //notif.setStyleName("mystyle");
                    //notif.setIcon(new ThemeResource("img/reindeer.png"));
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PR_CHANGELASTREAD.getViewName());
                }
            }
        });
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("QuickTickets Dashboard");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);
        labels.addComponent(title);
        return labels;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new Dao();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnSave.setEnabled(acceso.isAgregar());
    }
}
