import java.awt.*;
import java.io.Serializable;


/**
 * Stores the data for the shapes on canvas
 * Stores x and y coordinates
 * Store width and height of shape
 */
public class DShapeModel implements Serializable {

    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private Rectangle shapeRectangle;
    
    // knob ???

    public DShapeModel() {
        this.x = (int) (Math.random() * 750);
        this.y = (int) (Math.random() * 550);
        this.width = 50;
        this.height = 50;
        this.color = Color.GRAY;
        shapeRectangle = new Rectangle(x, y, width, height);
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
    
}
