package view.gameView;

import javafx.scene.Scene;

import java.awt.*;
import java.util.List;

/**
 * Abstract class containing methods for board game frontend classes
 *
 * @author Young Jun
 */
public abstract class GameView {

    /**
     * Highlight possible moves on the game board
     *
     * @param possibleMoves List of possible moves
     */
    public abstract void highlightPossibleMoves(List<Point> possibleMoves);

    /**
     * Move piece to empty tile
     *
     * @param xOrigin column number of a piece
     * @param yOrigin row number of a piece
     * @param xNew    column number of a destination tile
     * @param yNew    row number of a destination tile
     */
    public abstract void movePiece(int xOrigin, int yOrigin, int xNew, int yNew);


    /**
     * Display message to the user
     *
     * @param messageID ID of the message
     */
    public abstract void showMessage(String messageID);

    /**
     * getter method for gameScene
     *
     * @return scene of the game
     */
    public abstract Scene getGameScene();

    /**
     * sets the tile
     *
     * @param pieceType type of the piece
     * @param team      team number
     * @param rowNum    row number
     * @param colNum    column number
     */
    public abstract void setTile(String pieceType, int team, int rowNum, int colNum);

    /**
     * changes the current player displayed on the game scene
     */
    public abstract void updateCurrentPlayer();

    /**
     * updates player's score displayed on the game scene
     *
     * @param playerNumber player number
     * @param score        player's current score
     */
    public abstract void updatePlayerScore(int playerNumber, int score);

    /**
     * display capture pieces
     *
     * @param playerNumber player number
     * @param pieceType    type of the captured piece
     */
    public abstract void addCapturedPiece(int playerNumber, String pieceType);

}
