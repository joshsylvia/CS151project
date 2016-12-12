import java.awt.*;
import java.util.*;



public class DText extends DShape  {

 
    public DText( DShapeModel model) {
        super(model);
    }
    
    public void draw(Graphics g) {

        g.setColor(model.getColor());
        g.setFont(new Font(model.getFont(),Font.PLAIN,computeFont()));
        g.drawString(model.getText(), model.getX() ,model.getY() + model.getHeight());
        int width = g.getFontMetrics().stringWidth(model.getText());
        model.setWidth(width);
        ((DTextModel)model).showKnob(g);
        
    }
    
    public int computeFont(){ 
    	double size=1.0;
    	int height = model.getHeight(); 
    	Font font = new Font(model.getFont(), Font.PLAIN, (int)size);
    	FontMetrics metr = new FontMetrics(font){};

    	if(metr.getHeight()<height){
    		while(metr.getHeight()<height){
    			size = (size*1.10)+1;
    			metr = new FontMetrics(font){};
    			font = new Font(model.getFont(), Font.PLAIN, (int)size);
    		}
    		
    	}
    	return (int)size;
    }
    
}