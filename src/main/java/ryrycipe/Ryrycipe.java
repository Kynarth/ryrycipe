package ryrycipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.controller.RecipeCreatorController;
import ryrycipe.controller.RyrycipeController;
import ryrycipe.util.LocaleUtil;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * <h1>Application to manage Ryzom's craft plans</h1>
 * <p>
 * This application allows the user to create, read and upload his own recipe.
 * It will calculate the user's plan stats in function of chosen materials. This will allow the user to know which
 * recipe fitting his wishes.
 * </p>
 *
 */
public class Ryrycipe extends Application {

    private final static Logger LOGGER = LoggerFactory.getLogger(Ryrycipe.class.getName());

    /**
     * Application {@link Locale}.
     */
    private Locale locale;

    /**
     * Application {@link Stage}.
     */
    private Stage primaryStage;

    /**
     * Application root layout
     * @see BorderPane
     */
    private BorderPane rootLayout;

    /**
     * Current create pane to load it if user navigates through pane.
     */
    private SplitPane recipeCreatorPane;

    /**
     * Current search pane to load it if user navigates through pane.
     */
    private SplitPane recipeSearchPane;

    /**
     * Reference to {@link RyrycipeController}
     */
    RyrycipeController ryrycipeController;

    /**
     * Reference to {@link RecipeCreatorController}
     */
    RecipeCreatorController creatorController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Ryrycipe");
        this.primaryStage.getIcons().add(new Image("/images/logo.png"));

        this.locale = new Locale(LocaleUtil.getLanguage());

        initialize();
        showRecipeCreator();

        this.primaryStage.show();
    }

    /**
     * Load the application's main UI.
     */
    public void initialize() {
        try {
            // Load the main layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("view/RyrycipeView.fxml"));
            loader.setResources(ResourceBundle.getBundle("lang", locale));
            rootLayout = loader.load();

            ryrycipeController = loader.getController();
            ryrycipeController.setMainApp(this);

            // Create a scene with the loaded layout at preferred user's size
            Preferences prefs = Preferences.userNodeForPackage(Ryrycipe.class);
            Scene scene = new Scene(rootLayout, prefs.getDouble("appWidth", 700), prefs.getDouble("appHeight", 445));
            scene.widthProperty().addListener((observable, oldValue, newValue) -> {
                prefs.putDouble("appWidth", newValue.doubleValue());
            });
            scene.heightProperty().addListener((observable, oldValue, newValue) -> {
                prefs.putDouble("appHeight", newValue.doubleValue());
            });

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException | IllegalStateException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Load the interface for creating recipes.
     */
    public void showRecipeCreator() {
        // Load RecipeCreator fxml file
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("view/RecipeCreator.fxml"));
            loader.setResources(ResourceBundle.getBundle("lang", locale));
            recipeCreatorPane = loader.load();

            // Get the corresponding controller
            creatorController = loader.getController();
            creatorController.setMainApp(this);
            ryrycipeController.initCreatePaneTB();

            rootLayout.setCenter(recipeCreatorPane);
        } catch (IOException | IllegalStateException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Application's launcher
     * @param args Application's parameters
     */
    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public RecipeCreatorController getCreatorController() {
        return creatorController;
    }

    public BorderPane getRootLayout() {
        return rootLayout;
    }

    public SplitPane getRecipeCreatorPane() {
        return recipeCreatorPane;
    }

    public void setRecipeSearchPane(SplitPane recipeSearchPane) {
        this.recipeSearchPane = recipeSearchPane;
    }

    public SplitPane getRecipeSearchPane() {
        return recipeSearchPane;
    }
}
