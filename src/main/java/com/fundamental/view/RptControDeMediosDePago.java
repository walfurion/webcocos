package com.fundamental.view;

import com.fundamental.model.Dia;
import com.fundamental.model.Turno;
import com.fundamental.model.Utils;
import com.fundamental.services.SvcMedioPago;
import com.fundamental.services.SvcMtd;
import com.fundamental.services.SvcTurno;
import com.fundamental.services.SvcUsuario;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import com.fundamental.services.SvcReporteControlMediosPago;
import com.fundamental.utils.AdvancedFileDownloader;
import com.fundamental.utils.AdvancedFileDownloader.AdvancedDownloaderListener;
import com.fundamental.utils.AdvancedFileDownloader.DownloaderEvent;
import com.fundamental.utils.ExcelGeneratorCrlMediosPago;
import com.fundamental.utils.XlsxReportGenerator;
import com.fundamental.utils.Zip;
import com.sisintegrados.generic.bean.GenericEstacion;
import com.sisintegrados.generic.bean.GenericRprControlMediosPago;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Grid;
import com.vaadin.ui.OptionGroup;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.sisintegrados.generic.bean.Estacion;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.ui.UI;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Mery
 */
public class RptControDeMediosDePago extends Panel implements View {

    CreateComponents components = new CreateComponents();
    Label lblUltimoDia = new Label();
    Label lblUltimoTurno = new Label();
    ComboBox cmbPais = new ComboBox();
    DateField cmbFechaInicio = new DateField("Fecha Inicio:");
    DateField cmbFechaFin = new DateField("Fecha Fin:");
    Usuario usuario = new Usuario();
    Dia ultimoDia;
    Turno ultimoTurno;
    Utils utils = new Utils();
    SvcTurno dao = new SvcTurno();
    SvcReporteControlMediosPago SvcReporteControlMediosPago = new SvcReporteControlMediosPago();
    BeanItemContainer<Pais> contPais = new BeanItemContainer<Pais>(Pais.class);
    Button btnGenerar = new Button("Generar Reporte");
    Button btnExportar2 = new Button("Exportar a Excel", FontAwesome.EDIT);
    Button btnSelectAll;
    Button btnUnselectAll;

    //traer estaciones con su checkbox
    BeanContainer<Integer, GenericEstacion> checkestacionesm = new BeanContainer<>(GenericEstacion.class);
    BeanItemContainer<GenericRprControlMediosPago> sourceGeneric = new BeanItemContainer<GenericRprControlMediosPago>(GenericRprControlMediosPago.class);
    OptionGroup optStation = new OptionGroup();
    Grid grid;
    ExcelGeneratorCrlMediosPago excel = new ExcelGeneratorCrlMediosPago();
    String Estacion = "";
    final AdvancedFileDownloader downloader = new AdvancedFileDownloader();
    final AtomicBoolean makeZip = new AtomicBoolean(false);
    final AtomicReference<Object> name = new AtomicReference<Object>();
    SvcMtd svcmtd = new SvcMtd();

    public RptControDeMediosDePago() {
        super.setLocale(VaadinSession.getCurrent().getAttribute(Locale.class));
        super.addStyleName(ValoTheme.PANEL_BORDERLESS);
        super.setSizeFull();
        DashboardEventBus.register(this);
        usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
        super.setContent(components.createVertical(Constant.styleTransactions, "100%", false, true, true, new Component[]{buildForm()}));
        checkestacionesm.setBeanIdProperty("estacionid");
        cargaInfoSesion();
    }

    private Component buildForm() {
        btnExportar2.setEnabled(false);
        return components.createVertical(Constant.styleLogin, "100%", false, false, true, new Component[]{buildTitle(), buildHeader(), buildTableData(),/*buildToolbar2(),*/ buildButtons()});
    }

    private Component buildTitle() {
        Label title = new Label("Control de medios de pago");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        Component toolBar = components.createHorizontal(Constant.styleToolbar, Constant.sizeUndefined, true, false, false, new Component[]{buildToolbar()});
        return components.createHorizontal(Constant.styleViewheader, Constant.sizeUndefined, true, false, false, new Component[]{title, toolBar});
    }

