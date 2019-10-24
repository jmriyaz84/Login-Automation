package com.login.test.framework.helpers;

import com.login.test.framework.hooks.BrowserManagement;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by S763311 on 07/11/2016.
 */
public class ScenarioTablePrintHelper {

    public static StringBuffer htmlTable = new StringBuffer();


    public static void createHTMLTableHeader(String[] tableHeaders) {
        htmlTable.append("<table border='1'>");
        for (int i = 0; i < tableHeaders.length; i++) {
            htmlTable.append("<th>" + tableHeaders[i] + "</th>");
        }
    }
    public static void createHTMLTableRow(String[] tableRowValues) {
        htmlTable.append("<tr>");
        for (int i = 0; i < tableRowValues.length; i++) {
            htmlTable.append("<td>" + tableRowValues[i] + "</td>");
        }
        htmlTable.append("</tr>");
    }
    public static void createHTML() {
        htmlTable.append("</table>");
        writeToHTMLReport(htmlTable.toString());
    }

    public static void writeToHTMLReport(String input) {
        BrowserManagement.scenario.write(input);
    }
    
    public static void writeToHTMLFile(String fileName) throws IOException{
    	File htmlTemplateFile = new File("target/"+fileName+".html");
    	FileUtils.writeStringToFile(htmlTemplateFile, htmlTable.toString());
    }
}
