package ryrycipe.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import ryrycipe.util.LanguageUtil;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test {@link CreatorPaneController}.
 */
public class CreatorPaneControllerTest extends FxRobot {

    private static ResourceBundle resources;

    @BeforeClass
    public static void setUp() {
        try {
            resources = ResourceBundle.getBundle("lang", new Locale(LanguageUtil.getLanguage()));
            FxToolkit.registerPrimaryStage();

            // Load fxml file corresponding to CreatorPaneController
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CreatorPaneController.class.getResource("/ryrycipe/view/CreatorPane.fxml"));
            loader.setResources(resources);
            SplitPane creatorPane = loader.load();

            // Setup loaded fxml file in the scene and show it
            FxToolkit.registerStage(Stage::new);
            FxToolkit.setupStage(stage -> stage.setScene(new Scene(creatorPane)));
            FxToolkit.showStage();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPlanQualityItems() {
        ComboBox<String> planQuality = nodes().lookup("#planQualityCB").queryFirst();
        assertThat(
            planQuality.getItems().toArray(),
            is(resources.getString("combobox.quality.items").split(","))
        );
    }
}