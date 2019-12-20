/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.sisintegrados.generic.bean.Estacion;
import com.sisintegrados.generic.bean.GenericBeanCliente;
import com.sisintegrados.generic.bean.GenericBeanMedioPago;
import com.sisintegrados.generic.bean.GenericDetalleBCR;
import com.sisintegrados.generic.bean.GenericDetalleFM;
import com.sisintegrados.generic.bean.GenericLote;
import com.vaadin.data.util.BeanContainer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Allan G.
 */
public class SvcDetalleTcClientes extends Dao {

    private String query;

    public boolean CreaClienteFMDavivienda(Integer turnoid, Integer mediopagoid, BeanContainer<Integer, GenericDetalleFM> bcrDetalleCliDavi) throws SQLException {
        boolean result = false;
        PreparedStatement pst = null;

        /*Elimina antes de volver asignar*/
        try {
            query = "DELETE FROM TARJETA_DETALLE_FM WHERE IDMEDIOPAGO = " + mediopagoid + " AND TURNOID = " + turnoid;
            pst = getConnection().prepareStatement(query);
            pst.executeUpdate();
            closePst();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*Asigna detalle clientes tc al turno*/
        try {
            query = "INSERT INTO TARJETA_DETALLE_FM (IDDET,IDESTACION, IDMEDIOPAGO, TURNOID, LOTE,CLIENTE,VENTA,COMENTARIO) "
                    + "VALUES (SQ_TC_DETALLE_FM.nextval,?,?,?,?,?,?,?)";

            for (Integer itemId : bcrDetalleCliDavi.getItemIds()) {
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, bcrDetalleCliDavi.getItem(itemId).getBean().getEstacion().getEstacionId());
                pst.setInt(2, bcrDetalleCliDavi.getItem(itemId).getBean().getMediopago().getMediopagoid());
                pst.setInt(3, turnoid);
                pst.setInt(4, bcrDetalleCliDavi.getItem(itemId).getBean().getGenlote().getIdlote());
                pst.setString(5, bcrDetalleCliDavi.getItem(itemId).getBean().getCliente());
                pst.setDouble(6, bcrDetalleCliDavi.getItem(itemId).getBean().getVenta());
                pst.setString(7, bcrDetalleCliDavi.getItem(itemId).getBean().getComentario());
                pst.executeUpdate();
                closePst();
            }
            result = true;
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
        return result;
    }

    public boolean CreaClienteFMScott(Integer turnoid, Integer mediopagoid, BeanContainer<Integer, GenericDetalleFM> bcrDetalleCliScott) throws SQLException {
        boolean result = false;
        PreparedStatement pst = null;

        /*Elimina antes de volver asignar*/
        try {
            query = "DELETE FROM TARJETA_DETALLE_FM WHERE IDMEDIOPAGO = " + mediopagoid + " AND TURNOID = " + turnoid;
            pst = getConnection().prepareStatement(query);
            pst.executeUpdate();
            closePst();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*Asigna detalle clientes tc al turno*/
        try {
            query = "INSERT INTO TARJETA_DETALLE_FM (IDDET,IDESTACION, IDMEDIOPAGO, TURNOID, LOTE,CLIENTE,VENTA,COMENTARIO) "
                    + "VALUES (SQ_TC_DETALLE_FM.nextval,?,?,?,?,?,?,?)";

            for (Integer itemId : bcrDetalleCliScott.getItemIds()) {
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, bcrDetalleCliScott.getItem(itemId).getBean().getEstacion().getEstacionId());
                pst.setInt(2, bcrDetalleCliScott.getItem(itemId).getBean().getMediopago().getMediopagoid());
                pst.setInt(3, turnoid);
                pst.setInt(4, bcrDetalleCliScott.getItem(itemId).getBean().getGenlote().getIdlote());
                pst.setString(5, bcrDetalleCliScott.getItem(itemId).getBean().getCliente());
                pst.setDouble(6, bcrDetalleCliScott.getItem(itemId).getBean().getVenta());
                pst.setString(7, bcrDetalleCliScott.getItem(itemId).getBean().getComentario());
                pst.executeUpdate();
                closePst();
            }
            result = true;
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
        return result;
    }

    public List<GenericDetalleFM> getDetalleByMedioPago(Integer estacionid, Integer turnoid, Integer mediopagoid) {
        List<GenericDetalleFM> result = new ArrayList();

        try {
            query = "SELECT A.IDDET,A.CLIENTE,A.VENTA,A.COMENTARIO,       \n"
                    + "       B.ESTACION_ID,B.NOMBRE,\n"
                    + "       C.MEDIOPAGO_ID,C.NOMBRE,\n"
                    + "       D.LOTE_ID,D.LOTE\n"
                    + "  FROM TARJETA_DETALLE_FM  A, ESTACION B, MEDIOPAGO C,\n"
                    + "       (SELECT x.LOTE LOTE_ID, x.lote FROM arqueocaja_tc x, mediopago y WHERE x.TARJETA_ID = y.MEDIOPAGO_ID AND x.tarjeta_id = ? AND x.arqueocaja_id IN\n"
                    + "                          (SELECT arqueocaja_id FROM arqueocaja WHERE turno_id = ?)\n"
                    + "         GROUP BY x.tarjeta_id, y.NOMBRE, x.lote) D\n"
                    + " WHERE     A.IDESTACION = B.ESTACION_ID\n"
                    + "       AND A.IDMEDIOPAGO = C.MEDIOPAGO_ID\n"
                    + "       AND A.LOTE = D.LOTE_ID\n"
                    + "       AND A.IDESTACION = ?\n"
                    + "       AND A.turnoid = ?";
            pst = getConnection().prepareStatement(query);

            /*Envio parametros necesarios*/
            pst.setInt(1, mediopagoid);
            pst.setInt(2, turnoid);
            pst.setInt(3, estacionid);
            pst.setInt(4, turnoid);
            ResultSet rst = pst.executeQuery();

            while (rst.next()) {
                result.add(new GenericDetalleFM(rst.getInt(1), new Estacion(rst.getInt(5), rst.getString(6)), new GenericBeanMedioPago(rst.getInt(7), rst.getString(8)), new GenericLote(rst.getInt(9), rst.getInt(10)), rst.getString(2), rst.getDouble(3), rst.getString(4)));
            }
            closePst();
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

    public List<Estacion> getAllEstaciones(boolean includeInactive, Integer paisid) {
        List<Estacion> result = new ArrayList();
        //String statusName;
        ResultSet rst = null;
        try {
            query = (includeInactive) ? "" : " AND e.estado = 'A' ";
            query = "SELECT e.estacion_id, e.nombre "
                    + "FROM estacion e, pais p "
                    + "WHERE e.pais_id = p.pais_id "
                    + "AND e.pais_id = " + paisid
                    + query
                    + " ORDER BY p.nombre ";
            pst = getConnection().prepareStatement(query);
            rst = pst.executeQuery();
            Estacion estacion;
            while (rst.next()) {
                estacion = new Estacion(rst.getInt(1), rst.getString(2));
                result.add(estacion);
            }
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

    public List<GenericLote> getAllLotesbyMedioPago(Integer mediopagoid, Integer turnoid) {
        List<GenericLote> result = new ArrayList();
        //String statusName;
        ResultSet rst = null;
        try {
            query = "SELECT a.LOTE LOTE_ID, a.lote\n"
                    + "   FROM arqueocaja_tc a, mediopago b\n"
                    + "   WHERE     a.TARJETA_ID = b.MEDIOPAGO_ID\n"
                    + "         AND a.tarjeta_id = ?\n"
                    + "         AND a.arqueocaja_id IN\n"
                    + "                 (SELECT arqueocaja_id\n"
                    + "                    FROM arqueocaja\n"
                    + "                   WHERE turno_id = ?)\n"
                    + "GROUP BY a.tarjeta_id, b.NOMBRE, a.lote";

            pst = getConnection().prepareStatement(query);


            /*Envio parametros necesarios*/
            pst.setInt(1, mediopagoid);
            pst.setInt(2, turnoid);

            rst = pst.executeQuery();

            GenericLote lote;
            while (rst.next()) {
                lote = new GenericLote(rst.getInt(1), rst.getInt(2));
                result.add(lote);
            }
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

    public List<GenericBeanMedioPago> getAllMediosPago(boolean includeInactives, Integer paisid) {
        List<GenericBeanMedioPago> result = new ArrayList();
        try {
            query = (includeInactives) ? "" : " AND m.estado = 'A' ";
            query = "SELECT m.mediopago_id, m.nombre "
                    + "FROM mediopago m, pais p "
                    + "WHERE m.pais_id = p.pais_id "
                    + " AND m.pais_id = " + paisid
                    + query
                    + " ORDER BY m.nombre";
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            GenericBeanMedioPago mediopago;
            while (rst.next()) {
                mediopago = new GenericBeanMedioPago(rst.getInt(1), rst.getString(2));
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

    public List<GenericDetalleBCR> getDetalleByMedioPagoForBCR(Integer estacionid, Integer turnoid, Integer mediopagoid) {
        List<GenericDetalleBCR> result = new ArrayList();

        try {
            query = "SELECT A.IDDET,A.CLIENTE,A.VENTA,A.COMENTARIO, --DETALLECLIE\n"
                    + "                     B.ESTACION_ID,B.NOMBRE, --ESTACION\n"
                    + "                       C.MEDIOPAGO_ID,C.NOMBRE, ---MEDIO PAGO\n"
                    + "                      D.LOTE_ID,D.LOTE,  ---INFORMACION GENERIC BEAN LOTE\n"
                    + "                      E.CLIENTE_ID,E.CODIGO,E.NOMBRE  ---INFORMACION PARA EL GENERIC BEAN CLIENTE\n"
                    + "                 FROM TARJETA_DETALLE_CLIE  A, ESTACION B, MEDIOPAGO C, CLIENTE E,\n"
                    + "                     (SELECT x.LOTE LOTE_ID, x.lote FROM arqueocaja_tc x, mediopago y WHERE x.TARJETA_ID = y.MEDIOPAGO_ID AND x.tarjeta_id = ? AND x.arqueocaja_id IN\n"
                    + "                                         (SELECT arqueocaja_id FROM arqueocaja WHERE turno_id = ?)\n"
                    + "                          GROUP BY x.tarjeta_id, y.NOMBRE, x.lote) D\n"
                    + "                    WHERE     A.IDESTACION = B.ESTACION_ID\n"
                    + "                        AND A.IDMEDIOPAGO = C.MEDIOPAGO_ID\n"
                    + "                       AND A.LOTE = D.LOTE_ID\n"
                    + "                       AND A.CLIENTE = E.CLIENTE_ID\n"
                    + "                       AND A.IDESTACION = E.ESTACION_ID\n"
                    + "                       AND A.IDMEDIOPAGO = ?\n"
                    + "                       AND A.IDESTACION = ?\n"
                    + "                        AND A.turnoid = ?";
            pst = getConnection().prepareStatement(query);

            /*Envio parametros necesarios*/
            pst.setInt(1, mediopagoid);
            pst.setInt(2, turnoid);
            pst.setInt(3, mediopagoid);
            pst.setInt(4, estacionid);
            pst.setInt(5, turnoid);
            ResultSet rst = pst.executeQuery();

            while (rst.next()) {
                result.add(
                        new GenericDetalleBCR(rst.getInt(1),
                                new Estacion(rst.getInt(5), rst.getString(6)),
                                new GenericBeanMedioPago(rst.getInt(7), rst.getString(8)),
                                new GenericLote(rst.getInt(9), rst.getInt(10)),
                                new GenericBeanCliente(rst.getInt(11), rst.getInt(12), rst.getString(13)),
                                rst.getDouble(3),
                                rst.getString(4)));
            }
            closePst();
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

    public boolean CreaClienteBCR(Integer turnoid, Integer mediopagoid, BeanContainer<Integer, GenericDetalleBCR> bcrDetalleCliBCR) throws SQLException {
        boolean result = false;
        PreparedStatement pst = null;

        /*Elimina antes de volver asignar*/
        try {
            query = "DELETE FROM TARJETA_DETALLE_CLIE WHERE IDMEDIOPAGO = " + mediopagoid + " AND TURNOID = " + turnoid;
            pst = getConnection().prepareStatement(query);
            pst.executeUpdate();
            closePst();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*Asigna detalle clientes tc al turno*/
        try {
            query = "INSERT INTO TARJETA_DETALLE_CLIE (IDDET,IDESTACION, IDMEDIOPAGO, TURNOID, LOTE,CLIENTE,VENTA,COMENTARIO) "
                    + "VALUES (SQ_TC_DETALLE_CLIE.nextval,?,?,?,?,?,?,?)";

            for (Integer itemId : bcrDetalleCliBCR.getItemIds()) {
                pst = getConnection().prepareStatement(query);
                pst.setInt(1, bcrDetalleCliBCR.getItem(itemId).getBean().getEstacion().getEstacionId());
                pst.setInt(2, bcrDetalleCliBCR.getItem(itemId).getBean().getMediopago().getMediopagoid());
                pst.setInt(3, turnoid);
                pst.setInt(4, bcrDetalleCliBCR.getItem(itemId).getBean().getGenlote().getIdlote());
                pst.setInt(5, bcrDetalleCliBCR.getItem(itemId).getBean().getCliente().getClienteid());
                pst.setDouble(6, bcrDetalleCliBCR.getItem(itemId).getBean().getVenta());
                pst.setString(7, bcrDetalleCliBCR.getItem(itemId).getBean().getComentario());
                pst.executeUpdate();
                closePst();
            }
            result = true;
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
        return result;
    }

    public List<GenericBeanCliente> getAllCustomers(boolean includeInactives, Integer estacionid) {
        List<GenericBeanCliente> result = new ArrayList();
        try {
            query = (includeInactives) ? "" : " AND m.estado = 'A' ";
            query = "select cliente_id, codigo, nombre "
                    + " from cliente "
                    + " where "
                    + " ESTACION_ID = " + estacionid
                    + query;
               
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            GenericBeanCliente customer;
            while (rst.next()) {
                customer = new GenericBeanCliente(rst.getInt(1), rst.getInt(2), rst.getString(3));
                result.add(customer);
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

}
