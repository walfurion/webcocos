/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sisintegrados.generic.bean;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Allan G.
 */
@Data
@NoArgsConstructor

public class GenericMTD {
    private Date fecha;
    private Double p_super;
    private Double p_regular;
    private Double p_diesel;
    private Double l_super;
    private Double l_regular;
    private Double l_diesel;
    private Double c_diesel;
    private Double c_super;
    private Double c_regular;
    private Double l_total;
    private Double c_total;
    private Double tienda_total;
    private Double canasta_total;
    private Double medicamento_total;
    private Double otros_lub_total;
    private Double uno_lub_total;
    private Double venta_turno_a;
    private Double venta_turno_b;
    private Double venta_turno_c;
    private Double venta_bomba_1;
    private Double venta_bomba_2;
    private Double venta_bomba_3;
    private Double venta_bomba_4;
    private Double venta_bomba_5;
    private Double venta_bomba_6;
    private Double total_medio_pago;
    private Double contado;
    private Double porc_contado;
    private Double credomatic;
    private Double porc_credomatic;
    private Double bank_nacional;
    private Double porc_bank_nacional;
    private Double bcr;
    private Double porc_bcr;
    private Double fleet_magic_sb;
    private Double porc_fleet_magic;
    private Double fm_davivienda;
    private Double porc_fm_davivienda;
    private Double versatec;
    private Double porc_versatec;
    private Double flota_bcr;
    private Double porc_flota_bcr;
    private Double flota_bac;
    private Double porc_flota_bac;
    private Double uno_plus;
    private Double porc_uno_plus;
    private Double cupon;
    private Double porc_cupon;
    private Double prepago;
    private Double porc_prepago;
    private Double davivienda;
    private Double porc_davivienda;
    private Double credito;
    private Double porc_credito;
    private Double contado_usd;
    private Double porc_contado_usd;
    private Double ws_l_super;
    private Double ws_l_regular;
    private Double ws_l_diesel;
    private Double ws_l_total;
    private Double calibracion_super;
    private Double calibracion_regular;
    private Double calibracion_diesel;
    private Double calibracion_total;
    private Double sobrante_l;
    private Double faltante_l;
    private Double so_fal_total;
    private Double cxc_total;
    private Double porc_cxc_total;
    private Double super_l_fal_sob;
    private Double regular_l_fal_sob;
    private Double diesel_l_fal_sob;
    private Double efe_super;
    private Double efe_regular;
    private Double efe_diesel;
    private Double credomatic_super;
    private Double credomatic_regular;
    private Double credomatic_diesel;
    private Double bank_nacional_super;
    private Double bank_nacional_regular;
    private Double bank_nacional_diesel;
    private Double bcr_super;
    private Double bcr_regular;
    private Double bcr_diesel;
    private Double fleet_magic_sb_super;
    private Double fleet_magic_sb_regular;
    private Double fleet_magic_sb_diesel;
    private Double versatec_super;
    private Double versatec_regular;
    private Double versatec_diesel;
    private Double flota_bcr_super;
    private Double flota_bcr_regular;
    private Double flota_bcr_diesel;
    private Double flota_bac_super;
    private Double flota_bac_regular;
    private Double flota_bac_diesel;
    private Double uno_plus_super;
    private Double uno_plus_regular;
    private Double uno_plus_diesel;
    private Double cupon_super;
    private Double cupon_regular;
    private Double cupon_diesel;
    private Double prepago_super;
    private Double prepago_regular;
    private Double prepago_diesel;
    private Double credito_super;
    private Double credito_regular;
    private Double credito_diesel;
    private Double efectivo_usd_super;
    private Double efectivo_usd_regular;
    private Double efectivo_usd_diesel;
    private Double davivienda_super;
    private Double davivienda_regular;
    private Double davivienda_diesel;

