package view;

import javafx.scene.layout.GridPane;

import java.awt.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class BoardView extends GridPane {

    private static final String MAGIC_VALUE_RESOURCE_PATH = "view.resources.MagicValues";

    private final int rowCount;
    private final int colCount;
    private final Consumer clickMethod;
    private final ResourceBundle magicValueBundle;
    private final TileView[][] boardArray;
    private List<Point> possibleMoves;

    public BoardView(Consumer<Point> clickMethod, int rowCount, int colCount) {
        this.clickMethod = clickMethod;
        this.rowCount = rowCount;
        this.colCount = colCount;
        boardArray = new TileView[rowCount][colCount];
        magicValueBundle = ResourceBundle.getBundle(MAGIC_VALUE_RESOURCE_PATH);
    }

    public void setTile(String pieceType, int team, int rowNum, int colNum){
        TileView tile = new TileView(pieceType, team);
        addTileToBoard(tile, rowNum, colNum);
    }

    public void movePiece(int xOrigin, int yOrigin, int xNew, int yNew){
        removeHighlight();
        TileView currentPiece = boardArray[yOrigin][xOrigin];
        TileView destinationTile = boardArray[yNew][xNew];
        TileView emptyTile = new TileView("-", 0);
        this.getChildren().remove(currentPiece);
        this.getChildren().remove(destinationTile);
        addTileToBoard(currentPiece, yNew, xNew);
        addTileToBoard(emptyTile, yOrigin, xOrigin);
    }

    /**
     * Highlight possible moves in green.
     *
     * @param possibleMoves List of points with possible moves.
     */
    public void highlightPossibleMoves(List<Point> possibleMoves) {
        if(this.possibleMoves != null) removeHighlight();
        this.possibleMoves = possibleMoves;
        for (Point move : possibleMoves) {
            TileView tile = boardArray[move.y][move.x];
            this.getChildren().remove(tile);
            tile.setBackground(2);
            this.add(tile, move.x, move.y);
        }
    }

    private void removeHighlight(){
        for (Point move : possibleMoves) {
            TileView tile = boardArray[move.y][move.x];
            this.getChildren().remove(tile);
            addTileToBoard(tile, move.y, move.x);
        }
    }

    private void setTileColor(TileView tile, int rowNum, int colNum){
        int tileColor = ((rowNum % 2) + colNum)%2;
        tile.setBackground(tileColor);
    }

    private void addTileToBoard(TileView tile, int rowNum, int colNum){
        tile.setOnMouseClicked(event -> clickMethod.accept(new Point(colNum, rowNum)));
        setTileColor(tile, rowNum, colNum);
        boardArray[rowNum][colNum] = tile;
        this.add(tile, colNum, rowNum);
    }
}
