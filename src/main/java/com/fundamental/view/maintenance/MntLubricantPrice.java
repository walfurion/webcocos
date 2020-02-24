package com.fundamental.view.maintenance;

import com.fundamental.model.Acceso;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.Lubricanteprecio;
import com.fundamental.model.Marca;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Producto;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.sisintegrados.dao.Dao;
import com.fundamental.services.SvcGeneral;
import com.fundamental.utils.Constant;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.view.form.FormUploadTarjetas;
import com.vaadin.addon.tableexport.DefaultTableHolder;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.addon.tableexport.TableHolder;
import com.vaadin.data.Container;
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
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.v7.ui.themes.BaseTheme;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.maddon.ListContainer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.util.Iterator;
import java.io.IOException;
import org.vaadin.addons.excelexporter.ExportExcelComponentConfiguration;
import org.vaadin.addons.excelexporter.ExportExcelConfiguration;
import org.vaadin.addons.excelexporter.ExportExcelSheetConfiguration;
import org.vaadin.addons.excelexporter.ExportToExcelUtility;
import org.vaadin.addons.excelexporter.ExportType;
import org.vaadin.addons.excelexporter.builder.ExportExcelComponentConfigurationBuilder;
import org.vaadin.addons.excelexporter.builder.ExportExcelConfigurationBuilder;
import org.vaadin.addons.excelexporter.builder.ExportExcelSheetConfigurationBuilder;

/**
 * @author Henry Barrientos
 */
public class MntLubricantPrice extends Panel implements View {

    Acceso acceso = new Acceso();
    Button btnSave, btnAdd, btnFilterClear, btnExcel, btnCarga;
    TextField tfdFilter;
    BeanContainer<Integer, Lubricanteprecio> bcrProduct = new BeanContainer<Integer, Lubricanteprecio>(Lubricanteprecio.class);
    Table tblProduct = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("precio")) {
                return Constant.decimal2D.format(property.getValue()).trim();
            } else if (colId.equals("fechaInicio") || colId.equals("fechaFin")) {
                return Constant.SDF_yyyyMMdd.format(property.getValue()).trim();
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };

    private BeanFieldGroup<Lubricanteprecio> binder = new BeanFieldGroup<Lubricanteprecio>(Lubricanteprecio.class);
//    @PropertyId("pais")
    ComboBox cbxCountry; //cbxStation, 
//    @PropertyId("producto")
    ComboBox cbxProduct;
//    @PropertyId("marca")
    ComboBox cbxBrand;
    @PropertyId("precio")
    TextField tfdPrice;
    @PropertyId("fechaInicio")
    DateField dfdStart;
    @PropertyId("fechaFin")
    DateField dfdEnd;
    Lubricanteprecio lubricante;
    Lubricanteprecio lubricante_log;

    Upload upload;
    File tempFile;
    int line;

    List<Pais> listCountries;
    List<Estacion> listStations = new ArrayList();
    List<Lubricanteprecio> listProducts = new ArrayList();
    List<Producto> allLubricants = new ArrayList();
    List<Marca> listBrands = new ArrayList();
    String action = DaoImp.ACTION_ADD;

//template
    private final VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Usuario user;
    FormUploadTarjetas frmUploadTarjetas;

    public MntLubricantPrice() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        vlRoot = new VerticalLayout();
        vlRoot.setSizeFull();
//        vlRoot.setSizeUndefined();
        vlRoot.setMargin(true);
        vlRoot.addStyleName("dashboard-view");
        setContent(vlRoot);
        Responsive.makeResponsive(vlRoot);
        vlRoot.setId("vlRoot");

        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        vlRoot.addComponent(utils.buildHeader("Precio lubricantes", false, true));
        vlRoot.addComponent(utils.buildSeparator());
        getAllData();
//template

        buildControls();
        buildTableContent();
        buildButtons();

        CssLayout filtering = new CssLayout();
        filtering.addComponents(tfdFilter, btnFilterClear);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filtering.setSizeUndefined();

