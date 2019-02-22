package Models;

import Utilies.ServerEngine;

public class PowerUpHalfSize extends PowerUp {

    public PowerUpHalfSize(){
        super();
        setTypeName("HalfSize");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setBomberWidth(getOwner().getBomberWidth()/2);
        getOwner().setBomberHeight(getOwner().getBomberHeight()/2);
        ServerEngine.addUpdate("BomberGuy",getOwner().getId(),"bomberHeight",String.valueOf(getOwner().getBomberHeight()));
        ServerEngine.addUpdate("BomberGuy",getOwner().getId(),"bomberWidth",String.valueOf(getOwner().getBomberWidth()));
        new java.util.Timer().schedule(
                new java.util.TimerTask(){
                    @Override
                    public void run() {
                        getOwner().setBomberWidth(getOwner().getBomberWidth()*2);
                        getOwner().setBomberHeight(getOwner().getBomberHeight()*2);
                        ServerEngine.addUpdate("BomberGuy",getOwner().getId(),"bomberHeight",String.valueOf(getOwner().getBomberHeight()));
                        ServerEngine.addUpdate("BomberGuy",getOwner().getId(),"bomberWidth",String.valueOf(getOwner().getBomberWidth()));
                    }
                }
                ,15000);
    }
}
