package ryrycipe.util;

import java.util.Properties;
import java.util.prefs.Preferences;


/**
 * Helper functions for handling locales.
 */
public class LocaleUtil {

    private static Preferences prefs = Preferences.userNodeForPackage(LocaleUtil.class);

    private LocaleUtil() {

    }

    /**
     * Return the current application's language.
     *
     * @return The language currently used by the application.
     */
    public static String getLanguage() {
        return prefs.get("language", "en");
    }

    public static void setLanguage(String lang) {
        Properties parameters = PropertiesUtil.loadProperties("src/main/resources/parameters.properties");


        if (parameters != null) {
            if (parameters.getProperty("language.supported").contains(lang))
                prefs.put("language", lang);
        }
    }
}
