/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.view.form;

import com.fundamental.model.Utils;
import com.fundamental.services.SvcTarjetaCredito;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.generic.bean.GenericTarjeta;
import com.sisintegrados.generic.bean.Tarjeta;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Allan G.
 */
public class FormTarjetasCredito extends Window {

    static final DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00;-#");
    static final DecimalFormat numberFmt3D = new DecimalFormat("### ###,##0.000;-#");
    static final String HEIGHT_TABLE = "300px";
    CreateComponents components = new CreateComponents();
    Constant cons = new Constant();
    Button btnasignar = new Button();
    Button btnguardar = new Button();
    Usuario usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
    Table tblCreditCard;
    BeanItemContainer<Tarjeta> ContCreditC = new BeanItemContainer<Tarjeta>(Tarjeta.class);
    BeanContainer<Integer, GenericTarjeta> bcrCreditC = new BeanContainer<Integer, GenericTarjeta>(GenericTarjeta.class);
    Utils utils = new Utils();
    SvcTarjetaCredito dao = new SvcTarjetaCredito();
    Button btnAddCreditC;
    double tmpDouble;
    String currencySymbol;
    List<GenericTarjeta> listTarjeta = new ArrayList();

    public FormTarjetasCredito(String currencySymbol, BeanContainer<Integer, GenericTarjeta> bcrCreditC) {
        this.bcrCreditC = bcrCreditC;
        this.currencySymbol = currencySymbol;
        addStyleName(Constant.stylePopUps);
        Responsive.makeResponsive(this);
        setModal(true);
        addCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(80.0f, Sizeable.Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);
        ContCreditC = new BeanItemContainer<Tarjeta>(Tarjeta.class);
        ContCreditC.addAll(dao.getTarjetas());
        detailsWrapper.addComponent(buildFields());
        content.addComponent(buildButtons());
    }

    private Component buildButtons() {
        btnasignar = new Button("Agregar");
        btnasignar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnasignar.addStyleName(ValoTheme.BUTTON_SMALL);
        btnasignar.addClickListener((Button.ClickListener) event -> {
            GenericTarjeta genericTarjeta = new GenericTarjeta(utils.getRandomNumberInRange(1, 1000), null, null, null);
            genericTarjeta.setMonto(0D);
            listTarjeta.add(genericTarjeta);
            bcrCreditC.addAll(listTarjeta);
        });
        btnasignar.focus();

        btnguardar = new Button("Guardar", FontAwesome.SAVE);

        btnguardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnguardar.addStyleName(ValoTheme.BUTTON_SMALL);
        btnguardar.addClickListener((Button.ClickListener) event -> {
            if (bcrCreditC.size() <= 0) {
                bcrCreditC = new BeanContainer<Integer, GenericTarjeta>(GenericTarjeta.class);;
                VaadinSession.getCurrent().setAttribute("detalleTarjetaCredito", bcrCreditC);
                VaadinSession.getCurrent().setAttribute("totalTarjetaCredito", 0.00);
            } else {
                updateTableFooterCreditCard();
            }
            close();
        });

        HorizontalLayout footer = (HorizontalLayout) components.createHorizontal(ValoTheme.WINDOW_BOTTOM_TOOLBAR, "", true, false, false, new Component[]{btnasignar, btnguardar});
        footer.setComponentAlignment(btnasignar, Alignment.TOP_RIGHT);
        if (!btnasignar.isVisible()) {
            footer.setComponentAlignment(btnguardar, Alignment.TOP_CENTER);
        }
        footer.setWidth(100.0f, Sizeable.Unit.PERCENTAGE);
        return footer;
    }

    private Component buildFields() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setCaption("Tarjetas de credito");
        hl.setIcon(FontAwesome.FLAG);
        hl.setSpacing(true);
        buildTableCreditCard();
        hl.addComponent(tblCreditCard);
        return hl;
    }

    public void buildTableCreditCard() {
        bcrCreditC.setBeanIdProperty("idGenerico");
        tblCreditCard = new Table();
        tblCreditCard.setContainerDataSource(bcrCreditC);
        tblCreditCard.setWidth(650f, Unit.PIXELS);
        tblCreditCard.setHeight(335f, Unit.PIXELS);

        tblCreditCard.addStyleName(ValoTheme.TABLE_BORDERLESS);
        tblCreditCard.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblCreditCard.setImmediate(true);
        tblCreditCard.addGeneratedColumn("colTarjeta", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("tarjeta");  //Atributo del bean
                ComboBox cmbTarjeta = utils.buildCombobox("", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, ContCreditC);
                cmbTarjeta.setPropertyDataSource(pro);
                cmbTarjeta.setFilteringMode(FilteringMode.CONTAINS);
                cmbTarjeta.setWidth("250px");
                return cmbTarjeta;
            }
        });
        tblCreditCard.addGeneratedColumn("colMonto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("monto");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
                nfd.setValue(numberFmt.format(value));
                nfd.setWidth("150px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        updateTableFooterCreditCard();
                    }
                });
                return nfd;
            }
        });
        tblCreditCard.addGeneratedColumn("colLote", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("lote");  //Atributo del bean
                final TextField nfd = new TextField(pro);
                nfd.setNullRepresentation("");
                nfd.setWidth("85px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        updateTableFooterCreditCard();
                    }
                });
                return nfd;
            }
        });
        tblCreditCard.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        bcrCreditC.removeItem(itemId);
//                        List<DtoProducto> tempList = new ArrayList();
//                        for (DtoProducto deo : listTarjeta) {
//                            if (deo.getProductoId() != itemId) {
//                                tempList.add(deo);
//                            }
//                        }
//                        listTarjeta = tempList;
                        tblCreditCard.refreshRowCache();
                        updateTableFooterCreditCard();
                    }
                });
                return btnDelete;
            }
        });
        tblCreditCard.setVisibleColumns(new Object[]{"colTarjeta", "colLote", "colMonto", "colDelete"});
        tblCreditCard.setColumnHeaders(new String[]{"Tarjeta", "Lote", "Monto", "Borrar"});
        tblCreditCard.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT, Table.Align.RIGHT, Table.Align.CENTER);
    }

    public void updateTableFooterCreditCard() {
        tmpDouble = 0;
        for (Integer itemId : bcrCreditC.getItemIds()) {
            tmpDouble += bcrCreditC.getItem(itemId).getBean().getMonto();
        }
        tblCreditCard.setFooterVisible(true);
        tblCreditCard.setColumnFooter("colCliente", "Total:");
        tblCreditCard.setColumnFooter("colMonto", currencySymbol + numberFmt.format(tmpDouble).trim());
        VaadinSession.getCurrent().setAttribute("detalleTarjetaCredito", bcrCreditC);
        VaadinSession.getCurrent().setAttribute("totalTarjetaCredito", tmpDouble);
    }
}
