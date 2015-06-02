package ryrycipe.util;

import java.util.Properties;
import java.util.prefs.Preferences;


/**
 * Helper functions for handling {@link java.util.Locale}s.
 */
public class LocaleUtil {

    /**
     * Stock all user's preferences concerning the application.
     */
    private static Preferences prefs = Preferences.userNodeForPackage(LocaleUtil.class);

    private LocaleUtil() {}

    /**
     * Return the current application's language.
     *
     * @return The language currently used by the application.
     */
    public static String getLanguage() {
        return prefs.get("language", "en");
    }


    /**
     * Change the current application's language if supported.
     *
     * @param lang New application's language to setup.
     */
    public static void setLanguage(String lang) {
        Properties parameters = PropertiesUtil.loadProperties(
            LocaleUtil.class.getClassLoader().getResource("parameters.properties")
        );

        if (parameters != null) {
            if (parameters.getProperty("language.supported").contains(lang))
                prefs.put("language", lang);
        }
    }
}
