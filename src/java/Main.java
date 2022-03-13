import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main extends Application to use frontend featured from javaFx
 */
public class Main extends Application {


    /**
     * The start methods serves the role of a main method of this project.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainController mainController = new MainController();
        String gameLanguage = mainController.getUserLanguage();
    }
}
