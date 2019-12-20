/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.utils;

import com.sisintegrados.generic.bean.GenericMTD;
import com.sisintegrados.generic.bean.GenericRprControlMediosPago;
import com.vaadin.data.util.BeanItemContainer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
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
 * @author Mery
 */
public class ExcelGeneratorCrlMediosPago {

    public XSSFWorkbook generar(Integer sheets, Integer nocols, List<String> colsname, String titulo1, String subtitulo1, String sheetname, BeanItemContainer<GenericRprControlMediosPago> lista, ArrayList<String> sheetsname) {
        XSSFWorkbook workbook = new XSSFWorkbook();
//        Valida que solo sea una hoja la que se va crear
        if (sheets == 1) {
            /*Se agrega el titulo a la hoja*/
            XSSFSheet sheet = workbook.createSheet(sheetname);
            /*Se setea el ancho de cada una de las columnas */
            for (int i = 0; i < nocols + 7; i++) {
                sheet.setColumnWidth(i, 5500);
            }


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
            XSSFRow subcombinado = sheet.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCell = subcombinado.createCell(0);
            subcombinadoCell.setCellValue("DETALLE DE VENTAS EN LA ESTACION");
            subcombinadoCell.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipal = sheet.createRow(6);
            XSSFCell subprincipalcell;
            Integer i;
            i = 0;
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

            /*Para textos*/
            CellStyle styleString = workbook.createCellStyle();
            styleString.setWrapText(true);

            XSSFRow datos;
            XSSFCell datoscell;
            Integer ii; //COLUMNAS
            Integer j = 7; //FILAS
            ii = 0;
            Integer filas = lista.size();

            for (GenericRprControlMediosPago itemId : lista.getItemIds()) {
                datos = sheet.createRow(j);
                ii = 0;
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getFecha());
                datoscell.setCellStyle(styleFecha);
                ii++;
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getLote());
                datoscell.setCellStyle(styleNumber);
                ii++;
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getMonto_bruto());
                datoscell.setCellStyle(styleNumber);
                ii++;
                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getComision());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getMonto_neto());
                datoscell.setCellStyle(styleNumber);
                ii++;

                datoscell = datos.createCell(ii);
                datoscell.setCellValue(itemId.getComentarios());
                datoscell.setCellStyle(styleString);
                ii++;

                /*Al Final*/
                j++;
            }

        } else {
            /*Se agrega el titulo a la hoja*/
            XSSFSheet sheetVERSATEC = null;
            XSSFSheet sheetTC_FLOTA_BCR = null;
            XSSFSheet sheetTARJETA_BANCO_NACIONAL = null;
            XSSFSheet sheetTARJETA_CREDOMATIC = null;
            XSSFSheet sheetFLEET_MAGIC_DAVIVIENDA = null;
            XSSFSheet sheetTARJETA_FLEET_MAGIC_SB = null;
            XSSFSheet sheetTARJETA_BCR = null;
            XSSFSheet sheetTC_FLOTA_BAC = null;
            XSSFSheet sheetUNO_PLUS = null;
            XSSFSheet sheetTC_DAVIVIENDA = null;

            for (String titulohoja : sheetsname) {
                if (titulohoja.equals("VERSATEC")) {
                    sheetVERSATEC = workbook.createSheet(titulohoja);
                }
                if (titulohoja.equals("TC FLOTA BCR")) {
                    sheetTC_FLOTA_BCR = workbook.createSheet(titulohoja);
                }
                if (titulohoja.equals("TARJETA BANCO NACIONAL")) {
                    sheetTARJETA_BANCO_NACIONAL = workbook.createSheet(titulohoja);
                }
                if (titulohoja.equals("TARJETA CREDOMATIC")) {
                    sheetTARJETA_CREDOMATIC = workbook.createSheet(titulohoja);
                }
                if (titulohoja.equals("FLEET MAGIC DAVIVIENDA")) {
                    sheetFLEET_MAGIC_DAVIVIENDA = workbook.createSheet(titulohoja);
                }
                if (titulohoja.equals("TARJETA FLEET MAGIC SB")) {
                    sheetTARJETA_FLEET_MAGIC_SB = workbook.createSheet(titulohoja);
                }
                if (titulohoja.equals("TARJETA BCR")) {
                    sheetTARJETA_BCR = workbook.createSheet(titulohoja);
                }
                if (titulohoja.equals("TC FLOTA BAC")) {
                    sheetTC_FLOTA_BAC = workbook.createSheet(titulohoja);
                }
                if (titulohoja.equals("UNO PLUS")) {
                    sheetUNO_PLUS = workbook.createSheet(titulohoja);
                }
                if (titulohoja.equals("TC DAVIVIENDA")) {
                    sheetTC_DAVIVIENDA = workbook.createSheet(titulohoja);
                }
            }

            //VERSATEC
            /*Se setea el ancho de cada una de las columnas */
            for (int v = 0; v < nocols + 7; v++) {
                sheetVERSATEC.setColumnWidth(v, 5500);
            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow headerv = sheetVERSATEC.createRow(0);
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
            XSSFCell headerCellv = headerv.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellv.setCellValue("VERSATEC");
            headerCellv.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtitulov = sheetVERSATEC.createRow(2);
            XSSFCell subtituloCellv = subtitulov.createCell(2);
            subtituloCellv.setCellValue(subtitulo1);
            subtituloCellv.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadov = sheetVERSATEC.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetVERSATEC.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellv = subcombinadov.createCell(0);
            subcombinadoCellv.setCellValue("DETALLE DE VENTAS EN LA ESTACION");
            subcombinadoCellv.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalv = sheetVERSATEC.createRow(6);
            XSSFCell subprincipalcellv;
            Integer v;
            v = 0;
            for (String string : colsname) {
                subprincipalcellv = subprincipalv.createCell(v);
                subprincipalcellv.setCellValue(string);
                subprincipalcellv.setCellStyle(subheaderStyle);
                v++;

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

            /*Para textos*/
            CellStyle styleString = workbook.createCellStyle();
            styleString.setWrapText(true);

            //TC FLOTA BCR
//            
            /*Se setea el ancho de cada una de las columnas */
            for (int a = 0; a < nocols + 7; a++) {
                sheetTC_FLOTA_BCR.setColumnWidth(a, 5500);
            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow header2 = sheetTC_FLOTA_BCR.createRow(0);

//            Se escribe el titulo principal
            XSSFCell headerCellFB = header2.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellFB.setCellValue("TC FLOTA BCR");
            headerCellFB.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtituloFB = sheetTC_FLOTA_BCR.createRow(2);
            XSSFCell subtituloCellFB = subtituloFB.createCell(2);
            subtituloCellFB.setCellValue(subtitulo1);
            subtituloCellFB.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadoFB = sheetTC_FLOTA_BCR.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetTC_FLOTA_BCR.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellFB = subcombinadoFB.createCell(0);
            subcombinadoCellFB.setCellValue("DETALLE DE VENTAS EN LA ESTACION");
            subcombinadoCellFB.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalFB = sheetTC_FLOTA_BCR.createRow(6);
            XSSFCell subprincipalcellFB;
            Integer a;
            a = 0;
            for (String string : colsname) {
                subprincipalcellFB = subprincipalFB.createCell(a);
                subprincipalcellFB.setCellValue(string);
                subprincipalcellFB.setCellStyle(subheaderStyle);
                a++;

            }

//            
//            //TARJETA BANCO NACIONAL
//            
            /*Se setea el ancho de cada una de las columnas */
            for (int b = 0; b < nocols + 7; b++) {
                sheetTARJETA_BANCO_NACIONAL.setColumnWidth(b, 5500);
            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow header3 = sheetTARJETA_BANCO_NACIONAL.createRow(0);

//            Se escribe el titulo principal
            XSSFCell headerCellBN = header3.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellBN.setCellValue("TARJETA BANCO NACIONAL");
            headerCellBN.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtituloBN = sheetTARJETA_BANCO_NACIONAL.createRow(2);
            XSSFCell subtituloCellBN = subtituloBN.createCell(2);
            subtituloCellBN.setCellValue(subtitulo1);
            subtituloCellBN.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadoBN = sheetTARJETA_BANCO_NACIONAL.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetTARJETA_BANCO_NACIONAL.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellBN = subcombinadoBN.createCell(0);
            subcombinadoCellBN.setCellValue("DETALLE DE VENTAS EN LA ESTACION");
            subcombinadoCellBN.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalBN = sheetTARJETA_BANCO_NACIONAL.createRow(6);
            XSSFCell subprincipalcellBN;
            Integer b;
            b = 0;
            for (String string : colsname) {
                subprincipalcellBN = subprincipalBN.createCell(b);
                subprincipalcellBN.setCellValue(string);
                subprincipalcellBN.setCellStyle(subheaderStyle);
                b++;

            }
//            
//            //TARJETA CREDOMATIC
//            
            /*Se setea el ancho de cada una de las columnas */
            for (int c = 0; c < nocols + 7; c++) {
                sheetTARJETA_CREDOMATIC.setColumnWidth(c, 5500);
            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow header4 = sheetTARJETA_CREDOMATIC.createRow(0);

//            Se escribe el titulo principal
            XSSFCell headerCellCA = header4.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellCA.setCellValue("TARJETA CREDOMATIC");
            headerCellCA.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtituloCA = sheetTARJETA_CREDOMATIC.createRow(2);
            XSSFCell subtituloCellCA = subtituloCA.createCell(2);
            subtituloCellCA.setCellValue(subtitulo1);
            subtituloCellCA.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadoCA = sheetTARJETA_CREDOMATIC.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetTARJETA_CREDOMATIC.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellCA = subcombinadoCA.createCell(0);
            subcombinadoCellCA.setCellValue("DETALLE DE VENTAS EN LA ESTACION");
            subcombinadoCellCA.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalCA = sheetTARJETA_CREDOMATIC.createRow(6);
            XSSFCell subprincipalcellCA;
            Integer c;
            c = 0;
            for (String string : colsname) {
                subprincipalcellCA = subprincipalCA.createCell(c);
                subprincipalcellCA.setCellValue(string);
                subprincipalcellCA.setCellStyle(subheaderStyle);
                c++;

            }
//            
//            //FLEET MAGIC DAVIVIENDA
//            
            /*Se setea el ancho de cada una de las columnas */
            for (int d = 0; d < nocols + 7; d++) {
                sheetFLEET_MAGIC_DAVIVIENDA.setColumnWidth(d, 5500);
            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow header5 = sheetFLEET_MAGIC_DAVIVIENDA.createRow(0);

//            Se escribe el titulo principal
            XSSFCell headerCellFMD = header5.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellFMD.setCellValue("FLEET MAGIC DAVIVIENDA");
            headerCellFMD.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtituloFMD = sheetFLEET_MAGIC_DAVIVIENDA.createRow(2);
            XSSFCell subtituloCellFMD = subtituloFMD.createCell(2);
            subtituloCellFMD.setCellValue(subtitulo1);
            subtituloCellFMD.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadoFMD = sheetFLEET_MAGIC_DAVIVIENDA.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetFLEET_MAGIC_DAVIVIENDA.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellFMD = subcombinadoFMD.createCell(0);
            subcombinadoCellFMD.setCellValue("DETALLE DE VENTAS EN LA ESTACION");
            subcombinadoCellFMD.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalFMD = sheetFLEET_MAGIC_DAVIVIENDA.createRow(6);
            XSSFCell subprincipalcellFMD;
            Integer d;
            d = 0;
            for (String string : colsname) {
                subprincipalcellFMD = subprincipalFMD.createCell(d);
                subprincipalcellFMD.setCellValue(string);
                subprincipalcellFMD.setCellStyle(subheaderStyle);
                d++;

            }
//            
//            //TARJETA FLEET MAGIC SB
//            
            /*Se setea el ancho de cada una de las columnas */
            for (int e = 0; e < nocols + 7; e++) {
                sheetTARJETA_FLEET_MAGIC_SB.setColumnWidth(e, 5500);
            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow header6 = sheetTARJETA_FLEET_MAGIC_SB.createRow(0);

//            Se escribe el titulo principal
            XSSFCell headerCellFMS = header6.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellFMS.setCellValue("TARJETA FLEET MAGIC SB");
            headerCellFMS.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtituloFMS = sheetTARJETA_FLEET_MAGIC_SB.createRow(2);
            XSSFCell subtituloCellFMS = subtituloFMS.createCell(2);
            subtituloCellFMS.setCellValue(subtitulo1);
            subtituloCellFMS.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadoFMS = sheetTARJETA_FLEET_MAGIC_SB.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetTARJETA_FLEET_MAGIC_SB.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellFMS = subcombinadoFMS.createCell(0);
            subcombinadoCellFMS.setCellValue("DETALLE DE VENTAS EN LA ESTACION");
            subcombinadoCellFMS.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalFMS = sheetTARJETA_FLEET_MAGIC_SB.createRow(6);
            XSSFCell subprincipalcellFMS;
            Integer e;
            e = 0;
            for (String string : colsname) {
                subprincipalcellFMS = subprincipalFMS.createCell(e);
                subprincipalcellFMS.setCellValue(string);
                subprincipalcellFMS.setCellStyle(subheaderStyle);
                e++;

            }
//         
//            //TARJETA BCR
//            
            /*Se setea el ancho de cada una de las columnas */
            for (int f = 0; f < nocols + 7; f++) {
                sheetTARJETA_BCR.setColumnWidth(f, 5500);
            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow header7 = sheetTARJETA_BCR.createRow(0);

//            Se escribe el titulo principal
            XSSFCell headerCellBCR = header7.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellBCR.setCellValue("TARJETA BCR");
            headerCellBCR.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtituloBCR = sheetTARJETA_BCR.createRow(2);
            XSSFCell subtituloCellBCR = subtituloBCR.createCell(2);
            subtituloCellBCR.setCellValue(subtitulo1);
            subtituloCellBCR.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadoBCR = sheetTARJETA_BCR.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetTARJETA_BCR.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellBCR = subcombinadoBCR.createCell(0);
            subcombinadoCellBCR.setCellValue("DETALLE DE VENTAS EN LA ESTACION");
            subcombinadoCellBCR.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalBCR = sheetTARJETA_BCR.createRow(6);
            XSSFCell subprincipalcellBCR;
            Integer f;
            f = 0;
            for (String string : colsname) {
                subprincipalcellBCR = subprincipalBCR.createCell(f);
                subprincipalcellBCR.setCellValue(string);
                subprincipalcellBCR.setCellStyle(subheaderStyle);
                f++;

            }
//            
//            //TC FLOTA BAC
//            
            /*Se setea el ancho de cada una de las columnas */
            for (int g = 0; g < nocols + 7; g++) {
                sheetTC_FLOTA_BAC.setColumnWidth(g, 5500);
            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow header8 = sheetTC_FLOTA_BAC.createRow(0);

//            Se escribe el titulo principal
            XSSFCell headerCellTFB = header8.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellTFB.setCellValue("TC FLOTA BAC");
            headerCellTFB.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtituloTFB = sheetTC_FLOTA_BAC.createRow(2);
            XSSFCell subtituloCellTFB = subtituloTFB.createCell(2);
            subtituloCellTFB.setCellValue(subtitulo1);
            subtituloCellTFB.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadoTFB = sheetTC_FLOTA_BAC.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetTC_FLOTA_BAC.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellTFB = subcombinadoTFB.createCell(0);
            subcombinadoCellTFB.setCellValue("DETALLE DE VENTAS EN LA ESTACION");
            subcombinadoCellTFB.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalTFB = sheetTC_FLOTA_BAC.createRow(6);
            XSSFCell subprincipalcellTFB;
            Integer g;
            g = 0;
            for (String string : colsname) {
                subprincipalcellTFB = subprincipalTFB.createCell(g);
                subprincipalcellTFB.setCellValue(string);
                subprincipalcellTFB.setCellStyle(subheaderStyle);
                g++;

            }
//            
//            //UNO PLUS
//            
            /*Se setea el ancho de cada una de las columnas */
            for (int h = 0; h < nocols + 7; h++) {
                sheetUNO_PLUS.setColumnWidth(h, 5500);
            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow header9 = sheetUNO_PLUS.createRow(0);

//            Se escribe el titulo principal
            XSSFCell headerCellUP = header9.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellUP.setCellValue("UNO PLUS");
            headerCellUP.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtituloUP = sheetUNO_PLUS.createRow(2);
            XSSFCell subtituloCellUP = subtituloUP.createCell(2);
            subtituloCellUP.setCellValue(subtitulo1);
            subtituloCellUP.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadoUP = sheetUNO_PLUS.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetUNO_PLUS.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellUP = subcombinadoUP.createCell(0);
            subcombinadoCellUP.setCellValue("DETALLE DE VENTAS EN LA ESTACION");
            subcombinadoCellUP.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalUP = sheetUNO_PLUS.createRow(6);
            XSSFCell subprincipalcellUP;
            Integer h;
            h = 0;
            for (String string : colsname) {
                subprincipalcellUP = subprincipalUP.createCell(h);
                subprincipalcellUP.setCellValue(string);
                subprincipalcellUP.setCellStyle(subheaderStyle);
                h++;

            }
//            
//            //TC DAVIVIENDA
//            
            /*Se setea el ancho de cada una de las columnas */
            for (int k = 0; k < nocols + 7; k++) {
                sheetTC_DAVIVIENDA.setColumnWidth(k, 5500);
            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow header10 = sheetTC_DAVIVIENDA.createRow(0);

//            Se escribe el titulo principal
            XSSFCell headerCellTD = header10.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellTD.setCellValue("TC DAVIVIENDA");
            headerCellTD.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtituloTD = sheetTC_DAVIVIENDA.createRow(2);
            XSSFCell subtituloCellTD = subtituloTD.createCell(2);
            subtituloCellTD.setCellValue(subtitulo1);
            subtituloCellTD.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadoTD = sheetTC_DAVIVIENDA.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetTC_DAVIVIENDA.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellTD = subcombinadoTD.createCell(0);
            subcombinadoCellTD.setCellValue("DETALLE DE VENTAS EN LA ESTACION");
            subcombinadoCellTD.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalTD = sheetTC_DAVIVIENDA.createRow(6);
            XSSFCell subprincipalcellTD;
            Integer k;
            k = 0;
            for (String string : colsname) {
                subprincipalcellTD = subprincipalTD.createCell(k);
                subprincipalcellTD.setCellValue(string);
                subprincipalcellTD.setCellStyle(subheaderStyle);
                k++;

            }

            //Cuando se desee crear mas de una Hoja se debe leer el Array de Hojas y crear 1 a 1
            XSSFRow datosVERSATEC;
            XSSFCell datoscellVERSATEC;
            Integer ii; //COLUMNAS
            Integer j = 7; //FILAS
            ii = 0;
            Integer filas = lista.size();

            XSSFRow datosTC_FLOTA_BCR;
            XSSFCell datoscellTC_FLOTA_BCR;
            Integer aa; //COLUMNAS
            Integer fb = 7; //FILAS
            aa = 0;
            Integer filasfb = lista.size();

            XSSFRow datosTARJETA_BANCO_NACIONAL;
            XSSFCell datoscellTARJETA_BANCO_NACIONAL;
            Integer bb; //COLUMNAS
            Integer bn = 7; //FILAS
            bb = 0;
            Integer filasbn = lista.size();

            XSSFRow datosTARJETA_CREDOMATIC;
            XSSFCell datoscellTARJETA_CREDOMATIC;
            Integer cc; //COLUMNAS
            Integer tc = 7; //FILAS
            cc = 0;
            Integer filastc = lista.size();

            XSSFRow datosFLEET_MAGIC_DAVIVIENDA;
            XSSFCell datoscellFLEET_MAGIC_DAVIVIENDA;
            Integer dd; //COLUMNAS
            Integer md = 7; //FILAS
            dd = 0;
            Integer filasmd = lista.size();

            XSSFRow datosTARJETA_FLEET_MAGIC_SB;
            XSSFCell datoscellTARJETA_FLEET_MAGIC_SB;
            Integer ee; //COLUMNAS
            Integer fm = 7; //FILAS
            ee = 0;
            Integer filasfm = lista.size();

            XSSFRow datosTARJETA_BCR;
            XSSFCell datoscellTARJETA_BCR;
            Integer ff; //COLUMNAS
            Integer tb = 7; //FILAS
            ff = 0;
            Integer filastb = lista.size();

            XSSFRow datosTC_FLOTA_BAC;
            XSSFCell datoscellTC_FLOTA_BAC;
            Integer gg; //COLUMNAS
            Integer tfb = 7; //FILAS
            gg = 0;
            Integer filastfb = lista.size();

            XSSFRow datosUNO_PLUS;
            XSSFCell datoscellUNO_PLUS;
            Integer hh; //COLUMNAS
            Integer up = 7; //FILAS
            hh = 0;
            Integer filasup = lista.size();

            XSSFRow datosTC_DAVIVIENDA;
            XSSFCell datoscellTC_DAVIVIENDA;
            Integer jj; //COLUMNAS
            Integer dv = 7; //FILAS
            jj = 0;
            Integer filasdv = lista.size();

            for (GenericRprControlMediosPago itemId : lista.getItemIds()) {
                if (itemId.getMediopago_id() == 119) {
                    datosVERSATEC = sheetVERSATEC.createRow(j);

                    ii = 0;
                    datoscellVERSATEC = datosVERSATEC.createCell(ii);
                    datoscellVERSATEC.setCellValue(itemId.getFecha());
                    datoscellVERSATEC.setCellStyle(styleFecha);
                    ii++;
                    datoscellVERSATEC = datosVERSATEC.createCell(ii);
                    datoscellVERSATEC.setCellValue(itemId.getLote());
                    datoscellVERSATEC.setCellStyle(styleNumber);
                    ii++;
                    datoscellVERSATEC = datosVERSATEC.createCell(ii);
                    datoscellVERSATEC.setCellValue(itemId.getMonto_bruto());
                    datoscellVERSATEC.setCellStyle(styleNumber);
                    ii++;
                    datoscellVERSATEC = datosVERSATEC.createCell(ii);
                    datoscellVERSATEC.setCellValue(itemId.getComision());
                    datoscellVERSATEC.setCellStyle(styleNumber);
                    ii++;

                    datoscellVERSATEC = datosVERSATEC.createCell(ii);
                    datoscellVERSATEC.setCellValue(itemId.getMonto_neto());
                    datoscellVERSATEC.setCellStyle(styleNumber);
                    ii++;

                    datoscellVERSATEC = datosVERSATEC.createCell(ii);
                    datoscellVERSATEC.setCellValue(itemId.getComentarios());
                    datoscellVERSATEC.setCellStyle(styleString);
                    ii++;

                    /*Al Final*/
                    j++;

//                    System.out.println("MEDIO " + itemId.getFecha());
//                    System.out.println("MEDIO2 " + itemId.getLote());
//                    System.out.println("MEDIO3 " + itemId.getMediopago_id());
//                    System.out.println("MEDIO4 " + itemId.getMonto_bruto());
//                    System.out.println("MEDIO5 " + itemId.getComision());
//                    System.out.println("MEDIO6 " + itemId.getMonto_neto());
                }

                if (itemId.getMediopago_id() == 120) {
                    datosTC_FLOTA_BCR = sheetTC_FLOTA_BCR.createRow(fb);

                    aa = 0;
                    datoscellTC_FLOTA_BCR = datosTC_FLOTA_BCR.createCell(aa);
                    datoscellTC_FLOTA_BCR.setCellValue(itemId.getFecha());
                    datoscellTC_FLOTA_BCR.setCellStyle(styleFecha);
                    aa++;
                    datoscellTC_FLOTA_BCR = datosTC_FLOTA_BCR.createCell(aa);
                    datoscellTC_FLOTA_BCR.setCellValue(itemId.getLote());
                    datoscellTC_FLOTA_BCR.setCellStyle(styleNumber);
                    aa++;
                    datoscellTC_FLOTA_BCR = datosTC_FLOTA_BCR.createCell(aa);
                    datoscellTC_FLOTA_BCR.setCellValue(itemId.getMonto_bruto());
                    datoscellTC_FLOTA_BCR.setCellStyle(styleNumber);
                    aa++;
                    datoscellTC_FLOTA_BCR = datosTC_FLOTA_BCR.createCell(aa);
                    datoscellTC_FLOTA_BCR.setCellValue(itemId.getComision());
                    datoscellTC_FLOTA_BCR.setCellStyle(styleNumber);
                    aa++;

                    datoscellTC_FLOTA_BCR = datosTC_FLOTA_BCR.createCell(aa);
                    datoscellTC_FLOTA_BCR.setCellValue(itemId.getMonto_neto());
                    datoscellTC_FLOTA_BCR.setCellStyle(styleNumber);
                    aa++;

                    datoscellTC_FLOTA_BCR = datosTC_FLOTA_BCR.createCell(aa);
                    datoscellTC_FLOTA_BCR.setCellValue(itemId.getComentarios());
                    datoscellTC_FLOTA_BCR.setCellStyle(styleString);
                    aa++;

                    /*Al Final*/
                    fb++;
                }

                if (itemId.getMediopago_id() == 7) {
                    datosTARJETA_BANCO_NACIONAL = sheetTARJETA_BANCO_NACIONAL.createRow(bn);

                    bb = 0;
                    datoscellTARJETA_BANCO_NACIONAL = datosTARJETA_BANCO_NACIONAL.createCell(bb);
                    datoscellTARJETA_BANCO_NACIONAL.setCellValue(itemId.getFecha());
                    datoscellTARJETA_BANCO_NACIONAL.setCellStyle(styleFecha);
                    bb++;
                    datoscellTARJETA_BANCO_NACIONAL = datosTARJETA_BANCO_NACIONAL.createCell(bb);
                    datoscellTARJETA_BANCO_NACIONAL.setCellValue(itemId.getLote());
                    datoscellTARJETA_BANCO_NACIONAL.setCellStyle(styleNumber);
                    bb++;
                    datoscellTARJETA_BANCO_NACIONAL = datosTARJETA_BANCO_NACIONAL.createCell(bb);
                    datoscellTARJETA_BANCO_NACIONAL.setCellValue(itemId.getMonto_bruto());
                    datoscellTARJETA_BANCO_NACIONAL.setCellStyle(styleNumber);
                    bb++;
                    datoscellTARJETA_BANCO_NACIONAL = datosTARJETA_BANCO_NACIONAL.createCell(bb);
                    datoscellTARJETA_BANCO_NACIONAL.setCellValue(itemId.getComision());
                    datoscellTARJETA_BANCO_NACIONAL.setCellStyle(styleNumber);
                    bb++;

                    datoscellTARJETA_BANCO_NACIONAL = datosTARJETA_BANCO_NACIONAL.createCell(bb);
                    datoscellTARJETA_BANCO_NACIONAL.setCellValue(itemId.getMonto_neto());
                    datoscellTARJETA_BANCO_NACIONAL.setCellStyle(styleNumber);
                    bb++;

                    datoscellTARJETA_BANCO_NACIONAL = datosTARJETA_BANCO_NACIONAL.createCell(bb);
                    datoscellTARJETA_BANCO_NACIONAL.setCellValue(itemId.getComentarios());
                    datoscellTARJETA_BANCO_NACIONAL.setCellStyle(styleString);
                    bb++;

                    /*Al Final*/
                    bn++;
                }

                if (itemId.getMediopago_id() == 107) {
                    datosTARJETA_CREDOMATIC = sheetTARJETA_CREDOMATIC.createRow(tc);

                    cc = 0;
                    datoscellTARJETA_CREDOMATIC = datosTARJETA_CREDOMATIC.createCell(cc);
                    datoscellTARJETA_CREDOMATIC.setCellValue(itemId.getFecha());
                    datoscellTARJETA_CREDOMATIC.setCellStyle(styleFecha);
                    cc++;
                    datoscellTARJETA_CREDOMATIC = datosTARJETA_CREDOMATIC.createCell(cc);
                    datoscellTARJETA_CREDOMATIC.setCellValue(itemId.getLote());
                    datoscellTARJETA_CREDOMATIC.setCellStyle(styleNumber);
                    cc++;
                    datoscellTARJETA_CREDOMATIC = datosTARJETA_CREDOMATIC.createCell(cc);
                    datoscellTARJETA_CREDOMATIC.setCellValue(itemId.getMonto_bruto());
                    datoscellTARJETA_CREDOMATIC.setCellStyle(styleNumber);
                    cc++;
                    datoscellTARJETA_CREDOMATIC = datosTARJETA_CREDOMATIC.createCell(cc);
                    datoscellTARJETA_CREDOMATIC.setCellValue(itemId.getComision());
                    datoscellTARJETA_CREDOMATIC.setCellStyle(styleNumber);
                    cc++;

                    datoscellTARJETA_CREDOMATIC = datosTARJETA_CREDOMATIC.createCell(cc);
                    datoscellTARJETA_CREDOMATIC.setCellValue(itemId.getMonto_neto());
                    datoscellTARJETA_CREDOMATIC.setCellStyle(styleNumber);
                    cc++;

                    datoscellTARJETA_CREDOMATIC = datosTARJETA_CREDOMATIC.createCell(cc);
                    datoscellTARJETA_CREDOMATIC.setCellValue(itemId.getComentarios());
                    datoscellTARJETA_CREDOMATIC.setCellStyle(styleString);
                    cc++;

                    /*Al Final*/
                    tc++;
                }

                if (itemId.getMediopago_id() == 115) {
                    datosFLEET_MAGIC_DAVIVIENDA = sheetFLEET_MAGIC_DAVIVIENDA.createRow(md);

                    dd = 0;
                    datoscellFLEET_MAGIC_DAVIVIENDA = datosFLEET_MAGIC_DAVIVIENDA.createCell(dd);
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellValue(itemId.getFecha());
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellStyle(styleFecha);
                    dd++;
                    datoscellFLEET_MAGIC_DAVIVIENDA = datosFLEET_MAGIC_DAVIVIENDA.createCell(dd);
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellValue(itemId.getLote());
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellStyle(styleNumber);
                    dd++;
                    datoscellFLEET_MAGIC_DAVIVIENDA = datosFLEET_MAGIC_DAVIVIENDA.createCell(dd);
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellValue(itemId.getMonto_bruto());
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellStyle(styleNumber);
                    dd++;
                    datoscellFLEET_MAGIC_DAVIVIENDA = datosFLEET_MAGIC_DAVIVIENDA.createCell(dd);
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellValue(itemId.getComision());
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellStyle(styleNumber);
                    dd++;

                    datoscellFLEET_MAGIC_DAVIVIENDA = datosFLEET_MAGIC_DAVIVIENDA.createCell(dd);
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellValue(itemId.getMonto_neto());
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellStyle(styleNumber);
                    dd++;

                    datoscellFLEET_MAGIC_DAVIVIENDA = datosFLEET_MAGIC_DAVIVIENDA.createCell(dd);
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellValue(itemId.getComentarios());
                    datoscellFLEET_MAGIC_DAVIVIENDA.setCellStyle(styleString);
                    dd++;

                    /*Al Final*/
                    md++;
                }

                if (itemId.getMediopago_id() == 116) {
                    datosTARJETA_FLEET_MAGIC_SB = sheetTARJETA_FLEET_MAGIC_SB.createRow(fm);

                    ee = 0;
                    datoscellTARJETA_FLEET_MAGIC_SB = datosTARJETA_FLEET_MAGIC_SB.createCell(ee);
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellValue(itemId.getFecha());
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellStyle(styleFecha);
                    ee++;
                    datoscellTARJETA_FLEET_MAGIC_SB = datosTARJETA_FLEET_MAGIC_SB.createCell(ee);
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellValue(itemId.getLote());
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellStyle(styleNumber);
                    ee++;
                    datoscellTARJETA_FLEET_MAGIC_SB = datosTARJETA_FLEET_MAGIC_SB.createCell(ee);
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellValue(itemId.getMonto_bruto());
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellStyle(styleNumber);
                    ee++;
                    datoscellTARJETA_FLEET_MAGIC_SB = datosTARJETA_FLEET_MAGIC_SB.createCell(ee);
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellValue(itemId.getComision());
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellStyle(styleNumber);
                    ee++;

                    datoscellTARJETA_FLEET_MAGIC_SB = datosTARJETA_FLEET_MAGIC_SB.createCell(ee);
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellValue(itemId.getMonto_neto());
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellStyle(styleNumber);
                    ee++;

                    datoscellTARJETA_FLEET_MAGIC_SB = datosTARJETA_FLEET_MAGIC_SB.createCell(ee);
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellValue(itemId.getComentarios());
                    datoscellTARJETA_FLEET_MAGIC_SB.setCellStyle(styleString);
                    ee++;

                    /*Al Final*/
                    fm++;
                }

                if (itemId.getMediopago_id() == 118) {
                    datosTARJETA_BCR = sheetTARJETA_BCR.createRow(tb);

                    ff = 0;
                    datoscellTARJETA_BCR = datosTARJETA_BCR.createCell(ff);
                    datoscellTARJETA_BCR.setCellValue(itemId.getFecha());
                    datoscellTARJETA_BCR.setCellStyle(styleFecha);
                    ff++;
                    datoscellTARJETA_BCR = datosTARJETA_BCR.createCell(ff);
                    datoscellTARJETA_BCR.setCellValue(itemId.getLote());
                    datoscellTARJETA_BCR.setCellStyle(styleNumber);
                    ff++;
                    datoscellTARJETA_BCR = datosTARJETA_BCR.createCell(ff);
                    datoscellTARJETA_BCR.setCellValue(itemId.getMonto_bruto());
                    datoscellTARJETA_BCR.setCellStyle(styleNumber);
                    ff++;
                    datoscellTARJETA_BCR = datosTARJETA_BCR.createCell(ff);
                    datoscellTARJETA_BCR.setCellValue(itemId.getComision());
                    datoscellTARJETA_BCR.setCellStyle(styleNumber);
                    ff++;

                    datoscellTARJETA_BCR = datosTARJETA_BCR.createCell(ff);
                    datoscellTARJETA_BCR.setCellValue(itemId.getMonto_neto());
                    datoscellTARJETA_BCR.setCellStyle(styleNumber);
                    ff++;

                    datoscellTARJETA_BCR = datosTARJETA_BCR.createCell(ff);
                    datoscellTARJETA_BCR.setCellValue(itemId.getComentarios());
                    datoscellTARJETA_BCR.setCellStyle(styleString);
                    ff++;

                    /*Al Final*/
                    tb++;
                }

                if (itemId.getMediopago_id() == 121) {
                    datosTC_FLOTA_BAC = sheetTC_FLOTA_BAC.createRow(tfb);

                    gg = 0;
                    datoscellTC_FLOTA_BAC = datosTC_FLOTA_BAC.createCell(gg);
                    datoscellTC_FLOTA_BAC.setCellValue(itemId.getFecha());
                    datoscellTC_FLOTA_BAC.setCellStyle(styleFecha);
                    gg++;
                    datoscellTC_FLOTA_BAC = datosTC_FLOTA_BAC.createCell(gg);
                    datoscellTC_FLOTA_BAC.setCellValue(itemId.getLote());
                    datoscellTC_FLOTA_BAC.setCellStyle(styleNumber);
                    gg++;
                    datoscellTC_FLOTA_BAC = datosTC_FLOTA_BAC.createCell(gg);
                    datoscellTC_FLOTA_BAC.setCellValue(itemId.getMonto_bruto());
                    datoscellTC_FLOTA_BAC.setCellStyle(styleNumber);
                    gg++;
                    datoscellTC_FLOTA_BAC = datosTC_FLOTA_BAC.createCell(gg);
                    datoscellTC_FLOTA_BAC.setCellValue(itemId.getComision());
                    datoscellTC_FLOTA_BAC.setCellStyle(styleNumber);
                    gg++;

                    datoscellTC_FLOTA_BAC = datosTC_FLOTA_BAC.createCell(gg);
                    datoscellTC_FLOTA_BAC.setCellValue(itemId.getMonto_neto());
                    datoscellTC_FLOTA_BAC.setCellStyle(styleNumber);
                    gg++;

                    datoscellTC_FLOTA_BAC = datosTC_FLOTA_BAC.createCell(gg);
                    datoscellTC_FLOTA_BAC.setCellValue(itemId.getComentarios());
                    datoscellTC_FLOTA_BAC.setCellStyle(styleString);
                    gg++;

                    /*Al Final*/
                    tfb++;
                }

                if (itemId.getMediopago_id() == 122) {
                    datosUNO_PLUS = sheetUNO_PLUS.createRow(up);

                    hh = 0;
                    datoscellUNO_PLUS = datosUNO_PLUS.createCell(hh);
                    datoscellUNO_PLUS.setCellValue(itemId.getFecha());
                    datoscellUNO_PLUS.setCellStyle(styleFecha);
                    hh++;
                    datoscellUNO_PLUS = datosUNO_PLUS.createCell(hh);
                    datoscellUNO_PLUS.setCellValue(itemId.getLote());
                    datoscellUNO_PLUS.setCellStyle(styleNumber);
                    hh++;
                    datoscellUNO_PLUS = datosUNO_PLUS.createCell(hh);
                    datoscellUNO_PLUS.setCellValue(itemId.getMonto_bruto());
                    datoscellUNO_PLUS.setCellStyle(styleNumber);
                    hh++;
                    datoscellUNO_PLUS = datosUNO_PLUS.createCell(hh);
                    datoscellUNO_PLUS.setCellValue(itemId.getComision());
                    datoscellUNO_PLUS.setCellStyle(styleNumber);
                    hh++;

                    datoscellUNO_PLUS = datosUNO_PLUS.createCell(hh);
                    datoscellUNO_PLUS.setCellValue(itemId.getMonto_neto());
                    datoscellUNO_PLUS.setCellStyle(styleNumber);
                    hh++;

                    datoscellUNO_PLUS = datosUNO_PLUS.createCell(hh);
                    datoscellUNO_PLUS.setCellValue(itemId.getComentarios());
                    datoscellUNO_PLUS.setCellStyle(styleString);
                    hh++;

                    /*Al Final*/
                    up++;
                }

                if (itemId.getMediopago_id() == 123) {
                    datosTC_DAVIVIENDA = sheetTC_DAVIVIENDA.createRow(dv);

                    jj = 0;
                    datoscellTC_DAVIVIENDA = datosTC_DAVIVIENDA.createCell(jj);
                    datoscellTC_DAVIVIENDA.setCellValue(itemId.getFecha());
                    datoscellTC_DAVIVIENDA.setCellStyle(styleFecha);
                    jj++;
                    datoscellTC_DAVIVIENDA = datosTC_DAVIVIENDA.createCell(jj);
                    datoscellTC_DAVIVIENDA.setCellValue(itemId.getLote());
                    datoscellTC_DAVIVIENDA.setCellStyle(styleNumber);
                    jj++;
                    datoscellTC_DAVIVIENDA = datosTC_DAVIVIENDA.createCell(jj);
                    datoscellTC_DAVIVIENDA.setCellValue(itemId.getMonto_bruto());
                    datoscellTC_DAVIVIENDA.setCellStyle(styleNumber);
                    jj++;
                    datoscellTC_DAVIVIENDA = datosTC_DAVIVIENDA.createCell(jj);
                    datoscellTC_DAVIVIENDA.setCellValue(itemId.getComision());
                    datoscellTC_DAVIVIENDA.setCellStyle(styleNumber);
                    jj++;

                    datoscellTC_DAVIVIENDA = datosTC_DAVIVIENDA.createCell(jj);
                    datoscellTC_DAVIVIENDA.setCellValue(itemId.getMonto_neto());
                    datoscellTC_DAVIVIENDA.setCellStyle(styleNumber);
                    jj++;

                    datoscellTC_DAVIVIENDA = datosTC_DAVIVIENDA.createCell(jj);
                    datoscellTC_DAVIVIENDA.setCellValue(itemId.getComentarios());
                    datoscellTC_DAVIVIENDA.setCellStyle(styleString);
                    jj++;

                    /*Al Final*/
                    dv++;
                }

            }
            XSSFSheet sheetDEPOSITOS_COLONES = null;
            XSSFSheet sheetDEPOSITOS_DOLARES = null;
            List<String> tituloscolumnasefectivo = new ArrayList<String>();
            String[] titulosefectivo = new String[]{"Dia de venta", "Codigo", "Estacion", "Banco", "No. Deposito", "Monto de deposito", "Monto de cheque", "Total", "Comentarios"};
            /*como crear un objeto arrylist de tipo string*/
            ArrayList<String> tituloshojaefectivo = new ArrayList<String>();
            tituloshojaefectivo.add("DEPOSITOS COLONES");
            tituloshojaefectivo.add("DEPOSITOS DOLARES");

            tituloscolumnasefectivo = Arrays.asList(titulosefectivo);

            //DEPOSITOS COLONES
            for (String titulosHojaefectivo : tituloshojaefectivo) {
                if (titulosHojaefectivo.equals("DEPOSITOS COLONES")) {
                    sheetDEPOSITOS_COLONES = workbook.createSheet(titulosHojaefectivo);
                }
                if (titulosHojaefectivo.equals("DEPOSITOS DOLARES")) {
                    sheetDEPOSITOS_DOLARES = workbook.createSheet(titulosHojaefectivo);
                }

            }
//            
            /*Se setea el ancho de cada una de las columnas */
            for (int vv = 0; vv < nocols + 7; vv++) {
                sheetDEPOSITOS_COLONES.setColumnWidth(vv, 5500);
            }
            
//            for (int vv = 0; vv < tituloscolumnasefectivo.size(); vv++) {
//                sheetDEPOSITOS_COLONES.setColumnWidth(vv, 5500);
//            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow header11 = sheetDEPOSITOS_COLONES.createRow(0);

//            Se escribe el titulo principal
            XSSFCell headerCellDC = header11.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellDC.setCellValue("DEPOSITOS COLONES");
            headerCellDC.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtituloDC = sheetDEPOSITOS_COLONES.createRow(2);
            XSSFCell subtituloCellDC = subtituloDC.createCell(2);
            subtituloCellDC.setCellValue(subtitulo1);
            subtituloCellDC.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadoDC = sheetDEPOSITOS_COLONES.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetDEPOSITOS_COLONES.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellDC = subcombinadoDC.createCell(0);
            subcombinadoCellDC.setCellValue("CONTROL DE VENTAS EN EFECTIVO");
            subcombinadoCellDC.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalDC = sheetDEPOSITOS_COLONES.createRow(6);
            XSSFCell subprincipalcellDC;
            Integer l;
            l = 0;
            for (String string : titulosefectivo) {
                subprincipalcellDC = subprincipalDC.createCell(l);
                subprincipalcellDC.setCellValue(string);
                subprincipalcellDC.setCellStyle(subheaderStyle);
                l++;

            }

            //DEPOSITOS DOLARES
//            
            /*Se setea el ancho de cada una de las columnas */
            for (int xx = 0; xx < nocols + 7; xx++) {
                sheetDEPOSITOS_DOLARES.setColumnWidth(xx, 5500);
            }
            
//            for (int xx = 0; xx < tituloscolumnasefectivo.size(); xx++) {
//                sheetDEPOSITOS_DOLARES.setColumnWidth(xx, 5500);
//            }


            /*Se crea la primer fila para el titulo*/
            XSSFRow header12 = sheetDEPOSITOS_DOLARES.createRow(0);

//            Se escribe el titulo principal
            XSSFCell headerCellUSD = header12.createCell(2); // Numero de columna en la que se creara el titulo
            headerCellUSD.setCellValue("DEPOSITOS DOLARES");
            headerCellUSD.setCellStyle(headerStyle);

            /*Se crea el subtitulo*/
            XSSFRow subtituloUSD = sheetDEPOSITOS_DOLARES.createRow(2);
            XSSFCell subtituloCellUSD = subtituloUSD.createCell(2);
            subtituloCellUSD.setCellValue(subtitulo1);
            subtituloCellUSD.setCellStyle(headerStyle);

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinadoUSD = sheetDEPOSITOS_DOLARES.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheetDEPOSITOS_DOLARES.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCellUSD = subcombinadoUSD.createCell(0);
            subcombinadoCellUSD.setCellValue("CONTROL DE VENTAS EN EFECTIVO");
            subcombinadoCellUSD.setCellStyle(subheaderStyle);

            /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

            /*Se crean los titulos de columnas*/
            XSSFRow subprincipalUSD = sheetDEPOSITOS_DOLARES.createRow(6);
            XSSFCell subprincipalcellUSD;
            Integer m;
            m = 0;
            for (String string : titulosefectivo) {
                subprincipalcellUSD = subprincipalUSD.createCell(m);
                subprincipalcellUSD.setCellValue(string);
                subprincipalcellUSD.setCellStyle(subheaderStyle);
                m++;

            }

            XSSFRow datosDEPOSITOS_COLONES;
            XSSFCell datoscellDEPOSITOS_COLONES;
            Integer kk; //COLUMNAS
            Integer dc = 7; //FILAS
            kk = 0;
            Integer filasdc = lista.size();
            
            XSSFRow datosDEPOSITOS_DOLARES;
            XSSFCell datoscellDEPOSITOS_DOLARES;
            Integer oo; //COLUMNAS
            Integer usdd = 7; //FILAS
            oo = 0;
            Integer filasusdd = lista.size();


            for (GenericRprControlMediosPago itemId : lista.getItemIds()) {
                if (itemId.getMediopago_id() == 45) {
                    datosDEPOSITOS_COLONES = sheetDEPOSITOS_COLONES.createRow(dc);

                    kk = 0;
                    datoscellDEPOSITOS_COLONES = datosDEPOSITOS_COLONES.createCell(kk);
                    datoscellDEPOSITOS_COLONES.setCellValue(itemId.getFecha());
                    datoscellDEPOSITOS_COLONES.setCellStyle(styleFecha);
                    kk++;
                    datoscellDEPOSITOS_COLONES = datosDEPOSITOS_COLONES.createCell(kk);
                    datoscellDEPOSITOS_COLONES.setCellValue(itemId.getCodigo());
                    datoscellDEPOSITOS_COLONES.setCellStyle(styleNumber);
                    kk++;
                    datoscellDEPOSITOS_COLONES = datosDEPOSITOS_COLONES.createCell(kk);
                    datoscellDEPOSITOS_COLONES.setCellValue(itemId.getEstacion());
                    datoscellDEPOSITOS_COLONES.setCellStyle(styleString);
                    kk++;
                    datoscellDEPOSITOS_COLONES = datosDEPOSITOS_COLONES.createCell(kk);
                    datoscellDEPOSITOS_COLONES.setCellValue(itemId.getBanco());
                    datoscellDEPOSITOS_COLONES.setCellStyle(styleString);
                    kk++;

                    datoscellDEPOSITOS_COLONES = datosDEPOSITOS_COLONES.createCell(kk);
                    datoscellDEPOSITOS_COLONES.setCellValue(itemId.getNodeposito());
                    datoscellDEPOSITOS_COLONES.setCellStyle(styleNumber);
                    kk++;

                    datoscellDEPOSITOS_COLONES = datosDEPOSITOS_COLONES.createCell(kk);
                    datoscellDEPOSITOS_COLONES.setCellValue(itemId.getMonto_bruto());
                    datoscellDEPOSITOS_COLONES.setCellStyle(styleNumber);
                    kk++;

                    datoscellDEPOSITOS_COLONES = datosDEPOSITOS_COLONES.createCell(kk);
                    datoscellDEPOSITOS_COLONES.setCellValue(itemId.getMontoch());
                    datoscellDEPOSITOS_COLONES.setCellStyle(styleNumber);
                    kk++;

                    datoscellDEPOSITOS_COLONES = datosDEPOSITOS_COLONES.createCell(kk);
                    datoscellDEPOSITOS_COLONES.setCellValue(itemId.getMonto_bruto());
                    datoscellDEPOSITOS_COLONES.setCellStyle(styleNumber);
                    kk++;

                    datoscellDEPOSITOS_COLONES = datosDEPOSITOS_COLONES.createCell(kk);
                    datoscellDEPOSITOS_COLONES.setCellValue(itemId.getComentarios());
                    datoscellDEPOSITOS_COLONES.setCellStyle(styleString);
                    kk++;

                    /*Al Final*/
                    dc++;
                }
                
                if (itemId.getMediopago_id() == 8) {
                    datosDEPOSITOS_DOLARES = sheetDEPOSITOS_DOLARES.createRow(usdd);

                    oo = 0;
                    datoscellDEPOSITOS_DOLARES = datosDEPOSITOS_DOLARES.createCell(oo);
                    datoscellDEPOSITOS_DOLARES.setCellValue(itemId.getFecha());
                    datoscellDEPOSITOS_DOLARES.setCellStyle(styleFecha);
                    oo++;
                    datoscellDEPOSITOS_DOLARES = datosDEPOSITOS_DOLARES.createCell(oo);
                    datoscellDEPOSITOS_DOLARES.setCellValue(itemId.getCodigo());
                    datoscellDEPOSITOS_DOLARES.setCellStyle(styleNumber);
                    oo++;
                    datoscellDEPOSITOS_DOLARES = datosDEPOSITOS_DOLARES.createCell(oo);
                    datoscellDEPOSITOS_DOLARES.setCellValue(itemId.getEstacion());
                    datoscellDEPOSITOS_DOLARES.setCellStyle(styleString);
                    oo++;
                    datoscellDEPOSITOS_DOLARES = datosDEPOSITOS_DOLARES.createCell(oo);
                    datoscellDEPOSITOS_DOLARES.setCellValue(itemId.getBanco());
                    datoscellDEPOSITOS_DOLARES.setCellStyle(styleString);
                    oo++;

                    datoscellDEPOSITOS_DOLARES = datosDEPOSITOS_DOLARES.createCell(oo);
                    datoscellDEPOSITOS_DOLARES.setCellValue(itemId.getNodeposito());
                    datoscellDEPOSITOS_DOLARES.setCellStyle(styleNumber);
                    oo++;

                    datoscellDEPOSITOS_DOLARES = datosDEPOSITOS_DOLARES.createCell(oo);
                    datoscellDEPOSITOS_DOLARES.setCellValue(itemId.getMontousd());
                    datoscellDEPOSITOS_DOLARES.setCellStyle(styleNumber);
                    oo++;

                    datoscellDEPOSITOS_DOLARES = datosDEPOSITOS_DOLARES.createCell(oo);
                    datoscellDEPOSITOS_DOLARES.setCellValue(itemId.getMontoch());
                    datoscellDEPOSITOS_DOLARES.setCellStyle(styleNumber);
                    oo++;

                    datoscellDEPOSITOS_DOLARES = datosDEPOSITOS_DOLARES.createCell(oo);
                    datoscellDEPOSITOS_DOLARES.setCellValue(itemId.getMontousd());
                    datoscellDEPOSITOS_DOLARES.setCellStyle(styleNumber);
                    oo++;

                    datoscellDEPOSITOS_DOLARES = datosDEPOSITOS_DOLARES.createCell(oo);
                    datoscellDEPOSITOS_DOLARES.setCellValue(itemId.getComentarios());
                    datoscellDEPOSITOS_DOLARES.setCellStyle(styleString);
                    oo++;

                    /*Al Final*/
                    usdd++;
                }
            }
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
