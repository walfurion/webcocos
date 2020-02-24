package com.vaadin.demo.dashboard.view;

import com.fundamental.model.Acceso;
import com.sisintegrados.dao.Dao;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.services.SvcUsuario;
import com.fundamental.utils.Constant;
import com.sisintegrados.daoimp.DaoImp;
import com.vaadin.demo.dashboard.event.DashboardEvent.UserLoginRequestedEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout {

    Button restorePassword = new Button("Recuperar credenciales");

    public LoginView() {
        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(false);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        CssLayout clt = (CssLayout) buildLabels();
        loginPanel.addComponent(clt);
        loginPanel.addComponent(buildFields());
        loginPanel.setComponentAlignment(clt, Alignment.BOTTOM_CENTER);

        return loginPanel;
    }

    private Component buildFields() {
        VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Usuario");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        username.focus();
//        username.setVisible(false);

        final PasswordField password = new PasswordField("Clave");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Ingresar");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);

        restorePassword.setIcon(FontAwesome.REFRESH);
        restorePassword.addStyleName(ValoTheme.LINK_SMALL);
        restorePassword.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        restorePassword.addClickListener((final ClickEvent event) -> {
            restorePasswordWindow.open();
        });

        Label lblVersion = new Label("v: 1.0");
        lblVersion.addStyleName(ValoTheme.LABEL_TINY);
        lblVersion.addStyleName(ValoTheme.LABEL_BOLD);
        lblVersion.setWidth("50px");

        fields.addComponents(
                //                cbRol,
                username,
                //                cbPais, cbEstacion, name,
                password,
                restorePassword,
                signin,
                lblVersion);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
        fields.setComponentAlignment(restorePassword, Alignment.BOTTOM_RIGHT);
        fields.setComponentAlignment(lblVersion, Alignment.MIDDLE_CENTER);

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                boolean validData = false;
                String usuario = ""//, selectedRol = (String) cbRol.getValue()
                        ;
                usuario = username.getValue();
                if (!usuario.isEmpty() && !password.getValue().isEmpty()) {
                    validData = true;
                } else {
                    Notification.show("Faltan uno o más datos de autenticación.", Notification.Type.ERROR_MESSAGE);
                    return;
                }

                if (validData) {
                    SvcUsuario svcUsuario = new SvcUsuario();
                    Usuario user = svcUsuario.getUserByUserPass(usuario, password.getValue());
//                    List<Acceso> misAccessos = svcUsuario.getAccesosByUsuarioid(user.getUsuarioId(), user.isSysadmin());
                    svcUsuario.closeConnections();
//                    user.getRoles().get(0).setAccesos(misAccessos);
//                    for (Acceso a : misAccessos) {
////                        System.out.println("LoginView "+a.getTitulo()+" "+a.isVer()+" "+a.isAgregar()+" "+a.isCambiar()+" "+a.isEliminar());
//                    }

                    if (user.getUsuarioId() != null && user.getUsuarioId() > 0) {
                        String myNombreLogin = user.getNombre().concat(" ").concat(user.getApellido());
                        user.setNombreLogin(myNombreLogin);
                        user.setRolLogin(Constant.ROL_LOGIN_SUPERVISOR);
                        DashboardEventBus.post(new UserLoginRequestedEvent(user));
                    } else {
                        Notification.show("Por favor verifique su usuario y clave.", Notification.Type.ERROR_MESSAGE);
                    }
                }
            }
        });

        return fields;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
        Label welcome = new Label("Estaciones COCO");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H2);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);
        return labels;
    }

}
