package com.fundamental.view;

import com.fundamental.model.Arqueocaja;
import com.fundamental.model.ArqueocajaBomba;
import com.fundamental.model.ArqueocajaDetalle;
import com.fundamental.model.ArqueocajaProducto;
import com.fundamental.model.Bomba;
import com.fundamental.model.Cliente;
import com.fundamental.model.Dia;
import com.fundamental.services.Dao;
import com.fundamental.model.Efectivo;
import com.sisintegrados.generic.bean.Empleado;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.FactelectronicaPos;
import com.fundamental.model.Lubricanteprecio;
import com.fundamental.model.Mediopago;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Parametro;
import com.fundamental.model.Producto;
import com.fundamental.model.TasaCambio;
import com.fundamental.model.Turno;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoArqueo;
import com.fundamental.model.dto.DtoEfectivo;
import com.fundamental.model.dto.DtoProducto;
import com.fundamental.services.SvcArqueo;
import com.fundamental.services.SvcClientePrepago;
import com.fundamental.services.SvcCuadre;
import com.fundamental.services.SvcTurno;
import com.fundamental.services.SvcTurnoCierre;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.fundamental.utils.Mail;
import com.fundamental.utils.Util;
import com.sisintegrados.view.form.FormDetalleVenta;
import com.sisintegrados.view.form.FormDetalleVenta2;
import com.sisintegrados.view.form.FormClientePrepago;
import com.vaadin.data.Container;
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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
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
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.maddon.ListContainer;
import org.vaadin.ui.NumberField;

/**
 * @author Mery Gil
 */
public class PrCuadre extends Panel implements View {

    ComboBox cmbLubricante = new ComboBox("Lubricantes:");
    static final DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00");
    static final DecimalFormat numberFmt3D = new DecimalFormat("### ###,##0.000;-#");
    static final String HEIGHT_TABLE = "300px";

    VerticalLayout root;
    Table tblProd, tblMediospago, tblEfectivo, tblFactElect, tblDiferencias, tblCxC, tblLubricantes, tblPrepaid,
            tableBombas = new Table("Bombas:"), tblPartida, tblCreditCard,
            tableCalc = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("volumen") || colId.equals("venta") || colId.equals("diferencia")) {
                return numberFmt.format(property.getValue()).trim();
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };
    Button btnSave, btnAll, btnNone, btnAdd, btnAddCustomer, btnAddLubs, btnAddPrep, btnAddCreditC, btnDetail;

    /*Botones Detalles*/ //ASG
    Button btnLubricante;
    Button btnClienteCredito;
    Button btnClientePrepago;
    Button btnTarjetaCredito;
    /*fin */

    FormDetalleVenta frmDetalle;
    Label lblTotalVentas1 = new Label("0"),
            lblTotalVentas2 = new Label("0"),
            lblDiferencia = new Label("0"),
            lblUltimoDía = new Label("Último día:"),
            lblUltimoTurno = new Label("Último turno:"),
            lblChangeRate = new Label("", ContentMode.HTML);
    TextArea txaComentario = new TextArea("Comentario diferencia:");

    ComboBox cbxEmpleado,
            cbxPais = new ComboBox("País:"),
            cbxEstacion = new ComboBox("Estación:"),
            cbxTurno = new ComboBox("Turno:"),
            cbxArqueos = new ComboBox("Cuadre:");

    TextField tfdNameSeller, tfdNameChief;
    DateField dfdFecha = new DateField("Fecha:");
    Upload upload;

    Pais pais;
    Estacion estacion;
    Turno turno, ultimoTurno;
    Dia dia, ultimoDia;
    Container contTurnos = new ListContainer<>(Turno.class, new ArrayList()),
            contPais = new ListContainer<>(Pais.class, new ArrayList()),
            contArqueos = new ListContainer<>(Arqueocaja.class, new ArrayList()),
            contCustomerCredit = new ListContainer<>(Cliente.class, new ArrayList()),
            contCustomerPrepaid = new ListContainer<>(Cliente.class, new ArrayList()),
            contLubs = new ListContainer<>(Producto.class, new ArrayList()),
            contCreditC = new ListContainer<>(Cliente.class, new ArrayList());

    Utils utils = new Utils();
    Usuario user;
    List<Producto> productos;
    List<Bomba> allBombas;
    List<DtoEfectivo> listaEfectivo = new ArrayList();
    List<Mediopago> mediosPagoEfectivo;
    List<Bomba> bombasConCierre;
    List<Empleado> listEmpleados;
    TasaCambio tasacambio = new TasaCambio();
    List<DtoProducto> listCustomers = new ArrayList(), listLubs = new ArrayList(), listPrepaid = new ArrayList();

    BeanContainer<Integer, Bomba> bcrBombas = new BeanContainer<Integer, Bomba>(Bomba.class);
    BeanContainer<Integer, DtoArqueo> bcArqueo = new BeanContainer<Integer, DtoArqueo>(DtoArqueo.class);
    BeanContainer<Integer, Producto> bcrProducto = new BeanContainer<Integer, Producto>(Producto.class);
    BeanContainer<Integer, DtoEfectivo> bcEfectivo = new BeanContainer<Integer, DtoEfectivo>(DtoEfectivo.class);
    BeanContainer<Integer, Mediopago> bcrMediopago = new BeanContainer<Integer, Mediopago>(Mediopago.class),
            bcrPartida = new BeanContainer<Integer, Mediopago>(Mediopago.class);
    BeanContainer<Integer, DtoProducto> bcFactElect = new BeanContainer<Integer, DtoProducto>(DtoProducto.class),
            bcDiferencias = new BeanContainer<Integer, DtoProducto>(DtoProducto.class),
            bcrClientes = new BeanContainer<Integer, DtoProducto>(DtoProducto.class),
            bcrLubs = new BeanContainer<Integer, DtoProducto>(DtoProducto.class),
            bcrCreditC = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);

    Double totalArqueoVol = 0D, totalArqueoCurr = 0D, totalArqueoDif = 0D, totalArqueoElectronico = 0D;
    Double totalProducto = 0D, totalMediosPago = 0D, totalEfectivo = 0D;
    Double totalFEVolumen = 0D, totalFECurrency = 0D;
    //TODO: Definir (el volumen) basado en el pais de la estacion 
    String currencySymbol, volumenSymbol, tmpString;
    File tempFile;
    double tmpDouble;
    int tmpInt;
    List<Pais> allCountries;
    String[] uniqueStation;
    BeanItemContainer<Lubricanteprecio> contLubricante = new BeanItemContainer<Lubricanteprecio>(Lubricanteprecio.class);
    CreateComponents components = new CreateComponents();

    /*Detalle Clientes Prepago*/
    FormClientePrepago formClientePrepago;
    BeanContainer<Integer, DtoProducto> bcrPrepaid = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
    SvcClientePrepago dao = new SvcClientePrepago();

    public PrCuadre() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        currencySymbol = (user.getPaisLogin() == null) ? "" : user.getPaisLogin().getMonedaSimbolo() + ". ";
        volumenSymbol = "L"; //MG volSimbolo

        root = utils.buildVertical("vlMainLayout", true, true, true, true, "dashboard-view");
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        root.addComponent(utils.buildHeader("Cuadre de caja", true, true));
        root.addComponent(utils.buildSeparator());
        Responsive.makeResponsive(root);
        setContent(root);

        getAllData();
        buildTableBombas();
        buildTablesAdd();
        buildTableProdAdicionales();
        buildTableMediosPago();
        buildTablePartida();
        buildTableEfectivo();
        buildTableFactElect();
        buildTableDiferencias();
        buildButtons();
        buildFilters();

        TabSheet tabsheet = new TabSheet();
        tabsheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        HorizontalLayout hlLabels = utils.buildHorizontal("hlLabels", false, true, true, true);
        hlLabels.addComponents(lblUltimoDía, lblUltimoTurno, lblChangeRate);
        hlLabels.setComponentAlignment(lblChangeRate, Alignment.MIDDLE_LEFT);

        CssLayout cltCombos = new CssLayout(utils.vlContainer(cbxPais), utils.vlContainer(cbxEstacion), utils.vlContainer(dfdFecha), utils.vlContainer(cbxTurno)//, utils.vlContainer(cbxArqueos)
        );
        Responsive.makeResponsive(cltCombos);
        HorizontalLayout hlCombo = utils.buildHorizontal("hlCombo", false, true, true, true);
        hlCombo.addComponent(cltCombos);

        HorizontalLayout hlBombasButton = utils.buildHorizontal("hlBombasButton", false, true, true, false);
        hlBombasButton.addComponents(btnAll, btnNone);
        hlBombasButton.setComponentAlignment(btnAll, Alignment.MIDDLE_RIGHT);
        hlBombasButton.setComponentAlignment(btnNone, Alignment.MIDDLE_LEFT);
        VerticalLayout vlBombasAll = new VerticalLayout(tableBombas, hlBombasButton);
        vlBombasAll.setExpandRatio(tableBombas, .85f);
        vlBombasAll.setExpandRatio(hlBombasButton, .15f);
        Responsive.makeResponsive(vlBombasAll);
        vlBombasAll.setSizeUndefined();

        CssLayout cltVentas = new CssLayout(//vlBombasAll, 
                utils.vlContainer(tableCalc), utils.vlContainer(tblProd), utils.vlContainer(lblTotalVentas1) //, hltCalculation
        );
        cltVentas.setSizeFull();
        Responsive.makeResponsive(cltVentas);
//
        Label lbEspacio = new Label(" ");
        VerticalLayout vltEfectivo = utils.buildVertical("vlEfectivo", false, true, true, true, null);
        vltEfectivo.addComponents(tblEfectivo, btnAdd, lbEspacio);
        vltEfectivo.setExpandRatio(tblEfectivo, 0.9f);
        vltEfectivo.setExpandRatio(btnAdd, 0.1f);
        vltEfectivo.setComponentAlignment(btnAdd, Alignment.TOP_CENTER);
        vltEfectivo.setSizeUndefined();
        vltEfectivo.setMargin(new MarginInfo(false, true, false, false));

        CssLayout cltMedios = new CssLayout(utils.vlContainer(tblMediospago), vltEfectivo, utils.vlContainer(lblTotalVentas2));
        cltMedios.setSizeFull();
        Responsive.makeResponsive(cltMedios);

        CssLayout cltUpload = new CssLayout(utils.vlContainer(upload));
        cltUpload.setSizeFull();
        cltUpload.setVisible(false);
        Responsive.makeResponsive(cltUpload);

        CssLayout cltTaDiff = buildDetalleMontos();
        cltTaDiff.setSizeFull();
        cltTaDiff.addComponent(btnSave);

        /*ASG botones de detalles*/
        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setMargin(new MarginInfo(true, false, true, false));
        Label lblpuesto = new Label("Puesto");
        lblpuesto.setStyleName(ValoTheme.LABEL_SMALL);
        lblpuesto.setWidth("45px");
        hl.addComponent(lblpuesto);
        hl.addComponent(cbxEmpleado);
        hl.addComponent(btnLubricante);
        hl.addComponent(btnClienteCredito);
        hl.addComponent(btnClientePrepago);
        hl.addComponent(btnTarjetaCredito);

        /*fin botones detalle*/
        CssLayout cltEmpleado = new CssLayout(hl /**
         * , utils.vlContainer(tfdNameSeller), utils.vlContainer(tfdNameChief)*
         */
        );
        cltEmpleado.setSizeUndefined();
        Responsive.makeResponsive(cltEmpleado);

