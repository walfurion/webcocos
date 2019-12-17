package com.fundamental.services;

import com.fundamental.model.Cliente;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.EstacionConfHead;
import com.fundamental.model.Horario;
import com.fundamental.model.Lubricanteprecio;
import com.fundamental.model.Marca;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Producto;
import com.fundamental.model.dto.DtoGenericBean;
import com.fundamental.utils.Constant;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Henry Barrientos
 */
public class SvcGeneral extends Dao {

    String query, tmpString;

    public SvcGeneral() {
    }

    public List<Cliente> getCustomersByStationidType(//Integer estacionId, 
            String type) {
        List<Cliente> result = new ArrayList();
        try {
            query = "SELECT c.cliente_id, c.codigo, c.nombre, c.estacion_id,  decode(c.estado,'A','Activo','Inactivo'), c.creado_por, c.creado_el, decode(c.tipo,'P','Prepago','Crédito'), c.codigo_envoy, c.cedula_juridica, e.nombre, p.pais_id, p.nombre "
                    + "FROM cliente c, estacion e, pais p "
                    + "WHERE c.estacion_id = e.estacion_id AND e.pais_id = p.pais_id "; //c.estacion_id = "+estacionId+" AND "
                    if(type!=null && "".equals(type.trim())){
                        query = query + " AND c.tipo = '" + type + "'";
                    }
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Cliente cliente;
            while (rst.next()) {
                cliente = new Cliente(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5), rst.getString(6), null, rst.getString(8), rst.getString(9), rst.getString(10));
                cliente.setEstacionNombre(rst.getString(11));
                cliente.setPaisId(rst.getInt(12));
                cliente.setPaisNombre(rst.getString(13));
                result.add(cliente);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }
    
    public Map<String,Cliente> getCustomersByStationidTypeMap(//Integer estacionId, 
            String type) {
        Map<String,Cliente> result = new HashMap<String,Cliente>();
        try {
            query = "SELECT c.cliente_id, c.codigo, c.nombre, c.estacion_id,  decode(c.estado,'A','Activo','Inactivo'), c.creado_por, c.creado_el, decode(c.tipo,'P','Prepago','Crédito'), c.codigo_envoy, c.cedula_juridica, e.nombre, p.pais_id, p.nombre "
                    + "FROM cliente c, estacion e, pais p "
                    + "WHERE c.estacion_id = e.estacion_id AND e.pais_id = p.pais_id "; //c.estacion_id = "+estacionId+" AND "
                    if(type!=null && "".equals(type.trim())){
                        query = query + " AND c.tipo = '" + type + "'";
                    }
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Cliente cliente;
            while (rst.next()) {
                cliente = new Cliente(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5), rst.getString(6), null, rst.getString(8), rst.getString(9), rst.getString(10));
                cliente.setEstacionNombre(rst.getString(11));
                cliente.setPaisId(rst.getInt(12));
                cliente.setPaisNombre(rst.getString(13));
                result.put(cliente.getPaisId()+""+cliente.getCodigo(),cliente);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public boolean doActionCustomer(String action, Cliente cliente) {
        boolean result = false;
        try {
            if (action.equals(Dao.ACTION_ADD)) {
                query = "INSERT INTO cliente (cliente_id, codigo, nombre, estacion_id, codigo_envoy, cedula_juridica, creado_por, tipo) "
                        + "VALUES (cliente_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setString(1, cliente.getCodigo());
                pst.setString(2, cliente.getNombre());
                pst.setInt(3, cliente.getEstacionId());
                pst.setString(4, cliente.getCodigoEnvoy());
                pst.setString(5, cliente.getCedulaJuridica());
                pst.setString(6, cliente.getCreadoPor());
                pst.setString(7, cliente.getTipo());
                pst.executeUpdate();
                result = true;
            } else if (action.equals(Dao.ACTION_UPDATE)) {
                query = "UPDATE cliente "
                        + "SET codigo = ?, nombre = ?, estacion_id = ?, modificado_por = ?, codigo_envoy = ?, cedula_juridica = ?, modificado_el = SYSDATE , estado = ? "
                        + " WHERE cliente_id = ? ";
                pst = getConnection().prepareStatement(query);
                pst.setString(1, cliente.getCodigo());
                pst.setString(2, cliente.getNombre());
                pst.setInt(3, cliente.getEstacionId());
                pst.setString(4, cliente.getCreadoPor());
                pst.setString(5, cliente.getCodigoEnvoy());
                pst.setString(6, cliente.getCedulaJuridica());
                System.out.println("estado "+cliente.getEstado());
                pst.setString(7, cliente.getEstado());                
                pst.setInt(8, cliente.getClienteId());
                pst.executeUpdate();
                result = true;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public List<Lubricanteprecio> getLubpriceByCountryidStationid(int countryId, int brandId) {
        List<Lubricanteprecio> result = new ArrayList();
        try {
            query = "SELECT l.lubricanteprecio, l.pais_id, 0 eestacion_id, l.producto_id, l.fecha_inicio, "
                    + "l.fecha_fin, l.precio, l.creado_por, c.nombre, '' enombre, "
                    + "p.nombre, m.id_marca, m.nombre, m.estado, c.codigo, "
                    + "p.codigo "
                    + "FROM lubricanteprecio l, pais c, producto p, marca m "
                    + "WHERE l.pais_id = c.pais_id AND p.id_marca = m.id_marca "
                    + "AND l.producto_id = p.producto_id AND l.pais_id = ? "
                    + "AND p"
                    + ".id_marca = ?";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, countryId);
            pst.setInt(2, brandId);
            ResultSet rst = pst.executeQuery();
            Lubricanteprecio lpo;
            while (rst.next()) {
                lpo = new Lubricanteprecio(rst.getInt(1), rst.getInt(2), rst.getInt(4), new java.util.Date(rst.getDate(5).getTime()), new java.util.Date(rst.getDate(6).getTime()), rst.getDouble(7), rst.getString(8));
                lpo.setPaisNombre(rst.getString(9));
                lpo.setEstacionNombre(rst.getString(10));
                lpo.setProductoNombre(rst.getString(11));
                lpo.setMarca(new Marca(rst.getInt(12), rst.getString(13), rst.getString(14)));
                lpo.setMarcaNombre(rst.getString(13));
                lpo.setPais(new Pais(rst.getInt(2), rst.getString(9), rst.getString(15), null, null, null, null));
                lpo.setProducto(new Producto(rst.getInt(4), rst.getString(11), rst.getString(16), null, null, null));
                result.add(lpo);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public List<Lubricanteprecio> getLubpriceByProduct(int countryId, int brandId, int productId) {
        List<Lubricanteprecio> result = new ArrayList();
        try {
            query = "SELECT l.lubricanteprecio, l.pais_id, 0 eestacion_id, l.producto_id, l.fecha_inicio, "
                    + "l.fecha_fin, l.precio, l.creado_por, c.nombre, '' enombre, "
                    + "p.nombre, m.id_marca, m.nombre, m.estado, c.codigo, "
                    + "p.codigo "
                    + "FROM lubricanteprecio l, pais c, producto p, marca m "
                    + "WHERE l.pais_id = c.pais_id AND p.id_marca = m.id_marca "
                    + "AND l.producto_id = p.producto_id AND l.pais_id = ? "
                    + "AND p.id_marca = ? and l.producto_id= ? ";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, countryId);
            pst.setInt(2, brandId);
            pst.setInt(3, productId);
            ResultSet rst = pst.executeQuery();
            Lubricanteprecio lpo;
            while (rst.next()) {
                lpo = new Lubricanteprecio(rst.getInt(1), rst.getInt(2), rst.getInt(4), new java.util.Date(rst.getDate(5).getTime()), new java.util.Date(rst.getDate(6).getTime()), rst.getDouble(7), rst.getString(8));
                lpo.setPaisNombre(rst.getString(9));
                lpo.setEstacionNombre(rst.getString(10));
                lpo.setProductoNombre(rst.getString(11));
                lpo.setMarca(new Marca(rst.getInt(12), rst.getString(13), rst.getString(14)));
                lpo.setMarcaNombre(rst.getString(13));
                lpo.setPais(new Pais(rst.getInt(2), rst.getString(9), rst.getString(15), null, null, null, null));
                lpo.setProducto(new Producto(rst.getInt(4), rst.getString(11), rst.getString(16), null, null, null));
                result.add(lpo);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public List<Horario> getHorarios(boolean includeInactive) {
        List<Horario> result = new ArrayList();
        try {
            query = (includeInactive) ? "" : " WHERE h.estado = 'A' ";
            query = "SELECT h.horario_id, h.nombre, h.hora_inicio, h.hora_fin, h.estado, h.descripcion "
                    + "FROM horario h "
                    + query
                    + " ORDER BY h.horario_id";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Horario horario;
            String schedules = "";
            while (rst.next()) {
                horario = new Horario(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6));
                tmpString = (rst.getString(5).equals("A")) ? "Activo" : "Inactivo";
                horario.setStatus(new DtoGenericBean(rst.getString(5), tmpString));
                horario.setListStations(new ArrayList());
                result.add(horario);
                schedules += (schedules.isEmpty()) ? rst.getInt(1) : "," + rst.getInt(1);
            }
            closePst();

            if (!schedules.isEmpty()) {
                query = "SELECT eh.horario_id, eh.estacion_id, e.nombre, ech.estacionconfhead_id, ech.nombre "
                        + "FROM estacion_horario eh, estacion e, estacion_conf_head ech "
                        + "WHERE eh.estacion_id = e.estacion_id AND eh.estacionconfhead_id = ech.estacionconfhead_id AND eh.horario_id IN (" + schedules + ") "
                        + "ORDER BY eh.horario_id";
                pst = getConnection().prepareStatement(query);
                rst = pst.executeQuery();
                Estacion station;
                while (rst.next()) {
                    for (Horario item : result) {
                        if (item.getHorarioId() == rst.getInt(1)) {
                            station = new Estacion(rst.getInt(2), rst.getString(3), null, null, null, null);
                            station.setSelected(true);
                            station.setEstacionConfHead(new EstacionConfHead(rst.getInt(4), rst.getString(5), null, null, null, null, null));
                            item.getListStations().add(station);
                            break;
                        }
                    }
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public Horario doActionHorario(String action, Horario horario) {
        Horario result = new Horario();
        try {
            getConnection().setAutoCommit(false);
            if (action.equals(Dao.ACTION_ADD)) {
                query = "SELECT horario_seq.NEXTVAL FROM DUAL";
                System.out.println("SELECT_HORARIO" + query); //MG
                pst = getConnection().prepareStatement(query);
                ResultSet rst = pst.executeQuery();
                int horarioId = (rst.next()) ? rst.getInt(1) : 0;
                horario.setHorarioId(horarioId);
                closePst();

                query = "INSERT INTO horario (horario_id, nombre, descripcion, hora_inicio, hora_fin, creado_por) "
                        + "VALUES (" + horarioId + ", ?, ?, ?, ?, ?)";
                System.out.println("HORARIO " + query); //MG
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, horario.getNombre());
                pst.setObject(2, horario.getDescripcion());
                pst.setObject(3, horario.getHoraInicio());
                pst.setObject(4, horario.getHoraFin());
                pst.setObject(5, horario.getCreadoPor());
                pst.executeUpdate();
                closePst();

                query = "INSERT INTO estacion_horario (horario_id, estacion_id, creado_por, estacionconfhead_id, paisestacion_id) "
                        + "VALUES (?, ?, ?, ?, ?)";
                System.out.println("ESTACION_HORARIO " + query); //MG
                for (Estacion item : horario.getListStations()) {
                    pst = getConnection().prepareStatement(query);
                    pst.setObject(1, horarioId);
                    pst.setObject(2, item.getEstacionId());
                    pst.setObject(3, horario.getCreadoPor());
                    pst.setObject(4, item.getEstacionConfHead().getEstacionconfheadId());
                    pst.setObject(5, item.getPaisId());
                    pst.executeUpdate();
                    closePst();
                }
                result = horario;
                getConnection().commit();
            } else if (action.equals(Dao.ACTION_UPDATE)) {
                query = "UPDATE horario "
                        + "SET nombre = ?, descripcion = ?, hora_inicio = ?, hora_fin = ?, estado = ?, modificado_por = ?, modificado_el = SYSDATE "
                        + "WHERE horario_id = ?";
                System.out.println("UPDATE " + query); //MG
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, horario.getNombre());
                pst.setObject(2, horario.getDescripcion());
                pst.setObject(3, horario.getHoraInicio());
                pst.setObject(4, horario.getHoraFin());
                pst.setObject(5, horario.getEstado());
                pst.setObject(6, horario.getModificadoPor());
                pst.setObject(7, horario.getHorarioId());
                pst.executeUpdate();
                closePst();

                String tmpString = "";
                for (Estacion item : horario.getListStations()) {
                    tmpString += (tmpString.isEmpty()) ? item.getEstacionId() : "," + item.getEstacionId();
                }
                query = "DELETE FROM estacion_horario "
                        + "WHERE horario_id = ? AND paisestacion_id = ? ";//MG
                //  + "AND estacion_id NOT IN (" + tmpString + ")";
                System.out.println("DELETE " + query); //MG
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, horario.getHorarioId());
                pst.setObject(2, horario.getListStations().get(0).getPaisId());
                System.out.println("HORARIO-1 " + horario.getHorarioId());
                System.out.println("HORARIO-2 " + +horario.getListStations().get(0).getPaisId());

                pst.executeUpdate();
                closePst();
                try {
                    query = "INSERT INTO estacion_horario (horario_id, estacion_id, creado_por, estacionconfhead_id, paisestacion_id) "
                            + "VALUES (?, ?, ?, ?, ?)";

                    for (Estacion item : horario.getListStations()) {
                        System.out.println("INSERT " + query + " " + item.toString()); //MG
                        pst = getConnection().prepareStatement(query);
                        pst.setObject(1, horario.getHorarioId());
                        System.out.println("1 " + horario.getHorarioId());
                        pst.setObject(2, item.getEstacionId());
                        System.out.println("2 " + item.getEstacionId());
                        pst.setObject(3, horario.getCreadoPor());
                        System.out.println("3 " + horario.getCreadoPor());
                        pst.setObject(4, item.getEstacionConfHead().getEstacionconfheadId());
                        System.out.println("4 " + item.getEstacionConfHead().getEstacionconfheadId());
                        pst.setObject(5, item.getPaisId());
                        System.out.println("5 " + item.getPaisId());
                        pst.executeUpdate();
                        closePst();
                    }
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
                result = horario;
                getConnection().commit();
            }
        } catch (Exception exc) {
            try {
                getConnection().rollback();
            } catch (Exception ignore) {
            }
            System.out.println("SvcGeneral.doActionHorario - " + exc.getMessage());
            exc.printStackTrace();
            result.setDescError(exc.getMessage());
        } finally {
            closePst();
        }
        return result;
    }

//    public Lubricanteprecio doActionLubprecio(String action, Lubricanteprecio lub) {
//        Lubricanteprecio result = new Lubricanteprecio();
//        try {
//            getConnection().setAutoCommit(false);
//            if (action.equals(Dao.ACTION_ADD)) {
//                query = "SELECT lubricanteprecio_seq.NEXTVAL FROM DUAL";
//                pst = getConnection().prepareStatement(query);
//                ResultSet rst = pst.executeQuery();
//                int lubprecioId = (rst.next()) ? rst.getInt(1) : 0;
//                lub.setLubricanteprecio(lubprecioId);
//                closePst();
//                System.out.println("lub.getFechaInicio().getTime() "+lub.getFechaInicio().getTime());
//                query = "INSERT INTO lubricanteprecio (pais_id, producto_id, fecha_inicio, fecha_fin, precio, creado_por, lubricanteprecio) "
//                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
//                System.out.println("doActionLubprecio "+query);
//                pst = getConnection().prepareStatement(query);
//                pst.setObject(1, lub.getPaisId());
//                pst.setObject(2, lub.getProductoId());
//                pst.setObject(3, new java.sql.Date(lub.getFechaInicio().getTime()));
//                pst.setObject(4, new java.sql.Date(lub.getFechaFin().getTime()));
//                pst.setObject(5, lub.getPrecio());
//                pst.setObject(6, lub.getCreadoPor());
//                pst.setObject(7, lub.getLubricanteprecio());
//                pst.executeUpdate();
//            } else if (action.equals(Dao.ACTION_UPDATE)) {
//                query = "UPDATE lubricanteprecio "
//                        + "SET pais_id = ?, producto_id = ?, fecha_inicio = ?, fecha_fin = ?, precio = ?, modificado_por = ?, modificado_el = SYSDATE "
//                        + "WHERE lubricanteprecio = ?";
//                pst = getConnection().prepareStatement(query);
//                pst.setObject(1, lub.getPaisId());
//                pst.setObject(2, lub.getProductoId());
//                pst.setObject(3, new java.sql.Date(lub.getFechaInicio().getTime()));
//                pst.setObject(4, new java.sql.Date(lub.getFechaFin().getTime()));
//                pst.setObject(5, lub.getPrecio());
//                pst.setObject(6, lub.getModificadoPor());
//                pst.setObject(7, lub.getLubricanteprecio());
//                pst.executeUpdate();
//            }
//            result = lub;
//            getConnection().commit();
//        } catch (Exception exc) {
//            try { getConnection().rollback(); } catch(Exception ignore) {}
//            result.setDescError(exc.getMessage());
//            exc.printStackTrace();
//        }
//        return result;
//    }
    public Lubricanteprecio doActionLubprecio(String action, Lubricanteprecio lub, Lubricanteprecio lubricante_log) {
        Lubricanteprecio result = new Lubricanteprecio();
        try {
            getConnection().setAutoCommit(false);
            if (action.equals(Dao.ACTION_ADD)) {
                query = "SELECT lubricanteprecio_seq.NEXTVAL FROM DUAL";
                pst = getConnection().prepareStatement(query);
                ResultSet rst = pst.executeQuery();
                int lubprecioId = (rst.next()) ? rst.getInt(1) : 0;
                System.out.println("lub precio " + lubprecioId);
                lub.setLubricanteprecio(lubprecioId);
                closePst();
                query = "INSERT INTO lubricanteprecio (pais_id, producto_id, fecha_inicio, fecha_fin, precio, creado_por, lubricanteprecio) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                System.out.println("doActionLubprecio " + query + " " + lub.toString());
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, lub.getPaisId());
                pst.setObject(2, lub.getProductoId());
                pst.setObject(3, new java.sql.Date(lub.getFechaInicio().getTime()));
                pst.setObject(4, new java.sql.Date(lub.getFechaFin().getTime()));
                pst.setObject(5, lub.getPrecio());
                pst.setObject(6, lub.getCreadoPor());
                pst.setObject(7, lub.getLubricanteprecio());
                pst.executeUpdate();
            } else if (action.equals(Dao.ACTION_UPDATE)) {
                query = "UPDATE lubricanteprecio "
                        + "SET pais_id = ?, producto_id = ?, fecha_inicio = ?, fecha_fin = ?, precio = ?, modificado_por = ?, modificado_el = SYSDATE "
                        + "WHERE lubricanteprecio = ?";
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, lub.getPaisId());
                pst.setObject(2, lub.getProductoId());
                pst.setObject(3, new java.sql.Date(lub.getFechaInicio().getTime()));
                pst.setObject(4, new java.sql.Date(lub.getFechaFin().getTime()));
                pst.setObject(5, lub.getPrecio());
                pst.setObject(6, lub.getModificadoPor());
                pst.setObject(7, lub.getLubricanteprecio());
                pst.executeUpdate();
                doActionLubprecioLog(lubricante_log);
            }
            result = lub;
            getConnection().commit();
        } catch (Exception exc) {
            try {
                getConnection().rollback();
            } catch (Exception ignore) {
            }
            result.setDescError(exc.getMessage());
            exc.printStackTrace();
        }
        return result;
    }

    public String doBulkInsert(List<String> listInsert) {
        String result = null, currentInsert = "";
        int count = 0;
        try {
            getConnection().setAutoCommit(false);
            for (String item : listInsert) {
                currentInsert = item;
                pst = getConnection().prepareStatement(item);
                pst.executeUpdate();
                closePst();
                count++;
            }
            getConnection().commit();
            result = Integer.toString(count);
        } catch (Exception exc) {
            try {
                getConnection().rollback();
            } catch (Exception ignore) {
            }
            result = count + "; " + exc.getMessage();
            System.out.println("" + exc.getMessage() + "\n " + currentInsert);
            exc.printStackTrace();
        }
        return result;
    }

    public Map<String, Integer> getStationsMap() {
        Map<String, Integer> result = new HashMap<>();
        try {
            query = "SELECT codigo, estacion_id FROM estacion";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.put(rst.getString(1), rst.getInt(2));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public boolean existeCodEnvoy(String CODIGO_ENVOY) {
        boolean result = false;
        ResultSet rst = null;
        try {
            miQuery = "SELECT CODIGO_ENVOY "
                    + "FROM CLIENTE "
                    + "WHERE CODIGO_ENVOY = '" + CODIGO_ENVOY + "'";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            result = rst.next();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public void doActionLubprecioLog(Lubricanteprecio lub) {
        try {
            getConnection().setAutoCommit(false);
            query = "INSERT INTO LUBRICANTEPRECIOLOG (lubricanteprecio, fecha_inicio, fecha_fin, precio, modificado_por,modificado_el) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            System.out.println("doActionLubprecio " + query + " " + lub.toString());
            pst = getConnection().prepareStatement(query);
            pst.setObject(1, lub.getLubricanteprecio());
            pst.setObject(2, new java.sql.Date(lub.getFechaInicio().getTime()));
            pst.setObject(3, new java.sql.Date(lub.getFechaFin().getTime()));
            pst.setObject(4, lub.getPrecio());
            pst.setObject(5, lub.getModificadoPor());
            if (lub.getModificadoEl() == null) {
                lub.setModificadoEl(new Date());
            }
            pst.setObject(6, new java.sql.Date(lub.getModificadoEl().getTime()));
            pst.executeUpdate();
            getConnection().commit();
        } catch (Exception exc) {
            try {
                getConnection().rollback();
            } catch (Exception ignore) {
            }
            exc.printStackTrace();
        }
    }

    public List<Lubricanteprecio> getLubpriceHistory(int id) {
        List<Lubricanteprecio> result = new ArrayList();
        try {
            query = " SELECT rownum,FECHA_INICIO,FECHA_FIN,PRECIO,MODIFICADO_POR,MODIFICADO_EL "
                    + " FROM LUBRICANTEPRECIOLOG "
                    + " WHERE LUBRICANTEPRECIO = ? ";
            System.out.println("getLub " + query);
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rst = pst.executeQuery();
            Lubricanteprecio lpo;
            while (rst.next()) {
                lpo = new Lubricanteprecio();
                lpo.setLubricanteprecio(rst.getInt(1));
                lpo.setFechaInicio(new java.util.Date(rst.getDate(2).getTime()));
                lpo.setFechaFin(new java.util.Date(rst.getDate(3).getTime()));
                lpo.setPrecio(rst.getDouble(4));
                lpo.setModificadoPor(rst.getString(5));
                lpo.setModificadoEl(new java.util.Date(rst.getDate(6).getTime()));
                result.add(lpo);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public Lubricanteprecio doActionLubprecioCarga(String action, Lubricanteprecio lub, Lubricanteprecio lubAnterior) {
        Lubricanteprecio result = new Lubricanteprecio();
        System.out.println("fechhhhhhhhhhhhha " + lub.getFechaFin()+ " " + lub.getFechaInicio());
        try {
            getConnection().setAutoCommit(false);
            if (action.equals(Dao.ACTION_ADD)) {
                query = "SELECT lubricanteprecio_seq.NEXTVAL FROM DUAL";
                pst = getConnection().prepareStatement(query);
                ResultSet rst = pst.executeQuery();
                int lubprecioId = (rst.next()) ? rst.getInt(1) : 0;
                System.out.println("lub precio " + lubprecioId);
                lub.setLubricanteprecio(lubprecioId);
                closePst();
                query = "INSERT INTO lubricanteprecio (lubricanteprecio, pais_id, producto_id, fecha_inicio, fecha_fin, precio, creado_por, creado_el) "
                        + "VALUES (" + lubprecioId + ",?, ?, to_date(?,'dd/mm/yyyy'), to_date(?,'dd/mm/yyyy'), ?, ?, sysdate)";
                System.out.println("doActionLubprecio " + query + " " + lub.toString());
                
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, lub.getPaisId());
                pst.setObject(2, lub.getProductoId());
                pst.setString(3, Constant.SDF_ddMMyyyy.format(lub.getFechaInicio()));
                pst.setString(4, Constant.SDF_ddMMyyyy.format(lub.getFechaFin()));
                pst.setObject(5, lub.getPrecio());
                pst.setObject(6, lub.getCreadoPor());
                //pst.setObject(7, lub.getLubricanteprecio());
                pst.executeUpdate();
            } else if (action.equals(Dao.ACTION_UPDATE)) {
                query = "UPDATE lubricanteprecio "
                        + "SET pais_id = ?, producto_id = ?, fecha_inicio = to_date(?,'dd/mm/yyyy'), fecha_fin = to_date(?,'dd/mm/yyyy'), precio = ?, modificado_por = ?, modificado_el = SYSDATE "
                        + "WHERE lubricanteprecio = ? ";
                System.out.println("query update " + query);
                System.out.println("lub para update " + lub.toString());
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, lub.getPaisId());
                pst.setObject(2, lub.getProductoId());
                pst.setString(3, Constant.SDF_ddMMyyyy.format(lub.getFechaInicio()));
                pst.setString(4, Constant.SDF_ddMMyyyy.format(lub.getFechaFin()));
                pst.setObject(5, lub.getPrecio());
                pst.setObject(6, lub.getModificadoPor());
                pst.setObject(7, lub.getLubricanteprecio());
                pst.executeUpdate();
                doActionLubprecioLog(lubAnterior);
            }
            result = lub;
            getConnection().commit();
        } catch (Exception exc) {
            try {
                getConnection().rollback();
            } catch (Exception ignore) {
            }
            result.setDescError(exc.getMessage());
            exc.printStackTrace();
        }
        return result;
    }

    public List<Producto> getProductsNotInLubsPrecio() {
        List<Producto> result = new ArrayList();
        try {
            query = "select PRODUCTO_ID, NOMBRE from PRODUCTO "
                    + " WHERE PRODUCTO_ID NOT IN(SELECT PRODUCTO_ID FROM LUBRICANTEPRECIO)";
            System.out.println("getProductsNotInLubsPrecio " + query);
            pst = getConnection().prepareStatement(query);
            //pst.setInt(1, id);
            ResultSet rst = pst.executeQuery();
            Producto prod;
            while (rst.next()) {
                prod = new Producto();
                prod.setProductoId(rst.getInt(1));
                prod.setNombre(rst.getString(2));
                result.add(prod);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

}
