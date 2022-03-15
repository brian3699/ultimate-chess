package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.gameEngine.ChessEngine;
import view.ChessView;
import view.GameViewInterface;

import java.util.ResourceBundle;

public class ChessController {
    private static final String LANGUAGE_RESOURCE_DIRECTORY = "view.resources.language.";
    private final ResourceBundle languageResource;
    private final Stage myStage;
    private GameViewInterface chessView;
    private ChessEngine chessEngine;


    public ChessController(String gameLanguage, Stage stage){
        myStage = stage;
        languageResource = makeResourceBundle(LANGUAGE_RESOURCE_DIRECTORY + gameLanguage);
        this.chessEngine = new ChessEngine();
        this.chessView = new ChessView(languageResource, e -> startGame(), 8, 8);
    }

    public void startGame(){
        initializeBoard();
        Scene scene = chessView.getGameScene();
        myStage.setScene(scene);
        myStage.show();
    }

    private void initializeBoard(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j< 8; j++){
                String piece = chessEngine.getPieceType(i, j);
                int team = chessEngine.getPieceTeam(i,j);
                chessView.setTile(piece, team, j, i);
            }
        }
    }

    // make ResourceBundle and return
    private ResourceBundle makeResourceBundle(String path) {
        try {
            return ResourceBundle.getBundle(path);
        } catch (Exception e) {
            return null;
        }
    }
}
