package Models;

import Config.ServerConfig;
import Utilies.Direction;
import Utilies.LocationPoint;

import java.util.Random;

public class HunterEasy extends Hunter {

    @Override
    public int delayBetweenMoves() {
        return 350;
    }

    public HunterEasy(long Id) {
        super(Id);
    }

    @Override
    public int startLevel() {
        return 1;
    }

    @Override
    public boolean isGhost() {
        return false;
    }

    @Override
    public String calculateNextMove() {
        int tempInt = new Random().nextInt(4);
        switch (tempInt){
            case 1:
                return Direction.Up.toString();
            case 2:
                return Direction.Down.toString();
            case 3:
                return Direction.Left.toString();
            default:
                return Direction.Right.toString();
        }
    }
}