    private Component buildHeader() {
        contPais = new BeanItemContainer<Pais>(Pais.class);
        contPais.addAll(dao.getAllPaises());
        cargaUltTurnoDia();
        cmbPais = new ComboBox("País:", contPais);
        cmbPais.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        cmbPais.setItemCaptionPropertyId("nombre");
        cmbPais.setItemIconPropertyId("flag");
        cmbPais.setWidth("165px");
        cmbPais.setRequired(true);
        cmbPais.setNullSelectionAllowed(false);
        cmbPais.setRequiredError("Debe seleccionar un país");
        cmbPais.addStyleName(ValoTheme.COMBOBOX_TINY);
        cmbPais.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                checkestacionesm = new BeanContainer<>(GenericEstacion.class);
                checkestacionesm.setBeanIdProperty("estacionid");
                if (cmbPais.getValue() != null) {
                    Pais pais = new Pais();
                    pais = (Pais) cmbPais.getValue();
                    checkestacionesm.addAll(SvcReporteControlMediosPago.getCheckEstacionesM(pais.getPaisId()));
                    toolbarContainerTables.removeAllComponents();
                    VerticalLayout vl = new VerticalLayout();
                    HorizontalLayout hl = new HorizontalLayout();
                    HorizontalLayout hlroot = new HorizontalLayout();
//                    hlroot.setSpacing(true);
                    hlroot.setResponsive(true);
                    btnSelectAll = new Button("Todas");
                    btnSelectAll.setStyleName(ValoTheme.BUTTON_TINY);
                    btnSelectAll.setStyleName(ValoTheme.BUTTON_FRIENDLY);
                    btnSelectAll.setStyleName(ValoTheme.BUTTON_LINK);
                    btnSelectAll.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
//                            optStation.select(361);
//                            optStation.select(checkestacionesm.getIdByIndex(2));
                            for (int i = 0; i < optStation.size(); i++) {
                                optStation.select(checkestacionesm.getIdByIndex(i));
                            }
                        }
                    });
                    btnUnselectAll = new Button("Ninguna");
                    btnUnselectAll.setStyleName(ValoTheme.BUTTON_TINY);
                    btnUnselectAll.setStyleName(ValoTheme.BUTTON_FRIENDLY);
                    btnUnselectAll.setStyleName(ValoTheme.BUTTON_LINK);
                    btnUnselectAll.addClickListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
//                            optStation.select(361);
//                            optStation.select(checkestacionesm.getIdByIndex(2));
                            for (int i = 0; i < optStation.size(); i++) {
                                optStation.unselect(checkestacionesm.getIdByIndex(i));
                            }
                        }
                    });
                    hl.addComponent(btnSelectAll);
                    hl.addComponent(btnUnselectAll);
