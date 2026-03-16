package chess.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chess.board.Colors;
import chess.board.Position;
import chess.board.ChessPair;
import chess.game.User;
import chess.game.Game;
import chess.game.Player;
import chess.game.ComputerPlayer;
import chess.pieces.Piece;
import chess.pieces.PieceFactory;

public final class JsonReaderUtil {

    private JsonReaderUtil() {
    }

    public static List<User> readAccounts(Path path) throws IOException, ParseException {
        if (path == null || !Files.exists(path)) {
            return new ArrayList<>();
        }

        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        Object root = parser.parse(reader);
        reader.close();

        JSONArray array = asArray(root);
        List<User> result = new ArrayList<>();
        if (array == null) {
            return result;
        }

        for (Object item : array) {
            JSONObject obj = asObject(item);
            if (obj == null) continue;

            String email = asString(obj.get("email"));
            String password = asString(obj.get("password"));
            User user = new User(email, password);
            user.setPoints(asInt(obj.get("points"), 0));
            result.add(user);
        }
        return result;
    }

    public static Map<Integer, Game> readGamesAsMap(Path path) throws IOException, ParseException {
        Map<Integer, Game> map = new HashMap<>();
        if (path == null || !Files.exists(path)) {
            return map;
        }

        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        Object root = parser.parse(reader);
        reader.close();

        JSONArray array = asArray(root);
        if (array == null) return map;

        for (Object item : array) {
            JSONObject obj = asObject(item);
            if (obj == null) continue;

            int id = asInt(obj.get("id"), -1);
            if (id < 0) continue;

            JSONArray playersArr = asArray(obj.get("players"));
            if (playersArr == null || playersArr.size() < 2) continue;

            JSONObject p1Obj = asObject(playersArr.get(0));
            JSONObject p2Obj = asObject(playersArr.get(1));

            String name1 = asString(p1Obj.get("name"));
            Colors color1 = "WHITE".equals(asString(p1Obj.get("color"))) ? Colors.WHITE : Colors.BLACK;
            boolean isComputer1 = asBoolean(p1Obj.get("isComputer"), false);

            String name2 = asString(p2Obj.get("name"));
            Colors color2 = "WHITE".equals(asString(p2Obj.get("color"))) ? Colors.WHITE : Colors.BLACK;
            boolean isComputer2 = asBoolean(p2Obj.get("isComputer"), false);

            Player player1 = isComputer1 ? new ComputerPlayer(name1, color1) : new Player(name1, color1);
            Player player2 = isComputer2 ? new ComputerPlayer(name2, color2) : new Player(name2, color2);

            Game game = new Game(id, player1, player2);

            if (asString(obj.get("currentPlayerColor")).equals(asString(p2Obj.get("color")))) {
                game.switchPlayer();
            }

            JSONArray boardArr = asArray(obj.get("board"));
            if (boardArr != null) {
                game.getBoard().getPieces().clear();

                for (Object bItem : boardArr) {
                    JSONObject bObj = asObject(bItem);
                    if (bObj == null) continue;

                    String type = asString(bObj.get("type"));
                    String colorStr = asString(bObj.get("color"));
                    String positionStr = asString(bObj.get("position"));

                    Colors color = "WHITE".equals(colorStr) ? Colors.WHITE : Colors.BLACK;
                    Position position = parsePosition(positionStr);

                    if (position != null && type != null && type.length() > 0) {
                        Piece piece = PieceFactory.createPiece(type, color, position);
                        if (piece != null) {
                            game.getBoard().getPieces().add(new ChessPair<>(position, piece));
                        }
                    }
                }
            }

            JSONArray movesArr = asArray(obj.get("moves"));
            if (movesArr != null) {
                for (Object mItem : movesArr) {
                    JSONObject mObj = asObject(mItem);
                    if (mObj == null) continue;

                    Colors moveColor = "WHITE".equals(asString(mObj.get("playerColor"))) ? Colors.WHITE : Colors.BLACK;
                    Position fromPos = parsePosition(asString(mObj.get("from")));
                    Position toPos = parsePosition(asString(mObj.get("to")));

                    Player p = (player1.getColor() == moveColor) ? player1 : player2;
                    game.addMove(p, fromPos, toPos);
                }
            }

            map.put(id, game);
        }
        return map;
    }

    private static Position parsePosition(String posStr) {
        if (posStr == null || posStr.length() != 2) return null;
        char x = posStr.charAt(0);
        int y = Character.getNumericValue(posStr.charAt(1));
        return new Position(x, y);
    }

    private static JSONArray asArray(Object o) {
        return (o instanceof JSONArray) ? (JSONArray) o : null;
    }

    private static JSONObject asObject(Object o) {
        return (o instanceof JSONObject) ? (JSONObject) o : null;
    }

    private static String asString(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private static int asInt(Object o, int def) {
        if (o instanceof Number) return ((Number) o).intValue();
        try {
            return o != null ? Integer.parseInt(String.valueOf(o)) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static boolean asBoolean(Object o, boolean def) {
        if (o instanceof Boolean) return (Boolean) o;
        try {
            return o != null ? Boolean.parseBoolean(String.valueOf(o)) : def;
        } catch (Exception e) {
            return def;
        }
    }
}