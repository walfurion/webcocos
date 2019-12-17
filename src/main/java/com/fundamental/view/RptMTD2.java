/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.view;

import com.fundamental.model.Dia;
import com.fundamental.model.Turno;
import com.fundamental.model.Utils;
import com.fundamental.services.SvcMtd;
import com.fundamental.services.SvcTurno;
import com.fundamental.services.SvcUsuario;
import com.fundamental.utils.AdvancedFileDownloader;
import com.fundamental.utils.AdvancedFileDownloader.AdvancedDownloaderListener;
import com.fundamental.utils.AdvancedFileDownloader.DownloaderEvent;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.fundamental.utils.ExcelGenerator;
import com.sisintegrados.generic.bean.GenericEstacion;
import com.sisintegrados.generic.bean.GenericMTD;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Allan G.
 */
public class RptMTD2 extends Panel implements View {

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
    SvcMtd svcmtd = new SvcMtd();
    BeanItemContainer<Pais> contPais = new BeanItemContainer<Pais>(Pais.class);
    Button btnGenerar = new Button("Generar Reporte");
    Button btnExportar2 = new Button("Exportar a Excel", FontAwesome.EDIT);
//    Button btnExportar = new Button("GenerarDownload", FontAwesome.EDIT);
    Button btnSelectAll;
    Button btnUnselectAll;
    //traer estaciones con su checkbox
    BeanContainer<Integer, GenericEstacion> checkestaciones = new BeanContainer<>(GenericEstacion.class);
    BeanItemContainer<GenericMTD> sourceGeneric = new BeanItemContainer<GenericMTD>(GenericMTD.class);
    OptionGroup optStation = new OptionGroup();
    Grid grid;
    ExcelGenerator excel = new ExcelGenerator();
    String Estacion = "";
//    StreamResource sr;
//    FileDownloader fileDownloader;
    final AdvancedFileDownloader downloader = new AdvancedFileDownloader();

    public RptMTD2() {
        super.setLocale(VaadinSession.getCurrent().getAttribute(Locale.class));
        super.addStyleName(ValoTheme.PANEL_BORDERLESS);
        super.setSizeFull();
        DashboardEventBus.register(this);
        usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
        super.setContent(components.createVertical(Constant.styleTransactions, "100%", false, true, true, new Component[]{buildForm()}));
        checkestaciones.setBeanIdProperty("estacionid");
        cargaInfoSesion();
        //Para exportar
//        sr = GenerarExcel();
//        fileDownloader = new FileDownloader(sr);
//        fileDownloader.extend(btnExportar2);
    }

    private Component buildForm() {
        btnExportar2.setEnabled(false);
        return components.createVertical(Constant.styleLogin, "100%", false, false, true, new Component[]{buildTitle(), buildHeader(), buildTableData(),/*buildToolbar2(),*/ buildButtons()});
    }

