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
import com.fundamental.services.SvcInventarioFisico;
import com.fundamental.services.SvcEstacion;
import com.fundamental.services.SvcGeneral;
import com.fundamental.services.SvcTurno;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.generic.bean.ComInventarioFisico;
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
import com.vaadin.ui.TextArea;
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
public class MntInventarioFisico extends Panel implements View {

    CreateComponents components = new CreateComponents();
    ComboBox cmbPais = new ComboBox();
    ComboBox cmbMarca = new ComboBox();
    DateField cmbFecha = new DateField("Fecha:");
    Usuario usuario = new Usuario();
    Utils utils = new Utils();

    Button btnGuardar = new Button("Guardar");
    Button btnAddEmpPump;
    Acceso acceso = new Acceso();
    BeanItemContainer<Pais> contPais = new BeanItemContainer<Pais>(Pais.class);
    BeanContainer<Integer, ComInventarioFisico> contLub = new BeanContainer<Integer, ComInventarioFisico>(ComInventarioFisico.class);
    SvcTurno dao = new SvcTurno();
    List<Producto> allLubricants = new ArrayList();
    List<Marca> listBrands = new ArrayList();
    List<ComInventarioFisico> list = new ArrayList();
    List<ComInventarioFisico> listProducts = new ArrayList();       
    Table tblProduct = new Table();
    boolean bloqueo = true;

