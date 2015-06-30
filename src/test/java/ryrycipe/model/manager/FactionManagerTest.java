package ryrycipe.model.manager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ryrycipe.exception.UnsupportedLanguageException;
import ryrycipe.model.Faction;
import ryrycipe.util.DBConnection;
import ryrycipe.util.LanguageUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test {@link FactionManager} class.
 */
public class FactionManagerTest {

    private static String language = LanguageUtil.getLanguage();

    private FactionManager factionManager = new FactionManager();

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
    public void testFind() {
        // Create some factions
        Faction generic = new Faction("Generic", "BK_generic.png");
        Faction prime = new Faction("Prime", "BK_primes.png");
        Faction matis = new Faction("Matis", "BK_matis.png");
        Faction fyros = new Faction("Fyros", "BK_fyros.png");
        Faction tryker = new Faction("Tryker", "BK_tryker.png");
        Faction zorai = new Faction("Zoraï", "BK_zoraï.png");

        // verify that FactionManager retrieve correct factions
        assertThat(factionManager.find("Generic"), is(generic));
        assertThat(factionManager.find("Prime"), is(prime));
        assertThat(factionManager.find("Matis"), is(matis));
        assertThat(factionManager.find("Fyros"), is(fyros));
        assertThat(factionManager.find("Tryker"), is(tryker));
        assertThat(factionManager.find("Zoraï"), is(zorai));
    }
}