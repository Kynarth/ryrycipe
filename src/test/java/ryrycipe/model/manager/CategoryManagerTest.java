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

    private CategoryManager categoryManager = new CategoryManager();

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
        // Create some categories
        Category armorTest = new Category(4, "Armor", "Medium Armor");
        Category jewelTest = new Category(10, "Jewel");
        Category weaponTest = new Category(3, "Weapon", "Melee", 2);
        Category ammoTest = new Category(11, "Ammo", "One-handed ranged weapon");

        // Verify if the category manager find correct categories
        assertThat(armorTest, is(categoryManager.find(4)));
        assertThat(jewelTest, is(categoryManager.find(10)));
        assertThat(weaponTest, is(categoryManager.find(3)));
        assertThat(ammoTest, is(categoryManager.find(11)));
    }
}