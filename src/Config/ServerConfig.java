package Config;

import java.util.ArrayList;

public class ServerConfig {
    public static ArrayList<Models.Game> Games = new ArrayList<>();
    public static int serverPort;
    public static String dbName = "bomberman";
    public static String dbPW = "";
    public static String dbUser = "";
    public static java.sql.Connection dbConnection;
    public static int currentGameId;
    public static int[] defaultBomberHeight = {32,28,28,32};
    public static int[] defaultBomberWidth = {28,32,32,20};
}