//                    vl.setSpacing(true);
                    vl.setResponsive(true);
                    Label lblestacion = new Label("Estaciones");
                    lblestacion.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
                    vl.addComponent(lblestacion);
                    optStation = new OptionGroup(null, checkestacionesm);
                    optStation.setMultiSelect(true);
                    optStation.setStyleName(ValoTheme.OPTIONGROUP_SMALL);
                    vl.addComponent(optStation);
                    for (Integer noid : checkestacionesm.getItemIds()) {
                        optStation.setItemCaption(checkestacionesm.getItem(noid).getBean().getEstacionid(), checkestacionesm.getItem(noid).getBean().getNombre());
                    }
                    hlroot.addComponent(vl);
                    hlroot.addComponent(hl);
                    toolbarContainerTables.addComponent(hlroot);
                }
            }
        });

        cmbFechaInicio.setWidth("135px");
        cmbFechaInicio.setDateFormat("dd/MM/yyyy");
        cmbFechaInicio.setRangeEnd(Date.from(Instant.now()));
        cmbFechaInicio.setLocale(new Locale("es", "ES"));
        cmbFechaInicio.setLenient(true);
        cmbFechaInicio.addStyleName(ValoTheme.DATEFIELD_TINY);
        cmbFechaInicio.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
            }
        });

        cmbFechaFin.setWidth("135px");
        cmbFechaFin.setDateFormat("dd/MM/yyyy");
        cmbFechaFin.setRangeEnd(Date.from(Instant.now()));
        cmbFechaFin.setLocale(new Locale("es", "ES"));
        cmbFechaFin.setLenient(true);
        cmbFechaFin.addStyleName(ValoTheme.DATEFIELD_TINY);
        cmbFechaFin.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
            }
        });
        Component toolBar = components.createCssLayout(Constant.styleToolbar, Constant.sizeFull, false, false, true, new Component[]{utils.vlContainer(cmbPais),/* utils.vlContainer(cmbEstacion),*/ utils.vlContainer(cmbFechaInicio), utils.vlContainer(cmbFechaFin), utils.vlContainer(buildToolbar2())});
        VerticalLayout v = new VerticalLayout(toolBar);
        return components.createHorizontal(Constant.styleViewheader, Constant.sizeFull, false, false, true, new Component[]{v});
    }
    private CssLayout toolbarContainerTables;

    private Component buildToolbar2() {
        toolbarContainerTables = new CssLayout();
        return components.createHorizontal(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{utils.vlContainer(toolbarContainerTables)});
    }

    private CssLayout toolbarData;

    private Component buildTableData() {
        toolbarData = new CssLayout();
        return components.createHorizontal(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{utils.vlContainer(toolbarData)});
    }

    private CssLayout toolBar2;

    private Component buildToolbar() {
        toolBar2 = new CssLayout();
        VerticalLayout v = new VerticalLayout(toolBar2);
        return components.createHorizontal(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{v});
    }

    private Component buildButtons() {
        btnGenerar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnGenerar.setIcon(FontAwesome.SAVE);
        btnGenerar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if (cmbPais.getValue() != null && cmbFechaInicio.getValue() != null && cmbFechaFin.getValue() != null && optStation.size() > 0) {
                    if (optStation.getValue() != null) {
                        try {
                            sourceGeneric = new BeanItemContainer<GenericRprControlMediosPago>(GenericRprControlMediosPago.class);
                            String[] Seleccion;
                            Seleccion = getSeleccion();
                           for (String string : Seleccion) {
                                SvcReporteControlMediosPago.generar_datacrt(cmbFechaInicio.getValue(), cmbFechaFin.getValue(), string.trim(),"188");
                                sourceGeneric.addAll(SvcReporteControlMediosPago.getCtlMediosPago());
                                Estacion = svcmtd.getEstacion(Integer.parseInt(string.trim()));
                           }
                            System.out.println("seleccion " + Seleccion.length);
                            if (Seleccion.length>1) {
                                name.set("Estaciones_".concat(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())).concat(".zip"));
                                makeZip.set(true);
                            } else {
                                name.set("COCOs_CMDP_" + Estacion.concat(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())).concat(".xlsx"));
                                makeZip.set(false);
                            }

                            grid = new Grid(sourceGeneric);
                            grid.setWidth("1080px");
                            grid.removeAllColumns();
                            grid.addColumn("fecha");
                            grid.addColumn("lote");
                            grid.addColumn("monto_bruto");
                            grid.addColumn("comision");
                            grid.addColumn("monto_neto");
                            grid.addColumn("comentarios");
                            Grid.Column fecha = grid.getColumn("fecha");
                            fecha.setHeaderCaption("Fecha");
                            Grid.Column lote = grid.getColumn("lote");
                            lote.setHeaderCaption("Lote");
                            Grid.Column monto_bruto = grid.getColumn("monto_bruto");
                            monto_bruto.setHeaderCaption("Monto bruto");
                            Grid.Column comision = grid.getColumn("comision");
                            comision.setHeaderCaption("Comision");
                            Grid.Column monto_neto = grid.getColumn("monto_neto");
                            monto_neto.setHeaderCaption("Monto neto");
                            Grid.Column comentarios = grid.getColumn("comentarios");
                            comentarios.setHeaderCaption("Comentarios");
                            grid.setColumnOrder("fecha", "lote", "monto_bruto", "comision", "monto_neto", "comentarios");
                            toolbarData.removeAllComponents();
                            toolbarData.addComponent(grid);
                            btnExportar2.setEnabled(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            }
                        }
                    } else {
                        Notification.show("ERROR:", "Debe seleccionar todos los campos necesarios.\n", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }
            });

            btnExportar2.setCaption ("Exportar a Excel");
            btnExportar2.setStyleName (ValoTheme.BUTTON_PRIMARY);
            btnExportar2.setIcon (FontAwesome.EDIT);
            btnExportar2.addClickListener ( new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
          }
        });

            downloader.addAdvancedDownloaderListener (new AdvancedDownloaderListener() {
                @Override
                public void beforeDownload(DownloaderEvent downloadEvent) {
                if (sourceGeneric.size() > 0) {
                        System.out.println("IMP mery fea " + Estacion);
                        downloader.setFileDownloadResource(GenerarExcel());
                    }
                }
            });
            downloader.extend (btnExportar2);

            HorizontalLayout footer = (HorizontalLayout) components.createHorizontal(ValoTheme.WINDOW_BOTTOM_TOOLBAR, "", true, false, false, new Component[]{btnGenerar, btnExportar2});
            
            footer.setComponentAlignment (btnGenerar, Alignment.TOP_RIGHT);
            footer.setWidth (
                100.0f, Unit.PERCENTAGE);
            return footer ;
        }

    private StreamResource GenerarExcel() {
        StreamResource.StreamSource source = new StreamResource.StreamSource(){
            public InputStream getStream() {
                ByteArrayOutputStream stream = null;
                InputStream input = null;
                try {

                    String[] Seleccion;
                    Seleccion = getSeleccion();
                    /*Recorro la seleccion de estaciones para enviarlas al query*/
                    List<Object[]> files = new ArrayList<>();
                    for (String string : Seleccion) {
                        try {
                            Object[] f = new Object[2];
                            sourceGeneric = new BeanItemContainer<GenericRprControlMediosPago>(GenericRprControlMediosPago.class);
//                            int idEstacion = Integer.parseInt(optStation.getValue().toString());
//                            String paisId = SvcReporteControlMediosPago.getPaisId(idEstacion);
                            SvcReporteControlMediosPago.generar_datacrt(cmbFechaInicio.getValue(), cmbFechaFin.getValue(), string.trim(), "188");
                            sourceGeneric.addAll(SvcReporteControlMediosPago.getCtlMediosPago());
                             System.out.println("sourceGeneric "+sourceGeneric.size());
                            Estacion = svcmtd.getEstacion(Integer.parseInt(string.trim()));                            
                            
                            List<String> tituloscolumnas = new ArrayList<String>();
                            String[] titulos = new String[]{"Fecha", "Lote", "Monto bruto", "Comision", "Monto neto", "Comentarios"};

                            tituloscolumnas = Arrays.asList(titulos);

//                            XSSFWorkbook workbook = new XSSFWorkbook();
                            /*Generar Reporte en XLS*/
  //                          System.out.println("ESTACION " + Estacion);
                            XSSFWorkbook workbook = excel.generar(10, tituloscolumnas.size(), tituloscolumnas, "UNO-PETROL", "ESTACION " + Estacion, "RCMD", sourceGeneric, null);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            try {
                                workbook.write(bos);
                            } catch (Exception exc) {
                                bos.close();
                            }
                            String nameFile = "COCOs_CMDP_" + Estacion.concat(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())).concat(".xlsx");
                            f[0] = nameFile;
                            f[1] = bos;
                            files.add(f);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (makeZip.get()) {
                        Zip zip = new Zip();
                        input = zip.makeZip("Estaciones", files);
                    } else {
                        stream = (ByteArrayOutputStream) files.get(0)[10];
                        input = new ByteArrayInputStream(stream.toByteArray());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return input;
            }
        };
        StreamResource resource = new StreamResource(source, (String) name.get());
        return resource;
    }

    private String[] getSeleccion() {
        String[] result;
        String valor = String.valueOf(optStation.getValue());
        valor = valor.replaceAll("\\[", "");
        valor = valor.replaceAll("\\]", "");
        valor = valor.trim();
        result = valor.split(",");
        return result;
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
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
