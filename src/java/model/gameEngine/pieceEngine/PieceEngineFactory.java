package model.gameEngine.pieceEngine;

/**
 * Factory class for classes that extends abstract class PieceEngine
 *
 * @author Young Jun
 */
public class PieceEngineFactory {

    private static final PieceEngineFactory instance = new PieceEngineFactory();

    public static PieceEngineFactory getInstance() {
        return instance;
    }

    private PieceEngineFactory() {
    }

    public PieceEngine getPieceEngine(String pieceType) {
        if (pieceType == null) return null;

        return switch (pieceType) {
            case "Bishop" -> BishopEngine.getInstance();
            case "King" -> KingEngine.getInstance();
            case "Knight" -> KnightEngine.getInstance();
            case "Pawn" -> PawnEngine.getInstance();
            case "Queen" -> QueenEngine.getInstance();
            case "Rook" -> RookEngine.getInstance();
            default -> null;
        };
    }

}
