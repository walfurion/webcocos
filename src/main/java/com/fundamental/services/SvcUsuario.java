package com.fundamental.services;

import com.fundamental.model.Rol;
import com.sisintegrados.generic.bean.Usuario;
import com.sisintegrados.generic.bean.GenericDia;
import com.sisintegrados.generic.bean.GenericTurno;
import com.fundamental.utils.Constant;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author Henry Barrientos
 */
public class SvcUsuario extends Dao {

    public SvcUsuario() {
    }

    private String query;

    public Usuario getUserByUserPass(String username, String password) {
        Usuario result = new Usuario();
        GenericDia ultDia = new GenericDia();
        GenericTurno ultTurno = new GenericTurno();

        ResultSet rst = null;
        try {
            query = (username.matches("\\d+"))
                    ? " AND u.username LIKE '%" + username + "' " : " AND u.username = '" + username + "'";

            query = "SELECT u.usuario_id, u.username, u.clave, u.nombre, u.apellido, u.estado, u.pais_id "
                    + "FROM usuario u "
                    + "WHERE u.estado = 'A' AND u.clave = ? "
                    + query;
            pst = getConnection().prepareStatement(query);
            pst.setString(1, password);
//            pst.setString(1, username);
            rst = pst.executeQuery();
            if (rst.next()) {
                result = new Usuario(rst.getInt(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6), null);
                result.setPaisId(rst.getInt(7));
                List<Rol> misRoles = getRolesByUserid(rst.getInt(1));
                result.setRoles(misRoles);
                for (Rol r : misRoles) {
                    if (r.getRolId().equals(Constant.ROL_PERMISSION_SYSADMIN)) {
                        result.setSysadmin(true);
                    }
                    if (r.getRolId().equals(Constant.ROL_PERMISSION_GERENTE)) {
                        result.setGerente(true);
                    }
                    if (r.getRolId().equals(Constant.ROL_PERMISSION_ADMINISTRATIVO)) {
                        result.setAdministrativo(true);
                    }
                    if (r.getRolId().equals(Constant.ROL_PERMISSION_JEFEPAIS)) {
                        result.setJefePais(true);
                    }
                }
            }
            rst.close();
            pst.close();

            result = getLastTurnLastDay(result);

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }

        return result;
    }

    public Usuario getLastTurnLastDay(Usuario usuario) {
        GenericDia ultDia = new GenericDia();
        GenericTurno ultTurno = new GenericTurno();
        ResultSet rst = null;
        Usuario user = new Usuario();
        user = usuario;
        try {

            /*Recupera*/
 /*Datos de pais y estacion asignada al usuario*/
            query = "SELECT a.ESTACION_ID,a.PAIS_ID\n"
                    + "  FROM estacion a, estacion_usuario b\n"
                    + " WHERE a.ESTACION_ID = b.ESTACION_ID\n"
                    + " AND b.USUARIO_ID  = ? \n"
                    + " AND (SELECT COUNT(*) FROM estacion_usuario where usuario_id = b.USUARIO_ID) = 1";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, usuario.getUsuarioId());
            rst = pst.executeQuery();

            if (rst.next()) {
                user.setPaisId(rst.getInt(2));
                user.setEstacionid(rst.getInt(1));
            }

            rst.close();
            pst.close();

            /*Recupera datos ultimo dia*/
            query = "Select to_char(d.fecha,'dd/mm/yyyy') DIA,\n"
                    + "CASE d.estado_id WHEN 2 THEN 'CERRADO'\n"
                    + "ELSE 'ABIERTO'\n"
                    + "END ESTADO\n"
                    + "from dia d\n"
                    + "where d.fecha = (select max(fecha) from dia where estacion_id = d.ESTACION_ID)\n"
                    + "and estacion_id = ?";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, user.getEstacionid());
            rst = pst.executeQuery();

            if (rst.next()) {
                ultDia.setDia(rst.getString(1));
                ultDia.setEstado(rst.getString(2));
                user.setDia(ultDia);
            }
            rst.close();
            pst.close();

            /*Recupera datos ultimo turno*/
            query = "select h.NOMBRE ||'   '||h.HORA_INICIO||'-'|| h.HORA_FIN TURNO,\n"
                    + "CASE d.estado_id WHEN 2 THEN 'CERRADO'\n"
                    + "ELSE 'ABIERTO'\n"
                    + "END ESTADO\n"
                    + "from turno d,\n"
                    + "     horario h\n"
                    + "where fecha = (select max(fecha) from dia where estacion_id = d.ESTACION_ID)\n"
                    + "and turno_id = (select max(turno_id) from turno where estacion_id = d.estacion_id and fecha = d.fecha)\n"
                    + "and d.horario_id = h.HORARIO_ID\n"
                    + "and d.estacion_id = ?";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, user.getEstacionid());
            rst = pst.executeQuery();

            if (rst.next()) {
                ultTurno.setTurno(rst.getString(1));
                ultTurno.setEstado(rst.getString(2));
                user.setTurno(ultTurno);
            }
            rst.close();
            pst.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                rst.close();
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return user;
    }
}
