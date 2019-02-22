package Models;

public class PowerUpDecreaseBombCount extends PowerUp {

    public PowerUpDecreaseBombCount(){
        super();
        setTypeName("DecreaseBombCount");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setMaxBombsCount(Math.min(1,getOwner().getMaxBombsCount()-1));
    }
}