    public MntInventarioFisico() {

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
        Label title = new Label("Inventario Fisico");
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
        contLub.setBeanIdProperty("producto_id");
        toolbarContainerTables.removeAllComponents();
        toolbarContainerTables.addComponent(buildTableContent());
        SvcInventarioFisico service = new SvcInventarioFisico();
        int countryId = ((Pais) cmbPais.getValue()).getPaisId();
        int brandId = ((Marca) cmbMarca.getValue()).getIdMarca();        
        contLub.addAll(service.getLubricantes(brandId, cmbFecha.getValue()));  
//        ComInventarioFisico fis;
//        for(ComInventarioFisico c: list){
//            fis.setProductoNombre(c.getProductoNombre());
//            System.out.println("fis "+fis.getProductoNombre());            
//        }
//        listProducts.add(fis);
//        contLub.addAll(listProducts);  
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
                SvcEstacion svcEstacion = new SvcEstacion();
                Pais pais = new Pais();
                pais = (Pais) cmbPais.getValue();
                contEstacion.addAll(svcEstacion.getStationsByCountryUser(pais.getPaisId(), usuario.getUsuarioId()));
                svcEstacion.closeConnections();
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
        cmbMarca.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                if (cmbMarca.getValue() != null && cmbFecha.getValue() != null) {
                    getAllData();
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
                if (cmbMarca.getValue() != null && cmbFecha.getValue() != null) {
                    getAllData();
                }
            }
        });        

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
        tblProduct.removeGeneratedColumn("uniFisTienda");
        tblProduct.addGeneratedColumn("uniFisTienda", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("unidad_fis_tienda");  //Atributo del bean
                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.setNullRepresentation("0.00");
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        ComInventarioFisico invdto = (ComInventarioFisico) ((BeanItem) tblProduct.getItem(itemId)).getBean();
                        Double fisTienda = invdto.getUnidad_fis_tienda() == null ? 0.0 : invdto.getUnidad_fis_tienda();
                        Double fisBodega = invdto.getUnidad_fis_bodega() == null ? 0.0 : invdto.getUnidad_fis_bodega();
                        Double fisPista = invdto.getUnidad_fis_pista() == null ? 0.0 : invdto.getUnidad_fis_pista();

                        invdto.setTotal_unidad_fisica(fisTienda+fisBodega+fisPista);
                        contLub.getItem(itemId).getItemProperty("total_unidad_fisica").setValue(invdto.getTotal_unidad_fisica());
                    }
                });
                return tfdValue;
            }
        });
        tblProduct.removeGeneratedColumn("uniFisBodega");
        tblProduct.addGeneratedColumn("uniFisBodega", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("unidad_fis_bodega");  //Atributo del bean
                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.setNullRepresentation("0.00");
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        ComInventarioFisico invdto = (ComInventarioFisico) ((BeanItem) tblProduct.getItem(itemId)).getBean();
                        Double fisTienda = invdto.getUnidad_fis_tienda() == null ? 0.0 : invdto.getUnidad_fis_tienda();
                        Double fisBodega = invdto.getUnidad_fis_bodega() == null ? 0.0 : invdto.getUnidad_fis_bodega();
                        Double fisPista = invdto.getUnidad_fis_pista() == null ? 0.0 : invdto.getUnidad_fis_pista();

                        invdto.setTotal_unidad_fisica(fisTienda+fisBodega+fisPista);
                        contLub.getItem(itemId).getItemProperty("total_unidad_fisica").setValue(invdto.getTotal_unidad_fisica());
                    }
                });
                return tfdValue;
            }
        }); 
        tblProduct.removeGeneratedColumn("uniFisPista");
        tblProduct.addGeneratedColumn("uniFisPista", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("unidad_fis_pista");  //Atributo del bean
                TextField tfdValue = new TextField(utils.getPropertyFormatterDouble(pro));
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTFIELD_SMALL);
                tfdValue.setNullRepresentation("0.00");
                tfdValue.addStyleName("align-right");
                tfdValue.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        ComInventarioFisico invdto = (ComInventarioFisico) ((BeanItem) tblProduct.getItem(itemId)).getBean();
                        Double fisTienda = invdto.getUnidad_fis_tienda() == null ? 0.0 : invdto.getUnidad_fis_tienda();
                        Double fisBodega = invdto.getUnidad_fis_bodega() == null ? 0.0 : invdto.getUnidad_fis_bodega();
                        Double fisPista = invdto.getUnidad_fis_pista() == null ? 0.0 : invdto.getUnidad_fis_pista();

                        invdto.setTotal_unidad_fisica(fisTienda+fisBodega+fisPista);
                        contLub.getItem(itemId).getItemProperty("total_unidad_fisica").setValue(invdto.getTotal_unidad_fisica());
                    }
                });
                return tfdValue;
            }
        }); 
        tblProduct.removeGeneratedColumn("totalInvUniFis");
        tblProduct.addGeneratedColumn("totalInvUniFis", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("total_unidad_fisica");  //Atributo del bean
                Label lbl = new Label(utils.getPropertyFormatterDouble(pro));
                lbl.setWidth("65px");
//                lbl.addStyleName(ValoTheme.LABEL_SMALL);
                lbl.addStyleName("align-right");
                lbl.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        ComInventarioFisico invdto = (ComInventarioFisico) ((BeanItem) tblProduct.getItem(itemId)).getBean();
                        Double invFinal = invdto.getInv_final() == null ? 0.0 : invdto.getInv_final();
                        Double totalUniFis = invdto.getTotal_unidad_fisica() == null ? 0.0 : invdto.getTotal_unidad_fisica();

                        invdto.setDiferencia_inv(invFinal-totalUniFis);
                        contLub.getItem(itemId).getItemProperty("diferencia_inv").setValue(invdto.getDiferencia_inv());
                    }
                });
                return lbl;
            }
        });
        tblProduct.removeGeneratedColumn("difInvFinal");
        tblProduct.addGeneratedColumn("difInvFinal", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("diferencia_inv");  //Atributo del bean
                Label lbl = new Label(utils.getPropertyFormatterDouble(pro));
                lbl.setWidth("85px");
                lbl.addStyleName(ValoTheme.LABEL_SMALL);
                lbl.addStyleName("align-right");
                return lbl;
            }
        });
        tblProduct.removeGeneratedColumn("colComentario");
        tblProduct.addGeneratedColumn("colComentario", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("comentario");  //Atributo del bean
                TextArea tfdValue = new TextArea(pro);
                tfdValue.setRows(2);
                tfdValue.setWidth("85px");
                tfdValue.setStyleName(ValoTheme.TEXTAREA_TINY);
                tfdValue.setNullRepresentation("");
                return tfdValue;
            }
        }); 
