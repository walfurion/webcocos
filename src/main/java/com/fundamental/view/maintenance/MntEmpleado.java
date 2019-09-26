/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.view.maintenance;

import com.fundamental.model.Empleado;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoBean;
import com.fundamental.services.Dao;
import com.fundamental.services.SvcEmpleado;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.vaadin.maddon.ListContainer;

/**
 *
 * @author Alfredo
 */
public class MntEmpleado extends Panel implements View{
    ComboBox cbxEstado;
    TextField tfdNombre = new TextField("Nombre:");
    Button btnAgregar = new Button("Agregar", FontAwesome.PLUS),
            btnGuardar;
    Table tblEmpleado;

    private BeanContainer<Integer, Empleado> bcrEmpleado = new BeanContainer<Integer, Empleado>(Empleado.class);

    Empleado empleado;
    List<DtoBean> estados = new ArrayList(Arrays.asList(
            new DtoBean(1, "ACTIVO", "A"), new DtoBean(2, "INACTIVO", "I")
    ));
    String action;

//template
    private final VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Usuario user;
    
     public MntEmpleado() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        vlRoot = new VerticalLayout();
        vlRoot.setSizeFull();
        vlRoot.setMargin(true);
        vlRoot.addStyleName("dashboard-view");
        setContent(vlRoot);
        Responsive.makeResponsive(vlRoot);
        vlRoot.setId("vlRoot");

        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        vlRoot.addComponent(utils.buildHeader("Modo de servicio", false, true));
        vlRoot.addComponent(utils.buildSeparator());
        getAllData();

        builFields();
        buildTable();
        buildButtons();

        VerticalLayout vlRight = new VerticalLayout(//utils.vlContainer(cbxPais), utils.vlContainer(cbxEstacion), 
                utils.vlContainer(tblEmpleado), utils.vlContainer(btnAgregar));
        vlRight.setSizeUndefined();
        vlRight.setId("vlRight");
        Responsive.makeResponsive(vlRight);


        VerticalLayout vlLeft = new VerticalLayout(tfdNombre, cbxEstado, //dfdHoraInicio, dfdHoraFin, 
              utils.vlContainer(btnGuardar));
        vlLeft.setSizeUndefined();
        vlLeft.setId("vlLeft");
        Responsive.makeResponsive(vlLeft);

        CssLayout cltTables = new CssLayout(vlRight, vlLeft);
        cltTables.setId("cltTables");
        cltTables.setSizeUndefined();
        Responsive.makeResponsive(cltTables);

        vlRoot.addComponents(cltTables);
        vlRoot.setExpandRatio(cltTables, 1);

    }

    public void getAllData() {
        bcrEmpleado.setBeanIdProperty("empleadoId");
        SvcEmpleado service = new SvcEmpleado();
        bcrEmpleado.removeAllItems();
        bcrEmpleado.addAll(service.getEmpleados());
        service.closeConnections();
    }

    private void builFields() {

        tfdNombre.setRequired(true);
        tfdNombre.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        cbxEstado = new ComboBox("Estado:", new ListContainer<DtoBean>(DtoBean.class, estados));
        cbxEstado.setSizeUndefined();
        cbxEstado.setItemCaptionPropertyId("nombre");
        cbxEstado.setNullSelectionAllowed(false);
        cbxEstado.setRequired(true);
        cbxEstado.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        
    }

    private void buildTable() {
        tblEmpleado = utils.buildTable("Empleados:", 100f, 100f, bcrEmpleado,
                new String[]{"nombre"},
                new String[]{"Nombre"});
        tblEmpleado.setSizeUndefined();
        tblEmpleado.setSelectable(true);
        tblEmpleado.setHeight("300px");
        tblEmpleado.setWidth("400px");
        
        tblEmpleado.setValue(bcrEmpleado.firstItemId());
        tblEmpleado.addStyleName(ValoTheme.TABLE_COMPACT);
        tblEmpleado.addStyleName(ValoTheme.TABLE_SMALL);
        tblEmpleado.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(tblEmpleado.getValue() == null) return;
                empleado =  bcrEmpleado.getItem(tblEmpleado.getValue()).getBean();
                tfdNombre.setValue(empleado.getNombre());
                cbxEstado.setValue(empleado.getEstado().equals("A")?estados.get(0):estados.get(1));
                action = Dao.ACTION_UPDATE;
            }
        });
        tblEmpleado.addGeneratedColumn("colEstado", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Label result  = new Label();
                String estado = bcrEmpleado.getItem(itemId).getBean().getEstado();
                result.setValue(estado.equals("A")?"Activo":"Inactivo");
                result.setWidth("75px");
                return result;
            }
        });
        tblEmpleado.setVisibleColumns(new Object[]{"nombre","colEstado"});
        tblEmpleado.setColumnHeaders(new String[]{"Nombre","Estado"});
        tblEmpleado.setColumnAlignments(Table.Align.LEFT,Table.Align.LEFT);
        
    }

    private void buildButtons() {
        btnAgregar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAgregar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                    empleado = new Empleado();
                    tblEmpleado.setValue(null);
                    tfdNombre.setValue("");
                    cbxEstado.setValue(estados.get(0));
                    action = Dao.ACTION_ADD;
            }
        });

        btnGuardar = new Button("Guardar", FontAwesome.SAVE);
        btnGuardar.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnGuardar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String nombre = (tfdNombre.getValue() != null && !tfdNombre.getValue().trim().isEmpty()) ? tfdNombre.getValue().trim().toUpperCase() : null;
                if (!tfdNombre.isValid()  || !cbxEstado.isValid() ) {
                    Notification.show("Todos los valores son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                empleado.setNombre(nombre);
                empleado.setEstado(((DtoBean)cbxEstado.getValue()).getEstado() );
                empleado.setCreadoPor(user.getUsername());
                empleado.setModificadoPor(user.getUsername());
           
                SvcEmpleado service = new SvcEmpleado();
                empleado = service.doAction(action, empleado);
                service.closeConnections();
                if (empleado.getEmpleadoId()> 0) {
                    Notification notif = new Notification("ÉXITO:", "El registro de empleado se realizó con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_EMPLEADO.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n", Notification.Type.ERROR_MESSAGE);
                    return;
                }
            }
        });

    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
