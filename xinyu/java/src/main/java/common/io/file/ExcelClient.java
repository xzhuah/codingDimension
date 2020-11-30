package common.io.file;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/11/18, 21:55
 * common.io.file in codingDimensionTemplate
 * support xlsx format, may support xls in the future;
 */
public class ExcelClient {
    public static Map<String, List<String>> readExcelTableByColumnAsMapToRow(String filename, String tableName) throws IOException {
        return readExcelTableByColumnAsMapToRow(filename, tableName, 0);
    }

    public static List<Map<String, String>> readExcelTableByColumnAsRowOfMap(String filename, String tableName) throws IOException {
        return readExcelTableByColumnAsRowOfMap(filename, tableName, 0);

    }

    public static Map<String, List<String>> readExcelTableByRowAsMapToColumn(String filename, String tableName) throws IOException {
        return readExcelTableByRowAsMapToColumn(filename, tableName, 0);

    }

    public static List<Map<String, String>> readExcelTableByRowAsColumnOfMap(String filename, String tableName) throws IOException {
        return readExcelTableByRowAsColumnOfMap(filename, tableName, 0);
    }

    public static Map<String, Map<String, String>> readExcelTableByRowAndColAsRowToColMap(String filename, String tableName) throws IOException {
        return readExcelTableByRowAndColAsRowToColMap(filename, tableName, 0, 0);
    }

    public static Map<String, Map<String, String>> readExcelTableByRowAndColAsColToRowMap(String filename, String tableName) throws IOException {
        return readExcelTableByRowAndColAsColToRowMap(filename, tableName, 0, 0);
    }


    public static Map<String, List<String>> readExcelTableByColumnAsMapToRow(String filename, String tableName, int skipColumnNum) throws IOException {
        checkStatus(skipColumnNum >= 0, "Invalid number of skipColumn:" + skipColumnNum);
        throw new RuntimeException("Non implementation");
    }

    public static List<Map<String, String>> readExcelTableByColumnAsRowOfMap(String filename, String tableName, int skipColumnNum) throws IOException {
        checkStatus(skipColumnNum >= 0, "Invalid number of skipColumn:" + skipColumnNum);
        throw new RuntimeException("Non implementation");
    }

    public static Map<String, List<String>> readExcelTableByRowAsMapToColumn(String filename, String tableName, int skipRowNum) throws IOException {
        checkStatus(skipRowNum >= 0, "Invalid number of skipColumn:" + skipRowNum);
        XSSFSheet sheet = getXSSFSheet(filename, tableName);

        Iterator<Row> rawIterator = sheet.rowIterator();

        for (int i = 0; i < skipRowNum; i++) {
            checkStatus(rawIterator.hasNext(), "There is no " + skipRowNum + " rows to skip in table " + tableName + " of file " + filename);
            rawIterator.next();
        }

        Row row = rawIterator.next();
        Iterator<Cell> cellIterator = row.cellIterator();
        List<String> colNames = new ArrayList<>();

        Map<String, List<String>> result = new HashMap<>();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String colName = cell.getStringCellValue();
            colNames.add(colName);
            result.put(colName, new ArrayList<>());
        }

        while (rawIterator.hasNext()) {
            Row valueRow = rawIterator.next();
            Iterator<Cell> valueCellIterator = valueRow.cellIterator();
            for (String colName : colNames) {
                if (valueCellIterator.hasNext()) {
                    Cell cell = valueCellIterator.next();
                    result.get(colName).add(getCellValueAsString(cell));
                } else {
                    result.get(colName).add("");
                }
            }
        }

        return result;

    }

    public static List<Map<String, String>> readExcelTableByRowAsColumnOfMap(String filename, String tableName, int skipRowNum) throws IOException {
        checkStatus(skipRowNum >= 0, "Invalid number of skipColumn:" + skipRowNum);
        throw new RuntimeException("Non implementation");
    }

    public static Map<String, Map<String, String>> readExcelTableByRowAndColAsRowToColMap(String filename, String tableName, int skipColumnNum, int skipRowNum) throws IOException {
        checkStatus(skipColumnNum >= 0, "Invalid number of skipColumn:" + skipColumnNum);
        checkStatus(skipRowNum >= 0, "Invalid number of skipColumn:" + skipRowNum);
        throw new RuntimeException("Non implementation");
    }

    public static Map<String, Map<String, String>> readExcelTableByRowAndColAsColToRowMap(String filename, String tableName, int skipColumnNum, int skipRowNum) throws IOException {
        checkStatus(skipColumnNum >= 1, "Invalid number of skipColumn:" + skipColumnNum);
        checkStatus(skipRowNum >= 1, "Invalid number of skipColumn:" + skipRowNum);
        throw new RuntimeException("Non implementation");
    }

    private static XSSFSheet getXSSFSheet(String filename, String tableName) throws IOException {
        File file = new File(filename);
        try (FileInputStream fis = new FileInputStream(file)) {
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            return wb.getSheet(tableName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue() + "";
            case FORMULA:
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue() + "";
            case BLANK:
            case _NONE:
            case ERROR:
                return "";
            default:
                throw new RuntimeException(cell.getCellType() + " is not supported");
        }
    }

    public static long convertExcelTimeToMilliTimestamp(long excelTime) {
        return (excelTime * 3600L * 24L - 2209161600L) * 1000L;
    }

    public static void main(String[] args) throws IOException {
        Map<String, List<String>> result = readExcelTableByRowAsMapToColumn("C:/my C/Finance/炒股记录表.xlsx", "历史");
        for (String key : result.keySet()) {
            System.out.println(key);
            System.out.println(result.get(key));
        }

    }
}
