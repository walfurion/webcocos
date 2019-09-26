package com.fundamental.services;

import com.fundamental.model.Rol;
import com.sisintegrados.generic.bean.Usuario;
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
//                result.setEstacionUsuario(getEstacionusuarioByUserid(estacionId, rst.getInt(1)));
            }
            rst.close();
            pst.close();
            query = "SELECT a.ESTACION_ID,a.PAIS_ID\n"
                    + "  FROM estacion a, estacion_usuario b\n"
                    + " WHERE a.ESTACION_ID = b.ESTACION_ID\n"
                    + " AND b.USUARIO_ID  = ? \n"
                    + " AND (SELECT COUNT(*) FROM estacion_usuario where usuario_id = b.USUARIO_ID) = 1";
            pst = getConnection().prepareStatement(query);
            pst.setInt(1, result.getUsuarioId());
            rst = pst.executeQuery();
            if (rst.next()) {
                System.out.println("PAIS ID " + rst.getInt(2));
                result.setPaisId(rst.getInt(2));
            }
//            result.sete(rst.getInt(2));

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

}
