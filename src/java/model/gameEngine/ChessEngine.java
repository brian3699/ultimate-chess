package model.gameEngine;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import model.board.Board;
import model.piece.Piece;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;




public class ChessEngine {
    private static final String DEFAULT_BOARD_DATA = "resources/board/Default_Chess_Board.csv";
    private static final String DEFAULT_TEAM_DATA = "resources/board/Default_Chess_Board_Team.csv";
    private static final ResourceBundle CHESS_PIECE_DATA =
            ResourceBundle.getBundle("model/pieceInfo/ChessPiecePaths");

    private Board<Piece> myBoard;
    private int width;
    private int height;
    private CSVReader boardReader;
    private CSVReader teamReader;
    private Point currentPiece;


    /**
     * sets the board using default data
     */
    public ChessEngine() throws IOException, CsvException {
        initializeBoard(DEFAULT_BOARD_DATA, DEFAULT_TEAM_DATA);
        setPiece();
    }

    /**
     * sets the board from user input
     * @param boardFilePath file path to a csv file containing the state of each cell
     * @param teamFilePath file path to a csv file containing the team number of each cell
     */
    public ChessEngine(String boardFilePath, String teamFilePath) throws IOException, CsvException {
        initializeBoard(boardFilePath, teamFilePath);
        setPiece();
    }

    // initialize class Board
    private void initializeBoard(String boardFilePath, String teamFilePath) throws IOException, CsvException {
        // create csv file reader
        boardReader = new CSVReader(new FileReader(boardFilePath));
        teamReader = new CSVReader(new FileReader(teamFilePath));
        // get width and height
        width = boardReader.peek().length;
        height = (int) boardReader.readAll().size();
        // reset boardReader
        boardReader = new CSVReader(new FileReader(boardFilePath));
        // initialize Board
        myBoard = new Board<>(width, height);
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
                    String pieceInfoPath = CHESS_PIECE_DATA.getString(pieceLine[j]);
                    ResourceBundle pieceDataResource = ResourceBundle.getBundle(pieceInfoPath);
                    // team number of the piece
                    int teamNumber = Integer.parseInt(teamLine[j]);
                    myBoard.setCell(teamNumber, pieceDataResource, j, i);
                }catch (MissingResourceException e){
                    break;
                }
            }
        }
    }

    public ArrayList<Point> getValidMoves(int x, int y) throws InvocationTargetException, IllegalAccessException {
        currentPiece = new Point(x,y);
        String methodName = "get" + myBoard.getPieceType(x,y) + "Moves";
        return (ArrayList<Point>) handleMethod(methodName).invoke(ChessEngine.this);
    }

    private ArrayList<Point> getPawnMoves(){
        int x = currentPiece.x;
        int y = currentPiece.y;
        ArrayList<Point> moves = getPossibleMoves(x, y);
        int player = myBoard.getPlayerNumber(x,y);
        for(Point move : moves){
            int team = myBoard.getPlayerNumber(move.x,move.y);
            if(team == player) moves.remove(move);
        }
        if(player == 1 && y == 1) moves.add(new Point(x, y+2));
        if(player == 2 && y == height - 1) moves.add(new Point(x, y - 2));
        return moves;
    }

    private ArrayList<Point> getKnightMoves(){
        int x = currentPiece.x;
        int y = currentPiece.y;
        ArrayList<Point> moves = getPossibleMoves(x, y);
        int player = myBoard.getPlayerNumber(x,y);
        for(Point move : moves){
            int team = myBoard.getPlayerNumber(move.x,move.y);
            if(team == player) moves.remove(move);
        }
        return moves;
    }

    private ArrayList<Point> getBishopMoves(){
        int x = currentPiece.x;
        int y = currentPiece.y;
        return getPossibleMoves(x, y);
    }

    private ArrayList<Point> getRookMoves() {
        int x = currentPiece.x;
        int y = currentPiece.y;
        return getPossibleMoves(x, y);
    }

    private ArrayList<Point> getKingMoves() {
        int x = currentPiece.x;
        int y = currentPiece.y;
        return getPossibleMoves(x, y);
    }

    private ArrayList<Point> getQueenMoves() {
        int x = currentPiece.x;
        int y = currentPiece.y;
        return getPossibleMoves(x, y);
    }


    private ArrayList<Point> getPossibleMoves(int x, int y) {
        int team = 1;
        if(myBoard.getPlayerNumber(x,y) == 2) team = -1;
        ArrayList<Point> possibleMoves = new ArrayList<>();
        ResourceBundle pieceMoves = ResourceBundle.getBundle(CHESS_PIECE_DATA.getString(myBoard.getPieceType(x,y)));
        Enumeration<String> keys = pieceMoves.getKeys();
        // skip the first two keys.
        keys.nextElement();
        // enumerate all possible moves of a piece
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String[] move = pieceMoves.getString(key).split(",");
            int newX = x + Integer.parseInt(move[0])*team;
            int newY = y + Integer.parseInt(move[1])*team;
            possibleMoves.add(new Point(newX, newY));
        }
            possibleMoves.removeIf(p -> p.getX() > width - 1 || p.getX() < 0 || p.getY() > height - 1 || p.getY() < 0);
        return possibleMoves;
    }


    private Method handleMethod(String name) {
        try {
            Class<?> thisClass = Class.forName("model.gameEngine.ChessEngine");
            Method m = thisClass.getDeclaredMethod(name);
            return m;
        } catch (NoSuchMethodException e) {
            String error = String.format("The method: %s could not be generated. Double check method you are trying to call's name", name);
            System.out.println(error);
            return null;
        }catch(ClassNotFoundException e){
            String error = String.format("The class: %s could not be generated. Double check class you are trying to call's name", name);
            System.out.println(error);
            return null;
        }
    }

    public Board<Piece> getBoard(){
        return myBoard;
    }

}
