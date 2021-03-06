package com.fundamental.view;

import com.fundamental.model.Dia;
import com.fundamental.services.Dao;
import com.fundamental.model.EstacionConf;
import com.fundamental.model.EstacionConfHead;
import com.fundamental.model.Horario;
import com.fundamental.model.Precio;
import com.fundamental.model.Producto;
import com.fundamental.model.Turno;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.services.SvcEstacionConf;
import com.fundamental.services.SvcProducto;
import com.fundamental.services.SvcTurno;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.demo.dashboard.view.DashboardViewType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.vaadin.maddon.ListContainer;
import org.vaadin.ui.NumberField;

/**
 * @author Henry Barrientos
 */
public class PrTurnBkp extends Panel implements View {

    VerticalLayout root = new VerticalLayout();
    Button btnGuardar, btnModificar;
    Table tablePrecio;
    Label labelStation, labelTurno;
    TextField tfTurnoRef;
    ComboBox cbxConfiguracion;
    DateField fieldFecha;

    List<Producto> productos;
    BeanContainer<Integer, Producto> bcPrecios;
    Container contConfigs;

    Turno turno;
    List<Precio> precios;
    List<Horario> horariosHoy;
    Boolean showAutoservicio = false;
    Dia dia;

    //template
    private final VerticalLayout vlRoot;
    private CssLayout content = new CssLayout();
    Utils utils = new Utils();
    Usuario user;

    public PrTurnBkp() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        content.addStyleName("dashboard-panels");
        Responsive.makeResponsive(content);
        vlRoot = utils.buildVerticalR("vlRoot", true, false, true, "dashboard-view");
        setContent(vlRoot);
        Responsive.makeResponsive(vlRoot);
        vlRoot.addComponent(utils.buildHeaderR("Turno"));
        vlRoot.addComponent(utils.buildSeparator());
        vlRoot.addComponent(content);
        vlRoot.setExpandRatio(content, 1);
        user = (Usuario) VaadinSession.getCurrent().getAttribute(Usuario.class.getName());
        setAllData();
        //template

        buildTablePrecio();
        buildActions();

        Label lblMessage = new Label("Día actual (en operación):       ");
        lblMessage.setSizeUndefined();
        lblMessage.addStyleName(ValoTheme.LABEL_BOLD);
        lblMessage.addStyleName(ValoTheme.LABEL_H2);
        
        CssLayout cltToolbar = new CssLayout();
        cltToolbar.setSizeUndefined();
        cltToolbar.setWidth(100f, Unit.PERCENTAGE);
        Responsive.makeResponsive(cltToolbar);
        if (user.getPaisLogin().getPaisId().equals(320)) {  //Guatemala
            cltToolbar.addComponent(utils.vlContainer(tfTurnoRef));
        } else {
            cltToolbar.addComponent(utils.vlContainer(cbxConfiguracion));
        }
        if (dia.getEstacionId()!=null) {
            cltToolbar.addComponent(lblMessage);
        }
        cltToolbar.addComponent(utils.vlContainer(fieldFecha));
        CssLayout cltTable = new CssLayout(utils.vlContainer(tablePrecio));
        cltTable.setSizeUndefined();
        cltTable.setWidth(100f, Unit.PERCENTAGE);
        Responsive.makeResponsive(cltTable);

        CssLayout cltButton = new CssLayout(utils.vlContainer(btnGuardar), utils.vlContainer(btnModificar));
        cltButton.setSizeUndefined();
        cltButton.setWidth(100f, Unit.PERCENTAGE);
        Responsive.makeResponsive(cltButton);

