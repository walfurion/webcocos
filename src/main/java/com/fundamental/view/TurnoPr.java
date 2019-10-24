/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.view;

import com.fundamental.model.Bomba;
import com.fundamental.model.Dia;
import com.sisintegrados.generic.bean.Empleado;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.EstacionConf;
import com.fundamental.model.EstacionConfHead;
import com.fundamental.model.Horario;
import com.fundamental.model.Precio;
import com.fundamental.model.Producto;
import com.fundamental.model.Turno;
import com.fundamental.model.Utils;
import com.fundamental.services.Dao;
import com.fundamental.services.SvcEstacion;
import com.fundamental.services.SvcTurno;
import com.fundamental.services.SvcUsuario;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.view.form.FormEmpleado;
import com.sisintegrados.generic.bean.EmpleadoBombaTurno;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.combobox.FilteringMode;
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
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Allan G.
 */
public class TurnoPr extends Panel implements View {

    CreateComponents components = new CreateComponents();
    Label lblUltimoDia = new Label();
    Label lblUltimoTurno = new Label();
    ComboBox cmbPais = new ComboBox();
    ComboBox cmbEstacion = new ComboBox();
    DateField cmbFecha = new DateField("Fecha:");
    ComboBox cmbHorario = new ComboBox();
    ComboBox cmbTurno = new ComboBox();
    ComboBox cmbEmpleado = new ComboBox();
    Usuario usuario = new Usuario();
    SvcTurno dao = new SvcTurno();
    Utils utils = new Utils();
    Table tablaPrecio = new Table("Precios:");
    Table tablaAsignacion = new Table();
    BeanContainer<Integer, Producto> bcrPrecios = new BeanContainer<Integer, Producto>(Producto.class);
    Boolean showAutoservicio = false;
    List<Producto> combustibles;
    List<Precio> precios;
    Turno turno;
    Dia dia;
    Dia ultimoDia;
    Turno ultimoTurno;
    Estacion estacion;
    Pais pais;
    EstacionConfHead estConfHead;
    Button btnGuardar = new Button("Crear Turno");
    Button btnModificar = new Button("Modificar precio", FontAwesome.EDIT);
    Button btnAddEmpPump;
    BeanItemContainer<Pais> contPais = new BeanItemContainer<Pais>(Pais.class);

    /*Para Asignar Bombas Pistero*/
    BeanItemContainer<EmpleadoBombaTurno> bcrEmpPump = new BeanItemContainer<EmpleadoBombaTurno>(EmpleadoBombaTurno.class);
    BeanItemContainer<Bomba> contBomba = new BeanItemContainer<Bomba>(Bomba.class);
    BeanItemContainer<Empleado> contEmpleado = new BeanItemContainer<Empleado>(Empleado.class);
    Button nuevo = new Button();
    TextField nombrePistero = new TextField();
    CheckBox chkBomba1 = new CheckBox();
    CheckBox chkBomba2 = new CheckBox();
    CheckBox chkBomba3 = new CheckBox();
    CheckBox chkBomba4 = new CheckBox();
    CheckBox chkBomba5 = new CheckBox();
    CheckBox chkBomba6 = new CheckBox();
    CheckBox chkBomba7 = new CheckBox();
    CheckBox chkBomba8 = new CheckBox();
    CheckBox chkBomba9 = new CheckBox();
    CheckBox chkBomba10 = new CheckBox();
    CheckBox chkBomba11 = new CheckBox();
    CheckBox chkBomba12 = new CheckBox();

    Button addEmpleado = new Button();
    Button editEmpleado = new Button();
    FormEmpleado frmEmpleado;
    Empleado empleado = new Empleado();
    Integer varMaxBomba = 0;
    Integer bombaMasAltaOld = 0;

    public TurnoPr() {
        super.setLocale(VaadinSession.getCurrent().getAttribute(Locale.class));
        super.addStyleName(ValoTheme.PANEL_BORDERLESS);
        super.setSizeFull();
        DashboardEventBus.register(this);
        usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
        super.setContent(components.createVertical(Constant.styleTransactions, "100%", false, true, true, new Component[]{buildForm()}));
        cargaInfoSesion();
    }

    private Component buildForm() {
        return components.createVertical(Constant.styleLogin, "100%", false, false, true, new Component[]{buildTitle(), buildHeader(), buildToolbar2(), buildButtons()});
    }

    private Component buildTitle() {
        Label title = new Label("Turno");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        Component toolBar = components.createHorizontal(Constant.styleToolbar, Constant.sizeUndefined, true, false, false, new Component[]{buildToolbar()});
        return components.createHorizontal(Constant.styleViewheader, Constant.sizeUndefined, true, false, false, new Component[]{title, toolBar});
    }

    private CssLayout toolBar2;

    private Component buildToolbar() {
        toolBar2 = new CssLayout();
        VerticalLayout v = new VerticalLayout(toolBar2);
        return components.createHorizontal(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{v});
    }

