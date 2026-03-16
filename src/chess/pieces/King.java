package chess.pieces;

import chess.board.Colors;
import chess.board.Position;
import chess.strategy.KingMoveStrategy;

public class King extends Piece {
    public King(Colors color, Position position) {
        super(color, position, new KingMoveStrategy());
    }

    @Override
    public char type() {
        return 'K';
    }
}