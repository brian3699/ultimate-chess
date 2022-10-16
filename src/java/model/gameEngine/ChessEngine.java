package model.gameEngine;

import lombok.Getter;
import model.gameEngine.pieceEngine.PieceEngine;
import model.gameEngine.pieceEngine.PieceEngineFactory;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Class that extends abstract class GameEngine. GameEngine of a chess game.
 * In singleton pattern
 *
 * @author Young Jun
 */
public class ChessEngine extends GameEngine {
    private static final String DEFAULT_CHESS_PIECE_DATA = "model/pieceInfo/ChessPiecePaths";
    private static final String ERROR_CLICK_METHOD_NAME = "errorClick";
    private static final String MOVE_PIECE_METHOD_NAME = "movePiece";
    private static final String CAPTURE_PIECE_METHOD_NAME = "capturePiece";
    private static final String KING = "King";
    private static final String QUEEN = "Queen";
    private static final String BISHOP = "Bishop";
    private static final String ROOK = "Rook";

        private Point currentPieceLocation;
    private List<Point> targetPieces;
    private Set<Point> checkPieces;
    private List<Point> possibleMoves;
    @Getter private Point target;
    @Getter private Point player1King;
    @Getter private Point player2King;

    private static final ChessEngine instance = new ChessEngine();
    private static final PieceEngineFactory pieceEngineFactory = PieceEngineFactory.getInstance();
    public static ChessEngine getInstance(){
        return instance;
    }

    /**
     * sets the board using default file
     */
    private ChessEngine() {
        super(DEFAULT_CHESS_PIECE_DATA);
        // save the position of the king
        player1King = new Point(4, 7);
        player2King = new Point(4, 0);
        targetPieces = new ArrayList<>();
        checkPieces = new HashSet<>();
    }


    @Override
    public String clickType(int x, int y) {
        if (super.clickType(x, y) != null) return super.clickType(x, y);
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
        possibleMoves = new ArrayList<>();
        // update the current location of the piece
        currentPieceLocation = new Point(x, y);
        // call method getMoves to return all valid moves
        return getMoves(x,y);
    }

