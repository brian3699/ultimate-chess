package model.board;

import java.util.ResourceBundle;

/**
 * backend interface of a game board
 *
 * @author Young Jun
 */
public interface BoardInterface {

    /**
     * captures a piece in the backend
     *
     * @param x1 x coordinate of the moving piece
     * @param y1 y coordinate of the moving piece
     * @param x2 x coordinate of the captured piece
     * @param y2 y coordinate of the captured piece
     */
    public void capture(int x1, int y1, int x2, int y2);

    /**
     * moves a piece in the backend
     *
     * @param x1 x coordinate of the moving piece
     * @param y1 y coordinate of the moving piece
     * @param x2 x coordinate of the destination tile
     * @param y2 y coordinate of the destination tile
     */
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

    /**
     * revert a move
     *
     * @param x1 x coordinate of the original location
     * @param y1 y coordinate of the original location
     * @param x2 x coordinate of the current location
     * @param y2 y coordinate of the current location
     */
    public void revert(int x1, int y1, int x2, int y2);

    /**
     * getter method of board's width
     *
     * @return board's width
     */
    public int getWidth();

    /**
     * getter method of board's height
     *
     * @return board's height
     */
    public int getHeight();

    /**
     * return array of player's captured pieces
     * @param playerNumber player number
     * @return player's captured pieces
     */
    public String[] getCapturedPieceList(int playerNumber);

    /**
     * change pawn to another piece
     * @param x col number
     * @param y row number
     * @param pieceType type of piece
     */
    public void pawnPromotion(int x, int y, String pieceType);




    }
