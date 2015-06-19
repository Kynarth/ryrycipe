package ryrycipe.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.model.DropBoxAccount;
import ryrycipe.task.AccessTokenValidation;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for AskAccessToken view.
 */
public class AskAccessTokenDialogController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(AskAccessTokenDialogController.class.getName());

    @FXML
    private Button okBtn;

    /**
     * {@link TextField} containing entered dropbox account's access token in clear text.
     */
    @FXML
    private TextField accessTokenTF;

    /**
     * {@link PasswordField} containing entered dropbox account's access token with a mask.
     */
    @FXML
    private PasswordField accessTokenPF;

    /**
     * Switch between TextField and PasswordField to hide ot not access token.
     */
    @FXML
    private CheckBox switchCB;

    /**
     * Reference to dialog's {@link Stage} to close it.
     */
    private Stage dialogStage;

    /**
     * References to {@link Ryrycipe} to get access to {@link Ryrycipe#dpAccounts}
     */
    private Ryrycipe mainApp;

    private DropBoxAccount account;

    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        // Link fields to the checkbox to get one be visible while the other is not and vice versa
        accessTokenTF.managedProperty().bind(switchCB.selectedProperty());
        accessTokenTF.visibleProperty().bind(switchCB.selectedProperty());

        accessTokenPF.managedProperty().bind(switchCB.selectedProperty().not());
        accessTokenPF.visibleProperty().bind(switchCB.selectedProperty().not());

        // Link both field to get same access token
        accessTokenTF.textProperty().bindBidirectional(accessTokenPF.textProperty());
    }

    @FXML
    private void handleOKBtn() {
        // Check if access token is empty
        if (accessTokenPF.getText().isEmpty()) {
            if (accessTokenPF.isVisible()) {
                accessTokenPF.setPromptText(resources.getString("dialog.cloud.add.account.key.prompt.empty"));
            } else {
                accessTokenTF.setPromptText(resources.getString("dialog.cloud.add.account.key.prompt.empty"));
            }

            LOGGER.info("User did not fill the access token information.");
            return;
        }

        // Check validity of the account's access token
        Task validationTask = new AccessTokenValidation(this);

        Thread validationThread = new Thread(validationTask);
        validationThread.setDaemon(true);
        validationThread.start();
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
        if (event.getCode() == KeyCode.ENTER) {
            handleOKBtn();
        }
    }

    // -------------------- Accessors -------------------- //

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public ResourceBundle getResources() {
        return resources;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public PasswordField getAccessTokenPF() {
        return accessTokenPF;
    }

    public TextField getAccessTokenTF() {
        return accessTokenTF;
    }

    public Ryrycipe getMainApp() {
        return mainApp;
    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }

    public void setAccount(DropBoxAccount account) {
        this.account = account;
    }

    public DropBoxAccount getAccount() {
        return account;
    }

    public Button getOkBtn() {
        return okBtn;
    }
}
