package com.fundamental.view.maintenance;

import com.fundamental.model.Acceso;
import com.fundamental.model.Rol;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoGenericBean;
import com.sisintegrados.dao.Dao;
import com.fundamental.services.SvcMaintenance;
import com.sisintegrados.daoimp.DaoImp;
import com.vaadin.data.Item;
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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
    Button btnSave, btnAdd,btnCancel;
    String action;
    OptionGroup group;
//template
    private VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Usuario user;
    CheckBox cbxVer,cbxCambiar,cbxEliminar,cbxAgregar;
    Label lb;
    Acceso acceso = new Acceso();
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
        HorizontalLayout hl2 = utils.buildHorizontal("hl2", false, true, true, false);
        lb = new Label();
        lb.setCaption("Pantallas:");
        hl2.addComponents(cbxVer,cbxCambiar,cbxAgregar,cbxEliminar);
        vltLeft.addComponents(tfdName, txaDescription, cbxStatus,lb,hl2,tblAccess, btnSave);
        vltLeft.setComponentAlignment(btnSave, Alignment.MIDDLE_CENTER);
        vltLeft.setSizeUndefined();
//        vltLeft.setComponentAlignment(group,Alignment.TOP_CENTER);
        Responsive.makeResponsive(vltLeft);

        VerticalLayout vltRight = utils.buildVertical("vltRight", false, false, true, false, null);
        HorizontalLayout hl = utils.buildHorizontal("hl", true, false, true, false);
        hl.addComponents(btnAdd,btnCancel);
        hl.setComponentAlignment(btnAdd, Alignment.TOP_CENTER);
        hl.setComponentAlignment(btnCancel, Alignment.TOP_LEFT);
        hl.setSpacing(true);
        vltRight.addComponents(tblRoles,hl);
        vltRight.setComponentAlignment(hl, Alignment.TOP_CENTER);
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
                    action = DaoImp.ACTION_UPDATE;
                    rol = bcrRol.getItem(tblRoles.getValue()).getBean();
                    binder.setItemDataSource(rol);
                    bcrAccess.removeAllItems();
                    for (Acceso acc : listAcces) {
                        acc.setSelected(false);
                        acc.setVer(false);
                        acc.setCambiar(false);
                        acc.setAgregar(false);
                        acc.setEliminar(false);
                        for (Acceso item : rol.getAccesos()) {
                            if (item.getAccesoId().equals(acc.getAccesoId())) {
//                                System.out.println(" - "+item.getVer()+" - "+item.getCambiar()+" "+item.getAgregar()+" "+item.getEliminar());
                                acc.setSelected(true);
                                acc.setVer(item.isVer());
                                acc.setCambiar(item.isCambiar());
                                acc.setAgregar(item.isAgregar());
                                acc.setEliminar(item.isEliminar());
                                break;
                            }
                        }
                    }
                    bcrAccess.addAll(listAcces);
                    tblAccess.refreshRowCache();
                    cbxVer.setValue(Boolean.FALSE);
                    cbxAgregar.setValue(Boolean.FALSE);
                    cbxCambiar.setValue(Boolean.FALSE);
                    cbxEliminar.setValue(Boolean.FALSE);
                }
            }
        });

        tblRoles.setVisibleColumns(new Object[]{"nombre", "estado", "descripcion"});
        tblRoles.setColumnHeaders(new String[]{"Nombre", "Estado", "Descripcion"});
        tblRoles.setColumnWidth("descripcion", 600);
        tblRoles.setColumnAlignment("colCanSave", Table.Align.CENTER);
        tblRoles.setColumnAlignment("estado", Table.Align.CENTER);
        tblAccess = utils.buildTable("", 200, 500, bcrAccess, new Object[]{"titulo"}, new String[]{"Pantalla"});
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
//         tblAccess.addGeneratedColumn("colVer", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, final Object itemId, Object columnId) {
//                Property pro = source.getItem(itemId).getItemProperty("ver");
//                CheckBox cbxSelect = new CheckBox("",pro);
//                cbxSelect.addStyleName(ValoTheme.CHECKBOX_SMALL);
//                return cbxSelect;
//            }
//        });
         tblAccess.addGeneratedColumn("colCambiar", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("cambiar");
                CheckBox cbxSelect = new CheckBox("",pro);
                cbxSelect.addStyleName(ValoTheme.CHECKBOX_SMALL);
                return cbxSelect;
            }
        });
         tblAccess.addGeneratedColumn("colAgregar", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("agregar");
                CheckBox cbxSelect = new CheckBox("",pro);
                cbxSelect.addStyleName(ValoTheme.CHECKBOX_SMALL);
                return cbxSelect;
            }
        });
         tblAccess.addGeneratedColumn("colEliminar", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("eliminar");
                CheckBox cbxSelect = new CheckBox("",pro);
                cbxSelect.addStyleName(ValoTheme.CHECKBOX_SMALL);
                return cbxSelect;
            }
        });
         
