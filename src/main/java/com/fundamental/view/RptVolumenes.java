package com.fundamental.view;

import com.fundamental.model.Acceso;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.sisintegrados.dao.Dao;
import com.fundamental.services.SvcMedioPago;
import com.fundamental.services.SvcReporte;
import com.fundamental.utils.XlsxReportGenerator;
import com.sisintegrados.daoimp.DaoImp;
import com.vaadin.data.Property;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
public class RptVolumenes extends Panel implements View {

//    String currency;
    DateField dfdFechaInicial, dfdFechaFinal, dfdFechaInicialMov, dfdFechaFinalMov;
    ComboBox cbxPais;
    ComboBox cbxPaisMov;//, cbxEstacionMov;
    Button btnGenerar, btnGenerarMov;

    List<Pais> paises = new ArrayList();

    VerticalLayout vlRoot;
    Utils utils = new Utils();
    Usuario user;
    Acceso acceso = new Acceso();

    public RptVolumenes() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        vlRoot = new VerticalLayout();
        vlRoot.setSizeFull();
        vlRoot.setMargin(true);
        vlRoot.addStyleName("dashboard-view");
        setContent(vlRoot);
        Responsive.makeResponsive(vlRoot);
        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
//        currency = user.getPaisLogin().getMonedaSimbolo().concat(". ");
        vlRoot.addComponent(utils.buildHeader("Reporte volúmenes", true, true));
        vlRoot.addComponent(utils.buildSeparator());
        getAllData();

        CssLayout content = new CssLayout();
        Responsive.makeResponsive(content);
        content.addStyleName("dashboard-panels");
        vlRoot.addComponents(content);
        vlRoot.setExpandRatio(content, 1);
//        template

        buildButtons();
        buildButtonsMov();

        HorizontalLayout hlTop = utils.buildHorizontal("hlTop", true, true, true, true);
        hlTop.addComponents(utils.vlContainer(dfdFechaInicial), utils.vlContainer(dfdFechaFinal), utils.vlContainer(cbxPais), btnGenerar);
        hlTop.setComponentAlignment(btnGenerar, Alignment.MIDDLE_CENTER);
        
        HorizontalLayout hlBottom = utils.buildHorizontal("hlTop", true, true, true, true);
        hlBottom.addComponents(utils.vlContainer(dfdFechaInicialMov), utils.vlContainer(dfdFechaFinalMov), utils.vlContainer(cbxPaisMov), btnGenerarMov);
        hlBottom.setComponentAlignment(btnGenerarMov, Alignment.MIDDLE_CENTER);
        
        VerticalLayout vlContainer = new VerticalLayout(hlTop, hlBottom);

//        content.addComponents(utils.vlContainer(dfdFechaInicial), utils.vlContainer(dfdFechaFinal), utils.vlContainer(cbxPais), btnGenerar);
//        content.addComponents(utils.vlContainer(dfdFechaInicialMov), utils.vlContainer(dfdFechaFinalMov), utils.vlContainer(cbxPaisMov), utils.vlContainer(cbxEstacionMov), btnGenerarMov);
        content.addComponents(vlContainer);

        //Para exportar
        StreamResource sr = getExcelStreamResource();
        FileDownloader fileDownloader = new FileDownloader(sr);
        fileDownloader.extend(btnGenerar);
        
