package model.piece;

/**
 * Data carrier class containing information of a board game piece.
 *
 * @author Young Jun
 */
public class Piece implements PieceInterface {


    String pieceType;
    int pieceScore;
    int playerNumber;

    public Piece(String pieceType, int pieceScore, int playerNumber){
        this.pieceType = pieceType;
        this.pieceScore = pieceScore;
        this.playerNumber = playerNumber;
    }


    @Override
    public String getPieceType() {
        return pieceType;
    }

    @Override
    public void setPieceType(String pieceType){
        this.pieceType = pieceType;
    }

    @Override
    public void setPieceScore(int pieceScore){
        this.pieceScore = pieceScore;
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
