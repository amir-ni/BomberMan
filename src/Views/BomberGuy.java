package Views;

import Config.LocalConfig;
import Utilies.Direction;
import Utilies.LocationPoint;

import java.awt.*;
import java.io.File;

public class BomberGuy extends Drawable {

    public boolean isAlive;
    public Direction currentDirection;
    public int moveAnimationState;
    public int bomberHeight;
    public int bomberWidth;
    public LocationPoint location = new LocationPoint(0,0);
    public String name;
    public int bomberGuyViewModelId;
    public boolean ghost;

    public BomberGuy(long id) {
        super(id);
    }


    @Override
    public void render(Graphics2D G) {
        if(!isAlive || currentDirection == null || bomberHeight == 0 || bomberWidth==0 || location.getX()==0 || location.getY()==0)
            return;
        drawingImage = LocalConfig.bomberGuyBufferedImage[bomberGuyViewModelId][(currentDirection.getValue()-1)][moveAnimationState];
        Color tempColor = G.getColor();
        Font tempFont = G.getFont();
        G.setColor(Color.BLACK);
        try {
            G.setFont(Font.createFont(Font.TRUETYPE_FONT,new File(LocalConfig.gameFontLocation)).deriveFont(10.0f));
        }catch (Exception e){e.printStackTrace();}

        G.drawString(name,location.getX(),location.getY());
        G.setColor(tempColor);
        G.setFont(tempFont);
        G.drawImage(drawingImage.getScaledInstance(bomberWidth, bomberHeight, Image.SCALE_SMOOTH),
                location.getX() , location.getY(),
                new Color(0, 0, 0, 0), null);
    }
}
