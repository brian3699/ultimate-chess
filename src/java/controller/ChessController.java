package controller;

import com.opencsv.exceptions.CsvException;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.gameEngine.ChessEngine;
import model.util.ReflectionHandler;
import view.ChessView;
import view.GameViewInterface;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChessController {
    private static final String CLASS_PATH = "controller.ChessController";
    private static final String LANGUAGE_RESOURCE_DIRECTORY = "view.resources.language.";
    private final ResourceBundle languageResource;
    private final Stage myStage;
    private GameViewInterface chessView;
    private ChessEngine chessEngine;
    private final ReflectionHandler reflectionHandler;
    private Point clickedTile;



    public ChessController(String gameLanguage, Stage stage) throws IOException, CsvException {
        myStage = stage;
        languageResource = makeResourceBundle(LANGUAGE_RESOURCE_DIRECTORY + gameLanguage);
        reflectionHandler = new ReflectionHandler();
        this.chessEngine = new ChessEngine();
        this.chessView = new ChessView(languageResource, e -> onTileClick((Point) e), 8, 8);
    }

    public void startGame(){
        initializeBoard();
        Scene scene = chessView.getGameScene();
        myStage.setScene(scene);
        myStage.show();
    }

    private void onTileClick(Point clickedTile){

        try{
            this.clickedTile = clickedTile;
            String clickType = chessEngine.clickType(clickedTile.x, clickedTile.y);
            reflectionHandler.handleMethod(clickType,CLASS_PATH).invoke(ChessController.this);
        }catch (InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
            chessView.showMessage("errorClick");
        }

    }

    private void clickOnPiece(){
        ArrayList<Point> validMoves = chessEngine.getValidMoves(clickedTile.x, clickedTile.y);
        chessView.highlightPossibleMoves(validMoves);
    }

    private void errorClick(){
        chessView.showMessage("errorClick");
    }

    private void movePiece(){
        Point currentPoint = chessEngine.getCurrentPiece();
        chessEngine.movePiece(currentPoint.x, currentPoint.y, clickedTile.x, clickedTile.y);
        chessView.movePiece(currentPoint.x, currentPoint.y, clickedTile.x, clickedTile.y);
        chessView.updateCurrentPlayer();
        System.out.println(chessEngine.detectCheck());
    }

    private void capturePiece(){
        int currentPlayer = chessEngine.getCurrentPlayer();
        Point currentPoint = chessEngine.getCurrentPiece();
        chessView.addCapturedPiece(currentPlayer, chessEngine.getPieceType(clickedTile.x, clickedTile.y));
        chessEngine.capturePiece(currentPoint.x, currentPoint.y,clickedTile.x, clickedTile.y);
        chessView.movePiece(currentPoint.x, currentPoint.y, clickedTile.x, clickedTile.y);
        chessView.updateCurrentPlayer();
        chessView.updatePlayerScore(currentPlayer, chessEngine.getUserScore(currentPlayer));
        System.out.println(chessEngine.detectCheck());
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
