package ryrycipe.util;


import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Helper functions for handling properties files.
 */
public class PropertiesUtil {

    private PropertiesUtil() {

    }

    /**
     * Load properties file in the a Properties object.
     *
     * @return A Properties object.
     */
    @Nullable
    public static Properties loadProperties(String filePath) {
        InputStream inputStream = null;
        Properties properties = new Properties();

        try {
            inputStream = new FileInputStream(filePath);
            properties.load(inputStream);

            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.err.println("An error occurred while loading properties file");
        return null;
    }
}
