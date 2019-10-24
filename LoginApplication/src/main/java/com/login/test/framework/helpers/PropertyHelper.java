package com.login.test.framework.helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by s746032 on 18/11/2015.
 */
public class PropertyHelper {
    private static final String PROPERTY_FILE_PATH = "src/main/resources/config.properties";
    private static PropertyHelper propertyHelper;
    private static Properties properties;

    private PropertyHelper() throws Exception {
        properties = new Properties();
        FileInputStream input = new FileInputStream(PROPERTY_FILE_PATH);
        try {
            properties.load(input);
        } catch (IOException ioe) {
            throw new Exception("There was a problem in reading the property file \n" + ioe.getMessage());
        }
        properties.putAll(System.getProperties());
    }

    public static String getProperty(String property) throws Exception {
        if (properties == null) {
            new PropertyHelper();
        }
        return properties.getProperty(property);
    }

    public static String getBaseUrl() throws Exception {
        return "http://localhost:3000";
    }

}
