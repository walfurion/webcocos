package com.fundamental.view;

import com.fundamental.model.Acceso;
import com.fundamental.model.Rol;
import com.sisintegrados.generic.bean.Usuario;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoGenericBean;
import com.fundamental.services.Dao;
import com.fundamental.services.SvcMaintenance;
import com.fundamental.services.SvcMntUser;
import com.fundamental.utils.Constant;
import com.google.gwt.user.client.ui.ListBox;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
//TODO:revisar funcionamiento de actulizacion de estado de usuario.
public class MntUser extends Panel implements View {

    Table tblUser;
    BeanContainer<Integer, Usuario> bcrUsuarios = new BeanContainer<>(Usuario.class);
    Table tblRol;
    BeanContainer<Integer, Rol> bcrRoles = new BeanContainer<>(Rol.class);
    Table tblStations;
    BeanContainer<Integer, Estacion> bcrStations = new BeanContainer<>(Estacion.class);
    List<Estacion> allStations = new ArrayList();

    Button btnAdd, btnSave, btnFilterClear;
    TextField tfdFilter;
    private CssLayout filtering;
    TextField username, nombre, apellido;   //do not change variable name, with this function binder
    @PropertyId("correo")
    TextField tfdEmail;
    PasswordField clave;

    //Para el mantenimiento la estacion viene en el objeto Usuario.estacionLogin
//    @PropertyId("estacionLogin")
//    ComboBox estacion = new ComboBox("Estación:");
//    @PropertyId("paisLogin")
//    ComboBox cbPais = new ComboBox("País(Estación):");
    @PropertyId("status")
    ComboBox cbxStatus;
    private List<DtoGenericBean> listStatus;
//    ComboBox cbPaisUsuario = new ComboBox("País(usuario):");

    FormLayout form;
    BeanFieldGroup<Usuario> binder = new BeanFieldGroup<Usuario>(Usuario.class);
    private Usuario usuario;
    private List<Rol> allRoles;
    List<Pais> paises;
    String action = Dao.ACTION_UPDATE;

    //template
    private final VerticalLayout vlRoot;
    private CssLayout content = new CssLayout();
    Utils utils = new Utils();
    Usuario user;
    private OptionGroup optGroup;
    Acceso acceso = new Acceso();

    public MntUser() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        content.addStyleName("dashboard-panels");
        Responsive.makeResponsive(content);
        vlRoot = utils.buildVerticalR("vlRoot", true, false, true, "dashboard-view");
        setContent(vlRoot);
        Responsive.makeResponsive(vlRoot);
        vlRoot.addComponent(utils.buildHeaderR("Mantenimiento de usuarios"));
        vlRoot.addComponent(utils.buildSeparator());
        vlRoot.addComponent(content);
        vlRoot.setExpandRatio(content, 1);
        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        getAllData();
        //template

        initControls();
        buildButtons();
//        buildTableRoles();
        buildTableStations();

        filtering = new CssLayout();
        filtering.addComponents(tfdFilter, btnFilterClear);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

//        CssLayout cltToolbar = new CssLayout(utils.vlContainer(filtering), utils.vlContainer(btnAdd));
//        Responsive.makeResponsive(cltToolbar);
        VerticalLayout vlLeft = utils.buildVertical("vlLeft", false, false, true, false, null);
        vlLeft.setSizeUndefined();
        vlLeft.addComponents(filtering, tblUser, btnAdd);
        vlLeft.setMargin(new MarginInfo(false, true, true, false));
        vlLeft.setComponentAlignment(filtering, Alignment.MIDDLE_CENTER);
        vlLeft.setComponentAlignment(btnAdd, Alignment.TOP_CENTER);
        Responsive.makeResponsive(vlLeft);

//        CssLayout cltButtons = new CssLayout(utils.vlContainer(tblRol), utils.vlContainer(tblStations));
        CssLayout cltButtons = new CssLayout(utils.vlContainer(optGroup), utils.vlContainer(tblStations));
        cltButtons.setSizeUndefined();
        Responsive.makeResponsive(cltButtons);

