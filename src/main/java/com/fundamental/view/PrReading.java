package com.fundamental.view;

import com.fundamental.model.Bomba;
import com.fundamental.model.BombaEmpleadoEstacion;
import com.fundamental.model.Dia;
import com.sisintegrados.generic.bean.Empleado;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.services.Dao;
import com.fundamental.model.dto.DtoLectura;
import com.sisintegrados.generic.bean.Lectura;
import com.fundamental.model.LecturaDetalle;
import com.fundamental.model.Lecturafinal;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Parametro;
import com.fundamental.model.Producto;
import com.fundamental.model.Turno;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoPrecio;
import com.fundamental.services.SvcEstacion;
import com.fundamental.services.SvcReading;
import com.fundamental.services.SvcTurno;
import com.fundamental.utils.Constant;
import com.fundamental.utils.Mail;
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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import static org.eclipse.persistence.jpa.rs.util.JPARSLogger.exception;
import org.vaadin.maddon.ListContainer;
import org.vaadin.ui.NumberField;

/**
 * @author Henry Barrientos
 */
public class PrReading extends VerticalLayout implements View {

    DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00;-#");
    VerticalLayout root = new VerticalLayout();
    //TODO: El turno no debiera ser una eleccion, ya que solo puede haber un turno Activo, mejor colocar como un dato informativo
    Button btnSave = new Button("Guardar lecturas"),
            //            btnAll, btnNone, 
            testButton;
    Label labelStation, labelTurno, labelCreadopor,
            lblUltimoDía = new Label("Último día:"),
            lblUltimoTurno = new Label("Último turno:");
    //TextField tfdNameSeller, tfdNameChief;
    DateField dfdFecha = new DateField("Fecha:");
    ComboBox cbxEmpleado,
            cbxCountry = new ComboBox("País:"),
            cbxEstacion = new ComboBox("Estación:"),
            cbxTurno = new ComboBox("Turno:");
    Container contTurnos = new ListContainer<>(Turno.class, new ArrayList()),
            ctrEmpleado;
    Upload upload;
    Table tableElectronicas, tableBombas, tableManuales,
            tablePrecios = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("precioAS") || colId.equals("precioSC")) {
                return numberFmt.format(property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Utils utils = new Utils();
    BeanContainer<Integer, Bomba> bcrBombas = new BeanContainer<Integer, Bomba>(Bomba.class);
    BeanContainer<Integer, DtoLectura> bcrElectronicas = new BeanContainer<Integer, DtoLectura>(DtoLectura.class);
    BeanContainer<Integer, DtoLectura> bcrManuales = new BeanContainer<Integer, DtoLectura>(DtoLectura.class);
    BeanContainer<Integer, DtoPrecio> bcrPrecios = new BeanContainer<Integer, DtoPrecio>(DtoPrecio.class);
    Usuario user;
    List<Producto> productos;
    List<Bomba> allBombas, bombasInhabilitadas;
    List<DtoPrecio> precios;
    List<DtoLectura> lecturasTurnoActivoElectronicas, lecturasTurnoActivoMecanicas;
    List<Empleado> listEmpleados;
    List<Pais> allCountries;
    Double totalDiferencia = 0D;
    String action, tmpString;
    List<Lecturafinal> lecturasUltimaMecanicas, lecturasUltimaElectronicas;
    Pais pais;
    Estacion estacion;
    Turno turno, ultimoTurno;
    Dia dia, ultimoDia;
    File tempFile;
    String[] uniqueStation;
    public TextField txtNumeroCaso;
    Double cantCalibrar;

    public PrReading() {
        setSizeFull();
        DashboardEventBus.register(this);

        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());

        action = Dao.ACTION_ADD;

        HorizontalLayout hlTitle = utils.buildHeader("Ingreso lecturas", true, true);
        addComponent(hlTitle);

        getAllData();
        buildFilters();
        buildControls();

        defineTableBombas();
        tableElectronicas = new Table("Lecturas electrónicas");
//        Integer paisId = (pais == null) ? null : pais.getPaisId();
//        if (paisId != null && paisId == Constant.CAT_PAIS_GUATEMALA) {
        buildTableElectronicas();
//        }
        buildTableManuales();

//        root.setSizeFull();
        root.setId("root");
        root.setSizeUndefined();
        root.setWidth(100f, Unit.PERCENTAGE);
        root.setMargin(true);
        root.setSpacing(true);
//        root.addStyleName("dashboard-view");
        root.setResponsive(true);
        //buildTablePrecio();

        HorizontalLayout hlHeader = buildLabels();

//        HorizontalLayout cltInfo = utils.buildHorizontal("cltInfo", false, true, true, false);
//        cltInfo.setSizeUndefined();
//        cltInfo.addComponents(lblUltimoDía, lblUltimoTurno);
//
//        HorizontalLayout cltToolbar = utils.buildHorizontal("cltToolbar", false, true, true, false);
//        cltToolbar.setSizeUndefined();
//        cltToolbar.addComponents(cbxCountry, cbxEstacion, dfdFecha, cbxTurno);
//
        HorizontalLayout hlContent = utils.buildHorizontal("hlContent", false, true, true, false);
        hlContent.setSizeUndefined();

        VerticalLayout vlBombas = utils.buildVertical("vlBombas", false, true, true, true, null);
//        HorizontalLayout hlBombasButton = utils.buildHorizontal("hlBombasButton", false, true, true, true);
//        hlBombasButton.addComponents(btnAll, btnNone);
//        hlBombasButton.setComponentAlignment(btnAll, Alignment.BOTTOM_RIGHT);
//        hlBombasButton.setComponentAlignment(btnNone, Alignment.BOTTOM_LEFT);
        txtNumeroCaso = utils.buildTextField("Número Caso:", "", false, 50, true, ValoTheme.TEXTFIELD_SMALL);
        vlBombas.addComponents(cbxEmpleado, tableBombas);//, hlBombasButton);
        vlBombas.setExpandRatio(tableBombas, 0.85f);

//        vlBombas.setExpandRatio(hlBombasButton, 0.15f);
//        hlContent.addComponents(vlBombas);
//        if (paisId != null && paisId == Constant.CAT_PAIS_GUATEMALA) {
        hlContent.addComponents(tableElectronicas);
//        }
        hlContent.addComponents(tableManuales);
//        hlContent.setExpandRatio(vlBombas, 0.18f);
        hlContent.setExpandRatio(tableManuales, 0.41f);
//        if (paisId != null && paisId == Constant.CAT_PAIS_GUATEMALA) {
        hlContent.setExpandRatio(tableElectronicas, 0.41f);
//        }

        VerticalLayout vlButton = new VerticalLayout();
        vlButton.addComponent(btnSave);
        vlButton.setSizeUndefined();
        vlButton.setMargin(new MarginInfo(true, false, false, false));
        Responsive.makeResponsive(vlButton);
        vlButton.setComponentAlignment(btnSave, Alignment.MIDDLE_LEFT);

        CssLayout cltButtons = new CssLayout(utils.vlContainer(upload),
                vlButton);
        cltButtons.setResponsive(true);
        cltButtons.setVisible(true);
        upload.setVisible(false);
        txtNumeroCaso = utils.buildTextField("Caso Help Desk:", "", false, 15, true, ValoTheme.TEXTFIELD_SMALL);

        HorizontalLayout h = new HorizontalLayout();
        h.addComponent(cbxEmpleado);
        h.addComponent(txtNumeroCaso);
        h.setSpacing(true);
        txtNumeroCaso.setVisible(false);

        root.addComponents(hlHeader,
                //                cltInfo,
                //                cltToolbar,
                h,
                hlContent, //b tnSave
                cltButtons);
        root.setExpandRatio(hlHeader, 0.30f);
//        root.setExpandRatio(cltInfo, 0.05f);
//        root.setExpandRatio(cltToolbar, 0.10f);
        root.setExpandRatio(hlContent, 0.65f);
//        root.setExpandRatio(btnSave, 0.05f);
        root.setExpandRatio(cltButtons, 0.05f);

        Panel panel = new Panel();
        panel.setContent(root);
        panel.setSizeFull();

        addComponent(panel);
        setExpandRatio(hlTitle, 0.15f);
        setExpandRatio(panel, 0.85f);

        Responsive.makeResponsive(root);
//        setExpandRatio(root, 1);
        setExpandRatio(panel, 1);
        defineInitialCountryStation();
    }

    private void getAllData() {
        SvcReading service = new SvcReading();

        pais = (user.getPaisLogin() != null)
                ? user.getPaisLogin() : ((pais != null) ? pais : new Pais());
//        if (//user.getPaisLogin() == null && 
//                contPais.getItemIds().isEmpty()) {
        allCountries = service.getAllPaises();
//        }

        estacion = (user.getEstacionLogin() != null)
                ? user.getEstacionLogin() : ((estacion != null) ? estacion : new Estacion());

//        ultimoDia = (ultimoDia == null) ? service.getUltimoDiaByEstacionid(estacion.getEstacionId()) : ultimoDia;
////        ultimoDia.setEstadoId((ultimoDia.getEstadoId() == null) ? 0 : ultimoDia.getEstadoId());  //Para las validaciones de determinarPermisos
//        if (dia == null || dia.getFecha() == null) {    //La primera vez
//            dia = service.getDiaActivoByEstacionid(estacion.getEstacionId());
////            dia.setEstadoId((dia.getEstadoId() == null) ? 0 : dia.getEstadoId());  //Para las validaciones de determinarPermisos
//            dia = (dia.getEstadoId() == null) ? ultimoDia : dia;
//        }
//
//        ultimoTurno = (ultimoTurno == null) ? service.getUltimoTurnoByEstacionid(estacion.getEstacionId()) : ultimoTurno;
////        ultimoTurno.setEstadoId((ultimoTurno.getEstadoId() != null) ? ultimoTurno.getEstadoId() : 0); //No hay abierto o No existe turno alguno
//        //Solo puede haber un turno activo para cada estacion
//        if (turno == null || turno.getTurnoId() == null) {
//            turno = service.getTurnoActivoByEstacionid(estacion.getEstacionId());
////            turno.setEstadoId((turno.getEstadoId() != null) ? turno.getEstadoId() : 0); //No hay abierto o No existe turno alguno
//            turno = (turno.getEstadoId() == null) ? ultimoTurno : turno;
//        }
//        List<Turno> turnosTemp = service.getTurnosByEstacionid(estacion.getEstacionId());
//        List<Turno> misTurnos = new ArrayList();
//        for (Turno t : turnosTemp) {
//            if (t.getTurnoId() == null || !t.getTurnoId().equals(turno.getTurnoId())) {
//                misTurnos.add(t);
//            }
//        }
//        contTurnos = new ListContainer<>(Turno.class, misTurnos);
//        allBombas = service.getBombasByEstacionidTurnoid(estacion.getEstacionId(), turno.getTurnoId());
//        bombasInhabilitadas = service.getBombasConLectura(estacion.getEstacionId(), turno.getTurnoId());
//        productos = service.getCombustiblesByEstacionid(estacion.getEstacionId());
//        precios = service.getPrecioByTurnoid(turno.getTurnoId());
//        if (pais!=null && pais.getPaisId() != null && pais.getPaisId() == Constant.CAT_PAIS_GUATEMALA) {
//            lecturasTurnoActivoElectronicas = service.getLecturasByTurnoid(turno.getTurnoId(), "E");
//        }
//        lecturasTurnoActivoMecanicas = service.getLecturasByTurnoid(turno.getTurnoId(), "M");
//        lecturasUltimaMecanicas = service.getLecturasfinales(estacion.getEstacionId(), "M");
//        lecturasUltimaElectronicas = service.getLecturasfinales(estacion.getEstacionId(), "E");
        listEmpleados = new ArrayList(); //service.getEmpleadosByTurnoid(turno.getTurnoId());

        uniqueStation = service.getUniqueStation(user.getUsuarioId());
        service.closeConnections();

        bcrBombas.setBeanIdProperty("id");
        bcrBombas.removeAllItems();
//        bcrBombas.addAll(allBombas);
        bcrPrecios.setBeanIdProperty("id");
        bcrPrecios.removeAllItems();
//        bcrPrecios.addAll(precios);
//        determinarColumnasPrecios();
        bcrElectronicas.setBeanIdProperty("bombaIdDto");
        bcrManuales.setBeanIdProperty("bombaIdDto");

//        if (precios.isEmpty()) {
//            Notification.show("NO existen precios configurados.", Notification.Type.WARNING_MESSAGE);
//            return;
//        }
    }

//    private void determinarColumnasPrecios() {
//        if (tablePrecios!=null && tablePrecios.getContainerDataSource() != null) {
//            Object[] columns = new Object[]{"productoName"};
//            String[] titles = new String[]{"Producto"};
//            Align[] alignments = new Align[]{Align.LEFT};
//            if (!precios.isEmpty()) {
//                DtoPrecio dpo = precios.get(0);
//                if (dpo.getPrecioAS() > 0 && dpo.getPrecioSC() > 0) {
//                    columns = new Object[]{"productoName", "precioAS", "precioSC"};
//                    titles = new String[]{"Producto", "Autoservicio", "Completo"};
//                    alignments = new Align[]{Align.LEFT, Align.RIGHT, Align.RIGHT};
//                } else if (dpo.getPrecioAS() > 0) {
//                    columns = new Object[]{"productoName", "precioAS"};
//                    titles = new String[]{"Producto", "Autoservicio"};
//                    alignments = new Align[]{Align.LEFT, Align.RIGHT};
//                } else if (dpo.getPrecioSC() > 0) {
//                    columns = new Object[]{"productoName", "precioSC"};
//                    titles = new String[]{"Producto", "Completo"};
//                    alignments = new Align[]{Align.LEFT, Align.RIGHT};
//                }
//            }
//            tablePrecios.setVisibleColumns(columns);
//            tablePrecios.setColumnHeaders(titles);
//            tablePrecios.setColumnAlignments(alignments);
//        }
//    }
    private void buildFilters() {
//        buildLabelInfo();

        cbxCountry.setContainerDataSource(new ListContainer<>(Pais.class, allCountries));
        cbxCountry.setItemCaptionPropertyId("nombre");
        cbxCountry.setItemIconPropertyId("flag");
        cbxCountry.setNullSelectionAllowed(false);
//        cbxCountry.setVisible(user.isGerente() || user.isAdministrativo());
        cbxCountry.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxCountry.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                pais = (Pais) cbxCountry.getValue();
                SvcEstacion svcEstacion = new SvcEstacion();
                List<Estacion> listStations = svcEstacion.getStationsByCountryUser(pais.getPaisId(), user.getUsuarioId());
                svcEstacion.closeConnections();
                cbxEstacion.setContainerDataSource(new ListContainer<>(Estacion.class, listStations));
                //limpiar
                dfdFecha.setValue(null);
                contTurnos = new ListContainer<Turno>(Turno.class, new ArrayList());
                cbxTurno.setContainerDataSource(contTurnos);
                cbxTurno.setValue(null);
                bcrPrecios.removeAllItems();
                if (listStations.size() == 1) {
                    cbxEstacion.setValue(listStations.get(0));
                }
            }
        });

        cbxEstacion.setItemCaptionPropertyId("nombre");
        cbxEstacion.setNullSelectionAllowed(false);
        cbxEstacion.setFilteringMode(FilteringMode.CONTAINS);
        cbxEstacion.setWidth("300px");
