package chess.pieces;

import chess.board.Colors;
import chess.board.Position;
import chess.strategy.BishopMoveStrategy;

public class Bishop extends Piece {
    public Bishop(Colors color, Position position) {
        super(color, position, new BishopMoveStrategy());
    }

    @Override
    public char type() {
        return 'B';
    }
}