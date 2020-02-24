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
import com.sisintegrados.dao.Dao;
import com.fundamental.services.SvcComVenLubricantes;
import com.fundamental.services.SvcGeneral;
import com.fundamental.services.SvcTurno;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.generic.bean.ComVenLubricantes;
import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
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
//    ComboBox cmbEstacion = new ComboBox();
    ComboBox cmbMarca = new ComboBox();
//    ComboBox cmbProducto = new ComboBox();
    DateField cmbFecha = new DateField("Fecha:");
    Usuario usuario = new Usuario();
    Utils utils = new Utils();

    Button btnGuardar = new Button("Guardar");
    Button btnAddEmpPump;
    Acceso acceso = new Acceso();
    BeanItemContainer<Pais> contPais = new BeanItemContainer<Pais>(Pais.class);
    BeanContainer<Integer, ComVenLubricantes> contLub = new BeanContainer<Integer, ComVenLubricantes>(ComVenLubricantes.class);
    SvcTurno dao = new SvcTurno();
    List<Producto> allLubricants = new ArrayList();
    List<Marca> listBrands = new ArrayList();
    List<ComVenLubricantes> listProducts = new ArrayList();        
    Table tblProduct = new Table();
    boolean bloqueo = false;

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
    
    private void getAllData(){
        contLub.removeAllItems();
        contLub.setBeanIdProperty("productoId");
        toolbarContainerTables.removeAllComponents();
        toolbarContainerTables.addComponent(buildTableContent());
        SvcComVenLubricantes service = new SvcComVenLubricantes();
        int countryId = ((Pais) cmbPais.getValue()).getPaisId();
        int brandId = ((Marca) cmbMarca.getValue()).getIdMarca();
        listProducts = service.getComVenLub(countryId, brandId, cmbFecha.getValue(),usuario.getEstacionid());//ASG ESTACION            
        contLub.addAll(listProducts);  
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
//                cmbEstacion.removeAllItems();
                cmbFecha.setValue(null);
                Dao svcEstacion = new DaoImp();
                Pais pais = new Pais();
                pais = (Pais) cmbPais.getValue();
                contEstacion.addAll(svcEstacion.getStationsByCountryUser(pais.getPaisId(), usuario.getUsuarioId()));
//                svcEstacion.closeConnections();
//                cmbEstacion.setContainerDataSource(contEstacion);
//                if (contEstacion.size() == 1) {
//                    cmbEstacion.setValue(contEstacion.getIdByIndex(0));
//                }
            }
        });

//        cmbEstacion = new ComboBox("Estación:");
//        cmbEstacion.setItemCaptionPropertyId("nombre");
//        cmbEstacion.setNullSelectionAllowed(false);
//        cmbEstacion.setFilteringMode(FilteringMode.CONTAINS);
//        cmbEstacion.setRequired(true);
//        cmbEstacion.setRequiredError("Debe seleccionar una estacion");
//        cmbEstacion.setWidth("230px");
//        cmbEstacion.addStyleName(ValoTheme.COMBOBOX_SMALL);
//        cmbEstacion.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                if (cmbEstacion.getValue() != null) {
////                    cargaUltTurnoDia();
//                }
//
//            }
//        });

        cmbMarca = new ComboBox("Marca:");
        cmbMarca.setItemCaptionPropertyId("nombre");
        cmbMarca.setNullSelectionAllowed(false);
        cmbMarca.setFilteringMode(FilteringMode.CONTAINS);
        cmbMarca.setRequired(true);
        cmbMarca.setRequiredError("Debe seleccionar una Marca");
        cmbMarca.setWidth("230px");
        cmbMarca.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cmbMarca.setContainerDataSource(new ListContainer<>(Marca.class, listBrands));
        cmbMarca.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                if (cmbMarca.getValue() != null) {
                    Dao dao = new DaoImp();
                    allLubricants = dao.getAllProducts();
                }
            }
        });

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
                getAllData();
            }
        });