//        cbxEstacion.setVisible(user.isGerente() || user.isAdministrativo());
        cbxEstacion.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxEstacion.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                estacion = (Estacion) cbxEstacion.getValue();
                dfdFecha.setValue(null);
                contTurnos = new ListContainer<>(Turno.class, new ArrayList());
                cbxTurno.setContainerDataSource(contTurnos);
                cbxTurno.setValue(null);
                bcrPrecios.removeAllItems();
                bcrManuales.removeAllItems();

                SvcReading service = new SvcReading();
                ultimoDia = (ultimoDia == null) ? service.getUltimoDiaByEstacionid(estacion.getEstacionId()) : ultimoDia;
                if (dia == null || dia.getFecha() == null) {    //La primera vez
                    dia = service.getDiaActivoByEstacionid(estacion.getEstacionId());
                    dia = (dia.getEstadoId() == null) ? ultimoDia : dia;
                }

                ultimoTurno = (ultimoTurno == null) ? service.getUltimoTurnoByEstacionid(estacion.getEstacionId()) : ultimoTurno;
                //Solo puede haber un turno activo para cada estacion
                if (turno == null || turno.getTurnoId() == null) {
                    turno = service.getTurnoActivoByEstacionid(estacion.getEstacionId());
                    turno = (turno.getEstadoId() == null) ? ultimoTurno : turno;
                }

                List<Turno> turnosTemp = service.getTurnosByEstacionid(estacion.getEstacionId());
                List<Turno> misTurnos = new ArrayList();
                for (Turno t : turnosTemp) {
                    if (turno.getTurnoId() != null) {
                        if (t.getTurnoId() == null || !t.getTurnoId().equals(turno.getTurnoId())) {
                            misTurnos.add(t);
                        }
                    }
                }
                contTurnos = new ListContainer<>(Turno.class, misTurnos);

                service.closeConnections();
                buildLabelInfo();

                labelStation.setContentMode(ContentMode.HTML);
                labelStation.setValue("<p>Estación: <h2><b>" + estacion.getNombre() + "</b></h2></p>");
                Integer paisId = (pais == null) ? null : pais.getPaisId();
                String turnName = (paisId != null && paisId == Constant.CAT_PAIS_GUATEMALA) ? turno.getTurnoFusion() : turno.getTurnoId().toString();
                labelTurno.setContentMode(ContentMode.HTML);
                labelTurno.setValue("Turno: <h2><b>" + turnName + "</b></h2>");
                labelCreadopor.setContentMode(ContentMode.HTML);
                labelCreadopor.setValue("Creado por: <h2><b>" + turno.getCreadoPersona() + "</b></h2>");
            }
        });

        dfdFecha.setRequired(true);
        dfdFecha.setRequiredError("La fecha de creacion/modificaciones es requerida");
        dfdFecha.setDateFormat("dd-MM-yyyy");
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
                    dia.setFecha((dia.getFecha() == null) ? dfdFecha.getValue() : dia.getFecha());  //dia es siempre el mismo que lo seleccionado en el control
