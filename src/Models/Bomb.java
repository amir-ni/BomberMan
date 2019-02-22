package Models;

import Utilies.ServerEngine;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Bomb implements Serializable {
    protected int id;
    protected int cellX;
    protected int cellY;
    protected boolean controlled;

    public void setControlled(boolean controlled) {
        this.controlled = controlled;
    }

    public boolean isControlled() {

        return controlled;
    }

    public int getDelayForPause() {
        return delayForPause;
    }

    public void setTimePlaced(LocalDateTime timePlaced) {
        this.timePlaced = timePlaced;
    }

    public void setBombExplodingRange(int bombExplodingRange) {
        this.bombExplodingRange = bombExplodingRange;
    }

    public void setBombDestructionTime(int bombDestructionTime) {
        this.bombDestructionTime = bombDestructionTime;
    }

    public void setDelayForPause(int delayForPause) {
        this.delayForPause = delayForPause;
    }

    protected java.time.LocalDateTime timePlaced;
    protected long ownerId;
    protected int bombExplodingRange;
    protected int bombDestructionTime;
    protected int delayForPause;
    protected int destructionState;

    public int getDestructionState() {
        return destructionState;
    }

    public void setDestructionState(int destructionState) {
        this.destructionState = destructionState;
    }

    public int getBombDestructionTime() {
        return bombDestructionTime;
    }

    public long getId() {
        return id;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public LocalDateTime getTimePlaced() {
        return timePlaced;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public int getBombExplodingRange() {
        return bombExplodingRange;
    }

    public void addDelayForPause(long time){
        delayForPause += time;
    }

    public BomberGuy getOwner(){
        return ServerEngine.findBomberGuyById(getOwnerId());
    }

    public Bomb(int id, int cellX, int cellY, long ownerId) {
        this.id = id;
        this.cellX = cellX;
        this.cellY = cellY;
        this.ownerId = ownerId;
        this.delayForPause = 0;
        this.destructionState = 0;
        this.bombExplodingRange = ServerEngine.findBomberGuyById(ownerId).bombExplodingRange;
        this.bombDestructionTime = ServerEngine.findBomberGuyById(ownerId).bombDestructionTime;
        this.timePlaced = LocalDateTime.now();
    }
}
