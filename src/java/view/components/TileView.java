package view.components;

import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ResourceBundle;

/**
 * A class that extends Region. Creates a view of the tile.
 *
 * @author Young Jun
 */
public class TileView extends Region {
    private static final String MAGIC_VALUE_RESOURCE_PATH = "view.resources.MagicValues";
    private static final int DEFAULT_TILE_SIZE = 100;
    private final ResourceBundle magicValueResource;
    private final Image pieceImage;
    private int tileSize;
    private String pieceType;

    /**
     * Constructor for TileView
     *
     * @param pieceType type of the piece
     * @param team      team number
     * @param tileSize  size of the tile
     */
    public TileView(String pieceType, int team, int tileSize) {
        magicValueResource = ResourceBundle.getBundle(MAGIC_VALUE_RESOURCE_PATH);
        this.pieceType = pieceType;
        this.tileSize = tileSize;
        // Initialize a resource bundle containing all magic values
        String imagePath = magicValueResource.getString(pieceType + "_Image" + team);
        pieceImage = new Image(imagePath);
        setTile(pieceImage);
    }

    /**
     * Constructor for TileView. Sets tile to default size.
     *
     * @param pieceType type of the piece
     * @param team      team number
     */
    public TileView(String pieceType, int team) {
        this(pieceType, team, DEFAULT_TILE_SIZE);
    }

    /**
     * return piece type
     * @return pieceType
     */
    public String getPieceType(){
        return pieceType;
    }



    // Initialize a piece node and insert an image.
    private void setTile(Image pieceImage) {
        Rectangle rectangle = new Rectangle(tileSize, tileSize);
        // fill the rectangle with image

        rectangle.setFill(new ImagePattern(pieceImage));
        // add rectangle
        getChildren().add(rectangle);
    }

    /**
     * Sets the background color of a tile.
     *
     * @param colorID hex color code
     */
    public void setBackground(int colorID) {
        // get color from magicValueResource
        String colorHex = magicValueResource.getString(String.valueOf(colorID));
        setStyle(colorHex);
    }

}
