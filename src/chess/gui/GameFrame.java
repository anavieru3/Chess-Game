package chess.gui;

import chess.Main;
import chess.game.Game;
import chess.game.Player;
import chess.board.Position;
import chess.pieces.Piece;
import chess.observer.ScoreObserver;
import chess.observer.MoveHistoryObserver;
import chess.exceptions.InvalidMoveException;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameFrame extends JFrame implements BoardPanel.BoardClickListener {

    private Game game;
    private BoardPanel boardPanel;
    private Position selectedFrom;
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JTextArea movesArea;
    private Main main;

    public GameFrame(Game game) {
        this.game = game;
        this.main = Main.getInstance();

        game.addObserver(new ScoreObserver());
        game.addObserver(new MoveHistoryObserver());

        setTitle("Chess Game - " + game.getPlayer1().getName() + " vs " + game.getPlayer2().getName());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        game.start();
        updateStatus();

        if (game.isComputerTurn()) {
            processComputerMove();
        }

        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        boardPanel = new BoardPanel(game.getBoard());
        boardPanel.setClickListener(this);
        add(boardPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 700));
        rightPanel.setBackground(new Color(240, 240, 240));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.setBackground(new Color(240, 240, 240));

        statusLabel = new JLabel("Turn: " + game.getCurrentPlayer().getName());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(statusLabel);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(scoreLabel);

        rightPanel.add(infoPanel, BorderLayout.NORTH);

        movesArea = new JTextArea();
        movesArea.setEditable(false);
        movesArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(movesArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Moves"));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton resignButton = new JButton("Resign");
        resignButton.addActionListener(e -> handleResign());
        buttonPanel.add(resignButton);

        JButton saveButton = new JButton("Save & Exit");
        saveButton.addActionListener(e -> handleSaveExit());
        buttonPanel.add(saveButton);

        JButton menuButton = new JButton("Main Menu");
        menuButton.addActionListener(e -> handleMainMenu());
        buttonPanel.add(menuButton);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);
    }

    @Override
    public void onSquareClicked(Position position) {
        if (game.isComputerTurn()) {
            return;
        }

        Player currentPlayer = game.getCurrentPlayer();
        Piece clickedPiece = game.getBoard().getPieceAt(position);

        if (selectedFrom == null) {
            if (clickedPiece != null && clickedPiece.getColor() == currentPlayer.getColor()) {
                selectedFrom = position;
                boardPanel.setSelectedPosition(position);

                List<Position> moves = clickedPiece.getPossibleMoves(game.getBoard());
                List<Position> validMoves = new java.util.ArrayList<>();
                for (Position move : moves) {
                    if (game.getBoard().isValidMove(position, move)) {
                        validMoves.add(move);
                    }
                }
                boardPanel.setHighlightedMoves(validMoves);
            }
        } else {
            if (position.equals(selectedFrom)) {
                boardPanel.clearSelection();
                selectedFrom = null;
            } else {
                try {
                    currentPlayer.makeMove(selectedFrom, position, game.getBoard());
                    game.addMove(currentPlayer, selectedFrom, position);

                    boardPanel.clearSelection();
                    boardPanel.updateBoard();
                    selectedFrom = null;

                    updateMoves();
                    updateScore();

                    if (game.checkForCheckMate()) {
                        handleGameEnd(currentPlayer, "checkmate");
                        return;
                    }

                    game.switchPlayer();
                    updateStatus();

                    if (game.isComputerTurn()) {
                        processComputerMove();
                    }

                } catch (InvalidMoveException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid move!", "Error", JOptionPane.ERROR_MESSAGE);
                    boardPanel.clearSelection();
                    selectedFrom = null;
                }
            }
        }
    }

    private void processComputerMove() {
        SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            boolean moved = game.processComputerMove();
            if (!moved) {
                handleGameEnd(game.getOpponentPlayer(), "opponent_no_moves");
                return;
            }

            boardPanel.updateBoard();
            updateMoves();
            updateScore();

            if (game.checkForCheckMate()) {
                handleGameEnd(game.getCurrentPlayer(), "checkmate");
                return;
            }

            game.switchPlayer();
            updateStatus();
        });
    }

    private void updateStatus() {
        statusLabel.setText("Turn: " + game.getCurrentPlayer().getName() +
                " (" + game.getCurrentPlayer().getColor() + ")");

        if (game.isKingInCheck(game.getCurrentPlayer().getColor())) {
            statusLabel.setText(statusLabel.getText() + " - CHECK!");
            statusLabel.setForeground(Color.RED);
        } else {
            statusLabel.setForeground(Color.BLACK);
        }
    }

    private void updateScore() {
        int p1Score = game.getPlayer1().getPoints();
        int p2Score = game.getPlayer2().getPoints();
        scoreLabel.setText("Score: " + game.getPlayer1().getName() + ": " + p1Score +
                " | " + game.getPlayer2().getName() + ": " + p2Score);
    }

    private void updateMoves() {
        StringBuilder sb = new StringBuilder();
        List<chess.game.Move> moves = game.getMoves();
        for (int i = 0; i < moves.size(); i++) {
            sb.append((i + 1)).append(". ").append(moves.get(i)).append("\n");
        }
        movesArea.setText(sb.toString());
    }

    private void handleGameEnd(Player winner, String reason) {
        game.endGame(winner, reason);

        Player humanPlayer = game.getPlayer1().isComputer() ? game.getPlayer2() : game.getPlayer1();
        int gamePoints = humanPlayer.getPoints();

        if (reason.equals("checkmate")) {
            if (winner == humanPlayer) {
                gamePoints += 300;
                main.getCurrentUser().setPoints(main.getCurrentUser().getPoints() + gamePoints);
            } else {
                gamePoints -= 300;
                main.getCurrentUser().setPoints(main.getCurrentUser().getPoints() + gamePoints);
            }
        }

        main.getCurrentUser().removeGame(game);
        new EndGameFrame(winner, reason, gamePoints);
        dispose();
    }

    private void handleResign() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to resign?",
                "Resign", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            handleGameEnd(game.getOpponentPlayer(), "resign");
        }
    }

    private void handleSaveExit() {
        JOptionPane.showMessageDialog(this, "Game saved!", "Info", JOptionPane.INFORMATION_MESSAGE);
        new MainMenuFrame();
        dispose();
    }

    private void handleMainMenu() {
        new MainMenuFrame();
        dispose();
    }
}