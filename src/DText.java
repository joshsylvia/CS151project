import java.awt.*;
import java.util.*;



public class DText extends DShape {

 
    public DText( DShapeModel model) {
        super(model);
    }
    
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(model.getColor());
        g2.setFont(new Font(Canvas.item,Font.PLAIN,computeFont()));
        g2.drawString(Canvas.textField.getText(), model.getX() ,model.getY() + model.getHeight());
        int width = g.getFontMetrics().stringWidth(Canvas.textField.getText());
        model.setWidth(width);//sets width of the rectangle holding the text
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
 //       DTextModel.showKnobsIfSelected(g);
        
    }
    
    public int computeFont(){ 
    	double size=1.0;
    	int height = model.getHeight(); 
    	Font font = new Font(Canvas.item, Font.PLAIN, (int)size);
    	FontMetrics metr = new FontMetrics(font){};

    	if(metr.getHeight()<height){
    		while(metr.getHeight()<height){
    			size = (size*1.10)+1;
    			metr = new FontMetrics(font){};
    			font = new Font(Canvas.item, Font.PLAIN, (int)size);
    		}
    		
    	}
    	return (int)size;
    }
    
}