package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.gameEngine.ChessEngine;
import model.gameEngine.GameEngineFactory;
import model.util.ReflectionHandler;
import view.components.ChoiceView;
import view.gameView.GameView;
import view.gameView.GameViewFactory;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class responsible for communicating user's interaction from the frontend to the backend.
 *
 * @author Young Jun
 */
public class ChessController {
    private static final String CLASS_PATH = "controller.ChessController";
    private static final String LANGUAGE_RESOURCE_DIRECTORY = "view.resources.language.";
    private final ResourceBundle languageResource;
    private final Stage myStage;
    private GameView chessView;
    private ChessEngine chessEngine;
    private final ReflectionHandler reflectionHandler;
    private Point clickedTile;
    private ChoiceView choiceView;

    /**
     * Initializes game's model and view classes
     *
     * @param gameLanguage language
     * @param stage        stage
     */
    public ChessController(String gameLanguage, Stage stage) {
        myStage = stage;
        languageResource = makeResourceBundle(LANGUAGE_RESOURCE_DIRECTORY + gameLanguage);
        reflectionHandler = new ReflectionHandler();
        GameEngineFactory gameEngineFactory = new GameEngineFactory();
        chessEngine = (ChessEngine) gameEngineFactory.getGameEngine("Chess");
        GameViewFactory gameViewFactory = new GameViewFactory();
        this.chessView = gameViewFactory.getGameView("Chess", languageResource, e -> onTileClick((Point) e), 8, 8);
        choiceView = new ChoiceView();
    }

    /**
     * Sets the game's frontend and backend classes and starts the game
     */
    public void startGame() {
        initializeBoard();
        Scene scene = chessView.getGameScene();
        myStage.setScene(scene);
        myStage.show();
    }

    /**
     * Method called by frontend classes when a user clicks on a tile
     *
     * @param clickedTile location of the tile
     */
    private void onTileClick(Point clickedTile) {
        try {
            this.clickedTile = clickedTile;
            String clickType = chessEngine.clickType(clickedTile.x, clickedTile.y);
            System.out.println(clickType);
            reflectionHandler.handleMethod(clickType, CLASS_PATH).invoke(ChessController.this);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            chessView.showMessage("errorClick");
        }
    }

    private void clickOnPiece() {
        List<Point> validMoves = chessEngine.getValidMoves(clickedTile.x, clickedTile.y);
        chessView.highlightPossibleMoves(validMoves);
    }

    private void errorClick() {
        chessView.showMessage("errorClick");
    }

    private void movePiece() {
        Point currentPoint = chessEngine.getCurrentPieceLocation();
        int newX = clickedTile.x;
        int newY = clickedTile.y;
        chessEngine.movePiece(currentPoint.x, currentPoint.y, newX, newY);
        chessView.movePiece(currentPoint.x, currentPoint.y, newX, newY);
        chessView.updateCurrentPlayer();
        pawnPromotion(newX, newY);
        chessEngine.nextTurn();
        if (chessEngine.detectCheck()) {
            System.out.println("Checkmate : " + chessEngine.detectCheckMate());
            chessView.showMessage("Check");
        }
    }


    private void capturePiece() {
        int currentPlayer = chessEngine.getCurrentPlayer();
        Point currentPoint = chessEngine.getCurrentPieceLocation();
        int newX = clickedTile.x;;
        int newY = clickedTile.y;
        chessView.addCapturedPiece(currentPlayer, chessEngine.getPieceType(newX, newY));
        chessEngine.capturePiece(currentPoint.x, currentPoint.y, newX, newY);
        chessView.movePiece(currentPoint.x, currentPoint.y, newX, newY);
        chessView.updateCurrentPlayer();
        chessView.updatePlayerScore(currentPlayer, chessEngine.getPlayerScore(currentPlayer));
        pawnPromotion(newX, newY);
        chessEngine.nextTurn();
        if (chessEngine.detectCheck()) {
            System.out.println("Checkmate : " + chessEngine.detectCheckMate());
            chessView.showMessage("Check");
        }
    }

    private void pawnPromotion(int x, int y){
        if((y == 0 | y == 7) && chessEngine.getPieceType(x, y).equals("Pawn")){

            String[] capturedPieces = chessEngine.getCapturedPiece(chessEngine.getCurrentPlayer());
            if(capturedPieces.length == 0) return;

            String promotePiece = choiceView.getUserLanguage(capturedPieces[0], capturedPieces, "Choose a piece to promote");
            chessEngine.pawnPromotion(x,y,promotePiece);
            chessView.setTile(promotePiece, chessEngine.getCurrentPlayer(), y, x );
            chessView.removeCapturedPiece(chessEngine.getCurrentPlayer()%2+1, promotePiece);

        }
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String piece = chessEngine.getPieceType(i, j);
                int team = chessEngine.getPiecePlayerNumber(i, j);
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
