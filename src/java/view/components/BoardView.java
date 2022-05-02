package view.components;

import javafx.scene.layout.GridPane;

import java.awt.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * class that extends GridPane. Creates the board of the game.
 *
 * @author Young Jun
 */
public class BoardView extends GridPane {

    private static final String MAGIC_VALUE_RESOURCE_PATH = "view.resources.MagicValues";

    private final int rowCount;
    private final int colCount;
    private final Consumer clickMethod;
    private final ResourceBundle magicValueBundle;
    private final TileView[][] boardArray;
    private List<Point> possibleMoves;

    /**
     * Constructor for BoardView
     *
     * @param clickMethod method that will be clicked when a user clicks on a tile
     * @param rowCount    number of rows
     * @param colCount    number of columns
     */
    public BoardView(Consumer<Point> clickMethod, int rowCount, int colCount) {
        this.clickMethod = clickMethod;
        this.rowCount = rowCount;
        this.colCount = colCount;
        boardArray = new TileView[rowCount][colCount];
        magicValueBundle = ResourceBundle.getBundle(MAGIC_VALUE_RESOURCE_PATH);
    }

    /**
     * Sets the tile at (colNum, rowNum)
     *
     * @param pieceType type of the piece
     * @param team      number of the team
     * @param rowNum    row number
     * @param colNum    column number
     */
    public void setTile(String pieceType, int team, int rowNum, int colNum) {
        TileView tile = new TileView(pieceType, team);
        addTileToBoard(tile, rowNum, colNum);
    }

    /**
     * Move piece at (xOrigin, yOrigin) to (xNew, yNew)
     *
     * @param xOrigin column number of the moving tile
     * @param yOrigin row number of the tile
     * @param xNew    column number of the destination tile
     * @param yNew    row number of the destination tile
     */
    public void movePiece(int xOrigin, int yOrigin, int xNew, int yNew) {
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
        if (this.possibleMoves != null) removeHighlight();
        this.possibleMoves = possibleMoves;
        for (Point move : possibleMoves) {
            TileView tile = boardArray[move.y][move.x];
            this.getChildren().remove(tile);
            tile.setBackground(2);
            this.add(tile, move.x, move.y);
        }
    }

    // remove highlight
    private void removeHighlight() {
        for (Point move : possibleMoves) {
            TileView tile = boardArray[move.y][move.x];
            this.getChildren().remove(tile);
            addTileToBoard(tile, move.y, move.x);
        }
    }

    // highlights the tile green
    private void setTileColor(TileView tile, int rowNum, int colNum) {
        int tileColor = ((rowNum % 2) + colNum) % 2;
        tile.setBackground(tileColor);
    }

    // sets the tile and add to board
    private void addTileToBoard(TileView tile, int rowNum, int colNum) {
        tile.setOnMouseClicked(event -> clickMethod.accept(new Point(colNum, rowNum)));
        setTileColor(tile, rowNum, colNum);
        boardArray[rowNum][colNum] = tile;
        this.add(tile, colNum, rowNum);
    }
}
