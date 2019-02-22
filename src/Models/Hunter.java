package Models;

import Config.ServerConfig;
import Utilies.BlocksState;
import Utilies.Direction;
import Utilies.LocationPoint;
import Utilies.ServerEngine;

import java.time.LocalDateTime;

public abstract class Hunter {
    protected long id;
    protected int moveAnimationState;
    protected boolean isAlive;
    protected Direction currentDirection;
    protected LocationPoint location;
    protected int hunterSize;
    protected int hunterSpeed;
    protected int hunterViewModelId;
    protected LocalDateTime lastMove;
    protected String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void checkForMove(){
        if(lastMove.plusNanos(delayBetweenMoves()*1000000).isBefore(LocalDateTime.now()) && !ServerConfig.Games.get(ServerConfig.currentGameId).isPaused){
            this.doNextMove();
        }
    }

    public abstract int delayBetweenMoves();

    public void setLastMove(LocalDateTime lastMove) {
        this.lastMove = lastMove;
    }


    public LocalDateTime getLastMove() {

        return lastMove;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getMoveAnimationState() {
        return moveAnimationState;
    }

    public void setHunterViewModelId(int hunterViewModelId) {
        this.hunterViewModelId = hunterViewModelId;
    }

    public int getHunterViewModelId() {
        return hunterViewModelId;
    }

    public Hunter(long Id){
        this.id = Id;
        this.moveAnimationState = 0;
        this.isAlive = true;
        this.currentDirection = Direction.Stand;
        this.hunterSize = (ServerConfig.Games.get(ServerConfig.currentGameId).cellSize * 2) / 3;
        this.lastMove = LocalDateTime.now();
        hunterSpeed = ServerConfig.Games.get(ServerConfig.currentGameId).defaultBomberSpeed/2;
    }

    public abstract int startLevel();

    public void doNextMove(){
        int cellSize = ServerConfig.Games.get(ServerConfig.currentGameId).cellSize;
        lastMove = LocalDateTime.now();
        currentDirection = Direction.ParseDirection(calculateNextMove());
        moveAnimationState = 1 - moveAnimationState;
        if(isGhost()) {
            switch (currentDirection) {
                case Left:
                    location.setX(location.getX() - hunterSpeed);
                    break;
                case Right:
                    location.setX(location.getX() + hunterSpeed);
                    break;
                case Up:
                    location.setY(location.getY() - hunterSpeed);
                    break;
                case Down:
                    location.setY(location.getY() + hunterSpeed);
                    break;
            }
        }else{
            switch (currentDirection) {
                case Left:
                    if(ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[(getLocation().getX() + cellSize/3 - getHunterSpeed()) / cellSize][(getLocation().getY() + cellSize/3) / cellSize] == BlocksState.Grass ||
                            ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[(getLocation().getX() + cellSize/3 - getHunterSpeed()) / cellSize][(getLocation().getY() + cellSize/3) / cellSize] == BlocksState.PowerUp){
                        location.setX(location.getX()-hunterSpeed);
                    }
                    break;
                case Right:
                    if(ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[(getLocation().getX() + cellSize/3 + getHunterSpeed()) / cellSize][(getLocation().getY() + cellSize/3) / cellSize] == BlocksState.Grass ||
                            ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[(getLocation().getX() + cellSize/3 + getHunterSpeed()) / cellSize][(getLocation().getY() + cellSize/3) / cellSize] == BlocksState.PowerUp){
                        location.setX(location.getX()+hunterSpeed);
                    }
                    break;
                case Up:
                    if(ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[(getLocation().getX() + cellSize/3) / cellSize][(getLocation().getY() + cellSize/3  - getHunterSpeed()) / cellSize] == BlocksState.Grass ||
                            ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[(getLocation().getX() + cellSize/3) / cellSize][(getLocation().getY() + cellSize/3  - getHunterSpeed()) / cellSize] == BlocksState.PowerUp){
                        location.setY(location.getY()-hunterSpeed);
                    }
                    break;
                case Down:
                    if(ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[(getLocation().getX() + cellSize/3) / cellSize][(getLocation().getY() + cellSize/3  + getHunterSpeed()) / cellSize] == BlocksState.Grass ||
                            ServerConfig.Games.get(ServerConfig.currentGameId).blocksState[(getLocation().getX() + cellSize/3) / cellSize][(getLocation().getY() + cellSize/3  + getHunterSpeed()) / cellSize] == BlocksState.PowerUp){
                        location.setY(location.getY()+hunterSpeed);
                    }
                    break;
            }
        }

        ServerEngine.addUpdate("Hunter",this.getId(),"currentDirection",String.valueOf(this.getCurrentDirection()));
        ServerEngine.addUpdate("Hunter",this.getId(),"moveAnimationState",String.valueOf(this.getMoveAnimationState()));
        ServerEngine.addUpdate("Hunter",this.getId(),"location",this.getLocation().getX()+","+this.getLocation().getY());

    }

    public void setMoveAnimationState(int moveAnimationState) {
        this.moveAnimationState = moveAnimationState;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public void setLocation(LocationPoint location) {
        this.location = location;
    }

    public void setHunterSize(int hunterSize) {
        this.hunterSize = hunterSize;
    }

    public void setHunterSpeed(int hunterSpeed) {
        this.hunterSpeed = hunterSpeed;
    }

    public long getId() {
        return id;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public LocationPoint getLocation() {
        return location;
    }

    public int getHunterSize() {
        return hunterSize;
    }

    public int getHunterSpeed() {
        return hunterSpeed;
    }

    public abstract boolean isGhost();

    public abstract String calculateNextMove();


}