//        tblProduct.setVisibleColumns(new Object[]{"numero","productoNombre", "presentacion",  "precio", "inv_final", "uniFisTienda", "uniFisBodega", "uniFisPista","totalInvUniFis","difInvFinal","colComentario"});
//        tblProduct.setColumnHeaders(new String[]{"#","Descripcion", "Presentacion", "Precio </br> de venta", "Inventario </br> final", "Unidades </br> fisicas </br> en tienda", "Unidades </br> fisicas </br> en Bodega", "Unidades </br> fisicas </br> en Pista","Total </br> unidades </br> fisicas","Diferencia </br> inv. final en </br> mov. vrs toma","Comentarios"});
        tblProduct.setVisibleColumns(new Object[]{"productoNombre", "presentacion",  "precio", "inv_final", "uniFisTienda", "uniFisBodega", "uniFisPista","totalInvUniFis","difInvFinal","colComentario"});
        tblProduct.setColumnHeaders(new String[]{"Descripcion", "Presentacion", "Precio </br> de venta", "Inventario </br> final", "Unidades </br> fisicas </br> en tienda", "Unidades </br> fisicas </br> en Bodega", "Unidades </br> fisicas </br> en Pista","Total </br> unidades </br> fisicas","Diferencia </br> inv. final en </br> mov. vrs toma","Comentarios"});
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
                SvcInventarioFisico service = new SvcInventarioFisico();
                ComInventarioFisico comInv = new ComInventarioFisico();
                ComInventarioFisico inv;
                for (Integer i : (List<Integer>) tblProduct.getItemIds()) {
                    ComInventarioFisico lub;
                    lub = (ComInventarioFisico) ((BeanItem) tblProduct.getItem(i)).getBean();     
                    comInv.setProductoNombre(lub.getProductoNombre());
                    comInv.setPresentacion(lub.getPresentacion());
                    comInv.setPrecio(lub.getPrecio());
                    comInv.setInv_final(lub.getInv_final());
                    comInv.setProducto_id(lub.getProducto_id());
                    comInv.setFecha(cmbFecha.getValue());                
                    comInv.setCreado_por(usuario.getUsername());
                    comInv.setCreado_el(usuario.getCreadoEl());
                    comInv.setUnidad_fis_tienda(lub.getUnidad_fis_tienda());
                    comInv.setUnidad_fis_bodega(lub.getUnidad_fis_bodega());
                    comInv.setUnidad_fis_pista(lub.getUnidad_fis_pista());
                    comInv.setDiferencia_inv(lub.getDiferencia_inv());
                    comInv.setTotal_unidad_fisica(lub.getTotal_unidad_fisica());
                    comInv.setComentario(lub.getComentario());
                    System.out.println("total unidad fisica "+comInv.getDiferencia_inv());
                    if (comInv.getDiferencia_inv()>=0.0) {
                        service.insertCompra(comInv);
                    } else {
                        Notification.show("El valor final no puede ser negativo. \n", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                    
                }
//                for (Integer pid : contLub.getItemIds()) {
//                    inv = contLub.getItem(pid).getBean();
//                    comInv.setProducto_id(inv.getProducto_id());
//                    
//                }                
//                service.insertVenta(123, 188, 150.00, cmbFecha.getValue());
                service.closeConnections();
                if (comInv.getProducto_id()>0) {
                    Notification notif = new Notification("ÉXITO:", "El registro se realizó con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_INV_FIS.getViewName());
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
        Dao dao = new Dao();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnGuardar.setEnabled(acceso.isAgregar());
    } 
}
