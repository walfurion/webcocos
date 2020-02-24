package com.fundamental.view;

import com.fundamental.model.Bomba;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.EstacionConf;
import com.fundamental.model.EstacionConfHead;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoBean;
import com.sisintegrados.dao.Dao;
import com.fundamental.services.SvcConfBombaEstacion;
import com.fundamental.utils.Constant;
import com.sisintegrados.daoimp.DaoImp;
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
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.vaadin.maddon.ListContainer;
//import org.vaadin.teemu.switchui.Switch;

/**
 * @author Henry Barrientos
 */
public class MntConfBombaEstacion extends Panel implements View {

    ComboBox cbxPais, cbxEstacion, cbxEstado;
    TextField tfdNombre = new TextField("Modo servicio:");
    Button btnAgregar = new Button("Agregar", FontAwesome.PLUS),
            btnGuardar,
            btnEliminar;
    Table tableConfiguracion, tableBomba;
    DateField dfdHoraInicio = new DateField("Hora incio:"), dfdHoraFin = new DateField("Hora fin:");
    private BeanContainer<Integer, EstacionConfHead> bcrConfiguraciones = new BeanContainer<Integer, EstacionConfHead>(EstacionConfHead.class);
    private BeanContainer<Integer, EstacionConf> bcrEstConf = new BeanContainer<Integer, EstacionConf>(EstacionConf.class);

    List<Pais> paises = new ArrayList();
    EstacionConfHead configuracion;
    List<DtoBean> estados = new ArrayList(Arrays.asList(
            new DtoBean(1, "ACTIVO", "A"), new DtoBean(2, "INACTIVO", "I")
    ));
    String action;

//template
    private final VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Usuario user;

    public MntConfBombaEstacion() {
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
        vlRoot.addComponent(utils.buildHeader("Mantenimiento conf bomba-estación", false, true));
        vlRoot.addComponent(utils.buildSeparator());
        getAllData();
//template

        buildFilters();
        buildTableConf();
        buildTableBomba();
        buildButtons();

        VerticalLayout vlRight = new VerticalLayout(utils.vlContainer(cbxPais), utils.vlContainer(cbxEstacion), utils.vlContainer(tableConfiguracion), utils.vlContainer(btnAgregar));
        vlRight.setSizeUndefined();
        vlRight.setId("vlRight");
        Responsive.makeResponsive(vlRight);

//        CssLayout cltFilters = new CssLayout(utils.vlContainer(cbxPais), utils.vlContainer(cbxEstacion));
//        cltFilters.setId("filters");
//        cltFilters.setSizeUndefined();
//        Responsive.makeResponsive(cltFilters);

        VerticalLayout vlLeft = new VerticalLayout(tfdNombre, cbxEstado, dfdHoraInicio, dfdHoraFin, utils.vlContainer(tableBomba), utils.vlContainer(btnGuardar));
        vlLeft.setSizeUndefined();
        vlLeft.setId("vlLeft");
        Responsive.makeResponsive(vlLeft);

//        HorizontalLayout hlButtons = new HorizontalLayout(utils.vlContainer(btnEliminar), utils.vlContainer(btnAgregar), utils.vlContainer(btnGuardar));
//        hlButtons.setSizeFull();
//        hlButtons.setSpacing(true);
//        hlButtons.setId("hlButtons");
//        Responsive.makeResponsive(hlButtons);
        
        CssLayout cltTables = new CssLayout(//utils.vlContainer(tableConfiguracion), 
                vlRight, vlLeft//, hlButtons
        );
        cltTables.setId("cltTables");
        cltTables.setSizeUndefined();
        Responsive.makeResponsive(cltTables);

//        CssLayout cltButtons = new CssLayout(utils.vlContainer(btnEliminar), utils.vlContainer(btnAgregar), utils.vlContainer(btnGuardar));
//        cltButtons.setId("cltButtons");
//        cltButtons.setSizeUndefined();
//        Responsive.makeResponsive(cltButtons);


        vlRoot.addComponents(//cltFilters, 
                cltTables);
        vlRoot.setExpandRatio(cltTables, 1);

    }

