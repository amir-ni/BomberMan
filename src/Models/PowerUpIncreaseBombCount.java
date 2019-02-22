package Models;

public class PowerUpIncreaseBombCount extends PowerUp {

    public PowerUpIncreaseBombCount(){
        super();
        setTypeName("IncreaseBombCount");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setMaxBombsCount(getOwner().getMaxBombsCount()+1);
    }
}


