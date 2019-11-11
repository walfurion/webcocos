package com.fundamental.view.maintenance;

import com.fundamental.model.Acceso;
import com.fundamental.model.Marca;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Producto;
import com.fundamental.model.Tipoproducto;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoGenericBean;
import com.fundamental.services.Dao;
import com.fundamental.services.SvcMaintenance;
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
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
public class MntProducto extends Panel implements View {

    Button btnAdd, btnSave, btnFilterClear;
    TextField tfdFilter;
    Table tblProduct;
    Table tblPais;
    BeanContainer<Integer, Producto> bcrProduct = new BeanContainer<>(Producto.class);
    BeanContainer<Integer, Pais> bcrPais = new BeanContainer<>(Pais.class);
    Producto product;

    @PropertyId("nombre")
    TextField tfdName;
    @PropertyId("codigo")
    TextField tfdCode;
    @PropertyId("typeProd")
    ComboBox cbxTypeProd;
    @PropertyId("sku")
    TextField tfdSku;
    @PropertyId("ordenPos")
    TextField tfdOrder;
    @PropertyId("status")
    ComboBox cbxStatus;
    @PropertyId("codigoNumerico")
    TextField tfdNumericCode;
    @PropertyId("presentacion")
    TextField tfdPresentation;
    @PropertyId("codigoBarras")
    TextField tfdBarCode;
    @PropertyId("marca")
    ComboBox cbxBrand;
    @PropertyId("codigoEnvoy")
    TextField tfdEnvoyCode;
    
    BeanFieldGroup<Producto> binder = new BeanFieldGroup<Producto>(Producto.class);
    List<DtoGenericBean> listStatus = Arrays.asList(new DtoGenericBean("A", "Activo"), new DtoGenericBean("I", "Inactivo"));
    List<Marca> listBrands = new ArrayList();
    List<Tipoproducto> listTipoproducto = new ArrayList();
    String action;
    Acceso acceso = new Acceso();     
//template
    private VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Usuario user;

    public MntProducto() {
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
        vlRoot.addComponent(utils.buildHeader("Productos", false, true));
        vlRoot.addComponent(utils.buildSeparator());
        getAllData();
//template
        buildFields();
        buildTable();
        buildTablePaises();
        buildButtons();

        CssLayout filtering = new CssLayout();
        filtering.addComponents(tfdFilter, btnFilterClear);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filtering.setSizeUndefined();

        VerticalLayout vlRight = new VerticalLayout(//utils.vlContainer(cbxPais), utils.vlContainer(cbxEstacion), 
                filtering, tblProduct, btnAdd);
        vlRight.setSizeUndefined();
        vlRight.setId("vlRight");
        vlRight.setComponentAlignment(filtering, Alignment.TOP_CENTER);
        vlRight.setComponentAlignment(btnAdd, Alignment.TOP_CENTER);
        Responsive.makeResponsive(vlRight);

        VerticalLayout vlLeft = new VerticalLayout(tfdName, tfdCode, cbxTypeProd, cbxBrand, tfdOrder, tfdSku, tfdNumericCode, tfdPresentation, tfdBarCode, tfdEnvoyCode, cbxStatus,tblPais, btnSave);
        vlLeft.setSizeUndefined();
        vlLeft.setId("vlLeft");
        vlLeft.setComponentAlignment(btnSave, Alignment.BOTTOM_CENTER);
        vlLeft.setMargin(new MarginInfo(false, true, false, false));
        Responsive.makeResponsive(vlLeft);
        
        VerticalLayout vlLeft2 = new VerticalLayout(tblPais);
        vlLeft2.setSizeUndefined();
        vlLeft2.setId("vlLeft");
        vlLeft2.setMargin(new MarginInfo(false, true, false, false));
        Responsive.makeResponsive(vlLeft2);

        CssLayout cltTables = new CssLayout(vlLeft,vlLeft2, vlRight);
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
        bcrProduct.setBeanIdProperty("productoId");
        bcrPais.setBeanIdProperty("paisId");
        SvcMaintenance service = new SvcMaintenance();
        bcrProduct.addAll(service.getAllProducts(true));
        bcrPais.addAll(service.getAllPaises());
        listBrands = service.getAllBrands(false);
        listTipoproducto = service.getAllTipoproducto(false);
        service.closeConnections();
    }

