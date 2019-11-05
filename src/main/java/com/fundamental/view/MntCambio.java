package com.fundamental.view;

import com.fundamental.model.Acceso;
import com.fundamental.model.TasaCambio;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.services.Dao;
import com.fundamental.services.SvcMntTasaCambio;
import com.fundamental.utils.Constant;
import com.fundamental.utils.XlsxReportGenerator;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vaadin.maddon.ListContainer;

public class MntCambio extends Panel implements View {

    Table tblChangeRate = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("tasa")) {
                return Constant.decFmt3.format(property.getValue());
            } else if (colId.equals("fechaInicio") || colId.equals("fechaFin")) {
                return Constant.SDF_yyyyMMdd.format(property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };
    Button btnAdd, btnSave, btnFilterClear;
    TextField tfdFilter;
    CssLayout filtering;

    BeanContainer<Integer, TasaCambio> bcrChangeRate;
    FormLayout form;
    BeanFieldGroup<TasaCambio> binder = new BeanFieldGroup<TasaCambio>(TasaCambio.class);
    private TasaCambio tasaCambio;
    private final Button btnExcel = new Button("Exportar", FontAwesome.FILE_EXCEL_O);

    String action;
    @PropertyId("tasa")
    TextField tasa;
    @PropertyId("fechaInicio")
    PopupDateField pdfDateStart;
    @PropertyId("fechaFin")
    PopupDateField pdfDateEnd;
    @PropertyId("pais")
    ComboBox cbxPais;
    List<Pais> paises;

    private final VerticalLayout vlRoot;
    private CssLayout content = new CssLayout();
    Utils utils = new Utils();
    Usuario user;
    Acceso acceso = new Acceso();
    public MntCambio() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        content.addStyleName("dashboard-panels");
        Responsive.makeResponsive(content);
        vlRoot = utils.buildVerticalR("vlRoot", true, false, true, "dashboard-view");
        setContent(vlRoot);
        Responsive.makeResponsive(vlRoot);
        vlRoot.addComponent(utils.buildHeaderR("Tipo de cambio"));
        vlRoot.addComponent(utils.buildSeparator());
        vlRoot.addComponent(content);
        vlRoot.setExpandRatio(content, 1);
        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        getAllData();
        //template

        initControls();
        buildButtons();

        filtering = new CssLayout();
        filtering.addComponents(tfdFilter, btnFilterClear);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        
        HorizontalLayout vlButtons = new HorizontalLayout(btnExcel, btnAdd);
        vlButtons.setSizeUndefined();
        
        VerticalLayout vlLeft = new VerticalLayout(filtering, tblChangeRate,vlButtons);
        vlLeft.setSizeUndefined();
        vlLeft.setId("vlLeft");
        vlLeft.setComponentAlignment(filtering, Alignment.MIDDLE_CENTER);
        vlLeft.setComponentAlignment(vlButtons, Alignment.TOP_CENTER);
        Responsive.makeResponsive(vlLeft);

        VerticalLayout vlRight = new VerticalLayout(form, btnSave);
        vlRight.setSizeUndefined();
        vlRight.setId("vlRight");
        vlRight.setComponentAlignment(btnSave, Alignment.MIDDLE_CENTER);
        Responsive.makeResponsive(vlRight);

        content.addComponents(utils.vlContainer(vlRight), vlLeft);

        if (bcrChangeRate.size() > 0) {
            tblChangeRate.setValue(bcrChangeRate.getItemIds().get(0));
        } else {
            btnAdd.click();
        }
    }

    private void getAllData() {
        bcrChangeRate = new BeanContainer<>(TasaCambio.class);
        bcrChangeRate.setBeanIdProperty("tasacambioId");

        SvcMntTasaCambio service = new SvcMntTasaCambio();
        List<TasaCambio> tasasc = service.getAllRates();
        bcrChangeRate.addAll(tasasc);
        paises = service.getAllPaises();
        service.closeConnections();
    }

    private void initControls() {
        tfdFilter = utils.buildTextFilter("Búsqueda tabla");
        tfdFilter.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        tfdFilter.addShortcutListener(new ShortcutListener("Clear", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                tfdFilter.setValue("");
                ((Filterable) tblChangeRate.getContainerDataSource()).removeAllContainerFilters();
            }
        });
        tfdFilter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                Container.Filterable data = (Container.Filterable) tblChangeRate.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Container.Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId, final Item item) {
                        if (event.getText() == null || event.getText().equals("")) {
                            return true;
                        }
                        return utils.filterByProperty("paisNombre", item, event.getText())
                                || utils.filterByProperty("fechaInicio", item, event.getText())
                                || utils.filterByProperty("fechaFin", item, event.getText());
                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        if (propertyId.equals("paisNombre")
                                || propertyId.equals("fechaInicio")
                                || propertyId.equals("fechaFin")) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        btnFilterClear = new Button(FontAwesome.TIMES);
        btnFilterClear.addStyleName(ValoTheme.BUTTON_SMALL);
        btnFilterClear.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                tfdFilter.setValue("");
                ((Filterable) tblChangeRate.getContainerDataSource()).removeAllContainerFilters();
            }
        });

        tblChangeRate.setCaption("Tasas de Cambio");
        tblChangeRate.setContainerDataSource(bcrChangeRate);
        tblChangeRate.setVisibleColumns(new Object[]{"paisNombre", "fechaInicio", "fechaFin", "tasa"});
        tblChangeRate.setColumnHeaders(new String[]{"Pais", "Inicio", "Fin", "Tasa"});
        tblChangeRate.setColumnAlignments(new Table.Align[]{Table.Align.LEFT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT});
        tblChangeRate.setSizeUndefined();
        tblChangeRate.setStyleName(ValoTheme.TABLE_COMPACT);
        tblChangeRate.setStyleName(ValoTheme.TABLE_SMALL);
        tblChangeRate.setSelectable(true);
        tblChangeRate.setHeight("400px");
