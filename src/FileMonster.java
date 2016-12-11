import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileMonster {
	static Canvas ref;
	
	public FileMonster(Canvas c){
		ref = c;
		
	}
	
	public void open(File f){
		DShapeModel[] dShapeArray = null;
        try {
            XMLDecoder xmlIn = new XMLDecoder(new BufferedInputStream(
            new FileInputStream(f))); 
            dShapeArray = (DShapeModel[]) xmlIn.readObject();
            xmlIn.close();
            ref.clearCanvas();
            for(DShapeModel dm:dShapeArray) {
            	ref.addShape(dm);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void save(File f){
		try {
	        XMLEncoder xmlOut = new XMLEncoder(
	            new BufferedOutputStream(
	            new FileOutputStream(f)));
	        	ArrayList<DShapeModel> shapes = ref.shapeModelList;
	            DShapeModel[] dShapeModelArray = shapes.toArray(new DShapeModel[0]);
	            xmlOut.writeObject(dShapeModelArray);
	            xmlOut.close();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	public void saveImage(File f){
		DShape temp = ref.selectedShape;
		if(temp != null){
			temp.isSelected = false;
			BufferedImage image = (BufferedImage) ref.whiteBoard1.createImage(ref.whiteBoard1.getWidth(), ref.whiteBoard1.getHeight());
	        Graphics g = image.getGraphics();
	        ref.paintAll(g);
	        g.dispose(); // Good but not required--
	        // dispose() Graphics you create yourself when done with them.
	        try {
	            javax.imageio.ImageIO.write(image, "PNG", f);
	        }
	        catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        temp.isSelected = true;
		}
	}

}
