package ryrycipe.controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.model.Material;
import ryrycipe.model.view.MaterialView;
import ryrycipe.model.wrapper.ComponentWrapper;
import ryrycipe.model.wrapper.RecipeWrapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.prefs.Preferences;

public class SaveRecipeDialogController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(SaveRecipeDialogController.class.getName());

    /**
     * {@link TextField} to enter the name of the recipe's author.
     */
    @FXML
    private TextField authorNameTF;

    /**
     * {@link TextField} to enter the name of the recipe's name.
     */
    @FXML
    private TextField recipeNameTF;

    /**
     * {@link TextField} asking for path to user's saved recipes
     */
    @FXML
    private TextField recipesFolderTF;

    /**
     * {@link Button} asking the user to choose a folder to save within user's recipes.
     */
    @FXML
    private Button chooseDirBtn;

    /**
     * Reference to dialog {@link Stage} to close it later.
     */
    private Stage dialogStage;

    /**
     * Reference to {@link Ryrycipe}.
     */
    private Ryrycipe mainApp;

    /**
     * {@link ResourceBundle}
     */
    private ResourceBundle resources;

    /**
     * {@link File} where user's recipes are saved.
     */
    private File savedRecipesFolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        Preferences prefs = Preferences.userNodeForPackage(Ryrycipe.class);
        authorNameTF.setText(prefs.get("userName", ""));

        String prefRecipeFolder = prefs.get("recipeFolder", "");
        if (!prefRecipeFolder.isEmpty()) { // Previous folder path has been saved
            if (new File(prefRecipeFolder).exists()) {  // folder still exists
                recipesFolderTF.setText(prefRecipeFolder);
            } else { // folder not longer exists
                recipesFolderTF.setPromptText(resources.getString("dialog.save.folder.prompt.notfound"));
            }
        }

        // Set focus on recipe name text field if author name has already been entered
        if (!authorNameTF.getText().isEmpty()) {
            Platform.runLater(recipeNameTF::requestFocus);
        }
    }

    /**
     * Make the user select a directory to save his recipes.
     */
    @FXML
    private void handleChooseDirClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(resources.getString("dialog.save.choosedir.title"));
        savedRecipesFolder = directoryChooser.showDialog(dialogStage);

        if (savedRecipesFolder != null) {
            recipesFolderTF.setText(savedRecipesFolder.getPath() + "");
        } else {
            recipesFolderTF.setPromptText(resources.getString("dialog.save.folder.prompt"));
        }
    }

    /**
     * Save the user's recipe when he has filled the form and clicked the OK button
     */
    @FXML
    private void handleOKClick() {
        // Check if user filled the form and warn him to fil fields
        if (authorNameTF.getText().isEmpty() || recipeNameTF.getText().isEmpty()
            || recipesFolderTF.getText().isEmpty()) {

            if (authorNameTF.getText().isEmpty()) {
                authorNameTF.setPromptText(resources.getString("dialog.save.author.prompt"));
            }

            if (recipeNameTF.getText().isEmpty()) {
                recipeNameTF.setPromptText(resources.getString("dialog.save.recipename.prompt"));
            }

            if (recipesFolderTF.getText().isEmpty()) {
                recipesFolderTF.setPromptText(resources.getString("dialog.save.folder.prompt"));
            }

            LOGGER.info("User did not filled the saving form.");
            return;
        }

        Preferences prefs = Preferences.userNodeForPackage(Ryrycipe.class);
        prefs.put("userName", authorNameTF.getText());

        // Check if given path exist and is a directory
        savedRecipesFolder = new File(recipesFolderTF.getText());

        if (savedRecipesFolder.exists() && savedRecipesFolder.isDirectory()) {
            prefs.put("recipeFolder", recipesFolderTF.getText());
        } else {
            recipesFolderTF.setText(null);
            recipesFolderTF.setPromptText(resources.getString("dialog.save.folder.prompt"));
            return;
        }

        // Wrapping user's recipe
        RecipeWrapper wrapper = new RecipeWrapper(
            authorNameTF.getText(), recipeNameTF.getText(),
            mainApp.getCreatorController().getRecipeComment().getText(),
            mainApp.getCreatorController().getPlanCB().getValue().getName()
        );
        wrapper.setComponents(getRecipeComponent());

        // Check if user's recipe name already exists
        if (checkDuplication(wrapper)) {
            // User doesn't want to erase previous recipe
            LOGGER.info("The old recipe has not been overwritten.");
            return;
        }

        // Try to save user's recipe
        if (saveRecipe(wrapper)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(resources.getString("dialog.save.success.title"));
            alert.setHeaderText(null);
            alert.setContentText(resources.getString("dialog.save.success.content"));
            LOGGER.info("{}'s Recipe {} saved", authorNameTF.getText(), recipeNameTF.getText());

            alert.showAndWait();
            dialogStage.close();
        } else { // Saving failed
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(resources.getString("dialog.save.fail.title"));
            alert.setHeaderText(resources.getString("dialog.save.fail.header"));
            alert.setContentText(resources.getString("dialog.save.error.content"));

            LOGGER.warn("{}'s Recipe {} couldn't be saved", authorNameTF.getText(), recipeNameTF.getText());
            alert.showAndWait();
        }
    }

    /**
     * Cancel recipe's saving.
     */
    @FXML
    private void handleCancelClick() {
        dialogStage.close();
    }

    /**
     * Save the current recipe in XML format.
     *
     * @param wrapper {@link RecipeWrapper} of current user's recipe.
     * @return boolean - Return true if the save succeed.
     */
    private boolean saveRecipe(RecipeWrapper wrapper) {
        try {
            JAXBContext context = JAXBContext
                .newInstance(RecipeWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Marshalling and saving XML to the file
            File savedRecipe = new File(savedRecipesFolder, authorNameTF.getText() + "_" +
                    mainApp.getCreatorController().getPlanCB().getValue().getName() +
                    "_" + recipeNameTF.getText() + ".xml"
            );
            marshaller.marshal(wrapper, savedRecipe);
            return true;

        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resources.getString("dialog.save.fail.title"));
            alert.setHeaderText(resources.getString("dialog.save.fail.header"));
            alert.setContentText(resources.getString("dialog.save.error.content"));

            alert.showAndWait();
        }

        return false;
    }

    /**
     * Retrieve the list of RecipeComponent and associated materials.
     *
     * @return {@link List} of {@link ComponentWrapper}s.
     */
    private List<ComponentWrapper> getRecipeComponent() {
        List<ComponentWrapper> componentWrappers = new ArrayList<>();
        for(Node node: mainApp.getCreatorController().getComponentsContainer().getChildren()) {
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
     * Check if recipe name is already taken.
     *
     * @param wrapper Loaded recipe from file.
     * @return true if a duplication is founded and user don't want to erase the first recipe.
     */
    private boolean checkDuplication(RecipeWrapper wrapper) {
        File[] recipes = savedRecipesFolder.listFiles();

        if (recipes != null && recipes.length > 0) {
            for (File recipe : recipes) {
                if (recipe.isFile() && wrapper.fileName().equals(recipe.getName())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(resources.getString("dialog.duplication.title"));
                    alert.setHeaderText(resources.getString("dialog.duplication.header"));
                    alert.setContentText(resources.getString("dialog.duplication.content"));

                    ButtonType okButton = new ButtonType(resources.getString("button.ok.text"));
                    ButtonType cancelButton = new ButtonType(resources.getString("button.cancel.text"));
                    alert.getButtonTypes().setAll(okButton, cancelButton);

                    Optional<ButtonType> response = alert.showAndWait();
                    return response.get() != okButton;
                }
            }
        }

        return false;
    }

    /**
     * Validate saving when user push enter key in {@link SaveRecipeDialogController#recipeNameTF} text field.
     *
     * @param event {@link KeyEvent}
     */
    @FXML
    private void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleOKClick();
        }
    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

}
