package model.gameEngine;

import com.opencsv.exceptions.CsvException;
import model.board.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

class ChessEngineTest {
    ChessEngine chessEngine;
    Board myBoard;
    private static final String DEFAULT_CHESS_BOARD_DATA_PATH = "resources/board/Default_Chess_Board.csv";
    private static final String DEFAULT_CHESS_TEAM_DATA_PATH = "resources/board/Default_Chess_Board_Team.csv";

    @BeforeEach
    void setUp() throws CsvException, IOException {
        chessEngine = ChessEngine.getInstance();
        chessEngine.initializeBoard(DEFAULT_CHESS_BOARD_DATA_PATH, DEFAULT_CHESS_TEAM_DATA_PATH);

    }

    @Test
    void testPawnMoveWhite() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Point> moves = (ArrayList<Point>) chessEngine.getValidMoves(5,6);
        Assertions.assertEquals("5, 5",moves.get(0).x + ", " + moves.get(0).y );
        Assertions.assertEquals("5, 4",moves.get(1).x + ", " + moves.get(1).y );
    }

    @Test
    void testKingMoveBlack() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Point> moves = (ArrayList<Point>) chessEngine.getValidMoves(4,7);
        for(Point p : moves) System.out.println(p.x + ", " + p.y);
        Assertions.assertEquals(0,moves.size());
    }

    @Test
    void testKingMoveWhite() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Point> moves = (ArrayList<Point>) chessEngine.getValidMoves(4,0);
        for(Point p : moves) System.out.println(p.x + ", " + p.y);
        Assertions.assertEquals(0,moves.size());
    }

    @Test
    void testQueenMoveBlack() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Point> moves = (ArrayList<Point>) chessEngine.getValidMoves(3,7);
        for(Point p : moves) System.out.println(p.x + ", " + p.y);
        Assertions.assertEquals(0,moves.size());
    }

    @Test
    void testQueenMoveWhite() throws InvocationTargetException, IllegalAccessException {
        ArrayList<Point> moves = (ArrayList<Point>) chessEngine.getValidMoves(3,0);
        for(Point p : moves) System.out.println(p.x + ", " + p.y);
        Assertions.assertEquals(0,moves.size());
    }

}