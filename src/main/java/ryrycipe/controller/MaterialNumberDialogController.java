package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ryrycipe.model.view.MaterialView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the dialog used to select a number of material to include in the recipe.
 */
public class MaterialNumberDialogController implements Initializable {

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
            // Check if the new entered value is a number. If not set the oldValue
            Pattern pattern = Pattern.compile("[0-9]");
            if (!newValue.isEmpty()){
                if (!pattern.matcher(String.valueOf(newValue.charAt(newValue.length() - 1))).matches() ||
                    newValue.charAt(0) == '0') {
                    nbMaterialField.setText(oldValue);
                    return; // to not enter in the second if statement
                }
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
     * Set the number of materials needed for a recipe a component.
     *
     * @param amount Number of materials needed for a recipe a component.
     */
    public void setMaterialAmount(int amount) {
        this.materialAmount = amount;
    }

    /**
     * Close the dialog and update the corresponding RecipeComponent with the chosen MaterialView and number.
     * Note: Do nothing if no value is chosen.
     */
    @FXML
    public void handleOKBtn() {
        dialogStage.close();
    }

    /**
     * Close the dialog when the user clicks the cancel button.
     */
    @FXML
    public void handleCancelBtn() {
        // Remove potential number from the TextField before leaving the dialog
        nbMaterialField.setText("");
        dialogStage.close();
    }

    public TextField getNbMaterialField() {
        return nbMaterialField;
    }
}
