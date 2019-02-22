package Utilies;

import Config.ServerConfig;
import Models.*;
import javafx.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.DriverManager;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;

public class ServerEngine {

    public static Models.BomberGuy findBomberGuyById(long id) {
        try {
            for (Models.BomberGuy bomberGuy : ServerConfig.Games.get(ServerConfig.currentGameId).bomberGuys)
                if (bomberGuy.getId() == id)
                    return bomberGuy;
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void endGame() {
        addUpdate("Game", 0, "End", "");
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                ServerConfig.Games.get(ServerConfig.currentGameId).gameTitle = "Closed";
                ServerConfig.Games.get(ServerConfig.currentGameId).bombs = new ArrayList<>();
                ServerConfig.Games.get(ServerConfig.currentGameId).powerUps = new ArrayList<>();
                ServerConfig.Games.get(ServerConfig.currentGameId).hunters = new ArrayList<>();
                ServerConfig.Games.get(ServerConfig.currentGameId).chats = new ArrayList<>();
                ServerConfig.Games.get(ServerConfig.currentGameId).obstacles = new ArrayList<>();
                ServerConfig.Games.get(ServerConfig.currentGameId).updates = new ArrayList<>();
            }
        }, 5000);
    }

    public static JSONObject getScores() throws JSONException {
        JSONObject scores = new JSONObject();
        for (Game game : ServerConfig.Games) {
            JSONObject gameJson = new JSONObject();
            for (BomberGuy bomberGuy : game.bomberGuys) {
                gameJson.put(bomberGuy.getName(), bomberGuy.getScore());
            }
            scores.put(game.gameTitle, gameJson);
        }
        return scores;
    }

    public static Models.Bomb findBombById(long id) {
        try {
            for (Models.Bomb bomb : ServerConfig.Games.get(ServerConfig.currentGameId).bombs)
                if (bomb.getId() == id)
                    return bomb;
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Models.Obstacle findObstacleById(long id) {
        try {
            for (Models.Obstacle obstacle : ServerConfig.Games.get(ServerConfig.currentGameId).obstacles)
                if (obstacle.getId() == id)
                    return obstacle;
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PowerUp findPowerUpByLocation(int x, int y) {
        for (PowerUp powerUp : ServerConfig.Games.get(ServerConfig.currentGameId).powerUps) {
            if (powerUp.getCellX() == x && powerUp.getCellY() == y && !powerUp.isUsed())
                return powerUp;
        }
        return null;
    }

    public static boolean sentChat(JSONObject request, int BomberId) {
        try {

            Chat chat = new Chat(ServerConfig.Games.get(ServerConfig.currentGameId).chats.size());
            chat.setText(request.get("Text").toString());
            chat.setTimeSent(LocalDateTime.parse(request.get("Time").toString()));
            chat.setSenderId(BomberId);
            addUpdate("Chat", chat.getId(), "Text", findBomberGuyById(BomberId).getName() + " : " + chat.getText());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized static boolean keyPressed(JSONObject request, int BomberId) {
        try {
            String Key = request.get("Key").toString();
            Models.BomberGuy bomberGuy = findBomberGuyById(BomberId);

            switch (Key) {
            case "KillAllHunters":
                if (!ServerConfig.Games.get(ServerConfig.currentGameId).isPaused)
                    for (Hunter hunter : ServerConfig.Games.get(ServerConfig.currentGameId).hunters) {
                        hunterManDie(hunter);
                    }
                levelUp();
                break;
            case "Explode":
                if (!ServerConfig.Games.get(ServerConfig.currentGameId).isPaused) {
                    assert bomberGuy != null;
                    if (bomberGuy.bombsInControlId.size() > 0) {
                        (new BombDemolitionThread(
                                bomberGuy.bombsInControlId.get(bomberGuy.bombsInControlId.size() - 1))).start();
                        bomberGuy.bombsInControlId.remove(bomberGuy.bombsInControlId.size() - 1);
                    }
                }
                break;
            case "Up":
                if (!ServerConfig.Games.get(ServerConfig.currentGameId).isPaused)
                    bomberManDoMove(bomberGuy, Direction.Up);
                break;
            case "Left":
                if (!ServerConfig.Games.get(ServerConfig.currentGameId).isPaused)
                    bomberManDoMove(bomberGuy, Direction.Left);
                break;
            case "Right":
                if (!ServerConfig.Games.get(ServerConfig.currentGameId).isPaused)
                    bomberManDoMove(bomberGuy, Direction.Right);
                break;
            case "Down":
                if (!ServerConfig.Games.get(ServerConfig.currentGameId).isPaused)
                    bomberManDoMove(bomberGuy, Direction.Down);
                break;
            case "Plant":
                if (!ServerConfig.Games.get(ServerConfig.currentGameId).isPaused)
                    ServerConfig.Games.get(ServerConfig.currentGameId).bombs.add(plant(bomberGuy));
                break;
            case "Save":
                saveGame(ServerConfig.currentGameId, request.get("Path").toString());
                break;
            case "Load":
                break;
            case "PauseOrResume":
                if (ServerConfig.Games.get(ServerConfig.currentGameId).isPaused) {
                    if (ServerConfig.Games.get(ServerConfig.currentGameId).pauseTime == null) {
                        resume();
                        return true;
                    }
                    if (ServerConfig.Games.get(ServerConfig.currentGameId).pauseOwnerId == BomberId || LocalDateTime
                            .now().isAfter(ServerConfig.Games.get(ServerConfig.currentGameId).pauseTime.plusSeconds(
                                    ServerConfig.Games.get(ServerConfig.currentGameId).defaultPauseWaitTime)))
                        resume();
                } else {
                    pause(BomberId);
                }
                break;
            case "Pause":
                if (!ServerConfig.Games.get(ServerConfig.currentGameId).isPaused)
                    pause(BomberId);
                break;
            case "Quit":
                bomberManLeave(bomberGuy);
                break;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Models.Bomb plant(BomberGuy bomberGuy) throws Exception {
        if (bomberGuy.getPlantedBombsCount() >= bomberGuy.getMaxBombsCount())
            throw new Exception("Max bomb count exceeded");
        if (ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                + (bomberGuy.getBomberWidth()) / 2)
                / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize][(bomberGuy.getLocation().getY()
                        + (bomberGuy.getBomberHeight()) / 2)
                        / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize] == BlocksState.Bomb)
            throw new Exception("Already bombed");
        Models.Bomb bomb = new Models.Bomb(ServerConfig.Games.get(ServerConfig.currentGameId).bombs.size() + 1,
                ((bomberGuy.getLocation().getX() + (bomberGuy.getBomberWidth()) / 2)
                        / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize),
                ((bomberGuy.getLocation().getY() + (bomberGuy.getBomberHeight()) / 2)
                        / ServerConfig.Games.get(ServerConfig.currentGameId).cellSize),
                bomberGuy.getId());
        if (bomberGuy.isBombControl()) {
            bomberGuy.bombsInControlId.add(bomb.getId());
            bomb.setControlled(true);
        }
        addUpdate("Bomb", bomb.getId());
        addUpdate("Bomb", bomb.getId(), "cellX", String.valueOf(bomb.getCellX()));
        addUpdate("Bomb", bomb.getId(), "cellY", String.valueOf(bomb.getCellY()));
        addUpdate("Bomb", bomb.getId(), "destructionState", String.valueOf(bomb.getDestructionState()));
        addUpdate("Bomb", bomb.getId(), "bombExplodingRange", String.valueOf(bomb.getBombExplodingRange()));
        addUpdate("Bomb", bomb.getId(), "destructionTime",
                bomb.getTimePlaced().plusSeconds(bomb.getBombDestructionTime()).atZone(ZoneOffset.UTC).toString());
        return bomb;
    }

    public static void pause(int BomberId) {
        try {
            ServerConfig.Games.get(ServerConfig.currentGameId).pauseTime = LocalDateTime.now();
            ServerConfig.Games.get(ServerConfig.currentGameId).isPaused = true;
            ServerConfig.Games.get(ServerConfig.currentGameId).pauseOwnerId = BomberId;
            addUpdate("Game", 0, "isPaused",
                    String.valueOf(ServerConfig.Games.get(ServerConfig.currentGameId).isPaused));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resume() {
        try {
            Duration duration = Duration.between(ServerConfig.Games.get(ServerConfig.currentGameId).pauseTime,
                    LocalDateTime.now());

            for (Models.Bomb bomb : ServerConfig.Games.get(ServerConfig.currentGameId).bombs)
                bomb.addDelayForPause(duration.getSeconds());

            ServerConfig.Games.get(ServerConfig.currentGameId).isPaused = false;
            ServerConfig.Games.get(ServerConfig.currentGameId).pauseTime = null;
            addUpdate("Game", 0, "isPaused",
                    String.valueOf(ServerConfig.Games.get(ServerConfig.currentGameId).isPaused));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bomberManLeave(BomberGuy bomberGuy) {
        bomberGuy.setAlive(false);
        addUpdate("BomberGuy", bomberGuy.getId(), "isAlive", String.valueOf(bomberGuy.isAlive()));
        if (ServerConfig.Games.get(ServerConfig.currentGameId).pauseOwnerId == bomberGuy.getId()) {
            ServerConfig.Games.get(ServerConfig.currentGameId).isPaused = false;
            addUpdate("Game", 0, "isPaused",
                    String.valueOf(ServerConfig.Games.get(ServerConfig.currentGameId).isPaused));
        }
    }

    public static boolean levelUp() {
        for (Hunter hunter : ServerConfig.Games.get(ServerConfig.currentGameId).hunters) {
            if (hunter.isAlive())
                return false;
        }
        ServerConfig.Games.get(ServerConfig.currentGameId).level++;
        addUpdate("Game", 0, "levelUp", String.valueOf(ServerConfig.Games.get(ServerConfig.currentGameId).level));
        Game serverConfig = ServerConfig.Games.get(ServerConfig.currentGameId);
        serverConfig.bombs = new ArrayList<>();
        serverConfig.powerUps = new ArrayList<>();
        serverConfig.hunters = new ArrayList<>();
        serverConfig.chats = new ArrayList<>();
        serverConfig.obstacles = new ArrayList<>();
        serverConfig.blocksState = new BlocksState[serverConfig.cellCountX][serverConfig.cellCountY];

        for (int i = 0; i < ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX; i++)
            for (int j = 0; j < ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY; j++)
                if ((i % 2 == 0 && j % 2 == 0) || i == 0 || j == 0
                        || i == ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX - 1
                        || j == ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY - 1)
                    ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[i][j] = BlocksState.Wall;
                else
                    ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[i][j] = BlocksState.Grass;

        int obstacleCount = (new Random()).nextInt((ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX
                * ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY) / 8);
        addObstacle(true);
        for (int i = 0; i < obstacleCount - 1; i++)
            addObstacle(false);
        int hunterCount = (new Random()).nextInt((ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX
                * ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY) / 8);
        for (int i = 0; i < hunterCount; i++) {
            if (ServerConfig.Games.get(ServerConfig.currentGameId).newHunterClass != null) {
                if (ServerConfig.Games.get(ServerConfig.currentGameId).newHunterClassLevel >= ServerConfig.Games
                        .get(ServerConfig.currentGameId).level) {
                    if ((new Random()).nextInt(100) > 70) {
                        addHunter(5);
                        continue;
                    }
                }
            }
            addHunter(((new Random()).nextInt(Math.min(4, ServerConfig.Games.get(ServerConfig.currentGameId).level)))
                    + 1);

        }
        return true;
    }

    public static void bomberManDoMove(BomberGuy bomberGuy, Direction moveDirection) {
        if (!bomberGuy.isAlive())
            return;
        if (moveDirection == bomberGuy.getCurrentDirection()) {
            bomberGuy.setMoveAnimationState((bomberGuy.getMoveAnimationState() + 1) % 4);
        } else {
            bomberGuy.setCurrentDirection(moveDirection);
            bomberGuy.setMoveAnimationState(0);
        }
        BlocksState first;
        BlocksState second;
        int cellSize = ServerConfig.Games.get(ServerConfig.currentGameId).cellSize;
        synchronized (ServerConfig.Games.get(ServerConfig.currentGameId).blocksStateLock) {
            if (bomberGuy.isGhost()) {
                switch (moveDirection) {
                case Left:
                    first = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    - bomberGuy.getBomberSpeed()) / cellSize][(bomberGuy.getLocation().getY())
                                            / cellSize];
                    second = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    - bomberGuy.getBomberSpeed())
                                    / cellSize][(bomberGuy.getLocation().getY() + bomberGuy.getBomberHeight())
                                            / cellSize];
                    if ((first == BlocksState.Door) || (second == BlocksState.Door)) {
                        if (levelUp())
                            return;
                    }
                    bomberGuy.getLocation().setX(bomberGuy.getLocation().getX() - bomberGuy.getBomberSpeed());
                    if (first == BlocksState.PowerUp || second == BlocksState.PowerUp) {
                        usePowerUp(findPowerUpByLocation(bomberGuy.getLocation().getX() / cellSize,
                                bomberGuy.getLocation().getY() / cellSize), bomberGuy);
                    }
                    break;
                case Right:
                    first = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    + bomberGuy.getBomberWidth() + bomberGuy.getBomberSpeed())
                                    / cellSize][(bomberGuy.getLocation().getY()) / cellSize];
                    second = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    + bomberGuy.getBomberWidth() + bomberGuy.getBomberSpeed())
                                    / cellSize][(bomberGuy.getLocation().getY() + bomberGuy.getBomberHeight())
                                            / cellSize];
                    if ((first == BlocksState.Door) || (second == BlocksState.Door)) {
                        if (levelUp())
                            return;
                    }
                    bomberGuy.getLocation().setX(bomberGuy.getLocation().getX() + bomberGuy.getBomberSpeed());
                    if (first == BlocksState.PowerUp || second == BlocksState.PowerUp) {
                        usePowerUp(findPowerUpByLocation(bomberGuy.getLocation().getX() / cellSize,
                                bomberGuy.getLocation().getY() / cellSize), bomberGuy);
                    }
                    break;
                case Up:
                    first = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX())
                                    / cellSize][(bomberGuy.getLocation().getY() - bomberGuy.getBomberSpeed())
                                            / cellSize];
                    second = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    + bomberGuy.getBomberWidth())
                                    / cellSize][(bomberGuy.getLocation().getY() - bomberGuy.getBomberSpeed())
                                            / cellSize];
                    if ((first == BlocksState.Door) || (second == BlocksState.Door)) {
                        if (levelUp())
                            return;
                    }
                    bomberGuy.getLocation().setY(bomberGuy.getLocation().getY() - bomberGuy.getBomberSpeed());
                    if (first == BlocksState.PowerUp || second == BlocksState.PowerUp) {
                        usePowerUp(findPowerUpByLocation(bomberGuy.getLocation().getX() / cellSize,
                                bomberGuy.getLocation().getY() / cellSize), bomberGuy);
                    }
                    break;
                case Down:
                    first = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX())
                                    / cellSize][(bomberGuy.getLocation().getY() + bomberGuy.getBomberHeight()
                                            + bomberGuy.getBomberSpeed()) / cellSize];
                    second = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    + bomberGuy.getBomberWidth())
                                    / cellSize][(bomberGuy.getLocation().getY() + bomberGuy.getBomberHeight()
                                            + bomberGuy.getBomberSpeed()) / cellSize];
                    if ((first == BlocksState.Door) || (second == BlocksState.Door)) {
                        if (levelUp())
                            return;
                    }
                    bomberGuy.getLocation().setY(bomberGuy.getLocation().getY() + bomberGuy.getBomberSpeed());
                    if (first == BlocksState.PowerUp || second == BlocksState.PowerUp) {
                        usePowerUp(findPowerUpByLocation(bomberGuy.getLocation().getX() / cellSize,
                                bomberGuy.getLocation().getY() / cellSize), bomberGuy);
                    }
                    break;
                }
            } else {
                switch (moveDirection) {
                case Left:
                    first = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    - bomberGuy.getBomberSpeed()) / cellSize][(bomberGuy.getLocation().getY())
                                            / cellSize];
                    second = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    - bomberGuy.getBomberSpeed())
                                    / cellSize][(bomberGuy.getLocation().getY() + bomberGuy.getBomberHeight())
                                            / cellSize];
                    if ((first == BlocksState.Door) || (second == BlocksState.Door)) {
                        if (levelUp())
                            return;
                    }
                    if ((first == BlocksState.Grass) && (second == BlocksState.Grass)) {
                        bomberGuy.getLocation().setX(bomberGuy.getLocation().getX() - bomberGuy.getBomberSpeed());
                    } else if ((first == BlocksState.PowerUp || first == BlocksState.Grass)
                            && (second == BlocksState.PowerUp || second == BlocksState.Grass)) {
                        bomberGuy.getLocation().setX(bomberGuy.getLocation().getX() - bomberGuy.getBomberSpeed());
                        usePowerUp(findPowerUpByLocation(bomberGuy.getLocation().getX() / cellSize,
                                bomberGuy.getLocation().getY() / cellSize), bomberGuy);
                    } else {
                        bomberGuy.getLocation().setX(((bomberGuy.getLocation().getX()) / cellSize) * cellSize);
                    }
                    break;
                case Right:
                    first = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    + bomberGuy.getBomberWidth() + bomberGuy.getBomberSpeed())
                                    / cellSize][(bomberGuy.getLocation().getY()) / cellSize];
                    second = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    + bomberGuy.getBomberWidth() + bomberGuy.getBomberSpeed())
                                    / cellSize][(bomberGuy.getLocation().getY() + bomberGuy.getBomberHeight())
                                            / cellSize];
                    if ((first == BlocksState.Door) || (second == BlocksState.Door)) {
                        if (levelUp())
                            return;
                    }
                    if ((first == BlocksState.Grass) && (second == BlocksState.Grass)) {
                        bomberGuy.getLocation().setX(bomberGuy.getLocation().getX() + bomberGuy.getBomberSpeed());
                    } else if ((first == BlocksState.PowerUp || first == BlocksState.Grass)
                            && (second == BlocksState.PowerUp || second == BlocksState.Grass)) {
                        bomberGuy.getLocation().setX(bomberGuy.getLocation().getX() + bomberGuy.getBomberSpeed());
                        usePowerUp(findPowerUpByLocation(bomberGuy.getLocation().getX() / cellSize,
                                bomberGuy.getLocation().getY() / cellSize), bomberGuy);
                    } else {
                        bomberGuy.getLocation().setX((((bomberGuy.getLocation().getX()) / cellSize) + 1) * cellSize
                                - bomberGuy.getBomberWidth());
                    }
                    break;
                case Up:
                    first = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX())
                                    / cellSize][(bomberGuy.getLocation().getY() - bomberGuy.getBomberSpeed())
                                            / cellSize];
                    second = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    + bomberGuy.getBomberWidth())
                                    / cellSize][(bomberGuy.getLocation().getY() - bomberGuy.getBomberSpeed())
                                            / cellSize];
                    if ((first == BlocksState.Door) || (second == BlocksState.Door)) {
                        if (levelUp())
                            return;
                    }
                    if ((first == BlocksState.Grass) && (second == BlocksState.Grass)) {
                        bomberGuy.getLocation().setY(bomberGuy.getLocation().getY() - bomberGuy.getBomberSpeed());
                    } else if ((first == BlocksState.PowerUp || first == BlocksState.Grass)
                            && (second == BlocksState.PowerUp || second == BlocksState.Grass)) {
                        bomberGuy.getLocation().setY(bomberGuy.getLocation().getY() - bomberGuy.getBomberSpeed());
                        usePowerUp(findPowerUpByLocation(bomberGuy.getLocation().getX() / cellSize,
                                bomberGuy.getLocation().getY() / cellSize), bomberGuy);
                    } else {
                        bomberGuy.getLocation().setY(((bomberGuy.getLocation().getY()) / cellSize) * cellSize);
                    }
                    break;
                case Down:
                    first = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX())
                                    / cellSize][(bomberGuy.getLocation().getY() + bomberGuy.getBomberHeight()
                                            + bomberGuy.getBomberSpeed()) / cellSize];
                    second = ServerConfig.Games
                            .get(ServerConfig.currentGameId).blocksState[(bomberGuy.getLocation().getX()
                                    + bomberGuy.getBomberWidth())
                                    / cellSize][(bomberGuy.getLocation().getY() + bomberGuy.getBomberHeight()
                                            + bomberGuy.getBomberSpeed()) / cellSize];
                    if ((first == BlocksState.Door) || (second == BlocksState.Door)) {
                        if (levelUp())
                            return;
                    }
                    if ((first == BlocksState.Grass) && (second == BlocksState.Grass)) {
                        bomberGuy.getLocation().setY(bomberGuy.getLocation().getY() + bomberGuy.getBomberSpeed());
                    } else if ((first == BlocksState.PowerUp || first == BlocksState.Grass)
                            && (second == BlocksState.PowerUp || second == BlocksState.Grass)) {
                        bomberGuy.getLocation().setY(bomberGuy.getLocation().getY() + bomberGuy.getBomberSpeed());
                        usePowerUp(findPowerUpByLocation(bomberGuy.getLocation().getX() / cellSize,
                                bomberGuy.getLocation().getY() / cellSize), bomberGuy);
                    } else {
                        bomberGuy.getLocation().setY((((bomberGuy.getLocation().getY()) / cellSize) + 1) * cellSize
                                - bomberGuy.getBomberHeight());
                    }
                    break;
                }
            }
        }
        addUpdate("BomberGuy", bomberGuy.getId(), "currentDirection", String.valueOf(bomberGuy.getCurrentDirection()));
        addUpdate("BomberGuy", bomberGuy.getId(), "moveAnimationState",
                String.valueOf(bomberGuy.getMoveAnimationState()));
        addUpdate("BomberGuy", bomberGuy.getId(), "location",
                bomberGuy.getLocation().getX() + "," + bomberGuy.getLocation().getY());
    }

    public static void addUpdate(String updatedItemType, long updatedItemId, String updatedField,
            String updatedFieldValue) {
        JSONObject newUpdate = new JSONObject();
        synchronized (ServerConfig.Games.get(ServerConfig.currentGameId).updates) {
            try {
                newUpdate.put("Id", ServerConfig.Games.get(ServerConfig.currentGameId).updates.size() + 1);
                newUpdate.put("updatedItemType", updatedItemType);
                newUpdate.put("updatedItemId", updatedItemId);
                newUpdate.put("updatedField", updatedField);
                newUpdate.put("updatedFieldValue", updatedFieldValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ServerConfig.Games.get(ServerConfig.currentGameId).updates.add(newUpdate);
        }
    }

    public static void addUpdate(String updatedItemType, long updatedItemId) {
        addUpdate(updatedItemType, updatedItemId, "Add", "Add");
    }

    public static long addBomberGuy(String name) {
        try {
            BomberGuy bomberGuy = new Models.BomberGuy(
                    ServerConfig.Games.get(ServerConfig.currentGameId).bomberGuys.size() + 1, name);
            Pair<Integer, Integer> emptyCell = findEmptyCell();
            bomberGuy.setLocation(new LocationPoint(emptyCell.getKey() * ServerConfig.Games.get(ServerConfig.currentGameId).cellSize,
                    emptyCell.getValue() * ServerConfig.Games.get(ServerConfig.currentGameId).cellSize));
            bomberGuy.setBomberGuyViewModelId(new Random().nextInt(3));
            bomberGuy.setBomberHeight(ServerConfig.defaultBomberHeight[bomberGuy.getBomberGuyViewModelId()]);
            bomberGuy.setBomberWidth(ServerConfig.defaultBomberWidth[bomberGuy.getBomberGuyViewModelId()]);
            ServerConfig.Games.get(ServerConfig.currentGameId).bomberGuys.add(bomberGuy);
            addUpdate("BomberGuy", bomberGuy.getId());
            addUpdate("BomberGuy", bomberGuy.getId(), "isAlive", String.valueOf(bomberGuy.isAlive()));
            addUpdate("BomberGuy", bomberGuy.getId(), "hunterViewModelId",
                    String.valueOf(bomberGuy.getBomberGuyViewModelId()));
            addUpdate("BomberGuy", bomberGuy.getId(), "name", bomberGuy.getName());
            addUpdate("BomberGuy", bomberGuy.getId(), "currentDirection",
                    String.valueOf(bomberGuy.getCurrentDirection()));
            addUpdate("BomberGuy", bomberGuy.getId(), "moveAnimationState",
                    String.valueOf(bomberGuy.getMoveAnimationState()));
            addUpdate("BomberGuy", bomberGuy.getId(), "bomberHeight", String.valueOf(bomberGuy.getBomberHeight()));
            addUpdate("BomberGuy", bomberGuy.getId(), "bomberWidth", String.valueOf(bomberGuy.getBomberWidth()));
            addUpdate("BomberGuy", bomberGuy.getId(), "location",
                    bomberGuy.getLocation().getX() + "," + bomberGuy.getLocation().getY());
            addUpdate("BomberGuy", bomberGuy.getId(), "score", String.valueOf(bomberGuy.getScore()));
            return bomberGuy.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    public static void newMonster(JSONObject request) {
        try {
            File file = new File(request.get("PATH").toString());
            URL url = file.toURI().toURL();
            URL[] urls = new URL[] { url };
            ClassLoader classLoader = new URLClassLoader(urls);
            Class hunterClass = classLoader.loadClass("Models.HunterCustom");
            ServerConfig.Games.get(ServerConfig.currentGameId).newHunterClass = hunterClass;
            Hunter hunter = (Hunter) hunterClass.getConstructor(long.class).newInstance(0);
            ServerConfig.Games.get(ServerConfig.currentGameId).newHunterClassLevel = hunter.startLevel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addHunter(int type) {
        try {
            String hunterType;
            switch (type) {
            case 1:
                hunterType = "Easy";
                break;
            case 2:
                hunterType = "Normal";
                break;
            case 3:
                hunterType = "Hard";
                break;
            case 4:
                hunterType = "Hardcore";
                break;
            case 5:
                hunterType = "Custom";
                break;
            default:
                hunterType = "Easy";
                break;
            }
            Class hunterClass;
            if (!hunterType.equals("Custom")) {
                hunterClass = Class.forName("Models.Hunter" + hunterType);
            } else {
                hunterClass = ServerConfig.Games.get(ServerConfig.currentGameId).newHunterClass;
            }
            Constructor constructor = hunterClass.getConstructor(long.class);
            Hunter hunter = (Hunter) constructor
                    .newInstance(ServerConfig.Games.get(ServerConfig.currentGameId).hunters.size() + 1);
            Pair<Integer, Integer> emptyCell = findEmptyCell();
            hunter.setLocation(new LocationPoint( emptyCell.getKey() * ServerConfig.Games.get(ServerConfig.currentGameId).cellSize,
                    emptyCell.getValue() * ServerConfig.Games.get(ServerConfig.currentGameId).cellSize));
            ServerConfig.Games.get(ServerConfig.currentGameId).hunters.add(hunter);
            hunter.setHunterViewModelId(type);
            addUpdate("Hunter", hunter.getId());
            addUpdate("Hunter", hunter.getId(), "isAlive", String.valueOf(hunter.isAlive()));
            addUpdate("Hunter", hunter.getId(), "isGhost", String.valueOf(hunter.isGhost()));
            addUpdate("Hunter", hunter.getId(), "hunterViewModelId", String.valueOf(hunter.getHunterViewModelId()));
            addUpdate("Hunter", hunter.getId(), "currentDirection", String.valueOf(hunter.getCurrentDirection()));
            addUpdate("Hunter", hunter.getId(), "moveAnimationState", String.valueOf(hunter.getMoveAnimationState()));
            addUpdate("Hunter", hunter.getId(), "location",
                    hunter.getLocation().getX() + "," + hunter.getLocation().getY());

        } catch (Exception e) {
        }
    }

    public static void addDoor(int x, int y) {
        try {
            Door door = new Door(0);
            door.setCellX(x);
            door.setCellY(y);
            ServerConfig.Games.get(ServerConfig.currentGameId).door = door;
            addUpdate("Door", door.getId());
            addUpdate("Door", door.getId(), "isHidden", String.valueOf(true));
            addUpdate("Door", door.getId(), "cellX", String.valueOf(door.getCellX()));
            addUpdate("Door", door.getId(), "cellY", String.valueOf(door.getCellY()));
            System.out.println("door : " + x + "," + y);
        } catch (Exception e) {
        }
    }

    public static Pair<Integer, Integer> findEmptyCell(){
        int rand1 = 0, rand2 = 0;
        while (ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[rand1][rand2] != BlocksState.Grass) {
            rand1 = new Random().nextInt(ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX - 1);
            rand2 = new Random().nextInt(ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY - 1);
            rand1 = ((rand1 - rand1 % 2) + 1);
            rand2 = ((rand2 - rand2 % 2) + 1);
        }

        return new Pair<>(rand1,rand2);
    }

    public static long addPowerUp() {
        try {
            int powerUpTypeId = (new Random())
                    .nextInt(ServerConfig.Games.get(ServerConfig.currentGameId).powerUpTypes.size());
            String powerUpModelName = "Models.PowerUp"
                    + ServerConfig.Games.get(ServerConfig.currentGameId).powerUpTypes.get(powerUpTypeId);
            PowerUp powerUp = ((PowerUp) (Class.forName(powerUpModelName).newInstance()));
            while ((powerUp.getCellX() % 2 == 0 && powerUp.getCellY() % 2 == 0)
                    || (ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[powerUp.getCellX()][powerUp
                            .getCellY()] != BlocksState.Grass)) {
                powerUp.setCellX(
                        (new Random()).nextInt(ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX - 2) + 1);
                powerUp.setCellY(
                        (new Random()).nextInt(ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY - 2) + 1);
            }
            ServerConfig.Games.get(ServerConfig.currentGameId).powerUps.add(powerUp);
            ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[powerUp.getCellX()][powerUp
                    .getCellY()] = BlocksState.PowerUp;
            addUpdate("PowerUp", powerUp.getId());
            addUpdate("PowerUp", powerUp.getId(), "cellX", String.valueOf(powerUp.getCellX()));
            addUpdate("PowerUp", powerUp.getId(), "cellY", String.valueOf(powerUp.getCellY()));
            addUpdate("PowerUp", powerUp.getId(), "typeName", powerUp.getTypeName());
            addUpdate("PowerUp", powerUp.getId(), "isUsed", String.valueOf(false));
            return powerUp.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void addPowerUp(int x, int y) {
        try {
            int powerUpTypeId = (new Random())
                    .nextInt(ServerConfig.Games.get(ServerConfig.currentGameId).powerUpTypes.size());
            String powerUpModelName = "Models.PowerUp"
                    + ServerConfig.Games.get(ServerConfig.currentGameId).powerUpTypes.get(powerUpTypeId);
            PowerUp powerUp = ((PowerUp) (Class.forName(powerUpModelName).newInstance()));
            powerUp.setCellX(x);
            powerUp.setCellY(y);
            ServerConfig.Games.get(ServerConfig.currentGameId).powerUps.add(powerUp);
            System.out.println("power up - " + powerUp.getTypeName() + " : " + x + "," + y);
            addUpdate("PowerUp", powerUp.getId());
            addUpdate("PowerUp", powerUp.getId(), "cellX", String.valueOf(powerUp.getCellX()));
            addUpdate("PowerUp", powerUp.getId(), "cellY", String.valueOf(powerUp.getCellY()));
            addUpdate("PowerUp", powerUp.getId(), "typeName", powerUp.getTypeName());
            addUpdate("PowerUp", powerUp.getId(), "isUsed", String.valueOf(false));
            addUpdate("PowerUp", powerUp.getId(), "hidden", String.valueOf(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addObstacle(boolean isDoor) {
        try {
            Obstacle obstacle = new Models.Obstacle(
                    ServerConfig.Games.get(ServerConfig.currentGameId).obstacles.size() + 1);
            while (obstacle.getCellX() % 2 == 0 && obstacle.getCellY() % 2 == 0) {
                obstacle.setCellX(
                        (new Random()).nextInt(ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX - 2) + 1);
                obstacle.setCellY(
                        (new Random()).nextInt(ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY - 2) + 1);
            }
            ServerConfig.Games.get(ServerConfig.currentGameId).obstacles.add(obstacle);
            ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[obstacle.getCellX()][obstacle
                    .getCellY()] = BlocksState.Obstacle;
            addUpdate("Obstacle", obstacle.getId());
            addUpdate("Obstacle", obstacle.getId(), "cellX", String.valueOf(obstacle.getCellX()));
            addUpdate("Obstacle", obstacle.getId(), "cellY", String.valueOf(obstacle.getCellY()));
            addUpdate("Obstacle", obstacle.getId(), "destructed", String.valueOf(obstacle.isDestructed()));
            if (isDoor)
                addDoor(obstacle.getCellX(), obstacle.getCellY());
            else if ((new Random()).nextInt(100) > 70) {
                addPowerUp(obstacle.getCellX(), obstacle.getCellY());
            }
        } catch (Exception e) {
        }
    }

    public static long addObstacle(int x, int y) {
        try {
            Obstacle obstacle = new Models.Obstacle(
                    ServerConfig.Games.get(ServerConfig.currentGameId).obstacles.size() + 1);
            obstacle.setCellX(x);
            obstacle.setCellY(y);
            ServerConfig.Games.get(ServerConfig.currentGameId).obstacles.add(obstacle);
            ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[obstacle.getCellX()][obstacle
                    .getCellY()] = BlocksState.Obstacle;
            addUpdate("Obstacle", obstacle.getId());
            addUpdate("Obstacle", obstacle.getId(), "cellX", String.valueOf(obstacle.getCellX()));
            addUpdate("Obstacle", obstacle.getId(), "cellY", String.valueOf(obstacle.getCellY()));
            addUpdate("Obstacle", obstacle.getId(), "destructed", String.valueOf(obstacle.isDestructed()));
            return obstacle.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    public static void usePowerUp(PowerUp powerUp, BomberGuy bomberGuy) {
        if (powerUp == null)
            return;
        synchronized (ServerConfig.Games.get(ServerConfig.currentGameId).blocksStateLock) {
            ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[powerUp.getCellX()][powerUp
                    .getCellY()] = BlocksState.Grass;
        }
        powerUp.setUsed(true);
        powerUp.setOwnerId(bomberGuy.getId());
        powerUp.setTimeUsed(LocalDateTime.now());
        powerUp.powerUpUsed();
        addUpdate("PowerUp", powerUp.getId(), "isUsed", String.valueOf(true));
    }

    public static void checkForBombs() {
        for (Models.Bomb bomb : ServerConfig.Games.get(ServerConfig.currentGameId).bombs) {
            if (bomb.getTimePlaced().plusSeconds(bomb.getBombDestructionTime()).isBefore(LocalDateTime.now())
                    && bomb.getDestructionState() == 0) {
                if (!bomb.isControlled())
                    (new BombDemolitionThread(bomb.getId())).start();
            }
        }
        for (Models.Hunter hunter : ServerConfig.Games.get(ServerConfig.currentGameId).hunters) {
            if (!hunter.isAlive())
                continue;
            hunter.checkForMove();
            LocationPoint locationPoint = new LocationPoint(hunter.getLocation().getX(), hunter.getLocation().getY());
            locationPoint.setX(locationPoint.getX() + ServerConfig.Games.get(ServerConfig.currentGameId).cellSize / 3);
            locationPoint.setY(locationPoint.getY() + ServerConfig.Games.get(ServerConfig.currentGameId).cellSize / 3);
            for (BomberGuy bomberGuy : ServerConfig.Games.get(ServerConfig.currentGameId).bomberGuys) {
                if (isInBomber(bomberGuy, locationPoint)) {
                    bomberManDie(bomberGuy);
                }
            }

        }
    }

    public static boolean isInBomber(BomberGuy bomberGuy, LocationPoint locationPoint) {
        return ((bomberGuy.getLocation().getX() < locationPoint.getX()
                && bomberGuy.getLocation().getX() + bomberGuy.getBomberWidth() > locationPoint.getX())
                && (bomberGuy.getLocation().getY() < locationPoint.getY()
                        && bomberGuy.getLocation().getY() + bomberGuy.getBomberHeight() > locationPoint.getY()));
    }

    public static void bomberManKill(BomberGuy bomberGuy) {
        bomberGuy.setScore(bomberGuy.getScore() + bomberGuy.getScoreMultiplier());
        addUpdate("BomberGuy", bomberGuy.getId(), "score", String.valueOf(bomberGuy.getScore()));
    }

    public static void increaseScore(BomberGuy bomberGuy, int score) {
        bomberGuy.setScore(bomberGuy.getScore() + score);
        addUpdate("BomberGuy", bomberGuy.getId(), "score", String.valueOf(bomberGuy.getScore()));
    }

    public static void bomberManDie(BomberGuy bomberGuy) {
        if (!bomberGuy.isAlive())
            return;
        bomberGuy.setScore(bomberGuy.getScore() - 1);
        addUpdate("BomberGuy", bomberGuy.getId(), "score", String.valueOf(bomberGuy.getScore()));
        bomberGuy.setAlive(false);
        addUpdate("BomberGuy", bomberGuy.getId(), "isAlive", String.valueOf(bomberGuy.isAlive()));
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                bomberGuy.setAlive(true);
                Pair<Integer, Integer> emptyCell = findEmptyCell();
                bomberGuy.setLocation(new LocationPoint(
                        emptyCell.getKey() * ServerConfig.Games.get(ServerConfig.currentGameId).cellSize,
                        emptyCell.getValue() * ServerConfig.Games.get(ServerConfig.currentGameId).cellSize));
                addUpdate("BomberGuy", bomberGuy.getId(), "location",
                        bomberGuy.getLocation().getX() + "," + bomberGuy.getLocation().getY());
                addUpdate("BomberGuy", bomberGuy.getId(), "isAlive", String.valueOf(bomberGuy.isAlive()));
            }
        }, ServerConfig.Games.get(ServerConfig.currentGameId).defaultRespawnTime);
    }

    public static void hunterManDie(Hunter hunter) {
        hunter.setAlive(false);
        addUpdate("Hunter", hunter.getId(), "isAlive", String.valueOf(hunter.isAlive()));
    }

    public static String getGames() {
        StringBuilder respond = new StringBuilder();
        for (Game serverConfig : ServerConfig.Games) {
            respond.append(serverConfig.id).append(":").append(serverConfig.gameTitle).append(":")
                    .append(serverConfig.cellCountX).append(":").append(serverConfig.cellCountY).append(",");
        }
        return respond.toString();
    }

    public static void startDbCon() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            ServerConfig.dbConnection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + ServerConfig.dbName, ServerConfig.dbUser, ServerConfig.dbPW);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet execQuery(String query, boolean hasReturnValue) {
        try {
            Statement stmt = ServerConfig.dbConnection.createStatement();
            if (hasReturnValue) {
                return stmt.executeQuery(query);
            } else {
                stmt.executeUpdate(query);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int initateServerEngine(){
        startDbCon();
        ResultSet request;
        try{
            request = execQuery("select * from games where `id`='" + ServerConfig.currentGameId + "'",true);
            assert request != null;
            request.next();
            Game serverConfig = new Game(ServerConfig.currentGameId);
            serverConfig.fullFrameWidth = request.getInt("fullFrameWidth");
            serverConfig.fullFrameHeight = request.getInt("fullFrameHeight");
            serverConfig.gameTitle = request.getString("gameTitle");
            serverConfig.cellCountX = request.getInt("cellCountX") ;
            serverConfig.cellCountY = request.getInt("cellCountY");
            serverConfig.cellSize = request.getInt("cellSize");
            serverConfig.BackColor = (Color)((Class.forName("java.awt.Color").getField(request.getString("BackColor").toLowerCase())).get(null));

            serverConfig.level = request.getInt("level");
            serverConfig.defaultBomberSpeed = request.getInt("defaultBomberSpeed");
            serverConfig.defaultBomberSpeedIncreaseRate = request.getInt("defaultBomberSpeedIncreaseRate");
            serverConfig.defaultMaxBombsCount = request.getInt("defaultMaxBombsCount");
            serverConfig.defaultBombExplodingRange = request.getInt("defaultBombExplodingRange");
            serverConfig.defaultBombDestructionTime = request.getInt("defaultBombDestructionTime");
            serverConfig.defaultBombDestructionAnimationTime = request.getInt("defaultBombDestructionAnimationTime");
            serverConfig.defaultRespawnTime = request.getInt("defaultRespawnTime");
            serverConfig.defaultPowerUpSpawnInterval = request.getInt("defaultPowerUpSpawnInterval");
            serverConfig.defaultObstacleDestructionPoint = request.getInt("defaultObstacleDestructionPoint");
            serverConfig.defaultHunterDestructionPoint = request.getInt("defaultHunterDestructionPoint");

            serverConfig.pauseOwnerId = request.getInt("pauseOwnerId");
            serverConfig.defaultPauseWaitTime = request.getInt("defaultPauseWaitTime");

            serverConfig.updates = new ArrayList<>();
            serverConfig.bomberGuys = new ArrayList<>(); // done
            serverConfig.bombs = new ArrayList<>();  // done
            serverConfig.chats = new ArrayList<>(); // done
            serverConfig.powerUps = new ArrayList<>();  // done
            serverConfig.hunters = new ArrayList<>();  // partially done
            serverConfig.powerUpTypes = new ArrayList<>(); // done
            serverConfig.powerUpTypes.add("BombControl");
            serverConfig.powerUpTypes.add("DecreaseBombCount");
            serverConfig.powerUpTypes.add("DecreaseBomberGuySpeed");
            serverConfig.powerUpTypes.add("DecreaseBombRadius");
            serverConfig.powerUpTypes.add("DecreasePoint");
            serverConfig.powerUpTypes.add("Ghost");
            serverConfig.powerUpTypes.add("IncreaseBombCount");
            serverConfig.powerUpTypes.add("IncreaseBomberGuySpeed");
            serverConfig.powerUpTypes.add("IncreasePoint");
            serverConfig.powerUpTypes.add("IncreaseBombRadius");
            serverConfig.obstacles = new ArrayList<>();  // done
            serverConfig.blocksState = new BlocksState[serverConfig.cellCountX][serverConfig.cellCountY];  // done


            request = execQuery("select * from bomberguys where `gameId`='" + ServerConfig.currentGameId + "'",true);
            assert request != null;
            while(request.next())
            {
                BomberGuy bomberGuy = new BomberGuy(request.getInt("id"),request.getString("name"));
                bomberGuy.setMoveAnimationState(request.getInt("moveAnimationState"));
                bomberGuy.setBomberHeight(request.getInt("bomberHeight"));
                bomberGuy.setBomberWidth(request.getInt("bomberWidth"));
                bomberGuy.setBomberSpeed(request.getInt("bomberSpeed"));
                bomberGuy.setMaxBombsCount(request.getInt("maxBombsCount"));
                bomberGuy.setScoreMultiplier(request.getInt("scoreMultiplier"));
                bomberGuy.setScore(request.getInt("score"));
                bomberGuy.setPlantedBombsCount(request.getInt("plantedBombsCount"));
                bomberGuy.setBombExplodingRange(request.getInt("bombExplodingRange"));
                bomberGuy.setBombDestructionTime(request.getInt("bombDestructionTime"));
                bomberGuy.setAlive(request.getBoolean("isAlive"));
                bomberGuy.setCurrentDirection(Direction.ParseDirection(request.getString("currentDirection")));
                bomberGuy.setLocation(new LocationPoint(request.getInt("locationX"),request.getInt("locationY")));
                bomberGuy.setBomberGuyViewModelId(request.getInt("bomberGuyViewModelId"));
                bomberGuy.setGhost(request.getBoolean("ghost"));
                bomberGuy.setBombControl(request.getBoolean("bombControl"));
                serverConfig.bomberGuys.add(bomberGuy);
            }

            request = execQuery("select * from bombs where `gameId`='" + ServerConfig.currentGameId + "'",true);
            assert request != null;
            while(request.next())
            {
                Bomb bomb = new Bomb(request.getInt("id"),request.getInt("cellX"),request.getInt("cellY"),request.getInt("ownerId"));
                bomb.setTimePlaced(ZonedDateTime.parse(request.getString("timePlaced")).toLocalDateTime());
                bomb.setBombExplodingRange(request.getInt("bombExplodingRange"));
                bomb.setBombDestructionTime(request.getInt("bombDestructionTime"));
                bomb.setDelayForPause(request.getInt("delayForPause"));
                bomb.setDestructionState(request.getInt("destructionState"));
                serverConfig.bombs.add(bomb);
            }

            /*request = execQuery("select * from chats where `gameId`='" + ServerConfig.currentGameId + "'",true);
            while(request.next())
            {
                Chat chat = new Chat(request.getInt("id"));
                chat.setText(request.getString("text"));
                chat.setSenderId(request.getInt("senderId"));
                chat.setTimeSent(ZonedDateTime.parse(request.getString("timeSent")).toLocalDateTime());
                serverConfig.chats.add(chat);
            }*/

            request = execQuery("select * from doors where `gameId`='" + ServerConfig.currentGameId + "'",true);
            assert request != null;
            while(request.next())
            {
                Door door = new Door(request.getInt("id"));
                door.setCellX(request.getInt("cellX"));
                door.setCellY(request.getInt("cellY"));
                door.setHidden(request.getBoolean("hidden"));
                serverConfig.door = door;
            }

            request = execQuery("select * from hunters where `gameId`='" + ServerConfig.currentGameId + "'",true);
            assert request != null;
            while(request.next())
            {
                Hunter hunter = new HunterEasy(request.getInt("id"));
                hunter.setMoveAnimationState(request.getInt("moveAnimationState"));
                hunter.setAlive(request.getBoolean("isAlive"));
                hunter.setCurrentDirection(Direction.ParseDirection(request.getString("currentDirection")));
                hunter.setLocation(new LocationPoint(request.getInt("locationX"),request.getInt("locationY")));
                hunter.setHunterSize(request.getInt("hunterSize"));
                hunter.setHunterSpeed(request.getInt("hunterSpeed"));
                hunter.setHunterViewModelId(request.getInt("hunterViewModelId"));
                hunter.setLastMove(ZonedDateTime.parse(request.getString("lastMove")).toLocalDateTime());
                hunter.setName(request.getString("name"));
                serverConfig.hunters.add(hunter);
            }

            request = execQuery("select * from powerups where `gameId`='" + ServerConfig.currentGameId + "'",true);
            assert request != null;
            while(request.next())
            {
                String powerUpModelName = "Models.PowerUp" + request.getString("typeName");
                PowerUp powerUp = ((PowerUp)( Class.forName(powerUpModelName).newInstance()));
                powerUp.setId(request.getInt("id"));
                powerUp.setTimeUsed(ZonedDateTime.parse(request.getString("timeUsed")).toLocalDateTime());
                powerUp.setOwnerId(request.getInt("ownerId"));
                powerUp.setPowerUpImageAddress(request.getString("powerUpImageAddress"));
                powerUp.setCellX(request.getInt("cellX"));
                powerUp.setCellY(request.getInt("cellY"));
                powerUp.setUsed(request.getBoolean("isUsed"));
                powerUp.setHidden(request.getBoolean("hidden"));
                serverConfig.powerUps.add(powerUp);
            }

            request = execQuery("select * from obstacles where `gameId`='" + ServerConfig.currentGameId + "'",true);
            assert request != null;
            while(request.next())
            {
                Obstacle obstacle = new Obstacle(request.getInt("id"));
                obstacle.setCellX(request.getInt("cellX"));
                obstacle.setCellY(request.getInt("cellY"));
                obstacle.setDestructed(request.getBoolean("destructed"));
                serverConfig.obstacles.add(obstacle);
            }

            request = execQuery("select * from blocksstate where `gameId`='" + ServerConfig.currentGameId + "'",true);
            assert request != null;
            while(request.next())
            {
                serverConfig.blocksState[request.getInt("x")][request.getInt("y")] = BlocksState.ParseBlocksState(request.getInt("state"));
            }

            ServerConfig.Games.add(serverConfig);
            return ServerConfig.Games.size()-1;
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }


    public static void saveToDB() {
        Game serverConfig = ServerConfig.Games.get(ServerConfig.currentGameId);
        execQuery(
                "INSERT INTO `games` (`id`, `fullFrameWidth`, `fullFrameHeight`, `gameTitle`, `cellCountX`, `cellCountY`,"
                        + " `cellSize`, `BackColor`, `level`, `defaultBomberSpeed`, `defaultBomberSpeedIncreaseRate`, `defaultMaxBombsCount`,"
                        + " `defaultBombExplodingRange`, `defaultBombDestructionTime`, `defaultBombDestructionAnimationTime`, `defaultRespawnTime`,"
                        + " `defaultPowerUpSpawnInterval`, `defaultObstacleDestructionPoint`, `defaultHunterDestructionPoint`, `pauseTime`, `isPaused`,"
                        + " `pauseOwnerId`, `defaultPauseWaitTime`) VALUES ('" + serverConfig.id + "', '"
                        + serverConfig.fullFrameWidth + "', '" + serverConfig.fullFrameHeight + "', '"
                        + serverConfig.gameTitle + "'," + " '" + serverConfig.cellCountX + "', '"
                        + serverConfig.cellCountY + "', '" + serverConfig.cellSize + "', '" + serverConfig.BackColor
                        + "', '" + serverConfig.level + "', '" + serverConfig.defaultBomberSpeed + "'," + " '"
                        + serverConfig.defaultBomberSpeedIncreaseRate + "', '" + serverConfig.defaultMaxBombsCount
                        + "', '" + serverConfig.defaultBombExplodingRange + "', '"
                        + serverConfig.defaultBombDestructionTime + "', '"
                        + serverConfig.defaultBombDestructionAnimationTime + "', '" + serverConfig.defaultRespawnTime
                        + "', '" + serverConfig.defaultPowerUpSpawnInterval + "'," + " '"
                        + serverConfig.defaultObstacleDestructionPoint + "', '"
                        + serverConfig.defaultHunterDestructionPoint + "', '" + serverConfig.pauseTime + "', '"
                        + serverConfig.isPaused + "', '" + serverConfig.pauseOwnerId + "', '"
                        + serverConfig.defaultPauseWaitTime + "');",
                false);
        int mt = 0;
        try {

            ResultSet request = execQuery("select * from games", true);
            assert request != null;
            while (request.next()) {
                mt = Math.max(request.getInt("id"),mt);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        execQuery("INSERT INTO `doors` (`id`, `cellX`, `cellY`, `hidden`, `gameId`) VALUES (NULL, '"
                + serverConfig.door.getCellX() + "', '" + serverConfig.door.getCellY() + "', '"
                + serverConfig.door.isHidden() + "', '" + mt + "');", false);

        for (Bomb bomb : serverConfig.bombs)
            execQuery(
                    "INSERT INTO `bombs` (`id`, `cellX`, `cellY`, `timePlaced`, `ownerId`, `bombExplodingRange`, `bombDestructionTime`, `delayForPause`, `destructionState`, `gameId`) "
                            + "VALUES (NULL, '" + bomb.getCellX() + "', '" + bomb.getCellY() + "', '"
                            + bomb.getTimePlaced() + "', '" + bomb.getOwnerId() + "', '" + bomb.getBombExplodingRange()
                            + "', '" + bomb.getDestructionState() + "', '" + bomb.getDelayForPause() + "', '"
                            + bomb.getDestructionState() + "', '" + mt + "');",
                    false);

        for(int i=0;i<serverConfig.cellCountX;i++)
            for(int j=0;j<serverConfig.cellCountY;j++)
                execQuery("INSERT INTO `blocksstate` (`id`, `x`, `y`, `gameId`, `state`) VALUES (NULL, '"+i+"', '"+j+"', '"+mt+"', '"+serverConfig.blocksState[i][j]+"');",false);

        for(BomberGuy bomberGuy : serverConfig.bomberGuys)
            execQuery("INSERT INTO `bomberguys` (`id`, `moveAnimationState`, `bomberHeight`, `bomberWidth`, `bomberSpeed`, `maxBombsCount`, `scoreMultiplier`," +
                    " `score`, `plantedBombsCount`, `bombExplodingRange`, `bombDestructionTime`, `isAlive`, `currentDirection`, `locationX`, `locationY`, `name`," +
                    " `bomberGuyViewModelId`, `ghost`, `bombControl`, `gameId`) VALUES" +
                    " (NULL, '"+bomberGuy.getMoveAnimationState()+"', '"+bomberGuy.getBomberHeight()+"', '"+bomberGuy.getBomberWidth()+"', '"+bomberGuy.getBomberSpeed()+"'," +
                    " '"+bomberGuy.getMaxBombsCount()+"', '"+bomberGuy.getScoreMultiplier()+"', '"+bomberGuy.getScore()+"',  '"+bomberGuy.getPlantedBombsCount()+"', '"+bomberGuy.getBombExplodingRange()+"'," +
                    " '"+bomberGuy.getBombDestructionTime()+"', '"+bomberGuy.isAlive()+"', '"+bomberGuy.getCurrentDirection()+"', '"+bomberGuy.getLocation().getX()+"', '"+bomberGuy.getLocation().getY()+"', '"+bomberGuy.getName()+"'," +
                    " '"+bomberGuy.getBomberGuyViewModelId()+"', '"+bomberGuy.isGhost()+"', '"+bomberGuy.isBombControl()+"', '"+mt+"');",false);

        for(Hunter hunter : serverConfig.hunters)
            execQuery("INSERT INTO `hunters` (`id`, `moveAnimationState`, `isAlive`, `currentDirection`, `locationX`, `locationY`, `hunterSize`," +
                    " `hunterSpeed`, `ghost`, `hunterViewModelId`, `lastMove`, `delayBetweenMoves`, `gameId`, `name`) " +
                    "VALUES (NULL, '"+hunter.getMoveAnimationState()+"', '"+hunter.isAlive()+"', '"+hunter.getCurrentDirection()+"', '"+hunter.getLocation().getX()+"', '"+hunter.getLocation().getY()+"', '"+hunter.getHunterSize()+"'," +
                    " '"+hunter.getHunterSpeed()+"', '"+hunter.isGhost()+"', '"+hunter.getHunterViewModelId()+"', '"+hunter.getLastMove()+"', '"+hunter.delayBetweenMoves()+"', '"+mt+"', '"+hunter.getName()+"');",false);

        for(Obstacle obstacle : serverConfig.obstacles)
            execQuery("INSERT INTO `obstacles` (`id`, `cellX`, `cellY`, `destructed`, `gameId`)" +
                    " VALUES (NULL, '"+obstacle.getCellX()+"', '"+obstacle.getCellY()+"', '"+obstacle.isDestructed()+"', '"+mt+"');",false);

        for(PowerUp powerUp : serverConfig.powerUps)
            execQuery("INSERT INTO `powerups` (`id`, `timeUsed`, `ownerId`, `powerUpImageAddress`, `typeName`, `cellX`, `cellY`, `isUsed`, `hidden`, `gameId`) VALUES" +
                    " (NULL, '"+powerUp.getTimeUsed()+"', '"+powerUp.getOwnerId()+"', '"+powerUp.getPowerUpImageAddress()+"', '"+powerUp.getTypeName()+"', '"+powerUp.getCellX()+"', '"+powerUp.getCellY()+"', '"+powerUp.isUsed()+"', '"+powerUp.isHidden()+"', '"+mt+"');",false);


    }

    public static int initSolo(JSONObject request) {
        try {

            Game serverConfig = new Game(ServerConfig.Games.size());
            serverConfig.defaultBombExplodingRange = Integer.parseInt(request.get("BombExplosionRadius").toString());
            serverConfig.defaultBombDestructionTime = Integer.parseInt(request.get("BombTimer").toString());
            serverConfig.defaultBomberSpeed = Integer.parseInt(request.get("BomberManSpeed").toString());
            serverConfig.cellSize = Integer.parseInt(request.get("CellSize").toString());
            serverConfig.gameTitle = request.get("GameName").toString();
            serverConfig.defaultMaxBombsCount = Integer.parseInt(request.get("MaxBombCount").toString());
            serverConfig.defaultPauseWaitTime = Integer.parseInt(request.get("PauseTime").toString());
            serverConfig.defaultRespawnTime = Integer.parseInt(request.get("RespawnTime").toString());
            serverConfig.BackColor = Color.decode(request.get("BackColor").toString());
            serverConfig.cellCountX = Integer.parseInt(request.get("CellCountX").toString());
            serverConfig.cellCountY = Integer.parseInt(request.get("CellCountY").toString());
            serverConfig.fullFrameWidth = Integer.parseInt(request.get("GameFrameWidth").toString());
            serverConfig.fullFrameHeight = Integer.parseInt(request.get("GameFrameHeight").toString());
            serverConfig.level = Integer.parseInt(request.get("level").toString());
            serverConfig.updates = new ArrayList<>();
            serverConfig.bomberGuys = new ArrayList<>();
            serverConfig.bombs = new ArrayList<>();
            serverConfig.powerUps = new ArrayList<>();
            serverConfig.hunters = new ArrayList<>();
            serverConfig.chats = new ArrayList<>();
            serverConfig.powerUpTypes = new ArrayList<>();
            serverConfig.powerUpTypes.add("BombControl");
            serverConfig.powerUpTypes.add("DecreaseBombCount");
            serverConfig.powerUpTypes.add("DecreaseBomberGuySpeed");
            serverConfig.powerUpTypes.add("DecreaseBombRadius");
            serverConfig.powerUpTypes.add("DecreasePoint");
            serverConfig.powerUpTypes.add("Ghost");
            serverConfig.powerUpTypes.add("IncreaseBombCount");
            serverConfig.powerUpTypes.add("IncreaseBomberGuySpeed");
            serverConfig.powerUpTypes.add("IncreasePoint");
            serverConfig.powerUpTypes.add("IncreaseBombRadius");
            serverConfig.obstacles = new ArrayList<>();
            serverConfig.blocksState = new BlocksState[serverConfig.cellCountX][serverConfig.cellCountY];
            ServerConfig.Games.add(serverConfig);
            ServerConfig.currentGameId = serverConfig.id;

            for (int i = 0; i < ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX; i++)
                for (int j = 0; j < ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY; j++)
                    if ((i % 2 == 0 && j % 2 == 0) || i == 0 || j == 0
                            || i == ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX - 1
                            || j == ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY - 1)
                        ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[i][j] = BlocksState.Wall;
                    else
                        ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[i][j] = BlocksState.Grass;

            int obstacleCount = (new Random()).nextInt((ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX
                    * ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY) / 8);
            addObstacle(true);
            for (int i = 0; i < obstacleCount - 1; i++)
                addObstacle(false);
            int hunterCount = (new Random()).nextInt((ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX
                    * ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY) / 8);

            for (int i = 0; i < hunterCount; i++)
                addHunter(
                        ((new Random()).nextInt(Math.min(4, ServerConfig.Games.get(ServerConfig.currentGameId).level)))
                                + 1);

            return ServerConfig.currentGameId;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static JSONObject ConnectSolo(JSONObject request) {
        try {
            JSONObject response = new JSONObject();
            response.put("cellSize", ServerConfig.Games.get(ServerConfig.currentGameId).cellSize);
            response.put("cellCountX", ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX);
            response.put("cellCountY", ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY);
            response.put("isPaused", ServerConfig.Games.get(ServerConfig.currentGameId).isPaused);
            response.put("fullFrameWidth", ServerConfig.Games.get(ServerConfig.currentGameId).fullFrameWidth);
            response.put("fullFrameHeight", ServerConfig.Games.get(ServerConfig.currentGameId).fullFrameHeight);
            response.put("gameTitle", ServerConfig.Games.get(ServerConfig.currentGameId).gameTitle);
            response.put("backColor", "#" + Integer
                    .toHexString(ServerConfig.Games.get(ServerConfig.currentGameId).BackColor.getRGB()).substring(2));
            response.put("level", ServerConfig.Games.get(ServerConfig.currentGameId).level);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveGame(int id, String filename) {
        for (Game game : ServerConfig.Games) {
            if (game.id == id) {
                try {
                    FileOutputStream file = new FileOutputStream(filename);
                    ObjectOutputStream out = new ObjectOutputStream(file);
                    out.writeObject(game);
                    out.close();
                    file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int init(JSONObject request) {
        try {
            Game serverConfig = new Game(ServerConfig.Games.size());
            ServerConfig.Games.add(serverConfig);
            ServerConfig.currentGameId = serverConfig.id;
            serverConfig.defaultBombExplodingRange = Integer.parseInt(request.get("BombExplosionRadius").toString());
            serverConfig.defaultBombDestructionTime = Integer.parseInt(request.get("BombTimer").toString());
            serverConfig.defaultBomberSpeed = Integer.parseInt(request.get("BomberManSpeed").toString());
            serverConfig.cellSize = Integer.parseInt(request.get("CellSize").toString());
            serverConfig.gameTitle = request.get("GameName").toString();
            serverConfig.defaultMaxBombsCount = Integer.parseInt(request.get("MaxBombCount").toString());
            serverConfig.defaultPauseWaitTime = Integer.parseInt(request.get("PauseTime").toString());
            serverConfig.defaultRespawnTime = Integer.parseInt(request.get("RespawnTime").toString());
            serverConfig.BackColor = Color.decode(request.get("BackColor").toString());
            serverConfig.cellCountX = Integer.parseInt(request.get("CellCountX").toString());
            serverConfig.cellCountY = Integer.parseInt(request.get("CellCountY").toString());
            serverConfig.fullFrameWidth = Integer.parseInt(request.get("GameFrameWidth").toString());
            serverConfig.fullFrameHeight = Integer.parseInt(request.get("GameFrameHeight").toString());
            serverConfig.updates = new ArrayList<>();
            serverConfig.bomberGuys = new ArrayList<>();
            serverConfig.bombs = new ArrayList<>();
            serverConfig.powerUps = new ArrayList<>();
            serverConfig.powerUpTypes = new ArrayList<>();
            serverConfig.powerUpTypes.add("DoubleScore");
            serverConfig.powerUpTypes.add("HalfSize");
            serverConfig.obstacles = new ArrayList<>();
            serverConfig.blocksState = new BlocksState[ServerConfig.Games
                    .get(ServerConfig.currentGameId).cellCountX][ServerConfig.Games
                            .get(ServerConfig.currentGameId).cellCountY];
            for (int i = 0; i < ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX; i++)
                for (int j = 0; j < ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY; j++)
                    if ((i % 2 == 0 && j % 2 == 0) || i == 0 || j == 0
                            || i == ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX - 1
                            || j == ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY - 1)
                        ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[i][j] = BlocksState.Wall;
                    else
                        ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[i][j] = BlocksState.Grass;
            // int obstacleCount = (new
            // Random()).nextInt((ServerConfig.Games.get(ServerConfig.currentGameId).cellCountX
            // * ServerConfig.Games.get(ServerConfig.currentGameId).cellCountY)/8);
            // for(int i=0;i<obstacleCount;i++)
            // addObstacle();
            return ServerConfig.currentGameId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}