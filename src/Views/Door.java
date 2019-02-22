package Views;

import Config.LocalConfig;

import java.awt.*;

public class Door extends Drawable{
    public int cellX;
    public int cellY;
    public boolean hidden;

    public Door(long id){
        super(id);
        this.drawingImage = LocalConfig.doorBufferedImage;
        this.hidden = true;
    }

    @Override
    public void render(Graphics2D G) {
        if(!hidden)
        G.drawImage(drawingImage.getScaledInstance(LocalConfig.cellSize, LocalConfig.cellSize, Image.SCALE_SMOOTH),
                cellX * LocalConfig.cellSize, cellY * LocalConfig.cellSize,
                new Color(0, 0, 0, 0), null);


    }
}