//        HorizontalLayout hltFilters = utils.buildHorizontal("hltFilters", false, true, true, false);
//        hltFilters.setSizeFull();
//        hltFilters.addComponents(cbxCountry//, cbxStation
//        );
        VerticalLayout vlLeft = new VerticalLayout(//hltFilters, 
                filtering, tblProduct, btnAdd);
        vlLeft.setSpacing(true);
        vlLeft.setSizeUndefined();
        vlLeft.setId("vlLeft");
        vlLeft.setComponentAlignment(filtering, Alignment.MIDDLE_CENTER);
        vlLeft.setComponentAlignment(btnAdd, Alignment.MIDDLE_CENTER);
        Responsive.makeResponsive(vlLeft);

        VerticalLayout vlRight = new VerticalLayout(cbxCountry, cbxBrand, cbxProduct, dfdStart, dfdEnd, tfdPrice, btnSave, btnExcel, btnCarga);
        vlRight.setSpacing(true);
        vlRight.setSizeUndefined();
        vlRight.setId("vlRight");
        vlRight.setComponentAlignment(btnSave, Alignment.MIDDLE_CENTER);
        vlRight.setComponentAlignment(btnExcel, Alignment.MIDDLE_CENTER);
        vlRight.setComponentAlignment(btnCarga, Alignment.MIDDLE_CENTER);

        Responsive.makeResponsive(vlRight);

//        VerticalLayout vlUpload = new VerticalLayout(upload);
//        vlUpload.setSpacing(true);
//        vlUpload.setSizeUndefined();
//        vlUpload.setId("vlUpload");
//        vlUpload.setComponentAlignment(upload, Alignment.MIDDLE_CENTER);
//        Responsive.makeResponsive(vlUpload);
        CssLayout cltTables = new CssLayout(utils.vlContainer(vlLeft), utils.vlContainer(vlRight));
        cltTables.setId("cltTables");
        cltTables.setSizeUndefined();
        Responsive.makeResponsive(cltTables);

        vlRoot.addComponents(cltTables);
        vlRoot.setExpandRatio(cltTables, 1);

        if (bcrProduct.size() > 0) {
            tblProduct.setValue(bcrProduct.getItemIds().get(0));
        } else {
            btnAdd.click();
        }
    }

    private void getAllData() {
        bcrProduct.setBeanIdProperty("lubricanteprecio");
        bcrProduct.addAll(new ArrayList());
        SvcGeneral service = new SvcGeneral();
        listCountries = service.getAllPaises();
        listBrands = service.getAllBrands(false);
//        allLubricants = service.getAllProductosByTypeMarca(2, false, null); //lubricants
        service.closeConnections();
    }

    private void buildControls() {
        cbxCountry = utils.buildCombobox("Pais:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<Pais>(Pais.class, listCountries));
        cbxCountry.setItemIconPropertyId("flag");
        cbxCountry.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (cbxCountry.getValue() != null && cbxBrand.getValue() != null) {
                    btnAdd.setEnabled(true);
                    bcrProduct.removeAllItems();
                    SvcGeneral service = new SvcGeneral();
                    int countryId = ((Pais) cbxCountry.getValue()).getPaisId();
                    int brandId = ((Marca) cbxBrand.getValue()).getIdMarca();
                    listProducts = service.getLubpriceByCountryidStationid(countryId, brandId);
                    allLubricants = service.getAllProductosByCountryTypeBrand(countryId, 2, brandId, false); //lubricants
                    bcrProduct.addAll(listProducts);
                    cbxProduct.setContainerDataSource(new ListContainer<Producto>(Producto.class, allLubricants));
                    service.closeConnections();
                }
            }
        });

        cbxProduct = utils.buildCombobox("Producto:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<Producto>(Producto.class, allLubricants));
        cbxProduct.setFilteringMode(FilteringMode.CONTAINS);
        cbxProduct.setWidth("300px");

        btnFilterClear = new Button(FontAwesome.TIMES);
        btnFilterClear.addStyleName(ValoTheme.BUTTON_SMALL);
        btnFilterClear.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                tfdFilter.setValue("");
                ((Container.Filterable) tblProduct.getContainerDataSource()).removeAllContainerFilters();
            }
        });
        tfdFilter = utils.buildTextFilter("Filtro búsqueda");
        tfdFilter.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        tfdFilter.addShortcutListener(new ShortcutListener("Clear", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                tfdFilter.setValue("");
                ((com.vaadin.data.Container.Filterable) tblProduct.getContainerDataSource()).removeAllContainerFilters();
            }
        });
        tfdFilter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                Container.Filterable data = (Container.Filterable) tblProduct.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Container.Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId, final Item item) {
                        if (event.getText() == null || event.getText().equals("")) {
                            return true;
                        }
                        return filterByProperty("paisNombre", item, event.getText())
                                //                                || filterByProperty("estacionNombre", item, event.getText())
                                || filterByProperty("productoNombre", item, event.getText());
                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        return propertyId.equals("paisNombre")
                                //                                || propertyId.equals("estacionNombre")
                                || propertyId.equals("productoNombre");
                    }
                });
            }
        });

        dfdStart = utils.buildDateField("Fecha inicio:", "yyyy/MM/dd", new Locale("es", "ES"), ValoTheme.DATEFIELD_SMALL, null, null, true, Resolution.DAY, new Date());

        dfdEnd = utils.buildDateField("Fecha fin:", "yyyy/MM/dd", new Locale("es", "ES"), ValoTheme.DATEFIELD_SMALL, new Date(), null, true, Resolution.DAY, new Date());

        tfdPrice = utils.buildTextField("Precio:", "0", false, 10, true, ValoTheme.TEXTFIELD_SMALL);
        tfdPrice.addStyleName("align-right");
        binder.bindMemberFields(this);
        binder.setItemDataSource(lubricante);
        cbxBrand = utils.buildCombobox("Marca:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(Marca.class, listBrands));
        cbxBrand.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (cbxCountry.getValue() != null && cbxBrand.getValue() != null) {
                    btnAdd.setEnabled(true);
                    bcrProduct.removeAllItems();
                    SvcGeneral service = new SvcGeneral();
                    int countryId = ((Pais) cbxCountry.getValue()).getPaisId();
                    int brandId = ((Marca) cbxBrand.getValue()).getIdMarca();
                    listProducts = service.getLubpriceByCountryidStationid(countryId, brandId);
                    allLubricants = service.getAllProductosByCountryTypeBrand(countryId, 2, brandId, false); //lubricants
                    bcrProduct.addAll(listProducts);
                    cbxProduct.setContainerDataSource(new ListContainer<Producto>(Producto.class, allLubricants));
                    service.closeConnections();
                }
            }
        });

