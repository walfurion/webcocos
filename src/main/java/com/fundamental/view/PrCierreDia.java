package com.fundamental.view;

import com.fundamental.model.Acceso;
import com.fundamental.model.Dia;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.Mediopago;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Precio;
import com.fundamental.model.Producto;
import com.fundamental.model.Turno;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoArqueo;

import com.fundamental.model.dto.DtoProducto;
import com.fundamental.model.dto.InventarioDto;
import com.fundamental.model.dto.RecepcionDto;

import com.fundamental.services.Dao;
import com.fundamental.services.SvcDeposito;
import com.fundamental.services.SvcEstacion;
import com.fundamental.services.SvcMedioPago;
import com.fundamental.services.SvcTurno;
import com.fundamental.services.SvcTurnoCierre;
import com.fundamental.utils.Constant;

import com.sisintegrados.generic.bean.GenericDepositoDet;
import com.sisintegrados.view.form.FormClientesCredito;
import com.sisintegrados.view.form.FormDetalleDeposito;

import com.sisintegrados.generic.bean.InventarioRecepcion;
import com.sisintegrados.generic.bean.RecepcionInventario;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
public class PrCierreDia extends Panel implements View {

    static final DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00;-#");
    BeanContainer<Integer, RecepcionInventario> bcrRecepcion = new BeanContainer<>(RecepcionInventario.class);
    private RecepcionInventario recepcion;
    VerticalLayout root;
//    Table tableBombas;
    Label lblTotalVentas = new Label(numberFmt.format(0D)),
            lblTotalDinero = new Label(numberFmt.format(0D)),
            lblDiferencia = new Label(numberFmt.format(0D));
    Button btnGuardar = new Button("Cerrar día"),
            btnAll = new Button("Todas"),
            btnNone = new Button("Ninguna");

    Label lblUltimoDía = new Label("Último día:"),
            lblUltimoTurno = new Label("Último turno:");
    DateField dfdFecha;
    ComboBox cbxPais, cbxEstacion;

