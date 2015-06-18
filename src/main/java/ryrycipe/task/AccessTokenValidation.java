package ryrycipe.task;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.Ryrycipe;
import ryrycipe.model.DropBoxAccount;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * {@link javafx.concurrent.Task} checking if accounts access is valid and if there is an account
 * with the same name already existing.
 */
public class AccessTokenValidation extends Task<Void> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccessTokenValidation.class.getName());

    /**
     * Reference to {@link Ryrycipe} to get access to the list of saved {@link DropBoxAccount}.
     */
    private Ryrycipe mainApp;

    /**
     * {@link TextField} where account's name is filled in.
     */
    private TextField accountNameTF;

    /**
     * {@link TextField} where access token's is filled in.
     */
    private TextField accessTokenTF;

    /**
     * {@link PasswordField} where access token's is filled in with a mask.
     */
    private PasswordField accessTokenPF;

    /**
     * Reference to {@link Stage} to close the dialog.
     */
    private Stage dialogStage;

    private ResourceBundle resources;

    public AccessTokenValidation(
        Ryrycipe mainApp, TextField accountNameTF, TextField accessTokenTF,
        PasswordField accessTokenPF, ResourceBundle resources, Stage dialogStage)
    {
        this.mainApp = mainApp;
        this.accountNameTF = accountNameTF;
        this.accessTokenTF = accessTokenTF;
        this.accessTokenPF = accessTokenPF;
        this.resources = resources;
        this.dialogStage = dialogStage;
    }

    @Override
    protected Void call() throws Exception {
        // Check if given access token is valid
        DbxRequestConfig config = new DbxRequestConfig("Ryrycipe", mainApp.getLocale().toString());
        DbxClient client = new DbxClient(config, accessTokenPF.getText());
        try {
            client.getAccountInfo();
            LOGGER.info("User provides right access token.");
        } catch (DbxException e) {
            // 401 response -> unauthorized error
            if (e.getMessage().contains("401")) {
                // Warn user
                Platform.runLater(() -> {
                    if (accessTokenPF.isVisible()) {
                        accessTokenPF.setText("");
                        accessTokenPF.setPromptText(resources.getString("dialog.cloud.add.account.key.prompt.wrong"));
                    } else {
                        accessTokenTF.setText("");
                        accessTokenTF.setPromptText(resources.getString("dialog.cloud.add.account.key.prompt.wrong"));
                    }

                    LOGGER.info("User provides wrong access token.");
                });

                return null;
            } else {
                LOGGER.error(e.getMessage());
            }
        }

        // Check if entered account name already exists and ask if he wants to erase the old account
        Platform.runLater(() -> {
            for (DropBoxAccount account : mainApp.getDpAccounts()) {
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
        });

            return null;
        }

    /**
     * Change the cursor for waiting one just before the run.
     */
    @Override
    protected void scheduled() {
        super.scheduled();
        dialogStage.getScene().setCursor(Cursor.WAIT);
    }

    /**
     * Change the cursor for default one at the end of task.
     */
    @Override
    protected void succeeded() {
        super.succeeded();
        dialogStage.getScene().setCursor(Cursor.DEFAULT);
    }
}
