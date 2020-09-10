package com.boot.common.helper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class ExcelHelper {

    private static final Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

    public static void generateSimpleExcel(String sheetName, String[] titles, List<String[]> data, OutputStream out)
            throws IOException {
        HSSFWorkbook wb = generateSimpleExcel(sheetName, titles, data);
        wb.write(out);
        wb.close();
        out.flush();
        out.close();
    }

    public static HSSFWorkbook generateSimpleExcel(String sheetName, String[] titles, List<String[]> data)
            throws IOException {

        // create a workbook
        HSSFWorkbook wb = new HSSFWorkbook();
        // create a sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        int i = 0;
        // set content in center position
        HSSFCellStyle centerStyle = wb.createCellStyle();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);

        // create a title
        HSSFRow row = sheet.createRow(i++);
        for (int j = 0; j < titles.length; j++) {
            HSSFCell cell = row.createCell(j);
            cell.setCellValue(titles[j]);
            cell.setCellStyle(centerStyle);
        }

        if (CollectionUtils.isNotEmpty(data)) {
            for (String[] d : data) {
                HSSFRow dataRow = sheet.createRow(i++);
                for (int j = 0; j < d.length; j++) {
                    HSSFCell cell = dataRow.createCell(j);
                    cell.setCellValue(d[j]);
                    cell.setCellStyle(centerStyle);
                }
            }
        }

        return wb;
    }

    public static List<List<String>> readExcel(Sheet sheet) {
        List<List<String>> data = new ArrayList<>();

        for (int i = 0, rowNum = sheet.getLastRowNum(); i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            List<String> list = new ArrayList<>();
            if (row != null) {
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    // when cell is null
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        list.add("");
                        continue;
                    }

                    String value;
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            value = String.valueOf(cell.getNumericCellValue());
                            break;
                        case STRING:
                            value = String.valueOf(cell.getStringCellValue());
                            break;
                        case BOOLEAN:
                            value = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            value = String.valueOf(cell.getCellFormula());
                            break;
                        default:
                            value = "";
                            break;
                    }
                    list.add(value);
                }
            }
            data.add(list);
        }

        return data;
    }

    public static List<List<String>> readExcel(InputStream is, String sheetName) throws Exception {
        Workbook workBook = null;
        Sheet sheet;
        try {
            workBook = WorkbookFactory.create(is);
            sheet = workBook.getSheet(sheetName);
            return readExcel(sheet);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (workBook != null) {
                    workBook.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public static List<List<String>> readExcel(InputStream is, int sheetIndex) throws Exception {
        Workbook workBook = null;
        Sheet sheet;
        try {
            workBook = WorkbookFactory.create(is);
            sheet = workBook.getSheetAt(sheetIndex);
            return readExcel(sheet);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (workBook != null) {
                    workBook.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
