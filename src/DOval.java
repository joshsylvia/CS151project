import java.awt.*;

public class DOval extends DShape {

 
    public DOval(DShapeModel model) {
        super(model);
    }
    
    public void draw(Graphics g) {
        g.setColor(model.getColor());
        g.fillOval(model.getX(), model.getY(), model.getWidth(), model.getHeight());
        DOvalModel.showKnob(g);

    }
}