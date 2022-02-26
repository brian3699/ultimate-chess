package model.gameEngine;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.board.Board;
import model.piece.Piece;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;




public class ChessEngine {
    private static final String DEFAULT_BOARD_DATA ="ultimate_chess.src.model.resources.board.Default_Chess_Board.csv";
    private static final String DEFAULT_TEAM_DATA = "ultimate_chess.src.model.resources.board.Default_Chess_Board_Team.csv";
    private static final ResourceBundle CHESS_PIECE_DATA =
            ResourceBundle.getBundle("ultimate_chess.src.main.java.model.resources.pieceInfo.ChessPieceInfo.properties");

    private Board<Piece> myBoard;
    private int width;
    private int height;
    private CSVReader boardReader;
    private CSVReader teamReader;


    /**
     * sets the board using default data
     */
    public ChessEngine() throws IOException, CsvValidationException {
        initializeBoard(DEFAULT_BOARD_DATA, DEFAULT_TEAM_DATA);
        setPiece();
    }

    /**
     * sets the board from user input
     * @param boardFilePath file path to a csv file containing the state of each cell
     * @param teamFilePath file path to a csv file containing the team number of each cell
     */
    public ChessEngine(String boardFilePath, String teamFilePath) throws IOException, CsvValidationException {
        initializeBoard(boardFilePath, teamFilePath);
        setPiece();
    }

    // initialize class Board
    private void initializeBoard(String boardFilePath, String teamFilePath) throws IOException {
        // create csv file reader
        boardReader = new CSVReader(new FileReader(boardFilePath));
        teamReader = new CSVReader(new FileReader(teamFilePath));
        // get width and height
        width = boardReader.peek().length;
        height = (int) boardReader.getLinesRead();
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
                // path to the ResourceBundle containing Piece information
                String pieceInfoPath = CHESS_PIECE_DATA.getString(pieceLine[j]);
                ResourceBundle pieceDataResource = ResourceBundle.getBundle(pieceInfoPath);
                // team number of the piece
                int teamNumber = Integer.parseInt(teamLine[j]);
                myBoard.setCell(teamNumber, pieceDataResource, j, i);
            }
        }
    }

    private Point[] getValidMoves(int x, int y){
        ArrayList<Point> moves = myBoard.getPossibleMoves(x, y);

    }


}
