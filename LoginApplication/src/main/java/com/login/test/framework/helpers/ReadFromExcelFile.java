package com.login.test.framework.helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadFromExcelFile {
	FileInputStream inputStream = null;
	XSSFWorkbook wb = null;

	public ReadFromExcelFile(String fileName) {
		try {
			inputStream = new FileInputStream(new File(fileName));
		} catch (FileNotFoundException e1) {
			inputStream = null;
		}
		try {
			wb = new XSSFWorkbook(inputStream);
		} catch (IOException e) {
			wb = null;
		}
	}

	public List<String> getValuesFromExcel(String sheetName, int colNumber) throws Exception {

		XSSFSheet ws = wb.getSheet(sheetName);
		XSSFCell cell = null;
		int rowNum = ws.getLastRowNum() + 1;
		// int colNum = ws.getRow(0).getLastCellNum();

		List<String> listOfColValues = new ArrayList<String>();
		for (int i = 1; i < rowNum; i++) {
			XSSFRow row = ws.getRow(i);

			cell = row.getCell(colNumber);
			if (cell != null) {
				String value = cell.getStringCellValue();
				listOfColValues.add(value);
			}

			// System.out.println(value);

		}

		inputStream.close();
		return listOfColValues;

	}


	/*public void writeValuesToExcel(String sheetName, String valueToWrite, int rowIndex, int colIndex) throws Exception {
		XSSFSheet ws = wb.getSheet(sheetName);
		XSSFCell cell = null;
		XSSFRow row = null;
		row = ws.getRow(rowIndex);
		cell = row.createCell(colIndex);
		cell.setCellValue(valueToWrite);
	}

	public void closeAllStrems(String fileName) throws Exception {
		FileOutputStream outputStream = new FileOutputStream(fileName);
		//write data in the excel file
		wb.write(outputStream);
		//close output stream
		//inputStream.close();
		outputStream.close();
	}

	public  void writeValuesToExistingExcel(String fileName, String sheetName, String valueToWrite, int rowIndex, int colIndex) throws Exception {
		try {
			inputStream = new FileInputStream(new File(fileName));
			wb = new XSSFWorkbook(inputStream);
			XSSFSheet ws = wb.getSheet(sheetName);
			XSSFCell cell = null;
			cell = ws.getRow(rowIndex).getCell(colIndex);

			cell.setCellValue(valueToWrite);

			inputStream.close(); //Close the InputStream

			FileOutputStream output_file = new FileOutputStream(new File(fileName));  //Open FileOutputStream to write updates

			wb.write(output_file); //write changes

			output_file.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
