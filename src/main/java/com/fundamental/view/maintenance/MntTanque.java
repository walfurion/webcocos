/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.view.maintenance;

import com.fundamental.model.Acceso;
import com.fundamental.model.Producto;
import com.fundamental.model.Utils;
import com.fundamental.services.Dao;
import com.fundamental.services.SvcMaintenance;
import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.Tanque;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
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

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.maddon.ListContainer;

/**
 *
 * @author jjosue
 */
public class MntTanque extends Panel implements View {

    Button btnAdd, btnSave, btnFilterClear;
    TextField tfdFilter;
    Table tblTanque;
    //Table tblPais;
    BeanContainer<Integer, Tanque> bcrTanque = new BeanContainer<>(Tanque.class);
    //BeanContainer<Integer, Pais> bcrPais = new BeanContainer<>(Pais.class);
    Producto product;
    Tanque tanque;

    @PropertyId("producto")
    ComboBox cbxProducto;
    @PropertyId("estacion")
    ComboBox cbxEstacion;
    @PropertyId("descripcion")
    TextField txtDescripcion;

    BeanFieldGroup<Tanque> binder = new BeanFieldGroup<Tanque>(Tanque.class);
    //List<DtoGenericBean> listStatus = Arrays.asList(new DtoGenericBean("A", "Activo"), new DtoGenericBean("I", "Inactivo"));
    //List<Marca> listBrands = new ArrayList();
    List<Producto> listproducto = new ArrayList();
    List<Estacion> listestacion = new ArrayList();
    String action;
    Acceso acceso = new Acceso();
//template
    private VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Usuario user;

    public MntTanque() {
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
        vlRoot.addComponent(utils.buildHeader("Tanques", false, true));
        vlRoot.addComponent(utils.buildSeparator());
        getAllData();
//template
        buildFields();
        buildTable();
        // buildTablePaises();
        buildButtons();

        CssLayout filtering = new CssLayout();
        filtering.addComponents(tfdFilter, btnFilterClear);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filtering.setSizeUndefined();

        VerticalLayout vlRight = new VerticalLayout(//utils.vlContainer(cbxPais), utils.vlContainer(cbxEstacion), 
                filtering, tblTanque, btnAdd);
        vlRight.setSizeUndefined();
        vlRight.setId("vlRight");
        vlRight.setComponentAlignment(filtering, Alignment.TOP_CENTER);
        vlRight.setComponentAlignment(btnAdd, Alignment.TOP_CENTER);
        Responsive.makeResponsive(vlRight);

        VerticalLayout vlLeft = new VerticalLayout(cbxProducto, cbxEstacion, txtDescripcion, btnSave);
        vlLeft.setSizeUndefined();
        vlLeft.setId("vlLeft");
        vlLeft.setComponentAlignment(btnSave, Alignment.BOTTOM_CENTER);
        vlLeft.setMargin(new MarginInfo(false, true, false, false));
        Responsive.makeResponsive(vlLeft);

//        VerticalLayout vlLeft2 = new VerticalLayout(tblPais);
//        vlLeft2.setSizeUndefined();
//        vlLeft2.setId("vlLeft");
//        vlLeft2.setMargin(new MarginInfo(false, true, false, false));
//        Responsive.makeResponsive(vlLeft2);
        CssLayout cltTables = new CssLayout(vlLeft, vlRight);
        cltTables.setId("cltTables");
        cltTables.setSizeUndefined();
        Responsive.makeResponsive(cltTables);

        vlRoot.addComponents(cltTables);
        vlRoot.setExpandRatio(cltTables, 1);

        if (bcrTanque.size() > 0) {
            tblTanque.setValue(bcrTanque.getItemIds().get(0));
        } else {
            btnAdd.click();
        }
    }

    private void getAllData() {
        bcrTanque.setBeanIdProperty("idtanque");
        //bcrPais.setBeanIdProperty("paisId");
        SvcMaintenance service = new SvcMaintenance();
        bcrTanque.addAll(service.getAllTanques());
        //bcrPais.addAll(service.getAllPaises());

        listproducto = service.getProductsToTanques();
        listestacion = service.getEstacionToTanques();
        service.closeConnections();
    }

