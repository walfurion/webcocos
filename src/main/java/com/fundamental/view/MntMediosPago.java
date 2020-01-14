package com.fundamental.view;

import com.fundamental.model.Acceso;
import com.fundamental.services.Dao;
import com.fundamental.model.Mediopago;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Tipoproducto;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoGenericBean;
import com.fundamental.services.SvcMaintenance;
import com.fundamental.services.SvcMntMedioPago;
import com.fundamental.utils.Constant;
import com.fundamental.utils.Util;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.maddon.ListContainer;

/**
 * @author Henry Barrientos
 */
public class MntMediosPago extends Panel implements View {

    private Button btnAdd,
            btnSave,
            btnFilterClear;

    private Table tblData;
    private TextField tfdFilter;
    @PropertyId("nombre")
    private TextField nombre;
    @PropertyId("partidacontPor")
    private TextField tfdPartidacontPorc;
    @PropertyId("orden")
    private TextField tfdOrden;
    @PropertyId("partidacont")
    private CheckBox chxPartidacont = new CheckBox("¿Se usa en partida contable?");
    ;
    @PropertyId("country")
    private ComboBox cbxPais;
    @PropertyId("isTCredito")
    private CheckBox chxTCredito = new CheckBox("¿Es tarjeta de crédito?");
    @PropertyId("status")
    private ComboBox cbxStatus;
    private ComboBox cbxTipo, cbxTipoproducto;
    private CssLayout cltFilter;

    private BeanContainer<Integer, Mediopago> bcrMediopago = new BeanContainer<Integer, Mediopago>(Mediopago.class);
//    private BeanContainer<Integer, Pais> bcrPais = new BeanContainer<Integer, Pais>(Pais.class);
//    private Container contTipo;

//    private FormLayout form = new FormLayout();
    private BeanFieldGroup<Mediopago> binder = new BeanFieldGroup<Mediopago>(Mediopago.class);
    private Mediopago mediopago;
    private Pais pais;
    private List<Pais> paises;
    private List<Tipoproducto> productTypes;

//
    private final VerticalLayout vlRoot;
    private Utils utils = new Utils();
    private Util ut = new Util();
    private Usuario user;
    String action;
    private List<DtoGenericBean> listStatus;
    Acceso acceso = new Acceso();

    public MntMediosPago() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);

        vlRoot = new VerticalLayout();
        vlRoot.setSizeFull();
        vlRoot.setMargin(true);
        vlRoot.addStyleName("dashboard-view");
        setContent(vlRoot);
        Responsive.makeResponsive(vlRoot);
        vlRoot.setId("vlRoot");

        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        vlRoot.addComponent(utils.buildHeader("Medio de pago", false, true));
        vlRoot.addComponent(utils.buildSeparator());
        getAllData();
        initControls();

        CssLayout content = new CssLayout();
        content.setId("mainContent");
        content.addStyleName("dashboard-panels");
        Responsive.makeResponsive(content);