//        tblAccess.setVisibleColumns("colSelected", "nombrePadre", "titulo");
        tblAccess.setVisibleColumns("colSelected", "nombrePadre", "titulo","colCambiar","colAgregar","colEliminar");
        tblAccess.setColumnHeaders("Ver", "Grupo", "Pantalla","Cambiar","Agregar","Eliminar");
        tblAccess.setColumnAlignment("colSelected",Table.Align.CENTER);
        tblAccess.setColumnAlignment("colCambiar",Table.Align.CENTER);
        tblAccess.setColumnAlignment("colAgregar",Table.Align.CENTER);
        tblAccess.setColumnAlignment("colEliminar",Table.Align.CENTER);
        tblAccess.setColumnWidth("colSelected",100);
        tblAccess.setColumnWidth("colCambiar",100);
        tblAccess.setColumnWidth("colAgregar",100);
        tblAccess.setColumnWidth("colEliminar",100);
        //Importante
        binder.bindMemberFields(this);
        binder.setItemDataSource(rol);
    }

    private void buildButtons() {
        btnAdd = utils.buildButton("Agregar", FontAwesome.PLUS, ValoTheme.BUTTON_PRIMARY);
        btnCancel = utils.buildButton("Cancelar", FontAwesome.ERASER, ValoTheme.BUTTON_PRIMARY);
        btnAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                action = DaoImp.ACTION_ADD;
                rol = new Rol();
                binder.setItemDataSource(rol);
                bcrAccess.removeAllItems();
                for (Acceso acc : listAcces) {
                    acc.setSelected(false);
                    acc.setAgregar(false);
                    acc.setEliminar(false);
                    acc.setCambiar(false);
                    acc.setVer(false);
                }
                bcrAccess.addAll(listAcces);
                tfdName.focus();
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
                        Acceso a = bcrAccess.getItem(itemId).getBean();
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
        btnCancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_ROL.getViewName());
            }
        });
        cbxVer = new CheckBox("Ver", false);
        cbxVer.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                boolean checked = (boolean) event.getProperty().getValue();
                setAllCheck("selected",checked);
            }
        });
        cbxCambiar = new CheckBox("Cambiar", false);
        cbxCambiar.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                System.out.println("event "+event);
                System.out.println(" -- "+event.getProperty());
                boolean checked = (boolean) event.getProperty().getValue();
                setAllCheck("cambiar",checked);
            }
        });      
        cbxAgregar = new CheckBox("Agregar", false);
        cbxAgregar.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                System.out.println("event "+event);
                System.out.println(" -- "+event.getProperty());
                boolean checked = (boolean) event.getProperty().getValue();
                setAllCheck("agregar",checked);
            }
        });  
         cbxEliminar = new CheckBox("Eliminar", false);
        cbxEliminar.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                System.out.println("event "+event);
                System.out.println(" -- "+event.getProperty());
                boolean checked = (boolean) event.getProperty().getValue();
                setAllCheck("eliminar",checked);
            }
        });  
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new DaoImp();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnAdd.setEnabled(acceso.isAgregar());
        btnSave.setEnabled(acceso.isCambiar());
    }
    public void setAllCheck(String caption,boolean all){
                for (Object itemId : tblAccess.getItemIds()) {
                    Item row = tblAccess.getItem(itemId);
                    row.getItemProperty(caption).setValue(all);
                }
    }

}