        VerticalLayout vlRight = new VerticalLayout(form, cltButtons, btnSave);
        vlRight.setSizeUndefined();
        vlRight.setId("vlRight");
        vlRight.setComponentAlignment(vlRight.getComponent(1), Alignment.MIDDLE_CENTER);
        vlRight.setComponentAlignment(btnSave, Alignment.MIDDLE_CENTER);
        Responsive.makeResponsive(vlRight);

        content.addComponents(vlLeft, vlRight);

    }

    private void getAllData() {
        bcrUsuarios.setBeanIdProperty("usuarioId");
        bcrRoles.setBeanIdProperty("rolId");
        bcrStations.setBeanIdProperty("estacionId");

        SvcMntUser service = new SvcMntUser();
        bcrUsuarios.addAll(service.getAllUsuarios(false));
        allRoles = service.getAllRoles(true);
        paises = service.getAllPaises();
        allStations = service.getAllEstaciones(false);
        bcrStations.addAll(allStations);
        service.closeConnections();

        usuario = bcrUsuarios.getItem(bcrUsuarios.getItemIds().get(0)).getBean();
//        if (usuario.getEstacionLogin() != null) {
//            cbPais.setValue(usuario.getEstacionLogin().getPais());
//        }
        bcrRoles.addAll(allRoles);
        listStatus = Arrays.asList(new DtoGenericBean("I", "Inactivo"), new DtoGenericBean("A", "Activo"));
        optGroup = new OptionGroup("Perfiles", bcrRoles);
        for (Rol r : allRoles) {
            optGroup.setItemCaption(r.getRolId(), r.getNombre());
        }
        defineSelectedRoles();
    }

    private void initControls() {
        tfdFilter = new TextField();
        tfdFilter.setInputPrompt("Valor de filtro");
        tfdFilter.setIcon(FontAwesome.SEARCH);
        tfdFilter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        tfdFilter.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        tfdFilter.addShortcutListener(new ShortcutListener("Clear",
                ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                tfdFilter.setValue("");
                ((Filterable) tblUser.getContainerDataSource()).removeAllContainerFilters();
            }
        });
        tfdFilter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                Filterable data = (Filterable) tblUser.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Container.Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId, final Item item) {
                        if (event.getText() == null || event.getText().equals("")) {
                            return true;
                        }
                        return filterByProperty("username", item, event.getText())
                                || filterByProperty("nombre", item, event.getText())
                                || filterByProperty("apellido", item, event.getText());
                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        if (propertyId.equals("username")
                                || propertyId.equals("nombre")
                                || propertyId.equals("apellido")) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        btnFilterClear = new Button(FontAwesome.TIMES);
        btnFilterClear.addStyleName(ValoTheme.BUTTON_SMALL);
        btnFilterClear.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                tfdFilter.setValue("");
                ((Filterable) tblUser.getContainerDataSource()).removeAllContainerFilters();
            }
        });

        tblUser = utils.buildTable("Usuarios", 100f, 400f, bcrUsuarios,
                new String[]{"username", "nombre", "apellido"},
                new String[]{"Usuario", "Nombre", "Apellido"});
        tblUser.setSizeUndefined();
        tblUser.setStyleName(ValoTheme.TABLE_COMPACT);
        tblUser.setStyleName(ValoTheme.TABLE_SMALL);
        tblUser.setSelectable(true);
        tblUser.setValue(usuario.getUsuarioId());
        tblUser.addGeneratedColumn("colEstado", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Label result = new Label();
                String estado = bcrUsuarios.getItem(itemId).getBean().getEstado();
                result.setValue(estado.equals("A") ? "Activo" : "Inactivo");
                result.setWidth("75px");
                return result;
            }
        });
        tblUser.setVisibleColumns(new Object[]{"username", "nombre", "apellido", "colEstado"});
        tblUser.setColumnHeaders(new String[]{"Usuario", "Nombre", "Apellido", "Estado"});
        tblUser.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (tblUser.getValue() != null) {
                    action = Dao.ACTION_UPDATE;
                    usuario = bcrUsuarios.getItem(tblUser.getValue()).getBean();
                    binder.setItemDataSource(usuario);
//TODO: hacer que al seleccionar un usuario, se coloque el valor correcto de pais de usuario.
//                    if (usuario.getPaisLogin() != null) {
//                        cbPais.setValue(usuario.getPaisLogin());
//                    }
                    binder.setItemDataSource(usuario);
                    defineSelectedStations();
                    defineSelectedRoles();
                }
            }
        });

        username = utils.buildTextField("Usuario:", "", false, 50, true, ValoTheme.TEXTFIELD_SMALL);
        username.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                //TODO: al cambiar de valor este mismo colocarlo en la clave
            }
        });

        clave = new PasswordField("Clave:");
        clave.setNullRepresentation("");
        clave.setRequired(true);
        clave.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        nombre = utils.buildTextField("Nombre:", "", false, 50, true, ValoTheme.TEXTFIELD_SMALL);

        apellido = utils.buildTextField("Apellido:", "", false, 50, true, ValoTheme.TEXTFIELD_SMALL);

        tfdEmail = utils.buildTextField("Correo:", "", false, 100, true, ValoTheme.TEXTFIELD_SMALL);
        tfdEmail.setWidth("350px");
        tfdEmail.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (tfdEmail.getValue() != null) {
                    if (!tfdEmail.getValue().matches(Constant.REGEX_EMAIL)) {
                        tfdEmail.setValue(null);
                        Notification.show("El correo no cumple con un formato válido.", Notification.Type.WARNING_MESSAGE);
                        return;
                    }
                }
            }
        });

