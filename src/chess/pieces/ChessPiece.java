package chess.pieces;

import chess.board.Board;
import chess.board.Colors;
import chess.board.Position;

import java.util.List;

public interface ChessPiece {
    List<Position> getPossibleMoves(Board board);
    boolean checkForCheck(Board board, Position kingPosition);
    char type();
    Colors getColor();
    Position getPosition();
}
