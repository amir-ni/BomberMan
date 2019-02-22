package Models;

import Utilies.BlocksState;
import Utilies.Lock;
import org.json.JSONObject;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    public Game(int id){
        this.id = id;
    }

    public int id;
    public int fullFrameWidth;
    public int fullFrameHeight;
    public String gameTitle = "Game";
    public int cellCountX;
    public int cellCountY;
    public int cellSize = 40;
    public Color BackColor = Color.BLACK;

    public volatile BlocksState[][] blocksState;
    public final Utilies.Lock blocksStateLock = new Lock();
    public volatile ArrayList<JSONObject> updates;

    public ArrayList<Models.BomberGuy> bomberGuys;
    public ArrayList<Models.Bomb> bombs;
    public ArrayList<Models.Chat> chats;
    public ArrayList<Models.Hunter> hunters;
    public ArrayList<Models.Obstacle> obstacles;
    public ArrayList<Models.PowerUp> powerUps;
    public ArrayList<String> powerUpTypes;
    public ArrayList<String> huntertypes;
    public Models.Door door;

    public int level = 1;
    public int defaultBomberSpeed = 5;
    public int defaultBomberSpeedIncreaseRate = 5;
    public int defaultMaxBombsCount = 1;
    public int defaultBombExplodingRange = 1;
    public int defaultBombDestructionTime = 5; //(s)
    public int defaultBombDestructionAnimationTime = 200; //(ms)
    public int defaultRespawnTime = 5000; //(ms)
    public int defaultPowerUpSpawnInterval = 10000;
    public int defaultObstacleDestructionPoint = 10;
    public int defaultHunterDestructionPoint = 20;
    public Class newHunterClass;
    public int newHunterClassLevel;

    public java.time.LocalDateTime pauseTime;
    public boolean isPaused;
    public int pauseOwnerId = -1;
    public int defaultPauseWaitTime = 5;
}
