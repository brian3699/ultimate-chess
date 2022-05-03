package model.piece;

/**
 * Data carrier class containing information of a board game piece.
 *
 * @author Young Jun
 */
public record Piece(String pieceType, int pieceScore, int playerNumber) implements PieceInterface {
    /**
     * Constructor for class Piece
     *
     * @param pieceType    type of the piece
     * @param pieceScore   score of the piece
     * @param playerNumber player number
     */
    public Piece {
    }


    @Override
    public String getPieceType() {
        return pieceType;
    }

    @Override
    public int getPieceScore() {
        return pieceScore;
    }

    @Override
    public int getPlayerNumber() {
        return playerNumber;
    }


}
