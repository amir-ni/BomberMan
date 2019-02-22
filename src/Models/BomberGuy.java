package Models;

import Config.ServerConfig;
import Utilies.Direction;
import Utilies.LocationPoint;

import java.util.ArrayList;

public class BomberGuy {
    protected long id;
    protected int moveAnimationState;
    protected int bomberHeight;
    protected int bomberWidth;
    protected int bomberSpeed;
    protected int maxBombsCount;
    protected int scoreMultiplier;
    protected int score;
    protected int plantedBombsCount;
    protected int bombExplodingRange;
    protected int bombDestructionTime;
    protected boolean isAlive;
    protected Direction currentDirection;
    protected LocationPoint location;
    protected String name;
    protected int bomberGuyViewModelId;
    protected boolean ghost;
    protected boolean bombControl;
    public ArrayList<Long> bombsInControlId = new ArrayList<>();



    public void setBombExplodingRange(int bombExplodingRange) {
        this.bombExplodingRange = bombExplodingRange;
    }


    public void setGhost(boolean ghost) {
        this.ghost = ghost;
    }

    public void setBombControl(boolean bombControl) {
        this.bombControl = bombControl;
    }

    public boolean isGhost() {
        return ghost;
    }

    public boolean isBombControl() {
        return bombControl;
    }

    public int getBomberGuyViewModelId() {
        return bomberGuyViewModelId;
    }

    public void setBomberGuyViewModelId(int bomberGuyViewModelId) {
        this.bomberGuyViewModelId = bomberGuyViewModelId;
    }

    public void setScoreMultiplier(int scoreMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
    }

    public void setBomberSpeed(int bomberSpeed) {
        this.bomberSpeed = bomberSpeed;
    }

    public void setMaxBombsCount(int maxBombsCount) {
        this.maxBombsCount = maxBombsCount;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getScoreMultiplier() {
        return scoreMultiplier;
    }

    public int getBombExplodingRange() {
        return bombExplodingRange;
    }

    public int getBombDestructionTime() {
        return bombDestructionTime;
    }

    public void setLocation(LocationPoint location) {
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public int getMoveAnimationState() {
        return moveAnimationState;
    }

    public int getBomberHeight() {
        return bomberHeight;
    }

    public int getBomberWidth() {
        return bomberWidth;
    }

    public int getBomberSpeed() {
        return bomberSpeed;
    }

    public int getMaxBombsCount() {
        return maxBombsCount;
    }

    public int getScore() {
        return score;
    }

    public int getPlantedBombsCount() {
        return plantedBombsCount;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public LocationPoint getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setBomberHeight(int bomberHeight) {
        this.bomberHeight = bomberHeight;
    }

    public void setBomberWidth(int bomberWidth) {
        this.bomberWidth = bomberWidth;
    }

    public BomberGuy(long Id, String Name){
        this.id = Id;
        this.moveAnimationState = 0;

        this.bomberSpeed = ServerConfig.Games.get(ServerConfig.currentGameId).defaultBomberSpeed;
        this.maxBombsCount = ServerConfig.Games.get(ServerConfig.currentGameId).defaultMaxBombsCount;
        this.bombExplodingRange = ServerConfig.Games.get(ServerConfig.currentGameId).defaultBombExplodingRange;
        this.bombDestructionTime = ServerConfig.Games.get(ServerConfig.currentGameId).defaultBombDestructionTime;
        this.score = 0;
        this.scoreMultiplier = 1;
        this.plantedBombsCount = 0;
        this.isAlive = true;
        this.currentDirection = Direction.Stand;
        this.ghost = false;
        this.bombControl = false;

        this.name = Name;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public void setMoveAnimationState(int moveAnimationState) {
        this.moveAnimationState = moveAnimationState;
    }

    public void setPlantedBombsCount(int plantedBombsCount) {
        this.plantedBombsCount = plantedBombsCount;
    }

    public void setBombDestructionTime(int bombDestructionTime) {
        this.bombDestructionTime = bombDestructionTime;
    }
}
