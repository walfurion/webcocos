/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fundamental.utils;

//import java.awt.Color;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.CellReference;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFBorderFormatting;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Henry Barrientos
 */
public class XlsxReportGenerator {

    private List<CellRangeAddress> listRegions = new ArrayList();
    private List<String> listTitleRegions = new ArrayList();
    private List<String> listFormulas = new ArrayList();
    private boolean readonlyData = false;

//    public XlsxReportGenerator(String title, Map<String, String> filters, String[] colTitles, List<String[]> data) throws IOException {
//        generate(title, filters, colTitles, data);
    public XlsxReportGenerator() {
    }
    
    public XSSFWorkbook generate(String title, Map<String, String> filters, String[] colTitles, List<String[]> data,Integer [] colTypes) throws IOException {
        int rowCount = 0, colCount = 0;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(title.toLowerCase());

        XSSFRow row = sheet.createRow(rowCount++);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue(title);
        XSSFCellStyle cellStyle = getCellStyleMaintitle(workbook);
        XSSFCellStyle cellPercentStyle = getCellStyleMaintitle(workbook);
        cell.setCellStyle(cellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colTitles.length - 1));
        RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, sheet.getMergedRegion(0), sheet, workbook);

        rowCount++;
        for (Entry<String, String> filter : filters.entrySet()) {
            row = sheet.createRow(rowCount++);
            cell = row.createCell(0);
            cell.setCellValue(filter.getKey().concat(":"));
            cell.setCellStyle(getFontBold(workbook));
            cell = row.createCell(1);
            cell.setCellValue(filter.getValue());
        }
        
        rowCount++;
        row = sheet.createRow(rowCount++);
        colCount = 0;
        cellStyle = getCellStyleForCellTitle(workbook);
        for (String colTitle : colTitles) {
            cell = row.createCell(colCount);
            cell.setCellValue(colTitle);
            cell.setCellStyle(cellStyle);
            colCount++;
        }

        cellStyle = getCellStyleForCellValue(workbook);
        cellPercentStyle = getCellStyleForCellValue(workbook);
        for (String[] rowData : data) {
            row = sheet.createRow(rowCount++);
            colCount = 0;
            for (String value : rowData) {
            cell = row.createCell(colCount);
            if(value == null)
            {
              cell.setCellValue("");
              colCount++;
              continue;
            }
            switch (colTypes[colCount])
            {
                case CELL_TYPE_NUMERIC:
                    cell.setCellValue(Double.parseDouble(value.replace(",", "")));
                    XSSFCellStyle cellStyle2 = getCellStyleForCellValue(workbook);
                    cellStyle2.setDataFormat(workbook.createDataFormat().getFormat("#,###,##0.00"));
                    cell.setCellStyle(cellStyle2);
                    break;
                case CELL_TYPE_STRING:
                    cell.setCellValue(value);
                    break;
                case 3:
                    cellPercentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
                    cell.setCellValue(Double.parseDouble(value));
                    cell.setCellStyle(cellPercentStyle);
                    break;
                case 4:
                    if(isnumber(value.replace(" ", "")))
                        cell.setCellValue(Integer.parseInt(value.replace(" ", "")));
                    else 
                        cell.setCellValue("");
                    break;
                case 50:
                    cell.setCellValue(Double.parseDouble(value.replace(",", "")));
                    XSSFCellStyle cellStyle3 = getCellStyleForCellValue(workbook);
                    cellStyle3.setDataFormat(workbook.createDataFormat().getFormat("#,###,##0.00000"));
                    cell.setCellStyle(cellStyle3);
                    break;
            }
            colCount++;
            
            }
        }
        return workbook;
    }
    
    public XSSFWorkbook generate(XSSFWorkbook workbook, String title, Map<String, String> filters, String[] colTitles, List<String[]> data, Integer[] colTypes) throws IOException {
        int rowCount = 0, colCount = 0;
//        XSSFWorkbook 
                workbook = (workbook==null) ? new XSSFWorkbook() : workbook;
        XSSFSheet sheet = workbook.createSheet(title.toLowerCase());
        if (readonlyData) {
            sheet.protectSheet("estacionescoco");
        }

        XSSFRow row = sheet.createRow(rowCount++);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue(title);
        XSSFCellStyle cellStyle = getCellStyleMaintitle(workbook), 
                cellPercentStyle,
                cellStandardStyle;
        cell.setCellStyle(cellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colTitles.length - 1));
        RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, sheet.getMergedRegion(0), sheet, workbook);

        rowCount++;
        for (Entry<String, String> filter : filters.entrySet()) {
            row = sheet.createRow(rowCount++);
            cell = row.createCell(0);
            cell.setCellValue(filter.getKey().concat(":"));
            cell.setCellStyle(getFontBold(workbook));
            cell = row.createCell(1);
            cell.setCellValue(filter.getValue());
        }

        CellRangeAddress cra;
