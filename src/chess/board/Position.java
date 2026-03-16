package chess.board;

import java.util.Objects;

public class Position implements Comparable<Position> {
    private char x;
    private int y;

    public Position(char x, int y) {
        this.x = x;
        this.y = y;
    }

    public char getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(char x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "" + x + ", " + y;
    }

    @Override
    public int compareTo(Position o){
        if(this.y != o.y)
            return this.y - o.y;
        return this.x - o.x;
    }

    public boolean isValid() {
        return x >= 'A' && x <= 'H' && y >= 1 && y <= 8;
    }


}
