/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.view;

import com.fundamental.model.Acceso;
import com.fundamental.model.Lubricanteprecio;
import com.fundamental.model.Marca;
import com.fundamental.model.Producto;
import com.fundamental.model.Utils;
import com.fundamental.services.Dao;
import com.fundamental.services.SvcEstacion;
import com.fundamental.services.SvcGeneral;
import com.fundamental.services.SvcTurno;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.vaadin.maddon.ListContainer;

/**
 *
 * @author m
 */
public class MntLubricanteCV extends Panel implements View {

    CreateComponents components = new CreateComponents();
    ComboBox cmbPais = new ComboBox();
    ComboBox cmbEstacion = new ComboBox();
    ComboBox cmbMarca = new ComboBox();
    ComboBox cmbProducto = new ComboBox();
    DateField cmbFecha = new DateField("Fecha:");
    Usuario usuario = new Usuario();
    Utils utils = new Utils();

    Button btnGuardar = new Button("Crear Turno");
    Button btnModificar = new Button("Modificar Turno", FontAwesome.EDIT);
    Button btnAddEmpPump;
    Acceso acceso = new Acceso();
    BeanItemContainer<Pais> contPais = new BeanItemContainer<Pais>(Pais.class);
    BeanContainer<Integer, Lubricanteprecio> contLub = new BeanContainer<Integer, Lubricanteprecio>(Lubricanteprecio.class);
    SvcTurno dao = new SvcTurno();
    List<Producto> allLubricants = new ArrayList();
    List<Marca> listBrands = new ArrayList();
    List<Lubricanteprecio> listProducts = new ArrayList();
    Table tblProduct = new Table();
    Table tablaPrecio = new Table("Precios:");

    public MntLubricanteCV() {

        super.setLocale(VaadinSession.getCurrent().getAttribute(Locale.class));
        super.addStyleName(ValoTheme.PANEL_BORDERLESS);
        super.setSizeFull();
        DashboardEventBus.register(this);
        usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
        super.setContent(components.createVertical(Constant.styleTransactions, "100%", false, true, true, new Component[]{buildForm()}));
        cargaInfoSesion();
        buildTableContent();

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

    private Component buildForm() {
        return components.createVertical(Constant.styleLogin, "100%", false, false, true, new Component[]{buildTitle(), buildHeader(), buildToolbar2(), buildButtons()});
    }

    private Component buildTitle() {
        Label title = new Label("Compras Ventas de Lubricantes");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        Component toolBar = components.createHorizontal(Constant.styleToolbar, Constant.sizeUndefined, true, false, false, new Component[]{buildToolbar()});
        return components.createHorizontal(Constant.styleViewheader, Constant.sizeUndefined, true, false, false, new Component[]{title, toolBar});
    }

    private CssLayout toolBar2;

    private Component buildToolbar() {
        toolBar2 = new CssLayout();
        VerticalLayout v = new VerticalLayout(toolBar2);
        return components.createHorizontal(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{v});
    }
    private CssLayout toolbarContainerTables;

    private Component buildToolbar2() {
        toolbarContainerTables = new CssLayout();
        return components.createHorizontal(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{utils.vlContainer(toolbarContainerTables)});
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new Dao();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnGuardar.setEnabled(acceso.isAgregar());
        btnModificar.setEnabled(acceso.isCambiar());
    }

    private Component buildButtons() {
        HorizontalLayout footer = (HorizontalLayout) components.createHorizontal(ValoTheme.WINDOW_BOTTOM_TOOLBAR, "", true, false, false, new Component[]{btnGuardar, btnModificar});

        footer.setComponentAlignment(btnGuardar, Alignment.TOP_RIGHT);
        footer.setWidth(
                100.0f, Unit.PERCENTAGE);
        return footer;
    }

    private Component buildHeader() {
//        cargaTablaPrecios();
        SvcGeneral service = new SvcGeneral();
        listBrands = service.getAllBrands(false);
        contPais = new BeanItemContainer<Pais>(Pais.class);
        contPais.addAll(dao.getAllPaises());
        BeanItemContainer<Estacion> contEstacion = new BeanItemContainer<Estacion>(Estacion.class);
        BeanItemContainer<Producto> contProductos = new BeanItemContainer<Producto>(Producto.class);
        cmbPais = new ComboBox("País:", contPais);
        cmbPais.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        cmbPais.setItemCaptionPropertyId("nombre");
        cmbPais.setItemIconPropertyId("flag");
        cmbPais.setWidth("230px");
        cmbPais.setRequired(true);
        cmbPais.setNullSelectionAllowed(false);
        cmbPais.setRequiredError("Debe seleccionar un país");
        cmbPais.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cmbPais.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                cmbEstacion.removeAllItems();
                cmbFecha.setValue(null);
                SvcEstacion svcEstacion = new SvcEstacion();
                Pais pais = new Pais();
                pais = (Pais) cmbPais.getValue();
                contEstacion.addAll(svcEstacion.getStationsByCountryUser(pais.getPaisId(), usuario.getUsuarioId()));
                svcEstacion.closeConnections();
                cmbEstacion.setContainerDataSource(contEstacion);
                if (contEstacion.size() == 1) {
                    cmbEstacion.setValue(contEstacion.getIdByIndex(0));
                }
            }
        });

        cmbEstacion = new ComboBox("Estación:");
        cmbEstacion.setItemCaptionPropertyId("nombre");
        cmbEstacion.setNullSelectionAllowed(false);
        cmbEstacion.setFilteringMode(FilteringMode.CONTAINS);
        cmbEstacion.setRequired(true);
        cmbEstacion.setRequiredError("Debe seleccionar una estacion");
        cmbEstacion.setWidth("230px");
        cmbEstacion.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cmbEstacion.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (cmbEstacion.getValue() != null) {
//                    cargaUltTurnoDia();
                }

            }
        });

        cmbMarca = new ComboBox("Marca:");
        cmbMarca.setItemCaptionPropertyId("nombre");
        cmbMarca.setNullSelectionAllowed(false);
        cmbMarca.setFilteringMode(FilteringMode.CONTAINS);
        cmbMarca.setRequired(true);
        cmbMarca.setRequiredError("Debe seleccionar una Marca");
        cmbMarca.setWidth("230px");
        cmbMarca.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cmbMarca.setContainerDataSource(new ListContainer<>(Marca.class, listBrands));

