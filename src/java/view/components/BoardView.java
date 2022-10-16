package view.components;

import javafx.scene.layout.GridPane;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * class that extends GridPane. Creates the board of the game.
 *
 * @author Young Jun
 */
public class BoardView extends GridPane {

    private final Consumer clickMethod;
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
        boardArray = new TileView[rowCount][colCount];
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
        // create a tile with piece image
        TileView tile = new TileView(pieceType, team);
        // add tile to the board
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
        // remove highlight after a user moves a piece
        removeHighlight();
        // get the current piece
        TileView currentPiece = boardArray[yOrigin][xOrigin];
        // get the destination tile
        TileView destinationTile = boardArray[yNew][xNew];
        // create an empty tile
        TileView emptyTile = new TileView("-", 0);
        // remove current piece
        this.getChildren().remove(currentPiece);
        // remove destination tile
        this.getChildren().remove(destinationTile);
        // add current piece to empty destination tile
        addTileToBoard(currentPiece, yNew, xNew);
        // add empty tile to current tile
        addTileToBoard(emptyTile, yOrigin, xOrigin);
    }

    /**
     * Highlight possible moves in green.
     *
     * @param possibleMoves List of points with possible moves.
     */
    public void highlightPossibleMoves(List<Point> possibleMoves) {
        // remove any existing highlights
        if (this.possibleMoves != null) removeHighlight();
        // update possible moves
        this.possibleMoves = possibleMoves;
        // loop all tiles that a piece can move to
        for (Point move : possibleMoves) {
            // get tile
            TileView tile = boardArray[move.y][move.x];
            // remove the tile from the board
            this.getChildren().remove(tile);
            // change the background
            tile.setBackground(2);
            // add the tile back to the board
            this.add(tile, move.x, move.y);
        }
    }

    // remove highlight
    private void removeHighlight() {
        // check all tiles in possibleMoves
        for (Point move : possibleMoves) {
            // get the highlighted tile
            TileView tile = boardArray[move.y][move.x];
            // remove from the board
            this.getChildren().remove(tile);
            // add tile to board again after updating the background color
            addTileToBoard(tile, move.y, move.x);
        }
    }

    // highlights the tile green
    private void setTileColor(TileView tile, int rowNum, int colNum) {
        // calculate tile's background color
        // Chess game has two colors
        int tileColor = ((rowNum % 2) + colNum) % 2;
        // set the background color
        tile.setBackground(tileColor);
    }

    // sets the tile and add to board
    private void addTileToBoard(TileView tile, int rowNum, int colNum) {
        // assign a method that will be triggered when the tile is clicked
        tile.setOnMouseClicked(event -> clickMethod.accept(new Point(colNum, rowNum)));
        // set tile color
        setTileColor(tile, rowNum, colNum);
        // add tile to data structure
        boardArray[rowNum][colNum] = tile;
        // add tile to board
        add(tile, colNum, rowNum);
    }
}
