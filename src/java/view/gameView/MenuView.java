package view.gameView;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import view.components.ButtonCreator;

import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * Creates the Menu of the chess variants
 *
 * @author Young Jun,
 */
public class MenuView {

    private Scene gameMenuScene;

    private static final String DEFAULT_STYLESHEET = "/view/resources/css/Default.css";
    private static final String CHESS_IMAGE = "/view/resources/images/Chess.png";
    private static final String XIANGQI_IMAGE = "/view/resources/images/Xiangqi.png";

    private static final String TITLE_ID = "Title2";
    private final ButtonCreator buttonGenerator;
    private final ResourceBundle languageResource;

    /**
     * Constructor for GameOptionsView.
     */
    public MenuView(ResourceBundle languageResource) {
        this.languageResource = languageResource;
        buttonGenerator = new ButtonCreator(languageResource);
    }

    /**
     * Generate the menu scene with the buttonMap given from the controller
     *
     * @param buttonMap the Map containing the name of the button and method it should trigger
     * @return MenuScene
     */
    public Scene generateMenuScene(String pageTitle, Map<String, EventHandler> buttonMap) {
        VBox root = new VBox();
        HBox title = new HBox(new Text(languageResource.getString(pageTitle)));
        String[] imageArray = new String[]{CHESS_IMAGE, XIANGQI_IMAGE};
        HBox buttonBox = buttonGenerator.createMultipleButtons(buttonMap, imageArray);
        title.setId(TITLE_ID);
        root.getChildren().addAll(title, buttonBox);
        gameMenuScene = new Scene(root);
        gameMenuScene.getStylesheets()
                .add(Objects.requireNonNull(getClass().getResource(DEFAULT_STYLESHEET)).toExternalForm());
        return gameMenuScene;
    }

}
