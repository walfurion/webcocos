/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.utils;

import com.fundamental.model.Mediopago;
import com.fundamental.model.Producto;
import com.fundamental.model.dto.DtoArqueo;
import com.fundamental.model.dto.DtoEfectivo;
import com.sisintegrados.generic.bean.GenericMTD;
import com.sisintegrados.generic.bean.LitroCalibracion;
import com.sisintegrados.generic.bean.RepCuadrePistero;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Allan G.
 */
public class ExcelGenerator {

    CellRangeAddress cc;

    public XSSFWorkbook generar(Integer sheets, Integer nocols, List<String> colsname, String titulo1, String subtitulo1, String sheetname, BeanItemContainer<GenericMTD> lista, ArrayList<String> sheetsname) {
        XSSFWorkbook workbook = new XSSFWorkbook();
//        Valida que solo sea una hoja la que se va crear
        if (sheets == 1) {
            /*Se agrega el titulo a la hoja*/
            XSSFSheet sheet = workbook.createSheet(sheetname);
            /*Se setea el ancho de cada una de las columnas */

            for (int i = 0; i < nocols + 7; i++) {
                sheet.setColumnWidth(i, 5500);
            }
            /*APLICA SOLO PARA MTD*/
            sheet.setColumnWidth(0, 500);
            sheet.setColumnWidth(1, 500);
            sheet.setColumnWidth(2, 500);
            sheet.setColumnWidth(95, 500);
            sheet.setColumnWidth(96, 500);
            sheet.setColumnWidth(97, 500);
            sheet.setColumnWidth(98, 500);
            /*FIN APLICA SOLO PARA MTD*/

 /*Se crea la primer fila para el titulo*/
            XSSFRow header = sheet.createRow(0);
            /*Se adicionan lo estilos para los titulos*/
            XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            XSSFFont font = workbook.createFont();
            font.setFontName("Times New Roman");
            font.setFontHeightInPoints((short) 16);
            font.setBold(true);
            headerStyle.setFont(font);

            /*Se adicionan lo estilos para los Subtitulos*/
            XSSFCellStyle subheaderStyle = workbook.createCellStyle();
            subheaderStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            XSSFFont subfont = workbook.createFont();
            subfont.setFontName("Times New Roman");
            subfont.setFontHeightInPoints((short) 12);
            subfont.setBold(true);
            subheaderStyle.setFont(subfont);
            subheaderStyle.setAlignment(HorizontalAlignment.CENTER);
//            subheaderStyle.setBorderBottom(BorderStyle.MEDIUM);
//            subheaderStyle.setBorderTop(BorderStyle.MEDIUM);
//            subheaderStyle.setBorderRight(BorderStyle.MEDIUM);
//            subheaderStyle.setBorderLeft(BorderStyle.MEDIUM);

//            Se escribe el titulo principal
            XSSFCell headerCell = header.createCell(2); // Numero de columna en la que se creara el titulo
            headerCell.setCellValue(titulo1);
            headerCell.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtitulo = sheet.createRow(2);
            XSSFCell subtituloCell = subtitulo.createCell(2);
            subtituloCell.setCellValue(subtitulo1);
            subtituloCell.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinado = sheet.createRow(3);
//            int firstRow int lastRow int firstCol int lastCol
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));
            XSSFCell subcombinadoCell = subcombinado.createCell(2);
            subcombinadoCell.setCellValue("");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 4, 6));
            subcombinadoCell = subcombinado.createCell(4);
            subcombinadoCell.setCellValue("Precios Oficiales en Colones");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 7, 11));
            subcombinadoCell = subcombinado.createCell(7);
            subcombinadoCell.setCellValue("Ventas en Litros");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 12, 16));
            subcombinadoCell = subcombinado.createCell(12);
            subcombinadoCell.setCellValue("Ventas en Colones");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 17, 18));
            subcombinadoCell = subcombinado.createCell(17);
            subcombinadoCell.setCellValue("Total Ventas Tienda 13%");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 19, 20));
            subcombinadoCell = subcombinado.createCell(19);
            subcombinadoCell.setCellValue("Total Ventas Canasta Basica 0%");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 21, 22));
            subcombinadoCell = subcombinado.createCell(21);
            subcombinadoCell.setCellValue("Total Ventas Medicamentos 2%");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 23, 26));
            subcombinadoCell = subcombinado.createCell(23);
            subcombinadoCell.setCellValue("Ventas Lubricantes");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 27, 29));
            subcombinadoCell = subcombinado.createCell(27);
            subcombinadoCell.setCellValue("Ventas por Turno Colones");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 30, 35));
            subcombinadoCell = subcombinado.createCell(30);
            subcombinadoCell.setCellValue("Ventas por Bomba Litros");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 36, 38));
            subcombinadoCell = subcombinado.createCell(36);
            subcombinadoCell.setCellValue("Ingresos Segun Forma de Pago Colones");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 39, 40));
            subcombinadoCell = subcombinado.createCell(39);
            subcombinadoCell.setCellValue("Caja - Tarjetas");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 41, 42));
            subcombinadoCell = subcombinado.createCell(41);
            subcombinadoCell.setCellValue("Caja - T/C");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 43, 44));
            subcombinadoCell = subcombinado.createCell(43);
            subcombinadoCell.setCellValue("Caja - T/C");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 45, 46));
            subcombinadoCell = subcombinado.createCell(45);
            subcombinadoCell.setCellValue("Caja - T/C Fleet");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 47, 48));
            subcombinadoCell = subcombinado.createCell(47);
            subcombinadoCell.setCellValue("");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 49, 50));
            subcombinadoCell = subcombinado.createCell(49);
            subcombinadoCell.setCellValue("");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 51, 52));
            subcombinadoCell = subcombinado.createCell(51);
            subcombinadoCell.setCellValue("Caja - T/Flota");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 53, 54));
            subcombinadoCell = subcombinado.createCell(53);
            subcombinadoCell.setCellValue("");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 55, 56));
            subcombinadoCell = subcombinado.createCell(55);
            subcombinadoCell.setCellValue("");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 57, 58));
            subcombinadoCell = subcombinado.createCell(57);
            subcombinadoCell.setCellValue("");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 59, 60));
            subcombinadoCell = subcombinado.createCell(59);
            subcombinadoCell.setCellValue("");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 61, 62));
            subcombinadoCell = subcombinado.createCell(61);
            subcombinadoCell.setCellValue("");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 63, 64));
            subcombinadoCell = subcombinado.createCell(63);
            subcombinadoCell.setCellValue("");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 65, 66));
            subcombinadoCell = subcombinado.createCell(65);
            subcombinadoCell.setCellValue("");// HAST ACA 
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 67, 71));
            subcombinadoCell = subcombinado.createCell(67);
            subcombinadoCell.setCellValue("Inventario Final en Litros");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 72, 76));
            subcombinadoCell = subcombinado.createCell(72);
            subcombinadoCell.setCellValue("Compras de Combustible en Litros");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 77, 81));
            subcombinadoCell = subcombinado.createCell(77);
            subcombinadoCell.setCellValue("Calibraciones en Litros");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 82, 85));
            subcombinadoCell = subcombinado.createCell(82);
            subcombinadoCell.setCellValue("Faltante/Sobrante Colones");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 86, 87));
            subcombinadoCell = subcombinado.createCell(86);
            subcombinadoCell.setCellValue("CxC Empleados");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 88, 93));
            subcombinadoCell = subcombinado.createCell(88);
            subcombinadoCell.setCellValue("Faltante/Sobrante litros y el final en galones (Reporte vrs Tanques)");
            subcombinadoCell.setCellStyle(subheaderStyle);

