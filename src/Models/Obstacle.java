package Models;


import java.io.Serializable;

public class Obstacle implements Serializable {
    protected int id;
    protected int cellX = 0;
    protected int cellY = 0;
    protected boolean destructed;

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }

    public void setDestructed(boolean destructed) {
        this.destructed = destructed;
    }

    public Obstacle(int id){
        this.id = id;
        destructed = false;
    }

    public int getId() {
        return id;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public boolean isDestructed() {
        return destructed;
    }

    public void destruct(){
        destructed = true;
    }
}
