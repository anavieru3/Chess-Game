package chess.strategy;

import chess.pieces.Piece;

public class CaptureScoreStrategy implements ScoreStrategy {

    @Override
    public int calcScore(Object context) {
        if (!(context instanceof Piece)) {
            return 0;
        }

        Piece capturedPiece = (Piece) context;

        switch(capturedPiece.type()) {
            case 'Q':
                return 90;
            case 'R':
                return 50;
            case 'B':
                return 30;
            case 'N':
                return 30;
            case 'P':
                return 10;
            case 'K':
                return 0;
            default:
                return 0;
        }
    }
}