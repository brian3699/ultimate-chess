package model.gameEngine;

import com.opencsv.exceptions.CsvException;
import model.board.Board;
import model.piece.Piece;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

class ChessEngineTest {
    ChessEngine chessEngine;
    Board<Piece> myBoard;

    @BeforeEach
    void setUp() throws CsvException, IOException {
        chessEngine = new ChessEngine();
        myBoard = chessEngine.getBoard();

    }

    @Test
    void testPawnMoveBlack() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Point> moves = chessEngine.getValidMoves(5,1);
        Assertions.assertEquals("Pawn", myBoard.getPieceType(5,1));
        Assertions.assertEquals("5, 2",moves.get(0).x + ", " + moves.get(0).y );
        Assertions.assertEquals("5, 3",moves.get(1).x + ", " + moves.get(1).y );

    }

    @Test
    void testPawnMoveWhite() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Point> moves = chessEngine.getValidMoves(5,6);
        Assertions.assertEquals("Pawn", myBoard.getPieceType(5,6));
        Assertions.assertEquals("5, 5",moves.get(0).x + ", " + moves.get(0).y );
        Assertions.assertEquals("5, 4",moves.get(1).x + ", " + moves.get(1).y );
    }

    @Test
    void testKingMoveBlack() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Point> moves = chessEngine.getValidMoves(4,7);
        Assertions.assertEquals("King", myBoard.getPieceType(4,7));
        for(Point p : moves) System.out.println(p.x + ", " + p.y);
        Assertions.assertEquals(0,moves.size());
    }

    @Test
    void testKingMoveWhite() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Point> moves = chessEngine.getValidMoves(4,0);
        Assertions.assertEquals("King", myBoard.getPieceType(4,0));
        for(Point p : moves) System.out.println(p.x + ", " + p.y);
        Assertions.assertEquals(0,moves.size());
    }

    @Test
    void testQueenMoveBlack() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Point> moves = chessEngine.getValidMoves(3,7);
        Assertions.assertEquals("Queen", myBoard.getPieceType(3,7));
        for(Point p : moves) System.out.println(p.x + ", " + p.y);
        Assertions.assertEquals(0,moves.size());
    }

    @Test
    void testQueenMoveWhite() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Point> moves = chessEngine.getValidMoves(3,0);
        Assertions.assertEquals("Queen", myBoard.getPieceType(3,0));
        for(Point p : moves) System.out.println(p.x + ", " + p.y);
        Assertions.assertEquals(0,moves.size());
    }

}