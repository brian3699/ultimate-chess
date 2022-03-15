package model.board;


import model.piece.Piece;
import model.piece.PieceInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class Board <T extends PieceInterface> implements BoardInterface{
    // Instances of pieces will be stored in this List
    private List<List<T>> myBoard;
    private List<List<T>> capturedPiece;
    private int[] scoreTable;
    private int width;
    private int height;


    /**
     * default constructor. Creates 8X8 board for 2 players
     */
    public Board(){
        scoreTable = new int[2];
        setCapturedPieceList(2);
        setDefaultBoard(8,8);
    }

    /**
     * Creates a board for 2 players
     * @param width width of the board
     * @param height height of the board
     */
    public Board(int width, int height){
        scoreTable = new int[2];
        setCapturedPieceList(2);
        setDefaultBoard(width, height);
    }

    /**
     * Creates a board for n players
     * @param numPlayers number of players
     * @param width width of the board
     * @param height height of the board
     */
    public Board(int numPlayers, int width, int height){
        scoreTable = new int[numPlayers];
        setCapturedPieceList(numPlayers);
        setDefaultBoard(width, height);
    }

    // Initialize board
    private void setDefaultBoard(int width, int height){
        this.width = width;
        this.height = height;
        myBoard = new ArrayList<>();
        for(int i = 0; i < height; i++){
            // create a row
            List<T> row = (List<T>) Arrays.asList(new Piece[width]);
            // add to myBoard
            myBoard.add(row);
        }
    }

    // initializes capturedPiece
    private void setCapturedPieceList(int numPlayers){
        capturedPiece = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++){
            capturedPiece.add(new ArrayList<>());
        }
    }

    // capture a piece
    private void capture(T movingPiece, T capPiece){
        // update myBoard
        movePiece(movingPiece, capPiece.getX(), capPiece.getY());

        // update scoreTable and capturedPiece
        capturedPiece.get(capPiece.getPlayerNumber()).add(capPiece);
        scoreTable[movingPiece.getPlayerNumber()] += 1;
    }

    // move piece to a new cell
    private void movePiece(T movingPiece, int x, int y){
        myBoard.get(y).set(x, movingPiece);
        myBoard.get(movingPiece.getY()).set(movingPiece.getX(), null);
    }


    @Override
    public boolean move(int x1, int y1, int x2, int y2) {
        try {
            // when it is not a valid move return false
            if (!checkValidMove(x1, y1, x2, y2)) return false;

            T currentCell = myBoard.get(y1).get(x1);
            T destinationCell = myBoard.get(y2).get(x2);

            if (destinationCell != null) {
                // if there is a piece in the destination cell, capture it
                capture(currentCell, destinationCell);
            } else {
                // if the destination cell is empty, move the piece to this cell
                movePiece(currentCell, x2, y2);
            }

            return true;
        }catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    // check whether it is a valid move
    private boolean checkValidMove(int x1, int y1, int x2, int y2){
        /*
        // if the current cell is empty return false
        if(myBoard.get(y1).get(x1) != null) return false;
        for(Point coordinate : getPossibleMoves(x1, y1)){
            // if the target cell is in the possible moves of the current cell, return true
            if (coordinate.x == x2 && coordinate.y == y2 ) return true;
        }
        // return false otherwise
        return false;

         */
        return true;
    }

    @Override
    public void setCell(int playerNumber, ResourceBundle pieceInfo, int x, int y) {
        // TODO : need to finish this part after completing Piece classes
        myBoard.get(y).set(x, (T) new Piece(pieceInfo.getString("name"),
                Integer.parseInt(pieceInfo.getString("point")), playerNumber, x, y));
    }

    @Override
    public String getPieceType(int x, int y){
        try{
            return myBoard.get(y).get(x).getPieceType();
        }catch (NullPointerException | IndexOutOfBoundsException e){
            return "-";
        }
    }


    @Override
    public int getPlayerNumber(int x, int y){
        try{
            return myBoard.get(y).get(x).getPlayerNumber();
        }catch (NullPointerException | IndexOutOfBoundsException e){
            return 0;
        }
    }
}