    private void cargaUltTurnoDia() {
        SvcUsuario svu = new SvcUsuario();
        usuario = svu.getLastTurnLastDay(usuario);
        if (usuario.getDia() == null) {
            lblUltimoDia.setValue("SIN DATOS REGISTRADOS");
            lblUltimoTurno.setValue("SIN DATOS REGISTRADOS");
        } else {
            lblUltimoDia.setValue("Último día: " + usuario.getDia().getDia() + " (" + usuario.getDia().getEstado() + ")");
            lblUltimoTurno.setValue("Último turno: " + usuario.getTurno().getTurno() + " (" + usuario.getTurno().getEstado() + ")");
        }

        lblUltimoDia.addStyleName(ValoTheme.LABEL_BOLD);
        lblUltimoDia.addStyleName(ValoTheme.LABEL_H3);
        lblUltimoDia.addStyleName(ValoTheme.LABEL_COLORED);
        lblUltimoDia.setWidth("35%");

        lblUltimoTurno.addStyleName(ValoTheme.LABEL_BOLD);
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_H3);
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_COLORED);
        lblUltimoTurno.setSizeUndefined();

        toolBar2.removeAllComponents();
        toolBar2.addComponent(utils.vlContainer2(lblUltimoDia));
        toolBar2.addComponent(utils.vlContainer2(lblUltimoTurno));
    }

    private Component buildHeader() {
        bcrPrecios.setBeanIdProperty("productoId");
        bcrPrecios.removeAllItems();
        cargaTablaPrecios();
        contPais = new BeanItemContainer<Pais>(Pais.class);
        contPais.addAll(dao.getAllPaises());

        BeanItemContainer<Horario> contHorario = new BeanItemContainer<Horario>(Horario.class);
        BeanItemContainer<Estacion> contEstacion = new BeanItemContainer<Estacion>(Estacion.class);

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
                cmbEstacion.removeAllItems();
                cmbHorario.removeAllItems();
                cmbTurno.removeAllItems();
                cmbFecha.setValue(null);
                SvcEstacion svcEstacion = new SvcEstacion();
                Pais pais = new Pais();
                pais = (Pais) cmbPais.getValue();
                contEstacion.addAll(svcEstacion.getStationsByCountryUser(pais.getPaisId(), usuario.getUsuarioId()));
                svcEstacion.closeConnections();
                cmbEstacion.setContainerDataSource(contEstacion);
                if (contEstacion.size() == 1) {
                    cmbEstacion.setValue(contEstacion.getIdByIndex(0));
                }
                bcrPrecios.removeAllItems();
            }
        });

        cmbEstacion = new ComboBox("Estación:");
        cmbEstacion.setItemCaptionPropertyId("nombre");
        cmbEstacion.setNullSelectionAllowed(false);
        cmbEstacion.setFilteringMode(FilteringMode.CONTAINS);
        cmbEstacion.setRequired(true);
        cmbEstacion.setRequiredError("Debe seleccionar una estacion");
        cmbEstacion.setWidth("250px");
        cmbEstacion.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cmbEstacion.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (cmbEstacion.getValue() != null) {
                    cargaUltTurnoDia();
                }

            }
        });

        cmbFecha.setWidth("120px");
        cmbFecha.setDateFormat("dd-MM-yyyy");
        cmbFecha.setRangeEnd(Date.from(Instant.now()));
        cmbFecha.setLocale(new Locale("es", "ES"));
        cmbFecha.setLenient(true);
        cmbFecha.addStyleName(ValoTheme.DATEFIELD_SMALL);
        cmbFecha.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                if (cmbFecha.getValue() != null) {
                    cmbHorario.removeAllItems();
                    cmbTurno.removeAllItems();
                    estacion = new Estacion();
                    estacion = (Estacion) cmbEstacion.getValue();
                    pais = new Pais();
                    pais = (Pais) cmbPais.getValue();
                    cmbHorario.setValue(null);
                    contHorario.addAll(dao.getHorarioByEstacionid2(estacion.getEstacionId(), pais.getPaisId()));
                    cmbHorario.setContainerDataSource(contHorario);

                    ultimoDia = dao.getUltimoDiaByEstacionid(estacion.getEstacionId());
                    if (dia == null || dia.getFecha() == null) {    //La primera vez
                        dia = dao.getDiaActivoByEstacionid(estacion.getEstacionId());
                        dia = (dia.getEstadoId() == null) ? ultimoDia : dia;
                    }

                    ultimoTurno = dao.getUltimoTurnoByEstacionid2(estacion.getEstacionId(), cmbFecha.getValue());
                    //Solo puede haber un turno activo para cada estacion
                    if (turno == null || turno.getTurnoId() == null) {
                        turno = dao.getTurnoActivoByEstacionid(estacion.getEstacionId());
                        turno = (turno.getEstadoId() == null) ? ultimoTurno : turno;
                    }

                    if (turno.getHorario() != null || ultimoTurno.getHorario() != null) {
                        Horario h = (turno.getHorario() != null) ? turno.getHorario() : ultimoTurno.getHorario();
                        int i = 0;
                        for (i = 0; i < contHorario.size(); i++) {
                            Horario hh = new Horario();
                            hh = contHorario.getIdByIndex(i);
                            if (h.getHorarioId() == hh.getHorarioId()) {
                                cmbHorario.setValue(hh);
                            }
                        }
                    }

                    //se obtiene de base de datos pues necesitamos saber el estado.
                    BeanItemContainer<Turno> contTurno = new BeanItemContainer<Turno>(Turno.class);
                    dia = dao.getDiaByEstacionidFecha(estacion.getEstacionId(), cmbFecha.getValue());
                    dia.setFecha((dia.getFecha() == null) ? cmbFecha.getValue() : dia.getFecha());  //Para las validaciones iniciales de buildControls()
                    contTurno.addAll(dao.getTurnosByEstacionidDiaNolectura(estacion.getEstacionId(), cmbFecha.getValue()));
                    dao.closeConnections();
                    cmbTurno.setContainerDataSource(contTurno);
                    if (contTurno.size() > 0) {
//                        turno = (Turno) ((ArrayList) ctrTurnos.getItemIds()).get(listTurno.size() - 1);
                        turno = contTurno.getIdByIndex(contTurno.size() - 1);
                        cmbTurno.setValue(turno);

                    } else {
                        //La siguiente validacion es importante pues permite a un usuario crear un nuevo turno cuando es la PRIMERISIMA vez que este se crea para una estacion.
                        if (ultimoDia.getFecha() != null) {
                            bcrPrecios.removeAllItems();
                        }
                        turno = new Turno();
                    }
                    getDataPrecios();
//                    cargaTablaPrecios();
                    /*Agrego la tabla de precios y Empleados y sus Bombas*/
                    toolbarContainerTables.removeAllComponents();
                    toolbarContainerTables.addComponent(buildTables());
                }

                /*Operaciones con el turno*/
                //Validar si existen turnos sobre el dia
                if ((cmbEstacion.getValue() != null) && (cmbFecha.getValue() != null) && (cmbFecha.getValue() != null)) {
                    if (dao.validaTurnodia(estacion.getEstacionId(), contHorario.getIdByIndex(0).getEstacionconfheadId(), cmbFecha.getValue()) > 0) {
                        cmbTurno.setEnabled(true);
                    } else {
                        cmbTurno.setEnabled(false);
                    }
                }
