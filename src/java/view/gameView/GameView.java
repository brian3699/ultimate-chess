package view.gameView;

import javafx.scene.Scene;

import java.awt.*;
import java.util.List;

public abstract class GameView {

    /**
     * Highlight possible moves on the game board
     * @param possibleMoves List of possible moves
     */
    public abstract void highlightPossibleMoves(List<Point> possibleMoves);

    /**
     * Move piece to empty tile
     * @param xOrigin column number of a piece
     * @param yOrigin row number of a piece
     * @param xNew column number of a destination tile
     * @param yNew row number of a destination tile
     */
    public abstract void movePiece(int xOrigin, int yOrigin, int xNew, int yNew);




    /**
     * Display message to the user
     * @param messageID ID of the message
     */
    public abstract void showMessage(String messageID);

    public abstract Scene getGameScene();

    public abstract void setTile(String pieceType, int team, int rowNum, int colNum);

    public abstract void updateCurrentPlayer();

    public abstract void updatePlayerScore(int playerNumber, int score);

    public abstract void addCapturedPiece(int playerNumber, String pieceType);

}
