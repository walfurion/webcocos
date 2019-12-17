///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.sisintegrados.view.form;
//
//import com.fundamental.model.Cliente;
//import com.fundamental.model.Mediopago;
//import com.fundamental.model.Utils;
//import com.fundamental.model.dto.DtoProducto;
//import com.fundamental.services.SvcCuadre;
//import com.fundamental.services.SvcMedioPago;
//import com.fundamental.utils.Constant;
//import com.fundamental.utils.CreateComponents;
//import com.sisintegrados.generic.bean.DepositoDet;
//import com.sisintegrados.generic.bean.GenericDepositoDet;
//import com.sisintegrados.generic.bean.GenericMedioPago;
//import com.sisintegrados.generic.bean.Usuario;
//import com.vaadin.data.Container;
//import com.vaadin.data.Property;
//import com.vaadin.data.util.BeanContainer;
//import com.vaadin.data.util.BeanItemContainer;
//import com.vaadin.event.ShortcutAction;
//import com.vaadin.server.FontAwesome;
//import com.vaadin.server.Responsive;
//import com.vaadin.server.Sizeable;
//import com.vaadin.server.VaadinSession;
//import com.vaadin.shared.ui.combobox.FilteringMode;
//import com.vaadin.ui.AbstractSelect;
//import com.vaadin.ui.Alignment;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.ComboBox;
//import com.vaadin.ui.Component;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.TabSheet;
//import com.vaadin.ui.Table;
//import com.vaadin.ui.TextField;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.Window;
//import com.vaadin.ui.themes.ValoTheme;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//import org.vaadin.maddon.ListContainer;
//
///**
// *
// * @author Jorge
// */
//public class FormDetalleDeposito extends Window {
//
//    static final DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00;-#");
//    static final DecimalFormat numberFmt3D = new DecimalFormat("### ###,##0.000;-#");
//    static final String HEIGHT_TABLE = "300px";
//    CreateComponents components = new CreateComponents();
//    Constant cons = new Constant();
//    Button btnasignar = new Button();
//    Button btnguardar = new Button();
//    Usuario usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
//    Table tblDeposito; //tblDeposito
//    BeanItemContainer<GenericMedioPago> ContMedioPago = new BeanItemContainer<GenericMedioPago>(GenericMedioPago.class);
//    BeanContainer<Integer, GenericDepositoDet> bcrDeposito = new BeanContainer<Integer, GenericDepositoDet>(GenericDepositoDet.class);
//    Utils utils = new Utils();
//    SvcMedioPago dao = new SvcMedioPago();
//    Button btnAddCreditC;
//    double tmpDouble;
//    double tmpDoubleDolar;
//    double tmpDoubleOther;
//    String currencySymbol;
//    List<GenericDepositoDet> listTarjeta = new ArrayList();
//    Integer idpais;
//    Integer idestacion;
//
//    public FormDetalleDeposito(String currencySymbol, BeanContainer<Integer, GenericDepositoDet> bcrDeposito, Integer idpais, Integer idestacion) {
//        //public FormDetalleDeposito(String currencySymbol, Integer idpais, Integer idestacion) {
//        this.bcrDeposito = bcrDeposito;
//        this.currencySymbol = currencySymbol;
//        this.idpais = idpais;
//        this.idestacion = idestacion;
//
//        addStyleName(Constant.stylePopUps);
//        Responsive.makeResponsive(this);
//        setModal(true);
//        addCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
//        setResizable(false);
//        setClosable(false);
//        setHeight(80.0f, Sizeable.Unit.PERCENTAGE);
//
//        VerticalLayout content = new VerticalLayout();
//        content.setSizeFull();
//        setContent(content);
//
//        TabSheet detailsWrapper = new TabSheet();
//        detailsWrapper.setSizeFull();
//        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
//        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
//        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
//        content.addComponent(detailsWrapper);
//        content.setExpandRatio(detailsWrapper, 1f);
////        ContMedioPago = new BeanItemContainer<Tarjeta>(Tarjeta.class);
////        ContMedioPago.addAll(dao.getTarjetas());
//        detailsWrapper.addComponent(buildFields());
//        content.addComponent(buildButtons());
//    }
//
//    private Component buildButtons() {
//        btnasignar = new Button("Agregar");
//        btnasignar.addStyleName(ValoTheme.BUTTON_PRIMARY);
//        btnasignar.addStyleName(ValoTheme.BUTTON_SMALL);
//        btnasignar.addClickListener((Button.ClickListener) event -> {
//            GenericDepositoDet genericTarjeta = new GenericDepositoDet(utils.getRandomNumberInRange(1, 1000), null, null, null, null);
//            genericTarjeta.setMonto(0D);
//            listTarjeta.add(genericTarjeta);
//            bcrDeposito.addAll(listTarjeta);
//        });
//        btnasignar.focus();
//
//        btnguardar = new Button("Guardar", FontAwesome.SAVE);
//
//        btnguardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
//        btnguardar.addStyleName(ValoTheme.BUTTON_SMALL);
//        btnguardar.addClickListener((Button.ClickListener) event -> {
//            if (bcrDeposito.size() <= 0) {
//                System.out.println("ingresa a bcrdep menor de 0");
//                bcrDeposito = new BeanContainer<Integer, GenericDepositoDet>(GenericDepositoDet.class);
//                VaadinSession.getCurrent().setAttribute("detalleDeposito", bcrDeposito);
//                VaadinSession.getCurrent().setAttribute("totalDolar", 0.00);
//                VaadinSession.getCurrent().setAttribute("totalOtro", 0.00);
//            } else {
//                updateTableFooterCreditCard();
//            }
//            close();
//        });
//
//        HorizontalLayout footer = (HorizontalLayout) components.createHorizontal(ValoTheme.WINDOW_BOTTOM_TOOLBAR, "", true, false, false, new Component[]{btnasignar, btnguardar});
//        footer.setComponentAlignment(btnasignar, Alignment.TOP_RIGHT);
//        if (!btnasignar.isVisible()) {
//            footer.setComponentAlignment(btnguardar, Alignment.TOP_CENTER);
//        }
//        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
//        return footer;
//    }
//
//    private Component buildFields() {
//        HorizontalLayout hl = new HorizontalLayout();
//        hl.setCaption("Detalle de Depósitos");
//        hl.setIcon(FontAwesome.FLAG);
//        hl.setSpacing(true);
//        buildTableCreditCard();
//        hl.addComponent(tblDeposito);
//        return hl;
//    }
//
//    public void buildTableCreditCard() {
//        bcrDeposito.setBeanIdProperty("idGenerico");
//        tblDeposito = new Table();
//        tblDeposito.setContainerDataSource(bcrDeposito);
//        tblDeposito.setWidth(650f, Unit.PIXELS);
//        tblDeposito.setHeight(335f, Unit.PIXELS);
//
//        tblDeposito.addStyleName(ValoTheme.TABLE_BORDERLESS);
//        tblDeposito.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
//        tblDeposito.setImmediate(true);
//        tblDeposito.addGeneratedColumn("colMedioPago", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("mediopago");  //Atributo del bean
//                ContMedioPago = new BeanItemContainer<GenericMedioPago>(GenericMedioPago.class);
//                ContMedioPago.addAll(dao.getMedioPagoByCountry(idpais));
//                ComboBox cmbTarjeta = new ComboBox(null, ContMedioPago);
//                cmbTarjeta.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
//                cmbTarjeta.setItemCaptionPropertyId("nombre");
//                cmbTarjeta.setNullSelectionAllowed(false);
//                cmbTarjeta.addStyleName(ValoTheme.COMBOBOX_SMALL);
//                cmbTarjeta.setPropertyDataSource(pro);
//                cmbTarjeta.setFilteringMode(FilteringMode.CONTAINS);
//                cmbTarjeta.setWidth("250px");
//                return cmbTarjeta;
//            }
//        });
//        tblDeposito.addGeneratedColumn("colMonto", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("monto");  //Atributo del bean
//                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
//                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
//                nfd.setValue(numberFmt.format(value));
//                nfd.setWidth("150px");
//                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
//                nfd.addStyleName("align-right");
//                nfd.addValueChangeListener(new Property.ValueChangeListener() {
//                    @Override
//                    public void valueChange(Property.ValueChangeEvent event) {
//                        updateTableFooterCreditCard();
//                    }
//                });
//                return nfd;
//            }
//        });
//        tblDeposito.addGeneratedColumn("colObservaciones", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("observaciones");  //Atributo del bean
//                final TextField nfd = new TextField(pro);
//                nfd.setNullRepresentation("");
//                nfd.setWidth("85px");
//                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
//                nfd.addStyleName("align-right");
//                nfd.addValueChangeListener(new Property.ValueChangeListener() {
//                    @Override
//                    public void valueChange(Property.ValueChangeEvent event) {
//                        updateTableFooterCreditCard();
//                    }
//                });
//                return nfd;
//            }
//        });
//        tblDeposito.addGeneratedColumn("colBoleta", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("numeroboleta");  //Atributo del bean
//                final TextField nfd = new TextField(pro);
//                nfd.setNullRepresentation("");
//                nfd.setWidth("85px");
//                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
//                nfd.addStyleName("align-right");
//                nfd.addValueChangeListener(new Property.ValueChangeListener() {
//                    @Override
//                    public void valueChange(Property.ValueChangeEvent event) {
//                        updateTableFooterCreditCard();
//                    }
//                });
//                return nfd;
//            }
//        });
//        tblDeposito.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Button btnDelete = new Button(FontAwesome.TRASH);
//                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
//                btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
//                btnDelete.addClickListener(new Button.ClickListener() {
//                    @Override
//                    public void buttonClick(Button.ClickEvent event) {
//                        bcrDeposito.removeItem(itemId);
////                        List<DtoProducto> tempList = new ArrayList();
////                        for (DtoProducto deo : listTarjeta) {
////                            if (deo.getProductoId() != itemId) {
////                                tempList.add(deo);
////                            }
////                        }
////                        listTarjeta = tempList;
//                        tblDeposito.refreshRowCache();
//                        updateTableFooterCreditCard();
//                    }
//                });
//                return btnDelete;
//            }
//        });
//        tblDeposito.setVisibleColumns(new Object[]{"colMedioPago", "colMonto", "colObservaciones", "colBoleta", "colDelete"});
//        tblDeposito.setColumnHeaders(new String[]{"Medio Pago", "Monto", "Observaciones", "Boleta", "Borrar"});
//        tblDeposito.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT, Table.Align.RIGHT, Table.Align.CENTER, Table.Align.CENTER);
//    }
//
//    public void updateTableFooterCreditCard() {
//        tmpDouble = 0;
//        tmpDoubleDolar = 0;
//        tmpDoubleOther = 0;
//        Double constanteDolar = 560.00;
//        for (Integer itemId : bcrDeposito.getItemIds()) {
//            tmpDouble += bcrDeposito.getItem(itemId).getBean().getMonto();
//            if (bcrDeposito.getItem(itemId).getBean().getMediopago().getNombre().contains("Efectivo USD") || bcrDeposito.getItem(itemId).getBean().getMediopago().getNombre().contains("EFECTIVO USD")) {
//                tmpDoubleDolar += bcrDeposito.getItem(itemId).getBean().getMonto();
//            } else {
//                tmpDoubleOther += bcrDeposito.getItem(itemId).getBean().getMonto();
//            }
//        }
//        
//        System.out.println("total dolar  " + tmpDoubleDolar);
//        System.out.println("total otros  " + tmpDoubleOther);
//        tmpDouble = (tmpDoubleDolar * constanteDolar) + tmpDoubleOther;
//        System.out.println("tmpDouble total " + tmpDouble);
//        
//        tblDeposito.setFooterVisible(true);
//        tblDeposito.setColumnFooter("colMedioPago", "Total:");
//        tblDeposito.setColumnFooter("colMonto", currencySymbol + numberFmt.format(tmpDouble).trim());
//        
//        VaadinSession.getCurrent().setAttribute("detalleDeposito", bcrDeposito);
//        VaadinSession.getCurrent().setAttribute("totalDolar", tmpDoubleDolar);
//        VaadinSession.getCurrent().setAttribute("totalOtro", tmpDoubleOther);
//        VaadinSession.getCurrent().setAttribute("total", tmpDouble);
//        
//    }
//}