//        estacion.setItemCaptionPropertyId("nombre");
//        estacion.setNullSelectionAllowed(false);
//        estacion.addStyleName(ValoTheme.COMBOBOX_SMALL);
//        cbPais.setContainerDataSource(new ListContainer<Pais>(Pais.class, paises));
//        cbPais.setItemCaptionPropertyId("nombre");
//        cbPais.setItemIconPropertyId("flag");
//        cbPais.addStyleName(ValoTheme.COMBOBOX_SMALL);
//        cbPais.addValueChangeListener(new Property.ValueChangeListener() {
//            @Override
//            public void valueChange(final Property.ValueChangeEvent event) {
//                if (cbPais.getValue() != null) {
//                    Pais pais = (Pais) cbPais.getValue();
//                    SvcEstacion svcEstacion = new SvcEstacion();
//                    List<Estacion> estaciones = svcEstacion.getStationsByCountry(pais.getPaisId(), true);
//                    svcEstacion.closeConnections();
//                    Container estacionContainer = new ListContainer<Estacion>(Estacion.class, estaciones);
//                    estacion.setContainerDataSource(estacionContainer);
//                    estacion.setEnabled(true);
//                } else {
//                    estacion.select(null);
//                    estacion.setEnabled(false);
//                }
//            }
//        });
        //Pais al que estara asociado el usuario
