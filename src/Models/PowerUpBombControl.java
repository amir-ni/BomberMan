package Models;

public class PowerUpBombControl extends PowerUp{

    public PowerUpBombControl(){
        super();
        setTypeName("BombControl");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setBombControl(true);
    }
}
