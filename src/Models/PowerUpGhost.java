package Models;

public class PowerUpGhost extends PowerUp {

    public PowerUpGhost(){
        super();
        setTypeName("Ghost");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setGhost(true);
    }
}