//            sheet.addMergedRegion(new CellRangeAddress(3, 3, 94, 98));
            subcombinadoCell = subcombinado.createCell(94);
            subcombinadoCell.setCellValue("");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 99, 101));
            subcombinadoCell = subcombinado.createCell(99);
            subcombinadoCell.setCellValue("Caja - Efectivo");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 102, 104));
            subcombinadoCell = subcombinado.createCell(102);
            subcombinadoCell.setCellValue("Caja - Tarjetas Credomatic");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 105, 107));
            subcombinadoCell = subcombinado.createCell(105);
            subcombinadoCell.setCellValue("Caja - T/C Banco Nacional");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 108, 110));
            subcombinadoCell = subcombinado.createCell(108);
            subcombinadoCell.setCellValue("Caja - T/C BCR");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 111, 113));
            subcombinadoCell = subcombinado.createCell(111);
            subcombinadoCell.setCellValue("Caja - T/C Fleet Magic SB");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 114, 116));
            subcombinadoCell = subcombinado.createCell(114);
            subcombinadoCell.setCellValue("Caja - T/C FM Davivienda");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 117, 119));
            subcombinadoCell = subcombinado.createCell(117);
            subcombinadoCell.setCellValue("Caja - T/C Versatec");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 120, 122));
            subcombinadoCell = subcombinado.createCell(120);
            subcombinadoCell.setCellValue("Caja - T/Flota BCR");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 123, 125));
            subcombinadoCell = subcombinado.createCell(123);
            subcombinadoCell.setCellValue("Caja - T/Flota BAC");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 126, 128));
            subcombinadoCell = subcombinado.createCell(126);
            subcombinadoCell.setCellValue("Caja - T/Uno Plus");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 129, 131));
            subcombinadoCell = subcombinado.createCell(129);
            subcombinadoCell.setCellValue("Caja - Cupon");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 132, 134));
            subcombinadoCell = subcombinado.createCell(132);
            subcombinadoCell.setCellValue("Caja - Prepagos");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 135, 137));
            subcombinadoCell = subcombinado.createCell(135);
            subcombinadoCell.setCellValue("CxC - Clientes");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 138, 140));
            subcombinadoCell = subcombinado.createCell(138);
            subcombinadoCell.setCellValue("Caja - Efectivo USD");
            subcombinadoCell.setCellStyle(subheaderStyle);

            sheet.addMergedRegion(new CellRangeAddress(3, 3, 141, 143));
            subcombinadoCell = subcombinado.createCell(141);
            subcombinadoCell.setCellValue("Caja - T/C Davivienda");
            subcombinadoCell.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipal = sheet.createRow(4);
            XSSFCell subprincipalcell;
            Integer i;
            i = 3;
            for (String string : colsname) {
                subprincipalcell = subprincipal.createCell(i);
                subprincipalcell.setCellValue(string);
                subprincipalcell.setCellStyle(subheaderStyle);
                i++;
                if (string.equals("Contado en USD")) {
                    i = 99;
                }
            }

            /*Contenido*/
 /*Estilo celdas registros*/
            CreationHelper createHelper = workbook.getCreationHelper();
            /*Para Numero*/
            CellStyle styleNumber = workbook.createCellStyle();
            styleNumber.setDataFormat(createHelper.createDataFormat().getFormat("###,###,###,##0.00"));
            styleNumber.setWrapText(true);

            /*Para Fecha*/
            CellStyle styleFecha = workbook.createCellStyle();
            styleFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
            styleFecha.setWrapText(true);

            /*Para Porcentaje %*/
            CellStyle stylePorcentaje = workbook.createCellStyle();
            stylePorcentaje.setDataFormat(createHelper.createDataFormat().getFormat("#0.00%"));
            stylePorcentaje.setWrapText(true);

            XSSFRow datos;
            XSSFCell datoscell;
            Integer ii;
            Integer j = 5;
            ii = 3;
            Integer filas = lista.size();

            for (GenericMTD itemId : lista.getItemIds()) {
                datos = sheet.createRow(j);
                ii = 3;
                /*Fecha*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFecha());
                datoscell.setCellStyle(styleFecha);
                ii++;
                /*Precio*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getP_super());
                datoscell.setCellStyle(styleNumber);
                ii++;
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getP_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getP_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /* VENTAS EN LITROS*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getL_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getL_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getL_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getL_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*VENTAS EN COLONES*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getC_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getC_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getC_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getC_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*VENTAS TIENDA*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getTienda_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*VENTAS CANASTA BASICA*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCanasta_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*VENTAS MEDICAMENTOS*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getMedicamento_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*VENTAS LUBRICANTES*/
 /*OTROS LUBRICANTES*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getOtros_lub_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*LUBRICANTES UNO*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getUno_lub_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*VENTAS POR TURNOS COLONES*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVenta_turno_a());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVenta_turno_b());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVenta_turno_c());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*VENTAS POR BOMBAS LITROS*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVenta_bomba_1());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVenta_bomba_2());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVenta_bomba_3());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVenta_bomba_4());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVenta_bomba_5());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVenta_bomba_6());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*INGRESOS SEGUN FORMA DE PAGO*/
 /*GRAN TOTAL*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getTotal_medio_pago());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getContado());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_contado());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*CAJA TARJETAS CREDOMATIC*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCredomatic());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_credomatic());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*CAJA TARJETAS BANCO NACIONAL*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getBank_nacional());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_bank_nacional());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*CAJA TARJETAS BCR*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getBcr());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_bcr());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*CAJA TARJETAS FLEET MAGIC SB*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFleet_magic_sb());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_fleet_magic());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*FM DAVIVIENDA*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFm_davivienda());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_davivienda());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*VERSATEC*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVersatec());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_versatec());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*FLOTA BCR*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFlota_bcr());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_flota_bcr());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*FLOTA BAC*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFlota_bac());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_flota_bac());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*UNO PLUS*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getUno_plus());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_uno_plus());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*CUPONES*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCupon());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_cupon());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*CLIENTES PREPAGO*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPrepago());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_prepago());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*TARJETAS TC DAVIVIENDA*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getDavivienda());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_davivienda());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*CLIENTES CREDITO*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCredito());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_credito());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*CONTADO USD*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getContado_usd());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPorc_contado_usd());
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*Inventario Final en Litros*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getWs_l_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getWs_l_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getWs_l_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getWs_l_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*Compras de Combustibles en litros*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getWs_cl_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getWs_cl_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getWs_cl_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getWs_cl_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*Calibraciones en Litros*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCalibracion_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCalibracion_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCalibracion_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCalibracion_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*Faltantes y Sobrantes Colones*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getSobrante_l());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFaltante_l());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getSo_fal_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*CxC Empleados*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCxc_total());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*Faltante/Sobrante litros y el final en galones(Reporte vrs Tanque)*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getSuper_l_fal_sob());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getRegular_l_fal_sob());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getDiesel_l_fal_sob());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                /*CONTADO EN USD*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getEfe_usd_monto());
                datoscell.setCellStyle(styleNumber);
                ii++;

                if (ii == 95) {
                    ii = 99;
                }
                /*CAJA EFECTIVO*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getEfe_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getEfe_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getEfe_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA TARJETAS CREDOMATIC*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCredomatic_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCredomatic_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCredomatic_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA TC BANCO NACIONAL*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getBank_nacional_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getBank_nacional_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getBank_nacional_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA TC BCR*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getBcr_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getBcr_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getBcr_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA TC FLEET MAGIC SB*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFleet_magic_sb_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFleet_magic_sb_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFleet_magic_sb_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA TC FM DAVIVIENDA*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFm_davivienda_sb_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFm_davivienda_sb_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFm_davivienda_sb_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA TC VERSATEC*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVersatec_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVersatec_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getVersatec_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA T FLOTA BCR*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFlota_bcr_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFlota_bcr_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFlota_bcr_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA T FLOTA BAC*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFlota_bac_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFlota_bac_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFlota_bac_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA T UNO PLUS*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getUno_plus_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getUno_plus_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getUno_plus_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA CUPON*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCupon_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCupon_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCupon_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA PREPAGOS*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPrepago_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPrepago_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getPrepago_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA CREDITO*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCredito_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCredito_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getCredito_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA EFECTIVO USD*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getEfectivo_usd_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getEfectivo_usd_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getEfectivo_usd_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*CAJA TC DAVIVIENDA*/
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getDavivienda_super());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getDavivienda_regular());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getDavivienda_diesel());
                datoscell.setCellStyle(styleNumber);
                ii++;

                /*Al Final*/
                j++;
            }

        } else {
            //Cuando se desee crear mas de una Hoja se debe leer el Array de Hojas y crear 1 a 1
        }
        return workbook;
    }

    public XSSFWorkbook generarCuadrePistero(Integer sheets, String titulo1, String sheetname, RepCuadrePistero lista) {
        XSSFWorkbook workbook = new XSSFWorkbook();
//        Valida que solo sea una hoja la que se va crear
        if (sheets == 1) {
            /*Se agrega el titulo a la hoja*/
            XSSFSheet sheet = workbook.createSheet(sheetname);
            /*Se setea el ancho de cada una de las columnas */

            sheet.setColumnWidth(0, 1000);
            sheet.setColumnWidth(1, 1000);
            sheet.setColumnWidth(2, 5000);
            sheet.setColumnWidth(3, 5000);
            sheet.setColumnWidth(4, 5000);
            sheet.setColumnWidth(5, 5000);
            sheet.setColumnWidth(6, 8000);
            sheet.setColumnWidth(7, 8000);
            sheet.setColumnWidth(8, 5000);

            /*Se adicionan lo estilos para los titulos*/
            XSSFCellStyle headerStyle = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(16);
            font.setBold(true);
            font.setColor(IndexedColors.BLACK.getIndex());
            headerStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerStyle.setBorderTop(BorderStyle.MEDIUM);
            headerStyle.setBorderRight(BorderStyle.MEDIUM);
            headerStyle.setBorderLeft(BorderStyle.MEDIUM);
            headerStyle.setFont(font);

            /*Se adicionan lo estilos para los Subtitulos*/
            XSSFCellStyle subheaderStyle = workbook.createCellStyle();
            XSSFFont subfont = workbook.createFont();
            subfont.setColor(IndexedColors.BLACK.getIndex());
            subfont.setFontHeight(12);
            subfont.setBold(true);
            subheaderStyle.setBorderBottom(BorderStyle.MEDIUM);
            subheaderStyle.setBorderTop(BorderStyle.MEDIUM);
            subheaderStyle.setBorderRight(BorderStyle.MEDIUM);
            subheaderStyle.setBorderLeft(BorderStyle.MEDIUM);
            subheaderStyle.setFont(subfont);
            subheaderStyle.setAlignment(HorizontalAlignment.CENTER);

            /*Se crea la primer fila para el titulo*/
            XSSFRow header = sheet.createRow(1);
//            Se escribe el titulo principal ESTACION
            setMerge(sheet, 1, 1, 2, 4, true);
            XSSFCell headerCell = header.getCell(2);
            headerCell.setCellValue(titulo1);
            headerCell.setCellStyle(headerStyle);
            /*Bomba*/
            setMerge(sheet, 1, 1, 5, 8, true);
            XSSFCell headerCell2 = header.getCell(5);
            headerCell2.setCellValue(lista.getBomba());
            headerCell2.setCellStyle(headerStyle);

            /*Cajero, Fecha Turno*/
            XSSFRow header2 = sheet.createRow(2);
            setMerge(sheet, 2, 2, 2, 4, true);
            XSSFCell header2Cell = header2.getCell(2);
            header2Cell.setCellValue("CAJERO: " + lista.getEmpleado());
            header2Cell.setCellStyle(headerStyle);

            setMerge(sheet, 2, 2, 5, 6, true);
            XSSFCell header2Cell2 = header2.getCell(5);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String fechaTexto = formatter.format(lista.getFecha());
            header2Cell2.setCellValue("FECHA: " + fechaTexto);
            header2Cell2.setCellStyle(headerStyle);

            setMerge(sheet, 2, 2, 7, 8, true);
            XSSFCell header2Cell3 = header2.getCell(7);
            header2Cell3.setCellValue(lista.getTurno());
            header2Cell3.setCellStyle(headerStyle);

            /*DETALLE CONTROL LECTURAS MECANICAS*/
            XSSFRow header3 = sheet.createRow(3);
            setMerge(sheet, 3, 3, 2, 8, true);
            XSSFCell header3Cell = header3.getCell(2);
            header3Cell.setCellValue("DETALLE CONTROL LECTURAS MECANICAS");
            header3Cell.setCellStyle(headerStyle);

            /*Se adicionan lo estilos para los titulos lecturas Verticales*/
            XSSFCellStyle headerStyleLecturaV = workbook.createCellStyle();
            XSSFFont fontLecturaV = workbook.createFont();
            fontLecturaV.setFontHeight(14);
            fontLecturaV.setBold(true);
            fontLecturaV.setColor(IndexedColors.BLACK.getIndex());
            headerStyleLecturaV.setBorderBottom(BorderStyle.MEDIUM);
            headerStyleLecturaV.setBorderTop(BorderStyle.MEDIUM);
            headerStyleLecturaV.setBorderRight(BorderStyle.MEDIUM);
            headerStyleLecturaV.setBorderLeft(BorderStyle.MEDIUM);
            headerStyleLecturaV.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyleLecturaV.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            headerStyleLecturaV.setFont(font);
            headerStyleLecturaV.setRotation((short) 90);

            /*Se adicionan lo estilos para los titulos lecturas */
            XSSFCellStyle headerStyleLecturaT = workbook.createCellStyle();
            XSSFFont fontLecturaT = workbook.createFont();
            fontLecturaT.setFontHeight(14);
            fontLecturaT.setBold(true);
            fontLecturaT.setColor(IndexedColors.BLACK.getIndex());
            headerStyleLecturaT.setBorderBottom(BorderStyle.MEDIUM);
            headerStyleLecturaT.setBorderTop(BorderStyle.MEDIUM);
            headerStyleLecturaT.setBorderRight(BorderStyle.MEDIUM);
            headerStyleLecturaT.setBorderLeft(BorderStyle.MEDIUM);
            headerStyleLecturaT.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyleLecturaT.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            headerStyleLecturaT.setFont(font);
            headerStyleLecturaT.setWrapText(true);
            headerStyleLecturaT.setAlignment(HorizontalAlignment.CENTER);
            headerStyleLecturaT.setVerticalAlignment(VerticalAlignment.CENTER);

            /*Se adicionan lo estilos para las lineas manuales */
            XSSFCellStyle headerStyleLectura = workbook.createCellStyle();
            XSSFFont fontLectura = workbook.createFont();
            fontLectura.setFontHeight(14);
            fontLectura.setBold(true);
            fontLectura.setColor(IndexedColors.BLACK.getIndex());
            headerStyleLectura.setBorderBottom(BorderStyle.MEDIUM);
            headerStyleLectura.setBorderTop(BorderStyle.MEDIUM);
            headerStyleLectura.setBorderRight(BorderStyle.MEDIUM);
            headerStyleLectura.setBorderLeft(BorderStyle.MEDIUM);
            headerStyleLectura.setFont(font);
            headerStyleLectura.setWrapText(true);

            /*Contenido*/
 /*Estilo celdas registros*/
            CreationHelper createHelper = workbook.getCreationHelper();
            /*Se adiciona para formatos*/
            XSSFCellStyle subheaderFormatStyle = workbook.createCellStyle();
            XSSFFont subfontFormatStyle = workbook.createFont();
            subfontFormatStyle.setColor(IndexedColors.BLACK.getIndex());
            subfontFormatStyle.setFontHeight(12);
            subfontFormatStyle.setBold(true);
            subheaderFormatStyle.setBorderBottom(BorderStyle.MEDIUM);
            subheaderFormatStyle.setBorderTop(BorderStyle.MEDIUM);
            subheaderFormatStyle.setBorderRight(BorderStyle.MEDIUM);
            subheaderFormatStyle.setBorderLeft(BorderStyle.MEDIUM);
            subheaderFormatStyle.setDataFormat(createHelper.createDataFormat().getFormat("###,###,###,##0.00"));
            subheaderFormatStyle.setFont(subfont);
            subheaderFormatStyle.setAlignment(HorizontalAlignment.CENTER);


            /*Para Numero*/
            CellStyle styleNumber = workbook.createCellStyle();
            styleNumber.setDataFormat(createHelper.createDataFormat().getFormat("###,###,###,##0.00"));
            styleNumber.setWrapText(true);

            /*Para Fecha*/
            CellStyle styleFecha = workbook.createCellStyle();
            styleFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
            styleFecha.setWrapText(true);

            /*Para textos*/
            CellStyle styleString = workbook.createCellStyle();
            styleString.setWrapText(true);

            XSSFRow header4 = sheet.createRow(4);
//            setMerge(sheet, 3, 3, 2, 8);
            XSSFCell header4Cell = header4.createCell(1);
            header4Cell.setCellValue("SURTIDOR");
            header4Cell.setCellStyle(headerStyleLecturaV);

            XSSFCell header4Cell2 = header4.createCell(2);
            header4Cell2.setCellValue("LECTURA INICIAL");
            header4Cell2.setCellStyle(headerStyleLecturaT);

            XSSFCell header4Cell3 = header4.createCell(3);
            header4Cell3.setCellValue("LECTURA FINAL");
            header4Cell3.setCellStyle(headerStyleLecturaT);

            XSSFCell header4Cell4 = header4.createCell(4);
            header4Cell4.setCellValue("VENTA LITROS");
            header4Cell4.setCellStyle(headerStyleLecturaT);

            XSSFCell header4Cell5 = header4.createCell(5);
            header4Cell5.setCellValue("PRECIO");
            header4Cell5.setCellStyle(headerStyleLecturaT);

            XSSFCell header4Cell6 = header4.createCell(6);
            header4Cell6.setCellValue("VENTA COLONES");
            header4Cell6.setCellStyle(headerStyleLecturaT);

            /*CARA 1*/
            XSSFRow header5 = sheet.createRow(5);
//            setMerge(sheet, 3, 3, 2, 8);
            XSSFCell header5Cell = header5.createCell(1);
            header5Cell.setCellValue("D1");
            header5Cell.setCellStyle(headerStyleLecturaV);

            XSSFCell header5Cell2 = header5.createCell(2);
            header5Cell2.setCellValue("");
            header5Cell2.setCellStyle(headerStyleLectura);

            XSSFCell header5Cell3 = header5.createCell(3);
            header5Cell3.setCellValue("");
            header5Cell3.setCellStyle(headerStyleLectura);

            XSSFCell header5Cell4 = header5.createCell(4);
            header5Cell4.setCellValue("");
            header5Cell4.setCellStyle(headerStyleLectura);

            XSSFCell header5Cell5 = header5.createCell(5);
            header5Cell5.setCellValue("");
            header5Cell5.setCellStyle(headerStyleLectura);

            XSSFCell header5Cell6 = header5.createCell(6);
            header5Cell6.setCellValue("₡");
            header5Cell6.setCellStyle(headerStyleLectura);

            XSSFRow header6 = sheet.createRow(6);
//            setMerge(sheet, 3, 3, 2, 8);
            XSSFCell header6Cell = header6.createCell(1);
            header6Cell.setCellValue("R1");
            header6Cell.setCellStyle(headerStyleLecturaV);

            XSSFCell header6Cell2 = header6.createCell(2);
            header6Cell2.setCellValue("");
            header6Cell2.setCellStyle(headerStyleLectura);

            XSSFCell header6Cell3 = header6.createCell(3);
            header6Cell3.setCellValue("");
            header6Cell3.setCellStyle(headerStyleLectura);

            XSSFCell header6Cell4 = header6.createCell(4);
            header6Cell4.setCellValue("");
            header6Cell4.setCellStyle(headerStyleLectura);

            XSSFCell header6Cell5 = header6.createCell(5);
            header6Cell5.setCellValue("");
            header6Cell5.setCellStyle(headerStyleLectura);

            XSSFCell header6Cell6 = header6.createCell(6);
            header6Cell6.setCellValue("₡");
            header6Cell6.setCellStyle(headerStyleLectura);

            XSSFRow header7 = sheet.createRow(7);
//            setMerge(sheet, 3, 3, 2, 8);
            XSSFCell header7Cell = header7.createCell(1);
            header7Cell.setCellValue("S1");
            header7Cell.setCellStyle(headerStyleLecturaV);

            XSSFCell header7Cell2 = header7.createCell(2);
            header7Cell2.setCellValue("");
            header7Cell2.setCellStyle(headerStyleLectura);

            XSSFCell header7Cell3 = header7.createCell(3);
            header7Cell3.setCellValue("");
            header7Cell3.setCellStyle(headerStyleLectura);

            XSSFCell header7Cell4 = header7.createCell(4);
            header7Cell4.setCellValue("");
            header7Cell4.setCellStyle(headerStyleLectura);

            XSSFCell header7Cell5 = header7.createCell(5);
            header7Cell5.setCellValue("");
            header7Cell5.setCellStyle(headerStyleLectura);

            XSSFCell header7Cell6 = header7.createCell(6);
            header7Cell6.setCellValue("₡");
            header7Cell6.setCellStyle(headerStyleLectura);

            /*CARA 2*/
            XSSFRow header8 = sheet.createRow(8);
//            setMerge(sheet, 3, 3, 2, 8);
            XSSFCell header8Cell = header8.createCell(1);
            header8Cell.setCellValue("D2");
            header8Cell.setCellStyle(headerStyleLecturaV);

            XSSFCell header8Cell2 = header8.createCell(2);
            header8Cell2.setCellValue("");
            header8Cell2.setCellStyle(headerStyleLectura);

            XSSFCell header8Cell3 = header8.createCell(3);
            header8Cell3.setCellValue("");
            header8Cell3.setCellStyle(headerStyleLectura);

            XSSFCell header8Cell4 = header8.createCell(4);
            header8Cell4.setCellValue("");
            header8Cell4.setCellStyle(headerStyleLectura);

            XSSFCell header8Cell5 = header8.createCell(5);
            header8Cell5.setCellValue("");
            header8Cell5.setCellStyle(headerStyleLectura);

            XSSFCell header8Cell6 = header8.createCell(6);
            header8Cell6.setCellValue("₡");
            header8Cell6.setCellStyle(headerStyleLectura);

            XSSFRow header9 = sheet.createRow(9);
//            setMerge(sheet, 3, 3, 2, 8);
            XSSFCell header9Cell = header9.createCell(1);
            header9Cell.setCellValue("R2");
            header9Cell.setCellStyle(headerStyleLecturaV);

            XSSFCell header9Cell2 = header9.createCell(2);
            header9Cell2.setCellValue("");
            header9Cell2.setCellStyle(headerStyleLectura);

            XSSFCell header9Cell3 = header9.createCell(3);
            header9Cell3.setCellValue("");
            header9Cell3.setCellStyle(headerStyleLectura);

            XSSFCell header9Cell4 = header9.createCell(4);
            header9Cell4.setCellValue("");
            header9Cell4.setCellStyle(headerStyleLectura);

            XSSFCell header9Cell5 = header9.createCell(5);
            header9Cell5.setCellValue("");
            header9Cell5.setCellStyle(headerStyleLectura);

            XSSFCell header9Cell6 = header9.createCell(6);
            header9Cell6.setCellValue("₡");
            header9Cell6.setCellStyle(headerStyleLectura);

            XSSFRow header10 = sheet.createRow(10);
//            setMerge(sheet, 3, 3, 2, 8);
            XSSFCell header10Cell = header10.createCell(1);
            header10Cell.setCellValue("S2");
            header10Cell.setCellStyle(headerStyleLecturaV);

            XSSFCell header10Cell2 = header10.createCell(2);
            header10Cell2.setCellValue("");
            header10Cell2.setCellStyle(headerStyleLectura);

            XSSFCell header10Cell3 = header10.createCell(3);
            header10Cell3.setCellValue("");
            header10Cell3.setCellStyle(headerStyleLectura);

            XSSFCell header10Cell4 = header10.createCell(4);
            header10Cell4.setCellValue("");
            header10Cell4.setCellStyle(headerStyleLectura);

            XSSFCell header10Cell5 = header10.createCell(5);
            header10Cell5.setCellValue("");
            header10Cell5.setCellStyle(headerStyleLectura);

            XSSFCell header10Cell6 = header10.createCell(6);
            header10Cell6.setCellValue("₡");
            header10Cell6.setCellStyle(headerStyleLectura);

            XSSFRow totalvnta = sheet.createRow(11);
            XSSFCell totalvntaCell = totalvnta.createCell(6);
            totalvntaCell.setCellValue("₡");
            totalvntaCell.setCellStyle(headerStyleLectura);

            /*DETALLE DEPOSITOS*/
            XSSFRow detDepo = sheet.createRow(13);
            XSSFCell detDepoCel = detDepo.createCell(6);
            setMerge(sheet, 13, 13, 6, 8, true);
            detDepoCel.setCellValue("DETALLE DEPOSITOS");
            detDepoCel.setCellStyle(headerStyleLectura);

            XSSFRow detDepo1 = sheet.createRow(14);
            XSSFCell detDepoCel1 = detDepo1.createCell(6);
            detDepoCel1.setCellValue("ENTREGA");
            detDepoCel1.setCellStyle(headerStyleLectura);

            XSSFCell detDepoCel2 = detDepo1.createCell(7);
            detDepoCel2.setCellValue("RECIBIDO Y HORA");
            detDepoCel2.setCellStyle(headerStyleLectura);

            XSSFCell detDepoCel3 = detDepo1.createCell(8);
            detDepoCel3.setCellValue("MONTO");
            detDepoCel3.setCellStyle(headerStyleLectura);

            /*VENTAS COMBUSTIBLES*/
            XSSFRow ventaComb = sheet.createRow(15);
            setMerge(sheet, 15, 15, 1, 3, true);
            XSSFCell ventaCombCel = ventaComb.getCell(1);
            ventaCombCel.setCellValue("VENTA COMBUSTIBLES");
            ventaCombCel.setCellStyle(subheaderStyle);
            //ventaCombCel.setCellStyle(styleNumber);

            /*Arqueos Manuales*/
            XSSFCell ventaCombCel1 = ventaComb.createCell(4);
            BeanContainer<Integer, DtoArqueo> arqueo;
            arqueo = lista.getArqueo();
            Double totalVentaComb = 0.00;
            for (Integer itemId : arqueo.getItemIds()) {
                totalVentaComb += (Double) arqueo.getItem(itemId).getItemProperty("venta").getValue();
            }
            ventaCombCel1.setCellValue(totalVentaComb);
            ventaCombCel1.setCellStyle(subheaderFormatStyle);

            //aca
            XSSFCell filaMedioCel21 = ventaComb.createCell(6);
            filaMedioCel21.setCellValue("");
            filaMedioCel21.setCellStyle(subheaderStyle);

            XSSFCell filaMedioCel31 = ventaComb.createCell(7);
            filaMedioCel31.setCellValue("");
            filaMedioCel31.setCellStyle(subheaderStyle);

            XSSFCell filaMedioCel41 = ventaComb.createCell(8);
            filaMedioCel41.setCellValue("");
            filaMedioCel41.setCellStyle(subheaderStyle);

            BeanContainer<Integer, Mediopago> medioPago;
            medioPago = lista.getMediopago();

            /*Medio Pago*/
            int ii = 16;
            /*Total Medios PAgo*/
            Double totalMedioPago = 0.00;
            for (Integer itemId : medioPago.getItemIds()) {
//                if (((Integer) medioPago.getItem(itemId).getItemProperty("mediopagoId").getValue() != 6) || ((Integer) medioPago.getItem(itemId).getItemProperty("mediopagoId").getValue()!=117) || ((Integer) medioPago.getItem(itemId).getItemProperty("mediopagoId").getValue()!=108) || ((Integer) medioPago.getItem(itemId).getItemProperty("mediopagoId").getValue()!=109)) {
                if ((Integer) medioPago.getItem(itemId).getItemProperty("mediopagoId").getValue() != 6 && (Integer) medioPago.getItem(itemId).getItemProperty("mediopagoId").getValue() != 117 && (Integer) medioPago.getItem(itemId).getItemProperty("mediopagoId").getValue() != 108 && (Integer) medioPago.getItem(itemId).getItemProperty("mediopagoId").getValue() != 109) {
                    XSSFRow filaMedio = sheet.createRow(ii);
                    setMerge(sheet, ii, ii, 1, 3, true);
                    XSSFCell filaMedioCel = filaMedio.getCell(1);
                    filaMedioCel.setCellValue(medioPago.getItem(itemId).getItemProperty("nombre").getValue().toString());
                    filaMedioCel.setCellStyle(subheaderStyle);

                    XSSFCell filaMedioCel1 = filaMedio.createCell(4);
                    filaMedioCel1.setCellValue((Double) medioPago.getItem(itemId).getItemProperty("value").getValue());
                    totalMedioPago += (Double) medioPago.getItem(itemId).getItemProperty("value").getValue();//SUMA TOTAL MEDIOPAGO
                    filaMedioCel1.setCellStyle(subheaderFormatStyle);

                    XSSFCell filaMedioCel2 = filaMedio.createCell(6);
                    filaMedioCel2.setCellValue("");
                    filaMedioCel2.setCellStyle(subheaderStyle);

                    XSSFCell filaMedioCel3 = filaMedio.createCell(7);
                    filaMedioCel3.setCellValue("");
                    filaMedioCel3.setCellStyle(subheaderStyle);

                    XSSFCell filaMedioCel4 = filaMedio.createCell(8);
                    filaMedioCel4.setCellValue("");
                    filaMedioCel4.setCellStyle(subheaderStyle);
                    ii++;
                }
            }
            /*Calibraciones total colones*/
            XSSFRow calibColon = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 1, 3, true);
            XSSFCell calibColonCel = calibColon.getCell(1);
            calibColonCel.setCellValue("Seraphines y Dev");
            calibColonCel.setCellStyle(subheaderStyle);

            ArrayList<LitroCalibracion> calibraciones = new ArrayList<LitroCalibracion>();
            calibraciones = lista.getCalibracion();
            Double colonCalibracion = 0.00;
            for (LitroCalibracion calibracione : calibraciones) {
                colonCalibracion += calibracione.getVentaColones();
            }

            XSSFCell calibColonCel1 = calibColon.createCell(4);
            calibColonCel1.setCellValue(colonCalibracion);
            calibColonCel1.setCellStyle(subheaderStyle);
            calibColonCel1.setCellStyle(subheaderFormatStyle);

            XSSFCell filaMedioCel2 = calibColon.createCell(6);
            filaMedioCel2.setCellValue("");
            filaMedioCel2.setCellStyle(subheaderStyle);

            XSSFCell filaMedioCel3 = calibColon.createCell(7);
            filaMedioCel3.setCellValue("");
            filaMedioCel3.setCellStyle(subheaderStyle);

            XSSFCell filaMedioCel4 = calibColon.createCell(8);
            filaMedioCel4.setCellValue("");
            filaMedioCel4.setCellStyle(subheaderStyle);

            ii++;
            /*MAS ACEITES O FRESCOS */
            XSSFRow prodAdic = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 1, 3, true);
            XSSFCell prodAdicCel = prodAdic.getCell(1);
            prodAdicCel.setCellValue("Aceites o Frescos");
            prodAdicCel.setCellStyle(subheaderStyle);

            Double totalOtrosProd = 0.00;
            BeanContainer<Integer, Producto> proAdicional;
            proAdicional = lista.getProdadicionales();
            for (Integer itemId : proAdicional.getItemIds()) {
                totalOtrosProd += (Double) proAdicional.getItem(itemId).getItemProperty("value").getValue();
            }

            XSSFCell prodAdicCel1 = prodAdic.createCell(4);
            prodAdicCel1.setCellValue(totalOtrosProd);
            prodAdicCel1.setCellStyle(subheaderStyle);
            prodAdicCel1.setCellStyle(subheaderFormatStyle);

            XSSFCell filaMedioCel22 = prodAdic.createCell(6);
            filaMedioCel22.setCellValue("");
            filaMedioCel22.setCellStyle(subheaderStyle);

            XSSFCell filaMedioCel33 = prodAdic.createCell(7);
            filaMedioCel33.setCellValue("");
            filaMedioCel33.setCellStyle(subheaderStyle);

            XSSFCell filaMedioCel44 = prodAdic.createCell(8);
            filaMedioCel44.setCellValue("");
            filaMedioCel44.setCellStyle(subheaderStyle);

            ii++;
            /*TOTAL EFECTIVO*/
            BeanContainer<Integer, DtoEfectivo> efectivo;
            efectivo = lista.getEfectivo();
            Double totalEfectivo = 0.00;
            for (Integer itemId : efectivo.getItemIds()) {
                totalEfectivo += (Double) efectivo.getItem(itemId).getItemProperty("value").getValue();
            }

            /*TOTAL A ENTREGAR*/
            XSSFRow totEntregar = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 1, 3, true);
            XSSFCell totEntregarCel = totEntregar.getCell(1);
            totEntregarCel.setCellValue("EFECTIVO ENTREGADO");
            totEntregarCel.setCellStyle(subheaderStyle);

            Double totalEntregar = 0.00;
