package view;

import javafx.scene.Scene;

import java.awt.*;
import java.util.List;

public interface GameViewInterface {

    /**
     * Highlight possible moves on the game board
     * @param possibleMoves List of possible moves
     */
    public void highlightPossibleMoves(List<Point> possibleMoves);

    /**
     * Move piece to empty tile
     * @param xOrigin column number of a piece
     * @param yOrigin row number of a piece
     * @param xNew column number of a destination tile
     * @param yNew row number of a destination tile
     */
    public void movePiece(int xOrigin, int yOrigin, int xNew, int yNew);



    /**
     * Update score on score board
     * @param player1 player1 score
     * @param player2 player2 score
     */
    public void updateScore(int player1, int player2);

    /**
     * Display message to the user
     * @param messageID ID of the message
     */
    public void showMessage(String messageID);

    public Scene getGameScene();

    public void setTile(String pieceType, int team, int rowNum, int colNum);

}
