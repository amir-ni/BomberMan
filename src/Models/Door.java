package Models;

import java.io.Serializable;

public class Door implements Serializable {
    protected long id;
    protected int cellX = 0;
    protected int cellY = 0;

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    protected boolean hidden = true;

    public void setCellX(int cellX) {
        this.cellX = cellX;
    }

    public void setCellY(int cellY) {
        this.cellY = cellY;
    }

    public Door(long id){
        this.id = id;
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

}
