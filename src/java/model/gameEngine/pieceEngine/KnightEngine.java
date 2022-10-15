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
public class KnightEngine extends PieceEngine {
    private static final ResourceBundle MOVES = ResourceBundle.getBundle("model/pieceInfo/Knight");
    private static KnightEngine instance = new KnightEngine();

    /**
     * Getter of instance
     *
     * @return KnightEngine
     */
    public static KnightEngine getInstance() {
        return instance;
    }

    private KnightEngine() {
    }

    @Override
    // return a list of points a knight can move to
    public List<Point> getMoves(int x, int y, int currentPlayer, List<Point> targetPieces, Set<Point> checkPieces) {
        updateLists(targetPieces, checkPieces);
        return getSimpleMoves(x, y, currentPlayer, MOVES);
    }


}
