package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ryrycipe.model.view.MaterialView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the dialog used to select a number of material to include in the recipe.
 */
public class MaterialNumberDialogController implements Initializable {

    @FXML
    private ImageView materialImg;

    @FXML
    private TextField nbMaterialField;

    private Stage dialogStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMaterialImage(MaterialView materialView) {
        this.materialImg.setImage(materialView.getImage());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
