package com.fundamental.view;

import com.fundamental.model.Acceso;
import com.fundamental.model.Cliente;
import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoGenericBean;
import com.sisintegrados.dao.Dao;
import com.fundamental.services.SvcConfBombaEstacion;
import com.fundamental.services.SvcGeneral;
import com.fundamental.utils.Constant;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.view.form.FormUploadClientes;
import com.vaadin.addon.tableexport.DefaultTableHolder;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.addon.tableexport.TableHolder;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
public class MntCustCredito extends Panel implements View {
    Button btnSave, btnAdd, btnFilterClear, btnExcel, btnCarga;
    Acceso acceso = new Acceso();
    ComboBox cbxCountry, cbxStation, cbxStatus, cbxType;
    Table tblCustomer;
    TextField tfdName, tfdCode, tfdFilter, tfdCodigoEnvoy, tfdCedulaJuridica;
    Upload upload;

    BeanContainer<Integer, Cliente> bcrCustomer = new BeanContainer<Integer, Cliente>(Cliente.class);

    List<Pais> listCountries = new ArrayList();
    List<Estacion> listStations = new ArrayList();
    List<DtoGenericBean> listStatus = Arrays.asList(new DtoGenericBean("A", "Activo"), new DtoGenericBean("I", "Inactivo"));
    List<DtoGenericBean> listType = Arrays.asList(new DtoGenericBean("C", "Crédito"), new DtoGenericBean("P", "Prepago"));
    String tmpString, action = DaoImp.ACTION_ADD;
    File tempFile;
    int line;

//template
    private final VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Usuario user;
    
    FormUploadClientes formUploadClientes;

    public MntCustCredito() {
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
        vlRoot.addComponent(utils.buildHeader("Clientes crédito/prepago", false, true));
        vlRoot.addComponent(utils.buildSeparator());
        getAllData();
//template
        buildControls();
        buildButtons();

        CssLayout filtering = new CssLayout();
        filtering.addComponents(tfdFilter, btnFilterClear);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filtering.setSizeUndefined();

        VerticalLayout vlLeft = new VerticalLayout(filtering, tblCustomer, btnAdd);
        vlLeft.setSpacing(false);
        vlLeft.setSizeUndefined();
        vlLeft.setId("vlLeft");
        vlLeft.setComponentAlignment(filtering, Alignment.MIDDLE_CENTER);
        vlLeft.setComponentAlignment(btnAdd, Alignment.MIDDLE_CENTER);
        Responsive.makeResponsive(vlLeft);

        VerticalLayout vlRight = new VerticalLayout(cbxCountry, cbxStation, tfdName, cbxType, tfdCode, tfdCodigoEnvoy, tfdCedulaJuridica, cbxStatus, btnSave,btnExcel,btnCarga);
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

        CssLayout cltTables = new CssLayout(utils.vlContainer(vlRight), utils.vlContainer(vlLeft));
        cltTables.setId("cltTables");
        cltTables.setSizeUndefined();
        Responsive.makeResponsive(cltTables);

//        vlRoot.addComponents(cltTables, utils.vlContainer(upload));
        vlRoot.addComponents(cltTables);
        vlRoot.setExpandRatio(cltTables, 1);

        if (bcrCustomer.size() > 0) {
            tblCustomer.setValue(bcrCustomer.getItemIds().get(0));
        } else {
            btnAdd.click();
        }
    }

    private void getAllData() {
        bcrCustomer.setBeanIdProperty("clienteId");
        SvcGeneral service = new SvcGeneral();
        listCountries = service.getAllPaises();
        bcrCustomer.addAll(service.getCustomersByStationidType(null)); //todos
//        bcrCustomer.addAll(service.getCustomersByStationidType("P")); //Prepago
//        service.closeConnections(); //ASG
    } 

