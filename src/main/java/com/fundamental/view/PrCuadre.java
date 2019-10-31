package com.fundamental.view;

import com.fundamental.model.Arqueocaja;
import com.fundamental.model.ArqueocajaBomba;
import com.fundamental.model.ArqueocajaDetalle;
import com.fundamental.model.ArqueocajaProducto;
import com.fundamental.model.Bomba;
import com.fundamental.model.Cliente;
import com.fundamental.model.Dia;
import com.fundamental.services.Dao;
import com.fundamental.model.Efectivo;
import com.sisintegrados.generic.bean.Empleado;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.FactelectronicaPos;
import com.fundamental.model.Lubricanteprecio;
import com.fundamental.model.Mediopago;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Parametro;
import com.fundamental.model.Producto;
import com.fundamental.model.TasaCambio;
import com.fundamental.model.Turno;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.Utils;
import com.fundamental.model.dto.DtoArqueo;
import com.fundamental.model.dto.DtoEfectivo;
import com.fundamental.model.dto.DtoProducto;
import com.fundamental.services.SvcArqueo;
import com.fundamental.services.SvcClientePrepago;
import com.fundamental.services.SvcCuadre;
import com.fundamental.services.SvcTarjetaCredito;
import com.fundamental.services.SvcDetalleLubricantes;
import com.fundamental.services.SvcTurno;
import com.fundamental.services.SvcTurnoCierre;
import com.fundamental.utils.Constant;
import com.fundamental.utils.CreateComponents;
import com.fundamental.utils.Mail;
import com.fundamental.utils.Util;
import com.sisintegrados.generic.bean.GenericTarjeta;
import com.sisintegrados.view.form.FormClientesCredito;
import com.sisintegrados.view.form.FormTarjetasCredito;
import com.sisintegrados.view.form.FormDetalleVenta;
import com.sisintegrados.view.form.FormDetalleVenta2;
import com.sisintegrados.view.form.FormClientePrepago;
import com.sisintegrados.view.form.FormDetalleLubricantes;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
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
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.vaadin.maddon.ListContainer;
import org.vaadin.ui.NumberField;

/**
 * @author Mery Gil
 */
public class PrCuadre extends Panel implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  
}
