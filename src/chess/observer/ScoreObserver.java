package chess.observer;

import chess.game.Player;
import chess.game.Move;
import chess.pieces.Piece;
import chess.strategy.CheckmateScoreStrategy;
import chess.strategy.CaptureScoreStrategy;
import chess.strategy.ResignScoreStrategy;

import java.util.Observer;

public class ScoreObserver implements GameObserver {

    private CaptureScoreStrategy captureStrategy;
    private CheckmateScoreStrategy checkmateStrategy;
    private ResignScoreStrategy resignStrategy;

    public ScoreObserver() {
        this.captureStrategy = new CaptureScoreStrategy();
        this.checkmateStrategy = new CheckmateScoreStrategy();
        this.resignStrategy = new ResignScoreStrategy();
    }

    public void moveMade(Move move){

    }

    public void pieceCaptured(Piece piece, Player capturedBy){
        int points = captureStrategy.calcScore(piece);
        capturedBy.setPoints(capturedBy.getPoints() + points);
        System.out.println(capturedBy.getName() + " a capturat " + piece.type() + " și primește +" + points + " puncte!");
        System.out.println("Punctaj curent: " + capturedBy.getPoints());
    }

    public void playerSwitch(Player currentPlayer){

    }

    public void gameOver(Player winner, String winnerMessage){
        if (winner == null) {
            System.out.println("Joc terminat fără câștigător");
            return;
        }

        int bonusPoints = 0;

        switch(winnerMessage.toLowerCase()) {
            case "checkmate":
                bonusPoints = checkmateStrategy.calcScore(true);
                System.out.println(winner.getName() + " a câștigat prin ȘAH-MAT! +" + bonusPoints + " puncte bonus!");
                break;

            case "resign":
                bonusPoints = resignStrategy.calcScore(true);
                System.out.println(winner.getName() + " a câștigat prin RENUNȚARE! +" + bonusPoints + " puncte bonus!");
                break;

            case "draw":
                bonusPoints = resignStrategy.calcScore(true);
                System.out.println("EGALITATE! " + winner.getName() + " primește +" + bonusPoints + " puncte!");
                break;
        }

        winner.setPoints(winner.getPoints() + bonusPoints);
        System.out.println("Punctaj final " + winner.getName() + ": " + winner.getPoints() + " puncte");
    }
}


