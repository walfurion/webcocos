/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.utils;

import com.sisintegrados.generic.bean.GenericMTD;
import com.sisintegrados.generic.bean.GenericRptWetStock;
import com.vaadin.data.util.BeanItemContainer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Allan G.
 */
public class ExcelGeneratorWetStock {

    CellRangeAddress cc;

    public XSSFWorkbook generar(Integer sheets, Integer nocols, List<String> colsname, String titulo1, String subtitulo1, String sheetname, BeanItemContainer<GenericRptWetStock> lista, ArrayList<String> sheetsname) {
        XSSFWorkbook workbook = new XSSFWorkbook();
//        Valida que solo sea una hoja la que se va crear
//        if (sheets == 1) {
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

//        XSSFCellStyle stilo1 = crearStilo(workbook, false, true, true, true, true, true, true, IndexedColors.BLACK.getIndex(), 12, false, true, false, "");

        /*tanque 1*/
//            XSSFRow ta1 = sheet.createRow(3);
//            XSSFCell st1= ta1.createCell(1);
//            st1.setCellValue("Tanque1");
        XSSFRow producto = sheet.createRow(3);
        XSSFRow pro = sheet.createRow(4);
        
        XSSFCell subProducto = producto.createCell(1);
        subProducto.setCellValue("Tanque 1");
        subProducto.setCellStyle(subheaderStyle);
        cc = new CellRangeAddress(3, 3, 1, 6);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, true, false, 2); 
        
        XSSFCell subInf = producto.createCell(7);
        subInf.setCellValue("");
        subInf.setCellStyle(subheaderStyle);
        cc = new CellRangeAddress(3, 3, 7, 11);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, true, false, 2); 
        
        XSSFCell subProducto1 = producto.createCell(13);
        subProducto1.setCellValue("Tanque 2");
        subProducto1.setCellStyle(subheaderStyle);
        cc = new CellRangeAddress(3, 3, 13, 18);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, true, false, 2);
        
        XSSFCell subInf1 = producto.createCell(19);
        subInf1.setCellValue("");
        subInf1.setCellStyle(subheaderStyle);
        cc = new CellRangeAddress(3, 3, 19, 23);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, true, false, 2);
        
        XSSFCell subProducto2 = producto.createCell(25);
        subProducto2.setCellValue("Tanque 3");
        subProducto2.setCellStyle(subheaderStyle);
        cc = new CellRangeAddress(3, 3, 25, 30);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, true, false, 2);
        
        XSSFCell subInf2 = producto.createCell(31);
        subInf2.setCellValue("");
        subInf2.setCellStyle(subheaderStyle);
        cc = new CellRangeAddress(3, 3, 31, 35);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, true, false, 2);
        
        XSSFCell subProducto3 = producto.createCell(37);
        subProducto3.setCellValue("Tanque 4");
        subProducto3.setCellStyle(subheaderStyle);
        cc = new CellRangeAddress(3, 3, 37, 42);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, true, false, 2);

        XSSFCell subInf3 = producto.createCell(43);
        subInf3.setCellValue("");
        subInf3.setCellStyle(subheaderStyle);
        cc = new CellRangeAddress(3, 3, 43, 47);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, true, false, 2);
        
        XSSFCell subinfo = pro.createCell(7);
        subinfo.setCellValue("Informacion de Recepcion y Descarga");
        subinfo.setCellStyle(subheaderStyle);
//        subIfon1.setCellStyle(stilo1);
        cc = new CellRangeAddress(4, 4, 7, 11);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, false, true, 2);
        
        XSSFCell subinfo1 = pro.createCell(19);
        subinfo1.setCellValue("Informacion de Recepcion y Descarga");
        subinfo1.setCellStyle(subheaderStyle);
        cc = new CellRangeAddress(4, 4, 19, 23);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, false, true, 2);
        
        XSSFCell subinfo2 = pro.createCell(31);
        subinfo2.setCellValue("Informacion de Recepcion y Descarga");
        subinfo2.setCellStyle(subheaderStyle);
        cc = new CellRangeAddress(4, 4, 31, 35);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, false, true, 2);
        
        XSSFCell subinfo3 = pro.createCell(43);
        subinfo3.setCellValue("Informacion de Recepcion y Descarga");
        subinfo3.setCellStyle(subheaderStyle);
        cc = new CellRangeAddress(4, 4, 43, 47);
        sheet.addMergedRegion(cc);
        setBorders(sheet, cc, true, true, false, true, 2);
        
