package model.piece;

public interface PieceInterface {

    /**
     * return Piece Type
     * @return Piece Type
     */
    char getPieceType();

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
