import java.io.Serializable;

public class DShape implements Serializable {
    public DShapeModel model;
    public static boolean isSelected = false;

    public DShape(DShapeModel smodel) {
        model = smodel;
    }

    public void setModel(DShapeModel smodel) {
        model = smodel;
    }

    public DShapeModel getModel() {
        return model;
    }
}
