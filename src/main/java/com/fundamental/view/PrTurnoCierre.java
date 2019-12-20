package com.fundamental.view;

import com.fundamental.model.Acceso;
import com.fundamental.model.Arqueocaja;
import com.fundamental.model.Bomba;
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
import com.fundamental.services.Dao;
import com.fundamental.services.SvcDetalleTcClientes;
import com.fundamental.services.SvcEstacion;
import com.fundamental.services.SvcTurno;
import com.fundamental.services.SvcTurnoCierre;
import com.sisintegrados.generic.bean.ArqueoTC;
import com.sisintegrados.generic.bean.GenericBeanCliente;
import com.sisintegrados.generic.bean.GenericBeanMedioPago;
import com.sisintegrados.generic.bean.GenericDetalleBCR;
import com.sisintegrados.generic.bean.GenericDetalleFM;
import com.sisintegrados.generic.bean.GenericLote;
import com.sisintegrados.view.form.FormDetalleBCR;
import com.sisintegrados.view.form.FormDetalleCliDavivienda;
import com.sisintegrados.view.form.FormDetalleCliScottia;
import com.sisintegrados.view.form.FormDetalleCredomatic;
import com.sisintegrados.view.form.FormDetalleDavivienda;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect;
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
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.vaadin.addons.stackpanel.StackPanel;
import org.vaadin.maddon.ListContainer;

/**
 * @author Mery Gil
 */
public class PrTurnoCierre extends Panel implements View {

    static final DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00;-#");

    VerticalLayout root;
    Table tableBombas;
    Label lblTotalVentas = new Label(numberFmt.format(0D)),
            lblTotalDinero = new Label(numberFmt.format(0D)),
            lblDiferencia = new Label(numberFmt.format(0D));
    Button btnGuardar = new Button("Cerrar turno"),
            btnAll = new Button("Todas"),
            btnNone = new Button("Ninguna");

    Label lblUltimoDía = new Label("Último día:"),
            lblUltimoTurno = new Label("Último turno:");

    ComboBox cbxPais = new ComboBox("Pais:"),
            cbxEstacion = new ComboBox("Estación:"),
            cbxTurno = new ComboBox("Turnos:");
    DateField dfdFecha = new DateField("Navegar día:");
    Pais pais;
    Estacion estacion;
    Turno turno, ultimoTurno;
    Dia dia, ultimoDia;
    Container contTurnos = new ListContainer<Turno>(Turno.class, new ArrayList());
    Container contPais = new ListContainer<Pais>(Pais.class, new ArrayList());

