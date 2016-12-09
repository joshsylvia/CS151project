import java.awt.*;

public class DOvalModel extends DShapeModel {
    public DOvalModel() {

    }
    
    public static void showKnob(Graphics g) {
        if(DShape.isSelected) {
            Rectangle[] knob = getKnobs();
            for (int i = 0; i < knobs.length; i++) {
                g.setColor(Color.BLACK);
                g.fillRect(knob[i].x, knob[i].y, knob[i].width, knob[i].height);
            }
        }  
    }
}
