package com.login.test.framework.helpers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by vivek on 5/25/16.
 */
public class JsonReaderWriter {

	public static void setTestData(String id, String value) throws Exception {

		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(new File("TestData.json").getAbsolutePath()));

			JSONObject js = (JSONObject) obj;
			js.put(id, value); // add the new value for id

			FileWriter file = new FileWriter(new File("TestData.json").getAbsolutePath());
			file.write(js.toJSONString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("\nJSON Object: " + js);
			file.flush();
			file.close();
		} catch (Exception ex) {
			throw new Exception("modify test data file failed, Reason:" + ex.getMessage());
		}
	}

	public static String getTestData(String dataType) {
		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader(new File("TestData.json").getAbsolutePath()));
			JSONObject jsonObject = (JSONObject) obj;

			String desiredContent = (String) jsonObject.get(dataType);
			return desiredContent;
		} catch (Exception ex) {
			return null;
		}
	}


}
