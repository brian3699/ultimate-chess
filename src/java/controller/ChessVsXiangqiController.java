package controller;

import javafx.stage.Stage;

public class ChessVsXiangqiController extends ChessController{


    /**
     * Initializes game's model and view classes
     *
     * @param gameLanguage language
     * @param stage        stage
     */
    public ChessVsXiangqiController(String gameLanguage, Stage stage, String boardFilePath, String teamFilePath) {
        super(gameLanguage, stage, boardFilePath, teamFilePath, true);
    }

    @Override
    public void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String piece = chessEngine.getPieceType(i, j);
                int team = chessEngine.getPiecePlayerNumber(i, j);
                if(team == 1) team += 2;
                chessView.setTile(piece, team, j, i);
            }
        }
    }

}