//        XSSFRow pro = sheet.createRow(4);              
//        XSSFCell subPro = pro.createCell(1);
//        subPro.setCellValue("Producto: Super");
//        subPro.setCellStyle(subheaderStyle);
//        cc = new CellRangeAddress(4, 4, 1, 6);
//        sheet.addMergedRegion(cc);
//        setBorders(sheet, cc, true, true, false, true, 2);
//
//
//        XSSFCell subPro1 = pro.createCell(13);
//        subPro1.setCellValue("Producto: Regular");
//        subPro1.setCellStyle(subheaderStyle);
//        cc = new CellRangeAddress(4, 4, 13, 18);
//        sheet.addMergedRegion(cc);
//        setBorders(sheet, cc, true, true, false, true, 2);
//
//        XSSFCell subPro2 = pro.createCell(25);
//        subPro2.setCellValue("Producto: Diesel");
//        subPro2.setCellStyle(subheaderStyle);
//        cc = new CellRangeAddress(4, 4, 25, 30);
//        sheet.addMergedRegion(cc);
//        setBorders(sheet, cc, true, true, false, true, 2);
//
//        XSSFCell subPro3 = pro.createCell(37);
//        subPro3.setCellValue("Producto: ");
//        subPro3.setCellStyle(subheaderStyle);
//        cc = new CellRangeAddress(4, 4, 37, 42);
//        sheet.addMergedRegion(cc);
//        setBorders(sheet, cc, true, true, false, true, 2);


        /**/
//            XSSFRow firmas = sheet.createRow(3);
//            XSSFCell firmasCel = firmas.createCell(0);
//            firmasCel.setCellValue("Tanque 1 ");
//            sheet.addMergedRegion(new CellRangeAddress(ii, ii, 1, 6));
//            cc = new CellRangeAddress(3, 3, 1, 6);
//            sheet.addMergedRegion(cc);
//            setBorders(sheet, cc, true, true, true, false, 2);
        /*Agrega Borde a todas las Celdas Combinadas*/
//            setBordersToMergedCells(sheet); Pendiente de corregir

        /*Se crean los titulos de columnas*/
        XSSFRow subprincipal = sheet.createRow(5);
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

        XSSFRow datos = sheet.createRow(7);;
        XSSFCell datoscell;
        Integer ii; //COLUMNAS
        Integer j = 6; //FILAS
        ii = 0;
        Integer filas = lista.size();
        Date fecpivot = new Date();
        String nombreTanque1 = "";
        String nombreTanque2 = "";
        String nombreTanque3 = "";
        String nombreTanque4 = "";
        int k = 0;        
        