    Table tableCuadre = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("diferencia")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tableVentas = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("volumen") || colId.equals("venta")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tableProductos = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("value")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tableMediosPago = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("value")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tableEfectivo = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("value")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    /*Detalle TC *///ASG
    Table tableFMDavivienda = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("venta")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };
    Table tableFMScott = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("venta")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tableBCR = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("venta")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tableCredomatic = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("venta")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    Table tableDavivienda = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("venta")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };
    /*Fin Detalle ASG*/

    BeanContainer<Integer, Arqueocaja> bcrArqueocaja = new BeanContainer<Integer, Arqueocaja>(Arqueocaja.class);
    BeanContainer<Integer, Bomba> bcrBombas = new BeanContainer<Integer, Bomba>(Bomba.class);
    BeanContainer<Integer, DtoArqueo> bcrVentas = new BeanContainer<Integer, DtoArqueo>(DtoArqueo.class);
    BeanContainer<Integer, Producto> bcrProducto = new BeanContainer<Integer, Producto>(Producto.class);
    BeanContainer<Integer, Mediopago> bcrMediospago = new BeanContainer<Integer, Mediopago>(Mediopago.class);
    BeanContainer<Integer, Mediopago> bcrEfectivo = new BeanContainer<Integer, Mediopago>(Mediopago.class);
    BeanContainer<Integer, ArqueoTC> bcrTarjeta = new BeanContainer<Integer, ArqueoTC>(ArqueoTC.class);

    /*Detalle TC ASG*/
    SvcDetalleTcClientes dao = new SvcDetalleTcClientes();
    HorizontalLayout hltables = new HorizontalLayout();
    BeanItemContainer<Estacion> ContEstacion = new BeanItemContainer<Estacion>(Estacion.class);
    BeanItemContainer<GenericBeanMedioPago> ContMediosPago = new BeanItemContainer<GenericBeanMedioPago>(GenericBeanMedioPago.class);
    BeanItemContainer<GenericLote> ContLote = new BeanItemContainer<GenericLote>(GenericLote.class);
    BeanItemContainer<GenericLote> ContLoteScott = new BeanItemContainer<GenericLote>(GenericLote.class);
    BeanItemContainer<GenericLote> ContLoteBCR = new BeanItemContainer<GenericLote>(GenericLote.class);
    BeanItemContainer<GenericLote> ContLoteCredomatic = new BeanItemContainer<GenericLote>(GenericLote.class);
    BeanItemContainer<GenericLote> ContLoteDavivienda = new BeanItemContainer<GenericLote>(GenericLote.class);

    BeanItemContainer<GenericBeanCliente> ContCliGen = new BeanItemContainer<GenericBeanCliente>(GenericBeanCliente.class);

    BeanContainer<Integer, GenericDetalleFM> bcrDetalleCliDavi = new BeanContainer<Integer, GenericDetalleFM>(GenericDetalleFM.class);
    BeanContainer<Integer, GenericDetalleFM> bcrDetalleCliScott = new BeanContainer<Integer, GenericDetalleFM>(GenericDetalleFM.class);
    BeanContainer<Integer, GenericDetalleBCR> bcrDetalleCliBCR = new BeanContainer<Integer, GenericDetalleBCR>(GenericDetalleBCR.class);
    BeanContainer<Integer, GenericDetalleBCR> bcrDetalleCliCredomatic = new BeanContainer<Integer, GenericDetalleBCR>(GenericDetalleBCR.class);
    BeanContainer<Integer, GenericDetalleBCR> bcrDetalleCliDavivienda = new BeanContainer<Integer, GenericDetalleBCR>(GenericDetalleBCR.class);

    /*Popups Detalle Clientes TC ASG*/
    FormDetalleCliDavivienda formDetalleCliDavivienda;
    FormDetalleCliScottia formDetalleCliScottia;
    FormDetalleBCR formDetalleBCR;
    FormDetalleCredomatic formDetalleCredomatic;
    FormDetalleDavivienda formDetalleDavivienda;
    //Totales Para Detalles de TC
    Double totFMDavi = 0D;
    Double totFMScott = 0D;
    Double totBCR = 0D;
    Double totCredomatic = 0D;
    Double totDavivienda = 0D;

    /*FIN DETALLE ASG*/
    Double totalVentas = 0D, totalDinero = 0D;
    String symCurrency, symVolumen;
    String currencySymbol;

    Utils utils = new Utils();
    Usuario user;
    List<Precio> precios;
    List<Producto> productos;
    List<Bomba> allBombas;
    Acceso acceso = new Acceso();
    Panel panelDetalles = new Panel();
    Panel panelTableDetalles = new Panel();
    Button btnbac;
    Button btnbanknac;
    Button btnbcr;
    Button btncredomatic;
    Button btndavivienda;
    Button btnfmdavivienda;
    Button btndmscottia;

    public PrTurnoCierre() {
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
        root.addComponent(utils.buildHeader("Cierre de turno", true, true));
        root.addComponent(utils.buildSeparator());
        getAllData();

        buildTableCuadre();
        buildTableBombas();
        buildTableVentas();

        buildTableProductos();
        buildTableMediosPago();
        buildTableEfectivo();

        /*Tablas para detalles de tarjetas de credito*/ //ASG
        buildTableFMDavivienda();
        buildTableFMScottia();
        buildTableBCR();
        buildTableCredomatic();
       // buildTableDavivienda();

        /*FIN ASG*/
        buildFilters();
        buildButtons();

        HorizontalLayout hlContent = new HorizontalLayout();
        Responsive.makeResponsive(hlContent);
        hlContent.setHeight("60%");

        HorizontalLayout hlContent2 = new HorizontalLayout();
        hlContent2.setMargin(new MarginInfo(false, true));
        Responsive.makeResponsive(hlContent2);
        hlContent2.setHeight("60%");

        HorizontalLayout hlContent4 = new HorizontalLayout();
        hlContent4.setMargin(false);
        hlContent4.setSpacing(true);
        hlContent4.setSizeUndefined();
        Responsive.makeResponsive(hlContent4);
        hlContent4.addComponents(buildDetalleMontos(), btnGuardar);
        hlContent4.setComponentAlignment(btnGuardar, Alignment.BOTTOM_LEFT);

        CssLayout cltInfo = new CssLayout();
        cltInfo.setSizeUndefined();
        cltInfo.setWidth(100f, Unit.PERCENTAGE);
        Responsive.makeResponsive(cltInfo);
        cltInfo.addComponents(lblUltimoDía, lblUltimoTurno);

        HorizontalLayout hlCombos = utils.buildHorizontal("hlCombos", false, false, true, false);
        hlCombos.setSizeUndefined();
        CssLayout cltCombo = new CssLayout(utils.vlContainer(cbxPais), utils.vlContainer(cbxEstacion), utils.vlContainer(dfdFecha), utils.vlContainer(cbxTurno));
        hlCombos.addComponents(cltCombo);

        /*ASG detalle*/
        VerticalLayout vldetalles = new VerticalLayout();
        HorizontalLayout hldetalles = new HorizontalLayout();
        vldetalles.setSpacing(true);
        panelDetalles = new SectionPanelButtons("Detalles Tarjetas");
        panelDetalles.setIcon(FontAwesome.ADJUST);
        panelDetalles.setSizeFull();
        StackPanel.extend(panelDetalles)
                .close();
        panelTableDetalles = new SectionPanelTablesDet("Tablas Detalles Tarjetas");
        panelTableDetalles.setIcon(FontAwesome.ADJUST);
        panelTableDetalles.setSizeFull();
        StackPanel.extend(panelTableDetalles);
        hldetalles.addComponent(panelDetalles);
        vldetalles.addComponent(hldetalles);
        vldetalles.addComponent(panelTableDetalles);
        vldetalles.addComponent(hlCombos);
        /*FIN ASG*/

        HorizontalLayout hlBtnsSel = new HorizontalLayout();
        hlBtnsSel.setMargin(false);
        hlBtnsSel.setSpacing(true);
        hlBtnsSel.setSizeUndefined();
        Responsive.makeResponsive(hlBtnsSel);
        hlBtnsSel.addComponents(btnAll, btnNone);

        VerticalLayout vlTableButtons = new VerticalLayout(tableCuadre, hlBtnsSel);
        vlTableButtons.setSizeUndefined();
        vlTableButtons.setHeight("90%");

        CssLayout content = new CssLayout();
        content.addStyleName("dashboard-panels");
        Responsive.makeResponsive(content);
        content.addComponents(
                utils.vlContainer(vlTableButtons), utils.vlContainer(tableBombas), utils.vlContainer(tableVentas), utils.vlContainer(tableProductos),
                utils.vlContainer(tableMediosPago), utils.vlContainer(tableEfectivo) /*,utils.vlContainer(tableTarjeta) ACA PODRIA PINTAR TABLA*/ //                hlContent, hlContent2, //hlContent5, 
                //                hlContent3, 
                ,
                 utils.vlContainer(hlContent4)
        );

        root.addComponents(cltInfo, vldetalles/*hlCombos*/, content);
        root.setExpandRatio(content, 1);

    }

    public class SectionPanelButtons extends Panel {

        public SectionPanelButtons(String caption) {
            btnbac = new Button("BAC", FontAwesome.PLUS);
            btnbac.addStyleName(ValoTheme.BUTTON_PRIMARY);
            btnbac.addStyleName(ValoTheme.BUTTON_SMALL);
            btnbac.addClickListener((final Button.ClickEvent event) -> {
//            FormDetalleVenta2.open();
            });

            btnbanknac = new Button("BANCO NACIONAL", FontAwesome.PLUS);
            btnbanknac.addStyleName(ValoTheme.BUTTON_PRIMARY);
            btnbanknac.addStyleName(ValoTheme.BUTTON_SMALL);
            btnbanknac.addClickListener((final Button.ClickEvent event) -> {
//            FormDetalleVenta2.open();
            });

            btnbcr = new Button("BCR", FontAwesome.PLUS);
            btnbcr.addStyleName(ValoTheme.BUTTON_PRIMARY);
            btnbcr.addStyleName(ValoTheme.BUTTON_SMALL);
            btnbcr.addClickListener((final Button.ClickEvent event) -> {
                formDetalleBCR(estacion, symCurrency, pais.getPaisId());
            });

            btncredomatic = new Button("CREDOMATIC", FontAwesome.PLUS);
            btncredomatic.addStyleName(ValoTheme.BUTTON_PRIMARY);
            btncredomatic.addStyleName(ValoTheme.BUTTON_SMALL);
            btncredomatic.addClickListener((final Button.ClickEvent event) -> {
                formDetalleCredomatic(estacion, symCurrency, pais.getPaisId());
            });

            btndavivienda = new Button("DAVIVIENDA", FontAwesome.PLUS);
            btndavivienda.addStyleName(ValoTheme.BUTTON_PRIMARY);
            btndavivienda.addStyleName(ValoTheme.BUTTON_SMALL);
            btndavivienda.addClickListener((final Button.ClickEvent event) -> {
                formDetalleCliDavivienda(estacion, symCurrency, pais.getPaisId());
            });

            btnfmdavivienda = new Button("FM DAVIVIENDA", FontAwesome.PLUS);
            btnfmdavivienda.addStyleName(ValoTheme.BUTTON_PRIMARY);
            btnfmdavivienda.addStyleName(ValoTheme.BUTTON_SMALL);
            btnfmdavivienda.addClickListener(clickEvent -> formDetalleCliDavivienda(estacion, symCurrency, pais.getPaisId()));

            btndmscottia = new Button("FM SCOTTIA", FontAwesome.PLUS);
            btndmscottia.addStyleName(ValoTheme.BUTTON_PRIMARY);
            btndmscottia.addStyleName(ValoTheme.BUTTON_SMALL);
            btndmscottia.addClickListener(clickEvent -> formDetalleCliScottia(estacion, symCurrency, pais.getPaisId()));
            setCaption(caption);
            setContent(new HorizontalLayout() {
                {
                    setSizeFull();
                    setMargin(true);
                    setSpacing(true);
                    setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
                    addComponents(btnbac, btnbanknac, btnbcr, btncredomatic, btndavivienda, btnfmdavivienda, btndmscottia);
                }
            });
        }
    }

    public class SectionPanelTablesDet extends Panel {

        public SectionPanelTablesDet(String caption) {
            setCaption(caption);
            hltables.setSizeFull();
            hltables.setMargin(true);
            hltables.setSpacing(true);
            hltables.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
            setContent(hltables);
        }
    }

    /*Metodo Llama Forma Clientes Prepago*///ASG
    private void formDetalleCliDavivienda(Estacion idestacion, String simboloMoneda, Integer idpais) {
        if (cbxTurno.getValue() != null) {
            formDetalleCliDavivienda = new FormDetalleCliDavivienda(idestacion, simboloMoneda, idpais, bcrDetalleCliDavi, turno);
            formDetalleCliDavivienda.addCloseListener((e) -> {
                updateTableFooterDetaCliFm();
            });
            getUI().addWindow(formDetalleCliDavivienda);
            formDetalleCliDavivienda.focus();
        }
    }

    private void formDetalleCliScottia(Estacion idestacion, String simboloMoneda, Integer idpais) {
        if (cbxTurno.getValue() != null) {
            formDetalleCliScottia = new FormDetalleCliScottia(idestacion, simboloMoneda, idpais, bcrDetalleCliScott, turno);
            formDetalleCliScottia.addCloseListener((e) -> {
                updateTableFooterDetaCliFm();
            });
            getUI().addWindow(formDetalleCliScottia);
            formDetalleCliScottia.focus();
        }
    }

    /*Metodo Llama Forma BCR*///JLOPEZ
    private void formDetalleBCR(Estacion idestacion, String simboloMoneda, Integer idpais) {
        if (cbxTurno.getValue() != null) {
            formDetalleBCR = new FormDetalleBCR(idestacion, simboloMoneda, idpais, bcrDetalleCliBCR, turno);
            formDetalleBCR.addCloseListener((e) -> {
                updateTableFooterDetaCliFm();
            });
            getUI().addWindow(formDetalleBCR);
            formDetalleBCR.focus();
        }
    }

    /*Metodo Llama Forma CREDOMATIC*///JLOPEZ
    private void formDetalleCredomatic(Estacion idestacion, String simboloMoneda, Integer idpais) {
        if (cbxTurno.getValue() != null) {
            formDetalleCredomatic = new FormDetalleCredomatic(idestacion, simboloMoneda, idpais, bcrDetalleCliCredomatic, turno);
            formDetalleCredomatic.addCloseListener((e) -> {
                updateTableFooterDetaCliFm();
            });
            getUI().addWindow(formDetalleCredomatic);
            formDetalleCredomatic.focus();
        }
    }

        /*Metodo Llama Forma DAVIVIENDA*///JLOPEZ
    private void formDetalleDavivienda(Estacion idestacion, String simboloMoneda, Integer idpais) {
        if (cbxTurno.getValue() != null) {
            formDetalleDavivienda = new FormDetalleDavivienda(idestacion, simboloMoneda, idpais, bcrDetalleCliDavivienda, turno);
            formDetalleDavivienda.addCloseListener((e) -> {
                updateTableFooterDetaCliFm();
            });
            getUI().addWindow(formDetalleDavivienda);
            formDetalleDavivienda.focus();
        }
    }
    /*ASG DETALLE CLIENTES*/
    public void updateTableFooterDetaCliFm() {
        totFMDavi = 0D;
        totFMScott = 0D;
        totBCR = 0D;
        totCredomatic = 0D;
        totDavivienda = 0D;
        
        /*Footer para FM Davivienda*/
        for (Integer itemId : bcrDetalleCliDavi.getItemIds()) {
            totFMDavi += bcrDetalleCliDavi.getItem(itemId).getBean().getVenta();
        }
        tableFMDavivienda.setFooterVisible(true);
        tableFMDavivienda.setColumnFooter("comentario", "Total:");
        tableFMDavivienda.setColumnFooter("comentario", symCurrency + numberFmt.format(totFMDavi).trim());

        /*Footer para FM Scottia*/
        for (Integer itemId : bcrDetalleCliScott.getItemIds()) {
            totFMScott += bcrDetalleCliScott.getItem(itemId).getBean().getVenta();
        }
        tableFMScott.setFooterVisible(true);
        tableFMScott.setColumnFooter("comentario", "Total:");
        tableFMScott.setColumnFooter("comentario", symCurrency + numberFmt.format(totFMScott).trim());

        /*Footer para FM BCR*/
        for (Integer itemId : bcrDetalleCliBCR.getItemIds()) {
            totBCR += bcrDetalleCliBCR.getItem(itemId).getBean().getVenta();
        }
        tableBCR.setFooterVisible(true);
        tableBCR.setColumnFooter("comentario", "Total:");
        tableBCR.setColumnFooter("comentario", symCurrency + numberFmt.format(totBCR).trim());

        /*Footer para FM CREDOMATIC*/
        for (Integer itemId : bcrDetalleCliCredomatic.getItemIds()) {
            totCredomatic += bcrDetalleCliCredomatic.getItem(itemId).getBean().getVenta();
        }
        tableCredomatic.setFooterVisible(true);
        tableCredomatic.setColumnFooter("comentario", "Total:");
        tableCredomatic.setColumnFooter("comentario", symCurrency + numberFmt.format(totCredomatic).trim());
    }

    public boolean validaTotalMedioPago(Integer mediopagoid, Double total) {
        boolean result = true;
        /*Valida los totales en medios pago*/
        for (Integer itemId : bcrMediospago.getItemIds()) {
            if (bcrMediospago.getItem(itemId).getBean().getMediopagoId() == mediopagoid) {
                if (Math.abs(total - bcrMediospago.getItem(itemId).getBean().getValue()) > 0.009) {
                    result = false;
                }
            }
        }
        return result;
    }

    /*ASG FIN*/
    public void getAllData() {
        bcrArqueocaja.setBeanIdProperty("arqueocajaId");
        bcrBombas.setBeanIdProperty("id");
        bcrVentas.setBeanIdProperty("id");
        bcrProducto.setBeanIdProperty("productoId");
        bcrMediospago.setBeanIdProperty("mediopagoId");
        bcrEfectivo.setBeanIdProperty("efectivoId");
        bcrTarjeta.setBeanIdProperty("id");

        /*ASG Detalle Cliente Tarjetas*/
        bcrDetalleCliDavi.setBeanIdProperty("iddet");
        bcrDetalleCliScott.setBeanIdProperty("iddet");
        bcrDetalleCliBCR.setBeanIdProperty("iddet");
        bcrDetalleCliCredomatic.setBeanIdProperty("iddet");

        /*FIN ASG*/
        SvcTurnoCierre service = new SvcTurnoCierre();

        pais = (user.getPaisLogin() != null)
                ? user.getPaisLogin() : ((pais != null) ? pais : new Pais());
//        if (user.getPaisLogin() == null && contPais.getItemIds().isEmpty()) {
        contPais = new ListContainer<>(Pais.class, service.getAllPaises());
//        }

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

        bcrArqueocaja.addAll(service.getArqueocajaByTurnoidDia(turno.getTurnoId(), dia.getFecha()));

        service.closeConnections();

        lblTotalVentas.addStyleName(ValoTheme.LABEL_BOLD);
        lblTotalVentas.setSizeUndefined();
        lblTotalDinero.addStyleName(ValoTheme.LABEL_BOLD);
        lblTotalDinero.setSizeUndefined();
        lblDiferencia.addStyleName(ValoTheme.LABEL_BOLD);
        lblDiferencia.addStyleName(ValoTheme.LABEL_H2);
        lblDiferencia.setSizeUndefined();

    }

    private void buildFilters() {
        buildLabelInfo();

        cbxPais.setContainerDataSource(contPais);
        cbxPais.setItemCaptionPropertyId("nombre");
        cbxPais.setItemIconPropertyId("flag");
        cbxPais.setNullSelectionAllowed(false);
//        cbxPais.setVisible(user.isAdministrativo() || user.isGerente());
        cbxPais.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxPais.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                pais = (Pais) cbxPais.getValue();
                SvcEstacion svcEstacion = new SvcEstacion();
                Container estacionContainer = new ListContainer<Estacion>(Estacion.class, svcEstacion.getStationsByCountryUser(pais.getPaisId(), user.getUsuarioId()));
                svcEstacion.closeConnections();
                cbxEstacion.setContainerDataSource(estacionContainer);
                //limpiar
                dfdFecha.setValue(null);
                contTurnos = new ListContainer<Turno>(Turno.class, new ArrayList());
                cbxTurno.setContainerDataSource(contTurnos);
                cbxTurno.setValue(null);
                currencySymbol = pais.getMonedaSimbolo() + " ";
                symCurrency = currencySymbol;
            }
        });

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
                contTurnos = new ListContainer<>(Turno.class, new ArrayList());
                cbxTurno.setContainerDataSource(contTurnos);
                cbxTurno.setValue(null);
                bcrArqueocaja.removeAllItems();

                SvcTurnoCierre service = new SvcTurnoCierre();
                ultimoDia = (ultimoDia.getFecha() == null) ? service.getUltimoDiaByEstacionid(estacion.getEstacionId()) : ultimoDia;
                dia = service.getDiaActivoByEstacionid(estacion.getEstacionId());
