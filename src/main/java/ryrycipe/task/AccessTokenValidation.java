package ryrycipe.task;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.controller.AskAccessTokenDialogController;

/**
 * Verify that provided access token for the account is valid.
 */
public class AccessTokenValidation extends Task<Void> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccountValidation.class.getName());

    AskAccessTokenDialogController controller;

    public AccessTokenValidation(AskAccessTokenDialogController controller) {
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
                    controller.getOkBtn().setDisable(false);
                });

                return null;
            } else {
                LOGGER.error(e.getMessage());
            }
        }

        Platform.runLater(() -> {
            // Set the access token as valid
            controller.getAccount().setAccessToken(controller.getAccessTokenTF().getText());
            controller.getAccount().setIsAuthenticated(true);
            LOGGER.info("Account: {} has been authenticated.", controller.getAccount().getName());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(controller.getResources().getString("dialog.ask.token.valid.title"));
            alert.setHeaderText(controller.getResources().getString("dialog.ask.token.valid.header"));
            alert.setContentText(controller.getResources().getString("dialog.ask.token.valid.content"));

            alert.showAndWait();

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
        controller.getOkBtn().setDisable(true);
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
