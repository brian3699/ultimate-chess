package model.piece;

import java.awt.Point;
import java.util.List;

public interface PieceInterface {

    /**
     * return Piece Type
     * @return Piece Type
     */
    char getPieceType();

    /**
     * return the number of points this piece is worth
     * @return number of points
     */
    int getPieceScore();


    /**
     * return the player number this piece belongs to
     * @return player number
     */
    int getPlayerNumber();

    /**
     * return x coordinate
     * @return x coordinate
     */
    int getX();

    /**
     * return y coordinate
     * @return y coordinate
     */
    int getY();
}
