import java.awt.Graphics;
import java.io.Serializable;

public class DShape implements Serializable, ModelListener {
    public DShapeModel model;
    //public boolean isSelected = false;

    public DShape(DShapeModel smodel) {
        model = smodel;
        model.register(this);
    }

    public void setModel(DShapeModel smodel) {
        model = smodel;
    }

    public DShapeModel getModel() {
        return model;
    }

	@Override
	public void modelChanged(DShapeModel model) {
		
	}
	
	public void draw(Graphics g) {
        draw(g);      
    }
	
	public String toString(){
		String s = "";
		s += "This shape isSelected: " + model.getIsSelected() + " Model: " + model.toString();
		return s;
	}
}
