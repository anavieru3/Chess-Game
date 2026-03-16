package chess.game;

import chess.board.Board;
import chess.board.ChessPair;
import chess.board.Colors;
import chess.board.Position;
import chess.exceptions.InvalidMoveException;
import chess.pieces.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Player {

    private String name;
    private Colors color;
    private List<Piece> capturatedPieces;
    private TreeSet<ChessPair<Position, Piece>> ownedPieces;
    private int points;

    public Player(String name, Colors color) {
        this.name = name;
        this.color = color;
        this.capturatedPieces = new ArrayList<>();
        this.ownedPieces = new TreeSet<>();
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public Colors getColor() {
        return color;
    }

    public boolean isComputer() {
        return false;
    }

    public void makeMove(Position from, Position to, Board board) throws InvalidMoveException {
        Piece p = board.getPieceAt(from);

        if(p == null) {
            throw new InvalidMoveException("Nu exista piesa la pozitia " + from);
        }

        if(p.getColor() != color) {
            throw new InvalidMoveException("Nu poti muta piesa adversarului!");
        }

        Piece capturated = board.getPieceAt(to);

        board.movePiece(from, to);

        if(capturated != null && capturated.getColor() != color) {
            capturatedPieces.add(capturated);
            points(capturated);
        }

        ownedPieces(board);
    }

    public List<Piece> getCapturedPieces() {
        return capturatedPieces;
    }

    public List<ChessPair<Position, Piece>> getOwnedPieces() {
        return new ArrayList<>(ownedPieces);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    private void points(Piece capturated) {
        switch(capturated.type()) {
            case 'Q':
                points += 90;
                break;
            case 'R':
                points += 50;
                break;
            case 'B':
                points += 30;
                break;
            case 'N':
                points += 30;
                break;
            case 'P':
                points += 10;
                break;
        }
    }

    private void ownedPieces(Board board) {
        ownedPieces.clear();

        for(ChessPair<Position, Piece> pair : board.getPieces()) {
            if(pair.getValue().getColor() == color) {
                ownedPieces.add(pair);
            }
        }
    }
}