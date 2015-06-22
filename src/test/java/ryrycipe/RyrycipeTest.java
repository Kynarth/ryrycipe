package ryrycipe;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

/**
 * Unit tests about main class: {@link Ryrycipe}.
 */
public class RyrycipeTest extends FxRobot {

    private static Stage primaryStage;

    @BeforeClass
    public static void setUp() {
        try {
            primaryStage = FxToolkit.registerPrimaryStage();
            FxToolkit.setupApplication(Ryrycipe.class);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if app's name is correct.
     */
    @Test
    public void testAppName() {
        assert primaryStage.getTitle().equals("Ryrycipe");
    }

    /**
     * Check if app's icon is correct.
     */
    @Test
    public void testAppIcon() {
        Image appIcon = primaryStage.getIcons().get(0);
        Image testIcon = new Image("/images/logo.png");
        for (int i = 0; i < appIcon.getWidth(); i++)
        {
            for (int j = 0; j < appIcon.getHeight(); j++)
            {
                assert appIcon.getPixelReader().getColor(i, j).equals(testIcon.getPixelReader().getColor(i, j));
            }
        }
    }
}