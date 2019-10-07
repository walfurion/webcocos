package com.fundamental.services;

import com.fundamental.model.Acceso;
import com.sisintegrados.generic.bean.Estacion;
import com.fundamental.model.Marca;
import com.fundamental.model.Mediopago;
import com.sisintegrados.generic.bean.Pais;
import com.fundamental.model.Producto;
import com.fundamental.model.Rol;
import com.fundamental.model.Tipoproducto;
import com.sisintegrados.generic.bean.Usuario;
import com.fundamental.model.dto.DtoGenericBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Henry Barrientos
 */
public class SvcMaintenance extends Dao {

    String query, tmpString;

    public SvcMaintenance() {
    }

    public Usuario doActionUser(String action, Usuario usuario) {
        Usuario result = new Usuario();
        try {
            getConnection().setAutoCommit(false);
            if (action.equals(Dao.ACTION_ADD)) {
                query = "SELECT usuario_seq.NEXTVAL FROM DUAL";
                pst = getConnection().prepareStatement(query);
                ResultSet rst = pst.executeQuery();
                Integer userId = (rst.next()) ? rst.getInt(1) : 0;
                usuario.setUsuarioId(userId);
                closePst();

                query = "INSERT INTO usuario (username, clave, nombre, apellido, creado_por, usuario_id, pais_id, correo) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setString(1, usuario.getUsername());
                pst.setString(2, usuario.getClave());
                pst.setString(3, usuario.getNombre());
                pst.setString(4, usuario.getApellido());
                pst.setString(5, usuario.getCreadoPor());
                pst.setInt(6, usuario.getUsuarioId());
                pst.setObject(7, usuario.getPaisId());
                pst.setObject(8, usuario.getCorreo());
                pst.executeUpdate();
                result = usuario;
            } else if (action.equals(Dao.ACTION_UPDATE)) {
                query = "UPDATE usuario "
                        + "SET username = ?, clave = ?, nombre = ?, apellido = ?, modificado_por = ?, modificado_el = SYSDATE, pais_id = ?, estado = ?, correo = ? "
                        + "WHERE usuario_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setString(1, usuario.getUsername());
                pst.setString(2, usuario.getClave());
                pst.setString(3, usuario.getNombre());
                pst.setString(4, usuario.getApellido());
                pst.setString(5, usuario.getModificadoPor());
                pst.setObject(6, usuario.getPaisId());
                pst.setString(7, usuario.getEstado());
                pst.setObject(8, usuario.getCorreo());
                pst.setInt(9, usuario.getUsuarioId());
                pst.executeUpdate();
                result = usuario;
            } else if (action.equals(Dao.ACTION_DELETE)) {
                query = "UPDATE usuario "
                        + "SET estado = ?, modificado_por = ?, modificado_el = SYSDATE "
                        + "WHERE usuario_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setString(1, usuario.getEstado());
                pst.setString(2, usuario.getModificadoPor());
                pst.setInt(3, usuario.getUsuarioId());
                pst.executeUpdate();
                result = usuario;
            }
            closePst();

            if ((action.equals(Dao.ACTION_ADD) || action.equals(Dao.ACTION_UPDATE)) //&& usuario.getEstacionLogin() != null
                    ) {
                query = "DELETE FROM estacion_usuario "
                        + "WHERE usuario_id = ?"; // AND estacion_id != ?";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, usuario.getUsuarioId());
//                pst.setInt(2, usuario.getEstacionLogin().getEstacionId());
                pst.executeUpdate();
                closePst();

                query = "INSERT INTO estacion_usuario (estacion_id, usuario_id, creado_por, creado_el) "
                        + "VALUES (?, ?, ?, SYSDATE)";
                for (Estacion stn : usuario.getStations()) {
                    pst = getConnection().prepareStatement(query);
                    pst.setInt(1, stn.getEstacionId()); // usuario.getEstacionLogin().getEstacionId());
                    pst.setInt(2, usuario.getUsuarioId());
                    pst.setString(3, usuario.getCreadoPor());
                    try {
                        pst.executeUpdate();
                    } catch (Exception ignore) {
                    }
                    closePst();
                }
            } else if (action.equals(Dao.ACTION_DELETE) && usuario.getEstacionLogin() != null) {
                query = "DELETE FROM estacion_usuario "
                        + "WHERE usuario = ?";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, usuario.getUsuarioId());
                pst.executeUpdate();
                closePst();
            }

            if (action.equals(Dao.ACTION_ADD) || action.equals(Dao.ACTION_UPDATE)) {
                String rolesIds = "";
                query = "INSERT INTO rol_usuario (rol_id, usuario_id, creado_por, creado_el) "
                        + "VALUES (?, ?, ?, SYSDATE)";
                for (Rol rol : usuario.getRoles()) {
                    pst = getConnection().prepareStatement(query);
                    pst.setInt(1, rol.getRolId());
                    pst.setInt(2, usuario.getUsuarioId());
                    pst.setString(3, usuario.getCreadoPor());
                    try {
                        pst.executeUpdate();
                    } catch (Exception ignore) {
                    }
                    closePst();
                    rolesIds += (rolesIds.isEmpty()) ? rol.getRolId() : ",".concat(rol.getRolId().toString());
                }
                if (!rolesIds.isEmpty()) {
                    query = "DELETE FROM rol_usuario "
                            + "WHERE usuario_id = ? AND rol_id NOT IN (" + rolesIds + ")";
                    pst = getConnection().prepareStatement(query);
                    pst.setInt(1, usuario.getUsuarioId());
                    pst.executeUpdate();
                    closePst();
                }
            } else if (action.equals(Dao.ACTION_DELETE)) {
                query = "DELETE FROM rol_usuario "
                        + "WHERE usuario = ?";
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, usuario.getUsuarioId());
                pst.executeUpdate();
                closePst();
            }

