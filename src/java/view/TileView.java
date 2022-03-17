package view;

import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ResourceBundle;

public class TileView extends Region {
    private static final String MAGIC_VALUE_RESOURCE_PATH = "view.resources.MagicValues";
    private static final int DEFAULT_TILE_SIZE = 100;
    private final ResourceBundle magicValueResource;
    private final Image pieceImage;
    private int tileSize;

    public TileView(String pieceType, int team, int tileSize){
        magicValueResource = ResourceBundle.getBundle(MAGIC_VALUE_RESOURCE_PATH);
        this.tileSize = tileSize;
        // Initialize a resource bundle containing all magic values
        String imagePath = magicValueResource.getString(pieceType + "_Image"+team);
        pieceImage = new Image(imagePath);
        setTile(pieceImage);
    }

    public TileView(String pieceType, int team) {
        this(pieceType, team, DEFAULT_TILE_SIZE);
    }

    // Initialize a piece node and insert an image.
    private void setTile(Image pieceImage){
        Rectangle rectangle = new Rectangle(tileSize, tileSize);
        // fill the rectangle with image

        rectangle.setFill(new ImagePattern(pieceImage));
        // add rectangle
        getChildren().add(rectangle);
    }

    public void setBackground(int colorID){
        // get color from magicValueResource
        String colorHex = magicValueResource.getString(String.valueOf(colorID));
        setStyle(colorHex);
    }

}