//
        buildTableData();

        VerticalLayout vlCenter = new VerticalLayout(cltFilter, tblData, btnAdd);
        vlCenter.setComponentAlignment(cltFilter, Alignment.MIDDLE_CENTER);
        vlCenter.setComponentAlignment(btnAdd, Alignment.MIDDLE_CENTER);
        vlCenter.setSizeUndefined();
        vlCenter.setSpacing(false);
        vlCenter.setMargin(false);
        vlCenter.setId("vlCenter");
        Responsive.makeResponsive(vlCenter);

        /*Se retira el cbxTipoProducto 
          se retira chkpartida contable 
          se retira porcentaje partida contable
        ASG*/
        VerticalLayout vlRight = new VerticalLayout(cbxPais, /*cbxTipoproducto ,*/ nombre, cbxTipo, tfdOrden, cbxStatus, /*chxPartidacont, tfdPartidacontPorc,*/ chxTCredito, btnSave);
        vlRight.setSpacing(true);
        vlRight.setMargin(false);
        vlRight.setSizeUndefined();
        vlRight.setId("vlRight");
        vlRight.setComponentAlignment(btnSave, Alignment.MIDDLE_CENTER);
        Responsive.makeResponsive(vlRight);

        content.addComponents(utils.vlContainer(vlRight), utils.vlContainer(vlCenter));

        vlRoot.addComponent(content);
        vlRoot.setExpandRatio(content, 1);

    }

    private void getAllData() {
        bcrMediopago.setBeanIdProperty("mediopagoId");
        bcrMediopago.removeAllItems();
        SvcMaintenance service = new SvcMaintenance();
        paises = service.getAllPaises();
        int idPais = 0;
        if(cbxPais!=null){
            Pais pais = new Pais();
            pais = (Pais) cbxPais.getValue();
            idPais = pais.getPaisId();
            System.out.println("entra al combo "+idPais);
        }else{
            idPais = service.recuperaIdPais();
            System.out.println("entra al else "+idPais);
        }        
        productTypes = service.getAllTipoproducto(true);
        bcrMediopago.addAll(service.getAllMediosPago(true,idPais));
        service.closeConnections();
        if(bcrMediopago.size()>0){
            int firstItemId = bcrMediopago.getItemIds().get(0);
            mediopago = bcrMediopago.getItem(firstItemId).getBean();
        }        
        listStatus = Arrays.asList(new DtoGenericBean("I", "Inactivo"), new DtoGenericBean("A", "Activo"));
    }

    private void initControls() {
        tfdFilter = utils.buildTextFilter("Búsqueda tabla");
        tfdFilter.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        tfdFilter.addShortcutListener(new ShortcutListener("Clear", ShortcutAction.KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                tfdFilter.setValue("");
                ((Filterable) tblData.getContainerDataSource()).removeAllContainerFilters();
            }
        });
        tfdFilter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                Container.Filterable data = (Container.Filterable) tblData.getContainerDataSource();
                data.removeAllContainerFilters();
                data.addContainerFilter(new Container.Filter() {
                    @Override
                    public boolean passesFilter(final Object itemId, final Item item) {
                        if (event.getText() == null || event.getText().equals("")) {
                            return true;
                        }
                        return utils.filterByProperty("nombre", item, event.getText())
                                || utils.filterByProperty("nombrePais", item, event.getText());
                    }

                    @Override
                    public boolean appliesToProperty(final Object propertyId) {
                        if (propertyId.equals("nombre")
                                || propertyId.equals("nombrePais")) {
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
                ((Filterable) tblData.getContainerDataSource()).removeAllContainerFilters();
            }
        });

        cltFilter = new CssLayout();
        cltFilter.addComponents(tfdFilter, btnFilterClear);
        cltFilter.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        nombre = utils.buildTextField("Nombre:", "", false, 50, true, ValoTheme.TEXTFIELD_SMALL);

        cbxTipo = utils.buildCombobox("Tipo:", "name", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<DtoGenericBean>(DtoGenericBean.class, Constant.TIPOS_EFECTIVO));
        cbxTipo.setValue(Constant.TIPOS_EFECTIVO.get(mediopago.getTipo() - 1));

        cbxPais = utils.buildCombobox("País:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<Pais>(Pais.class, paises));
        cbxPais.setItemIconPropertyId("flag");
        cbxPais.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                getAllData();
            }
        });

        cbxTipoproducto = utils.buildCombobox("Para producto:", "nombre", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<Tipoproducto>(Tipoproducto.class, productTypes));

        cbxStatus = utils.buildCombobox("Estado:", "name", false, true, ValoTheme.COMBOBOX_SMALL, new ListContainer<DtoGenericBean>(DtoGenericBean.class, listStatus));

        tfdPartidacontPorc = utils.buildTextField("Porcentaje en Part cont:", "0", false, 6, false, ValoTheme.TEXTFIELD_SMALL);
        tfdPartidacontPorc.addStyleName("align-right");
        tfdPartidacontPorc.setVisible(false);

        chxPartidacont.setVisible(false);
//        chxTCredito.setVisible(false); //ASG
        chxTCredito.setVisible(true);//ASG

        tfdOrden = utils.buildTextField("Orden:", "0", false, 2, true, ValoTheme.TEXTFIELD_SMALL);
        tfdOrden.addStyleName("align-right");

        chxPartidacont.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                tfdPartidacontPorc.setReadOnly(false);
                if (!chxPartidacont.getValue()) {
                    tfdPartidacontPorc.setValue("0");
                    tfdPartidacontPorc.setReadOnly(true);
                }
            }
        });

//Importante
        binder.bindMemberFields(this);
        binder.setItemDataSource(mediopago);

        btnAdd = new Button("Agregar", FontAwesome.PLUS);
        btnAdd.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnAdd.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                mediopago = new Mediopago();
                cbxTipoproducto.setValue(null);
                cbxTipo.setValue(null);
                mediopago.setPaises(new ArrayList());
                mediopago.setPartidacontPor(0D);
                tfdPartidacontPorc.setReadOnly(true);
                tfdPartidacontPorc.setVisible(true);
                chxPartidacont.setVisible(true);
                chxTCredito.setVisible(true);
