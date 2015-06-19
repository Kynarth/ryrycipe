package ryrycipe.task;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.controller.NewCloudAccountController;
import ryrycipe.model.DropBoxAccount;

import java.util.Optional;

/**
 * {@link javafx.concurrent.Task} checking if accounts access is valid and if there is an account
 * with the same name already existing.
 */
public class AccountValidation extends Task<Void> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccountValidation.class.getName());

    NewCloudAccountController controller;

    public AccountValidation(NewCloudAccountController controller)
    {
        this.controller = controller;
    }

    @Override
    protected Void call() throws Exception {
        // Check if given access token is valid
        DbxRequestConfig config = new DbxRequestConfig("Ryrycipe", controller.getMainApp().getLocale().toString());
        DbxClient client = new DbxClient(config, controller.getAccessTokenPF().getText());
        try {
            client.getAccountInfo();
            LOGGER.info("User provides right access token.");
        } catch (DbxException e) {
            // 401 response -> unauthorized error
            if (e.getMessage().contains("401")) {
                // Warn user
                Platform.runLater(() -> {
                    if (controller.getAccessTokenPF().isVisible()) {
                        controller.getAccessTokenPF().setText("");
                        controller.getAccessTokenPF().setPromptText(
                            controller.getResources().getString("dialog.cloud.add.account.key.prompt.wrong")
                        );
                    } else {
                        controller.getAccessTokenTF().setText("");
                        controller.getAccessTokenTF().setPromptText(
                            controller.getResources().getString("dialog.cloud.add.account.key.prompt.wrong")
                        );
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
            for (DropBoxAccount account : controller.getMainApp().getDpAccounts()) {
                if (account.getName().equals(controller.getAccountNameTF().getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(controller.getResources().getString("dialog.cloud.add.warn.title"));
                    alert.setHeaderText(controller.getResources().getString("dialog.cloud.add.warn.header"));
                    alert.setContentText(controller.getResources().getString("dialog.cloud.add.warn.content"));

                    ButtonType okBtn = ButtonType.OK;
                    ButtonType cancelBtn = ButtonType.CANCEL;

                    alert.getButtonTypes().setAll(okBtn, cancelBtn);

                    Optional<ButtonType> response = alert.showAndWait();
                    // Remove old account
                    if (response.get() == ButtonType.OK) {
                        controller.getMainApp().getDpAccounts().remove(account);
                        break;
                    } else {
                        controller.getAccountNameTF().setText("");
                        controller.getAccountNameTF().setPromptText(controller.getResources().getString(
                                "dialog.cloud.add.account.name.prompt.exists")
                        );
                        return;
                    }
                }
            }

            DropBoxAccount newAccount = new DropBoxAccount(
                controller.getAccountNameTF().getText(), controller.getAccessTokenTF().getText()
            );
            controller.getMainApp().getDpAccounts().add(newAccount);

            LOGGER.info("New account: {} added.", newAccount.getName());
            controller.getDialogStage().close();
        });

        return null;
    }

    /**
     * Change the cursor for waiting one just before the run.
     */
    @Override
    protected void scheduled() {
        super.scheduled();
        controller.getDialogStage().getScene().setCursor(Cursor.WAIT);
    }

    /**
     * Change the cursor for default one at the end of task.
     */
    @Override
    protected void succeeded() {
        super.succeeded();
        controller.getDialogStage().getScene().setCursor(Cursor.DEFAULT);
    }
}