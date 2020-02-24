/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.view.form;

import com.fundamental.model.Cliente;
import com.fundamental.model.Lubricanteprecio;
import com.fundamental.model.Marca;
import com.fundamental.model.Producto;
import com.sisintegrados.dao.Dao;
import com.fundamental.services.SvcGeneral;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.jain.addon.resource.DefaultI18NResourceProvider;
import com.jain.addon.resource.I18NProvider;
import com.sisintegrados.daoimp.DaoImp;
import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.GenericClientes;
import com.sisintegrados.generic.bean.GenericLubricantePrecio;
import com.sisintegrados.generic.bean.Pais;
import com.sisintegrados.generic.bean.Usuario;
import com.vaadin.addon.excel.ExcelUploader;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.demo.dashboard.event.DashboardEvent;
import com.vaadin.demo.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjosu
 */
public class FormUploadClientes extends Window {

    I18NProvider provider = DefaultI18NResourceProvider.instance();
//    CreateComponents components = new CreateComponents();
//    Constantes cons = new Constantes();
//    Dao dao = new DaoImpl();
    Button guardar = new Button();
    Button cancelar = new Button();
    //DaoPYW daoPYW = new DaoPYWImpl();
    Grid grid;
    String codigocuenta;
    String tipoBitacora = "precios-lubricantes";
    int newStatus = 0;
    String newStatusDesc;
    final ExcelUploader<GenericClientes> excelUploader = new ExcelUploader<>(GenericClientes.class);
    final Upload upload = new Upload();
    VerticalLayout v = new VerticalLayout();
    BeanItemContainer<GenericClientes> container;
    List<Lubricanteprecio> lstAsignarTarjetas = new ArrayList<>();
    List<Pais> lstPais = new ArrayList<>();
    List<Producto> lstProducto = new ArrayList<>();
    //List<GenericTarjetasCentroCosto> lstCentroCosto = new ArrayList<>();
    Usuario usuario = UI.getCurrent().getSession().getAttribute(Usuario.class);
    Date fechaInicio = new Date();
    Date fechaFin;
    Date fechaFinN;
    CreateComponents components = new CreateComponents();
    Constant cons = new Constant();
    List<Lubricanteprecio> listProducts = new ArrayList();
    List<Producto> listProductsNoPrecio = new ArrayList();
    SvcGeneral service = new SvcGeneral();
    List<Pais> listCountries;
    List<Marca> listBrands = new ArrayList();

    public FormUploadClientes() {
        setLocale(VaadinSession.getCurrent().getAttribute(Locale.class));
        addStyleName(Constant.stylePopUps);
        Responsive.makeResponsive(this);
        setModal(true);
        addCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(80.0f, Sizeable.Unit.PERCENTAGE);

        setModal(true);
        addCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(80.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
//        content.setMargin(new MarginInfo(true, false, false, false));
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildFields());
        content.addComponent(buildButtons());
        listCountries = service.getAllPaises();
        listBrands = service.getAllBrands(false);

    }

