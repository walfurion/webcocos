package com.fundamental.view;

import com.fundamental.model.Bomba;
import com.fundamental.model.Dia;
import com.fundamental.model.Empleado;
import com.fundamental.model.Estacion;
import com.fundamental.services.Dao;
import com.fundamental.model.EstacionConf;
import com.fundamental.model.EstacionConfHead;
import com.fundamental.model.Horario;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Precio;
import com.fundamental.model.Producto;
import com.fundamental.model.Turno;
import com.fundamental.model.TurnoEmpleadoBomba;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.services.SvcEstacion;
import com.fundamental.services.SvcTurno;
import com.fundamental.utils.Constant;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
public class PrTurnOLD extends Panel implements View {

    VerticalLayout root = new VerticalLayout();
    Button btnGuardar = new Button("Crear Turno"),
            btnModificar = new Button("Modificar precio", FontAwesome.EDIT),
            btnAddEmpPump;
    Table tablePrecio = new Table("Precios:"), tblEmployeePump;
    Label labelStation, labelTurno;
    TextField tfTurnoRef = new TextField("Turno referencia");
//    ComboBox cbxConfiguracion = new ComboBox("Configuración de bombas:");
    Container contConfigs;
    ComboBox cbxSchedule;
    List<Horario> listSchedules = new ArrayList();

    Label lblUltimoDía = new Label(),
            lblUltimoTurno = new Label(), lblHoraConf = new Label();

    DateField dfdFecha = new DateField("Correspondiente a fecha:");
    ComboBox cbxCountry = new ComboBox("País:", new ListContainer<>(Pais.class, new ArrayList())),
            cbxEstacion = new ComboBox("Estación:"),
            cbxTurno = new ComboBox("Turno:");   //controles de navegacion
    Pais pais;
    Estacion estacion;
    Container ctrTurnos;

    BeanContainer<Integer, Producto> bcrPrecios = new BeanContainer<Integer, Producto>(Producto.class);
    BeanContainer<Integer, TurnoEmpleadoBomba> bcrEmpPump = new BeanContainer<Integer, TurnoEmpleadoBomba>(TurnoEmpleadoBomba.class);

    Turno turno, ultimoTurno;
    Dia dia, ultimoDia;
    List<Producto> combustibles;
    List<Empleado> listEmployees;
    List<Precio> precios;
    List<Horario> horariosHoy;
    List<Bomba> listPump = new ArrayList();
    List<Pais> allCountries = new ArrayList();
    Boolean showAutoservicio = false;
    EstacionConfHead estConfHead;
    String[] uniqueStation;

    //template
    private final VerticalLayout vlRoot;
    private CssLayout content = new CssLayout();
    Utils utils = new Utils();
    Usuario user;

    public PrTurnOLD() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        content.addStyleName("dashboard-panels");
        Responsive.makeResponsive(content);
        vlRoot = utils.buildVerticalR("vlRoot", true, false, true, "dashboard-view");
        setContent(vlRoot);
        Responsive.makeResponsive(vlRoot);
        vlRoot.addComponent(utils.buildHeaderR("Turno"));
        vlRoot.addComponent(utils.buildSeparator());
        vlRoot.addComponent(content);
        vlRoot.setExpandRatio(content, 1);
        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        getAllData();
        //template

        buildTables();
//        buildComboboxConfiguration();
        buildControls();
        buildButtons();

//        Label lblMessage = new Label("Día actual (en operación):       ");
//        lblMessage.setSizeUndefined();
//        lblMessage.addStyleName(ValoTheme.LABEL_BOLD);
//        lblMessage.addStyleName(ValoTheme.LABEL_H2);
        CssLayout cltInfo = new CssLayout();
        cltInfo.setSizeUndefined();
        cltInfo.setWidth(100f, Unit.PERCENTAGE);
        Responsive.makeResponsive(cltInfo);
        cltInfo.addComponents(lblUltimoDía, lblUltimoTurno);

        CssLayout cltToolbar = new CssLayout();
        cltToolbar.setSizeUndefined();
        cltToolbar.setWidth(100f, Unit.PERCENTAGE);
        Responsive.makeResponsive(cltToolbar);
        Integer estacionId = (user.getPaisLogin() == null) ? null : user.getPaisLogin().getPaisId();
//        if (dia.getEstacionId() != null) {
//            cltToolbar.addComponent(lblMessage);
//        }
        cltToolbar.addComponents(utils.vlContainer(cbxCountry), utils.vlContainer(cbxEstacion), utils.vlContainer(dfdFecha));
        if (estacionId != null && estacionId.equals(320)) {  //Guatemala
            cltToolbar.addComponent(utils.vlContainer(tfTurnoRef));
        } else {
//            cltToolbar.addComponents(utils.vlContainer(cbxConfiguracion), utils.vlContainer(lblHoraConf));
            cltToolbar.addComponents(utils.vlContainer(cbxSchedule)); //, utils.vlContainer(lblHoraConf));
        }
        cltToolbar.addComponents(utils.vlContainer(cbxTurno));

