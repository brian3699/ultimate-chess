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

        switch (pieceType){
            case "Bishop": return BishopEngine.getInstance();
            case "King" : return KingEngine.getInstance();
            case "Knight" : return KnightEngine.getInstance();
            case "Pawn": return PawnEngine.getInstance();
            case "Queen": return QueenEngine.getInstance();
            case "Rook": return RookEngine.getInstance();
            case "Cannon" : return CannonEngine.getInstance();
        }
        return null;

    }

}
