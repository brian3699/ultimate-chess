package model.board;

import java.awt.*;

/**
 * backend class of a chess board
 *
 * @author Young Jun
 */
public interface BoardInterface {

    /**
     * returns the state (name of the piece or empty) of the cell
     * @param x column number
     * @param y row number
     * @return state of the cell
     */
    char peek(int x, int y);

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
     * @param pieceType type of the piece that will be added
     * @param x column number of the cell
     * @param y row number of the cell
     * @return true if the piece has been successfully added, false if not
     */
    boolean setCell(char pieceType, int x, int y);

    /**
     * return possible moves of a cell
     * @param x
     * @param y
     * @return
     */
    Point[] getPossibleMoves(int x, int y);

}