//            totalEntregar = (totalVentaComb + totalOtrosProd) - (colonCalibracion) - (totalMedioPago);
            totalEntregar = totalEfectivo;
            XSSFCell totEntregarCel1 = totEntregar.createCell(4);
            totEntregarCel1.setCellValue(totalEntregar);
            totEntregarCel1.setCellStyle(subheaderStyle);
            totEntregarCel1.setCellStyle(subheaderFormatStyle);

            XSSFCell filaMedioCel222 = totEntregar.createCell(6);
            filaMedioCel222.setCellValue("");
            filaMedioCel222.setCellStyle(subheaderStyle);

            XSSFCell filaMedioCel333 = totEntregar.createCell(7);
            filaMedioCel333.setCellValue("");
            filaMedioCel333.setCellStyle(subheaderStyle);

            XSSFCell filaMedioCel444 = totEntregar.createCell(8);
            filaMedioCel444.setCellValue("");
            filaMedioCel444.setCellStyle(subheaderStyle);

            ii++;
            /*TOTAL ENTREGADO*/
            XSSFRow totEntregado = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 1, 3, true);
            XSSFCell totEntregadoCel = totEntregado.getCell(1);
            totEntregadoCel.setCellValue("TOTAL ENTREGADO");
            totEntregadoCel.setCellStyle(subheaderStyle);

            Double totalEntregado = 0.00;
