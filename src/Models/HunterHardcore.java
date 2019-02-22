package Models;

import Config.ServerConfig;
import Utilies.Direction;
import Utilies.LocationPoint;

import java.util.Random;

public class HunterHardcore extends Hunter {
    @Override
    public int delayBetweenMoves() {
        return 200;
    }

    public HunterHardcore(long Id) {
        super(Id);
    }

    @Override
    public int startLevel() {
        return 4;
    }

    @Override
    public boolean isGhost() {
        return true;
    }

    @Override
    public String calculateNextMove() {
        int tempInt = new Random().nextInt(2);
        int tempIntBomber = new Random().nextInt(ServerConfig.Games.get(ServerConfig.currentGameId).bomberGuys.size());
        if(tempInt == 1){
            if(ServerConfig.Games.get(ServerConfig.currentGameId).bomberGuys.get(tempIntBomber).location.getX() < this.location.getX()){
                return Direction.Left.toString();
            }else{
                return Direction.Right.toString();
            }
        }else{
            if(ServerConfig.Games.get(ServerConfig.currentGameId).bomberGuys.get(tempIntBomber).location.getY() < this.location.getY()){
                return Direction.Up.toString();
            }else{
                return Direction.Down.toString();
            }
        }
    }
}