        //Para exportar
        StreamResource srMov = getExcelStreamResourceMov();
        FileDownloader fileDownloaderMov = new FileDownloader(srMov);
        fileDownloaderMov.extend(btnGenerarMov);
    }
    Pais pais;

    private void getAllData() {
        SvcMedioPago svcMP = new SvcMedioPago();
//        pais = (Pais) ((cbxPais != null && cbxPais.getValue() != null)
//                ? cbxPais.getValue() : ((user.getPaisLogin() != null) ? user.getPaisLogin() : new Pais()));
        pais = new Pais();
        if ((cbxPais != null && cbxPais.getValue() != null)) {
            pais = (Pais) cbxPais.getValue();
        } else if (user.getPaisLogin() != null) {   //elegido en login
            pais = user.getPaisLogin();
        } else if (user.getPaisId()!=null && user.getPaisId()>0) { //en mantenimiento
            for (Pais p : svcMP.getAllPaises()) {
                if (p.getPaisId().equals(user.getPaisId())) {
                    pais = p; break;
                }
            }
        }
        if (pais.getPaisId() != null && pais.getPaisId() > 0) {
            paises.add(pais);
        } else {
            paises = svcMP.getAllPaises();
        }
//        svcMP.closeConnections();
    }

    private void buildButtons() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        dfdFechaInicial = new DateField("Fecha inicial:", cal.getTime());
        dfdFechaInicial.setLocale(new Locale("es", "ES"));
        dfdFechaInicial.setRequired(true);

        dfdFechaFinal = new DateField("Fecha final:", new Date());
        dfdFechaFinal.setLocale(new Locale("es", "ES"));
        dfdFechaFinal.setRequired(true);

        cbxPais = new ComboBox("País:", new ListContainer<Pais>(Pais.class, paises));
        cbxPais.setItemCaptionPropertyId("nombre");
        cbxPais.setSizeUndefined();
        cbxPais.setItemIconPropertyId("flag");
        cbxPais.setNullSelectionAllowed(false);
        cbxPais.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
//                SvcMedioPago svcMP = new SvcMedioPago();
                pais = (Pais) cbxPais.getValue();
//                svcMP.closeConnections();
            }
        });

        btnGenerar = new Button("Generar volúmenes", FontAwesome.FILE_EXCEL_O);
        btnGenerar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    }

    private void buildButtonsMov() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        dfdFechaInicialMov = new DateField("Fecha inicial:", cal.getTime());
        dfdFechaInicialMov.setLocale(new Locale("es", "ES"));
        dfdFechaInicialMov.setRequired(true);

        dfdFechaFinalMov = new DateField("Fecha final:", new Date());
        dfdFechaFinalMov.setLocale(new Locale("es", "ES"));
        dfdFechaFinalMov.setRequired(true);

        cbxPaisMov = new ComboBox("País:", new ListContainer<Pais>(Pais.class, paises));
        cbxPaisMov.setItemCaptionPropertyId("nombre");
        cbxPaisMov.setSizeUndefined();
        cbxPaisMov.setItemIconPropertyId("flag");
        cbxPaisMov.setNullSelectionAllowed(false);
        cbxPaisMov.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
//                List<Estacion> estaciones = new ArrayList();
//                if (user.getEstacionLogin() != null) {
//                    estaciones.add(user.getEstacionLogin());
//                } else {
//                    SvcChangeLastRead svcCLR = new SvcChangeLastRead();
//                    estaciones = svcCLR.getStationsByCountry(((Pais) cbxPaisMov.getValue()).getPaisId(), true);
//                    svcCLR.closeConnections();
//                }
//                cbxEstacionMov.setContainerDataSource(new ListContainer<Estacion>(Estacion.class, estaciones));
//                cbxEstacionMov.setValue((user.getEstacionLogin() != null) ? user.getEstacionLogin() : null);
            }
        });

//        cbxEstacionMov = new ComboBox("Estación:");
//        cbxEstacionMov.setItemCaptionPropertyId("nombre");
//        cbxEstacionMov.setSizeUndefined();
//        cbxEstacionMov.setNullSelectionAllowed(false);
//        cbxEstacionMov.setRequired(true);
//        cbxEstacionMov.setFilteringMode(FilteringMode.CONTAINS);

        btnGenerarMov = new Button("Generar movimientos", FontAwesome.FILE_EXCEL_O);
        btnGenerarMov.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnGenerarMov.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