//            totalEntregado = totalMedioPago + totalEfectivo + totalOtrosProd; //ASG PARA CORRECION
            totalEntregado = totalMedioPago + totalEfectivo;
            XSSFCell totEntregadoCel1 = totEntregado.createCell(4);
            totEntregadoCel1.setCellValue(totalEntregado);
            totEntregadoCel1.setCellStyle(subheaderStyle);
            totEntregadoCel1.setCellStyle(subheaderFormatStyle);

            XSSFCell filaMedioCel2222 = totEntregado.createCell(6);
            filaMedioCel2222.setCellValue("");
            filaMedioCel2222.setCellStyle(subheaderStyle);

            XSSFCell filaMedioCel3333 = totEntregado.createCell(7);
            filaMedioCel3333.setCellValue("");
            filaMedioCel3333.setCellStyle(subheaderStyle);

            XSSFCell filaMedioCel4444 = totEntregado.createCell(8);
            filaMedioCel4444.setCellValue("");
            filaMedioCel4444.setCellStyle(subheaderStyle);

            ii++;
            /*SOBRANTE FALTANTE*/
            XSSFRow falsob = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 1, 3, true);
            XSSFCell falsobCel = falsob.getCell(1);
            falsobCel.setCellValue("SOBRANTE/FALTANTE");
            falsobCel.setCellStyle(subheaderStyle);

            Double faltantesob = 0.00;
