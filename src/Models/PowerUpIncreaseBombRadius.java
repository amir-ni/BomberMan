package Models;

public class PowerUpIncreaseBombRadius extends PowerUp {

    public PowerUpIncreaseBombRadius(){
        super();
        setTypeName("IncreaseBombRadius");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setBombExplodingRange(getOwner().getBombExplodingRange()+1);

    }
}