//        cmbProducto = new ComboBox("Producto:");
//        cmbProducto.setItemCaptionPropertyId("nombre");
//        cmbProducto.setNullSelectionAllowed(false);
//        cmbProducto.setFilteringMode(FilteringMode.CONTAINS);
//        cmbProducto.setRequired(true);
//        cmbProducto.setRequiredError("Debe seleccionar un Producto");
//        cmbProducto.setWidth("230px");
//        cmbProducto.addStyleName(ValoTheme.COMBOBOX_SMALL);
//        cmbProducto.setContainerDataSource(new ListContainer<Producto>(Producto.class, allLubricants));
//        cmbProducto.addListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(final Property.ValueChangeEvent event) {
//                if (cmbProducto.getValue() != null) {
//                    getAllData();
//                }
//            }
//        });

        Component toolBar = components.createCssLayout(Constant.styleToolbar, Constant.sizeFull, false, false, true, new Component[]{utils.vlContainer(cmbPais), utils.vlContainer(cmbMarca), utils.vlContainer(cmbFecha)});
        VerticalLayout v = new VerticalLayout(toolBar);
        return components.createHorizontal(Constant.styleViewheader, Constant.sizeFull, false, false, true, new Component[]{v});
    }

    private Component buildTableContent() {
        tblProduct.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblProduct.addStyleName(ValoTheme.TABLE_COMPACT);
        tblProduct.addStyleName(ValoTheme.TABLE_SMALL);
        tblProduct.setSizeFull();
        tblProduct.setContainerDataSource(contLub);
        tblProduct.removeGeneratedColumn("colInvInicial");
        tblProduct.addGeneratedColumn("colInvInicial", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("invInicial");  //Atributo del bean
                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                Property inicial = source.getItem(itemId).getItemProperty("invInicial");
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.setNullRepresentation("0.00");
                if ((Double) inicial.getValue() > 0.0) {
                    tfdValue.setReadOnly(true);
                }
                tfdValue.addStyleName("align-right");
                return tfdValue;
            }
        }); 
        tblProduct.removeGeneratedColumn("ColCompra");
        tblProduct.addGeneratedColumn("ColCompra", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("compra");  //Atributo del bean
                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.setNullRepresentation("0.00");
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        ComVenLubricantes lub = contLub.getItem(itemId).getBean();
                        System.out.println("lub.getVenta() "+lub.getVenta());
                        Double venta = lub.getVenta()==null?0.0:lub.getVenta();
                        contLub.getItem(itemId).getItemProperty("invfinal").setValue(lub.getInvInicial()+ lub.getCompra()-venta);
                    }
                });
                return tfdValue;
            }
        }); 
//        tblProduct.removeGeneratedColumn("colVenta");
//        tblProduct.addGeneratedColumn("colVenta", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("venta");  //Atributo del bean
//                Label lbl = new Label(utils.getPropertyFormatterDouble(pro));
//                lbl.setWidth("85px");
//                lbl.addStyleName(ValoTheme.LABEL_SMALL);
//                lbl.addStyleName("align-right");
//                return lbl;
//            }
//        }); 
//        tblProduct.removeGeneratedColumn("colInvFinal");
//        tblProduct.addGeneratedColumn("colInvFinal", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("invfinal");  //Atributo del bean
//                Label lbl = new Label(utils.getPropertyFormatterDouble(pro));
//                lbl.setWidth("85px");
//                lbl.addStyleName(ValoTheme.LABEL_SMALL);
//                lbl.addStyleName("align-right");
//                return lbl;
//            }
//        });
        tblProduct.setVisibleColumns(new Object[]{"fecha","productoNombre", "colInvInicial", "ColCompra"});
        tblProduct.setColumnHeaders(new String[]{"Fecha","Nombre", "Inventario Inicial", "Compra"});
