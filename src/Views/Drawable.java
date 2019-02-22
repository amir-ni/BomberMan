package Views;

import java.awt.*;

public abstract class Drawable {
    Drawable(long id){
        this.id = id;
    }
    protected long id;
    protected Image drawingImage;

    public abstract void render(Graphics2D G);

    public void setDrawingImage(Image drawingImage) {
        this.drawingImage = drawingImage;
    }

    public long getId() {
        return id;
    }
}
