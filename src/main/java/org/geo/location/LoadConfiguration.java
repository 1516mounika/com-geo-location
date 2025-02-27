package org.geo.location;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadConfiguration {

    private static final String PROPERTIES_FILE = "src/main/resources/config.properties";
    private static Properties properties = new Properties();

    static {
        try {
            FileInputStream inputStream = new FileInputStream(PROPERTIES_FILE);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getApiUrl() {
        return properties.getProperty("API_URL");
    }

    public static String getApiKey() {
        return properties.getProperty("API_KEY");
    }
}
