package chess.observer;

import chess.game.Move;
import chess.game.Player;
import chess.pieces.Piece;

public class GUIObserver implements GameObserver {

    private Object gameFrame;

    public GUIObserver(Object gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void moveMade(Move move) {
            System.out.println("GUI: Actualizare tablă după mutare");
    }

    public void pieceCaptured(Piece piece, Player capturedBy) {
            System.out.println("GUI: Actualizare piese capturate și punctaj");
    }

    public void playerSwitch(Player currentPlayer) {
        System.out.println("GUI: Actualizare jucător curent: " + currentPlayer.getName());
    }

    public void gameOver(Player winner, String reason) {
        System.out.println("GUI: Afișare dialog final de joc");
    }
}