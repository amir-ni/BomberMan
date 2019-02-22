package Models;

public class PowerUpIncreasePoint extends PowerUp {

    public PowerUpIncreasePoint(){
        super();
        setTypeName("IncreasePoint");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setScore(getOwner().getScore()+100);
    }
}