//                    List<Turno> listTurno = svcTurno.getTurnosByEstacionidDia_lectura(estacion.getEstacionId(), dfdFecha.getValue());
                    List<Turno> listTurno = svcTurno.getTurnosByEstacionidDiaNolectura(estacion.getEstacionId(), dfdFecha.getValue());
                    svcTurno.closeConnections();
                    contTurnos = new ListContainer<Turno>(Turno.class, listTurno);
                    cbxTurno.setContainerDataSource(contTurnos);
                    cbxTurno.setValue(null);
                    bcrPrecios.removeAllItems();
                    bcrBombas.removeAllItems();
                    bcrManuales.removeAllItems();
                    turno = new Turno();
                    if (!contTurnos.getItemIds().isEmpty()) {
                        int lastIndex = listTurno.size() - 1;
                        System.out.println("lastIndex " + lastIndex);
                        turno = (Turno) ((ArrayList) contTurnos.getItemIds()).get(lastIndex);
                        System.out.println("turno " + turno.toString());
                        cbxTurno.setValue(turno);
                        actionComboboxTurno();
                    }
                    determinarPermisos();
                }
            }
        });
//        dfdFecha.setValue(dia.getFecha());

        cbxTurno.setContainerDataSource(contTurnos);
        cbxTurno.setItemCaptionPropertyId("nombre");
        cbxTurno.setNullSelectionAllowed(false);
        cbxTurno.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxTurno.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                turno = (Turno) cbxTurno.getValue();
                actionComboboxTurno();
                buildTablePrecio();
            }
        });
        cbxTurno.setValue(turno);

        //tfdNameSeller = utils.buildTextField("Pistero:", "", false, 50, true, ValoTheme.TEXTFIELD_SMALL);
        //  txtNumeroCaso = utils.buildTextField("Número Caso:", "", false, 50, true, ValoTheme.TEXTFIELD_SMALL);
        //tfdNameChief = utils.buildTextField("Jefe de pista:", "", false, 50, true, ValoTheme.TEXTFIELD_SMALL);
//        determinarPermisos();
    }

    boolean editar = false;

    private void determinarPermisos() {
        boolean explorar = false, crearLectura = false;
        editar = false;
        if (dia.getEstadoId() == null && turno.getEstadoId() == null && dia.getFecha() == null && ultimoDia.getFecha() == null) {
            explorar = true;
        } else if (dia.getEstadoId() == null && turno.getEstadoId() == null && ultimoDia.getFecha() == null) {
            explorar = true;
        } else if (dia.getEstadoId() == null && turno.getEstadoId() == null && dia.getFecha() != null && ultimoDia.getFecha() != null
                && (dia.getFecha().equals(ultimoDia.getFecha()) || dia.getFecha().after(ultimoDia.getFecha()))) {
            explorar = crearLectura = true;
        } else if (dia.getEstadoId() == null && turno.getEstadoId() == null && dia.getFecha() != null && ultimoDia.getFecha() != null
                && dia.getFecha().before(ultimoDia.getFecha())) {
            explorar = true;
        } else if ((user.getRolLogin().equals(Constant.ROL_LOGIN_CAJERO) || user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR))
                && dia.getEstadoId() == 2 && turno.getEstadoId() == 2) {
            explorar = true;
        } else if ((user.getRolLogin().equals(Constant.ROL_LOGIN_CAJERO) || user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR))
                && dia.getEstadoId() == 1 //                && turno.getEstadoId() == 2
                ) {
            explorar = editar = crearLectura = true;
        } else if (user.isAdministrativo() || user.isGerente()) {
            explorar = editar = crearLectura = true;
        }
