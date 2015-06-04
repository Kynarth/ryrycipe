package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import ryrycipe.Ryrycipe;
import ryrycipe.util.DBConnection;
import ryrycipe.util.LocaleUtil;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType;


/**
 * Controller for the Ryrycipe view.
 */
public class RyrycipeController implements Initializable {

    /**
     * Create a new RecipeCreator view.
     */
    @FXML
    public Button newBtn;

    /**
     * Search for user's saved recipe.
     */
    @FXML
    public Button searchBtn;

    /**
     * Upload the current recipe.
     */
    @FXML
    public Button uploadBtn;

    /**
     * Call a dialog to ask to change current language.
     * Application has to restart to apply changes.
     */
    @FXML
    public Button languageBtn;

    /**
     * Save current recipe.
     */
    @FXML
    public Button saveBtn;

    private Ryrycipe mainApp;
    private ResourceBundle resources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
    }

    /**
     * Save the current recipe.
     */
    @FXML
    public void save() {
        if (mainApp.getCreatorController().isPlanFilled()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(resources.getString("dialog.save.success.title"));
            alert.setContentText(resources.getString("dialog.save.success.content"));

            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle(resources.getString("dialog.save.fail.title"));
            alert.setHeaderText(resources.getString("dialog.save.fail.header"));
            alert.setContentText(resources.getString("dialog.save.fail.content"));

            alert.showAndWait();
        }
    }

    /**
     * Toggle language between french and english.
     */
    @FXML
    public void changeLanguage() {
        if (mainApp.getLocale().toString().equals("fr")) {
            LocaleUtil.setLanguage("en");
            DBConnection.changeLanguage();  // update the path to the database in function of language
            mainApp.setLocale(new Locale("en"));
        } else {
            LocaleUtil.setLanguage("fr");
            DBConnection.changeLanguage();  // update the path to the database in function of language
            mainApp.setLocale(new Locale("fr"));
        }
        mainApp.initialize();
        mainApp.showRecipeCreator();
    }

    public void setMainApp(Ryrycipe mainApp) {
        this.mainApp = mainApp;
    }
}
