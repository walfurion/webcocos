package com.fundamental.view.maintenance;

import com.fundamental.model.Acceso;
import com.fundamental.model.Rol;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoGenericBean;
import com.fundamental.services.Dao;
import com.fundamental.services.SvcMaintenance;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
public class MntRol extends Panel implements View {

    BeanFieldGroup<Rol> binder = new BeanFieldGroup<Rol>(Rol.class);
    Rol rol = new Rol();
    @PropertyId("nombre")
    TextField tfdName;
    @PropertyId("descripcion")
    TextArea txaDescription;
    @PropertyId("status")
    ComboBox cbxStatus;
    List<DtoGenericBean> listStatus = Arrays.asList(new DtoGenericBean("I", "Inactivo"), new DtoGenericBean("A", "Activo"));

    BeanContainer<Integer, Rol> bcrRol = new BeanContainer<Integer, Rol>(Rol.class);
    Table tblRoles;
    BeanContainer<Integer, Acceso> bcrAccess = new BeanContainer<Integer, Acceso>(Acceso.class);
    List<Acceso> listAcces = new ArrayList();
    Table tblAccess;
    Button btnSave, btnAdd;
    String action;

//template
    private VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Usuario user;

    public MntRol() {
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
        vlRoot.addComponent(utils.buildHeader("Roles", false, true));
        vlRoot.addComponent(utils.buildSeparator());
        getAllData();
//template

        buildControls();
        buildButtons();

        VerticalLayout vltLeft = utils.buildVertical("vltLeft", false, true, true, false, null);
        vltLeft.addComponents(tfdName, txaDescription, cbxStatus, tblAccess, btnSave);
        vltLeft.setComponentAlignment(btnSave, Alignment.MIDDLE_CENTER);
        vltLeft.setSizeUndefined();
        Responsive.makeResponsive(vltLeft);

        VerticalLayout vltRight = utils.buildVertical("vltRight", false, false, true, false, null);
        vltRight.addComponents(tblRoles, btnAdd);
        vltRight.setComponentAlignment(btnAdd, Alignment.TOP_CENTER);
        vltRight.setSizeUndefined();
        Responsive.makeResponsive(vltRight);

        CssLayout cltContainer = new CssLayout(utils.vlContainer(vltLeft), vltRight);
        cltContainer.setId("cltContainer");
//        cltContainer.setSizeUndefined();
        Responsive.makeResponsive(cltContainer);

        vlRoot.addComponents(cltContainer);
        vlRoot.setExpandRatio(cltContainer, 1);

        if (bcrRol.size() > 0) {
            tblRoles.setValue(bcrRol.getItemIds().get(0));
        } else {
            btnAdd.click();
        }
    }

    private void getAllData() {
        SvcMaintenance service = new SvcMaintenance();
        bcrRol.setBeanIdProperty("rolId");
        bcrAccess.setBeanIdProperty("accesoId");
        bcrRol.addAll(service.getAllRoles(true));
        listAcces = service.getAllAccess(false);
        bcrAccess.addAll(listAcces);
        service.closeConnections();
    }

    private void buildControls() {
        tfdName = utils.buildTextField("Nombre:", "", false, 30, true, ValoTheme.TEXTFIELD_SMALL);
        tfdName.setSizeFull();

        txaDescription = utils.buildTextArea("Descripción:", 20, 3, 100, true, true, "", ValoTheme.TEXTAREA_SMALL);

        cbxStatus = utils.buildCombobox("Estado:", "name", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<>(DtoGenericBean.class, listStatus));

        tblRoles = utils.buildTable("Roles:", 200, 300, bcrRol, new Object[]{"nombre", "estado", "descripcion"}, new String[]{"Nombre", "Estado", "Descripción"});
        tblRoles.setSizeUndefined();
        tblRoles.setHeight("500px");
        tblRoles.addStyleName(ValoTheme.TABLE_COMPACT);
        tblRoles.addStyleName(ValoTheme.TABLE_SMALL);
        tblRoles.addGeneratedColumn("colCanSave", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("canSave");  //Atributo del bean
                CheckBox cbxSave = new CheckBox("", pro);
                cbxSave.addStyleName(ValoTheme.CHECKBOX_SMALL);
                return cbxSave;
            }
        });
        tblRoles.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (tblRoles.getValue() != null) {
                    action = Dao.ACTION_UPDATE;
                    rol = bcrRol.getItem(tblRoles.getValue()).getBean();
                    binder.setItemDataSource(rol);
                    bcrAccess.removeAllItems();
                    for (Acceso acc : listAcces) {
                        acc.setSelected(false);
                        for (Acceso item : rol.getAccesos()) {
                            if (item.getAccesoId().equals(acc.getAccesoId())) {
                                acc.setSelected(true);
                                break;
                            }
                        }
                    }
                    bcrAccess.addAll(listAcces);
                    tblAccess.refreshRowCache();
                }
            }
        });

        tblRoles.setVisibleColumns(new Object[]{"nombre", "estado", "colCanSave", "descripcion"});
        tblRoles.setColumnHeaders(new String[]{"Nombre", "Estado", "¿Editar?", "Descripcion"});
        tblRoles.setColumnWidth("descripcion", 600);
        tblRoles.setColumnAlignment("colCanSave", Table.Align.CENTER);
        tblRoles.setColumnAlignment("estado", Table.Align.CENTER);

