package chess.pieces;

import chess.board.Colors;
import chess.board.Position;
import chess.strategy.KnightMoveStrategy;

public class Knight extends Piece {
    public Knight(Colors color, Position position) {
        super(color, position, new KnightMoveStrategy());
    }

    @Override
    public char type() {
        return 'N';
    }
}
