package Views;

import Config.LocalConfig;

import java.awt.*;

public class PowerUp extends Drawable {

    public boolean isUsed;
    public boolean hidden;
    public int cellX;
    public int cellY;
    public String typeName;

    public PowerUp(long id) {
        super(id);
    }

    @Override
    public void render(Graphics2D G) {
        if(!isUsed && typeName != null && !typeName.isEmpty() && !hidden)
            G.drawImage(drawingImage.getScaledInstance(LocalConfig.cellSize,LocalConfig.cellSize,Image.SCALE_SMOOTH), cellX * LocalConfig.cellSize, cellY * LocalConfig.cellSize, null);
    }
}
