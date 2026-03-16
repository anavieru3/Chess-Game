package chess.gui;

import chess.board.Board;
import chess.board.Position;
import chess.pieces.Piece;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class BoardPanel extends JPanel {

    private JButton[][] squares;
    private Board board;
    private Position selectedPosition;
    private List<Position> highlightedMoves;
    private BoardClickListener clickListener;

    public BoardPanel(Board board) {
        this.board = board;
        this.squares = new JButton[8][8];
        this.highlightedMoves = new ArrayList<>();

        setLayout(new GridLayout(8, 8));
        setPreferredSize(new Dimension(560, 560));
        initBoard();
    }

    private void initBoard() {
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setFont(new Font("Arial Unicode MS", Font.PLAIN, 40));
                square.setFocusPainted(false);

                Color lightSquare = new Color(255, 240, 245);
                Color darkSquare = new Color(255, 112, 223);
                square.setBackground((row + col) % 2 == 0 ? lightSquare : darkSquare);

                final int r = row;
                final int c = col;
                square.addActionListener(e -> handleSquareClick(r, c));

                squares[row][col] = square;
                add(square);
            }
        }
        updateBoard();
    }

    public void updateBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                char column = (char) ('A' + col);
                int rowNum = row + 1;
                Position pos = new Position(column, rowNum);

                Piece piece = board.getPieceAt(pos);
                JButton square = squares[row][col];

                if (piece != null) {
                    square.setText(getPieceUnicode(piece));
                } else {
                    square.setText("");
                }

                Color lightSquare = new Color(255, 240, 245);
                Color darkSquare = new Color(255, 112, 223);
                Color highlight = new Color(216, 150, 216, 150);
                Color selected = new Color(210, 21, 133, 150);

                if (selectedPosition != null && selectedPosition.equals(pos)) {
                    square.setBackground(selected);
                } else if (highlightedMoves.contains(pos)) {
                    square.setBackground(highlight);
                } else {
                    square.setBackground((row + col) % 2 == 0 ? lightSquare : darkSquare);
                }
            }
        }
        revalidate();
        repaint();
    }

    private void handleSquareClick(int row, int col) {
        char column = (char) ('A' + col);
        int rowNum = row + 1;
        Position clickedPos = new Position(column, rowNum);

        if (clickListener != null) {
            clickListener.onSquareClicked(clickedPos);
        }
    }

    public void setSelectedPosition(Position pos) {
        this.selectedPosition = pos;
        updateBoard();
    }

    public void setHighlightedMoves(List<Position> moves) {
        this.highlightedMoves = new ArrayList<>(moves);
        updateBoard();
    }

    public void clearSelection() {
        this.selectedPosition = null;
        this.highlightedMoves.clear();
        updateBoard();
    }

    public void setClickListener(BoardClickListener listener) {
        this.clickListener = listener;
    }

    private String getPieceUnicode(Piece piece) {
        boolean isWhite = piece.getColor() == chess.board.Colors.WHITE;
        switch (piece.type()) {
            case 'K': return isWhite ? "♔" : "♚";
            case 'Q': return isWhite ? "♕" : "♛";
            case 'R': return isWhite ? "♖" : "♜";
            case 'B': return isWhite ? "♗" : "♝";
            case 'N': return isWhite ? "♘" : "♞";
            case 'P': return isWhite ? "♙" : "♟";
            default: return "";
        }
    }

    public interface BoardClickListener {
        void onSquareClicked(Position position);
    }
}