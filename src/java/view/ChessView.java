package view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ChessView implements GameViewInterface{
    private static final String MAGIC_VALUE_RESOURCE_PATH = "view.resources.MagicValues";
    private int currentPlayer;

    private final ResourceBundle languageResource;
    private final ResourceBundle magicValueResource;
    private HBox player1ScoreBox;
    private HBox player2ScoreBox;

    private BoardView myBoard;
    private Scene myGameScene;
    private VBox root;

    public ChessView(ResourceBundle languageResource,Consumer<Point> clickMethod, int rowCount, int colCount){
        currentPlayer = 0;
        magicValueResource = ResourceBundle.getBundle(MAGIC_VALUE_RESOURCE_PATH);
        this.languageResource = languageResource;
        myBoard = new BoardView(clickMethod, rowCount, colCount);
        root = new VBox();
        initializeGame();
    }

    private void initializeGame(){
        player1ScoreBox = new HBox(makeUserIcon("Player1", 1));
        player2ScoreBox = new HBox(makeUserIcon("Player2", 2));
        root.getChildren().addAll(player2ScoreBox, myBoard, player1ScoreBox);
        updateCurrentPlayer();
    }

    public void updateCurrentPlayer(){
        currentPlayer = currentPlayer % 2 + 1;
        if(currentPlayer == 1){
            player1ScoreBox.setStyle(magicValueResource.getString("4"));
            player2ScoreBox.setStyle(magicValueResource.getString("3"));
        }else {
            player2ScoreBox.setStyle(magicValueResource.getString("4"));
            player1ScoreBox.setStyle(magicValueResource.getString("3"));
        }
    }

    private Node makeUserIcon(String userName, int teamNumber){
        VBox userIcon = new VBox();
        Text user = new Text(languageResource.getString(userName));
        TileView king = new TileView("King", teamNumber, 40);
        userIcon.getChildren().addAll(user, king);
        return userIcon;
    }



    public Scene getGameScene(){
        myGameScene = new Scene(root);
        return myGameScene;
    }

    public void setTile(String pieceType, int team, int rowNum, int colNum){
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
    public void updateScore(int player1, int player2) {

    }

    @Override
    public void showMessage(String messageID) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(languageResource.getString(messageID));
        alert.setContentText(languageResource.getString(messageID));
        alert.showAndWait();
    }
}
