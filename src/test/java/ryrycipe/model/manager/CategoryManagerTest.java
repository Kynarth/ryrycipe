package ryrycipe.model.manager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ryrycipe.exception.UnsupportedLanguageException;
import ryrycipe.model.Category;
import ryrycipe.util.DBConnection;
import ryrycipe.util.LanguageUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test {@link CategoryManager} class.
 */
public class CategoryManagerTest {

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
    public void testFind() throws Exception {
        // Create some categories
        Category armorTest = new Category(4, "Armor", "Medium Armor");
        Category jewelTest = new Category(3, "Jewel");
        Category weaponTest = new Category(5, "Weapon", "Melee", 2);
        Category ammoTest = new Category(10, "Ammo", "One-handed ranged weapon");

        // Verify if the category manager find correct categories
        assertThat(CategoryManager.find(4), is(armorTest));
        assertThat(CategoryManager.find(3), is(jewelTest));
        assertThat(CategoryManager.find(5), is(weaponTest));
        assertThat(CategoryManager.find(10), is(ammoTest));

        // Verify method return empty Category if wrong id
        assertThat(CategoryManager.find(50), is(new Category()));
    }
}