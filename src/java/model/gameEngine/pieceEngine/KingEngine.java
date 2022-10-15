package model.gameEngine.pieceEngine;

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
public class KingEngine extends PieceEngine {
    private static final ResourceBundle MOVES = ResourceBundle.getBundle("model/pieceInfo/King");
    private static KingEngine instance = new KingEngine();

    private KingEngine() {
    }

    /**
     * Getter of instance
     *
     * @return KingEngine
     */
    public static KingEngine getInstance() {
        return instance;
    }

    @Override
    // return a list of points a king can move to
    public List<Point> getMoves(int x, int y, int currentPlayer, List<Point> targetPieces, Set<Point> checkPieces) {
        updateLists(targetPieces, checkPieces);
        return getSimpleMoves(x, y, currentPlayer, MOVES);
    }
}
