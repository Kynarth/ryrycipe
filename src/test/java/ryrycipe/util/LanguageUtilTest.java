package ryrycipe.util;

import org.junit.Test;
import ryrycipe.exception.UnsupportedLanguageException;

import java.util.Locale;
import java.util.prefs.Preferences;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test the {@link LanguageUtil} class.
 */
public class LanguageUtilTest {

    private Preferences prefs = Preferences.userNodeForPackage(LanguageUtil.class);

    /**
     * Test {@link LanguageUtil#getLanguage()} without preferences.
     */
    @Test
    public void testGetLanguageDefault() {
        prefs.remove("language");
        assertThat(LanguageUtil.getLanguage(), is("en"));
    }

    /**
     * Test {@link LanguageUtil#getLocale()}} without preferences.
     */
    @Test
    public void testGetLocaleDefault() {
        prefs.remove("language");
        assertThat(LanguageUtil.getLocale(), is(new Locale("en")));
    }

    @Test
    public void testSetValidLanguage() throws UnsupportedLanguageException {
        LanguageUtil.setLanguage("fr");

        assertThat(prefs.get("language", ""), is("fr"));
    }

    @Test (expected = UnsupportedLanguageException.class)
    public void testSetInvalidLanguage() throws UnsupportedLanguageException {
        LanguageUtil.setLanguage("es");

        // Check if language in preferences is still fr
        assertThat(prefs.get("language", ""), is("fr"));
    }
}