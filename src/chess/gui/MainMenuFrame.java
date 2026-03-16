package chess.gui;

import chess.Main;
import chess.game.Game;
import chess.game.Player;
import chess.game.ComputerPlayer;
import chess.board.Colors;
import chess.game.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainMenuFrame extends JFrame {

    private Main main;
    private User currentUser;

    public MainMenuFrame() {
        this.main = Main.getInstance();
        this.currentUser = main.getCurrentUser();

        setTitle("Chess - Main Menu");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getEmail());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton newGameButton = new JButton("New Game (vs Computer)");
        newGameButton.setFont(new Font("Arial", Font.PLAIN, 16));
        newGameButton.setBackground(new Color(250, 130, 200));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.addActionListener(e -> handleNewGame());
        buttonPanel.add(newGameButton);

        JButton viewGamesButton = new JButton("View Active Games");
        viewGamesButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewGamesButton.addActionListener(e -> handleViewGames());
        buttonPanel.add(viewGamesButton);

        JButton accountInfoButton = new JButton("Account Info");
        accountInfoButton.setFont(new Font("Arial", Font.PLAIN, 16));
        accountInfoButton.addActionListener(e -> handleAccountInfo());
        buttonPanel.add(accountInfoButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.addActionListener(e -> handleLogout());
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void handleNewGame() {
        String[] options = {"White", "Black"};
        int choice = JOptionPane.showOptionDialog(this, "Choose your color:", "New Game",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == -1) return;

        Colors playerColor = (choice == 0) ? Colors.WHITE : Colors.BLACK;
        String playerName = JOptionPane.showInputDialog(this, "Enter your name:", currentUser.getEmail());

        if (playerName == null || playerName.trim().isEmpty()) return;

        Game game = main.createPlayerVsComputerGame(playerName.trim(), playerColor);
        new GameFrame(game);
        dispose();
    }

    private void handleViewGames() {
        List<Game> games = currentUser.getActiveGames();
        if (games.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No active games!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] gameList = new String[games.size()];
        for (int i = 0; i < games.size(); i++) {
            Game g = games.get(i);
            gameList[i] = "Game #" + g.getId() + " - " + g.getPlayer1().getName() + " vs " + g.getPlayer2().getName();
        }

        String selected = (String) JOptionPane.showInputDialog(this, "Select a game:", "Active Games",
                JOptionPane.QUESTION_MESSAGE, null, gameList, gameList[0]);

        if (selected != null) {
            int index = java.util.Arrays.asList(gameList).indexOf(selected);
            Game game = games.get(index);
            new GameFrame(game);
            dispose();
        }
    }

    private void handleAccountInfo() {
        String info = "Email: " + currentUser.getEmail() + "\n" +
                "Total Points: " + currentUser.getPoints() + "\n" +
                "Active Games: " + currentUser.getActiveGames().size();
        JOptionPane.showMessageDialog(this, info, "Account Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleLogout() {
        main.setCurrentUser(null);
        new LoginFrame();
        dispose();
    }
}