////Detalle ventas credito
//        buildTableCxC();
//        VerticalLayout vltCxC = utils.buildVertical("vltCxC", false, false, true, false, null);
//        vltCxC.addComponents(tblCxC, btnAddCustomer);
//        vltCxC.setComponentAlignment(btnAddCustomer, Alignment.TOP_CENTER);
////Detalle venta lubricantes
//        buildTableLubsDet();
//        VerticalLayout vltLubs = utils.buildVertical("vltLubs", false, false, true, false, null);
//        vltLubs.addComponents(tblLubricantes, btnAddLubs);
//        vltLubs.setComponentAlignment(btnAddLubs, Alignment.TOP_CENTER);
////Detalle venta lubricantes
//        buildTablePrepago();
//        VerticalLayout vltPrego = utils.buildVertical("vltPrego", false, false, true, false, null);
//        vltPrego.addComponents(tblPrepaid, btnAddPrep);
//        vltPrego.setComponentAlignment(btnAddPrep, Alignment.TOP_CENTER);
////Detalle venta lubricantes
//        buildTableCreditCard();
//        VerticalLayout vltCreditCard = utils.buildVertical("vltCreditCard", false, false, true, false, null);
//        vltCreditCard.addComponents(tblCreditCard, btnAddCreditC);
//        vltCreditCard.setComponentAlignment(btnAddCreditC, Alignment.TOP_CENTER);
//
//        final CssLayout cltCxc = new CssLayout(utils.vlContainer(vltCxC), utils.vlContainer(vltPrego), utils.vlContainer(vltLubs), utils.vlContainer(vltCreditCard));
//        cltCxc.setSizeUndefined();
//        cltCxc.setVisible(false);
//        Responsive.makeResponsive(cltCxc);
//        Panel pnlDetalles = new Panel("Detalles de venta", cltCxc);
//        pnlDetalles.setSizeFull();
//        pnlDetalles.addClickListener(new MouseEvents.ClickListener() {
//            @Override
//            public void click(MouseEvents.ClickEvent event) {
//                cltCxc.setVisible(!cltCxc.isVisible());
//            }
//        });
//        btnDetalles = new Button("Detalle venta"/**, cltCxc**/);
//        btnDetalles.setIcon(FontAwesome.EDIT);
//        btnDetalles.addClickListener(clickEvent -> formLubricantes("Nuevo"));
////        btnDetalles.addClickListener((final Button.ClickEvent event) -> {
//          //FormDetalleVenta.open();
////            @Override
////           public void click(MouseEvents.ClickEvent event) {
//          //    cltCxc.setVisible(!cltCxc.isVisible());
////           }
////       });
        CssLayout cltMain = new CssLayout(hlLabels, hlCombo, cltEmpleado, cltVentas, cltMedios, //utils.vlContainer(tblPartida), 
                cltUpload, cltTaDiff);
        Responsive.makeResponsive(cltMain);
        tabsheet.addTab(cltMain, "Principal", FontAwesome.LIST);

        root.addComponent(tabsheet);
        root.setExpandRatio(tabsheet, 1);

        defineInitialCountryStation();
    }

    private void formLubricantes(String tipo) {
        if (tipo.equals("Editar")) {
            if (cmbLubricante.getValue() != null) {
                Lubricanteprecio lubricante = new Lubricanteprecio();
                lubricante = (Lubricanteprecio) cmbLubricante.getValue();
                frmDetalle = new FormDetalleVenta(tipo, lubricante);
                frmDetalle.addCloseListener((e) -> {
                    cmbLubricante.removeAllItems();
                    contLubricante.removeAllItems();
                    contLubricante = new BeanItemContainer<Lubricanteprecio>(Lubricanteprecio.class);
//                    contLubricante.addAll(dao.getEmpleados2(true));
                    cmbLubricante.setContainerDataSource(contLubricante);
                    cmbLubricante.setItemCaptionPropertyId("nombre");
                    cmbLubricante.setStyleName(ValoTheme.COMBOBOX_TINY);
                    cmbLubricante.setRequired(true);
                    cmbLubricante.setRequiredError("Debe Seleccionar empleado");
                    cmbLubricante.setNullSelectionAllowed(false);
                    toolbarContainerCmbLubricantes.removeAllComponents();
                    toolbarContainerCmbLubricantes.addComponent(cmbLubricante);
                });
                getUI().addWindow(frmDetalle);
                frmDetalle.focus();
            } else {
                Notification.show("Warning!!!", "Debe seleccionar un empleado, para modificar", Notification.Type.WARNING_MESSAGE);
            }
        } else if (tipo.equals("Nuevo")) {
            Lubricanteprecio lub = new Lubricanteprecio();
            lub = (Lubricanteprecio) cmbLubricante.getValue();
            frmDetalle = new FormDetalleVenta(tipo, lub);
            frmDetalle.addCloseListener((e) -> {
                cmbLubricante.removeAllItems();
                contLubricante.removeAllItems();
                contLubricante = new BeanItemContainer<Lubricanteprecio>(Lubricanteprecio.class);
//                contLubricante.addAll(dao.getEmpleados2(true));
                cmbLubricante.setContainerDataSource(contLubricante);
                cmbLubricante.setItemCaptionPropertyId("nombre");
                cmbLubricante.setStyleName(ValoTheme.COMBOBOX_TINY);
                cmbLubricante.setRequired(true);
                cmbLubricante.setRequiredError("Debe Seleccionar empleado");
                cmbLubricante.setNullSelectionAllowed(false);
                toolbarContainerCmbLubricantes.removeAllComponents();
                toolbarContainerCmbLubricantes.addComponent(cmbLubricante);
            });
            getUI().addWindow(frmDetalle);
            frmDetalle.focus();
        }
    }
    private CssLayout toolbarContainerCmbLubricantes;
    private CssLayout toolbarContainerTableAsignacion;

    private Component buildTables() {
        VerticalLayout v = new VerticalLayout();
        HorizontalLayout h = new HorizontalLayout();
        Label lblpistero = new Label("Nombre Empleado");
        lblpistero.setStyleName(ValoTheme.LABEL_TINY);
        lblpistero.setWidth("100px");
        contLubricante = new BeanItemContainer<Lubricanteprecio>(Lubricanteprecio.class);
//        contLubricante.addAll(dao.getEmpleados2(true));
        cmbLubricante.setContainerDataSource(contLubricante);
        cmbLubricante.setItemCaptionPropertyId("nombre");
        cmbLubricante.setStyleName(ValoTheme.COMBOBOX_TINY);
        cmbLubricante.setRequired(true);
        cmbLubricante.setRequiredError("Debe Seleccionar Empleado");
        cmbLubricante.setNullSelectionAllowed(false);

        toolbarContainerCmbLubricantes = new CssLayout();
        toolbarContainerTableAsignacion = new CssLayout();
        toolbarContainerCmbLubricantes.addComponent(cmbLubricante);
        h.addComponent(lblpistero);
        h.addComponent(toolbarContainerCmbLubricantes);
        h.setSpacing(true);
        Component adicionBar = components.createCssLayout(Constant.styleViewheader2, Constant.sizeUndefined, false, false, true, new Component[]{h});
        v.addComponent(adicionBar);

        //tabla
        v.addComponent(toolbarContainerTableAsignacion);
        v.setSpacing(true);
        toolbarContainerTableAsignacion.removeAllComponents();
        return components.createCssLayout(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{utils.vlContainerTable(v)});
    }

    private void getAllData() {
        SvcCuadre service = new SvcCuadre();

        pais = (user.getPaisLogin() != null)
                ? user.getPaisLogin() : ((pais != null) ? pais : new Pais());
        allCountries = service.getAllPaises();
//        currencySymbol = (user.getPaisLogin() == null) ? "" : user.getPaisLogin().getMonedaSimbolo() + ". ";
//        volumenSymbol = "L"; //MG volSimbolo

        estacion = (Estacion) ((user.getEstacionLogin() != null)
                ? user.getEstacionLogin() : ((cbxEstacion != null && cbxEstacion.getValue() != null) ? cbxEstacion.getValue() : new Estacion()));

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
            if (t.getTurnoId() == null || !t.getTurnoId().equals(turno.getTurnoId())) {
                misTurnos.add(t);
            }
        }
        contTurnos = new ListContainer<Turno>(Turno.class, misTurnos);

        allBombas = service.getBombasByEstacionidTurnoid(estacion.getEstacionId(), turno.getTurnoId());
        bombasConCierre = service.getBombasCuadradasByTurnoid(turno.getTurnoId());

        List<Producto> productosPOS = service.getCombustiblesByEstacionidPOS(estacion.getEstacionId());
        List<Producto> prodAdicionales = service.getProdAdicionalesByEstacionid(estacion.getEstacionId());

        bcrMediopago.setBeanIdProperty("mediopagoId");
        bcrMediopago.addAll(service.getMediospagoByPaisidTipoid(estacion.getPaisId(), 1));   //genericos
        mediosPagoEfectivo = service.getMediospagoByPaisidTipoid(estacion.getPaisId(), 2);  //efectivo

        bcrPartida.setBeanIdProperty("mediopagoId");
        bcrPartida.addAll(service.getMediospagoByPaisidTipoid(estacion.getPaisId(), 1));   //genericos

        List<Arqueocaja> arqueos = service.getArqueocajaByTurnoid(turno.getTurnoId());
        Arqueocaja acaja = new Arqueocaja(null, estacion.getEstacionId(), turno.getTurnoId(), new Date(), 1, null, null, null, null, null);
        System.out.println("CAJA " + acaja);
        acaja.setNombre("Nuevo cuadre");
        arqueos.add(0, acaja);
        contArqueos = new ListContainer<Arqueocaja>(Arqueocaja.class, arqueos);
        System.out.println("ARQUEOS " + contArqueos);

        contCustomerCredit = new ListContainer<Cliente>(Cliente.class, service.getCustomersByStationidType(estacion.getEstacionId(), "C"));
        contCustomerPrepaid = new ListContainer<Cliente>(Cliente.class, service.getCustomersByStationidType(estacion.getEstacionId(), "P"));
        contLubs = new ListContainer<Producto>(Producto.class, service.getLubricantsByCountryStation(true, estacion.getPaisId(), estacion.getEstacionId()));
        contCreditC = new ListContainer<Cliente>(Cliente.class, service.getCreditCards());

        listEmpleados = new ArrayList();
        if (turno.getTurnoId() != null) {
            listEmpleados = service.getEmpleadosByTurnoid(turno.getTurnoId());
        }
        uniqueStation = service.getUniqueStation(user.getUsuarioId());
        service.closeConnections();

        bcrBombas.setBeanIdProperty("id");
        bcrBombas.removeAllItems();
        bcrBombas.addAll(allBombas);

        DtoProducto dpo;
        bcFactElect.setBeanIdProperty("productoId");
        bcDiferencias.setBeanIdProperty("productoId");
        bcrClientes.setBeanIdProperty("productoId");
        bcrLubs.setBeanIdProperty("productoId");
        bcrPrepaid.setBeanIdProperty("productoId");
        bcrCreditC.setBeanIdProperty("productoId");
        for (Producto p : productosPOS) {
            dpo = new DtoProducto(p.getProductoId(), p.getNombre(), null);
            dpo.initValorGalones();
            bcFactElect.addBean(dpo);
            bcDiferencias.addBean(dpo);
        }

        bcArqueo.setBeanIdProperty("id");
        bcArqueo.addAll(new ArrayList<DtoArqueo>());
        bcrProducto.setBeanIdProperty("productoId");
        bcrProducto.addAll(prodAdicionales);
        bcEfectivo.setBeanIdProperty("id");
        bcEfectivo.addAll(listaEfectivo);

    }

    private void buildFilters() {
        buildLabelInfo();

        cbxPais.setContainerDataSource(new ListContainer<>(Pais.class, allCountries));
        cbxPais.setItemCaptionPropertyId("nombre");
        cbxPais.setItemIconPropertyId("flag");
        cbxPais.setNullSelectionAllowed(false);
//        cbxPais.setVisible(user.isGerente() || user.isAdministrativo());
        cbxPais.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxPais.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                pais = (Pais) cbxPais.getValue();
                SvcCuadre service = new SvcCuadre();
                List<Estacion> listStations = service.getStationsByCountryUser(pais.getPaisId(), user.getUsuarioId());
                Container estacionContainer = new ListContainer<Estacion>(Estacion.class, listStations);
                bcrMediopago.addAll(service.getMediospagoByPaisidTipoid(pais.getPaisId(), 1));   //genericos
                mediosPagoEfectivo = service.getMediospagoByPaisidTipoid(pais.getPaisId(), 2);
                service.closeConnections();
                cbxEstacion.setContainerDataSource(estacionContainer);
                //limpiar
                dfdFecha.setValue(null);
                contTurnos = new ListContainer<>(Turno.class, new ArrayList());
                cbxTurno.setContainerDataSource(contTurnos);
                cbxTurno.setValue(null);
                if (listStations.size() == 1) {
                    cbxEstacion.setValue(listStations.get(0));
                }
                currencySymbol = pais.getMonedaSimbolo() + " ";
                volumenSymbol = pais.getVolSimbolo() + " ";
                System.out.println("PAIS " + pais.toString());

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
                System.out.println("estacion: " + estacion.getEstacionId());
                dfdFecha.setValue(null);
                contTurnos = new ListContainer<>(Turno.class, new ArrayList());
                System.out.println("contTurnos: " + contTurnos.size());
                cbxTurno.setContainerDataSource(contTurnos);
                cbxTurno.setValue(null);
                contArqueos = new ListContainer<>(Arqueocaja.class, new ArrayList());
                cbxArqueos.setContainerDataSource(contArqueos);
                cbxArqueos.setValue(null);
                SvcCuadre service = new SvcCuadre();
                List<Producto> prodAdicionales = service.getProdAdicionalesByEstacionid(estacion.getEstacionId());
                System.out.println("prodAdicionales: " + prodAdicionales.size());
                bcrProducto.removeAllItems();
                bcrProducto.addAll(prodAdicionales);
                ultimoDia = (ultimoDia.getFecha() == null) ? service.getUltimoDiaByEstacionid(estacion.getEstacionId()) : ultimoDia;
                dia = service.getDiaActivoByEstacionid(estacion.getEstacionId());
                ultimoTurno = (ultimoTurno.getTurnoId() == null) ? service.getUltimoTurnoByEstacionid(estacion.getEstacionId()) : ultimoTurno;
                turno = service.getTurnoActivoByEstacionid(estacion.getEstacionId());
                turno = (turno.getEstadoId() == null) ? ultimoTurno : turno; ///ASG
                tasacambio = service.getTasacambioByPaisFecha(pais.getPaisId(), turno.getFecha());

                contCustomerCredit = new ListContainer<>(Cliente.class, service.getCustomersByStationidType(estacion.getEstacionId(), "C"));
                contCustomerPrepaid = new ListContainer<>(Cliente.class, service.getCustomersByStationidType(estacion.getEstacionId(), "P"));
                contLubs = new ListContainer<>(Producto.class, service.getLubricantsByCountryStation(true, estacion.getPaisId(), estacion.getEstacionId()));
                bcrClientes.removeAllItems();
                bcrPrepaid.removeAllItems();

                service.closeConnections();
//                determinarPermisos();

            }
        });

        dfdFecha.setDateFormat("dd/MM/yyyy");
        dfdFecha.setRangeEnd(Date.from(Instant.now()));
        dfdFecha.setLocale(new Locale("es", "ES"));
        dfdFecha.addStyleName(ValoTheme.DATEFIELD_SMALL);
        dfdFecha.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (dfdFecha.getValue() != null) {
                    SvcTurno svcTurno = new SvcTurno();
                    //se obtiene de base de datos pues necesitamos saber el estado.
                    dia = svcTurno.getDiaByEstacionidFecha(estacion.getEstacionId(), dfdFecha.getValue());
                    dia.setFecha((dia.getFecha() == null) ? dfdFecha.getValue() : dia.getFecha());  //dia es siempre el mismo que lo seleccionado en el control
                    List<Turno> listTurno = svcTurno.getTurnosByEstacionidDiaNolectura(estacion.getEstacionId(), dfdFecha.getValue());
                    svcTurno.closeConnections();
                    contTurnos = new ListContainer<>(Turno.class, listTurno);
                    cbxTurno = (cbxTurno == null) ? new ComboBox("Turno:") : cbxTurno;
                    cbxTurno.setContainerDataSource(contTurnos);
                    cbxTurno.setValue(null);
                    bcrBombas.removeAllItems();
                    bcArqueo.removeAllItems();
//                    bcrProducto.removeAllItems();
                    bcEfectivo.removeAllItems();
                    recalcularFooter();
                    contArqueos.removeAllItems();
                    cbxArqueos.setContainerDataSource(contArqueos);
                    cbxArqueos.setValue(null);
                    for (Integer mpid : bcrMediopago.getItemIds()) {
                        bcrMediopago.getItem(mpid).getItemProperty("cantidad").setValue(0);
                        bcrMediopago.getItem(mpid).getItemProperty("value").setValue(0D);
                    }
                    for (Integer mpid : bcrProducto.getItemIds()) {
                        bcrProducto.getItem(mpid).getItemProperty("value").setValue(0D);
                    }
                    turno = new Turno();
                    if (!contTurnos.getItemIds().isEmpty()) {
                        int lastIndex = listTurno.size() - 1;
                        turno = (Turno) ((ArrayList) contTurnos.getItemIds()).get(lastIndex);
                        cbxTurno.setValue(turno);
                        actionComboboxTurno();
                        for (Object ac : contArqueos.getItemIds()) {
                            if (((Arqueocaja) ac).getArqueocajaId() == null) {
                                cbxArqueos.setValue(ac);
                            }
                        }
                    }
//                    determinarPermisos();
                    buildLabelInfo();
                }
            }
        });
        dfdFecha.setValue(dia.getFecha());

        cbxTurno.setContainerDataSource(contTurnos);
        cbxTurno.setItemCaptionPropertyId("nombre");
        cbxTurno.setNullSelectionAllowed(false);
        cbxTurno.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxTurno.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