        VerticalLayout vltEmployee = utils.buildVertical("vltEmployee", false, false, true, false, null);
        vltEmployee.addComponents(tblEmployeePump, btnAddEmpPump);
        vltEmployee.setComponentAlignment(btnAddEmpPump, Alignment.MIDDLE_CENTER);

        CssLayout cltTable = new CssLayout(utils.vlContainer(tablePrecio), utils.vlContainer(vltEmployee));
        cltTable.setSizeUndefined();
        cltTable.setSizeUndefined();
        Responsive.makeResponsive(cltTable);

        CssLayout cltButton = new CssLayout(utils.vlContainer(btnGuardar), utils.vlContainer(btnModificar));
        cltButton.setSizeUndefined();
        cltButton.setWidth(100f, Unit.PERCENTAGE);
        Responsive.makeResponsive(cltButton);

        content.addComponents(cltInfo, cltToolbar, cltTable, cltButton);
        defineInitialCountryStation();
    }

    private void getAllData() {
        SvcTurno svcTurno = new SvcTurno();
        allCountries = svcTurno.getAllPaises();
        uniqueStation = svcTurno.getUniqueStation(user.getUsuarioId());

        //determinar si la estacion tiene configuracion de autoservicio.
//        estacion = (user.getEstacionLogin() != null)
//                ? user.getEstacionLogin() : ((estacion != null) ? estacion : new Estacion());
//        List<EstacionConfHead> configs = svcTurno.getConfiguracionHeadByEstacionid(estacion.getEstacionId(), false);
//        List<EstacionConfHead> configs = svcTurno.getConfiguracionHeadByEstacionidHorario(estacion.getEstacionId(), false);
//        for (EstacionConfHead ech : configs) {
//            for (EstacionConf ecf : ech.getEstacionConf()) {
//                if (ecf.getTipodespachoId() == 1) { //autoservicio
//                    showAutoservicio = true;
//                    break;
//                }
//            }
//        }
//        contConfigs = new ListContainer<EstacionConfHead>(EstacionConfHead.class, configs);
//        combustibles = svcTurno.getCombustiblesByEstacionid(estacion.getEstacionId());
//        ultimoDia = (ultimoDia == null) ? svcTurno.getUltimoDiaByEstacionid(estacion.getEstacionId()) : ultimoDia;
//        if (dia == null || dia.getFecha() == null) {    //La primera vez
//            dia = svcTurno.getDiaActivoByEstacionid(estacion.getEstacionId());
////            dia.setEstadoId((dia.getEstadoId() == null) ? 0 : dia.getEstadoId());  //Para las validaciones de determinarPermisos
//            dia = (dia.getEstadoId() == null) ? ultimoDia : dia;
//        }
//        ultimoTurno = (ultimoTurno == null) ? svcTurno.getUltimoTurnoByEstacionid(estacion.getEstacionId()) : ultimoTurno;
////        ultimoTurno.setEstadoId((ultimoTurno.getEstadoId() != null) ? ultimoTurno.getEstadoId() : 0); //No hay abierto o No existe turno alguno
//        //Solo puede haber un turno activo para cada estacion
//        if (turno == null || turno.getTurnoId() == null) {
//            turno = svcTurno.getTurnoActivoByEstacionid(estacion.getEstacionId());
////            turno.setEstadoId((turno.getEstadoId() != null) ? turno.getEstadoId() : 0); //No hay abierto o No existe turno alguno
//            turno = (turno.getEstadoId() == null) ? ultimoTurno : turno;
//        }
//        precios = svcTurno.getPreciosByTurnoid((turno.getTurnoId() != null) ? turno.getTurnoId() : ultimoTurno.getTurnoId());
//
//        for (Producto p : combustibles) {
//            for (Precio pre : precios) {
//                if (p.getProductoId().equals(pre.getProductoId())) {
//                    if (pre.getTipodespachoId() == 1) { //auto
//                        p.setPriceAS(pre.getPrecio());
//                    } else if (pre.getTipodespachoId() == 2) {  //completo
//                        p.setPriceSC(pre.getPrecio());
//                    }
//                }
//            }
//        }
//        ctrTurnos = new ListContainer(Turno.class, svcTurno.getTurnosByEstacionidDiaNolectura(estacion.getEstacionId(), dia.getFecha()));
//        buildComboboxConfiguration();
//        if (user.getPaisLogin() == null && cbxCountry.getItemIds().isEmpty()) {
//            cbxCountry.setContainerDataSource(new ListContainer<>(Pais.class, svcTurno.getAllPaises()));
//        }
//        List<TurnoEmpleadoBomba> listTurnoEmpBom = svcTurno.getTurnoEmpBombaByTurnoid((turno.getTurnoId() != null) ? turno.getTurnoId() : ultimoTurno.getTurnoId());
//        listEmployees = svcTurno.getEmpleados(true);
//        listPump = svcTurno.getbo
        svcTurno.closeConnections();

        bcrPrecios.setBeanIdProperty("productoId");
        bcrPrecios.removeAllItems();
//        bcrPrecios.addAll(combustibles);
        bcrEmpPump.setBeanIdProperty("fakeId");
        bcrEmpPump.removeAllItems();
//        bcrEmpPump.addAll(listTurnoEmpBom);

    }

    private void buildTables() {
        tablePrecio.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tablePrecio.addStyleName(ValoTheme.TABLE_COMPACT);
        tablePrecio.setContainerDataSource(bcrPrecios);
        tablePrecio.addGeneratedColumn("colSC", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Property prop = source.getItem(itemId).getItemProperty("priceSC"); //Atributo del bean
                TextField tfdSC = new TextField(utils.getPropertyFormatterDouble(prop));
                tfdSC.addStyleName("align-right");
                tfdSC.setNullRepresentation("0.00");
                tfdSC.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                    }
                });
                return tfdSC;
            }
        });
        tablePrecio.addGeneratedColumn("colAS", new Table.ColumnGenerator() {
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
        tablePrecio.setVisibleColumns(visCols);
        tablePrecio.setColumnHeaders(colHeads);
        tablePrecio.setHeight("250px");
        Responsive.makeResponsive(tablePrecio);

        tblEmployeePump = utils.buildTable("Empleado - bomba:", 200f, 200f, bcrEmpPump, new String[]{"fakeId"}, new String[]{"Id"});
        tblEmployeePump.addGeneratedColumn("colEmployee", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("employee");  //bean's property
                ComboBox result = utils.buildCombobox("", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<Empleado>(Empleado.class, listEmployees));
                result.setPropertyDataSource(pro);
                result.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        
                    }
                });
                return result;
            }
        });
        tblEmployeePump.addGeneratedColumn("colPump", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("pump");  //bean's property
                ComboBox result = utils.buildCombobox("", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(Bomba.class, listPump));
                result.setPropertyDataSource(pro);
                result.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        List<Bomba> listTemp = new ArrayList();
                        for (Bomba b : listPump) {
                            if (!b.equals((Bomba) result.getValue())) {
                                listTemp.add(b);
                            }
                        }
                        listPump.clear();
                        listPump.addAll(listTemp);

                    }
                });
                return result;
            }
        });
        tblEmployeePump.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_TINY);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        if (bcrEmpPump.getItem(itemId).getBean().getPump() != null) {
                            listPump.add(bcrEmpPump.getItem(itemId).getBean().getPump());
                        }
                        bcrEmpPump.removeItem(itemId);
                        tblEmployeePump.refreshRowCache();
                    }
                });
                return btnDelete;
            }
        });
        tblEmployeePump.setVisibleColumns(new Object[]{"colEmployee", "colPump", "colDelete"});
        tblEmployeePump.setColumnHeaders(new String[]{"Empleado", "Bomba", "Acción"});
        tblEmployeePump.setHeight("250px");
        tblEmployeePump.refreshRowCache();
    }

    private void buildButtons() {
        btnGuardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnGuardar.setIcon(FontAwesome.SAVE);
//        btnGuardar.setEnabled(crearTurno);    //habilitado (cerrado)
        btnGuardar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                user.setPaisLogin(pais);
                user.setEstacionLogin(estacion);

                if (user.getPaisLogin().getPaisId().equals(Constant.CAT_PAIS_GUATEMALA) && !tfTurnoRef.isValid()) {
                    Notification.show("ERROR:", "Todos los campos marcados son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                if (!user.getPaisLogin().getPaisId().equals(Constant.CAT_PAIS_GUATEMALA) && !cbxSchedule.isValid() // !cbxConfiguracion.isValid()
                        || (!dfdFecha.isValid() || dfdFecha.getValue() == null)) {
                    Notification.show("ERROR:", "Todos los campos marcados son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                if (ultimoDia.getFecha() != null && ultimoDia.getEstadoId() == 2 && ultimoDia.getFecha().equals(dfdFecha.getValue())) {
                    Notification.show("ERROR:", "Para la fecha elegida, existe un día cerrado.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                Producto prod;
                for (Integer pi : (List<Integer>) tablePrecio.getItemIds()) {
                    prod = (Producto) ((BeanItem) tablePrecio.getItem(pi)).getBean();
                    if ((prod.getPriceAS() - prod.getPriceSC()) >= 0) {
                        Notification.show("ERROR:", "Por favor revise los valores de precios para el producto *" + prod.getNombre() + "*", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }
                if (bcrEmpPump.getItemIds().isEmpty()) {
                    Notification.show("ERROR:", "Debe asociar almenos un empleado con una bomba.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                TurnoEmpleadoBomba teb;
                for (Integer itemId : bcrEmpPump.getItemIds()) {
                    teb = bcrEmpPump.getItem(itemId).getBean();
                    if (teb.getEmployee() == null || teb.getPump() == null) {
                        Notification.show("ERROR:", "Las asociaciones de bomba/empleado debe ser válidas.", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }

                SvcTurno svcTurno = new SvcTurno();
                boolean crearDia = false;
                Turno turno = new Turno(null, user.getEstacionLogin().getEstacionId(), user.getUsuarioId(), 1, user.getUsername(), dfdFecha.getValue(), user.getNombreLogin());
                if (user.getPaisLogin().getPaisId().equals(Constant.CAT_PAIS_GUATEMALA)) {
                    turno.setTurnoFusion(tfTurnoRef.getValue());
                } else {
//                    turno.setEstacionconfheadId(((EstacionConfHead) cbxConfiguracion.getValue()).getEstacionconfheadId());
                    turno.setEstacionconfheadId(estConfHead.getEstacionconfheadId());
                }
                //El dia se manda crear solo si es necesario
                if (ultimoDia.getFecha() == null || (ultimoDia.getFecha() != null && !ultimoDia.getFecha().equals(dfdFecha.getValue()))) {
                    dia = new Dia(user.getEstacionLogin().getEstacionId(), dfdFecha.getValue(), 1, user.getUsername(), user.getNombreLogin());
                    if (dfdFecha.isEnabled()) {
                        crearDia = true;
//                        svcTurno.doActionDia(Dao.ACTION_ADD, dia);
                    }
                }
                turno.setFecha(dfdFecha.getValue());
//                turno = svcTurno.doActionTurno(Dao.ACTION_ADD, turno);

                Precio precio;
                int conExito = 0;
                List<Precio> listPrecio = new ArrayList();
                for (Integer pi : (List<Integer>) tablePrecio.getItemIds()) {
                    prod = (Producto) ((BeanItem) tablePrecio.getItem(pi)).getBean();
                    if (showAutoservicio) {
                        precio = new Precio(turno.getTurnoId(), pi, 1, prod.getPriceAS(), user.getUsername(), user.getNombreLogin());  //AutoServicio
//                        precio = svcTurno.doActionPrecio(Dao.ACTION_ADD, precio);
                        listPrecio.add(precio);
                        conExito = (precio != null) ? conExito + 1 : conExito;
                    }
                    precio = new Precio(turno.getTurnoId(), pi, 2, prod.getPriceSC(), user.getUsername(), user.getNombreLogin());  //Servicio completo
//                    precio = svcTurno.doActionPrecio(Dao.ACTION_ADD, precio);
                    listPrecio.add(precio);
                    conExito = (precio.getTurnoId() != null) ? conExito + 1 : conExito;
                }
                List<TurnoEmpleadoBomba> listTurnoEmpPump = new ArrayList();
                for (Integer itemId : bcrEmpPump.getItemIds()) {
                    listTurnoEmpPump.add(bcrEmpPump.getItem(itemId).getBean());
                }
                turno.setHorarioId(((Horario) cbxSchedule.getValue()).getHorarioId());
                boolean everythingOk = svcTurno.doCreateTurn(crearDia, dia, turno, listPrecio, listTurnoEmpPump);
                svcTurno.closeConnections();

//                if (conExito > 0) {
                if (everythingOk) {
                    Notification notif = new Notification("ÉXITO:", "El registro de turno y precio se ha creado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    //notif.setStyleName("mystyle");
                    //notif.setIcon(new ThemeResource("img/reindeer.png"));
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PR_TURN.getViewName());
                } else {
                    Notification.show("ERROR:", "Ocurrió un error al guardar el precio.\n", Notification.Type.ERROR_MESSAGE);
                    turno = svcTurno.doActionTurno(Dao.ACTION_DELETE, turno);
                    return;
                }

            }
        });

        btnModificar.setStyleName(ValoTheme.BUTTON_PRIMARY);
//        editar = (editar && crearTurno) ? false : editar;
//        btnModificar.setEnabled(editar);    //habilitado
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
                    precio.setModificadoPor(user.getUsername());
                    precio.setModificadoPersona(user.getNombreLogin());
                    precio = service.doActionPrecio(Dao.ACTION_UPDATE, precio);
                    counter += (precio.getTurnoId() != null) ? 1 : 0;
                    if (showAutoservicio) {   //Guatemala
                        precio = new Precio(turno.getTurnoId(), prod.getProductoId(), 1, prod.getPriceAS(), null, null);   //Autoservicio
                        precio.setModificadoPor(user.getUsername());
                        precio.setModificadoPersona(user.getNombreLogin());
                        precio = service.doActionPrecio(Dao.ACTION_UPDATE, precio);
                    }
                }
                service.closeConnections();

                if (bcrPrecios.getItemIds().size() == counter) {
//                        Notification.show("El registro de turno y precio se ha creado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    Notification notif = new Notification("ÉXITO:", "El registro de precio se ha actualizado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    //notif.setStyleName("mystyle");
                    //notif.setIcon(new ThemeResource("img/reindeer.png"));
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
                    bcrEmpPump.addBean(new TurnoEmpleadoBomba(utils.getRandomNumberInRange(1, 1000), turnoId, 0, 0, user.getNombreLogin()));
                    tblEmployeePump.refreshRowCache();
                }
            }
        });

    }

    private void buildControls() {
//        buildLabelInfo();

        buildComboboxConfiguration();

        cbxCountry.setContainerDataSource(new ListContainer<>(Pais.class, allCountries));
        cbxCountry.setItemCaptionPropertyId("nombre");
        cbxCountry.setItemIconPropertyId("flag");
        cbxCountry.setNullSelectionAllowed(false);
        cbxCountry.addStyleName(ValoTheme.COMBOBOX_SMALL);
        //cbxCountry.setVisible(user.isGerente() || user.isAdministrativo());
        cbxCountry.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                pais = (Pais) cbxCountry.getValue();
                SvcEstacion svcEstacion = new SvcEstacion();
                List<Estacion> listStations = svcEstacion.getStationsByCountryUser(pais.getPaisId(), user.getUsuarioId());
//                Container ctrStation = new ListContainer<>(Estacion.class, listStations);
                svcEstacion.closeConnections();
                cbxEstacion.setContainerDataSource(new ListContainer<>(Estacion.class, listStations));
                //limpiar
//                contConfigs = new ListContainer<>(EstacionConfHead.class, new ArrayList());
//                cbxConfiguracion.setContainerDataSource(contConfigs);
                dfdFecha.setValue(null);
                ctrTurnos = new ListContainer<>(Turno.class, new ArrayList());
                cbxTurno.setContainerDataSource(ctrTurnos);
                bcrPrecios.removeAllItems();
                if (listStations.size()==1) {
                    cbxEstacion.setValue(listStations.get(0));
                }
            }
        });

        cbxEstacion.setItemCaptionPropertyId("nombre");
        cbxEstacion.setNullSelectionAllowed(false);
        cbxEstacion.setFilteringMode(FilteringMode.CONTAINS);
        cbxEstacion.setWidth("300px");
        cbxEstacion.addStyleName(ValoTheme.COMBOBOX_SMALL);
        //cbxEstacion.setVisible(user.isGerente() || user.isAdministrativo());
        cbxEstacion.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (cbxEstacion.getValue() != null) {
                    estacion = (Estacion) cbxEstacion.getValue();
                    SvcTurno svcTurno = new SvcTurno();
//                    List<Horario> listSchedules = svcTurno.getHorarioByEstacionid(estacion.getEstacionId());
                    cbxSchedule.setContainerDataSource(new ListContainer<>(Horario.class, listSchedules));
                    //Limpiar
                    dfdFecha.setValue(null);
                    ctrTurnos = new ListContainer<>(Turno.class, new ArrayList());
                    cbxTurno.setContainerDataSource(ctrTurnos);
                    bcrPrecios.removeAllItems();

                    ultimoDia = svcTurno.getUltimoDiaByEstacionid(estacion.getEstacionId());
                    if (dia == null || dia.getFecha() == null) {    //La primera vez
                        dia = svcTurno.getDiaActivoByEstacionid(estacion.getEstacionId());
                        dia = (dia.getEstadoId() == null) ? ultimoDia : dia;
                    }

                    ultimoTurno = svcTurno.getUltimoTurnoByEstacionid(estacion.getEstacionId());
                    //Solo puede haber un turno activo para cada estacion
                    if (turno == null || turno.getTurnoId() == null) {
                        turno = svcTurno.getTurnoActivoByEstacionid(estacion.getEstacionId());
                        turno = (turno.getEstadoId() == null) ? ultimoTurno : turno;
                    }
                    if (turno.getHorario() != null || ultimoTurno.getHorario() != null) {
                        Horario h = (turno.getHorario() != null) ? turno.getHorario() : ultimoTurno.getHorario();
                        for (Horario item : listSchedules) {
                            if (h.getHorarioId() == item.getHorarioId()) {
                                cbxSchedule.setValue(item);
                            }
                        }
                    }

                    List<TurnoEmpleadoBomba> listTurnoEmpBom = svcTurno.getTurnoEmpBombaByTurnoid((turno.getTurnoId() != null) ? turno.getTurnoId() : ultimoTurno.getTurnoId());
                    listEmployees = svcTurno.getEmpleados(true);
                    bcrEmpPump.addAll(listTurnoEmpBom);

                    svcTurno.closeConnections();
                    buildLabelInfo();
                }
            }
        });

        tfTurnoRef.setRequired(true);
        tfTurnoRef.setRequiredError("El código de turno es requerido");
        tfTurnoRef.setNullRepresentation("");
        tfTurnoRef.setMaxLength(6);

        dfdFecha.setRequired(true);
