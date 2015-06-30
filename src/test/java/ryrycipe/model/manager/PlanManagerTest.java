package ryrycipe.model.manager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ryrycipe.exception.UnsupportedLanguageException;
import ryrycipe.model.Plan;
import ryrycipe.util.DBConnection;
import ryrycipe.util.LanguageUtil;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
    public void testFind() {
        CategoryManager categoryManager = new CategoryManager();
        ComponentManager componentManager = new ComponentManager();

        // Create some plans
        Plan normalSword = new Plan(
            126, "Sword", "Normal", "MW_sword.png",
            categoryManager.find(2),
            componentManager.findPlanComponents(126)
        );

        Plan mediumHeavyVest = new Plan(
            41, "Heavy vest", "Medium", "AR_gilet.png",
            categoryManager.find(8),
            componentManager.findPlanComponents(41)
        );

        Plan highDiadem = new Plan(
            132, "Diadem", "High", "PA_diadem.png",
            categoryManager.find(3),
            componentManager.findPlanComponents(132)
        );

        Plan riflePiercingAmmo = new Plan(
            15, "Rifle piercing ammo", "Normal", "RW_rifle.png",
            categoryManager.find(1),
            componentManager.findPlanComponents(15)
        );

        // Verify that correct plans are founded
        assertThat(planManager.find("Sword", "Normal"), is(normalSword));
        assertThat(planManager.find("Heavy vest", "Medium"), is(mediumHeavyVest));
        assertThat(planManager.find("Diadem", "High"), is(highDiadem));
        assertThat(planManager.find("Rifle piercing ammo", "Normal"), is(riflePiercingAmmo));

        // Verify that method returns empty Plan object if incorrect name or quality are passed
        assertThat(planManager.find("Sword", ""), is(new Plan()));
        assertThat(planManager.find("", "Normal"), is(new Plan()));
        assertThat(planManager.find("", ""), is(new Plan()));
    }

    @Test
    public void testFindAllPlans() {
        // Get list of plans by quality
        List<Plan> normalPlans = planManager.findAllPlans("Normal");
        List<Plan> mediumPlans = planManager.findAllPlans("Medium");
        List<Plan> highPlans = planManager.findAllPlans("High");

        // Verify that obtained lists have correct number of plans and that they are not empty
        assertThat(normalPlans.size(), is(59));
        normalPlans.forEach(plan -> assertThat(plan.getName(), is(notNullValue())));

        assertThat(mediumPlans.size(), is(41));
        mediumPlans.forEach(plan -> assertThat(plan.getName(), is(notNullValue())));

        assertThat(highPlans.size(), is(41));
        highPlans.forEach(plan -> assertThat(plan.getName(), is(notNullValue())));

        // Verify that providing incorrect quality, the method returns an empty list
        assertThat(planManager.findAllPlans("").isEmpty(), is(true));
    }
}