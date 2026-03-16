package chess.strategy;

import chess.board.Position;
import chess.board.Board;
import java.util.List;

public interface MoveStrategy {
    List<Position> getPossibleMoves(Board board, Position from, chess.board.Colors color);
}
