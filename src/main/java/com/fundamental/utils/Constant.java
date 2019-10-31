package com.fundamental.utils;

import com.fundamental.model.dto.DtoGenericBean;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Henry Barrientos
 */
public class Constant {

    public static final String SEPARATOR_PARAM = "\\|\\|";
    public static final SimpleDateFormat SDF_yyyyMMdd = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat SDF_ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat SDF_HHmm = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat SDF_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final DecimalFormat decimal2D = new DecimalFormat("### ###,##0.00;-#");
    public static final DecimalFormat decFmt3 = new DecimalFormat("### ###,##0.000;-#");

    public static final List<DtoGenericBean> TIPOS_EFECTIVO = Arrays.asList(new DtoGenericBean(1, "Generico"), new DtoGenericBean(2, "Efectivo"));
//    public static final Integer MP_ID_CALIBRACION = 41;
    public static final int MP_CRI_VENTA_CREDITO = 5;
    public static final int MP_CRI_VENTA_PREPAGO = 6;
    public static final int MP_CRI_EMPLEADO_CXC = 7;
    public static final int MP_CRI_EFECTIVO_DOLARES = 8;
    public static final int MP_CRI_TARJETAS = 108;
    

    //... el resto de paises ...
    public static final int MP_CRI_VENTA_LUBS_UNO = 10;

    public static final int AUTOSERVICIO = 1;
    public static final int SERVICIO_COMPLETO = 2;
    public static final double MAX_VALUE_COUNTER_PUMP = 999999.00;

    public static final String ROL_LOGIN_CAJERO = "CAJERO";
    public static final String ROL_LOGIN_OTRO = "OTRO";
    public static final String ROL_LOGIN_SUPERVISOR = "SUPERVISOR";
    public static final String REGEX_EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String SESSION_CONNECTION = "sessionconnection";

    public static final Integer ROL_PERMISSION_SYSADMIN = 1;
    public static final Integer ROL_PERMISSION_ADMINISTRATIVO = 2;
    public static final Integer ROL_PERMISSION_SUPERVISOR = 3;
    public static final Integer ROL_PERMISSION_CAJERO = 4;
    public static final Integer ROL_PERMISSION_GERENTE = 5;
    public static final Integer ROL_PERMISSION_REPORTES = 6;
    public static final Integer ROL_PERMISSION_JEFEPAIS = 7;

    public static final Integer CAT_PROD_DIFERENCIA_POSITIVA = 13;

    public static final short CAT_PAIS_GUATEMALA = 320;
    public static Map<String, String> MAP_MIMETYPES_EXT;
    public static String FORMATONUMERO = "########0.00";
    public static DecimalFormat numberFact = new DecimalFormat(FORMATONUMERO);
    public static String FORMATONUMERO4DECIMALES = "########0.0000";
    public static DecimalFormat NUMBER4DECIMAL = new DecimalFormat(FORMATONUMERO4DECIMALES);
    

    public Constant() {
        MAP_MIMETYPES_EXT = new HashMap();
        MAP_MIMETYPES_EXT.put("application/pdf", ".pdf");
    }

    //Estilos para componentes como horizontallayout, verticalLayout, paneles etc.
    //Estilo para componente contenedor como el de login
    public static String styleLogin = "login-panel";

    //Estilo de titulos para el header
    public static String styleViewheader = "viewheader";
    public static String styleViewheader2 = "viewheader2";

    //Estilo de campos de ingreso para los formularios
    public static String styleFields = "fields";

    //Estilo Labels    
    public static String styleLabels = "labels";

    //Estilo para busquedas
    public static String styleTransactions = "transactions";

    //Estilo de toolbars
    public static String styleToolbar = "toolbar";

    //Estilo de pop ups
    public static String stylePopUps = "profile-window";
    public static String stylePopUpsB = "profile-windowB";
    public static String stylePopUpsC = "profile-windowC";

    //Estilo del formulario
    public static String styleForm = "profile-form";

    //Estilo de panel como el dashboard del demo para que tenga scroll
    public static String styleDashBoard = "dashboard-view";
    public static String sizeFull = "FULL";
    public static String sizeUndefined = "UNDEFINED";
    public static String sizeEmpty = "";

    public static DateFormat formatoFechaHora = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    public static DateFormat formatoFechaHoraCompleto = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy");
    public static DateFormat formatoFechaMes = new SimpleDateFormat("MMMM");
    public static DateFormat formatoFechaCompleto = new SimpleDateFormat("dd/MM/yyyy");
    public static DateFormat formatoFechaUnido = new SimpleDateFormat("yyyyMMdd");
    public static DateFormat formatoFechaAnioMes = new SimpleDateFormat("yyyyMM");
    public static DateFormat formatoFechaAnio = new SimpleDateFormat("yyyy");
    public static DateFormat formatoHora = new SimpleDateFormat("HH:mm");
    public static DateFormat formatoHoraTransaccion = new SimpleDateFormat("HH:mm:ss");
    public static DateFormat formatoHoraLong = new SimpleDateFormat("HHmmss");
    public static DecimalFormat formatoNumero = new DecimalFormat("#####");
    public static DecimalFormat formatoNumeroGrande = new DecimalFormat("############");
    public static DecimalFormat formatoNumeroCeros = new DecimalFormat("000000");
    public static DecimalFormat formatoLong = new DecimalFormat("###########");
    public static DecimalFormat formatoLong2 = new DecimalFormat("##,###,###,###");
    public static DecimalFormat formatoDinero = new DecimalFormat("############.##");
    public static DecimalFormat formatoDineroComas = new DecimalFormat("###,###,###,##0.00");
    public static String formatoHoraSegundos = "##:##:##";
    public static DecimalFormat formatoDouble = new DecimalFormat("###,###,###,##0.00");
    public static DecimalFormat formatoDouble3 = new DecimalFormat("###,###,###,##0.000");
    public static String esPadre = "P";
    public static String esHijo = "H";
    public static String prioridadPadre = "N";
    public static String prioridadHija = "S";

    public static String formatoFechaFields = "dd/MM/yy";
    public static String formatoFechaTrans = "dd/MM/yyyy";
    public static String formatoHoraFields = "HH:mm";

    public static String[] horas = {"00", "01",};
    public static Character n = new Character('N');
    public static Character s = new Character('S');
    public static long issuer = 1042;
    public static String firstYear = "2010";

}