//            for(GenericRptWetStock item : lista.getItemIds()){
        for (GenericRptWetStock itemId : lista.getItemIds()) {
            if (!itemId.getFecha().equals(fecpivot)) {
                fecpivot = itemId.getFecha();
                datos = sheet.createRow(j);
                ii = 0;
                j++;               
            }
            
            if (itemId.getTanque_id() == 1) {
                System.out.println("descripcion tanque {"+itemId.getDescripcion()+"}");
                System.out.println("nombre tanque {"+nombreTanque1+"}");
                if(!itemId.getDescripcion().equals(nombreTanque1)){
                    nombreTanque1 = itemId.getDescripcion();
                    XSSFCell subPro = pro.createCell(1);
                    subPro.setCellValue("Producto: "+itemId.getDescripcion());
                    subPro.setCellStyle(subheaderStyle);
                    cc = new CellRangeAddress(4, 4, 1, 6);
                    sheet.addMergedRegion(cc);
                    setBorders(sheet, cc, true, true, false, true, 2);
                    k++;
                }
            }
            if (itemId.getTanque_id() == 2) {
                if(!itemId.getDescripcion().equals(nombreTanque2)){
                    nombreTanque2 = itemId.getDescripcion();                
                    XSSFCell subPro1 = pro.createCell(13);
                    subPro1.setCellValue("Producto: "+itemId.getDescripcion());
                    subPro1.setCellStyle(subheaderStyle);
                    cc = new CellRangeAddress(4, 4, 13, 18);
                    sheet.addMergedRegion(cc);
                    setBorders(sheet, cc, true, true, false, true, 2);
                }
            } 
            if (itemId.getTanque_id() == 3) {
                if(!itemId.getDescripcion().equals(nombreTanque3)){
                    nombreTanque3 = itemId.getDescripcion();                
                    XSSFCell subPro2 = pro.createCell(25);
                    subPro2.setCellValue("Producto: "+itemId.getDescripcion());
                    subPro2.setCellStyle(subheaderStyle);
                    cc = new CellRangeAddress(4, 4, 25, 30);
                    sheet.addMergedRegion(cc);
                    setBorders(sheet, cc, true, true, false, true, 2);
                }
            } 
            if (itemId.getTanque_id() == 4) {
                System.out.println("entra al id tanque 4 "+itemId.getTanque_id());
                if(!itemId.getDescripcion().equals(nombreTanque4)){
                    System.out.println("entra al nombre tanque 4 "+nombreTanque4);
                    nombreTanque4 = itemId.getDescripcion();                
                    XSSFCell subPro3 = pro.createCell(37);
                    subPro3.setCellValue("Producto: "+itemId.getDescripcion());
                    subPro3.setCellStyle(subheaderStyle);
                    cc = new CellRangeAddress(4, 4, 37, 42);
                    sheet.addMergedRegion(cc);
                    setBorders(sheet, cc, true, true, false, true, 2);
                }
            }
            

            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getFecha());
            datoscell.setCellStyle(styleFecha);
            ii++;
            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getInicial());
            datoscell.setCellStyle(styleNumber);
            ii++;
            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getCompras());
            datoscell.setCellStyle(styleNumber);
            ii++;
            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getVentas());
            datoscell.setCellStyle(styleNumber);
            ii++;
            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getAjuste());
            datoscell.setCellStyle(styleNumber);
            ii++;
            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getInv_fisico());
            datoscell.setCellStyle(styleString);
            ii++;
            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getNivel());
            datoscell.setCellStyle(styleNumber);
            ii++;
            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getPiloto());
            datoscell.setCellStyle(styleNumber);
            ii++;
            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getUnidad());
            datoscell.setCellStyle(styleNumber);
            ii++;
            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getCompratimiento());
            datoscell.setCellStyle(styleNumber);
            ii++;
            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getFactura());
            datoscell.setCellStyle(styleNumber);
            ii++;
            datoscell = datos.createCell(ii);
            datoscell.setCellValue(itemId.getCompras());
            datoscell.setCellStyle(styleNumber);
            ii++;

            /*Al Final*/
//                j++;
        }
//            }

//        }else {
        //Cuando se desee crear mas de una Hoja se debe leer el Array de Hojas y crear 1 a 1        
        XSSFSheet sheetTanque1 = null;
        XSSFSheet sheetTanque2 = null;
        XSSFSheet sheetTanque3 = null;
        XSSFSheet sheetTanque4 = null;

        List<String> titulosColumnasTanque1 = new ArrayList<String>();
        String[] titulosHijosTanque1 = new String[]{"Fecha", "Inventario Inicial", "Volumen Facturado", "Volumen Recibido en Tanque", "Ventas",
            "Ajuste de Transferencias", "Inventario Teorico", "Inventario Fisico", "Nivel de Agua en Tanque", "Varianza en Volumen",
            "Varianza en Volumen de Recepciones", "Varianza Acumulada en Volumen de Recepciones", "Varianza (%) Sobre Ventas",
            "Varianza Acumulada", "Ventas Acumuladas", "Varianza Acumulada (%) sobre Ventas"};
