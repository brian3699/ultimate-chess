package view.gameView;

import javafx.animation.Timeline;
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
public class ChessView extends GameView {
    private static final String MAGIC_VALUE_RESOURCE_PATH = "view.resources.MagicValues";
    private static final String DEFAULT_STYLESHEET = "/view/resources/css/Default.css";
    private static final String TEXT_ID = "Text";
    private int currentPlayer;

    private final ResourceBundle languageResource;
    private final ResourceBundle magicValueResource;
    private HBox player1Header;
    private HBox player2Header;
    private Text player1Score;
    private Text player2Score;
    private HBox player1Captured;
    private HBox player2Captured;
    //private boolean timeOver;
    //private Text player1Timer;
    //private Text player2Timer;

    private Timeline myAnimation;
    private BoardView myBoard;
    private Scene myGameScene;
    private VBox root;
    private final boolean isXiangqi;
    private Consumer<Point> clickMethod;


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
        magicValueResource = ResourceBundle.getBundle(MAGIC_VALUE_RESOURCE_PATH);
        this.languageResource = languageResource;
        this.clickMethod = clickMethod;
        myBoard = new BoardView(clickMethod, rowCount, colCount);
        root = new VBox();
        initializeVariables();
        initializeGame();
        updateCurrentPlayer();
    }

    private void initializeVariables() {
        //player1Timer = new Text("60");
        //player2Timer = new Text("60");
        player1Score = new Text("0");
        player2Score = new Text("0");
        player2Score.setId(TEXT_ID);
        player1Score.setId(TEXT_ID);
        player1Captured = new HBox();
        player2Captured = new HBox();
    }

    private void initializeGame() {
        player1Header = makeHeader("Player1", player1Score, player1Captured, 1);
        player2Header = makeHeader("Player2", player2Score, player2Captured, 2);

        root.getChildren().addAll(player2Header, myBoard, player1Header);
    }



    /**
     * Creates header
     *
     * @param userName       player's name
     * @param score          player's score
     * @param playerCaptured player's capture pieces
     * @param playerNumber   player number
     * @return header
     */
    public HBox makeHeader(String userName, Text score, HBox playerCaptured, int playerNumber) {
        Node userIcon = makeUserIcon(userName, playerNumber);
        //Node timer = makeTimer(playerTimer);
        Node scoreBoard = makeScoreBoard(score, playerCaptured);

        //return new HBox(userIcon, timer, scoreBoard);
        return new HBox(userIcon, scoreBoard);
    }

    @Override
    public void updateCurrentPlayer() {
        currentPlayer = currentPlayer % 2 + 1;
        if (currentPlayer == 1) {
            player1Header.setStyle(magicValueResource.getString("4"));
            player2Header.setStyle(magicValueResource.getString("3"));
        } else {
            player2Header.setStyle(magicValueResource.getString("4"));
            player1Header.setStyle(magicValueResource.getString("3"));
        }
    }


    private Node makeScoreBoard(Text score, HBox playerCapturedPieces) {
        Text playerScore = new Text(languageResource.getString("Score"));
        playerScore.setId(TEXT_ID);
        Text captured = new Text(languageResource.getString("Captured"));
        captured.setId(TEXT_ID);

        HBox firstRow = new HBox(playerScore, score);
        HBox secondRow = new HBox(captured, playerCapturedPieces);

        return new VBox(firstRow, secondRow);
    }

    private Node makeUserIcon(String userName, int teamNumber) {
        VBox userIcon = new VBox();
        Text user = new Text(languageResource.getString(userName));
        user.setId(TEXT_ID);
        TileView king = new TileView("King", teamNumber, 40);
        userIcon.getChildren().addAll(user, king);
        return userIcon;
    }


    @Override
    public void updatePlayerScore(int playerNumber, int score) {
        if (playerNumber == 1) {
            player1Score.setText(score + "");
        } else {
            player2Score.setText(score + "");
        }
    }

    @Override
    public void addCapturedPiece(int playerNumber, String pieceType) {
        int opponent = playerNumber % 2 + 1;
        if(opponent == 1 && isXiangqi) opponent += 2;
        TileView capturedPiece = new TileView(pieceType, opponent, 20);
        if (playerNumber == 1) {
            player1Captured.getChildren().add(capturedPiece);
        } else {
            player2Captured.getChildren().add(capturedPiece);
        }
    }

    @Override
    public void removeCapturedPiece(int playerNumber, String pieceType){
        HBox captured = player2Captured;
        if(playerNumber == 1) captured = player1Captured;

        for(Node node : captured.getChildren()){
            TileView piece = (TileView) node;
            if(piece.getPieceType().equals(pieceType)){
                captured.getChildren().remove(node);
                break;
            }
        }
    }

    @Override
    public Scene getGameScene() {
        myGameScene = new Scene(root);
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
        //player1Timer.setText("60");
        //player2Timer.setText("60");
        myBoard.movePiece(xOrigin, yOrigin, xNew, yNew);
    }

    @Override
    public void showMessage(String messageID) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(languageResource.getString(messageID));
        alert.setContentText(languageResource.getString(messageID));
        alert.showAndWait();
    }

    /**
    @Override
    public boolean timerStep() {
        Text timer;
        if (currentPlayer == 1) timer = player1Timer;
        else timer = player2Timer;
        int time = Integer.parseInt(timer.getText()) - 1;
        if(time < 0){
            time = 0;
            timeOver = true;
        }

        timer.setText(time + "");
        return timeOver;
    }

    private Node makeTimer(Text playerTimer) {
    Text remainingTime = new Text(languageResource.getString("Remaining"));
    Text seconds = new Text(languageResource.getString("Seconds"));

    return new HBox(remainingTime, playerTimer, seconds);
    }

    **/
}
