package ryrycipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ryrycipe.controller.RecipeCreatorController;
import ryrycipe.util.LocaleUtil;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

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

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Locale locale;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Ryrycipe");

        this.locale = new Locale(LocaleUtil.getLanguage());

        initialize();
        showRecipeCreator();

        this.primaryStage.show();
    }

    /**
     * Load the application's main UI.
     *
     * @throws IOException In case where the fxml file isn't found.
     */
    private void initialize() throws IOException {
        // Load the main layout from fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("view/RyrycipeView.fxml"));
        loader.setResources(ResourceBundle.getBundle("lang", locale));
        rootLayout = loader.load();

        // Create a scene with the loaded layout
        Scene scene = new Scene(rootLayout, 700, 445);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Load the interface for creating recipes.
     *
     * @throws IOException In case where the fxml file isn't found.
     */
    public void showRecipeCreator() throws IOException {
        // Load RecipeCreator fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("view/RecipeCreator.fxml"));
        loader.setResources(ResourceBundle.getBundle("lang", locale));
        SplitPane recipeCreator = loader.load();

        // Get the corresponding controller
        RecipeCreatorController controller = loader.getController();
        controller.setMainApp(this);

        rootLayout.setCenter(recipeCreator);
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
}
