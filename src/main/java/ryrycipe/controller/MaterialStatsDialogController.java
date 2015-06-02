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
    public GridPane materialStatsContainer;

    @FXML
    public ImageView materialIcon;

    @FXML
    public Label materialDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