    private void buildFields() {

        cbxProducto = utils.buildCombobox("Producto:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(Producto.class, listproducto));
        cbxEstacion = utils.buildCombobox("Estación:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(Estacion.class, listestacion));
        txtDescripcion = utils.buildTextField("Descripción:", "", false, 95, true, ValoTheme.TEXTFIELD_SMALL);

        binder.bindMemberFields(this);
        binder.setItemDataSource(tanque);

        btnFilterClear = new Button(FontAwesome.TIMES);
        btnFilterClear.addStyleName(ValoTheme.BUTTON_SMALL);
        btnFilterClear.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                tfdFilter.setValue("");
                ((Container.Filterable) tblTanque.getContainerDataSource()).removeAllContainerFilters();
            }
        });
        tfdFilter = utils.buildTextFilter("Filtro búsqueda");
        tfdFilter.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        tfdFilter.addShortcutListener(new ShortcutListener("Clear",
                ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                tfdFilter.setValue("");
                ((Filterable) tblTanque.getContainerDataSource()).removeAllContainerFilters();
            }
        });
        tfdFilter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                Container.Filterable data = (Container.Filterable) tblTanque.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Container.Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId, final Item item) {
                        if (event.getText() == null || event.getText().equals("")) {
                            return true;
                        }
                        return utils.filterByProperty("descProducto", item, event.getText())
                                || utils.filterByProperty("descEstacion", item, event.getText())
                                || utils.filterByProperty("descripcion", item, event.getText());
                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        return propertyId.equals("descProducto")
                                || propertyId.equals("descEstacion") || propertyId.equals("descripcion");
                    }
                });
            }
        });

    }

    private void buildTable() {
        tblTanque = utils.buildTable("Tanques:", 100f, 250f, bcrTanque, new Object[]{"descProducto", "descEstacion", "descripcion"}, new String[]{"Producto", "Estación", "Descripción"});
        tblTanque.setSizeUndefined();
        tblTanque.addStyleName(ValoTheme.TABLE_COMPACT);
        tblTanque.addStyleName(ValoTheme.TABLE_SMALL);
        tblTanque.setColumnWidth("descripcion", 250);
        tblTanque.setColumnAlignment("descripcion", Table.Align.CENTER);
        tblTanque.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                action = Dao.ACTION_UPDATE;
                tanque = bcrTanque.getItem(tblTanque.getValue()).getBean();
                binder.setItemDataSource(tanque);
                SvcMaintenance service = new SvcMaintenance();
                int i;
                for (i = 0; i < listestacion.size(); i++) {
                    if (listestacion.get(i).getEstacionId().toString().trim().equals(bcrTanque.getItem(tblTanque.getValue()).getBean().getEstacion().getEstacionId().toString().trim())) {
                        Estacion p = new Estacion();
                        p = listestacion.get(i);
                        cbxEstacion.setValue(p);
                    }
                }
                int z;
                for (z = 0; z < listproducto.size(); z++) {
                    if (listproducto.get(z).getProductoId().toString().trim().equals(bcrTanque.getItem(tblTanque.getValue()).getBean().getProducto().getProductoId().toString().trim())) {
                        Producto p = new Producto();
                        p = listproducto.get(z);
                        cbxProducto.setValue(p);
                    }
                }

                service.closeConnections();
            }
        });

    }

    private void buildButtons() {
        btnAdd = new Button("Agregar", FontAwesome.PLUS);
        btnAdd.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAdd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        btnAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                action = Dao.ACTION_ADD;
                tanque = new Tanque();
                binder.setItemDataSource(tanque);
                SvcMaintenance service = new SvcMaintenance();
                service.closeConnections();
            }
        });

        btnSave = new Button("Guardar", FontAwesome.SAVE);
        btnSave.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSave.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    binder.commit();
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(MntProducto.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!binder.isValid()) {
                    Notification.show("Por favor, todos los campos son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                tanque.setEstacion(((Estacion) cbxEstacion.getValue()));
                tanque.setProducto((Producto) cbxProducto.getValue());
                tanque.setDescripcion(txtDescripcion.getValue());
                tanque.setUsuarioCreacion(user.getUsername());
                tanque.setUsuarioModificacion(user.getUsername());
                SvcMaintenance service = new SvcMaintenance();

                service.doActionTanque(action, tanque);
                service.closeConnections();

                if (tanque != null) {
                    service.closeConnections();
                    Notification notif = new Notification("¡Exito!", "La acción se ha ejecutado correctamente.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_TANQUE.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + product.getDescError(), Notification.Type.ERROR_MESSAGE);
                }
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new Dao();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnAdd.setEnabled(acceso.isAgregar());
        btnSave.setEnabled(acceso.isCambiar());
    }

}
