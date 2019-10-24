/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.view.form;

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
public class FormClientesCredito extends Window {

    CreateComponents components = new CreateComponents();
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
    BeanContainer<Integer, Producto> bcrProducto = new BeanContainer<Integer, Producto>(Producto.class);
    Lubricanteprecio lubricante = new Lubricanteprecio();
    static final DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00");
    Utils utils = new Utils();
    Table tblCxC;
    

    BeanContainer<Integer, DtoProducto> bcrClientes = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
    Container contCustomerCredit = new ListContainer<>(Cliente.class, new ArrayList());
    double tmpDouble;
    List<DtoProducto> listCustomers = new ArrayList();
    String currencySymbol, volumenSymbol, tmpString;
    BeanContainer<Integer, Mediopago> bcrMediopago = new BeanContainer<Integer, Mediopago>(Mediopago.class),
            bcrPartida = new BeanContainer<Integer, Mediopago>(Mediopago.class);
    VerticalLayout root;
    
    
    
    public FormClientesCredito(String tipo, Lubricanteprecio lubricante) {
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
        
        buildTableCxC();
        VerticalLayout vltCxC = utils.buildVertical("vltCxC", false, false, true, false, null);
        vltCxC.addComponents(tblCxC, btnAddCustomer);
        vltCxC.setComponentAlignment(btnAddCustomer, Alignment.TOP_CENTER);

        detailsWrapper.addComponent(buildFields());
        //content.addComponent(buildButtons());
      
    }
 //    private Component buildButtons() //{
//        guardar = new Button("Guardar");
//        guardar.addStyleName(ValoTheme.BUTTON_PRIMARY);
//        guardar.addStyleName(ValoTheme.BUTTON_SMALL);
//        guardar.addClickListener((Button.ClickListener) event -> {
//
//        });
//        guardar.focus();
//
//        cancelar = new Button("Cancelar");
//        cancelar.addStyleName(ValoTheme.BUTTON_PRIMARY);
//        cancelar.addStyleName(ValoTheme.BUTTON_SMALL);
//        cancelar.addClickListener((Button.ClickListener) event -> {
//            close();
//        });
//
//        HorizontalLayout footer = (HorizontalLayout) components.createHorizontal(ValoTheme.WINDOW_BOTTOM_TOOLBAR, "", true, false, false, new Component[]{guardar, cancelar});
//        footer.setComponentAlignment(guardar, Alignment.TOP_RIGHT);
//        if (!guardar.isVisible()) {
//            footer.setComponentAlignment(cancelar, Alignment.TOP_CENTER);
//        }
//        footer.setWidth(100.0f, Unit.PERCENTAGE);
//        return footer;
   // }

    private Component buildFields() {
        HorizontalLayout regresa = (HorizontalLayout) components.createHorizontal(Constant.styleForm, "100%", true, true, false, null);
        regresa.setCaption("Detalle clientes credito");
        regresa.setIcon(FontAwesome.CAR);
        FormLayout form = new FormLayout();
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        regresa.addComponent(form);
        regresa.setExpandRatio(form, 1);

        HorizontalLayout hlDoc1 = (HorizontalLayout) components.createHorizontal(Constant.styleForm, "100%", false, false, false, null);
        hlDoc1.setHeight("55px");

       

        
       // form.addComponent(precio);
        buildTableCxC();
        form.addComponent(tblCxC);
        return regresa;
    }
    
    private void buildTableCxC() {
        tblCxC = utils.buildTable("Detalle clientes credito (CxC):", 100f, 100f, bcrClientes,
                new String[]{"nombre"},
                new String[]{"Nombre"}
        );
        tblCxC.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblCxC.addStyleName(ValoTheme.TABLE_COMPACT);
        tblCxC.addStyleName(ValoTheme.TABLE_SMALL);
        tblCxC.setImmediate(true);
        tblCxC.addGeneratedColumn("colCliente", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cliente");  //Atributo del bean
                ComboBox cbxCliente = utils.buildCombobox("", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, contCustomerCredit);
                cbxCliente.setPropertyDataSource(pro);
                cbxCliente.setFilteringMode(FilteringMode.CONTAINS);
                return cbxCliente;
            }
        });
        tblCxC.addGeneratedColumn("colMonto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("valor");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
                nfd.setValue(numberFmt.format(value));
                nfd.setWidth("100px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        updateTableFooterCxC();
                    }
                });
                return nfd;
            }
        });
        tblCxC.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        bcrClientes.removeItem(itemId);
                        List<DtoProducto> tempList = new ArrayList();
                        for (DtoProducto deo : listCustomers) {
                            if (deo.getProductoId() != itemId) {
                                tempList.add(deo);
                            }
                        }
                        listCustomers = tempList;
                        updateTableFooterCxC();
                    }
                });
                return btnDelete;
            }
        });
        tblCxC.setVisibleColumns(new Object[]{"colCliente", "colMonto", "colDelete"});
        tblCxC.setColumnHeaders(new String[]{"Cliente", "Monto", "Borrar"});
        tblCxC.setColumnAlignments(Table.Align.LEFT, Table.Align.RIGHT, Table.Align.CENTER);
        tblCxC.setSizeUndefined();
        tblCxC.setHeight(200f, Unit.PIXELS);

        btnAddCustomer = new Button("Agregar", FontAwesome.PLUS);
        btnAddCustomer.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAddCustomer.addStyleName(ValoTheme.BUTTON_SMALL);
       // btnAddCustomer.addClickListener((final Button.ClickEvent event) -> {
//            FormDetalleVenta.open();
          //  });
        btnAddCustomer.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                bcrClientes.removeAllItems();
                DtoProducto dtoprod = new DtoProducto(utils.getRandomNumberInRange(1, 1000), null, null);
                dtoprod.setValor(0D);
                listCustomers.add(dtoprod);
                bcrClientes.addAll(listCustomers);
            }
        });
    }
   
   public void updateTableFooterCxC() {
        tmpDouble = 0;
        for (Integer itemId : bcrClientes.getItemIds()) {
            tmpDouble += bcrClientes.getItem(itemId).getBean().getValor();
        }
        tblCxC.setFooterVisible(true);
        tblCxC.setColumnFooter("colCliente", "Total:");
        tblCxC.setColumnFooter("colMonto", currencySymbol + numberFmt.format(tmpDouble).trim());
        for (Integer itemId : bcrMediopago.getItemIds()) {
            if (bcrMediopago.getItem(itemId).getBean().getMediopagoId() == Constant.MP_CRI_VENTA_CREDITO) {
                bcrMediopago.getItem(itemId).getItemProperty("value").setValue(tmpDouble);  //put value
                break;
            }
        }
    } 
}