            getConnection().commit();
        } catch (Exception exc) {
            try {
                getConnection().rollback();
            } catch (Exception ignore) {
            }
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public List<Rol> getAllRoles(boolean includeInactive) {
        List<Rol> result = new ArrayList();
        try {
//            query = "SELECT acceso_id, titulo, padre, orden, recurso_interno, descripcion, estado "
//                    + "FROM acceso "
//                    + "ORDER BY padre, orden DESC";
            query = (includeInactive) ? "" : " WHERE estado = 'A' ";
            query = "SELECT rol_id, nombre, descripcion, rolpadre_id, estado "
                    + "FROM rol "
                    + query
                    + "ORDER BY nombre";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Rol rol;
            while (rst.next()) {
                rol = new Rol(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5));
                rol.setStatus((rst.getString(5).equals("A")) ? new DtoGenericBean(rst.getString(5), "Activo") : new DtoGenericBean("I", "Inactivo"));
                result.add(rol);
            }
            closePst();
            for (Rol item : result) {
                item.setAccesos(getAccessByRolid(item.getRolId()));
                closePst();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return result;
    }

    public List<Acceso> getAccessByRolid(int rolId) {
        List<Acceso> result = new ArrayList();
        try {
            query = "SELECT a.acceso_id, a.titulo, a.padre, a.orden, a.recurso_interno, a.descripcion, a.estado "
                    + "FROM acceso a, acceso_rol ar "
                    + "WHERE ar.acceso_id = a.acceso_id AND ar.rol_id = " + rolId
                    + " ORDER BY a.padre, a.orden DESC";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Acceso(rst.getInt(1), rst.getString(2), rst.getInt(3), rst.getInt(4), rst.getString(5), rst.getString(6), rst.getString(7)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return result;
    }

    public List<Acceso> getAllAccess(boolean includeInactive) {
        List<Acceso> result = new ArrayList();
        try {
            query = "SELECT a.acceso_id, a.titulo, a.padre, a.orden, a.recurso_interno, a.descripcion, a.estado, ap.titulo "
                    + "FROM acceso a, acceso ap "
                    + "WHERE a.padre = ap.acceso_id ORDER BY a.padre, a.orden DESC";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Acceso access;
            while (rst.next()) {
                access = new Acceso(rst.getInt(1), rst.getString(2), rst.getInt(3), rst.getInt(4), rst.getString(5), rst.getString(6), rst.getString(7));
                access.setNombrePadre(rst.getString(8));
                result.add(access);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return result;
    }

    public Rol doActionRol(String action, Rol rol) {
        Rol result = new Rol();
        try {
            getConnection().setAutoCommit(false);
            if (action.equals(Dao.ACTION_ADD)) {
                query = "SELECT rol_seq.NEXTVAL FROM DUAL";
                pst = getConnection().prepareStatement(query);
                ResultSet rst = pst.executeQuery();
                rol.setRolId(rst.next() ? rst.getInt(1) : 0);
                closePst();
                query = "INSERT INTO rol (nombre, descripcion, estado, creado_por, rol_id) "
                        + "VALUES (?, ?, ?, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, rol.getNombre());
                pst.setObject(2, rol.getDescripcion());
                pst.setObject(3, rol.getEstado());
                pst.setObject(4, rol.getCreadoPor());
                pst.setObject(5, rol.getRolId());
                pst.executeUpdate();
                closePst();
                query = "INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) "
                        + "VALUES (?, ?, ?)";
                for (Acceso item : rol.getAccesos()) {
                    pst = getConnection().prepareStatement(query);
                    pst.setObject(1, item.getAccesoId());
                    pst.setObject(2, rol.getRolId());
                    pst.setObject(3, rol.getCreadoPor());
                    pst.executeUpdate();
                    closePst();
                }
                result = rol;
            } else if (action.equals(Dao.ACTION_UPDATE)) {
                query = "UPDATE rol "
                        + "SET nombre = ?, descripcion = ?, estado = ?, modificado_por = ?, modificado_el = SYSDATE "
                        + "WHERE rol_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, rol.getNombre());
                pst.setObject(2, rol.getDescripcion());
                pst.setObject(3, rol.getEstado());
                pst.setObject(4, rol.getModificadoPor());
                pst.setObject(5, rol.getRolId());
                pst.executeUpdate();
                closePst();
                tmpString = "";
                for (Acceso item : rol.getAccesos()) {
                    tmpString += (tmpString.isEmpty()) ? item.getAccesoId() : "," + item.getAccesoId();
                }
                if (!tmpString.isEmpty()) {
                    query = "DELETE FROM acceso_rol WHERE rol_id = ? AND acceso_id NOT IN (" + tmpString + ")";
                    pst = getConnection().prepareStatement(query);
                    pst.setObject(1, rol.getRolId());
                    pst.executeUpdate();
                    closePst();
                }
                query = "INSERT INTO acceso_rol (acceso_id, rol_id, creado_por) "
                        + "VALUES (?, ?, ?)";
                for (Acceso item : rol.getAccesos()) {
                    pst = getConnection().prepareStatement(query);
                    pst.setObject(1, item.getAccesoId());
                    pst.setObject(2, rol.getRolId());
                    pst.setObject(3, rol.getCreadoPor());
                    try {
                        pst.executeUpdate();
                    } catch (Exception ignore) {
                    }
                    closePst();
                }
                result = rol;
            }
            getConnection().commit();
        } catch (Exception exc) {
            try {
                getConnection().rollback();
            } catch (Exception ignore) {
            }
            exc.printStackTrace();
        }
        return result;
    }

    public Usuario getUserByUsernameEmail(String username, String email) {
        Usuario result = null;
        try {
            query = "SELECT usuario_id, username, clave, nombre, apellido, estado, pais_id, correo "
                    + "FROM usuario "
                    + "WHERE username = ? AND correo = ?";
            pst = getConnection().prepareStatement(query);
            pst.setObject(1, username);
            pst.setObject(2, email);
            ResultSet rst = pst.executeQuery();
            if (rst.next()) {
                result = new Usuario(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6), null);
                result.setPaisId(rst.getInt(7));
                result.setCorreo(rst.getString(8));
            }
        } catch (Exception exc) {
            System.out.println("" + exc.getMessage());
            exc.printStackTrace();
        }
        return result;
    }

    public List<Producto> getAllProducts(boolean includeInactive) {
        List<Producto> result = new ArrayList();
        try {
            query = (includeInactive) ? "" : " AND p.estado = 'A' ";
            query = "SELECT p.producto_id, p.nombre, p.codigo, p.tipo_id, p.orden_pos, p.estado, p.codigo_num, p.presentacion, p.codigo_barras, p.id_marca, p.codigo_envoy "
                    + ", t.tipo_id, t.nombre, m.id_marca, m.nombre, p.sku " //12,...
                    + "FROM tipoproducto t, producto p "
                    + "LEFT JOIN marca m ON p.id_marca = m.id_marca "
                    + "WHERE p.tipo_id = t.tipo_id "
                    + query;
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Producto producto;
            while (rst.next()) {
                producto = new Producto(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getInt(5), rst.getString(6), rst.getInt(7), rst.getString(8), rst.getString(9), rst.getInt(10), rst.getString(11), rst.getString(16));
                producto.setTypeProd(new Tipoproducto(rst.getInt(12), rst.getString(13), "A"));
                producto.setMarca(new Marca(rst.getInt(14), rst.getString(15), "A"));
                producto.setStatus(new DtoGenericBean(rst.getString(6), (rst.getString(6).equals("A") ? "Activo" : "Inactivo")));
                result.add(producto);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return result;
    }

    public List<Tipoproducto> getAllTipoproducto(boolean includeInactives) {
        List<Tipoproducto> result = new ArrayList();
        try {
            query = (includeInactives) ? "" : " WHERE estado = 'A' ";
            query = "SELECT tipo_id, nombre, estado, creado_por, creado_el "
                    + "FROM tipoproducto "
                    + " ORDER BY nombre";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result.add(new Tipoproducto(rst.getInt(1), rst.getString(2), rst.getString(3)));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }

    public List<Mediopago> getAllMediosPago(boolean includeInactives) {
        List<Mediopago> result = new ArrayList();
        try {
            query = (includeInactives) ? "" : " AND m.estado = 'A' ";
            query = "SELECT m.mediopago_id, m.nombre, m.tipo, m.estado, m.orden, m.pais_id, m.tipoprod_id, p.nombre, m.partidacont_por, m.partidacont, m.is_tcredito, p.codigo " //12
                    + "FROM mediopago m, pais p "
                    + "WHERE m.pais_id = p.pais_id "
                    + query
                    + " ORDER BY p.nombre, m.nombre";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            Mediopago mediopago;
            while (rst.next()) {
                mediopago = new Mediopago(rst.getInt(1), rst.getString(2), rst.getInt(3), null, rst.getInt(5), rst.getString(8), rst.getBoolean(11), rst.getString(4));
                mediopago.setPaisId(rst.getInt(6));
                mediopago.setTipoprodId(rst.getInt(7));
                mediopago.setCountry(new Pais(rst.getInt(6), rst.getString(8), rst.getString(12), null, null, null,null));
                query = (rst.getString(4).equals("A")) ? "Activo" : "Inactivo";
                mediopago.setStatus(new DtoGenericBean(rst.getString(4), query));
                mediopago.setPartidacontPor(rst.getDouble(9));
                mediopago.setPartidacont(rst.getBoolean(10));
                result.add(mediopago);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public Producto doActionProduct(String action, Producto product) {
        Producto result = new Producto();
        try {
            if (action.equals(Dao.ACTION_ADD)) {
                query = "SELECT producto_seq.NEXTVAL FROM DUAL";
                pst = getConnection().prepareStatement(query);
                ResultSet rst = pst.executeQuery();
                product.setProductoId((rst.next()) ? rst.getInt(1) : 0);
                closePst();
                query = "INSERT INTO producto (nombre, codigo, tipo_id, orden_pos, estado, codigo_num, presentacion, codigo_barras, id_marca, codigo_envoy, creado_por, creado_el, producto_id, sku) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?)";
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, product.getNombre());
                pst.setObject(2, product.getCodigo());
                pst.setObject(3, product.getTipoId());
                pst.setObject(4, product.getOrdenPos());
                pst.setObject(5, product.getEstado());
                pst.setObject(6, product.getCodigoNumerico());
                pst.setObject(7, product.getPresentacion());
                pst.setObject(8, product.getCodigoBarras());
                pst.setObject(9, product.getIdMarca());
                pst.setObject(10, product.getCodigoEnvoy());
                pst.setObject(11, product.getCreadoPor());
                pst.setObject(12, product.getProductoId());
                pst.setObject(13, product.getSku());
                pst.executeUpdate();
                result = product;
            } else if (action.equals(Dao.ACTION_UPDATE)) {
                query = "UPDATE producto "
                        + "SET nombre = ?, codigo = ?, tipo_id = ?, orden_pos = ?, estado = ?, codigo_num = ?, presentacion = ?, "
                        + "     codigo_barras = ?, id_marca = ?, codigo_envoy = ?, modificado_por = ?, sku =  ?, modificado_el = SYSDATE "
                        + "WHERE producto_id = ?";
                pst = getConnection().prepareStatement(query);
                pst.setObject(1, product.getNombre());
                pst.setObject(2, product.getCodigo());
                pst.setObject(3, product.getTipoId());
                pst.setObject(4, product.getOrdenPos());
                pst.setObject(5, product.getEstado());
                pst.setObject(6, product.getCodigoNumerico());
                pst.setObject(7, product.getPresentacion());
                pst.setObject(8, product.getCodigoBarras());
                pst.setObject(9, product.getIdMarca());
                pst.setObject(10, product.getCodigoEnvoy());
                pst.setObject(11, product.getCreadoPor());
                pst.setObject(12, product.getSku());
                pst.setObject(13, product.getProductoId());
                pst.executeUpdate();
                result = product;
            }
        } catch (Exception exc) {
            result.setDescError(exc.getMessage());
            exc.printStackTrace();
        } finally {
            closePst();
        }
        return result;
    }
    
    public List<Pais> getAllPaisbyProduct(Integer idProducto) {
        List<Pais> result = new ArrayList<Pais>();
        miQuery = "SELECT p.pais_id, nombre, codigo, moneda_simbolo, estado, vol_simbolo, pp.PRODUCTO_ID "
                + "FROM pais p "
                + "LEFT JOIN  pais_producto pp ON p.pais_id = pp.pais_id AND  pp.PRODUCTO_ID = "+idProducto+" "
                + "ORDER BY p.pais_id";
        ResultSet rst = null; try {
            rst = getConnection().prepareStatement(miQuery).executeQuery();
            while (rst.next()) {
                result.add(new Pais(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(6), rst.getString(5),null, rst.getInt(7)>0));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try { rst.close(); pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }
    
    public boolean setProductoPais(List<Pais> paises,Producto product){
        query = "DELETE FROM  pais_producto WHERE producto_id =  "+product.getProductoId();
        try {
            pst = getConnection().prepareStatement(query);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SvcMaintenance.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Pais pais:paises) {
            if(pais.isSelected()){
                query = "INSERT INTO pais_producto (pais_id, producto_id,creado_por) "
                        + "VALUES (?, ?, ?)";
                try {
                    pst = getConnection().prepareStatement(query);
                    pst.setObject(1, pais.getPaisId());
                    pst.setObject(2, product.getProductoId());
                    pst.setObject(3, product.getCreadoPor());
                    pst.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(SvcMaintenance.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }
    
    public boolean existeE1Producto(String codigo){
        boolean result = false;
        ResultSet rst = null; try {
            miQuery = "SELECT codigo "
                    + "FROM PRODUCTO "
                    + "WHERE codigo = '" + codigo+"'";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            result =  rst.next();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
                try { rst.close(); pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }
    
    public boolean existeCodEnvoyProducto(String CODIGO_ENVOY){
        boolean result = false;
        ResultSet rst = null; try {
            miQuery = "SELECT CODIGO_ENVOY "
                    + "FROM PRODUCTO "
                    + "WHERE CODIGO_ENVOY = '" + CODIGO_ENVOY+"'";
            pst = getConnection().prepareStatement(miQuery);
            rst = pst.executeQuery();
            result =  rst.next();
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
                try { rst.close(); pst.close(); } catch (Exception ignore) { }
        }
        return result;
    }
}
