package ryrycipe.util;


import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;


/**
 * Helper functions for handling for {@link Properties}.
 */
public class PropertiesUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class.getName());

    private PropertiesUtil() {}

    /**
     * Load properties file in the a {@link Properties}.
     *
     * @param filePath {@link URL} path to the wanted properties file.
     * @return {@link Properties}.
     */
    @Nullable
    public static Properties loadProperties(URL filePath) {
        InputStreamReader inputStreamReader = null;
        Properties properties = new Properties();

        try {
            inputStreamReader = new InputStreamReader(filePath.openStream());
            properties.load(inputStreamReader);

            return properties;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }

        return null;
    }
}