//cse = workbook.createCellStyle();
//        List<XSSFColor> miscolores = new ArrayList(Arrays.asList(
//                new XSSFColor(java.awt.Color.BLUE), new XSSFColor(java.awt.Color.CYAN), new XSSFColor(java.awt.Color.GRAY), new XSSFColor(java.awt.Color.GREEN), new XSSFColor(java.awt.Color.LIGHT_GRAY )
//        ));
//        cse.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        cellStyle = getCellStyleForCellTitle(workbook);
        cellStyle.setLocked(!readonlyData);
        for (int i = 0; i < listRegions.size(); i++) {
            if (i == 0) {
                rowCount++;
                row = sheet.createRow(rowCount);
                colCount = 0;
            }
            cra = listRegions.get(i);   //las columnas ya estan configuradas.
            cra.setFirstRow(rowCount);
            cra.setLastRow(rowCount);
System.out.println("" + cra.getFirstColumn() +"; "+cra.getLastColumn());
            sheet.addMergedRegion(cra);
            cell = row.createCell(cra.getFirstColumn());
            cell.setCellValue(listTitleRegions.get(i).toUpperCase());
            cell.setCellStyle(cellStyle);
            RegionUtil.setBorderTop(CellStyle.BORDER_THICK, sheet.getMergedRegion(i + 1), sheet, workbook);
            RegionUtil.setBorderRight(CellStyle.BORDER_THICK, sheet.getMergedRegion(i + 1), sheet, workbook);
            RegionUtil.setBorderBottom(CellStyle.BORDER_THICK, sheet.getMergedRegion(i + 1), sheet, workbook);
            RegionUtil.setBorderLeft(CellStyle.BORDER_THICK, sheet.getMergedRegion(i + 1), sheet, workbook);
        }

        rowCount++;
        row = sheet.createRow(rowCount++);
        colCount = 0;
        cellStyle = getCellStyleForCellTitle(workbook);
        cellStyle.setLocked(!readonlyData);
        for (String colTitle : colTitles) {
            cell = row.createCell(colCount);
            cell.setCellValue(colTitle);
            cell.setCellStyle(cellStyle);
            colCount++;
        }

        Integer startRowData = rowCount + 1;
        cellStandardStyle = getCellStyleForCellValue(workbook);
        cellStyle = getCellStyleForCellValue(workbook);
        cellStyle.setDataFormat(workbook.createDataFormat().getFormat("#,###,###.00"));
        cellPercentStyle = getCellStyleForCellValue(workbook);
        cellPercentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
        for (String[] rowData : data) {
            row = sheet.createRow(rowCount++);
            colCount = 0;
            for (String value : rowData) {
                cell = row.createCell(colCount);
                sheet.autoSizeColumn(cell.getColumnIndex());
                if (value == null) {
                    cell.setCellValue("");
                    cell.setCellStyle(cellStandardStyle);
                    colCount++;
                    continue;
                }
                switch (colTypes[colCount]) {
                    case CELL_TYPE_NUMERIC:
                        cell.setCellValue((value.isEmpty()) ? Integer.parseInt("0") : Double.parseDouble(value.replaceAll(",", "")));
                        cell.setCellStyle(cellStyle);
                        break;
                    case CELL_TYPE_STRING:
                        cell.setCellValue(value);
                        cell.setCellStyle(cellStandardStyle);
                        break;
                    case 3:
                        cell.setCellValue(Double.parseDouble(value.replaceAll(",", "")));
                        cell.setCellStyle(cellPercentStyle);
                        break;
                    case 4:
                        cell.setCellValue(Integer.parseInt(value.replace(" ", "")));
                        cell.setCellStyle(cellStandardStyle);
                        break;
                }
                colCount++;
            }
        }

        rowCount++;
        colCount = 0;
        String colName, range;
        row = sheet.createRow(rowCount);
        for (int i = 0; i < listFormulas.size(); i++) {
            if (!listFormulas.get(i).isEmpty()) {
                cell = row.createCell(colCount);
                sheet.autoSizeColumn(cell.getColumnIndex());
                colName = CellReference.convertNumToColString(i);
                range = colName.concat(startRowData.toString())
                        .concat(":")
                        .concat(colName).concat(Integer.toString(rowCount - 1));
                cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                cell.setCellFormula("SUM(" + range + ")");
                cell.setCellStyle(cellStyle);
            }
            colCount++;
        }

        return workbook;
    }

    private XSSFCellStyle getCellStyleForCellTitle(XSSFWorkbook workbook) {
        XSSFCellStyle result = workbook.createCellStyle();
        result.setBorderTop(XSSFBorderFormatting.BORDER_THICK);
        result.setBorderRight(XSSFBorderFormatting.BORDER_THICK);
        result.setBorderBottom(XSSFBorderFormatting.BORDER_THICK);
        result.setBorderLeft(XSSFBorderFormatting.BORDER_THICK);
//        result.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(new XSSFColor(java.awt.Color.RED));
        result.setFont(font);
        return result;
    }

    private XSSFCellStyle getCellStyleForCellValue(XSSFWorkbook workbook) {
        XSSFCellStyle result = workbook.createCellStyle();
//        result.setBorderTop(XSSFBorderFormatting.BORDER_THIN);
//        result.setBorderRight(XSSFBorderFormatting.BORDER_THIN);
//        result.setBorderBottom(XSSFBorderFormatting.BORDER_THIN);
//        result.setBorderLeft(XSSFBorderFormatting.BORDER_THIN);
        return result;
    }

    private XSSFCellStyle getFontBold(XSSFWorkbook workbook) {
        XSSFCellStyle result = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        result.setFont(font);
        return result;
    }

    private XSSFCellStyle getCellStyleMaintitle(XSSFWorkbook workbook) {
        XSSFCellStyle result = workbook.createCellStyle();
        result.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//        result.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//        result.setFillForegroundColor(new XSSFColor(Color.WHITE));
        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeight(20);
        font.setColor(new XSSFColor(java.awt.Color.RED));
        result.setFont(font);
        return result;
    }


    public XSSFWorkbook generateWSM(XSSFWorkbook workbook, String[] data) throws IOException {
        int rowCount = 0, colCount = 0;
//        XSSFWorkbook 
                workbook = (workbook==null) ? new XSSFWorkbook() : workbook;
        XSSFSheet sheet = workbook.createSheet("Input Data Sheet");

        XSSFRow row = sheet.createRow(rowCount++);
        row = sheet.createRow(rowCount++);
        
        XSSFCell cell = row.createCell(0);
        cell = row.createCell(1);
        cell.setCellValue("Hoja de Cálculo: Reconciliación de los Inventarios de los Combustibles");
        
        XSSFCellStyle cellStyle = getCellStyleMaintitleWSM(workbook);
        XSSFCellStyle cellStyle2 = getCellStyleMaintitleWSM2(workbook);
        XSSFCellStyle cellStyle3 = getCellStyleMaintitleWSM3(workbook);
                
        row = sheet.createRow(rowCount++);  cell = row.createCell(0);
        cell = row.createCell(1);   cell.setCellStyle(cellStyle);   cell.setCellValue("Unidad Operativa:");
        cell = row.createCell(2);   cell.setCellStyle(cellStyle2);  cell.setCellValue(data[0]);
        row = sheet.createRow(rowCount++);  cell = row.createCell(0);
        cell = row.createCell(1);   cell.setCellStyle(cellStyle);   cell.setCellValue("Nombre E/S:");
        cell = row.createCell(2);   cell.setCellStyle(cellStyle2);  cell.setCellValue(data[1]);
        row = sheet.createRow(rowCount++);  cell = row.createCell(0);
        cell = row.createCell(1);   cell.setCellStyle(cellStyle);   cell.setCellValue("Codigo E/S:");
        cell = row.createCell(2);   cell.setCellStyle(cellStyle2);  cell.setCellValue(data[2]);
        row = sheet.createRow(rowCount++);  cell = row.createCell(0);
        cell = row.createCell(1);   cell.setCellStyle(cellStyle);   cell.setCellValue("Mes:");
        cell = row.createCell(2);   cell.setCellStyle(cellStyle2);  cell.setCellValue(data[3]);
        row = sheet.createRow(rowCount++);  cell = row.createCell(0);
        cell = row.createCell(1);   cell.setCellStyle(cellStyle);   cell.setCellValue("Año:");
        cell = row.createCell(2);   cell.setCellStyle(cellStyle2);  cell.setCellValue(data[4]);
        row = sheet.createRow(rowCount++);  cell = row.createCell(0);
        cell = row.createCell(1);   cell.setCellStyle(cellStyle);   cell.setCellValue("Unidad de Vol. (AG, Lt. M3):");
        cell = row.createCell(2);   cell.setCellStyle(cellStyle2);  cell.setCellValue(data[5]);

        row = sheet.createRow(rowCount++);  row = sheet.createRow(rowCount++);  row = sheet.createRow(rowCount++);  row = sheet.createRow(rowCount++);  row = sheet.createRow(rowCount++);

row.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));        
cell = row.createCell(0);   
        cell = row.createCell(1);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Fecha dd/mm/aa");
        cell = row.createCell(2);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Inventario inicial");
        cell = row.createCell(3);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Volumen recibido en tanque");
        cell = row.createCell(4);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Ventas");
        cell = row.createCell(5);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Ajuste de transferencia");
        cell = row.createCell(6);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Inventario fisico");
        cell = row.createCell(7);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Lectura veeder root");
        cell = row.createCell(8);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Nivel de agua");
        cell = row.createCell(9);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Piloto");
        cell = row.createCell(10);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Unidad #");
        cell = row.createCell(11);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Comp #");
        cell = row.createCell(12);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Factura/Traslado #");
        cell = row.createCell(13);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Cantidad en volumen facturado");
        cell = row.createCell(14);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Pulgadas");
        cell = row.createCell(15);   cell.setCellStyle(cellStyle3);   cell.setCellValue("Galones");
        
        
