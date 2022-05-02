import controller.ChessController;
import controller.MenuController;
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
        MenuController mainController = new MenuController();
        String gameLanguage = mainController.getUserLanguage();
        ChessController chessController = new ChessController(gameLanguage, primaryStage);
        chessController.startGame();
    }
}
