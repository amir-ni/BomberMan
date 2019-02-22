package Utilies;

import java.io.Serializable;

public enum BlocksState implements Serializable {

    Grass(0),
    Wall(1),
    Obstacle(2),
    Bomb(3),
    PowerUp(4),
    Door(5);

    private int value;

    BlocksState(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static BlocksState ParseBlocksState(int s){
        switch (s) {
            case 5:
                return Door;
            case 4:
                return PowerUp;
            case 3:
                return Bomb;
            case 2:
                return Obstacle;
            case 1:
                return Wall;
            default:
                return Grass;
        }
    }
}
