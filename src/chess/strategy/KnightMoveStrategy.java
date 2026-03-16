package chess.strategy;

import chess.board.Board;
import chess.board.Colors;
import chess.board.Position;
import chess.pieces.Piece;
import java.util.ArrayList;
import java.util.List;


public class KnightMoveStrategy implements MoveStrategy {

    @Override
    public List<Position> getPossibleMoves(Board board, Position from, Colors color) {
        List<Position> possibleMoves = new ArrayList<>();

        int[] dx = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] dy = {-1, 1, -2, 2, -2, 2, -1, 1};

        for(int i = 0; i < 8; i++) {
            char newX = (char)(from.getX() + dx[i]);
            int newY = from.getY() + dy[i];
            Position newPosition = new Position(newX, newY);

            if(newPosition.isValid()) {
                Piece p = board.getPieceAt(newPosition);

                if(p == null || p.getColor() != color) {
                    possibleMoves.add(newPosition);
                }
            }
        }
        return possibleMoves;
    }
}