//        cmbMarca = utils.buildCombobox("Marca:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(Marca.class, listBrands));
        cmbFecha.setWidth("120px");
        cmbFecha.setDateFormat("dd-MM-yyyy");
        cmbFecha.setRangeEnd(Date.from(Instant.now()));
        cmbFecha.setLocale(new Locale("es", "ES"));
        cmbFecha.setLenient(true);
        cmbFecha.setRequired(true);
        cmbFecha.addStyleName(ValoTheme.DATEFIELD_SMALL);
        cmbFecha.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                if (cmbFecha.getValue() != null) {
                    Dao dao = new Dao();
                    allLubricants = dao.getAllProducts();
                    cmbProducto.setContainerDataSource(new ListContainer<Producto>(Producto.class, allLubricants));
                }
            }
        });

        cmbProducto = new ComboBox("Producto:");
        cmbProducto.setItemCaptionPropertyId("nombre");
        cmbProducto.setNullSelectionAllowed(false);
        cmbProducto.setFilteringMode(FilteringMode.CONTAINS);
        cmbProducto.setRequired(true);
        cmbProducto.setRequiredError("Debe seleccionar un Producto");
        cmbProducto.setWidth("230px");
        cmbProducto.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cmbProducto.setContainerDataSource(new ListContainer<Producto>(Producto.class, allLubricants));
        cmbProducto.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                if (cmbProducto.getValue() != null) {
                    contLub.removeAllItems();
                    contLub.setBeanIdProperty("productoId");
                    toolbarContainerTables.removeAllComponents();
                    toolbarContainerTables.addComponent(buildTableContent());
                    SvcGeneral service = new SvcGeneral();
                    int countryId = ((Pais) cmbPais.getValue()).getPaisId();
                    int brandId = ((Marca) cmbMarca.getValue()).getIdMarca();
                    int productId = ((Producto) cmbProducto.getValue()).getProductoId();
                    listProducts = service.getLubpriceByProduct(countryId, brandId, productId);
                    allLubricants = service.getAllProductosByCountryTypeBrand(countryId, 2, brandId, false); //lubricants  
                    contLub.addAll(listProducts);
                }
            }
        });

        Component toolBar = components.createCssLayout(Constant.styleToolbar, Constant.sizeFull, false, false, true, new Component[]{utils.vlContainer(cmbPais), utils.vlContainer(cmbEstacion), utils.vlContainer(cmbMarca), utils.vlContainer(cmbFecha), utils.vlContainer(cmbProducto)});
        VerticalLayout v = new VerticalLayout(toolBar);
        return components.createHorizontal(Constant.styleViewheader, Constant.sizeFull, false, false, true, new Component[]{v});
    }

    private Component buildTableContent() {
        tblProduct.setCaption("Lubricantes:");
        tblProduct.setHeight(200, Unit.PERCENTAGE);
        tblProduct.setWidth(450, Unit.PERCENTAGE);
        tblProduct.setContainerDataSource(contLub);
        tablaPrecio.addGeneratedColumn("colSC", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Property prop = source.getItem(itemId).getItemProperty("priceSC"); //Atributo del bean
                TextField tfdSC = new TextField(utils.getPropertyFormatterDouble(prop));
                tfdSC.setWidth("100px");
                tfdSC.addStyleName("align-right");
                tfdSC.setNullRepresentation("0.00");
                tfdSC.setStyleName(ValoTheme.TEXTFIELD_TINY);
                tfdSC.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                    }
                });
                return tfdSC;
            }
        });
        tblProduct.setVisibleColumns(new Object[]{"paisNombre", "marcaNombre", "productoNombre", "fechaInicio", "fechaFin"});
        tblProduct.setColumnHeaders(new String[]{"Pais", "Marca", "Producto", "Inicio", "Fin"});
        tblProduct.addStyleName(ValoTheme.TABLE_COMPACT);
        tblProduct.addStyleName(ValoTheme.TABLE_SMALL);
        return components.createCssLayout(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{utils.vlContainerTable(tblProduct)});
    }
}
