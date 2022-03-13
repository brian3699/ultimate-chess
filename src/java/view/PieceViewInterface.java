package view;

import javafx.scene.Node;


public interface PieceViewInterface {


    /**
     * Set background of the tile
     * @param colorID ID of background color
     */
    public void setBackground(int colorID);

    /**
     * returns the Piece as a node
     * Piece is returned as a node for flexibility and reusability
     * @return piece as a node
     */
    public Node getPieceNode();
}
