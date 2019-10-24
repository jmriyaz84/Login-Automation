package com.login.test.framework.helpers;

import cucumber.api.DataTable;
import gherkin.formatter.model.DataTableRow;

import java.util.List;

public class GherkinsDataSource {

	public static String readValuesFromDataSource(DataTable dataTable,
			int rowNum, int colNum) {
		List<DataTableRow> dataTableRows = dataTable.getGherkinRows();
		List<String> listOfColValues = dataTableRows.get(rowNum).getCells();
		return listOfColValues.get(colNum).trim();

    }

    public static int getTotalRowsInDataTable(DataTable dataTable) {
        List<DataTableRow> dataTableRows = dataTable.getGherkinRows();
        return dataTableRows.size();

	}
}
