package chess.game;

import chess.board.Colors;
import chess.board.Position;
import chess.pieces.Piece;

public class Move {
    private Colors color;
    private Position to;
    private Position from;
    private Piece capturated;

    public Move(Colors color, Position to, Position from) {
        this.color = color;
        this.to = to;
        this.from = from;
        this.capturated = null;
    }

    public Move(Colors color, Position to, Position  from, Piece capturated) {
        this.color = color;
        this.to = to;
        this.from = from;
        this.capturated = capturated;
    }
    public Colors getColor() {
        return color;
    }
    public Position getFrom() {
        return to;
    }
    public Position getTo() {
        return from;
    }
    public Piece getCapturated() {
        return capturated;
    }
    public void setColor(Colors color) {
        this.color = color;
    }
    public void setFrom(Position to) {
        this.to = to;
    }
    public void setTo(Position from) {
        this.from = from;
    }
    public void setCapturated(Piece capturated) {
        this.capturated = capturated;
    }

    @Override
    public String toString() {
        String moveString = color + ": "  + to + " -> " + from;
        if(capturated != null) {
            moveString += "Capturat: " + capturated.type();
        }
        return moveString;
    }
}
