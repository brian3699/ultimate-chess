package model.piece;

import java.awt.Point;
import java.util.List;

public class Piece implements PieceInterface{
    private final int pieceScore;
    private final char pieceType;
    private final int xLocation;
    private final int yLocation;
    private final int playerNumber;


    /**
     * Made for testing
     * TODO: remove
     */
    public Piece(){
        pieceScore = 0;
        pieceType = 0;
        xLocation = 0;
        yLocation = 0;
        playerNumber = 0;
    }

    public Piece(char pieceType, int pieceScore, int playerNumber, int x, int y){
        this.pieceScore = pieceScore;
        this.pieceType = pieceType;
        this.xLocation = x;
        this.yLocation = y;
        this.playerNumber = playerNumber;
    }
    
    
    @Override
    public char getPieceType() {
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

    @Override
    public int getX() {
        return xLocation;
    }

    @Override
    public int getY() {
        return yLocation;
    }
}
