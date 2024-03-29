package model.gameEngine;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.board.Board;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * abstract class containing basic methods of a board game engine.
 *
 * @author Young Jun
 */
public abstract class GameEngine {
    private static final String CLICK_ON_PIECE_METHOD_NAME = "clickOnPiece";

    protected int width;
    protected int height;
    protected int turnCount;
    protected final ResourceBundle pieceResource;
    protected Board myBoard;

    private int currentPlayer;
    private boolean isCorrectFile;
    private CSVReader boardReader;
    private CSVReader teamReader;
    private Map<Integer, Integer> scoreBoard;


    /**
     * constructor of game engine
     *
     * @param pieceFilePath file path to a csv file containing the path to ResourceBundle containing piece information
     */
    public GameEngine(String pieceFilePath) {
        // create ResourceBundle from pieceFilePath
        pieceResource = ResourceBundle.getBundle(pieceFilePath);
        initializeScoreBoard();
        currentPlayer = 1;
    }
    /**
     * return click type
     *
     * @param x x location of piece
     * @param y y location of piece
     * @return click type
     */
    public String clickType(int x, int y) {
        // if a player clicks his or her piece return CLICK_ON_PIECE_METHOD_NAME
        if (currentPlayer == myBoard.getPlayerNumber(x, y)) return CLICK_ON_PIECE_METHOD_NAME;
        return null;
    }

    /**
     * Return the type of the piece
     *
     * @param x x location of a piece
     * @param y y location of a piece
     * @return piece type
     */
    public String getPieceType(int x, int y) {
        return myBoard.getPieceType(x, y);
    }

    /**
     * Return the player number (owner) of the piece
     *
     * @param x x location of a piece
     * @param y y location of a piece
     * @return player number
     */
    public int getPiecePlayerNumber(int x, int y) {
        return myBoard.getPlayerNumber(x, y);
    }


    /**
     * Move piece from one tile to the other
     *
     * @param xOrigin x location of a piece
     * @param yOrigin y location of a piece
     * @param xNew    x location of the destination tile
     * @param yNew    x location of the destination tile
     */
    public void movePiece(int xOrigin, int yOrigin, int xNew, int yNew) {
        myBoard.movePiece(xOrigin, yOrigin, xNew, yNew);
    }

    /**
     * Check if the King is captured
     *
     * @param x column number
     * @param y row number
     * @return boolean of whether the game is over
     */
    public boolean detectGameOver(int x, int y) {
        if (myBoard.getPieceType(x, y).equals("King")) return true;
        return false;
    }

    /**
     * Capture piece
     *
     * @param xOrigin   x location of the moving piece
     * @param yOrigin   y location of the moving piece
     * @param xCaptured x location of the captured piece
     * @param yCaptured y location of the captured piece
     */
    public void capturePiece(int xOrigin, int yOrigin, int xCaptured, int yCaptured) {
        // update user's current score
        int score = myBoard.getPiecePoint(xCaptured, yCaptured);
        scoreBoard.put(currentPlayer, scoreBoard.get(currentPlayer) + score);
        // call board to capture the piece
        myBoard.capture(xOrigin, yOrigin, xCaptured, yCaptured);
    }

    /**
     * Change currentPlayer and increment turnCount by 1
     */
    public void nextTurn() {
        // 1 -> 2, 2 -> 1
        currentPlayer = currentPlayer % 2 + 1;
        turnCount += 1;
    }

    /**
     * revert a move
     *
     * @param x1 x coordinate of the original location
     * @param y1 y coordinate of the original location
     * @param x2 x coordinate of the current location
     * @param y2 y coordinate of the current location
     */
    public void revert(int x1, int y1, int x2, int y2) {
        myBoard.revert(x1, y1, x2, y2);
    }

    /**
     * Return player's current score
     *
     * @param playerNumber player number
     * @return player's score
     */
    public int getPlayerScore(int playerNumber) {
        return scoreBoard.get(playerNumber);
    }

    /**
     * Return player number of the current player
     *
     * @return player number
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Calculate and return the list of points a piece could move to
     *
     * @param x x location of a piece
     * @param y y location of a piece
     * @return list of points a piece could move to
     */
    public abstract List<Point> getValidMoves(int x, int y);


    /**
     * initialize the board with user input
     *
     * @param boardFilePath file path to a csv file containing the state of each cell
     * @param teamFilePath  file path to a csv file containing the team number of each cell
     */
    public void initializeBoard(String boardFilePath, String teamFilePath) {
        // Board is in singleton pattern. Reset the board before using
        try {
            // create csv file reader
            boardReader = new CSVReader(new FileReader(boardFilePath));
            teamReader = new CSVReader(new FileReader(teamFilePath));
            // reset boardReader
            boardReader = new CSVReader(new FileReader(boardFilePath));
            // initialize Board
            myBoard = Board.getInstance();
        } catch (IOException e) {
            myBoard = Board.getInstance();
            isCorrectFile = false;
        }
        myBoard.reset();
        // get width and height from board
        width = myBoard.getWidth();
        height = myBoard.getHeight();
        // add piece to myBoard
        setPiece();
    }

    // Set pieces in myBoard
    private void setPiece() {
        try {
            // loop over all rows in the csv file
            for (int i = 0; i < height; i++) {
                // array containing the piece type
                String[] pieceLine = boardReader.readNext();
                // array containing the player number of a piece
                String[] teamLine = teamReader.readNext();

                // loops over all elements in the row
                for (int j = 0; j < width; j++) {
                    // add piece to board data structure
                    addPieceToBoard(i, j, pieceLine[j], teamLine[j]);
                }
            }
        } catch (CsvValidationException | IOException e) {
            isCorrectFile = false;
        }
    }

    // add a piece to board
    protected void addPieceToBoard(int i, int j, String pieceType, String team) {
        try {
            // path to the ResourceBundle containing Piece information
            String pieceInfoPath = pieceResource.getString(pieceType);
            ResourceBundle pieceDataResource = ResourceBundle.getBundle(pieceInfoPath);
            // team number of the piece
            int teamNumber = Integer.parseInt(team);
            myBoard.setCell(teamNumber, pieceDataResource, j, i);
        } catch (MissingResourceException e) {
            return;
        }
    }


    private void initializeScoreBoard() {
        scoreBoard = new HashMap<>();
        scoreBoard.put(1, 0);
        scoreBoard.put(2, 0);
    }
}
