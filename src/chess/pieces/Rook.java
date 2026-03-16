package chess.pieces;

import chess.board.Colors;
import chess.board.Position;
import chess.strategy.RookMoveStrategy;

public class Rook extends Piece {
    public Rook(Colors color, Position position) {
        super(color, position, new RookMoveStrategy());
    }

    @Override
    public char type() {
        return 'R';
    }
}