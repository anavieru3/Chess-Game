package chess.game;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String email;
    private String password;
    private List<Game> games;
    private int points;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.games = new ArrayList<>();
        this.points = 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void removeGame(Game game) {
        games.remove(game);
    }

    public List<Game> getActiveGames() {
        return games;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return "User{email='" + email + "', points=" + points + ", games=" + games.size() + "}";
    }
}