package Models;

import Utilies.ServerEngine;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class PowerUp implements Serializable{
    protected long id;
    protected LocalDateTime timeUsed;
    protected long ownerId;
    protected String powerUpImageAddress;
    protected String typeName;
    protected int cellX;
    protected int cellY;
    protected boolean isUsed;

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {

        isHidden = hidden;
    }

    protected boolean isHidden;

    public void setTimeUsed(LocalDateTime timeUsed) {
        this.timeUsed = timeUsed;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public int getCellX() {
        return cellX;
    }

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }

    public PowerUp(){
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public BomberGuy getOwner(){
        return ServerEngine.findBomberGuyById(getOwnerId());
    }

    public LocalDateTime getTimeUsed() {
        return timeUsed;
    }

    public void setPowerUpImageAddress(String powerUpImageAddress) {
        this.powerUpImageAddress = powerUpImageAddress;
    }

    public String getPowerUpImageAddress() {
        return powerUpImageAddress;
    }

    public void powerUpPicked(int ownerId){
        this.ownerId = ownerId;
        timeUsed = LocalDateTime.now();
        powerUpUsed();
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public abstract void powerUpUsed();
}
