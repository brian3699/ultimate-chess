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
public class ButtonCreator {

    private ResourceBundle languageBundle;
    private static final String BUTTON_ID = "Buttonbox";

    public ButtonCreator(ResourceBundle languageBundle) {
        this.languageBundle = languageBundle;
    }

    /**
     * Create multiple buttons from a map and return a Hbox containing all buttons
     *
     * @param buttonMap Map of button name and action method
     * @return HBox containing all generated buttons
     */
    public HBox createMultipleButtons(Map<String, EventHandler> buttonMap, String[] imagePath) {
        HBox retBox = new HBox();
        int i = 0;
        for (String s : buttonMap.keySet()) {
            VBox button = new VBox();
            button.getChildren().add(new ImageView(imagePath[i]));
            button.getChildren().add(createButton(s, buttonMap.get(s)));
            button.setId(BUTTON_ID);
            retBox.getChildren().add(button);
            i++;
        }
        return retBox;
    }

    /**
     * Create a button and returns it
     *
     * @param name  name of the button
     * @param event method to be triggered when the button is clicked
     * @return button created from this method
     */
    public Button createButton(String name, EventHandler event) {
        String buttonName = languageBundle.getString(name);
        Button button = new Button(buttonName);
        button.setOnAction(event);
        return (Button) setId(name, button);
    }

    private Node setId(String id, Node node) {
        node.setId(id);
        return node;
    }
}
