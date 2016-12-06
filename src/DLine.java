import java.awt.*;
import java.util.*;



public class DLine extends DShape {

	
    public DLine(DShapeModel model) {
		super(model);
		
	}

	public void draw(Graphics g) {
	//	Point[] corners = getKnobs();
		g.drawLine(model.getX(), model.getY(), model.getWidth(), model.getHeight());
       g.setColor(Color.black);
    //    for(Point corner: corners) {
      //      g.fillRect(corner.x, corner.y, 9, 9);
  //      }
    }
}