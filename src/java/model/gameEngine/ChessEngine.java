package model.gameEngine;

import model.util.ReflectionHandler;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.*;

/**
 * Class that extends abstract class GameEngine. GameEngine of a chess game.
 *
 * @author Young Jun
 */
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

    private Point target;
    private List<Point> targetPieces;
    private Set<Point> checkPieces;
    private List<Point> possibleMoves;
    private Point player1King;
    private Point player2King;

    private static ChessEngine instance = new ChessEngine();


    public static ChessEngine getInstance(){
        return instance;
    }

    /**
     * sets the board using default file
     */
    private ChessEngine() {
        super(DEFAULT_BOARD_DATA_PATH, DEFAULT_TEAM_DATA_PATH, DEFAULT_CHESS_PIECE_DATA);
        player1King = new Point(4, 7);
        player2King = new Point(4, 0);
        targetPieces = new ArrayList<>();
        checkPieces = new HashSet<>();
        reflectionHandler = new ReflectionHandler();
    }

    /**
     * sets the board with user input
     *
     * @param boardFilePath file path to a csv file containing the state of each cell
     * @param teamFilePath  file path to a csv file containing the team number of each cell
     */
    private ChessEngine(String boardFilePath, String teamFilePath, String pieceInfoPath) {
        super(boardFilePath, teamFilePath, pieceInfoPath);
        reflectionHandler = new ReflectionHandler();
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
        try {
            possibleMoves = new ArrayList<>();
            currentPieceLocation = new Point(x, y);
            // method name to invoke
            String methodName = GET + getPieceType(x, y) + MOVES;
            // invokes different method for each chess piece type
            return (ArrayList<Point>) reflectionHandler.handleMethod(methodName, CLASS_PATH).invoke(ChessEngine.this);
        } catch (InvocationTargetException | IllegalAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void movePiece(int xOrigin, int yOrigin, int xNew, int yNew) {
        if (getPieceType(xOrigin, yOrigin).equals("King")) {
            setPlayerKing(getPiecePlayerNumber(xOrigin, yOrigin), new Point(xNew, yNew) );
        }
        super.movePiece(xOrigin, yOrigin, xNew, yNew);
        System.out.println(""+xNew+yNew);
    }


    public void pawnPromotion(int x, int y, String promotePiece) {

        myBoard.pawnPromotion(x,y,promotePiece);
    }

    public String[] getCapturedPiece(int player){
        return myBoard.getCapturedPieceList(player);
    }

    @Override
    public void capturePiece(int xOrigin, int yOrigin, int xCaptured, int yCaptured) {
        if (getPieceType(xOrigin, yOrigin).equals("King")) {
            setPlayerKing(getPiecePlayerNumber(xOrigin, yOrigin), new Point(xCaptured, yCaptured) );
        }
        super.capturePiece(xOrigin, yOrigin, xCaptured, yCaptured);

    }

    // return a list of points a pawn can move to
    private List<Point> getPawnMoves() {
        int x = currentPieceLocation.x;
        int y = currentPieceLocation.y;
        int player = getPiecePlayerNumber(x, y);

        List<Point> validMoves = new ArrayList<>();
        List<Point> possibleMoves = getSimpleMoves(x, y);

        // pawns can move two tiles from the initial tile
        if (player == 1 && y == height - 2 && getPiecePlayerNumber(x, y - 1) == 0)
            possibleMoves.add(new Point(x, y - 2));
        if (player == 2 && y == 1 && getPiecePlayerNumber(x, y + 1) == 0) possibleMoves.add(new Point(x, y + 2));

        for (Point move : possibleMoves) {
            int xMove = move.x - x;
            int yMove = move.y - y;
            // if the tile is empty
            if (getPiecePlayerNumber(move.x, move.y) == 0) {
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
        this.possibleMoves = validMoves;
        return this.possibleMoves;
    }

    // return a list of points a knight can move to
    private List<Point> getKnightMoves() {
        return getSimpleMoves(currentPieceLocation.x, currentPieceLocation.y);
    }

    // return a list of points a bishop can move to
    private List<Point> getBishopMoves() {
        return getComplexMoves(currentPieceLocation.x, currentPieceLocation.y);
    }

    // return a list of points a rook can move to
    private List<Point> getRookMoves() {
        return getComplexMoves(currentPieceLocation.x, currentPieceLocation.y);
    }

    // return a list of points a king can move to
    private List<Point> getKingMoves() {
        // only when the current player clicks on a king
        if (getCurrentPlayer() == getPiecePlayerNumber(currentPieceLocation.x, currentPieceLocation.y)) {
            getSimpleMoves(currentPieceLocation.x, currentPieceLocation.y);
            // remove points that will put King in a check
            possibleMoves = removeCheckMoves(possibleMoves, getCurrentPlayer());
            return possibleMoves;
        } else {
            return getSimpleMoves(currentPieceLocation.x, currentPieceLocation.y);
        }
    }

    // return a list of points a queen can move to
    private List<Point> getQueenMoves() {
        return getComplexMoves(currentPieceLocation.x, currentPieceLocation.y);
    }

    // calculate and return possible moves of King, Knight, and Pawn
    private List<Point> getSimpleMoves(int x, int y) {
        // Resource bundle containing all possible moves of a piece
        ResourceBundle pieceMoves = ResourceBundle.getBundle(pieceResource.getString(getPieceType(x, y)));

        // enumerate all possible moves of a piece
        for (String key : pieceMoves.keySet()) {
            String teamNumber = "" + key.charAt(0);
            if (!teamNumber.equals("" + getPiecePlayerNumber(x, y))) continue;
            // add only valid moves to possibleMoves
            addValidMoves(x, y, pieceMoves, key, 1);
        }
        return possibleMoves;
    }

    // calculate and return possible moves of Queen, Bishop, and Rook
    private List<Point> getComplexMoves(int x, int y) {
        // Resource bundle containing all possible moves of a piece
        ResourceBundle pieceMoves = ResourceBundle.getBundle(pieceResource.getString(getPieceType(x, y)));

        // enumerate all possible moves of a piece
        for (String key : pieceMoves.keySet()) {
            String teamNumber = "" + key.charAt(0);
            if (!teamNumber.equals("" + getPiecePlayerNumber(x, y))) continue;
            // Queen, Bishop, and Rook can move multiple tiles in one move
            for (int i = 1; i < Math.max(width + 1, height + 1); i++) {
                if (!addValidMoves(x, y, pieceMoves, key, i)) break;
            }
        }
        return possibleMoves;
    }

    // add only valid moves to possibleMoves
    private boolean addValidMoves(int x, int y, ResourceBundle pieceMoves, String key, int multiplier) {
        String[] move = pieceMoves.getString(key).split(",");
        int newX = Integer.parseInt(move[0]) * multiplier + currentPieceLocation.x;
        int newY = Integer.parseInt(move[1]) * multiplier + currentPieceLocation.y;

        int player = getPiecePlayerNumber(x, y);
        int opponent = player % 2 + 1;
        int destinationTilePlayer = getPiecePlayerNumber(newX, newY);

        // a piece can't move to a tile occupied by the current player's piece
        boolean isMyPiece = destinationTilePlayer == player;
        // a piece can't move beyond Board
        boolean isMoveInBoard = newX >= 0 && newX < width && newY >= 0 && newY < height;

        // add to possibleMoves if a move satisfies both conditions
        if (!isMyPiece && isMoveInBoard) {
            possibleMoves.add(new Point(newX, newY));
            if ((newX == player1King.x && newY == player1King.y) || (newX == player2King.x && newY == player2King.y)) {
                checkPieces.add(new Point(x, y));
            } else if (target != null && target.x == newX && target.y == newY) {
                targetPieces.add(new Point(x, y));
            }
        }

        return !(isMyPiece || opponent == destinationTilePlayer);
    }


    // remove Points from the possibleMoves that will place King in check
    private List<Point> removeCheckMoves(List<Point> moves, int playerNumber) {
        Point bufferPiece = currentPieceLocation;
        int opponent = playerNumber % 2 + 1;
        // get all points the opponent can move a piece
        boolean[][] opponentMoves;

        List<Point> returnList = new ArrayList<>();

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
        Point king;

        if (getCurrentPlayer() == 1) king = player1King;
        else king = player2King;

        int opponent = getCurrentPlayer() % 2 + 1;
        boolean[][] opponentMoves = getAllMovableTile(opponent);

        return opponentMoves[king.y][king.x];
    }

    public boolean detectCheckMate() {
        boolean ret;
        List<int[]> myOptions = new ArrayList<>();

        Point king;
        if (getCurrentPlayer() == 1) king = new Point(player1King.x, player1King.y);
        else king = new Point(player2King.x, player2King.y);

        boolean[][] myMoves = getAllMovableTile(getCurrentPlayer());
        List<Point> kingMoves = getValidMoves(king.x, king.y);

        for (Point point : kingMoves) myOptions.add(new int[]{king.x, king.y, point.x, point.y});
        for (Point checkPiece : checkPieces) myOptions.addAll(getPointsBetween(myMoves, checkPiece, king));


        for (int[] move : myOptions) {
            targetPieces = new ArrayList<>();
            movePiece(move[0], move[1], move[2], move[3]);
            ret = detectCheck();


            if (getCurrentPlayer() == 1) player1King = king;
            else player2King = king;

            revert(move[0], move[1], move[2], move[3]);

            if (!ret) return false;
        }

        return true;
    }

    private List<int[]> getPointsBetween(boolean[][] myMoves, Point origin, Point king) {
        List<int[]> retList = new ArrayList<>();
        String pieceType = getPieceType(origin.x, origin.y);

        if (pieceType.equals("Queen") || pieceType.equals("Bishop") || pieceType.equals("Rook")) {
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
        System.out.println("retList :" + retList.size());

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
