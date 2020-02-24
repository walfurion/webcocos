package com.vaadin.demo.dashboard.view;

import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.sisintegrados.dao.Dao;
import com.fundamental.services.SvcMaintenance;
import com.fundamental.utils.EnvioCorreo;
import com.fundamental.utils.Mail;
import com.fundamental.utils.PasswordGenerator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Henry Barrientos
 */
public class restorePasswordWindow  extends Window {
    PasswordGenerator pg = new PasswordGenerator();
    EnvioCorreo ec = new EnvioCorreo();
    private TextField userName;
//    private TextField email;
    private final Utils utils;

    private restorePasswordWindow() {
        utils = new Utils();
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(true);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        content.addComponent(buildProfileTab());
        content.setSizeUndefined();
    }

    private Component buildProfileTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("Recuperar Credenciales");
        root.setIcon(FontAwesome.USER);
        root.setSpacing(true);
        root.setMargin(true);
        root.addStyleName("fields");

        userName = utils.buildTextField("Usuario o Correo:", "", false, 50, true, ValoTheme.TEXTFIELD_SMALL);
//        email = utils.buildTextField("Correo:", "", false, 100, true, ValoTheme.TEXTFIELD_SMALL);
//        email.addValidator(new RegexpValidator("^[_a-z0-9-]+(.[_a-z0-9-]+)*@*([a-z0-9-]+)(.[a-z0-9-]+)*(.[a-z]{2,4})?", "Correo Inválido"));        
        Button btnAceptar = new Button("Recuperar", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String password = "";
                if (userName.isValid()) {
                    SvcMaintenance service = new SvcMaintenance();
                    Usuario user = service.getUserByUsernameEmail(userName.getValue());
                    if (user != null) {
                        password = PasswordGenerator.getPassword(8);
                        user.setClave(password);
//                        service.doActionUser(Dao.ACTION_UPDATE, user);
                        service.updateUsuario(user.getUsuarioId(), user.getClave());
                        service.closeConnections();
                        String mensaje = service.getMensaje();
                        String mensajeInicial = mensaje.replaceAll("XXXXX", user.getUsername());
                        String mensajeFinal = mensajeInicial.replaceAll("YYYYY", password);
//                        String message = "Estimado colaborador.<br/>"
//                                + "Se ha solicitado la recuperación de clave desde la plataforma Web Cocos, a continuación los datos solicitados:<br/>"
//                                + "<br/>Nombre de Usuario: " + user.getUsername()
//                                + "<br/>Password:  " + password;
                        ec.enviarMail(user.getCorreo(), mensajeFinal, "Actualizacion de Clave");
//                        Mail mail = new Mail(user.getCorreo(), "Web COCOs - Recuperación de clave", message, null);
//                        mail.run();
                        Notification notif = new Notification("¡Exito!", "Se envió un correo con el cambio de contraseña", Notification.Type.TRAY_NOTIFICATION);
                        notif.setDelayMsec(3000);
                        notif.setPosition(Position.MIDDLE_CENTER);
                        notif.show(Page.getCurrent());
                        close();
                    } else {
                        service.closeConnections();
                        Notification.show("El usuario y correo no están registrados", Notification.Type.ERROR_MESSAGE);
                    }
                } else {
                    Notification.show("Se requiere nombre de usuario y correo", Notification.Type.ERROR_MESSAGE);
                }
            }
        });
        btnAceptar.addStyleName(ValoTheme.BUTTON_SMALL);
        btnAceptar.addStyleName(ValoTheme.BUTTON_PRIMARY);

        root.addComponents(userName, btnAceptar);

        return root;
    }

    public static void open() {
        DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
        Window w = new restorePasswordWindow();
        UI.getCurrent().addWindow(w);
        w.focus();
    }
}
