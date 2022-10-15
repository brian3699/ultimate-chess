package model.ai;


import model.board.Board;

public class ChessBoardScore {
    private static final ChessBoardScore instance = new ChessBoardScore();

    public static ChessBoardScore getInstance(){
        return instance;
    }

    private ChessBoardScore(){}

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
