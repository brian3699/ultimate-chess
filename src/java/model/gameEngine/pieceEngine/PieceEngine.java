package model.gameEngine.pieceEngine;

import model.board.Board;
import model.gameEngine.ChessEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Calculates the possible moves of a game piece.
 * In Singleton pattern
 *
 * @author Young Jun
 */
public abstract class PieceEngine {
    protected Board myBoard = Board.getInstance(); //TODO refactor
    protected ChessEngine chessEngine = ChessEngine.getInstance();
    protected List<Point> possibleMoves;
    private List<Point> targetPieces;
    private Set<Point> checkPieces;


    public abstract List<Point> getMoves(int x, int y, int currentPlayer, List<Point> targetPieces, Set<Point> checkPieces);

    protected List<Point> getSimpleMoves(int x, int y, int playerNumber, ResourceBundle pieceMoves) {

        // enumerate all possible moves of a piece
        for (String key : pieceMoves.keySet()) {
            String teamNumber = "" + key.charAt(0);
            if (!teamNumber.equals("" + playerNumber)) continue;
            // add only valid moves to possibleMoves
            addValidMoves(x, y, pieceMoves, key, 1);
        }
        return possibleMoves;
    }

    // calculate and return possible moves of Queen, Bishop, and Rook
    protected List<Point> getComplexMoves(int x, int y, int playerNumber, ResourceBundle pieceMoves) {

        // enumerate all possible moves of a piece
        for (String key : pieceMoves.keySet()) {
            String teamNumber = "" + key.charAt(0);
            if (!teamNumber.equals("" + myBoard.getPlayerNumber(x, y))) continue;
            // Queen, Bishop, and Rook can move multiple tiles in one move
            for (int i = 1; i < Math.max(myBoard.getWidth() + 1, myBoard.getHeight() + 1); i++) {
                if (!addValidMoves(x, y, pieceMoves, key, i)) break;
            }
        }
        return possibleMoves;
    }

    protected boolean addValidMoves(int x, int y, ResourceBundle pieceMoves, String key, int multiplier) {
        String[] move = pieceMoves.getString(key).split(",");
        int newX = Integer.parseInt(move[0]) * multiplier + x;
        int newY = Integer.parseInt(move[1]) * multiplier + y;

        int player = myBoard.getPlayerNumber(x, y);
        int opponent = player % 2 + 1;
        int destinationTilePlayer = myBoard.getPlayerNumber(newX, newY);

        // a piece can't move to a tile occupied by the current player's piece
        boolean isMyPiece = destinationTilePlayer == player;
        // a piece can't move beyond Board
        boolean isMoveInBoard = newX >= 0 && newX < myBoard.getWidth() && newY >= 0 && newY < myBoard.getHeight();

        // add to possibleMoves if a move satisfies both conditions
        if (!isMyPiece && isMoveInBoard) {
            possibleMoves.add(new Point(newX, newY));
            if ((newX == chessEngine.getPlayer1King().x && newY == chessEngine.getPlayer1King().y) ||
                    (newX == chessEngine.getPlayer2King().x && newY == chessEngine.getPlayer2King().y)) {
                checkPieces.add(new Point(x, y));
            } else if (chessEngine.getTarget() != null && chessEngine.getTarget().x == newX &&
                    chessEngine.getTarget().y == newY) {
                targetPieces.add(new Point(x, y));
            }
        }

        return !(isMyPiece || opponent == destinationTilePlayer);
    }

    protected void updateLists(List<Point> targetPieces, Set<Point> checkPieces) {
        possibleMoves = new ArrayList<>();
        this.targetPieces = targetPieces;
        this.checkPieces = checkPieces;
    }

}
