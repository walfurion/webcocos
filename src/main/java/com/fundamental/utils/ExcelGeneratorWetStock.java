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
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Allan G.
 */
public class ExcelGeneratorWetStock {
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

            /*Se crea el subtitulos combinados APLICA SOLO PARA MTD*/
            XSSFRow subcombinado = sheet.createRow(5);
//            int firstRow int lastRow int firstCol int lastCol
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 4));
            XSSFCell subcombinadoCell = subcombinado.createCell(0);
            subcombinadoCell.setCellValue("Reconciliacion de los Inventarios");
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

            XSSFRow datos = sheet.createRow(7);;
            XSSFCell datoscell;
            Integer ii; //COLUMNAS
            Integer j = 7; //FILAS
            ii = 0;
            Integer filas = lista.size();
            Date fecpivot = new Date();
            int k = 0;
//            for(GenericRptWetStock item : lista.getItemIds()){
            for (GenericRptWetStock itemId : lista.getItemIds()) {
                if(!itemId.getFecha().equals(fecpivot)){
                    fecpivot = itemId.getFecha();
                    System.out.println("jjjjjjjj 0 "+j);
                    datos = sheet.createRow(j);
                    ii = 0;
                    j++;
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
                                                        "Varianza Acumulada", "Venas Acumuladas", "Varianza Acumulada (%) sobre Ventas"};
//            List<String> titulosColumnasTanque2 = new ArrayList<String>();
//            String[] titulosHijosTanque2 = new String[]{"Fecha", "Inventario Inicial", "Volumen Facturado", "Volumen Recibido en Tanque", "Ventas", 
//                                                        "Ajuste de Transferencias", "Inventario Teorico", "Inventario Fisico", "Nivel de Agua en Tanque", "Varianza en Volumen",
//                                                        "Varianza en Volumen de Recepciones", "Varianza Acumulada en Volumen de Recepciones", "Varianza (%) Sobre Ventas", 
//                                                        "Varianza Acumulada", "Venas Acumuladas", "Varianza Acumulada (%) sobre Ventas"};
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
                    sheetTanque1= workbook.createSheet(tituloshojasHijosManualTanque);
                }
                if (tituloshojasHijosManualTanque.equals("TANQUE 2")) {
                    sheetTanque2= workbook.createSheet(tituloshojasHijosManualTanque);
                }
                if (tituloshojasHijosManualTanque.equals("TANQUE 3")) {
                    sheetTanque3= workbook.createSheet(tituloshojasHijosManualTanque);
                }
                if (tituloshojasHijosManualTanque.equals("TANQUE 4")) {
                    sheetTanque4= workbook.createSheet(tituloshojasHijosManualTanque);
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
            Double varianzaAcum = 0.0;
            Double ventasAcum = 0.0;
            Double porcentajeVarAcum = 0.0;

            for (GenericRptWetStock itemId : lista.getItemIds()) {
                if (itemId.getProducto_id() == 1) {
                    datosTanque1 = sheetTanque1.createRow(j1);

                    i1 = 0;
                    fecha = itemId.getFecha().toString();
                    varianzaAcum = varianzaAcum+itemId.getDiferencia();
                    ventasAcum = ventasAcum+itemId.getVentas();
                    porcentajeVarAcum = (varianzaAcum/ventasAcum)*100;
                    
                    
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
                    datoscellTanque1.setCellValue(varianzaAcum);
                    datoscellTanque1.setCellStyle(styleNumber);
                    i1++;
                    datoscellTanque1 = datosTanque1.createCell(i1);
                    datoscellTanque1.setCellValue(ventasAcum);
                    datoscellTanque1.setCellStyle(styleNumber);
                    i1++;
                    datoscellTanque1 = datosTanque1.createCell(i1);
                    datoscellTanque1.setCellValue(porcentajeVarAcum);
                    datoscellTanque1.setCellStyle(styleNumber);
                    i1++;

                    /*Al Final*/
                    j1++;
                }
                if (itemId.getProducto_id() == 2) {
                    datosTanque2 = sheetTanque2.createRow(j2);

                    i2 = 0;
                    fecha = itemId.getFecha().toString();
                    varianzaAcum = varianzaAcum+itemId.getDiferencia();
                    ventasAcum = ventasAcum+itemId.getVentas();
                    porcentajeVarAcum = (varianzaAcum/ventasAcum)*100;                    
                    
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
                    datoscellTanque2.setCellValue(varianzaAcum);
                    datoscellTanque2.setCellStyle(styleNumber);
                    i2++;
                    datoscellTanque2 = datosTanque2.createCell(i2);
                    datoscellTanque2.setCellValue(ventasAcum);
                    datoscellTanque2.setCellStyle(styleNumber);
                    i2++;
                    datoscellTanque2 = datosTanque2.createCell(i2);
                    datoscellTanque2.setCellValue(porcentajeVarAcum);
                    datoscellTanque2.setCellStyle(styleNumber);
                    i2++;

                    /*Al Final*/
                    j2++;
                }
                if (itemId.getProducto_id() == 3) {
                    datosTanque3 = sheetTanque3.createRow(j3);

                    i3 = 0;
                    fecha = itemId.getFecha().toString();
                    varianzaAcum = varianzaAcum+itemId.getDiferencia();
                    ventasAcum = ventasAcum+itemId.getVentas();
                    porcentajeVarAcum = (varianzaAcum/ventasAcum)*100;
                    
                    
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
                    datoscellTanque3.setCellValue(varianzaAcum);
                    datoscellTanque3.setCellStyle(styleNumber);
                    i3++;
                    datoscellTanque3 = datosTanque3.createCell(i3);
                    datoscellTanque3.setCellValue(ventasAcum);
                    datoscellTanque3.setCellStyle(styleNumber);
                    i3++;
                    datoscellTanque3 = datosTanque3.createCell(i3);
                    datoscellTanque3.setCellValue(porcentajeVarAcum);
                    datoscellTanque3.setCellStyle(styleNumber);
                    i3++;

                    /*Al Final*/
                    j3++;
                }
                if (itemId.getProducto_id() == 4) {
                    datosTanque4 = sheetTanque4.createRow(j4);

                    i4 = 0;
                    fecha = itemId.getFecha().toString();
                    varianzaAcum = varianzaAcum+itemId.getDiferencia();
                    ventasAcum = ventasAcum+itemId.getVentas();
                    porcentajeVarAcum = (varianzaAcum/ventasAcum)*100;
                    
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
                    datoscellTanque4.setCellValue(varianzaAcum);
                    datoscellTanque4.setCellStyle(styleNumber);
                    i4++;
                    datoscellTanque4 = datosTanque4.createCell(i4);
                    datoscellTanque4.setCellValue(ventasAcum);
                    datoscellTanque4.setCellStyle(styleNumber);
                    i4++;
                    datoscellTanque4 = datosTanque4.createCell(i4);
                    datoscellTanque4.setCellValue(porcentajeVarAcum);
                    datoscellTanque4.setCellStyle(styleNumber);
                    i4++;

                    /*Al Final*/
                    j4++;
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