    public void getAllData() {
        bcrConfiguraciones.setBeanIdProperty("estacionconfheadId");
        bcrEstConf.setBeanIdProperty("idDto");
        SvcConfBombaEstacion service = new SvcConfBombaEstacion();
        paises = service.getAllPaises();
//        service.closeConnections(); //ASG
    }

    private void buildFilters() {
        cbxPais = new ComboBox("País:", new ListContainer<Pais>(Pais.class, paises));
        cbxPais.setItemCaptionPropertyId("nombre");
        cbxPais.setSizeUndefined();
        cbxPais.setItemIconPropertyId("flag");
        cbxPais.setNullSelectionAllowed(false);
        cbxPais.setRequired(true);
        cbxPais.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxPais.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                List<Estacion> estaciones;
                SvcConfBombaEstacion service = new SvcConfBombaEstacion();
                estaciones = service.getStationsByCountry(((Pais) cbxPais.getValue()).getPaisId(), true);
//                service.closeConnections();  //ASG
                cbxEstacion.setContainerDataSource(new ListContainer<Estacion>(Estacion.class, estaciones));
                bcrConfiguraciones.removeAllItems();
                bcrEstConf.removeAllItems();
            }
        });

        cbxEstacion = new ComboBox("Estación:");
        cbxEstacion.setItemCaptionPropertyId("nombre");
        cbxEstacion.setSizeUndefined();
        cbxEstacion.setNullSelectionAllowed(false);
        cbxEstacion.setRequired(true);
        cbxEstacion.setFilteringMode(FilteringMode.CONTAINS);
        cbxEstacion.addStyleName(ValoTheme.COMBOBOX_SMALL);
        cbxEstacion.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                bcrConfiguraciones.removeAllItems();
                SvcConfBombaEstacion service = new SvcConfBombaEstacion();
                bcrConfiguraciones.addAll(service.getConfiguracionHeadByEstacionid(((Estacion) cbxEstacion.getValue()).getEstacionId(), true));
//                service.closeConnections(); //ASG
                if (bcrConfiguraciones.size() > 0) {
                    int kkk = bcrConfiguraciones.firstItemId();
                    tableConfiguracion.setValue(bcrConfiguraciones.getItem(kkk));
                }
                bcrEstConf.removeAllItems();
            }
        });

        tfdNombre.setRequired(true);
        tfdNombre.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        cbxEstado = new ComboBox("Estado:", new ListContainer<DtoBean>(DtoBean.class, estados));
        cbxEstado.setItemCaptionPropertyId("nombre");
        cbxEstado.setSizeUndefined();
        cbxEstado.setNullSelectionAllowed(false);
        cbxEstado.setRequired(true);
        cbxEstado.addStyleName(ValoTheme.COMBOBOX_SMALL);
        
        
        dfdHoraInicio = utils.buildDateField("Hora inicio:", "HH:mm", new Locale("es", "ES"), ValoTheme.DATEFIELD_SMALL, new Date(), new Date(), true, Resolution.MINUTE, new Date());