//            faltantesob = totalEntregado - totalVentaComb; //ASG CUADRE PISTERO
            faltantesob = totalEntregado - totalVentaComb - totalOtrosProd; 
            XSSFCell falsobCel1 = falsob.createCell(4);
            falsobCel1.setCellValue(faltantesob);
            falsobCel1.setCellStyle(subheaderStyle);
            falsobCel1.setCellStyle(subheaderFormatStyle);

            /*TOTAL DETALLE DEPOSITOS*/
            XSSFCell totalColonDet = falsob.createCell(6);
            totalColonDet.setCellValue("TOTAL DEPOSITOS");
            totalColonDet.setCellStyle(subheaderStyle);
            setMerge(sheet, ii, ii, 6, 7, true);

            XSSFCell totalColonDet1 = falsob.createCell(7);
            totalColonDet1.setCellValue("");
            totalColonDet1.setCellStyle(subheaderStyle);

            ii++;
            ii++;

            /*DEPOSITOS PREPAGOS*/
            XSSFRow deppre = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 1, 4, true);
            XSSFCell deppreCel = deppre.getCell(1);
            deppreCel.setCellValue("DEPOSITOS (PREPAGOS)");
            deppreCel.setCellStyle(subheaderStyle);

            ii++;

            /*CLIENTE Y MONTO*/
            XSSFRow cliepre = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 1, 3, true);
            XSSFCell cliepreCel = cliepre.getCell(1);
            cliepreCel.setCellValue("CLIENTE");
            cliepreCel.setCellStyle(subheaderStyle);

            XSSFCell cliepreCel1 = cliepre.createCell(4);
            cliepreCel1.setCellValue("MONTO");
            cliepreCel1.setCellStyle(subheaderStyle);

            ii++;

            /*LINEAS VACIAS*/
            XSSFRow clielin = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 1, 3, true);
            XSSFCell clielinCel = clielin.getCell(1);
            clielinCel.setCellValue("");
            clielinCel.setCellStyle(subheaderStyle);

            XSSFCell clielinCel1 = clielin.createCell(4);
            clielinCel1.setCellValue("");
            clielinCel1.setCellStyle(subheaderStyle);

            ii++;

            XSSFRow clielin1 = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 1, 3, true);
            XSSFCell clielin1Cel = clielin1.getCell(1);
            clielin1Cel.setCellValue("");
            clielin1Cel.setCellStyle(subheaderStyle);

            XSSFCell clielin1Cel1 = clielin1.createCell(4);
            clielin1Cel1.setCellValue("");
            clielin1Cel1.setCellStyle(subheaderStyle);

            ii++;
            ii++;

            /*SERAPHINES Y DEVOLUCIONES*/
            XSSFRow seradev = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 1, 4, true);
            XSSFCell seradevCel = seradev.getCell(1);
            seradevCel.setCellValue("SERAPHINES Y DEVOLUCIONES");
            seradevCel.setCellStyle(subheaderStyle);

            ii++;

            /*SERAPHINES Y DEVOLUCIONES TITULOS*/
            XSSFRow seratit = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 0, 1, true);
            XSSFCell seratitCel = seratit.getCell(0);
            seratitCel.setCellValue("HORA");
            seratitCel.setCellStyle(subheaderStyle);

            XSSFCell seratitCel1 = seratit.createCell(2);
            seratitCel1.setCellValue("TIPO COMBUSTIBLE");
            seratitCel1.setCellStyle(subheaderStyle);

            XSSFCell seratitCel2 = seratit.createCell(3);
            seratitCel2.setCellValue("LITROS");
            seratitCel2.setCellStyle(subheaderStyle);

            XSSFCell seratitCel3 = seratit.createCell(4);
            seratitCel3.setCellValue("COLONES");
            seratitCel3.setCellStyle(subheaderStyle);
            ii++;

            for (LitroCalibracion calibracione : calibraciones) {
                XSSFRow seracont = sheet.createRow(ii);
                setMerge(sheet, ii, ii, 0, 1, true);
                XSSFCell seracontCel = seracont.getCell(0);
                seracontCel.setCellValue("");
                seracontCel.setCellStyle(subheaderStyle);

                XSSFCell seracontCel1 = seracont.createCell(2);
                seracontCel1.setCellValue(calibracione.getNombre());
                seracontCel1.setCellStyle(subheaderStyle);

                XSSFCell seracontCel2 = seracont.createCell(3);
                seracontCel2.setCellValue(calibracione.getLitro());
                seracontCel2.setCellStyle(subheaderStyle);

                XSSFCell seracontCel3 = seracont.createCell(4);
                seracontCel3.setCellValue(calibracione.getVentaColones());
                seracontCel3.setCellStyle(subheaderStyle);
                seracontCel3.setCellStyle(subheaderFormatStyle);
                ii++;
            }

            /*TOTALES SERAPHINES Y DEVOLUCIONES*/
            XSSFRow seratot = sheet.createRow(ii);
            XSSFCell seratotCel = seratot.createCell(3);
            seratotCel.setCellValue("TOTALES");
            seratotCel.setCellStyle(subheaderStyle);

            XSSFCell seratotCel1 = seratot.createCell(4);
            seratotCel1.setCellValue(colonCalibracion);
            seratotCel1.setCellStyle(subheaderStyle);
            seratotCel1.setCellStyle(subheaderFormatStyle);

            ii++;
            ii++;

            /*OBSERVACIONES*/
            XSSFRow observaciones = sheet.createRow(ii);
            setMerge(sheet, ii, ii, 1, 3, true);
            XSSFCell observacionesCel = observaciones.getCell(1);
            observacionesCel.setCellValue("OBSERVACIONES :");
            observacionesCel.setCellStyle(subheaderStyle);

            setMerge(sheet, ii, ii + 3, 4, 8, true);

            ii = ii + 4;
            ii++;

            /*Estilos cuadro final*/
            XSSFCellStyle bottomLine = crearStilo(workbook, true, false, false, false, true, false, true, null, 0, false, false, false, "");
