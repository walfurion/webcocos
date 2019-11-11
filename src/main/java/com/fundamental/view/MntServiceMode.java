package com.fundamental.view;

import com.fundamental.model.Acceso;
import com.fundamental.model.Bomba;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.EstacionConf;
import com.fundamental.model.EstacionConfHead;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoBean;
import com.fundamental.model.dto.DtoGenericBean;
import com.fundamental.services.Dao;
import com.fundamental.services.SvcConfBombaEstacion;
import com.fundamental.services.SvcMaintenance;
import com.fundamental.utils.Constant;
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
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
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
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
public class MntServiceMode extends Panel implements View {

    ComboBox cbxPais, //cbxEstacion, 
            cbxEstado;
    TextField tfdNombre = new TextField("Modo servicio:");
    Button btnAdd = new Button("Agregar", FontAwesome.PLUS),
            btnSave,
            btnEliminar;
    Table tblConfiguracion, tblBombas;
    Table tblStations;
    private BeanContainer<Integer, Estacion> bcrEstaciones = new BeanContainer<>(Estacion.class);
    private Estacion estacion;

    private BeanContainer<Integer, EstacionConfHead> bcrConfiguraciones = new BeanContainer<>(EstacionConfHead.class);
    private BeanContainer<Integer, EstacionConf> bcrEstConf = new BeanContainer<>(EstacionConf.class);

    List<Pais> paises = new ArrayList();
    EstacionConfHead configuracion;
    List<DtoBean> estados = new ArrayList(Arrays.asList(
            new DtoBean(1, "ACTIVO", "A"), new DtoBean(2, "INACTIVO", "I")
    ));
    List<DtoGenericBean> listServicios = new ArrayList(Arrays.asList(
            new DtoGenericBean(1, "AUTO"), new DtoGenericBean(2, "FULL")
    ));
    String action;

//template
    private final VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Usuario user;
    
    Acceso acceso = new Acceso();

    public MntServiceMode() {
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
        vlRoot.addComponent(utils.buildHeader("Modo de servicio", false, true));
        vlRoot.addComponent(utils.buildSeparator());
        getAllData();
//template

        buildFilters();
        buildTableConf();
        buildTableBomba();
        buildTableStations();
        buildButtons();

        VerticalLayout vlRight = new VerticalLayout(//utils.vlContainer(cbxPais), utils.vlContainer(cbxEstacion), 
                tblConfiguracion, btnAdd);
        vlRight.setSizeUndefined();
        vlRight.setId("vlRight");
        vlRight.setMargin(new MarginInfo(false, true, true, false));
        vlRight.setComponentAlignment(btnAdd, Alignment.TOP_CENTER);
        Responsive.makeResponsive(vlRight);

        VerticalLayout vlCenter = utils.buildVertical("vlCenter", false, false, true, false, null);
        vlCenter.setSizeUndefined();
        vlCenter.addComponents(cbxPais, tblStations);
        vlCenter.setComponentAlignment(cbxPais, Alignment.MIDDLE_CENTER);
        vlCenter.setMargin(new MarginInfo(false, true, false, true));
        Responsive.makeResponsive(vlCenter);

        VerticalLayout vlLeft = new VerticalLayout(tfdNombre, cbxEstado, utils.vlContainer(tblBombas), btnSave);
        vlLeft.setSizeUndefined();
        vlLeft.setId("vlLeft");
        vlLeft.setComponentAlignment(btnSave, Alignment.TOP_CENTER);
        Responsive.makeResponsive(vlLeft);

        CssLayout cltTables = new CssLayout(vlRight, vlCenter, vlLeft);
        cltTables.setId("cltTables");
        cltTables.setSizeUndefined();
        Responsive.makeResponsive(cltTables);

        vlRoot.addComponents(cltTables);
        vlRoot.setExpandRatio(cltTables, 1);

        if (bcrConfiguraciones.size() > 0) {
            int kkk = bcrConfiguraciones.firstItemId();
            tblConfiguracion.setValue(bcrConfiguraciones.getItem(kkk));
        }
    }

