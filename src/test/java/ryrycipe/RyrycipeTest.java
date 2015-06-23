package ryrycipe;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import ryrycipe.util.LanguageUtil;

import java.util.Locale;
import java.util.concurrent.TimeoutException;
import java.util.prefs.Preferences;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Unit tests about main class: {@link Ryrycipe}.
 */
public class RyrycipeTest extends FxRobot {

    private static Stage primaryStage;
    private static Ryrycipe ryrycipe;

    @BeforeClass
    public static void setUp() {
        // Remove previous language preference
        Preferences prefs = Preferences.userNodeForPackage(LanguageUtil.class);
        prefs.remove("language");

        try {
            primaryStage = FxToolkit.registerPrimaryStage();
            ryrycipe = (Ryrycipe) FxToolkit.setupApplication(Ryrycipe.class);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAppName() {
        assertThat(primaryStage.getTitle(), is("Ryrycipe"));
    }

    @Test
    public void testAppIcon() {
        Image appIcon = primaryStage.getIcons().get(0);
        Image testIcon = new Image("/images/logo.png");
        for (int i = 0; i < appIcon.getWidth(); i++)
        {
            for (int j = 0; j < appIcon.getHeight(); j++)
            {
                assertThat(
                    appIcon.getPixelReader().getColor(i, j), is(testIcon.getPixelReader().getColor(i, j))
                );
            }
        }
    }

    @Test
    public void testAppLanguage() {
        assertThat(ryrycipe.getLocale(), is(new Locale("en")));
    }
}