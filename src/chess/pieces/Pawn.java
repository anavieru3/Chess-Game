package chess.pieces;

import chess.board.Colors;
import chess.board.Position;
import chess.strategy.PawnMoveStrategy;

public class Pawn extends Piece {
    private boolean firstMove;

    public Pawn(Colors color, Position position) {
        super(color, position, new PawnMoveStrategy());
        this.firstMove = true;
    }

    @Override
    public char type() {
        return 'P';
    }

    public boolean getIsFirstMove() {
        return firstMove;
    }

    public void setIsFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
        this.firstMove = false;
    }
}