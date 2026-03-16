package chess.board;

import chess.pieces.*;
import chess.exceptions.InvalidMoveException;

import java.util.TreeSet;

public class Board {
    private TreeSet<ChessPair<Position, Piece>> pieces;

    public Board() {
        pieces = new TreeSet<>();
    }

    public void initialize() {
        pieces.clear();

        pieces.add(new ChessPair<>(new Position('A', 1), new Rook(Colors.WHITE, new Position('A', 1))));
        pieces.add(new ChessPair<>(new Position('B', 1), new Knight(Colors.WHITE, new Position('B', 1))));
        pieces.add(new ChessPair<>(new Position('C', 1), new Bishop(Colors.WHITE, new Position('C', 1))));
        pieces.add(new ChessPair<>(new Position('D', 1), new Queen(Colors.WHITE, new Position('D', 1))));
        pieces.add(new ChessPair<>(new Position('E', 1), new King(Colors.WHITE, new Position('E', 1))));
        pieces.add(new ChessPair<>(new Position('F', 1), new Bishop(Colors.WHITE, new Position('F', 1))));
        pieces.add(new ChessPair<>(new Position('G', 1), new Knight(Colors.WHITE, new Position('G', 1))));
        pieces.add(new ChessPair<>(new Position('H', 1), new Rook(Colors.WHITE, new Position('H', 1))));

        for(char col = 'A'; col <= 'H'; col++) {
            Position position = new Position(col, 2);
            pieces.add(new ChessPair<>(position, new Pawn(Colors.WHITE, position)));
        }

        pieces.add(new ChessPair<>(new Position('A', 8), new Rook(Colors.BLACK, new Position('A', 8))));
        pieces.add(new ChessPair<>(new Position('B', 8), new Knight(Colors.BLACK, new Position('B', 8))));
        pieces.add(new ChessPair<>(new Position('C', 8), new Bishop(Colors.BLACK, new Position('C', 8))));
        pieces.add(new ChessPair<>(new Position('D', 8), new Queen(Colors.BLACK, new Position('D', 8))));
        pieces.add(new ChessPair<>(new Position('E', 8), new King(Colors.BLACK, new Position('E', 8))));
        pieces.add(new ChessPair<>(new Position('F', 8), new Bishop(Colors.BLACK, new Position('F', 8))));
        pieces.add(new ChessPair<>(new Position('G', 8), new Knight(Colors.BLACK, new Position('G', 8))));
        pieces.add(new ChessPair<>(new Position('H', 8), new Rook(Colors.BLACK, new Position('H', 8))));

        for(char col = 'A'; col <= 'H'; col++) {
            Position position = new Position(col, 7);
            pieces.add(new ChessPair<>(position, new Pawn(Colors.BLACK, position)));
        }
    }

    public Piece getPieceAt(Position position) {
        for(ChessPair<Position, Piece> p : pieces) {
            if(p.getKey().equals(position))
                return p.getValue();
        }
        return null;
    }

    public boolean isValidMove(Position from, Position to) {
        if(!from.isValid() || !to.isValid()) {
            return false;
        }

        Piece piece = getPieceAt(from);
        if(piece == null) {
            return false;
        }

        if(!piece.getPossibleMoves(this).contains(to)) {
            return false;
        }

        Piece capturated = getPieceAt(to);
        Colors color = piece.getColor();

        deletePiece(from);
        if(capturated != null) {
            deletePiece(to);
        }
        addPiece(to, piece);
        piece.setPosition(to);

        Position kingPosition = findKing(color);
        boolean inSah = false;

        if(kingPosition != null) {
            inSah = isSah(color);
        }

        deletePiece(to);
        addPiece(from, piece);
        piece.setPosition(from);
        if(capturated != null) {
            addPiece(to, capturated);
        }

        return !inSah;
    }

    public void movePiece(Position from, Position to) throws InvalidMoveException {
        if(!isValidMove(from, to)) {
            throw new InvalidMoveException("Mutare invalida de la " + from + " la " + to);
        }

        Piece piece = getPieceAt(from);
        Piece capturated = getPieceAt(to);

        deletePiece(from);

        if(capturated != null) {
            deletePiece(to);
        }

        piece.setPosition(to);
        addPiece(to, piece);

        if(piece instanceof Pawn) {
            if((piece.getColor() == Colors.WHITE && to.getY() == 8) ||
                    (piece.getColor() == Colors.BLACK && to.getY() == 1)) {
                deletePiece(to);
                Queen queen = new Queen(piece.getColor(), to);
                addPiece(to, queen);
            }
        }
    }

    private void addPiece(Position pos, Piece piece) {
        pieces.add(new ChessPair<>(pos, piece));
    }

    private void deletePiece(Position pos) {
        pieces.removeIf(pair -> pair.getKey().equals(pos));
    }


    private Position findKing(Colors color) {
        for(ChessPair<Position, Piece> pair : pieces) {
            Piece p = pair.getValue();
            if(p instanceof King && p.getColor() == color) {
                return pair.getKey();
            }
        }
        return null;
    }

    private boolean isSah(Colors culoareRege) {
        Position kingPosition = findKing(culoareRege);
        if(kingPosition == null) return false;

        for(ChessPair<Position, Piece> pair : pieces) {
            Piece p = pair.getValue();
            if(p.getColor() != culoareRege) {
                if(p.checkForCheck(this, kingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    public TreeSet<ChessPair<Position, Piece>> getPieces() {
        return pieces;
    }
}