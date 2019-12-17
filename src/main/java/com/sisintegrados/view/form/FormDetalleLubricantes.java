/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.view.form;

import com.fundamental.model.Cliente;
import com.fundamental.model.Lubricanteprecio;
import com.fundamental.model.Mediopago;
import com.fundamental.model.Producto;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoProducto;
import com.fundamental.services.SvcCuadre;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.generic.bean.GenericProduct;
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
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.vaadin.maddon.ListContainer;

/**
 *
 * @author Jorge
 */
public class FormDetalleLubricantes extends Window {

    static final DecimalFormat numberFmt = new DecimalFormat("### ###,##0.00;-#");
    static final DecimalFormat numberFmt3D = new DecimalFormat("### ###,##0.000;-#");
    static final String HEIGHT_TABLE = "300px";
    CreateComponents components = new CreateComponents();
    Constant cons = new Constant();
    Button btnasignar = new Button();
    Button btnguardar = new Button();
    Usuario usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
    Table tblPrepaid;
    Table tblLubricantes;
    BeanContainer<Integer, DtoProducto> bcrLubs = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
    BeanContainer<Integer, Producto> bcrProducto = new BeanContainer<Integer, Producto>(Producto.class);

//    BeanContainer<Integer, GenericProduct> bcrLubs = new BeanContainer<Integer, GenericProduct>(GenericProduct.class);
    //  BeanContainer<Integer, GenericProduct> bcrProducto = new BeanContainer<Integer, GenericProduct>(GenericProduct.class);
    Container contCustomerPrepaid = new ListContainer<>(Producto.class, new ArrayList());
    // BeanContainer<Integer, Mediopago> bcrMediopago = new BeanContainer<Integer, Mediopago>(Mediopago.class);
    List<DtoProducto> listLubs = new ArrayList();
    Utils utils = new Utils();
    SvcCuadre dao = new SvcCuadre();
    Button btnAddLubs;
    double tmpDouble;
    double tmpLubsUno;
    double tmpLubsOtros;
    String currencySymbol;
    Integer idestacion;
    Integer idpais;
    int disponibilidad;
    double diferencia;
    Date fechaQuery;
    
    SvcCuadre daoDispInve = new SvcCuadre();
    //DateField fecha = new DateField("Fecha");
DateField dfdFecha = new DateField("Fecha:");
    int tmpInt;
    int tmpIntUno;
    int tmpIntNoUno;
    // Container contLubs = new ListContainer<>(Producto.class, new ArrayList());
    Container contLubs = new ListContainer<>(GenericProduct.class, new ArrayList());
    SvcCuadre service = new SvcCuadre();

