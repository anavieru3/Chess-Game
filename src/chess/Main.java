package chess;

import chess.board.Colors;
import chess.board.Position;
import chess.game.User;
import chess.game.Game;
import chess.game.Player;
import chess.game.ComputerPlayer;
import chess.json.JsonReaderUtil;
import chess.exceptions.InvalidMoveException;

import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static Main instance;

    private List<User> users;
    private Map<Integer, Game> games;
    private User currentUser;
    private final Scanner scanner;
    private int nextGameId;

    private Main() {
        users = new ArrayList<>();
        games = new HashMap<>();
        scanner = new Scanner(System.in);
        nextGameId = 1;
    }

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    public void read() {
        try {
            Path accountsPath = Paths.get("accounts.json");
            users = JsonReaderUtil.readAccounts(accountsPath);

            Path gamesPath = Paths.get("games.json");
            games = JsonReaderUtil.readGamesAsMap(gamesPath);

            for (Integer id : games.keySet()) {
                nextGameId = Math.max(nextGameId, id + 1);
            }

            System.out.println("Date încărcate cu succes!");
        } catch (Exception e) {
            System.out.println("Eroare citire fișiere: " + e.getMessage());
        }
    }

    public void write() {
        System.out.println("Salvare date (neimplementată).");
    }

    public User login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                currentUser = u;
                return u;
            }
        }
        return null;
    }

    public User newAccount(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                System.out.println("Email deja existent!");
                return null;
            }
        }
        User u = new User(email, password);
        users.add(u);
        currentUser = u;
        return u;
    }

    public Game createPlayerVsComputerGame(String playerName, Colors playerColor) {
        Player humanPlayer = new Player(playerName, playerColor);

        Colors computerColor = (playerColor == Colors.WHITE) ? Colors.BLACK : Colors.WHITE;
        ComputerPlayer computer = new ComputerPlayer("Computer", computerColor);

        Player player1, player2;
        if (playerColor == Colors.WHITE) {
            player1 = humanPlayer;
            player2 = computer;
        } else {
            player1 = computer;
            player2 = humanPlayer;
        }

        Game game = new Game(nextGameId++, player1, player2);
        games.put(game.getId(), game);

        if (currentUser != null) {
            currentUser.addGame(game);
        }

        return game;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public Map<Integer, Game> getGames() {
        return games;
    }

    public void run() {
        System.out.println("=== CHESS ===");

        while (currentUser == null) {
            System.out.println("\n1. Login");
            System.out.println("2. Cont nou");
            System.out.println("3. Ieșire");
            System.out.print("Alegere: ");

            String c = scanner.nextLine();

            if (c.equals("1")) {
                System.out.print("Email: ");
                String e = scanner.nextLine();
                System.out.print("Parolă: ");
                String p = scanner.nextLine();
                if (login(e, p) == null)
                    System.out.println("Date invalide!");
            } else if (c.equals("2")) {
                System.out.print("Email: ");
                String e = scanner.nextLine();
                System.out.print("Parolă: ");
                String p = scanner.nextLine();
                newAccount(e, p);
            } else {
                return;
            }
        }

        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\n=== MENIU PRINCIPAL ===");
            System.out.println("1. Joc nou ");
            System.out.println("2. Jocuri active");
            System.out.println("3. Logout");
            System.out.print("Alegere: ");

            String c = scanner.nextLine();

            if (c.equals("1")) startNewGame();
            else if (c.equals("2")) viewGames();
            else break;
        }
    }

    private void startNewGame() {
        System.out.print("Nume jucător ALB: ");
        Player p1 = new Player(scanner.nextLine(), Colors.WHITE);

        System.out.print("Nume jucător NEGRU: ");
        Player p2 = new Player(scanner.nextLine(), Colors.BLACK);

        Game game = new Game(nextGameId++, p1, p2);
        games.put(game.getId(), game);
        currentUser.addGame(game);

        game.start();
        playGame(game);
    }

    private void viewGames() {
        List<Game> list = currentUser.getActiveGames();
        if (list.isEmpty()) {
            System.out.println("Nu ai jocuri active.");
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". Joc ID " + list.get(i).getId());
        }

        System.out.print("Selectează (0 = back): ");
        int c = Integer.parseInt(scanner.nextLine());
        if (c > 0) playGame(list.get(c - 1));
    }

    private void playGame(Game game) {
        while (true) {
            displayBoard(game);
            Player current = game.getCurrentPlayer();

            System.out.println("\nRândul lui " + current.getName() + " (" + current.getColor() + ")");
            System.out.print("Mutare: ");

            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("IESIRE"))
                return;

            try {
                String[] p = input.split("-");
                char fromCol = Character.toUpperCase(p[0].charAt(0));
                int fromRow = Character.getNumericValue(p[0].charAt(1));

                char toCol = Character.toUpperCase(p[1].charAt(0));
                int toRow = Character.getNumericValue(p[1].charAt(1));

                Position from = new Position(fromCol, fromRow);
                Position to = new Position(toCol, toRow);

                current.makeMove(from, to, game.getBoard());
                game.addMove(current, from, to);

                if (game.checkForCheckMate()) {
                    System.out.println("ȘAH MAT! Câștigă " + current.getName());
                    currentUser.removeGame(game);
                    return;
                }

                game.switchPlayer();

            } catch (InvalidMoveException | RuntimeException e) {
                System.out.println("Mutare invalidă!");
            }
        }
    }

    private void displayBoard(Game game) {
        System.out.println("\n  A B C D E F G H");
        for (int r = 8; r >= 1; r--) {
            System.out.print(r + " ");
            for (char c = 'A'; c <= 'H'; c++) {
                var piece = game.getBoard().getPieceAt(new Position(c, r));
                System.out.print((piece == null ? "." : piece.type()) + " ");
            }
            System.out.println(r);
        }
        System.out.println("  A B C D E F G H");
    }

    public static void main(String[] args) {
        Main m = Main.getInstance();
        m.read();

        javax.swing.SwingUtilities.invokeLater(() -> {
            new chess.gui.LoginFrame();
        });
    }
}