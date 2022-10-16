package view.components;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * Create button and return
 *
 * @author Young Jun
 */
public class ButtonCreator implements ButtonCreatorInterface{

    private ResourceBundle languageBundle;
    private static final String BUTTON_ID = "Buttonbox";

    public ButtonCreator(ResourceBundle languageBundle) {
        this.languageBundle = languageBundle;
    }

    @Override
    // Create multiple buttons from a map and return a Hbox containing all buttons
    public HBox createMultipleButtons(Map<String, EventHandler> buttonMap, String[] imagePath) {
        HBox retBox = new HBox();
        int i = 0;
        // loop over all elements in the button map
        for (String s : buttonMap.keySet()) {
            VBox button = new VBox();
            // add image that will be displayed with the button
            button.getChildren().add(new ImageView(imagePath[i]));
            // create button and assign EventHandler
            button.getChildren().add(createButton(s, buttonMap.get(s)));
            // set id
            button.setId(BUTTON_ID);
            // add all elements to retBox
            retBox.getChildren().add(button);
            i++;
        }
        return retBox;
    }

    @Override
    // Create a button and returns it
    public Button createButton(String name, EventHandler event) {
        // get button's name from languageBundle
        String buttonName = languageBundle.getString(name);
        // create new button
        Button button = new Button(buttonName);
        // assign EventHandler to the button
        button.setOnAction(event);
        // return button
        return (Button) setId(name, button);
    }

    // set's id of a node
    private Node setId(String id, Node node) {
        node.setId(id);
        return node;
    }
}
