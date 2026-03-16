package chess.game;

import chess.board.Board;
import chess.board.ChessPair;
import chess.board.Colors;
import chess.board.Position;
import chess.pieces.King;
import chess.pieces.Piece;
import chess.observer.GameObserver;
import chess.game.ComputerPlayer;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private int id;
    private Board board;
    private Player player1;
    private Player player2;
    private List<Move> moves;
    private int currentPlayerIndex;

    private List<GameObserver> observers;
    private boolean isPlayerVsComputer;

    public Game(int id, Player player1, Player player2) {
        this.id = id;
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.moves = new ArrayList<>();
        this.currentPlayerIndex = 0;

        this.observers = new ArrayList<>();
        this.isPlayerVsComputer = (player2 instanceof ComputerPlayer);
    }

    public int getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Player getCurrentPlayer() {
        return currentPlayerIndex == 0 ? player1 : player2;
    }

    public Player getOpponentPlayer() {
        return currentPlayerIndex == 0 ? player2 : player1;
    }

    public boolean isPlayerVsComputer() {
        return isPlayerVsComputer;
    }

    public boolean isComputerTurn() {
        return isPlayerVsComputer && (getCurrentPlayer() instanceof ComputerPlayer);
    }

    public void start() {
        board.initialize();
        moves.clear();
        currentPlayerIndex = 0;

        notifyPlayerSwitch(getCurrentPlayer());
    }

    public void resume() {
        notifyPlayerSwitch(getCurrentPlayer());
    }

    public void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;

        notifyPlayerSwitch(getCurrentPlayer());
    }

    public boolean checkForCheckMate() {
        Player current = getCurrentPlayer();
        Colors culoare = current.getColor();

        Position kingPos = findKingPosition(culoare);
        if(kingPos == null) return false;

        if(!isKingInCheck(culoare)) {
            return false;
        }

        for(ChessPair<Position, Piece> pair : board.getPieces()) {
            Piece piece = pair.getValue();
            if(piece.getColor() == culoare) {
                Position from = pair.getKey();
                List<Position> possibleMoves = piece.getPossibleMoves(board);

                for(Position to : possibleMoves) {
                    if(board.isValidMove(from, to)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void addMove(Player p, Position from, Position to) {
        Piece captured = board.getPieceAt(to);
        Move move = new Move(p.getColor(), from, to, captured);
        moves.add(move);

        notifyMoveMade(move);

        if(captured != null)
            notifyPieceCaptured(captured, p);
    }

    private Position findKingPosition(Colors color) {
        for(ChessPair<Position, Piece> pair : board.getPieces()) {
            Piece p = pair.getValue();
            if(p instanceof King && p.getColor() == color) {
                return pair.getKey();
            }
        }
        return null;
    }

    public boolean isKingInCheck(Colors kingColor) {
        Position kingPos = findKingPosition(kingColor);
        if(kingPos == null) return false;

        for(ChessPair<Position, Piece> pair : board.getPieces()) {
            Piece p = pair.getValue();
            if(p.getColor() != kingColor) {
                if(p.checkForCheck(board, kingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyMoveMade(Move move) {
        for (GameObserver observer : observers) {
            observer.moveMade(move);
        }
    }

    private void notifyPieceCaptured(Piece piece, Player capturedBy) {
        for (GameObserver observer : observers) {
            observer.pieceCaptured(piece, capturedBy);
        }
    }

    private void notifyPlayerSwitch(Player currentPlayer) {
        for (GameObserver observer : observers) {
            observer.playerSwitch(currentPlayer);
        }
    }

    private void notifyGameOver(Player winner, String reason) {
        for (GameObserver observer : observers) {
            observer.gameOver(winner, reason);
        }
    }

    public void endGame(Player winner, String reason) {
        notifyGameOver(winner, reason);
    }

    public boolean processComputerMove() {
        if (!isComputerTurn()) {
            return false;
        }

        ComputerPlayer computer = (ComputerPlayer) getCurrentPlayer();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return computer.makeRandomMove(board);
    }
}