package model.gameEngine;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.board.Board;
import model.piece.Piece;

import java.io.FileReader;
import java.io.IOException;
import java.util.ResourceBundle;




public class ChessEngine {
    private static final String DEFAULT_BOARD_DATA ="ultimate_chess.src.model.resources.board.Default_Chess_Board.csv";
    private static final String DEFAULT_TEAM_DATA = "ultimate_chess.src.model.resources.board.Default_Chess_Board_Team.csv";
    private static final ResourceBundle PIECE_VALUE_PATH =
            ResourceBundle.getBundle("ultimate_chess.src.model.resources.pieceInfo.PawnWhiteMoves.properties");

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
            String[] pieceLine = boardReader.readNext();
            String[] teamLine = teamReader.readNext();
            for(int j = 0; j < width; j++){
                int teamNumber = Integer.parseInt(teamLine[j]);
                myBoard.setCell(teamNumber, pieceLine[j], j, i);
            }
        }
    }

    private String[] getValidMoves(int x, int y){
        String pieceType = myBoard.getPieceType(x, y);

    }


}
