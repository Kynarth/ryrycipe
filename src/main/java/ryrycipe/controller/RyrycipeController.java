package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.model.ComponentWrapper;
import ryrycipe.model.Material;
import ryrycipe.model.RecipeWrapper;
import ryrycipe.model.view.MaterialView;
import ryrycipe.util.DBConnection;
import ryrycipe.util.LocaleUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;

import static javafx.scene.control.Alert.AlertType;


/**
 * Controller for the Ryrycipe view.
 */
public class RyrycipeController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(RyrycipeController.class.getName());
    /**
     * Display the pane for create recipe.
     */
    @FXML
    private Button createBtn;

    /**
     * Search for user's saved recipe.
     */
    @FXML
    private Button searchBtn;

    /**
     * Upload the current recipe.
     */
    @FXML
    private Button uploadBtn;

    /**
     * Call a dialog to ask to change current language.
     * Application has to restart to apply changes.
     */
    @FXML
    private Button languageBtn;

    /**
     * {@link ButtonBar} containing tool buttons for each specific application pane like creation or search pane.
     */
    @FXML
    private HBox specificToolBtns;

    /**
     * Save current recipe.
     */
    @FXML
    private Button saveBtn;

    /**
     * Main widget of the application.
     */
    @FXML
    private BorderPane mainPane;

    /**
     * Reference to the {@link Ryrycipe} object.
     */
    private Ryrycipe mainApp;

    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    @FXML
    public void createRecipe() {
        // Load previous CreatorPane with his tool bar else create new one.
        if (mainApp.getRecipeCreatorPane() != null) {
            mainApp.getCreatorController().initializeSpecificToolBar();
            mainApp.getRootLayout().setCenter(mainApp.getRecipeCreatorPane());
        } else {
            newRecipe();
            LOGGER.warn("Warning: Not found previous recipe's creator pane.");
        }
    }

    /**
     * Load the pane to search for recipes.
     */
    @FXML
    public void searchRecipe() {
        // Check if a previous search pane has been created to load it otherwise create a new one
        if (mainApp.getRecipeSearchPane() != null) {
            mainApp.getRyrycipeController().getSpecificToolBtns().getChildren().clear();
            mainApp.getRootLayout().setCenter(mainApp.getRecipeSearchPane());
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/ryrycipe/view/SearchRecipe.fxml"));
                loader.setResources(ResourceBundle.getBundle("lang", mainApp.getLocale()));
                mainApp.setRecipeSearchPane(loader.load());

                // Get the corresponding controller
                SearchRecipeController searchController = loader.getController();
                searchController.setMainApp(mainApp);
                searchController.searchLocalRecipes();  // Add local recipes to the SearchRecipe's list view

                mainPane.setCenter(mainApp.getRecipeSearchPane());

                // Load corresponding tool bar
                mainApp.getRyrycipeController().getSpecificToolBtns().getChildren().clear();
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Toggle language between french and english.
     */
    @FXML
    public void changeLanguage() {

        // Ask for confirmation
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("dialog.language.title"));
        alert.setHeaderText(resources.getString("dialog.language.header"));
        alert.setContentText(resources.getString("dialog.language.content"));

        Optional < ButtonType > result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            if (mainApp.getLocale().toString().equals("fr")) {
                LocaleUtil.setLanguage("en");
                DBConnection.changeLanguage();  // update the path to the database in function of language
                mainApp.setLocale(new Locale("en"));
            } else {
                LocaleUtil.setLanguage("fr");
                DBConnection.changeLanguage();  // update the path to the database in function of language
                mainApp.setLocale(new Locale("fr"));
            }
            mainApp.initialize();
            mainApp.showRecipeCreator();

            LOGGER.info("Language changed");
        }

    }

    /**
     * Create a new recipe.
     */
    public void newRecipe() {

        // Ask for confirmation
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("dialog.newrecipe.title"));
        alert.setHeaderText(resources.getString("dialog.newrecipe.header"));
        alert.setContentText(resources.getString("dialog.newrecipe.content"));

        Optional < ButtonType > result = alert.showAndWait();

        // If user accepts -> create a new recipe
        if (result.get() == ButtonType.OK) {
            mainApp.initialize();
            mainApp.showRecipeCreator();
            LOGGER.info("Launch new recipe.");
        }
    }

    /**
     * Ask for saving current recipe when the user click in the save tool button.
     */
    public void save() {
        if (mainApp.getCreatorController().isPlanFilled()) {
            String authorName = askAuthorName();
            String recipeName = askRecipeName();

            // Save the recipe and inform user for succeeding
            if (saveRecipe(authorName, recipeName)){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle(resources.getString("dialog.save.success.title"));
                alert.setHeaderText(null);
                alert.setContentText(resources.getString("dialog.save.success.content"));
                LOGGER.info("{}'s Recipe {} saved", authorName, recipeName);

                alert.showAndWait();
            } else { // Saving failed
                Alert alert = new Alert(AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle(resources.getString("dialog.save.fail.title"));
                alert.setHeaderText(resources.getString("dialog.save.fail.header"));
                alert.setContentText(resources.getString("dialog.save.fail.content"));

                alert.showAndWait();
            }
        } else { // Saving failed
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(resources.getString("dialog.save.fail.title"));
            alert.setHeaderText(resources.getString("dialog.save.fail.header"));
            alert.setContentText(resources.getString("dialog.save.fail.content"));

            alert.showAndWait();
        }
    }

    /**
     * Dialog asking for the user's name and save it in preferences.
     *
     * @return {@link String} - User's name.
     */
    private String askAuthorName() {
        Preferences prefs = Preferences.userNodeForPackage(Ryrycipe.class);

        TextInputDialog dialog = new TextInputDialog(prefs.get("userName", ""));
        dialog.setTitle(resources.getString("dialog.authorname.title"));
        dialog.setContentText(resources.getString("dialog.authorname.content"));
        dialog.setHeaderText(null);

        Optional<String> userName = dialog.showAndWait();

        if (userName.isPresent()) {
            prefs.put("userName", userName.get());
            return userName.get();
        } else {
            return "John Doe";
        }
    }

    /**
     * Dialog asking for the recipe's name.
     *
     * @return {@link String} - Recipe's name.
     */
    private String askRecipeName() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(resources.getString("dialog.recipename.title"));
        dialog.setContentText(resources.getString("dialog.recipename.content"));
        dialog.setHeaderText(null);

        Optional<String> recipeName = dialog.showAndWait();


        // Ask for a recipe name until the user furnish one
        if (recipeName.isPresent()) {
            if (!recipeName.get().isEmpty()) {
                return recipeName.get();
            } else {
                while (recipeName.get().isEmpty()) {
                    dialog.setHeaderText(resources.getString("dialog.recipename.header"));
                    recipeName = dialog.showAndWait();
                }
                return recipeName.get();
            }
        } else {
            LOGGER.warn("Problem with dialog asking for recipe name -> ifPresent is false.");

            return "";
        }
    }

    /**
     * Retrieve the list of RecipeComponent and associated materials.
     *
     * @return List<ComponentWrapper>
     */
    private List<ComponentWrapper> getRecipeComponent() {
        List<ComponentWrapper> componentWrappers = new ArrayList<>();
        for(Node node: mainApp.getCreatorController().componentsContainer.getChildren()) {
            Map<Material, Integer> materials = new HashMap<>();
            RecipeComponentController controller = (RecipeComponentController) node.getUserData();
            for(Node child: controller.getMaterialsContainer().getChildren()) {
                MaterialView materialView = (MaterialView) child;
                materials.put(materialView.getMaterial(), materialView.getNbMaterials());
            }
            ComponentWrapper componentWrapper = new ComponentWrapper();
            componentWrapper.setComponent(controller.getComponent());
            componentWrapper.setMaterials(materials);
            componentWrappers.add(componentWrapper);
        }

        return componentWrappers;
    }

    /**
     * Save the current recipe in XML format.
     * @param authorName String - Author's name
     * @param recipeName String - Recipe's name
     *
     * @return boolean - Return true if the save succeed.
     */
    private boolean saveRecipe(String authorName, String recipeName) {
        try {
            JAXBContext context = JAXBContext
                .newInstance(RecipeWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our recipe
            RecipeWrapper wrapper = new RecipeWrapper(
                authorName, recipeName, mainApp.getCreatorController().recipeComment.getText()
            );
            wrapper.setComponents(getRecipeComponent());

            // Marshalling and saving XML to the file
            File savedRecipe = new File(mainApp.getSavedRecipesFolder() + authorName + "_" + recipeName + ".xml");
            marshaller.marshal(wrapper, savedRecipe);
            return true;

        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(resources.getString("dialog.save.fail.title"));
            alert.setHeaderText(resources.getString("dialog.save.fail.header"));
            alert.setContentText(resources.getString("dialog.save.error.content"));

            alert.showAndWait();
        }

        return false;
    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }

    public HBox getSpecificToolBtns() {
        return specificToolBtns;
    }
}