    private Component buildTitle() {
        Label title = new Label("Ventas Diarias e Inventario");
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
                checkestaciones = new BeanContainer<>(GenericEstacion.class);
                checkestaciones.setBeanIdProperty("estacionid");
                if (cmbPais.getValue() != null) {
                    Pais pais = new Pais();
                    pais = (Pais) cmbPais.getValue();
                    checkestaciones.addAll(svcmtd.getCheckEstaciones(pais.getPaisId()));
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
//                            optStation.select(checkestaciones.getIdByIndex(2));
                            for (int i = 0; i < optStation.size(); i++) {
                                optStation.select(checkestaciones.getIdByIndex(i));
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
//                            optStation.select(checkestaciones.getIdByIndex(2));
                            for (int i = 0; i < optStation.size(); i++) {
                                optStation.unselect(checkestaciones.getIdByIndex(i));
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
                    System.out.println("CHECK ESTACION " + checkestaciones.size());
                    optStation = new OptionGroup(null, checkestaciones);
                    optStation.setMultiSelect(false);
                    optStation.setStyleName(ValoTheme.OPTIONGROUP_SMALL);
                    vl.addComponent(optStation);
                    for (Integer noid : checkestaciones.getItemIds()) {
                        optStation.setItemCaption(checkestaciones.getItem(noid).getBean().getEstacionid(), checkestaciones.getItem(noid).getBean().getNombre());
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
        Component toolBar = components.createCssLayout(Constant.styleToolbar, Constant.sizeFull, false, false, true, new Component[]{utils.vlContainer(cmbPais), /* utils.vlContainer(cmbEstacion),*/ utils.vlContainer(cmbFechaInicio), utils.vlContainer(cmbFechaFin), utils.vlContainer(buildToolbar2())});
        VerticalLayout v = new VerticalLayout(toolBar);
        return components.createHorizontal(Constant.styleViewheader, Constant.sizeFull, false, false, true, new Component[]{v});
    }
    private CssLayout toolbarContainerTables;

    private Component buildToolbar2() {
        toolbarContainerTables = new CssLayout();
//        return toolbarContainerTables;
        return components.createHorizontal(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{utils.vlContainer(toolbarContainerTables)});
    }

    private CssLayout toolbarData;

    private Component buildTableData() {
        toolbarData = new CssLayout();
//        return toolbarContainerTables;
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
                            sourceGeneric = new BeanItemContainer<GenericMTD>(GenericMTD.class);
                            svcmtd.generar_data(cmbFechaInicio.getValue(), cmbFechaFin.getValue(), optStation.getValue().toString());
                            sourceGeneric.addAll(svcmtd.getMTD());
                            Estacion = svcmtd.getEstacion(Integer.parseInt(optStation.getValue().toString()));
                            grid = new Grid(sourceGeneric);
//                        grid.setCaption("Ventas Diarias e Inventario");
                            grid.setWidth("1080px");
//                        grid.setContainerDataSource(sourceGeneric);
                            grid.removeAllColumns();
                            grid.addColumn("p_super");
                            grid.addColumn("p_regular");
                            grid.addColumn("p_diesel");
                            grid.addColumn("l_super");
                            grid.addColumn("l_regular");
                            grid.addColumn("l_diesel");
                            grid.addColumn("c_diesel");
                            grid.addColumn("c_super");
                            grid.addColumn("c_regular");
                            grid.addColumn("l_total");
                            grid.addColumn("c_total");
                            Grid.Column p_super = grid.getColumn("p_super");
                            p_super.setHeaderCaption("Precio Super");
                            Grid.Column p_regular = grid.getColumn("p_regular");
                            p_regular.setHeaderCaption("Precio Regular");
                            Grid.Column p_diesel = grid.getColumn("p_diesel");
                            p_diesel.setHeaderCaption("Precio Diesel");
                            Grid.Column l_super = grid.getColumn("l_super");
                            l_super.setHeaderCaption("Litros Super");
                            Grid.Column l_regular = grid.getColumn("l_regular");
                            l_regular.setHeaderCaption("Litros Regular");
                            Grid.Column l_diesel = grid.getColumn("l_diesel");
                            l_diesel.setHeaderCaption("Litros Diesel");
                            Grid.Column l_total = grid.getColumn("l_total");
                            l_total.setHeaderCaption("Total Litros");
                            Grid.Column c_super = grid.getColumn("c_super");
                            c_super.setHeaderCaption("Colon Super");
                            Grid.Column c_regular = grid.getColumn("c_regular");
                            c_regular.setHeaderCaption("Colon Regular");
                            Grid.Column c_diesel = grid.getColumn("c_diesel");
                            c_diesel.setHeaderCaption("Colon Diesel");
                            Grid.Column c_total = grid.getColumn("c_total");
                            c_total.setHeaderCaption("Colones Total");
                            grid.setColumnOrder("p_super", "p_regular", "p_diesel", "l_super", "l_regular", "l_diesel", "l_total", "c_super", "c_regular", "c_diesel", "c_total");
                            toolbarData.removeAllComponents();
                            toolbarData.addComponent(grid);
//                            sr = GenerarExcel();
//                            fileDownloader = new FileDownloader(sr);
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

        btnExportar2.setCaption("Exportar a Excel");
        btnExportar2.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnExportar2.setIcon(FontAwesome.EDIT);
        btnExportar2.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
//                getUI().getPage().reload();
//                getUI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_INV_FIS.getViewName());
//                UI.getCurrent().getNavigator().navigateTo(DashboardViewType.RPT_MTD.getViewName());
//                //                getUI().getPage().open(resource, "_blank", false);
//                /*Devulevo una lista de string seleccionados*/
//                String[] Seleccion;
//                Seleccion = getSeleccion();
//                /*Recorro la seleccion de estaciones para enviarlas al query*/
//                for (String string : Seleccion) {
//                    try {
//                        sourceGeneric = new BeanItemContainer<GenericMTD>(GenericMTD.class);
//                        //*Ejemplo de query */
//                        svcmtd.generar_data(cmbFechaInicio.getValue(), cmbFechaFin.getValue(), string.trim());
//                        sourceGeneric.addAll(svcmtd.getMTD());
//                        Estacion = svcmtd.getEstacion(Integer.parseInt(string.trim()));
//                        DownloadExcel();
////                        getUI().getPage().open(file, "_self", false);
//
////                        GenericMTD mtd = new GenericMTD();
////                        mtd = sourceGeneric.getIdByIndex(0);
////                        System.out.println("LITROS SUPER " + mtd.getL_super() + " ESTACION " + Estacion);
////                        System.out.println("Select * from estacion where estacion_id = " + string.trim());
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
            }
        });

        downloader.addAdvancedDownloaderListener(new AdvancedDownloaderListener() {
            @Override
            public void beforeDownload(DownloaderEvent downloadEvent) {
                if (sourceGeneric.size() > 0) {
                    downloader.setFileDownloadResource(GenerarExcel());
                }
            }
        });
        downloader.extend(btnExportar2);

        HorizontalLayout footer = (HorizontalLayout) components.createHorizontal(ValoTheme.WINDOW_BOTTOM_TOOLBAR, "", true, false, false, new Component[]{btnGenerar, btnExportar2});

        footer.setComponentAlignment(btnGenerar, Alignment.TOP_RIGHT);
        footer.setWidth(
                100.0f, Unit.PERCENTAGE);
        return footer;
    }

    private StreamResource GenerarExcel() {
//        /*Devulevo una lista de string seleccionados*/
//        String[] Seleccion;
//        Seleccion = getSeleccion();
//        /*Recorro la seleccion de estaciones para enviarlas al query*/
//        for (String string : Seleccion) {
//            try {
//                sourceGeneric = new BeanItemContainer<GenericMTD>(GenericMTD.class);
//                svcmtd.generar_data(cmbFechaInicio.getValue(), cmbFechaFin.getValue(), string.trim());
//                sourceGeneric.addAll(svcmtd.getMTD());
//                Estacion = svcmtd.getEstacion(Integer.parseInt(string.trim()));
//                System.out.println("ESTACUIB " + string.trim());
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
        StreamResource.StreamSource source = new StreamResource.StreamSource() {
            public InputStream getStream() {
                ByteArrayOutputStream stream;
                InputStream input = null;
                try {
                    List<String> tituloscolumnas = new ArrayList<String>();
                    String[] titulos = new String[]{"Dia", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Total", "%", "Super", "Regular", "Diesel", "Total", "%", "Total", "%", "Total", "%", "Total", "%", "Total Otros", "%", "Total Uno", "%", "A", "B", "C", "#1", "#2", "#3", "#4", "#5", "#6",
                        "Totales", "Contado", "%", "Credomatic", "%", "Banco Nac", "%", "BCR", "%", "Magic SB", "%", "FM Davivienda", "%", "Versatec", "%", "Flota BCR", "%", "Flota Bac", "%", "Uno Plus", "%", "Cupon", "%", "Prepagos", "%", "TC Davivienda", "%", "Credito", "%", "Contado USD", "%",
                        "Super", "Regular", "Diesel", "Total", "%", "Super", "Regular", "Diesel", "Total", "%", "Super", "Regular", "Diesel", "Total", "%", "Sobrantes", "Faltantes", "Total", "%", "Total", "%", "Super", "%", "Regular", "%", "Diesel", "%", "Contado en USD",
                        "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel",
                        "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel", "Super", "Regular", "Diesel"};

                    tituloscolumnas = Arrays.asList(titulos);
//                XSSFWorkbook workbook = new XSSFWorkbook();
                    /*Generar Reporte en XLS*/
//                System.out.println("ESTACION " + Estacion);
                    XSSFWorkbook workbook = excel.generar(1, tituloscolumnas.size(), tituloscolumnas, "UNO-PETROL", "ESTACION " + Estacion, "MTD", sourceGeneric, null);

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
        String fileName = "COCOs_MTD_".concat(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime())).concat(".xlsx");
        StreamResource resource = new StreamResource(source, fileName);
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
