package chess.observer;

import chess.game.Move;
import chess.game.Player;
import chess.pieces.Piece;

public interface GameObserver {
    void moveMade(Move move);
    void pieceCaptured(Piece piece, Player capturedBy);
    void playerSwitch(Player currentPlayer);
    void gameOver(Player winner, String winnerMessage);
}
