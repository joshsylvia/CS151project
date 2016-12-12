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

import javax.swing.JPanel;

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
		DShape temp = ref.selectedShape;
		if(temp != null)
			temp.model.setIsSelected(false);
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
		if(temp != null)
        	temp.model.setIsSelected(true);
	}
	
	public void saveImage(File f){
		DShape temp = ref.selectedShape;
		if(temp != null)
			temp.model.setIsSelected(false);
		JPanel board = ref.whiteBoard1;
		BufferedImage image = (BufferedImage) board.createImage(ref.whiteBoard1.getWidth(), ref.whiteBoard1.getHeight());
		Graphics g = image.getGraphics();
        board.print(g);
        g.dispose(); // Good but not required--
        // dispose() Graphics you create yourself when done with them.
        try {
            javax.imageio.ImageIO.write(image, "PNG", f);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        if(temp != null)
        	temp.model.setIsSelected(true);
	}

}
