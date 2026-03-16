package chess.observer;

import chess.game.Move;
import chess.game.Player;
import chess.pieces.Piece;
import java.util.ArrayList;
import java.util.List;

public class MoveHistoryObserver implements GameObserver {

    private List<Move> moveHistory;

    public MoveHistoryObserver() {
        this.moveHistory = new ArrayList<>();
    }

    public void moveMade(Move move) {
        moveHistory.add(move);
        int moveNumber = moveHistory.size();
        System.out.println("Mutare #" + moveNumber + ": " + move);
    }

    public void pieceCaptured(Piece piece, Player capturedBy) {
    }

    public void playerSwitch(Player currentPlayer) {
        System.out.println("──────────────────────────────────");
        System.out.println("🎮 Rândul lui " + currentPlayer.getName() +
                " (" + currentPlayer.getColor() + ")");
        System.out.println("──────────────────────────────────");
    }

    public void gameOver(Player winner, String reason) {
        System.out.println("\n╔════════════════════════════════╗");
        System.out.println("║     JOC TERMINAT               ║");
        System.out.println("╚════════════════════════════════╝");
        System.out.println("📊 Total mutări: " + moveHistory.size());
        System.out.println("🏆 Câștigător: " + (winner != null ? winner.getName() : "Egalitate"));
        System.out.println("📋 Motiv: " + reason);
        System.out.println("════════════════════════════════\n");
    }

    public List<Move> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }

    public void clearHistory() {
        moveHistory.clear();
        System.out.println("🗑️  Istoric șters - joc nou");
    }

    public Move getLastMove() {
        if (moveHistory.isEmpty()) {
            return null;
        }
        return moveHistory.get(moveHistory.size() - 1);
    }

    public void printFullHistory() {
        System.out.println("\n📜 ISTORIC COMPLET:");
        System.out.println("══════════════════════════════════");

        if (moveHistory.isEmpty()) {
            System.out.println("  (nicio mutare încă)");
        } else {
            for (int i = 0; i < moveHistory.size(); i++) {
                Move move = moveHistory.get(i);
                System.out.println((i + 1) + ". " + move);
            }
        }

        System.out.println("══════════════════════════════════\n");

    }
}
