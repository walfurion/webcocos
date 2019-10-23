/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.view.forms;

import com.fundamental.model.Arqueocaja;
import com.fundamental.model.Cliente;
import com.fundamental.model.Lubricanteprecio;
import com.fundamental.model.Mediopago;
import com.fundamental.model.Producto;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoProducto;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.vaadin.maddon.ListContainer;

/**
 *
 * @author Mery
 */
public class FormDetalleVenta extends Window {

    CreateComponents components = new CreateComponents();
    BeanFieldGroup<Arqueocaja> fieldGroup;
    Arqueocaja obj;
    Utils utils = new Utils();
    Button btnSave, btnAll, btnNone, btnAdd, btnAddCustomer, btnEliminar, guardar,cancelar, btnAddLubs ;
    String tipo;
    VerticalLayout v = new VerticalLayout();
    TextField producto            = new TextField();
    TextField precio;
    TextField cantidad;
    TextField total;
    int tmpInt;
    Usuario usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
    Container contLubs = new ListContainer<>(Producto.class, new ArrayList());
    BeanItemContainer<Lubricanteprecio> contLubricante = new BeanItemContainer<Lubricanteprecio>(Lubricanteprecio.class);
    BeanContainer<Integer, DtoProducto> bcrLubs = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
    BeanContainer<Integer, Producto> bcrProducto = new BeanContainer<Integer, Producto>(Producto.class);
    Lubricanteprecio lubricante = new Lubricanteprecio();
    static final DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00");
    Table tblProd, tblMediospago, tblEfectivo, tblFactElect, tblDiferencias, tblCxC, tblLubricantes, tblPrepaid,
            tableBombas = new Table("Bombas:"), tblPartida, tblCreditCard,
            tableCalc = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("volumen") || colId.equals("venta") || colId.equals("diferencia")) {
                return numberFmt.format(property.getValue()).trim();
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };
    BeanContainer<Integer, DtoProducto> bcrClientes = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
    Container contCustomerCredit = new ListContainer<>(Cliente.class, new ArrayList());
    double tmpDouble;
    List<DtoProducto> listCustomers = new ArrayList(), listLubs = new ArrayList(), listPrepaid = new ArrayList();
    String currencySymbol, volumenSymbol, tmpString;
    BeanContainer<Integer, Mediopago> bcrMediopago = new BeanContainer<Integer, Mediopago>(Mediopago.class),
            bcrPartida = new BeanContainer<Integer, Mediopago>(Mediopago.class);
    VerticalLayout root;
    
    
    
    public FormDetalleVenta(String tipo, Lubricanteprecio lubricante) {
        this.tipo = tipo;
        this.lubricante = lubricante;
        addStyleName(Constant.stylePopUps);
        Responsive.makeResponsive(this);
        setModal(true);
        addCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(80.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildFields());
        content.addComponent(buildButtons());
       // cargaEmpleado();
    }
     private Component buildButtons() {
        guardar = new Button("Guardar");
        guardar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        guardar.addStyleName(ValoTheme.BUTTON_SMALL);
        guardar.addClickListener((Button.ClickListener) event -> {
//            if (nombre.getValue() != null && cmbEstado.getValue() != null) {
//                try {
//                    if (tipo.equals("Nuevo")) {
//                        GenericEstado estado = new GenericEstado();
//                        estado = (GenericEstado) cmbEstado.getValue();
//                        dao.insert(nombre.getValue().toUpperCase().trim(), estado.getEstado(), usuario.getUsername());
//                        components.crearNotificacion("Datos ingresados correctamente!!!");
//                        DashboardEventBus.post(new DashboardEvent.ProfileUpdatedEvent());
//                        close();
//                    } else {
//                        GenericEstado estado = new GenericEstado();
//                        estado = (GenericEstado) cmbEstado.getValue();
//                        if (empleado != null) {
//                            dao.update(empleado.getEmpleadoId(), nombre.getValue().toUpperCase().trim(), estado.getEstado(), usuario.getUsername());
//                            components.crearNotificacion("Datos modificados correctamente!!!");
//                            DashboardEventBus.post(new DashboardEvent.ProfileUpdatedEvent());
//                            close();
//                        }
//                    }
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            } else {
//                Notification.show("Error!!!", "Se deben ingresar todos los datos.", Notification.Type.ERROR_MESSAGE);
//                return;
//            }
        });
        guardar.focus();

        cancelar = new Button("Cancelar");
        cancelar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        cancelar.addStyleName(ValoTheme.BUTTON_SMALL);
        cancelar.addClickListener((Button.ClickListener) event -> {
            close();
        });

        HorizontalLayout footer = (HorizontalLayout) components.createHorizontal(ValoTheme.WINDOW_BOTTOM_TOOLBAR, "", true, false, false, new Component[]{guardar, cancelar});
        footer.setComponentAlignment(guardar, Alignment.TOP_RIGHT);
        if (!guardar.isVisible()) {
            footer.setComponentAlignment(cancelar, Alignment.TOP_CENTER);
        }
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        return footer;
    }

    private Component buildFields() {
        HorizontalLayout regresa = (HorizontalLayout) components.createHorizontal(Constant.styleForm, "100%", true, true, false, null);
        regresa.setCaption("Lubricante");
        regresa.setIcon(FontAwesome.CAR);
        FormLayout form = new FormLayout();
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        regresa.addComponent(form);
        regresa.setExpandRatio(form, 1);

        HorizontalLayout hlDoc1 = (HorizontalLayout) components.createHorizontal(Constant.styleForm, "100%", false, false, false, null);
        hlDoc1.setHeight("55px");

        v.setSpacing(true);
        precio = new TextField("Precio");
        precio.setRequired(true);
        precio.setMaxLength(60);
        precio.setRequiredError("Debe ingresar precio");
        precio.setStyleName(ValoTheme.TEXTFIELD_SMALL);

        
       // form.addComponent(precio);
        buildTableLubsDet();
        form.addComponent(tblLubricantes);
        return regresa;
    }
    
    public void buildTableLubsDet() {
        tblLubricantes = utils.buildTable("Detalle lubricantes:", 100f, 100f, bcrLubs,
                new String[]{"nombre"},
                new String[]{"Nombre"}
        );
        tblLubricantes.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblLubricantes.addStyleName(ValoTheme.TABLE_COMPACT);
        tblLubricantes.addStyleName(ValoTheme.TABLE_SMALL);
        tblLubricantes.setImmediate(true);
        tblLubricantes.addGeneratedColumn("colProducto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("producto");  //Atributo del bean
                final ComboBox cbxProducto = utils.buildCombobox("", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, contLubs);
                cbxProducto.setPropertyDataSource(pro);
                cbxProducto.setFilteringMode(FilteringMode.CONTAINS);
                cbxProducto.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        bcrLubs.getItem(itemId).getItemProperty("valor").setValue(((Producto) cbxProducto.getValue()).getPrecio());
                    }
                });
                return cbxProducto;
            }
        });
        tblLubricantes.addGeneratedColumn("colCantidad", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cantidad");  //Atributo del bean
                final TextField nfd = new TextField(pro);
//                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
//                nfd.setValue(numberFmt.format(value));
                nfd.setWidth("60px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        if (nfd.getValue() != null && !nfd.getValue().trim().isEmpty() && nfd.getValue().matches("\\d+")) {
                            int cantidad = Integer.parseInt(nfd.getValue().trim());
                            double precio = Double.parseDouble(bcrLubs.getItem(itemId).getItemProperty("valor").getValue().toString());
                            bcrLubs.getItem(itemId).getItemProperty("total").setValue(cantidad * precio);
                            updateTableFooterLubs();
                        } else {
                            nfd.setValue("0");
                        }
                    }
                });
                return nfd;
            }
        });
        tblLubricantes.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        bcrLubs.removeItem(itemId);
                        List<DtoProducto> tempList = new ArrayList();
                        for (DtoProducto deo : listLubs) {
                            if (deo.getProductoId() != itemId) {
                                tempList.add(deo);
                            }
                        }
                        listLubs = tempList;
                        updateTableFooterLubs();
                    }
                });
                return btnDelete;
            }
        });
        tblLubricantes.setVisibleColumns(new Object[]{"colProducto", "valor", "colCantidad", "total", "colDelete"});
        tblLubricantes.setColumnHeaders(new String[]{"Producto", "Precio", "Cantidad", "Total", "Borrar"});
        tblLubricantes.setColumnAlignments(Table.Align.LEFT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.RIGHT, Table.Align.CENTER);
        tblLubricantes.setSizeUndefined();
        tblLubricantes.setHeight(200f, Unit.PIXELS);
        tblLubricantes.setColumnWidth("valor", 100);
        tblLubricantes.setColumnWidth("total", 100);

        btnAddLubs = new Button("Agregar", FontAwesome.PLUS);
        btnAddLubs.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAddLubs.addStyleName(ValoTheme.BUTTON_SMALL);
        btnAddLubs.addClickListener((final Button.ClickEvent event) -> {
//            FormDetalleVenta2.open();
            });
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                FormDetalleVenta2.open();
//                bcrLubs.removeAllItems();
//                DtoProducto dtoprod = new DtoProducto(utils.getRandomNumberInRange(1, 1000), null, null);
//                dtoprod.setValor(0D);
//                dtoprod.setCantidad(0);
//                dtoprod.setTotal(0D);
//                listLubs.add(dtoprod);
//                bcrLubs.addAll(listLubs);
//            }
//        });
    }
   public void updateTableFooterLubs() {
        tmpInt = 0;
        tmpDouble = 0;
        for (Integer itemId : bcrLubs.getItemIds()) {
            tmpDouble += bcrLubs.getItem(itemId).getBean().getTotal();
            tmpInt += bcrLubs.getItem(itemId).getBean().getCantidad();
        }
        tblLubricantes.setFooterVisible(true);
        tblLubricantes.setColumnFooter("colProducto", "Total:");
        tblLubricantes.setColumnFooter("colCantidad", Integer.toString(tmpInt));
        tblLubricantes.setColumnFooter("total", currencySymbol + numberFmt.format(tmpDouble).trim());
        for (Integer itemId : bcrProducto.getItemIds()) {
            if (bcrProducto.getItem(itemId).getBean().getProductoId() == Constant.MP_CRI_VENTA_LUBS_UNO) {
                bcrProducto.getItem(itemId).getItemProperty("value").setValue(tmpDouble);
                break;
            }
        }
    }
    
}
