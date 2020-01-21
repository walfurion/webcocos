/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.view.form;

import com.fundamental.model.Turno;
import com.fundamental.model.Utils;
import com.fundamental.services.SvcDetalleTcClientes;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.GenericBeanMedioPago;
import com.sisintegrados.generic.bean.GenericDetalleFM;
import com.sisintegrados.generic.bean.GenericLote;
import com.sisintegrados.generic.bean.Usuario;
import static com.sisintegrados.view.form.FormDetalleCliDavivienda.numberFmt;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect;
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
public class FormDetalleCliScottia extends Window {

    static final DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00;-#");
    static final DecimalFormat numberFmt3D = new DecimalFormat("### ###,##0.000;-#");
    static final String HEIGHT_TABLE = "300px";
    CreateComponents components = new CreateComponents();
    Constant cons = new Constant();
    Button btnasignar = new Button();
    Button btnguardar = new Button();
    Usuario usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
    double tmpDouble;
    String currencySymbol;
    Estacion estacion;
    Integer idpais;
    Turno turno;
    SvcDetalleTcClientes dao = new SvcDetalleTcClientes();
    BeanItemContainer<Estacion> ContEstacion = new BeanItemContainer<Estacion>(Estacion.class);
    BeanItemContainer<GenericBeanMedioPago> ContMediosPago = new BeanItemContainer<GenericBeanMedioPago>(GenericBeanMedioPago.class);
    BeanItemContainer<GenericLote> ContLote = new BeanItemContainer<GenericLote>(GenericLote.class);
    BeanContainer<Integer, GenericDetalleFM> bcrDetalleCliScott = new BeanContainer<Integer, GenericDetalleFM>(GenericDetalleFM.class);
    List<GenericDetalleFM> listDetallecli = new ArrayList();
    Table tableFMScott = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("venta")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };
    Utils utils = new Utils();
    GenericDetalleFM dtaclie;
    List<GenericLote> listaGen;

    public FormDetalleCliScottia(Estacion estacion, String currencySymbol, Integer idpais, BeanContainer<Integer, GenericDetalleFM> bcrDetalleCliScott, Turno turno) {
        this.estacion = estacion;
        this.currencySymbol = currencySymbol;
        this.idpais = idpais;
        this.bcrDetalleCliScott = bcrDetalleCliScott;
        this.turno = turno;
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
        if (estacion != null) {
            ContEstacion.addAll(dao.getAllEstaciones(true, idpais));
            ContMediosPago.addAll(dao.getAllMediosPago(true, idpais));
            listaGen = dao.getAllLotesbyMedioPago(116, turno.getTurnoId());
            ContLote.addAll(listaGen);
        }
        detailsWrapper.addComponent(buildFields());
        content.addComponent(buildButtons());
    }

    private Component buildFields() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setCaption("Clientes FleetMagic Scottia");
        hl.setIcon(FontAwesome.FLAG);
        hl.setSpacing(true);
        buildTableFMDavivienda();
        hl.addComponent(tableFMScott);
        return hl;
    }

    private Component buildButtons() {
        btnasignar = new Button("Agregar");
        btnasignar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnasignar.addStyleName(ValoTheme.BUTTON_SMALL);
        btnasignar.addClickListener((Button.ClickListener) event -> {
            dtaclie = new GenericDetalleFM(utils.getRandomNumberInRange(1, 1000), new Estacion(estacion.getEstacionId(), estacion.getNombre()), new GenericBeanMedioPago(116, "TARJETA FLEET MAGIC SB"), null, "", null, "");
            dtaclie.setVenta(0D);
            listDetallecli.add(dtaclie);
            bcrDetalleCliScott.addAll(listDetallecli);
        });
        btnasignar.focus();

        btnguardar = new Button("Guardar", FontAwesome.SAVE);

        btnguardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnguardar.addStyleName(ValoTheme.BUTTON_SMALL);
        btnguardar.addClickListener((Button.ClickListener) event -> {
            if (bcrDetalleCliScott.size() <= 0) {
                bcrDetalleCliScott = new BeanContainer<Integer, GenericDetalleFM>(GenericDetalleFM.class);
            } else {
                updateTableFooterDetaCli();
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

    private void buildTableFMDavivienda() {
        bcrDetalleCliScott.setBeanIdProperty("iddet");
        tableFMScott.setContainerDataSource(bcrDetalleCliScott);
        tableFMScott.setWidth(650f, Unit.PIXELS);
        tableFMScott.setHeight(335f, Unit.PIXELS);
        tableFMScott.addStyleName(ValoTheme.TABLE_BORDERLESS);
        tableFMScott.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tableFMScott.setImmediate(true);
        tableFMScott.addGeneratedColumn("colestacion", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("estacion");  //Atributo del bean
                ComboBox cmbEstacion = new ComboBox(null, ContEstacion);
                cmbEstacion.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbEstacion.setItemCaptionPropertyId("nombre");
                cmbEstacion.setNullSelectionAllowed(false);
                cmbEstacion.addStyleName(ValoTheme.BUTTON_TINY);
                cmbEstacion.setPropertyDataSource(pro);
                cmbEstacion.setFilteringMode(FilteringMode.CONTAINS);
//                cmbEstacion.setWidth("250px");
                return cmbEstacion;
            }
        });

        tableFMScott.addGeneratedColumn("colmedio", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("mediopago");  //Atributo del bean
                ComboBox cmbMedio = new ComboBox(null, ContMediosPago);
//                cmbMedio.setReadOnly(true);
                cmbMedio.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbMedio.setItemCaptionPropertyId("nombre");
                cmbMedio.setNullSelectionAllowed(false);
                cmbMedio.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbMedio.setPropertyDataSource(pro);
                cmbMedio.setFilteringMode(FilteringMode.CONTAINS);
//                cmbMedio.setWidth("125px");
                return cmbMedio;
            }
        });

        tableFMScott.addGeneratedColumn("collote", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("genlote");  //Atributo del bean
                BeanItemContainer<GenericLote> ContLote1 = new BeanItemContainer<GenericLote>(GenericLote.class);
                ContLote1.addAll(listaGen);
                ComboBox cmbLote = new ComboBox(null, ContLote1);
//                cmbLote.setReadOnly(true);
                cmbLote.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                cmbLote.setItemCaptionPropertyId("lote");
                cmbLote.setNullSelectionAllowed(false);
                cmbLote.addStyleName(ValoTheme.COMBOBOX_TINY);
                cmbLote.setPropertyDataSource(pro);
                cmbLote.setFilteringMode(FilteringMode.CONTAINS);
                cmbLote.setWidth("85px");
                return cmbLote;
            }
        });

        tableFMScott.addGeneratedColumn("colcliente", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cliente");  //Atributo del bean
                final TextField nfd = new TextField(pro);
                nfd.setWidth("150px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_TINY);
                nfd.addStyleName("align-right");
                return nfd;
            }
        });

        tableFMScott.addGeneratedColumn("colventa", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("venta");  //Atributo del bean
                final TextField nfd = new TextField(utils.getPropertyFormatterDouble(pro));
//                nfd.setReadOnly(true);
                Double value = (pro != null && pro.getValue() != null) ? Double.parseDouble(pro.getValue().toString()) : 0D;
                nfd.setValue(numberFmt.format(value));
                nfd.setWidth("100px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_TINY);
                nfd.addStyleName("align-right");
                nfd.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        updateTableFooterDetaCli();
                    }
                });
                return nfd;
            }
        });

        tableFMScott.addGeneratedColumn("colcomentario", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("comentario");  //Atributo del bean
                final TextField nfd = new TextField(pro);
//                nfd.setReadOnly(true);
                nfd.setNullRepresentation("");
                nfd.setWidth("150px");
                nfd.addStyleName(ValoTheme.TEXTFIELD_TINY);
                nfd.addStyleName("align-right");
                return nfd;
            }
        });

        tableFMScott.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        bcrDetalleCliScott.removeItem(itemId);
                        List<GenericDetalleFM> tempList = new ArrayList();
                        for (GenericDetalleFM deo : listDetallecli) {
                            if (deo.getIddet() != itemId) {
                                tempList.add(deo);
                            }
                        }
                        listDetallecli = tempList;
                        updateTableFooterDetaCli();
                    }
                });
                return btnDelete;
            }
        });