//        dfdFecha.setRequiredError("La fecha de creación/modificación es requerida");
        dfdFecha.setDateFormat("dd-MM-yyyy");
//        if (ultimoDia.getFecha()!=null) {
//            dfdFecha.setRangeStart(ultimoDia.getFecha());
//        }
        dfdFecha.setRangeEnd(Date.from(Instant.now()));
        dfdFecha.setLocale(new Locale("es", "ES"));
        dfdFecha.setLenient(true);
        dfdFecha.addStyleName(ValoTheme.DATEFIELD_SMALL);
        dfdFecha.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (dfdFecha.getValue() != null) {
                    SvcTurno svcTurno = new SvcTurno();
                    //se obtiene de base de datos pues necesitamos saber el estado.
                    dia = svcTurno.getDiaByEstacionidFecha(estacion.getEstacionId(), dfdFecha.getValue());
//                    dia.setEstadoId((dia.getEstadoId() == null) ? 0 : dia.getEstadoId());  //Para las validaciones iniciales de buildControls()
                    dia.setFecha((dia.getFecha() == null) ? dfdFecha.getValue() : dia.getFecha());  //Para las validaciones iniciales de buildControls()
                    List<Turno> listTurno = svcTurno.getTurnosByEstacionidDiaNolectura(estacion.getEstacionId(), dfdFecha.getValue());
//                    listTurno = (!listTurno.isEmpty()) 
//                            ? listTurno : svcTurno.getTurnosByEstacionidDiaNolectura(estacion.getEstacionId(), ultimoDia.getFecha());
                    svcTurno.closeConnections();
                    ctrTurnos = new ListContainer<>(Turno.class, listTurno);
                    cbxTurno.setContainerDataSource(ctrTurnos);
                    if (!ctrTurnos.getItemIds().isEmpty()) {
                        turno = (Turno) ((ArrayList) ctrTurnos.getItemIds()).get(listTurno.size() - 1);
                        cbxTurno.setValue(turno);
//                        getDataPrecios();
                    } else {
                        //La siguiente validacion es importante pues permite a un usuario crear un nuevo turno cuando es la PRIMERISIMA vez que este se crea para una estacion.
                        if (ultimoDia.getFecha() != null) {
                            bcrPrecios.removeAllItems();
                        }
                        turno = new Turno();
//                        turno.setEstadoId((turno.getEstadoId() != null) ? turno.getEstadoId() : 0);
                    }
                    ctrTurnos = new ListContainer(Turno.class, svcTurno.getTurnosByEstacionidDiaNolectura(estacion.getEstacionId(), dia.getFecha()));
                    getDataPrecios();

//                    if (!listTurno.isEmpty()) {
//                        cbxTurno.setValue( (Turno) ((ArrayList) ctrTurnos.getItemIds()).get(listTurno.size() - 1) );
//                    }
                    //Limpiar
//                    contConfigs = new ListContainer<EstacionConfHead>(EstacionConfHead.class, new ArrayList());
//                    cbxConfiguracion.setContainerDataSource(contConfigs);
//                    bcrPrecios.removeAllItems();
//                    buildControls();
                    determinarPermisos();
//                    getAllData();
                }
            }
        });
