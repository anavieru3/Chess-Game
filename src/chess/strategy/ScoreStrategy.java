package chess.strategy;

import chess.board.Board;
import chess.pieces.Piece;

public interface ScoreStrategy {
    int calcScore(Object context);
}
