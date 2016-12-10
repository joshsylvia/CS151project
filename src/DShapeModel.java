import java.awt.*;
import java.io.Serializable;


/**
 * Stores the data for the shapes on canvas
 * Stores x and y coordinates
 * Store width and height of shape
 */
public class DShapeModel implements Serializable {
	
    private static int knobize; 
    protected static Rectangle[] knob; 

    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private Rectangle rect;
    

    public DShapeModel() {
        knobize = 6;
        knob = new Rectangle[4];
        x = (int) (Math.random() * 350);
        y = (int) (Math.random() * 350);
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
    //Updates the knob rectangle position when dragged or resized
   public void setShapeRectangle() {
       rect = new Rectangle (x, y, width, height);
   }
   public Rectangle getShapeRectangle() {
       return rect;
   }
   
   
   public void updateRect(){
	   rect = new Rectangle(x, y, width, height);
   }
   
   public static String getShape(DShape shape) {
	   if (shape instanceof DRect) return "rectangle";
	   // TODO add more instances;
	   return "test";
   }
   
   public static Rectangle[] getknob() {

       String[] topLeft = Canvas.topLeft.split(",");
       String[] topRight = Canvas.topRight.split(",");
       String[] bottomLeft = Canvas.bottomLeft.split(",");
       String[] bottomRight = Canvas.bottomRight.split(",");

       knob[0] = new Rectangle(Integer.parseInt(topLeft[0]), Integer.parseInt(topLeft[1]), knobize, knobize);
       knob[1] = new Rectangle(Integer.parseInt(topRight[0]) - knobize, Integer.parseInt(topRight[1]), knobize, knobize);
       knob[2] = new Rectangle(Integer.parseInt(bottomLeft[0]), Integer.parseInt(bottomLeft[1])- knobize, knobize, knobize);
       knob[3] = new Rectangle(Integer.parseInt(bottomRight[0]) - knobize, Integer.parseInt(bottomRight[1])- knobize, knobize, knobize);

       return knob;
   }
}
