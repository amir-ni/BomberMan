package Views;

import Config.LocalConfig;

import java.awt.*;

public class Obstacle extends Drawable {
    public int cellX;
    public int cellY;
    public  boolean destructed;

    public Obstacle(long id){
        super(id);
        this.drawingImage = LocalConfig.obstacleBufferedImage;
    }

    @Override
    public void render(Graphics2D G) {
        if(!destructed) {
            G.drawImage(drawingImage,
                    cellX * LocalConfig.cellSize, cellY * LocalConfig.cellSize,
                    new Color(0, 0, 0, 0), null);

        }
    }
}