//            fontLecturaV.setColor(IndexedColors.BLACK.getIndex());

            /*CUADRO FINAL*/
            XSSFRow obserT = sheet.createRow(ii);
            sheet.addMergedRegion(new CellRangeAddress(ii, ii, 1, 2));

            XSSFCell obserTCel = obserT.createCell(3);
            obserTCel.setCellValue("RECIBO CANTIDAD");
            sheet.addMergedRegion(new CellRangeAddress(ii, ii, 3, 4));

            XSSFCell obserTCel1 = obserT.createCell(5);
            obserTCel1.setCellValue("ENTREGO CANTIDAD");
            sheet.addMergedRegion(new CellRangeAddress(ii, ii, 5, 6));

            ii++;

            XSSFRow obser = sheet.createRow(ii);
            XSSFCell obserCel = obser.createCell(1);
            obserCel.setCellValue("1-                       ");
            cc = new CellRangeAddress(ii, ii, 1, 2);
            sheet.addMergedRegion(cc);
//            setBorders(sheet, cc, false, false, false, true, 4);
            cc = new CellRangeAddress(ii, ii, 3, 4);
            sheet.addMergedRegion(cc);
            setBorders(sheet, cc, false, false, false, true, 4);
            cc = new CellRangeAddress(ii, ii, 5, 6);
            sheet.addMergedRegion(cc);
            setBorders(sheet, cc, false, false, false, true, 4);

            ii++;

            XSSFRow obser1 = sheet.createRow(ii);
            XSSFCell obser1Cel = obser1.createCell(1);
            obser1Cel.setCellValue("2-                       ");
            cc = new CellRangeAddress(ii, ii, 1, 2);
            sheet.addMergedRegion(cc);
