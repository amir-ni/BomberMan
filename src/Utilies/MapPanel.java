package Utilies;

import Config.LocalConfig;
import Views.Drawable;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel  {

    static int myBomberGuyX = 0;
    static int myBomberGuyY = 0;

    public MapPanel(){
        setBounds(myBomberGuyX, myBomberGuyY,
                LocalConfig.cellCountX * LocalConfig.cellSize, LocalConfig.cellCountY * LocalConfig.cellSize);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellWidth = LocalConfig.cellSize;
        int cellHeight = LocalConfig.cellSize;
        int boundsX = (LocalConfig.fullFrameWidth/2)-myBomberGuyX;
        int boundsY = (LocalConfig.fullFrameHeight/2)-myBomberGuyY;
        if(boundsX > 0)
            boundsX = 0;
        if( LocalConfig.cellCountX * LocalConfig.cellSize - myBomberGuyX < (LocalConfig.fullFrameWidth/2))
            boundsX = (LocalConfig.fullFrameWidth) - LocalConfig.cellCountX * LocalConfig.cellSize ;
        if(boundsY > 0)
             boundsY = 0;
        if( LocalConfig.cellCountY * LocalConfig.cellSize - myBomberGuyY < (LocalConfig.fullFrameHeight/2))
            boundsY = (LocalConfig.fullFrameHeight) - LocalConfig.cellCountY * LocalConfig.cellSize ;
        Graphics2D g2 = (Graphics2D) g;

        setBounds( boundsX ,  boundsY ,
                LocalConfig.cellCountX * LocalConfig.cellSize, LocalConfig.cellCountY * LocalConfig.cellSize);

        for(int i = 0; i < LocalConfig.cellCountX; i++)
            for(int j = 0; j < LocalConfig.cellCountY; j++)
                if((i%2==0 && j%2==0)|| i==0 || j == 0 || i== LocalConfig.cellCountX-1|| j== LocalConfig.cellCountY-1)
                    g2.drawImage(LocalConfig.wallBufferedImage,
                            i * cellWidth , j * cellHeight ,
                            new Color(0, 0, 0, 0), null);
                else
                    g2.drawImage(LocalConfig.grassBufferedImage,
                            i * cellWidth , j * cellHeight ,
                            new Color(0, 0, 0, 0), null);
        synchronized (LocalConfig.LOCK) {
            for (Drawable drawable : LocalConfig.drawables)
                drawable.render(g2);
        }
        if(LocalConfig.isPaused){
            g2.drawImage(LocalConfig.pauseBufferedImage.getScaledInstance(160, 80, Image.SCALE_SMOOTH),
                    LocalConfig.fullFrameWidth/2 - 80 , LocalConfig.fullFrameHeight/2 - 40 ,
                    new Color(0, 0, 0, 0), null);
        }
    }
}
