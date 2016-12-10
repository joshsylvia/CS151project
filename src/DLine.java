import java.awt.*;
import java.util.*;



public class DLine extends DShape {

	
    public DLine(DShapeModel model) {
		super(model);
		
	}

	public void draw(Graphics g) {
//		g.drawLine(model.getX(), model.getY(), model.getWidth(), model.getHeight());
//        g.setColor(model.getColor());
//        DLineModel.showKnob(g);
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(model.getColor());
        g2.drawLine(model.getX(), model.getY(), model.getX() + model.getHeight(), model.getY() + model.getHeight());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);       
        if(isSelected) {
            Rectangle[] knob = model.getknob();
            for (int i = 0; i < knob.length; i++) {
                g.setColor(Color.BLACK);
                g.fillRect(knob[i].x, knob[i].y, 1, 1);
            }
        }  
    }
}