//        tblProduct.setColumnAlignments(new Table.Align[]{Table.Align.LEFT, Table.Align.LEFT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT});
        return components.createCssLayout(Constant.styleToolbar, Constant.sizeFull, true, false, true, new Component[]{utils.vlContainerTable(tblProduct)});
    }
    
    private Component buildButtons() {
        btnGuardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnGuardar.setIcon(FontAwesome.SAVE);
        btnGuardar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!cmbPais.isValid() || !cmbMarca.isValid() || !cmbFecha.isValid())  {
                    Notification.show("Los campos marcados son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                SvcComVenLubricantes service = new SvcComVenLubricantes();
                ComVenLubricantes comVenLub = new ComVenLubricantes();
                for (Integer i : (List<Integer>) tblProduct.getItemIds()) {
                    ComVenLubricantes lub;
                    lub = contLub.getItem(i).getBean();
//                    lub = (ComVenLubricantes) ((BeanItem) tblProduct.getItem(i)).getBean(); 
                    Double invInicial = lub.getInvInicial()==null?0.0:lub.getInvInicial();
                    Double compra = lub.getCompra()==null?0.0:lub.getCompra();
                    Double invfinal = lub.getInvfinal()==null?invInicial:lub.getInvfinal();
                    comVenLub.setInvInicial(invInicial);
                    comVenLub.setCompra(compra);
                    comVenLub.setVenta(lub.getVenta());
                    comVenLub.setInvfinal(invfinal);
                    comVenLub.setProductoId(lub.getProductoId());
                    comVenLub.setFecha(cmbFecha.getValue());
                    comVenLub.setMarcaId(((Marca)cmbMarca.getValue()).getIdMarca());
                    comVenLub.setPaisId(((Pais)cmbPais.getValue()).getPaisId());
                    comVenLub.setCreadopor(usuario.getUsername());
                    comVenLub.setEstacionid(usuario.getEstacionid());
                    if(comVenLub.getInvInicial()>0.0){
                        service.insertCompra(comVenLub);
//                        service.closeConnections();
                    }
                    
                }                
//                service.insertVenta(126, 188, 150.00, cmbFecha.getValue());
                
                if (comVenLub.getProductoId()>0) {
                    Notification notif = new Notification("ÉXITO:", "El registro se realizó con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_LUBS_CV.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n", Notification.Type.ERROR_MESSAGE);
                    return;
                }
            }
        });
        
//        btnModificar.setStyleName(ValoTheme.BUTTON_PRIMARY);
//        btnModificar.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                ComVenLubricantes comVenLub = new ComVenLubricantes();
//                comVenLub.setCompra(2.00);
//                comVenLub.setInvInicial(3.00);
//                comVenLub.setInvfinal(1000.00);
//                comVenLub.setVenta(9000.00);
//                comVenLub.setModificadopor("jmeng");
//                comVenLub.setCompraId(1);
//                comVenLub.setProductoId(1);
//                SvcComVenLubricantes service = new SvcComVenLubricantes();
//                service.updateLub(comVenLub);
//                service.closeConnections();
//                if (comVenLub.getCompraId()>0) {
//                    Notification notif = new Notification("ÉXITO:", "El registro se modificó con éxito.", Notification.Type.HUMANIZED_MESSAGE);
//                    notif.setDelayMsec(3000);
//                    notif.setPosition(Position.MIDDLE_CENTER);
//                    notif.show(Page.getCurrent());
//                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_LUBS_CV.getViewName());
//                } else {
//                    Notification.show("Ocurrió un error al ejecutar la acción. \n", Notification.Type.ERROR_MESSAGE);
//                    return;
//                }
//            }
//        });
        HorizontalLayout footer = (HorizontalLayout) components.createHorizontal(ValoTheme.WINDOW_BOTTOM_TOOLBAR, "", true, false, false, new Component[]{btnGuardar});
        
        footer.setComponentAlignment(btnGuardar, Alignment.TOP_CENTER);
        footer.setWidth(
                100.0f, Unit.PERCENTAGE);
        return footer;
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new DaoImp();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnGuardar.setEnabled(acceso.isAgregar());
    } 
}
