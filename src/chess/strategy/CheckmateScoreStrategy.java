package chess.strategy;

public class CheckmateScoreStrategy implements ScoreStrategy {
    public int calcScore(Object context) {
        if (!(context instanceof Boolean)) {
            return 0;
        }

        Boolean isWinner = (Boolean) context;

        return isWinner ? 300 : -300;
    }
}