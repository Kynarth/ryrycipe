package ryrycipe.controller;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.model.DropBoxAccount;

import java.net.URL;
import java.util.Optional;
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
    public PasswordField accessTokenPF;

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
    private void handleOkBtn() {
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

        // Check if given access token is valid
        DbxRequestConfig config = new DbxRequestConfig("Ryrycipe", mainApp.getLocale().toString());
        DbxClient client = new DbxClient(config, accessTokenPF.getText());
        try {
            client.getAccountInfo();
        } catch (DbxException e) {
            // 401 response -> unauthorized error
            if (e.getMessage().contains("401")) {
                // Warn user
                if (accessTokenPF.isVisible()) {
                    accessTokenPF.setText("");
                    accessTokenPF.setPromptText(resources.getString("dialog.cloud.add.account.key.prompt.wrong"));
                } else {
                    accessTokenTF.setText("");
                    accessTokenTF.setPromptText(resources.getString("dialog.cloud.add.account.key.prompt.wrong"));
                }

                LOGGER.info("User provides wrong access token.");
                return;
            } else {
                LOGGER.error(e.getMessage());
            }
        }

        // Check if entered account name already exists and ask if he wants to
        for (DropBoxAccount account: mainApp.getDpAccounts()) {
            if (account.getName().equals(accountNameTF.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(resources.getString("dialog.cloud.add.warn.title"));
                alert.setHeaderText(resources.getString("dialog.cloud.add.warn.header"));
                alert.setContentText(resources.getString("dialog.cloud.add.warn.content"));

                ButtonType okBtn = ButtonType.OK;
                ButtonType cancelBtn = ButtonType.CANCEL;

                alert.getButtonTypes().setAll(okBtn, cancelBtn);

                Optional<ButtonType> response = alert.showAndWait();
                // Remove old account
                if (response.get() == ButtonType.OK) {
                    mainApp.getDpAccounts().remove(account);
                    break;
                } else {
                    accountNameTF.setText("");
                    accountNameTF.setPromptText(resources.getString("dialog.cloud.add.account.name.prompt.exists"));
                    return;
                }
            }
        }

        DropBoxAccount newAccount = new DropBoxAccount(accountNameTF.getText(), accessTokenTF.getText());
        mainApp.getDpAccounts().add(newAccount);

        LOGGER.info("New account: {} added.", newAccount.getName());
        dialogStage.close();
    }

    @FXML
    private void handleCancelBtn() {
        dialogStage.close();
    }

    // -------------------- Accessors -------------------- //

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }
}
