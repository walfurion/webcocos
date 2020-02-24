package com.fundamental.view;

import com.fundamental.model.Cliente;
import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.sisintegrados.dao.Dao;
import com.fundamental.services.SvcConfBombaEstacion;
import com.fundamental.services.SvcGeneral;
import com.sisintegrados.daoimp.DaoImp;
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
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
public class MntCustPrepago extends Panel implements View {

    Button btnSave, btnAdd, btnFilterClear;
    ComboBox cbxCountry, cbxStation;
    Table tblCustomer;
    TextField tfdName, tfdCode, tfdFilter;

    BeanContainer<Integer, Cliente> bcrCustomer = new BeanContainer<Integer, Cliente>(Cliente.class);

    List<Pais> listCountries = new ArrayList();
    List<Estacion> listStations = new ArrayList();
    String action = DaoImp.ACTION_ADD;

//template
    private final VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Usuario user;

    public MntCustPrepago() {
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
        vlRoot.addComponent(utils.buildHeader("Clientes prepago", false, true));
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
        vlLeft.setSpacing(true);
        vlLeft.setSizeUndefined();
        vlLeft.setId("vlLeft");
        vlLeft.setComponentAlignment(filtering, Alignment.MIDDLE_CENTER);
        vlLeft.setComponentAlignment(btnAdd, Alignment.MIDDLE_CENTER);
        Responsive.makeResponsive(vlLeft);

        VerticalLayout vlRight = new VerticalLayout(cbxCountry, cbxStation, tfdCode, tfdName, btnSave);
        vlRight.setSpacing(true);
        vlRight.setSizeUndefined();
        vlRight.setId("vlRight");
        vlRight.setComponentAlignment(btnSave, Alignment.MIDDLE_CENTER);
        Responsive.makeResponsive(vlRight);

        CssLayout cltTables = new CssLayout(utils.vlContainer(vlLeft), utils.vlContainer(vlRight));
        cltTables.setId("cltTables");
        cltTables.setSizeUndefined();
        Responsive.makeResponsive(cltTables);

        vlRoot.addComponents(cltTables);
        vlRoot.setExpandRatio(cltTables, 1);
    }

    private void getAllData() {
        bcrCustomer.setBeanIdProperty("clienteId");
        SvcGeneral service = new SvcGeneral();
        listCountries = service.getAllPaises();
        bcrCustomer.addAll(service.getCustomersByStationidType("P")); //Prepago
//        service.closeConnections(); //ASG
    }

    private void buildControls() {
        tfdCode = utils.buildTextField("Código:", "", false, 6, true, ValoTheme.TEXTFIELD_SMALL);

        tfdName = utils.buildTextField("Nombre:", "", false, 75, true, ValoTheme.TEXTFIELD_SMALL);

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
                                || filterByProperty("nombre", item, event.getText());
                    }
                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        return propertyId.equals("paisNombre")
                                || propertyId.equals("estacionNombre")
                                || propertyId.equals("codigo")
                                || propertyId.equals("nombre");
                    }
                });
            }
        });

        cbxCountry = utils.buildCombobox("Pais:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<Pais>(Pais.class, listCountries));
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

        cbxStation = utils.buildCombobox("Estación:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<Estacion>(Estacion.class, listStations));
        
        tblCustomer = utils.buildTable("Clientes prepago:", 200, 450, bcrCustomer, new String[]{"nombre"}, new String[]{"Nombre"});
        tblCustomer.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
//                        btnEliminar.click();
                    }
                });
                return btnDelete;
            }
        });
        tblCustomer.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (tblCustomer.getValue() != null) {
                    action = DaoImp.ACTION_UPDATE;
                    int itemid = Integer.parseInt(tblCustomer.getValue().toString());
                    Cliente cliente = bcrCustomer.getItem(itemid).getBean();
                    tfdCode.setValue(cliente.getCodigo());
                    tfdName.setValue(cliente.getNombre());
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
                }
            }
        });
        tblCustomer.addGeneratedColumn("colEstado", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Label result  = new Label();
                String estado = bcrCustomer.getItem(itemId).getBean().getEstado();
                result.setValue(estado.equals("A")?"Activo":"Inactivo");
                result.setWidth("75px");
                return result;
            }
        });
        tblCustomer.setVisibleColumns(new Object[]{"paisNombre", "estacionNombre", "codigo", "nombre","colEstado", "colDelete"});
        tblCustomer.setColumnHeaders(new String[]{"Pais", "Estacion", "Código", "Nombre","Estado", "Borrar"});
        tblCustomer.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.CENTER);
        tblCustomer.setSizeUndefined();
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
                cbxCountry.setValue(null);
                cbxStation.setValue(null);
            }
        });

        btnSave = utils.buildButton("Guardar", FontAwesome.SAVE, ValoTheme.BUTTON_FRIENDLY);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!cbxCountry.isValid() || !cbxStation.isValid() 
                        || !tfdCode.isValid() || tfdCode.getValue().trim().isEmpty()
                        || !tfdName.isValid() || tfdName.getValue().trim().isEmpty()) {
                    Notification.show("Los campos marcados son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                Cliente cliente;
                for (Integer itemId : bcrCustomer.getItemIds()) {
                    cliente = bcrCustomer.getItem(itemId).getBean();
                    if (cliente.getPaisId()==((Pais)cbxCountry.getValue()).getPaisId() && cliente.getCodigo().equals(tfdCode.getValue().trim()) ) {
                        Notification.show(String.format("Para el país %s ya existe el código %s.", ((Pais)cbxCountry.getValue()).getNombre(), tfdCode.getValue().trim()), 
                                Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }

                int custId = (action.equals(DaoImp.ACTION_ADD)) ? 0 : Integer.parseInt(tblCustomer.getValue().toString());
                cliente = new Cliente(custId, tfdCode.getValue(), tfdName.getValue(), ((Estacion) cbxStation.getValue()).getEstacionId(), "A", user.getUsername(), new java.util.Date(), "P", null, null);
                SvcGeneral service = new SvcGeneral();
                boolean everythingOk = service.doActionCustomer(action, cliente);
//                service.closeConnections(); //ASG
                if (everythingOk) {
                    Notification.show("La acción se ha ejecutado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_CUST_PREPAGO.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + cliente.getTipo(), Notification.Type.ERROR_MESSAGE);
                }
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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
