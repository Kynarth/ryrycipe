package ryrycipe.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import ryrycipe.Ryrycipe;
import ryrycipe.util.DBConnection;
import ryrycipe.util.LocaleUtil;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