//                turno = (Turno) cbxTurno.getValue();
//                System.out.println("turno: "+turno.getTurnoId().toString());
//                SvcCuadre service = new SvcCuadre();
//                listEmpleados = service.getEmpleadosByTurnoid(turno.getTurnoId());
//                System.out.println("listEmpleados: "+listEmpleados.size());
//                service.closeConnections();
//                cbxEmpleado.setContainerDataSource(new ListContainer<>(Empleado.class, listEmpleados));
//                actionComboboxTurno();
//                determinarPermisos();
                turno = (Turno) cbxTurno.getValue();
                if (turno != null) {
                    SvcCuadre service = new SvcCuadre();
                    listEmpleados = service.getEmpleadosByTurnoid2(turno.getTurnoId());
                    cbxEmpleado.setContainerDataSource(new ListContainer<>(Empleado.class, listEmpleados));
                    actionComboboxTurno();
//                    determinarPermisos();
                }
            }
        });

//        cbxTurno.setValue(turno);
        cbxArqueos = utils.buildCombobox("Arqueos:", "nombre", false, false, ValoTheme.COMBOBOX_SMALL, contArqueos);
//        cbxArqueos.setContainerDataSource(contArqueos);
//        cbxArqueos.setItemCaptionPropertyId("nombre");
//        cbxArqueos.setNullSelectionAllowed(false);
//        cbxArqueos.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxArqueos.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Arqueocaja acaja = (Arqueocaja) cbxArqueos.getValue();
                if (acaja != null && acaja.getArqueocajaId() != null) {
                    onchangeCbxArqueo(acaja.getArqueocajaId().toString());
                }
            }
        });

        cbxEmpleado = utils.buildCombobox(null, "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(Empleado.class, listEmpleados));
        cbxEmpleado.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                tfdNameSeller.setValue(null);
                tfdNameChief.setValue(null);
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

//                SvcCuadre service = new SvcCuadre();
//                String[] nombres = service.getEmpleadoByEstacionTurnoEmpleado(estacion.getEstacionId(), turno.getTurnoId(), ((Empleado) cbxEmpleado.getValue()).getEmpleadoId());
                Arqueocaja arqueocaja = ((Empleado) cbxEmpleado.getValue()).getArqueo();
                if (arqueocaja != null) {
                    tfdNameSeller.setValue(arqueocaja.getNombrePistero());
                    tfdNameChief.setValue(arqueocaja.getNombreJefe());
                    onchangeCbxArqueo(arqueocaja.getArqueocajaId().toString());

                    /*Recupera Detalle Cliente Prepago*/ //ASG
                    bcrPrepaid = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
                    bcrPrepaid = dao.getDetallePrepago(arqueocaja.getArqueocajaId());
//                    for (Integer itemId : bcrPrepaid.getItemIds()) {
//                        System.out.println(" CLIENTE ID "+bcrPrepaid.getItem(itemId).getBean().getCliente().getNombre());
//                        System.out.println(" CLIENTE NOMBRE "+bcrPrepaid.getItem(itemId).getBean().getCliente().getNombre());
//                        System.out.println(" MONTO "+bcrPrepaid.getItem(itemId).getBean().getValor());
//                    }

//                    System.out.println("ID CAJA RECUPERAR " + arqueocaja.getArqueocajaId());
                    //Fin
                }
//                service.closeConnections();

            }
        });

        tfdNameSeller = utils.buildTextField("Pistero:", "", false, 50, true, ValoTheme.TEXTFIELD_SMALL);

        tfdNameChief = utils.buildTextField("Jefe de pista:", "", false, 50, true, ValoTheme.TEXTFIELD_SMALL);

        upload = new Upload("Selección:", new Upload.Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                //Validaciones
                try {
                    Constant constant = new Constant();
                    if (constant.MAP_MIMETYPES_EXT.containsKey(mimeType)) {
                        System.out.println("receiveUpload::: " + filename + "; " + mimeType);
                        tmpString = "Faltante" + user.getEstacionLogin().getNombre().replaceAll(" ", "-").concat(Constant.SDF_yyyyMMddHHmmss.format(new Date()));
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
        upload.addStyleName(ValoTheme.BUTTON_SMALL);

//        determinarPermisos();
    }

    private void determinarPermisos() {
        boolean explorar = false, editar = false, crearCuadre = false;
        try {
            if (dia.getEstadoId() == null && (turno == null || turno.getEstadoId() == null) && dia.getFecha() != null && ultimoDia.getFecha() != null
                    && (dia.getFecha().equals(ultimoDia.getFecha()) || dia.getFecha().after(ultimoDia.getFecha()))) {
                explorar = crearCuadre = true;
            } else if (dia.getEstadoId() == null && (turno == null || turno.getEstadoId() == null) && dia.getFecha() != null && ultimoDia.getFecha() != null
                    && dia.getFecha().before(ultimoDia.getFecha())) {
                explorar = true;
            } else if ((user.getRolLogin().equals(Constant.ROL_LOGIN_CAJERO)
                    || user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR))
                    && dia.getEstadoId() != null && dia.getEstadoId() == 2
                    && turno.getEstadoId() != null && turno.getEstadoId() == 2) {
                explorar = true;
            } else if (user.getRolLogin().equals(Constant.ROL_LOGIN_CAJERO) && dia.getEstadoId() == 1 && turno.getEstadoId() == 2) {
                explorar = true;
            } else if (user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR)
                    && dia.getEstadoId() != null && dia.getEstadoId() == 1
                    && turno.getEstadoId() != null && turno.getEstadoId() == 2) {
                explorar = crearCuadre = true;
            } else if ((user.getRolLogin().equals(Constant.ROL_LOGIN_CAJERO) || user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR))
                    && dia.getEstadoId() != null && dia.getEstadoId() == 1
                    && turno.getEstadoId() != null && turno.getEstadoId() == 1) {
//            explorar = false;
                explorar = true;
                editar = crearCuadre = true;
            } else if (user.isAdministrativo()) {
                explorar = true;
                crearCuadre = true;
            } else if (user.isGerente()) {
                explorar = crearCuadre = true;
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        dfdFecha.setEnabled(explorar);   //habilitado
        cbxTurno.setEnabled(explorar);    //habilitado
//        crearCuadre = (editar && crearCuadre)
        btnSave.setEnabled(crearCuadre);    //habilitado (cerrado)
    }

    private void recalcularFooter() {
        totalArqueoVol = totalArqueoCurr = totalArqueoDif = 0D;
        tableCalc.setColumnFooter("nombreDespacho", "Total:");
        tableCalc.setColumnFooter("volumen", volumenSymbol + numberFmt.format(totalArqueoVol));
        tableCalc.setColumnFooter("venta", currencySymbol + numberFmt.format(totalArqueoCurr));
        tableCalc.setColumnFooter("diferencia", currencySymbol + numberFmt.format(totalArqueoDif));
        System.out.println("RECALCULAR " + totalArqueoDif);

        totalProducto = 0D;
        tblProd.setColumnFooter("nombre", "Total:");
        tblProd.setColumnFooter("monto", currencySymbol + numberFmt.format(totalProducto));
        totalMediosPago = 0D;
        tblMediospago.setColumnFooter("nombre", "Total:");
        tblMediospago.setColumnFooter("colMonto", currencySymbol + numberFmt.format(totalMediosPago));
        totalEfectivo = 0D;
        tblEfectivo.setColumnFooter("colMPname", "Total:");
        tblEfectivo.setColumnFooter("colMonto", currencySymbol + numberFmt.format(totalEfectivo));

        totalArqueoElectronico = 0D;
        updateTotalVentas(totalArqueoElectronico + totalProducto);

        sumarEfectivo();

    }

    private void actionComboboxTurno() {
        if (turno != null) {
            SvcCuadre service = new SvcCuadre();
            List<Arqueocaja> arqueos = service.getArqueocajaByTurnoid(turno.getTurnoId());
            if (user.getEstacionLogin() == null) {
                allBombas = service.getBombasByEstacionidTurnoid(estacion.getEstacionId(), turno.getTurnoId());
                productos = service.getCombustiblesByEstacionid(estacion.getEstacionId());
            }

            tasacambio = service.getTasacambioByPaisFecha(pais.getPaisId(), turno.getFecha());

            service.closeConnections();

            if (!user.isGerente() && !user.isAdministrativo()) {
                Arqueocaja acaja = new Arqueocaja(null, estacion.getEstacionId(), turno.getTurnoId(), new Date(), 1, null, null, null, null, null);
                acaja.setNombre("Nuevo cuadre");
                arqueos.add(0, acaja);
            }
            contArqueos = new ListContainer<Arqueocaja>(Arqueocaja.class, arqueos);
            cbxArqueos.setContainerDataSource(contArqueos);
            cbxArqueos.setValue(contArqueos.getItem(0));

            bcrBombas.removeAllItems();
            bcrBombas.addAll(allBombas);
        }
    }

    private void buildButtons() {

        btnLubricante = new Button("Lubricantes", FontAwesome.PLUS);
        btnLubricante.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnLubricante.addStyleName(ValoTheme.BUTTON_SMALL);
//        btnLubricante.addClickListener(clickEvent -> metodo1());

        btnClienteCredito = new Button("Clientes Credito", FontAwesome.PLUS);
        btnClienteCredito.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnClienteCredito.addStyleName(ValoTheme.BUTTON_SMALL);
//        btnClienteCredito.addClickListener(clickEvent -> metodo1());

        btnClientePrepago = new Button("Clientes Prepago", FontAwesome.PLUS);
        btnClientePrepago.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnClientePrepago.addStyleName(ValoTheme.BUTTON_SMALL);
        btnClientePrepago.addClickListener(clickEvent -> formPrepago(estacion.getEstacionId(), currencySymbol, pais.getPaisId()));

        btnTarjetaCredito = new Button("Tarjetas Credito", FontAwesome.PLUS);
        btnTarjetaCredito.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnTarjetaCredito.addStyleName(ValoTheme.BUTTON_SMALL);
//        btnTarjetaCredito.addClickListener(clickEvent -> metodo1());

        btnAll = new Button("Todas");
        btnAll.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btnAll.addStyleName(ValoTheme.BUTTON_LINK);
        btnAll.setIcon(FontAwesome.CHECK_SQUARE_O);
        btnAll.setEnabled(turno.getTurnoId() != null);    //habilitado
        btnAll.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                for (Integer i : (List<Integer>) tableBombas.getItemIds()) {
                    CheckBox myChk = (CheckBox) tableBombas.getColumnGenerator("colSelected").generateCell(tableBombas, i, "selected");
                    if (myChk.isEnabled()) {    //esta habilitada?
                        tableBombas.getItem(i).getItemProperty("selected").setValue(true);
                    }
                }
            }
        });

        btnNone = new Button("Ninguna");
        btnNone.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btnNone.addStyleName(ValoTheme.BUTTON_LINK);
        btnNone.setIcon(FontAwesome.SQUARE_O);
        btnNone.setEnabled(turno.getTurnoId() != null);    //habilitado
        btnNone.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                List<Integer> ids = (List<Integer>) tableBombas.getItemIds();
                for (Integer i : ids) {
                    tableBombas.getItem(i).getItemProperty("selected").setValue(false);
                }
            }
        });

        btnSave = utils.buildButton("Grabar", FontAwesome.SAVE, ValoTheme.BUTTON_FRIENDLY);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                //Habia una validacion en que era necesario que hubieran bombas seleccionadas, ya no es necesaria 
                //dado que ahora deben poderse crear cuadres sin asociar a una bomba esto para los cuadres de tiendas.
                totalArqueoElectronico = twoDecimal(totalArqueoElectronico.doubleValue());
                if ((totalArqueoElectronico + totalProducto) == 0) {
                    Notification.show("ADVERTENCIA:", "No existe calculo de ventas", Notification.Type.WARNING_MESSAGE);
                    return;
                }
                /*Eliminado porque ya no se usaran combos tfdNameChief tfdNameSeller*/
