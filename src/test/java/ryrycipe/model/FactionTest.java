package ryrycipe.model;

import javafx.application.Platform;
import javafx.scene.image.Image;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test {@link Faction} class.
 */
public class FactionTest {

    private Faction faction = new Faction("Matis", "BK_matis.png");

    @BeforeClass
    public static void setUpClass() throws Exception {
        try {
            FxToolkit.registerPrimaryStage();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetImage() throws Exception {
        Platform.runLater(() -> {
            Image factionImage = faction.getImage();
            Image testImage = new Image("/images/backgrounds/BK_matis.png");

            assertThat(factionImage, is(testImage));
        });
    }
}