    public GenericMTD(Date fecha, Double p_super, Double p_regular, Double p_diesel, Double l_super, Double l_regular, Double l_diesel, Double c_diesel, Double c_super, Double c_regular, Double l_total, Double c_total, Double tienda_total, Double canasta_total, Double medicamento_total, Double otros_lub_total, Double uno_lub_total, Double venta_turno_a, Double venta_turno_b, Double venta_turno_c, Double venta_bomba_1, Double venta_bomba_2, Double venta_bomba_3, Double venta_bomba_4, Double venta_bomba_5, Double venta_bomba_6, Double total_medio_pago, Double contado, Double porc_contado, Double credomatic, Double porc_credomatic, Double bank_nacional, Double porc_bank_nacional, Double bcr, Double porc_bcr, Double fleet_magic_sb, Double porc_fleet_magic, Double fm_davivienda, Double porc_fm_davivienda, Double versatec, Double porc_versatec, Double flota_bcr, Double porc_flota_bcr, Double flota_bac, Double porc_flota_bac, Double uno_plus, Double porc_uno_plus, Double cupon, Double porc_cupon, Double prepago, Double porc_prepago, Double davivienda, Double porc_davivienda, Double credito, Double porc_credito, Double contado_usd, Double porc_contado_usd, Double ws_l_super, Double ws_l_regular, Double ws_l_diesel, Double ws_l_total, Double calibracion_super, Double calibracion_regular, Double calibracion_diesel, Double calibracion_total, Double sobrante_l, Double faltante_l, Double so_fal_total, Double cxc_total, Double porc_cxc_total, Double super_l_fal_sob, Double regular_l_fal_sob, Double diesel_l_fal_sob, Double efe_super, Double efe_regular, Double efe_diesel, Double credomatic_super, Double credomatic_regular, Double credomatic_diesel, Double bank_nacional_super, Double bank_nacional_regular, Double bank_nacional_diesel, Double bcr_super, Double bcr_regular, Double bcr_diesel, Double fleet_magic_sb_super, Double fleet_magic_sb_regular, Double fleet_magic_sb_diesel, Double versatec_super, Double versatec_regular, Double versatec_diesel, Double flota_bcr_super, Double flota_bcr_regular, Double flota_bcr_diesel, Double flota_bac_super, Double flota_bac_regular, Double flota_bac_diesel, Double uno_plus_super, Double uno_plus_regular, Double uno_plus_diesel, Double cupon_super, Double cupon_regular, Double cupon_diesel, Double prepago_super, Double prepago_regular, Double prepago_diesel, Double credito_super, Double credito_regular, Double credito_diesel, Double efectivo_usd_super, Double efectivo_usd_regular, Double efectivo_usd_diesel, Double davivienda_super, Double davivienda_regular, Double davivienda_diesel) {
        this.fecha = fecha;
        this.p_super = p_super;
        this.p_regular = p_regular;
        this.p_diesel = p_diesel;
        this.l_super = l_super;
        this.l_regular = l_regular;
        this.l_diesel = l_diesel;
        this.c_diesel = c_diesel;
        this.c_super = c_super;
        this.c_regular = c_regular;
        this.l_total = l_total;
        this.c_total = c_total;
        this.tienda_total = tienda_total;
        this.canasta_total = canasta_total;
        this.medicamento_total = medicamento_total;
        this.otros_lub_total = otros_lub_total;
        this.uno_lub_total = uno_lub_total;
        this.venta_turno_a = venta_turno_a;
        this.venta_turno_b = venta_turno_b;
        this.venta_turno_c = venta_turno_c;
        this.venta_bomba_1 = venta_bomba_1;
        this.venta_bomba_2 = venta_bomba_2;
        this.venta_bomba_3 = venta_bomba_3;
        this.venta_bomba_4 = venta_bomba_4;
        this.venta_bomba_5 = venta_bomba_5;
        this.venta_bomba_6 = venta_bomba_6;
        this.total_medio_pago = total_medio_pago;
        this.contado = contado;
        this.porc_contado = porc_contado;
        this.credomatic = credomatic;
        this.porc_credomatic = porc_credomatic;
        this.bank_nacional = bank_nacional;
        this.porc_bank_nacional = porc_bank_nacional;
        this.bcr = bcr;
        this.porc_bcr = porc_bcr;
        this.fleet_magic_sb = fleet_magic_sb;
        this.porc_fleet_magic = porc_fleet_magic;
        this.fm_davivienda = fm_davivienda;
        this.porc_fm_davivienda = porc_fm_davivienda;
        this.versatec = versatec;
        this.porc_versatec = porc_versatec;
        this.flota_bcr = flota_bcr;
        this.porc_flota_bcr = porc_flota_bcr;
        this.flota_bac = flota_bac;
        this.porc_flota_bac = porc_flota_bac;
        this.uno_plus = uno_plus;
        this.porc_uno_plus = porc_uno_plus;
        this.cupon = cupon;
        this.porc_cupon = porc_cupon;
        this.prepago = prepago;
        this.porc_prepago = porc_prepago;
        this.davivienda = davivienda;
        this.porc_davivienda = porc_davivienda;
        this.credito = credito;
        this.porc_credito = porc_credito;
        this.contado_usd = contado_usd;
        this.porc_contado_usd = porc_contado_usd;
        this.ws_l_super = ws_l_super;
        this.ws_l_regular = ws_l_regular;
        this.ws_l_diesel = ws_l_diesel;
        this.ws_l_total = ws_l_total;
        this.calibracion_super = calibracion_super;
        this.calibracion_regular = calibracion_regular;
        this.calibracion_diesel = calibracion_diesel;
        this.calibracion_total = calibracion_total;
        this.sobrante_l = sobrante_l;
        this.faltante_l = faltante_l;
        this.so_fal_total = so_fal_total;
        this.cxc_total = cxc_total;
        this.porc_cxc_total = porc_cxc_total;
        this.super_l_fal_sob = super_l_fal_sob;
        this.regular_l_fal_sob = regular_l_fal_sob;
        this.diesel_l_fal_sob = diesel_l_fal_sob;
        this.efe_super = efe_super;
        this.efe_regular = efe_regular;
        this.efe_diesel = efe_diesel;
        this.credomatic_super = credomatic_super;
        this.credomatic_regular = credomatic_regular;
        this.credomatic_diesel = credomatic_diesel;
        this.bank_nacional_super = bank_nacional_super;
        this.bank_nacional_regular = bank_nacional_regular;
        this.bank_nacional_diesel = bank_nacional_diesel;
        this.bcr_super = bcr_super;
        this.bcr_regular = bcr_regular;
        this.bcr_diesel = bcr_diesel;
        this.fleet_magic_sb_super = fleet_magic_sb_super;
        this.fleet_magic_sb_regular = fleet_magic_sb_regular;
        this.fleet_magic_sb_diesel = fleet_magic_sb_diesel;
        this.versatec_super = versatec_super;
        this.versatec_regular = versatec_regular;
        this.versatec_diesel = versatec_diesel;
        this.flota_bcr_super = flota_bcr_super;
        this.flota_bcr_regular = flota_bcr_regular;
        this.flota_bcr_diesel = flota_bcr_diesel;
        this.flota_bac_super = flota_bac_super;
        this.flota_bac_regular = flota_bac_regular;
        this.flota_bac_diesel = flota_bac_diesel;
        this.uno_plus_super = uno_plus_super;
        this.uno_plus_regular = uno_plus_regular;
        this.uno_plus_diesel = uno_plus_diesel;
        this.cupon_super = cupon_super;
        this.cupon_regular = cupon_regular;
        this.cupon_diesel = cupon_diesel;
        this.prepago_super = prepago_super;
        this.prepago_regular = prepago_regular;
        this.prepago_diesel = prepago_diesel;
        this.credito_super = credito_super;
        this.credito_regular = credito_regular;
        this.credito_diesel = credito_diesel;
        this.efectivo_usd_super = efectivo_usd_super;
        this.efectivo_usd_regular = efectivo_usd_regular;
        this.efectivo_usd_diesel = efectivo_usd_diesel;
        this.davivienda_super = davivienda_super;
        this.davivienda_regular = davivienda_regular;
        this.davivienda_diesel = davivienda_diesel;
    }
}