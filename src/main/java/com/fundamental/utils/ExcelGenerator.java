/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.utils;

import com.sisintegrados.generic.bean.GenericMTD;
import com.vaadin.data.util.BeanItemContainer;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Allan G.
 */
public class ExcelGenerator {

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
            styleNumber.setDataFormat(createHelper.createDataFormat().getFormat("#########.####"));
            styleNumber.setWrapText(true);

            /*Para Fecha*/
            CellStyle styleFecha = workbook.createCellStyle();
            styleFecha.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
            styleFecha.setWrapText(true);

            /*Para Porcentaje %*/
            CellStyle stylePorcentaje = workbook.createCellStyle();
            stylePorcentaje.setDataFormat(createHelper.createDataFormat().getFormat("##.##%"));
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

                /*CLIENTES CREDITO*/
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
                datoscell.setCellValue(itemId.getRegular_l_fal_sob());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(stylePorcentaje);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(0);
                datoscell.setCellStyle(styleNumber);
                ii++;

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
                datoscell.setCellValue(itemId.getCredito_regular());
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

    private void setBordersToMergedCells(XSSFSheet sheet) {
        int numMerged = sheet.getNumMergedRegions();
        for (int i = 0; i < numMerged; i++) {
            CellRangeAddress mergedRegions = sheet.getMergedRegion(i);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, mergedRegions, sheet);
            RegionUtil.setBorderRight(CellStyle.BORDER_THIN, mergedRegions, sheet);
            RegionUtil.setBorderTop(CellStyle.BORDER_THIN, mergedRegions, sheet);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, mergedRegions, sheet);
        }
    }
}