    /**
     * return all moves that the current player can make
     * @return map of point and list of points that a piece can move to
     */
    public Map<Point, List<Point>> getPlayerAllPossibleMoves(){
        Map<Point, List<Point>> moves = new HashMap<>();
        // check all pieces in the board
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // if the piece belongs to the current player
                if (getPiecePlayerNumber(i, j) == getCurrentPlayer()) {
                    Point piece = new Point(i,j);
                    // add valid moves of a player's piece to moves
                    moves.put(piece, getValidMoves(i,j));
                }
            }
        }
        return moves;
    }

    @Override
    public void movePiece(int xOrigin, int yOrigin, int xNew, int yNew) {
        // update king's position if the player moves a king
        if (getPieceType(xOrigin, yOrigin).equals(KING)) {
            setPlayerKing(getPiecePlayerNumber(xOrigin, yOrigin), new Point(xNew, yNew) );
        }
        super.movePiece(xOrigin, yOrigin, xNew, yNew);
    }
    @Override
    public void capturePiece(int xOrigin, int yOrigin, int xCaptured, int yCaptured) {
        // update king's position if the player moves a king
        if (getPieceType(xOrigin, yOrigin).equals(KING)) {
            setPlayerKing(getPiecePlayerNumber(xOrigin, yOrigin), new Point(xCaptured, yCaptured) );
        }
        super.capturePiece(xOrigin, yOrigin, xCaptured, yCaptured);

    }



    /**
     * Method that handles pawn promotion
     * @param x column number of the promoted pawn
     * @param y row number of the promoted pawn
     * @param promotePiece type of the piece that the pawn will be replaced with
     */
    public void pawnPromotion(int x, int y, String promotePiece) {
        // get piece resource that will be used to set the new piece
        ResourceBundle pieceDataResource = ResourceBundle.getBundle(pieceResource.getString(promotePiece));

        myBoard.pawnPromotion(x,y,promotePiece, pieceDataResource);
    }

    /**
     * return all captured pieces
     * @param player player number
     * @return list of all captured pieces
     */
    public String[] getCapturedPiece(int player){
        return myBoard.getCapturedPieceList(player);
    }



    private List<Point> getMoves(int x, int y) {
        // get what piece it is
        String pieceType = getPieceType(x,y);
        // get pieceEngine from pieceEngineFactory
        PieceEngine pieceEngine = pieceEngineFactory.getPieceEngine(pieceType);
        // use pieceEngine to calculate all possible moves of a piece
        possibleMoves =  pieceEngine.getMoves(x,y,getCurrentPlayer(), targetPieces, checkPieces);
        // remove all moves that will put King into a check
        if(pieceType.equals(KING) && getCurrentPlayer() == getPiecePlayerNumber(x, y)) {
            possibleMoves = removeCheckMoves(possibleMoves, getCurrentPlayer());
        }
        return possibleMoves;

    }

    // remove Points from the possibleMoves that will place King in check
    private List<Point> removeCheckMoves(List<Point> moves, int playerNumber) {
        Point bufferPiece = currentPieceLocation;
        int opponent = playerNumber % 2 + 1;
        // get all points the opponent can move a piece
        boolean[][] opponentMoves;

        List<Point> returnList = new ArrayList<>();
        // Check all moves and remove any moves that will put King in check
        for (Point move : moves) {
            super.movePiece(bufferPiece.x, bufferPiece.y, move.x, move.y);
            opponentMoves = getAllMovableTile(opponent);
            // add a move to the returnList if it is not a move that will place king in check
            if (!opponentMoves[move.y][move.x]) {
                returnList.add(move);
            }
            revert(bufferPiece.x, bufferPiece.y, move.x, move.y);
        }
        currentPieceLocation = bufferPiece;
        return returnList;
    }

    // return 2d array containing info of all tiles a player can move a piece. true if a player can move a piece.
    private boolean[][] getAllMovableTile(int playerNumber) {
        ArrayList<Point> allPossibleMoves = new ArrayList<>();
        // check all piece on the board
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (getPiecePlayerNumber(j, i) == playerNumber) {
                    // add valid moves of a player's piece to allPossibleMoves
                    allPossibleMoves.addAll(getValidMoves(j, i));
                }
            }
        }
        boolean[][] playerBoard = new boolean[height][width];

        for (Point move : allPossibleMoves) {
            // set to true if a player can move a piece to this tile
            playerBoard[move.y][move.x] = true;
        }
        return playerBoard;
    }


    /**
     * check whether the current player's king is placed in a check or not
     *
     * @return whether the current player's king is placed in a check or not
     */
    public boolean detectCheck() {
        checkPieces = new HashSet<>();
        // get current player's king
        Point king = player2King;
        if (getCurrentPlayer() == 1) king = player1King;
        // 1 -> 2 , 2-> 1
        int opponent = getCurrentPlayer() % 2 + 1;
        // get all tiles an opponent can move to
        boolean[][] opponentMoves = getAllMovableTile(opponent);
        // return false if king is in a tile that the opponent can move to. Return true otherwise
        return opponentMoves[king.y][king.x];
    }

    /**
     * detect checkmate and return boolean
     * @return whether King is in checkmate
     */
    public boolean detectCheckMate() {
        boolean ret;
        List<int[]> myOptions = new ArrayList<>();

        Point king;
        // get current player's king
        if (getCurrentPlayer() == 1) king = new Point(player1King.x, player1King.y);
        else king = new Point(player2King.x, player2King.y);
        // get all points that my pieces can move to
        boolean[][] myMoves = getAllMovableTile(getCurrentPlayer());
        // get list of all tiles the king can move to
        List<Point> kingMoves = getValidMoves(king.x, king.y);

        for (Point point : kingMoves) myOptions.add(new int[]{king.x, king.y, point.x, point.y});
        for (Point checkPiece : checkPieces) myOptions.addAll(getPointsBetween(myMoves, checkPiece, king));

        // check all options a player can make
        for (int[] move : myOptions) {
            targetPieces = new ArrayList<>();
            // move piece to test the option
            movePiece(move[0], move[1], move[2], move[3]);
            // check if king is still in check
            ret = detectCheck();
            if (getCurrentPlayer() == 1) player1King = king;
            else player2King = king;
            revert(move[0], move[1], move[2], move[3]);
            // return false if there is an option to prevent checkmate
            if (!ret) return false;
        }
        return true;
    }

    // get all tiles that is between the king and an opponent piece that may capture a king
    // used to check if another piece can block the path and prevent checkmate
    private List<int[]> getPointsBetween(boolean[][] myMoves, Point origin, Point king) {
        List<int[]> retList = new ArrayList<>();
        String pieceType = getPieceType(origin.x, origin.y);

        if (pieceType.equals(QUEEN) || pieceType.equals(BISHOP) || pieceType.equals(ROOK)) {
            int xIncrement = (origin.x - king.x);
            int yIncrement = (origin.y - king.y);

            if (xIncrement != 0) xIncrement /= Math.abs(xIncrement);
            if (yIncrement != 0) yIncrement /= Math.abs(yIncrement);

            for (int i = 1; i <= Math.max(Math.abs(origin.x - king.x), Math.abs(origin.y - king.y)); i++) {
                int x = king.x + xIncrement * i;
                int y = king.y + yIncrement * i;

                if (myMoves[y][x]) {
                    targetPieces = new ArrayList<>();
                    target = new Point(x, y);
                    getAllMovableTile(getCurrentPlayer());
                    for (Point point : targetPieces) retList.add(new int[]{point.x, point.y, x, y});
                }
            }
        } else {
            targetPieces = new ArrayList<>();
            target = origin;

            getAllMovableTile(getCurrentPlayer());

            for (Point point : targetPieces) retList.add(new int[]{point.x, point.y, origin.x, origin.y});
        }

        return retList;
    }

    /**
     * getter method of Point currentPieceLocation
     *
     * @return currentPieceLocation
     */
    public Point getCurrentPieceLocation() {
        return currentPieceLocation;
    }

    private void setPlayerKing(int player, Point point){
        if (player == 1){
            player1King = point;
        } else {
            player2King = point;
        }
    }

}