        content.addComponents(cltToolbar, cltTable, cltButton);

    }

    private void setAllData() {
        SvcProducto svcProducto = new SvcProducto();
        productos = svcProducto.getCombustiblesByEstacionid(user.getEstacionLogin().getEstacionId());
        svcProducto.closeConnections();

        SvcTurno svcTurno = new SvcTurno();
        //Un horario tiene varios turnos, la siguiente linea va a traer el ultimo turno activo de una estacion (horario)
        Turno actualTurnoActivo = svcTurno.getTurnoActivoByEstacionid(user.getEstacionLogin().getEstacionId());
        Turno ultimoTurnoActivo = new Turno(); //svcTurno.getUltimoTurnoActivoByEstacionid(user.getEstacionLogin().getEstacionId());

        if (actualTurnoActivo.getTurnoId() != null) {
            turno = actualTurnoActivo;
        } else if (ultimoTurnoActivo.getTurnoId() != null) {
            turno = ultimoTurnoActivo;
        } else {
            turno = new Turno();
        }

        boolean startProducts = false;
        if (!user.getPaisLogin().getPaisId().equals(320) && turno.getTurnoId() != null) {

            //Ir a traer los precios del turno anterior.
            SvcTurno svcPrecio = new SvcTurno();
            precios = svcPrecio.getPreciosByTurnoid(turno.getTurnoId());
            svcPrecio.closeConnections();

            for (Producto p : productos) {
                NumberField nf = new NumberField();
                NumberField nf2 = new NumberField();
                nf.setDecimalPrecision(2);
                nf2.setDecimalPrecision(2);
                    nf.setValue(0D);
                    p.setPrecioAS(nf);
                    nf2.setValue(0D);
                    p.setPrecioSC(nf2);
                for (Precio pre : precios) {
                    if (p.getProductoId().equals(pre.getProductoId())) {
                        if (pre.getTipodespachoId() == 1) { //auto
                            nf.setValue(pre.getPrecio());
                            p.setPrecioAS(nf);
                        } else if (pre.getTipodespachoId() == 2) {
                            nf2.setValue(pre.getPrecio());
                            p.setPrecioSC(nf2);
                        }
                    }
                }
            }
        } else {
            startProducts = true;
        }

        //Si no hay turno previo
        if (startProducts) {
            for (final Producto p : productos) {
                final NumberField nfsc = new NumberField();
                //AUTOSERVICIO
                final NumberField nfas = new NumberField();
                nfas.setMinValue(0D);
                nfas.setValue(0D);
                if (user.getPaisLogin().getPaisId().equals(320)) {  //Guatemala
//TODO: Aca mandar a traer los valores que haya en fusion, siempre
                    nfas.setValue(21D);
                    nfas.setEnabled(false);
                }
                nfas.setRequired(true);
                nfas.setRequiredError("Este precio debe ser menor que el de servicio completo");
                nfas.setDecimalPrecision(2);

//                nfas.addValueChangeListener(new Property.ValueChangeListener() {
//                    @Override
//                    public void valueChange(Property.ValueChangeEvent event) {
//                        Double psc = Double.parseDouble(nfsc.getValue());
//                        Double pas = Double.parseDouble(nfas.getValue());
//                        if (psc - pas < 0) {
//                            nfas.addStyleName("v-textfield-error");
//                        } else {
//                            nfas.removeStyleName("v-textfield-error");
//                            nfsc.removeStyleName("v-textfield-error");
//                        }
//                    }
//                });
                p.setPrecioAS(nfas);

                //SERVICIO COMPLETO
//                final NumberField nfsc = new NumberField();
                nfsc.setMinValue(0D);
                nfsc.setValue(0D);
                if (user.getPaisLogin().getPaisId().equals(320)) {  //Guatemala
                    nfsc.setValue(22D);
                    nfsc.setEnabled(false);
                }
                nfsc.setRequired(true);
                nfsc.setRequiredError("Este precio debe ser mayor que el de autoservicio");
                nfsc.setDecimalPrecision(2);
//                nfsc.addValueChangeListener(new Property.ValueChangeListener() {
//                    @Override
//                    public void valueChange(Property.ValueChangeEvent event) {
//                        Double psc = Double.parseDouble(nfsc.getValue());
//                        Double pas = Double.parseDouble(nfas.getValue());
//                        if (psc - pas < 0) {
//                            nfsc.addStyleName("v-textfield-error");
//                        } else {
//                            nfsc.removeStyleName("v-textfield-error");
//                            nfas.removeStyleName("v-textfield-error");
//                        }
//                    }
//                });
                p.setPrecioSC(nfsc);
            }
        }

        dia = svcTurno.getDiaActivoByEstacionid(user.getEstacionLogin().getEstacionId());
        
        svcTurno.closeConnections();

        bcPrecios = new BeanContainer<Integer, Producto>(Producto.class);
        bcPrecios.setBeanIdProperty("productoId");
        bcPrecios.addAll(productos);

        SvcEstacionConf svcEstacionConf = new SvcEstacionConf();
        List<EstacionConfHead> configs = new ArrayList(); //svcEstacionConf.getConfiguracionHeadByEstacionid(user.getEstacionLogin().getEstacionId());
        svcEstacionConf.closeConnections();
        //determinar si la estacion tiene configuracion de autoservicio.
        for (EstacionConfHead ech : configs) {
            for (EstacionConf ecf : ech.getEstacionConf()) {
                if (ecf.getTipodespachoId() == 1) { //autoservicio
                    showAutoservicio = true;
                    break;
                }
            }
        }

        contConfigs = new ListContainer<EstacionConfHead>(EstacionConfHead.class, configs);
        
        

    }

    private void buildTablePrecio() {
        tablePrecio = new Table("Precios:");
        tablePrecio.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        tablePrecio.addStyleName(ValoTheme.TABLE_COMPACT);
        tablePrecio.setContainerDataSource(bcPrecios);
        Object[] viscols = new Object[]{"nombre", "precioSC"};
        String[] colheads = new String[]{"Producto", "Servicio completo"};
        if (showAutoservicio) {
            viscols = new Object[]{"nombre", "precioAS", "precioSC"};
            colheads = new String[]{"Producto", "Autoservicio", "Servicio completo"};
        }
        tablePrecio.setVisibleColumns(viscols);
        tablePrecio.setColumnHeaders(colheads);
        tablePrecio.setHeight("250px");
        Responsive.makeResponsive(tablePrecio);
    }

    private void buildActions() {

        tfTurnoRef = new TextField("Turno referencia");
        tfTurnoRef.setRequired(true);
        tfTurnoRef.setRequiredError("El código de turno es requerido");
        tfTurnoRef.setNullRepresentation("");
        tfTurnoRef.setMaxLength(6);
        
        fieldFecha = new DateField("Correspondiente a fecha:");
        fieldFecha.setRequired(true);
        fieldFecha.setRequiredError("La fecha de creacion/modificaciones es requerida");
        fieldFecha.setDateFormat("dd-MM-yyyy");
//        fieldFecha.setRangeStart(Date.from(Instant.now()));
        fieldFecha.setRangeEnd(Date.from(Instant.now()));
        fieldFecha.setLocale(new Locale("es", "ES"));
        fieldFecha.setLenient(true);
        if (dia.getEstacionId()!=null) {
            fieldFecha.setEnabled(false);   //habilitado
        }

        btnGuardar = new Button("Crear turno");
        btnGuardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnGuardar.setIcon(FontAwesome.SAVE);
        btnGuardar.setEnabled(turno.getEstadoId() == null
                || (turno.getEstadoId() != null && turno.getEstadoId().equals(2)));    //habilitado (cerrado)
//        btnGuardar.setEnabled(true);
        btnGuardar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if (user.getPaisLogin().getPaisId().equals(320) && !tfTurnoRef.isValid()) {
                    Notification.show("ERROR:", "Todos los campos son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }

                if (!user.getPaisLogin().getPaisId().equals(320) && !cbxConfiguracion.isValid()) {
                    Notification.show("ERROR:", "Todos los campos son requeridos.", Notification.Type.ERROR_MESSAGE);
                    return;
                }

                List<Integer> prodIds = (List<Integer>) tablePrecio.getItemIds();
                Producto prod;
                Double pas, psc;
                for (Integer pi : prodIds) {
                    prod = (Producto) ((BeanItem) tablePrecio.getItem(pi)).getBean();
                    //La validacion es para que funcione en el resto de paises.
                    pas = (prod.getPrecioAS() != null) ? Double.parseDouble(prod.getPrecioAS().getValue()) : 0D;
                    psc = Double.parseDouble(prod.getPrecioSC().getValue());
                    if (pas - psc >= 0D) {
                        Notification.show("ERROR:", "Por favor revise los valores de precios para el producto *" + prod.getNombre() + "*", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }

                SvcTurno svcTurno = new SvcTurno();
                Turno turno = new Turno(null, user.getEstacionLogin().getEstacionId(), user.getUsuarioId(), 1, user.getUsername(), fieldFecha.getValue(), user.getNombreLogin());
                if (user.getPaisLogin().getPaisId().equals(320)) {  //Guatemala
                    turno.setTurnoFusion(tfTurnoRef.getValue());
                } else {
                    turno.setEstacionconfheadId(((EstacionConfHead) cbxConfiguracion.getValue()).getEstacionconfheadId());
                }
                dia = new Dia(user.getEstacionLogin().getEstacionId(), fieldFecha.getValue(), 1, user.getUsername(), user.getNombreLogin());
                svcTurno.doActionDia(Dao.ACTION_ADD, dia);
                turno.setFecha(fieldFecha.getValue());
                turno = svcTurno.doActionTurno(Dao.ACTION_ADD, turno);
                svcTurno.closeConnections();

                if (turno.getTurnoId() != null) {
//                    prodIds = (List<Integer>) tablePrecio.getItemIds();
                    Precio precio;
                    SvcTurno svcPrecio = new SvcTurno();
                    int conExito = 0;
                    for (Integer pi : (List<Integer>) tablePrecio.getItemIds()) {
//                        BeanItem<DtolLectura> item = (BeanItem) tableLecturas.getItem(id);
                        prod = (Producto) ((BeanItem) tablePrecio.getItem(pi)).getBean();
                        if (user.getPaisLogin().getPaisId() == 320) {   //Guatemala
                            precio = new Precio(turno.getTurnoId(), pi, 1, Double.parseDouble(prod.getPrecioAS().getValue()), user.getUsername(), user.getNombreLogin());  //AutoServicio
                            precio = svcPrecio.doActionPrecio(Dao.ACTION_ADD, precio);
                            conExito = (precio != null) ? conExito + 1 : conExito;
                        }
                        precio = new Precio(turno.getTurnoId(), pi, 2, Double.parseDouble(prod.getPrecioSC().getValue()), user.getUsername(), user.getNombreLogin());  //Servicio completo
                        precio = svcPrecio.doActionPrecio(Dao.ACTION_ADD, precio);
                        conExito = (precio.getTurnoId() != null) ? conExito + 1 : conExito;
                    }
                    svcPrecio.closeConnections();
//                    if (conExito == productos.size() * 2) {
                    if (conExito > 0) {
//                        Notification.show("El registro de turno y precio se ha creado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                        Notification notif = new Notification("ÉXITO:", "El registro de turno y precio se ha creado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                        notif.setDelayMsec(3000);
                        notif.setPosition(Position.MIDDLE_CENTER);
                        //notif.setStyleName("mystyle");
                        //notif.setIcon(new ThemeResource("img/reindeer.png"));
                        notif.show(Page.getCurrent());
                        UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PR_TURN.getViewName());
                    } else {
                        Notification.show("ERROR:", "Ocurrió un error al guardar el precio.\n", Notification.Type.ERROR_MESSAGE);
                        turno = svcTurno.doActionTurno(Dao.ACTION_DELETE, turno);
                        return;
                    }
                } else {
                    Notification.show("ERROR:", "Ocurrió un error al guardar el turno.\n" + turno.getDescError(), Notification.Type.ERROR_MESSAGE);
                    return;
                }
            }
        });

        btnModificar = new Button("Modificar precio", FontAwesome.EDIT);
        btnModificar.setStyleName(ValoTheme.BUTTON_PRIMARY);
        btnModificar.setEnabled(turno.getTurnoId() != null && turno.getEstadoId() != 2);    //habilitado
        btnModificar.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                SvcTurno svcPrecio = new SvcTurno();
                Precio precio;
                Producto prod;
                int counter = 0;
                for (Integer pid : bcPrecios.getItemIds()) {
                    prod = bcPrecios.getItem(pid).getBean();
                    precio = new Precio(turno.getTurnoId(), prod.getProductoId(), 2, Double.parseDouble(prod.getPrecioSC().getValue()), null, null);  //ServicioCompleto
                    precio.setModificadoPor(user.getUsername());
                    precio.setModificadoPersona(user.getNombreLogin());
                    precio = svcPrecio.doActionPrecio(Dao.ACTION_UPDATE, precio);
                    counter += (precio.getTurnoId() != null) ? 1 : 0;
                    if (user.getPaisLogin().getPaisId() == 320) {   //Guatemala
                        precio = new Precio(turno.getTurnoId(), prod.getProductoId(), 1, Double.parseDouble(prod.getPrecioAS().getValue()), null, null);   //Autoservicio
                        precio.setModificadoPor(user.getUsername());
                        precio.setModificadoPersona(user.getNombreLogin());
                        precio = svcPrecio.doActionPrecio(Dao.ACTION_UPDATE, precio);
                        counter += (precio.getTurnoId() != null) ? 1 : 0;
                    }
                }
                svcPrecio.closeConnections();

                if (bcPrecios.getItemIds().size() == counter) {
//                        Notification.show("El registro de turno y precio se ha creado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    Notification notif = new Notification("ÉXITO:", "El registro de precio se ha actualizado con éxito.", Notification.Type.HUMANIZED_MESSAGE);
                    notif.setDelayMsec(3000);
                    notif.setPosition(Position.MIDDLE_CENTER);
                    //notif.setStyleName("mystyle");
                    //notif.setIcon(new ThemeResource("img/reindeer.png"));
                    notif.show(Page.getCurrent());
                    UI.getCurrent().getNavigator().navigateTo(DashboardViewType.PR_TURN.getViewName());
                } else {
                    Notification.show("ERROR:", "Ocurrió un error al actualizar el precio.\n", Notification.Type.ERROR_MESSAGE);
                    return;
                }
            }
        });

        cbxConfiguracion = new ComboBox("Configuración de bombas:", contConfigs);
        cbxConfiguracion.setItemCaptionPropertyId("nombre");
        cbxConfiguracion.setRequired(true);
        cbxConfiguracion.setRequiredError("Elija una configuración");
        cbxConfiguracion.setNullSelectionAllowed(false);
        if (contConfigs.getItemIds().size() == 1) {
            EstacionConfHead ech = (EstacionConfHead) ((ArrayList) contConfigs.getItemIds()).get(0);
            cbxConfiguracion.setValue(ech);
        }
        cbxConfiguracion.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
            }
        });

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