    private void buildFields() {
        tfdName = utils.buildTextField("Nombre:", "", false, 75, true, ValoTheme.TEXTFIELD_SMALL);
        tfdName.setWidth("350px");
        
        tfdCode = utils.buildTextField("Código E1:", "", false, 30, false, ValoTheme.TEXTFIELD_SMALL);

        cbxTypeProd = utils.buildCombobox("Tipo:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(Tipoproducto.class, listTipoproducto));

        tfdOrder = utils.buildTextField("Orden:", "", false, 3, true, ValoTheme.TEXTFIELD_SMALL);
        tfdOrder.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
        
        tfdSku = utils.buildTextField("SKU:", "", false, 13, false, ValoTheme.TEXTFIELD_SMALL);

        cbxStatus = utils.buildCombobox("Estado:", "name", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(DtoGenericBean.class, listStatus));
        
        tfdNumericCode = utils.buildTextField("Código numérico:", "", false, 8, false, ValoTheme.TEXTFIELD_SMALL);
        
        tfdPresentation = utils.buildTextField("Presentación:", "", false, 25, false, ValoTheme.TEXTFIELD_SMALL);
        
        tfdBarCode = utils.buildTextField("Código barras:", "", false, 25, false, ValoTheme.TEXTFIELD_SMALL);
        
        cbxBrand = utils.buildCombobox("Marca:", "nombre", true, false, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(Marca.class, listBrands));
        
        tfdEnvoyCode = utils.buildTextField("Código envoy:", "", false, 10, false, ValoTheme.TEXTFIELD_SMALL);
        
        binder.bindMemberFields(this);
        binder.setItemDataSource(product);
        
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
        tfdFilter.addShortcutListener(new ShortcutListener("Clear",
                ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                tfdFilter.setValue("");
                ((Filterable) tblProduct.getContainerDataSource()).removeAllContainerFilters();
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
                        return utils.filterByProperty("nombre", item, event.getText())
                                || utils.filterByProperty("codigo", item, event.getText())
                                || utils.filterByProperty("countrys", item, event.getText());
                    }
                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        return propertyId.equals("nombre") 
                                || propertyId.equals("codigo") || propertyId.equals("countrys");
                    }
                });
            }
        });

    }

    private void buildTable() {
        tblProduct = utils.buildTable("Productos:", 100f, 250f, bcrProduct, new Object[]{"nombre", "codigo", "codigoEnvoy", "estado","countrys"}, new String[]{"Nombre", "Código E1", "Código envoy", "Estado","País"});
        tblProduct.setSizeUndefined();
        tblProduct.addStyleName(ValoTheme.TABLE_COMPACT);
        tblProduct.addStyleName(ValoTheme.TABLE_SMALL);
        tblProduct.setColumnWidth("nombre", 250);
        tblProduct.setColumnAlignment("estado", Table.Align.CENTER);
        tblProduct.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                action = Dao.ACTION_UPDATE;
                product = bcrProduct.getItem(tblProduct.getValue()).getBean();
                binder.setItemDataSource(product);
                SvcMaintenance service = new SvcMaintenance();
                bcrPais.removeAllItems();
                bcrPais.addAll(service.getAllPaisbyProduct(product.getProductoId())); 
                service.closeConnections();
            }
        });
        
    }
    
    private void buildTablePaises() {
        tblPais = utils.buildTable("Países:", 100f, 100f, bcrPais, new Object[]{"nombre"}, new String[]{"Nombre"});
        tblPais.addStyleName(ValoTheme.TABLE_COMPACT);
        tblPais.addStyleName(ValoTheme.TABLE_SMALL);
        tblPais.setSizeUndefined();
        tblPais.setHeight("240px");
        tblPais.setWidth("190px");
        tblPais.addGeneratedColumn("colSelect", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property prop = source.getItem(itemId).getItemProperty("selected");  //Atributo del bean
                CheckBox result = new CheckBox("");
                result.setPropertyDataSource(prop);
                result.addStyleName(ValoTheme.CHECKBOX_SMALL);
                return result;
            }
        });
        tblPais.setVisibleColumns(new Object[]{"nombre", "colSelect"});
        tblPais.setColumnHeaders(new String[]{"Pais", "Asociar"});
        tblPais.setColumnAlignments(new Table.Align[]{Table.Align.LEFT, Table.Align.CENTER});
    }
    
    private void buildButtons() {
        btnAdd = new Button("Agregar", FontAwesome.PLUS);
        btnAdd.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAdd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        btnAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                action = Dao.ACTION_ADD;
                product = new Producto();
                binder.setItemDataSource(product);
                SvcMaintenance service = new SvcMaintenance();
                bcrPais.removeAllItems();
                bcrPais.addAll(service.getAllPaises()); 
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
                product.setEstado(product.getStatus().getStringId());
                product.setTipoId( ((Tipoproducto)cbxTypeProd.getValue()).getTipoId() );
                Integer idMarca = (((Marca)cbxBrand.getValue()).getIdMarca()>0 ? ((Marca)cbxBrand.getValue()).getIdMarca() : null);
                product.setIdMarca( idMarca );
                product.setCreadoPor(user.getUsername());
                product.setModificadoPor(user.getUsername());
                SvcMaintenance service = new SvcMaintenance();
                if (action.equals(Dao.ACTION_ADD)&&service.existeE1Producto(product.getCodigo())) {
                     Notification.show("El código E1 ingresado ya fue utilizado por otro producto.");
                     service.closeConnections();
                     return;
                }else if (action.equals(Dao.ACTION_ADD)&&service.existeCodEnvoyProducto(product.getCodigoEnvoy())) {
                    Notification.show("El código ENVOY ingresado ya fue utilizado en otro producto.");
                    service.closeConnections();
                    return;
                }
                service.doActionProduct(action, product);
                service.closeConnections();
                List<Pais> paises = new ArrayList();
                bcrPais.getItemIds().forEach((id) -> {
                    paises.add(bcrPais.getItem(id).getBean());
                });
                if (product.getProductoId() != null) {
                    service.setProductoPais(paises, product);
                    service.closeConnections();
                    Notification notif = new Notification("¡Exito!", "La acción se ha ejecutado correctamente.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_PRODUCTO.getViewName());
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
