package model.board;


import lombok.Getter;
import model.piece.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Class that implements a BoardInterface. Creates backend model of a board game.
 * Singleton Pattern
 *
 * @author Young Jun
 */
public class Board implements BoardInterface {

    private static final String NAME = "name";
    private static final String POINT = "point";
    private static final String EMPTY_CELL = "-";
    private static final int DEFAULT_WIDTH = 8;
    private static final int DEFAULT_HEIGHT = 8;

    @Getter private int width;
    @Getter private int height;


    // Instances of pieces will be stored in this List
    private List<List<Piece>> myBoard;
    private Piece historyOrigin;
    private Piece historyNew;
    private List<List<Piece>> capturedPiece;
    private List<List<Piece>> playerPieces;

    // Instance of Board
    private static Board instance = new Board(DEFAULT_WIDTH,DEFAULT_HEIGHT);

    public static Board getInstance(){
        return instance;
    }

    private Board(int width, int height) {
        this.width = width;
        this.height = height;
        setPieceList(3);
        setDefaultBoard(width, height);

    }

    @Override
    public void reset(){
        setPieceList(3);
        setDefaultBoard(width, height);
    }

    // Initialize board
    private void setDefaultBoard(int width, int height) {
        this.width = width;
        this.height = height;
        myBoard = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            // create a row
            List<Piece> row = (List<Piece>) Arrays.asList(new Piece[width]);
            // add to myBoard
            myBoard.add(row);
        }
    }

    // initializes capturedPiece
    private void setPieceList(int numPlayers) {
        capturedPiece = new ArrayList<>();
        playerPieces = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            capturedPiece.add(new ArrayList<>());
            playerPieces.add(new ArrayList<>());

        }
    }

    @Override
    public void capture(int x1, int y1, int x2, int y2) {
        historyOrigin = myBoard.get(y1).get(x1);
        historyNew = myBoard.get(y2).get(x2);

        //playerPieces.get(captured.getPlayerNumber()).remove(captured);
        capturedPiece.get(historyNew.getPlayerNumber()%2).add(historyNew);
        // update myBoard
        movePiece(x1, y1, x2, y2);
    }

    @Override
    public void movePiece(int x1, int y1, int x2, int y2) {
        historyOrigin = myBoard.get(y1).get(x1);
        historyNew = myBoard.get(y2).get(x2);

        // move piece to a new tile
        myBoard.get(y2).set(x2, historyOrigin);
        // remove piece from original position
        myBoard.get(y1).set(x1, null);
    }

    @Override
    public void revert(int x1, int y1, int x2, int y2) {
        myBoard.get(y1).set(x1, historyOrigin);
        myBoard.get(y2).set(x2, historyNew);
    }


    @Override
    public void setCell(int playerNumber, ResourceBundle pieceInfo, int x, int y) {
        Piece newPiece = new Piece(pieceInfo.getString(NAME),
                Integer.parseInt(pieceInfo.getString(POINT)), playerNumber);
        playerPieces.get(playerNumber).add(newPiece);
        myBoard.get(y).set(x, newPiece);
    }


    @Override
    public String getPieceType(int x, int y) {
        try {
            return myBoard.get(y).get(x).getPieceType();
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return EMPTY_CELL;
        }
    }

    @Override
    public void pawnPromotion(int x, int y, String pieceType, ResourceBundle pieceInfo){
        myBoard.get(y).get(x).setPieceType(pieceType);
        myBoard.get(y).get(x).setPieceScore(Integer.parseInt(pieceInfo.getString(POINT)));
    }

    @Override
    public String[] getCapturedPieceList(int playerNumber){
        int playerListNumber = playerNumber % 2;
        int listSize = capturedPiece.get(playerListNumber).size();
        String[] ret = new String[listSize];

        // put all elements in capturedPiece to an array
        for(int i = 0; i < listSize; i++){
            ret[i] = capturedPiece.get(playerListNumber).get(i).getPieceType();
        }
        return ret;
    }


    @Override
    public int getPiecePoint(int x, int y) {
        try {
            return myBoard.get(y).get(x).getPieceScore();
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

    @Override
    public int getPlayerNumber(int x, int y) {
        try {
            return myBoard.get(y).get(x).getPlayerNumber();
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return 0;
        }
    }

}
