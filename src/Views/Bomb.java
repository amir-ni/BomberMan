package Views;

import Config.LocalConfig;

import java.awt.*;

public class Bomb extends Drawable{

    public int cellX;
    public int cellY;
    public java.time.LocalDateTime destructionTime;
    public int destructionState;
    public int bombExplodingRange;

    public Bomb(long id){
        super(id);
        this.drawingImage = Config.LocalConfig.bombBufferedImage;
    }

    @Override
    public void render(Graphics2D G) {
        if(destructionTime == null || cellY == 0 || cellX==0 || destructionState==3)
            return;
        switch (destructionState){
            case 0:

                G.drawImage(drawingImage,
                        cellX * LocalConfig.cellSize, cellY * LocalConfig.cellSize,
                         null);
                break;
            case 1:
                for(int i = this.bombExplodingRange;i>=((-1)*this.bombExplodingRange);i--) {
                    G.drawImage(LocalConfig.bombFirstAnimationBufferedImage.getScaledInstance(LocalConfig.cellSize, LocalConfig.cellSize, Image.SCALE_SMOOTH),
                            (cellX+i) * LocalConfig.cellSize, (cellY) * LocalConfig.cellSize,
                            new Color(0, 0, 0, 0), null);
                    G.drawImage(LocalConfig.bombFirstAnimationBufferedImage.getScaledInstance(LocalConfig.cellSize, LocalConfig.cellSize, Image.SCALE_SMOOTH),
                            (cellX) * LocalConfig.cellSize, (cellY+i) * LocalConfig.cellSize,
                            new Color(0, 0, 0, 0), null);
                }
                break;
            case 2:
                for(int i = this.bombExplodingRange;i>=((-1)*this.bombExplodingRange);i--) {
                    G.drawImage(LocalConfig.bombSecondAnimationBufferedImage.getScaledInstance(LocalConfig.cellSize, LocalConfig.cellSize, Image.SCALE_SMOOTH),
                            (cellX+i) * LocalConfig.cellSize, (cellY) * LocalConfig.cellSize,
                            new Color(0, 0, 0, 0), null);
                    G.drawImage(LocalConfig.bombSecondAnimationBufferedImage.getScaledInstance(LocalConfig.cellSize, LocalConfig.cellSize, Image.SCALE_SMOOTH),
                            (cellX) * LocalConfig.cellSize, (cellY+i) * LocalConfig.cellSize,
                            new Color(0, 0, 0, 0), null);
                }
                break;
        }
        //G.setComposite(temp);
    }
}