//        tblChangeRate.setWidth(400f, Unit.PERCENTAGE);
        tblChangeRate.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (tblChangeRate.getValue()!=null) {
                action = Dao.ACTION_UPDATE;
                binder.setItemDataSource(bcrChangeRate.getItem(tblChangeRate.getValue()));
                tasaCambio = bcrChangeRate.getItem(tblChangeRate.getValue()).getBean();
                for (Pais p : paises) {
                    if (p.getPaisId().equals(tasaCambio.getPaisId())) {
                        tasaCambio.setPais(p);
                    }
                }
                binder.setItemDataSource(tasaCambio);
                }
            }
        });

        cbxPais = utils.buildCombobox("País:", "nombre", false, true, null, new ListContainer<Pais>(Pais.class, paises));
        cbxPais.setItemIconPropertyId("flag");

        tasa = utils.buildTextField("Tasa:", "0", false, 9, true, "align-right");

        pdfDateStart = utils.buildPopupDateField("Fecha inicio:", new Date(), "yyyy/MM/dd", true, Resolution.DAY, null, null, new Locale("es", "ES"), null);
        pdfDateStart.setDescription("La fecha es inclusiva.");

        pdfDateEnd = utils.buildPopupDateField("Fecha fin:", new Date(), "yyyy/MM/dd", true, Resolution.DAY, null, null, new Locale("es", "ES"), null);;
        pdfDateEnd.setDescription("La fecha es inclusiva.");

        form = new FormLayout();
        form.setSizeUndefined();
        form.addComponents(cbxPais, pdfDateStart, pdfDateEnd, tasa);

        binder.bindMemberFields(this);
        binder.setItemDataSource(tasaCambio);
    }

    private void buildButtons() {
        btnAdd = new Button("Agregar", FontAwesome.PLUS);
        btnAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                action = Dao.ACTION_ADD;
                tasaCambio = new TasaCambio();
                binder.setItemDataSource(tasaCambio);
            }
        });

        btnSave = new Button("Guardar", FontAwesome.SAVE);
        btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!cbxPais.isValid() || !pdfDateStart.isValid() || !pdfDateEnd.isValid() || !tasa.isValid()) {
                    Notification.show("Todos los campos marcados son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                if (pdfDateStart.getValue().after(pdfDateEnd.getValue())) {
                    Notification.show("Rango de fechas no válido.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                TasaCambio tco;
                for (Integer itemId : bcrChangeRate.getItemIds()) {
                    tco = bcrChangeRate.getItem(itemId).getBean();
                    if (  action.equals(Dao.ACTION_ADD) &&
                            tco.getPaisId().equals(((Pais) cbxPais.getValue()).getPaisId())
                            && (
                               tco.getFechaInicio().equals(pdfDateStart.getValue())
                            || tco.getFechaInicio().equals(pdfDateEnd.getValue())
                            || tco.getFechaFin().equals((pdfDateStart.getValue()))
                            || tco.getFechaFin().equals((pdfDateEnd.getValue()))
                            || (tco.getFechaInicio().before(pdfDateStart.getValue()) && tco.getFechaFin().after((pdfDateStart.getValue())))
                            || (tco.getFechaInicio().before(pdfDateEnd.getValue()) && tco.getFechaFin().after((pdfDateEnd.getValue())))
                            )) {
                        Notification.show("Revise los nuevos datos, coinciden con un país y rango existente.", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }

                try {
                    binder.commit();
                } catch (Exception ex) {
                    Logger.getLogger(MntCambio.class.getName()).log(Level.SEVERE, null, ex);
                }
                tasaCambio.setPaisId(((Pais) cbxPais.getValue()).getPaisId());
                tasaCambio.setCreadoPor(user.getUsername());
                tasaCambio.setModificadoPor(user.getUsername());

                SvcMntTasaCambio service = new SvcMntTasaCambio();
                tasaCambio = service.doAction(action, tasaCambio);
                service.closeConnections();
                if (tasaCambio.getTasacambioId() > 0) {
                    Notification notif = new Notification("¡Exito!", "La acción se ha ejecutado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_CAMBIO.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + tasaCambio.getDescError(), Notification.Type.ERROR_MESSAGE);
                }

            }
        });
        
        btnExcel.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnExcel.setVisible(true);
        StreamResource sr = getExcelStreamResource();
        FileDownloader fileDownloader = new FileDownloader(sr);
        fileDownloader.extend(btnExcel);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new Dao();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnAdd.setEnabled(acceso.isAgregar());
        btnSave.setEnabled(acceso.isCambiar());
    }
    
     private StreamResource getExcelStreamResource() {
        StreamResource.StreamSource source = new StreamResource.StreamSource() {
        public InputStream getStream() {
            ByteArrayOutputStream stream;
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
            InputStream input = null;
            try {
                    String[] colTitles = new String[]{"País", "Fecha de Inicio", "Fecha Fin", "Tasa"}; 
                    List<String[]> data = new ArrayList();
                    for (int id: bcrChangeRate.getItemIds()){
                      TasaCambio model =  bcrChangeRate.getItem(id).getBean();
                      data.add(new String []{
                          model.getPaisNombre(),dateformat.format(model.getFechaInicio()),dateformat.format(model.getFechaFin()),model.getTasa().toString()
                        });
                    }
                    Integer [] colTypes = new Integer [] {CELL_TYPE_STRING,CELL_TYPE_STRING,CELL_TYPE_STRING,50};
                    Map<String, String> filters = new HashMap();

                    XlsxReportGenerator xrg = new XlsxReportGenerator();
                    XSSFWorkbook workbook = xrg.generate("Tipo de Cambio", filters, colTitles, data,colTypes);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    try {
                       workbook.write(bos);
                     } catch (Exception exc) {
                        bos.close();
                    }

                    stream = bos;
                    input = new ByteArrayInputStream(stream.toByteArray());
                    
               } catch (IOException ex) {
                    Logger.getLogger(MntCambio.class.getName()).log(Level.SEVERE, null, ex);
                }
                return input;
            }
        };
        String fileName = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()).concat("_Tasa_Cambio")//.concat(fileName)
                .concat(".xlsx");
        StreamResource resource = new StreamResource(source, fileName);
        return resource;
    }

}
