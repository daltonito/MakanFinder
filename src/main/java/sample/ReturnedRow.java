package sample;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Iterator;

public class ReturnedRow {

    public String moduleType;
    public String hardware;
    public String vehicleGroup;
    public String ecuManufacturer;
    public String cloning;
    public String cloningTools;

    private static Sheet sheet;
    private static DataFormatter dataFormatter = new DataFormatter();

    public static void loadExcelData() throws IOException {

        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("data.xlsx");

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! data.xlsx");
        } else {
            Workbook workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
            inputStream.close();
        }
    }


    public static ReturnedRow retrieveByKeyword(String keyword) {
        ReturnedRow returnedRow = new ReturnedRow();
        Iterator<Row> rowIterator = sheet.iterator();

        while (rowIterator.hasNext()) {

            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                if (dataFormatter.formatCellValue(cell).contains(keyword)) {
                    return populateRowValues(row);
                }
            }
        }

        return returnedRow;
    }

    private static ReturnedRow populateRowValues(Row row) {
        ReturnedRow rtRow = new ReturnedRow();

//        rtRow.moduleType = dataFormatter.formatCellValue(row.getCell(0));
//        rtRow.hardware = dataFormatter.formatCellValue(row.getCell(1));
//        rtRow.vehicleGroup = dataFormatter.formatCellValue(row.getCell(4));
//        rtRow.ecuManufacturer = dataFormatter.formatCellValue(row.getCell(5));
//        rtRow.cloning = dataFormatter.formatCellValue(row.getCell(6));
//        rtRow.cloningTools = dataFormatter.formatCellValue(row.getCell(7));

        rtRow.moduleType = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "";
        rtRow.hardware = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
        rtRow.vehicleGroup = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "";
        rtRow.ecuManufacturer = row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "";
        rtRow.cloning = row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "";
        rtRow.cloningTools = row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "";

        return rtRow;
    }

    public boolean isNoResultFound() throws IllegalAccessException {
        return areAllFieldsNullOrEmpty(this);
    }

    private static boolean areAllFieldsNullOrEmpty(ReturnedRow obj) throws IllegalAccessException {
        for (Field field : obj.getClass().getFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value != null) {
                if (value instanceof String) {
                    String strValue = (String) value;
                    if (!strValue.isEmpty()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }
}
