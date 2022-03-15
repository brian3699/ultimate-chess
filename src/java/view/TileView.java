package view;

import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ResourceBundle;

public class TileView extends Region {
    private static final String MAGIC_VALUE_RESOURCE_PATH = "view.resources.MagicValues";
    private static final String TILE_SIZE = "tileSize";
    private final ResourceBundle magicValueResource;
    private final HBox myPiece;
    private final Image pieceImage;

    public TileView(String pieceType, int team) {
        // Initialize a resource bundle containing all magic values
        magicValueResource = ResourceBundle.getBundle(MAGIC_VALUE_RESOURCE_PATH);
        // HBox is used instead of a rectangle to set background
        myPiece = new HBox();
        String imagePath = magicValueResource.getString(pieceType + "_Image"+team);
        System.out.println(imagePath);
        pieceImage = new Image(imagePath);
        setTile(pieceImage);
    }

    // Initialize a piece node and insert an image.
    private void setTile(Image pieceImage){
        int tileSize = Integer.parseInt(magicValueResource.getString(TILE_SIZE));
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
