package model.gameEngine.pieceEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Class that extends abstract class PieceEngine. Calculates the possible moves of a game piece
 * In Singleton pattern
 *
 * @author Young Jun
 */
public class PawnEngine extends PieceEngine {
    private static final ResourceBundle MOVES = ResourceBundle.getBundle("model/pieceInfo/Pawn");
    private static PawnEngine instance = new PawnEngine();

    /**
     * Getter of instance
     *
     * @return PawnEngine
     */
    public static PawnEngine getInstance() {
        return instance;
    }

    private PawnEngine() {
    }


    @Override
    // return a list of points a pawn can move to
    public List<Point> getMoves(int x, int y, int currentPlayer, List<Point> targetPieces, Set<Point> checkPieces) {
        updateLists(targetPieces, checkPieces);

        int player = myBoard.getPlayerNumber(x, y);

        List<Point> validMoves = new ArrayList<>();
        List<Point> potentialMoves = getSimpleMoves(x, y, currentPlayer, MOVES);

        // pawns can move two tiles from the initial tile
        if (player == 1 && y == myBoard.getHeight() - 2 && myBoard.getPlayerNumber(x, y - 1) == 0)
            potentialMoves.add(new Point(x, y - 2));
        if (player == 2 && y == 1 && myBoard.getPlayerNumber(x, y + 1) == 0) potentialMoves.add(new Point(x, y + 2));

        for (Point move : potentialMoves) {
            int xMove = move.x - x;
            int yMove = move.y - y;
            // if the tile is empty
            if (myBoard.getPlayerNumber(move.x, move.y) == 0) {
                // pawn can only move diagonally when capturing an opponent's piece
                if ((xMove == 1 && yMove == -1) | (xMove == -1 && yMove == -1) | (xMove == 1 && yMove == 1) |
                        (xMove == -1 && yMove == 1)) continue;
            } else {
                // pawn can't capture the opponent's piece with a vertical move
                if ((xMove == 0 && yMove == 1) | (xMove == 0 && yMove == 2) | (xMove == 0 && yMove == -1) |
                        (xMove == 0 && yMove == -2)) continue;
            }
            // add move to returnList if this move doesn't belong to one of the above restrictions
            validMoves.add(move);
        }
        possibleMoves = validMoves;

        return possibleMoves;
    }

}
