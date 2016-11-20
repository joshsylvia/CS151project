import java.io.Serializable;

public class DShape implements Serializable {
    public DShapeModel model;
   

    public DShape(DShapeModel model) {
        this.model = model;
    }

    public void setModel(DShapeModel model) {
        this.model = model;
    }

    public DShapeModel getModel() {
        return this.model;
    }
}
