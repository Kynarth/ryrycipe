package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.model.DropBoxAccount;

import java.net.URL;
import java.util.Optional;
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
     * {@link ResourceBundle}
     */
    private ResourceBundle resources;

    /**
     * Reference to {@link Ryrycipe}.
     */
    private Ryrycipe mainApp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    /**
     * Load the list of user's {@link DropBoxAccount}s.
     */
    public void loadDPAccounts() {
        // Setup ListView and select public account by default
        if (mainApp.getDpAccounts().size() >= 1) {
            dropboxListView.setItems(mainApp.getDpAccounts());
            dropboxListView.getSelectionModel().select(0);
        }
    }

    /**
     * Display another dialog to add another {@link DropBoxAccount} on the
     * {@link SelectCloudDialogController#dropboxListView}
     */
    @FXML
    private void handleAddBtn() {
        // Dialog to ask name of Dropbox account
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle(resources.getString("dialog.cloud.add.title"));
        dialog.setHeaderText(resources.getString("dialog.cloud.add.header"));
        dialog.setContentText(resources.getString("dialog.cloud.add.content"));

        Optional<String> accountName = dialog.showAndWait();

        accountName.ifPresent(name -> {
            DropBoxAccount newAccount = new DropBoxAccount(name);
            mainApp.getDpAccounts().add(newAccount);
        });
    }

    /**
     * Upload the current recipe on selected {@link DropBoxAccount}.
     */
    @FXML
    private void handleOKBtn() {
        if ( dropboxListView.getSelectionModel().getSelectedIndex() != -1) {
            LOGGER.info("Recipe have been uploaded on account: {}",
                dropboxListView.getSelectionModel().getSelectedItem().toString()
            );
            dialogStage.close();
        } else { // user didn't choose an account before clicking OK button or press enter
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(resources.getString("dialog.cloud.warn.title"));
            alert.setHeaderText(resources.getString("dialog.cloud.warn.header"));
            alert.setContentText(resources.getString("dialog.cloud.warn.content"));

            alert.showAndWait();
        }
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

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }
}