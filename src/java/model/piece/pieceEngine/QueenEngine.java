package model.piece.pieceEngine;


import java.awt.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Class that extends abstract class PieceEngine. Calculates the possible moves of a game piece
 * In Singleton pattern
 *
 * @author Young Jun
 */
public class QueenEngine extends PieceEngine {
    private static final ResourceBundle MOVES = ResourceBundle.getBundle("model/pieceInfo/Queen");
    private static QueenEngine instance = new QueenEngine();

    /**
     * Getter of instance
     *
     * @return QueenEngine
     */
    public static QueenEngine getInstance() {
        return instance;
    }

    private QueenEngine() {
    }

    @Override
    // return a list of points a queen can move to
    public List<Point> getMoves(int x, int y, int currentPlayer, List<Point> targetPieces, Set<Point> checkPieces) {
        updateLists(targetPieces, checkPieces);
        return getComplexMoves(x, y, currentPlayer, MOVES);
    }
}
