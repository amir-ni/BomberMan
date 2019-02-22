package Models;

import Config.ServerConfig;

public class PowerUpDecreaseBomberGuySpeed extends PowerUp {

    public PowerUpDecreaseBomberGuySpeed(){
        super();
        setTypeName("DecreaseBomberGuySpeed");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setBomberSpeed(Math.min(ServerConfig.Games.get(ServerConfig.currentGameId).defaultBomberSpeed,getOwner().getBomberSpeed() - ServerConfig.Games.get(ServerConfig.currentGameId).defaultBomberSpeedIncreaseRate));
    }
}