//            List<String> titulosColumnasTanque2 = new ArrayList<String>();
//            String[] titulosHijosTanque2 = new String[]{"Fecha", "Inventario Inicial", "Volumen Facturado", "Volumen Recibido en Tanque", "Ventas", 
//                                                        "Ajuste de Transferencias", "Inventario Teorico", "Inventario Fisico", "Nivel de Agua en Tanque", "Varianza en Volumen",
//                                                        "Varianza en Volumen de Recepciones", "Varianza Acumulada en Volumen de Recepciones", "Varianza (%) Sobre Ventas", 
//                                                        "Varianza Acumulada", "Ventas Acumuladas", "Varianza Acumulada (%) sobre Ventas"};
        /*como crear un objeto arrylist de tipo string*/
        ArrayList<String> tituloshojasHijosTanque = new ArrayList<String>();
        tituloshojasHijosTanque.add("TANQUE 1");
        tituloshojasHijosTanque.add("TANQUE 2");
        tituloshojasHijosTanque.add("TANQUE 3");
        tituloshojasHijosTanque.add("TANQUE 4");

        titulosColumnasTanque1 = Arrays.asList(titulosHijosTanque1);
//            titulosColumnasTanque2 = Arrays.asList(titulosHijosTanque2);

        //TANQUE 1
        for (String tituloshojasHijosManualTanque : tituloshojasHijosTanque) {
            if (tituloshojasHijosManualTanque.equals("TANQUE 1")) {
                sheetTanque1 = workbook.createSheet(tituloshojasHijosManualTanque);
            }
            if (tituloshojasHijosManualTanque.equals("TANQUE 2")) {
                sheetTanque2 = workbook.createSheet(tituloshojasHijosManualTanque);
            }
            if (tituloshojasHijosManualTanque.equals("TANQUE 3")) {
                sheetTanque3 = workbook.createSheet(tituloshojasHijosManualTanque);
            }
            if (tituloshojasHijosManualTanque.equals("TANQUE 4")) {
                sheetTanque4 = workbook.createSheet(tituloshojasHijosManualTanque);
            }

        }
