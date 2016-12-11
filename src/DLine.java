import java.awt.*;
import java.util.*;



public class DLine extends DShape {
	private final int KNOBSIZE = 9;
	private int x;
	private int y;
	private int x1;
	private int y1;
	
    public DLine(DShapeModel model) {
		super(model);	

	}

	public void draw(Graphics g) {
		x = model.getX();
		y = model.getY();
		x1 = x + model.getWidth();
		y1 = y + model.getHeight();
		
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(model.getColor());
        g2.drawLine(x + KNOBSIZE/2, y + KNOBSIZE/2, x1 - KNOBSIZE/2, y1 - KNOBSIZE/2);
        ((DLineModel)model).showKnob(g2);

    }
}