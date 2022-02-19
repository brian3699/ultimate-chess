package model.board;


import model.piece.Piece;
import model.piece.PieceInterface;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.Object;


public class Board implements BoardInterface{
    // Instances of pieces will be stored in this List
    private List<List<?>> myBoard;


    /**
     * default constructor. Creates 8X8 board
     */
    public Board(){
        setDefaultBoard(8,8);
    }

    /**
     * Creates a board
     * @param width width of the board
     * @param height height of the board
     */
    public Board(int width, int height){
        setDefaultBoard(width, height);
    }


    // Initialize board
    private void setDefaultBoard(int width, int height){
        myBoard = new ArrayList<>();
        for(int i = 0; i < height; i++){
            // create a row
            ArrayList<Piece> row = (ArrayList<Piece>) Arrays.asList(new Piece[width]);
            // add to myBoard
            myBoard.add(row);
        }
    }

    @Override
    public char peek(int x, int y) {
        return 0;
    }

    @Override
    public boolean move(int x1, int y1, int x2, int y2) {
        return false;
    }

    @Override
    public boolean setCell(char pieceType, int x, int y) {
        return false;
    }

    @Override
    public Point[] getPossibleMoves(int x, int y) {
        return new Point[0];
    }
}