//            setBorders(sheet, cc, false, false, false, true, 4);
            cc = new CellRangeAddress(ii, ii, 3, 4);
            sheet.addMergedRegion(cc);
            setBorders(sheet, cc, false, false, false, true, 4);
            cc = new CellRangeAddress(ii, ii, 5, 6);
            sheet.addMergedRegion(cc);
            setBorders(sheet, cc, false, false, false, true, 4);

            ii++;

            XSSFRow obser2 = sheet.createRow(ii);
            XSSFCell obser2Cel = obser2.createCell(1);
            obser2Cel.setCellValue("3-                       ");
            cc = new CellRangeAddress(ii, ii, 1, 2);
            sheet.addMergedRegion(cc);
//            setBorders(sheet, cc, false, false, false, true, 4);
            cc = new CellRangeAddress(ii, ii, 3, 4);
            sheet.addMergedRegion(cc);
            setBorders(sheet, cc, false, false, false, true, 4);
            cc = new CellRangeAddress(ii, ii, 5, 6);
            sheet.addMergedRegion(cc);
            setBorders(sheet, cc, false, false, false, true, 4);

            ii++;

            XSSFRow obser3 = sheet.createRow(ii);
            XSSFCell obser3Cel = obser3.createCell(1);
            obser3Cel.setCellValue("4-                       ");
            cc = new CellRangeAddress(ii, ii, 1, 2);
            sheet.addMergedRegion(cc);
