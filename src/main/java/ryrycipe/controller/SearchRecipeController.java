package ryrycipe.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Controller for the SearchRecipe view.
 */
public class SearchRecipeController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(SearchRecipeController.class.getName());


    /**
     * {@link TabPane} that manage tabs displaying users recipes.
     */
    @FXML
    private TabPane recipeTabPane;

    /**
     * Contains all RecipeComponents description the plan's recipe.
     */
    @FXML
    private VBox planRecipeContainer;

    /**
     * Display selected plan's component with associated comment
     */
    @FXML
    private Tab planTab;

    /**
     * {@link TextArea} containing plan's comment
     */
    public TextArea commentArea;

    /**
     * Display selected plan's stats
     */
    @FXML
    private Tab planStatsTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences prefs = Preferences.userNodeForPackage(Ryrycipe.class);
        File recipesFolder = new File(prefs.get("recipeFolder", ""));

        // Load tab with recipes from last selected folder to save user's recipes
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/ryrycipe/view/RecipesTab.fxml"));
            loader.setResources(resources);
            Tab tab = loader.load();
            tab.setClosable(false);  // Default tab can't be closed

            RecipesTabController controller = loader.getController();
            controller.getRecipesTab().setText(resources.getString("search.tab.local.name"));
            controller.setSearchController(this);
            controller.setRecipesFolder(recipesFolder);

            tab.setUserData(controller); // to use his controller later

            recipeTabPane.getTabs().add(tab);

            if (recipesFolder.exists()) {
                controller.ListRecipes();
            } else {  // User hasn't yet chosen a directory
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(resources.getString("dialog.norecipedir.title"));
                alert.setHeaderText(resources.getString("dialog.norecipedir.header"));
                alert.setContentText(resources.getString("dialog.norecipedir.content"));

                LOGGER.warn("No recipes found.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public TabPane getRecipeTabPane() {
        return recipeTabPane;
    }

    public VBox getPlanRecipeContainer() {
        return planRecipeContainer;
    }

    public TextArea getCommentArea() {
        return commentArea;
    }
}
