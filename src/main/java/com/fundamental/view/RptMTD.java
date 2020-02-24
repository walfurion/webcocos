//package com.fundamental.view;
//
//import com.fundamental.model.Acceso;
//import com.sisintegrados.generic.bean.Estacion;
//import com.fundamental.model.Mediopago;
//import com.sisintegrados.generic.bean.Pais;
//import com.fundamental.model.Producto;
//import com.sisintegrados.generic.bean.Usuario;
//import com.fundamental.model.Utils;
//import com.sisintegrados.dao.Dao;
//import com.fundamental.services.SvcChangeLastRead;
//import com.fundamental.services.SvcMedioPago;
//import com.fundamental.services.SvcReporte;
//import com.fundamental.utils.XlsxReportGenerator;
//import com.vaadin.data.Property;
//import com.vaadin.demo.dashboard.component.LoadingWindow;
//import com.vaadin.demo.dashboard.event.DashboardEvent;
//import com.vaadin.demo.dashboard.event.DashboardEventBus;
//import com.vaadin.navigator.View;
//import com.vaadin.navigator.ViewChangeListener;
//import com.vaadin.server.FileDownloader;
//import com.vaadin.server.FontAwesome;
//import com.vaadin.server.Responsive;
//import com.vaadin.server.StreamResource;
//import com.vaadin.server.VaadinSession;
//import com.vaadin.shared.ui.combobox.FilteringMode;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.ComboBox;
//import com.vaadin.ui.CssLayout;
//import com.vaadin.ui.DateField;
//import com.vaadin.ui.Notification;
//import com.vaadin.ui.Panel;
//import com.vaadin.ui.UI;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.Window;
//import com.vaadin.ui.themes.ValoTheme;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map.Entry;
//import java.util.SortedMap;
//import java.util.TreeMap;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
//import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.vaadin.maddon.ListContainer;
//
///**
// * @author Henry Barrientos
// */
//public class RptMTD extends Panel implements View {
//
//    DecimalFormat decFmt2 = new DecimalFormat("### ###,##0.00;-#");
//    DateField dfdFechaInicial, dfdFechaFinal;
//    ComboBox cbxPais, cbxEstacion;
//    Button btnGenerar;
//
//    List<Producto> allCombustibles;
//    List<String[]> midataPriceA, midataPriceF, midataVol, midataMon, midataVent, midataVenExe, midataServi, midataVlubs, midataMisc, midataLlan, midataTurno, midataIsla, midataMP, midataMPefe, midataInvFin, midataInvComp, midataCalib;
//    List<String[]> midataFaltSob, midataFaltSobVol, totalOtrosProductos, detalleOtrosProductos;
//    List<Pais> paises = new ArrayList();
//
////    template
//    VerticalLayout vlRoot;
//    Utils utils = new Utils();
//    Usuario user;
//    Acceso acceso = new Acceso();
//
//    public RptMTD() {
//        addStyleName(ValoTheme.PANEL_BORDERLESS);
//        setSizeFull();
//        DashboardEventBus.register(this);
//        vlRoot = new VerticalLayout();
//        vlRoot.setSizeFull();
//        vlRoot.setMargin(true);
//        vlRoot.addStyleName("dashboard-view");
//        setContent(vlRoot);
//        Responsive.makeResponsive(vlRoot);
//        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
////        currency = user.getPaisLogin().getMonedaSimbolo().concat(". ");
//        vlRoot.addComponent(utils.buildHeader("Reporte MTD", true, true));
//        vlRoot.addComponent(utils.buildSeparator());
//        getAllData();
//
//        CssLayout content = new CssLayout();
//        Responsive.makeResponsive(content);
//        content.addStyleName("dashboard-panels");
//        content.setSizeUndefined();
//
//        CssLayout contentDos = new CssLayout();
//        Responsive.makeResponsive(contentDos);
//        contentDos.addStyleName("dashboard-panels");
//        contentDos.setSizeUndefined();
//
//        vlRoot.addComponents(content, contentDos);
//        vlRoot.setExpandRatio(content, .25f);
//        vlRoot.setExpandRatio(contentDos, .25f);
//
//        buildButtons();
//
//        content.addComponents(utils.vlContainer(dfdFechaInicial), utils.vlContainer(dfdFechaFinal), utils.vlContainer(cbxPais), utils.vlContainer(cbxEstacion));
//        contentDos.addComponents(btnGenerar);
//
//        //Para exportar
//        StreamResource sr = getExcelStreamResource();
//        FileDownloader fileDownloader = new FileDownloader(sr);
//        fileDownloader.extend(btnGenerar);
//
//    }
//    Pais pais;
//
//    private void getAllData() {
//
//        SvcMedioPago svcMP = new SvcMedioPago();
////        pais = (Pais) ((cbxPais != null && cbxPais.getValue() != null)
////                ? cbxPais.getValue() 
////                : ((user.getPaisLogin() != null) ? user.getPaisLogin() : new Pais()));
//
//        pais = new Pais();
//        if ((cbxPais != null && cbxPais.getValue() != null)) {
//            pais = (Pais) cbxPais.getValue();
//        } else if (user.getPaisLogin() != null) {   //elegido en login
//            pais = user.getPaisLogin();
//        } else if (user.getPaisId() != null && user.getPaisId() > 0) { //en mantenimiento
//            for (Pais p : svcMP.getAllPaises()) {
//                if (p.getPaisId().equals(user.getPaisId())) {
//                    pais = p;
//                    break;
//                }
//            }
//        }
//
//        if (pais.getPaisId() != null && pais.getPaisId() > 0) {
//            paises.add(pais);
//        } else {
//            paises = svcMP.getAllPaises();
//        }
//        svcMP.closeConnections();
//    }
//
//    private void buildButtons() {
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);
//        dfdFechaInicial = new DateField("Fecha inicial:", cal.getTime());
//        dfdFechaInicial.setLocale(new Locale("es", "ES"));
//        dfdFechaInicial.setRequired(true);
//
//        dfdFechaFinal = new DateField("Fecha final:", new Date());
//        dfdFechaFinal.setLocale(new Locale("es", "ES"));
//        dfdFechaFinal.setRequired(true);
//
//        cbxPais = new ComboBox("País:", new ListContainer<Pais>(Pais.class, paises));
//        cbxPais.setItemCaptionPropertyId("nombre");
//        cbxPais.setSizeUndefined();
//        cbxPais.setItemIconPropertyId("flag");
//        cbxPais.setNullSelectionAllowed(false);
//        cbxPais.setRequired(true);
//        cbxPais.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                List<Estacion> estaciones = new ArrayList();
//                if (user.getEstacionLogin() != null) {
//                    estaciones.add(user.getEstacionLogin());
//                } else {
//                    SvcChangeLastRead svcCLR = new SvcChangeLastRead();
//                    estaciones = svcCLR.getStationsByCountry(((Pais) cbxPais.getValue()).getPaisId(), true);
//                    svcCLR.closeConnections();
//                }
//                cbxEstacion.setContainerDataSource(new ListContainer<Estacion>(Estacion.class, estaciones));
//                cbxEstacion.setValue((user.getEstacionLogin() != null) ? user.getEstacionLogin() : null);
//            }
//        });
//
//        cbxEstacion = new ComboBox("Estación:");
//        cbxEstacion.setItemCaptionPropertyId("nombre");
//        cbxEstacion.setSizeUndefined();
//        cbxEstacion.setNullSelectionAllowed(false);
//        cbxEstacion.setRequired(true);
//        cbxEstacion.setFilteringMode(FilteringMode.CONTAINS);
//
//        btnGenerar = new Button("Generar MTD", FontAwesome.FILE_EXCEL_O);
//        btnGenerar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//        btnGenerar.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                System.out.println("*** buttonClick");
////DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
////Window w = new LoadingWindow(); //user, preferencesTabActive);
////UI.getCurrent().addWindow(w);
////w.focus();
////try { Thread.sleep(3000); } catch (Exception exc) {}
////UI.getCurrent().removeWindow(w);
//
//                Usuario loggedUser = ((Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName()));
//                if ((cbxPais == null || cbxPais.getValue() == null)
//                        || (!loggedUser.isJefePais() && (cbxEstacion == null || cbxEstacion.getValue() == null))) {
//                    Notification.show("ERROR:", "Todos los campos son requeridos.", Notification.Type.ERROR_MESSAGE);
//                    return;
//                }
//
//            }
//        });
//
////        btnGenerarFilas = new Button("Generar en filas", FontAwesome.FILE_EXCEL_O);
////        btnGenerarFilas.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//    }
//
//    private StreamResource getExcelStreamResource() {
//        StreamResource.StreamSource source = new StreamResource.StreamSource() {
//            public InputStream getStream() {
//                ByteArrayOutputStream stream;
//                InputStream input = null;
//                try {
//                    System.out.println("*** getExcelStreamResource 1 - " + new Date());
//                    List<CellRangeAddress> regions = new ArrayList();   //Agrupar titulos
//                    List<String> regionsTitle = new ArrayList();   //titulos
//                    int colRegion = 2;
//                    List<String> colFormula = new ArrayList();
//
//                    Usuario loggedUser = ((Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName()));
////                    if ((cbxPais == null || cbxPais.getValue() == null)
////                        || (!loggedUser.isJefePais() && (cbxEstacion == null || cbxEstacion.getValue() == null))
////                    ) {
////                        Notification.show("ERROR:", "Todos los campos son requeridos.", Notification.Type.ERROR_MESSAGE);
////                        return null;
////                    }
//
//                    String currencyS = ((Pais) cbxPais.getValue()).getMonedaSimbolo();
//                    String volumeS = ((Pais) cbxPais.getValue()).getVolSimbolo();
//
//                    Integer estacionId = (cbxEstacion != null && cbxEstacion.getValue() != null)
//                            ? ((Estacion) cbxEstacion.getValue()).getEstacionId() : null;
//
//                    String estacionesIds = "";
//                    if (estacionId == null && loggedUser.isJefePais()) {
//                        for (Object est : cbxEstacion.getContainerDataSource().getItemIds()) {
//                            estacionesIds += (estacionesIds.isEmpty()) ? ((Estacion) est).getEstacionId() : ",".concat(((Estacion) est).getEstacionId().toString());
//                        }
//                    } else {
//                        estacionesIds = estacionId.toString();
//                    }
//
//                    System.out.println("coco's Obtener datos MTD START: " + new Date());
//
//                    SvcReporte service = new SvcReporte();
//                    allCombustibles = service.getMTDProductos(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds);
//                    String productosIds = "";
//                    for (Producto p : allCombustibles) {
//                        productosIds += productosIds.isEmpty() ? p.getProductoId() : ",".concat(p.getProductoId().toString());
//                    }
//                    int cantTurnos = service.getCantidadTurnos(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds);
//                    String islasIds = service.getMTDIslasIds(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds);
//                    int cantIslas = (islasIds.contains(",")) ? islasIds.split(",").length : 0;
//                    List<Mediopago> mediospago = service.getMTDMediospagoByFechaEstacion_distintos(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds);
//                    List<Mediopago> efectivos = service.getMTDMediospagoEfectivoByFechaEstacion_distintos(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds);
//                    List<Producto> otrosProductos = service.getOtrosProductosByEstacionid(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds);
//
//                    midataPriceF = service.getMTDPrecios(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds, productosIds, 2);
//                    midataPriceA = service.getMTDPrecios(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds, productosIds, 1);
//                    midataVol = service.getMTDVolumenes(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds, productosIds);
//                    midataMon = service.getMTDMonetario(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds, productosIds);
//
////                    midataVent = service.getMTDVentasMiscelaneos(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionId);
////                    midataVenExe = service.getMTDVentaByProducto(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionId, 21); //Venta exenta: 21
////                    midataServi = service.getMTDVentaByProducto(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionId, 20); //Venta servicentro: 20
////                    midataVlubs = service.getMTDVentaByProducto(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionId, 19); //Venta lubricantes: 19
////                    midataMisc = service.getMTDVentaByProducto(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionId, 17); //Miscelaneos: 17
////                    midataLlan = service.getMTDVentaByProducto(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionId, 18); //Llantas: 18
//                    totalOtrosProductos = service.getMTDVentaOtrosProductos(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds, true);
//                    detalleOtrosProductos = service.getMTDVentaOtrosProductos(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds, false);
//
//                    midataTurno = service.getMTDVentaTurnoByFechaEstacion(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds);
//                    midataIsla = service.getMTDVentaIslaByFechaEstacion(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds);
//                    midataMP = service.getMTDMediospagoByFechaEstacion(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds);
//                    midataMPefe = service.getMTDEfectivoByFechaEstacion(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds);
//
//                    midataInvFin = service.getMTDInventarioByFechaEstacion(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds, "FINAL");
//                    midataInvComp = service.getMTDInventarioByFechaEstacion(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds, "COMPRAS");
//                    midataCalib = service.getMTDCalibraciones(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds, productosIds);
//                    midataFaltSob = service.getMTDFaltanteSobrante(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds, productosIds);
//                    midataFaltSobVol = service.getMTDFaltanteSobranteVol(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionesIds, productosIds);
//
//                    System.out.println("coco's Obtener datos MTD END: " + new Date());
//                    service.closeConnections();
//
//                    List<String> lTitles = new ArrayList();
//                    List<Integer> lTypes = new ArrayList();
//                    lTitles.addAll(Arrays.asList(new String[]{"ESTACION", "FECHA"}));
//                    lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_STRING, CELL_TYPE_STRING}));
//                    colFormula.addAll(Arrays.asList(new String[]{"", ""})); //espacio vacio significa que NO va formula
//
//                    //Precios, Volumen, Monetario
//                    boolean hasPriceA = false, hasPriceF = false;
//                    for (String[] pauto : midataPriceA) {
//                        if (Double.parseDouble(pauto[3]) > 0) {
//                            hasPriceA = true;
//                            break;
//                        }
//                    }
//                    for (String[] pauto : midataPriceF) {
//                        if (Double.parseDouble(pauto[3]) > 0) {
//                            hasPriceF = true;
//                            break;
//                        }
//                    }
//
//                    boolean yaAgregoNombreE = false;
//                    List<String> datamap = new ArrayList();
//                    String uFecha = "", estacion = "";
//                    SortedMap<String, List<String>> mymap = new TreeMap<String, List<String>>();
//                    //A continuacion la conversion de filas a columnas.
//
//                    //Precios FULL
//                    if (hasPriceF) {
//                        for (Producto mp : allCombustibles) {
//                            lTitles.add(mp.getNombre());
//                            lTypes.add(CELL_TYPE_NUMERIC);
//                            colFormula.add(""); //espacio vacio significa que NO va formula
//                        }
//                        regionsTitle.add("Precios FULL");
//                        regions.add(new CellRangeAddress(0, 0, colRegion, colRegion + allCombustibles.size() - 1));
//                        colRegion = colRegion + allCombustibles.size();
//                        for (String[] content : midataPriceF) {
//                            if (!uFecha.isEmpty() && !uFecha.equals(content[0])) {
//                                datamap.add(0, uFecha);
//                                datamap.add(1, estacion);
//                                yaAgregoNombreE = true;
//                                mymap.put(estacion.concat(uFecha), datamap);
//                                uFecha = content[0];
//                                estacion = content[1];
//                                datamap = new ArrayList();
//                            } else if (uFecha.isEmpty()) {
//                                uFecha = content[0];
//                                estacion = content[1];
//                            }
//                            datamap.add(content[3]);
//                        }
//                        if (midataPriceF.size() > 0) {    //Agregar el ultimo
//                            datamap.add(0, uFecha);
//                            datamap.add(1, estacion);
//                            yaAgregoNombreE = true;
//                            mymap.put(estacion.concat(uFecha), datamap);
//                        }
//                    }
//
//                    //PRECIOS AUTO
//                    if (hasPriceA) {
//                        for (Producto mp : allCombustibles) {
//                            lTitles.add(mp.getNombre());
//                            lTypes.add(CELL_TYPE_NUMERIC);
//                            colFormula.add(""); //espacio vacio significa que NO va formula
//                        }
//                        regionsTitle.add("Precios AUTO");
//                        regions.add(new CellRangeAddress(0, 0, colRegion, colRegion + allCombustibles.size() - 1));
//                        colRegion = colRegion + allCombustibles.size();
//                        uFecha = "";
//                        datamap = new ArrayList();
//                        for (String[] content : midataPriceA) {
//                            if (!uFecha.isEmpty() && !uFecha.equals(content[0])) {
//                                if (!yaAgregoNombreE) {
//                                    datamap.add(0, uFecha);
//                                    datamap.add(1, estacion);
//                                    mymap.put(estacion.concat(uFecha), datamap);
//                                } else {
//                                    mymap.get(estacion.concat(uFecha)).addAll(datamap);
//                                }
//                                uFecha = content[0];
//                                estacion = content[1];
//                                datamap = new ArrayList();
//                            } else if (uFecha.isEmpty()) {
//                                uFecha = content[0];
//                                estacion = content[1];
//                            }
//                            datamap.add(content[3]);
//                        }
//                        if (midataPriceA.size() > 0) {    //Agregar el ultimo
//                            if (!yaAgregoNombreE) {
//                                datamap.add(0, uFecha);
//                                datamap.add(1, estacion);
//                                mymap.put(estacion.concat(uFecha), datamap);
//                            } else {
//                                mymap.get(estacion.concat(uFecha)).addAll(datamap);
//                            }
//                        }
//                    }
//
//                    //VOLUMENES
//                    uFecha = "";
//                    Double total = 0D, diff = 0D, lastDiff = 0D;
//                    int itemNum = 0;
//                    datamap = new ArrayList();
//                    regionsTitle.addAll(Arrays.asList(new String[]{"Ventas volumen (" + volumeS + ")"}));
//                    for (Producto mp : allCombustibles) {
//                        lTitles.add(mp.getNombre());
//                        lTypes.add(CELL_TYPE_NUMERIC);
//                        colFormula.add("OK"); //espacio vacio significa que NO va formula
//                    }
//                    lTitles.addAll(Arrays.asList(new String[]{"TOTAL", "DIF %"}));
//                    lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, 3}));
//                    colFormula.addAll(Arrays.asList(new String[]{"OK", ""}));
//                    regions.add(new CellRangeAddress(0, 0, colRegion, colRegion + allCombustibles.size() - 1 + 2));
//                    colRegion = colRegion + allCombustibles.size() + 2;
//                    for (String[] content : midataVol) {
//                        datamap = Arrays.asList(content);
//                        mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                    }
//
//                    //MONETARIO
//                    uFecha = "";
//                    total = diff = lastDiff = 0D;
//                    itemNum = 0;
//                    datamap = new ArrayList();
//                    regionsTitle.addAll(Arrays.asList(new String[]{"Ventas monetario (" + currencyS + ")"}));
//                    for (Producto mp : allCombustibles) {
//                        lTitles.add(mp.getNombre());
//                        lTypes.add(CELL_TYPE_NUMERIC);
//                        colFormula.add("OK"); //espacio vacio significa que NO va formula
//                    }
//                    lTitles.addAll(Arrays.asList(new String[]{"TOTAL", "DIF %"}));
//                    lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, 3}));
//                    colFormula.addAll(Arrays.asList(new String[]{"OK", ""}));
//                    regions.add(new CellRangeAddress(0, 0, colRegion, colRegion + allCombustibles.size() - 1 + 2));
//                    colRegion = colRegion + allCombustibles.size() + 2;
//                    for (String[] content : midataMon) {
//                        if (!uFecha.isEmpty() && !uFecha.equals(content[0])) {
//                            datamap.add(total.toString());
//                            diff = (itemNum > 0 && total > 0) ? +(total - lastDiff) / lastDiff : 0D;
//                            datamap.add(decFmt2.format(diff));   //diferencia
//                            mymap.get(uFecha).addAll(datamap);
//                            uFecha = content[0];
//                            datamap = new ArrayList();
//                            lastDiff = total;
//                            total = 0D;
//                            itemNum++;
//                        } else if (uFecha.isEmpty()) {
//                            uFecha = content[0];
//                        }
//                        datamap.add(content[3]);
//                        total += Double.parseDouble(content[3]);
//                    }
//                    if (midataMon.size() > 0) {    //Agregar el ultimo
//                        datamap.add(total.toString());
//                        diff = (itemNum > 0 && total > 0) ? +(total - lastDiff) / lastDiff : 0D;
//                        datamap.add(diff.toString());   //diferencia
//                        mymap.get(uFecha).addAll(datamap);
//                    }
//
//                    //VENTAS TIENDA; VENTAS FERRETERIA; TOTAL TIENDA Y FERRETERIA
//                    if (!otrosProductos.isEmpty()) {
//                        regionsTitle.addAll(Arrays.asList(new String[]{"TOTAL VENTAS OTROS PRODS (" + currencyS + ")"}));
//                        lTitles.addAll(Arrays.asList(new String[]{"TOTAL", "Dif %"}));
//                        lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, 3}));
//                        colFormula.addAll(Arrays.asList(new String[]{"OK", ""}));
//                        int colsOtrosProds = otrosProductos.size();
//                        for (int i = 0; i < colsOtrosProds; i++) {
//                            lTitles.addAll(Arrays.asList(new String[]{otrosProductos.get(i).getNombre(), "Dif %"}));
//                            lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, 3}));
//                            colFormula.addAll(Arrays.asList(new String[]{"OK", ""}));
//                        }
//                        colsOtrosProds += 1;
//                        regions.add(new CellRangeAddress(0, 0, colRegion, colRegion + ((2 * colsOtrosProds) - 1)));
//                        colRegion = colRegion + colsOtrosProds * 2;
//                        diff = 0D;
//                        itemNum = 0;
//                        for (String[] content : totalOtrosProductos) {
//                            datamap = Arrays.asList(content);
//                            mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                        }
//                        for (String[] content : detalleOtrosProductos) {
//                            datamap = Arrays.asList(content);
//                            mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                        }
//                    }
//
//                    //Ventas turno
//                    regionsTitle.addAll(Arrays.asList(new String[]{"Ventas por turno (" + currencyS + ")"}));
//                    for (int i = 1; i <= cantTurnos; i++) {
//                        lTitles.add("Turno " + i);
//                        lTypes.add(CELL_TYPE_NUMERIC);
//                        colFormula.add("OK");
//                    }
//                    if (cantTurnos > 1) {
//                        regions.add(new CellRangeAddress(0, 0, colRegion, (colRegion + cantTurnos - 1)));
//                    }
//                    colRegion += cantTurnos;
//                    for (String[] content : midataTurno) {
//                        datamap = Arrays.asList(content);
//                        mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                    }
//
//                    //Ventas por Bomba
//                    regionsTitle.addAll(Arrays.asList(new String[]{"Ventas por bomba (" + volumeS + ")"}));
//                    for (int i = 1; i <= cantIslas; i++) {
//                        lTitles.add("Isla #" + i);
//                        lTypes.add(CELL_TYPE_NUMERIC);
//                        colFormula.add("OK");
//                    }
//                    regions.add(new CellRangeAddress(0, 0, colRegion, (colRegion + cantIslas - 1)));
//                    colRegion += cantIslas;
//                    for (String[] content : midataIsla) {
//                        datamap = Arrays.asList(content);
//                        mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                    }
//
//                    //Ingresos segun forma de pago
//                    regionsTitle.addAll(Arrays.asList(new String[]{"Ingreso segun forma de pago (" + currencyS + ")"}));
//                    lTitles.add("Totales");
//                    lTypes.add(CELL_TYPE_NUMERIC);
//                    colFormula.add("OK");
//                    for (int i = 0; i < mediospago.size(); i++) {
//                        lTitles.addAll(Arrays.asList(new String[]{mediospago.get(i).getNombre(), "Dif %"}));
//                        lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, 3}));
//                        colFormula.addAll(Arrays.asList(new String[]{"OK", ""}));
//                    }
////                    regions.add(new CellRangeAddress(0, 0, colRegion, (colRegion + (mediospago.size() * 2))));
////                    colRegion += 1 + (mediospago.size() * 2);
//                    for (String[] content : midataMP) {
//                        datamap = Arrays.asList(content);
//                        mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                    }
//                    //medio pago efectivo
//                    for (int i = 0; i < efectivos.size(); i++) {
//                        lTitles.addAll(Arrays.asList(new String[]{efectivos.get(i).getNombre(), "Dif %"}));
//                        lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, 3}));
//                        colFormula.addAll(Arrays.asList(new String[]{"OK", ""}));
//                    }
//                    regions.add(new CellRangeAddress(0, 0, colRegion, (colRegion + ((mediospago.size() + efectivos.size()) * 2))));
//                    colRegion += 1 + ((mediospago.size() + efectivos.size()) * 2);
//                    for (String[] content : midataMPefe) {
//                        datamap = Arrays.asList(content);
//                        mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                    }
//
//                    //Inventario final
//                    regionsTitle.addAll(Arrays.asList(new String[]{"Inventario final (" + volumeS + ")"}));
//                    for (Producto p : allCombustibles) {
//                        lTitles.add(p.getNombre());
//                        lTypes.add(CELL_TYPE_NUMERIC);
//                        colFormula.add("OK");
//                    }
//                    lTitles.addAll(Arrays.asList(new String[]{"Total", "Dif %"}));
//                    lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, 3}));
//                    colFormula.addAll(Arrays.asList(new String[]{"OK", ""}));
//                    regions.add(new CellRangeAddress(0, 0, colRegion, colRegion + (allCombustibles.size() + 2 - 1)));
//                    colRegion += allCombustibles.size() + 2;
//                    for (String[] content : midataInvFin) {
//                        datamap = Arrays.asList(content);
//                        mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                    }
//
//                    //Inventario compras
//                    regionsTitle.addAll(Arrays.asList(new String[]{"Compras de combustible (" + volumeS + ")"}));
//                    for (Producto p : allCombustibles) {
//                        lTitles.add(p.getNombre().toUpperCase());
//                        lTypes.add(CELL_TYPE_NUMERIC);
//                        colFormula.add("OK");
//                    }
//                    lTitles.addAll(Arrays.asList(new String[]{"Total", "Dif %"}));
//                    lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, 3}));
//                    colFormula.addAll(Arrays.asList(new String[]{"OK", ""}));
//                    regions.add(new CellRangeAddress(0, 0, colRegion, colRegion + (allCombustibles.size() + 2 - 1)));
//                    colRegion += allCombustibles.size() + 2;
//                    for (String[] content : midataInvComp) {
//                        datamap = Arrays.asList(content);
//                        mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                    }
//
//                    //CALIBRACIONES
//                    regionsTitle.addAll(Arrays.asList(new String[]{"Calibraciones (" + volumeS + ")"}));
//                    for (Producto p : allCombustibles) {
//                        lTitles.add(p.getNombre().toUpperCase());
//                        lTypes.add(CELL_TYPE_NUMERIC);
//                        colFormula.add("OK");
//                    }
//                    lTitles.addAll(Arrays.asList(new String[]{"Total", "Dif %"}));
//                    lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, 3}));
//                    colFormula.addAll(Arrays.asList(new String[]{"OK", ""}));
//                    regions.add(new CellRangeAddress(0, 0, colRegion, colRegion + (allCombustibles.size() + 2 - 1)));
//                    colRegion += allCombustibles.size() + 2;
//                    for (String[] content : midataCalib) {
//                        datamap = Arrays.asList(content);
//                        mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                    }
//
//                    //Faltante/Sobrante monetario
//                    regionsTitle.addAll(Arrays.asList(new String[]{"Faltante/Sobrante (" + currencyS + ")"}));
//                    lTitles.addAll(Arrays.asList(new String[]{"Sobrante", "Faltante", "Total", "Dif %"}));
//                    lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, CELL_TYPE_NUMERIC, CELL_TYPE_NUMERIC, 3}));
//                    colFormula.addAll(Arrays.asList(new String[]{"OK", "OK", "OK", ""}));
//                    regions.add(new CellRangeAddress(0, 0, colRegion, colRegion + 4 - 1));
//                    colRegion += 4;
//                    for (String[] content : midataFaltSob) {
//                        datamap = Arrays.asList(content);
//                        mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                    }
//
//                    //CxC empleados
//                    regionsTitle.addAll(Arrays.asList(new String[]{"CxC Empleados"}));
//                    lTitles.addAll(Arrays.asList(new String[]{"Total", "Dif %"}));
//                    lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, 3}));
//                    colFormula.addAll(Arrays.asList(new String[]{"OK", ""}));
//                    regions.add(new CellRangeAddress(0, 0, colRegion, colRegion + 2 - 1));
//                    colRegion += 2;
////                    for (Entry<String, List<String>> item : mymap.entrySet()) {
////                        mymap.get(item.getKey()).addAll(Arrays.asList(new String[]{null, null}));
////                    }
//                    for (String[] content : midataFaltSob) {
//                        datamap = Arrays.asList(content);
//                        mymap.get(content[0]).addAll(Arrays.asList(new String[]{datamap.get(1), datamap.get(4)}));
//                    }
//
//                    //Faltante/Sobrante volumen
//                    regionsTitle.addAll(Arrays.asList(new String[]{"Faltante/Sobrante (" + volumeS + ")"}));
//                    for (Producto p : allCombustibles) {
//                        lTitles.addAll(Arrays.asList(new String[]{p.getNombre().toUpperCase(), "Dif %"}));
//                        lTypes.addAll(Arrays.asList(new Integer[]{CELL_TYPE_NUMERIC, 3}));
//                        colFormula.addAll(Arrays.asList(new String[]{"OK", ""}));
//                    }
//                    regions.add(new CellRangeAddress(0, 0, colRegion, colRegion + (allCombustibles.size() * 2) - 1));
//                    colRegion += allCombustibles.size() * 2;
//                    for (String[] content : midataFaltSobVol) {
//                        datamap = Arrays.asList(content);
//                        mymap.get(content[0]).addAll(datamap.subList(1, datamap.size()));
//                    }
//
////conversion
//                    List<String[]> data = new ArrayList();
//                    for (Entry<String, List<String>> item : mymap.entrySet()) {
//                        data.add(Arrays.copyOf(item.getValue().toArray(), item.getValue().toArray().length, String[].class));
//                    }
//
//                    System.out.println("*** getExcelStreamResource 2 - " + new Date());
//                    XlsxReportGenerator xrg = new XlsxReportGenerator();
//                    xrg.setListRegions(regions);
//                    xrg.setListTitleRegions(regionsTitle);
//                    xrg.setListFormulas(colFormula);
//                    xrg.setReadonlyData(true);
//                    XSSFWorkbook workbook = xrg.generate(null, "MTD new", new HashMap(), lTitles.toArray(new String[lTitles.size()]), data, lTypes.toArray(new Integer[lTypes.size()]));
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    try {
//                        workbook.write(bos);
//                    } catch (Exception exc) {
//                        bos.close();
//                    }
//
//                    stream = bos;
//                    input = new ByteArrayInputStream(stream.toByteArray());
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//                System.out.println("*** getExcelStreamResource 3 - " + new Date());
////loadingw.close();
//                return input;
//            }
//        };
//        String fileName = "COCOs_MTDnew_".concat(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())).concat(".xlsx");
//        StreamResource resource = new StreamResource(source, fileName);
//        return resource;
//    }
//
//    @Override
//    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        Dao dao = new Dao();
//        acceso = dao.getAccess(event.getViewName());
//        dao.closeConnections();
//        btnGenerar.setEnabled(acceso.isAgregar());
//    }
//}
