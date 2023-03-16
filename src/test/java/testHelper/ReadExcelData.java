package testHelper;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ReadExcelData {
	private String filePath;
	private int sheetIndex;

	ReadExcelData(String filePath, int sheetIndex) {
		this.filePath = filePath;
		this.sheetIndex = sheetIndex;
	}

	private XSSFSheet getSheet() throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
		return sheet;
	}

	public Map<String, Map<String, String>> getExcelAsMap() throws IOException {
		XSSFSheet sheet = getSheet();
		Map<String, Map<String, String>> completeSheetData = new HashMap<String, Map<String, String>>();
		List<String> columnHeader = new ArrayList<String>();
		Row row = sheet.getRow(0);
		Iterator<Cell> cellIterator = row.cellIterator();
		while (cellIterator.hasNext()) {
			columnHeader.add(cellIterator.next().getStringCellValue());
		}
		int rowCount = row.getLastCellNum();
		int columnCount = row.getLastCellNum();
		for (int i = 1; i <= rowCount; i++) {
			Map<String, String> singleRowData = new HashMap<String, String>();
			Row row1 = sheet.getRow(i);
			for (int j = 0; j < columnCount; j++) {
				Cell cell = row1.getCell(j);
				singleRowData.put(columnHeader.get(j), getCellValueAsString(cell));
			}
			completeSheetData.put(String.valueOf(i), singleRowData);
		}
		return completeSheetData;
	}

	public String getCellValueAsString(Cell cell) {
		String cellValue = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			cellValue = String.valueOf(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			cellValue = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			cellValue = cell.getCellFormula();
		case Cell.CELL_TYPE_BLANK:
			cellValue = "BLANK";
		default:
			cellValue = "DEFAULT";
		}
		return cellValue;
	}

	public String getSheetName(int index) throws IOException {
		FileInputStream file = new FileInputStream(filePath);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		String sheet = workbook.getSheetName(index);
		return sheet;
	}

	public int getSheetCount() throws IOException {
		FileInputStream file = new FileInputStream(filePath);
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		int sheetCount = workbook.getNumberOfSheets();
		return sheetCount;
	}

	public int totolColumnCount() throws IOException {
		XSSFSheet sheet = getSheet();
		Row row = sheet.getRow(0);
		int lastColumnNum = row.getLastCellNum();
		return lastColumnNum;
	}
}