//                if (!cbxEmpleado.isValid()
//                        || !tfdNameChief.isValid() || tfdNameChief.getValue().trim().isEmpty()
//                        || !tfdNameSeller.isValid() || tfdNameSeller.getValue().trim().isEmpty()) {
//                    Notification.show("Existen campos vacios que son obligatorios.", Notification.Type.ERROR_MESSAGE);
//                    return;
//                }

                final double diferencia = (totalMediosPago + totalEfectivo) - (totalArqueoElectronico + totalProducto);

                String messageComp = "";
                if (diferencia > 0) {
                    for (Integer itemId : bcrProducto.getItemIds()) {
                        if (itemId.equals(Constant.CAT_PROD_DIFERENCIA_POSITIVA)) {
                            bcrProducto.getItem(itemId).getItemProperty("value").setValue(diferencia);
                        }
                    }
                } else {
                    // Notification.show("DIFERENCIA",Double.toString(diferencia),Notification.Type.ERROR_MESSAGE);
                    messageComp = (diferencia < -0.009 || diferencia > 0.009)
                            ? "<h3>El turno tiene diferencia de: <strong>" + lblDiferencia + "</strong></h3>\n" : "";
                    if (diferencia < -0.009 || diferencia > 0.009) {
                        Notification.show("Existe diferencia, asociela con el medio de pago correspondiente.", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                    if (diferencia < 0.00 && tempFile != null) {
                        Notification.show("ADVERTENCIA:", "Existe diferencia en sus calculos, debe adjuntar un documento", Notification.Type.WARNING_MESSAGE);
                        return;
                    }
                }

                MessageBox
                        .createQuestion()
                        .withCaption("Confirmación")
                        .withHtmlMessage(messageComp.concat("¿Seguro desea cuadrar/actualizar el turno para las bomba seleccionadas?"))
                        .withOkButton(new Runnable() {
                            public void run() {

                                SvcTurno svcTurno = new SvcTurno();

//TODO: Considerar hacer con commit y rollback este conjunto de interacciones a base de datos.
                                Arqueocaja arqueo = ((Empleado) cbxEmpleado.getValue()).getArqueo();
                                String myAction = (arqueo == null || arqueo.getArqueocajaId() == null) ? Dao.ACTION_ADD : Dao.ACTION_UPDATE;
                                System.out.println("ADD " + Dao.ACTION_ADD);
                                System.out.println("UPDATE " + Dao.ACTION_UPDATE);
                                arqueo = (arqueo == null || arqueo.getArqueocajaId() == null)
                                        ? new Arqueocaja(null, estacion.getEstacionId(), turno.getTurnoId(), turno.getFecha(), 1, user.getUsername(), user.getNombreLogin(), ((Empleado) cbxEmpleado.getValue()).getEmpleadoId(), tfdNameChief.getValue(), tfdNameSeller.getValue())
                                        : arqueo;
                                arqueo.setCreado_por(user.getUsername());
                                arqueo.setCreado_persona(user.getNombreLogin());
                                arqueo.setModificadoPor(user.getUsername());
                                arqueo.setModificadoPersona(user.getNombreLogin());
                                arqueo = svcTurno.saveArqueo(myAction, arqueo);

                                List<Integer> lecturasIds = (List<Integer>) tblMediospago.getItemIds();
                                Mediopago medio;
                                ArqueocajaDetalle ad = new ArqueocajaDetalle(arqueo.getArqueocajaId(), null, null, null, null);
                                svcTurno.saveArqueoDetalle(Dao.ACTION_DELETE, ad);
                                for (Integer id : lecturasIds) {
                                    medio = (Mediopago) ((BeanItem) tblMediospago.getItem(id)).getBean();
                                    if (medio.getValue() > 0) {
                                        ad = new ArqueocajaDetalle(arqueo.getArqueocajaId(), medio.getMediopagoId(), medio.getCantidad(), medio.getValue(), user.getUsername());
                                        ad.setModificadoPor(user.getUsername());
                                        svcTurno.saveArqueoDetalle(Dao.ACTION_ADD, ad);
                                    }
                                }

                                svcTurno.saveArqueoBomba(Dao.ACTION_DELETE, new ArqueocajaBomba(arqueo.getArqueocajaId(), null, null)); //las bombas que pertenecen al arqueo.
                                for (Integer bid : (List<Integer>) bcrBombas.getItemIds()) {
                                    if (bcrBombas.getItem(bid).getBean().getSelected()) {
                                        svcTurno.saveArqueoBomba(Dao.ACTION_DELETE, new ArqueocajaBomba(null, bid, turno.getTurnoId()));    //las agregadas pero ya pertenecian a otro turno.
                                        svcTurno.saveArqueoBomba(Dao.ACTION_ADD, new ArqueocajaBomba(arqueo.getArqueocajaId(), bid, turno.getTurnoId()));
                                    }
                                }

                                lecturasIds = (List<Integer>) tblProd.getItemIds();
                                Producto producto;
                                svcTurno.saveArqueoProducto(Dao.ACTION_DELETE, new ArqueocajaProducto(arqueo.getArqueocajaId(), null, null));
                                for (Integer id : lecturasIds) {
                                    producto = (Producto) ((BeanItem) tblProd.getItem(id)).getBean();
                                    if (producto.getValue() > 0) {
                                        ArqueocajaProducto apo = new ArqueocajaProducto(arqueo.getArqueocajaId(), producto.getProductoId(), producto.getValue());
                                        svcTurno.saveArqueoProducto(Dao.ACTION_ADD, apo);
                                    }
                                }

                                lecturasIds = (List<Integer>) tblEfectivo.getItemIds();
                                DtoEfectivo dtoE;
                                Efectivo efectivo;
                                svcTurno.doActionEfectivo(Dao.ACTION_DELETE, new Efectivo(arqueo.getArqueocajaId(), null, null, null));
                                for (Integer id : lecturasIds) {
                                    dtoE = (DtoEfectivo) ((BeanItem) tblEfectivo.getItem(id)).getBean();
                                    if (dtoE.getValue() > 0 && dtoE.getMedioPago() != null) {
                                        efectivo = new Efectivo(arqueo.getArqueocajaId(), dtoE.getMedioPago().getMediopagoId(), 0, dtoE.getValue());
                                        efectivo.setTasa(dtoE.getTasa());
                                        efectivo.setMonExtranjera(dtoE.getMonExtranjera());
                                        svcTurno.doActionEfectivo(Dao.ACTION_ADD, efectivo);
                                    }
                                }

                                lecturasIds = (List<Integer>) tblFactElect.getItemIds();
                                DtoProducto dtoP;
                                FactelectronicaPos fepos;
//                                svcTurno.doActionFactelectronicaPos(Dao.ACTION_DELETE, new FactelectronicaPos(arqueo.getArqueocajaId(), null, null, null));
                                for (Integer id : lecturasIds) {
                                    dtoP = (DtoProducto) ((BeanItem) tblFactElect.getItem(id)).getBean();
                                    if (dtoP.getGalones() > 0) {
                                        fepos = new FactelectronicaPos(arqueo.getArqueocajaId(), dtoP.getProductoId(), dtoP.getGalones(), dtoP.getValor());
//                                        svcTurno.doActionFactelectronicaPos(Dao.ACTION_ADD, fepos);
                                    }
                                }

                                tmpString = (user.getPaisLogin() != null) ? user.getPaisLogin().getNombre() : ((Pais) cbxPais.getValue()).getNombre();
                                Parametro parametro = svcTurno.getParameterByName("CORREO_CALIBRACIONES_" + tmpString.toUpperCase().replaceAll(" ", ""));

                                svcTurno.closeConnections();

                                if (arqueo.getArqueocajaId() != null) {

                                    if (diferencia < 0 && myAction.equals(Dao.ACTION_ADD)) {
                                        tmpString = (user.getEstacionLogin() != null) ? user.getEstacionLogin().getNombre() : ((Estacion) cbxEstacion.getValue()).getNombre();
                                        System.out.println("PrCuadre.doSave::: " + tmpString + "; " + turno.getTurnoId());
                                        Mail mail = new Mail(parametro.getValor(), "Web COCOs - Diferencia " + tmpString,
                                                String.format("Se ha encontrado una diferencia en el cuadre de caja de la estación %s del turno: %s", tmpString, turno.getTurnoId()),
                                                new ArrayList(Arrays.asList(tempFile.getAbsolutePath())));
                                        mail.run();
                                    }

                                    //*Registro detalle de clientes*// ASG
                                    try {
                                        dao.CreaClienteDetalle(arqueo.getArqueocajaId(), bcrPrepaid, user.getUsername());
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                    }
                                    /*fin registro detalle*/

                                    myAction = (myAction.equals(Dao.ACTION_ADD)) ? "cuadrado" : "actualizado";
                                    Notification notif = new Notification("ÉXITO:", "Se ha " + myAction + " las bombas con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                                    notif.setDelayMsec(3000);
                                    notif.setPosition(Position.MIDDLE_CENTER);
                                    //notif.setStyleName("mystyle");
                                    //notif.setIcon(new ThemeResource("img/reindeer.png"));
                                    notif.show(Page.getCurrent());
                                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PR_SQUAREUP.getViewName());
                                } else {
                                    Notification.show("Ocurrió un error al cerrar/cuadrar el turno. \n" + turno.getDescError(), Notification.Type.ERROR_MESSAGE);
                                }

                            }
                        },
                                new ButtonOption() {
                            @Override
                            public void apply(MessageBox mb, Button button) {
//                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                            }
                        }
                        )
                        .withCancelButton(new Runnable() {
                            public void run() {
                                /*Nothing to do here*/
                            }
                        },
                                new ButtonOption() {
                            @Override
                            public void apply(MessageBox mb, Button button) {
//                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                            }
                        })
                        .open();
                //Fin MessageBox

            }
        });

    }

    /*Metodo Llama Forma Clientes Prepago*///ASG
    private void formPrepago(Integer idestacion, String simboloMoneda, Integer idpais) {
        if (cbxEmpleado.getValue() != null) {
            formClientePrepago = new FormClientePrepago(idestacion, simboloMoneda, idpais,bcrPrepaid);
            formClientePrepago.addCloseListener((e) -> {
                bcrPrepaid = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
                bcrPrepaid = (BeanContainer<Integer, DtoProducto>) VaadinSession.getCurrent().getAttribute("detallePrepago");
                tmpDouble = (Double) VaadinSession.getCurrent().getAttribute("totalPrepago");
                for (Integer itemId : bcrMediopago.getItemIds()) {
                    if (bcrMediopago.getItem(itemId).getBean().getMediopagoId() == Constant.MP_CRI_VENTA_PREPAGO) {
                        bcrMediopago.getItem(itemId).getItemProperty("value").setValue(tmpDouble);
                        break;
                    }
                }
            });
            getUI().addWindow(formClientePrepago);
            formClientePrepago.focus();
        }
    }

    private void buildTableBombas() {
        tableBombas.setContainerDataSource(bcrBombas);
        tableBombas.setVisibleColumns(new Object[]{"tipoDespachoName", "nombre"});
        tableBombas.setColumnHeaders(new String[]{"Tipo", "Bomba"});
        tableBombas.setSizeUndefined();
        tableBombas.setHeight(HEIGHT_TABLE);
        tableBombas.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tableBombas.addStyleName(ValoTheme.TABLE_COMPACT);
        tableBombas.addStyleName(ValoTheme.TABLE_SMALL);
        tableBombas.addGeneratedColumn("colSelected", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("selected");  //Atributo del bean
                final CheckBox cb = new CheckBox("", pro);
                //Inhabilitar checkbo si ya tiene "cuadre"
                cb.setEnabled(true);
                if (cbxArqueos != null && cbxArqueos.getValue() != null
                        && ((Arqueocaja) cbxArqueos.getValue()).getArqueocajaId() == null) {
                    for (Bomba bcc : bombasConCierre) {
                        if (bcc.getId().equals(itemId)) {
                            cb.setEnabled(false);   //habilitado?
                        }
                    }
                }

                cb.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        onChangeCheckboxBomba(itemId, cb.getValue());
                    }
                });
                return cb;
            }
        });
        tableBombas.setVisibleColumns("colSelected", "tipoDespachoName", "nombre");
        tableBombas.setColumnHeaders("", "Tipo", "Bomba");
        tableBombas.setColumnAlignment("selected", Table.Align.CENTER);
    }

    private void buildTablesAdd() {
        tableCalc.setCaption("Arqueo manuales:");
        tableCalc.setContainerDataSource(bcArqueo);
        Object[] vColumns = new Object[]{"nombreDespacho", "nombreProducto", "volumen", "venta"};
        String[] cHeaders = new String[]{"Despacho", "Producto", "Volumen", "Venta"};
        Align[] cAlignments = new Align[]{Table.Align.LEFT, Table.Align.LEFT, Table.Align.RIGHT, Table.Align.RIGHT};
        if (pais.getPaisId() != null && pais.getPaisId() == 320) {  //Guatemala
            System.out.println("PAIS " + pais.getPaisId());
            vColumns = new Object[]{"nombreDespacho", "nombreProducto", "volumen", "venta", "diferencia"};
            cHeaders = new String[]{"Despacho", "Producto", "Volumen", "Venta", "Diferencia"};
            cAlignments = new Align[]{Table.Align.LEFT, Table.Align.LEFT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT};
        }

        tableCalc.setVisibleColumns(vColumns);
        tableCalc.setColumnHeaders(cHeaders);
        tableCalc.setColumnAlignments(cAlignments);
        tableCalc.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tableCalc.addStyleName(ValoTheme.TABLE_COMPACT);
        tableCalc.addStyleName(ValoTheme.TABLE_SMALL);
        tableCalc.setImmediate(true);
        tableCalc.setSizeUndefined();
        tableCalc.setHeight(HEIGHT_TABLE);
        tableCalc.setFooterVisible(true);
        tableCalc.setColumnFooter("nombreDespacho", "Total:");
        tableCalc.setColumnFooter("volumen", volumenSymbol + numberFmt.format(totalArqueoVol));
        tableCalc.setColumnFooter("venta", currencySymbol + numberFmt.format(totalArqueoCurr));
        tableCalc.setColumnFooter("diferencia", currencySymbol + numberFmt.format(totalArqueoDif));
    }

    private void buildTableProdAdicionales() {
        tblProd = utils.buildTable("Otras ventas:", 95f, 100f, bcrProducto,
                new String[]{"nombre"},
                new String[]{"Nombre"});
        tblProd.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblProd.addStyleName(ValoTheme.TABLE_COMPACT);
        tblProd.addStyleName(ValoTheme.TABLE_SMALL);
        tblProd.setSizeUndefined();
        tblProd.setHeight(HEIGHT_TABLE);
        tblProd.addGeneratedColumn("colMonto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("value");  //Atributo del bean
                final TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                tfdValue.setValue("0.00");
                tfdValue.setWidth("100px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        String value = tfdValue.getValue();
                        value = (value != null) ? value.replaceAll(",", "").trim() : value;
                        if (value != null && !value.isEmpty() && value.matches("(\\d+(\\.\\d+)?)|(\\.\\d+)")) {
                            List<Integer> ids = (List<Integer>) bcrProducto.getItemIds();
                            totalProducto = 0D;
                            for (Integer id : ids) {
                                totalProducto += Double.parseDouble(bcrProducto.getItem(id).getItemProperty("value").getValue().toString());
                            }
                            tblProd.setColumnFooter("colMonto", currencySymbol + numberFmt.format(totalProducto));
                            updateTotalVentas(totalArqueoElectronico + totalProducto);

                            updateDiferencia((totalMediosPago + totalEfectivo) - (totalArqueoElectronico + totalProducto));
                        } else if (value != null) {
//                            tfdValue.setValue("0");
                            bcrProducto.getItem(itemId).getItemProperty("value").setValue(0D);
                        }
                    }
                });
                return tfdValue;
            }
        });
        tblProd.setVisibleColumns(new Object[]{"nombre", "colMonto"});
        tblProd.setColumnHeaders(new String[]{"Nombre", "Monto"});
        tblProd.setColumnAlignment("monto", Align.RIGHT);
        tblProd.setFooterVisible(true);
        tblProd.setColumnFooter("nombre", "Total:");
        tblProd.setColumnFooter("monto", currencySymbol + numberFmt.format(totalProducto));
    }

    private void buildTableMediosPago() {
        tblMediospago = utils.buildTable("Medios de pago:", 100f, 100f, bcrMediopago,
                new String[]{"nombre"},
                new String[]{"Nombre"});
        tblMediospago.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblMediospago.addStyleName(ValoTheme.TABLE_COMPACT);
        tblMediospago.addStyleName(ValoTheme.TABLE_SMALL);
        tblMediospago.setId("_tblMediospago_");
        tblMediospago.addGeneratedColumn("colCantDoctos", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cantidad");  //Atributo del bean
                final TextField tfdValue = new TextField(utils.getPropertyFormatterInteger(pro));
                tfdValue.setValue("0");
                tfdValue.setNullRepresentation("0");
                tfdValue.setWidth("100px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        String value = tfdValue.getValue();
                        value = (value != null) ? value.replaceAll(",", "").trim() : value;
                        //La validacion de 999 es porque el campo es para un numero de documentos por turno
                        if (value != null && !value.trim().isEmpty() && value.matches("\\d+") && Integer.parseInt(value) < 999) {
                            //nothing to do here!
                        } else if (value != null) {
                            bcrMediopago.getItem(itemId).getItemProperty("cantidad").setValue(0);
                        }
                    }
                });
                return tfdValue;
            }
        });
        tblMediospago.addGeneratedColumn("colMonto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                System.out.println("tblMediospago.colMonto::: " + source.getId());
                Property pro = source.getItem(itemId).getItemProperty("value");  //Atributo del bean
                final TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                tfdValue.setValue("0.00");
                tfdValue.setWidth("125px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        String value = tfdValue.getValue();
                        value = (value != null) ? value.replaceAll(",", "").trim() : value;
                        if (value != null && !value.isEmpty() //&& value.matches("(\\d+(\\.\\d+)?)|(\\.\\d+)")
                                ) {
                            List<Integer> ids = (List<Integer>) bcrMediopago.getItemIds();
                            totalMediosPago = 0D;
                            double partialVal = 0D;
                            for (Integer id : ids) {
                                partialVal = (bcrMediopago.getItem(id).getItemProperty("value").getValue() == null)
                                        ? 0D : Double.parseDouble(bcrMediopago.getItem(id).getItemProperty("value").getValue().toString());
                                totalMediosPago += partialVal;
                            }
                            tblMediospago.setColumnFooter("colMonto", currencySymbol + numberFmt.format(totalMediosPago));
                            updateTotalPagos(totalMediosPago + totalEfectivo);

                            double diferencia = (totalMediosPago + totalEfectivo) - (totalArqueoElectronico + totalProducto);
                            updateDiferencia(diferencia);

//PARTIDA CONTABLE
                            Mediopago mepa = bcrMediopago.getItem(itemId).getBean();
                            if (mepa.isPartidacont()) {
                                if (bcrPartida.size() > 0) {
                                    if (mepa.getMediopagoId() == 5) {
                                        bcrPartida.getItem(itemId).getItemProperty("value").setValue(Double.parseDouble(value));
                                    } else if (mepa.getMediopagoId() >= 35 && mepa.getMediopagoId() <= 42) {
                                        double dvalue = Double.parseDouble(value) * (1 - (mepa.getPartidacontPor() / 100));
                                        bcrPartida.getItem(itemId).getItemProperty("value").setValue(dvalue);
                                    }
                                }
                            }

                        } else if (value != null) {
                            bcrMediopago.getItem(itemId).getItemProperty("value").setValue(0D);
                        }
                    }
                });
                return tfdValue;
            }
        });
        tblMediospago.setVisibleColumns(new Object[]{"nombre", /**
             * "colCantDoctos",*
             */
            "colMonto"});
        tblMediospago.setColumnHeaders(new String[]{"Nombre", /**
             * "Cant doctos",*
             */
            "Monto"});
        //tblMediospago.setColumnAlignment("colCantDoctos", Align.RIGHT);
        tblMediospago.setColumnAlignment("colMonto", Align.RIGHT);
        tblMediospago.setFooterVisible(true);
        tblMediospago.setColumnFooter("nombre", "Total:");
        tblMediospago.setColumnFooter("colMonto", currencySymbol + numberFmt.format(totalMediosPago));
        tblMediospago.setSizeUndefined();
        tblMediospago.setHeight(HEIGHT_TABLE);
    }

