package chess.game;

import chess.board.Board;
import chess.board.Colors;
import chess.board.Position;
import chess.board.ChessPair;
import chess.game.Player;
import chess.pieces.Piece;
import chess.pieces.King;
import chess.exceptions.InvalidMoveException;
import java.util.ArrayList;
import java.util.List;

public class ComputerPlayer extends Player {

    private static final int MAX_DEPTH = 3;
    private static final int INFINITY = 1000000;

    public ComputerPlayer(String name, Colors color) {
        super(name, color);
    }

    @Override
    public boolean isComputer() {
        return true;
    }

    public boolean makeRandomMove(Board board) {
        BestMove bestMove = findBestMove(board);

        if (bestMove == null || bestMove.from == null) {
            return false;
        }

        try {
            makeMove(bestMove.from, bestMove.to, board);
            System.out.println("Computer: " + bestMove.from + " -> " + bestMove.to + " (eval: " + bestMove.score + ")");
            return true;
        } catch (InvalidMoveException e) {
            return false;
        }
    }

    private BestMove findBestMove(Board board) {
        BestMove best = new BestMove();
        best.score = -INFINITY;
        Colors opponent = (getColor() == Colors.WHITE) ? Colors.BLACK : Colors.WHITE;

        for (ChessPair<Position, Piece> pair : board.getPieces()) {
            if (pair.getValue().getColor() != getColor()) continue;

            Position from = pair.getKey();
            Piece piece = pair.getValue();
            List<Position> moves = piece.getPossibleMoves(board);

            for (Position to : moves) {
                if (!board.isValidMove(from, to)) continue;

                GameState state = simulateMove(board, from, to);
                int score = minimax(board, state, MAX_DEPTH - 1, -INFINITY, INFINITY, false, opponent);
                undoMove(board, state);

                if (score > best.score) {
                    best.score = score;
                    best.from = from;
                    best.to = to;
                }
            }
        }

        return best;
    }

    private int minimax(Board board, GameState lastState, int depth, int alpha, int beta, boolean maximizing, Colors currentColor) {
        if (depth == 0 || isGameOver(board, currentColor)) {
            return evaluate(board);
        }

        Colors nextColor = (currentColor == Colors.WHITE) ? Colors.BLACK : Colors.WHITE;

        if (maximizing) {
            int maxEval = -INFINITY;

            for (ChessPair<Position, Piece> pair : board.getPieces()) {
                if (pair.getValue().getColor() != getColor()) continue;

                Position from = pair.getKey();
                List<Position> moves = pair.getValue().getPossibleMoves(board);

                for (Position to : moves) {
                    if (!board.isValidMove(from, to)) continue;

                    GameState state = simulateMove(board, from, to);
                    int eval = minimax(board, state, depth - 1, alpha, beta, false, nextColor);
                    undoMove(board, state);

                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);

                    if (beta <= alpha) break;
                }
            }

            return maxEval;
        } else {
            int minEval = INFINITY;

            for (ChessPair<Position, Piece> pair : board.getPieces()) {
                if (pair.getValue().getColor() != nextColor) continue;

                Position from = pair.getKey();
                List<Position> moves = pair.getValue().getPossibleMoves(board);

                for (Position to : moves) {
                    if (!board.isValidMove(from, to)) continue;

                    GameState state = simulateMove(board, from, to);
                    int eval = minimax(board, state, depth - 1, alpha, beta, true, nextColor);
                    undoMove(board, state);

                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);

                    if (beta <= alpha) break;
                }
            }

            return minEval;
        }
    }

    private int evaluate(Board board) {
        int score = 0;
        Colors opponent = (getColor() == Colors.WHITE) ? Colors.BLACK : Colors.WHITE;

        for (ChessPair<Position, Piece> pair : board.getPieces()) {
            Piece p = pair.getValue();
            int value = getPieceValue(p);

            if (p.getColor() == getColor()) {
                score += value;
                score += getPositionBonus(p, pair.getKey());
            } else {
                score -= value;
            }
        }

        if (isInCheck(board, opponent)) {
            score += 50;
        }

        if (isInCheckmate(board, opponent)) {
            score += 100000;
        }

        return score;
    }

    private int getPieceValue(Piece p) {
        switch(p.type()) {
            case 'K': return 20000;
            case 'Q': return 900;
            case 'R': return 500;
            case 'B': return 330;
            case 'N': return 320;
            case 'P': return 100;
            default: return 0;
        }
    }

    private int getPositionBonus(Piece p, Position pos) {
        int bonus = 0;
        char x = pos.getX();
        int y = pos.getY();

        if ((x == 'D' || x == 'E') && (y == 4 || y == 5)) {
            bonus += 30;
        }

        if (p.type() == 'N' || p.type() == 'B') {
            if (y != 1 && y != 8) {
                bonus += 10;
            }
        }

        return bonus;
    }

    private GameState simulateMove(Board board, Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        Piece captured = board.getPieceAt(to);
        Position oldPos = new Position(piece.getPosition().getX(), piece.getPosition().getY());

        board.getPieces().removeIf(p -> p.getKey().equals(from));
        if (captured != null) {
            board.getPieces().removeIf(p -> p.getKey().equals(to));
        }
        piece.setPosition(to);
        board.getPieces().add(new ChessPair<>(to, piece));

        return new GameState(piece, from, to, oldPos, captured);
    }

    private void undoMove(Board board, GameState state) {
        board.getPieces().removeIf(p -> p.getKey().equals(state.to));
        state.piece.setPosition(state.oldPosition);
        board.getPieces().add(new ChessPair<>(state.from, state.piece));

        if (state.captured != null) {
            board.getPieces().add(new ChessPair<>(state.to, state.captured));
        }
    }

    private boolean isInCheck(Board board, Colors color) {
        Position kingPos = findKingPosition(board, color);
        if (kingPos == null) return false;

        for (ChessPair<Position, Piece> pair : board.getPieces()) {
            if (pair.getValue().getColor() != color) {
                if (pair.getValue().checkForCheck(board, kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInCheckmate(Board board, Colors color) {
        if (!isInCheck(board, color)) return false;

        for (ChessPair<Position, Piece> pair : board.getPieces()) {
            if (pair.getValue().getColor() != color) continue;

            Position from = pair.getKey();
            List<Position> moves = pair.getValue().getPossibleMoves(board);

            for (Position to : moves) {
                if (board.isValidMove(from, to)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isGameOver(Board board, Colors color) {
        return isInCheckmate(board, color) || isInCheckmate(board, (color == Colors.WHITE) ? Colors.BLACK : Colors.WHITE);
    }

    private Position findKingPosition(Board board, Colors color) {
        for (ChessPair<Position, Piece> pair : board.getPieces()) {
            Piece p = pair.getValue();
            if (p instanceof King && p.getColor() == color) {
                return pair.getKey();
            }
        }
        return null;
    }

    private static class BestMove {
        Position from;
        Position to;
        int score;
    }

    private static class GameState {
        Piece piece;
        Position from;
        Position to;
        Position oldPosition;
        Piece captured;

        GameState(Piece piece, Position from, Position to, Position oldPos, Piece captured) {
            this.piece = piece;
            this.from = from;
            this.to = to;
            this.oldPosition = oldPos;
            this.captured = captured;
        }
    }
}