    Table tableTurno = new Table();

//    {
//        @Override
//        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
//            if (colId.equals("diferencia")) {
//                return numberFmt.format(property.getValue());
//            }
//            return super.formatPropertyValue(rowId, colId, property);
//        }
//    };
    Table tableVentas = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("volumen") || colId.equals("venta")) {
                return numberFmt.format(property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tableProductos = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("value")) {
                return numberFmt.format(property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tableMediosPago = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("value")) {
                return numberFmt.format(property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tableEfectivo = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("value")) {
                return numberFmt.format(property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tableDeposito = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("value")) {
                return numberFmt.format(property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tblInventory = new Table("Inventario:");

    TextField tfdDriver = new TextField("Piloto:"), tfdUnit = new TextField("Unidad:"), tfdBill = new TextField("Factura:");
    Label lblTittle = new Label("Recepción producto:");

    BeanContainer<Integer, Turno> bcrTurnos = new BeanContainer<Integer, Turno>(Turno.class);
//    BeanContainer<Integer, Bomba> bcrBombas;
    BeanContainer<Integer, DtoArqueo> bcrVentas = new BeanContainer<Integer, DtoArqueo>(DtoArqueo.class);
    BeanContainer<Integer, Producto> bcrProducto = new BeanContainer<Integer, Producto>(Producto.class);
    BeanContainer<Integer, Mediopago> bcrMediospago = new BeanContainer<Integer, Mediopago>(Mediopago.class);
    BeanContainer<Integer, Mediopago> bcrEfectivo = new BeanContainer<Integer, Mediopago>(Mediopago.class);
    BeanContainer<Integer, InventarioRecepcion> bcrInventario = new BeanContainer<Integer, InventarioRecepcion>(InventarioRecepcion.class);

    BeanContainer<Integer, GenericDepositoDet> bcrDeposito = new BeanContainer<Integer, GenericDepositoDet>(GenericDepositoDet.class);
    BeanItemContainer<GenericDepositoDet> ContDepositoDet = new BeanItemContainer<GenericDepositoDet>(GenericDepositoDet.class);

    Double totalVentas = 0D, totalDinero = 0D;
    String symCurrency, symVolumen;

    Estacion estacion = new Estacion();
    Utils utils = new Utils();
    Usuario user;
//    Turno turno;
    Dia dia, ultimoDia;
    List<Precio> precios;
    List<Producto> productos;
    Acceso acceso = new Acceso();
    FormDetalleDeposito formDetalleDeposito; //jlopez
    Button btnDetalleDeposito;

    SvcDeposito daoDeposito = new SvcDeposito();
    String currencySymbol;
    Pais pais = new Pais();

    public PrCierreDia() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);
        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        symCurrency = (user.getPaisLogin() == null) ? "" : user.getPaisLogin().getMonedaSimbolo().concat(". ");
        symVolumen = (user.getPaisLogin() == null) ? "" : user.getPaisLogin().getVolSimbolo().concat(". ");
        root.addComponent(utils.buildHeader("Cierre de día", true, true));
        root.addComponent(utils.buildSeparator());
        currencySymbol = (user.getPaisLogin() == null) ? "" : user.getPaisLogin().getMonedaSimbolo() + ". ";
        getAllData();
//***
        buildControls();
        buildTableCuadre();
        buildTableBombas();
        buildTableVentas();

//        buildTableArqueo();
        buildTableProductos();
        buildTableMediosPago();
        buildTableEfectivo();
        buildTableDeposito();
        buildTableInventario();
        buildButtons();

        HorizontalLayout hlContent = new HorizontalLayout();
        Responsive.makeResponsive(hlContent);
        hlContent.setHeight("60%");
//        hlContent.addComponents(tableArqueo);

        HorizontalLayout hlContent2 = new HorizontalLayout();
        hlContent2.setMargin(new MarginInfo(false, true));
        Responsive.makeResponsive(hlContent2);
        hlContent2.setHeight("60%");
//        hlContent2.addComponents(tableProductos);
//        hlContent2.addComponents(tableMediosPago);

        HorizontalLayout hlContent4 = new HorizontalLayout();
        hlContent4.setMargin(false);
        hlContent4.setSpacing(true);
        hlContent4.setSizeUndefined();
        Responsive.makeResponsive(hlContent4);
        hlContent4.addComponents(buildDetalleMontos(), btnGuardar);
        hlContent4.setComponentAlignment(btnGuardar, Alignment.BOTTOM_LEFT);

//        VerticalLayout vlLabels = new VerticalLayout(lblTotalVentas, lblTotalDinero, lblDiferencia);
//        vlLabels.setComponentAlignment(lblTotalVentas, Alignment.MIDDLE_RIGHT);
//        vlLabels.setComponentAlignment(lblTotalDinero, Alignment.MIDDLE_RIGHT);
//        vlLabels.setComponentAlignment(lblDiferencia, Alignment.MIDDLE_RIGHT);
//        Responsive.makeResponsive(vlLabels);
//        vlLabels.setSizeUndefined();
//        hlContent4.addComponent(vlLabels, 0);
//VerticalLayout vlContent = new VerticalLayout();
        CssLayout cltInfo = new CssLayout();
        cltInfo.setSizeUndefined();
        cltInfo.setWidth(100f, Unit.PERCENTAGE);
        Responsive.makeResponsive(cltInfo);
        cltInfo.addComponents(lblUltimoDía);

        HorizontalLayout hlCombo = utils.buildHorizontal("hlCombo", false, false, true, false);
        hlCombo.setSizeUndefined();
        CssLayout cltCombo = new CssLayout(utils.vlContainer(cbxPais), utils.vlContainer(cbxEstacion), utils.vlContainer(dfdFecha), utils.vlContainer(btnDetalleDeposito));
        hlCombo.addComponents(cltCombo);

        HorizontalLayout hlBtnsSel = new HorizontalLayout();
        hlBtnsSel.setMargin(false);
        hlBtnsSel.setSpacing(true);
        hlBtnsSel.setSizeUndefined();
        Responsive.makeResponsive(hlBtnsSel);
        hlBtnsSel.addComponents(btnAll, btnNone);

        VerticalLayout vlTableButtons = new VerticalLayout(tableTurno, hlBtnsSel);
        vlTableButtons.setSizeUndefined();
        vlTableButtons.setHeight("90%");

        tfdDriver.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        tfdUnit.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        tfdBill.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        lblTittle.addStyleName(ValoTheme.LABEL_BOLD);
        lblTittle.addStyleName(ValoTheme.LABEL_SMALL);
        HorizontalLayout hltInventory = new HorizontalLayout(lblTittle, tfdDriver, tfdUnit, tfdBill);
        hltInventory.setSizeUndefined();
        hltInventory.setSpacing(true);
        VerticalLayout vltInventory = new VerticalLayout(hltInventory, utils.vlContainer(tblInventory));
        vltInventory.setSizeUndefined();
        vltInventory.setMargin(new MarginInfo(false, true, true, false));

        CssLayout content = new CssLayout();
        content.addStyleName("dashboard-panels");
        Responsive.makeResponsive(content);
        content.addComponents(
                utils.vlContainer(vlTableButtons),
                utils.vlContainer(tableVentas), utils.vlContainer(tableProductos),
                utils.vlContainer(tableMediosPago), utils.vlContainer(tableEfectivo), utils.vlContainer(tableDeposito),
                //                utils.vlContainer(tblInventory),
                vltInventory,
                utils.vlContainer(hlContent4)
        );

        root.addComponents(cltInfo, hlCombo, content);
        root.setExpandRatio(content, 1);

    }

//List<InventarioRecepcion> inventarioHoy=new ArrayList();
    List<InventarioRecepcion> inventarioAyer = new ArrayList();

    private void getAllData() {
        bcrRecepcion.setBeanIdProperty("invrecepcion_id");
        bcrTurnos.setBeanIdProperty("turnoId");
        bcrVentas.setBeanIdProperty("id");
        bcrProducto.setBeanIdProperty("productoId");
        bcrMediospago.setBeanIdProperty("mediopagoId");
        bcrEfectivo.setBeanIdProperty("efectivoId");
        bcrInventario.setBeanIdProperty("idDto");

        bcrDeposito.setBeanIdProperty("idGenerico");
        tfdDriver.setValue("");
        tfdUnit.setValue("");
        tfdBill.setValue("");

        SvcTurnoCierre service = new SvcTurnoCierre();

        estacion = (Estacion) ((cbxEstacion != null && cbxEstacion.getValue() != null)
                ? cbxEstacion.getValue() : ((user.getEstacionLogin() != null) ? user.getEstacionLogin() : new Estacion()));

        ultimoDia = (ultimoDia == null || ultimoDia.getFecha() == null) ? service.getUltimoDiaByEstacionid(estacion.getEstacionId()) : ultimoDia;
        if (dia == null || dia.getFecha() == null) {    //La primera vez
            dia = service.getDiaActivoByEstacionid(estacion.getEstacionId());
            dia = (dia.getEstadoId() == null) ? ultimoDia : dia;
        }

        List<Turno> turnosTemp = service.getTurnosByEstacionidDia_lectura(estacion.getEstacionId(), dia.getFecha());

        bcrTurnos.removeAllItems();
        bcrTurnos.addAll(turnosTemp);

        List<InventarioRecepcion> inventarioHoy = new ArrayList();

        if (dia != null && dia.getEstadoId() != null) {

            List<String[]> calibraciones = service.getCalibracionByFechaEstacion(dia.getFecha(), estacion.getEstacionId()); //de hoy

            Calendar ayer = Calendar.getInstance();
            ayer.setTime(dia.getFecha());
            ayer.add(Calendar.DATE, -1);
            inventarioAyer = service.getInventarioByFechaEstacion(ayer.getTime(), estacion.getEstacionId());
            List<Producto> myprods = service.getCombustiblesByEstacionid(estacion.getEstacionId());
            int index = 1;
            InventarioRecepcion invdto;
            for (Producto p : myprods) {
                invdto = new InventarioRecepcion(index++, p.getNombre(), 0D, dia.getFecha(), estacion.getEstacionId(), p.getProductoId(), 0D, 0D, 0D, null, null);
                invdto.setVentas(0D);
                invdto.setVentasCons(0D);
                invdto.setComprasDto(0D);
                invdto.setFinallDto(0D);
                invdto.setInventarioFisico(0D);
                invdto.setTanque(p.getTanque());
                for (String[] cal : calibraciones) {
                    if (p.getProductoId() == Integer.parseInt(cal[0])) {
                        invdto.setCalibracion(Double.parseDouble(cal[1]));
                        invdto.setVentasCons(Double.parseDouble(cal[2]));
                        break;
                    }
                }
                for (InventarioRecepcion idto : inventarioAyer) {
                    if (p.getProductoId().equals(idto.getProductoId())) {
//                        invdto.setInicial(idto.getFinall());
                        invdto.setInicialDto(idto.getInventarioFisico());
                        invdto.setLecturaVeederRoot(invdto.getInicialDto()+invdto.getComprasDto()-invdto.getVentasCons());
                        invdto.setDiferencia(invdto.getInventarioFisico()-invdto.getLecturaVeederRoot());
                        invdto.setVarianza((invdto.getDiferencia()/invdto.getVentasCons())*100);
                        break;
                    }
                }
//                for (Integer id : (List<Integer>) tblInventory.getItemIds()) {     
//                    invdto.setLecturaVeederRoot(bcrInventario.getItem(id).getBean().getInicialDto()+bcrInventario.getItem(id).getBean().getComprasDto());
//                    invdto.setCompartimiento(bcrInventario.getItem(id).getBean().getCompartimiento());
//                    invdto.setGalonesCisterna(bcrInventario.getItem(id).getBean().getGalonesCisterna());
//                }
                invdto.setEsNuevo(Boolean.TRUE);
                inventarioHoy.add(invdto);
            }
            if (dia != null && dia.getEstadoId() != null && dia.getEstadoId() == 2) { //cerrado
                List<RecepcionInventario> listRec = service.getRecepcion(estacion.getPaisId(), estacion.getEstacionId(), dfdFecha.getValue()); 
                if(!listRec.isEmpty()){
                    bcrRecepcion.addAll(listRec);
                    recepcion = bcrRecepcion.getItem(bcrRecepcion.getItemIds().get(0)).getBean();
                    tfdDriver.setValue(recepcion.getPiloto());
                    tfdUnit.setValue(recepcion.getUnidad());
                    tfdBill.setValue(recepcion.getFactura());
                }                                                
                List<InventarioRecepcion> listTmpInv = service.getInventarioByFechaEstacion(dia.getFecha(), estacion.getEstacionId());
                if (!listTmpInv.isEmpty()) {    //cubre el caso cuando se olvido ingresar inventario dia anterior.
                    inventarioHoy = listTmpInv;
                    for (InventarioRecepcion item : inventarioHoy) {
                        for (String[] cal : calibraciones) {
                            if (item.getProductoId() == Integer.parseInt(cal[0])) {
                                item.setCalibracion(Double.parseDouble(cal[1]));
                                item.setVentasCons(Double.parseDouble(cal[2]));
                                break;
                            }
                        }
                        item.setVentas((item.getInicialDto() + item.getComprasDto()) - (item.getFinallDto() + item.getCalibracion()));
                        item.setDiferencia(item.getVentasCons() - item.getVentas());
                        item.setEstado((item.getDiferencia() > 0) ? "SOBRANTE" : ((item.getDiferencia() == 0) ? "OK" : "FALTANTE"));
                        item.setEsNuevo(Boolean.FALSE);
                        item.setLecturaVeederRoot(item.getInicialDto()+item.getComprasDto()-item.getVentasCons());
                        item.setDiferencia(item.getInventarioFisico()-item.getLecturaVeederRoot());
                        item.setVarianza((item.getDiferencia()/item.getVentasCons())*100);
                    }
                }
            }
//            else {
//                Calendar ayer = Calendar.getInstance();
//                ayer.setTime(dia.getFecha());
//                ayer.add(Calendar.DATE, -1);
//                inventarioAyer = service.getInventarioByFechaEstacion(ayer.getTime(), estacion.getEstacionId());
//                List<Producto> myprods = service.getCombustiblesByEstacionid(estacion.getEstacionId());
//                int index = 1;
//                InventarioRecepcion invdto;
//                for (Producto p : myprods) {
//                    invdto = new InventarioRecepcion(index++, p.getNombre(), 0D, dia.getFecha(), estacion.getEstacionId(), p.getProductoId(), 0D, 0D, 0D, null, null);
//                    invdto.setVentas(0D);
//                    invdto.setVentasCons(0D);
//                    invdto.setComprasDto(0D);
//                    invdto.setFinallDto(0D);
//                    for (String[] cal : calibraciones) {
//                        if (p.getProductoId() == Integer.parseInt(cal[0])) {
//                            invdto.setCalibracion(Double.parseDouble(cal[1]));
//                            invdto.setVentasCons(Double.parseDouble(cal[2]));
//                            break;
//                        }
//                    }
//                    for (InventarioRecepcion idto : inventarioAyer) {
//                        if (p.getProductoId().equals(idto.getProductoId())) {
////                        invdto.setInicial(idto.getFinall());
//                            invdto.setInicialDto(idto.getFinall());
//                            break;
//                        }
//                    }
//                    inventarioHoy.add(invdto);
//                }
//            }

        }

        bcrInventario.removeAllItems();
        bcrInventario.addAll(inventarioHoy);

        service.closeConnections();

        lblTotalVentas.addStyleName(ValoTheme.LABEL_BOLD);
        lblTotalVentas.setSizeUndefined();
        lblTotalDinero.addStyleName(ValoTheme.LABEL_BOLD);
        lblTotalDinero.setSizeUndefined();
        lblDiferencia.addStyleName(ValoTheme.LABEL_BOLD);
        lblDiferencia.addStyleName(ValoTheme.LABEL_H2);
        lblDiferencia.setSizeUndefined();
    }

    private void buildControls() {
        buildLabelInfo();

        //TODO: Los controles de pais y estacion se deben mostrar solo a los usuarios de rol de oficina.
        SvcTurnoCierre svcTC = new SvcTurnoCierre();
        cbxPais = new ComboBox("País:", new ListContainer<Pais>(Pais.class, svcTC.getAllPaises()));
        svcTC.closeConnections();

        cbxPais.setItemCaptionPropertyId("nombre");
        cbxPais.setItemIconPropertyId("flag");
        cbxPais.setNullSelectionAllowed(false);
//        cbxPais.setVisible(user.isAdministrativo() || user.isGerente());
        cbxPais.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxPais.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                pais = new Pais();
                pais = (Pais) cbxPais.getValue();
                SvcEstacion svcEstacion = new SvcEstacion();
//                List<Estacion> estaciones = svcEstacion.getStationsByCountry(pais.getPaisId());
                Container estacionContainer = new ListContainer<Estacion>(Estacion.class, svcEstacion.getStationsByCountryUser(pais.getPaisId(), user.getUsuarioId()));
                svcEstacion.closeConnections();
                cbxEstacion.setContainerDataSource(estacionContainer);
                currencySymbol = pais.getMonedaSimbolo() + " ";
            }
        });

        cbxEstacion = new ComboBox("Estación:");
        cbxEstacion.setItemCaptionPropertyId("nombre");
        cbxEstacion.setNullSelectionAllowed(false);
        cbxEstacion.setFilteringMode(FilteringMode.CONTAINS);
        cbxEstacion.setWidth("400px");
//        cbxEstacion.setVisible(user.isAdministrativo() || user.isGerente());
        cbxEstacion.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxEstacion.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                estacion = (Estacion) cbxEstacion.getValue();
                dfdFecha.setValue(null);
                bcrTurnos.removeAllItems();
                bcrVentas.removeAllItems();
                bcrProducto.removeAllItems();
                bcrMediospago.removeAllItems();
                bcrEfectivo.removeAllItems();
                bcrDeposito.removeAllItems();
                calcularSumas();
                printDataLabel();

                SvcTurnoCierre service = new SvcTurnoCierre();
                ultimoDia = (ultimoDia.getFecha() == null) ? service.getUltimoDiaByEstacionid(estacion.getEstacionId()) : ultimoDia;
                dia = service.getDiaActivoByEstacionid(estacion.getEstacionId());
//            dia = (dia.getEstadoId() == null) ? ultimoDia : dia;
                service.closeConnections();
                determinarPermisos();
                calculosInventario();
            }
        });

        dfdFecha = new DateField("Navegar día:");
        dfdFecha.setDateFormat("dd/MM/yyyy");
        dfdFecha.setRangeEnd(Date.from(Instant.now()));
        dfdFecha.setLocale(new Locale("es", "ES"));
        dfdFecha.setEnabled(bcrTurnos.getItemIds().isEmpty());  //No hay dia activo.
        dfdFecha.addStyleName(ValoTheme.DATEFIELD_SMALL);
        dfdFecha.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (dfdFecha.getValue() != null) {
                    bcrTurnos.removeAllItems();
                    bcrVentas.removeAllItems();
                    bcrProducto.removeAllItems();
                    bcrMediospago.removeAllItems();
                    bcrEfectivo.removeAllItems();
                    bcrDeposito.removeAllItems();
                    SvcTurno svcTurno = new SvcTurno();
                    //se obtiene de base de datos pues necesitamos saber el estado.
                    dia = svcTurno.getDiaByEstacionidFecha(estacion.getEstacionId(), dfdFecha.getValue());
                    dia.setFecha((dia.getFecha() == null) ? dfdFecha.getValue() : dia.getFecha());  //dia es siempre el mismo que lo seleccionado en el control
                    svcTurno.closeConnections();
                    getAllData();
                    for (Integer tid : bcrTurnos.getItemIds()) {
                        bcrTurnos.getItem(tid).getItemProperty("selected").setValue(Boolean.TRUE);
                    }

                    bcrDeposito = new BeanContainer<Integer, GenericDepositoDet>(GenericDepositoDet.class);
                    //  ContDepositoDet = new BeanItemContainer<GenericDepositoDet>(GenericDepositoDet.class);
                    //  bcrDeposito.addAll(daoDeposito.getDepositoByEstacion(estacion.getEstacionId()));

                  //  }                    
                    //Importante
//                    binder.bindMemberFields(this);
//                    binder.setItemDataSource(recepcion);
//                    
//                    recepcion = bcrRecepcion.getItem(dfdFecha.getValue()).getBean();
//                    binder.setItemDataSource(recepcion);

                    determinarPermisos();
                    calcularSumas();
                    printDataLabel();

                }
