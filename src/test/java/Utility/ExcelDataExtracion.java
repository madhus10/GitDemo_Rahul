package Utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataExtracion {

	public static ArrayList<String> getData(String testcasename, String Sheetname) throws IOException {
		ArrayList<String> al = new ArrayList();

		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\Excel_Data.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		int sheets = workbook.getNumberOfSheets();
		for (int i = 0; i < sheets; i++) {
			if (workbook.getSheetName(i).equalsIgnoreCase(Sheetname)) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				Iterator<Row> row = sheet.iterator();
				Row firstrow = row.next();
				Iterator<Cell> ce = firstrow.iterator();
				int k = 0, col = 0;
				while (ce.hasNext()) {
					Cell value = ce.next();
					if (value.getStringCellValue().equalsIgnoreCase("TestCase")) {
						col = k;
					}
					k++;

				}
				while (row.hasNext()) {
					Row r = row.next();
					if (r.getCell(col).getStringCellValue().equalsIgnoreCase(testcasename)) {
						Iterator<Cell> cv = r.iterator();
						while (cv.hasNext()) {
							Cell c = cv.next();
							if (c.getCellTypeEnum() == CellType.STRING) {
								al.add(c.getStringCellValue());
							} else if (c.getCellTypeEnum() == CellType.NUMERIC) {
								al.add(NumberToTextConverter.toText(c.getNumericCellValue()));
							}

						}
					}
				}
			}

		}
		return al;

	}
}