//    private void buildTableMediosPago() {
    private void buildTablePartida() {
        tblPartida = utils.buildTable("PARTIDA CONTABLE:", 100f, 100f, bcrPartida,
                new String[]{"nombre"},
                new String[]{"Nombre"});
        tblPartida.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblPartida.addStyleName(ValoTheme.TABLE_COMPACT);
        tblPartida.addStyleName(ValoTheme.TABLE_SMALL);
        tblPartida.setId("_tblPartida_");
        tblPartida.addGeneratedColumn("colMonto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                System.out.println("tblMediospago.colMonto::: " + source.getId());
                Property pro = source.getItem(itemId).getItemProperty("value");  //Atributo del bean
                final TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                tfdValue.setValue("0.00");
                tfdValue.setWidth("100px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                    }
                });
                return tfdValue;
            }
        });
        tblPartida.setVisibleColumns(new Object[]{"nombre", "colMonto"});
        tblPartida.setColumnHeaders(new String[]{"Nombre", "Monto"});
        tblPartida.setColumnAlignment("colCantDoctos", Align.RIGHT);
        tblPartida.setColumnAlignment("colMonto", Align.RIGHT);
        tblPartida.setFooterVisible(true);
        tblPartida.setSizeUndefined();
        tblPartida.setHeight(HEIGHT_TABLE);
    }

    private void buildTableEfectivo() {
        tblEfectivo = utils.buildTable("Efectivo:", 100f, 100f, bcEfectivo, new String[]{"noDocto"}, new String[]{"No. docto"});
        tblEfectivo.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblEfectivo.addStyleName(ValoTheme.TABLE_COMPACT);
        tblEfectivo.addStyleName(ValoTheme.TABLE_SMALL);
        tblEfectivo.setSizeUndefined();
        tblEfectivo.setHeight(HEIGHT_TABLE);
        tblEfectivo.addGeneratedColumn("colMPname", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("medioPago");  //Atributo del bean DtoEfectivo
                Container contMediosPago = new ListContainer<Mediopago>(Mediopago.class, mediosPagoEfectivo);
                final ComboBox combo = new ComboBox("", contMediosPago);
                combo.setItemCaptionPropertyId("nombre");   //atributo de medio de pago
                combo.setPropertyDataSource(pro);
                combo.setNullSelectionAllowed(false);
                combo.addStyleName(ValoTheme.COMBOBOX_SMALL);
                combo.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        sumarEfectivo();
                        if (((Mediopago) combo.getValue()).getMediopagoId().equals(Constant.MP_CRI_EFECTIVO_DOLARES)) {
                            double tasa = (tasacambio.getTasa() != null) ? tasacambio.getTasa() : 0D;
                            final TextField tfdMonto = new TextField("Monto en dólares (tasa = " + numberFmt3D.format(tasa) + "):");
                            tfdMonto.addStyleName("align-right");
                            tfdMonto.focus();
                            MessageBox
                                    .createQuestion()
                                    .withCaption("Ingreso")
                                    .withMessage(tfdMonto)
                                    .withOkButton(new Runnable() {
                                        public void run() {
                                            String value = tfdMonto.getValue().trim();
                                            if (value.matches("(\\d+(\\.\\d+)?)|(\\.\\d+)")) {
                                                double tasa = (tasacambio.getTasa() != null) ? tasacambio.getTasa() : 0D;
                                                bcEfectivo.getItem(itemId).getItemProperty("value").setValue(tasa * Double.parseDouble(value));
                                                bcEfectivo.getItem(itemId).getItemProperty("tasa").setValue(tasa);
                                                bcEfectivo.getItem(itemId).getItemProperty("monExtranjera").setValue(Double.parseDouble(value));
                                            }
                                        }
                                    },
                                            new ButtonOption() {
                                        @Override
                                        public void apply(MessageBox mb, Button button) {
//                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                                        }
                                    }
                                    )
                                    .withCancelButton()
                                    .open();
                            //Fin MessageBox
                        }
                    }
                });
                return combo;
            }
        });

        tblEfectivo.addGeneratedColumn("colBoleta", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("noDocto");  //Atributo del bean
                final NumberField nfd = new NumberField();
                nfd.setPropertyDataSource(pro);
                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
                nfd.setValue(value);
                nfd.setMinValue(0D);
                nfd.setDecimalPrecision(0);
                nfd.setGroupingSeparator(' ');
                nfd.setWidth("100px");
                nfd.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                return nfd;
            }
        });

        tblEfectivo.addGeneratedColumn("colMonto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("value");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
                nfd.setValue(numberFmt.format(value));
                nfd.setWidth("100px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
//                nfd.setEnabled(bcEfectivo.getItem(itemId).getBean().getMedioPago().getMediopagoId().equals(MP_DOLARES_EFECTIVO));
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {

//                        String value = nfd.getValue()==null?"0":nfd.getValue();
//                        System.out.println("evento cambiar "+value);
//                        if (Double.valueOf(value) < 0) {
//                        Notification.show("AVISO:", "No puede ingresar cantidades negativas.", Notification.Type.WARNING_MESSAGE);
//                        return;
//                        }
//                        value = (value != null) ? value.replaceAll(",", "").trim() : value;
//                        if (value != null && !value.isEmpty() && value.matches("(\\d+(\\.\\d+)?)|(\\.\\d+)")) {
                        if (bcEfectivo.getItem(itemId).getBean().getMedioPago() == null) {
                            Notification.show("AVISO:", "Seleccione por favor un medio de pago.", Notification.Type.WARNING_MESSAGE);
                            return;
                        }
                        if (!Util.isDoublePositive(bcEfectivo.getItem(itemId).getBean().getValue().toString().replaceAll(",", ""))) {
                            System.out.println("valida: " + bcEfectivo.getItem(itemId).getBean().getValue().toString().replaceAll(",", ""));
                            Double valorCero = 0.00;
                            if (bcEfectivo.getItem(itemId).getBean().getValue() < 0) {
                                bcEfectivo.getItem(itemId).getBean().setValue(valorCero);
                                totalEfectivo = 0D;
                            }
                            Notification.show("AVISO", "Monto no valido.", Notification.Type.WARNING_MESSAGE);
                            return;
                        }
                        sumarEfectivo();
//                        } else {
//                            bcEfectivo.getItem(itemId).getItemProperty("value").setValue(0D);
//                        }
                    }
                });
