import java.awt.*;
import java.util.*;



public class DLine extends DShape {

	
    public DLine(DShapeModel model) {
		super(model);
		
	}

	public void draw(Graphics g) {
		g.drawLine(model.getX(), model.getY(), model.getWidth(), model.getHeight());
        g.setColor(model.getColor());
        DLineModel.showKnob(g);
    }
}