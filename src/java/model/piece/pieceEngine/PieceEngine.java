package model.piece.pieceEngine;

import model.board.Board;
import model.gameEngine.ChessEngine;

import java.awt.*;
import java.util.List;
import java.util.ResourceBundle;

public abstract class PieceEngine {
    protected Board myBoard = Board.getInstance(); //TODO refactor
    protected ChessEngine chessEngine = ChessEngine.getInstance();
    protected List<Point> possibleMoves;



    protected java.util.List<Point> getSimpleMoves(int x, int y, int playerNumber, ResourceBundle pieceMoves) {

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

}
