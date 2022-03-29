package model.gameEngine;

import model.util.ReflectionHandler;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChessEngine extends GameEngine {
    private static final String DEFAULT_BOARD_DATA_PATH = "resources/board/Default_Chess_Board.csv";
    private static final String DEFAULT_TEAM_DATA_PATH = "resources/board/Default_Chess_Board_Team.csv";
    private static final String DEFAULT_CHESS_PIECE_DATA = "model/pieceInfo/ChessPiecePaths";
    private static final String CLASS_PATH = "model.gameEngine.ChessEngine";

    private static final String ERROR_CLICK_METHOD_NAME = "errorClick";
    private static final String MOVE_PIECE_METHOD_NAME = "movePiece";
    private static final String CAPTURE_PIECE_METHOD_NAME = "capturePiece";
    private static final String GET = "get";
    private static final String MOVES = "Moves";

    private final ReflectionHandler reflectionHandler;

    private Point currentPieceLocation;
    private List<Point> possibleMoves;
    private boolean[][] player1PossibleMoves;
    private boolean[][] player2PossibleMoves;

    /**
     * sets the board using default file
     */
    public ChessEngine(){
        super(DEFAULT_BOARD_DATA_PATH, DEFAULT_TEAM_DATA_PATH, DEFAULT_CHESS_PIECE_DATA);
        reflectionHandler = new ReflectionHandler();
    }

    /**
     * sets the board with user input
     * @param boardFilePath file path to a csv file containing the state of each cell
     * @param teamFilePath file path to a csv file containing the team number of each cell
     */
    public ChessEngine(String boardFilePath, String teamFilePath, String pieceInfoPath){
        super(boardFilePath, teamFilePath, pieceInfoPath);
        reflectionHandler = new ReflectionHandler();
    }

    @Override
    public String clickType(int x, int y){
        if(super.clickType(x, y) != null) return super.clickType(x,y);
        // if a point is not in possible moves, it is not a valid click
        if (possibleMoves == null) return ERROR_CLICK_METHOD_NAME;
        for (Point move : possibleMoves) {
            // if a point is in possible moves and empty, move piece
            if (move.x == x && move.y == y && getPiecePlayerNumber(x, y) == 0) return MOVE_PIECE_METHOD_NAME;
            // if a point is in possible moves and occupied by an opponent, capture piece
            if (move.x == x && move.y == y && getPiecePlayerNumber(x, y) != 0) return CAPTURE_PIECE_METHOD_NAME;
        }
        // it is not a valid click if a user clicked on a tile that doesn't belong to any of the above categories
        return ERROR_CLICK_METHOD_NAME;
    }

    @Override
    public List<Point> getValidMoves(int x, int y) {
        try{
            possibleMoves = new ArrayList<>();
            currentPieceLocation = new Point(x,y);
            // method name to invoke
            String methodName = GET + getPieceType(x,y) + MOVES;
            // invokes different method for each chess piece type
            return (ArrayList<Point>) reflectionHandler.handleMethod(methodName,CLASS_PATH).invoke(ChessEngine.this);
        }catch(InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // return a list of points a pawn can move to
    private List<Point> getPawnMoves(){
        int x = currentPieceLocation.x;
        int y = currentPieceLocation.y;
        int player = getPiecePlayerNumber(x,y);

        List<Point> validMoves = new ArrayList<>();
        List<Point> possibleMoves = getSimpleMoves(x, y);

        // pawns can move two tiles from the initial tile
        if(player == 1 && y == height - 2 && getPiecePlayerNumber(x, y - 1) == 0) possibleMoves.add(new Point(x, y - 2));
        if(player == 2 && y == 1 && getPiecePlayerNumber(x, y + 1) == 0) possibleMoves.add(new Point(x, y + 2));

        for(Point move: possibleMoves){
            int xMove = move.x - x;
            int yMove = move.y - y;
            // if the tile is empty
            if(getPiecePlayerNumber(move.x, move.y) == 0){
                // pawn can only move diagonally when capturing an opponent's piece
                if((xMove == 1 && yMove == -1) | (xMove == -1 && yMove == -1) | (xMove == 1 && yMove == 1) |
                        (xMove == -1 && yMove == 1)) continue;
            }else{
                // pawn can't capture the opponent's piece with a vertical move
                if((xMove == 0 && yMove == 1) | (xMove == 0 && yMove == 2) | (xMove == 0 && yMove == -1) |
                        (xMove == 0 && yMove == -2)) continue;
            }
            // add move to returnList if this move doesn't belong to one of the above restrictions
            validMoves.add(move);
        }
        this.possibleMoves = validMoves;
        return this.possibleMoves;
    }

    // return a list of points a knight can move to
    private List<Point> getKnightMoves(){
        return getSimpleMoves(currentPieceLocation.x, currentPieceLocation.y);
    }

    // return a list of points a bishop can move to
    private List<Point> getBishopMoves(){
        return getComplexMoves(currentPieceLocation.x, currentPieceLocation.y);
    }

    // return a list of points a rook can move to
    private List<Point> getRookMoves() {
        return getComplexMoves(currentPieceLocation.x, currentPieceLocation.y);
    }

    // return a list of points a king can move to
    private List<Point> getKingMoves() {
        // only when the current player clicks on a king
        if(getCurrentPlayer() == getPiecePlayerNumber(currentPieceLocation.x, currentPieceLocation.y)) {
            getSimpleMoves(currentPieceLocation.x, currentPieceLocation.y);
            // remove points that will put King in a check
            possibleMoves =  removeCheckMoves(possibleMoves, getCurrentPlayer());
            return possibleMoves;
        }else {
            return getSimpleMoves(currentPieceLocation.x, currentPieceLocation.y);
        }
    }

    // return a list of points a queen can move to
    private List<Point> getQueenMoves() {
        return getComplexMoves(currentPieceLocation.x, currentPieceLocation.y);
    }

    // calculate and return possible moves of King, Knight, and Pawn
    private List<Point> getSimpleMoves(int x, int y){
        // Resource bundle containing all possible moves of a piece
        ResourceBundle pieceMoves = ResourceBundle.getBundle(pieceResource.getString(getPieceType(x,y)));

        // enumerate all possible moves of a piece
        for(String key : pieceMoves.keySet()){
            String teamNumber = ""+ key.charAt(0);
            if(!teamNumber.equals(""+getPiecePlayerNumber(x,y))) continue;
            // add only valid moves to possibleMoves
            addValidMoves(x, y, pieceMoves, key, 1);
        }
        return possibleMoves;
    }

    // calculate and return possible moves of Queen, Bishop, and Rook
    private List<Point> getComplexMoves(int x, int y){
        // Resource bundle containing all possible moves of a piece
        ResourceBundle pieceMoves = ResourceBundle.getBundle(pieceResource.getString(getPieceType(x,y)));

        // enumerate all possible moves of a piece
        for(String key : pieceMoves.keySet()){
            String teamNumber = ""+ key.charAt(0);
            if(!teamNumber.equals(""+getPiecePlayerNumber(x,y))) continue;
            // Queen, Bishop, and Rook can move multiple tiles in one move
            for(int i = 1; i < Math.max(width, height); i++){
                addValidMoves(x, y, pieceMoves, key, i);
            }
        }
        return possibleMoves;
    }

    // add only valid moves to possibleMoves
    private void addValidMoves(int x, int y, ResourceBundle pieceMoves, String key, int multiplier){
        String[] move = pieceMoves.getString(key).split(",");
        int newX = Integer.parseInt(move[0])*multiplier + currentPieceLocation.x;
        int newY = Integer.parseInt(move[1])*multiplier + currentPieceLocation.y;

        // a piece can't move to a tile occupied by the current player's piece
        boolean isNotMyPiece = getPiecePlayerNumber(newX, newY) != getPiecePlayerNumber(x,y);
        // a piece can't move beyond Board
        boolean isMoveInBoard = newX > 0 && newX < width - 1 && newY > 0 && newY < height - 1;

        // add to possibleMoves if a move satisfies both conditions
        if(isNotMyPiece && isMoveInBoard){
            possibleMoves.add(new Point(newX, newY));
        }
    }

    // return 2d array containing info of all tiles a player can move a piece. true if a player can move a piece.
    private boolean[][] getAllMovableTile(int playerNumber){
        ArrayList<Point> allPossibleMoves = new ArrayList<>();
        for(int i = 0; i < height; i ++){
            for(int j = 0; j < width; j++){
                if(getPiecePlayerNumber(j, i) == playerNumber){
                    // add valid moves of a player's piece to allPossibleMoves
                    allPossibleMoves.addAll(getValidMoves(j, i));
                }
            }
        }
        boolean[][] playerBoard = new boolean[height][width];
        if(playerNumber == 1) player1PossibleMoves = playerBoard;
        else if(playerNumber == 2) player2PossibleMoves = playerBoard;

        for(Point move : allPossibleMoves){
            // set to true if a player can move a piece to this tile
            playerBoard[move.y][move.x] = true;
        }
        return playerBoard;
    }

    // remove Points from the possibleMoves that will place King in check
    private List<Point> removeCheckMoves(List<Point> possibleMoves, int playerNumber){
            Point bufferPiece = currentPieceLocation;
            int opponent = playerNumber % 2 + 1;
            // get all points the opponent can move a piece
            boolean[][] opponentMoves = getAllMovableTile(opponent);

            List<Point> returnList = new ArrayList<>();

            for(Point move : possibleMoves){
                // add a move to the returnList if it is not a move that will place king in check
                if(!opponentMoves[move.y][move.x]) returnList.add(move);
            }
            currentPieceLocation = bufferPiece;
            return returnList;
    }


    /**
     * check whether the current player's king is placed in a check or not
     * @return whether the current player's king is placed in a check or not
     */
    public boolean detectCheck(){
        int opponent = getCurrentPlayer() % 2 + 1;
        boolean[][] opponentMoves = getAllMovableTile(opponent);
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(opponentMoves[j][i] && getPieceType(i,j).equals("King") && getPiecePlayerNumber(i,j) == getCurrentPlayer()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * getter method of Point currentPieceLocation
     * @return currentPieceLocation
     */
    public Point getCurrentPieceLocation(){
        return currentPieceLocation;
    }

}
