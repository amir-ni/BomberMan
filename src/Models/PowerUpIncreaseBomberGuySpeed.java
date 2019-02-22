package Models;

import Config.ServerConfig;

public class PowerUpIncreaseBomberGuySpeed extends PowerUp {

    public PowerUpIncreaseBomberGuySpeed(){
        super();
        setTypeName("IncreaseBomberGuySpeed");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setBomberSpeed(getOwner().getBomberSpeed() + ServerConfig.Games.get(ServerConfig.currentGameId).defaultBomberSpeedIncreaseRate);

    }
}
