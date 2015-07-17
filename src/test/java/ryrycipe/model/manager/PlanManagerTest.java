package ryrycipe.model.manager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ryrycipe.model.Plan;
import ryrycipe.util.DBConnection;
import ryrycipe.util.LanguageUtil;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Test {@link PlanManager} class.
 */
public class PlanManagerTest {

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
        // Create some plans
        Plan normalSword = new Plan(
            126, "Sword", "Normal", "MW_sword.png",
            CategoryManager.find(2),
            ComponentManager.findPlanComponents(126)
        );

        Plan mediumHeavyVest = new Plan(
            41, "Heavy vest", "Medium", "AR_gilet.png",
            CategoryManager.find(8),
            ComponentManager.findPlanComponents(41)
        );

        Plan highDiadem = new Plan(
            132, "Diadem", "High", "PA_diadem.png",
            CategoryManager.find(3),
            ComponentManager.findPlanComponents(132)
        );

        Plan riflePiercingAmmo = new Plan(
            15, "Rifle piercing ammo", "Normal", "RW_rifle.png",
            CategoryManager.find(1),
            ComponentManager.findPlanComponents(15)
        );

        // Verify that correct plans are founded
        assertThat(PlanManager.find("Sword", "Normal"), is(normalSword));
        assertThat(PlanManager.find("Heavy vest", "Medium"), is(mediumHeavyVest));
        assertThat(PlanManager.find("Diadem", "High"), is(highDiadem));
        assertThat(PlanManager.find("Rifle piercing ammo", "Normal"), is(riflePiercingAmmo));

        // Verify that method returns empty Plan object if incorrect name or quality are passed
        assertThat(PlanManager.find("Sword", ""), is(new Plan()));
        assertThat(PlanManager.find("", "Normal"), is(new Plan()));
        assertThat(PlanManager.find("", ""), is(new Plan()));
    }

    @Test
    public void testFindAllPlans() throws Exception {
        // Get list of plans by quality
        List<Plan> normalPlans = PlanManager.findAllPlans("Normal");
        List<Plan> mediumPlans = PlanManager.findAllPlans("Medium");
        List<Plan> highPlans = PlanManager.findAllPlans("High");

        // Verify that obtained lists have correct number of plans and that they are not empty
        assertThat(normalPlans.size(), is(59));
        normalPlans.forEach(plan -> assertThat(plan.getName(), is(notNullValue())));

        assertThat(mediumPlans.size(), is(41));
        mediumPlans.forEach(plan -> assertThat(plan.getName(), is(notNullValue())));

        assertThat(highPlans.size(), is(41));
        highPlans.forEach(plan -> assertThat(plan.getName(), is(notNullValue())));

        // Verify that providing incorrect quality, the method returns an empty list
        assertThat(PlanManager.findAllPlans("").isEmpty(), is(true));
    }
}