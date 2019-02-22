package Models;

public class PowerUpDecreaseBombRadius extends PowerUp {

    public PowerUpDecreaseBombRadius(){
        super();
        setTypeName("DecreaseBombRadius");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setBombExplodingRange(Math.min(1,getOwner().getBombExplodingRange()-1));
    }
}