    public void getAllData() {
        bcrConfiguraciones.setBeanIdProperty("estacionconfheadId");
        bcrEstConf.setBeanIdProperty("idDto");
        SvcConfBombaEstacion service = new SvcConfBombaEstacion();
        paises = service.getAllPaises();
        bcrConfiguraciones.removeAllItems();
        bcrConfiguraciones.addAll(service.getAllConfiguracionHead(true));
        bcrEstaciones.setBeanIdProperty("estacionId");
        service.closeConnections();
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
                bcrEstaciones.removeAllItems();
                bcrEstConf.removeAllItems();
                if (cbxPais.getValue() != null) {
                    List<Estacion> estaciones;
                    SvcConfBombaEstacion service = new SvcConfBombaEstacion();
                    estaciones = service.getStationsByCountry(((Pais) cbxPais.getValue()).getPaisId(), true);
                    service.closeConnections();
                    bcrEstaciones.addAll(estaciones);
                }
            }
        });

        tfdNombre.setRequired(true);
        tfdNombre.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        cbxEstado = new ComboBox("Estado:", new ListContainer<DtoBean>(DtoBean.class, estados));
        cbxEstado.setItemCaptionPropertyId("nombre");
        cbxEstado.setSizeUndefined();
        cbxEstado.setNullSelectionAllowed(false);
        cbxEstado.setRequired(true);
        cbxEstado.addStyleName(ValoTheme.TEXTFIELD_SMALL);

    }

    private void buildTableConf() {
        tblConfiguracion = utils.buildTable("Configuraciones:", 100f, 100f, bcrConfiguraciones,
                new String[]{"nombre"},
                new String[]{"Modo servicio"});
        tblConfiguracion.setSizeUndefined();
        tblConfiguracion.setSelectable(true);
        tblConfiguracion.setHeight("300px");
        tblConfiguracion.setWidth("400px");
        tblConfiguracion.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                    bcrEstConf.removeAllItems();
                if (tblConfiguracion.getValue() != null) {
                    int itemid = Integer.parseInt(tblConfiguracion.getValue().toString());
                    EstacionConfHead confHead = bcrConfiguraciones.getItem(itemid).getBean();
                    tfdNombre.setValue(confHead.getNombre());
                    String state = confHead.getEstado();
                    cbxEstado.setValue(state.equals("A") ? estados.get(0) : estados.get(1));
                    try {
                    } catch (Exception ignore) {
                    }
                    configuracion = bcrConfiguraciones.getItem(itemid).getBean();
                    action = Dao.ACTION_UPDATE;
                }

                if (tblConfiguracion.getValue() != null && tblStations != null && tblStations.getValue() != null) {
//                    EstacionConfHead estConfHead = bcrConfiguraciones.getItem(tblConfiguracion.getValue()).getBean();
//                    bcrEstConf.addAll(estConfHead.getEstacionConf());
//                    estacion = bcrEstaciones.getItem(tblStations.getValue()).getBean();
tblStations.setValue(null);
                    action = Dao.ACTION_UPDATE;
                }
            }
        });
        tblConfiguracion.setValue(bcrConfiguraciones.firstItemId());
        tblConfiguracion.addStyleName(ValoTheme.TABLE_COMPACT);
        tblConfiguracion.addStyleName(ValoTheme.TABLE_SMALL);

        tblConfiguracion.addGeneratedColumn("colEstado", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Label result = new Label();
                String estado = bcrConfiguraciones.getItem(itemId).getBean().getEstado();
                result.setValue(estado.equals("A") ? "Activo" : "Inactivo");
                result.setWidth("75px");
                return result;
            }
        });
        tblConfiguracion.setVisibleColumns(new Object[]{"nombre", "colEstado"});
        tblConfiguracion.setColumnHeaders(new String[]{"Modo servicio", "Estado"});
        tblConfiguracion.setColumnAlignments(Table.Align.LEFT, Table.Align.LEFT);
    }

    private void buildTableBomba() {
        tblBombas = utils.buildTable("Bombas:", 100f, 100f, bcrEstConf,
                new String[]{"bombaNombre"},
                new String[]{"Bomba"});
        tblBombas.setSizeUndefined();
        tblBombas.setHeight("250px");
        tblBombas.addStyleName(ValoTheme.TABLE_COMPACT);
        tblBombas.addStyleName(ValoTheme.TABLE_SMALL);
        tblBombas.addGeneratedColumn("colServType", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("autoService");  //Atributo del bean
//                Switch swhAuto = new Switch();
                CheckBox swhAuto = new CheckBox();
                swhAuto.setPropertyDataSource(pro);
                swhAuto.addStyleName(ValoTheme.COMBOBOX_SMALL);
//                swhAuto.setAnimationEnabled(true);
                return swhAuto;
            }
        });
        tblBombas.addGeneratedColumn("colServTypeC", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Property pro = source.getItem(itemId).getItemProperty("typeServ");  //Atributo del bean
                ComboBox cbxType = utils.buildCombobox("", "name", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<DtoGenericBean>(DtoGenericBean.class, listServicios));
                cbxType.setPropertyDataSource(pro);
                cbxType.setWidth("100px");
                return cbxType;
//                Switch swhAuto = new Switch();
//                CheckBox swhAuto = new CheckBox();
//                swhAuto.setPropertyDataSource(pro);
//                swhAuto.addStyleName(ValoTheme.COMBOBOX_SMALL);
////                swhAuto.setAnimationEnabled(true);
//                return swhAuto;
            }
        });
        tblBombas.setVisibleColumns(new Object[]{"bombaNombre", "colServTypeC"});
        tblBombas.setColumnHeaders(new String[]{"Bomba", "Servicio"});
        tblBombas.setColumnAlignments(Table.Align.LEFT, Table.Align.CENTER);
    }

    private void buildButtons() {
        btnEliminar = new Button("Eliminar", FontAwesome.TRASH);
        btnEliminar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnEliminar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (tblConfiguracion.getValue() == null) {
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
                                configuracion = service.doAction(Dao.ACTION_DELETE, configuracion);
                                service.closeConnections();
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

        btnAdd.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
//                if (cbxEstacion.getValue() != null) {
                configuracion = new EstacionConfHead();
//                    SvcConfBombaEstacion service = new SvcConfBombaEstacion();
//                    List<Bomba> bombas = service.getBombasByEstacionid(((Estacion) cbxEstacion.getValue()).getEstacionId());
//                    List<Bomba> bombas = service.getAllBombas(false);
//                    service.closeConnections();
//                    EstacionConf ecf;
//                    int index = 1;
                bcrEstConf.removeAllItems();
//                    for (Bomba b : bombas) {
//                        ecf = new EstacionConf(null, b.getId(), 1, null);
//                        ecf.setIdDto(index++);
//                        ecf.setBombaNombre(b.getNombre());
//                        ecf.setAutoService(true);   //1:Auto, 2:Full
//                        bcrEstConf.addBean(ecf);
//                    }
                tblConfiguracion.setValue(null);
                tblStations.setValue(null);
                tfdNombre.setValue("");
//                    btnSave.setEnabled(true);
//                    tfdNombre.setEnabled(true);
                cbxEstado.setValue(estados.get(0));
//                    dfdHoraInicio.setValue(new Date());
//                    dfdHoraFin.setValue(new Date());
                action = Dao.ACTION_ADD;
//                } else {
//                    Notification.show("Primero elija una estación.", Notification.Type.ERROR_MESSAGE);
//                    return;
//                }
                cbxPais.setValue(null);
            }
        });

        btnSave = new Button("Guardar", FontAwesome.SAVE);
        btnSave.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String nombre = (tfdNombre.getValue() != null && !tfdNombre.getValue().trim().isEmpty()) ? tfdNombre.getValue().trim().toUpperCase() : null;
                if (//!cbxPais.isValid() || !cbxEstacion.isValid() || 
                        !tfdNombre.isValid() || nombre == null || !cbxEstado.isValid() //|| !dfdHoraInicio.isValid() || !dfdHoraFin.isValid()
                        ) {
                    Notification.show("Todos los valores son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                for (Integer itemid : bcrConfiguraciones.getItemIds()) {
                    if (action.equals(Dao.ACTION_ADD) && bcrConfiguraciones.getItem(itemid).getBean().getNombre().toUpperCase().equals(nombre)) {
                        Notification.show("El nombre de configuracion ya existe, elija otro.", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }
                if (tblStations.getValue() == null) {
                    Notification.show("Debe elegir una estación.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                configuracion.setEstacionConf(new ArrayList());
                for (Integer itemid : bcrEstConf.getItemIds()) {
                    if (bcrEstConf.getItem(itemid).getBean().getTypeServ()==null) {
                        Notification.show("Debe elegir un modo de servicio para cada bomba.", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                    configuracion.getEstacionConf().add(bcrEstConf.getItem(itemid).getBean());
                }
                configuracion.setNombre(nombre);
//                configuracion.setEstacionId(((Estacion) cbxEstacion.getValue()).getEstacionId());
                configuracion.setEstado(((DtoBean) cbxEstado.getValue()).getEstado());
                configuracion.setCreadoPor(user.getUsername());
                configuracion.setModificadoPor(user.getUsername());
//                configuracion.setHoraInicio(Constant.SDF_HHmm.format(dfdHoraInicio.getValue()));
//                configuracion.setHoraFin(Constant.SDF_HHmm.format(dfdHoraFin.getValue()));
                configuracion.setHoraInicio(Constant.SDF_HHmm.format(new Date()));
                configuracion.setHoraFin(Constant.SDF_HHmm.format(new Date()));
                SvcConfBombaEstacion service = new SvcConfBombaEstacion();
                configuracion = service.doAction(action, configuracion);
                service.closeConnections();
                if (configuracion.getEstacionconfheadId() > 0) {
                    Notification notif = new Notification("ÉXITO:", "El registro de configuración se realizó con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    //notif.setStyleName("mystyle");
                    //notif.setIcon(new ThemeResource("img/reindeer.png"));
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_MODOSERV.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + configuracion.getDescError(), Notification.Type.ERROR_MESSAGE);
                    return;
                }
            }
        });

    }

    private void buildTableStations() {
        tblStations = utils.buildTable("Estaciones:", 100f, 100f, bcrEstaciones,
                new String[]{"paisNombre", "codigo", "nombre"},
                new String[]{"País", "Código", "Nombre"});
        tblStations.setStyleName(ValoTheme.TABLE_COMPACT);
        tblStations.setStyleName(ValoTheme.TABLE_SMALL);
        tblStations.setSelectable(true);
        tblStations.setSizeUndefined();
        tblStations.setHeight("400px");
        tblStations.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                bcrEstConf.removeAllItems();
                if (tblConfiguracion.getValue() != null && tblStations.getValue() != null) {
                    EstacionConfHead estConfHead = bcrConfiguraciones.getItem(tblConfiguracion.getValue()).getBean();
                    estacion = bcrEstaciones.getItem(tblStations.getValue()).getBean();
                    for (EstacionConf ecf : estConfHead.getEstacionConf()) {
                        System.out.println(">>>>>::: "+ ecf.getEstacionId() +";   "+ estacion.getEstacionId());
                        if (ecf.getEstacionId().equals(estacion.getEstacionId())) {
                            bcrEstConf.addBean(ecf);
                        }
                    }
                    if (bcrEstConf.size()==0) {  //Cuando no haya configuraciones asociadas.
                        SvcMaintenance service = new SvcMaintenance();
                        List<Bomba> pumps = service.getBombasByEstacionid(Integer.parseInt(tblStations.getValue().toString()));
                        service.closeConnections();
                        EstacionConf ecf;
                        int index = 1;
                        for (Bomba b : pumps) {
                            ecf = new EstacionConf(null, b.getId(), null, user.getUsername());
                            ecf.setIdDto(index++);
                            ecf.setBombaNombre(b.getNombre());
                            System.out.println("getEstacionId()::: "+ bcrEstaciones.getItem(tblStations.getValue()).getBean().getEstacionId());
                            ecf.setEstacionId(bcrEstaciones.getItem(tblStations.getValue()).getBean().getEstacionId());
                            bcrEstConf.addBean(ecf);
                        }
                    }
//                binder.setItemDataSource(bcrEstaciones.getItem(tblStations.getValue()));
//                chkMuestraPOS.setValue(estacion.getFactElectronica().equals("S") ? true : false);
//                defineSelectedBombas();
//                defineSelectedProducts();
                    action = Dao.ACTION_UPDATE;
                } else if (action.equals(Dao.ACTION_ADD) && tblConfiguracion.getValue() == null && tblStations.getValue() != null) {
                    SvcMaintenance service = new SvcMaintenance();
                    List<Bomba> pumps = service.getBombasByEstacionid(Integer.parseInt(tblStations.getValue().toString()));
                    service.closeConnections();
                    EstacionConf ecf;
                    int index = 1;
                    for (Bomba b : pumps) {
                        ecf = new EstacionConf(null, b.getId(), null, user.getUsername());
                        ecf.setIdDto(index++);
                        ecf.setBombaNombre(b.getNombre());
                        System.out.println("getEstacionId()::: "+ bcrEstaciones.getItem(tblStations.getValue()).getBean().getEstacionId());
                        ecf.setEstacionId(bcrEstaciones.getItem(tblStations.getValue()).getBean().getEstacionId());
                        bcrEstConf.addBean(ecf);
                    }

                }
            }
        });
//        tblStations.setValue(bcrEstaciones.firstItemId());
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

}
