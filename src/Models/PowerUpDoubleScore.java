package Models;

public class PowerUpDoubleScore extends PowerUp{

    public PowerUpDoubleScore(){
        super();
        setTypeName("DoubleScore");
    }

    @Override
    public void powerUpUsed() {
        getOwner().setScoreMultiplier(getOwner().getScoreMultiplier()*2);
        new java.util.Timer().schedule(
                new java.util.TimerTask(){
                    @Override
                    public void run() {
                        getOwner().setScoreMultiplier(getOwner().getScoreMultiplier()/2);
                    }
                }
                ,15000);

    }
}
