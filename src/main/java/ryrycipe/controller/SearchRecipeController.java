package ryrycipe.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType;

/**
 * Controller for the SearchRecipe view.
 */
public class SearchRecipeController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(SearchRecipeController.class.getName());

    @FXML
    private ListView localRecipesView;

    private Ryrycipe mainApp;
    private ResourceBundle resources;
    private ObservableList<String> localRecipes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    /**
     * Add each local recipes in the localRecipesView ListView to get selected.
     */
    public void searchLocalRecipes() {
        File recipesFolder = new File(mainApp.getSavedRecipesFolder());

        if (recipesFolder.exists()) {
            File[] recipes = recipesFolder.listFiles();

            // User had created recipes
            if (recipes != null && recipes.length > 0) {
                for (File recipe: recipes) {
                    if (recipe.isFile()) {
                        localRecipes.add(recipe.getName());
                    }
                }

                localRecipesView.setItems(localRecipes);
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(resources.getString("dialog.norecipedir.title"));
                alert.setHeaderText(resources.getString("dialog.norecipedir.header"));
                alert.setContentText(resources.getString("dialog.norecipedir.content"));

                LOGGER.warn("No recipes found.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(resources.getString("dialog.norecipedir.title"));
            alert.setHeaderText(resources.getString("dialog.norecipedir.header"));
            alert.setContentText(resources.getString("dialog.norecipedir.content"));

            LOGGER.warn("No recipes found.");
            alert.showAndWait();
        }

        LOGGER.info("Local recipes have been listed.");

    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }
}
