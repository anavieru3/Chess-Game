package chess.pieces;

import chess.board.Colors;
import chess.board.Position;

/**
 * Factory Pattern - Creează piese de șah fără a expune logica de instanțiere
 * Centralizează crearea obiectelor și permite extensibilitate
 */
public class PieceFactory {

    /**
     * Creează o piesă de șah pe baza tipului specificat
     * @param type Tipul piesei (KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN)
     * @param color Culoarea piesei (WHITE, BLACK)
     * @param position Poziția inițială a piesei
     * @return Piesa creată sau null dacă tipul este invalid
     */
    public static Piece createPiece(String type, Colors color, Position position) {
        if (type == null || color == null || position == null) {
            return null;
        }

        switch (type.toUpperCase()) {
            case "KING":
            case "K":
                return new King(color, position);

            case "QUEEN":
            case "Q":
                return new Queen(color, position);

            case "ROOK":
            case "R":
                return new Rook(color, position);

            case "BISHOP":
            case "B":
                return new Bishop(color, position);

            case "KNIGHT":
            case "N":
                return new Knight(color, position);

            case "PAWN":
            case "P":
                return new Pawn(color, position);

            default:
                System.err.println("Tip de piesă necunoscut: " + type);
                return null;
        }
    }

    /**
     * Creează o piesă pe baza caracterului reprezentativ
     * @param pieceChar Caracterul piesei (K, Q, R, B, N, P)
     * @param color Culoarea piesei
     * @param position Poziția piesei
     * @return Piesa creată
     */
    public static Piece createPiece(char pieceChar, Colors color, Position position) {
        return createPiece(String.valueOf(pieceChar), color, position);
    }
}