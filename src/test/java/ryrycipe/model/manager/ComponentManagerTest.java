package ryrycipe.model.manager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ryrycipe.exception.UnsupportedLanguageException;
import ryrycipe.model.Component;
import ryrycipe.util.DBConnection;
import ryrycipe.util.LanguageUtil;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

/**
 * Test {@link ComponentManager} class.
 */
public class ComponentManagerTest {

    private static String language = LanguageUtil.getLanguage();

    private ComponentManager componentManager = new ComponentManager();

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
        Component grip = new Component("mpftMpG", "Grip", "ICO_Grip.png");
        Component shaft = new Component("mpftMpM", "Shaft", "ICO_Shaft.png");
        Component magicFocus = new Component("mpftMpMF", "Magic Focus", "ICO_Magic_focus.png");

        assertThat(grip, is(componentManager.find("mpftMpG")));
        assertThat(shaft, is(componentManager.find("mpftMpM")));
        assertThat(magicFocus, is(componentManager.find("mpftMpMF")));

    }

    @Test
    public void testFindPlanComponents() {
        // Create list of components from normal axe, medium bow-pistol and high shield
        List<Component> axeComponents = new ArrayList<>();
        axeComponents.add(new Component("mpftMpL", 25, "Blade", "ICO_Blade.png", 5));
        axeComponents.add(new Component("mpftMpG", 25, "Grip", "ICO_Grip.png", 3));
        axeComponents.add(new Component("mpftMpM", 25, "Shaft", "ICO_Shaft.png", 3));
        axeComponents.add(new Component("mpftMpC", 25, "Counterweight", "ICO_Counterweight.png", 3));

        List<Component> bowPistolComponents = new ArrayList<>();
        bowPistolComponents.add(new Component("mpftMpPE", 94, "Firing Pin", "ICO_Firing_pin.png", 2));
        bowPistolComponents.add(new Component("mpftMpM", 94, "Shaft", "ICO_Shaft.png", 1));
        bowPistolComponents.add(new Component("mpftMpCA", 94, "Barrel", "ICO_barrel.png", 2));
        bowPistolComponents.add(new Component("mpftMpGA", 94, "Trigger", "ICO_trigger.png", 2));

        List<Component> shieldComponents = new ArrayList<>();
        shieldComponents.add(new Component("mpftMpCR", 87, "Armor Shell", "ICO_Armor_shell.png", 11));
        shieldComponents.add(new Component("mpftMpAT", 87, "Armor Clip", "ICO_armor_clip.png", 10));

        // Verify if the components manager find correct lists
        assertThat(axeComponents, containsInAnyOrder(componentManager.findPlanComponents(25).toArray()));
        assertThat(bowPistolComponents, containsInAnyOrder(componentManager.findPlanComponents(94).toArray()));
        assertThat(shieldComponents, containsInAnyOrder(componentManager.findPlanComponents(87).toArray()));
    }

    @Test
    public void testFindMaterialComponents() {
        // Create components corresponding to:
        // Basic Buo Fiber
        List<Component> fiberComponents = new ArrayList<>();
        fiberComponents.add(new Component("mpftMpG", "Grip", "ICO_Grip.png"));
        fiberComponents.add(new Component("mpftMpVE", "Clothes", "ICO_Clothes.png"));

        // Bundle of Fine Abhaya Wood
        List<Component> woodComponents = new ArrayList<>();
        woodComponents.add(new Component("mpftMpCA", "Barrel", "ICO_barrel.png"));
        woodComponents.add(new Component("mpftMpCR", "Armor Shell", "ICO_Armor_shell.png"));

        // Choice Jungle Zun Amber
        List<Component> amberComponents = new ArrayList<>();
        amberComponents.add(new Component("mpftMpED", "Jewel Stone", "ICO_Jewel_stone.png"));
        amberComponents.add(new Component("mpftMpMF", "Magic Focus", "ICO_Magic_focus.png"));

        // Excellent Forest Torbak Fang
        List<Component> fangComponents = new ArrayList<>();
        fangComponents.add(new Component("mpftMpRE", "Stuffing", "ICO_Stuffing.png"));
        fangComponents.add(new Component("mpftMpE", "Explosive", "ICO_Explosive.png"));

        // Supreme Prime Root Patee Wood Node
        List<Component> woodNodeComponents = new ArrayList<>();
        woodNodeComponents.add(new Component("mpftMpH", "Hammer", "ICO_hammer.png"));
        woodNodeComponents.add(new Component("mpftMpC", "Counterweight", "ICO_Counterweight.png"));

        // Portion of Choice Lakes Glue Resin
        List<Component> glueComponents = new ArrayList<>();
        glueComponents.add(new Component("mpftMpRI", "Lining", "ICO_Lining.png"));
        glueComponents.add(new Component("mpftMpEN", "Ammo Jacket", "ICO_Ammo_jacket.png"));

        // Fragment of Supreme Forest Kincher Sting
        List<Component> stingComponents = new ArrayList<>();
        stingComponents.add(new Component("mpftMpL", "Blade", "ICO_Blade.png"));
        stingComponents.add(new Component("mpftMpP", "Point", "ICO_Pointe.png"));

        // Verify if the components manager find correct pairs of components for each materials
        assertThat(
            fiberComponents, containsInAnyOrder(componentManager.findMaterialComponents("m0021dxacb01").toArray())
        );
        assertThat(
            woodComponents, containsInAnyOrder(componentManager.findMaterialComponents("m0001dxacc01").toArray())
        );
        assertThat(
            amberComponents, containsInAnyOrder(componentManager.findMaterialComponents("m0155dxajd01").toArray())
        );
        assertThat(
            fangComponents, containsInAnyOrder(componentManager.findMaterialComponents("m0135ccdfd01").toArray())
        );
        assertThat(
            woodNodeComponents, containsInAnyOrder(componentManager.findMaterialComponents("m0100dxapf01").toArray())
        );
        assertThat(
            glueComponents, containsInAnyOrder(componentManager.findMaterialComponents("m0046dxald01").toArray())
        );
        assertThat(
            stingComponents, containsInAnyOrder(componentManager.findMaterialComponents("m0067ckdfe01").toArray())
        );
    }
}