//                binder.bindMemberFields(this);
                binder.setItemDataSource(mediopago);
                action = Dao.ACTION_ADD;
                nombre.focus();
                tblData.setValue(null);
            }
        });

        btnSave = new Button("Guardar", FontAwesome.SAVE);
        btnSave.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnSave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!nombre.isValid() || !cbxTipo.isValid() || !cbxPais.isValid() || !cbxStatus.isValid()) {
                    Notification.show("Por favor, todos los campos son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                if(ut.isNumber(tfdOrden.getValue())==0){
                    Notification.show("El campo Orden debe ser numerico.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                mediopago.setEstado(((DtoGenericBean) cbxStatus.getValue()).getStringId());
//                mediopago.setTipoprodId(((Tipoproducto) cbxTipoproducto.getValue()).getTipoId());  //ASG 
                mediopago.setTipo(((DtoGenericBean) cbxTipo.getValue()).getId());
                mediopago.setCreadoPor(user.getUsername());
                mediopago.setModificadoPor(user.getUsername());
                try {
                    binder.commit();
                } catch (FieldGroup.CommitException ex) {
                    Logger.getLogger(MntMediosPago.class.getName()).log(Level.SEVERE, null, ex);
                }
                mediopago.setPartidacontPor((!mediopago.isPartidacont()) ? 0D : mediopago.getPartidacontPor());

                SvcMntMedioPago service = new SvcMntMedioPago();
                mediopago = service.doAction(action, mediopago);
                Mediopago item;
                for (Integer id : bcrMediopago.getItemIds()) {
                    item = bcrMediopago.getItem(id).getBean();
                    item.setPartidacont(mediopago.getPartidacontPor() != null);
                    service.doAction(Dao.ACTION_UPDATE, item);
                }
                service.closeConnections();
                if (mediopago.getMediopagoId() != null) {
                    Notification notif = new Notification("¡Exito!", "La acción se ha ejecutado correctamente.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.MNT_MEDIOSPAGO.getViewName());
                } else {
                    Notification.show("Ocurrió un error al ejecutar la acción. \n" + mediopago.getDescError(), Notification.Type.ERROR_MESSAGE);
                }
            }
        });

    }

    private void buildTableData() {
        tblData = utils.buildTable("Medios de pago:", 100f, 100f, bcrMediopago,
                new String[]{"nombre", "nombrePais"},
                new String[]{"Nombre", "Pais"});
        tblData.addStyleName(ValoTheme.TABLE_COMPACT);
        tblData.addStyleName(ValoTheme.TABLE_SMALL);
        tblData.setSelectable(true);
        tblData.setHeight("475px");
        tblData.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (tblData.getValue() != null) {
                    mediopago = bcrMediopago.getItem(tblData.getValue()).getBean();
                    binder.setItemDataSource(mediopago);
//                    defineSelectedPaises();
                    action = Dao.ACTION_UPDATE;
                    cbxTipo.setValue(Constant.TIPOS_EFECTIVO.get(mediopago.getTipo() - 1));
//                    for (Pais item : paises) {
//                        if (item.getPaisId().equals( mediopago.getPaisId() )) {
//                            cbxPais.setValue(item); break;
//                        }
//                    }
                    for (Tipoproducto item : productTypes) {
                        if (item.getTipoId() == mediopago.getTipoprodId()) {
                            cbxTipoproducto.setValue(item);
                            break;
                        }
                    }
//                    chxTCredito.setValue(mediopago.isIsTCredito());
                    tfdPartidacontPorc.setReadOnly(!mediopago.isPartidacont());
                }
            }
        });
        tblData.setValue(bcrMediopago.firstItemId());
        tblData.setSizeUndefined();
        tblData.addGeneratedColumn("colPorcentaje", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                TextField result = utils.buildTextField("", "0", false, 6, false, ValoTheme.TEXTFIELD_SMALL);
                Property prop = source.getItem(itemId).getItemProperty("partidacontPor");  //Atributo del bean
                result.setPropertyDataSource(prop);
                result.setWidth("75px");
                result.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
                return result;
            }
        });
        tblData.addGeneratedColumn("colEstado", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Label result = new Label();
                String estado = bcrMediopago.getItem(itemId).getBean().getEstado();
                result.setValue(estado.equals("A") ? "Activo" : "Inactivo");
                result.setWidth("75px");
                return result;
            }
        });
        tblData.addGeneratedColumn("colTarjeta", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                CheckBox result = new CheckBox("");
                Property prop = source.getItem(itemId).getItemProperty("isTCredito");  //Atributo del bean
                result.setPropertyDataSource(prop);
                result.addStyleName(ValoTheme.CHECKBOX_SMALL);
                return result;
            }
        });
//        tblData.setHeight("400px");
//        tblData.setWidth("350px");
//        tblData.setVisibleColumns(new Object[]{"nombrePais", "orden", "nombre", "colPorcentaje", "colTarjeta", "colEstado"});
//        tblData.setColumnHeaders(new String[]{"Pais", "Orden", "Medio pago", "Porcentaje en Part cont", "¿Es tarjeta de crédito?", "Estado"});
        tblData.setVisibleColumns(new Object[]{"nombrePais", "orden", "nombre", "colTarjeta", "colEstado"}); //ASG RETIRA %PARTIDA CONT
        tblData.setColumnHeaders(new String[]{"Pais", "Orden", "Medio pago", "¿Es tarjeta de crédito?", "Estado"}); //ASG RETIRA %PARTIDA CONT
        tblData.setColumnAlignments(new Table.Align[]{
            Table.Align.LEFT, Table.Align.LEFT, Table.Align.LEFT, /*Table.Align.CENTER,*/ Table.Align.CENTER, Table.Align.LEFT});
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Dao dao = new Dao();
        acceso = dao.getAccess(event.getViewName());
        dao.closeConnections();
        btnAdd.setEnabled(acceso.isAgregar());
        btnSave.setEnabled(acceso.isCambiar());
    }
}