//            setBorders(sheet, cc, false, false, false, true, 4);
            cc = new CellRangeAddress(ii, ii, 3, 4);
            sheet.addMergedRegion(cc);
            setBorders(sheet, cc, false, false, false, true, 4);
            cc = new CellRangeAddress(ii, ii, 5, 6);
            sheet.addMergedRegion(cc);
            setBorders(sheet, cc, false, false, false, true, 4);

            ii = ii + 2;

            /*FIRMAS*/
            XSSFRow firmas = sheet.createRow(ii);
            XSSFCell firmasCel = firmas.createCell(1);
            firmasCel.setCellValue("Firmo como recibido de los articulos arriba anotados los cuales se encuentran en buen estado y funcionando.");
            sheet.addMergedRegion(new CellRangeAddress(ii, ii, 1, 6));
            ii = ii + 3;

            cc = new CellRangeAddress(ii, ii, 1, 5);
            sheet.addMergedRegion(cc);
            setBorders(sheet, cc, false, false, false, true, 3);

            cc = new CellRangeAddress(ii, ii, 7, 12);
            sheet.addMergedRegion(cc);
            setBorders(sheet, cc, false, false, false, true, 3);

            ii++;

            XSSFRow firmas1 = sheet.createRow(ii);
            XSSFCell firmas1Cel = firmas1.createCell(1);
            firmas1Cel.setCellValue("Nombre y firma de persona que entrega:");
            sheet.addMergedRegion(new CellRangeAddress(ii, ii, 1, 6));

            XSSFCell firmas1Cel1 = firmas1.createCell(7);
            firmas1Cel1.setCellValue("Nombre y firma de persona que recibe:");
            sheet.addMergedRegion(new CellRangeAddress(ii, ii, 7, 12));

////            obserCel.setCellStyle(subheaderStyle);            
//            /*Contenido*/
// /*Estilo celdas registros*/
//            CreationHelper createHelper = workbook.getCreationHelper();
//            /*Para Numero*/
//            CellStyle styleNumber = workbook.createCellStyle();
////            styleNumber.setDataFormat(createHelper.createDataFormat().getFormat("#########.####"));
//            styleNumber.setWrapText(true);
//
//            /*Para Fecha*/
//            CellStyle styleFecha = workbook.createCellStyle();
//            styleFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
//            styleFecha.setWrapText(true);
//
//            /*Para Porcentaje %*/
//            CellStyle stylePorcentaje = workbook.createCellStyle();
//            stylePorcentaje.setDataFormat(createHelper.createDataFormat().getFormat("##.##%"));
//            stylePorcentaje.setWrapText(true);
        }
        return workbook;
    }

    private XSSFCellStyle crearStilo(XSSFWorkbook workbook, boolean border, boolean leftb, boolean rigthb, boolean topb, boolean bottomb, boolean font, boolean negrita,
            XSSFColor colorF, Integer sizeF, boolean vertical, boolean ajustar, boolean fondo, String fontTitle) {
        XSSFCellStyle estilo = workbook.createCellStyle();
        XSSFFont fontStilo = workbook.createFont();

        if (border) {
            if (bottomb) {
                estilo.setBorderBottom(BorderStyle.MEDIUM);
            }
            if (topb) {
                estilo.setBorderTop(BorderStyle.MEDIUM);
            }
            if (rigthb) {
                estilo.setBorderRight(BorderStyle.MEDIUM);
            }
            if (leftb) {
                estilo.setBorderLeft(BorderStyle.MEDIUM);
            }
        }

        /*Asigna Fuente Personalizada*/
        if (font) {
            fontStilo.setFontHeight(sizeF);
            fontStilo.setBold(negrita);
//        fontStilo.setColor(IndexedColors.BLACK.getIndex());
            fontStilo.setColor(colorF);
            fontStilo.setFontName(fontTitle);
        }
        /*Ajustar texto*/
        estilo.setWrapText(ajustar);

        /*Fondo del texto*/
        if (fondo) {
            estilo.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            estilo.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            estilo.setFont(fontStilo);
        }
        /*Orientacion del texto*/
        if (vertical) {
            estilo.setRotation((short) 90);
        }
        return estilo;
    }

    protected void setMerge(XSSFSheet sheet, int numRow, int untilRow, int numCol, int untilCol, boolean border) {
        CellRangeAddress cellMerge = new CellRangeAddress(numRow, untilRow, numCol, untilCol);
        sheet.addMergedRegion(cellMerge);
        if (border) {
            setBordersToMergedCells(sheet, cellMerge);
        }
    }

    protected void setBorders(XSSFSheet sheet, CellRangeAddress rangeAddress, boolean leftb, boolean rigthb, boolean topb, boolean bottomb, int tipo) {
        if (tipo == 1) {
            if (bottomb) {
                RegionUtil.setBorderBottom(CellStyle.BORDER_HAIR, rangeAddress, sheet);
            }
            if (topb) {
                RegionUtil.setBorderTop(CellStyle.BORDER_HAIR, rangeAddress, sheet);
            }
            if (rigthb) {
                RegionUtil.setBorderRight(CellStyle.BORDER_HAIR, rangeAddress, sheet);
            }
            if (leftb) {
                RegionUtil.setBorderLeft(CellStyle.BORDER_HAIR, rangeAddress, sheet);
            }
        }
        if (tipo == 2) {
            if (bottomb) {
                RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, rangeAddress, sheet);
            }
            if (topb) {
                RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, rangeAddress, sheet);
            }
            if (rigthb) {
                RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, rangeAddress, sheet);
            }
            if (leftb) {
                RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, rangeAddress, sheet);
            }
        }
        if (tipo == 3) {
            if (bottomb) {
                RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, rangeAddress, sheet);
            }
            if (topb) {
                RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, rangeAddress, sheet);
            }
            if (rigthb) {
                RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, rangeAddress, sheet);
            }
            if (leftb) {
                RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, rangeAddress, sheet);
            }
        }
        if (tipo == 4) {
            if (bottomb) {
                RegionUtil.setBorderBottom(CellStyle.BORDER_DASH_DOT, rangeAddress, sheet);
            }
            if (topb) {
                RegionUtil.setBorderTop(CellStyle.BORDER_DASH_DOT, rangeAddress, sheet);
            }
            if (rigthb) {
                RegionUtil.setBorderRight(CellStyle.BORDER_DASH_DOT, rangeAddress, sheet);
            }
            if (leftb) {
                RegionUtil.setBorderLeft(CellStyle.BORDER_DASH_DOT, rangeAddress, sheet);
            }
        }
    }

    protected void setBordersToMergedCells(XSSFSheet sheet, CellRangeAddress rangeAddress) {
        RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, rangeAddress, sheet);
        RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, rangeAddress, sheet);
    }
}
