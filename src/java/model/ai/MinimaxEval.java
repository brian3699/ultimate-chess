package model.ai;


import model.board.Board;
import model.gameEngine.ChessEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Class MinimaxEval checks all possible moves and evaluates the moves using Minimax algorithm
 * In Singleton pattern
 */
public class MinimaxEval {
    private static final int DEFAULT_DEPTH = 3;
    private final ChessBoardScore chessBoardScore = ChessBoardScore.getInstance();
    private final ChessEngine chessEngine = ChessEngine.getInstance();
    private static MinimaxEval instance = new MinimaxEval();
    private ResourceBundle pieceResource;


    /**
     * Getter of class instance
     * @return MinimaxEval instance
     */
    public static MinimaxEval getInstance(){
        return instance;
    }

    private MinimaxEval(){}

    public int evaluate(Board board, int playerNumber){
        return evaluate(board, playerNumber,  DEFAULT_DEPTH);
    }

    public int evaluate(Board board, int playerNumber, int depth){
        if(depth == 0) return playerCurrentScore(board, playerNumber);
        int max = -1000000;
        List<int[][]> moveBoard = new ArrayList<>();
        Map<Point, List<Point>> moves =  chessEngine.getPlayerAllPossibleMoves(playerNumber);

        for(Point piece: moves.keySet()){
            for(Point move: moves.get(piece)){
                board.movePiece(piece.x, piece.y, move.x, move.y);
                int score = evaluate(board, playerNumber, depth-2);
                if(score > max) max = score;
                board.revert(piece.x, piece.y, move.x, move.y);
            }
        }

        return max;
    }


    public int playerCurrentScore(Board board, int player){
        int playerScore = 0;
        int opponent = (player + 1) % 2;

        // Check all tiles in the board
        for(int i = 0; i < board.getWidth(); i++){
            for(int j = 0; j < board.getHeight(); j++){
                int pieceOwner = board.getPlayerNumber(i,j);
                int point = board.getPiecePoint(i,j);
                // add point if the owner of the piece is the player
                if(pieceOwner == player){
                    playerScore += point;
                }else if(pieceOwner == opponent){
                    // subtract points if the piece is owned by the opponent
                    playerScore -= point;
                }
            }
        }

        return playerScore;
    }
}
