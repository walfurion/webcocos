package com.fundamental.view;

import com.fundamental.model.Mediopago;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.services.SvcMedioPago;
import com.fundamental.utils.XlsxReportGenerator;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
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
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
public class RptMediopago extends Panel implements View {

//    String currency;
    DateField dfdFechaInicial, dfdFechaFinal;
    ComboBox cbxPais;
    Button btnGenerar;
    Button btnGenerarFilas;

    List<Mediopago> allMediospago;
//    List<Mediopago> allMediosEfectivo;
    List<String[]> midata;//, midata1;
    List<String[]> midataFilas, midata1Filas;
    List<Pais> paises = new ArrayList();

//    template
    VerticalLayout vlRoot;
    Utils utils = new Utils();
    Usuario user;

    public RptMediopago() {
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
        vlRoot.addComponent(utils.buildHeader("Reporte medios de pago", true, true));
        vlRoot.addComponent(utils.buildSeparator());
        getAllData();

        CssLayout content = new CssLayout();
        Responsive.makeResponsive(content);
        content.addStyleName("dashboard-panels");
        content.setSizeUndefined();
        
        CssLayout contentDos = new CssLayout();
        Responsive.makeResponsive(contentDos);
        contentDos.addStyleName("dashboard-panels");
        contentDos.setSizeUndefined();
        
        vlRoot.addComponents(content, contentDos);
        vlRoot.setExpandRatio(content, .25f);
        vlRoot.setExpandRatio(contentDos, .25f);
//        template

        buildButtons();

//        VerticalLayout vlContMargSup = new VerticalLayout();
//        vlContMargSup.addComponent(btnGenerar);
//        vlContMargSup.setSizeUndefined();
//        Responsive.makeResponsive(vlContMargSup);


//CssLayout contentUno = new CssLayout();
//Responsive.makeResponsive(contentUno);
//contentUno.addComponents(utils.vlContainer(dfdFechaInicial), utils.vlContainer(dfdFechaFinal), utils.vlContainer(cbxPais), btnGenerar);

//Panel pnlUno = new Panel("Generar en columnas");
//pnlUno.setWidth(100f, Unit.PERCENTAGE);
//pnlUno.setHeightUndefined();
//pnlUno.setContent(contentUno);

//        vlRoot.addComponents();

        content.addComponents(utils.vlContainer(dfdFechaInicial), utils.vlContainer(dfdFechaFinal), utils.vlContainer(cbxPais));
        contentDos.addComponents(btnGenerar, btnGenerarFilas);

        //Para exportar
        StreamResource sr = getExcelStreamResource();
        FileDownloader fileDownloader = new FileDownloader(sr);
        fileDownloader.extend(btnGenerar);
        
        //Para exportar
        StreamResource sre = getExcelStreamResourceFilas();
        FileDownloader fdr = new FileDownloader(sre);
        fdr.extend(btnGenerarFilas);
    }
    Pais pais;

    private void getAllData() {
        SvcMedioPago svcMP = new SvcMedioPago();
//        pais = (Pais) ((cbxPais != null && cbxPais.getValue() != null)
//                ? cbxPais.getValue() : ((user.getPaisLogin() != null) ? user.getPaisLogin() : new Pais()));

//        allMediospago = svcMP.getMediospagoByPaisidTipoid(pais.getPaisId(), 1);
//        allMediospago.addAll(svcMP.getMediospagoByPaisidTipoid(pais.getPaisId(), 2));   //Se agregan los de efectivo
//        allMediosEfectivo = svcMP.getMediospagoByPaisidTipoid(pais.getPaisId(), 2);

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
        if (pais.getPaisId()!=null && pais.getPaisId() > 0 ) {
            paises.add(pais);
        } else {
            paises = svcMP.getAllPaises();
        }
        svcMP.closeConnections();
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
//        cbxPais.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//            }
//        });

        btnGenerar = new Button("Generar en columnas", FontAwesome.FILE_EXCEL_O);
        btnGenerar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        
        btnGenerarFilas = new Button("Generar en filas", FontAwesome.FILE_EXCEL_O);
        btnGenerarFilas.addStyleName(ValoTheme.BUTTON_FRIENDLY);
    }

