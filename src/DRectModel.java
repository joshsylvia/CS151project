import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class DRectModel extends DShapeModel {
    public DRectModel() {
    	
    }
    
   public void showKnob(Graphics g) {
        if(isSelected()) {
            Rectangle[] knob = getknob();
            for (int i = 0; i < knob.length; i++) {
                g.setColor(Color.BLACK);
                g.fillRect(knob[i].x, knob[i].y, knob[i].width, knob[i].height);
            }  
        }
        
    }
}
