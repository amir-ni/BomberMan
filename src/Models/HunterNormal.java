package Models;

import Config.ServerConfig;
import Utilies.Direction;

import java.util.Random;

public class HunterNormal extends Hunter {

    public HunterNormal(long Id) {
        super(Id);
    }

    @Override
    public int startLevel() {
        return 2;
    }

    @Override
    public int delayBetweenMoves() {
        return 350;
    }

    @Override
    public boolean isGhost() {
        return false;
    }

    @Override
    public String calculateNextMove() {
        int tempInt = new Random().nextInt(2);
        int tempIntBomber = new Random().nextInt(ServerConfig.Games.get(ServerConfig.currentGameId).bomberGuys.size());

        if (tempInt == 1) {
            if (ServerConfig.Games.get(ServerConfig.currentGameId).bomberGuys.get(tempIntBomber).location
                    .getX() < this.location.getX()) {
                return Direction.Left.toString();
            } else {
                return Direction.Right.toString();
            }
        } else {
            if (ServerConfig.Games.get(ServerConfig.currentGameId).bomberGuys.get(tempIntBomber).location
                    .getY() < this.location.getY()) {
                return Direction.Up.toString();
            } else {
                return Direction.Down.toString();
            }
        }
    }
}
