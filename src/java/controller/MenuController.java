package controller;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.gameView.MenuView;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Controller class responsible for creating the menu scene and communication user's choices to the backend.
 *
 * @author Young Jun
 */
public class MenuController {
    private static final String DEFAULT_CHESS_BOARD_DATA_PATH = "resources/board/Default_Chess_Board.csv";
    private static final String DEFAULT_CHESS_TEAM_DATA_PATH = "resources/board/Default_Chess_Board_Team.csv";
    private static final String DEFAULT_CHESS_XIANGQI_BOARD_DATA_PATH = "resources/board/Default_Chess_Xiangqi_Board.csv";
    private static final String DEFAULT_CHESS_XIANGQI_TEAM_DATA_PATH = "resources/board/Default_Chess_Xiangqi_Board_Team.csv";
    private static final String DEFAULT_LANGUAGE = "English";
    private static final String DEFAULT_LANGUAGE_RESOURCE_DIRECTORY = "view.resources.language.English";
    private static final String CHESS = "Chess1";
    private static final String CHESS_VS_CHINESE_CHESS = "Chess2";
    private static final String PAGE_TITLE_ID = "title";
    private final ResourceBundle languageResource;


    private MenuView menuView;
    private Map<String, EventHandler> buttonMap;
    private Stage stage;

    /**
     * Constructor for MenuController
     */
    public MenuController(Stage stage) {
        languageResource = ResourceBundle.getBundle(DEFAULT_LANGUAGE_RESOURCE_DIRECTORY);
        this.stage = stage;
        buttonMap = new TreeMap<>();
        populateButtonMap();
        menuView = new MenuView(languageResource);
    }

    /**
     * creates the menuScene and show the stage
     */
    public void startMenu(){
        Scene scene = menuView.generateMenuScene(PAGE_TITLE_ID, buttonMap );
        stage.setScene(scene);
        stage.show();
    }

    // starts chess game
    private void startChess(){
        ChessController chessController = new ChessController(DEFAULT_LANGUAGE, stage,
                DEFAULT_CHESS_BOARD_DATA_PATH, DEFAULT_CHESS_TEAM_DATA_PATH, false);
        chessController.startGame();
    }

    // starts chess vs. chinese chess game
    private void startChessVsXiangqi(){
        ChessVsXiangqiController chessVsXiangqiController = new ChessVsXiangqiController(DEFAULT_LANGUAGE,
                stage, DEFAULT_CHESS_XIANGQI_BOARD_DATA_PATH, DEFAULT_CHESS_XIANGQI_TEAM_DATA_PATH);
        chessVsXiangqiController.startGame();
    }

    // add button name and event to the buttonMap
    private void populateButtonMap(){
        buttonMap.put(CHESS, event -> startChess());
        buttonMap.put(CHESS_VS_CHINESE_CHESS, event -> startChessVsXiangqi());
    }
}
