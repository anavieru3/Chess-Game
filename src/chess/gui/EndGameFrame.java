package chess.gui;

import chess.Main;
import chess.game.Player;
import javax.swing.*;
import java.awt.*;

public class EndGameFrame extends JFrame {

    public EndGameFrame(Player winner, String reason, int pointsGained) {
        setTitle("Game Over");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents(winner, reason, pointsGained);
        setVisible(true);
    }

    private void initComponents(Player winner, String reason, int pointsGained) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBackground(new Color(240, 240, 240));

        JLabel resultLabel = new JLabel();
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        String reasonText = "";
        switch(reason) {
            case "checkmate":
                resultLabel.setText(winner.getName() + " WINS!");
                resultLabel.setForeground(new Color(0, 128, 0));
                reasonText = "by Checkmate";
                break;
            case "resign":
                resultLabel.setText(winner.getName() + " WINS!");
                resultLabel.setForeground(new Color(0, 128, 0));
                reasonText = "by Resignation";
                break;
            case "draw":
                resultLabel.setText("DRAW!");
                resultLabel.setForeground(new Color(128, 128, 0));
                reasonText = "Equal Position";
                break;
            default:
                resultLabel.setText("GAME OVER");
                break;
        }

        centerPanel.add(resultLabel);

        JLabel reasonLabel = new JLabel(reasonText);
        reasonLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        reasonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(reasonLabel);

        JLabel pointsLabel = new JLabel("Points: " + (pointsGained >= 0 ? "+" : "") + pointsGained);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pointsLabel.setForeground(pointsGained >= 0 ? new Color(0, 128, 0) : Color.RED);
        centerPanel.add(pointsLabel);

        JLabel totalLabel = new JLabel("Total Points: " + Main.getInstance().getCurrentUser().getPoints());
        totalLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(totalLabel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton menuButton = new JButton("Main Menu");
        menuButton.setFont(new Font("Arial", Font.PLAIN, 14));
        menuButton.addActionListener(e -> {
            new MainMenuFrame();
            dispose();
        });
        buttonPanel.add(menuButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }
}