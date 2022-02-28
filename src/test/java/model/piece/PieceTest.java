package model.piece;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    @Test
    void testInitialization(){
        String pieceType = "Pawn";
        int pieceScore = 50;
        int playerNumber = 1;
        int x = 3;
        int y = 7;
        Piece piece  = new Piece( pieceType,  pieceScore,  playerNumber,  x,  y);
        assertEquals(pieceType, piece.getPieceType());
        assertEquals(pieceScore, piece.getPieceScore());
        assertEquals(playerNumber, piece.getPlayerNumber());
        assertEquals(x, piece.getX());
        assertEquals(y, piece.getY());
    }

    @Test
    void testInitializationExtreme(){
        String pieceType = "test";
        int pieceScore = -100;
        int playerNumber = -10;
        int x = -200;
        int y = -300;
        Piece piece  = new Piece( pieceType,  pieceScore,  playerNumber,  x,  y);
        assertEquals(pieceType, piece.getPieceType());
        assertEquals(pieceScore, piece.getPieceScore());
        assertEquals(playerNumber, piece.getPlayerNumber());
        assertEquals(x, piece.getX());
        assertEquals(y, piece.getY());
    }

}