//            dia = (dia.getEstadoId() == null) ? ultimoDia : dia;
                ultimoTurno = (ultimoTurno.getTurnoId() == null) ? service.getUltimoTurnoByEstacionid(estacion.getEstacionId()) : ultimoTurno;
                turno = service.getTurnoActivoByEstacionid(estacion.getEstacionId());
//            turno = (turno.getEstadoId() == null) ? ultimoTurno : turno;
                service.closeConnections();
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
                    contTurnos = new ListContainer<Turno>(Turno.class, listTurno);
                    cbxTurno.setContainerDataSource(contTurnos);
                    cbxTurno.setValue(null);
                    bcrArqueocaja.removeAllItems();
                    bcrBombas.removeAllItems();
                    bcrVentas.removeAllItems();
                    bcrProducto.removeAllItems();
                    bcrMediospago.removeAllItems();
                    bcrEfectivo.removeAllItems();
                    bcrTarjeta.removeAllItems();

                    /*ASG DETALLE CLIENTES TC*/
                    bcrDetalleCliDavi.removeAllItems();
                    bcrDetalleCliScott.removeAllItems();
                    bcrDetalleCliBCR.removeAllItems();
                    bcrDetalleCliCredomatic.removeAllItems();
                    /*FIN ASG*/
                    calcularSumas();

                    turno = new Turno();
                    if (!contTurnos.getItemIds().isEmpty()) {
                        int lastIndex = listTurno.size() - 1;
                        turno = (Turno) ((ArrayList) contTurnos.getItemIds()).get(lastIndex);
                        cbxTurno.setValue(turno);
                        actionComboboxTurno();
                    }
                    buildLabelInfo();
                }
            }
        });
        dfdFecha.setValue(dia.getFecha());

        cbxTurno.setContainerDataSource(contTurnos);
        cbxTurno.setItemCaptionPropertyId("nombre");
        cbxTurno.setNullSelectionAllowed(false);
        cbxTurno.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxTurno.addValueChangeListener(
                new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                turno = (Turno) cbxTurno.getValue();
                actionComboboxTurno();
                changeTurno();
            }
        });
        cbxTurno.setValue(turno);
    }

    /*DETALLLE CLIENTES TC ASG*/
    private void changeTurno() {
        if (turno != null) {
            bcrDetalleCliDavi.removeAllItems();
            bcrDetalleCliScott.removeAllItems();
            bcrDetalleCliBCR.removeAllItems();
            bcrDetalleCliCredomatic.removeAllItems();
            ContEstacion.addAll(dao.getAllEstaciones(true, pais.getPaisId()));
            ContMediosPago.addAll(dao.getAllMediosPago(true, pais.getPaisId()));
            //FM Davivienda  115
            //FM Scottia 116
            ContLote.addAll(dao.getAllLotesbyMedioPago(115, turno.getTurnoId()));
            ContLoteScott.addAll(dao.getAllLotesbyMedioPago(116, turno.getTurnoId()));
            ContLoteBCR.addAll(dao.getAllLotesbyMedioPago(118, turno.getTurnoId()));
            ContLoteCredomatic.addAll(dao.getAllLotesbyMedioPago(107, turno.getTurnoId()));
            ContCliGen.addAll(dao.getAllCustomers(true, estacion.getEstacionId()));

            Estacion est = new Estacion();
            est = (Estacion) cbxEstacion.getValue();
            bcrDetalleCliDavi.addAll(dao.getDetalleByMedioPago(est.getEstacionId(), turno.getTurnoId(), 115));
            bcrDetalleCliScott.addAll(dao.getDetalleByMedioPago(est.getEstacionId(), turno.getTurnoId(), 116));
            bcrDetalleCliBCR.addAll(dao.getDetalleByMedioPagoForBCR(est.getEstacionId(), turno.getTurnoId(), 118));
            bcrDetalleCliCredomatic.addAll(dao.getDetalleByMedioPagoForBCR(est.getEstacionId(), turno.getTurnoId(), 107));

            hltables.removeComponent(tableFMDavivienda);
            hltables.removeComponent(tableFMScott);
            hltables.removeComponent(tableBCR);
            hltables.removeComponent(tableCredomatic);

            hltables.addComponent(tableFMDavivienda);
            hltables.addComponent(tableFMScott);
            hltables.addComponent(tableBCR);
            hltables.addComponent(tableCredomatic);

            updateTableFooterDetaCliFm();
        }
    }

    /*FIN ASG*/
    private void actionComboboxTurno() {
        if (turno != null) {
            bcrArqueocaja.removeAllItems();
            SvcTurnoCierre service = new SvcTurnoCierre();
            bcrArqueocaja.addAll(service.getArqueocajaByTurnoidDia(turno.getTurnoId(), dia.getFecha()));
            service.closeConnections();
            for (Integer tid : bcrArqueocaja.getItemIds()) {
                bcrArqueocaja.getItem(tid).getItemProperty("selected").setValue(Boolean.TRUE);
            }
        }
    }

    private void buildButtons() {

        btnAll.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btnAll.addStyleName(ValoTheme.BUTTON_LINK);
        btnAll.setIcon(FontAwesome.CHECK_SQUARE_O);
        btnAll.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                List<Integer> ids = (List<Integer>) tableCuadre.getItemIds();
                for (Integer i : ids) {
                    tableCuadre.getItem(i).getItemProperty("selected").setValue(true);
                }
            }
        });

        btnNone.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btnNone.addStyleName(ValoTheme.BUTTON_LINK);
        btnNone.setIcon(FontAwesome.SQUARE_O);
        btnNone.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                List<Integer> ids = (List<Integer>) tableCuadre.getItemIds();
                for (Integer i : ids) {
                    tableCuadre.getItem(i).getItemProperty("selected").setValue(false);
                }
            }
        });

        btnGuardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//        btnGuardar.setEnabled(!bcrArqueocaja.getItemIds().isEmpty());
        btnGuardar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                /*ASG DESCUADRE EN MEDIOS NO CERRAR*/
                if (!validaTotalMedioPago(115, totFMDavi)) {
                    Notification.show("AVISO:", "Se encontraron diferencias en detalle de clientes FM Davivienda!!", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                if (!validaTotalMedioPago(116, totFMScott)) {
                    Notification.show("AVISO:", "Se encontraron diferencias en detalle de clientes FM Scottia!!", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                /*FIN DESCUADRE ASG*/
                if (bcrArqueocaja.getItemIds().isEmpty()) {
                    Notification.show("AVISO:", "NO existen cuadres qué procesar.", Notification.Type.WARNING_MESSAGE);
                    return;
                }

                for (Integer bid : bcrArqueocaja.getItemIds()) {
                    bcrArqueocaja.getItem(bid).getItemProperty("selected").setValue(Boolean.TRUE);
                }

                boolean exists = false;
                for (Bomba pump : allBombas) {
                    exists = false;
                    for (Integer bid : bcrBombas.getItemIds()) {
                        if (bcrBombas.getItem(bid).getBean().getId().equals(pump.getId())) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        Notification notif = new Notification("ERROR:", "NO es posible cerrar el turno, aún existen bombas sin cuadre.", Notification.Type.ERROR_MESSAGE);
                        notif.show(Page.getCurrent());
                        return;
                    }
                }

                String messageComp = ((totalDinero - totalVentas) < -0.009) ? "<h3>El turno tiene diferencia de: <strong>" + lblDiferencia + "</strong></h3>\n" : "";
                MessageBox
                        .createQuestion()
                        .withCaption("Confirmación")
                        .withHtmlMessage(messageComp.concat("¿Seguro desea confirmar todos sus cuadres y cerrar el turno?"))
                        .withOkButton(new Runnable() {
                            public void run() {

                                SvcTurnoCierre svcTC = new SvcTurnoCierre();
                                turno.setEstadoId(2);   //cerrado
                                turno.setModificadoPor(user.getUsername());
                                turno.setModificadoPersona(user.getNombreLogin());
                                turno = svcTC.doActionTurno(Dao.ACTION_UPDATE, turno);
                                svcTC.closeConnections();
                                if (turno.getTurnoId() != null) {
                                    //*Registro detalle de clientes*// ASG
                                    try {
                                        dao.CreaClienteFMDavivienda(turno.getTurnoId(), 115, bcrDetalleCliDavi); //FM Davivienda
                                        dao.CreaClienteFMScott(turno.getTurnoId(), 116, bcrDetalleCliScott); //FM Scottia
                                        dao.CreaClienteBCR(turno.getTurnoId(), 118, bcrDetalleCliBCR); // BCR
                                        dao.CreaClienteBCR(turno.getTurnoId(), 107, bcrDetalleCliCredomatic); // CREDOMATIC

                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                    }
                                    /*fin registro detalle*/

                                    Notification.show("El turno se ha cerrado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PR_TURN_CLOSE.getViewName());
                                } else {
                                    Notification.show("Ocurrió un cerrar el turno.\n" + turno.getDescError(), Notification.Type.ERROR_MESSAGE);
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

    private void buildTableCuadre() {
        tableCuadre.setCaption("Cuadres:");
        tableCuadre.setContainerDataSource(bcrArqueocaja);
        tableCuadre.addStyleName(ValoTheme.TABLE_SMALL);
        tableCuadre.addGeneratedColumn("colSelected", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("selected");  //Atributo del bean
                final CheckBox cbxSel = new CheckBox("", pro);
                cbxSel.setValue(Boolean.FALSE);
                cbxSel.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {

                        SvcTurnoCierre svcTC = new SvcTurnoCierre();
                        String arqueosIds = "";
                        //Determinar bombas
                        bcrBombas.removeAllItems();
                        Arqueocaja arqueo;
                        for (Integer acId : bcrArqueocaja.getItemIds()) {
                            arqueo = bcrArqueocaja.getItem(acId).getBean();
                            if (arqueo.getSelected()) {
                                arqueosIds += (arqueosIds.isEmpty()) ? arqueo.getArqueocajaId().toString() : ",".concat(arqueo.getArqueocajaId().toString());
                            }
                        }
                        List<Bomba> bombasCuadre = (!arqueosIds.isEmpty()) ? svcTC.getBombasByArquoid(arqueosIds) : new ArrayList();
                        bcrBombas.addAll(bombasCuadre);
                        //Determinar ventas
                        bcrVentas.removeAllItems();
                        String bombasIds = "";
                        for (Integer bId : bcrBombas.getItemIds()) {
                            bombasIds += bombasIds.isEmpty() ? bId.toString() : ",".concat(bId.toString());
                        }
                        if (!bombasIds.isEmpty()) {
                            bcrVentas.addAll(svcTC.getArqueo(turno.getTurnoId().toString(), bombasIds, "E"));
                        }
                        //Determinar productos
                        bcrProducto.removeAllItems();
                        //Determinar medios de pago
                        bcrMediospago.removeAllItems();
                        //Determinar medios de pago (efectivo)
                        bcrEfectivo.removeAllItems();
                        bcrTarjeta.removeAllItems();
                        if (!arqueosIds.isEmpty()) {
                            bcrProducto.addAll(svcTC.getProductoByArqueoid(arqueosIds));
                            bcrMediospago.addAll(svcTC.getMediopagoByArqueoid(arqueosIds));
                            bcrEfectivo.addAll(svcTC.getEfectivoByArqueoid(arqueosIds));
                            bcrTarjeta.addAll(svcTC.getArqueoTC(arqueosIds));
                        }
                        svcTC.closeConnections();
                        calcularSumas();
//                        determinarSumatorias();
                        lblTotalVentas.setValue(numberFmt.format(totalVentas));
                        lblTotalDinero.setValue(numberFmt.format(totalDinero));
                        String style = ((totalDinero - totalVentas) < -0.009) ? "style=\"color:red;\"" : "";
                        lblDiferencia.setValue("<strong " + style + " >" + numberFmt.format(totalDinero - totalVentas) + "</strong>");
                        lblDiferencia.setContentMode(ContentMode.HTML);
                    }
                });
                return cbxSel;
            }
        });
        tableCuadre.setVisibleColumns(new Object[]{"colSelected", "nombre", "diferencia"});
        tableCuadre.setColumnHeaders(new String[]{"", "Cuadre", "Diferencia"});
        tableCuadre.setColumnAlignments(Align.CENTER, Align.LEFT, Align.RIGHT);
        tableCuadre.setSizeUndefined();
        tableCuadre.setHeight(200f, Unit.PIXELS);
    }

    private void calcularSumas() {
        totalVentas = totalDinero = 0D;
        Double tpVolumen = 0D, tpVentas = 0D, tpProducto = 0D, tpMediospago = 0D, tpEfectivo = 0D, tpTarjeta = 0D;
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
        for (Integer id : bcrTarjeta.getItemIds()) {
//            totalDinero += bcrTarjeta.getItem(id).getBean().getValue();
            tpTarjeta += bcrTarjeta.getItem(id).getBean().getValue();
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
    }

    private void buildTableBombas() {
        tableBombas = utils.buildTable("Bombas:", 100f, 100f, bcrBombas,
                new String[]{"nombre"},
                new String[]{"Nombre"});
        tableBombas.setSizeUndefined();
        tableBombas.setHeight(200f, Unit.PIXELS);
        tableBombas.addStyleName(ValoTheme.TABLE_SMALL);
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
        tableVentas.addStyleName(ValoTheme.TABLE_COMPACT);
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
        tableMediosPago.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private void buildTableEfectivo() {
        tableEfectivo.setCaption("Efectivo:");
        tableEfectivo.setContainerDataSource(bcrEfectivo);
        tableEfectivo.setVisibleColumns(new Object[]{"nombre", "cantidad", "value"});
        tableEfectivo.setColumnHeaders(new String[]{"Nombre", "No. boleta", "Valor"});
        tableEfectivo.setColumnAlignments(Table.Align.LEFT, Table.Align.RIGHT, Table.Align.RIGHT);
        tableEfectivo.setHeight(200f, Unit.PIXELS);
        tableEfectivo.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private void buildTableFMDavivienda() {
        tableFMDavivienda.setCaption("Detalle Clientes FM Davivienda:");
        tableFMDavivienda.setContainerDataSource(bcrDetalleCliDavi);
        tableFMDavivienda.setImmediate(true);

        tableFMDavivienda.addGeneratedColumn("colestacion", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("estacion");  //Atributo del bean
                ComboBox cmbEstacion = new ComboBox(null, ContEstacion);
                cmbEstacion.setReadOnly(true);
                cmbEstacion.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbEstacion.setItemCaptionPropertyId("nombre");
                cmbEstacion.setNullSelectionAllowed(false);
                cmbEstacion.addStyleName(ValoTheme.BUTTON_TINY);
                cmbEstacion.setPropertyDataSource(pro);
                cmbEstacion.setFilteringMode(FilteringMode.CONTAINS);
//                cmbEstacion.setWidth("250px");
                return cmbEstacion;
            }
        });

        tableFMDavivienda.addGeneratedColumn("colmedio", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("mediopago");  //Atributo del bean
                ComboBox cmbMedio = new ComboBox(null, ContMediosPago);
                cmbMedio.setReadOnly(true);
                cmbMedio.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbMedio.setItemCaptionPropertyId("nombre");
                cmbMedio.setNullSelectionAllowed(false);
                cmbMedio.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbMedio.setPropertyDataSource(pro);
                cmbMedio.setFilteringMode(FilteringMode.CONTAINS);
//                cmbMedio.setWidth("125px");
                return cmbMedio;
            }
        });

        tableFMDavivienda.addGeneratedColumn("collote", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("genlote");  //Atributo del bean
                ComboBox cmbLote = new ComboBox(null, ContLote);
                cmbLote.setReadOnly(true);
                cmbLote.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbLote.setItemCaptionPropertyId("lote");
                cmbLote.setNullSelectionAllowed(false);
                cmbLote.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbLote.setPropertyDataSource(pro);
                cmbLote.setFilteringMode(FilteringMode.CONTAINS);
                cmbLote.setWidth("85px");
                return cmbLote;
            }
        });

        tableFMDavivienda.setVisibleColumns(new Object[]{"collote", "cliente", "venta", "comentario"});
        tableFMDavivienda.setColumnHeaders(new String[]{"Lote", "Cliente", "Venta", "Comentarios   "});
        tableFMDavivienda.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT);
        tableFMDavivienda.setHeight(200f, Unit.PIXELS);
        tableFMDavivienda.addStyleName(ValoTheme.TABLE_COMPACT);
        tableFMDavivienda.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private void buildTableFMScottia() {
        tableFMScott.setCaption("Detalle Clientes FM Scottia:");
        tableFMScott.setContainerDataSource(bcrDetalleCliScott);
        tableFMScott.setImmediate(true);

        tableFMScott.addGeneratedColumn("colestacion", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("estacion");  //Atributo del bean
                ComboBox cmbEstacion = new ComboBox(null, ContEstacion);
                cmbEstacion.setReadOnly(true);
                cmbEstacion.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbEstacion.setItemCaptionPropertyId("nombre");
                cmbEstacion.setNullSelectionAllowed(false);
                cmbEstacion.addStyleName(ValoTheme.BUTTON_TINY);
                cmbEstacion.setPropertyDataSource(pro);
                cmbEstacion.setFilteringMode(FilteringMode.CONTAINS);
//                cmbEstacion.setWidth("250px");
                return cmbEstacion;
            }
        });

        tableFMScott.addGeneratedColumn("colmedio", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("mediopago");  //Atributo del bean
                ComboBox cmbMedio = new ComboBox(null, ContMediosPago);
                cmbMedio.setReadOnly(true);
                cmbMedio.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbMedio.setItemCaptionPropertyId("nombre");
                cmbMedio.setNullSelectionAllowed(false);
                cmbMedio.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbMedio.setPropertyDataSource(pro);
                cmbMedio.setFilteringMode(FilteringMode.CONTAINS);
//                cmbMedio.setWidth("125px");
                return cmbMedio;
            }
        });

        tableFMScott.addGeneratedColumn("collote", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("genlote");  //Atributo del bean
                ComboBox cmbLote = new ComboBox(null, ContLoteScott);
                cmbLote.setReadOnly(true);
                cmbLote.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbLote.setItemCaptionPropertyId("lote");
                cmbLote.setNullSelectionAllowed(false);
                cmbLote.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbLote.setPropertyDataSource(pro);
                cmbLote.setFilteringMode(FilteringMode.CONTAINS);
                cmbLote.setWidth("85px");
                return cmbLote;
            }
        });

        tableFMScott.setVisibleColumns(new Object[]{"collote", "cliente", "venta", "comentario"});
        tableFMScott.setColumnHeaders(new String[]{"Lote", "Cliente", "Venta", "Comentarios   "});
        tableFMScott.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT);
        tableFMScott.setHeight(200f, Unit.PIXELS);
        tableFMScott.addStyleName(ValoTheme.TABLE_COMPACT);
        tableFMScott.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private void buildTableBCR() {
        tableBCR.setCaption("Detalle Clientes BCR:");
        tableBCR.setContainerDataSource(bcrDetalleCliBCR);
        tableBCR.setImmediate(true);
        tableBCR.addGeneratedColumn("colestacion", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("estacion");  //Atributo del bean
                ComboBox cmbEstacion = new ComboBox(null, ContEstacion);
                cmbEstacion.setReadOnly(true);
                cmbEstacion.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbEstacion.setItemCaptionPropertyId("nombre");
                cmbEstacion.setNullSelectionAllowed(false);
                cmbEstacion.addStyleName(ValoTheme.BUTTON_TINY);
                cmbEstacion.setPropertyDataSource(pro);
                cmbEstacion.setFilteringMode(FilteringMode.CONTAINS);
//                cmbEstacion.setWidth("250px");
                return cmbEstacion;
            }
        });

        tableBCR.addGeneratedColumn("colmedio", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("mediopago");  //Atributo del bean
                ComboBox cmbMedio = new ComboBox(null, ContMediosPago);
                cmbMedio.setReadOnly(true);
                cmbMedio.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbMedio.setItemCaptionPropertyId("nombre");
                cmbMedio.setNullSelectionAllowed(false);
                cmbMedio.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbMedio.setPropertyDataSource(pro);
                cmbMedio.setFilteringMode(FilteringMode.CONTAINS);
//                cmbMedio.setWidth("125px");
                return cmbMedio;
            }
        });

        tableBCR.addGeneratedColumn("collote", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("genlote");  //Atributo del bean
                ComboBox cmbLote = new ComboBox(null, ContLoteBCR);
                cmbLote.setReadOnly(true);
                cmbLote.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbLote.setItemCaptionPropertyId("lote");
                cmbLote.setNullSelectionAllowed(false);
                cmbLote.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbLote.setPropertyDataSource(pro);
                cmbLote.setFilteringMode(FilteringMode.CONTAINS);
                cmbLote.setWidth("85px");
                return cmbLote;
            }
        });

        tableBCR.addGeneratedColumn("colcliente", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cliente");  //Atributo del bean
                ComboBox cmbCliente = new ComboBox(null, ContCliGen);
                cmbCliente.setReadOnly(true);
                cmbCliente.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbCliente.setItemCaptionPropertyId("nombre");
                cmbCliente.setNullSelectionAllowed(false);
                cmbCliente.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbCliente.setPropertyDataSource(pro);
                cmbCliente.setFilteringMode(FilteringMode.CONTAINS);
                cmbCliente.setWidth("85px");
                return cmbCliente;
            }
        });

        tableBCR.setVisibleColumns(new Object[]{"collote", "colcliente", "venta", "comentario"});
        tableBCR.setColumnHeaders(new String[]{"Lote", "Cliente", "Venta", "Comentarios   "});
        tableBCR.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT);
        tableBCR.setHeight(200f, Unit.PIXELS);
        tableBCR.addStyleName(ValoTheme.TABLE_COMPACT);
        tableBCR.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private void buildTableCredomatic() {
        tableCredomatic.setCaption("Detalle Clientes Credomatic:");
        tableCredomatic.setContainerDataSource(bcrDetalleCliCredomatic);
        tableCredomatic.setImmediate(true);
        tableCredomatic.addGeneratedColumn("colestacion", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("estacion");  //Atributo del bean
                ComboBox cmbEstacion = new ComboBox(null, ContEstacion);
                cmbEstacion.setReadOnly(true);
                cmbEstacion.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbEstacion.setItemCaptionPropertyId("nombre");
                cmbEstacion.setNullSelectionAllowed(false);
                cmbEstacion.addStyleName(ValoTheme.BUTTON_TINY);
                cmbEstacion.setPropertyDataSource(pro);
                cmbEstacion.setFilteringMode(FilteringMode.CONTAINS);
//                cmbEstacion.setWidth("250px");
                return cmbEstacion;
            }
        });

        tableCredomatic.addGeneratedColumn("colmedio", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("mediopago");  //Atributo del bean
                ComboBox cmbMedio = new ComboBox(null, ContMediosPago);
                cmbMedio.setReadOnly(true);
                cmbMedio.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbMedio.setItemCaptionPropertyId("nombre");
                cmbMedio.setNullSelectionAllowed(false);
                cmbMedio.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbMedio.setPropertyDataSource(pro);
                cmbMedio.setFilteringMode(FilteringMode.CONTAINS);
//                cmbMedio.setWidth("125px");
                return cmbMedio;
            }
        });

        tableCredomatic.addGeneratedColumn("collote", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("genlote");  //Atributo del bean
                ComboBox cmbLote = new ComboBox(null, ContLoteCredomatic);
                cmbLote.setReadOnly(true);
                cmbLote.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbLote.setItemCaptionPropertyId("lote");
                cmbLote.setNullSelectionAllowed(false);
                cmbLote.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbLote.setPropertyDataSource(pro);
                cmbLote.setFilteringMode(FilteringMode.CONTAINS);
                cmbLote.setWidth("85px");
                return cmbLote;
            }
        });

        tableCredomatic.addGeneratedColumn("colcliente", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cliente");  //Atributo del bean
                ComboBox cmbCliente = new ComboBox(null, ContCliGen);
                cmbCliente.setReadOnly(true);
                cmbCliente.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbCliente.setItemCaptionPropertyId("nombre");
                cmbCliente.setNullSelectionAllowed(false);
                cmbCliente.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbCliente.setPropertyDataSource(pro);
                cmbCliente.setFilteringMode(FilteringMode.CONTAINS);
                cmbCliente.setWidth("85px");
                return cmbCliente;
            }
        });

        tableCredomatic.setVisibleColumns(new Object[]{"collote", "colcliente", "venta", "comentario"});
        tableCredomatic.setColumnHeaders(new String[]{"Lote", "Cliente", "Venta", "Comentarios   "});
        tableCredomatic.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT);
        tableCredomatic.setHeight(200f, Unit.PIXELS);
        tableCredomatic.addStyleName(ValoTheme.TABLE_COMPACT);
        tableCredomatic.addStyleName(ValoTheme.TABLE_SMALL);
    }

    private HorizontalLayout buildDetalleMontos() {
        Label label3 = new Label("Diferencia: ");
        label3.addStyleName(ValoTheme.LABEL_BOLD);
        label3.addStyleName(ValoTheme.LABEL_H2);
        label3.setSizeUndefined();

        VerticalLayout vlLeft = new VerticalLayout(//new Label("Efectivo: "), new Label("Ventas: "), 
                label3);
        Label label6 = new Label(symCurrency + "\t\t\t");
        label6.addStyleName(ValoTheme.LABEL_BOLD);
        label6.addStyleName(ValoTheme.LABEL_H2);
        label6.setSizeUndefined();
        VerticalLayout vlMiddle = new VerticalLayout(//new Label(symCurrency + "\t"), new Label(symCurrency + "\t\t"), 
                label6);
        VerticalLayout vlRight = new VerticalLayout(//lblTotalDinero, lblTotalVentas, 
                lblDiferencia);
        vlRight.setSizeUndefined();
        Responsive.makeResponsive(vlRight);
//        vlRight.setComponentAlignment(lblTotalDinero, Alignment.TOP_RIGHT);
//        vlRight.setComponentAlignment(lblTotalVentas, Alignment.TOP_RIGHT);
        vlRight.setComponentAlignment(lblDiferencia, Alignment.TOP_RIGHT);
        HorizontalLayout result = new HorizontalLayout(vlLeft, vlMiddle, vlRight);
        result.setWidth("100%");
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
        lblUltimoDía.setWidth("30%");

        fechaString = (ultimoTurno.getTurnoId() != null) ? "Turno ".concat(ultimoTurno.getTurnoId().toString()) : "INEXISTENTE";
        estadoName = (ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 1)
                ? "ABIERTO" : ((ultimoTurno.getEstadoId() != null && ultimoTurno.getEstadoId() == 2) ? "CERRADO" : "SIN ESTADO");
        lblUltimoTurno.setValue("Último turno: " + fechaString + " (" + estadoName + ")");
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_BOLD);
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_H3);
        lblUltimoTurno.addStyleName(ValoTheme.LABEL_COLORED);
        lblUltimoTurno.setSizeUndefined();
        lblUltimoTurno.setWidth("40%");
//        lblUltimoTurno.setWidth("350px");
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new Dao();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        if (acceso.isCambiar() || acceso.isAgregar()) {
            btnGuardar.setEnabled(true);
        } else {
            btnGuardar.setEnabled(false);
        }
    }
}
