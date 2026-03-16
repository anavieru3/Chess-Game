package chess.pieces;

import chess.board.Colors;
import chess.board.Position;
import chess.strategy.QueenMoveStrategy;

public class Queen extends Piece {
    public Queen(Colors color, Position position) {
        super(color, position, new QueenMoveStrategy());
    }

    @Override
    public char type() {
        return 'Q';
    }
}
