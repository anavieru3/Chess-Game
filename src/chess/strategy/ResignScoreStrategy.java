package chess.strategy;

public class ResignScoreStrategy implements ScoreStrategy {

    @Override
    public int calcScore(Object context) {
        if (!(context instanceof Boolean)) {
            return 0;
        }

        Boolean isWinner = (Boolean) context;

        return isWinner ? 150 : -150;
    }
}