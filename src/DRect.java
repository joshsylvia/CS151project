import java.awt.*;


public class DRect extends DShape {

 
    public DRect(DShapeModel model) {
        super(model);
    }
    
    public void draw(Graphics g) {
        g.setColor(model.getColor());
        g.fillRect(model.getX(), model.getY(), model.getWidth(), model.getHeight());
        DRectModel.showKnob(g);      
    }
}