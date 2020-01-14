package com.vaadin.demo.dashboard.view;

import com.fundamental.view.MntEstacion;
import com.fundamental.view.MntMediosPago;
import com.fundamental.view.MntUser;
import com.fundamental.view.PrChangeLastRead;
import com.fundamental.view.PrCierreDia;
import com.fundamental.view.PrCuadre;
import com.fundamental.view.PrReading;
import com.fundamental.view.PrTurnoCierre;
import com.fundamental.view.RptMediopago;
import com.fundamental.view.MntCambio;
import com.fundamental.view.MntConfBombaEstacion;
import com.fundamental.view.MntCustCredito;
import com.fundamental.view.MntCustPrepago;
import com.fundamental.view.MntHorario;
import com.fundamental.view.MntInventarioFisico;
import com.fundamental.view.MntLubricanteCV;
import com.fundamental.view.MntServiceMode;
import com.fundamental.view.RptMTD2;
import com.fundamental.view.RptControDeMediosDePago;
import com.fundamental.view.RptVolumenes;
import com.fundamental.view.RptWSM;
import com.fundamental.view.RptWetStock;
import com.fundamental.view.TurnoPr;
import com.fundamental.view.maintenance.CambioClave;
import com.fundamental.view.maintenance.MntEmpleado;
import com.fundamental.view.maintenance.MntLubricantPrice;
import com.fundamental.view.maintenance.MntProducto;
import com.fundamental.view.maintenance.MntRol;
import com.fundamental.view.maintenance.MntTanque;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum DashboardViewType {
    
    HOME("Home", Home.class, FontAwesome.HOME, true),
    CAMBIO_CLAVE("CAMBIO_CLAVE", CambioClave.class, FontAwesome.LOCK, true),
    //    SALES("sales", SalesView.class, FontAwesome.BAR_CHART_O, false), 
    //    TRANSACTIONS("transactions", TransactionsView.class, FontAwesome.TABLE, false), 
    //    REPORTS("reports", ReportsView.class, FontAwesome.FILE_TEXT_O, true), 
    //    SCHEDULE("schedule", ScheduleView.class, FontAwesome.CALENDAR_O, false)
//    PR_TURN("PR_TURN", PrTurn.class, FontAwesome.TABLE, false),
    PR_TURN("PR_TURN", TurnoPr.class, FontAwesome.TABLE, false),
    PR_READING("PR_READING", PrReading.class, FontAwesome.TABLE, false), 
    PR_SQUAREUP("PR_SQUAREUP", PrCuadre.class, FontAwesome.MONEY, false), 
    PR_TURN_CLOSE("PR_TURN_CLOSE", PrTurnoCierre.class, FontAwesome.CLOSE, false), 
    PR_DAY_CLOSE("PR_DAY_CLOSE", PrCierreDia.class, FontAwesome.CLOSE, false), 
    PR_CHANGELASTREAD("PR_CHANGELASTREAD", PrChangeLastRead.class, FontAwesome.GAVEL, false), 
    
    MNT_ESTACION("MNT_ESTACION", MntEstacion.class, FontAwesome.HOUZZ, false), 
    MNT_MEDIOSPAGO("MNT_MEDIOSPAGO", MntMediosPago.class, FontAwesome.CREDIT_CARD, false), 
    MNT_USER("MNT_USER", MntUser.class, FontAwesome.USER_MD, false),
    MNT_CAMBIO("MNT_CAMBIO", MntCambio.class, FontAwesome.EXCHANGE, false),
    MNT_CONF_BOMBA_ESTACION("MNT_CONF_BOMBA_ESTACION", MntConfBombaEstacion.class, FontAwesome.COGS, false),
    MNT_CUST_CREDITO("MNT_CUST_CREDITO", MntCustCredito.class, FontAwesome.USER, false),
    MNT_CUST_PREPAGO("MNT_CUST_PREPAGO", MntCustPrepago.class, FontAwesome.USER, false),
    MNT_PRICE_LUBS("MNT_PRICE_LUBS", MntLubricantPrice.class, FontAwesome.BUS, false),
    MNT_LUBS_CV("MNT_LUBS_CV", MntLubricanteCV.class, FontAwesome.BUS, false),
    MNT_INV_FIS("MNT_INV_FIS", MntInventarioFisico.class, FontAwesome.BUS, false),
    MNT_SCHEDULE("MNT_SCHEDULE", MntHorario.class, FontAwesome.CLOCK_O, false),
    MNT_MODOSERV("MNT_MODOSERV", MntServiceMode.class, FontAwesome.MODX, false),
    MNT_ROL("MNT_ROL", MntRol.class, FontAwesome.COGS, false),
    MNT_PRODUCTO("MNT_PRODUCTO", MntProducto.class, FontAwesome.COGS, false),
    MNT_EMPLEADO("MNT_EMPLEADO", MntEmpleado.class, FontAwesome.USER, false),
    MNT_TANQUE("MNT_TANQUE", MntTanque.class, FontAwesome.CLOUD, false),
    
    RPT_MEDIOPAGO("RPT_MEDIOPAGO", RptMediopago.class, FontAwesome.FILE, false),
    RPT_CONTROLMEDIOPAGO("RPT_CONTROLMEDIOPAGO", RptControDeMediosDePago.class, FontAwesome.FILE, false),
    RPT_VOLUMEN("RPT_VOLUMEN", RptVolumenes.class, FontAwesome.FILE, false),
//    RPT_MTD("RPT_MTD", RptMTD.class, FontAwesome.FILE, false),
    RPT_MTD("RPT_MTD", RptMTD2.class, FontAwesome.FILE, false),
    RPT_WET_STOCK("RPT_WET_STOCK", RptWetStock.class, FontAwesome.FILE, false),
    RPT_WSM("RPT_WSM", RptWSM.class, FontAwesome.FILE, false)
    ;

    private final String viewName;
    private String viewTittle;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    private DashboardViewType(final String viewName,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public String getViewTittle() {
        return viewTittle;
    }

    public void setViewTittle(String viewTittle) {
        this.viewTittle = viewTittle;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static DashboardViewType getByViewName(final String viewName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
