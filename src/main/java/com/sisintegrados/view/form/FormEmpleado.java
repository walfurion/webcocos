/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.view.form;

import com.fundamental.services.SvcEmpleado;
import com.fundamental.services.SvcTurno;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.sisintegrados.generic.bean.Empleado;
import com.sisintegrados.generic.bean.GenericEstado;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author Allan G.
 */
public class FormEmpleado extends Window {

    CreateComponents components = new CreateComponents();
    Constant cons = new Constant();
    SvcEmpleado dao = new SvcEmpleado();
    SvcTurno dao2 = new SvcTurno();
    Button guardar = new Button();
    Button cancelar = new Button();
    String tipo;
    VerticalLayout v = new VerticalLayout();
    ComboBox cmbEstado = new ComboBox();
    ComboBox cmbEmpleado = new ComboBox();
    TextField nombre = new TextField();
    Usuario usuario = VaadinSession.getCurrent().getAttribute(Usuario.class);
    BeanItemContainer<GenericEstado> contEstado = new BeanItemContainer<GenericEstado>(GenericEstado.class);
    BeanItemContainer<Empleado> contEmpleado = new BeanItemContainer<Empleado>(Empleado.class);
    Empleado empleado = new Empleado();

    public FormEmpleado(String tipo, Empleado empleado) {
        this.tipo = tipo;
        this.empleado = empleado;
        cargaEstados();
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
        cargaEmpleado();
    }

    private Component buildButtons() {
        guardar = new Button("Guardar");
        guardar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        guardar.addStyleName(ValoTheme.BUTTON_SMALL);
        guardar.addClickListener((Button.ClickListener) event -> {
            if (nombre.getValue() != null && cmbEstado.getValue() != null) {
                try {
                    if (tipo.equals("Nuevo")) {
                        GenericEstado estado = new GenericEstado();
                        estado = (GenericEstado) cmbEstado.getValue();
                        dao.insert(nombre.getValue().toUpperCase().trim(), estado.getEstado(), usuario.getUsername());
                        components.crearNotificacion("Datos ingresados correctamente!!!");
                        DashboardEventBus.post(new DashboardEvent.ProfileUpdatedEvent());
                        close();
                    } else {
                        GenericEstado estado = new GenericEstado();
                        estado = (GenericEstado) cmbEstado.getValue();
                        if (empleado != null) {
                            dao.update(empleado.getEmpleadoId(), nombre.getValue().toUpperCase().trim(), estado.getEstado(), usuario.getUsername());
                            components.crearNotificacion("Datos modificados correctamente!!!");
                            DashboardEventBus.post(new DashboardEvent.ProfileUpdatedEvent());
                            close();
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Notification.show("Error!!!", "Se deben ingresar todos los datos.", Notification.Type.ERROR_MESSAGE);
                return;
            }
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
        regresa.setCaption("Empleados");
        regresa.setIcon(FontAwesome.FLAG);
        FormLayout form = new FormLayout();
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        regresa.addComponent(form);
        regresa.setExpandRatio(form, 1);

        HorizontalLayout hlDoc1 = (HorizontalLayout) components.createHorizontal(Constant.styleForm, "100%", false, false, false, null);
        hlDoc1.setHeight("55px");

        v.setSpacing(true);
        nombre = new TextField("Nombre Empleado");
        nombre.setRequired(true);
        nombre.setMaxLength(60);
        nombre.setRequiredError("Debe ingresar un nombre y un apellido");
        nombre.setStyleName(ValoTheme.TEXTFIELD_SMALL);

        cmbEstado = new ComboBox("Estado", contEstado);
        cmbEstado.setItemCaptionPropertyId("nombre");
        cmbEstado.setStyleName(ValoTheme.COMBOBOX_SMALL);
        cmbEstado.setRequired(true);
        cmbEstado.setNullSelectionAllowed(false);
        cmbEstado.setRequiredError("Debe seleccionar un estado");

        form.addComponent(nombre);
        form.addComponent(cmbEstado);
        return regresa;
    }

    public void cargaEmpleado() {
        if (empleado != null) {
            nombre.setValue(empleado.getNombre());
            for (GenericEstado estado : contEstado.getItemIds()) {
                if (estado.getEstado().equals(empleado.getEstado())) {
                    cmbEstado.setValue(estado);
                }
            }
        }
    }

    public void cargaEstados() {
        contEstado = new BeanItemContainer<GenericEstado>(GenericEstado.class);
        contEstado.addBean(new GenericEstado("A", "ACTIVO", usuario.getUsername()));
        contEstado.addBean(new GenericEstado("I", "INACTIVO", usuario.getUsername()));
    }
}
