package model.gameEngine;

import com.opencsv.exceptions.CsvException;
import model.util.ReflectionHandler;

import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class ChessEngine extends GameEngine {
    private static final String DEFAULT_BOARD_DATA_PATH = "resources/board/Default_Chess_Board.csv";
    private static final String DEFAULT_TEAM_DATA_PATH = "resources/board/Default_Chess_Board_Team.csv";
    private static final String DEFAULT_CHESS_PIECE_DATA = "model/pieceInfo/ChessPiecePaths";
    private static final String CLASS_PATH = "model.gameEngine.ChessEngine";

    private Point currentPiece;
    private final ReflectionHandler reflectionHandler;
    private List<Point> possibleMoves;
    private boolean[][] player1PossibleMoves;
    private boolean[][] player2PossibleMoves;


    /**
     * sets the board using default file
     */
    public ChessEngine() throws IOException, CsvException {
        super(DEFAULT_BOARD_DATA_PATH, DEFAULT_TEAM_DATA_PATH, DEFAULT_CHESS_PIECE_DATA);
        reflectionHandler = new ReflectionHandler();
    }

    /**
     * sets the board from user input
     * @param boardFilePath file path to a csv file containing the state of each cell
     * @param teamFilePath file path to a csv file containing the team number of each cell
     */
    public ChessEngine(String boardFilePath, String teamFilePath, String pieceFilePath) throws IOException, CsvException {
        super(boardFilePath, teamFilePath, pieceFilePath);
        reflectionHandler = new ReflectionHandler();
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
            String methodName = "get" + getPieceType(x,y) + "Moves";
            // invokes different method for each chess piece type
            System.out.println(""+x+y);
            System.out.println(reflectionHandler.handleMethod(methodName,CLASS_PATH));
            return (ArrayList<Point>) reflectionHandler.handleMethod(methodName,CLASS_PATH).invoke(ChessEngine.this);
        }catch(InvocationTargetException | IllegalAccessException e){
            return null;
        }
    }


    private List<Point> getPawnMoves(){
        int x = currentPiece.x;
        int y = currentPiece.y;
        List<Point> moves = getSimpleMoves(x, y);
        int player = getPieceTeam(x,y);
        if(player == 1 && y == height - 2 && getPieceTeam(x, y - 1) == 0) moves.add(new Point(x, y - 2));
        if(player == 2 && y == 1 && getPieceTeam(x, y + 1) == 0) moves.add(new Point(x, y + 2));
        List<Point> returnList = new ArrayList<>();

        for(Point move: moves){
            int xMove = move.x - x;
            int yMove = move.y - y;
            int playerNumber = getPieceTeam(move.x, move.y);
            if(playerNumber == 0){
                if((xMove == 1 && yMove == -1) | (xMove == -1 && yMove == -1) | (xMove == 1 && yMove == 1) |
                        (xMove == -1 && yMove == 1)) continue;
            }else{
                if((xMove == 0 && yMove == 1) | (xMove == 0 && yMove == 2) | (xMove == 0 && yMove == -1) |
                        (xMove == 0 && yMove == -2)) continue;
            }
            returnList.add(move);
        }
        possibleMoves = returnList;
        return possibleMoves;
    }

    private List<Point> getKnightMoves(){
        return getSimpleMoves(currentPiece.x, currentPiece.y);
    }

    private List<Point> getBishopMoves(){
        return getComplexMoves(currentPiece.x, currentPiece.y);
    }

    private List<Point> getRookMoves() {
        return getComplexMoves(currentPiece.x, currentPiece.y);
    }

    private List<Point> getKingMoves() {
        if(getCurrentPlayer() == getPieceTeam(currentPiece.x,currentPiece.y)) {
            possibleMoves =  removeCheckMoves(getSimpleMoves(currentPiece.x,currentPiece.y), getCurrentPlayer());
            return possibleMoves;
        }else {
            return getSimpleMoves(currentPiece.x, currentPiece.y);
        }
    }

    private List<Point> getQueenMoves() {
        return getComplexMoves(currentPiece.x, currentPiece.y);
    }


    private List<Point> getSimpleMoves(int x, int y){
        String team = ""+ getPieceTeam(x,y);
        int teamNumber = Integer.parseInt(team);
        possibleMoves = new ArrayList<>();
        ResourceBundle pieceMoves = ResourceBundle.getBundle(pieceResource.getString(getPieceType(x,y)));
        // enumerate all possible moves of a piece

        for(String key : pieceMoves.keySet()){
            if(team.equals("" + key.charAt(0))){
                String[] move = pieceMoves.getString(key).split(",");
                int newX = Integer.parseInt(move[0]) + currentPiece.x;
                int newY = Integer.parseInt(move[1]) + currentPiece.y;
                if(getPieceTeam(newX, newY) == getPieceTeam(x,y)) continue;
                else if(getPieceTeam(newX, newY) != 0){
                    possibleMoves.add(new Point(newX, newY));
                    continue;
                }
                possibleMoves.add(new Point(newX, newY));
            }
        }
        possibleMoves.removeIf(move -> move.x < 0 || move.x > width - 1 || move.y < 0 || move.y > height - 1);
        return possibleMoves;
    }

    private List<Point> getComplexMoves(int x, int y){
        String team = Integer.toString(getPieceTeam(x,y));
        possibleMoves = new ArrayList<>();
        ResourceBundle pieceMoves = ResourceBundle.getBundle(pieceResource.getString(getPieceType(x,y)));
        // enumerate all possible moves of a piece

        for(String key : pieceMoves.keySet()){
            if(team.equals("" + key.charAt(0))){
                String[] move = pieceMoves.getString(key).split(",");
                for(int i = 1; i < Math.max(width, height); i++){
                    int newX = Integer.parseInt(move[0])*i + currentPiece.x;
                    int newY = Integer.parseInt(move[1])*i + currentPiece.y;
                    if(getPieceTeam(newX, newY) == getPieceTeam(x,y)) break;
                    else if(getPieceTeam(newX, newY) != 0){
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

    @Override
    public String clickType(int x, int y){
        if(super.clickType(x, y) != null) return super.clickType(x,y);
        if (possibleMoves == null) return "errorClick";
        for (Point move : possibleMoves) {
            if (move.x == x && move.y == y && getPieceTeam(x, y) == 0) return "movePiece";
            if (move.x == x && move.y == y && getPieceTeam(x, y) != 0) return "capturePiece";
        }
        return "errorClick";
    }


    public boolean[][] getAllMovableTile(int playerNumber){
        ArrayList<Point> playerAllPossibleMoves = new ArrayList<>();
        for(int i = 0; i < height; i ++){
            for(int j = 0; j < width; j++){
                if(getPieceTeam(j, i) == playerNumber){
                    playerAllPossibleMoves.addAll(getValidMoves(j, i));
                }
            }
        }
        boolean[][] playerBoard = new boolean[height][width];
        if(playerNumber == 1) player1PossibleMoves = playerBoard;
        else if(playerNumber == 2) player2PossibleMoves = playerBoard;

        for(Point move : playerAllPossibleMoves){
            playerBoard[move.y][move.x] = true;
        }

        return playerBoard;
    }

    private List<Point> removeCheckMoves(List<Point> possibleMoves, int playerNumber){
            Point bufferPiece = currentPiece;
            int opponent = playerNumber % 2 + 1;
            boolean[][] opponentMoves = getAllMovableTile(opponent);

            List<Point> returnList = new ArrayList<>();

            for(Point move : possibleMoves){
                if(!opponentMoves[move.y][move.x]) returnList.add(move);
            }
            currentPiece = bufferPiece;
            return returnList;
    }

    public Point getCurrentPiece(){
        return new Point(currentPiece.x, currentPiece.y);
    }


    public boolean detectCheck(){
        int opponent = getCurrentPlayer() % 2 + 1;
        boolean[][] opponentMoves = getAllMovableTile(opponent);
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(opponentMoves[j][i] && getPieceType(i,j).equals("King") && getPieceTeam(i,j) == getCurrentPlayer()){
                    return true;
                }
            }
        }
        return false;
    }


}
