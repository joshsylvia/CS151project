import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ServerMonster implements ModelListener{
	
	private final boolean DEBUG_MODE = false;
	private int shapeCounter = 1;
	private Canvas ref;
	private ClientHandler clientHandler;
    private ServerAccepter serverAccepter;
    boolean inServerMode = false;
    boolean inClientMode = false;
 // List of object streams to which we send data
    private java.util.List<ObjectOutputStream> outputs =
        new ArrayList<ObjectOutputStream>();
    private ArrayList<DShapeModel> shapeList;

	public ServerMonster(Canvas c){
		ref = c;
		shapeList = ref.shapeModelList;
	}
	
	public void becomeServer(){
		if(!inServerMode && !inClientMode){
			ref.statusL.setText("Starting server");
	        String result = JOptionPane.showInputDialog("Run server on port", "39587");
	        if (result!=null) {
	            System.out.println("server: start");
	            serverAccepter = new ServerAccepter(Integer.parseInt(result.trim()));
	            serverAccepter.start();
	        } 
		} else {
        	System.out.println("Error: already in " + (inServerMode ? "server":"client") + " mode");
        }
		
	}
	
	public void becomeClient(){
		if(!inServerMode && !inClientMode){
			ref.statusL.setText("Starting client");
	        String result = JOptionPane.showInputDialog("Connect to host:port", "127.0.0.1:39587");
	        if (result!=null) {
	            String[] parts = result.split(":");
	            System.out.println("client: start");
	            clientHandler = new ClientHandler(parts[0].trim(), Integer.parseInt(parts[1].trim()));
	            clientHandler.start();
	            
	        }
		}else {
        	System.out.println("Error: already in " + (inServerMode ? "server":"client") + " mode");
        }
		
	}

	@Override
	public void modelChanged(DShapeModel model) {
		// if from model itself, command is always change
		if(inServerMode)
			sendRemote("change", model);
	}
	
	public void addShape(DShapeModel dsm){
		if(inServerMode){
			dsm.setID(shapeCounter++);
			sendRemote("add", dsm);
		}
			
	}
	
	public void move(String s, DShapeModel dsm){
		if(inServerMode){
			sendRemote(s, dsm);
		}
	}
	
	public void remove(DShapeModel dsm){
		if(inServerMode){
			sendRemote("remove", dsm);
		}
	}
	
	public void updateNewClient(ObjectOutputStream oos){
		// send oos a command to clear its screen
		// cycle through shape list and send add command for all shapes
		if(DEBUG_MODE){
			System.out.println("updating new client, starting shapeList state:");
			printShapeList();
		}
		Iterator iter = shapeList.iterator();
		while(iter.hasNext()){
			DShapeModel m = (DShapeModel)iter.next();
			DShapeModel[] models = {m};
			OutputStream memStream = new ByteArrayOutputStream();
	        XMLEncoder xmlOut = new XMLEncoder(
		            new BufferedOutputStream(memStream));
	        xmlOut.writeObject(models);
	        xmlOut.close();
	        String xmlString = memStream.toString();
	        if(DEBUG_MODE){
				System.out.println("Sending model: " + m.toString());
				System.out.println("XMLString sent: " + xmlString);
			}
	        try {
	        	oos.writeObject("add");
	            oos.flush();
	            oos.writeObject(xmlString);
	            oos.flush();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            // Cute use of iterator and exceptions --
	            // drop that socket from list if have probs with it
	        }
		}
		
        
	}
	
	public synchronized void sendRemote(String command, DShapeModel model) {
		OutputStream memStream = new ByteArrayOutputStream();
        XMLEncoder xmlOut = new XMLEncoder(
	            new BufferedOutputStream(memStream));
        DShapeModel[] models = {model};
        xmlOut.writeObject(models);
        xmlOut.close();
        String xmlString = memStream.toString();
        if(DEBUG_MODE){
			System.out.println("Sending command: " + command);
			System.out.println("Sending model: " + model.toString());
			System.out.println("XMLString sent: " + xmlString);
		}
		        // Now write that xml string to all the clients.
        Iterator<ObjectOutputStream> it = outputs.iterator();
        while (it.hasNext()) {
            ObjectOutputStream out = it.next();
            try {
                out.writeObject(command);
                out.flush();
                out.writeObject(xmlString);
                out.flush();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                it.remove();
                // Cute use of iterator and exceptions --
                // drop that socket from list if have probs with it
            }
        }
    }
	
	class ServerAccepter extends Thread {
	    private int port;
	    ServerAccepter(int port) {
	        this.port = port;
	    }
	    public void run() {
	        try {
	            ServerSocket serverSocket = new ServerSocket(port);
	            ref.statusL.setText("In Server Mode");
	            inServerMode = true;
	            ref.activateServerState();
	            tagCurrentShapes();
	            while (true) {
	                Socket toClient = null;
	                // this blocks, waiting for a Socket to the client
	                toClient = serverSocket.accept();
	                System.out.println("server: got client");
	                ObjectOutputStream oos = new ObjectOutputStream(toClient.getOutputStream());
	                updateNewClient(oos);
	                addOutput(oos);
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace(); 
	            ref.statusL.setText("");
	        }
	    }
	}
	
	private class ClientHandler extends Thread {
        private String name;
        private int port;
        ClientHandler(String name, int port) {
            this.name = name;
            this.port = port;
        }
        public void run() {
            try {
                Socket toServer = new Socket(name, port);
                ObjectInputStream in = new ObjectInputStream(toServer.getInputStream());
                System.out.println("client: connected!");
                ref.statusL.setText("In Client Mode");
                inClientMode = true;
                ref.activateClientState();
                ref.clearCanvas();
                while (true) {
                    String command = (String) in.readObject();
                    String xmlString = (String) in.readObject();
                    DShapeModel[] dShapeArray = null;
                    XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xmlString.getBytes()));
                    dShapeArray = (DShapeModel[]) decoder.readObject();
                    DShapeModel model = dShapeArray[0];
                    if(DEBUG_MODE){
                    	System.out.println("\nxmlString received: " + xmlString);
                    	System.out.println("Command received: " + command);
                    	System.out.println("ShapeModel received: " + model.toString());
                    }
                    invokeToGUI(command, model);
                }
            }
            catch (Exception ex) {
               ex.printStackTrace();
               ref.statusL.setText("");
            }
       }
   }

	public synchronized void addOutput(ObjectOutputStream out) {
	    outputs.add(out);
	}
	
	public void invokeToGUI(String command, DShapeModel model) {
		SwingUtilities.invokeLater( new Runnable() {
            public void run() {
            	if(command.equals("add")){
            		//ref.shapeModelList.add(model);
            		ref.addShape(model);
            		if(DEBUG_MODE){
            			System.out.println("In add - ShapeList state: ");
            			printShapeList();
            		}
            	} else if (command.equals("remove")){
            		int index = getModelIndex(model);
            		shapeList.remove(index);
            		ref.repaint();
            		if(DEBUG_MODE){
            			System.out.println("In remove - ShapeList state: ");
            			printShapeList();
            		}
            	} else if (command.equals("change")){
            		DShapeModel myModel = findMyModel(model);
            		if(myModel != null){
            			updateMyModel(myModel, model);
            		} else {
            			System.out.println("No matching model found in client");
            		}
            		if(DEBUG_MODE){
            			System.out.println("In change - ShapeList state: ");
            			printShapeList();
            		}
            	} else if (command.equals("front")){
            		int index = getModelIndex(model);
            		if(index >= 0){
            			if(index != (shapeList.size()-1)){
            				DShapeModel temp = shapeList.get(index);
            				//shapeList.set(index+1, model);
            				//shapeList.set(index, temp);
            				shapeList.remove(temp);
            				shapeList.add(temp);
            				ref.repaint();
            			}
            		}
            		if(DEBUG_MODE){
            			System.out.println("In front - ShapeList state: ");
            			printShapeList();
            		}
            	} else if (command.equals("back")){
            		int index = getModelIndex(model);
            		if(index > 0){
            			DShapeModel temp = shapeList.get(index);
            			shapeList.remove(temp);
            			shapeList.add(0, temp);
            			ref.repaint();
            			
            		}
            		if(DEBUG_MODE){
            			System.out.println("In back - ShapeList state: ");
            			printShapeList();
            		}
            	} else {
            		System.out.println("Error: command not recognized");
            	}
            }
        });
    }
	
	public DShapeModel findMyModel(DShapeModel m){
		//System.out.println("In findModel, mID: " + m.getID());
		DShapeModel result = null;
		Iterator iter = ref.shapeModelList.iterator();
		while(iter.hasNext()){
			DShapeModel mine = (DShapeModel) iter.next();
			//System.out.println("Now comparing m with: " + mine.getID());
			if(mine.getID() == m.getID())
				return mine;
		}
		return result;
	}
	
	public int getModelIndex(DShapeModel m) {
		int result = -1;
		for(int i = 0; i < shapeList.size(); i++){
			if(shapeList.get(i).getID() == m.getID())
				return i;
		}
		return result;
	}
	public void updateMyModel(DShapeModel myModel, DShapeModel newModel){
		myModel.setX(newModel.getX());
		myModel.setY(newModel.getY());
		myModel.setHeight(newModel.getHeight());
		myModel.setWidth(newModel.getWidth());
		myModel.setColor(newModel.getColor());
		myModel.setID(newModel.getID());
		myModel.setIsSelected(newModel.getIsSelected());
		myModel.setFontName(newModel.getFontName());
		myModel.setText(newModel.getText());
		ref.repaint();
	}
	
	private void tagCurrentShapes(){
		Iterator iter = shapeList.iterator();
		while(iter.hasNext()){
			DShapeModel dsm = (DShapeModel)iter.next();
			dsm.setID(shapeCounter++);
		}
	}
	
	private void printShapeList(){
		Iterator iter = shapeList.iterator();
		while(iter.hasNext()){
			System.out.println(iter.next().toString());
		}
	}
	
}


