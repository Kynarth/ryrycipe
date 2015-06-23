package ryrycipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ryrycipe.util.LanguageUtil;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

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

    private final static Logger LOGGER = LoggerFactory.getLogger(Ryrycipe.class.getName());

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

        // Setup the MainWindow view
        initMainWindow();

        this.primaryStage.show();
    }

    public void initMainWindow() {
        try {
            // Load MainWindow view from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("view/MainWindow.fxml"));
            loader.setResources(ResourceBundle.getBundle("lang", locale));
            BorderPane rootLayout = loader.load();

            // Setup MainWindow
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
        } catch (IllegalStateException e) {
            LOGGER.error("Couldn't find the MainWindow.fxml file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Application's launcher
     * @param args Application's parameters
     */
    public static void main(String[] args) {
        launch(args);
    }

    public Locale getLocale() {
        return locale;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
