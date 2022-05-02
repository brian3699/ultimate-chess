package view.gameView;

import java.awt.*;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class GetGameView {

    public GameView getGameView(String game, ResourceBundle languageResource, Consumer<Point> clickMethod, int rowCount, int colCount){
        switch(game) {
            case "Chess" :
                return new ChessView(languageResource, clickMethod, rowCount, colCount);
        }
        return null;
    }
}
