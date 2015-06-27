package ryrycipe.model.manager;

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

    private CategoryManager categoryManager = new CategoryManager();

    @Test
    public void testFind() throws UnsupportedLanguageException {
        LanguageUtil.setLanguage("en");
        DBConnection.update();

        // Create some categories
        Category armorTest = new Category(1, "Armor", "Medium Armor");
        Category jewelTest = new Category(2, "Jewel");
        Category weaponTest = new Category(9, "Weapon", "Melee", 2);
        Category ammoTest = new Category(5, "Ammo", "One-handed ranged weapon");

        // Verify if the category manager find correct categories
        assertThat(armorTest.compareTo(categoryManager.find(1)), is(0));
        assertThat(jewelTest.compareTo(categoryManager.find(2)), is(0));
        assertThat(weaponTest.compareTo(categoryManager.find(9)), is(0));
        assertThat(ammoTest.compareTo(categoryManager.find(5)), is(0));
    }
}