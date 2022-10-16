package view.gameView;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.components.BoardView;
import view.components.TileView;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * A class that extends GameView. Creates scene for a Chess game.
 *
 * @author Young Jun
 */
public class ChessView implements GameView {
    private static final String MAGIC_VALUE_RESOURCE_PATH = "view.resources.MagicValues";
    private static final String DEFAULT_STYLESHEET = "/view/resources/css/Default.css";
    private static final String TEXT_ID = "Text";
    private static final String PLAYER1 = "Player1";
    private static final String PLAYER2 = "Player2";
    private static final String SCORE = "Score";
    private static final String CAPTURED = "Captured";

    private int currentPlayer;

    private final ResourceBundle languageResource;
    private final ResourceBundle magicValueResource;
    private HBox player1Header;
    private HBox player2Header;
    private Text player1Score;
    private Text player2Score;
    private HBox player1Captured;
    private HBox player2Captured;

    private BoardView myBoard;
    private Scene myGameScene;
    private VBox root;
    private final boolean isXiangqi;


    /**
     * Constructor for ChessView.
     *
     * @param languageResource ResourceBundle containing language of the game
     * @param clickMethod      method that will be clicked when a user clicks on a tile
     * @param rowCount         number of rows
     * @param colCount         number of columns
     */
    public ChessView(ResourceBundle languageResource, Consumer<Point> clickMethod, int rowCount, int colCount, boolean isXiangqi) {
        currentPlayer = 0;
        this.isXiangqi = isXiangqi;
        // create ResourceBundle containing magic variables
        magicValueResource = ResourceBundle.getBundle(MAGIC_VALUE_RESOURCE_PATH);
        this.languageResource = languageResource;
        // Create BoardView
        myBoard = new BoardView(clickMethod, rowCount, colCount);
        root = new VBox();
        // initializes required variables
        initializeVariables();
        // Make elements that will be displayed on the game view and add to root
        createGameElements();
        // Creates the header for each player containing player and game information
        updateCurrentPlayer();
    }

    // method that initializes required variables
    private void initializeVariables() {
        player1Score = new Text(""+0);
        player2Score = new Text(""+0);
        player2Score.setId(TEXT_ID);
        player1Score.setId(TEXT_ID);
        player1Captured = new HBox();
        player2Captured = new HBox();
    }

    // Make elements that will be displayed on the game view and add to root
    private void createGameElements() {
        // make headers for player 1 and 2
        player1Header = makeHeader(PLAYER1, player1Score, player1Captured, 1);
        player2Header = makeHeader(PLAYER2, player2Score, player2Captured, 2);
        // add all elements to the root
        root.getChildren().addAll(player2Header, myBoard, player1Header);
    }

    // Creates the header for each player containing player and game information
    private HBox makeHeader(String userName, Text score, HBox playerCaptured, int playerNumber) {
        // create a Node containing the player number and user icon
        Node userIcon = makeUserIcon(userName, playerNumber);
        // create a node containing the user's current score and captured pieces
        Node scoreBoard = makeScoreBoard(score, playerCaptured);
        // create new HBox and return
        return new HBox(userIcon, scoreBoard);
    }

    @Override
    public void updateCurrentPlayer() {
        // update the current player
        // 1 -> 2 or 2 -> 1
        currentPlayer = currentPlayer % 2 + 1;
        if (currentPlayer == 1) {
            // change color of the scoreboard to let the user know whose turn it is
            player1Header.setStyle(magicValueResource.getString("4"));
            player2Header.setStyle(magicValueResource.getString("3"));
        } else {
            // change color of the scoreboard to let the user know whose turn it is
            player2Header.setStyle(magicValueResource.getString("4"));
            player1Header.setStyle(magicValueResource.getString("3"));
        }
    }

    // create a node containing the user's current score and captured pieces
    private Node makeScoreBoard(Text score, HBox playerCapturedPieces) {
        // Create text and add to HBox
        Text playerScore = new Text(languageResource.getString(SCORE));
        playerScore.setId(TEXT_ID);
        Text captured = new Text(languageResource.getString(CAPTURED));
        captured.setId(TEXT_ID);

        // first row contains user's current score
        HBox firstRow = new HBox(playerScore, score);
        // second row shows pieces that the user captured
        HBox secondRow = new HBox(captured, playerCapturedPieces);

        // create a new VBox and return
        return new VBox(firstRow, secondRow);
    }

    // Create a node containing user's name and user icon
    private Node makeUserIcon(String userName, int teamNumber) {
        VBox userIcon = new VBox();
        // user's name
        Text user = new Text(languageResource.getString(userName));
        user.setId(TEXT_ID);
        // the user's icon is the image of user's king
        TileView king = new TileView("King", teamNumber, 40);
        // add all elements to VBox and return
        userIcon.getChildren().addAll(user, king);
        return userIcon;
    }


    @Override
    public void updatePlayerScore(int playerNumber, int score) {
        if (playerNumber == 1) {
            // update Text to show user's updated score
            player1Score.setText(score + "");
        } else {
            // update Text to show user's updated score
            player2Score.setText(score + "");
        }
    }

    @Override
    public void addCapturedPiece(int playerNumber, String pieceType) {
        // 1 -> 2 or 2 -> 1
        int opponent = playerNumber % 2 + 1;
        // Make this change so that the correct image file is uploaded for Chess vs.Xiangqi
        if (opponent == 1 && isXiangqi) opponent += 2;
        // Create a new tile that will be added to the captured pieces
        TileView capturedPiece = new TileView(pieceType, opponent, 20);
        // Add the new piece to the correct player
        if (playerNumber == 1) {
            player1Captured.getChildren().add(capturedPiece);
        } else {
            player2Captured.getChildren().add(capturedPiece);
        }
    }

    @Override
    public void removeCapturedPiece(int playerNumber, String pieceType) {
        // Choose which player's list of capture pieces the piece must be removed from
        HBox captured = player2Captured;
        if (playerNumber == 1) captured = player1Captured;

        // Check pieces inside the list of captured pieces
        for (Node node : captured.getChildren()) {
            TileView piece = (TileView) node;
            // if the right piece is found, remove it
            if (piece.getPieceType().equals(pieceType)) {
                captured.getChildren().remove(node);
                break;
            }
        }
    }

    @Override
    public Scene getGameScene() {
        // create new scene from root
        myGameScene = new Scene(root);
        // apply css
        myGameScene.getStylesheets()
                .add(Objects.requireNonNull(getClass().getResource(DEFAULT_STYLESHEET)).toExternalForm());
        return myGameScene;
    }

    @Override
    public void setTile(String pieceType, int team, int rowNum, int colNum) {
        myBoard.setTile(pieceType, team, rowNum, colNum);
    }

    @Override
    public void highlightPossibleMoves(List<Point> possibleMoves) {
        myBoard.highlightPossibleMoves(possibleMoves);
    }

    @Override
    public void movePiece(int xOrigin, int yOrigin, int xNew, int yNew) {
        myBoard.movePiece(xOrigin, yOrigin, xNew, yNew);
    }

    @Override
    public void showMessage(String messageID) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // Set title
        alert.setTitle(languageResource.getString(messageID));
        // Set content
        alert.setContentText(languageResource.getString(messageID));
        alert.showAndWait();
    }
}