//        else if ((user.getRolLogin().equals(Constant.ROL_LOGIN_CAJERO) || user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR))
//                && dia.getEstadoId() == 1 && turno.getEstadoId() == 1) { editar = crearLectura = true; } 

        dfdFecha.setEnabled(explorar);   //habilitado
        cbxTurno.setEnabled(explorar);    //habilitado
        btnSave.setEnabled(crearLectura);    //habilitado (cerrado)
    }

    private void actionComboboxTurno() {
        if (turno != null) {
            SvcReading service = new SvcReading();
            allBombas = service.getBombasByEstacionidTurnoid(estacion.getEstacionId(), turno.getTurnoId());
            bombasInhabilitadas = service.getBombasConLectura(estacion.getEstacionId(), turno.getTurnoId());
            productos = service.getCombustiblesByEstacionid(estacion.getEstacionId());
            precios = service.getPrecioByTurnoid(turno.getTurnoId());
            lecturasTurnoActivoMecanicas = service.getLecturasByTurnoid(turno.getTurnoId(), "M");
            lecturasTurnoActivoElectronicas = service.getLecturasByTurnoid(turno.getTurnoId(), "E");
            lecturasUltimaMecanicas = service.getLecturasfinales(estacion.getEstacionId(), "M");
            lecturasUltimaElectronicas = service.getLecturasfinales(estacion.getEstacionId(), "E");
            listEmpleados = service.getEmpleadosByTurnoid(turno.getTurnoId());
            cbxEmpleado.setContainerDataSource(new ListContainer<>(Empleado.class, listEmpleados));
            service.closeConnections();
            bcrBombas.removeAllItems();
            bcrBombas.addAll(allBombas);
            bcrPrecios.removeAllItems();
            bcrPrecios.addAll(precios);
//determinarColumnasPrecios();
            bcrManuales.removeAllItems();
            determinarPermisos();
            buildTablePrecio();
        }
    }

    private void buildControls() {
        btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSave.setIcon(FontAwesome.SAVE);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {

//                List<Integer> listIds = (List<Integer>) tableBombas.getItemIds();
//                Bomba bomba;
//                boolean bombasSeleccionadas = false;
//                for (Integer id : listIds) {
////                        BeanItem<Bomba> item = (BeanItem) tableBombas.getItem(id);
//                    bomba = (Bomba) ((BeanItem) tableBombas.getItem(id)).getBean();
//                    if (bomba.getSelected()) {
//                        bombasSeleccionadas = true;
//                        break;
//                    }
//                }
//                if (!bombasSeleccionadas) {
//                    Notification.show("NO hay acciones que realizar.", Notification.Type.ERROR_MESSAGE);
//                    return;
//                }
                if ((pais.getPaisId() == Constant.CAT_PAIS_GUATEMALA && tableElectronicas.getItemIds().isEmpty())
                        || (pais.getPaisId() != Constant.CAT_PAIS_GUATEMALA && tableManuales.getItemIds().isEmpty())) {
                    Notification.show("ERROR:", "NO hay acciones que realizar.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                /*Se elimino validación de ingreso de Pistero y Jefe de Pista porque ya no se utilizaran*/
//                if (!tfdNameSeller.isValid() || tfdNameSeller.getValue().trim().isEmpty()
//                        || !tfdNameChief.isValid() || tfdNameChief.getValue().trim().isEmpty() || !cbxEmpleado.isValid()) {
//                    Notification.show("ERROR:", "Todos los campos marcados son requeridos.", Notification.Type.ERROR_MESSAGE);
//                    return;
//                }

                //Identificar lecturas finales que sean menores que la inicial
                DtoLectura dlectura;
                DtoLectura dlectura2;
                boolean existCalibration = false;
                for (Integer id : (List<Integer>) tableManuales.getItemIds()) {
                    dlectura = (DtoLectura) ((BeanItem) tableManuales.getItem(id)).getBean();
                    dlectura2 = (DtoLectura) ((BeanItem) tableElectronicas.getItem(id)).getBean();
                    if (dlectura.getmTotal() < 0) {
                        Notification notif = new Notification("ADVERTENCIA:", "Tiene un registro de ventas negativa (" + dlectura.getBomba() + " -> " + dlectura.getProducto() + ").", Notification.Type.WARNING_MESSAGE);
                        notif.setDelayMsec(4000);
                        notif.setPosition(Position.MIDDLE_CENTER);
                        notif.show(Page.getCurrent());
                        return;
                    }
                    if (dlectura.getmCalibracion() != null) {
                        if (dlectura.getmCalibracion() > 0) {
                            existCalibration = true;
                        }
                    }

                    Dao dao = new Dao();
                    Parametro parametro = dao.getParameterByName("LECTURAS_OBLIGATORIAS_ME");
                    if (parametro != null) {
                        if ("true".equals(parametro.getValor())) {
                            Double masmenos = dlectura2.geteFinal() - dlectura.getmFinal();
                            if (masmenos > 5 || masmenos < -5) {
                                Notification notif = new Notification("ADVERTENCIA:", "Valor Final no puede tener una diferencia de mayor a 5 o menor a -5 (" + dlectura.getBomba() + " -> " + dlectura.getProducto() + ").", Notification.Type.WARNING_MESSAGE);
                                notif.setDelayMsec(4000);
                                notif.setPosition(Position.MIDDLE_CENTER);
                                notif.show(Page.getCurrent());
                                return;
                            }
                        }
                    } else {
                        Notification.show("ADVERTENCIA:", "No se encontro el parametro de configuracion de Lecturas...", Notification.Type.WARNING_MESSAGE);
                        return;
                    }
                }

                /*ASG*/
                for (Integer id : (List<Integer>) tableElectronicas.getItemIds()) {
                    dlectura2 = (DtoLectura) ((BeanItem) tableElectronicas.getItem(id)).getBean();
                    /*ASG*/
                    if (dlectura2.getmCalibracion() != null) {
                        if (dlectura2.getmCalibracion() > 0) {
                            existCalibration = true;
                        }
                    }
                    /*FIn ASG*/
                }
//                if (existCalibration && tempFile != null) {
//                    Notification.show("ADVERTENCIA:", "Tiene por lo menos un dato de calibración, debe adjuntar un documento", Notification.Type.WARNING_MESSAGE);
//                    return;
//                }
                if (existCalibration && txtNumeroCaso.getValue().equals("")) {
                    Notification.show("ADVERTENCIA:", "Tiene por lo menos un dato de calibración, debe ingresar un numero de caso", Notification.Type.WARNING_MESSAGE);
                    return;
                }

                if (turno.getTurnoId() != null) {
                    Integer turnoId = turno.getTurnoId();
                    Lectura lectura = new Lectura();
//                    if (!txtNumeroCaso.getValue().equals("")) {
//                        lectura = new Lectura(null, estacion.getEstacionId(), turnoId, user.getUsername(), user.getNombreLogin(), txtNumeroCaso.getValue()/**
//                         * , tfdNameSeller.getValue(), tfdNameChief.getValue()*
//                         */
//                        );
//
//                    } else {
                    lectura = new Lectura(null, estacion.getEstacionId(), turnoId, user.getUsername(), user.getNombreLogin(), null/**
                     * , tfdNameSeller.getValue(), tfdNameChief.getValue()*
                     */
                    );
//                    }

                    lectura.setEmpleadoId(((Empleado) cbxEmpleado.getValue()).getEmpleadoId());
                    LecturaDetalle ldetalle;
                    boolean crearNuevaLectura = false;
//                    if (pais.getPaisId() == Constant.CAT_PAIS_GUATEMALA) {
                    for (Integer id : (List<Integer>) tableElectronicas.getItemIds()) {
                        dlectura = (DtoLectura) ((BeanItem) tableElectronicas.getItem(id)).getBean();
                        ldetalle = new LecturaDetalle(lectura.getLecturaId(), estacion.getEstacionId(), dlectura.getBombaId(), dlectura.getProductoId(), dlectura.getTipodespachoId(),
                                //                                "E", dlectura.geteInicial(), dlectura.geteFinal(), dlectura.geteVolumen(), null);
                                //                                "E", dlectura.geteInicial(), dlectura.geteFinal(), dlectura.geteVolumen(), dlectura.geteCalibracion());
                                "E", dlectura.geteInicial(), dlectura.geteFinal(), dlectura.geteVolumen(), dlectura.getmCalibracion());
                        if ((!crearNuevaLectura && dlectura.getEsNueva())) {
                            crearNuevaLectura = true;
                        }
                        ldetalle.setEsNueva(dlectura.getEsNueva());
                        ldetalle.setLecturaId(dlectura.getLecturaId()); //id de lectura al que pertenece
                        lectura.getLecturaDetalle().add(ldetalle);
                        if (dlectura.getEsNueva()) {
                            lectura.getLecturafinal().add(
                                    new Lecturafinal(estacion.getEstacionId(), dlectura.getBombaId(), dlectura.getProductoId(), "E", dlectura.geteInicial(), dlectura.geteFinal(), user.getUsername(), user.getNombreLogin())
                            );
                        }
                    }
//                    }

                    crearNuevaLectura = false;
                    for (Integer id : (List<Integer>) tableManuales.getItemIds()) {
                        dlectura = (DtoLectura) ((BeanItem) tableManuales.getItem(id)).getBean();
//                        ldetalle = new LecturaDetalle(lectura.getLecturaId(), estacion.getEstacionId(), dlectura.getBombaId(), dlectura.getProductoId(), dlectura.getTipodespachoId(), "M",
                        ldetalle = new LecturaDetalle(dlectura.getLecturaId(), estacion.getEstacionId(), dlectura.getBombaId(), dlectura.getProductoId(), dlectura.getTipodespachoId(),
                                "M", dlectura.getmInicial(), dlectura.getmFinal(), dlectura.getmTotal(), dlectura.getmCalibracion());
                        if ((!crearNuevaLectura && dlectura.getEsNueva())) {
                            crearNuevaLectura = true;
                        }
                        ldetalle.setEsNueva(dlectura.getEsNueva());
                        ldetalle.setLecturaId(dlectura.getLecturaId()); //id de lectura al que pertenece
                        lectura.getLecturaDetalle().add(ldetalle);
                        if (dlectura.getEsNueva()) {
                            lectura.getLecturafinal().add(
                                    new Lecturafinal(estacion.getEstacionId(), dlectura.getBombaId(), dlectura.getProductoId(), "M", dlectura.getmInicial(), dlectura.getmFinal(), user.getUsername(), user.getNombreLogin())
                            );
                        }
                    }

                    SvcReading svcLectura = new SvcReading();
                    if (action.equals(Dao.ACTION_ADD)) {
//                        Bomba bomba;
//                        String bombasStringList = "";
//                        for (Integer id : (List<Integer>) tableBombas.getItemIds()) {
//                            bomba = (Bomba) ((BeanItem) tableBombas.getItem(id)).getBean();
//                            if (bomba.getSelected()) {
//                                bombasStringList += (bombasStringList.isEmpty()) ? bomba.getId() : ", " + bomba.getId();
//                            }
//                        }
//                        String bombasConLectura = svcLectura.existsLecturasForBombas(estacion.getEstacionId(), turnoId, bombasStringList);
//                        if (bombasConLectura != null && !bombasConLectura.isEmpty()) {
//                            svcLectura.closeConnections();
//                            Notification.show("ERROR:", "NO es posible realizar la acción, ya existe lectura para la(s) bomba(s) " + bombasConLectura, Notification.Type.ERROR_MESSAGE);
//                            return;
//                        }
                    }

//                    lectura = svcLectura.doAction(action, lectura);
//                    Lectura miLectura;
//                    for (LecturaDetalle lde : lectura.getLecturaDetalle()) {
//                        miLectura = lectura;
//                        miLectura.setLecturaDetalle(new ArrayList());
//                        miLectura.getLecturaDetalle().add(lde);
//                        if (lde.getEsNueva()) {
//                            svcLectura.doActionLectura(Dao.ACTION_ADD, miLectura);
//                        } else {
//                            miLectura.setLecturaId(lde.getLecturaId());
//                            svcLectura.doAction(Dao.ACTION_UPDATE, miLectura);
//                        }
//                    }
//                    lectura = svcLectura.doActionLectura(Dao.ACTION_ADD, lectura);
//                    for (LecturaDetalle lde : lectura.getLecturaDetalle()) {
//                        svcLectura.doActionLecturaDetalle(Dao.ACTION_DELETE, lde);
//                        lde.setLecturaId(lectura.getLecturaId());
//                        svcLectura.doActionLecturaDetalle(Dao.ACTION_ADD, lde);
//                    }
                    int contador = 0;
                    if (crearNuevaLectura) {
                        lectura.setNumeroCaso(txtNumeroCaso.getValue());
                        lectura = svcLectura.doActionLectura(Dao.ACTION_ADD, lectura);
                    } else {
                        //ASG
                        lectura.setLecturaId(svcLectura.getLecturaID(lectura.getEmpleadoId(), turnoId));
                        lectura.setNumeroCaso(txtNumeroCaso.getValue());
                        //FIN ASG
                        lectura = svcLectura.doActionLectura(Dao.ACTION_UPDATE, lectura);
                    }
                    for (LecturaDetalle lde : lectura.getLecturaDetalle()) {
                        if (lde.getEsNueva()) {
                            lde.setLecturaId(lectura.getLecturaId());
                            svcLectura.doActionLecturaDetalle(Dao.ACTION_ADD, lde);
                        } else {
                            svcLectura.doActionLecturaDetalle(Dao.ACTION_UPDATE, lde);
                            //Si existe una lectura siguiente, se actualiza la lectura inicial de esa siguiente.
                            LecturaDetalle ldeNext = svcLectura.getLecturaDetalleSiguiente(lde.getEstacionId(), lde.getLecturaId(), lde.getBombaId(), lde.getProductoId());
                            if (ldeNext.getLecturaId() != null) {
                                ldeNext.setLecturaInicial(lde.getLecturaFinal());
                                svcLectura.doActionLecturaDetalle(Dao.ACTION_UPDATE, ldeNext);
                            } else {
                                Lecturafinal lfinal = new Lecturafinal(lde.getEstacionId(), lde.getBombaId(), lde.getProductoId(), lde.getTipo(), lde.getLecturaInicial(), lde.getLecturaFinal(), user.getUsername(), user.getNombreLogin());
                                svcLectura.doActionLecturaFinal(Dao.ACTION_UPDATE, lfinal);
                            }
                        }
                        contador++;
                    }

                    boolean existeMecanica = false;
                    for (Lecturafinal lfl : lectura.getLecturafinal()) {
                        existeMecanica = false;
                        for (Lecturafinal lflu : lecturasUltimaMecanicas) {
                            if (lfl.getBombaId().equals(lflu.getBombaId()) && lfl.getProductoId().equals(lflu.getProductoId())
                                    && lfl.getTipo().equals(lflu.getTipo())) {
                                existeMecanica = true;
                                break;
                            }
                        }
                        if (existeMecanica) {
                            svcLectura.doActionLecturaFinal(Dao.ACTION_UPDATE, lfl);
                        } else {
                            svcLectura.doActionLecturaFinal(Dao.ACTION_ADD, lfl);
                        }
                    }

                    boolean existeElectronica = false;
                    for (Lecturafinal lfl : lectura.getLecturafinal()) {
                        existeElectronica = false;
                        for (Lecturafinal lflu : lecturasUltimaElectronicas) {
                            if (lfl.getBombaId().equals(lflu.getBombaId()) && lfl.getProductoId().equals(lflu.getProductoId())
                                    && lfl.getTipo().equals(lflu.getTipo())) {
                                existeElectronica = true;
                                break;
                            }
                        }
                        if (existeElectronica) {
                            svcLectura.doActionLecturaFinal(Dao.ACTION_UPDATE, lfl);
                        } else {
                            svcLectura.doActionLecturaFinal(Dao.ACTION_ADD, lfl);
                        }
                    }

                    tmpString = (user.getPaisLogin() != null) ? user.getPaisLogin().getNombre() : ((Pais) cbxCountry.getValue()).getNombre();
                    Parametro parametro = svcLectura.getParameterByName("CORREO_CALIBRACIONES_" + tmpString.toUpperCase().replaceAll(" ", ""));
                    svcLectura.closeConnections();

                    if (contador > 0) {
                        // ----------17/10/2019 se omite correo
//                        if (crearNuevaLectura && existCalibration) {
//                            Mail mail = new Mail(parametro.getValor(), "Web COCOs - Calibración " + user.getEstacionLogin().getNombre(), "Se ingresado una lectura con calibraciones", new ArrayList(Arrays.asList(tempFile.getAbsolutePath())));
//                            mail.run();
//                        }

//                    if (lectura.getLecturaId() != null) {
//                        Notification.show("La lectura se ha creado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                        Notification notif = new Notification("ÉXITO:", "Operación realizada con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                        notif.setDelayMsec(3000);
                        notif.setPosition(Position.MIDDLE_CENTER);
                        //notif.setStyleName("mystyle");
                        //notif.setIcon(new ThemeResource("img/reindeer.png"));
                        notif.show(Page.getCurrent());
                        UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PR_READING.getViewName());
                    } else {
                        Notification.show("ERROR:", "Ocurrió un error al guardar la lectura.\n" + lectura.getDescError(), Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        });

        /*        btnAll = new Button("Todas");
        btnAll.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btnAll.addStyleName(ValoTheme.BUTTON_LINK);
        btnAll.setIcon(FontAwesome.CHECK_SQUARE_O);
        btnAll.setEnabled(!user.getRolLogin().equals(Constant.ROL_LOGIN_OTRO) && turno.getTurnoId() != null);    //habilitado
        btnAll.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                List<Integer> ids = (List<Integer>) tableBombas.getItemIds();
                for (Integer i : ids) {
//                    boolean exists = false;
//                    for (Bomba b : bombasInhabilitadas) {
//                        if (b.getId().equals(i)) {
//                            exists = true;
//                            break;
//                        }
//                    }
//                    tableBombas.getItem(i).getItemProperty("selected").setValue(!exists);
                    tableBombas.getItem(i).getItemProperty("selected").setValue(true);
                }
            }
        });

        btnNone = new Button("Ninguna");
        btnNone.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btnNone.addStyleName(ValoTheme.BUTTON_LINK);
        btnNone.setIcon(FontAwesome.SQUARE_O);
        btnNone.setEnabled(!user.getRolLogin().equals(Constant.ROL_LOGIN_OTRO) && turno.getTurnoId() != null);    //habilitado
        btnNone.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                List<Integer> ids = (List<Integer>) tableBombas.getItemIds();
                for (Integer i : ids) {
                    tableBombas.getItem(i).getItemProperty("selected").setValue(false);
                }
            }
        });
         */
//        cbxTurno = new ComboBox("Turnos:", contTurnos);
//        cbxTurno.setItemCaptionPropertyId("nombre");
//        cbxTurno.setNullSelectionAllowed(false);
////        cbxTurno.setEnabled(turno.getTurnoId()==null || turno.getEstadoId() != 1);    //habilitado
//        //Al momento de "construir" el control el turno es el activo.
//        cbxTurno.setEnabled(contTurnos.size() > 0 && turno.getTurnoId() == null);    //habilitado
//        cbxTurno.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                turno = (Turno) cbxTurno.getValue();
//                bcrElectronicas.removeAllItems();
//                bcrManuales.removeAllItems();
//                bcrPrecios.removeAllItems();
//                bcrBombas.removeAllItems();
//                getAllData();
////                buildLabels();
//                defineTableBombas();
//                btnAll.setEnabled(true);
//                btnNone.setEnabled(true);
//                btnSave.setEnabled(true);    //habilitado
//            }
//        });
        upload = new Upload("Selección:", new Upload.Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                //Validaciones
                try {
                    Constant constant = new Constant();
                    if (constant.MAP_MIMETYPES_EXT.containsKey(mimeType)) {
                        System.out.println("receiveUpload::: " + filename + "; " + mimeType);
                        tmpString = "Calibraciones" + user.getEstacionLogin().getNombre().replaceAll(" ", "-").concat(Constant.SDF_yyyyMMddHHmmss.format(new Date()));
                        tempFile = File.createTempFile(tmpString, constant.MAP_MIMETYPES_EXT.get(mimeType));
                        return new FileOutputStream(tempFile);
                    } else {
                        Notification.show("ERROR", "Tipo de documento NO reconocido", Notification.Type.ERROR_MESSAGE);
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
        upload.addFinishedListener(new Upload.FinishedListener() {
            @Override
            public void uploadFinished(Upload.FinishedEvent finishedEvent) {
                try {
                    System.out.println("uploadFinished::: " + finishedEvent.getFilename() + "; " + finishedEvent.getMIMEType() + "; " + finishedEvent.getLength() + "; " + finishedEvent.toString());
                    if (tempFile != null && tempFile.exists()) {
//                        XlsxReader(tempFile, true);
                    }
                } catch (Exception ex) {
//                    Notification.show("Fila: " + line + "; " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        });
        upload.addSucceededListener(new Upload.SucceededListener() {
            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                System.out.println("uploadSucceeded::: ");
            }
        });
        upload.addProgressListener(new Upload.ProgressListener() {
            @Override
            public void updateProgress(long readBytes, long contentLength) {
                System.out.println("updateProgress::: " + new Date());
            }
        });
        upload.addFailedListener(new Upload.FailedListener() {
            @Override
            public void uploadFailed(Upload.FailedEvent event) {
                System.out.println("uploadFailed::: ");
            }
        });
        upload.setButtonCaption("Cargar comprobante");

    }

    private void buildTablePrecio() {
        tablePrecios.setCaption("Precios:");
        tablePrecios.setContainerDataSource(bcrPrecios);
//determinarColumnasPrecios();

        Object[] columns = new Object[]{"productoName"};
        String[] titles = new String[]{"Producto"};
        Align[] alignments = new Align[]{Align.LEFT};
        if (!precios.isEmpty()) {
            DtoPrecio dpo = precios.get(0);
            if (dpo.getPrecioAS() > 0 && dpo.getPrecioSC() > 0) {
                columns = new Object[]{"productoName", "precioAS", "precioSC"};
                titles = new String[]{"Producto", "Autoservicio", "Completo"};
                alignments = new Align[]{Align.LEFT, Align.RIGHT, Align.RIGHT};
            } else if (dpo.getPrecioAS() > 0) {
                columns = new Object[]{"productoName", "precioAS"};
                titles = new String[]{"Producto", "Autoservicio"};
                alignments = new Align[]{Align.LEFT, Align.RIGHT};
            } else if (dpo.getPrecioSC() > 0) {
                columns = new Object[]{"productoName", "precioSC"};
                titles = new String[]{"Producto", "Completo"};
                alignments = new Align[]{Align.LEFT, Align.RIGHT};
            }
        }
        tablePrecios.setVisibleColumns(columns);
        tablePrecios.setColumnHeaders(titles);
        tablePrecios.setColumnAlignments(alignments);

//        tablePrecios.setVisibleColumns(columns);
//        tablePrecios.setColumnHeaders(titles);
//        tablePrecios.setColumnAlignments(alignments);
        tablePrecios.addStyleName(ValoTheme.TABLE_NO_STRIPES);
        tablePrecios.addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        tablePrecios.addStyleName(ValoTheme.TABLE_SMALL);
        tablePrecios.addStyleName(ValoTheme.TABLE_COMPACT);
//        tablePrecios.setSizeFull();
//        tablePrecios.setHeight(150, Unit.PIXELS);

    }

    private HorizontalLayout buildLabels() {
        HorizontalLayout hlInputs = new HorizontalLayout();
        hlInputs.setId("hlLabels");
        hlInputs.setSpacing(true);
        hlInputs.setWidth(100f, Unit.PERCENTAGE);
//        hlInputs.setHeight(160f, Unit.PIXELS);  //160 para tres productos
//        if (bcrPrecios != null && bcrPrecios.getItemIds().size() == 4) {
//            hlInputs.setHeight(191f, Unit.PIXELS);  //160 para tres productos
//        }

        String stationname = (estacion != null) ? estacion.getNombre() : "";
        labelStation = new Label("<p>Estación: <h2><b>" + stationname + "</b></h2></p>", ContentMode.HTML);

//        if (turno.getTurnoId() == null) {
//            turno.setTurnoFusion("");
//            turno.setTurnoId(0);
//            turno.setCreadoPersona("");
//        }
//        Integer paisId = (pais == null) ? null : pais.getPaisId();
//        String turnName = (paisId != null && paisId == Constant.CAT_PAIS_GUATEMALA) ? turno.getTurnoFusion() : turno.getTurnoId().toString();
        String turnName = "";
        labelTurno = new Label("Turno: <h2><b>" + turnName + "</b></h2>", ContentMode.HTML);
//        labelCreadopor = new Label("Creado por: <h2><b>" + turno.getCreadoPersona() + "</b></h2>", ContentMode.HTML);
        labelCreadopor = new Label("Creado por: <h2><b>" + "" + "</b></h2>", ContentMode.HTML);

        HorizontalLayout hlLabels2 = utils.buildHorizontal("hlLabels2", false, true, true, false);
        hlLabels2.setWidth("100%");
        hlLabels2.setHeight(70f, Unit.PIXELS);
        hlLabels2.addComponents(labelStation, labelTurno, labelCreadopor);
        hlLabels2.setExpandRatio(labelStation, 0.42f);
        hlLabels2.setExpandRatio(labelTurno, 0.16f);
        hlLabels2.setExpandRatio(labelCreadopor, 0.42f);

        HorizontalLayout cltInfo = utils.buildHorizontal("cltInfo", false, true, true, false);
//        cltInfo.setSizeUndefined();
        cltInfo.addComponents(lblUltimoDía, lblUltimoTurno);

        HorizontalLayout cltToolbar = utils.buildHorizontal("cltToolbar", false, true, true, false);
//        cltToolbar.setSizeUndefined();
        cltToolbar.addComponents(cbxCountry, cbxEstacion, dfdFecha, cbxTurno /**
         * , tfdNameSeller, tfdNameChief*
         */
        );

        VerticalLayout vlItems = utils.buildVertical("vlItems", false, true, true, false, null);
//        vlItems.setSizeUndefined();
        vlItems.addComponents(hlLabels2, cltInfo, cltToolbar);

        tablePrecios.setHeight(150, Unit.PIXELS);
        hlInputs.addComponents(//labelStation, labelTurno, labelCreadopor, 
                vlItems, tablePrecios);
        hlInputs.setExpandRatio(vlItems, 0.80f);
        hlInputs.setExpandRatio(tablePrecios, 0.20f);
//        hlInputs.setComponentAlignment(labelStation, Alignment.TOP_RIGHT);
//        hlInputs.setComponentAlignment(labelTurno, Alignment.TOP_LEFT);
//        hlInputs.setComponentAlignment(labelCreadopor, Alignment.TOP_CENTER);
        hlInputs.setComponentAlignment(tablePrecios, Alignment.TOP_LEFT);
        return hlInputs;
    }

    private void defineTableBombas() {
        cbxEmpleado = utils.buildCombobox("Puesto:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(Empleado.class, listEmpleados));
        cbxEmpleado.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                //tfdNameSeller.setValue(null);
                //tfdNameChief.setValue(null);
                for (Integer itemId : bcrBombas.getItemIds()) {
                    bcrBombas.getItem(itemId).getItemProperty("selected").setValue(false);
                    onChangeCheckboxBomba(itemId, false);
                }
                for (Bomba bomba : ((Empleado) cbxEmpleado.getValue()).getBombas()) {
                    for (Integer itemId : bcrBombas.getItemIds()) {
                        if (bomba.getId().equals(itemId)) {
                            bcrBombas.getItem(itemId).getItemProperty("selected").setValue(true);
                            onChangeCheckboxBomba(itemId, true);
                            break;
                        }
                    }
                }
            }
        });

        tableBombas = new Table("Bombas:");
        tableBombas.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tableBombas.addStyleName(ValoTheme.TABLE_COMPACT);
        tableBombas.addStyleName(ValoTheme.TABLE_SMALL);
        tableBombas.setSelectable(false);
        tableBombas.setContainerDataSource(bcrBombas);
        tableBombas.setWidthUndefined();
        tableBombas.setHeight(100f, Unit.PERCENTAGE);
        tableBombas.setWidth("150px");
//        tableBombas.setHeight("300px");
        tableBombas.addGeneratedColumn("colSelected", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("selected");  //Atributo del bean
                final CheckBox cb = new CheckBox("", pro);
//                for (Bomba b : bombasInhabilitadas) {
//                    if (b.getId().equals(itemId)) {
//                        cb.setEnabled(false);   //habilitado
//                        break;
//                    }
//                }

                cb.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
//                        Boolean.parseBoolean(event.getProperty().getValue().toString())
                        onChangeCheckboxBomba(itemId, Boolean.parseBoolean(event.getProperty().getValue().toString()));
                    }
                });
                return cb;
            }
        });

        tableBombas.setVisibleColumns("colSelected", "tipoDespachoName", "nombre");
        tableBombas.setColumnHeaders("", "Tipo", "Bomba");

    }

    private void buildTableElectronicas() {
        tableElectronicas.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tableElectronicas.addStyleName(ValoTheme.TABLE_COMPACT);
        tableElectronicas.addStyleName(ValoTheme.TABLE_SMALL);
        tableElectronicas.setContainerDataSource(bcrElectronicas);
        tableElectronicas.setSizeFull();

        tableElectronicas.addGeneratedColumn("colInicial", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("eInicial");  //Atributo del bean

                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                nfd.setWidth(100f, Unit.PIXELS);
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                boolean existe = false;
                DtoLectura dtol = (DtoLectura) ((BeanItem) tableElectronicas.getItem(itemId)).getBean();
                for (Lecturafinal lfl : lecturasUltimaElectronicas) {
                    if (lfl.getBombaId().equals(dtol.getBombaId()) && lfl.getProductoId().equals(dtol.getProductoId())) {
                        existe = true;
                        break;
                    }
                }
                nfd.setEnabled(!existe);  //habilitada
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        DtoLectura lectura;
                        List<Integer> bombIds = (List<Integer>) bcrElectronicas.getItemIds();
                        for (Integer i : bombIds) {
                            if (i.equals(itemId)) {
                                lectura = (DtoLectura) ((BeanItem) tableElectronicas.getItem(i)).getBean();
                                lectura.seteVolumen(lectura.geteFinal() - lectura.geteInicial());
                                bcrElectronicas.getItem(itemId).getItemProperty("eVolumen").setValue(lectura.geteInicial() + lectura.geteVolumen());
                                //TODO: evaluar la utilidad del siguiente codigo
//                                if (pais.getPaisId() == Constant.CAT_PAIS_GUATEMALA) {
//                                Double val1 = Double.parseDouble(bcrManuales.getItem(itemId).getItemProperty("mTotal").getValue().toString());
//                                Double val2 = lectura.geteVolumen();
//                                bcrManuales.getItem(itemId).getItemProperty("mDiferencia").setValue(
//                                        val2 - val1
//                                );
//                            }
                                break;
                            }
                        }

                    }
                });
                return nfd;
            }
        });

        tableElectronicas.addGeneratedColumn("colFinal", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("eFinal");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                nfd.setWidth(100f, Unit.PIXELS);
                nfd.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.setEnabled(editar);
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        DtoLectura lectura;
                        List<Integer> bombIds = (List<Integer>) bcrElectronicas.getItemIds();
                        double difference;
                        for (Integer i : bombIds) {
                            if (i.equals(itemId)) {
                                lectura = (DtoLectura) ((BeanItem) tableElectronicas.getItem(i)).getBean();
                                difference = lectura.geteFinal() - lectura.geteInicial();
                                if (lectura.geteFinal() < lectura.geteInicial()) {
                                    difference = (Constant.MAX_VALUE_COUNTER_PUMP - lectura.geteInicial()) + lectura.geteFinal();
                                }
                                lectura.seteVolumen(difference);
                                bcrElectronicas.getItem(itemId).getItemProperty("eVolumen").setValue(difference);
                                //TODO: evaluar la utilidad del siguiente codigo
//                                if (pais.getPaisId() == Constant.CAT_PAIS_GUATEMALA) {
//                                    Double val1 = Double.parseDouble(bcrElectronicas.getItem(itemId).getItemProperty("eVolumen").getValue().toString());
//                                    Double val2 = lectura.getmTotal();
//                                    bcrElectronicas.getItem(itemId).getItemProperty("mDiferencia").setValue(
//                                            val1 - val2
//                                    );
//                                }
                                break;
                            }
                        }
                    }
                });
                return nfd;
            }
        });

        tableElectronicas.addGeneratedColumn("colCalibracion", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("mCalibracion");  //Atributo del bean
//                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble_3Digit(pro));
                nfd.setWidth(60f, Unit.PIXELS);
                nfd.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.setNullRepresentation("0.000");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        DtoLectura dtol = bcrElectronicas.getItem(itemId).getBean();
                        Double diff = dtol.geteFinal() - dtol.geteInicial() - Double.parseDouble(nfd.getValue());
                        bcrElectronicas.getItem(itemId).getItemProperty("eVolumen").setValue(diff);
//                        bcrManuales.getItem(itemId).getItemProperty("mCalibracion").setValue(Double.parseDouble(nfd.getValue()));   //replicar en calibraciones manuales  ASG COMENTADO

                        //ASG 
                        boolean valida = false;
                        DtoLectura dlectura2;
                        for (Integer id : (List<Integer>) tableElectronicas.getItemIds()) {
                            dlectura2 = (DtoLectura) ((BeanItem) tableElectronicas.getItem(id)).getBean();
                            if (dlectura2.getmCalibracion() != null) {
                                if (dlectura2.getmCalibracion() > 0) {
                                    valida = true;
                                }
                            }
                        }
                        if (valida) {
                            txtNumeroCaso.setVisible(true);
                        } else {
                            txtNumeroCaso.setVisible(false);
                        }
                        /*FIN ASG*/
                    }
                });
                return nfd;
            }
        });

        tableElectronicas.addGeneratedColumn("colVolumen", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("eVolumen");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                nfd.setEnabled(false);  //habilitada
                nfd.setWidth(75f, Unit.PIXELS);
                nfd.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        calculateVentaTotalElectronicas();
                    }
                });

                return nfd;
            }
        });

        tableElectronicas.setVisibleColumns("bomba", "producto", "colInicial", "colFinal", "colCalibracion", "colVolumen");
        tableElectronicas.setColumnHeaders("Bomba", "Producto", "Inicial", "Final", "Calibración", "Venta");
        tableElectronicas.setColumnAlignments(new Align[]{Align.LEFT, Align.LEFT, Align.RIGHT, Align.RIGHT, Align.RIGHT, Align.RIGHT});
        tableElectronicas.setFooterVisible(true);
        tableElectronicas.setColumnFooter("bomba", "Totales:");
    }

    private void buildTableManuales() {
        tableManuales = new Table("Lecturas manuales:");
        tableManuales.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tableManuales.addStyleName(ValoTheme.TABLE_COMPACT);
        tableManuales.addStyleName(ValoTheme.TABLE_SMALL);
        tableManuales.setContainerDataSource(bcrManuales);
        tableManuales.setHeight("450px");

        tableManuales.addGeneratedColumn("colInicial", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("mInicial");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                nfd.setWidth(100f, Unit.PIXELS);
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                boolean existe = false;
//                Double myFinalValue = 0D;
                DtoLectura dtol = (DtoLectura) ((BeanItem) tableManuales.getItem(itemId)).getBean();
                for (Lecturafinal lfl : lecturasUltimaMecanicas) {
                    if (lfl.getBombaId().equals(dtol.getBombaId()) && lfl.getProductoId().equals(dtol.getProductoId())) {
                        existe = true;
//                        myFinalValue = lfl.getLecturaFinal();
                        break;
                    }
                }

                //La primera vez que se ingresan lecturas para la estacion o cuando se elija un turno cerrado
//                existe = false;
//                for (DtoLectura dla : lecturasTurnoActivoMecanicas) {
//                    if (dla.getBombaId().equals(dtol.getBombaId()) && dla.getProductoId().equals(dtol.getProductoId())) {
//                        existe = true;
//                        break;
//                    }
//                }
//                nfd.setEnabled(turno.getEstadoId() == 1 && !existe);  //habilitada
                nfd.setEnabled(!existe);  //habilitada
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        DtoLectura lectura;
                        List<Integer> bombIds = (List<Integer>) bcrManuales.getItemIds();
                        for (Integer i : bombIds) {
                            if (i.equals(itemId)) {
                                lectura = (DtoLectura) ((BeanItem) tableManuales.getItem(i)).getBean();
                                lectura.setmTotal(lectura.getmFinal() - lectura.getmInicial());
                                bcrManuales.getItem(itemId).getItemProperty("mTotal").setValue(lectura.getmFinal() - lectura.getmInicial());
                                //TODO: evaluar la utilidad del siguiente codigo
//                                if (pais.getPaisId() == Constant.CAT_PAIS_GUATEMALA) {
//                                    Double val1 = Double.parseDouble(bcrElectronicas.getItem(itemId).getItemProperty("eVolumen").getValue().toString());
//                                    Double val2 = lectura.getmTotal();
//                                    bcrManuales.getItem(itemId).getItemProperty("mDiferencia").setValue(
//                                            val1 - val2
//                                    );
//                                }
                                break;
                            }
                        }
                    }
                });
                return nfd;
            }
        });

        tableManuales.addGeneratedColumn("colFinal", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("mFinal");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                nfd.setWidth(100f, Unit.PIXELS);
                nfd.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.setEnabled(editar);
//                nfd.setEnabled(
//                        dia.getFecha() != null && ultimoDia.getFecha() != null && turno.getEstadoId() != null
//                        && dia.getFecha().equals(ultimoDia.getFecha())
//                        && turno.getEstadoId() == 1);
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        DtoLectura lectura;
                        List<Integer> bombIds = (List<Integer>) bcrManuales.getItemIds();
                        double difference;
                        for (Integer i : bombIds) {
                            if (i.equals(itemId)) {
                                lectura = (DtoLectura) ((BeanItem) tableManuales.getItem(i)).getBean();
                                difference = lectura.getmFinal() - lectura.getmInicial();
                                if (lectura.getmFinal() < lectura.getmInicial()) {
                                    difference = (Constant.MAX_VALUE_COUNTER_PUMP - lectura.getmInicial()) + lectura.getmFinal();
                                }
                                lectura.setmTotal(difference);
                                bcrManuales.getItem(itemId).getItemProperty("mTotal").setValue(difference);
                                //TODO: evaluar la utilidad del siguiente codigo
//                                if (pais.getPaisId() == Constant.CAT_PAIS_GUATEMALA) {
//                                    Double val1 = Double.parseDouble(bcrElectronicas.getItem(itemId).getItemProperty("eVolumen").getValue().toString());
//                                    Double val2 = lectura.getmTotal();
//                                    bcrManuales.getItem(itemId).getItemProperty("mDiferencia").setValue(
//                                            val1 - val2
//                                    );
//                                }
                                break;
                            }
                        }
                    }
                });
                return nfd;
            }
        });

        tableManuales.addGeneratedColumn("colTotal", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("mTotal");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                nfd.setEnabled(false);  //habilitada
                nfd.setWidth(75f, Unit.PIXELS);
                nfd.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        calculateVentaTotal();
                    }
                });
                return nfd;
            }
        });

        tableManuales.addGeneratedColumn("colDiferencia", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("mDiferencia");  //Atributo del bean
                final NumberField nfd = new NumberField();
                nfd.setPropertyDataSource(pro);
                nfd.setEnabled(false);  //habilitada
                nfd.setWidth(65f, Unit.PIXELS);
                nfd.setDecimalPrecision(3);
                nfd.setMinValue(-0.99);
                nfd.setMaxValue(0.99);
                nfd.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        List<Integer> ids = (List<Integer>) bcrManuales.getItemIds();
                        totalDiferencia = 0D;
                        for (Integer id : ids) {
                            totalDiferencia += Double.parseDouble(bcrManuales.getItem(id).getItemProperty("mDiferencia").getValue().toString());
                        }
                        tableManuales.setColumnFooter("colDiferencia", numberFmt.format(totalDiferencia));
                    }
                });
                return nfd;
            }
        });

        tableManuales.addGeneratedColumn("colCalibracion", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("mCalibracion");  //Atributo del bean
//                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble_3Digit(pro));
                nfd.setWidth(60f, Unit.PIXELS);
                nfd.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.setNullRepresentation("0.000");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        DtoLectura dtol = bcrManuales.getItem(itemId).getBean();
                        Double diff = dtol.getmFinal() - dtol.getmInicial() - Double.parseDouble(nfd.getValue());
                        bcrManuales.getItem(itemId).getItemProperty("mTotal").setValue(diff);
                        try {
                            //ASG//
                            boolean valida = false;
                            DtoLectura dlectura;
                            for (Integer id : (List<Integer>) tableManuales.getItemIds()) {
                                dlectura = (DtoLectura) ((BeanItem) tableManuales.getItem(id)).getBean();
                                if (dlectura.getmCalibracion() != null) {
                                    if (dlectura.getmCalibracion() > 0) {
                                        valida = true;
                                    }
                                }
                            }
                            if (valida) {
                                txtNumeroCaso.setVisible(true);
                            } else {
                                txtNumeroCaso.setVisible(false);
                            }
                            /*FIN ASG*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                );
                return nfd;
            }
        }
        );

        Object[] columns = new Object[]{"bomba", "producto", "colInicial", "colFinal", "colCalibracion", "colTotal"};
        String[] tittles = new String[]{"Bomba", "Producto", "Inicial", "Final", "Calibración", "Venta"};
        Align[] aligns = new Align[]{Align.LEFT, Align.LEFT, Align.RIGHT, Align.RIGHT, Align.RIGHT, Align.RIGHT};
        Integer paisId = (pais == null) ? null : pais.getPaisId();
        if (paisId != null && paisId == Constant.CAT_PAIS_GUATEMALA) {
            columns = new Object[]{"bomba", "producto", "colInicial", "colFinal", "colTotal", "colDiferencia"};
            tittles = new String[]{"Bomba", "Producto", "Inicial", "Final", "Venta", "Diferencia"};
            aligns = new Align[]{Align.LEFT, Align.LEFT, Align.RIGHT, Align.RIGHT, Align.RIGHT, Align.RIGHT};
        }

        tableManuales.setVisibleColumns(columns);

        tableManuales.setColumnHeaders(tittles);

        tableManuales.setColumnAlignments(aligns);

        tableManuales.setFooterVisible(
                true);
        tableManuales.setColumnFooter(
                "bomba", "Totales:");
        tableManuales.setColumnFooter(
                "colDiferencia", numberFmt.format(totalDiferencia));

    }

    private void calculateVentaTotal() {
        List<Integer> ids = (List<Integer>) bcrManuales.getItemIds();
        Double totalVenta = 0D;
        for (Integer id : ids) {
            totalVenta += Double.parseDouble(bcrManuales.getItem(id).getItemProperty("mTotal").getValue().toString());
        }
        tableManuales.setColumnFooter("colTotal", numberFmt.format(totalVenta));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        lblUltimoDía.setSizeUndefined();
        lblUltimoDía.setWidth("30%");

        fechaString = (ultimoTurno.getTurnoId() != null) ? "Turno ".concat(ultimoTurno.getTurnoId().toString()) : "INEXISTENTE";
        estadoName = (ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 1)
                ? "ABIERTO" : ((ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 2) ? "CERRADO" : "SIN ESTADO");
        lblUltimoTurno.setValue("Último turno: " + fechaString + " (" + estadoName + ")");
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_BOLD);
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_H3);
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_COLORED);
        lblUltimoTurno.setSizeUndefined();
//        lblUltimoTurno.setWidth("350px");
    }

    private void calculateVentaTotalElectronicas() {
        List<Integer> ids = (List<Integer>) bcrElectronicas.getItemIds();
        Double totalVenta = 0D;
        for (Integer id : ids) {
            totalVenta += Double.parseDouble(bcrElectronicas.getItem(id).getItemProperty("eVolumen").getValue().toString());
        }
        tableElectronicas.setColumnFooter("colVolumen", numberFmt.format(totalVenta));
    }

    private void onChangeCheckboxBomba(Object itemId, boolean isselected) {
        if (action.equals(Dao.ACTION_UPDATE)) {
            bcrElectronicas.removeAllItems();
            bcrManuales.removeAllItems();
//                            cbxTurno.setValue(null);
        }

        action = Dao.ACTION_ADD;
//                        cbxTurno.setValue(null);
        Bomba bomba = (Bomba) ((BeanItem) tableBombas.getItem(itemId)).getBean();
        String nameBombaSuffix = bomba.getNombre();
//                        String nameBombaSuffix = "Bomba ";
//                        if (Boolean.parseBoolean(event.getProperty().getValue().toString())) {
        if (isselected) {

            //agregarlas en orden
            DtoLectura lectura;
            List<Integer> bombIds = (List<Integer>) bcrManuales.getItemIds();
            int previous = 0, newid = 0;
            for (Integer i : bombIds) {
//                                BeanItem<DtoLectura> item = (BeanItem) tableElectronicas.getItem(i);
                lectura = (DtoLectura) ((BeanItem) tableManuales.getItem(i)).getBean();
                if (Integer.parseInt(itemId.toString()) > lectura.getBombaId()) {
                    previous++;
                }
                if (i > newid) {
                    newid = i;
                }
                if (Integer.parseInt(itemId.toString()) == lectura.getBombaId()) {
                    return; //nothing to do here!
                }
            }

//                            Bomba bomba = (Bomba) ((BeanItem) tableBombas.getItem(itemId)).getBean();
            DtoLectura dla;
            for (Producto p : productos) {
                newid += 1;
//                                if (estacion.getPaisId() == Constant.CAT_PAIS_GUATEMALA) {
//                                    dla = new DtoLectura(newid, Integer.parseInt(itemId.toString()), nameBombaSuffix + Integer.parseInt(itemId.toString()), p.getProductoId(), p.getNombre());
                dla = new DtoLectura(newid, Integer.parseInt(itemId.toString()), nameBombaSuffix, p.getProductoId(), p.getNombre());
                dla.setTipodespachoId(bomba.getTipoDespachoId());
                dla.seteInicial(0D);
                dla.seteFinal(0D);
                dla.seteVolumen(0D);
                dla.setEsNueva(Boolean.TRUE);
//                                    dla.seteCalibracion(0D);

                boolean lecturaFinalTurnoActual = false;
                for (DtoLectura dlec : lecturasTurnoActivoElectronicas) {
                    if (dlec.getBombaId().equals(itemId) && dlec.getProductoId().equals(p.getProductoId())
                            && dlec.getTipodespachoId().equals(bomba.getTipoDespachoId()) //&& dlec.getmFinal() > 0
                            ) {
                        lecturaFinalTurnoActual = true;
                    }
                }
                if (lecturaFinalTurnoActual) {    //cerrado
                    //ASG
                    String numeroCaso = "";
                    for (DtoLectura dlec : lecturasTurnoActivoElectronicas) {
                        if (dlec.getBombaId().equals(itemId) && dlec.getProductoId().equals(p.getProductoId()) && dlec.getTipodespachoId().equals(bomba.getTipoDespachoId())) {
                            //tfdNameSeller.setValue(dlec.getNombrePistero());
                            //tfdNameChief.setValue(dlec.getNombreJefe());
                            dla.seteInicial(dlec.getmInicial());
                            dla.seteFinal(dlec.getmFinal());
                            dla.setmCalibracion(dlec.getmCalibracion());  ///ASG
                            if (dlec.getNumeroCaso() != null) {
                                numeroCaso = dlec.getNumeroCaso();
                            }
                            dla.setEsNueva(Boolean.FALSE);
                            dla.setLecturaId(dlec.getLecturaId());
//                                            dla.setmTotal(dlec.getmFinal() - dlec.getmInicial() - dlec.getmCalibracion());
//                            dla.seteVolumen(dlec.getmFinal() - dlec.getmInicial());  //ASG COMENTE
                            dla.seteVolumen(dlec.getmTotal());//ASG

                            break;
                        }
                    }
                    // ASG

                    System.out.println("NUMERO CASOOO " + numeroCaso);
                    if (!numeroCaso.equals("")) {
                        txtNumeroCaso.setVisible(true);
                        txtNumeroCaso.setValue(numeroCaso);
                    } else {
                        txtNumeroCaso.setVisible(false);
                        txtNumeroCaso.setValue("");
                    }
                    //FIN
                } else {
                    //al crear nuevo conjunto de lecturas, la lectura inicial se obtiene de esta tabla.
                    for (Lecturafinal lfinal : lecturasUltimaElectronicas) {
                        if (lfinal.getBombaId().equals(itemId) && lfinal.getProductoId().equals(p.getProductoId())) {
                            dla.seteInicial(lfinal.getLecturaFinal());
                            dla.seteVolumen(0D);
                            break;
                        }
//                                        tfdNameSeller.setValue(dlec.getNombrePistero());
//                                        tfdNameChief.setValue(dlec.getNombreJefe());
                        txtNumeroCaso.setVisible(false);
                        txtNumeroCaso.setValue("");
                    }
                }

//                                    for (DtoLectura dlec : lecturasTurnoActivoElectronicas) {
//                                        if (dlec.getBombaId().equals(itemId) && dlec.getProductoId().equals(p.getProductoId()) && dlec.getTipodespachoId().equals(bomba.getTipoDespachoId())) {
//                                            dla.seteInicial(dlec.getmFinal());
//                                            break;
//                                        }
//                                    }
                //TODO: parece que aca hay que agregar programacion igual a la de abajo.
                bcrElectronicas.addBeanAt(previous, dla);
//                                }

                dla = new DtoLectura(newid, Integer.parseInt(itemId.toString()), nameBombaSuffix, p.getProductoId(), p.getNombre());
                dla.setTipodespachoId(bomba.getTipoDespachoId());
                dla.setmInicial(0D);
                dla.setmFinal(0D);
                dla.setmTotal(0D);
                dla.setmDiferencia(0D);
                dla.setEsNueva(Boolean.TRUE);
                dla.setmCalibracion(0D);

                lecturaFinalTurnoActual = false;
                for (DtoLectura dlec : lecturasTurnoActivoMecanicas) {
                    if (dlec.getBombaId().equals(itemId) && dlec.getProductoId().equals(p.getProductoId())
                            && dlec.getTipodespachoId().equals(bomba.getTipoDespachoId()) //&& dlec.getmFinal() > 0
                            ) {
                        lecturaFinalTurnoActual = true;
                    }
                }

//                                if (turno.getEstadoId()==2 || (turno.getEstadoId()==1 && lecturasTurnoActivoMecanicas.size()>0)) {    //cerrado
                if (lecturaFinalTurnoActual) {    //cerrado
                    for (DtoLectura dlec : lecturasTurnoActivoMecanicas) {
                        //tfdNameSeller.setValue(dlec.getNombrePistero());
                        //tfdNameChief.setValue(dlec.getNombreJefe());
                        if (dlec.getBombaId().equals(itemId) && dlec.getProductoId().equals(p.getProductoId()) && dlec.getTipodespachoId().equals(bomba.getTipoDespachoId())) {
                            dla.setmInicial(dlec.getmInicial());
                            dla.setmFinal(dlec.getmFinal());
                            dla.setmCalibracion(dlec.getmCalibracion());
                            dla.setEsNueva(Boolean.FALSE);
                            dla.setLecturaId(dlec.getLecturaId());
                            dla.setmTotal(dlec.getmFinal() - dlec.getmInicial() - dlec.getmCalibracion());
                            break;
                        }
                    }
                } else {
                    //al crear nuevo conjunto de lecturas, la lectura inicial se obtiene de esta tabla.
                    for (Lecturafinal lfinal : lecturasUltimaMecanicas) {
                        if (lfinal.getBombaId().equals(itemId) && lfinal.getProductoId().equals(p.getProductoId())) {
                            dla.setmInicial(lfinal.getLecturaFinal());
                            dla.setmTotal(0D);
                            break;
                        }
                    }
                }

                bcrManuales.addBeanAt(previous, dla);
                previous++;
            }
        } else {
            List<Integer> ids = new ArrayList();
            BeanItem<DtoLectura> item;
            DtoLectura lectura;
            for (Integer i : bcrManuales.getItemIds()) {
                item = (BeanItem) tableManuales.getItem(i);
                lectura = item.getBean();
//                                if (lectura.getBomba().equals(nameBombaSuffix + itemId)) {
                if (lectura.getBomba().equals(nameBombaSuffix)) {
                    ids.add(lectura.getBombaIdDto());
                }
            }
            for (Integer id : ids) {
                bcrElectronicas.removeItem(id);
                bcrManuales.removeItem(id);
            }
        }
        calculateVentaTotal();
        calculateVentaTotalElectronicas();
//buildTableElectronicas();
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

}
