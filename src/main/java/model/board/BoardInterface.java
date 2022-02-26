package model.board;

import java.awt.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * backend class of a chess board
 *
 * @author Young Jun
 */
public interface BoardInterface  {

    /**
     * returns the state (name of the piece or empty) of the cell
     * @param x column number
     * @param y row number
     * @return state of the cell
     */
    String peek(int x, int y);

    /**
     * moves the piece in the grid
     * @param x1 column number of the piece
     * @param y1 row number of the piece
     * @param x2 column number of the cell the piece will be moved to
     * @param y2 row number of the cell the piece will be moved to
     * @return true if the move could be made, false if not
     */
    boolean move(int x1, int y1, int x2, int y2);

    /**
     * add piece to a cell
     * @param pieceInfo ResourceBundle containing the information of the cell
     * @param x column number of the cell
     * @param y row number of the cell
     * @return true if the piece has been successfully added, false if not
     */
    public void setCell(int playerNumber, ResourceBundle pieceInfo, int x, int y);

    /**
     * Return the piece type of the cell
     * @param x x cell number
     * @param y y cell number
     * @return
     */
    public String getPieceType(int x, int y);

    /**
     * return arraylist of possible moves of a piece
     * @param x x cell number
     * @param y y cell number
     * @return arraylist of possible moves of a piece
     */
    public ArrayList<Point> getPossibleMoves(int x, int y);
}
