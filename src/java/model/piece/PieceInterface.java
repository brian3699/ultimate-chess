package model.piece;

/**
 * Interface of a data carrier class containing information of a board game piece.
 *
 * @author Young Jun
 */
public interface PieceInterface {


    /**
     * return Piece Type
     *
     * @return Piece Type
     */
    String getPieceType();



    /**
     * return the number of points this piece is worth
     *
     * @return number of points
     */
    int getPieceScore();

    /**
     * set piece type
     * @param pieceType piece type
     */
    void setPieceType(String pieceType);


    /**
     * return the player number this piece belongs to
     *
     * @return player number
     */
    int getPlayerNumber();

    /**
     * set pieceScore
     * @param pieceScore score of a piece
     */
    void setPieceScore(int pieceScore);



}
