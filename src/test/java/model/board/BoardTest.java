package model.board;

import model.piece.Piece;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ResourceBundle;

class BoardTest {
    ResourceBundle testResource = ResourceBundle.getBundle("model/pieceInfo/ChessPiecePaths");
    Board<Piece> testBoard = new Board<>();

    @BeforeEach
    void setUp(){
        testResource.getString("Pawn");
    }

    @Test
    void testSetCell(){
        testBoard.setCell(1,ResourceBundle.getBundle(testResource.getString("King")), 3,3);
        Assertions.assertEquals(1, testBoard.getPlayerNumber(3,3));
        Assertions.assertEquals("King", testBoard.getPieceType(3,3));


    }

    @Test
    void testSetCellOverwrite(){
        testBoard.setCell(1,ResourceBundle.getBundle(testResource.getString("King")), 4,5);
        testBoard.setCell(2,ResourceBundle.getBundle(testResource.getString("Queen")), 4,5);
        Assertions.assertEquals(2, testBoard.getPlayerNumber(4,5));
        Assertions.assertEquals("Queen", testBoard.getPieceType(4,5));
    }

    @Test
    void testMoveToEmptyCell(){
        testBoard.setCell(1,ResourceBundle.getBundle(testResource.getString("King")), 3,3);
        testBoard.move(3,3,4,4);
        Assertions.assertEquals("King", testBoard.getPieceType(4,4));
        Assertions.assertEquals(1, testBoard.getPlayerNumber(4,4));

    }

    @Test
    void testMoveBeyondBoard(){
        testBoard.setCell(1,ResourceBundle.getBundle(testResource.getString("Pawn")), 3,3);
        Assertions.assertEquals(false, testBoard.move(3,3,15,15));
    }

    @Test
    void testMoveCapture(){
        testBoard.setCell(1,ResourceBundle.getBundle(testResource.getString("Pawn")), 3,3);
        testBoard.setCell(2,ResourceBundle.getBundle(testResource.getString("King")), 3,4);
        testBoard.move(3,3,3,4);
        Assertions.assertEquals("Pawn", testBoard.getPieceType(3,4));
        Assertions.assertEquals(1, testBoard.getPlayerNumber(3,4));
    }


}