    private Component buildButtons() {
        guardar = new Button("Guardar", FontAwesome.SAVE);
        guardar.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        guardar.addStyleName(ValoTheme.BUTTON_SMALL);
        guardar.setDisableOnClick(true);
        guardar.addClickListener((Button.ClickListener) event -> {
            if (container != null) {

                if (validarData()) {
                    cancelar.setEnabled(false);
                    Map<String,Cliente> result = service.getCustomersByStationidTypeMap(null);
                    Map<String,Pais> countries = service.getAllPaisesMap();
                    Map<String,Estacion> stations = service.getAllEstacionesMap();
                    
                    Estacion estacion = null;
                    Cliente c = null;
                    Pais p = null;
                    int u = 0;
                    int i = 0;
                    for(GenericClientes t: container.getItemIds()){
                        estacion = stations.get(t.getEstacion());
                        p = countries.get(t.getPais());
                        if(estacion!=null && p!=null){
                            c = result.get(p.getPaisId()+""+t.getCodigo_e1());
                            try{
                                String codigoE1 = new Double(t.getCodigo_e1()).intValue()+"";
                                String codigoEn = new Double(t.getCodigoEnvoy()).intValue()+"";
                                if(c!=null){                                     
                                    if((!c.getEstado().equals(t.getEstado().trim())  || !c.getNombre().equals(t.getNombre().trim()) 
                                            || !c.getTipo().equals(t.getTipo().trim())
                                            || !c.getCodigoEnvoy().equals(codigoEn)) && c.getCodigo().endsWith(codigoE1)
                                            ){
                                        c.setEstado(t.getEstado().equals("Activo")?"A":"I");
                                        c.setNombre(t.getNombre());
                                        c.setTipo(t.getTipo().equals("Prepago")?"P":"C");
                                        c.setCodigoEnvoy(t.getCodigoEnvoy());
                                        if(service.doActionCustomer(DaoImp.ACTION_UPDATE, c)){
                                            u = u + 1 ;                         
                                        }   
                                    }
                                }else{
                                   
                                    c = new Cliente(0, codigoE1, t.getNombre(),estacion.getEstacionId(),
                                            t.getEstado().equals("Activo")?"A":"I",
                                            usuario.getUsername(), new java.util.Date(),  t.getTipo().equals("Prepago")?"P":"C", codigoEn, "");
                                    if(service.doActionCustomer(DaoImp.ACTION_ADD,c)){
                                        i = i+ 1;
                                    }
                                }
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                        
                    }    
                    components.crearNotificacion("Se actualizaron "+u+"  y se Insertaron "+i+" registros ");
                    DashboardEventBus.post(new DashboardEvent.ProfileUpdatedEvent());
                    close();
                } else {
                    Notification.show("Error", "Validar Datos", Notification.Type.ERROR_MESSAGE);                    
                }
            } else {
                Notification.show("Error", "Debe cargar un archivo", Notification.Type.ERROR_MESSAGE);
            }
            guardar.setEnabled(true);                    
            cancelar.setEnabled(true);
        });
        guardar.focus();

        cancelar = new Button("Cancelar");
        cancelar.addStyleName(ValoTheme.BUTTON_PRIMARY);
        cancelar.addStyleName(ValoTheme.BUTTON_SMALL);
        cancelar.addClickListener((Button.ClickListener) event -> {
            close();
        });

        HorizontalLayout footer = (HorizontalLayout) components.createHorizontal(ValoTheme.WINDOW_BOTTOM_TOOLBAR, "", true, false, false, new Component[]{guardar, cancelar});
        footer.setComponentAlignment(guardar, Alignment.TOP_RIGHT);
        if (!guardar.isVisible()) {
            footer.setComponentAlignment(cancelar, Alignment.TOP_CENTER);
        }
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        return footer;
    }

    private boolean validarData() {
        boolean val = true;
        for (GenericClientes genCard : container.getItemIds()) {
            if ((genCard.getPais().length() < 0) || (genCard.getEstacion().length() < 0) || (genCard.getTipo().length() < 0) || (genCard.getCodigo_e1().length() < 0) 
                    || (genCard.getCodigoEnvoy().length() < 0) || (genCard.getNombre().length() < 0)) {
                val = false;
            }

        }
        return val;
    }

    private Component buildFields() {
        HorizontalLayout regresa = (HorizontalLayout) components.createHorizontal(Constant.styleForm, "100%", true, true, false, null);
        regresa.setCaption("Cargar Clientes");
        regresa.setIcon(FontAwesome.FLAG);
        FormLayout form = new FormLayout();
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        regresa.addComponent(form);
        regresa.setExpandRatio(form, 1);

        HorizontalLayout hlDoc1 = (HorizontalLayout) components.createHorizontal(Constant.styleForm, "100%", false, false, false, null);
        hlDoc1.setHeight("55px");
        excelUploader.setFirstRow(1);
        excelUploader.addSucceededListener((event, items) -> {
            System.out.println("items "+(GenericClientes) items.get(0));
            if (items.size() > 0) {
                if (v.getComponentCount() > 1) {
                    v.removeComponent(grid);
                }
                container = new BeanItemContainer<GenericClientes>(GenericClientes.class);
                container.addAll(items);
                grid = new Grid(container);
                grid.setColumnOrder("pais", "estacion", "tipo", "codigo_e1", "codigoEnvoy", "estado", "nombre");
                grid.setWidth("100%");
                grid.addStyleName(ValoTheme.TABLE_BORDERLESS);
                grid.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
                grid.addStyleName(ValoTheme.TABLE_SMALL);
                grid.addStyleName("smallgrid");
                v.addComponent(grid);
            }
        });

        v.setSpacing(true);
        v.addComponent(upload);
        form.addComponent(v);
        upload.setButtonCaption("Cargar");
        upload.setReceiver(excelUploader);
        upload.addSucceededListener(excelUploader);

        return regresa;
    }

}