//        upload = new Upload("Selección de archivo:", new Upload.Receiver() {
//            @Override
//            public OutputStream receiveUpload(String filename, String mimeType) {
//                if (filename.isEmpty() || !filename.toUpperCase().contains(".XLSX")) {
//                    Notification.show("Debe seleccionar un archivo y ser de formato .xlsx", Notification.Type.WARNING_MESSAGE);
//                    return null;
//                }
//                try {
//                    tempFile = File.createTempFile("tempCocoFile", ".xlsx");
//                    return new FileOutputStream(tempFile);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//        });
//        upload.addFinishedListener(new Upload.FinishedListener() {
//            @Override
//            public void uploadFinished(Upload.FinishedEvent finishedEvent) {
//                try {
//                    if (tempFile != null && tempFile.exists()) {
//                        XlsxReader(tempFile, true);
//                    }
//                } catch (Exception ex) {
//                    Notification.show("Fila: " + line + "; " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
//                }
//            }
//        });
//        upload.setButtonCaption("Cargar");
        //Importante
//        binder.bindMemberFields(this);
//        binder.setItemDataSource(lubricante);
    }

    private void buildTableContent() {
        tblProduct.setCaption("Lubricantes:");
        tblProduct.setHeight(200, Unit.PERCENTAGE);
        tblProduct.setWidth(450, Unit.PERCENTAGE);
        tblProduct.setContainerDataSource(bcrProduct);
        tblProduct.setVisibleColumns(new Object[]{"productoNombre"});
        tblProduct.setColumnHeaders(new String[]{"Nombre"});
        tblProduct.addStyleName(ValoTheme.TABLE_COMPACT);
        tblProduct.addStyleName(ValoTheme.TABLE_SMALL);
        tblProduct.setSizeUndefined();
        tblProduct.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (tblProduct.getValue() != null) {
                    btnSave.setEnabled(true);
                    action = DaoImp.ACTION_UPDATE;
                    lubricante = bcrProduct.getItem(tblProduct.getValue()).getBean();
                    lubricante_log = (Lubricanteprecio) lubricante.clone();
                    if (lubricante_log.getModificadoEl() == null) {
                        lubricante_log.setModificadoEl(lubricante.getCreadoEl());
                    }
                    if (lubricante_log.getModificadoPor() == null || !lubricante_log.getModificadoPor().trim().equals("")) {
                        lubricante_log.setModificadoPor(lubricante.getCreadoPor());
                    }
                    allLubricants.add(lubricante.getProducto());
//                    cbxProduct.setContainerDataSource(new ListContainer<Producto>(Producto.class, allLubricants));
                    cbxProduct.removeAllItems();
                    cbxProduct.addItem(lubricante.getProducto());
                    cbxProduct.select(lubricante.getProducto());

                    System.out.println("lubricante_log " + lubricante_log.toString());
                    binder.setItemDataSource(bcrProduct.getItem(tblProduct.getValue()));
                }
            }
        });
        tblProduct.addGeneratedColumn("history", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("lubricanteprecio");  //Atributo del bean
                Property pnom = source.getItem(itemId).getItemProperty("productoNombre");  //Atributo del bean
                int id = (int) pro.getValue();
                Button btnHist = new Button("Historial");
                btnHist.setStyleName(BaseTheme.BUTTON_LINK);
                btnHist.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        getHistory(id, pnom.getValue().toString());
                    }
                });
                return btnHist;
            }
        });
        tblProduct.setVisibleColumns(new Object[]{"paisNombre", "marcaNombre", "productoNombre", "fechaInicio", "fechaFin", "precio", "history"});
        tblProduct.setColumnHeaders(new String[]{"Pais", "Marca", "Producto", "Inicio", "Fin", "Precio", "Historial"});
        tblProduct.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.RIGHT, Table.Align.CENTER);
    }

    private void buildButtons() {
        btnAdd = utils.buildButton("Agregar", FontAwesome.PLUS, ValoTheme.BUTTON_PRIMARY);
        btnAdd.setSizeUndefined();
//        btnAdd.setEnabled(cbxCountry.getValue() != null && cbxStation.getValue() != null);
        btnAdd.setEnabled(cbxCountry.getValue() != null);
        btnAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                action = DaoImp.ACTION_ADD;
                tblProduct.setValue(null);
                lubricante = new Lubricanteprecio();
                btnSave.setEnabled(true);
                binder.setItemDataSource(lubricante);
            }
        });

        btnSave = utils.buildButton("Guardar", FontAwesome.SAVE, ValoTheme.BUTTON_FRIENDLY);
        btnSave.setEnabled(false);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!cbxCountry.isValid() || !cbxBrand.isValid() || !cbxProduct.isValid()
                        || !dfdStart.isValid()
                        || !dfdEnd.isValid()
                        || tfdPrice.getValue() == null
                        || !tfdPrice.isValid()) {
                    Notification.show("Los campos marcados son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                try {
                    binder.commit();
                } catch (Exception ex) {
                    Logger.getLogger(MntLubricantPrice.class.getName()).log(Level.SEVERE, null, ex);
                }
                lubricante.setPaisId(((Pais) cbxCountry.getValue()).getPaisId());
                lubricante.setProductoId(((Producto) cbxProduct.getValue()).getProductoId());
                lubricante.setCreadoPor(user.getUsername());
                lubricante.setModificadoPor(user.getUsername());
//                lubricante_log.setModificadoPor(user.getUsername());
                SvcGeneral service = new SvcGeneral();
//                lubricante = service.doActionLubprecio(action, lubricante);

                lubricante = service.doActionLubprecio(action, lubricante, lubricante_log);
                service.closeConnections();
                if (lubricante.getLubricanteprecio() > 0) {
                    Notification notif = new Notification("¡Exito!", "La acción se ha ejecutado correctamente.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_PRICE_LUBS.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + lubricante.getDescError(), Notification.Type.ERROR_MESSAGE);
                }

//                Cliente cliente;
//                for (Integer itemId : bcrCustomer.getItemIds()) {
//                    cliente = bcrCustomer.getItem(itemId).getBean();
//                    if (cliente.getPaisId()==((Pais)cbxCountry.getValue()).getPaisId() && cliente.getCodigo().equals(tfdCode.getValue().trim()) ) {
//                        Notification.show( String.format("Para el país %s ya existe el código %s.", ((Pais)cbxCountry.getValue()).getNombre(), tfdCode.getValue().trim()), 
//                                Notification.Type.ERROR_MESSAGE);
//                        return;
//                    }
//                }
//
//                int custId = (action.equals(Dao.ACTION_ADD)) ? 0 : Integer.parseInt(tblCustomer.getValue().toString());
//                cliente = new Cliente(custId, tfdCode.getValue(), tfdName.getValue(), ((Estacion) cbxStation.getValue()).getEstacionId(), "A", user.getUsername(), new java.util.Date(), "P");
//                SvcGeneral service = new SvcGeneral();
//                boolean everythingOk = service.doActionCustomer(action, cliente);
//                service.closeConnections();
//                if (everythingOk) {
//                    Notification.show("La acción se ha ejecutado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
//                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_CUST_PREPAGO.getViewName());
//                } else {
//                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + cliente.getTipo(), Notification.Type.ERROR_MESSAGE);
//                }
            }
        });

        btnExcel = utils.buildButton("Exportar", FontAwesome.TABLE, ValoTheme.BUTTON_FRIENDLY);
        btnExcel.setDescription("Exportar a Excel");
        btnExcel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (tblProduct.isVisible()) {
                    generaExcel().export();
                }
            }
        });

        btnCarga = utils.buildButton("Cargar", FontAwesome.UPLOAD, ValoTheme.BUTTON_FRIENDLY);
        btnCarga.setDescription("Cargar desde Excel");
        btnCarga.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Marca m = (Marca) cbxBrand.getValue();
                Pais p = (Pais) cbxCountry.getValue();
                frmUploadTarjetas = new FormUploadTarjetas(p, m);
                frmUploadTarjetas.addCloseListener((e) -> {
                    bcrProduct.removeAllItems();
                    SvcGeneral service = new SvcGeneral();
                    int countryId = ((Pais) cbxCountry.getValue()).getPaisId();
                    int brandId = ((Marca) cbxBrand.getValue()).getIdMarca();
                    listProducts = service.getLubpriceByCountryidStationid(countryId, brandId);
                    allLubricants = service.getAllProductosByCountryTypeBrand(countryId, 2, brandId, false); //lubricants
                    bcrProduct.addAll(listProducts);
                    cbxProduct.setContainerDataSource(new ListContainer<Producto>(Producto.class, allLubricants));
                    service.closeConnections();
                });
                getUI().addWindow(frmUploadTarjetas);
                frmUploadTarjetas.focus();
            }
        });

    }

    private boolean filterByProperty(final String prop, final Item item, final String text) {
        if (item == null || item.getItemProperty(prop) == null || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    private void XlsxReader(File tempFile, boolean firstRowContainsHeaders) throws IOException {
        FileInputStream excelFile = new FileInputStream(tempFile);
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = datatypeSheet.iterator();
        Row currentRow = null;
        if (firstRowContainsHeaders && rowIterator.hasNext()) {
            currentRow = rowIterator.next();    //move cursor
        }

        List<String> insertList = new ArrayList();
        line = 1;
        while (rowIterator.hasNext()) {
            line++;
            currentRow = rowIterator.next();
            if (currentRow.getCell(0) != null && !currentRow.getCell(0).toString().trim().isEmpty()) {
                insertList.add("INSERT INTO lubricanteprecio (lubricanteprecio, pais_id, producto_id, fecha_inicio, fecha_fin, precio, creado_por) "
                        + "VALUES (lubricanteprecio_seq.NEXTVAL, " + currentRow.getCell(1).toString() + ", " + currentRow.getCell(5).toString() + ", "
                        + "TO_DATE('" + currentRow.getCell(7).toString() + "', 'dd/mm/yyyy'), TO_DATE('" + currentRow.getCell(8).toString() + "', 'dd/mm/yyyy'), "
                        + "" + currentRow.getCell(9).toString() + ", '" + user.getUsername() + "')");
            }
        }
        workbook.close();
        excelFile.close();
        this.tempFile.delete();
        if (tempFile.exists()) {
            tempFile.delete();
        }

        if (!insertList.isEmpty()) {
            SvcGeneral service = new SvcGeneral();
            String result = service.doBulkInsert(insertList);
            service.closeConnections();
            if (result.matches("\\d+") && Integer.parseInt(result) > 0) {
                Notification notif = new Notification("¡EXITO!", "Acción finalizada con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                notif.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
                notif.setDelayMsec(3000);
                notif.setPosition(Position.MIDDLE_CENTER);
                notif.show(Page.getCurrent());
                UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_CUST_CREDITO.getViewName());
            } else {
                Notification.show("Ocurrió un error al insertar los datos: \n" + result, Notification.Type.ERROR_MESSAGE);
            }
        }

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new DaoImp();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnAdd.setEnabled(acceso.isAgregar());
        btnSave.setEnabled(acceso.isCambiar());
        btnExcel.setEnabled(acceso.isAgregar());
        btnCarga.setEnabled(acceso.isAgregar());
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void getHistory(int id, String descproduct) {
        System.out.println("getHItory");
        VerticalLayout v = new VerticalLayout();
        v.setSizeFull();
        v.setSizeUndefined();
        v.setMargin(true);
        v.addStyleName("dashboard-view");
        v.addComponent(utils.buildHeader(descproduct, false, true));
        v.addComponent(utils.buildSeparator());
        BeanContainer<Integer, Lubricanteprecio> bcrProductH = new BeanContainer<Integer, Lubricanteprecio>(Lubricanteprecio.class);
        SvcGeneral service = new SvcGeneral();
        List<Lubricanteprecio> listProductsH = service.getLubpriceHistory(id);
        service.closeConnections();
        bcrProductH.setBeanIdProperty("lubricanteprecio");
        bcrProductH.removeAllItems();
        bcrProductH.addAll(new ArrayList());
        bcrProductH.addAll(listProductsH);

        Table tblProductH = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId, Object colId, Property property) {
                if (colId.equals("precio")) {
                    return Constant.decimal2D.format(property.getValue()).trim();
                } else if (colId.equals("fechaInicio") || colId.equals("fechaFin") || colId.equals("modificadoEl")) {
                    return Constant.SDF_yyyyMMdd.format(property.getValue()).trim();
                }
                return super.formatPropertyValue(rowId, colId, property);
            }
        };
        tblProductH.setHeight(400, Unit.PERCENTAGE);
        tblProductH.setWidth(500, Unit.PERCENTAGE);
        tblProductH.setContainerDataSource(bcrProductH);
        tblProductH.addStyleName(ValoTheme.TABLE_COMPACT);
        tblProductH.addStyleName(ValoTheme.TABLE_SMALL);
        tblProductH.setSizeUndefined();
        tblProductH.setVisibleColumns(new Object[]{"fechaInicio", "fechaFin", "precio", "modificadoPor", "modificadoEl"});
        tblProductH.setColumnHeaders(new String[]{"Inicio", "Fin", "Precio", "Modificado por", "Modificado el"});
        tblProductH.setColumnAlignments(Table.Align.CENTER, Table.Align.CENTER, Table.Align.LEFT, Table.Align.CENTER, Table.Align.CENTER);
        v.addComponent(tblProductH);
        Window window = new Window("Historico");
        window.setWidth(500, Unit.PIXELS);
        window.setHeight(500, Unit.PIXELS);
        window.center();
        window.setContent(v);
        this.getUI().getUI().addWindow(window);
    }

    public void enabledField(boolean enabled) {
        cbxCountry.setEnabled(enabled);
        cbxProduct.setEnabled(enabled);
        cbxBrand.setEnabled(enabled);
    }

    public ExcelExport generaExcel() {
        TableHolder tableHolder = new DefaultTableHolder(tblProduct);
        ExcelExport excelExport = new ExcelExport(tableHolder);
        excelExport.excludeCollapsedColumns();
        excelExport.setReportTitle("LubricantesPrecio");
        excelExport.setExportFileName("LubricantesPrecio" + ".xls");
        return excelExport;
    }

    private void uploadTarjetas() {
        if (tblProduct.isVisible()) {
            Pais pais = (Pais) cbxCountry.getValue();
            Marca marca = (Marca) cbxBrand.getValue();
            frmUploadTarjetas = new FormUploadTarjetas(pais, marca);
            frmUploadTarjetas.addCloseListener((e) -> {
                //buscar.click();
            });
            getUI().addWindow(frmUploadTarjetas);
            frmUploadTarjetas.focus();
        }
    }

}