//        dfdHoraInicio.setDateFormat("HH:mm");
//        dfdHoraInicio.setLocale(new Locale("es", "ES"));
//        dfdHoraInicio.addStyleName(ValoTheme.DATEFIELD_SMALL);
//        dfdHoraInicio.setRangeStart(new Date());
//        dfdHoraInicio.setRangeEnd(new Date());
//        dfdHoraInicio.setRequired(true);
//        dfdHoraInicio.setResolution(Resolution.MINUTE);
        
        dfdHoraFin.setResolution(Resolution.MINUTE);
        dfdHoraFin.setLocale(new Locale("es", "ES"));
        dfdHoraFin.setRequired(true);
        dfdHoraFin.addStyleName(ValoTheme.DATEFIELD_SMALL);
        dfdHoraFin.setDateFormat("HH:mm");
        dfdHoraFin.setRangeStart(new Date());
        dfdHoraFin.setRangeEnd(new Date());
        
    }

    private void buildTableConf() {
        tableConfiguracion = utils.buildTable("Configuraciones:", 100f, 100f, bcrConfiguraciones,
                new String[]{"nombre"},
                new String[]{"Modo servicio"});
        tableConfiguracion.setSizeUndefined();
        tableConfiguracion.setSelectable(true);
        tableConfiguracion.setHeight("300px");
        tableConfiguracion.setWidth("400px");
        tableConfiguracion.addGeneratedColumn("colDelete", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button btnDelete = new Button(FontAwesome.TRASH);
                btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
                btnDelete.addStyleName(ValoTheme.BUTTON_SMALL);
                btnDelete.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        btnEliminar.click();
                    }
                });
                return btnDelete;
            }
        });
        tableConfiguracion.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (tableConfiguracion.getValue() != null) {
                    bcrEstConf.removeAllItems();
                    int itemid = Integer.parseInt(tableConfiguracion.getValue().toString());
                    EstacionConfHead confHead = bcrConfiguraciones.getItem(itemid).getBean();
                    bcrEstConf.addAll(confHead.getEstacionConf());
                    tfdNombre.setValue(confHead.getNombre());
                    String state = confHead.getEstado();
                    cbxEstado.setValue( state.equals("A") ? estados.get(0) : estados.get(1) );
                    dfdHoraInicio.setValue(null);
                    dfdHoraFin.setValue(null);
                    try {
                        Calendar today = Calendar.getInstance();
                        today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(confHead.getHoraInicio().split(":")[0]));
                        today.set(Calendar.MINUTE, Integer.parseInt(confHead.getHoraInicio().split(":")[1]));
                        dfdHoraInicio.setValue(today.getTime());
                        today.set(Calendar.HOUR_OF_DAY, Integer.parseInt(confHead.getHoraFin().split(":")[0]));
                        today.set(Calendar.MINUTE, Integer.parseInt(confHead.getHoraFin().split(":")[1]));
                        dfdHoraFin.setValue(today.getTime());
                    } catch(Exception ignore) {}
