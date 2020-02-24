/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.view.maintenance;

import com.fundamental.model.Acceso;
import com.fundamental.model.Utils;
import com.sisintegrados.dao.Dao;
import com.fundamental.services.SvcMaintenance;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjosue
 */
public class CambioClave extends Panel implements View {

    Button btnSave, btnLimpiar, btnAdd;

    @PropertyId("clavenueva")
    PasswordField clavenueva;
    @PropertyId("confirmaclave")
    PasswordField confirmaclave;

    Acceso acceso = new Acceso();
    private VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Usuario user;
    BeanFieldGroup<Usuario> binder = new BeanFieldGroup<Usuario>(Usuario.class);

    Usuario usuarioActualizar;

    public CambioClave() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        vlRoot = new VerticalLayout();
        vlRoot.setSizeFull();
//        vlRoot.setSizeUndefined();
        vlRoot.setMargin(true);
        vlRoot.addStyleName("dashboard-view");
        setContent(vlRoot);
        Responsive.makeResponsive(vlRoot);
        vlRoot.setId("vlRoot");

        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        vlRoot.addComponent(utils.buildHeader("Cambio Clave", false, true));
        vlRoot.addComponent(utils.buildSeparator());

        buildFields();
        
        buildButtons();


        VerticalLayout vlLeft = new VerticalLayout(clavenueva, confirmaclave, btnSave);
        vlLeft.setSizeUndefined();
        vlLeft.setId("vlLeft");
        vlLeft.setComponentAlignment(btnSave, Alignment.BOTTOM_CENTER);
        vlLeft.setMargin(new MarginInfo(false, true, false, false));
        Responsive.makeResponsive(vlLeft);


        CssLayout cltTables = new CssLayout(vlLeft);
        cltTables.setId("cltTables");
        cltTables.setSizeUndefined();
        Responsive.makeResponsive(cltTables);

        vlRoot.addComponents(cltTables);
        vlRoot.setExpandRatio(cltTables, 1);

    }

    private void buildFields() {
        clavenueva = utils.buildPasswordField("Nueva clave", "", false, 45, true, ValoTheme.TEXTFIELD_SMALL);
        clavenueva.setWidth("350px");
        confirmaclave = utils.buildPasswordField("Confirmar Clave", "", false, 45, true, ValoTheme.TEXTFIELD_SMALL);
        clavenueva.setWidth("350px");
    }

    private void buildButtons() {

        btnSave = new Button("Guardar", FontAwesome.SAVE);
        btnSave.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSave.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    binder.commit();
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(MntProducto.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!binder.isValid()) {
                    Notification.show("Por favor, todos los campos son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }

                if (clavenueva.getValue().toString().trim().equals(confirmaclave.getValue().toString().trim())) {
                    SvcMaintenance service = new SvcMaintenance();
                    try {
                        usuarioActualizar = user;
                        usuarioActualizar.setModificadoPor(user.getUsername());
                        usuarioActualizar.setClave(clavenueva.getValue().toString());
                        usuarioActualizar.setUsuarioId(user.getUsuarioId());
                        System.out.println("usuarioActualizar " + usuarioActualizar.toString());

                        service.doActionPassword(usuarioActualizar);
                        service.closeConnections();
                        Notification notif = new Notification("¡Exito!", "La acción se ha ejecutado correctamente.", Notification.Type.HUMANIZED_MESSAGE);
                        notif.setDelayMsec(3000);
                        notif.setPosition(Position.MIDDLE_CENTER);
                        notif.show(Page.getCurrent());
                        UI.getCurrent().getNavigator().navigateTo(DashboardViewType.CAMBIO_CLAVE.getViewName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        service.closeConnections();
                        Notification.show("Ocurrió un error al ejecutar la acción. \n", Notification.Type.ERROR_MESSAGE);
                    }

                } else {
                        Notification.show("Las contraseñas ingresadas no coinciden", Notification.Type.ERROR_MESSAGE);
                }
                        
            }
        });
    }


  @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new DaoImp();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnSave.setEnabled(acceso.isCambiar());
    }

}