//determinarPermisos();
            }
        });
        dfdFecha.setValue(dia.getFecha());
    }

    private void determinarPermisos() {
//        Calendar udMenosUno = null;
//        if (ultimoDia.getFecha()!=null) {
//            udMenosUno = Calendar.getInstance();
//            udMenosUno.setTime(ultimoDia.getFecha());
//            udMenosUno.add(Calendar.DATE, -1);
//        }
//       
//        boolean explorar = false, editar = false, cerrarDia = false;
//        if (dia.getEstadoId() == null && dia.getFecha() != null && ultimoDia.getFecha() != null
//                && (dia.getFecha().equals(ultimoDia.getFecha()) || dia.getFecha().after(ultimoDia.getFecha()))) {
//            explorar = true;
//        } else if (dia.getEstadoId() == null && dia.getFecha() != null && ultimoDia.getFecha() != null
//                && dia.getFecha().before(ultimoDia.getFecha())) {
//            explorar = true;
//        } else if (user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR) 
//                && dia.getEstadoId()!=null && dia.getEstadoId()== 2) {
//            explorar = true;
//        } else if (user.getRolLogin().equals(Constant.ROL_LOGIN_SUPERVISOR) 
//                && dia.getEstadoId()!=null && dia.getEstadoId()== 1) {    //dia abierto
//            editar = cerrarDia = true;
//explorar = true;
//        } else if ( (user.isAdministrativo() || user.isGerente()) 
//                && dia.getFecha()!=null && ultimoDia.getFecha()!=null && udMenosUno!=null && ultimoDia.getEstadoId()==1 && udMenosUno.getTime().equals(dia.getFecha()) ) {
//            explorar = cerrarDia = true;
//        } else if ( (user.isAdministrativo() || user.isGerente()) 
//                && dia.getFecha()!=null && ultimoDia.getFecha()!=null && ultimoDia.getEstadoId()==2 && ultimoDia.getFecha().equals(dia.getFecha()) ) {
//            explorar = cerrarDia = true;
//        } else if (user.isAdministrativo() || user.isGerente()) {
//            explorar = true;
//        }
//
//        dfdFecha.setEnabled(explorar);   //habilitado
//        btnGuardar.setEnabled(cerrarDia);    //habilitado (cerrado)
    }

    private void buildTableCuadre() {
        tableTurno.setCaption("Turnos:");
        tableTurno.setContainerDataSource(bcrTurnos);
        tableTurno.addStyleName(ValoTheme.TABLE_SMALL);
        tableTurno.addGeneratedColumn("colSelected", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("selected");  //Atributo del bean
                final CheckBox cbxSel = new CheckBox("", pro);
                cbxSel.setValue(Boolean.FALSE);
                cbxSel.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        refreshAllData();
                    }
                });
                return cbxSel;
            }
        });
        tableTurno.setVisibleColumns(new Object[]{"colSelected", "nombre"});
        tableTurno.setColumnHeaders(new String[]{"", "Turno"});
        tableTurno.setColumnAlignments(Align.CENTER, Align.LEFT);
        tableTurno.setSizeUndefined();
        tableTurno.setHeight(200f, Unit.PIXELS);
    }

    private void calcularSumas() {
        totalVentas = totalDinero = 0D;
        Double tpVolumen = 0D, tpVentas = 0D, tpProducto = 0D, tpMediospago = 0D, tpEfectivo = 0D, tpDeposito = 0D;
        for (Integer id : bcrVentas.getItemIds()) {
            totalVentas += bcrVentas.getItem(id).getBean().getVenta();
            tpVentas += bcrVentas.getItem(id).getBean().getVenta();
            tpVolumen += bcrVentas.getItem(id).getBean().getVolumen();
        }
        for (Integer id : bcrProducto.getItemIds()) {
            totalVentas += bcrProducto.getItem(id).getBean().getValue();
            tpProducto += bcrProducto.getItem(id).getBean().getValue();
        }
        for (Integer id : bcrMediospago.getItemIds()) {
            totalDinero += bcrMediospago.getItem(id).getBean().getValue();
            tpMediospago += bcrMediospago.getItem(id).getBean().getValue();
        }
        for (Integer id : bcrEfectivo.getItemIds()) {
            totalDinero += bcrEfectivo.getItem(id).getBean().getValue();
            tpEfectivo += bcrEfectivo.getItem(id).getBean().getValue();
        }

        for (Integer id : bcrDeposito.getItemIds()) {
            totalDinero += bcrDeposito.getItem(id).getBean().getMonto();
            tpDeposito += bcrDeposito.getItem(id).getBean().getMonto();
        }

        tableVentas.setFooterVisible(true);
        tableVentas.setColumnFooter("nombreDespacho", "Total:");
        tableVentas.setColumnFooter("venta", symCurrency + numberFmt.format(tpVentas));
        tableVentas.setColumnFooter("volumen", symVolumen + numberFmt.format(tpVolumen));
        tableProductos.setFooterVisible(true);
        tableProductos.setColumnFooter("nombre", "Total:");
        tableProductos.setColumnFooter("value", symCurrency + numberFmt.format(tpProducto));

        tableMediosPago.setFooterVisible(true);
        tableMediosPago.setColumnFooter("nombre", "Total:");
        tableMediosPago.setColumnFooter("value", symCurrency + numberFmt.format(tpMediospago));
        tableEfectivo.setFooterVisible(true);
        tableEfectivo.setColumnFooter("nombre", "Total:");
        tableEfectivo.setColumnFooter("value", symCurrency + numberFmt.format(tpEfectivo));
        tableDeposito.setFooterVisible(true);
        tableDeposito.setColumnFooter("nombre", "Total:");
        tableDeposito.setColumnFooter("value", symCurrency + numberFmt.format(tpDeposito));

    }

    private void buildTableBombas() {
//        tableBombas = utils.buildTable("Bombas:", 100f, 100f, bcrBombas,
//                new String[]{"nombre"},
//                new String[]{"Nombre"});
//        tableBombas.setSizeUndefined();
//        tableBombas.setHeight(200f, Unit.PIXELS);
    }

    private void buildTableVentas() {
        tableVentas.setCaption("Ventas:");
        tableVentas.setContainerDataSource(bcrVentas);
        tableVentas.setVisibleColumns(new Object[]{"nombreDespacho", "nombreProducto", "volumen", "venta"});
        tableVentas.setColumnHeaders(new String[]{"Despacho", "Producto", "Volumen", "Venta"});
        tableVentas.setSizeUndefined();
        tableVentas.setColumnAlignments(new Table.Align[]{Table.Align.LEFT, Table.Align.LEFT, Table.Align.RIGHT, Table.Align.RIGHT});
        tableVentas.setHeight(200f, Unit.PIXELS);
        tableVentas.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private void buildTableProductos() {
        tableProductos.setCaption("Productos adicionales:");
        tableProductos.setContainerDataSource(bcrProducto);
        tableProductos.setVisibleColumns(new Object[]{"nombre", "value"});
        tableProductos.setColumnHeaders(new String[]{"Nombre", "Monto"});
        tableProductos.setColumnAlignments(Table.Align.LEFT, Table.Align.RIGHT);
        tableProductos.setSizeUndefined();
        tableProductos.setHeight(200f, Unit.PIXELS);
        tableProductos.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private void buildTableMediosPago() {
        tableMediosPago.setCaption("Medios de pago:");
        tableMediosPago.setContainerDataSource(bcrMediospago);
        tableMediosPago.setVisibleColumns(new Object[]{"nombre", "cantidad", "value"});
        tableMediosPago.setColumnHeaders(new String[]{"Nombre", "Cantidad", "Valor"});
        tableMediosPago.setColumnAlignments(Table.Align.LEFT, Table.Align.RIGHT, Table.Align.RIGHT);
        tableMediosPago.setHeight(200f, Unit.PIXELS);
        tableMediosPago.addStyleName(ValoTheme.TABLE_COMPACT);
        tableMediosPago.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private void buildTableEfectivo() {
        tableEfectivo.setCaption("Efectivo:");
        tableEfectivo.setContainerDataSource(bcrEfectivo);
        tableEfectivo.setVisibleColumns(new Object[]{"nombre", "value"});
        tableEfectivo.setColumnHeaders(new String[]{"Nombre", "Valor"});
        tableEfectivo.setColumnAlignments(Table.Align.LEFT, Table.Align.RIGHT);
        tableEfectivo.setHeight(200f, Unit.PIXELS);
        tableEfectivo.addStyleName(ValoTheme.TABLE_COMPACT);
        tableEfectivo.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private void buildTableDeposito() {
        tableDeposito.setCaption("Depósito:");
        tableDeposito.setContainerDataSource(bcrDeposito);
        tableDeposito.setVisibleColumns(new Object[]{"mediopago", "numeroboleta","monto"});
        tableDeposito.setColumnHeaders(new String[]{"Nombre", "Número Boleta","Valor"});
//        tableDeposito.setColumnAlignments(Table.Align.LEFT, Table.Align.RIGHT);
        tableDeposito.setHeight(200f, Unit.PIXELS);
        tableDeposito.addStyleName(ValoTheme.TABLE_COMPACT);
        tableDeposito.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private void buildTableInventario() {

        tblInventory.setContainerDataSource(bcrInventario);
        tblInventory.addStyleName(ValoTheme.TABLE_COMPACT);
        tblInventory.addStyleName(ValoTheme.TABLE_SMALL);
        tblInventory.setHeight(200f, Unit.PIXELS);
        tblInventory.addGeneratedColumn("colInicial", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("inicialDto");  //Atributo del bean
                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                tfdValue.setNullRepresentation("0.00");
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.addStyleName("align-right");
                tfdValue.setEnabled(inventarioAyer.isEmpty());
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        InventarioRecepcion invdto = (InventarioRecepcion) ((BeanItem) tblInventory.getItem(itemId)).getBean();
//                        invdto.setVentas((invdto.getInicialDto() + invdto.getComprasDto()) - (invdto.getFinallDto() + invdto.getCalibracion()));
                        Double valIniDto = invdto.getInicialDto()==null?0.0:invdto.getInicialDto();
                        Double valComprasDto = invdto.getComprasDto()==null?0.0:invdto.getComprasDto(); 
                        Double valCalibracion = invdto.getCalibracion()==null?0.0:invdto.getCalibracion();
                        Double valFinalDto = invdto.getFinallDto()==null?0.0:invdto.getFinallDto();
                        Double valVentasCons = invdto.getVentasCons()==null?0.0:invdto.getVentasCons();
                        Double valCompras = invdto.getCompras()==null?0.0:invdto.getCompras();

                        invdto.setVentas(valIniDto + valComprasDto + valCalibracion - valFinalDto);
                        invdto.setDiferencia(valVentasCons - invdto.getVentas());
                        invdto.setEstado((invdto.getDiferencia() > 0) ? "SOBRANTE" : ((invdto.getDiferencia() == 0) ? "OK" : "FALTANTE"));
                        bcrInventario.getItem(itemId).getItemProperty("ventas").setValue(invdto.getVentas());
                        bcrInventario.getItem(itemId).getItemProperty("diferencia").setValue(invdto.getDiferencia());
                        bcrInventario.getItem(itemId).getItemProperty("lecturaVeederRoot").setValue(valIniDto+valCompras-valVentasCons);
                        bcrInventario.getItem(itemId).getItemProperty("estado").setValue(invdto.getEstado());
                    }
                });
                return tfdValue;
            }
        });
        tblInventory.addGeneratedColumn("colCompras", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("comprasDto");  //Atributo del bean
                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                tfdValue.setNullRepresentation("0.00");
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        InventarioRecepcion invdto = (InventarioRecepcion) ((BeanItem) tblInventory.getItem(itemId)).getBean();
//                        invdto.setVentas((invdto.getInicialDto() + invdto.getComprasDto()) - (invdto.getFinallDto() + invdto.getCalibracion()));                        
                        invdto.setVentas(invdto.getInicialDto() + invdto.getComprasDto() + invdto.getCalibracion() - invdto.getFinallDto());
                        invdto.setDiferencia(invdto.getVentasCons() - invdto.getVentas());
                        invdto.setEstado((invdto.getDiferencia() > 0) ? "SOBRANTE" : ((invdto.getDiferencia() == 0) ? "OK" : "FALTANTE"));
                        bcrInventario.getItem(itemId).getItemProperty("ventas").setValue(invdto.getVentas());                        
                        bcrInventario.getItem(itemId).getItemProperty("estado").setValue(invdto.getEstado());
                        bcrInventario.getItem(itemId).getItemProperty("inicialDto").setValue(invdto.getInicialDto());
                        bcrInventario.getItem(itemId).getItemProperty("lecturaVeederRoot").setValue(invdto.getInicialDto()+invdto.getComprasDto()-invdto.getVentasCons());
                        bcrInventario.getItem(itemId).getItemProperty("diferencia").setValue(invdto.getDiferencia());
                        bcrInventario.getItem(itemId).getItemProperty("varianza").setValue(invdto.getDiferencia()/invdto.getVentasCons());
                    }
                });
                return tfdValue;
            }
        });
        tblInventory.addGeneratedColumn("colVentas", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("ventasCons");  //Atributo del bean
                Label lbl = new Label(utils.getPropertyFormatterDouble(pro));
                lbl.setWidth("85px");
                lbl.addStyleName(ValoTheme.LABEL_SMALL);
                lbl.addStyleName("align-right");
                return lbl;
            }
        });
        tblInventory.addGeneratedColumn("colCalibracion", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("calibracion");  //Atributo del bean
                Label lbl = new Label(utils.getPropertyFormatterDouble(pro));
                lbl.setWidth("85px");
                lbl.addStyleName(ValoTheme.LABEL_SMALL);
                lbl.addStyleName("align-right");
                return lbl;
            }
        });
        tblInventory.addGeneratedColumn("colDiferencia", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("diferencia");  //Atributo del bean
                Label lbl = new Label(utils.getPropertyFormatterDouble(pro));
                lbl.setWidth("85px");
                lbl.addStyleName(ValoTheme.LABEL_SMALL);
                lbl.addStyleName("align-right");
                return lbl;
            }
        });
        tblInventory.addGeneratedColumn("colVarianza", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("varianza");  //Atributo del bean
                Label lbl = new Label(utils.getPropertyFormatterDouble(pro));
                lbl.setWidth("85px");
                lbl.addStyleName(ValoTheme.LABEL_SMALL);
                lbl.addStyleName("align-right");
                return lbl;
            }
        });
        tblInventory.addGeneratedColumn("colInvFisico", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("inventarioFisico");  //Atributo del bean
                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.setNullRepresentation("0.00");
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        InventarioRecepcion invdto = bcrInventario.getItem(itemId).getBean();
                        Double invFis = invdto.getInventarioFisico()==null?0.0:invdto.getInventarioFisico();
                        Double invTeo = invdto.getLecturaVeederRoot()==null?0.0:invdto.getLecturaVeederRoot();
                        bcrInventario.getItem(itemId).getItemProperty("diferencia").setValue(invFis-invTeo);
                        bcrInventario.getItem(itemId).getItemProperty("varianza").setValue((invdto.getDiferencia()/invdto.getVentasCons())*100);
//                        bcrInventario.getItem(itemId).getItemProperty("inventarioFisico").setValue(invFis);                        
                    }
                });
                return tfdValue;
            }
        });
        tblInventory.addGeneratedColumn("colVeederroot", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("lecturaVeederRoot");  //Atributo del bean
                Label lbl = new Label(utils.getPropertyFormatterDouble(pro));
                lbl.setWidth("85px");
                lbl.setStyleName(ValoTheme.LABEL_SMALL);
                lbl.addStyleName("align-right");
                lbl.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        InventarioRecepcion invdto = bcrInventario.getItem(itemId).getBean();
                        Double invFis = invdto.getInventarioFisico();
                        bcrInventario.getItem(itemId).getItemProperty("diferencia").setValue(invdto.getInventarioFisico()-invdto.getLecturaVeederRoot());
                        bcrInventario.getItem(itemId).getItemProperty("varianza").setValue((invdto.getDiferencia()/invdto.getVentasCons())*100);
//                        bcrInventario.getItem(itemId).getItemProperty("inventarioFisico").setValue(invFis);                        
                    }
                });
                return lbl;
            }
        });

        tblInventory.addGeneratedColumn("colCompartimiento", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("compartimiento");  //Atributo del bean
                TextField tfdValue = new TextField(pro);
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.setNullRepresentation(""); //tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        InventarioRecepcion invdto = bcrInventario.getItem(itemId).getBean();
                        String comp = invdto.getCompartimiento();
                    }
                });
                return tfdValue;
            }
        });
