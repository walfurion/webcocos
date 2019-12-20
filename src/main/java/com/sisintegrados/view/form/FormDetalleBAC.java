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
import com.sisintegrados.generic.bean.GenericDetalleBCR;
import com.sisintegrados.generic.bean.GenericLote;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author m
 */
public class FormDetalleBAC extends Window{
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
    BeanContainer<Integer, GenericDetalleBCR> bcrDetalle = new BeanContainer<Integer, GenericDetalleBCR>(GenericDetalleBCR.class);
    List<GenericDetalleBCR> listDetallecli = new ArrayList();
    Table tableBAC = new Table() {
        @Override
        protected String formatPropertyValue(Object rowId, Object colId, Property property) {
            if (colId.equals("venta")) {
                return numberFmt.format((property.getValue() == null) ? 0D : property.getValue());
            }
            return super.formatPropertyValue(rowId, colId, property);
        }
    };
    
    Utils utils = new Utils();
    
    public FormDetalleBAC(Estacion estacion, String currencySymbol, Integer idpais, BeanContainer<Integer, GenericDetalleBCR> bcrDetalle, Turno turno) {
        this.estacion = estacion;
        this.currencySymbol = currencySymbol;
        this.idpais = idpais;
        this.bcrDetalle = bcrDetalle;
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
            ContLote.addAll(dao.getAllLotesbyMedioPago(115,turno.getTurnoId()));
        }
        detailsWrapper.addComponent(buildFields());
        content.addComponent(buildButtons());
    }
    
    private Component buildFields() {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setCaption("Clientes FleetMagic Scottia");
        hl.setIcon(FontAwesome.FLAG);
        hl.setSpacing(true);
//        buildTableBAC;
        hl.addComponent(tableBAC);
        return hl;
    }

    private Component buildButtons() {
        btnasignar = new Button("Agregar");
        btnasignar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnasignar.addStyleName(ValoTheme.BUTTON_SMALL);
        btnasignar.addClickListener((Button.ClickListener) event -> {
            GenericDetalleBCR dtaclie = new GenericDetalleBCR(new Estacion(estacion.getEstacionId(),"",);
            dtaclie.setVenta(0D);
            listDetallecli.add(dtaclie);
            bcrDetalle.addAll(listDetallecli);
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
}
