/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.view.form;

import com.fundamental.model.Cliente;
import com.fundamental.model.Mediopago;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoProducto;
import com.fundamental.services.SvcCuadre;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
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
import org.vaadin.maddon.ListContainer;

/**
 *
 * @author Mery
 */
public class FormClientesCredito extends Window {

    static final DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00;-#");
    static final DecimalFormat numberFmt3D = new DecimalFormat("### ###,##0.000;-#");
    static final String HEIGHT_TABLE = "300px";
    CreateComponents components = new CreateComponents();
    Constant cons = new Constant();
    Button btnasignar = new Button();
    Button btnguardar = new Button();
    Usuario usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
    Table tblCxC;
    BeanContainer<Integer, DtoProducto> bcrClientes = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
    Container contCustomerCredit = new ListContainer<>(Cliente.class, new ArrayList());
    BeanContainer<Integer, Mediopago> bcrMediopago = new BeanContainer<Integer, Mediopago>(Mediopago.class);
    List<DtoProducto> listCustomers = new ArrayList();
    Utils utils = new Utils();
    SvcCuadre dao = new SvcCuadre();
    Button btnAddCustomer;
    double tmpDouble;
    String currencySymbol;
    Integer idestacion;
    Integer idpais;

    public FormClientesCredito(Integer idestacion, String currencySymbol, Integer idpais, BeanContainer<Integer, DtoProducto> bcrClientes) {
        this.idestacion = idestacion;
        this.currencySymbol = currencySymbol;
        this.idpais = idpais;
        this.bcrClientes = bcrClientes;
        addStyleName(Constant.stylePopUps);
        Responsive.makeResponsive(this);
        setModal(true);
        addCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(80.0f, Sizeable.Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
//        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);
        if (idestacion != null) {
            contCustomerCredit = new ListContainer<Cliente>(Cliente.class, dao.getCustomersByStationidType(idestacion, "C"));
            bcrMediopago.setBeanIdProperty("mediopagoId");
            bcrMediopago.addAll(dao.getMediospagoByPaisidTipoid(idpais, 1));   //genericos
        }
        detailsWrapper.addComponent(buildFields());
        content.addComponent(buildButtons());
    }

    private Component buildButtons() {
        btnasignar = new Button("Agregar");
        btnasignar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnasignar.addStyleName(ValoTheme.BUTTON_SMALL);
        btnasignar.addClickListener((Button.ClickListener) event -> {
//            bcrPrepaid.removeAllItems();
            DtoProducto dtoprod = new DtoProducto(utils.getRandomNumberInRange(1, 1000), null, null);
            dtoprod.setValor(0D);
            listCustomers.add(dtoprod);
            bcrClientes.addAll(listCustomers);
        });
        btnasignar.focus();

        btnguardar = new Button("Guardar", FontAwesome.SAVE);

        btnguardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnguardar.addStyleName(ValoTheme.BUTTON_SMALL);
        btnguardar.addClickListener((Button.ClickListener) event -> {
            if (bcrClientes.size() <= 0) {
                bcrClientes = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
                VaadinSession.getCurrent().setAttribute("detalleCredito", bcrClientes);
                VaadinSession.getCurrent().setAttribute("totalCredito", 0.00);
            } else {
                updateTableFooterCxC();
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
        hl.setCaption("Clientes Credito");
        hl.setIcon(FontAwesome.FLAG);
        hl.setSpacing(true);
        buildTableCxC();
        hl.addComponent(tblCxC);
        return hl;
    }

    public void buildTableCxC() {
        bcrClientes.setBeanIdProperty("productoId");
        tblCxC = new Table();
        tblCxC.setContainerDataSource(bcrClientes);
        tblCxC.setWidth(650f, Unit.PIXELS);
        tblCxC.setHeight(335f, Unit.PIXELS);

        tblCxC.addStyleName(ValoTheme.TABLE_BORDERLESS);
        tblCxC.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
//        tblPrepaid.addStyleName(ValoTheme.TABLE_SMALL);
        tblCxC.setImmediate(true);
        tblCxC.addGeneratedColumn("colCliente", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cliente");  //Atributo del bean
                if (idestacion != null) {
                    contCustomerCredit = new ListContainer<Cliente>(Cliente.class, dao.getCustomersByStationidType(idestacion, "C"));
                }
                ComboBox cbxCliente = utils.buildCombobox("", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, contCustomerCredit);
                cbxCliente.setPropertyDataSource(pro);
                cbxCliente.setFilteringMode(FilteringMode.CONTAINS);
                cbxCliente.setWidth("250px");
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
                nfd.setWidth("200px");
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

//        btnAddPrep = new Button(FontAwesome.PLUS_CIRCLE);
//        btnAddPrep.addStyleName(ValoTheme.BUTTON_TINY);
//        btnAddPrep.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
//        btnAddPrep.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                bcrPrepaid.removeAllItems();
//                DtoProducto dtoprod = new DtoProducto(utils.getRandomNumberInRange(1, 1000), null, null);
//                dtoprod.setValor(0D);
//                listPrepaid.add(dtoprod);
//                bcrPrepaid.addAll(listPrepaid);
//            }
//        });
    }

    public void updateTableFooterCxC() {
        tmpDouble = 0;
        for (Integer itemId : bcrClientes.getItemIds()) {
            tmpDouble += bcrClientes.getItem(itemId).getBean().getValor();
        }
        tblCxC.setFooterVisible(true);
        tblCxC.setColumnFooter("colCliente", "Total:");
        tblCxC.setColumnFooter("colMonto", currencySymbol + numberFmt.format(tmpDouble).trim());
        VaadinSession.getCurrent().setAttribute("detalleCredito", bcrClientes);
        VaadinSession.getCurrent().setAttribute("totalCredito", tmpDouble);
    }
}