//        tableFMDavivienda.setVisibleColumns(new Object[]{"colestacion", "colmedio", "collote", "cliente", "venta", "comentario"});
//        tableFMDavivienda.setColumnHeaders(new String[]{"Estacion", "Medio Pago", "Lote", "Cliente", "Venta", "Comentarios"});
        tableFMScott.setVisibleColumns(new Object[]{"collote", "colcliente", "colventa", "colcomentario", "colDelete"});
        tableFMScott.setColumnHeaders(new String[]{"Lote", "Cliente", "Venta", "Comentarios", "Borrar"});
//        tableFMDavivienda.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT,Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT);
        tableFMScott.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT);
        tableFMScott.setHeight(200f, Unit.PIXELS);
        tableFMScott.addStyleName(ValoTheme.TABLE_COMPACT);
        tableFMScott.addStyleName(ValoTheme.TABLE_SMALL);
    }

    public void updateTableFooterDetaCli() {
        tmpDouble = 0;
        for (Integer itemId : bcrDetalleCliScott.getItemIds()) {
            tmpDouble += bcrDetalleCliScott.getItem(itemId).getBean().getVenta();
        }
        tableFMScott.setFooterVisible(true);
        tableFMScott.setColumnFooter("colcomentario", "Total:");
        tableFMScott.setColumnFooter("colcomentario", currencySymbol + numberFmt.format(tmpDouble).trim());
    }
}
