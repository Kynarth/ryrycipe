package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.model.DropBoxAccount;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
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
        // Dialog to ask name and access token of Dropbox account
        try {
            // Load fxml file containing GUI information for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/ryrycipe/view/NewCloudAccount.fxml"));
            loader.setResources(resources);
            AnchorPane dialogPane = loader.load();

            // Setup dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle(loader.getResources().getString("dialog.cloud.add.title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(dialogPane);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Setup controller
            NewCloudAccountController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(mainApp);

            dialogStage.showAndWait();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @FXML
    private void handleDeleteBtn() {
        if ( dropboxListView.getSelectionModel().getSelectedIndex() != -1) {
            // Ask confirmation to user
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(resources.getString("dialog.cloud.delete.warn.title"));
            alert.setHeaderText(resources.getString("dialog.cloud.delete.warn.header"));
            alert.setContentText(MessageFormat.format(
                resources.getString("dialog.cloud.delete.warn.content"),
                dropboxListView.getSelectionModel().getSelectedItem().getName()
            ));

            ButtonType okBtn = ButtonType.OK;
            ButtonType cancelBtn = ButtonType.CANCEL;

            alert.getButtonTypes().setAll(okBtn, cancelBtn);

            Optional<ButtonType> response = alert.showAndWait();

            if (response.get() == ButtonType.OK) { // Remove selected account
                mainApp.getDpAccounts().remove(dropboxListView.getSelectionModel().getSelectedItem());

                LOGGER.info("The account: {} has been deleted.",
                    dropboxListView.getSelectionModel().getSelectedItem().getName()
                );
            }
        }
    }

    /**
     * Upload the current recipe on selected {@link DropBoxAccount}.
     */
    @FXML
    private void handleOKBtn() {
        if ( dropboxListView.getSelectionModel().getSelectedIndex() != -1) {

            // Ask access token for dropbox accounts if not provided
            if (!dropboxListView.getSelectionModel().getSelectedItem().isAuthenticated()) {

            }

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

    // -------------------- Accessors -------------------- //

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }
}