//                if (!cbxEstacionMov.isValid()) {
//                    Notification.show("ERROR:", "Por favor elija una estación.", Notification.Type.ERROR_MESSAGE);
////                    return null;
//                }
            }
        });
    }

    private StreamResource getExcelStreamResource() {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {
            public InputStream getStream() {
                ByteArrayOutputStream stream;
                InputStream input = null;
                try {
                    List<String> lTitles = new ArrayList(Arrays.asList("CÓDIGO", "BU", "DEPÓSITO", "ESTACIÓN", "DÍA", "CÓDIGO NUM", "CÓDIGO ALF", "PRODUCTO", "VOLUMEN", "MONTO"));
                    List<Integer> lTypes = new ArrayList(Arrays.asList(4, 4, 4, CELL_TYPE_STRING, CELL_TYPE_STRING, 4, CELL_TYPE_STRING, CELL_TYPE_STRING, CELL_TYPE_NUMERIC, CELL_TYPE_NUMERIC));

                    SvcMedioPago svcMP = new SvcMedioPago();
                    Integer paisId = (cbxPais != null && cbxPais.getValue() != null) ? ((Pais) cbxPais.getValue()).getPaisId() : null;
                    List<String[]> data = svcMP.getVolumenesReporte(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), paisId);
//                    svcMP.closeConnections();

                    XlsxReportGenerator xrg = new XlsxReportGenerator();
                    XSSFWorkbook workbook = xrg.generate(null, "Volumenes", new HashMap(), lTitles.toArray(new String[lTitles.size()]), data, lTypes.toArray(new Integer[lTypes.size()]));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    try {
                        workbook.write(bos);
                    } catch (Exception exc) {
                        bos.close();
                    }

                    stream = bos;
                    input = new ByteArrayInputStream(stream.toByteArray());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return input;
            }
        };
        String fileName = "COCOs_Volumenes_".concat(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())).concat(".xlsx");
        StreamResource resource = new StreamResource(source, fileName);
        return resource;
    }

    private StreamResource getExcelStreamResourceMov() {

        StreamResource.StreamSource source = new StreamResource.StreamSource() {
            public InputStream getStream() {
//                if (!cbxEstacionMov.isValid()) {
////                    Notification.show("ERROR:", "Por favor elija una estación.", Notification.Type.ERROR_MESSAGE);
//                    return null;
//                }
                ByteArrayOutputStream stream;
                InputStream input = null;
                try {
                    List<String> lTitles = new ArrayList(Arrays.asList("DEPÓSITO", "AGENCIA", "PRODUCTO", "FECHA", "INV INICIAL", "COMPRAS", "VENTAS", "INV FÍSICO", "INV CONTABLE", "MERMAS"));
                    List<Integer> lTypes = new ArrayList(Arrays.asList(CELL_TYPE_NUMERIC, CELL_TYPE_STRING, CELL_TYPE_STRING, CELL_TYPE_STRING, CELL_TYPE_NUMERIC, CELL_TYPE_NUMERIC, CELL_TYPE_NUMERIC, CELL_TYPE_NUMERIC, CELL_TYPE_NUMERIC, CELL_TYPE_NUMERIC));

//                    Integer estacionId = (cbxEstacionMov != null && cbxEstacionMov.getValue() != null)
//                            ? ((Estacion) cbxEstacionMov.getValue()).getEstacionId() : null;
                    Integer paisId = (cbxPaisMov != null && cbxPaisMov.getValue() != null) 
                            ? ((Pais) cbxPaisMov.getValue()).getPaisId() : null;
//                    String productosIds = "";
                    
                    SvcReporte service = new SvcReporte();
//                    List<Producto> allCombustibles = service.getMTDProductos(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), estacionId);
//                    for (Producto p : allCombustibles) {
//                        productosIds += productosIds.isEmpty() ? p.getProductoId() : ",".concat(p.getProductoId().toString());
//                    }
                    List<String[]> data = service.getDataMovimiento(dfdFechaInicialMov.getValue(), dfdFechaFinalMov.getValue(), paisId);//, estacionId);//, productosIds);
//                    service.closeConnections();

                    XlsxReportGenerator xrg = new XlsxReportGenerator();
                    XSSFWorkbook workbook = xrg.generate(null, "Movimiento producto", new HashMap(), lTitles.toArray(new String[lTitles.size()]), data, lTypes.toArray(new Integer[lTypes.size()]));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    try {
                        workbook.write(bos);
                    } catch (Exception exc) {
                        bos.close();
                    }

                    stream = bos;
                    input = new ByteArrayInputStream(stream.toByteArray());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return input;
            }
        };
        String fileName = "COCOs_MovimientoProducto_".concat(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())).concat(".xlsx");
        StreamResource resource = new StreamResource(source, fileName);
        return resource;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new DaoImp();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnGenerar.setEnabled(acceso.isAgregar());    
        btnGenerarMov.setEnabled(acceso.isAgregar());
    }
}
