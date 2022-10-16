package view.components;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.Map;

public interface ButtonCreatorInterface {

    /**
     * Create multiple buttons from a map and return a Hbox containing all buttons
     *
     * @param buttonMap Map of button name and action method
     * @return HBox containing all generated buttons
     */
    HBox createMultipleButtons(Map<String, EventHandler> buttonMap, String[] imagePath);

    /**
     * Create a button and returns it
     *
     * @param name  name of the button
     * @param event method to be triggered when the button is clicked
     * @return button created from this method
     */
    Button createButton(String name, EventHandler event);

}
