package sample;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class MinimalCount {
    public static void main(String[] args) throws IOException {
        calculate();
    }

    public static Sheet sheet;

    private static InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = Main.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public static void loadExcelData() throws IOException {
        String fileName = "data.xlsx"; // replace with your file name
        InputStream is = MinimalCount.getFileFromResourceAsStream(fileName);
        // Create Workbook instance holding reference to .xlsx file
        Workbook workbook = new XSSFWorkbook(is);

        // Get first/desired sheet from the workbook
        sheet = workbook.getSheetAt(0);

        is.close();
    }


    public static void calculate() throws IOException {
        List<String> lista = new ArrayList<>();

        loadExcelData();
        // Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        Row row = null;


        DataFormatter dataFormatter = new DataFormatter();
        // from 5.2.0 on the DataFormatter can set to use cached values for formula cells


        while (rowIterator.hasNext()) {
            try {
                row = rowIterator.next();

                lista.addAll(parse(dataFormatter.formatCellValue(row.getCell(1))));
                lista.addAll(parse(dataFormatter.formatCellValue(row.getCell(2))));
                lista.addAll(parse(dataFormatter.formatCellValue(row.getCell(3))));

//                lista.addAll(parse(row.getCell(1) != null ? row.getCell(1).getStringCellValue() : ""));
//                lista.addAll(parse(row.getCell(2) != null ? row.getCell(2).getStringCellValue() : ""));
//                lista.addAll(parse(row.getCell(3) != null ? row.getCell(3).getStringCellValue() : ""));
            } catch (Exception e) {
                System.out.println(row.getRowNum());
            }
        }


        String shortestString = Collections.max(lista, Comparator.comparing(String::length));

        System.out.println("Longest string: " + shortestString);
        System.out.println("Longest string count: " + shortestString.length());
    }

    public static List parse(String inputString) throws Exception {

        if(inputString.isEmpty())
            return new ArrayList();

        if (inputString.contains("?"))
            throw new Exception();

        String[] strings = inputString.split("[.,]");

        return Arrays.stream(strings).map(String::trim).collect(Collectors.toList());

    }

}
