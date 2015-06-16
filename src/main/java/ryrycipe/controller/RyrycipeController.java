package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.util.DBConnection;
import ryrycipe.util.LocaleUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

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

    /**
     * Reference to {@link SearchRecipeController}.
     */
    private SearchRecipeController searchController;
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    /**
     * Display the pane to create recipes.
     */
    @FXML
    private void createRecipe() {
        // Load previous CreatorPane with his tool bar else create new one.
        if (mainApp.getRecipeCreatorPane() != null) {
            initCreatePaneTB();
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
    private void searchRecipe() {
        // Check if a previous search pane has been created to load it otherwise create a new one
        if (mainApp.getRecipeSearchPane() != null) {
            initSearchPaneTB();
            mainApp.getRootLayout().setCenter(mainApp.getRecipeSearchPane());
        } else {
            try {
                // Load SearchRecipe view
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/ryrycipe/view/SearchRecipe.fxml"));
                loader.setResources(ResourceBundle.getBundle("lang", mainApp.getLocale()));
                mainApp.setRecipeSearchPane(loader.load());

                // Get the corresponding controller
                searchController = loader.getController();

                mainPane.setCenter(mainApp.getRecipeSearchPane());

                // Load corresponding tool bar
                initSearchPaneTB();
            } catch (IOException | IllegalStateException e) {
               LOGGER.error(e.getMessage());
            }
        }
    }

    /**
     * Upload current recipe.
     */
    @FXML
    private void uploadRecipe() {
        // Display a dialog to make the user choose or add a dropbox to save current recipe
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/ryrycipe/view/SelectCloudDialog.fxml"));
            loader.setResources(resources);
            AnchorPane dialogPane = loader.load();

            // Setup dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle(loader.getResources().getString("dialog.cloud.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            SelectCloudDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException | IllegalStateException e) {
            LOGGER.error(e.getMessage());
        }

    }

    /**
     * Toggle language between french and english.
     */
    @FXML
    private void changeLanguage() {
        // Ask for confirmation
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("dialog.language.title"));
        alert.setHeaderText(resources.getString("dialog.language.header"));
        alert.setContentText(resources.getString("dialog.language.content"));

        Optional < ButtonType > result = alert.showAndWait();

        // Switch language
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

            // Reinitialize application
            mainApp.initialize();
            mainApp.showRecipeCreator();

            LOGGER.info("Language changed");
        }

        // change language for the memory saved search pane
        mainApp.setRecipeSearchPane(null);
    }

    /**
     * Create a new recipe.
     */
    private void newRecipe() {
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
    @FXML
    private void save() {
        if (mainApp.getCreatorController().isPlanFilled()) {
            // Load SaveRecipeDialog view
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/ryrycipe/view/SaveRecipeDialog.fxml"));
                loader.setResources(ResourceBundle.getBundle("lang", mainApp.getLocale()));
                AnchorPane dialogPane = loader.load();

                // Dialog configuration
                Stage dialogStage = new Stage();
                dialogStage.setTitle(loader.getResources().getString("dialog.save.title"));
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(mainApp.getPrimaryStage());
                Scene scene = new Scene(dialogPane);
                dialogStage.setScene(scene);
                dialogStage.setResizable(false);

                // Initialize associated controller
                SaveRecipeDialogController controller = loader.getController();
                controller.setMainApp(mainApp);
                controller.setDialogStage(dialogStage);

                dialogStage.showAndWait();
            } catch (IOException | IllegalStateException e) {
                LOGGER.error(e.getMessage());
            }
        } else { // Use hasn't filled his recipe before saving
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(resources.getString("dialog.save.fail.title"));
            alert.setHeaderText(resources.getString("dialog.save.fail.header"));
            alert.setContentText(resources.getString("dialog.save.fail.content"));

            LOGGER.info("User didn't filled his recipe.");
            alert.showAndWait();
        }
    }

    /**
     * Allow the user to add a new {@link Tab} with recipes from selected folder.
     */
    @FXML
    private void addRecipesFolder() {
        // Make the user choose a folder containing recipes
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(resources.getString("search.dialog.addfolder.title"));
        File recipesFolder = directoryChooser.showDialog(mainApp.getPrimaryStage());

        if (recipesFolder == null) {
            LOGGER.warn("Problem with folder selection.");
            return;
        }

        try {
            // Create a new RecipeTab
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/ryrycipe/view/RecipesTab.fxml"));
            loader.setResources(resources);
            Tab tab = loader.load();

            // Initialize tab's controller
            RecipesTabController controller = loader.getController();
            controller.getRecipesTab().setText(recipesFolder.getName());
            controller.setSearchController(searchController);
            controller.setRecipesFolder(recipesFolder);
            tab.setUserData(controller); // to use his controller later

            // Added all recipes of selected folder to the inner ListView
            controller.ListRecipes();

            searchController.getRecipeTabPane().getTabs().add(tab);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        LOGGER.info("User choose another recipes' folder: {}", recipesFolder.getPath());
    }

    /**
     * Refresh current recipe's tab in case where new recipes are saved in his associated folder
     */
    @FXML
    private void refresh() {
        RecipesTabController controller = (RecipesTabController) searchController
            .getRecipeTabPane()
            .getSelectionModel()
            .getSelectedItem()
            .getUserData();

        controller.ListRecipes();
    }

    /**
     * Add a new and save recipe tool buttons to creator pane tool bar
     */
    public void initCreatePaneTB() {
        // Button to create new recipe
        Button newRecipeBtn = new Button("");
        newRecipeBtn.setId("newBtn");
        newRecipeBtn.setPrefSize(32, 32);
        Tooltip.install(newRecipeBtn, new Tooltip(resources.getString("toolbtn.new.tip")));
        newRecipeBtn.setOnAction(e -> newRecipe());

        // Button to save current recipe
        Button saveRecipeBtn = new Button("");
        saveRecipeBtn.setId("saveBtn");
        saveRecipeBtn.setPrefSize(32, 32);
        Tooltip.install(saveRecipeBtn, new Tooltip(resources.getString("toolbtn.save.tip")));
        saveRecipeBtn.setOnAction(e -> save());

        // Add buttons to the tool bar
        specificToolBtns.getChildren().clear();
        specificToolBtns.getChildren().addAll(newRecipeBtn, saveRecipeBtn);
    }

    /**
     * Add a refresh tool button to search pane tool bar
     */
    private void initSearchPaneTB() {
        // Button to add a folder to look for saved recipes
        Button addFolderBtn = new Button("");
        addFolderBtn.setId("addFolderBtn");
        addFolderBtn.setPrefSize(32, 32);
        Tooltip.install(addFolderBtn, new Tooltip(resources.getString("toolbtn.addfolder.tip")));
        addFolderBtn.setOnAction(e -> addRecipesFolder());

        // Button to refresh recipes in search pane
        Button refreshBtn = new Button("");
        refreshBtn.setId("refreshBtn");
        refreshBtn.setPrefSize(32, 32);
        Tooltip.install(refreshBtn, new Tooltip(resources.getString("toolbtn.refresh.tip")));
        refreshBtn.setOnAction(e -> refresh());

        // Add buttons to the tool bar
        specificToolBtns.getChildren().clear();
        specificToolBtns.getChildren().addAll(addFolderBtn, refreshBtn);
    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }
}