//        cbPaisUsuario.setContainerDataSource(new ListContainer<Pais>(Pais.class, paises));
//        cbPaisUsuario.setItemCaptionPropertyId("nombre");
//        cbPaisUsuario.setItemIconPropertyId("flag");
//        cbPaisUsuario.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxStatus = utils.buildCombobox("Estado:", "name", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(DtoGenericBean.class, listStatus));

        form = new FormLayout();
        form.setSizeUndefined();
        form.addComponents(username, clave, nombre, apellido, //cbPais, estacion, 
                tfdEmail, cbxStatus);

        //Importante
        binder.bindMemberFields(this);
        binder.setItemDataSource(usuario);
    }

    private void buildButtons() {
        btnAdd = new Button("Agregar", FontAwesome.PLUS);
        btnAdd.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                usuario = new Usuario();
                usuario.setEstado("A");
                binder.setItemDataSource(usuario);
                action = Dao.ACTION_ADD;
                for (Integer bombaId : bcrRoles.getItemIds()) {
                    bcrRoles.getItem(bombaId).getItemProperty("selected").setValue(false);
                }
                for (Integer itemId : bcrStations.getItemIds()) {
                    bcrStations.getItem(itemId).getItemProperty("selected").setValue(false);
                }
                tblUser.setValue(null);
                optGroup.setValue(null);
                cbxStatus.setValue(null);
            }
        });

        btnSave = new Button("Guardar", FontAwesome.SAVE);
        btnSave.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!binder.isValid()) {
                    Notification.show("Los campos indicados son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                //Validacion y/o asignacion de roles
                usuario.setRoles(new ArrayList());
                boolean exists = false;
                if (optGroup.getValue() != null) {
                    List<Rol> roles = new ArrayList<Rol>();
                    roles.add(bcrRoles.getItem(optGroup.getValue()).getBean());
                    usuario.setRoles(roles);
                    exists = true;
                }
//                for (Integer rid : bcrRoles.getItemIds()) {
//                    if (bcrRoles.getItem(rid).getBean().getSelected()) {
//                        System.out.println("bcrRoles.getItem(rid) "+bcrRoles.getItem(rid));
//                        
//                        exists = true;
//                    }
//                }
                if (!exists) {
                    Notification.show("Es obligatorio seleccionar al menos un rol.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                usuario.setStations(new ArrayList());
                for (Integer rid : bcrStations.getItemIds()) {
                    if (bcrStations.getItem(rid).getBean().isSelected()) {
                        usuario.getStations().add(bcrStations.getItem(rid).getBean());
                    }
                }

                String newUsername = username.getValue();

//                String codeStation = (estacion.getValue() != null) ? ((Estacion) estacion.getValue()).getCodigo() : "";
//                if (!codeStation.isEmpty() && !username.getValue().contains(codeStation)) {
//                    newUsername = username.getValue().concat(codeStation);
//                }
                if (action.equals(Dao.ACTION_ADD)) {
                    for (Integer uid : bcrUsuarios.getItemIds()) {
                        if (newUsername.equals(bcrUsuarios.getItem(uid).getItemProperty("username").getValue())) {
                            Notification.show("El nombre de usuario ya existe", Notification.Type.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

//                if (estacion.getValue() != null) {
//                    Rol rol;
//                    for (Integer rid : bcrRoles.getItemIds()) {
//                        rol = bcrRoles.getItem(rid).getBean();
//                        if (rol.getSelected() == true
//                                && !rol.getRolId().equals(Constant.ROL_PERMISSION_SUPERVISOR)
//                                && !rol.getRolId().equals(Constant.ROL_PERMISSION_CAJERO)
//                                && !rol.getRolId().equals(Constant.ROL_PERMISSION_REPORTES)) {
//                            Notification.show("Uno o mas roles NO pueden tener asignada una estación.", Notification.Type.ERROR_MESSAGE);
//                            return;
//                        }
//                    }
//                }
//                if (estacion.getValue() == null) {
//                    usuario.setEstacionLogin(new Estacion());
//                    usuario.getEstacionLogin().setPais(new Pais());
//                }
                try {
                    binder.commit();
                } catch (Exception ex) {
                    Logger.getLogger(MntUser.class.getName()).log(Level.SEVERE, null, ex);
                }

//                if (estacion.getValue() == null) {
//                    usuario.setEstacionLogin(null);
//                }
                //Para el mantenimiento en el atributo estacionLogin se va la estacion que pudiese tener asociado el usuario.
//                usuario.setEstacionLogin((Estacion) estacion.getValue());
                usuario.setUsername(newUsername);
                usuario.setCreadoPor(user.getUsername());
                usuario.setModificadoPor(user.getUsername());
                Integer paisIdU = null; //(cbPais.getValue() == null) ? null : ((Pais) cbPais.getValue()).getPaisId();
                usuario.setPaisId(paisIdU);
                usuario.setEstado(usuario.getStatus().getStringId());
                SvcMaintenance service = new SvcMaintenance();
                usuario = service.doActionUser(action, usuario);
                service.closeConnections();
                if (usuario.getUsuarioId() != null) {
                    Notification notif = new Notification("¡Exito!", "La acción se ha ejecutado correctamente.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_USER.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + usuario.getDescError(), Notification.Type.ERROR_MESSAGE);
                }
            }
        });
    }

    private void defineSelectedRoles() {
        for (Rol r : allRoles) {
            r.setSelected(false);
            for (Rol rl : usuario.getRoles()) {
                if (r.getRolId().equals(rl.getRolId())) {
                    optGroup.select(r.getRolId());
                    r.setSelected(true);
                    break;
                }
            }
        }
        bcrRoles.removeAllItems();
        bcrRoles.addAll(allRoles);
    }

    private void defineSelectedStations() {
        allStations.stream().forEach((station) -> {
            station.setSelected(false);
        });
        for (Estacion stationUser : usuario.getStations()) {
            for (Estacion station : allStations) {
                if (stationUser.getEstacionId().equals(station.getEstacionId())) {
                    station.setSelected(true);
                    break;
                }
            }
        }
        bcrStations.removeAllItems();
        bcrStations.addAll(allStations);
    }

//    private void buildTableRoles() {
//        tblRol = utils.buildTable("Roles:", 100f, 100f, bcrRoles,
//                new String[]{"nombre"},
//                new String[]{"Nombre"});
//        tblRol.setSizeUndefined();
//        tblRol.setStyleName(ValoTheme.TABLE_COMPACT);
//        tblRol.setStyleName(ValoTheme.TABLE_SMALL);
//        tblRol.setHeight("200px");
//        tblRol.setWidth("250px");
//        tblRol.addGeneratedColumn("colSelected", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("selected");  //Atributo del bean
//                
//                CheckBox cbxSelect = new CheckBox("", pro);
//                cbxSelect.addStyleName(ValoTheme.CHECKBOX_SMALL);
//                return cbxSelect;
//            }
//        });
//        tblRol.setVisibleColumns(new Object[]{"colSelected", "nombre"});
//        tblRol.setColumnHeaders(new String[]{"", "Nombre"});
//    }
//                cbxSelect.addListener(new Button.ClickListener() {
//                    public void buttonClick(ClickEvent event) {
//                        entity.setBooleanProperty(event.getButton().booleanValue());
//                        BeanItemContainer<Entity> container = (BeanItemContainer<Entity>) getContainerDataSource();
//                        for (Iterator iterator = getItemIds().iterator(); iterator.hasNext();) {
//                            
//                            Entity bean = (Entity) iterator.next();
//                            container.removeItem(bean);
//                            if (!entity.equals(bean)) {
//                                entity.setBooleanProperty(false);
//                            }
//                            container.addBean(bean);
//                        }
//                    }
//                });
//                return cb;
//            }
//        });
//    }
    private void buildTableStations() {
        tblStations = utils.buildTable("Estaciones:", 100f, 100f, bcrStations,
                new String[]{"nombre"},
                new String[]{"Nombre"});
        tblStations.setSizeUndefined();
        tblStations.setStyleName(ValoTheme.TABLE_COMPACT);
        tblStations.setStyleName(ValoTheme.TABLE_SMALL);
        tblStations.setHeight("200px");
        tblStations.setWidth("350px");
        tblStations.addGeneratedColumn("colSelected", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("selected");  //Atributo del bean
                CheckBox cbxSelect = new CheckBox("", pro);
                cbxSelect.addStyleName(ValoTheme.CHECKBOX_SMALL);
                return cbxSelect;
            }
        });
        tblStations.setVisibleColumns(new Object[]{"colSelected", "codigo", "nombre"});
        tblStations.setColumnHeaders(new String[]{"", "Código", "Nombre"});
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new Dao();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnAdd.setEnabled(acceso.isAgregar());
        btnSave.setEnabled(acceso.isCambiar());
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean filterByProperty(final String prop, final Item item, final String text) {
        if (item == null || item.getItemProperty(prop) == null || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }

}
