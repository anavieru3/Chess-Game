package chess.strategy;

import chess.board.Board;
import chess.board.Colors;
import chess.board.Position;
import chess.pieces.Piece;
import java.util.ArrayList;
import java.util.List;

public class KingMoveStrategy implements MoveStrategy{
    public List<Position> getPossibleMoves(Board board, Position from, Colors color) {
        List<Position> possibleMoves = new ArrayList<>();

        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            char newX = (char) (from.getX() + dx[i]);
            int newY = from.getY() + dy[i];
            Position newPosition = new Position(newX, newY);

            if (newPosition.isValid()) {
                Piece p = board.getPieceAt(newPosition);

                if (p == null)
                    possibleMoves.add(newPosition);
            }
        }
        return possibleMoves;
    }
}
