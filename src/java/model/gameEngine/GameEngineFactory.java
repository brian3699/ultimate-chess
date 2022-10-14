package model.gameEngine;

/**
 * Getter class of a Factory. Returns GameEngine of the board game.
 *
 * @author Young Jun
 */
public class GameEngineFactory {

    public GameEngine getGameEngine(String game) {
        switch (game) {
            case "Chess":
                return ChessEngine.getInstance();
        }
        return null;
    }
}