//        tblRoles.addGeneratedColumn("colEstado", new Table.ColumnGenerator() {
//                    @Override
//                    public Object generateCell(Table source, final Object itemId, Object columnId) {
//                        Label result = new Label();
//                        String estado = bcrRol.getItem(itemId).getBean().getEstado();
//                        result.setValue(estado.equals("A") ? "Activo" : "Inactivo");
//                        result.setWidth("75px");
//                        return result;
//                    }
//                });
//        tblRoles.setVisibleColumns(new Object[]{"nombre", "estado", "descripcion", "colEstado"});
//        tblRoles.setColumnHeaders(new String[]{"Nombre", "Estado", "Descripcion", "Estado"});

        tblAccess = utils.buildTable("Pantallas:", 200, 500, bcrAccess, new Object[]{"titulo"}, new String[]{"Pantalla"});
        tblAccess.setSizeUndefined();
        tblAccess.setHeight("250px");
        tblAccess.addStyleName(ValoTheme.TABLE_COMPACT);
        tblAccess.addStyleName(ValoTheme.TABLE_SMALL);
        tblAccess.addGeneratedColumn("colSelected", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("selected");  //Atributo del bean
                CheckBox cbxSelect = new CheckBox("", pro);
                cbxSelect.addStyleName(ValoTheme.CHECKBOX_SMALL);
                return cbxSelect;
            }
        });
        tblAccess.setVisibleColumns("colSelected", "nombrePadre", "titulo");
        tblAccess.setColumnHeaders("", "Grupo", "Pantalla");

        //Importante
        binder.bindMemberFields(this);
        binder.setItemDataSource(rol);
    }

    private void buildButtons() {
        btnAdd = utils.buildButton("Agregar", FontAwesome.PLUS, ValoTheme.BUTTON_PRIMARY);
        btnAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                action = Dao.ACTION_ADD;
                rol = new Rol();
                binder.setItemDataSource(rol);
                bcrAccess.removeAllItems();
                for (Acceso acc : listAcces) {
                    acc.setSelected(false);
                }
                bcrAccess.addAll(listAcces);
            }
        });

        btnSave = utils.buildButton("Guardar", FontAwesome.SAVE, ValoTheme.BUTTON_PRIMARY);
        btnSave.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!binder.isValid()) {
                    Notification.show("Los campos indicados son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                try {
                    binder.commit();
                } catch (Exception ex) {
                    Logger.getLogger(MntRol.class.getName()).log(Level.SEVERE, null, ex);
                }
                rol.setEstado(rol.getStatus().getStringId());
                rol.setCreadoPor(user.getUsername());
                rol.setModificadoPor(user.getUsername());
                rol.setAccesos(new ArrayList());
                for (Integer itemId : bcrAccess.getItemIds()) {
                    if (bcrAccess.getItem(itemId).getBean().isSelected()) {
                        rol.getAccesos().add(bcrAccess.getItem(itemId).getBean());
                    }
                }
                SvcMaintenance service = new SvcMaintenance();
                rol = service.doActionRol(action, rol);
                service.closeConnections();
                if (rol.getRolId() > 0) {
                    Notification notif = new Notification("¡Exito!", "La acción se ha ejecutado correctamente.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_ROL.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + rol.getDescError(), Notification.Type.ERROR_MESSAGE);
                }
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
