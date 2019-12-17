/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.services;

import com.sisintegrados.generic.bean.GenericEstacion;
import com.sisintegrados.generic.bean.GenericMTD;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Allan G.
 */
public class SvcMtd extends Dao {

    private String query;

    public ArrayList<GenericEstacion> getCheckEstaciones(Integer idpais) {
        ArrayList<GenericEstacion> result = new ArrayList<GenericEstacion>();
        GenericEstacion genestacion = new GenericEstacion();
        try {
            query = "Select estacion_id,nombre from estacion where pais_id =" + idpais;
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                genestacion = new GenericEstacion();
                genestacion.setEstacionid(rst.getInt(1));
                genestacion.setNombre(rst.getString(2));
                result.add(genestacion);
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

    public ArrayList<GenericMTD> getMTD() {
        ArrayList<GenericMTD> result = new ArrayList<GenericMTD>();
        GenericMTD genmtd = new GenericMTD();
        try {
            query = "Select FECHA, P_SUPER, P_REGULAR, P_DIESEL, \n"
                    + "                L_SUPER, L_REGULAR, L_DIESEL, C_DIESEL, C_SUPER, \n"
                    + "                C_REGULAR, L_TOTAL, C_TOTAL, TIENDA_TOTAL, CANASTA_TOTAL, \n"
                    + "                MEDICAMENTO_TOTAL, OTROS_LUB_TOTAL, UNO_LUB_TOTAL, VENTA_TURNO_A, VENTA_TURNO_B, \n"
                    + "                VENTA_TURNO_C, VENTA_BOMBA_1, VENTA_BOMBA_2, VENTA_BOMBA_3, VENTA_BOMBA_4, \n"
                    + "                VENTA_BOMBA_5, VENTA_BOMBA_6, TOTAL_MEDIO_PAGO, CONTADO, PORC_CONTADO, \n"
                    + "                CREDOMATIC, PORC_CREDOMATIC, BANK_NACIONAL, PORC_BANK_NACIONAL, BCR, \n"
                    + "                PORC_BCR, FLEET_MAGIC_SB, PORC_FLEET_MAGIC, FM_DAVIVIENDA, PORC_FM_DAVIVIENDA, \n"
                    + "                VERSATEC, PORC_VERSATEC, FLOTA_BCR, PORC_FLOTA_BCR, FLOTA_BAC, \n"
                    + "                PORC_FLOTA_BAC, UNO_PLUS, PORC_UNO_PLUS, CUPON,PORC_CUPON, PREPAGO,PORC_PREPAGO, \n"
                    + "                DAVIVIENDA, PORC_DAVIVIENDA, CREDITO, PORC_CREDITO, CONTADO_USD, \n"
                    + "                PORC_CONTADO_USD, WS_L_SUPER, WS_L_REGULAR, WS_L_DIESEL, WS_L_TOTAL, WS_CL_SUPER, WS_CL_REGULAR,WS_CL_DIESEL,WS_CL_TOTAL,  \n"
                    + "                CALIBRACION_SUPER, CALIBRACION_REGULAR, CALIBRACION_DIESEL, CALIBRACION_TOTAL, SOBRANTE_L, \n"
                    + "                FALTANTE_L, SO_FAL_TOTAL, CXC_TOTAL, PORC_CXC_TOTAL, SUPER_L_FAL_SOB, \n"
                    + "                REGULAR_L_FAL_SOB, DIESEL_L_FAL_SOB, EFE_SUPER, EFE_REGULAR, EFE_DIESEL, \n"
                    + "                CREDOMATIC_SUPER, CREDOMATIC_REGULAR, CREDOMATIC_DIESEL, BANK_NACIONAL_SUPER, BANK_NACIONAL_REGULAR, \n"
                    + "                BANK_NACIONAL_DIESEL, BCR_SUPER, BCR_REGULAR, BCR_DIESEL, FLEET_MAGIC_SB_SUPER, \n"
                    + "                FLEET_MAGIC_SB_REGULAR, FLEET_MAGIC_SB_DIESEL,FM_DAVIVIENDA_SB_SUPER,FM_DAVIVIENDA_SB_REGULAR,FM_DAVIVIENDA_SB_DIESEL, VERSATEC_SUPER, VERSATEC_REGULAR, VERSATEC_DIESEL, \n"
                    + "                FLOTA_BCR_SUPER, FLOTA_BCR_REGULAR, FLOTA_BCR_DIESEL, FLOTA_BAC_SUPER, FLOTA_BAC_REGULAR, \n"
                    + "                FLOTA_BAC_DIESEL, UNO_PLUS_SUPER, UNO_PLUS_REGULAR, UNO_PLUS_DIESEL, CUPON_SUPER, \n"
                    + "                CUPON_REGULAR, CUPON_DIESEL, PREPAGO_SUPER, PREPAGO_REGULAR, PREPAGO_DIESEL, \n"
                    + "                CREDITO_SUPER, CREDITO_REGULAR, CREDITO_DIESEL, EFECTIVO_USD_SUPER, EFECTIVO_USD_REGULAR, \n"
                    + "                EFECTIVO_USD_DIESEL, DAVIVIENDA_SUPER, DAVIVIENDA_REGULAR, DAVIVIENDA_DIESEL "
                    + "from PV_MTD order by fecha asc";
//            System.out.println("QUERY "+query);
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                genmtd = new GenericMTD(rst.getDate(1),
                        rst.getDouble(2),
                        rst.getDouble(3),
                        rst.getDouble(4),
                        rst.getDouble(5),
                        rst.getDouble(6),
                        rst.getDouble(7),
                        rst.getDouble(8),
                        rst.getDouble(9),
                        rst.getDouble(10),
                        rst.getDouble(11),
                        rst.getDouble(12),
                        rst.getDouble(13),
                        rst.getDouble(14),
                        rst.getDouble(15),
                        rst.getDouble(16),
                        rst.getDouble(17),
                        rst.getDouble(18),
                        rst.getDouble(19),
                        rst.getDouble(20),
                        rst.getDouble(21),
                        rst.getDouble(22),
                        rst.getDouble(23),
                        rst.getDouble(24),
                        rst.getDouble(25),
                        rst.getDouble(26),
                        rst.getDouble(27),
                        rst.getDouble(28),
                        rst.getDouble(29),
                        rst.getDouble(30),
                        rst.getDouble(31),
                        rst.getDouble(32),
                        rst.getDouble(33),
                        rst.getDouble(34),
                        rst.getDouble(35),
                        rst.getDouble(36),
                        rst.getDouble(37),
                        rst.getDouble(38),
                        rst.getDouble(39),
                        rst.getDouble(40),
                        rst.getDouble(41),
                        rst.getDouble(42),
                        rst.getDouble(43),
                        rst.getDouble(44),
                        rst.getDouble(45),
                        rst.getDouble(46),
                        rst.getDouble(47),
                        rst.getDouble(48),
                        rst.getDouble(49),
                        rst.getDouble(50),
                        rst.getDouble(51),
                        rst.getDouble(52),
                        rst.getDouble(53),
                        rst.getDouble(54),
                        rst.getDouble(55),
                        rst.getDouble(56),
                        rst.getDouble(57),
                        rst.getDouble(58),
                        rst.getDouble(59),
                        rst.getDouble(60),
                        rst.getDouble(61),
                        rst.getDouble(62),
                        rst.getDouble(63),
                        rst.getDouble(64),
                        rst.getDouble(65),
                        rst.getDouble(66),
                        rst.getDouble(67),
                        rst.getDouble(68),
                        rst.getDouble(69),
                        rst.getDouble(70),
                        rst.getDouble(71),
                        rst.getDouble(72),
                        rst.getDouble(73),
                        rst.getDouble(74),
                        rst.getDouble(75),
                        rst.getDouble(76),
                        rst.getDouble(77),
                        rst.getDouble(78),
                        rst.getDouble(79),
                        rst.getDouble(80),
                        rst.getDouble(81),
                        rst.getDouble(82),
                        rst.getDouble(83),
                        rst.getDouble(84),
                        rst.getDouble(85),
                        rst.getDouble(86),
                        rst.getDouble(87),
                        rst.getDouble(88),
                        rst.getDouble(89),
                        rst.getDouble(90),
                        rst.getDouble(91),
                        rst.getDouble(92),
                        rst.getDouble(93),
                        rst.getDouble(94),
                        rst.getDouble(95),
                        rst.getDouble(96),
                        rst.getDouble(97),
                        rst.getDouble(98),
                        rst.getDouble(99),
                        rst.getDouble(100),
                        rst.getDouble(101),
                        rst.getDouble(102),
                        rst.getDouble(103),
                        rst.getDouble(104),
                        rst.getDouble(105),
                        rst.getDouble(106),
                        rst.getDouble(107),
                        rst.getDouble(108),
                        rst.getDouble(109),
                        rst.getDouble(110),
                        rst.getDouble(111),
                        rst.getDouble(112),
                        rst.getDouble(113),
                        rst.getDouble(114),
                        rst.getDouble(115),
                        rst.getDouble(116),
                        rst.getDouble(117),
                        rst.getDouble(118),
                        rst.getDouble(119),
                        rst.getDouble(120),
                        rst.getDouble(121),
                        rst.getDouble(122)
                );
                result.add(genmtd);
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

    public String getEstacion(Integer idestacion) {
        String result = "";
        query = "SELECT NOMBRE FROM ESTACION WHERE ESTACION_ID =" + idestacion;
        try {
            pst = getConnection().prepareStatement(query);
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result = rst.getString(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                pst.close();
            } catch (Exception ignore) {
            }
        }
        return result;
    }

    public void generar_data(Date fechaini, Date fechafin, String idestacion) throws SQLException {
        String query = "{call REPORT_MTD2 (?,?,?)}";
        CallableStatement cst = getConnection().prepareCall(query);

        java.sql.Date sqlDateIni = new java.sql.Date(fechaini.getTime());
        java.sql.Date sqlDateFin = new java.sql.Date(fechafin.getTime());

        /*Envio parametros necesarios*/
        cst.setDate(1, sqlDateIni);
        cst.setDate(2, sqlDateFin);
        cst.setString(3, idestacion);
        cst.execute();
    }

}