//                    btnGuardar.setEnabled(false);
//                    tfdNombre.setEnabled(false);
                    configuracion = bcrConfiguraciones.getItem(itemid).getBean();
                    action = DaoImp.ACTION_UPDATE;
                }
            }
        });
        tableConfiguracion.setValue(bcrConfiguraciones.firstItemId());
        tableConfiguracion.addStyleName(ValoTheme.TABLE_COMPACT);
        tableConfiguracion.addStyleName(ValoTheme.TABLE_SMALL);
        tableConfiguracion.setVisibleColumns(new String[]{"nombre", "colDelete"});
        tableConfiguracion.setColumnHeaders(new String[]{"Modo servicio", "Borrar"});
        tableConfiguracion.setColumnAlignments(Table.Align.LEFT, Table.Align.CENTER);
    }

    private void buildTableBomba() {
        tableBomba = utils.buildTable("Bombas:", 100f, 100f, bcrEstConf,
                new String[]{"bombaNombre"},
                new String[]{"Bomba"});
        tableBomba.setSizeUndefined();
        tableBomba.setHeight("250px");
        tableBomba.addStyleName(ValoTheme.TABLE_COMPACT);
        tableBomba.addStyleName(ValoTheme.TABLE_SMALL);
        tableBomba.addGeneratedColumn("colServType", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("autoService");  //Atributo del bean
//                Switch swhAuto = new Switch();
                CheckBox swhAuto = new CheckBox();
                swhAuto.setPropertyDataSource(pro);
//                swhAuto.setAnimationEnabled(true);
                return swhAuto;
            }
        });
        tableBomba.setVisibleColumns(new Object[]{"bombaNombre", "colServType"});
        tableBomba.setColumnHeaders(new String[]{"Bomba", "¿es AUTO?"});
        tableBomba.setColumnAlignments(Table.Align.LEFT, Table.Align.CENTER);
    }

    private void buildButtons() {
        btnEliminar = new Button("Eliminar", FontAwesome.TRASH);
        btnEliminar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnEliminar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (tableConfiguracion.getValue() == null) {
                    Notification.show("Primero elija una configuración.", Notification.Type.ERROR_MESSAGE);
                    return;
                }

                MessageBox
                        .createQuestion()
                        .withCaption("Confirmación")
                        .withMessage("¿Está seguro de eliminar la configuración (" + configuracion.getNombre() + ")?")
                        .withOkButton(new Runnable() {
                            public void run() {
                                configuracion.setEstado("I");
                                configuracion.setModificadoPor(user.getUsername());
                                SvcConfBombaEstacion service = new SvcConfBombaEstacion();
                                configuracion = service.doAction(DaoImp.ACTION_DELETE, configuracion);
//                                service.closeConnections(); //ASG
                                if (configuracion.getEstacionId() != null) {
                                    Notification.show("La acción se ha ejecutado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_CONF_BOMBA_ESTACION.getViewName());
                                } else {
                                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + configuracion.getDescError(), Notification.Type.ERROR_MESSAGE);
                                    return;
                                }
                            }
                        }, ButtonOption.caption("Aceptar")
                        )
                        .withCancelButton(new Runnable() {
                            public void run() {
                                /*Nothing to do here*/
                            }
                        }, ButtonOption.caption("Cancelar")
                        )
                        .open();
                //Fin MessageBox                

            }
        });

        btnAgregar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAgregar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (cbxEstacion.getValue() != null) {
                    configuracion = new EstacionConfHead();
                    SvcConfBombaEstacion service = new SvcConfBombaEstacion();
                    List<Bomba> bombas = service.getBombasByEstacionid(((Estacion) cbxEstacion.getValue()).getEstacionId());
//                    service.closeConnections(); //ASG
                    EstacionConf ecf;
                    int index = 1;
                    bcrEstConf.removeAllItems();
                    for (Bomba b : bombas) {
                        ecf = new EstacionConf(null, b.getId(), 1, null);
                        ecf.setIdDto(index++);
                        ecf.setBombaNombre(b.getNombre());
                        ecf.setAutoService(true);   //1:Auto, 2:Full
                        bcrEstConf.addBean(ecf);
                    }
                    tableConfiguracion.setValue(null);
                    tfdNombre.setValue("");
//                    btnGuardar.setEnabled(true);
//                    tfdNombre.setEnabled(true);
                    cbxEstado.setValue(estados.get(0));
                    dfdHoraInicio.setValue(new Date());
                    dfdHoraFin.setValue(new Date());
                    action = DaoImp.ACTION_ADD;
                } else {
                    Notification.show("Primero elija una estación.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
            }
        });

        btnGuardar = new Button("Guardar", FontAwesome.SAVE);
        btnGuardar.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnGuardar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String nombre = (tfdNombre.getValue() != null && !tfdNombre.getValue().trim().isEmpty()) ? tfdNombre.getValue().trim().toUpperCase() : null;
                if (!cbxPais.isValid() || !cbxEstacion.isValid() || !tfdNombre.isValid() || nombre == null  || !cbxEstado.isValid() || !dfdHoraInicio.isValid() || !dfdHoraFin.isValid()) {
                    Notification.show("Todos los valores son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                for (Integer itemid : bcrConfiguraciones.getItemIds()) {
                    if (action.equals(DaoImp.ACTION_ADD) && bcrConfiguraciones.getItem(itemid).getBean().getNombre().toUpperCase().equals(nombre)) {
                        Notification.show("El nombre de configuracion ya existe, elija otro.", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }
                configuracion.setEstacionConf(new ArrayList());
                for (Integer itemid : bcrEstConf.getItemIds()) {
                    configuracion.getEstacionConf().add(bcrEstConf.getItem(itemid).getBean());
                }
                configuracion.setNombre(nombre);
                configuracion.setEstacionId(((Estacion) cbxEstacion.getValue()).getEstacionId());
                configuracion.setEstado( ((DtoBean)cbxEstado.getValue()).getEstado() );
                configuracion.setCreadoPor(user.getUsername());
                configuracion.setModificadoPor(user.getUsername());
                configuracion.setHoraInicio(Constant.SDF_HHmm.format(dfdHoraInicio.getValue()));
                configuracion.setHoraFin(Constant.SDF_HHmm.format(dfdHoraFin.getValue()));
                SvcConfBombaEstacion service = new SvcConfBombaEstacion();
                configuracion = service.doAction(action, configuracion);
//                service.closeConnections(); //ASG
                if (configuracion.getEstacionconfheadId() > 0) {
                    Notification notif = new Notification("ÉXITO:", "El registro de configuración se realizó con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    //notif.setStyleName("mystyle");
                    //notif.setIcon(new ThemeResource("img/reindeer.png"));
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_CONF_BOMBA_ESTACION.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + configuracion.getDescError(), Notification.Type.ERROR_MESSAGE);
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
