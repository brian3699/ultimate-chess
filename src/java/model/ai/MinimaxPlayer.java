package model.ai;


import model.board.Board;
import model.gameEngine.ChessEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class MinimaxEval checks all possible moves and evaluates the moves using Minimax algorithm
 * In Singleton pattern
 */
public class MinimaxPlayer {
    private static final int DEFAULT_DEPTH = 2;
    private final ChessBoardScore chessBoardScore = ChessBoardScore.getInstance();
    private final ChessEngine chessEngine = ChessEngine.getInstance();
    private static MinimaxPlayer instance = new MinimaxPlayer();
    private List<Point> bestMove;

    /**
     * Getter of class instance
     * @return MinimaxEval instance
     */
    public static MinimaxPlayer getInstance(){
        return instance;
    }

    private MinimaxPlayer(){
        bestMove = new ArrayList<>();
        bestMove.add(new Point(-1,-1));
        bestMove.add(new Point(-1,-1));
    }


    public List<Point> getBestMove(Board board){
        return getBestMove(board, DEFAULT_DEPTH);
    }

    public List<Point> getBestMove(Board board, int depth){
        bestMove.set(0,new Point(-1,-1));
        bestMove.set(1,new Point(-1,-1));
        evaluate(board, depth);
        return bestMove;
    }

    private int evaluate(Board board , int depth){
        if(depth == 0) return playerCurrentScore(board);
        if(chessEngine.detectCheck() && chessEngine.detectCheckMate()) return -1000000;

        int max = -1000000;

        List<int[][]> moveBoard = new ArrayList<>();
        Map<Point, List<Point>> moves =  chessEngine.getPlayerAllPossibleMoves();

        for(Point piece: moves.keySet()){
            for(Point move: moves.get(piece)){
                board.movePiece(piece.x, piece.y, move.x, move.y);
                int score = evaluate(board, depth-2);
                if(chessEngine.detectCheck()) score-=1000;
                if(score > max) {
                    max = score;
                    bestMove.get(0).x = piece.x;;
                    bestMove.get(0).y = piece.y;;
                    bestMove.get(1).x = move.x;;
                    bestMove.get(1).y = move.y;;
                }
                board.revert(piece.x, piece.y, move.x, move.y);
            }
        }

        return max;
    }


    public int playerCurrentScore(Board board){
        int playerScore = 0;
        int opponent = (chessEngine.getCurrentPlayer() + 1) % 2;

        // Check all tiles in the board
        for(int i = 0; i < board.getWidth(); i++){
            for(int j = 0; j < board.getHeight(); j++){
                int pieceOwner = board.getPlayerNumber(i,j);
                int point = board.getPiecePoint(i,j);
                // add point if the owner of the piece is the player
                if(pieceOwner == chessEngine.getCurrentPlayer()){
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
