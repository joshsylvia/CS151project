import java.awt.*;
import java.io.Serializable;


/**
 * Stores the data for the shapes on canvas
 * Stores x and y coordinates
 * Store width and height of shape
 */
public class DShapeModel implements Serializable {
	
    private static int knobSize; 
    protected static Rectangle[] knobs; 

    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private Rectangle rect;
    

    public DShapeModel() {
        knobSize = 6;
        knobs = new Rectangle[4];
        x = (int) (Math.random() * 750);
        y = (int) (Math.random() * 550);
        width = 50;
        height = 50;
        color = Color.GRAY;
        rect = new Rectangle(x, y, width, height);        
    }
 
    public void setX(int x) {
    	this.x = x;
    }
    public void setY(int y) {
    	this.y = y;
    }    
    public void setWidth(int w) {
    	width = w;
    }    
    public void setHeight(int h) {
    	height = h;
    }    
 
    public void setColor(Color c) {
    	color = c;
    }    
    public int getX() {
    	return x;
    }
    public int getY() {
    	return y;
    }
    public int getWidth() {
    	return width;
    }
    public int getHeight() {
    	return height;
    }
    public Color getColor() {
    	return color;
    }
    //Updates the knobs rectangle position when dragged or resized
   public void setShapeRectangle() {
       rect = new Rectangle (x, y, width, height);
   }
   public Rectangle getShapeRectangle() {
       return rect;
   }
   
   public static String getShape(DShape shape) {
	   if (shape instanceof DRect) return "rectangle";
	   // TODO add more instances;
	   return "test";
   }
   
   public static Rectangle[] getKnobs() {
/*
       String[] topLeft = Canvas.selectedTopLeft.split(",");
       String[] topRight = Canvas.selectedTopRight.split(",");
       String[] bottomLeft = Canvas.selectedBottomLeft.split(",");
       String[] bottomRight = Canvas.selectedBottomRight.split(",");

       knobs[0] = new Rectangle(Integer.parseInt(topLeft[0]), Integer.parseInt(topLeft[1]), knobSize, knobSize);
       knobs[1] = new Rectangle(Integer.parseInt(topRight[0]) - knobSize, Integer.parseInt(topRight[1]), knobSize, knobSize);
       knobs[2] = new Rectangle(Integer.parseInt(bottomLeft[0]), Integer.parseInt(bottomLeft[1])- knobSize, knobSize, knobSize);
       knobs[3] = new Rectangle(Integer.parseInt(bottomRight[0]) - knobSize, Integer.parseInt(bottomRight[1])- knobSize, knobSize, knobSize);
*/
       return knobs;
   }
}