//                nfd.addFocusListener(new FieldEvents.FocusListener() {
//                    @Override
//                    public void focus(FieldEvents.FocusEvent event) {
//                        System.out.println("- nfd.addFocusListener -");
//                        
//                    }
//                });
//                nfd.addBlurListener(new FieldEvents.BlurListener() {
//                    @Override
//                    public void blur(FieldEvents.BlurEvent event) {
//                        
//                        System.out.println("- nfd.addBlurListener(new FieldEvents.BlurListener() { -");
//                    }
//                });
                return nfd;
            }
        });

        tblEfectivo.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_TINY);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        bcEfectivo.removeItem(itemId);
                        List<DtoEfectivo> tempList = new ArrayList();
                        totalEfectivo = 0D;
                        for (DtoEfectivo deo : listaEfectivo) {
                            if (deo.getId() != itemId) {
                                tempList.add(deo);
                                totalEfectivo += deo.getValue();
                            }
                        }
                        listaEfectivo = tempList;

                        tblEfectivo.setColumnFooter("colMonto", currencySymbol + numberFmt.format(totalEfectivo));
                        updateTotalPagos(totalMediosPago + totalEfectivo);

                        updateDiferencia((totalMediosPago + totalEfectivo) - (totalArqueoElectronico + totalProducto));
                    }
                });
                return btnDelete;
            }
        });

        tblEfectivo.setVisibleColumns(new String[]{"colMPname", /**
             * "colBoleta",*
             */
            "colMonto", "colDelete"});
        tblEfectivo.setColumnHeaders(new String[]{"Tipo", /**
             * "# boleta",*
             */
            "Monto", "Borrar"});
        tblEfectivo.setColumnAlignments(Align.LEFT, /**
                 * Align.RIGHT,*
                 */
                Align.RIGHT, Align.CENTER);
        tblEfectivo.setFooterVisible(true);
        tblEfectivo.setColumnFooter("colMPname", "Total:");
        tblEfectivo.setColumnFooter("colMonto", currencySymbol + numberFmt.format(totalEfectivo));

        btnAdd = new Button("Agregar", FontAwesome.PLUS);
        btnAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAdd.addStyleName(ValoTheme.BUTTON_SMALL);
        btnAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                int itemId = 1;
                //TODO: Revisar si el id del BeanContainer se podria repeteir con este metodo.
                if (!bcEfectivo.getItemIds().isEmpty()) {
                    itemId = bcEfectivo.getIdByIndex(bcEfectivo.getItemIds().size() - 1) + 1;
                }
                bcEfectivo.removeAllItems();
                listaEfectivo.add(new DtoEfectivo(itemId, null, 0D));
                bcEfectivo.addAll(listaEfectivo);

            }
        });

    }

    private void buildTableFactElect() {
        tblFactElect = utils.buildTable("Datos facturación electrónica", 100f, 100f, bcFactElect,
                new String[]{"nombre"},
                new String[]{"Nombre"}
        );
        tblFactElect.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblFactElect.addStyleName(ValoTheme.TABLE_COMPACT);
        tblFactElect.addStyleName(ValoTheme.TABLE_SMALL);
        tblFactElect.setImmediate(true);
        tblFactElect.addGeneratedColumn("colGalones", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("galones");  //Atributo del bean
                final NumberField nfd = new NumberField();
                nfd.setPropertyDataSource(pro);
                nfd.setValue(0D);
                nfd.setMinValue(0D);
                nfd.setDecimalPrecision(3);
                nfd.setWidth("100px");
                nfd.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        List<Integer> ids = (List<Integer>) bcFactElect.getItemIds();
                        totalFEVolumen = 0D;
                        for (Integer id : ids) {
                            totalFEVolumen += Double.parseDouble(bcFactElect.getItem(id).getItemProperty("galones").getValue().toString());
                        }
                        tblFactElect.setColumnFooter("colGalones", volumenSymbol + numberFmt.format(totalFEVolumen));
                        //update tblDiferencias
                        Double value = Double.parseDouble(bcFactElect.getItem(itemId).getItemProperty("galones").getValue().toString());
                        bcDiferencias.getItem(itemId).getItemProperty("galones").setValue(value);
                        calculateDiferences();
                    }
                });
                return nfd;
            }
        });

        tblFactElect.addGeneratedColumn("colQuetzales", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("valor");  //Atributo del bean
                final NumberField nfd = new NumberField();
                nfd.setPropertyDataSource(pro);
                nfd.setValue(0D);
                nfd.setMinValue(0D);
                nfd.setWidth("100px");
                nfd.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        List<Integer> ids = (List<Integer>) bcFactElect.getItemIds();
                        totalFECurrency = 0D;
                        for (Integer id : ids) {
                            totalFECurrency += Double.parseDouble(bcFactElect.getItem(id).getItemProperty("valor").getValue().toString());
                        }
                        tblFactElect.setColumnFooter("colQuetzales", numberFmt.format(totalFECurrency));
                        //update tblDiferencias
                        Double value = Double.parseDouble(bcFactElect.getItem(itemId).getItemProperty("valor").getValue().toString());
                        bcDiferencias.getItem(itemId).getItemProperty("valor").setValue(value);
                        calculateDiferences();
                    }
                });
                return nfd;
            }
        });

        tblFactElect.setVisibleColumns(new String[]{"nombre", "colGalones", "colQuetzales"});
        tblFactElect.setColumnHeaders(new String[]{"Producto", "Galones", "Quetzales"});
        tblFactElect.setColumnAlignments(Table.Align.LEFT, Table.Align.RIGHT, Table.Align.RIGHT);
        tblFactElect.setFooterVisible(true);
        tblFactElect.setColumnFooter("TOTAL", "nombre");
        tblFactElect.setColumnFooter("colGalones", volumenSymbol + numberFmt.format(totalFEVolumen));
        tblFactElect.setColumnFooter("colQuetzales", numberFmt.format(totalFECurrency));
    }

    private void buildTableDiferencias() {
        tblDiferencias = utils.buildTable("Diferencias", 100f, 100f, bcDiferencias,
                new String[]{"nombre", "galonesFS", "valorFS", "galones", "valor", "galonesDif", "valorDif", "facturar"},
                new String[]{"Producto", "Galones Fus", "Quetzales Fus", "Galones POS", "Quetzales POS", "Galones", "Quetzales", "Acción"}
        );
        tblDiferencias.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblDiferencias.addStyleName(ValoTheme.TABLE_COMPACT);
        tblDiferencias.addStyleName(ValoTheme.TABLE_SMALL);
        tblDiferencias.addStyleName(ValoTheme.TABLE_BORDERLESS);
        tblDiferencias.addStyleName(ValoTheme.TABLE_NO_STRIPES);
        tblDiferencias.setImmediate(true);
        tblDiferencias.setColumnAlignments(Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.LEFT);
        tblDiferencias.setFooterVisible(true);
        tblDiferencias.setColumnFooter("TOTAL", "nombre");
    }

    private void calculateDiferences() {
        List<Integer> difsIds = bcDiferencias.getItemIds();
        Double val1, val2;
        Double sumCol5 = 0D, sumCol6 = 0D;
        DtoProducto dpFooter = new DtoProducto();
        dpFooter.setGalonesFS(0D);
        dpFooter.setValorFS(0D);
        for (Integer difId : difsIds) {
            val1 = Double.parseDouble(bcDiferencias.getItem(difId).getItemProperty("galones").getValue().toString());
            val2 = Double.parseDouble(bcDiferencias.getItem(difId).getItemProperty("galonesFS").getValue().toString());
            bcDiferencias.getItem(difId).getItemProperty("galonesDif").setValue(val1 - val2);
            dpFooter.setGalonesFS(dpFooter.getGalonesFS() + val2);
            val1 = Double.parseDouble(bcDiferencias.getItem(difId).getItemProperty("valor").getValue().toString());
            val2 = Double.parseDouble(bcDiferencias.getItem(difId).getItemProperty("valorFS").getValue().toString());
            bcDiferencias.getItem(difId).getItemProperty("valorDif").setValue(val1 - val2);
            dpFooter.setValorFS(dpFooter.getValorFS() + val2);

            sumCol5 += Double.parseDouble(bcDiferencias.getItem(difId).getItemProperty("galonesDif").getValue().toString());
            sumCol6 += Double.parseDouble(bcDiferencias.getItem(difId).getItemProperty("valorDif").getValue().toString());

            if (Double.parseDouble(bcDiferencias.getItem(difId).getItemProperty("valorDif").getValue().toString()) < -0.009) {
                bcDiferencias.getItem(difId).getItemProperty("facturar").setValue("FACTURAR");
            } else {
                bcDiferencias.getItem(difId).getItemProperty("facturar").setValue("NO FACTURAR");
            }
        }
        tblDiferencias.setColumnFooter("galonesFS", volumenSymbol + numberFmt.format(dpFooter.getGalonesFS()));
        tblDiferencias.setColumnFooter("valorFS", numberFmt.format(dpFooter.getValorFS()));
        tblDiferencias.setColumnFooter("galones", volumenSymbol + numberFmt.format(totalFEVolumen));
        tblDiferencias.setColumnFooter("valor", numberFmt.format(totalFECurrency));
        tblDiferencias.setColumnFooter("galonesDif", volumenSymbol + numberFmt.format(sumCol5));
        tblDiferencias.setColumnFooter("valorDif", numberFmt.format(sumCol6));

    }

    private void sumarEfectivo() {
        totalEfectivo = 0D;
        for (Integer id : bcEfectivo.getItemIds()) {
            totalEfectivo += Double.parseDouble(bcEfectivo.getItem(id).getItemProperty("value").getValue().toString());
        }
        tblEfectivo.setColumnFooter("colMonto", currencySymbol + numberFmt.format(totalEfectivo));
        updateTotalPagos(totalMediosPago + totalEfectivo);

        updateDiferencia((totalMediosPago + totalEfectivo) - (totalArqueoElectronico + totalProducto));
    }

    private CssLayout buildDetalleMontos() {
        Label label3 = new Label("Diferencia: ");
        label3.addStyleName(ValoTheme.LABEL_H2);
        label3.setSizeUndefined();
        txaComentario.addStyleName(ValoTheme.TEXTAREA_SMALL);
        CssLayout result = new CssLayout(utils.vlContainer(txaComentario), utils.vlContainer(label3), utils.vlContainer(lblDiferencia));
        return result;
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

        fechaString = (ultimoTurno.getTurnoId() != null) ? "Turno ".concat(ultimoTurno.getTurnoId().toString()) : "INEXISTENTE";
        estadoName = (ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 1)
                ? "ABIERTO" : ((ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 2) ? "CERRADO" : "SIN ESTADO");
        lblUltimoTurno.setValue("Último turno: " + fechaString + " (" + estadoName + ")");
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_BOLD);
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_H3);
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_COLORED);
        lblUltimoTurno.setSizeUndefined();

        tmpString = (tasacambio.getTasa() != null && tasacambio.getTasa() > 0) ? "" : "<b><font color='red' size='+1.5'>No existe una tasa de cambio configurada</font></b>";
        lblChangeRate.setValue(tmpString);
        lblChangeRate.setContentMode(ContentMode.HTML);
        lblChangeRate.setSizeUndefined();
        lblChangeRate.addStyleName(ValoTheme.LABEL_BOLD);
        lblChangeRate.addStyleName(ValoTheme.LABEL_H3);
    }

    private void buildTableCxC() {
        tblCxC = utils.buildTable("Detalle clientes credito (CxC):", 100f, 100f, bcrClientes,
                new String[]{"nombre"},
                new String[]{"Nombre"}
        );
        tblCxC.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblCxC.addStyleName(ValoTheme.TABLE_COMPACT);
        tblCxC.addStyleName(ValoTheme.TABLE_SMALL);
        tblCxC.setImmediate(true);
        tblCxC.addGeneratedColumn("colCliente", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cliente");  //Atributo del bean
                ComboBox cbxCliente = utils.buildCombobox("", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, contCustomerCredit);
                cbxCliente.setPropertyDataSource(pro);
                cbxCliente.setFilteringMode(FilteringMode.CONTAINS);
                return cbxCliente;
            }
        });
        tblCxC.addGeneratedColumn("colMonto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("valor");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
                nfd.setValue(numberFmt.format(value));
                nfd.setWidth("100px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        updateTableFooterCxC();
                    }
                });
                return nfd;
            }
        });
        tblCxC.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        bcrClientes.removeItem(itemId);
                        List<DtoProducto> tempList = new ArrayList();
                        for (DtoProducto deo : listCustomers) {
                            if (deo.getProductoId() != itemId) {
                                tempList.add(deo);
                            }
                        }
                        listCustomers = tempList;
                        updateTableFooterCxC();
                    }
                });
                return btnDelete;
            }
        });
        tblCxC.setVisibleColumns(new Object[]{"colCliente", "colMonto", "colDelete"});
        tblCxC.setColumnHeaders(new String[]{"Cliente", "Monto", "Borrar"});
        tblCxC.setColumnAlignments(Align.LEFT, Align.RIGHT, Align.CENTER);
        tblCxC.setSizeUndefined();
        tblCxC.setHeight(200f, Unit.PIXELS);

        btnAddCustomer = new Button("Agregar", FontAwesome.PLUS);
        btnAddCustomer.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAddCustomer.addStyleName(ValoTheme.BUTTON_SMALL);
        btnAddCustomer.addClickListener((final Button.ClickEvent event) -> {
//            FormDetalleVenta.open();
        });
