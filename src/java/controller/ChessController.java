package controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ai.MinimaxPlayer;
import model.board.Board;
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
import java.util.Timer;

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
    private Timer timer;
    protected GameView chessView;
    protected ChessEngine chessEngine;
    private final ReflectionHandler reflectionHandler;
    private Point clickedTile;
    private ChoiceView choiceView;
    private static final MinimaxPlayer minimaxPlayer = MinimaxPlayer.getInstance();
    private static final Board board = Board.getInstance();
    private static final boolean AI_MODE = true;

    /**
     * Initializes game's model and view classes
     *
     * @param gameLanguage language
     * @param stage        stage
     * @param boardFilePath path of board file
     * @param teamFilePath path of team file
     * @param isXiangqi whether this game is Chess vs. Xiangqi
     */
    public ChessController(String gameLanguage, Stage stage, String boardFilePath, String teamFilePath, boolean isXiangqi) {
        myStage = stage;
        languageResource = makeResourceBundle(LANGUAGE_RESOURCE_DIRECTORY + gameLanguage);
        reflectionHandler = new ReflectionHandler();
        GameEngineFactory gameEngineFactory = new GameEngineFactory();
        // Get ChessEngine
        chessEngine = (ChessEngine) gameEngineFactory.getGameEngine("Chess");
        // Initialize the backend board
        chessEngine.initializeBoard(boardFilePath, teamFilePath);
        GameViewFactory gameViewFactory = new GameViewFactory();
        // Get ChessView from factory
        chessView = gameViewFactory.getGameView("Chess", languageResource, e -> onTileClick((Point) e),
                8, 8, isXiangqi);
        choiceView = new ChoiceView();
    }

    /**
     * Sets the game's frontend and backend classes and starts the game
     */
    public void startGame() {
        initializeBoard();
        Scene scene = chessView.getGameScene();
        // Set scene to view of the chessboard created by ChessView
        myStage.setScene(scene);
        myStage.show();
        // Check whether time is over
    }

    /**
     * Method called by frontend classes when a user clicks on a tile
     *
     * @param clickedTile location of the tile
     */
    private void onTileClick(Point clickedTile) {
        try {
            this.clickedTile = clickedTile;
            // Get the type of click made by the user
            String clickType = chessEngine.clickType(clickedTile.x, clickedTile.y);
            // Invoke the correct method using reflection
            reflectionHandler.handleMethod(clickType, CLASS_PATH).invoke(ChessController.this);
        } catch (InvocationTargetException | IllegalAccessException e) {
            // Show message if it is not a valid click
            chessView.showMessage("errorClick");
        }
    }

    private void endGame(){
        chessView.showMessage("GameOver");
        MenuController menuController = new MenuController(myStage);
        menuController.startMenu();
    }

    private void clickOnPiece() {
        List<Point> validMoves = chessEngine.getValidMoves(clickedTile.x, clickedTile.y);
        chessView.highlightPossibleMoves(validMoves);
    }

    private void errorClick() {
        chessView.showMessage("errorClick");
    }

    private void movePiece() {
        int currentPlayer = chessEngine.getCurrentPlayer();
        Point currentPoint = chessEngine.getCurrentPieceLocation();
        int newX = clickedTile.x;
        int newY = clickedTile.y;
        chessEngine.movePiece(currentPoint.x, currentPoint.y, newX, newY);
        chessView.movePiece(currentPoint.x, currentPoint.y, newX, newY);
        chessView.updateCurrentPlayer();
        pawnPromotion(newX, newY);
        chessEngine.nextTurn();
        if (chessEngine.detectCheck()) {
            chessView.showMessage("Check");
        }
        if(AI_MODE == true && currentPlayer == 1){
            List<Point> move = minimaxPlayer.getBestMove(board, 2);
            onTileClick(move.get(0));
            onTileClick(move.get(1));
        }
    }


    private void capturePiece() {
        int currentPlayer = chessEngine.getCurrentPlayer();
        Point currentPoint = chessEngine.getCurrentPieceLocation();
        boolean gameOver = false;
        int newX = clickedTile.x;;
        int newY = clickedTile.y;
        if(chessEngine.detectGameOver(newX, newY)){
            gameOver = true;
        }
        chessView.addCapturedPiece(currentPlayer, chessEngine.getPieceType(newX, newY));
        chessEngine.capturePiece(currentPoint.x, currentPoint.y, newX, newY);
        chessView.movePiece(currentPoint.x, currentPoint.y, newX, newY);
        chessView.updateCurrentPlayer();
        chessView.updatePlayerScore(currentPlayer, chessEngine.getPlayerScore(currentPlayer));
        pawnPromotion(newX, newY);
        chessEngine.nextTurn();
        if(gameOver){
            endGame();
            return;
        }
        if (chessEngine.detectCheck()) {
            System.out.println("Checkmate : " + chessEngine.detectCheckMate());
            chessView.showMessage("Check");
        }
        if(AI_MODE == true && currentPlayer == 1){
            List<Point> move = minimaxPlayer.getBestMove(board, 2);
            onTileClick(move.get(0));
            onTileClick(move.get(1));
        }
    }

    // Method for handling pawn promotion
    private void pawnPromotion(int x, int y){
        if((y == 0 | y == 7) && chessEngine.getPieceType(x, y).equals("Pawn")){
            // return all captured pieces
            String[] capturedPieces = chessEngine.getCapturedPiece(chessEngine.getCurrentPlayer());
            // if there aren't any capture pieces, return
            if(capturedPieces.length == 0) return;
            // create a choice view where user chooses the piece to promote a pawn
            String promotePiece = choiceView.getUserLanguage(capturedPieces[0], capturedPieces, "Choose a piece to promote");
            // update the model
            chessEngine.pawnPromotion(x,y,promotePiece);
            // update the frontend
            chessView.setTile(promotePiece, chessEngine.getCurrentPlayer(), y, x );
            chessView.removeCapturedPiece(chessEngine.getCurrentPlayer()%2+1, promotePiece);

        }
    }

    /**
     * Initialize the frontend chess board
     */
    public void initializeBoard() {
        // row
        for (int i = 0; i < 8; i++) {
            // column
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

    /**
     // Update timer every second and ends game if the time expires
     private void runTimer() {
         timer = new Timer();
         // Schedule a timer
             timer.schedule(new TimerTask() {
            @Override
            // method to run every second
            public void run() {
            if(chessView.timerStep()) {
                endGame();
            }
        }}, 0, 1000);
     }
     **/


}
