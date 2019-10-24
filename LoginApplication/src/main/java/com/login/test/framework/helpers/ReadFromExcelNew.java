package com.login.test.framework.helpers;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by S781529 on 22/06/2016.
 * If file is either password protected or the file itself is corrupted.
 * This is only to read a .xlsx file.
 */
public class ReadFromExcelNew {
	FileInputStream inputStream = null;
	Workbook wb = null;

	/**
	 * <pre>
	 * <b>Description : </b>
	 * initialize the required workbook object
	 * &#64;param - none
	 * &#64;return none
	 * &#64;throws - none
	 * &#64;Developed by - s781529
	 * </pre>
	 */
	public ReadFromExcelNew(String fileName) {
		File file = null;

		try {
			file = new File(getClass().getClassLoader().getResource("EndToEndScenario_TestData.xls").toURI());
		} catch (URISyntaxException e) {
			file = null;
		}
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			inputStream =  null;
		}

		try {
			wb = WorkbookFactory.create(inputStream);
		} catch (IOException e) {
			wb = null;
		} catch (InvalidFormatException e) {
			wb = null;
		}
	}

	/**
	 * <pre>
	 * <b>Description : </b>
	 * returns the list of content in a column
	 * &#64;param - none
	 * &#64;return List<String>  - the list of content in a column
	 * &#64;throws - Exception
	 * &#64;Developed by - s781529
	 * </pre>
	 */
	public List<String> getValuesInColumnFromExcel_new(String sheetName, int colNumber) throws Exception {

		Sheet ws = wb.getSheet(sheetName);
		Cell cell = null;
		int rowNum = ws.getLastRowNum() + 1;
		// int colNum = ws.getRow(0).getLastCellNum();

		List<String> listOfColValues = new ArrayList<String>();
		for (int i = 1; i < rowNum; i++) {
			Row row = ws.getRow(i);

			cell = row.getCell(colNumber);
			if (cell != null) {
				String value = cell.getStringCellValue();
				listOfColValues.add(value);
				System.out.println(value);
			}
		}
		inputStream.close();
		return listOfColValues;
	}

	/**
	 * <pre>
	 * <b>Description : </b>
	 * returns the list of content in a Row
	 * &#64;param - none
	 * &#64;return List<String>  - the list of content in a Row
	 * &#64;throws - Exception
	 * &#64;Developed by - s781529
	 * </pre>
	 */
	public List<String> getValuesInRowFromExcel_new(String sheetName, int rowNumber) throws Exception {

		Sheet ws = wb.getSheet(sheetName);
		Cell cell = null;

		Row row = ws.getRow(rowNumber);
		int columnNum = row.getLastCellNum();

		List<String> listOfRowValues = new ArrayList<String>();
		for (int i=0; i < columnNum; i++) {
			cell = row.getCell(i);

			if (cell != null) {
				String value = cell.getStringCellValue();
				listOfRowValues.add(value);
				System.out.println(value);
			}
		}

		inputStream.close();
		return listOfRowValues;
	}
}
