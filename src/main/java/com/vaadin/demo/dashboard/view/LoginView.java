package com.vaadin.demo.dashboard.view;

import com.fundamental.model.Acceso;
import com.fundamental.services.Dao;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.services.SvcUsuario;
import com.fundamental.utils.Constant;
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

//        Notification notification = new Notification(
//                "Welcome to Dashboard Demo");
//        notification
//                .setDescription("<span>This application is not real, it only demonstrates an application built with the <a href=\"https://vaadin.com\">Vaadin framework</a>.</span> <span>No username or password is required, just click the <b>Sign In</b> button to continue.</span>");
//        notification.setHtmlContentAllowed(true);
//        notification.setStyleName("tray dark small closable login-help");
//        notification.setPosition(Position.BOTTOM_CENTER);
//        notification.setDelayMsec(20000);
//        notification.show(Page.getCurrent());
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

//        Button btnForgotPass = new Button("¿Olvidó su clave?");
//        btnForgotPass.setStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
////        btnForgotPass.setStyleName("rightCaption");
//        btnForgotPass.addClickListener(new ClickListener() {
//            @Override
//            public void buttonClick(final ClickEvent event) {
////                WinForgotPassword.open();
//                Notification.show("Se implementara proximamente...", Notification.Type.ASSISTIVE_NOTIFICATION);
//            }
//        });
//        loginPanel.addComponent(btnForgotPass);
//        loginPanel.setComponentAlignment(btnForgotPass, Alignment.BOTTOM_RIGHT);

//        loginPanel.addComponent(new CheckBox("Remember me", true));
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
//        password.setVisible(false);

//        ComboBox cbEstacion = new ComboBox("Estación",  new ListContainer<Estacion>(Estacion.class, estaciones));
//        final ComboBox cbEstacion = new ComboBox("Estación");
//        cbEstacion.setItemCaptionPropertyId("nombre");
//        cbEstacion.setVisible(false);
//        cbEstacion.setNullSelectionAllowed(false);
//        cbEstacion.setFilteringMode(FilteringMode.CONTAINS);

//        SvcPais svcPais = new SvcPais();
//        List<Pais> paises = svcPais.getAllPaises();
//        svcPais.closeConnections();
//        final ComboBox cbPais = new ComboBox("País", new ListContainer<Pais>(Pais.class, paises));
//        cbPais.setItemCaptionPropertyId("nombre");
//        cbPais.setVisible(false);
//        cbPais.setItemIconPropertyId("flag");
//        cbPais.setNullSelectionAllowed(false);
//        cbPais.setFilteringMode(FilteringMode.CONTAINS);
//        cbPais.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(final Property.ValueChangeEvent event) {
//                Pais pais = (Pais) cbPais.getValue();
//                SvcEstacion svcEstacion = new SvcEstacion();
//                List<Estacion> estaciones = svcEstacion.getStationsByCountry(pais.getPaisId(), true);
//                svcEstacion.closeConnections();
//                Container estacionContainer = new ListContainer<Estacion>(Estacion.class, estaciones);
//                cbEstacion.setContainerDataSource(estacionContainer);
//            }
//        });

//        final TextField name = new TextField("Nombre");
//        name.setIcon(FontAwesome.USER_TIMES);
//        name.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
//        name.setVisible(false);

//        Collection<String> roles = new ArrayList<String>(Arrays.asList(new String[]{Constant.ROL_LOGIN_CAJERO, Constant.ROL_LOGIN_SUPERVISOR, Constant.ROL_LOGIN_OTRO}));
//        Container rolesContainer = new ListContainer<String>(String.class, roles);
//        final ComboBox cbRol = new ComboBox("Rol", rolesContainer);
//        cbRol.focus();
//        cbRol.setNullSelectionAllowed(false);
//        cbRol.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(final Property.ValueChangeEvent event) {
//                String selectedRol = (String) cbRol.getValue();
//                username.setVisible(false);
//                cbPais.setVisible(false);
//                cbEstacion.setVisible(false);
//                name.setVisible(false);
//                password.setVisible(false);
//                if (selectedRol != null && selectedRol.equals(Constant.ROL_LOGIN_CAJERO)) {
//                    cbPais.setVisible(true);
//                    cbEstacion.setVisible(true);
//                    name.setVisible(true);
//                    password.setVisible(true);
//                } else if (selectedRol != null && selectedRol.equals(Constant.ROL_LOGIN_SUPERVISOR)) {
//                    cbPais.setVisible(true);
//                    cbEstacion.setVisible(true);
//                    password.setVisible(true);
//                } else if (selectedRol != null && selectedRol.equals(Constant.ROL_LOGIN_OTRO)) {
//                    username.setVisible(true);
//                    password.setVisible(true);
//                }
//            }
//        });

        final Button signin = new Button("Ingresar");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
