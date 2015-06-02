package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.model.view.MaterialView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the dialog used to select a number of material to include in the recipe.
 */
public class MaterialNumberDialogController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(MaterialNumberDialogController.class.getName());

    @FXML
    private ImageView materialImg;

    @FXML
    private TextField nbMaterialField;

    private Stage dialogStage;

    /**
     * Number of materials needed for recipe component.
     */
    private int materialAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nbMaterialField.textProperty().addListener(((observable, oldValue, newValue) -> {
            Pattern pattern = Pattern.compile("^[0-9]+$");
            if (!newValue.isEmpty()){
                // Check if the new entered value is a number. If not set the oldValue
                if (!pattern.matcher(newValue).matches()) {
                    nbMaterialField.setText(oldValue);
                    return; // to not enter in the second if statement
                }
                // Check if the user enters a '0' before other numbers to remove it.
                if (newValue.charAt(0) == '0' && newValue.length() > 1)
                    nbMaterialField.setText(newValue.substring(1));
            }

            // Check if the entered value does not exceed the number of needed materials
            if (!newValue.isEmpty() && Integer.parseInt(newValue) > materialAmount) {
                nbMaterialField.setText(String.valueOf(materialAmount));
            }
        }));
    }

    /**
     * Set the imageView with the selected MaterialView from {@link RecipeCreatorController#materialChooser}
     *
     * @param materialView {@link MaterialView}
     */
    public void setMaterialImage(MaterialView materialView) {
        this.materialImg.setImage(materialView.getImage());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Set the number of materials needed for a recipe a component and put it as default value in the
     * {@link MaterialNumberDialogController#nbMaterialField}
     *
     * @param amount Number of materials needed for a recipe a component.
     */
    public void setMaterialAmount(int amount) {
        this.materialAmount = amount;
        nbMaterialField.setText(String.valueOf(amount));
    }

    /**
     * Close the dialog and update the corresponding RecipeComponent with the chosen MaterialView and number.
     * Note: Do nothing if no value is chosen.
     */
    @FXML
    public void handleOKBtn() {
        dialogStage.close();

        LOGGER.info("USer chose {} materials", nbMaterialField);
    }

    /**
     * Close the dialog when the user clicks the cancel button.
     */
    @FXML
    public void handleCancelBtn() {
        // Set TextField to '0' in order to remove potential numbers from the it before leaving the dialog
        nbMaterialField.setText("0");
        dialogStage.close();
    }

    @FXML
    public void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleOKBtn();
        }
    }

    public TextField getNbMaterialField() {
        return nbMaterialField;
    }
}
