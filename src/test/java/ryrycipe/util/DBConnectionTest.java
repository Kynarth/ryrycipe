package ryrycipe.util;

import org.junit.Test;
import ryrycipe.exception.UnsupportedLanguageException;

import java.sql.SQLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test the {@link DBConnection} class.
 */
public class DBConnectionTest {

    @Test
    public void testConnection() throws SQLException {
        assertThat(DBConnection.getInstance().isValid(100), is(true));
    }

    @Test
    public void testConnectionWithLanguageChange() throws UnsupportedLanguageException, SQLException {
        String currentLanguage = LanguageUtil.getLanguage();
        if (currentLanguage.equals("fr")) {
            LanguageUtil.setLanguage("en");
        } else {
            LanguageUtil.setLanguage("fr");
        }

        DBConnection.update();
        assertThat(DBConnection.getInstance().isValid(100), is(true));
        LanguageUtil.setLanguage(currentLanguage);
    }
}