//            
        /*Se setea el ancho de cada una de las columnas */
        for (int vv = 0; vv < titulosColumnasTanque1.size() + 7; vv++) {
            sheetTanque1.setColumnWidth(vv, 5500);
        }

        /*Se crean los titulos de columnas*/
        XSSFRow subprincipal1 = sheetTanque1.createRow(0);
        XSSFCell subprincipalcell1;
        Integer t1;
        t1 = 0;
        for (String string : titulosColumnasTanque1) {
            subprincipalcell1 = subprincipal1.createCell(t1);
            subprincipalcell1.setCellValue(string);
            subprincipalcell1.setCellStyle(subheaderStyle);
            t1++;

        }

        //TANQUE 2
        /*Se setea el ancho de cada una de las columnas */
        for (int v1 = 0; v1 < titulosColumnasTanque1.size() + 7; v1++) {
            sheetTanque2.setColumnWidth(v1, 5500);
        }

        /*Se crean los titulos de columnas*/
        XSSFRow subprincipal2 = sheetTanque2.createRow(0);
        XSSFCell subprincipalcell2;
        Integer t2;
        t2 = 0;
        for (String string : titulosColumnasTanque1) {
            subprincipalcell2 = subprincipal2.createCell(t2);
            subprincipalcell2.setCellValue(string);
            subprincipalcell2.setCellStyle(subheaderStyle);
            t2++;

        }
        //TANQUE 3
        /*Se setea el ancho de cada una de las columnas */
        for (int v1 = 0; v1 < titulosColumnasTanque1.size() + 7; v1++) {
            sheetTanque2.setColumnWidth(v1, 5500);
        }

        /*Se crean los titulos de columnas*/
        XSSFRow subprincipal3 = sheetTanque3.createRow(0);
        XSSFCell subprincipalcell3;
        Integer t3;
        t3 = 0;
        for (String string : titulosColumnasTanque1) {
            subprincipalcell3 = subprincipal3.createCell(t3);
            subprincipalcell3.setCellValue(string);
            subprincipalcell3.setCellStyle(subheaderStyle);
            t3++;

        }
        //TANQUE 4
        /*Se setea el ancho de cada una de las columnas */
        for (int v1 = 0; v1 < titulosColumnasTanque1.size() + 7; v1++) {
            sheetTanque2.setColumnWidth(v1, 5500);
        }

        /*Se crean los titulos de columnas*/
        XSSFRow subprincipal4 = sheetTanque4.createRow(0);
        XSSFCell subprincipalcell4;
        Integer t4;
        t4 = 0;
        for (String string : titulosColumnasTanque1) {
            subprincipalcell4 = subprincipal4.createCell(t4);
            subprincipalcell4.setCellValue(string);
            subprincipalcell4.setCellStyle(subheaderStyle);
            t4++;

        }

        XSSFRow datosTanque1;
        XSSFCell datoscellTanque1;
        Integer i1; //COLUMNAS
        Integer j1 = 1; //FILAS
        i1 = 0;
        Integer filas1 = lista.size();

        XSSFRow datosTanque2;
        XSSFCell datoscellTanque2;
        Integer i2; //COLUMNAS
        Integer j2 = 1; //FILAS
        i2 = 0;
        Integer filas2 = lista.size();

        XSSFRow datosTanque3;
        XSSFCell datoscellTanque3;
        Integer i3; //COLUMNAS
        Integer j3 = 1; //FILAS
        i3 = 0;
        Integer filas3 = lista.size();

        XSSFRow datosTanque4;
        XSSFCell datoscellTanque4;
        Integer i4; //COLUMNAS
        Integer j4 = 1; //FILAS
        i4 = 0;
        Integer filas4 = lista.size();

        String fecha = "";
        Double varianzaAcum1 = 0.0;
        Double varianzaAcum2 = 0.0;
        Double varianzaAcum3 = 0.0;
        Double varianzaAcum4 = 0.0;
        Double ventasAcum1 = 0.0;
        Double ventasAcum2 = 0.0;
        Double ventasAcum3 = 0.0;
        Double ventasAcum4 = 0.0;
        Double porcentajeVarAcum1 = 0.0;
        Double porcentajeVarAcum2 = 0.0;
        Double porcentajeVarAcum3 = 0.0;
        Double porcentajeVarAcum4 = 0.0;

        for (GenericRptWetStock itemId : lista.getItemIds()) {
            if (itemId.getTanque_id() == 1) {
                datosTanque1 = sheetTanque1.createRow(j1);

                i1 = 0;
                fecha = itemId.getFecha().toString();
                varianzaAcum1 = varianzaAcum1 + itemId.getDiferencia();
                ventasAcum1 = ventasAcum1 + itemId.getVentas();
                porcentajeVarAcum1 = (varianzaAcum1 / ventasAcum1) * 100;

                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(fecha.substring(8));
                datoscellTanque1.setCellStyle(styleFecha);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(itemId.getInicial());
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(itemId.getCompras());
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(itemId.getCompras());
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(itemId.getVentas());
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(itemId.getAjuste());
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(itemId.getInv_teorico());
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(itemId.getInv_fisico());
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(itemId.getNivel());
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(itemId.getDiferencia());//varianza en volumen
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(0);//varianza en volumen de Recepciones
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(0);//varianza acumulada en volumen de Recepciones
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(itemId.getVarianza());
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;

                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(varianzaAcum1);
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(ventasAcum1);
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;
                datoscellTanque1 = datosTanque1.createCell(i1);
                datoscellTanque1.setCellValue(porcentajeVarAcum1);
                datoscellTanque1.setCellStyle(styleNumber);
                i1++;

                /*Al Final*/
                j1++;
            }
            if (itemId.getTanque_id() == 2) {
                datosTanque2 = sheetTanque2.createRow(j2);

                i2 = 0;
                fecha = itemId.getFecha().toString();
                varianzaAcum2 = varianzaAcum2 + itemId.getDiferencia();
                ventasAcum2 = ventasAcum2 + itemId.getVentas();
                porcentajeVarAcum2 = (varianzaAcum2 / ventasAcum2) * 100;

                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(fecha.substring(8));
                datoscellTanque2.setCellStyle(styleFecha);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(itemId.getInicial());
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(itemId.getCompras());
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(itemId.getCompras());
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(itemId.getVentas());
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(itemId.getAjuste());
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(itemId.getInv_teorico());
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(itemId.getInv_fisico());
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(itemId.getNivel());
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(itemId.getDiferencia());//varianza en volumen
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(0);//varianza en volumen de Recepciones
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(0);//varianza acumulada en volumen de Recepciones
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(itemId.getVarianza());
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;

                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(varianzaAcum2);
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(ventasAcum2);
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;
                datoscellTanque2 = datosTanque2.createCell(i2);
                datoscellTanque2.setCellValue(porcentajeVarAcum2);
                datoscellTanque2.setCellStyle(styleNumber);
                i2++;

                /*Al Final*/
                j2++;
            }
            if (itemId.getTanque_id() == 3) {
                datosTanque3 = sheetTanque3.createRow(j3);

                i3 = 0;
                fecha = itemId.getFecha().toString();
                varianzaAcum3 = varianzaAcum3 + itemId.getDiferencia();
                ventasAcum3 = ventasAcum3 + itemId.getVentas();
                porcentajeVarAcum3 = (varianzaAcum3 / ventasAcum3) * 100;

                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(fecha.substring(8));
                datoscellTanque3.setCellStyle(styleFecha);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(itemId.getInicial());
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(itemId.getCompras());
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(itemId.getCompras());
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(itemId.getVentas());
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(itemId.getAjuste());
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(itemId.getInv_teorico());
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(itemId.getInv_fisico());
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(itemId.getNivel());
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(itemId.getDiferencia());//varianza en volumen
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(0);//varianza en volumen de Recepciones
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(0);//varianza acumulada en volumen de Recepciones
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(itemId.getVarianza());
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;

                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(varianzaAcum3);
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(ventasAcum3);
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;
                datoscellTanque3 = datosTanque3.createCell(i3);
                datoscellTanque3.setCellValue(porcentajeVarAcum3);
                datoscellTanque3.setCellStyle(styleNumber);
                i3++;

                /*Al Final*/
                j3++;
            }
            if (itemId.getTanque_id() == 4) {
                datosTanque4 = sheetTanque4.createRow(j4);

                i4 = 0;
                fecha = itemId.getFecha().toString();
                varianzaAcum4 = varianzaAcum4 + itemId.getDiferencia();
                ventasAcum4 = ventasAcum4 + itemId.getVentas();
                porcentajeVarAcum4 = (varianzaAcum4 / ventasAcum4) * 100;

                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(fecha.substring(8));
                datoscellTanque4.setCellStyle(styleFecha);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(itemId.getInicial());
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(itemId.getCompras());
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(itemId.getCompras());
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(itemId.getVentas());
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(itemId.getAjuste());
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(itemId.getInv_teorico());
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(itemId.getInv_fisico());
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(itemId.getNivel());
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(itemId.getDiferencia());//varianza en volumen
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(0);//varianza en volumen de Recepciones
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(0);//varianza acumulada en volumen de Recepciones
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(itemId.getVarianza());
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;

                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(varianzaAcum4);
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(ventasAcum4);
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;
                datoscellTanque4 = datosTanque4.createCell(i4);
                datoscellTanque4.setCellValue(porcentajeVarAcum4);
                datoscellTanque4.setCellStyle(styleNumber);
                i4++;

                /*Al Final*/
                j4++;
            }
        }
        return workbook;
    }

    protected void setMerge(XSSFSheet sheet, int numRow, int untilRow, int numCol, int untilCol, boolean border) {
        CellRangeAddress cellMerge = new CellRangeAddress(numRow, untilRow, numCol, untilCol);
        sheet.addMergedRegion(cellMerge);
        if (border) {
            setBordersToMergedCells(sheet, cellMerge);
        }
    }

    private XSSFCellStyle crearStilo(XSSFWorkbook workbook, boolean border, boolean leftb, boolean rigthb, boolean topb, boolean bottomb, boolean font, boolean negrita,
            short colorF, Integer sizeF, boolean vertical, boolean ajustar, boolean fondo, String fontTitle) {
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
