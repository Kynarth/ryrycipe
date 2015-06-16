package ryrycipe.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.model.DropBoxAccount;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for SelectCloudDialog view
 */
public class SelectCloudDialogController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(SelectCloudDialogController.class.getName());

    @FXML
    private Label headerLbl;

    /**
     * {@link ListView} of DropBox accounts.
     */
    @FXML
    private ListView<DropBoxAccount> dropboxListView;

    /**
     * {@link Button} to add a DropBox account.
     */
    @FXML
    private Button addBtn;

    @FXML
    private Button okBtn;

    @FXML
    private Button cancelBtn;

    /**
     * Dialog's {@link Stage}
     */
    @FXML
    private Stage dialogStage;

    /**
     * {@link ObservableList} of {@link DropBoxAccount}.
     */
    private ObservableList<DropBoxAccount> DPAccounts = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DropBoxAccount publicAccount = new DropBoxAccount("Ryrycipe");
        DPAccounts.add(publicAccount);
        dropboxListView.setItems(DPAccounts);
    }

    /**
     * Display another dialog to add another {@link DropBoxAccount} on the
     * {@link SelectCloudDialogController#dropboxListView}
     */
    @FXML
    private void handleAddBtn() {
        System.out.println("Ajout !");
    }

    /**
     * Upload the current recipe on selected {@link DropBoxAccount}.
     */
    @FXML
    private void handleOKBtn() {
        dialogStage.close();

        LOGGER.info("Recipe have been uploaded on {} account",
            dropboxListView.getSelectionModel().getSelectedItem().toString()
        );
    }

    /**
     * Close the dialog when the user clicks the cancel button.
     */
    @FXML
    private void handleCancelBtn() {
        dialogStage.close();
    }

    @FXML
    private void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && dropboxListView.getSelectionModel().getSelectedIndex() != -1) {
            handleOKBtn();
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
