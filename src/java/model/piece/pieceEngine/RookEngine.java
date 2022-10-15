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
public class RookEngine extends PieceEngine {
    private static final ResourceBundle MOVES = ResourceBundle.getBundle("model/pieceInfo/Rook");
    private static RookEngine instance = new RookEngine();

    /**
     * Getter of instance
     *
     * @return RookEngine
     */
    public static RookEngine getInstance() {
        return instance;
    }

    private RookEngine() {
    }

    @Override
    // return a list of points a rook can move to
    public List<Point> getMoves(int x, int y, int currentPlayer, List<Point> targetPieces, Set<Point> checkPieces) {
        updateLists(targetPieces, checkPieces);
        return getComplexMoves(x, y, currentPlayer, MOVES);
    }
}
