/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.view.form;

import com.fundamental.model.Arqueocaja;
import com.fundamental.model.Cliente;
import com.fundamental.model.Mediopago;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoProducto;
import com.fundamental.utils.Constant;
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
public class FormDetalleVenta2 extends Window {

    
    BeanFieldGroup<Arqueocaja> fieldGroup;
    Arqueocaja obj;
    Utils utils = new Utils();
    Button btnSave, btnAll, btnNone, btnAdd, btnAddCustomer;
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
    public FormDetalleVenta2() {
        setLocale(VaadinSession.getCurrent().getAttribute(Locale.class));
//        selected(obj);         
        addStyleName(Constant.stylePopUps);
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);
        
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
        detailsWrapper.addComponent(buildFileds());
        
            
    }
        public static void open() {
        Window w = new FormDetalleVenta2(); 
        UI.getCurrent().addWindow(w);
        w.focus();
    }
    public void createDetail() {
        buildTableCxC();
        VerticalLayout vltCxC = utils.buildVertical("vltCxC", false, false, true, false, null);
        vltCxC.addComponents(tblCxC, btnAddCustomer);
        vltCxC.setComponentAlignment(btnAddCustomer, Alignment.TOP_CENTER);
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
    private Component buildFileds(){
        root = utils.buildVertical("vlMainLayout", true, true, true, true, "dashboard-view");
        root.setSizeFull();
        root.setMargin(true);
        root.addStyleName("dashboard-view");
        root.addComponent(utils.buildHeader("Cuadre de caja", true, true));
        root.addComponent(utils.buildSeparator());
         FormLayout form = new FormLayout();
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
         root.addComponent(form);
        root.setExpandRatio(form, 1);
        Responsive.makeResponsive(form);
        setContent(form);
        TabSheet tabsheet = new TabSheet();
        tabsheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        //regresa.setCaption(cons.getTexto(new String[]{ (Constantes.nuevo.equals(tipo)?Constantes.nuevaEstacionCompania:(Constantes.ver.equals(tipo)?Constantes.verEstacionPorCompania:Constantes.actualizarEstCompania))}, getLocale(), provider));
       
        buildTableCxC();
        VerticalLayout vltCxC = utils.buildVertical("vltCxC", false, false, true, false, null);
        vltCxC.addComponents(tblCxC, btnAddCustomer);
        vltCxC.setComponentAlignment(btnAddCustomer, Alignment.TOP_CENTER);   
         final CssLayout cltCxc = new CssLayout(utils.vlContainer(vltCxC));
        cltCxc.setSizeUndefined();
        cltCxc.setVisible(false);
        Responsive.makeResponsive(cltCxc);
        Panel pnlDetalles = new Panel("Detalles de venta", cltCxc);
        pnlDetalles.setSizeFull();
        pnlDetalles.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                cltCxc.setVisible(!cltCxc.isVisible());
            }
        });

        CssLayout cltMain = new CssLayout(pnlDetalles);
        Responsive.makeResponsive(cltMain);
        tabsheet.addTab(cltMain, "Principal", FontAwesome.LIST);

        root.addComponent(tabsheet);
        root.setExpandRatio(tabsheet, 1);

        

        form.addComponents(root);
        return form;
    }
//    buildTableCxC();
//        VerticalLayout vltCxC = utils.buildVertical("vltCxC", false, false, true, false, null);
//        vltCxC.addComponents(tblCxC, btnAddCustomer);
//        vltCxC.setComponentAlignment(btnAddCustomer, Alignment.TOP_CENTER);

}