//        if (dia != null && dia.getFecha() != null) {
//            dfdFecha.setValue(dia.getFecha());
//        } else if (dfdFecha != null) {
//            dfdFecha.setValue(new Date());
//        }

        cbxTurno.setContainerDataSource(ctrTurnos);
        cbxTurno.setItemCaptionPropertyId("nombre");
        cbxTurno.setNullSelectionAllowed(false);
        cbxTurno.addStyleName(ValoTheme.COMBOBOX_SMALL);
//        cbxTurno.setEnabled(explorar);    //habilitado
        cbxTurno.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                turno = (Turno) cbxTurno.getValue();
                if (turno != null) {
                    getDataPrecios();
                    determinarPermisos();
//                    int estacionConfheadId;
//                    for (Object item : contConfigs.getItemIds()) {
//                        estacionConfheadId = ((EstacionConfHead) item).getEstacionconfheadId();
//                        if (estacionConfheadId == turno.getEstacionconfheadId()) {
//                            cbxConfiguracion.setValue((EstacionConfHead) item);
//                        }
//                    }
                    SvcTurno service = new SvcTurno();
                    bcrEmpPump.removeAllItems();
                    bcrEmpPump.addAll(service.getTurnoEmpBombaByTurnoid(turno.getTurnoId()));
                    service.closeConnections();
                }
            }
        });
        cbxTurno.setValue(turno);

        cbxSchedule = utils.buildCombobox("Horarios:", "nombreHoras", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(Horario.class, listSchedules));
        cbxSchedule.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (cbxSchedule.getValue() != null) {
                    SvcTurno service = new SvcTurno();
                    List<EstacionConfHead> configs = service.getConfiguracionHeadByEstacionidHorario(estacion.getEstacionId(), ((Horario) cbxSchedule.getValue()).getHorarioId());
                    estConfHead = configs.get(0);
                    service.closeConnections();
                    for (EstacionConfHead ech : configs) {
                        for (EstacionConf ecf : ech.getEstacionConf()) {
                            if (ecf.getTipodespachoId() == 1) { //autoservicio
                                showAutoservicio = true;
                                break;
                            }
                        }
                    }
                    contConfigs = new ListContainer<EstacionConfHead>(EstacionConfHead.class, configs);
                    buildComboboxConfiguration();
                    nuevoMetodoConfhead(configs.get(0));    //solo traera 1
                }
            }
        });