    private void buildControls() {
        tfdCode = utils.buildTextField("Código E1:", "", false, 6, true, ValoTheme.TEXTFIELD_SMALL);

        tfdName = utils.buildTextField("Nombre:", "", false, 75, true, ValoTheme.TEXTFIELD_SMALL);

        tfdCodigoEnvoy = utils.buildTextField("Código envoy:", "", false, 10, false, ValoTheme.TEXTFIELD_SMALL);
        
        tfdCedulaJuridica = utils.buildTextField("Tax id:", "", false, 10, true, ValoTheme.TEXTFIELD_SMALL);

        btnFilterClear = new Button(FontAwesome.TIMES);
        btnFilterClear.addStyleName(ValoTheme.BUTTON_SMALL);
        btnFilterClear.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                tfdFilter.setValue("");
                ((Filterable) tblCustomer.getContainerDataSource()).removeAllContainerFilters();
            }
        });
        tfdFilter = utils.buildTextFilter("Filtro búsqueda");
        tfdFilter.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        tfdFilter.addShortcutListener(new ShortcutListener("Clear",
                ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                tfdFilter.setValue("");
                ((Filterable) tblCustomer.getContainerDataSource()).removeAllContainerFilters();
            }
        });
        tfdFilter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                Container.Filterable data = (Container.Filterable) tblCustomer.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Container.Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId, final Item item) {
                        if (event.getText() == null || event.getText().equals("")) {
                            return true;
                        }
                        return filterByProperty("paisNombre", item, event.getText())
                                || filterByProperty("estacionNombre", item, event.getText())
                                || filterByProperty("codigo", item, event.getText())
                                || filterByProperty("nombre", item, event.getText())
                                || filterByProperty("estado", item, "INACTIVO".equals(event.getText().toUpperCase())?"I":("ACTIVO".equals(event.getText().toUpperCase())?"A":event.getText())) ;
                    }
                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        return propertyId.equals("paisNombre")
                                || propertyId.equals("estacionNombre")
                                || propertyId.equals("codigo")
                                || propertyId.equals("estado")
                                || propertyId.equals("nombre");
                    }
                });
            }
        });

        cbxCountry = utils.buildCombobox("País:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<Pais>(Pais.class, listCountries));
        cbxCountry.setItemIconPropertyId("flag");
        cbxCountry.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                listStations = new ArrayList();
                cbxStation.removeAllItems();
                if (cbxCountry.getValue() != null) {
                    SvcConfBombaEstacion service = new SvcConfBombaEstacion();
                    listStations = service.getStationsByCountry(((Pais) cbxCountry.getValue()).getPaisId(), true);
//                    service.closeConnections(); //ASG
                    cbxStation.setContainerDataSource(new ListContainer<Estacion>(Estacion.class, listStations));
                }
            }
        });

        cbxStation = utils.buildCombobox("Estación:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(Estacion.class, listStations));
        
        cbxStatus = utils.buildCombobox("Estado:", "name", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(DtoGenericBean.class, listStatus));
        
        cbxType = utils.buildCombobox("Tipo:", "name", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(DtoGenericBean.class, listType));

        tblCustomer = utils.buildTable("Clientes:", 200, 450, bcrCustomer, new String[]{"nombre"}, new String[]{"Nombre"});
        tblCustomer.addStyleName(ValoTheme.TABLE_COMPACT);
        tblCustomer.addStyleName(ValoTheme.TABLE_SMALL);
        tblCustomer.setSizeUndefined();
        tblCustomer.setHeight("475px");
        tblCustomer.setVisibleColumns(new Object[]{"paisNombre", "estacionNombre", "tipo", "codigo", "codigoEnvoy", "estado", "nombre"});
        tblCustomer.setColumnHeaders(new String[]{"Pais", "Estacion", "Tipo", "Código E1", "Código envoy", "Estado", "Nombre"});
        tblCustomer.setColumnAlignment("codigo", Table.Align.RIGHT);
        tblCustomer.setColumnAlignment("estado", Table.Align.CENTER);
        tblCustomer.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (tblCustomer.getValue() != null) {
                    action = DaoImp.ACTION_UPDATE;
                    int itemid = Integer.parseInt(tblCustomer.getValue().toString());
                    Cliente cliente = bcrCustomer.getItem(itemid).getBean();
                    tfdCode.setValue(cliente.getCodigo());
                    tfdName.setValue(cliente.getNombre());
                    tfdCodigoEnvoy.setValue(cliente.getCodigoEnvoy());
                    tfdCedulaJuridica.setValue(cliente.getCedulaJuridica());
                    for (Pais p : listCountries) {
                        if (p.getPaisId() == cliente.getPaisId()) {
                            cbxCountry.setValue(p);
                            break;
                        }
                    }
                    for (Estacion e : listStations) {
                        if (e.getEstacionId() == cliente.getEstacionId()) {
                            cbxStation.setValue(e);
                            break;
                        }
                    }
                    for (DtoGenericBean dgb : listStatus) {
                        if (dgb.getStringId().equals("Activo".equals(cliente.getEstado())?"A":"I")) {
                            cbxStatus.setValue(dgb);
                            break;
                        }
                    }
                    for (DtoGenericBean dgb : listType) {
                        if (dgb.getStringId().equals("Prepago".equals(cliente.getTipo())?"P":"C")) {
                            cbxType.setValue(dgb);
                            break;
                        }
                    }
                }
            }
        });
        
        upload = new Upload("Selección de archivo:", new Upload.Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                if (filename.isEmpty() || !filename.toUpperCase().contains(".XLSX")) {
                    Notification.show("Debe seleccionar un archivo y ser de formato .xlsx", Notification.Type.WARNING_MESSAGE);
                    return null;
                }
                try {
                    tempFile = File.createTempFile("tempCocoFile", ".xlsx");
                    return new FileOutputStream(tempFile);
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
                    if (tempFile != null && tempFile.exists()) {
                        XlsxReader(tempFile, true);
                    }
                } catch (Exception ex) {
                    Notification.show("Fila: " + line + "; " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        });
        upload.setButtonCaption("Cargar");
    }

    private void buildButtons() {
        btnAdd = utils.buildButton("Agregar", FontAwesome.PLUS, ValoTheme.BUTTON_PRIMARY);
        btnAdd.setSizeUndefined();
        btnAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                action = DaoImp.ACTION_ADD;
                tblCustomer.setValue(null);
                tfdCode.setValue(null);
                tfdName.setValue(null);
                tfdCodigoEnvoy.setValue(null);
                tfdCedulaJuridica.setValue(null);
                cbxCountry.setValue(null);
                cbxStation.setValue(null);
                cbxStatus.setValue(null);
                cbxType.setValue(null);
            }
        });

        btnSave = utils.buildButton("Guardar", FontAwesome.SAVE, ValoTheme.BUTTON_FRIENDLY);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                int custId = (action.equals(DaoImp.ACTION_ADD)) ? 0 : Integer.parseInt(tblCustomer.getValue().toString());
                if (!cbxCountry.isValid() || !cbxStation.isValid() 
                        || !tfdCode.isValid() || tfdCode.getValue().trim().isEmpty()
                        || !tfdName.isValid() || tfdName.getValue().trim().isEmpty()
//                        || !tfdCodigoEnvoy.isValid() || tfdCodigoEnvoy.getValue().trim().isEmpty()
                        || !tfdCedulaJuridica.isValid() || tfdCedulaJuridica.getValue().trim().isEmpty()
                        ) {
                    Notification.show("Los campos marcados son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                Cliente cliente;
                for (Integer itemId : bcrCustomer.getItemIds()) {
                    cliente = bcrCustomer.getItem(itemId).getBean();
                    if (custId!=cliente.getClienteId() && cliente.getPaisId()==((Pais)cbxCountry.getValue()).getPaisId() && cliente.getCodigo().equals(tfdCode.getValue().trim()) ) {
                        Notification.show(String.format("Para el país %s ya existe el código %s.", ((Pais)cbxCountry.getValue()).getNombre(), tfdCode.getValue().trim()), 
                                Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }

                
                tmpString = ((DtoGenericBean)cbxType.getValue()).getStringId();
                cliente = new Cliente(custId, tfdCode.getValue(), tfdName.getValue(), ((Estacion) cbxStation.getValue()).getEstacionId(), ((DtoGenericBean) cbxStatus.getValue()).getStringId(), user.getUsername(), new java.util.Date(), tmpString, tfdCodigoEnvoy.getValue(), tfdCedulaJuridica.getValue());
                SvcGeneral service = new SvcGeneral();
                if (action.equals(DaoImp.ACTION_ADD) && service.existeCodEnvoy(cliente.getCodigoEnvoy())) {
                    Notification.show("El código ENVOY ingresado ya fue utilizado en otro cliente.");
//                    service.closeConnections(); //ASG
                    return;
                }
                boolean everythingOk = service.doActionCustomer(action, cliente);
//                service.closeConnections(); //ASG
                if (everythingOk) {
                    Notification.show("La acción se ha ejecutado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_CUST_CREDITO.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + cliente.getTipo(), Notification.Type.ERROR_MESSAGE);
                }
            }
        });
        
        btnExcel = utils.buildButton("Exportar", FontAwesome.TABLE, ValoTheme.BUTTON_FRIENDLY);
        btnExcel.setDescription("Exportar a Excel");
        btnExcel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (tblCustomer.isVisible()) {
                    generaExcel().export();
                }
            }
        });
        
        btnCarga = utils.buildButton("Cargar", FontAwesome.UPLOAD, ValoTheme.BUTTON_FRIENDLY);
        btnCarga.setDescription("Cargar desde Excel");
        btnCarga.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                formUploadClientes = new FormUploadClientes();
                formUploadClientes.addCloseListener((e) -> {
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_CUST_CREDITO.getViewName());
                });
                getUI().addWindow(formUploadClientes);
                formUploadClientes.focus();
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

    public void XlsxReader(File tempFile, boolean firstRowContainsHeaders) throws IOException {
        FileInputStream excelFile = new FileInputStream(tempFile);
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = datatypeSheet.iterator();
        Row currentRow = null;
        if (firstRowContainsHeaders && rowIterator.hasNext()) {
            currentRow = rowIterator.next();    //move cursor
        }
        
        SvcGeneral service = new SvcGeneral();
        Map<String, Integer> stationsMap = service.getStationsMap();

        List<String> insertList = new ArrayList();
        line = 1;
        while (rowIterator.hasNext()) {
            line++;
            currentRow = rowIterator.next();
            if (currentRow.getCell(0) != null && !currentRow.getCell(0).toString().trim().isEmpty()) {
                insertList.add(String.format("INSERT INTO cliente (cliente_id, codigo, nombre, estacion_id, creado_por, tipo, codigo_envoy, cedula_juridica, estado) "
                        + "VALUES (cliente_seq.NEXTVAL, '%d', '%s', %d, '%s', '%s', '%s', '%s', '%s')",  
                        (int)currentRow.getCell(3).getNumericCellValue(),
                        currentRow.getCell(5).toString(),
//                        (int)currentRow.getCell(1).getNumericCellValue(),
                        stationsMap.get( Integer.toString((int)currentRow.getCell(1).getNumericCellValue()) ),
                        user.getUsername(),
                        currentRow.getCell(6).toString(),
                        currentRow.getCell(4).toString(),
                        currentRow.getCell(7).toString(),
                        currentRow.getCell(8).toString()
                        ));
            }
        }
        workbook.close();
        excelFile.close();
        this.tempFile.delete();
        if (tempFile.exists()) {
            tempFile.delete();
        }

        if (!insertList.isEmpty()) {
            String result = service.doBulkInsert(insertList);
//            service.closeConnections(); //ASG
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
//            service.closeConnections(); //ASG
        }

    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new DaoImp();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnAdd.setEnabled(acceso.isAgregar());
        btnSave.setEnabled(acceso.isCambiar());
    }

     public ExcelExport generaExcel() {
        TableHolder tableHolder = new DefaultTableHolder(tblCustomer);
        ExcelExport excelExport = new ExcelExport(tableHolder);
        excelExport.excludeCollapsedColumns();
        excelExport.setReportTitle("Clientes");
        excelExport.setExportFileName("Clientes-"+Constant.SDF_ddMMyyyyS.format(new Date()) + ".xls");
        return excelExport;
    }
}
