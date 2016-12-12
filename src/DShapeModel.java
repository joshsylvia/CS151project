import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Stores the data for the shapes on canvas
 * Stores x and y coordinates
 * Store width and height of shape
 */
public class DShapeModel {
	
    int knobize; 
    protected static Rectangle[] knob; 
    boolean isSelected;
    ArrayList<ModelListener> listenerList = new ArrayList<ModelListener>();
    int x;
    int y;
    int width;
    int height;
    Color color;
    Rectangle rect;
    int ID;

    public DShapeModel() {
    	setID(0);
    	knobize = 9;
        knob = new Rectangle[4];
        x = (int) (Math.random() * 350);
        y = (int) (Math.random() * 350);
        width = 50;
        height = 50;
        color = Color.GRAY;
        text = "Hello";
        rect = new Rectangle(x, y, width, height);
        setIsSelected(false);
    }

    String text;
    String font; 
    
 
    
    public void setText(String t) {
    	text = t;
    	notifyListeners();
    }
    
    public String getText() {
    	return text;
    }
    
    
    public void setFontName(String f) {
        font = f;
        notifyListeners();
    }
    
    public String getFontName() {
        return font;
    }
    
    public void setX(int x) {
    	this.x = x;
    	notifyListeners();
    }
    public void setY(int y) {
    	this.y = y;
    	notifyListeners();
    }    
    public void setWidth(int w) {
    	width = w;
    	notifyListeners();
    }    
    public void setHeight(int h) {
    	height = h;
    	notifyListeners();
    }    
 
    public void setColor(Color c) {
    	color = c;
    	notifyListeners();
    }    
    
    public void setID(int n){
    	ID = n;
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
    public int getID(){
    	return ID;
    }
    public Color getColor() {
    	return color;
    }

    public boolean getIsSelected(){
    	return isSelected;
    }
    
    public void setIsSelected(boolean b){
    	isSelected = b;
    	notifyListeners();
    }

   public void setShapeRectangle() {
       rect = new Rectangle (x, y, width, height);
       notifyListeners();
   }
   public Rectangle getShapeRectangle() {
       return rect;
   }
   
   
   public void updateRect(){
	   rect = new Rectangle(x, y, width, height);
	   notifyListeners();
   }
   

   public void mimic(DShapeModel other) {
       setID(other.getID());
       setColor(other.getColor());
       notifyListeners();
   }
   
   public Rectangle[] getknob() {

       String[] topLeft = Canvas.topLeft.split(",");
       String[] topRight = Canvas.topRight.split(",");
       String[] bottomLeft = Canvas.bottomLeft.split(",");
       String[] bottomRight = Canvas.bottomRight.split(",");

//       knob[0] = new Rectangle(Integer.parseInt(topLeft[0]), Integer.parseInt(topLeft[1]), knobize, knobize);
//       knob[1] = new Rectangle(Integer.parseInt(topRight[0]) - knobize, Integer.parseInt(topRight[1]), knobize, knobize);
//       knob[2] = new Rectangle(Integer.parseInt(bottomLeft[0]), Integer.parseInt(bottomLeft[1])- knobize, knobize, knobize);
//       knob[3] = new Rectangle(Integer.parseInt(bottomRight[0]) - knobize, Integer.parseInt(bottomRight[1])- knobize, knobize, knobize);

       knob[0] = new Rectangle((this.getX()),(getY()), knobize, knobize);
       knob[1] = new Rectangle(((this.getX() + this.getWidth()) - knobize),(getY()), knobize, knobize);
       knob[2] = new Rectangle((this.getX()),((getY() + this.getHeight()) - knobize), knobize, knobize);
       knob[3] = new Rectangle(((this.getX() + this.getWidth() - knobize)),((getY() + this.getHeight() - knobize)), knobize, knobize);
       return knob;
   }
   
   public String toString(){
	   String s = "";
	   s+= "Model ID: " + ID + " Pos: (" + x + "," + y + ") Size: " + 
	   width + " x " + height + " Color: " + color + " " + "IsSelected: " +
			   isSelected + " " + super.toString();
	   return s;
   }
   
   public void notifyListeners(){
	   Iterator iter = listenerList.iterator();
	   while(iter.hasNext()){
		   ModelListener m = (ModelListener) iter.next();
		   m.modelChanged(this);
	   }
   }
   
   public void register(ModelListener m){
	   listenerList.add(m);
   }
   
   public void unRegister(ModelListener m){
	   listenerList.remove(m);
   }
}