    public FormDetalleLubricantes(Integer idestacion, String currencySymbol, Integer idpais, BeanContainer<Integer, DtoProducto> bcrLubs, Date fechaQuery) {
        this.fechaQuery = fechaQuery;
        this.idestacion = idestacion;
        this.currencySymbol = currencySymbol;
        this.idpais = idpais;
        this.bcrLubs = bcrLubs;
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
            //    contCustomerPrepaid = new ListContainer<Producto>(Producto.class, dao.getLubricantsByCountryStation(true, idestacion, idpais));
            contLubs = new ListContainer<Producto>(Producto.class, service.getLubricantsGenericsCountryStation(idpais, idestacion));

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
            listLubs.add(dtoprod);
            bcrLubs.addAll(listLubs);
        });
        btnasignar.focus();

        btnguardar = new Button("Guardar", FontAwesome.SAVE);

        btnguardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnguardar.addStyleName(ValoTheme.BUTTON_SMALL);
        btnguardar.addClickListener((Button.ClickListener) event -> {

            for (Integer itemId : bcrLubs.getItemIds()) {
                bcrLubs.getItem(itemId).getBean().getProducto().getProductoId();
                bcrLubs.getItem(itemId).getBean().getCantidad(); //Venta
                //fechaQuery
                disponibilidad = daoDispInve.recuperaDisponibilidadInventario(fechaQuery,bcrLubs.getItem(itemId).getBean().getProducto().getProductoId());
                diferencia = disponibilidad - bcrLubs.getItem(itemId).getBean().getCantidad();
                if(diferencia < 0){  
                    Notification.show("VENTA LUBRICANTE CON INVENTARIO NEGATIVO.\n", Notification.Type.ERROR_MESSAGE);
                }
            }
            if (bcrLubs.size() <= 0) {
                bcrLubs = new BeanContainer<Integer, DtoProducto>(DtoProducto.class);
                VaadinSession.getCurrent().setAttribute("detalleProducto", bcrLubs);
                VaadinSession.getCurrent().setAttribute("totalProd", 0.00);
                VaadinSession.getCurrent().setAttribute("totalProductoUno", 0.00);
                VaadinSession.getCurrent().setAttribute("totalProducto", 0.00);
            } else {
                updateTableFooterLubs();
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
        hl.setCaption("Lubricantes");
        hl.setIcon(FontAwesome.FLAG);
        hl.setSpacing(true);
        buildTablePrepago();
        hl.addComponent(tblLubricantes);
        return hl;
    }

    public void buildTablePrepago() {
//        SvcCuadre service = new SvcCuadre();
//        List<Producto> prodAdicionales = service.getProdAdicionalesByEstacionid(idestacion);
//        bcrProducto.setBeanIdProperty("productoId");
//        bcrProducto.addAll(prodAdicionales);

        bcrLubs.setBeanIdProperty("productoId");
        tblLubricantes = new Table();
        tblLubricantes.setContainerDataSource(bcrLubs);
        tblLubricantes.setWidth(650f, Sizeable.Unit.PIXELS);
        tblLubricantes.setHeight(335f, Sizeable.Unit.PIXELS);

        tblLubricantes.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tblLubricantes.addStyleName(ValoTheme.TABLE_COMPACT);
        //   tblLubricantes.addStyleName(ValoTheme.TABLE_SMALL);
        tblLubricantes.setImmediate(true);
        tblLubricantes.addGeneratedColumn("colProducto", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("producto");  //Atributo del bean
                if (idestacion != null) {
                    contLubs = new ListContainer<Producto>(Producto.class, service.getLubricantsGenericsCountryStation(idpais, idestacion));
                }
                final ComboBox cbxProducto = utils.buildCombobox("", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, contLubs);
                cbxProducto.setPropertyDataSource(pro);
                cbxProducto.setFilteringMode(FilteringMode.CONTAINS);
                cbxProducto.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        bcrLubs.getItem(itemId).getItemProperty("valor").setValue(((Producto) cbxProducto.getValue()).getPrecio());
                        bcrLubs.getItem(itemId).getItemProperty("idmarca").setValue(((Producto) cbxProducto.getValue()).getIdMarca());
                    }
                });
                return cbxProducto;
            }
        });
        tblLubricantes.addGeneratedColumn("colCantidad", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cantidad");  //Atributo del bean
                TextField nfd = new TextField(pro);
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

        tblLubricantes.addGeneratedColumn("colTipo", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("idmarca");  //Atributo del bean
                TextField ntipo = new TextField(pro);
                ntipo.setWidth("60px");
                ntipo.addStyleName(ValoTheme.TEXTFIELD_SMALL);
                ntipo.addStyleName("align-right");
                return ntipo;
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
        tblLubricantes.setColumnAlignments(Table.Align.LEFT, Table.Align.RIGHT, Table.Align.CENTER, Table.Align.RIGHT, Table.Align.RIGHT);

    }

    public void updateTableFooterLubs() {
        tmpInt = 0;
        tmpDouble = 0;

        Double totalUno = 0.00;
        Double totalNoUno = 0.00;
        for (Integer itemId : bcrLubs.getItemIds()) {
            tmpDouble += bcrLubs.getItem(itemId).getBean().getValor();
            tmpInt += bcrLubs.getItem(itemId).getBean().getCantidad();
        }

        for (Integer itemId : bcrLubs.getItemIds()) {
            tmpLubsUno = 0;
            tmpIntUno = 0;
            tmpLubsOtros = 0;
            tmpIntNoUno = 0;
            System.out.println("MARCA " + bcrLubs.getItem(itemId).getBean().getIdmarca());
            if (bcrLubs.getItem(itemId).getBean().getIdmarca() == 100) {
                tmpLubsUno = bcrLubs.getItem(itemId).getBean().getValor();
                tmpIntUno = bcrLubs.getItem(itemId).getBean().getCantidad();
                totalUno += tmpIntUno * tmpLubsUno;
            } else {
                tmpLubsOtros = bcrLubs.getItem(itemId).getBean().getValor();
                tmpIntNoUno = bcrLubs.getItem(itemId).getBean().getCantidad();
                totalNoUno += tmpLubsOtros * tmpIntNoUno;
            }
        }

        tblLubricantes.setFooterVisible(true);
        tblLubricantes.setColumnFooter("colProducto", "Total:");
        tblLubricantes.setColumnFooter("colCantidad", Integer.toString(tmpInt));
        tblLubricantes.setColumnFooter("total", currencySymbol + numberFmt.format(totalUno + totalNoUno).trim());

        System.out.println("TOTALES " + totalUno + " TOTAL NO UNO " + totalNoUno);

        VaadinSession.getCurrent().setAttribute("detalleProducto", bcrLubs);
        VaadinSession.getCurrent().setAttribute("totalProd", tmpDouble);
        VaadinSession.getCurrent().setAttribute("totalProductoUno", totalUno);
        VaadinSession.getCurrent().setAttribute("totalProducto", totalNoUno);
        
    }
}