    private StreamResource getExcelStreamResource() {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {
            public InputStream getStream() {
                ByteArrayOutputStream stream;
                InputStream input = null;
                try {
                    
                    if (cbxPais==null || cbxPais.getValue()==null) {
                        Notification.show("ERROR:", "Todos los campos son requeridos.", Notification.Type.ERROR_MESSAGE);
                        return null;
                    }
                    
                SvcMedioPago svcMP = new SvcMedioPago();
                pais = (Pais) cbxPais.getValue();
                allMediospago = svcMP.getMediospagoByPaisidTipoid(pais.getPaisId(), 1);
                allMediospago.addAll(svcMP.getMediospagoByPaisidTipoid(pais.getPaisId(), 2));
//                allMediosEfectivo = svcMP.getMediospagoByPaisidTipoid(pais.getPaisId(), 2);
                    Integer paisId = (cbxPais != null && cbxPais.getValue() != null) ? ((Pais) cbxPais.getValue()).getPaisId() : null;
                    midata = svcMP.getMediospagoReporte(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), paisId);
                svcMP.closeConnections();
                    
                    Map<Integer, Integer> mapPosTitle = new HashMap();
                    int index, colIniciales = 5;
                    List<String> lTitles = new ArrayList();
                    List<Integer> lTypes = new ArrayList();
                    lTitles.add("FECHA");
                    lTitles.add("CODIGO");
                    lTitles.add("BU");
                    lTitles.add("DEPOSITO");
                    lTitles.add("ESTACION");
                    lTypes.add(CELL_TYPE_STRING);
                    lTypes.add(CELL_TYPE_STRING);
                    lTypes.add(CELL_TYPE_STRING);
                    lTypes.add(CELL_TYPE_STRING);
                    lTypes.add(CELL_TYPE_STRING);
                    index = colIniciales;
                    for (Mediopago mp : allMediospago) {
                        lTitles.add(mp.getNombre());
                        lTypes.add(CELL_TYPE_NUMERIC);
                        mapPosTitle.put(mp.getMediopagoId(), index++);
                    }
//                    for (Mediopago mp : allMediosEfectivo) {
//                        lTitles.add(mp.getNombre());
//                        lTypes.add(CELL_TYPE_NUMERIC);
//                        mapPosTitle.put(mp.getMediopagoId(), index++);
//                    }
                    lTitles.add("TOTAL");
                    lTypes.add(CELL_TYPE_NUMERIC);

                    //el 3 es por FECHA, CODIGO, ESTACION; el 1 es por la columna de TOTAL
//                    String[] info = new String[colIniciales + allMediospago.size() + allMediosEfectivo.size() + 1];
                    String[] info = new String[colIniciales + allMediospago.size() + 1];

//                    SvcMedioPago svcMP = new SvcMedioPago();
//                    Integer paisId = (cbxPais != null && cbxPais.getValue() != null) ? ((Pais) cbxPais.getValue()).getPaisId() : null;
//                    midata = svcMP.getMediospagoReporte(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), paisId);
////                    midata1 = svcMP.getEfectivoReporte(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), paisId);
//                    svcMP.closeConnections();

                    String fecha_a = "";
                    List<String[]> data = new ArrayList();
                    Double total = 0D;
                    //A continuacion la conversion de filas a columnas.
                    for (String[] content : midata) {
                        if (!fecha_a.isEmpty() && !fecha_a.equals(content[0])) {
                            info[0] = fecha_a;
                            info[1] = content[1];
                            info[2] = content[2];
                            info[3] = content[3];
                            info[4] = content[4];
//                            info[colIniciales + allMediospago.size() + allMediosEfectivo.size()] = total.toString();   //valor para ultima columna
                            info[colIniciales + allMediospago.size()] = total.toString();   //valor para ultima columna
                            data.add(info);
//                            info = new String[colIniciales + allMediospago.size() + allMediosEfectivo.size() + 1];
                            info = new String[colIniciales + allMediospago.size() + 1];
                            fecha_a = content[0];
                            total = 0D;
                        }
                        if (fecha_a.isEmpty()) {
                            fecha_a = content[0];
                        }
                        info[mapPosTitle.get(Integer.parseInt(content[5]))] = content[7];
                        total += (content[7] != null && !content[7].isEmpty()) ? Double.parseDouble(content[7]) : 0D; //valor final sumatoria
                    }
                    //Agregar el ultimo
                    if (midata.size() > 0) {
                        info[0] = fecha_a;
                        info[1] = midata.get(midata.size() - 1)[1];
                        info[2] = midata.get(midata.size() - 1)[2];
                        info[3] = midata.get(midata.size() - 1)[3];
                        info[4] = midata.get(midata.size() - 1)[4];
//                        info[colIniciales + allMediospago.size() + allMediosEfectivo.size()] = total.toString();   //valor para ultima columna
                        info[colIniciales + allMediospago.size()] = total.toString();   //valor para ultima columna
                        data.add(info);
                    }

//                    fecha_a = "";
////                    info = new String[colIniciales + allMediospago.size() + allMediosEfectivo.size() + 1];
//                    info = new String[colIniciales + allMediospago.size() + 1];
//                    for (String[] content : midata1) {
//                        if (!fecha_a.isEmpty() && !fecha_a.equals(content[0])) {
//                            info[0] = fecha_a;
//                            info[1] = content[1];
//                            info[2] = content[2];
//                            info[3] = content[3];
//                            info[4] = content[4];
////                            info[colIniciales + allMediospago.size() + allMediosEfectivo.size()] = total.toString();   //valor para ultima columna
//                            info[colIniciales + allMediospago.size()] = total.toString();   //valor para ultima columna
//                            data.add(info);
////                            info = new String[colIniciales + allMediospago.size() + allMediosEfectivo.size() + 1];
//                            info = new String[colIniciales + allMediospago.size() + 1];
//                            fecha_a = content[0];
//                            total = 0D;
//                        }
//                        if (fecha_a.isEmpty()) {
//                            fecha_a = content[0];
//                        }
//                        info[mapPosTitle.get(Integer.parseInt(content[5]))] = content[7];
//                        total += (content[7] != null && !content[7].isEmpty()) ? Double.parseDouble(content[7]) : 0D; //valor final sumatoria
//                    }
//                    if (midata1.size() > 0) {
//                        info[0] = fecha_a;
//                        info[1] = midata1.get(midata1.size() - 1)[1];
//                        info[2] = midata1.get(midata1.size() - 1)[2];
//                        info[3] = midata1.get(midata1.size() - 1)[3];
//                        info[4] = midata1.get(midata1.size() - 1)[4];
////                        info[colIniciales + allMediospago.size() + allMediosEfectivo.size()] = total.toString();   //valor para ultima columna
//                        info[colIniciales + allMediospago.size()] = total.toString();   //valor para ultima columna
//                        data.add(info);
//                    }
                    
                    XlsxReportGenerator xrg = new XlsxReportGenerator();
                    XSSFWorkbook workbook = xrg.generate(null, "Medios de pago", new HashMap(), lTitles.toArray(new String[lTitles.size()]), data, lTypes.toArray(new Integer[lTypes.size()]));
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
        String fileName = "COCOs_MediosPago_".concat(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())).concat(".xlsx");
        StreamResource resource = new StreamResource(source, fileName);
        return resource;
    }

    
    
    

    private StreamResource getExcelStreamResourceFilas() {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {
            public InputStream getStream() {
                ByteArrayOutputStream stream;
                InputStream input = null;
                try {
                    List<String> lTitles = new ArrayList(Arrays.asList("FECHA", "CÓDIGO", "BU", "DEPÓSITO", "ESTACIÓN", "MEDIOPAGO", "MONTO"));
                    List<Integer> lTypes = new ArrayList(Arrays.asList(CELL_TYPE_STRING, 4, 4, 4, CELL_TYPE_STRING, CELL_TYPE_STRING, CELL_TYPE_NUMERIC));

                    SvcMedioPago svcMP = new SvcMedioPago();
                    Integer paisId = (cbxPais != null && cbxPais.getValue() != null) ? ((Pais) cbxPais.getValue()).getPaisId() : null;
                    midata = svcMP.getMediospagoReporte(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), paisId);
//                    midata1 = svcMP.getEfectivoReporte(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), paisId);
List<String[]> dataVol = svcMP.getVolumenesReporte(dfdFechaInicial.getValue(), dfdFechaFinal.getValue(), paisId);
                    svcMP.closeConnections();

//Quitamos la columna mediopago_id
List<String[]> data = new ArrayList();
for (String[] dato : midata) {
    data.add(new String[]{ 
        dato[0], dato[1], dato[2], dato[3], dato[4], dato[6], dato[7]
    });
}
//for (String[] dato : midata1) {
//    data.add(new String[]{ 
//        dato[0], dato[1], dato[2], dato[3], dato[4], dato[6], dato[7]
//    });
//}







                    XlsxReportGenerator xrg = new XlsxReportGenerator();
                    XSSFWorkbook workbook = xrg.generate(null, "Medios de pago", new HashMap(), lTitles.toArray(new String[lTitles.size()]), data, lTypes.toArray(new Integer[lTypes.size()]));



//-- Para generar dos reportes en un mismo archivo de Excel
lTitles = new ArrayList(Arrays.asList("CÓDIGO", "BU", "DEPÓSITO", "ESTACIÓN", "DÍA", "CÓDIGO NUM", "CÓDIGO ALF", "PRODUCTO", "VOLUMEN", "MONTO"));
lTypes = new ArrayList(Arrays.asList(4, 4, 4, CELL_TYPE_STRING, CELL_TYPE_STRING, 4, CELL_TYPE_STRING, CELL_TYPE_STRING, CELL_TYPE_NUMERIC, CELL_TYPE_NUMERIC));
workbook = xrg.generate(workbook, "Volúmenes", new HashMap(), lTitles.toArray(new String[lTitles.size()]), dataVol, lTypes.toArray(new Integer[lTypes.size()]));
//-- Para generar dos reportes en un mismo archivo de Excel





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
        String fileName = "COCOs_MediosPago_".concat(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())).concat(".xlsx");
        StreamResource resource = new StreamResource(source, fileName);
        return resource;
    }
    
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
