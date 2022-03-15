package view;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ChessView implements GameViewInterface{
    private final ResourceBundle languageResource;
    private BoardView myBoard;
    private Scene myGameScene;
    private VBox root;

    public ChessView(ResourceBundle languageResource,Consumer<Point> clickMethod, int rowCount, int colCount){
        this.languageResource = languageResource;
        myBoard = new BoardView(clickMethod, rowCount, colCount);
        root = new VBox();
        myGameScene = new Scene(root);
    }

    private void initializeGame(){
        Text title = new Text(languageResource.getString("Chess"));
        Text scoreBoard = new Text(languageResource.getString("Player1") + 0 +
                languageResource.getString("Player2") + 1);
        root.getChildren().addAll(title, scoreBoard, myBoard);
    }

    public void setTile(int rowNum, int colNum, String pieceType){
        myBoard.setTile(rowNum, colNum, pieceType);
    }

    @Override
    public void showMove(List<Point> possibleMoves) {
        myBoard.highlightPossibleMoves(possibleMoves);
    }

    @Override
    public void movePiece(int xOrigin, int yOrigin, int xNew, int yNew) {
        myBoard.movePiece(xOrigin, yOrigin, xNew, yNew);
    }

    @Override
    public void capturePiece(int xOrigin, int yOrigin, int xNew, int yNew) {
        myBoard.capturePiece(xOrigin, yOrigin, xNew, yNew);
    }

    @Override
    public void updateScore(int player1, int player2) {

    }

    @Override
    public void showMessage(String messageID) {

    }
}
