package com.login.test.framework.helpers;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;

public class ReadServiceConfigFile {

    private static PropertyParser properties;

    private ReadServiceConfigFile() throws Exception {
        String PROPERTY_FILE_PATH = System.getProperty("user.dir") + "\\src\\test\\resources\\serviceConfig\\";
        try {
            properties = new PropertyParser();
            String host = System.getProperty("environment.host");
            if (StringUtils.isBlank(host)) {
                host = PropertyHelper.getProperty("local.host").toLowerCase();
            }
            if (host.startsWith("dev")) {
                PROPERTY_FILE_PATH = PROPERTY_FILE_PATH + "dev_config.properties";
            } else if (host.startsWith("tst")) {
                PROPERTY_FILE_PATH = PROPERTY_FILE_PATH + "tst_config.properties";
            } else if (host.startsWith("stg")) {
                PROPERTY_FILE_PATH = PROPERTY_FILE_PATH + "stg_config.properties";
            } else {
                throw new Exception("Host value : " + host);
            }
            properties.load(new FileInputStream(PROPERTY_FILE_PATH));
        } catch (Exception ex) {
            throw new Exception("There was a problem in reading the property file " + PROPERTY_FILE_PATH + " due to : " + ex.getMessage());
        }
    }

    public static String getProperty(String property) throws Exception {
        if (properties == null) {
            new ReadServiceConfigFile();
        }
        return properties.getProperty(property);
    }

}
