package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class with static methods to help with loading in game configuration settings
 *
 * @author Aykut Yilmaz - aykut.yilmaz.u1495@student.hogent.be
 */
public class Configuration {
    
    private static final String PROPERTY_FILE_URI = "config.properties";
    private static final Properties prop = new Properties();

    /**
     * Grabs the database properties out of the database
     * 
     * @return Array with database properties
     */
    public static String[] getDatabaseProperties(){
        String[] dbProps = new String[3];
        dbProps[0] = getProp("DATABASE_URL");
        dbProps[1] = getProp("DATABASE_USERNAME");
        dbProps[2] = getProp("DATABASE_PASSWORD");
        return dbProps;
    }

    /**
     * Grab a properity out of the config file
     * 
     * @param key String with config property
     * @return String with data
     */
    public static String getProp(String key) {
        InputStream inputStream
                = Configuration.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_URI);
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }
}
