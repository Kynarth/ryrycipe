package ryrycipe.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.task.AccessTokenValidation;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for NewCloudAccount view
 */
public class NewCloudAccountController implements Initializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(NewCloudAccountController.class.getName());

    /**
     * {@link TextField} containing entered dropbox account's name.
     */
    @FXML
    private TextField accountNameTF;

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

    private Ryrycipe mainApp;

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

        // Set focus on to account name text field
        Platform.runLater(accountNameTF::requestFocus);
    }

    @FXML
    private void handleOKBtn() {

        // Check if the form is filled and warn user if not
        if (accountNameTF.getText().isEmpty() || accessTokenPF.getText().isEmpty()) {
            // Check account name
            if (accountNameTF.getText().isEmpty()) {
                accountNameTF.setPromptText(resources.getString("dialog.cloud.add.account.name.prompt.empty"));
            }

            // Check access token
            if (accessTokenPF.getText().isEmpty()) {
                if (accessTokenPF.isVisible()) {
                    accessTokenPF.setPromptText(resources.getString("dialog.cloud.add.account.key.prompt.empty"));
                } else {
                    accessTokenTF.setPromptText(resources.getString("dialog.cloud.add.account.key.prompt.empty"));
                }
            }

            LOGGER.info("User did not filled the adding account form.");
            return;
        }

        // Check validity if account's access token
        Thread validationThread = new Thread(new AccessTokenValidation(
            mainApp, accountNameTF, accessTokenTF, accessTokenPF, resources, dialogStage
        ));
        validationThread.setDaemon(true);
        validationThread.start();
    }

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

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }
}
