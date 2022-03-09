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
        if(player == 1 && y == 1) moves.add(new Point(x, y+2));
        if(player == 2 && y == height - 2) moves.add(new Point(x, y - 2));
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

    private ArrayList<Point> removePointsBeyond(ArrayList<Point> moves, Point point, int xIncrement, int yIncrement){
        for(Point move : moves){
            int xMove = move.x - point.x;
            int yMove = move.y - point.y;
            for(int i = 0; i < 10; i++){
                if(xMove == xIncrement*i && yMove == yIncrement*i){
                    moves.remove(move);
                    break;
                }
            }
        }
        return moves;
    }


    private ArrayList<Point> getPossibleMoves(int x, int y) {
        String team = Integer.toString(myBoard.getPlayerNumber(x,y));
        ArrayList<Point> possibleMoves = new ArrayList<>();
        ResourceBundle pieceMoves = ResourceBundle.getBundle(CHESS_PIECE_DATA.getString(myBoard.getPieceType(x,y)));
        // enumerate all possible moves of a piece

        for(String key : pieceMoves.keySet()){
            if(team.equals("" + key.charAt(0))){
                String[] move = pieceMoves.getString(key).split(",");
                int newX = Integer.parseInt(move[0]) + currentPiece.x;
                int newY = Integer.parseInt(move[1]) + currentPiece.y;
                possibleMoves.add(new Point(newX, newY));
            }
        }
        for(Point move: possibleMoves){
            if(myBoard.getPlayerNumber(move.x, move.y) == myBoard.getPlayerNumber(x,y)) {
                possibleMoves.remove(move);
                int xIncrement = 0;
                int yIncrement = 0;
                if(move.x - x > 0)  xIncrement = 1;
                if(move.x - x < 0)  xIncrement = -1;
                if(move.y - y > 0)  yIncrement = 1;
                if(move.y - y < 0)  yIncrement = -1;
                possibleMoves = removePointsBeyond(possibleMoves, move, xIncrement, yIncrement);
            }
        }
        possibleMoves.removeIf(move -> move.x < 0 || move.x > width - 1 || move.y < 0 || move.y > height - 1);
        return possibleMoves;
    }

    private ArrayList<Point> getSimpleMoves(int x, int y){
        String team = Integer.toString(myBoard.getPlayerNumber(x,y));
        ArrayList<Point> possibleMoves = new ArrayList<>();
        ResourceBundle pieceMoves = ResourceBundle.getBundle(CHESS_PIECE_DATA.getString(myBoard.getPieceType(x,y)));
        // enumerate all possible moves of a piece

        for(String key : pieceMoves.keySet()){
            if(team.equals("" + key.charAt(0))){
                String[] move = pieceMoves.getString(key).split(",");
                int newX = Integer.parseInt(move[0]) + currentPiece.x;
                int newY = Integer.parseInt(move[1]) + currentPiece.y;
                possibleMoves.add(new Point(newX, newY));
            }
        }
        possibleMoves.removeIf(move -> move.x < 0 || move.x > width - 1 || move.y < 0 || move.y > height - 1);
        return possibleMoves;
    }

    private ArrayList<Point> getComplexMoves(int x, int y){
        String team = Integer.toString(myBoard.getPlayerNumber(x,y));
        ArrayList<Point> possibleMoves = new ArrayList<>();
        ResourceBundle pieceMoves = ResourceBundle.getBundle(CHESS_PIECE_DATA.getString(myBoard.getPieceType(x,y)));
        // enumerate all possible moves of a piece

        for(String key : pieceMoves.keySet()){
            if(team.equals("" + key.charAt(0))){
                for(int i = 0; i < 15; i++){
                    String[] move = pieceMoves.getString(key).split(",");
                    int newX = Integer.parseInt(move[0])*i + currentPiece.x;
                    int newY = Integer.parseInt(move[1])*i + currentPiece.y;
                    possibleMoves.add(new Point(newX, newY));
                }
            }
        }
        possibleMoves.removeIf(move -> move.x < 0 || move.x > width - 1 || move.y < 0 || move.y > height - 1);
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
