package model.board;

import java.util.ResourceBundle;

/**
 * backend class of a chess board
 *
 * @author Young Jun
 */
public interface BoardInterface {

    public void capture(int x1, int y1, int x2, int y2);

    // move piece to a new cell
    public void movePiece(int x1, int y1, int x2, int y2);

    /**
     * add piece to a cell
     *
     * @param pieceInfo ResourceBundle containing the information of the cell
     * @param x         column number of the cell
     * @param y         row number of the cell
     * @return true if the piece has been successfully added, false if not
     */
    public void setCell(int playerNumber, ResourceBundle pieceInfo, int x, int y);

    /**
     * Return the piece type of the cell
     *
     * @param x x cell number
     * @param y y cell number
     * @return
     */
    public String getPieceType(int x, int y);


    /**
     * return the player number of a piece
     *
     * @param x x cell number
     * @param y y cell number
     * @return return the player number of a piece
     */
    public int getPlayerNumber(int x, int y);

    public int getPiecePoint(int x, int y);

    public void revert(int x1, int y1, int x2, int y2);
}