//        btnAddCustomer.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                bcrClientes.removeAllItems();
//                DtoProducto dtoprod = new DtoProducto(utils.getRandomNumberInRange(1, 1000), null, null);
//                dtoprod.setValor(0D);
//                listCustomers.add(dtoprod);
//                bcrClientes.addAll(listCustomers);
//            }
//        });
    }

    public void buildTableLubsDet() {
        tblLubricantes = utils.buildTable("Detalle lubricantes:", 100f, 100f, bcrLubs,
                new String[]{"nombre"},
                new String[]{"Nombre"}
        );
        tblLubricantes.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblLubricantes.addStyleName(ValoTheme.TABLE_COMPACT);
        tblLubricantes.addStyleName(ValoTheme.TABLE_SMALL);
        tblLubricantes.setImmediate(true);
        tblLubricantes.addGeneratedColumn("colProducto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("producto");  //Atributo del bean
                final ComboBox cbxProducto = utils.buildCombobox("", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, contLubs);
                cbxProducto.setPropertyDataSource(pro);
                cbxProducto.setFilteringMode(FilteringMode.CONTAINS);
                cbxProducto.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        bcrLubs.getItem(itemId).getItemProperty("valor").setValue(((Producto) cbxProducto.getValue()).getPrecio());
                    }
                });
                return cbxProducto;
            }
        });
        tblLubricantes.addGeneratedColumn("colCantidad", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cantidad");  //Atributo del bean
                final TextField nfd = new TextField(pro);
//                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
//                nfd.setValue(numberFmt.format(value));
                nfd.setWidth("60px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        if (nfd.getValue() != null && !nfd.getValue().trim().isEmpty() && nfd.getValue().matches("\\d+")) {
                            int cantidad = Integer.parseInt(nfd.getValue().trim());
                            double precio = Double.parseDouble(bcrLubs.getItem(itemId).getItemProperty("valor").getValue().toString());
                            bcrLubs.getItem(itemId).getItemProperty("total").setValue(cantidad * precio);
                            updateTableFooterLubs();
                        } else {
                            nfd.setValue("0");
                        }
                    }
                });
                return nfd;
            }
        });
        tblLubricantes.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        bcrLubs.removeItem(itemId);
                        List<DtoProducto> tempList = new ArrayList();
                        for (DtoProducto deo : listLubs) {
                            if (deo.getProductoId() != itemId) {
                                tempList.add(deo);
                            }
                        }
                        listLubs = tempList;
                        updateTableFooterLubs();
                    }
                });
                return btnDelete;
            }
        });
        tblLubricantes.setVisibleColumns(new Object[]{"colProducto", "valor", "colCantidad", "total", "colDelete"});
        tblLubricantes.setColumnHeaders(new String[]{"Producto", "Precio", "Cantidad", "Total", "Borrar"});
        tblLubricantes.setColumnAlignments(Align.LEFT, Align.RIGHT, Align.RIGHT, Align.RIGHT, Align.CENTER);
        tblLubricantes.setSizeUndefined();
        tblLubricantes.setHeight(200f, Unit.PIXELS);
        tblLubricantes.setColumnWidth("valor", 100);
        tblLubricantes.setColumnWidth("total", 100);

        btnAddLubs = new Button("Agregar", FontAwesome.PLUS);
        btnAddLubs.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAddLubs.addStyleName(ValoTheme.BUTTON_SMALL);
        btnAddLubs.addClickListener((final Button.ClickEvent event) -> {
            FormDetalleVenta2.open();
        });
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                FormDetalleVenta2.open();
//                bcrLubs.removeAllItems();
//                DtoProducto dtoprod = new DtoProducto(utils.getRandomNumberInRange(1, 1000), null, null);
//                dtoprod.setValor(0D);
//                dtoprod.setCantidad(0);
//                dtoprod.setTotal(0D);
//                listLubs.add(dtoprod);
//                bcrLubs.addAll(listLubs);
//            }
//        });
    }

    public void buildTablePrepago() {
        tblPrepaid = utils.buildTable("Detalle clientes prepago:", 100f, 100f, bcrPrepaid,
                new String[]{"nombre"},
                new String[]{"Nombre"}
        );
        tblPrepaid.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblPrepaid.addStyleName(ValoTheme.TABLE_COMPACT);
        tblPrepaid.addStyleName(ValoTheme.TABLE_SMALL);
        tblPrepaid.setImmediate(true);
        tblPrepaid.addGeneratedColumn("colCliente", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cliente");  //Atributo del bean
                ComboBox cbxCliente = utils.buildCombobox("", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, contCustomerPrepaid);
                cbxCliente.setPropertyDataSource(pro);
                cbxCliente.setFilteringMode(FilteringMode.CONTAINS);
                return cbxCliente;
            }
        });
        tblPrepaid.addGeneratedColumn("colMonto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("valor");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
                nfd.setValue(numberFmt.format(value));
                nfd.setWidth("100px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {

                        updateTableFooterPrepaid();
                    }
                });
                return nfd;
            }
        });
        tblPrepaid.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        bcrPrepaid.removeItem(itemId);
                        List<DtoProducto> tempList = new ArrayList();
                        for (DtoProducto deo : listPrepaid) {
                            if (deo.getProductoId() != itemId) {
                                tempList.add(deo);
                            }
                        }
                        listPrepaid = tempList;
                        updateTableFooterPrepaid();
                    }
                });
                return btnDelete;
            }
        });
        tblPrepaid.setVisibleColumns(new Object[]{"colCliente", "colMonto", "colDelete"});
        tblPrepaid.setColumnHeaders(new String[]{"Cliente", "Monto", "Borrar"});
        tblPrepaid.setColumnAlignments(Align.LEFT, Align.RIGHT, Align.CENTER);
        tblPrepaid.setSizeUndefined();
        tblPrepaid.setHeight(200f, Unit.PIXELS);

        btnAddPrep = new Button("Agregar", FontAwesome.PLUS);
        btnAddPrep.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAddPrep.addStyleName(ValoTheme.BUTTON_SMALL);
        btnAddPrep.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                bcrPrepaid.removeAllItems();
                DtoProducto dtoprod = new DtoProducto(utils.getRandomNumberInRange(1, 1000), null, null);
                dtoprod.setValor(0D);
                listPrepaid.add(dtoprod);
                bcrPrepaid.addAll(listPrepaid);
            }
        });
    }

    public void buildTableCreditCard() {

        tblCreditCard = utils.buildTable("Detalle tarjetas de crédito:", 100f, 100f, bcrCreditC,
                new String[]{"nombre"},
                new String[]{"Nombre"}
        );
        tblCreditCard.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblCreditCard.addStyleName(ValoTheme.TABLE_COMPACT);
        tblCreditCard.addStyleName(ValoTheme.TABLE_SMALL);
        tblCreditCard.setImmediate(true);
        tblCreditCard.addGeneratedColumn("colCliente", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cliente");  //Atributo del bean
                ComboBox cbxCliente = utils.buildCombobox("", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, contCreditC);
                cbxCliente.setPropertyDataSource(pro);
                cbxCliente.setFilteringMode(FilteringMode.CONTAINS);
                return cbxCliente;
            }
        });
        tblCreditCard.addGeneratedColumn("colMonto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("valor");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
                nfd.setValue(numberFmt.format(value));
                nfd.setWidth("100px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
//                        updateTableFooterPrepaid();
                    }
                });
                return nfd;
            }
        });
        tblCreditCard.addGeneratedColumn("colLote", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("presentacion");  //Atributo del bean
//                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
//                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
//                nfd.setValue(numberFmt.format(value));
                final TextField nfd = new TextField(pro);
                nfd.setNullRepresentation("");
                nfd.setWidth("100px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
//                        updateTableFooterPrepaid();
                    }
                });
                return nfd;
            }
        });
        tblCreditCard.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        bcrCreditC.removeItem(itemId);
                        List<DtoProducto> tempList = new ArrayList();
                        for (DtoProducto deo : listPrepaid) {
                            if (deo.getProductoId() != itemId) {
                                tempList.add(deo);
                            }
                        }
                        listPrepaid = tempList;
                        updateTableFooterPrepaid();
                    }
                });
                return btnDelete;
            }
        });
        tblCreditCard.setVisibleColumns(new Object[]{"colCliente", "colLote", "colMonto", "colDelete"});
        tblCreditCard.setColumnHeaders(new String[]{"Tarjeta", "Lote", "Monto", "Borrar"});
        tblCreditCard.setColumnAlignments(Align.LEFT, Align.RIGHT, Align.RIGHT, Align.CENTER);
        tblCreditCard.setSizeUndefined();
        tblCreditCard.setHeight(200f, Unit.PIXELS);

        btnAddCreditC = new Button("Agregar", FontAwesome.PLUS);
        btnAddCreditC.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAddCreditC.addStyleName(ValoTheme.BUTTON_SMALL);
        btnAddCreditC.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
//                bcrCreditC.removeAllItems();
                DtoProducto dtoprod = new DtoProducto(utils.getRandomNumberInRange(1, 1000), null, null);
                dtoprod.setValor(0D);
                bcrCreditC.addBean(dtoprod);
                tblCreditCard.refreshRowCache();
