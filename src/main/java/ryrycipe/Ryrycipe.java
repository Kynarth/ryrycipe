package ryrycipe;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ryrycipe.util.LanguageUtil;

import java.util.Locale;

/**
 * <h1>Application to manage Ryzom's craft plans</h1>
 * <p>
 * This application able the user to create, read and upload his own recipe. It also offers the possibility to
 * watch and import recipes from others users who upload them in the public cloud. The application should calculate
 * the user's plan stats in function of chosen materials, this will allow the user to choose the recipe that best fits
 * his wishes.
 * </p>
 */
public class Ryrycipe extends Application {

    /**
     * Application's {@link Locale}.
     * It defines if the app's language is english or french.
     */
    private Locale locale;

    /**
     * Application {@link Stage}.
     */
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Ryrycipe");
        this.primaryStage.getIcons().add(new Image("/images/logo.png"));

        // Get user's language
        this.locale = new Locale(LanguageUtil.getLanguage());

        this.primaryStage.show();
    }

    public Locale getLocale() {
        return locale;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
