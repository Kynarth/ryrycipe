package ryrycipe.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.model.ComponentWrapper;
import ryrycipe.model.Material;
import ryrycipe.model.RecipeWrapper;
import ryrycipe.model.view.MaterialView;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType;

/**
 * Controller for the SearchRecipe view.
 */
public class SearchRecipeController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(SearchRecipeController.class.getName());

    /**
     * Contains all RecipeComponents description the plan's recipe.
     */
    @FXML
    private VBox planRecipeContainer;

    /**
     * {@link ListView} displaying local stored recipes.
     */
    @FXML
    private ListView<RecipeWrapper> localRecipesView;

    /**
     * Reference to the {@link Ryrycipe} object.
     */
    private Ryrycipe mainApp;

    private ResourceBundle resources;
    private ObservableList<RecipeWrapper> localRecipes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    /**
     * Add each local recipes in the localRecipesView ListView to get selected.
     */
    public void searchLocalRecipes() {
        localRecipes.clear();
        File recipesFolder = new File(mainApp.getSavedRecipesFolder());

        if (recipesFolder.exists()) {
            File[] recipes = recipesFolder.listFiles();

            // User had created recipes
            if (recipes != null && recipes.length > 0) {
                for (File recipe: recipes) {
                    if (recipe.isFile()) {
                        loadRecipeFromFile(recipe);
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

    /**
     * Add each recipes from given folder in the localRecipesView ListView to get selected.
     */
    public void searchLocalRecipes(File recipesFolder) {
        localRecipes.clear();
        if (recipesFolder.exists()) {
            File[] recipes = recipesFolder.listFiles();

            // User had created recipes
            if (recipes != null && recipes.length > 0) {
                for (File recipe: recipes) {
                    if (recipe.isFile()) {
                        loadRecipeFromFile(recipe);
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


    /**
     * Load a {@link RecipeWrapper}from given file.
     * @param recipesFile {@link File} containing XML data representing a user's recipe.
     */
    public void loadRecipeFromFile(File recipesFile) {
        try {
            JAXBContext context = JAXBContext.newInstance(RecipeWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            RecipeWrapper wrapper = (RecipeWrapper) unmarshaller.unmarshal(recipesFile);

            localRecipes.add(wrapper);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(resources.getString("dialog.loaddataerror.title"));
            alert.setHeaderText(resources.getString("dialog.loaddataerror.header"));
            alert.setContentText(resources.getString("dialog.loaddataerror.content" + recipesFile.getPath()));

            alert.showAndWait();
        }
    }

    /**
     * Add a RecipeComponent to the Plan's tab with all informations from loaded recipe.
     *
     * @param componentWrapper {@link ComponentWrapper}
     */
    private void addRecipeComponent(ComponentWrapper componentWrapper) {
        try {
            // Load the fxml file relative to the RecipeComponent
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/ryrycipe/view/RecipeComponent.fxml"));
            AnchorPane recipeComponent = loader.load();

            // Disable click listener changing the componentCB value from material filter
            recipeComponent.setOnMouseClicked(null);

            RecipeComponentController controller = loader.getController();
            controller.setComponent(componentWrapper.getComponent());
            controller.setupRecipeComponent();

            // Add each MaterialView to the RecipeComponent
            for (Map.Entry<Material, Integer> entry: componentWrapper.getMaterials().entrySet()) {
                MaterialView materialView = new MaterialView(entry.getKey(), entry.getValue());
                materialView.setRCController(controller);
                controller.getMaterialsContainer().getChildren().add(0, materialView);
                controller.updateIndicator(entry.getValue());
            }

            // Add each filled RecipeComponent to the plan
            planRecipeContainer.getChildren().add(recipeComponent);
            LOGGER.info("Plan successfully extracted from xml file.");
        } catch (IOException | IllegalStateException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Extract data from loaded {@link RecipeWrapper} to display recipe's components.
     */
    @FXML
    public void onClickRecipe() {
        planRecipeContainer.getChildren().clear();
        localRecipesView.getSelectionModel().getSelectedItem().getComponents().forEach(this::addRecipeComponent);
    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }
}
