package Models;

public class PowerUpDecreasePoint extends PowerUp {

    public PowerUpDecreasePoint(){
        super();
        setTypeName("DecreasePoint");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setScore(getOwner().getScore()-100);
    }
}