//        determinarPermisos();
    }

    private void determinarPermisos() {
        boolean explorar = false, editar = false, crearTurno = false;

//        if (dia.getEstadoId() == null && turno.getEstadoId() == null && dia.getFecha()!=null && ultimoDia.getFecha()!=null
//                && (dia.getFecha().equals(ultimoDia.getFecha()) || dia.getFecha().after(ultimoDia.getFecha()))
//                ) {
//            explorar = true;
//        } else if (dia.getEstadoId() == null && turno.getEstadoId() == null  && dia.getFecha()!=null && ultimoDia.getFecha()!=null
//                && dia.getFecha().before(ultimoDia.getFecha())) {
//            explorar = true;
//        } else {
        if ((user.getRolLogin().equals(Constant.ROL_LOGIN_CAJERO) || user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR))
                && dia.getEstadoId() == null && turno.getEstadoId() == null) {
            //El dia elegido No existe aun en base de datos
            if (dia.getFecha() != null && ultimoDia.getFecha() != null && (dia.getFecha().after(ultimoDia.getFecha()) || dia.getFecha().equals(ultimoDia.getFecha()))) {
                explorar = crearTurno = true;
            } else if (ultimoDia.getFecha() == null) {
                //La primericima vez que se crea un turno y precio tendra que pasar por esta validacion que aplica solo para el cajero y supervisor, permite habilitar el boton  para Guardar
                explorar = crearTurno = true;
            } else {
                explorar = true;
            }
        } else if ((user.getRolLogin().equals(Constant.ROL_LOGIN_CAJERO) && dia.getEstadoId() == 2 && turno.getEstadoId() == 2)) {
            explorar = crearTurno = true;
        } else if (user.getRolLogin().equals(Constant.ROL_LOGIN_CAJERO) && dia.getEstadoId() == 1 && turno.getEstadoId() == 2) {
            explorar = crearTurno = true;
        } else if (user.getRolLogin().equals(Constant.ROL_LOGIN_CAJERO) && dia.getEstadoId() == 1 && turno.getEstadoId() == 1) {
            editar = true;
        } else if ((user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR) && dia.getEstadoId() == 2 && turno.getEstadoId() == 2)) {
            explorar = crearTurno = true;
        } else if (user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR) && dia.getEstadoId() == 1
                && turno.getEstadoId() != null && turno.getEstadoId() == 2) {
            explorar = editar = crearTurno = true;
        } else if (user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR) && dia.getEstadoId() == 1
                && turno.getEstadoId() != null && turno.getEstadoId() == 1) {
            editar = true;
        } else if (user.isAdministrativo()) {
            explorar = editar = true;
        } else if (user.isGerente()) {
            explorar = editar = true;
        }

        dfdFecha.setEnabled(explorar);   //habilitado
        cbxTurno.setEnabled(explorar);    //habilitado
        crearTurno = (crearTurno && dia.getFecha() != null) // && dia.getFecha().equals(ultimoDia.getFecha()))
                ? true : crearTurno;
        btnGuardar.setEnabled(crearTurno);    //habilitado (cerrado)
        editar = (editar && crearTurno) ? false : editar;
        btnModificar.setEnabled(editar);    //habilitado
    }

    private void getDataPrecios() {
        SvcTurno svcTurno = new SvcTurno();
        combustibles = svcTurno.getCombustiblesByEstacionid(estacion.getEstacionId());
        //La siguiente funcion, si el idTurno es null, entonces devuelve los ultimos precios de la estacion
        Integer turnoId = (turno.getTurnoId() != null) ? turno.getTurnoId() : ultimoTurno.getTurnoId();
        precios = svcTurno.getPreciosByTurnoid(turnoId);
        svcTurno.closeConnections();
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
    }

    private void buildComboboxConfiguration() {
//        cbxConfiguracion.setContainerDataSource(contConfigs);
//        cbxConfiguracion.setItemCaptionPropertyId("nombre");
//        cbxConfiguracion.setRequired(true);
//        cbxConfiguracion.setRequiredError("Elija una configuración");
//        cbxConfiguracion.setNullSelectionAllowed(false);
//        cbxConfiguracion.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                Integer[] showAutoFull = new Integer[]{0, 0};
//                showAutoservicio = false;
//                EstacionConfHead echSelected = ((EstacionConfHead) cbxConfiguracion.getValue());
////                if (echSelected!=null) {
//                    for (EstacionConf ecf : echSelected.getEstacionConf()) {
//                        if (ecf.getTipodespachoId() == 1 && showAutoFull[0] == 0) { //Auto
//                            showAutoFull[0] = 1;
//                            showAutoservicio = true;
//                        } else if (ecf.getTipodespachoId() == 2 && showAutoFull[1] == 0) { //Full
//                            showAutoFull[1] = 1;
//                        }
//                    }
////                }
//
//                Object[] visCols = new Object[]{""};
//                String[] colHeads = new String[]{""};
//                if (showAutoFull[0] == 1 && showAutoFull[1] == 1) {
//                    visCols = new Object[]{"nombre", "colAS", "colSC"};
//                    colHeads = new String[]{"Producto", "Autoservicio", "Servicio completo"};
//                } else if (showAutoFull[0] == 1) {
//                    visCols = new Object[]{"nombre", "colAS"};
//                    colHeads = new String[]{"Producto", "Autoservicio"};
//                } else if (showAutoFull[1] == 1) {
//                    visCols = new Object[]{"nombre", "colSC"};
//                    colHeads = new String[]{"Producto", "Servicio completo"};
//                }
//                tablePrecio.setVisibleColumns(visCols);
//                tablePrecio.setColumnHeaders(colHeads);
////                lblHoraConf.setValue("Horario: " + echSelected.getHoraInicio() + " - " + echSelected.getHoraFin());
//                if (turno.getTurnoId() == null || ultimoTurno.getTurnoId() == null) {
//                    SvcTurno service = new SvcTurno();
//                    listPump = service.getBombasByEstacionConfheadId(echSelected.getEstacionconfheadId());
//                    bcrEmpPump.removeAllItems();
//                    service.closeConnections();
//                }
//            }
//        });
    }

    private void nuevoMetodoConfhead(EstacionConfHead echSelected) {
        Integer[] showAutoFull = new Integer[]{0, 0};
        showAutoservicio = false;
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
        tablePrecio.setVisibleColumns(visCols);
        tablePrecio.setColumnHeaders(colHeads);
        lblHoraConf.setValue("Horario: " + echSelected.getHoraInicio() + " - " + echSelected.getHoraFin());
        if (turno.getTurnoId() == null || ultimoTurno.getTurnoId() == null) {
            SvcTurno service = new SvcTurno();
            listPump = service.getBombasByEstacionConfheadId(echSelected.getEstacionconfheadId(),0);
            bcrEmpPump.removeAllItems();
            service.closeConnections();
        }
    }

    private void buildLabelInfo() {
        SimpleDateFormat sdf_ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");
        String fechaString = (ultimoDia.getFecha() != null) ? sdf_ddmmyyyy.format(ultimoDia.getFecha()) : "INEXISTENTE";
        String estadoName = (ultimoDia.getEstadoId() != null && ultimoDia.getEstadoId() == 1)
                ? "ABIERTO" : ((ultimoDia.getEstadoId() != null && ultimoDia.getEstadoId() == 2) ? "CERRADO" : "SIN ESTADO");
        lblUltimoDía.setValue("Último día: " + fechaString + " (" + estadoName + ")");
        lblUltimoDía.addStyleName(ValoTheme.LABEL_BOLD);
        lblUltimoDía.addStyleName(ValoTheme.LABEL_H3);
        lblUltimoDía.addStyleName(ValoTheme.LABEL_COLORED);
        lblUltimoDía.setWidth("35%");

        fechaString = (ultimoTurno.getTurnoId() != null) ? "Turno ".concat(ultimoTurno.getTurnoId().toString()) : "INEXISTENTE";
        estadoName = (ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 1)
                ? "ABIERTO" : ((ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 2) ? "CERRADO" : "SIN ESTADO");
        lblUltimoTurno.setValue("Último turno: " + fechaString + " (" + estadoName + ")");
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_BOLD);
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_H3);
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_COLORED);
        lblUltimoTurno.setSizeUndefined();

        lblHoraConf.addStyleName(ValoTheme.LABEL_BOLD);
        lblHoraConf.addStyleName(ValoTheme.LABEL_SUCCESS);
    }

    private void defineInitialCountryStation() {
        if (uniqueStation != null) {
            allCountries.forEach(item -> {
                if (item.getPaisId().toString().equals(uniqueStation[0])) {
                    cbxCountry.setValue(item);
                    return;
                }
            });
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