//                listPrepaid.add(dtoprod);
//                bcrCreditC.addAll(listPrepaid);
            }
        });
    }

    public void updateTableFooterCxC() {
        tmpDouble = 0;
        for (Integer itemId : bcrClientes.getItemIds()) {
            tmpDouble += bcrClientes.getItem(itemId).getBean().getValor();
        }
        tblCxC.setFooterVisible(true);
        tblCxC.setColumnFooter("colCliente", "Total:");
        tblCxC.setColumnFooter("colMonto", currencySymbol + numberFmt.format(tmpDouble).trim());
        for (Integer itemId : bcrMediopago.getItemIds()) {
            if (bcrMediopago.getItem(itemId).getBean().getMediopagoId() == Constant.MP_CRI_VENTA_CREDITO) {
                bcrMediopago.getItem(itemId).getItemProperty("value").setValue(tmpDouble);  //put value
                break;
            }
        }
    }

    public void updateTableFooterLubs() {
        tmpInt = 0;
        tmpDouble = 0;
        for (Integer itemId : bcrLubs.getItemIds()) {
            tmpDouble += bcrLubs.getItem(itemId).getBean().getTotal();
            tmpInt += bcrLubs.getItem(itemId).getBean().getCantidad();
        }
        tblLubricantes.setFooterVisible(true);
        tblLubricantes.setColumnFooter("colProducto", "Total:");
        tblLubricantes.setColumnFooter("colCantidad", Integer.toString(tmpInt));
        tblLubricantes.setColumnFooter("total", currencySymbol + numberFmt.format(tmpDouble).trim());
        for (Integer itemId : bcrProducto.getItemIds()) {
            if (bcrProducto.getItem(itemId).getBean().getProductoId() == Constant.MP_CRI_VENTA_LUBS_UNO) {
                bcrProducto.getItem(itemId).getItemProperty("value").setValue(tmpDouble);
                break;
            }
        }
    }

    public void updateTableFooterPrepaid() {
        tmpDouble = 0;
        for (Integer itemId : bcrPrepaid.getItemIds()) {
            tmpDouble += bcrPrepaid.getItem(itemId).getBean().getValor();
        }
        tblPrepaid.setFooterVisible(true);
        tblPrepaid.setColumnFooter("colCliente", "Total:");
        tblPrepaid.setColumnFooter("colMonto", currencySymbol + numberFmt.format(tmpDouble).trim());
        for (Integer itemId : bcrMediopago.getItemIds()) {
            if (bcrMediopago.getItem(itemId).getBean().getMediopagoId() == Constant.MP_CRI_VENTA_PREPAGO) {
                bcrMediopago.getItem(itemId).getItemProperty("value").setValue(tmpDouble);
                break;
            }
        }
    }

    public void onChangeCheckboxBomba(Object itemId, boolean selected) {
        SvcArqueo svcArqueo = new SvcArqueo();
//                        if (cb.getValue()) {
        if (selected) {
            boolean bombaConLectura = svcArqueo.bombaTieneLecturaByTurnoid(turno.getTurnoId(), Integer.parseInt(itemId.toString()));
            if (!bombaConLectura) {
                svcArqueo.closeConnections();
                for (Bomba bmb : allBombas) {
                    if (bmb.getId() == Integer.parseInt(itemId.toString())) {
                        bmb.setSelected(false);
                        break;
                    }
                }
                bcrBombas.removeAllItems();
                bcrBombas.addAll(allBombas);
                Notification.show("La bomba NO posee lecturas.\nPor favor primero ingrese lecturas para la misma.", Notification.Type.ERROR_MESSAGE);
                return;
            }
        }
        //Al quitar seleccion de checkbox
//                        if (!cb.getValue()) {
        if (!selected) {
            bcArqueo.removeAllItems();
        }

        String bombasIds = "";
        boolean comma = false;
        for (Bomba b : allBombas) {
            if (b.getSelected()) {
                bombasIds = bombasIds + (comma ? "," : "") + Integer.toString(b.getId());
                comma = true;
            }
        }
        List<DtoArqueo> allArqueo = new ArrayList();
        List<DtoArqueo> arqueoElec = new ArrayList();
        totalArqueoVol = 0D;
        totalArqueoCurr = 0D;
        totalArqueoDif = 0D;
        if (!bombasIds.isEmpty()) {
            allArqueo = svcArqueo.getArqueo(turno.getTurnoId().toString(), bombasIds, "M");
            arqueoElec = svcArqueo.getArqueo(turno.getTurnoId().toString(), bombasIds, "E");
            svcArqueo.closeConnections();
            totalArqueoElectronico = 0D;
//                            for (DtoArqueo dao : allArqueo) {
            for (DtoArqueo dao : arqueoElec) {
//                                if (pais.getPaisId() != null && pais.getPaisId().equals(320)) {  //Guatemala
//                                    for (DtoArqueo dqueo : arqueoElec) {
//                                        totalArqueoElectronico += dqueo.getVenta();
//                                        if (dao.getProductoId().equals(dqueo.getProductoId()) && dao.getTipodespachoId().equals(dqueo.getTipodespachoId())) {
//                                            dao.setDiferencia(dqueo.getVenta() - dao.getVenta());
//                                            //                                        dao.setDiferenciaString( numberFmt.format(dqueo.getVenta() - dao.getVenta()) );
//                                            break;
//                                        }
//                                    }
//                                } else {
                totalArqueoElectronico += dao.getVenta();
//                                }
                totalArqueoVol += dao.getVolumen();
                totalArqueoCurr += dao.getVenta();
                totalArqueoDif += (dao.getDiferencia() != null) ? dao.getDiferencia() : 0;
            }

            //update tblDiferencias
            List<Integer> difsIds = bcDiferencias.getItemIds();
            int prodId;
            Double galonesFS = 0D, valorFS = 0D;
            for (Integer difId : difsIds) {
                prodId = Integer.parseInt(bcDiferencias.getItem(difId).getItemProperty("productoId").getValue().toString());
                galonesFS = valorFS = 0D;
                for (DtoArqueo dao : arqueoElec) {
                    if (prodId == dao.getProductoId()) {
                        galonesFS += dao.getVolumen();
                        valorFS += dao.getVenta();
                    }
                }
                bcDiferencias.getItem(difId).getItemProperty("galonesFS").setValue(galonesFS);
                bcDiferencias.getItem(difId).getItemProperty("valorFS").setValue(valorFS);
            }
            calculateDiferences();

        } else {
            //Al desmarcar todos
            for (Integer pid : bcrProducto.getItemIds()) {
                bcrProducto.getItem(pid).getItemProperty("value").setValue(0D);
            }
            totalArqueoElectronico = 0D;
            for (Integer mpid : bcrMediopago.getItemIds()) {
                bcrMediopago.getItem(mpid).getItemProperty("cantidad").setValue(0);
                bcrMediopago.getItem(mpid).getItemProperty("value").setValue(0D);
            }
            totalMediosPago = 0D;
            if (bcEfectivo.getItemIds().size() > 0) {
                bcEfectivo.removeAllItems();
            }
            totalEfectivo = 0D;
            tblEfectivo.setColumnFooter("colMonto", currencySymbol + numberFmt.format(totalEfectivo));

            updateTotalPagos(0D);
            svcArqueo.closeConnections();

        }

        tableCalc.setColumnFooter("volumen", volumenSymbol + numberFmt.format(totalArqueoVol));
        tableCalc.setColumnFooter("venta", currencySymbol + numberFmt.format(totalArqueoCurr));
        tableCalc.setColumnFooter("diferencia", currencySymbol + numberFmt.format(totalArqueoDif));
        bcArqueo.removeAllItems();
//                        bcArqueo.addAll(allArqueo);
        bcArqueo.addAll(arqueoElec);
        updateTotalVentas(totalArqueoElectronico + totalProducto);

        updateDiferencia((totalMediosPago + totalEfectivo) - (totalArqueoElectronico + totalProducto));
    }

    public void updateTotalVentas(double value) {
        lblTotalVentas1.setValue(currencySymbol + numberFmt.format(value));
        lblTotalVentas1.addStyleName(ValoTheme.LABEL_BOLD);
        lblTotalVentas1.addStyleName(ValoTheme.LABEL_H2);
        lblTotalVentas1.setSizeUndefined();
    }

    public void updateTotalPagos(double value) {
        lblTotalVentas2.setValue(currencySymbol + numberFmt.format(value));
        lblTotalVentas2.addStyleName(ValoTheme.LABEL_BOLD);
        lblTotalVentas2.addStyleName(ValoTheme.LABEL_H2);
        lblTotalVentas2.setSizeUndefined();
    }

    public void updateDiferencia(double diferencia) {
        tmpString = (diferencia < -0.009) ? "style=\"color:red;\"" : "";
        lblDiferencia.setValue(currencySymbol + "<strong " + tmpString + ">" + numberFmt.format(diferencia) + "</strong>");
        lblDiferencia.setContentMode(ContentMode.HTML);
        lblDiferencia.addStyleName(ValoTheme.LABEL_H2);
        lblDiferencia.setSizeUndefined();
    }

    static final DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();

    public double twoDecimal(double value) {
        return Double.parseDouble(Constant.decimal2D.format(value).trim().replaceAll("" + symbols.getGroupingSeparator(), ""));
    }

    private void defineInitialCountryStation() {
        if (uniqueStation != null) {
            allCountries.forEach(item -> {
                if (item.getPaisId().toString().equals(uniqueStation[0])) {
                    cbxPais.setValue(item);
                    return;
                }
            });
        }
    }

    private void onchangeCbxArqueo(String arqueoCajaId) {
        if (arqueoCajaId != null) {
            SvcTurnoCierre svcTC = new SvcTurnoCierre();
            //Determinar bombas
            List<Bomba> bombas = svcTC.getBombasByArquoid(arqueoCajaId);
            for (Integer bid : bcrBombas.getItemIds()) {
                bcrBombas.getItem(bid).getItemProperty("selected").setValue(Boolean.FALSE);
                for (Bomba b : bombas) {
                    if (b.getId().equals(bcrBombas.getItem(bid).getBean().getId())) {
                        bcrBombas.getItem(bid).getItemProperty("selected").setValue(Boolean.TRUE);
                        break;
                    }
                }
            }
            //Determinar productos
            List<Producto> productos = svcTC.getProductoByArqueoid(arqueoCajaId);
            for (Integer pid : bcrProducto.getItemIds()) {
                bcrProducto.getItem(pid).getItemProperty("value").setValue(0D);
                for (Producto pro : productos) {
                    if (pro.getProductoId().equals(bcrProducto.getItem(pid).getBean().getProductoId())) {
                        bcrProducto.getItem(pid).getItemProperty("value").setValue(pro.getValue());
                        break;
                    }
                }
            }
            //Determinar medios de pago
            List<Mediopago> mediospago = svcTC.getMediopagoByArqueoid(arqueoCajaId);
            for (Integer mpid : bcrMediopago.getItemIds()) {
                bcrMediopago.getItem(mpid).getItemProperty("cantidad").setValue(0);
                bcrMediopago.getItem(mpid).getItemProperty("value").setValue(0D);
                for (Mediopago mp : mediospago) {
                    if (mp.getMediopagoId().equals(bcrMediopago.getItem(mpid).getBean().getMediopagoId())) {
                        bcrMediopago.getItem(mpid).getItemProperty("cantidad").setValue(mp.getCantidad());
                        bcrMediopago.getItem(mpid).getItemProperty("value").setValue(mp.getValue());
                        break;
                    }
                }
            }
            //Determinar efectivo
            List<Mediopago> efectivos = svcTC.getEfectivoByArqueoid(arqueoCajaId);
            bcEfectivo.removeAllItems();
            listaEfectivo = new ArrayList();
            int itemId = 1;
            for (Mediopago mp : efectivos) {
                for (Mediopago mpoe : mediosPagoEfectivo) {
                    if (mp.getMediopagoId().equals(mpoe.getMediopagoId())) {
                        DtoEfectivo deo = new DtoEfectivo(itemId++, Double.parseDouble(mp.getCantidad().toString()), mp.getValue());
                        deo.setMedioPago(mpoe);
                        listaEfectivo.add(deo);
                    }
                }
            }
            bcEfectivo.addAll(listaEfectivo);
            //suma global
            for (Integer eid : bcEfectivo.getItemIds()) {
                bcEfectivo.getItem(eid).getItemProperty("value").setValue(
                        bcEfectivo.getItem(eid).getBean().getValue()
                );
            }

            svcTC.closeConnections();
        } else {
            for (Integer bid : bcrBombas.getItemIds()) {
                bcrBombas.getItem(bid).getItemProperty("selected").setValue(Boolean.FALSE);
            }
            listaEfectivo.clear();
            bcEfectivo.removeAllItems();
        }

        tableBombas.removeGeneratedColumn("colSelected");
        buildTableBombas();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
