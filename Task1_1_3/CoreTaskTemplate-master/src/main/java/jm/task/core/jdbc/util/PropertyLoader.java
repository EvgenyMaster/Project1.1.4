package jm.task.core.jdbc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader extends Properties {

    private static final Properties properties;

    static {
        InputStream propertyPath;
        properties = new Properties();
        try {
            propertyPath = PropertyLoader.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(propertyPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLoadedProperty(String key) {
        return properties.getProperty(key);
    }
}