package model.gameEngine;

import com.opencsv.exceptions.CsvException;
import model.board.Board;
import model.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

class ChessEngineTest {
    ChessEngine chessEngine;

    @BeforeEach
    void setUp() throws CsvException, IOException {
        chessEngine = new ChessEngine();
    }

    @Test
    void test1() throws InvocationTargetException, IllegalAccessException {
        Board<Piece> myBoard = chessEngine.getBoard();
        System.out.println(myBoard.getPieceType(4,7));

    }

}