//        cell.setCellValue(title);
//        XSSFCellStyle cellStyle = getCellStyleMaintitle(workbook), 
//                cellPercentStyle,
//                cellStandardStyle;
//        cell.setCellStyle(cellStyle);
        
        return workbook;
    }
    
    private XSSFCellStyle getCellStyleMaintitleWSM(XSSFWorkbook workbook) {
        XSSFCellStyle result = workbook.createCellStyle();
        result.setAlignment(XSSFCellStyle.ALIGN_LEFT);
//        result.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//        result.setFillForegroundColor(new XSSFColor(Color.WHITE));
        XSSFFont font = workbook.createFont();
//        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeight(10);
//        font.setColor(new XSSFColor(java.awt.Color.RED));
        font.setColor(new XSSFColor(java.awt.Color.BLACK));
        result.setFont(font);
        result.setBorderTop(XSSFBorderFormatting.BORDER_THIN);
        result.setBorderRight(XSSFBorderFormatting.BORDER_THIN);
        result.setBorderBottom(XSSFBorderFormatting.BORDER_THIN);
        result.setBorderLeft(XSSFBorderFormatting.BORDER_THIN);
        return result;
    }
    
    private XSSFCellStyle getCellStyleMaintitleWSM2(XSSFWorkbook workbook) {
        XSSFCellStyle result = workbook.createCellStyle();
        result.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        result.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        result.setFillForegroundColor(new XSSFColor(Color.YELLOW));
        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeight(10);
//        font.setColor(new XSSFColor(java.awt.Color.RED));
        font.setColor(new XSSFColor(java.awt.Color.BLUE));
        result.setFont(font);
        result.setBorderTop(XSSFBorderFormatting.BORDER_THIN);
        result.setBorderRight(XSSFBorderFormatting.BORDER_THIN);
        result.setBorderBottom(XSSFBorderFormatting.BORDER_THIN);
        result.setBorderLeft(XSSFBorderFormatting.BORDER_THIN);
        return result;
    }

    private XSSFCellStyle getCellStyleMaintitleWSM3(XSSFWorkbook workbook) {
        XSSFCellStyle result = workbook.createCellStyle();
        result.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//        result.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//        result.setFillForegroundColor(new XSSFColor(Color.YELLOW));
        XSSFFont font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontHeight(10);
//        font.setColor(new XSSFColor(java.awt.Color.RED));
        font.setColor(new XSSFColor(java.awt.Color.BLACK));
        result.setFont(font);
        result.setBorderTop(XSSFBorderFormatting.BORDER_THIN);
        result.setBorderRight(XSSFBorderFormatting.BORDER_THIN);
        result.setBorderBottom(XSSFBorderFormatting.BORDER_THIN);
        result.setBorderLeft(XSSFBorderFormatting.BORDER_THIN);
        return result;
    }

    
    
    public void setListRegions(List<CellRangeAddress> listRegions) {
        this.listRegions = listRegions;
    }

    public void setListTitleRegions(List<String> listTitleRegions) {
        this.listTitleRegions = listTitleRegions;
    }

    public List<String> getListFormulas() {
        return listFormulas;
    }

    public void setListFormulas(List<String> listFormulas) {
        this.listFormulas = listFormulas;
    }

    public void setReadonlyData(boolean readonlyData) {
        this.readonlyData = readonlyData;
    }
    
    private boolean isnumber (String text){
        try {
        int i  =  Integer.parseInt(text.replace(" ", ""));
            return true;
        }
        catch (Exception e){
            return false;
        }
    
    }

}
