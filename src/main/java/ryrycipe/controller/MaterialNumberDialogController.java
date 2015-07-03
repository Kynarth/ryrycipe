package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.model.manager.view.MaterialView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the dialog used to select a number of material to include in the recipe.
 */
public class MaterialNumberDialogController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(MaterialNumberDialogController.class.getName());

    /**
     * {@link ImageView} of selected {@link MaterialView} to be introduced in the recipe
     */
    @FXML
    private ImageView materialImg;

    /**
     * {@link TextField} where the user enter the number of materials he wants.
     */
    @FXML
    private TextField nbMaterialField;

    /**
     * Number of materials needed for recipe component.
     */
    private int materialAmount;

    /**
     * Reference to the dialog's {@link Stage} to be enable to close it.
     */
    private Stage dialogStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nbMaterialField.textProperty().addListener(((observable, oldValue, newValue) -> {
            Pattern pattern = Pattern.compile("^[0-9]+$");
            if (!newValue.isEmpty()){
                // Check if the new entered value is a number. If not set the oldValue
                if (!pattern.matcher(newValue).matches()) {
                    nbMaterialField.setText(oldValue);
                    return; // to not enter in following if statements
                }
                // Check if the user enters a '0' before other numbers to remove it.
                if (newValue.charAt(0) == '0' && newValue.length() > 1)
                    nbMaterialField.setText(newValue.substring(1));
            }

            // Check if the entered value does not exceed the number of needed materials
            if (!newValue.isEmpty() && Integer.valueOf(newValue) > materialAmount) {
                nbMaterialField.setText(String.valueOf(materialAmount));
            }
        }));

        nbMaterialField.requestFocus();
    }

    /**
     * Set the number of materials needed for a plan a component and put it as default value in the
     * nbMaterialField
     *
     * @param amount Number of materials needed for a recipe a component.
     */
    public void setMaterialAmount(int amount) {
        this.materialAmount = amount;
        nbMaterialField.setText(String.valueOf(amount));
        nbMaterialField.selectNextWord();
    }

    /**
     * Close the dialog and update the corresponding ComponentView with the chosen MaterialView and number.
     * Note: Do nothing if no value is chosen.
     */
    @FXML
    private void handleOKBtn() {
        dialogStage.close();

        LOGGER.info("USer chose {} materials", nbMaterialField);
    }

    /**
     * Close the dialog when the user clicks the cancel button.
     */
    @FXML
    private void handleCancelBtn() {
        // Set TextField to '0' in order to remove potential numbers from the it before leaving the dialog
        nbMaterialField.setText("0");
        dialogStage.close();
    }

    @FXML
    private void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleOKBtn();
        }
    }

    /**
     * Set the imageView with the selected MaterialView's image from {@link CreatorPaneController#materialChooser}
     *
     * @param image {@link Image}
     */
    public void setMaterialImage(Image image) {
        this.materialImg.setImage(image);
    }

    public TextField getNbMaterialField() {
        return nbMaterialField;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
