package chess.strategy;


import chess.board.Board;
import chess.board.Colors;
import chess.board.Position;
import chess.pieces.Piece;
import java.util.ArrayList;
import java.util.List;

public class BishopMoveStrategy implements MoveStrategy {

    @Override
    public List<Position> getPossibleMoves(Board board, Position from, Colors color) {
        List<Position> possibleMoves = new ArrayList<>();

        int[] dx = {-1, -1, 1, 1};
        int[] dy = {-1, 1, -1, 1};

        for(int i = 0; i < 4; i++){
            int steps = 1;

            while(steps < 8) {
                char newX = (char) (from.getX() + dx[i] * steps);
                int newY = from.getY() + dy[i] * steps;
                Position newPosition = new Position(newX, newY);

                if (!newPosition.isValid()) {
                    break;
                }

                Piece p = board.getPieceAt(newPosition);

                if (p == null) {
                    possibleMoves.add(newPosition);
                    steps++;
                } else if (p.getColor() != color) {
                    possibleMoves.add(newPosition);
                    break;
                } else
                    break;

            }
        }
        return possibleMoves;
    }
}
