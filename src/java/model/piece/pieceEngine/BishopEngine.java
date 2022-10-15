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
public class BishopEngine extends PieceEngine {
    private static final ResourceBundle MOVES = ResourceBundle.getBundle("model/pieceInfo/Bishop");
    private static BishopEngine instance = new BishopEngine();

    /**
     * Getter of instance
     *
     * @return BishopEngine
     */
    public static BishopEngine getInstance() {
        return instance;
    }

    private BishopEngine() {
    }

    @Override
    // return a list of points a bishop can move to
    public List<Point> getMoves(int x, int y, int currentPlayer, List<Point> targetPieces, Set<Point> checkPieces) {
        updateLists(targetPieces, checkPieces);

        return getComplexMoves(x, y, currentPlayer, MOVES);
    }
}
