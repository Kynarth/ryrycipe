package ryrycipe.model.manager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ryrycipe.util.DBConnection;
import ryrycipe.util.LanguageUtil;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test for {@link MaterialManager} class.
 */
public class MaterialManagerTest {

    private static String language = LanguageUtil.getLanguage();

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Setup english database if not already set
        if (!language.equals("en")) {
            LanguageUtil.setLanguage("en");
            DBConnection.update();
        }
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Setup previous language if changed
        if (!language.equals("en")) {
            LanguageUtil.setLanguage(language);
        }
    }

    @Test
    public void testFilter() throws Exception {
        // Create some filter's parameters
        Map<String, String> firstFilterParams = new HashMap<>();
        firstFilterParams.put("qualityLvl", "250");
        firstFilterParams.put("foraged", "Foraged");
        firstFilterParams.put("quartered", null);
        firstFilterParams.put("component", "mpftMpG");  // Grip
        firstFilterParams.put("quality", "Basic");
        firstFilterParams.put("faction", "Generic");

        Map<String, String> secondFilterParams = new HashMap<>();
        secondFilterParams.put("qualityLvl", "1");
        secondFilterParams.put("foraged", "Foraged");
        secondFilterParams.put("quartered", "Quartered");
        secondFilterParams.put("component", "mpftMpE");  // Explosive
        secondFilterParams.put("quality", "Choice");
        secondFilterParams.put("faction", "Matis");

        Map<String, String> thirdFilterParams = new HashMap<>();
        thirdFilterParams.put("qualityLvl", "78");
        thirdFilterParams.put("foraged", null);
        thirdFilterParams.put("quartered", "Quartered");
        thirdFilterParams.put("component", "mpftMpRI");  // Lining
        thirdFilterParams.put("quality", "Supreme");
        thirdFilterParams.put("faction", "Prime");

        Map<String, String> lastFilterParams = new HashMap<>();
        lastFilterParams.put("qualityLvl", "101");
        lastFilterParams.put("foraged", null);
        lastFilterParams.put("quartered", null);
        lastFilterParams.put("component", "mpftMpM");  // Shaft
        lastFilterParams.put("quality", "Excellent");
        lastFilterParams.put("faction", "Zoraï");

        // Verify that MaterialManager's filter method return correct list of materials
        assertThat(MaterialManager.filter(firstFilterParams).isEmpty(), is(false));
        assertThat(MaterialManager.filter(secondFilterParams).isEmpty(), is(false));
        assertThat(MaterialManager.filter(thirdFilterParams).isEmpty(), is(false));
        assertThat(MaterialManager.filter(lastFilterParams).isEmpty(), is(true));
    }
}