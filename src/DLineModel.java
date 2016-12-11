import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class DLineModel extends DShapeModel {

    public DLineModel() {

    }
   
    public void showKnob(Graphics g) {
        if(getIsSelected()) {
            Rectangle[] knob = getknob();
            g.setColor(Color.BLACK);
            g.fillRect(knob[0].x, knob[0].y, knob[0].width, knob[0].height);
            g.fillRect(knob[3].x, knob[3].y, knob[3].width, knob[3].height);

        }    
    }
}
