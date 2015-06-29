package ryrycipe.model.manager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ryrycipe.exception.UnsupportedLanguageException;
import ryrycipe.util.DBConnection;
import ryrycipe.util.LanguageUtil;

/**
 * Test {@link PlanManager} class.
 */
public class PlanManagerTest {

    private static String language = LanguageUtil.getLanguage();

    private PlanManager planManager = new PlanManager();

    @BeforeClass
    public static void setUpClass() throws UnsupportedLanguageException {
        // Setup english database if not already set
        if (!language.equals("en")) {
            LanguageUtil.setLanguage("en");
            DBConnection.update();
        }
    }

    @AfterClass
    public static void tearDownClass() throws UnsupportedLanguageException {
        // Setup previous language if changed
        if (!language.equals("en")) {
            LanguageUtil.setLanguage(language);
        }
    }

    @Test
    public void testFind() throws Exception {

    }

    @Test
    public void testFindAllPlans() throws Exception {

    }
}