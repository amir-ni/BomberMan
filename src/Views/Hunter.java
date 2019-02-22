package Views;

import Config.LocalConfig;
import Utilies.Direction;
import Utilies.LocationPoint;

import java.awt.*;

public class Hunter extends Drawable {

    public boolean isAlive;
    public Direction currentDirection;
    public int moveAnimationState;
    public LocationPoint location = new LocationPoint(0,0);
    public int hunterViewModelId;
    public boolean isGhost;

    public Hunter(long id) {
        super(id);
    }


    @Override
    public void render(Graphics2D G) {
        if(!isAlive || currentDirection == null || location.getX()==0 || location.getY()==0)
            return;
        drawingImage = LocalConfig.hunterBufferedImage[hunterViewModelId][(currentDirection.getValue()-1)][moveAnimationState];
        G.drawImage(drawingImage,
                location.getX() , location.getY(),
                new Color(0, 0, 0, 0), null);
    }
}
