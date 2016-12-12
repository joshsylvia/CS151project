import java.awt.*;

public class DOvalModel extends DShapeModel {
    public DOvalModel() {
    	
    }
    
    public void showKnob(Graphics g) {
        if(getIsSelected()) {
            Rectangle[] knob = getknob();
            for (int i = 0; i < knob.length; i++) {
                g.setColor(Color.BLACK);
                g.fillRect(knob[i].x, knob[i].y, knob[i].width, knob[i].height);
            }
        }  
    }
}