//                cargaTablaPrecios();
                /*Agrego la tabla de precios y Empleados y sus Bombas*/
//                toolbarContainerTables.removeAllComponents();
//                toolbarContainerTables.addComponent(buildTables());
            }
        });

        cmbHorario = new ComboBox("Horarios:");
        cmbHorario.setItemCaptionPropertyId("nombreHoras");
        cmbHorario.setNullSelectionAllowed(false);
        cmbHorario.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cmbHorario.setWidth("130px");
        cmbHorario.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (cmbHorario.getValue() != null) {
                    bombaMasAltaOld = 0;
                    List<EstacionConfHead> configs = dao.getConfiguracionHeadByEstacionidHorario(estacion.getEstacionId(), ((Horario) cmbHorario.getValue()).getHorarioId());
                    estConfHead = configs.get(0);
                    dao.closeConnections();
                    for (EstacionConfHead ech : configs) {
                        for (EstacionConf ecf : ech.getEstacionConf()) {
                            if (ecf.getTipodespachoId() == 1) { //autoservicio
                                showAutoservicio = true;
                                break;
                            }
                        }
                    }
                    nuevoMetodoConfhead(configs.get(0));    //solo traera 1
                    /*Agrego la tabla de precios y Empleados y sus Bombas*/
                    toolbarContainerTables.removeAllComponents();
                    toolbarContainerTables.addComponent(buildTables());
                }
            }
        });

        cmbTurno = new ComboBox("Turno:");
        cmbTurno.setItemCaptionPropertyId("nombre");
        cmbTurno.setWidth("115px");
        cmbTurno.setNullSelectionAllowed(false);
        cmbTurno.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cmbTurno.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                turno = (Turno) cmbTurno.getValue();
                if (turno != null) {
                    getDataPrecios();
                    /*Agrego la tabla de precios y Empleados y sus Bombas*/
                    toolbarContainerTables.removeAllComponents();
                    toolbarContainerTables.addComponent(buildTables());
                    dao.closeConnections();

                    Integer h = dao.getIdHorario(turno.getTurnoId());
                    int i = 0;
                    for (i = 0; i < contHorario.size(); i++) {
                        Horario hh = new Horario();
                        hh = contHorario.getIdByIndex(i);
                        if (h == hh.getHorarioId()) {
                            cmbHorario.setValue(hh);
                        }
                    }
                }
            }
        });

        Component toolBar = components.createCssLayout(Constant.styleToolbar, Constant.sizeFull, false, false, true, new Component[]{utils.vlContainer(cmbPais), utils.vlContainer(cmbEstacion), utils.vlContainer(cmbFecha), utils.vlContainer(cmbHorario), utils.vlContainer(cmbTurno)});
        VerticalLayout v = new VerticalLayout(toolBar);
        return components.createHorizontal(Constant.styleViewheader, Constant.sizeFull, false, false, true, new Component[]{v});
    }
    private CssLayout toolbarContainerTables;

    private Component buildToolbar2() {
        toolbarContainerTables = new CssLayout();
        return components.createHorizontal(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{utils.vlContainer(toolbarContainerTables)});
    }

    private void formEmpleados(String tipo) {
        if (tipo.equals("Editar")) {
            if (cmbEmpleado.getValue() != null) {
                Empleado empleado = new Empleado();
                empleado = (Empleado) cmbEmpleado.getValue();
                frmEmpleado = new FormEmpleado(tipo, empleado);
                frmEmpleado.addCloseListener((e) -> {
                    cmbEmpleado.removeAllItems();
                    contEmpleado.removeAllItems();
                    contEmpleado = new BeanItemContainer<Empleado>(Empleado.class);
                    contEmpleado.addAll(dao.getEmpleados2(true));
                    cmbEmpleado.setContainerDataSource(contEmpleado);
                    cmbEmpleado.setItemCaptionPropertyId("nombre");
                    cmbEmpleado.setStyleName(ValoTheme.COMBOBOX_TINY);
                    cmbEmpleado.setRequired(true);
                    cmbEmpleado.setRequiredError("Debe Seleccionar empleado");
                    cmbEmpleado.setNullSelectionAllowed(false);
                    toolbarContainerCmbEmpleados.removeAllComponents();
                    toolbarContainerCmbEmpleados.addComponent(cmbEmpleado);
                });
                getUI().addWindow(frmEmpleado);
                frmEmpleado.focus();
            } else {
                Notification.show("Warning!!!", "Debe seleccionar un empleado, para modificar", Notification.Type.WARNING_MESSAGE);
            }
        } else if (tipo.equals("Nuevo")) {
            Empleado empleado = new Empleado();
            empleado = (Empleado) cmbEmpleado.getValue();
            frmEmpleado = new FormEmpleado(tipo, empleado);
            frmEmpleado.addCloseListener((e) -> {
                cmbEmpleado.removeAllItems();
                contEmpleado.removeAllItems();
                contEmpleado = new BeanItemContainer<Empleado>(Empleado.class);
                contEmpleado.addAll(dao.getEmpleados2(true));
                cmbEmpleado.setContainerDataSource(contEmpleado);
                cmbEmpleado.setItemCaptionPropertyId("nombre");
                cmbEmpleado.setStyleName(ValoTheme.COMBOBOX_TINY);
                cmbEmpleado.setRequired(true);
                cmbEmpleado.setRequiredError("Debe Seleccionar empleado");
                cmbEmpleado.setNullSelectionAllowed(false);
                toolbarContainerCmbEmpleados.removeAllComponents();
                toolbarContainerCmbEmpleados.addComponent(cmbEmpleado);
            });
            getUI().addWindow(frmEmpleado);
            frmEmpleado.focus();
        }
    }

    private CssLayout toolbarContainerCmbEmpleados;
    private CssLayout toolbarContainerTableAsignacion;

    private Component buildTables() {
        VerticalLayout v = new VerticalLayout();
        HorizontalLayout h = new HorizontalLayout();
        Label lblpistero = new Label("Nombre Empleado");
        lblpistero.setStyleName(ValoTheme.LABEL_TINY);
        lblpistero.setWidth("100px");
        contEmpleado = new BeanItemContainer<Empleado>(Empleado.class);
        contEmpleado.addAll(dao.getEmpleados2(true));
        cmbEmpleado.setContainerDataSource(contEmpleado);
        cmbEmpleado.setItemCaptionPropertyId("nombre");
        cmbEmpleado.setStyleName(ValoTheme.COMBOBOX_TINY);
        cmbEmpleado.setRequired(true);
        cmbEmpleado.setRequiredError("Debe Seleccionar Empleado");
        cmbEmpleado.setNullSelectionAllowed(false);

        addEmpleado = new Button(FontAwesome.PLUS_CIRCLE);
        addEmpleado.addStyleName(ValoTheme.BUTTON_TINY);
        addEmpleado.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        addEmpleado.setDescription("Nuevo Empleado");
        addEmpleado.addClickListener(clickEvent -> formEmpleados("Nuevo"));

        editEmpleado = new Button(FontAwesome.EDIT);
        editEmpleado.addStyleName(ValoTheme.BUTTON_TINY);
        editEmpleado.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        editEmpleado.setDescription("Editar Empleado");
        editEmpleado.addClickListener(clickEvent -> formEmpleados("Editar"));
        toolbarContainerCmbEmpleados = new CssLayout();
        toolbarContainerTableAsignacion = new CssLayout();
        toolbarContainerCmbEmpleados.addComponent(cmbEmpleado);
        h.addComponent(lblpistero);
        h.addComponent(toolbarContainerCmbEmpleados);
        h.addComponent(addEmpleado);
        h.addComponent(editEmpleado);
        h.setSpacing(true);
        Component adicionBar = components.createCssLayout(Constant.styleViewheader2, Constant.sizeUndefined, false, false, true, new Component[]{h});
        v.addComponent(adicionBar);

        //tabla
        v.addComponent(buildCheckBoxPumps());
        v.addComponent(toolbarContainerTableAsignacion);
        v.setSpacing(true);
        toolbarContainerTableAsignacion.removeAllComponents();
        ConstruyeTablaAsignacion();
        toolbarContainerTableAsignacion.addComponent(tablaAsignacion);
        return components.createCssLayout(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{utils.vlContainerTable(tablaPrecio), utils.vlContainerTable(v)});
    }

    private void ConstruyeTablaAsignacion() {
        tablaAsignacion = new Table();
        tablaAsignacion.addStyleName(ValoTheme.TABLE_COMPACT);
        tablaAsignacion.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        Turno tt = new Turno();
        tt = (Turno) cmbTurno.getValue();
        Integer maxBomba = 0;
        if (tt != null) {
            tablaAsignacion.setContainerDataSource(bcrEmpPump);
            tablaAsignacion.addGeneratedColumn("colNombre", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("nombre"); //Atributo del bean
                    TextField txtNombre = new TextField(pro);
                    txtNombre.setEnabled(false);
                    txtNombre.addStyleName("align-left");
                    txtNombre.setWidth("120px");
                    txtNombre.setStyleName(ValoTheme.TEXTFIELD_TINY);
                    txtNombre.setNullRepresentation("");
                    return txtNombre;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba1", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba1"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba2", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba2"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba3", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba3"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba4", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba4"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba5", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba5"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba6", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba6"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba7", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba7"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba8", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba8"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba9", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba9"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba10", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba10"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba11", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba11"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba12", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba12"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, final Object itemId, Object columnId) {
                    Button btnDelete = new Button(FontAwesome.TRASH);
                    btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                    btnDelete.addStyleName(ValoTheme.BUTTON_TINY);
                    btnDelete.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            bcrEmpPump.removeItem(itemId);
                            tablaAsignacion.refreshRowCache();
                        }
                    });
                    return btnDelete;
                }
            });

            for (EmpleadoBombaTurno empturno : bcrEmpPump.getItemIds()) {
                maxBomba = empturno.getMaxBombas();
            }

            List<Object> columnas = new ArrayList<Object>();
            List<String> encabezados = new ArrayList<String>();
            columnas.add("colNombre");
            encabezados.add("Pistero");
            if (maxBomba > 0) {
                for (int i = 1; i <= maxBomba; i++) {
                    columnas.add("colBomba" + i);
                    encabezados.add("Bomba" + i);
                }
            }
            columnas.add("colDelete");
            encabezados.add("Accion");
            Object[] colCols = columnas.toArray(new Object[0]);
            String[] colheads = encabezados.toArray(new String[0]);

            tablaAsignacion.setVisibleColumns(colCols);
            tablaAsignacion.setColumnHeaders(colheads);
            tablaAsignacion.setHeight("220px");
            Responsive.makeResponsive(tablaAsignacion);

        } else {
            tablaAsignacion.setContainerDataSource(bcrEmpPump);
            tablaAsignacion.addGeneratedColumn("colNombre", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("nombre"); //Atributo del bean
                    TextField txtNombre = new TextField(pro);
                    txtNombre.setEnabled(false);
                    txtNombre.addStyleName("align-left");
                    txtNombre.setWidth("120px");
                    txtNombre.setStyleName(ValoTheme.TEXTFIELD_TINY);
                    txtNombre.setNullRepresentation("");
                    return txtNombre;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba1", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba1"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba2", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba2"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba3", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba3"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba4", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba4"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba5", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba5"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba6", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba6"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba7", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba7"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba8", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba8"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba9", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba9"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba10", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba10"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba11", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba11"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colBomba12", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, Object itemId, Object columnId) {
                    Property pro = source.getItem(itemId).getItemProperty("bomba12"); //Atributo del bean
                    CheckBox chk = new CheckBox();
                    chk.setStyleName(ValoTheme.CHECKBOX_SMALL);
                    boolean bool = (boolean) pro.getValue();
                    chk.setValue(bool);
                    chk.setEnabled(false);
                    chk.addStyleName("align-center");
                    return chk;
                }
            });
            tablaAsignacion.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
                @Override
                public Object generateCell(Table source, final Object itemId, Object columnId) {
                    Button btnDelete = new Button(FontAwesome.TRASH);
                    btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                    btnDelete.addStyleName(ValoTheme.BUTTON_TINY);
                    btnDelete.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            bcrEmpPump.removeItem(itemId);
                            tablaAsignacion.refreshRowCache();
                            /*Recupera valores de bombas*/
                            validaBombas("I");
                            reactivaBombas((EmpleadoBombaTurno) itemId);
                            toolbarContainerTableAsignacion.removeAllComponents();
                            ConstruyeTablaAsignacion();
                            toolbarContainerTableAsignacion.addComponent(tablaAsignacion);

                        }
                    });
                    return btnDelete;
                }
            });

            List<Object> columnas = new ArrayList<Object>();
            List<String> encabezados = new ArrayList<String>();
            columnas.add("colNombre");
            encabezados.add("Pistero");
            if (varMaxBomba > 0) {
                for (int i = 1; i <= varMaxBomba; i++) {
                    columnas.add("colBomba" + i);
                    encabezados.add("Bomba" + i);
                }
            }
            columnas.add("colDelete");
            encabezados.add("Accion");
            Object[] colCols = columnas.toArray(new Object[0]);
            String[] colheads = encabezados.toArray(new String[0]);

            tablaAsignacion.setVisibleColumns(colCols);
            tablaAsignacion.setColumnHeaders(colheads);
            tablaAsignacion.setHeight("220px");
            Responsive.makeResponsive(tablaAsignacion);

        }
    }

    private void validaBombas(String val) {
        varMaxBomba = 0;
        Integer bombaMasAlta = 0;
        bcrEmpPump.sort(new Object[]{"empleadoid"}, new boolean[]{true});

        for (EmpleadoBombaTurno empleado : bcrEmpPump.getItemIds()) {
            if (empleado.isBomba1()) {
                varMaxBomba++;
                bombaMasAlta = 1;
                chkBomba1.setEnabled(false);
            }
            if (empleado.isBomba2()) {
                varMaxBomba++;
                bombaMasAlta = 2;
                chkBomba2.setEnabled(false);
            }
            if (empleado.isBomba3()) {
                varMaxBomba++;
                bombaMasAlta = 3;
                chkBomba3.setEnabled(false);
            }
            if (empleado.isBomba4()) {
                varMaxBomba++;
                bombaMasAlta = 4;
                chkBomba4.setEnabled(false);
            }
            if (empleado.isBomba5()) {
                varMaxBomba++;
                bombaMasAlta = 5;
                chkBomba5.setEnabled(false);
            }
            if (empleado.isBomba6()) {
                varMaxBomba++;
                bombaMasAlta = 6;
                chkBomba6.setEnabled(false);
            }
            if (empleado.isBomba7()) {
                varMaxBomba++;
                bombaMasAlta = 7;
                chkBomba7.setEnabled(false);
            }
            if (empleado.isBomba8()) {
                varMaxBomba++;
                bombaMasAlta = 8;
                chkBomba8.setEnabled(false);
            }
            if (empleado.isBomba9()) {
                varMaxBomba++;
                bombaMasAlta = 9;
                chkBomba9.setEnabled(false);
            }
            if (empleado.isBomba10()) {
                varMaxBomba++;
                bombaMasAlta = 10;
                chkBomba10.setEnabled(false);
            }

            if (empleado.isBomba11()) {
                varMaxBomba++;
                bombaMasAlta = 11;
                chkBomba11.setEnabled(false);
            }

            if (empleado.isBomba12()) {
                varMaxBomba++;
                bombaMasAlta = 12;
                chkBomba12.setEnabled(false);
            }
        }
        System.out.println("VAL " + val);
        if (val.equals("A")) {
            if (bombaMasAlta > bombaMasAltaOld) {
                bombaMasAltaOld = bombaMasAlta;
                varMaxBomba = bombaMasAlta;
            } else {
                varMaxBomba = bombaMasAltaOld;
            }
        } else {
            System.out.println("BOMBA MAS ALTA " + bombaMasAlta);
            System.out.println("BOMBA MAS ALTA OLD" + bombaMasAltaOld);
            if (bombaMasAlta < bombaMasAltaOld) {
                bombaMasAltaOld = bombaMasAlta;
                varMaxBomba = bombaMasAlta;
            } else {
                varMaxBomba = bombaMasAltaOld;
            }
        }

    }

    private void reactivaBombas(EmpleadoBombaTurno accion) {
        if (accion.isBomba1()) {
            chkBomba1.setEnabled(true);
        }
        if (accion.isBomba2()) {
            chkBomba2.setEnabled(true);
        }
        if (accion.isBomba3()) {
            chkBomba3.setEnabled(true);
        }
        if (accion.isBomba4()) {
            chkBomba4.setEnabled(true);
        }
        if (accion.isBomba5()) {
            chkBomba5.setEnabled(true);
        }
        if (accion.isBomba6()) {
            chkBomba6.setEnabled(true);
        }
        if (accion.isBomba7()) {
            chkBomba7.setEnabled(true);
        }
        if (accion.isBomba8()) {
            chkBomba8.setEnabled(true);
        }
        if (accion.isBomba9()) {
            chkBomba9.setEnabled(true);
        }
        if (accion.isBomba10()) {
            chkBomba10.setEnabled(true);
        }

        if (accion.isBomba11()) {
            chkBomba11.setEnabled(true);
        }
        if (accion.isBomba12()) {
            chkBomba12.setEnabled(true);
        }
    }

    private Component buildCheckBoxPumps() {
        HorizontalLayout h = new HorizontalLayout();
        ArrayList<CheckBox> bombasChk = new ArrayList<CheckBox>();
        contBomba = new BeanItemContainer<Bomba>(Bomba.class);
        Horario hh = new Horario();
        hh = (Horario) cmbHorario.getValue();

        if (hh != null) {
            contBomba.addAll(dao.getBombasByEstacionConfheadId(hh.getEstacionconfheadId(), estacion.getEstacionId()));
            chkBomba1 = new CheckBox("1");
            chkBomba1.setDescription("1");
            chkBomba1.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba1);
            chkBomba2 = new CheckBox("2");
            chkBomba2.setDescription("2");
            chkBomba2.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba2);
            chkBomba3 = new CheckBox("3");
            chkBomba3.setDescription("3");
            chkBomba3.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba3);
            chkBomba4 = new CheckBox("4");
            chkBomba4.setDescription("4");
            chkBomba4.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba4);
            chkBomba5 = new CheckBox("5");
            chkBomba5.setDescription("5");
            chkBomba5.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba5);
            chkBomba6 = new CheckBox("6");
            chkBomba6.setDescription("6");
            chkBomba6.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba6);
            chkBomba7 = new CheckBox("7");
            chkBomba7.setDescription("7");
            chkBomba7.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba7);
            chkBomba8 = new CheckBox("8");
            chkBomba8.setDescription("8");
            chkBomba8.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba8);
            chkBomba9 = new CheckBox("9");
            chkBomba9.setDescription("9");
            chkBomba9.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba9);
            chkBomba10 = new CheckBox("10");
            chkBomba10.setDescription("10");
            chkBomba10.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba10);
            chkBomba11 = new CheckBox("11");
            chkBomba11.setDescription("11");
            chkBomba11.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba11);
            chkBomba12 = new CheckBox("12");
            chkBomba12.setDescription("12");
            chkBomba12.setStyleName(ValoTheme.CHECKBOX_SMALL);
            bombasChk.add(chkBomba12);
            for (Bomba bomba : contBomba.getItemIds()) {
                h.addComponent(bombasChk.get(bomba.getId() - 1));
            }

            nuevo = new Button("Asignar");
            nuevo.setIcon(FontAwesome.CHECK_SQUARE_O);
            nuevo.addStyleName(ValoTheme.BUTTON_TINY);
            nuevo.addStyleName(ValoTheme.BUTTON_FRIENDLY);
            nuevo.setDescription("Agregar Asignacion");
            nuevo.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    if (cmbEmpleado.getValue() == null) {
                        Notification.show("Error !!! ", "Debe seleccionar un empleado", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                    if ((chkBomba1.getValue()) || (chkBomba2.getValue()) || (chkBomba3.getValue()) || (chkBomba4.getValue()) || (chkBomba5.getValue()) || (chkBomba6.getValue()) || (chkBomba7.getValue()) || (chkBomba8.getValue()) || (chkBomba9.getValue()) || (chkBomba10.getValue()) || (chkBomba11.getValue()) || (chkBomba12.getValue())) {
                        empleado = new Empleado();
                        empleado = (Empleado) cmbEmpleado.getValue();
                        boolean val = false;
                        if (bcrEmpPump.size() > 0) {
                            for (EmpleadoBombaTurno bombaturno : bcrEmpPump.getItemIds()) {
                                if (bombaturno.getEmpleadoid() == empleado.getEmpleadoId()) {
                                    val = true;
                                }
                            }
                        }
                        if (!val) {
                            bcrEmpPump.addItem(new EmpleadoBombaTurno(0, empleado.getEmpleadoId(), empleado.getNombre(), varMaxBomba, chkBomba1.getValue(), chkBomba2.getValue(), chkBomba3.getValue(), chkBomba4.getValue(), chkBomba5.getValue(), chkBomba6.getValue(), chkBomba7.getValue(), chkBomba8.getValue(), chkBomba9.getValue(), chkBomba10.getValue(), chkBomba11.getValue(), chkBomba12.getValue()));
                            cmbEmpleado.setValue(null);
                            reiniciaCheckBox();
                            /*Carga Tabla Asignaciones*/
                            validaBombas("A");
                            toolbarContainerTableAsignacion.removeAllComponents();
                            ConstruyeTablaAsignacion();
                            toolbarContainerTableAsignacion.addComponent(tablaAsignacion);
                        }
                    } else {
                        Notification.show("Error !!! ", "Debe seleccionar bombas", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }
            });

            h.addComponent(nuevo);
            h.setSpacing(true);
            h.setResponsive(true);
        }
        return h;
    }

    private void reiniciaCheckBox() {
        chkBomba1.setValue(false);
        chkBomba2.setValue(false);
        chkBomba3.setValue(false);
        chkBomba4.setValue(false);
        chkBomba5.setValue(false);
        chkBomba6.setValue(false);
        chkBomba7.setValue(false);
        chkBomba8.setValue(false);
        chkBomba9.setValue(false);
        chkBomba10.setValue(false);
        chkBomba11.setValue(false);
        chkBomba12.setValue(false);
    }

    private void cargaTablaPrecios() {
        tablaPrecio.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tablaPrecio.addStyleName(ValoTheme.TABLE_COMPACT);
        tablaPrecio.setContainerDataSource(bcrPrecios);
        tablaPrecio.addGeneratedColumn("colSC", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Property prop = source.getItem(itemId).getItemProperty("priceSC"); //Atributo del bean
                TextField tfdSC = new TextField(utils.getPropertyFormatterDouble(prop));
                tfdSC.setWidth("100px");
                tfdSC.addStyleName("align-right");
                tfdSC.setNullRepresentation("0.00");
                tfdSC.setStyleName(ValoTheme.TEXTFIELD_TINY);
                tfdSC.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                    }
                });
                return tfdSC;
            }
        });
        tablaPrecio.addGeneratedColumn("colAS", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Property prop = source.getItem(itemId).getItemProperty("priceAS"); //Atributo del bean
                TextField tfdAS = new TextField(utils.getPropertyFormatterDouble(prop));
                tfdAS.addStyleName("align-right");
                tfdAS.setNullRepresentation("0.00");
                tfdAS.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                    }
                });
                return tfdAS;
            }
        });

        Object[] visCols = new Object[]{"nombre", "colSC"};
        String[] colHeads = new String[]{"Producto", "Servicio completo"};
        if (showAutoservicio) {
            visCols = new Object[]{"nombre", "colAS", "colSC"};
            colHeads = new String[]{"Producto", "Autoservicio", "Servicio completo"};
        }
        tablaPrecio.setVisibleColumns(visCols);
        tablaPrecio.setColumnHeaders(colHeads);
        tablaPrecio.setHeight("160px");
        Responsive.makeResponsive(tablaPrecio);
    }

    private void getDataPrecios() {
        /*Los productos tipo 1 son del tipo combustible y estan asociados a la estacion*/
        combustibles = dao.getCombustiblesByEstacionid(estacion.getEstacionId());
        //La siguiente funcion, si el idTurno es null, entonces devuelve los ultimos precios de la estacion
        Integer turnoId = (turno.getTurnoId() != null) ? turno.getTurnoId() : ultimoTurno.getTurnoId();
        precios = dao.getPreciosByTurnoid(turnoId);
        dao.closeConnections();
        for (Producto p : combustibles) {
            for (Precio pre : precios) {
                if (p.getProductoId().equals(pre.getProductoId())) {
                    if (pre.getTipodespachoId() == 1) { //auto
                        p.setPriceAS(pre.getPrecio());
                    } else if (pre.getTipodespachoId() == 2) {  //completo
                        p.setPriceSC(pre.getPrecio());
                    }
                }
            }

            //Si eligen un dia "nuevo", debe mostrar los valores de precios.
            if (precios.isEmpty()) {
                p.setPriceAS(0D);
                p.setPriceSC(0D);
            }
        }
        bcrPrecios.removeAllItems();
        bcrPrecios.addAll(combustibles);

        bcrEmpPump = new BeanItemContainer<EmpleadoBombaTurno>(EmpleadoBombaTurno.class);
        bcrEmpPump.removeAllItems();
        bcrEmpPump.addAll(dao.getTurnoEmpBombaByTurnoid2(turnoId));

    }

    private void nuevoMetodoConfhead(EstacionConfHead echSelected) {
        Integer[] showAutoFull = new Integer[]{0, 0};
        showAutoservicio = false;
//        EstacionConfHead echSelected = ((EstacionConfHead) cbxConfiguracion.getValue());
        for (EstacionConf ecf : echSelected.getEstacionConf()) {
            if (ecf.getTipodespachoId() == 1 && showAutoFull[0] == 0) { //Auto
                showAutoFull[0] = 1;
                showAutoservicio = true;
            } else if (ecf.getTipodespachoId() == 2 && showAutoFull[1] == 0) { //Full
                showAutoFull[1] = 1;
            }
        }

        Object[] visCols = new Object[]{""};
        String[] colHeads = new String[]{""};
        if (showAutoFull[0] == 1 && showAutoFull[1] == 1) {
            visCols = new Object[]{"nombre", "colAS", "colSC"};
            colHeads = new String[]{"Producto", "Autoservicio", "Servicio completo"};
        } else if (showAutoFull[0] == 1) {
            visCols = new Object[]{"nombre", "colAS"};
            colHeads = new String[]{"Producto", "Autoservicio"};
        } else if (showAutoFull[1] == 1) {
            visCols = new Object[]{"nombre", "colSC"};
            colHeads = new String[]{"Producto", "Servicio completo"};
        }
        tablaPrecio.setVisibleColumns(visCols);
        tablaPrecio.setColumnHeaders(colHeads);
//        lblHoraConf.setValue("Horario: " + echSelected.getHoraInicio() + " - " + echSelected.getHoraFin());
//        if (turno.getTurnoId() == null || ultimoTurno.getTurnoId() == null) {
//            SvcTurno service = new SvcTurno();
//            listPump = service.getBombasByEstacionConfheadId(echSelected.getEstacionconfheadId(), 0);
//            bcrEmpPump.removeAllItems();
//            service.closeConnections();
//        }
    }

    private Component buildButtons() {
        btnGuardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnGuardar.setIcon(FontAwesome.SAVE);
        btnGuardar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if (ultimoDia.getFecha() != null && ultimoDia.getEstadoId() == 2 && ultimoDia.getFecha().equals(cmbFecha.getValue())) {
                    Notification.show("ERROR:", "Para la fecha elegida, existe un día cerrado.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                Producto prod;
                for (Integer pi : (List<Integer>) tablaPrecio.getItemIds()) {
                    prod = (Producto) ((BeanItem) tablaPrecio.getItem(pi)).getBean();
                    if ((prod.getPriceAS() - prod.getPriceSC()) >= 0) {
                        Notification.show("ERROR:", "Por favor revise los valores de precios para el producto *" + prod.getNombre() + "*", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }
                if (bcrEmpPump.getItemIds().isEmpty()) {
                    Notification.show("ERROR:", "Debe asociar almenos un empleado con una bomba.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
//                TurnoEmpleadoBomba teb;
//                for (Integer itemId : bcrEmpPump.getItemIds()) {
//                    teb = bcrEmpPump.getItem(itemId).getBean();
//                    if (teb.getEmployee() == null || teb.getPump() == null) {
//                        Notification.show("ERROR:", "Las asociaciones de bomba/empleado debe ser válidas.", Notification.Type.ERROR_MESSAGE);
//                        return;
//                    }
//                }

                boolean crearDia = false;
                Turno turno = new Turno(null, usuario.getEstacionLogin().getEstacionId(), usuario.getUsuarioId(), 1, usuario.getUsername(), cmbFecha.getValue(), usuario.getNombreLogin());
                turno.setEstacionconfheadId(estConfHead.getEstacionconfheadId());
                //El dia se manda crear solo si es necesario
                if (ultimoDia.getFecha() == null || (ultimoDia.getFecha() != null && !ultimoDia.getFecha().equals(cmbFecha.getValue()))) {
                    dia = new Dia(usuario.getEstacionLogin().getEstacionId(), cmbFecha.getValue(), 1, usuario.getUsername(), usuario.getNombreLogin());
                    if (cmbFecha.isEnabled()) {
                        crearDia = true;
                    }
                }
                turno.setFecha(cmbFecha.getValue());

                Precio precio;
                int conExito = 0;
                List<Precio> listPrecio = new ArrayList();
                for (Integer pi : (List<Integer>) tablaPrecio.getItemIds()) {
                    prod = (Producto) ((BeanItem) tablaPrecio.getItem(pi)).getBean();
                    if (showAutoservicio) {
                        precio = new Precio(turno.getTurnoId(), pi, 1, prod.getPriceAS(), usuario.getUsername(), usuario.getNombreLogin());  //AutoServicio
//                        precio = dao.doActionPrecio(Dao.ACTION_ADD, precio);
                        listPrecio.add(precio);
                        conExito = (precio != null) ? conExito + 1 : conExito;
                    }
                    precio = new Precio(turno.getTurnoId(), pi, 2, prod.getPriceSC(), usuario.getUsername(), usuario.getNombreLogin());  //Servicio completo
//                    precio = dao.doActionPrecio(Dao.ACTION_ADD, precio);
                    listPrecio.add(precio);
                    conExito = (precio.getTurnoId() != null) ? conExito + 1 : conExito;
                }
//                List<TurnoEmpleadoBomba> listTurnoEmpPump = new ArrayList();
//                for (Integer itemId : bcrEmpPump.getItemIds()) {
//                    listTurnoEmpPump.add(bcrEmpPump.getItem(itemId).getBean());
//                }
                turno.setHorarioId(((Horario) cmbHorario.getValue()).getHorarioId());

//                boolean everythingOk = dao.doCreateTurn(crearDia, dia, turno, listPrecio, listTurnoEmpPump);  //Descomentar
                dao.closeConnections();

                /*TODO EL IF DESCOMENTAR*/
//                if (everythingOk) {
//                    Notification notif = new Notification("ÉXITO:", "El registro de turno y precio se ha creado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
//                    notif.setDelayMsec(3000);
//                    notif.setPosition(Position.MIDDLE_CENTER);
//                    notif.show(Page.getCurrent());
//                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PR_TURN.getViewName());
//                } else {
//                    Notification.show("ERROR:", "Ocurrió un error al guardar el precio.\n", Notification.Type.ERROR_MESSAGE);
//                    turno = dao.doActionTurno(Dao.ACTION_DELETE, turno);
//                    return;
//                }
            }
        });

        btnModificar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnModificar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                SvcTurno service = new SvcTurno();
                Precio precio;
                Producto prod;
                int counter = 0;
                for (Integer pid : bcrPrecios.getItemIds()) {
                    prod = bcrPrecios.getItem(pid).getBean();
                    precio = new Precio(turno.getTurnoId(), prod.getProductoId(), 2, prod.getPriceSC(), null, null);  //ServicioCompleto
                    precio.setModificadoPor(usuario.getUsername());
                    precio.setModificadoPersona(usuario.getNombreLogin());
                    precio = service.doActionPrecio(Dao.ACTION_UPDATE, precio);
                    counter += (precio.getTurnoId() != null) ? 1 : 0;
                    if (showAutoservicio) {   //Guatemala
                        precio = new Precio(turno.getTurnoId(), prod.getProductoId(), 1, prod.getPriceAS(), null, null);   //Autoservicio
                        precio.setModificadoPor(usuario.getUsername());
                        precio.setModificadoPersona(usuario.getNombreLogin());
                        precio = service.doActionPrecio(Dao.ACTION_UPDATE, precio);
                    }
                }
                service.closeConnections();

                if (bcrPrecios.getItemIds().size() == counter) {
                    Notification notif = new Notification("ÉXITO:", "El registro de precio se ha actualizado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PR_TURN.getViewName());
                } else {
                    Notification.show("ERROR:", "Ocurrió un error al actualizar el precio.\n", Notification.Type.ERROR_MESSAGE);
                    return;
                }
            }
        });

        btnAddEmpPump = utils.buildButton("Agregar", FontAwesome.PLUS, ValoTheme.BUTTON_PRIMARY);
        btnAddEmpPump.addStyleName(ValoTheme.BUTTON_SMALL);
        btnAddEmpPump.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (turno != null) {
                    int turnoId = (turno.getTurnoId() != null) ? turno.getTurnoId() : 0;
//                    bcrEmpPump.addBean(new TurnoEmpleadoBomba(utils.getRandomNumberInRange(1, 1000), turnoId, 0, 0, user.getNombreLogin()));
//                    tableEmployeePump.refreshRowCache();
                }
            }
        });

        HorizontalLayout footer = (HorizontalLayout) components.createHorizontal(ValoTheme.WINDOW_BOTTOM_TOOLBAR, "", true, false, false, new Component[]{btnGuardar, btnModificar});

        footer.setComponentAlignment(btnGuardar, Alignment.TOP_RIGHT);
        footer.setWidth(
                100.0f, Unit.PERCENTAGE);
        return footer;
    }

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
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
