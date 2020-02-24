/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.view.form;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jjosu
 */
public class FormUploadTarjetas extends Window {

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
    final ExcelUploader<GenericLubricantePrecio> excelUploader = new ExcelUploader<>(GenericLubricantePrecio.class);
    final Upload upload = new Upload();
    VerticalLayout v = new VerticalLayout();
    BeanItemContainer<GenericLubricantePrecio> container;
    List<Lubricanteprecio> lstAsignarTarjetas = new ArrayList<>();
    List<Pais> lstPais = new ArrayList<>();
    List<Producto> lstProducto = new ArrayList<>();
    //List<GenericTarjetasCentroCosto> lstCentroCosto = new ArrayList<>();
    Usuario usuario = UI.getCurrent().getSession().getAttribute(Usuario.class);
    Date fechaInicio = new Date();
    Date fechaFin;
    Date fechaFinN;
    private final Pais pais;
    private final Marca marca;
    CreateComponents components = new CreateComponents();
    Constant cons = new Constant();
    List<Lubricanteprecio> listProducts = new ArrayList();
    List<Producto> listProductsNoPrecio = new ArrayList();
    SvcGeneral service = new SvcGeneral();
    List<Pais> listCountries;
    List<Marca> listBrands = new ArrayList();

    public FormUploadTarjetas(Pais pais, Marca marca) {
        this.pais = pais;
        this.marca = marca;
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
        guardar.addClickListener((Button.ClickListener) event -> {
            if (container != null) {

                if (validarData()) {

                    listProducts = service.getLubpriceByCountryidStationid(pais.getPaisId(), marca.getIdMarca());
                    listProductsNoPrecio = service.getProductsNotInLubsPrecio();
   
                    for (GenericLubricantePrecio genCard : container.getItemIds()) {
              
                        for (Lubricanteprecio pr : listProducts) {
                            if (genCard.getPRODUCTO().trim().toUpperCase().equals(pr.getProducto().getNombre().trim().toUpperCase())
                                    && genCard.getPAIS().equals(pr.getPais().getNombre())) //&& genCard.getMARCA().equals(marca.getNombre())) 
                            {
                                /*
                            Si existe el producto en sistema, se procede a actualizar
                                 */
                                Lubricanteprecio lubPrecio = new Lubricanteprecio();
                                lubPrecio.setLubricanteprecio(pr.getLubricanteprecio());
                                lubPrecio.setPaisId(pais.getPaisId());
                                lubPrecio.setProductoId(pr.getProductoId());
                                Date date1 = null;
                                Date date2 = null;
                                try {
                                    date1 = new SimpleDateFormat("mm/dd/yyyy").parse(genCard.getINICIO());
                                    date2 = new SimpleDateFormat("mm/dd/yyyy").parse(genCard.getFIN());
                                } catch (ParseException ex) {
                                    Logger.getLogger(FormUploadTarjetas.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                lubPrecio.setFechaInicio(date1);
                                lubPrecio.setFechaFin(date2);
                                lubPrecio.setPrecio(Double.valueOf(genCard.getPRECIO()));
                                lubPrecio.setModificadoPor(usuario.getNombre());
                                pr.setModificadoPor(usuario.getNombre());
                                System.out.println("fila a enviar update " + lubPrecio.toString());
                                Lubricanteprecio p = service.doActionLubprecioCarga(DaoImp.ACTION_UPDATE, lubPrecio, pr);
                                System.out.println("p " + p.toString());

                            }

                        }

                        for (Producto prod : listProductsNoPrecio) {
                            if (genCard.getPRODUCTO().trim().toUpperCase().equals(prod.getNombre().trim().toUpperCase())) {
                                {
                                    /*
                            No existe el producto en sistema, se procede a crear
                                     */
                                    Lubricanteprecio lubPrecio = new Lubricanteprecio();
                                    //lubPrecio.setLubricanteprecio(Integer.valueOf(genCard.getPRECIO()));
                                    lubPrecio.setPaisId(pais.getPaisId());
                                    lubPrecio.setProductoId(prod.getProductoId());
                                    Date date1 = null;
                                    Date date2 = null;
                                    try {
                                        date1 = new SimpleDateFormat("mm/dd/yyyy").parse(genCard.getINICIO());
                                        date2 = new SimpleDateFormat("mm/dd/yyyy").parse(genCard.getFIN());
                                        System.out.println("date1 " + date1);
                                        System.out.println("date2 " + date2);
                                    } catch (ParseException ex) {
                                        Logger.getLogger(FormUploadTarjetas.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    lubPrecio.setFechaInicio(date1);
                                    lubPrecio.setFechaFin(date2);
                                    lubPrecio.setPrecio(Double.valueOf(genCard.getPRECIO()));
                                    //lubPrecio.setModificadoPor(usuario.getNombre());
                                    lubPrecio.setCreadoPor(usuario.getNombre());
                                    System.out.println("fila a enviar create " + lubPrecio.toString());
                                    Lubricanteprecio p = service.doActionLubprecioCarga(DaoImp.ACTION_ADD, lubPrecio, lubPrecio);
                                    System.out.println("p " + p.toString());

                                }

                            }
                        }
                    }
                    components.crearNotificacion("Carga procesada exitosamente");
                    DashboardEventBus.post(new DashboardEvent.ProfileUpdatedEvent());
                    close();
                } else {
                    Notification.show("Error", "Validar Datos", Notification.Type.ERROR_MESSAGE);
                }

            } else {
                Notification.show("Error", "Debe cargar un archivo", Notification.Type.ERROR_MESSAGE);
            }
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
        for (GenericLubricantePrecio genCard : container.getItemIds()) {
            if ((genCard.getPAIS().length() < 0) || (genCard.getPAIS().length() < 0) || (genCard.getPRODUCTO().length() < 0) || (genCard.getINICIO().length() < 0) || (genCard.getFIN().length() < 0) || (genCard.getPRECIO().length() < 0)) {
                val = false;
            }

        }
        return val;
    }

    private Component buildFields() {
        HorizontalLayout regresa = (HorizontalLayout) components.createHorizontal(Constant.styleForm, "100%", true, true, false, null);
        regresa.setCaption("Cargar Precios Lubricantes");
        regresa.setIcon(FontAwesome.FLAG);
        FormLayout form = new FormLayout();
        form.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        regresa.addComponent(form);
        regresa.setExpandRatio(form, 1);

        HorizontalLayout hlDoc1 = (HorizontalLayout) components.createHorizontal(Constant.styleForm, "100%", false, false, false, null);
        hlDoc1.setHeight("55px");
        excelUploader.setFirstRow(1);
        excelUploader.addSucceededListener((event, items) -> {
            if (items.size() > 0) {
                if (v.getComponentCount() > 1) {
                    v.removeComponent(grid);
                }
                container = new BeanItemContainer<GenericLubricantePrecio>(GenericLubricantePrecio.class);
                container.addAll(items);
                grid = new Grid(container);
                grid.setColumnOrder("PAIS", "MARCA", "PRODUCTO", "INICIO", "FIN", "PRECIO", "HISTORIAL");
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
