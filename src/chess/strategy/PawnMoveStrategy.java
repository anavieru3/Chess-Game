package chess.strategy;

import chess.board.Board;
import chess.board.Colors;
import chess.board.Position;
import chess.pieces.Piece;
import chess.pieces.Pawn;
import java.util.ArrayList;
import java.util.List;


public class PawnMoveStrategy implements MoveStrategy {

    @Override
    public List<Position> getPossibleMoves(Board board, Position from, Colors color) {
        List<Position> possibleMoves = new ArrayList<>();

        int direction = (color == Colors.WHITE) ? 1 : -1;

        Piece pawn = board.getPieceAt(from);
        boolean isFirstMove = (pawn instanceof Pawn) && ((Pawn) pawn).getIsFirstMove();

        Position oneStep = new Position(from.getX(), from.getY() + direction);
        if(oneStep.isValid() && board.getPieceAt(oneStep) == null) {
            possibleMoves.add(oneStep);

            if(isFirstMove) {
                Position twoSteps = new Position(from.getX(), from.getY() + direction * 2);
                if(twoSteps.isValid() && board.getPieceAt(twoSteps) == null) {
                    possibleMoves.add(twoSteps);
                }
            }
        }

        Position leftDiagonal = new Position((char)(from.getX() - 1), from.getY() + direction);
        if(leftDiagonal.isValid()) {
            Piece pieceAtLeft = board.getPieceAt(leftDiagonal);
            if(pieceAtLeft != null && pieceAtLeft.getColor() != color) {
                possibleMoves.add(leftDiagonal);
            }
        }

        Position rightDiagonal = new Position((char)(from.getX() + 1), from.getY() + direction);
        if(rightDiagonal.isValid()) {
            Piece pieceAtRight = board.getPieceAt(rightDiagonal);
            if(pieceAtRight != null && pieceAtRight.getColor() != color) {
                possibleMoves.add(rightDiagonal);
            }
        }
        return possibleMoves;
    }
}