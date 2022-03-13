package view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ResourceBundle;

public class PieceView implements PieceViewInterface{
    private static final String MAGIC_VALUE_RESOURCE_PATH = "view.resources.MagicValues";
    private static final String TILE_SIZE = "tileSize";
    private final ResourceBundle magicValueResource;
    private final HBox myPiece;

    public PieceView(Image pieceImage) {
        // Initialize a resource bundle containing all magic values
        magicValueResource = ResourceBundle.getBundle(MAGIC_VALUE_RESOURCE_PATH);
        // HBox is used instead of a rectangle to set background
        myPiece = new HBox();
        setPieceNode(pieceImage);
    }

    // Initialize a piece node and insert an image.
    private void setPieceNode(Image pieceImage){
        int tileSize = Integer.parseInt(magicValueResource.getString(TILE_SIZE));
        Rectangle rectangle = new Rectangle(tileSize, tileSize);
        // fill the rectangle with image
        rectangle.setFill(new ImagePattern(pieceImage));
        // add rectangle to HBox myPiece
        myPiece.getChildren().add(rectangle);
    }

    public void setBackground(int colorID){
        // get color from magicValueResource
        String color = magicValueResource.getString(String.valueOf(colorID));
        myPiece.setStyle(color);
    }

    public Node getPieceNode() {return myPiece;}
}
