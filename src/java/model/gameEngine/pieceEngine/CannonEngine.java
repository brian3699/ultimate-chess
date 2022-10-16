package model.gameEngine.pieceEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class that extends abstract class PieceEngine. Calculates the possible moves of a cannon
 * In Singleton pattern
 *
 * @author Young Jun
 */
public class CannonEngine extends PieceEngine{
    private static CannonEngine instance = new CannonEngine();

    private CannonEngine(){};

    /**
     * Getter of instance
     * @return CannonEngine
     */
    public static CannonEngine getInstance(){
        return instance;
    }

    @Override
    public List<Point> getMoves(int x, int y, int currentPlayer, List<Point> targetPieces, Set<Point> checkPieces) {
        updateLists(targetPieces, checkPieces);
        List<Point> validMoves = new ArrayList<>();
        addMoves(validMoves, currentPlayer, x,y,1,0);
        addMoves(validMoves, currentPlayer, x,y,-1,0);
        addMoves(validMoves, currentPlayer, x,y,0,1);
        addMoves(validMoves, currentPlayer, x,y,0,-1);

        possibleMoves = validMoves;
        return possibleMoves;

    }

    // Add moves to list of points. This method is specific to Cannon
    private void addMoves(List<Point> moves,  int currentPlayer, int x, int y, int xMove, int yMove){
        boolean detectOtherPiece = false;

        // Check the entire row or column
        for(int i = 1; i < 8; i++){
            // break if the coordinates go beyond the board
            if((x+ i*xMove > 7) ||(x+ i*xMove < 0) || (y+ i*yMove > 7) ||(y+ i*yMove < 0) ) break;
            if(detectOtherPiece){
                if(myBoard.getPlayerNumber(x + i*xMove, y+i*yMove) == currentPlayer) break;
                moves.add(new Point(x + i*xMove, y+i*yMove));
                if(myBoard.getPlayerNumber(x + i*xMove, y+i*yMove) != 0) break;
            }
            if(myBoard.getPlayerNumber(x + i*xMove, y+i*yMove) != 0) detectOtherPiece = true;
        }

    }
}
