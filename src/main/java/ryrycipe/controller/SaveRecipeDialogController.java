package ryrycipe.controller;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.model.wrapper.ComponentWrapper;
import ryrycipe.model.Material;
import ryrycipe.model.wrapper.RecipeWrapper;
import ryrycipe.model.view.MaterialView;

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
    private TextField authorName;

    /**
     * {@link TextField} to enter the name of the recipe's name.
     */
    @FXML
    private TextField recipeName;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        Preferences prefs = Preferences.userNodeForPackage(Ryrycipe.class);
        authorName.setText(prefs.get("userName", ""));

        // Set focus on recipe name text field if author name has already been entered
        if (!authorName.getText().isEmpty()) {
            Platform.runLater(recipeName::requestFocus);
        }
    }

    /**
     * Save the user's recipe when he has filled the form and clicked the OK button
     */
    @FXML
    private void handleOKClick() {
        if (!authorName.getText().isEmpty() && !recipeName.getText().isEmpty()) {
            // Wrapping user's recipe
            RecipeWrapper wrapper = new RecipeWrapper(
                authorName.getText(), recipeName.getText(), mainApp.getCreatorController().getRecipeComment().getText(),
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
                LOGGER.info("{}'s Recipe {} saved", authorName.getText(), recipeName.getText());

                alert.showAndWait();
                dialogStage.close();
            } else { // Saving failed
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle(resources.getString("dialog.save.fail.title"));
                alert.setHeaderText(resources.getString("dialog.save.fail.header"));
                alert.setContentText(resources.getString("dialog.save.error.content"));

                LOGGER.warn("{}'s Recipe {} couldn't be saved", authorName.getText(), recipeName.getText());
                alert.showAndWait();
            }
        } else { // Saving failed
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(resources.getString("dialog.noinfo.title"));
            alert.setHeaderText(resources.getString("dialog.noinfo.header"));
            alert.setContentText(resources.getString("dialog.noinfo.content"));

            LOGGER.warn("{}'s Recipe {} couldn't be saved", authorName.getText(), recipeName.getText());
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
            File savedRecipe = new File(
                mainApp.getSavedRecipesFolder() + authorName.getText() + "_" +
                    mainApp.getCreatorController().getPlanCB().getValue().getName() +
                    "_" + recipeName.getText() + ".xml"
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
        File recipesFolder = new File(mainApp.getSavedRecipesFolder());

        if (recipesFolder.exists()) {
            File[] recipes = recipesFolder.listFiles();

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
        }

        return false;
    }

    /**
     * Validate saving when user push enter key in {@link SaveRecipeDialogController#recipeName} text field.
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
