package model.piece;

import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

public class Piece implements PieceInterface{
    private final int pieceScore;
    private final String pieceType;
    private final int xLocation;
    private final int yLocation;
    private final int playerNumber;
    private final ResourceBundle pieceInfo;

    private static final String POINT_ID = "point";
    private static final String NAME_ID = "name";



    /**
     * Made for testing
     * TODO: remove
     */
    public Piece(){
        pieceScore = 0;
        pieceType = null;
        pieceInfo = null;
        xLocation = 0;
        yLocation = 0;
        playerNumber = 0;
    }

    public Piece(ResourceBundle pieceInfo, int playerNumber, int x, int y){
        this.pieceScore = Integer.parseInt(pieceInfo.getString(POINT_ID));
        this.pieceType = pieceInfo.getString(NAME_ID);
        this.pieceInfo = pieceInfo;
        this.xLocation = x;
        this.yLocation = y;
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

    @Override
    public int getX() {
        return xLocation;
    }

    @Override
    public int getY() {
        return yLocation;
    }

    @Override
    public ArrayList<Point> getMoves(int x, int y) {
        int team = 1;
        if(playerNumber == 2) team = -1;
        ArrayList<Point> possibleMoves = new ArrayList<>();
        Enumeration<String> keys = pieceInfo.getKeys();
        // skip the first two keys.
        keys.nextElement();
        keys.nextElement();
        // enumerate all possible moves of a piece
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String[] move = pieceInfo.getString(key).split(",");
            int newX = x + Integer.parseInt(move[0])*team;
            int newY = y + Integer.parseInt(move[1])*team;
            possibleMoves.add(new Point(newX, newY));
        }
        return possibleMoves;
    }


}
