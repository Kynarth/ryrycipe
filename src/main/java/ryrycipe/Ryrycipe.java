package ryrycipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.controller.RecipeCreatorController;
import ryrycipe.controller.RyrycipeController;
import ryrycipe.util.LocaleUtil;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * Path to folder where user's recipes are saved.
     */
    private String savedRecipesFolder;

    private Locale locale;
    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * Current create pane to load it if user navigates through pane.
     */
    private SplitPane recipeCreatorPane;

    /**
     * Current search pane to load it if user navigates through pane.
     */
    private SplitPane recipeSearchPane;

    RyrycipeController ryrycipeController;
    RecipeCreatorController creatorController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Ryrycipe");

        this.locale = new Locale(LocaleUtil.getLanguage());

        createSavedRecipesDir();

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

            // Create a scene with the loaded layout
            Scene scene = new Scene(rootLayout, 700, 445);
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
            creatorController.initializeSpecificToolBar();

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

    /**
     * Create a directory to save user's recipes.
     */
    private void createSavedRecipesDir() {
        try {
            String executablePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            Pattern pattern = Pattern.compile("/[^/]+\\.\\w{3}$");
            Matcher matcher = pattern.matcher(executablePath);
            if (matcher.find()) {
                savedRecipesFolder = matcher.replaceFirst("/recipes/");
            } else { // case when use IDE to launch application
                savedRecipesFolder = executablePath + "recipes/";
            }

            File dir = new File(savedRecipesFolder);
            if (!dir.exists()) {
                if (dir.mkdir())
                    LOGGER.info("Succeed to create {} directory", savedRecipesFolder);
                else
                    LOGGER.warn("Could not create {} directory", savedRecipesFolder);
            }
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }
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

    public String getSavedRecipesFolder() {
        return savedRecipesFolder;
    }

    public RyrycipeController getRyrycipeController() {
        return ryrycipeController;
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
