package ryrycipe.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.exception.UnsupportedLanguageException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * Helper class handling application's language.
 */
public class LanguageUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(LanguageUtil.class.getName());

    /**
     * User's language is stored in {@link Preferences} to get used at app's startup.
     */
    private static Preferences prefs = Preferences.userNodeForPackage(LanguageUtil.class);

    private LanguageUtil() {}

    /**
     * Give the app's language or english if not defined.
     *
     * @return String - application's language ("en" or "fr")
     */
    public static String getLanguage() {
        return prefs.get("language", "en");
    }

    /**
     * Give the app's locale or english locale if not defined.
     *
     * @return {@link Locale} - application's locale.
     */
    public static Locale getLocale() {
        return new Locale(prefs.get("language", "en"));
    }

    /**
     * Setup a new app's language if it supported.
     *
     * @param language New app's language to setup.
     * @throws UnsupportedLanguageException thrown when user attempt to set a language not supported by the application.
     */
    public static void setLanguage(String language) throws UnsupportedLanguageException {
        InputStream inputStream = LanguageUtil.class.getClassLoader().getResourceAsStream("config.properties");

        if (inputStream != null) {
            try {
                Properties config = new Properties();
                config.load(inputStream);

                // Check if the given language is supported and if it is, add it in preferences
                if (config.getProperty("language.supported").contains(language))
                    prefs.put("language", language);
                else {
                    LOGGER.info("The language: {} isn't supported by the application", language);
                    throw new UnsupportedLanguageException(language);
                }

            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        } else {
            LOGGER.error("Could not find the file: config.properties.");
        }
    }
}
