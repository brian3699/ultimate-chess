package model.gameEngine;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import model.board.Board;
import model.piece.Piece;
import model.util.ReflectionHandler;

import java.awt.Point;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;




public class ChessEngine {
    private static final String DEFAULT_BOARD_DATA_PATH = "resources/board/Default_Chess_Board.csv";
    private static final String DEFAULT_TEAM_DATA_PATH = "resources/board/Default_Chess_Board_Team.csv";
    private static final ResourceBundle CHESS_PIECE_DATA = ResourceBundle.getBundle("model/pieceInfo/ChessPiecePaths");
    private static final String CLASS_PATH = "model.gameEngine.ChessEngine";

    private Board<Piece> myBoard;
    private int width;
    private int height;
    private CSVReader boardReader;
    private CSVReader teamReader;
    private Point currentPiece;
    private final ReflectionHandler reflectionHandler;

    /**
     * sets the board using default file
     */
    public ChessEngine() {
        this(DEFAULT_BOARD_DATA_PATH, DEFAULT_TEAM_DATA_PATH);
    }

    /**
     * sets the board from user input
     * @param boardFilePath file path to a csv file containing the state of each cell
     * @param teamFilePath file path to a csv file containing the team number of each cell
     */
    public ChessEngine(String boardFilePath, String teamFilePath) {
        reflectionHandler = new ReflectionHandler();
        try{
            initializeBoard(boardFilePath, teamFilePath);
            setPiece();
        }catch (IOException | CsvException e){
            // TODO: Refactor
            System.out.println("Please Choose a Correct File");
        }

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

    /**
     * Return list of points a piece could move
     * @param x x coordinate of the piece
     * @param y y coordinate of the piece
     * @return ArrayList if Points a piece could move to
     */
    public ArrayList<Point> getValidMoves(int x, int y) {
        try{
            currentPiece = new Point(x,y);
            String methodName = "get" + myBoard.getPieceType(x,y) + "Moves";
            // invokes different method for each chess piece type
            return (ArrayList<Point>) reflectionHandler.handleMethod(methodName,CLASS_PATH).invoke(ChessEngine.this);
        }catch(InvocationTargetException | IllegalAccessException e){
            return null;
        }

    }

    private ArrayList<Point> getPawnMoves(){
        int x = currentPiece.x;
        int y = currentPiece.y;
        ArrayList<Point> moves = getSimpleMoves(x, y);
        int player = myBoard.getPlayerNumber(x,y);
        if(player == 1 && y == 1) moves.add(new Point(x, y+2));
        if(player == 2 && y == height - 2) moves.add(new Point(x, y - 2));
        return moves;
    }

    private ArrayList<Point> getKnightMoves(){
        return getSimpleMoves(currentPiece.x, currentPiece.y);
    }

    private ArrayList<Point> getBishopMoves(){
        return getComplexMoves(currentPiece.x, currentPiece.y);
    }

    private ArrayList<Point> getRookMoves() {
        return getComplexMoves(currentPiece.x, currentPiece.y);
    }

    private ArrayList<Point> getKingMoves() {
        return getSimpleMoves(currentPiece.x,currentPiece.y);
    }

    private ArrayList<Point> getQueenMoves() {
        return getComplexMoves(currentPiece.x, currentPiece.y);
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
                if(myBoard.getPlayerNumber(newX, newY) == myBoard.getPlayerNumber(x,y)) break;
                else if(myBoard.getPlayerNumber(newX, newY) != 0){
                    possibleMoves.add(new Point(newX, newY));
                    break;
                }
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
                for(int i = 0; i < Math.max(width, height); i++){
                    String[] move = pieceMoves.getString(key).split(",");
                    int newX = Integer.parseInt(move[0])*i + currentPiece.x;
                    int newY = Integer.parseInt(move[1])*i + currentPiece.y;
                    if(myBoard.getPlayerNumber(newX, newY) == myBoard.getPlayerNumber(x,y)) break;
                    else if(myBoard.getPlayerNumber(newX, newY) != 0){
                        possibleMoves.add(new Point(newX, newY));
                        break;
                    }
                    possibleMoves.add(new Point(newX, newY));
                }
            }
        }
        possibleMoves.removeIf(move -> move.x < 0 || move.x > width - 1 || move.y < 0 || move.y > height - 1);
        return possibleMoves;
    }


}