//        tblInventory.addGeneratedColumn("colVolFacturado", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("volFacturado");  //Atributo del bean
//                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
//                tfdValue.setWidth("85px"); tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
//                tfdValue.setNullRepresentation("0.00"); tfdValue.addStyleName("align-right");
//                return tfdValue;
//            }
//        });
//        tblInventory.addGeneratedColumn("colPulgadas", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("pulgadas");  //Atributo del bean
//                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
//                tfdValue.setWidth("85px");
//                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
//                tfdValue.setNullRepresentation(""); //tfdValue.addStyleName("align-right");
//                return tfdValue;
//            }
//        });
        tblInventory.addGeneratedColumn("colGalones", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("galonesCisterna");  //Atributo del bean
                TextField tfdValue = new TextField(utils.getPropertyFormatterInteger(pro));
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.setNullRepresentation("0.00");
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        InventarioRecepcion invdto = bcrInventario.getItem(itemId).getBean();
                        int galones = invdto.getGalonesCisterna();
                    }
                });
                return tfdValue;
            }
        });
//        tblInventory.addGeneratedColumn("colCalibracion", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("galonesCisterna");  //Atributo del bean
//                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
//                tfdValue.setWidth("85px");
//                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
//                tfdValue.setNullRepresentation("0.00");
//                tfdValue.addStyleName("align-right");
//                return tfdValue;
//            }
//        });
//        tblInventory.addGeneratedColumn("colDif", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("galonesCisterna");  //Atributo del bean
//                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
//                tfdValue.setWidth("85px");
//                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
//                tfdValue.setNullRepresentation("0.00");
//                tfdValue.addStyleName("align-right");
//                return tfdValue;
//            }
//        });
//        tblInventory.addGeneratedColumn("colVarianza", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("galonesCisterna");  //Atributo del bean
//                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
//                tfdValue.setWidth("85px");
//                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
//                tfdValue.setNullRepresentation("0.00");
//                tfdValue.addStyleName("align-right");
//                return tfdValue;
//            }
//        });
            tblInventory.addGeneratedColumn("colTanque", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("tanque");  //Atributo del bean
                Label lbl = new Label(pro);
                lbl.setWidth("85px");
                lbl.addStyleName(ValoTheme.LABEL_SMALL);
                return lbl;
            }
        });
        tblInventory.setVisibleColumns(new Object[]{"tanque","productoNombre", "colInicial", "colCompras", "colVentas", "colCalibracion", "colVeederroot", "colInvFisico", "colDiferencia", "colVarianza", "colCompartimiento", "colGalones"});
        tblInventory.setColumnHeaders(new String[]{"Nombre tanque","Producto", "Iniicial", "Compras", "Ventas", "Calibración", "Inv teórico", "Inv físico", "Diferencia", "% Varianza sobre ventas", "Compartimiento", "Galones"});

        /*        tblInventory.addGeneratedColumn("colFinal", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("finallDto");  //Atributo del bean
                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                tfdValue.setNullRepresentation("0.00");
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        InventarioRecepcion invdto = (InventarioRecepcion) ((BeanItem) tblInventory.getItem(itemId)).getBean();
//                        invdto.setVentas((invdto.getInicialDto() + invdto.getComprasDto()) - (invdto.getFinallDto() + invdto.getCalibracion()));
                        invdto.setVentas(invdto.getInicialDto() + invdto.getComprasDto() + invdto.getCalibracion() - invdto.getFinallDto());
                        invdto.setDiferencia(invdto.getVentasCons() - invdto.getVentas());
                        invdto.setEstado((invdto.getDiferencia() > 0) ? "SOBRANTE" : ((invdto.getDiferencia() == 0) ? "OK" : "FALTANTE"));
                        bcrInventario.getItem(itemId).getItemProperty("ventas").setValue(invdto.getVentas());
                        bcrInventario.getItem(itemId).getItemProperty("diferencia").setValue(invdto.getDiferencia());
                        bcrInventario.getItem(itemId).getItemProperty("estado").setValue(invdto.getEstado());
                    }
                });
                return tfdValue;
            }
        });
        tblInventory.addGeneratedColumn("colDiferencia", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("diferencia");  //Atributo del bean
                Label lbl = new Label(pro);
                lbl.setWidth("85px");
                lbl.addStyleName(ValoTheme.LABEL_SMALL);
                lbl.addStyleName("align-right");
                return lbl;
            }
        });
        tblInventory.addGeneratedColumn("colEstado", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("estado");  //Atributo del bean
                Label lbl = new Label(pro);
                lbl.setWidth("85px");
                return lbl;
            }
        });

        tblInventory.setVisibleColumns(new Object[]{"productoNombre", "colInicial", "colFinal", "colCompras",
            "calibracion", "colVentas", "ventasCons", "colDiferencia", "colEstado"});
        tblInventory.setColumnHeaders(new String[]{"Producto", "Inicial", "Final", "Compras",
            "Calibración", "Ventas", "VentasCons", "Diferencia", "Estado"});
        tblInventory.setColumnAlignments(Table.Align.LEFT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.LEFT);
         */
    }

    private void buildButtons() {
        btnDetalleDeposito = new Button("Detalle Efectivo", FontAwesome.PLUS);
        btnDetalleDeposito.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnDetalleDeposito.addStyleName(ValoTheme.BUTTON_SMALL);
        btnDetalleDeposito.addClickListener(clickEvent -> formDeposito(estacion.getEstacionId(), currencySymbol, pais.getPaisId()));

        btnAll.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btnAll.addStyleName(ValoTheme.BUTTON_LINK);
        btnAll.setIcon(FontAwesome.CHECK_SQUARE_O);
        btnAll.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                List<Integer> ids = (List<Integer>) tableTurno.getItemIds();
                for (Integer i : ids) {
                    tableTurno.getItem(i).getItemProperty("selected").setValue(true);
                }
            }
        });

        btnNone.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btnNone.addStyleName(ValoTheme.BUTTON_LINK);
        btnNone.setIcon(FontAwesome.SQUARE_O);
        btnNone.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                List<Integer> ids = (List<Integer>) tableTurno.getItemIds();
                for (Integer i : ids) {
                    tableTurno.getItem(i).getItemProperty("selected").setValue(false);
                }
            }
        });

        btnGuardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnGuardar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {

                for (Integer itemId : bcrInventario.getItemIds()) {
                    if (bcrInventario.getItem(itemId).getBean().getComprasDto() > 0
                            && (tfdDriver.getValue().trim().isEmpty() || tfdUnit.getValue().trim().isEmpty() || tfdBill.getValue().trim().isEmpty())) {
                        Notification.show("ERROR:", "Dado que hay compras, la información de recepción es obligatoria.", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }

                if (bcrTurnos.getItemIds().isEmpty()) {
                    Notification.show("AVISO:", "No existen turnos para cerrar el día.", Notification.Type.WARNING_MESSAGE);
                    return;
                }

                Turno turno;
                for (Integer bid : bcrTurnos.getItemIds()) {
                    bcrTurnos.getItem(bid).getItemProperty("selected").setValue(Boolean.TRUE);
                    turno = bcrTurnos.getItem(bid).getBean();
                    if (turno.getEstadoId() == 1) {
                        Notification.show("ERROR:", "No es posible cerrar el día con turno abierto (" + turno.getNombre() + ").", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }
                String messageComp = ((totalDinero - totalVentas) < -0.009 || (totalDinero - totalVentas) > 0.009)
                        ? "<h3>El turno tiene diferencia de: <strong>" + lblDiferencia + "</strong></h3>\n" : "";
                MessageBox
                        .createQuestion()
                        .withCaption("Confirmación")
                        .withHtmlMessage(messageComp.concat("<h3>¿Seguro desea cerrar el día definitivamente?</h3>"))
                        .withOkButton(new Runnable() {
                            public void run() {

                                SvcTurnoCierre svcTC = new SvcTurnoCierre();
                                boolean invNuevo = (dia.getEstadoId() == 1);
                                dia.setModificadoPersona(user.getNombreLogin());
                                dia.setModificadoPor(user.getUsername());
                                dia = svcTC.doActionDia(Dao.ACTION_UPDATE, dia);
                                
                                if (tfdDriver.getValue() != null && tfdUnit.getValue() != null && tfdBill.getValue() != null) {
                                    RecepcionInventario recepcion = new RecepcionInventario(null, dia.getFecha(), estacion.getPaisId(), estacion.getEstacionId(), tfdDriver.getValue(), tfdUnit.getValue(), tfdBill.getValue());
                                    recepcion.setCreado_por(user.getUsername());
                                    svcTC.doActionInvRecepcion(Dao.ACTION_ADD, recepcion);
                                }
                                
                                InventarioRecepcion inventario;
                                //InventarioRecepcion idto;
                                for (Integer id : (List<Integer>) tblInventory.getItemIds()) {
                                    inventario = bcrInventario.getItem(id).getBean();
                                    inventario.setInicial(inventario.getInicialDto());
                                    inventario.setFinall(inventario.getFinallDto());
                                    inventario.setCompras((inventario.getComprasDto() == null) ? 0D : inventario.getComprasDto());
                                    inventario.setCreadoPor(user.getUsername());
                                    inventario.setCreadoPersona(user.getNombreLogin());
                                    inventario.setModificadoPor(user.getUsername());
                                    inventario.setModificadoPersona(user.getNombreLogin());                                    
                                    inventario.setInventarioFisico(bcrInventario.getItem(id).getBean().getInventarioFisico());
                                    inventario.setDiferencia(bcrInventario.getItem(id).getBean().getDiferencia());
                                    inventario.setVarianza(bcrInventario.getItem(id).getBean().getVarianza());
                                    inventario.setCompartimiento(bcrInventario.getItem(id).getBean().getCompartimiento());
                                    int galones = bcrInventario.getItem(id).getBean().getGalonesCisterna()==null?0:bcrInventario.getItem(id).getBean().getGalonesCisterna();
                                    inventario.setGalonesCisterna(galones);
                                    inventario.setVentas(bcrInventario.getItem(id).getBean().getVentasCons());
                                    inventario.setCalibracion(bcrInventario.getItem(id).getBean().getCalibracion());
//                                    svcTC.doActionInventario( (invNuevo ? Dao.ACTION_ADD : Dao.ACTION_UPDATE), 
                                    inventario.setVolFacturado(id);
                                    svcTC.doActionInventario((inventario.getEsNuevo() ? Dao.ACTION_ADD : Dao.ACTION_UPDATE),inventario);
                                }                                

                                svcTC.closeConnections();
                                if (dia.getFecha() != null) {
//                                    Notification.show("El dia se ha cerrado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                                    Notification notif = new Notification("ÉXITO:", "El dia se ha cerrado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                                    notif.setDelayMsec(3000);
                                    notif.setPosition(Position.MIDDLE_CENTER);
                                    //notif.setStyleName("mystyle");
                                    //notif.setIcon(new ThemeResource("img/reindeer.png"));
                                    notif.show(Page.getCurrent());
                                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PR_DAY_CLOSE.getViewName());
                                } else {
                                    Notification.show("Ocurrió un cerrar el turno.\n" + dia.getDescError(), Notification.Type.ERROR_MESSAGE);
                                    return;
                                }

                            }
                        },
                                ButtonOption.caption("Sí") //button caption
                        )
                        .withCancelButton(new Runnable() {
                            public void run() {
                                /*Nothing to do here*/
                            }
                        },
                                ButtonOption.caption("Cancelar") //buton caption
                        )
                        .open();
                //Fin MessageBox

            }
        });

    }

    private HorizontalLayout buildDetalleMontos() {
        Label label3 = new Label("Diferencia: ");
        label3.addStyleName(ValoTheme.LABEL_BOLD);
        label3.addStyleName(ValoTheme.LABEL_H2);
        label3.setSizeUndefined();

        VerticalLayout vlLeft = new VerticalLayout(
                //new Label("Efectivo: "), new Label("Ventas: "), 
                label3);
        vlLeft.setMargin(new MarginInfo(false, true, false, false));
        Label label6 = new Label(symCurrency + "\t\t\t");
        label6.addStyleName(ValoTheme.LABEL_BOLD);
        label6.addStyleName(ValoTheme.LABEL_H2);
        label6.setSizeUndefined();
        VerticalLayout vlMiddle = new VerticalLayout(//new Label(symCurrency + "\t"), new Label(symCurrency + "\t\t"), 
                label6);
        vlMiddle.setMargin(new MarginInfo(false, true, false, false));
        VerticalLayout vlRight = new VerticalLayout(//lblTotalDinero, lblTotalVentas, 
                lblDiferencia);
//        vlRight.setMargin(new MarginInfo(true, true, false, false));
        vlRight.setSizeUndefined();
        Responsive.makeResponsive(vlRight);
//        vlRight.setComponentAlignment(lblTotalVentas, Alignment.TOP_RIGHT);
//        vlRight.setComponentAlignment(lblTotalDinero, Alignment.TOP_RIGHT);
        vlRight.setComponentAlignment(lblDiferencia, Alignment.TOP_RIGHT);
        HorizontalLayout result = new HorizontalLayout(vlLeft, vlMiddle, vlRight);
        result.setWidth("100%");
        return result;
    }

    private void refreshAllData() {
        SvcTurnoCierre svcTC = new SvcTurnoCierre();
        SvcDeposito svcDep = new SvcDeposito();
//                        String arqueosIds = "";
//                        //Determinar bombas
////                        bcrBombas.removeAllItems();
//                        Arqueocaja arqueo;
//                        for (Integer acId : bcrTurnos.getItemIds()) {
////                            arqueo = bcrTurnos.getItem(acId).getBean();
////                            if (arqueo.getSelected()) {
//////                                bcrBombas.addAll(svcTC.getBombasByArquoid(arqueo.getArqueocajaId().toString()));
////                                arqueosIds += (arqueosIds.isEmpty()) ? arqueo.getArqueocajaId().toString() : ",".concat(arqueo.getArqueocajaId().toString());
////                            }
//                        }
        //Determinar ventas
        bcrVentas.removeAllItems();
        String turnosIds = "";
        for (Integer bId : bcrTurnos.getItemIds()) {
            if (bcrTurnos.getItem(bId).getBean().getSelected()) {
                turnosIds += turnosIds.isEmpty() ? bId.toString() : ",".concat(bId.toString());
            }
        }
        if (!turnosIds.isEmpty()) {
            bcrVentas.addAll(svcTC.getArqueo(turnosIds, null, "E"));
        }
        //Determinar productos
        bcrProducto.removeAllItems();
        //Determinar medios de pago
        bcrMediospago.removeAllItems();
        //Determinar medios de pago (efectivo)
        bcrEfectivo.removeAllItems();
        if (!turnosIds.isEmpty()) {
            bcrProducto.addAll(svcTC.getProductoByTurnoid(turnosIds));
            bcrMediospago.addAll(svcTC.getMediopagoByTurnosid(turnosIds));
            bcrEfectivo.addAll(svcTC.getEfectivoByTurnosid(turnosIds));
            String p = new SimpleDateFormat("yyyy-MM-dd").format(dfdFecha.getValue());
            bcrDeposito.addAll(svcDep.getDepositoByEstacion(estacion.getEstacionId().toString(), p));
        }
        svcTC.closeConnections();
        calcularSumas();
        lblTotalVentas.setValue(numberFmt.format(totalVentas));
        lblTotalDinero.setValue(numberFmt.format(totalDinero));

        printDataLabel();
    }

    private void printDataLabel() {
        String style = ((totalDinero - totalVentas) < -0.009) ? "style=\"color:red;\"" : "";
        lblDiferencia.setValue("<strong " + style + " >" + numberFmt.format(totalDinero - totalVentas) + "</strong>");
        lblDiferencia.setContentMode(ContentMode.HTML);
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

//        fechaString = (ultimoTurno.getTurnoId() != null) ? "Turno ".concat(ultimoTurno.getTurnoId().toString()) : "INEXISTENTE";
//        estadoName = (ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 1)
//                ? "ABIERTO" : ((ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 2) ? "CERRADO" : "SIN ESTADO");
//        lblUltimoTurno.setValue("Último turno: " + fechaString + " (" + estadoName + ")");
//        lblUltimoTurno.addStyleName(ValoTheme.LABEL_BOLD);
//        lblUltimoTurno.addStyleName(ValoTheme.LABEL_H3);
//        lblUltimoTurno.addStyleName(ValoTheme.LABEL_COLORED);
//        lblUltimoTurno.setSizeUndefined();
////        lblUltimoTurno.setWidth("350px");
    }

    public void calculosInventario() {

        SvcTurnoCierre service = new SvcTurnoCierre();
        List<InventarioRecepcion> inventarioHoy = new ArrayList();
        if (dia != null && dia.getEstadoId() != null) {
            List<String[]> calibraciones = service.getCalibracionByFechaEstacion(dia.getFecha(), estacion.getEstacionId()); //de hoy

            Calendar ayer = Calendar.getInstance();
            ayer.setTime(dia.getFecha());
            ayer.add(Calendar.DATE, -1);
            inventarioAyer = service.getInventarioByFechaEstacion(ayer.getTime(), estacion.getEstacionId());
            List<Producto> myprods = service.getCombustiblesByEstacionid(estacion.getEstacionId());
            int index = 1;
            InventarioRecepcion invdto;
            for (Producto p : myprods) {
                invdto = new InventarioRecepcion(index++, p.getNombre(), 0D, dia.getFecha(), estacion.getEstacionId(), p.getProductoId(), 0D, 0D, 0D, null, null);
                invdto.setVentas(0D);
                invdto.setVentasCons(0D);
                invdto.setComprasDto(0D);
                invdto.setFinallDto(0D);
                invdto.setTanque(p.getTanque());
                for (String[] cal : calibraciones) {
                    if (p.getProductoId() == Integer.parseInt(cal[0])) {
                        invdto.setCalibracion(Double.parseDouble(cal[1]));
//                            invdto.setVentasCons(Double.parseDouble(cal[2]));
                        invdto.setVentasCons(Double.parseDouble(cal[2]) / 2);
                        break;
                    }
                }
                for (InventarioRecepcion idto : inventarioAyer) {
                    if (p.getProductoId().equals(idto.getProductoId())) {
//                        invdto.setInicial(idto.getFinall());
                        invdto.setInicialDto(idto.getFinall());
                        break;
                    }
                }
                invdto.setEsNuevo(Boolean.TRUE);
                inventarioHoy.add(invdto);
            }

            if (dia != null && dia.getEstadoId() != null && dia.getEstadoId() == 2) { //cerrado
                List<InventarioRecepcion> listTmpInv = service.getInventarioByFechaEstacion(dia.getFecha(), estacion.getEstacionId());
                if (!listTmpInv.isEmpty()) {    //cubre el caso cuando se olvido ingresar inventario dia anterior.
                    inventarioHoy = listTmpInv;
                    for (InventarioRecepcion item : inventarioHoy) {
                        for (String[] cal : calibraciones) {
                            if (item.getProductoId() == Integer.parseInt(cal[0])) {
                                item.setCalibracion(Double.parseDouble(cal[1]));
                                item.setVentasCons(Double.parseDouble(cal[2]));
                                break;
                            }
                        }
                        System.out.println("entra a lectura veeder "+item.getInicialDto());
                        System.out.println("entra a lectura veeder "+item.getComprasDto());
                        item.setLecturaVeederRoot(item.getInicialDto()+item.getComprasDto());
                        item.setVentas((item.getInicialDto() + item.getComprasDto()) - (item.getFinallDto() + item.getCalibracion()));
                        item.setDiferencia(item.getVentasCons() - item.getVentas());
                        item.setEstado((item.getDiferencia() > 0) ? "SOBRANTE" : ((item.getDiferencia() == 0) ? "OK" : "FALTANTE"));
                        item.setEsNuevo(Boolean.FALSE);
                    }
                }
            }
//            else {
//                Calendar ayer = Calendar.getInstance();
//                ayer.setTime(dia.getFecha());
//                ayer.add(Calendar.DATE, -1);
//                inventarioAyer = service.getInventarioByFechaEstacion(ayer.getTime(), estacion.getEstacionId());
//                List<Producto> myprods = service.getCombustiblesByEstacionid(estacion.getEstacionId());
//                int index = 1;
//                InventarioRecepcion invdto;
//                for (Producto p : myprods) {
//                    invdto = new InventarioRecepcion(index++, p.getNombre(), 0D, dia.getFecha(), estacion.getEstacionId(), p.getProductoId(), 0D, 0D, 0D, null, null);
//                    invdto.setVentas(0D);
//                    invdto.setVentasCons(0D);
//                    invdto.setComprasDto(0D);
//                    invdto.setFinallDto(0D);
//                    for (String[] cal : calibraciones) {
//                        if (p.getProductoId() == Integer.parseInt(cal[0])) {
//                            invdto.setCalibracion(Double.parseDouble(cal[1]));
//                            invdto.setVentasCons(Double.parseDouble(cal[2]));
//                            break;
//                        }
//                    }
//                    for (InventarioRecepcion idto : inventarioAyer) {
//                        if (p.getProductoId().equals(idto.getProductoId())) {
////                        invdto.setInicial(idto.getFinall());
//                            invdto.setInicialDto(idto.getFinall());
//                            break;
//                        }
//                    }
//                    inventarioHoy.add(invdto);
//                }
//            }

        }

        bcrInventario.removeAllItems();
        bcrInventario.addAll(inventarioHoy);

        service.closeConnections();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new Dao();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        //btnGuardar.setEnabled(acceso.isAgregar());
        btnGuardar.setEnabled(acceso.isCambiar());
    }

    /*Metodo Llama Forma Detalle de depositos */// jlopez
    private void formDeposito(Integer idestacion, String simboloMoneda, Integer idpais) {
        System.out.println("ingresa a metodo formDeposito");
        if (cbxEstacion.getValue() != null) {
            System.out.println("dentro de for cbxestacion");
            formDetalleDeposito = new FormDetalleDeposito(simboloMoneda, bcrDeposito, idpais, idestacion);
            //formDetalleDeposito = new FormDetalleDeposito(simboloMoneda, idpais, idestacion);
            formDetalleDeposito.addCloseListener((e) -> {
                bcrDeposito = new BeanContainer<Integer, GenericDepositoDet>(GenericDepositoDet.class);
                bcrDeposito = (BeanContainer<Integer, GenericDepositoDet>) VaadinSession.getCurrent().getAttribute("detalleDeposito");

            });
            getUI().addWindow(formDetalleDeposito);
            formDetalleDeposito.focus();
        }
    }


}
