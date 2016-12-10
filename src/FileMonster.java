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
            // Create an XMLDecoder around the file
            XMLDecoder xmlIn = new XMLDecoder(new BufferedInputStream(
            new FileInputStream(f))); 
            // Read in the whole array of DotModels
            dShapeArray = (DShapeModel[]) xmlIn.readObject();
            xmlIn.close();
            //ref.clear();
            for(DShapeModel dm:dShapeArray) {
                ref.addShape(dm);
            }
            //setDirty(false);
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
	            // setDirty(false);
	            // Tip: only clear dirty bit *after* all the things that
	            // could fail/throw an exception
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	public void saveImage(File f){
		BufferedImage image = (BufferedImage) ref.whiteBoard1.createImage(ref.whiteBoard1.getWidth(), ref.whiteBoard1.getHeight());
        // Get Graphics pointing to the bitmap, and call paintAll()
        // This is the RARE case where calling paint() is appropriate
        // (normally the system calls paint()/paintComponent())
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
	}

}