//        signin.focus();

//Image stateImage = new Image("", new ThemeResource("img/spinner.gif"));

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

//                if (selectedRol != null && selectedRol.equals(Constant.ROL_LOGIN_CAJERO)) {
//                    String codEstacion = (cbEstacion.getValue() != null) ? ((Estacion) cbEstacion.getValue()).getCodigo() : "0";
//                    usuario = codEstacion;
//                } else if (selectedRol != null && selectedRol.equals(Constant.ROL_LOGIN_SUPERVISOR)) {
//                    String codEstacion = (cbEstacion.getValue() != null) ? ((Estacion) cbEstacion.getValue()).getCodigo() : "0";
//                    usuario = codEstacion;
//                } else if (selectedRol != null && selectedRol.equals(Constant.ROL_LOGIN_OTRO)) {
                    usuario = username.getValue();
//                    cbPais.setValue(null);
//                    cbEstacion.setValue(null);
//                }

//                if (selectedRol != null
//                        && ((selectedRol.equals(Constant.ROL_LOGIN_CAJERO) && cbPais.getValue() != null && cbEstacion.getValue() != null && !name.getValue().isEmpty() && !usuario.isEmpty() && !password.getValue().isEmpty())
//                        || (selectedRol.equals(Constant.ROL_LOGIN_SUPERVISOR) && cbPais.getValue() != null && cbEstacion.getValue() != null && !usuario.isEmpty() && !password.getValue().isEmpty())
//                        || (selectedRol.equals(Constant.ROL_LOGIN_OTRO) && !usuario.isEmpty() && !password.getValue().isEmpty()))) {
                if ( !usuario.isEmpty() && !password.getValue().isEmpty()) {
                    validData = true;
                } else {
                    Notification.show("Faltan uno o más datos de autenticación.", Notification.Type.ERROR_MESSAGE);
                    return;
                }

                if (validData) {
                    SvcUsuario svcUsuario = new SvcUsuario();
                    Usuario user = svcUsuario.getUserByUserPass(usuario, password.getValue());
                    Dao servicio = new Dao();
                    List<Acceso> misAccessos = servicio.getAccesosByUsuarioid(user.getUsuarioId(), user.isSysadmin());
                     svcUsuario.closeConnections();
//                    user.getRoles().get(0).setAccesos(misAccessos);
                    for(Acceso a:misAccessos){
//                        System.out.println("LoginView "+a.getTitulo()+" "+a.isVer()+" "+a.isAgregar()+" "+a.isCambiar()+" "+a.isEliminar());
                    }
                   
                    if (user.getUsuarioId() != null && user.getUsuarioId() > 0) {
                        String myNombreLogin = user.getNombre().concat(" ").concat(user.getApellido());
                        user.setNombreLogin(myNombreLogin);
                        user.setRolLogin(Constant.ROL_LOGIN_SUPERVISOR);
                        DashboardEventBus.post(new UserLoginRequestedEvent(user));
                    } else {
                        Notification.show("Por favor verifique su usuario y clave.", Notification.Type.ERROR_MESSAGE);
                    }
                }

//                Dao dao = new Dao();
//                User user = new User();
//                try {
////    PreparedStatement pst = dao.getConnection().prepareStatement("SELECT usuario_id, username, clave, nombre, apellido FROM ec_usuario WHERE username = '"+event.getUserName()+"' AND clave = '"+event.getPassword()+"'");
//                    PreparedStatement pst = dao.getConnection().prepareStatement(
//                            "SELECT u.usuario_id, u.username, u.clave, u.nombre, u.apellido, r.rol_id, r.nombre "
//                            + "FROM usuario u, rol_usuario ru, rol r "
//                            + "WHERE r.rol_id = ru.rol_id AND u.usuario_id = ru.usuario_id AND username = '" + username.getValue() + "' AND clave = '" + password.getValue() + "'");
//                    ResultSet rst = pst.executeQuery();
//                    if (rst.next-()) {
//                        user.setFirstName(rst.getString(4));
//                        user.setLastName(rst.getString(5));
//                        user.setId(rst.getInt(1));
//                        success = true;
//                    }
//                } catch (Exception exc) {
//                    exc.printStackTrace();
//                }
//                dao.closeConnections();
            }
        });

        return fields;
    }
    
    

    private Component buildLabels() {
        CssLayout labels = new CssLayout();
//        labels.addStyleName("labels");

        Label welcome = new Label("Estaciones COCO");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H2);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

//        Label title = new Label("QuickTickets Dashboard");
//        title.setSizeUndefined();
//        title.addStyleName(ValoTheme.LABEL_H3);
//        title.addStyleName(ValoTheme.LABEL_LIGHT);
//        labels.addComponent(title);
        return labels;
    }

}
