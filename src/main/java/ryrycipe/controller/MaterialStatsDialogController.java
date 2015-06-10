package ryrycipe.controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MaterialStatsDialogController implements Initializable {

    @FXML
    private GridPane materialStatsContainer;

    @FXML
    private ImageView materialIcon;

    @FXML
    private Label materialDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public GridPane getMaterialStatsContainer() {
        return materialStatsContainer;
    }

    public ImageView getMaterialIcon() {
        return materialIcon;
    }

    public Label getMaterialDescription() {
        return materialDescription;
    }
}
