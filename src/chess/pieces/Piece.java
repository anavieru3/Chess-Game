package chess.pieces;

import chess.board.Board;
import chess.board.Colors;
import chess.board.Position;
import chess.strategy.MoveStrategy;

import java.util.List;

public abstract class Piece implements ChessPiece {
    protected Colors color;
    protected Position position;
    protected MoveStrategy moveStrategy;

    public Piece(Colors color, Position position, MoveStrategy moveStrategy) {
        this.color = color;
        this.position = position;
        this.moveStrategy = moveStrategy;
    }
    public Colors getColor() {
        return color;
    }
    public Position getPosition() {
        return position;
    }
    public void setPosition(Position position) {
        this.position = position;
    }
    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }
    public List<Position> getPossibleMoves(Board board){
        return moveStrategy.getPossibleMoves(board, position, color);
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        List<Position> possibleMoves = getPossibleMoves(board);
        for (Position possibleMove : possibleMoves) {
            if (possibleMove.equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }
    public abstract char type();
}
