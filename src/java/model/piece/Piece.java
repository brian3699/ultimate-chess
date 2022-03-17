package model.piece;

public class Piece implements PieceInterface{
    private final int pieceScore;
    private final String pieceType;
    private final int playerNumber;


    /**
     * Made for testing
     * TODO: remove
     */
    public Piece(){
        pieceScore = 0;
        pieceType = null;
        playerNumber = 0;
    }

    public Piece(String pieceType, int pieceScore, int playerNumber){
        this.pieceScore = pieceScore;
        this.pieceType = pieceType;
        this.playerNumber = playerNumber;
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
