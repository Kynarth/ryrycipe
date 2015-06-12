package ryrycipe.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.model.Material;
import ryrycipe.model.view.MaterialView;
import ryrycipe.model.wrapper.ComponentWrapper;
import ryrycipe.model.wrapper.RecipeWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for RecipesTabController view.
 */
public class RecipesTabController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecipesTabController.class.getName());

    /**
     * Tab containing all recipes from corresponding container.
     */
    @FXML
    private Tab recipesTab;

    /**
     * {@link ListView} of {@link ryrycipe.model.wrapper.RecipeWrapper}.
     */
    @FXML
    private ListView<RecipeWrapper> recipesListView;

    /**
     * Reference to {@link SearchRecipeController}.
     */
    private SearchRecipeController searchController;

    /**
     * {@link File} containing user's recipes in xml format.
     */
    private File recipesFolder;

    /**
     * {@link ObservableList} of {@link RecipeWrapper} representing recipes in {@link RecipesTabController#recipesFolder}
     */
    private ObservableList<RecipeWrapper> recipesList = FXCollections.observableArrayList();

    /**
     * {@link ResourceBundle}
     */
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    /**
     * list all xml files from given folder to extract recipe from them.
     */
    public void ListRecipes() {
        recipesList.clear();

        File[] recipes = recipesFolder.listFiles();
        if (recipes != null && recipes.length > 0) {
            for (File recipe : recipes) {
                if (recipe.isFile() && recipe.getName().endsWith(".xml")) {
                    loadRecipeFromFile(recipe);
                }
            }
        } else {  // User chose an empty or invalid folder
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(resources.getString("dialog.norecipedir.title"));
            alert.setHeaderText(resources.getString("dialog.norecipedir.header"));
            alert.setContentText(resources.getString("dialog.norecipedir.content"));

            LOGGER.warn("No recipes found.");
            alert.showAndWait();
        }

        recipesListView.setItems(recipesList);
    }

    /**
     * Load a {@link RecipeWrapper}from given file to add it in a {@link ObservableList}.
     *
     * @param recipeFile {@link File} containing XML data representing a user's recipe.
     */
    private void loadRecipeFromFile(File recipeFile) {
        try {
            JAXBContext context = JAXBContext.newInstance(RecipeWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            RecipeWrapper wrapper = (RecipeWrapper) unmarshaller.unmarshal(recipeFile);

            recipesList.add(wrapper);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resources.getString("dialog.loaddataerror.title"));
            alert.setHeaderText(resources.getString("dialog.loaddataerror.header"));
            alert.setContentText(resources.getString("dialog.loaddataerror.content" + recipeFile.getPath()));

            alert.showAndWait();
        }
    }

    /**
     * Add a RecipeComponent to the {@link SearchRecipeController#planTab} with all informations from loaded recipe.
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
            for (Material material: componentWrapper.getMaterials()) {
                MaterialView materialView = new MaterialView(material);
                materialView.setRCController(controller);
                controller.getMaterialsContainer().getChildren().add(0, materialView);
                controller.updateIndicator(material.getNbMaterials());
            }

            // Add each filled RecipeComponent to the plan
            searchController.getPlanRecipeContainer().getChildren().add(recipeComponent);
            LOGGER.info("Plan successfully extracted from xml file.");
        } catch (IOException | IllegalStateException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Extract data from loaded {@link RecipeWrapper} to display recipe's components.
     */
    @FXML
    private void onClickRecipe() {
        searchController.getPlanRecipeContainer().getChildren().clear();

        // Verify that user's do not select nothing
        if (recipesListView.getSelectionModel().getSelectedItem() != null) {
            // Display RecipeComponent composing the user's plan
            recipesListView.getSelectionModel().getSelectedItem().getComponents().forEach(this::addRecipeComponent);

            // Display associated comment
            searchController.getCommentArea().setText(
                recipesListView.getSelectionModel().getSelectedItem().getRecipeComment()
            );
        }
    }

    public Tab getRecipesTab() {
        return recipesTab;
    }

    public void setSearchController(SearchRecipeController searchController) {
        this.searchController = searchController;
    }

    public void setRecipesFolder(File recipesFolder) {
        this.recipesFolder = recipesFolder;
    }
}
