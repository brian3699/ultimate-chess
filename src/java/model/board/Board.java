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
    private T historyOrigin;
    private T historyNew;
    private List<List<T>> capturedPiece;
    private List<List<T>> playerPieces;

    private int[] scoreTable;
    private int width;
    private int height;


    /**
     * default constructor. Creates 8X8 board for 2 players
     */
    public Board(){
        this(8,8);
    }

    /**
     * Creates a board for 2 players
     * @param width width of the board
     * @param height height of the board
     */
    public Board(int width, int height){
        scoreTable = new int[3];
        this.width = width;
        this.height = height;
        setPieceList(3);
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
        setPieceList(numPlayers);
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
    private void setPieceList(int numPlayers){
        capturedPiece = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++){
            capturedPiece.add(new ArrayList<>());
            capturedPiece.add(new ArrayList<>());
        }
    }

    // capture a piece
    public void capture(int x1, int y1, int x2, int y2){
        historyOrigin = myBoard.get(y1).get(x1);
        historyNew = myBoard.get(y2).get(x2);

        // update scoreTable and capturedPiece
        //playerPieces.get(captured.getPlayerNumber()).remove(captured);
        capturedPiece.get(historyNew.getPlayerNumber()).add(historyNew);

        // update myBoard
        movePiece(x1, y1, x2, y2);
    }

    // move piece to a new cell
    public void movePiece(int x1, int y1, int x2, int y2){
        historyOrigin = myBoard.get(y1).get(x1);
        historyNew = myBoard.get(y2).get(x2);

        myBoard.get(y2).set(x2, historyOrigin);
        myBoard.get(y1).set(x1, null);
    }

    public void revert(int x1, int y1, int x2, int y2){
        myBoard.get(y1).set(x1, historyOrigin);
        myBoard.get(y2).set(x2, historyNew);
    }


    @Override
    public void setCell(int playerNumber, ResourceBundle pieceInfo, int x, int y) {
        // TODO : need to finish this part after completing Piece classes
        T newPiece = (T) new Piece(pieceInfo.getString("name"),
                Integer.parseInt(pieceInfo.getString("point")), playerNumber);
        capturedPiece.get(playerNumber).add(newPiece);
        myBoard.get(y).set(x, newPiece);
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
    public int getPiecePoint(int x, int y){
        try{
            return myBoard.get(y).get(x).getPieceScore();
        }catch (NullPointerException | IndexOutOfBoundsException e){
            return 0;
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

    public int getWidth(){return width;};

    public int getHeight(){return height;};

}
