package com.sisintegrados.dao;

import com.fundamental.model.Acceso;
import com.fundamental.model.Arqueocaja;
import com.fundamental.model.Bomba;
import com.fundamental.model.BombaEstacion;
import com.fundamental.model.Dia;
import com.sisintegrados.generic.bean.Empleado;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.EstacionConf;
import com.fundamental.model.EstacionConfHead;
import com.fundamental.model.Lecturafinal;
import com.fundamental.model.Marca;
import com.fundamental.model.Mediopago;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Parametro;
import com.fundamental.model.Precio;
import com.fundamental.model.Producto;
import com.fundamental.model.Rol;
import com.fundamental.model.Turno;
import com.fundamental.model.dto.DtoArqueo;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Allan G.
 */
public interface Dao {

    public Connection getConnection();

    public void setOwnAutocommit(boolean autoCommit);

    public void closeConnections();

    public void closePst();

    /*
    * FUNCIONES COMUNES A TODOS LOS SERVICIOS *
     */
    public List<Rol> getRolesByUserid(Integer userId);

    public List<Bomba> getBombasByEstacionid(Integer estacionId);

    public List<DtoArqueo> getArqueo(String turnosIds, String bombasIds, String tipo);

    public List<Pais> getAllPaises();

    public HashMap<String, Pais> getAllPaisesMap();

    public List<Estacion> getStationsByCountry(Integer paisId);

    public Map<String, Estacion> getAllEstacionesMap();

    public List<Estacion> getStationsByCountry(Integer paisId, boolean onlyActive);

    /* Ultimo turno del dia, es util para lecturas y cuadre, se necesita el ultimo del dia*/
    public Turno getTurnoActivoByEstacionid(Integer estacionId);

    /* Ultimo turno registrado para la estacion, no importa si de ayer o hoy */
    public Turno getUltimoTurnoByEstacionid(Integer estacionId);

    public Turno getUltimoTurnoByEstacionid2(Integer estacionId, Date Fecha);

    public List<Bomba> getBombasByEstacionidTurnoid(Integer estacionId, Integer turnoId);

    public List<BombaEstacion> getBombaEstacionByEstacionid(Integer estacionId);

    public List<Producto> getCombustiblesByEstacionid(Integer estacionId);

    public List<Producto> getCombustiblesByEstacionid2(Integer estacionId);

    public List<Lecturafinal> getLecturasfinales(Integer estacionId, String tipoLectura);

    public Turno doActionTurno(String action, Turno turno);

    public List<Mediopago> getMediospagoByPaisidTipoid(Integer paisId, Integer tipoId);

    //SvcTurnoCierre
    public List<Arqueocaja> getArqueocajaByTurnoid(Integer turnoId);

    public List<Arqueocaja> getArqueocajaByTurnoidDia(Integer turnoId, Date fecha);

    public List<Turno> getTurnosByEstacionid(Integer estacionId);

    public List<Turno> getTurnosByEstacionidDiaNolectura(Integer estacionId, Date fecha);

    public List<Turno> getTurnosByEstacionidDia_lectura(Integer estacionId, Date fecha);

    public Dia doActionDia(String action, Dia dia);

    public Precio doActionPrecio(String action, Precio precio);

    public Dia getDiaActivoByEstacionid(Integer estacionId);

    /*Esta funcion es basicamente para obtener el estado de un dia seleccionado en un control de fecha.*/
    public Dia getDiaByEstacionidFecha(Integer estacionId, Date fecha);

    public Dia getUltimoDiaByEstacionid(Integer estacionId);

    public List<Acceso> getAccesosByUsuarioid(Integer usuarioId, boolean isSysadmin);

    public List<Estacion> getAllEstaciones(boolean includeInactive);

    public List<Producto> getProductosByEstacionid(Integer estacionId);

    public List<EstacionConfHead> getConfiguracionHeadByEstacionid(Integer estacionId, boolean addInactive);

    public List<EstacionConfHead> getAllConfiguracionHead(boolean addInactive);

    public List<EstacionConf> getConfiguracionByEstconfhead(Integer estconfhead);

    public Parametro getParameterByName(String name);

    public List<Empleado> getEmpleadosByTurnoid(int turnoId);

    public List<Empleado> getEmpleadosByTurnoid2(int turnoId);

    public List<Bomba> getAllBombas(boolean includeInactive);

    public List<EstacionConfHead> getConfiguracionHeadByEstacionidHorario(Integer estacionId, int horarioId);

    public List<Producto> getAllProductosByTypeMarca(int type, boolean includeInactive, Integer brandId);

    public List<Producto> getAllProductosByCountryTypeBrand(Integer countryId, int type, Integer brandId, boolean includeInactive);

    public List<Marca> getAllBrands(boolean includeInactive);

    public List<Producto> getAllProducts();

    public List<Estacion> getStationsByCountryUser(Integer paisId, Integer userId);

    public List<EstacionConfHead> getAllConfiguracionheadPais();

    public String[] getUniqueStation(int userId);

    public List<Acceso> getParents();

    public Acceso getAccess(String screen);

}
