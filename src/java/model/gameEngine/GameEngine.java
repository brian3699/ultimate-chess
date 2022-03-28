package model.gameEngine;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import model.board.Board;
import model.piece.Piece;

import java.awt.Point;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameEngine {
    protected final int width;
    protected final int height;

    private Board<Piece> myBoard;
    private int[][] myPlayerBoard;


    private CSVReader boardReader;
    private final CSVReader teamReader;

    private int currentPlayer;
    private int turnCount;
    private Point currentPiece;
    private Map<Integer, Integer> scoreBoard;
    protected ResourceBundle pieceResource;


    /**
     * sets the board from user input
     * @param boardFilePath file path to a csv file containing the state of each cell
     * @param teamFilePath file path to a csv file containing the team number of each cell
     */
    public GameEngine(String boardFilePath, String teamFilePath, String pieceFilePath) throws IOException, CsvException {
        // create csv file reader
        boardReader = new CSVReader(new FileReader(boardFilePath));
        teamReader = new CSVReader(new FileReader(teamFilePath));

        // get width and height
        width = boardReader.peek().length;
        height = (int) boardReader.readAll().size();

        pieceResource = ResourceBundle.getBundle(pieceFilePath);
        currentPlayer = 1;

        initializeScoreBoard();
        try{
            initializeBoard(boardFilePath, teamFilePath);
            setPiece();
        }catch (IOException | CsvException e){
            // TODO: Refactor
            System.out.println("Please Choose a Correct File");
        }

    }

    public String clickType(int x, int y){
        if (currentPlayer == myBoard.getPlayerNumber(x, y)) return "clickOnPiece";
        return null;
    }


    // initialize class Board
    private void initializeBoard(String boardFilePath, String teamFilePath) throws IOException, CsvException {
        // reset boardReader
        boardReader = new CSVReader(new FileReader(boardFilePath));
        // initialize Board
        myBoard = new Board<>(width, height);

        myPlayerBoard = new int[height][width];
    }

    private void initializeScoreBoard(){
        scoreBoard = new HashMap<>();
        scoreBoard.put(1,0);
        scoreBoard.put(2,0);
    }

    public String getPieceType(int x, int y){
        return myBoard.getPieceType(x,y);
    }
    public int getPieceTeam(int x, int y){
        return myBoard.getPlayerNumber(x,y);
    }

    // Set pieces in myBoard
    private void setPiece() throws CsvValidationException, IOException {

        for(int i=0; i<height; i++){
            // array containing the types of the pieces
            String[] pieceLine = boardReader.readNext();
            // array containing the team numbers of the pieces
            String[] teamLine = teamReader.readNext();
            for(int j = 0; j < width; j++){
                try {
                    // path to the ResourceBundle containing Piece information
                    String pieceInfoPath = pieceResource.getString(pieceLine[j]);
                    ResourceBundle pieceDataResource = ResourceBundle.getBundle(pieceInfoPath);
                    // team number of the piece
                    int teamNumber = Integer.parseInt(teamLine[j]);
                    myBoard.setCell(teamNumber, pieceDataResource, j, i);
                    myPlayerBoard[i][j] = teamNumber;
                }catch (MissingResourceException e){
                    break;
                }
            }
        }
    }

    public void movePiece(int xOrigin, int yOrigin, int xNew, int yNew){
        myPlayerBoard[yOrigin][xOrigin] = 0;
        myPlayerBoard[yNew][xNew] = currentPlayer;
        myBoard.movePiece(xOrigin, yOrigin, xNew, yNew);
        nextTurn();
    }

    public void capturePiece(int xOrigin, int yOrigin, int xNew, int yNew){
        int score = myBoard.getPiecePoint(xNew, yNew);
        myPlayerBoard[yOrigin][xOrigin] = 0;
        myPlayerBoard[yNew][xNew] = currentPlayer;
        scoreBoard.put(currentPlayer, scoreBoard.get(currentPlayer) + score);
        myBoard.capture(xOrigin, yOrigin, xNew, yNew);
        nextTurn();
    }

    public void nextTurn(){
        currentPlayer = currentPlayer % 2 + 1;
        turnCount += 1;
    }

    public int getUserScore(int playerNumber){
        return scoreBoard.get(playerNumber);
    }

    public int getCurrentPlayer(){return currentPlayer;}

    protected boolean checkPieceOwner(int x, int y) {
        return currentPlayer == myBoard.getPlayerNumber(x, y);
    }
}
