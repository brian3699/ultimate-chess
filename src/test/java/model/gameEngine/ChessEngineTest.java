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

    @BeforeEach
    void setUp() throws CsvException, IOException {
        chessEngine = new ChessEngine();
    }

    @Test
    void testPawnMoveBlack() throws InvocationTargetException, IllegalAccessException {
        Board<Piece> myBoard = chessEngine.getBoard();
        ArrayList<Point> moves = chessEngine.getValidMoves(5,1);
        Assertions.assertEquals("Pawn", myBoard.getPieceType(5,1));
        Assertions.assertEquals("5, 2",moves.get(0).x + ", " + moves.get(0).y );
        Assertions.assertEquals("5, 3",moves.get(1).x + ", " + moves.get(1).y );

    }

    @Test
    void testPawnMoveWhite() throws InvocationTargetException, IllegalAccessException {
        Board<Piece> myBoard = chessEngine.getBoard();
        ArrayList<Point> moves = chessEngine.getValidMoves(5,6);
        Assertions.assertEquals("Pawn", myBoard.getPieceType(5,6));
        Assertions.assertEquals("5, 5",moves.get(0).x + ", " + moves.get(0).y );
        Assertions.assertEquals("5, 4",moves.get(1).x + ", " + moves.get(1).y );
    }

}