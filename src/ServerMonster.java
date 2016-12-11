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
	
	private int shapeCounter = 1;
	private Canvas ref;
	private ClientHandler clientHandler;
    private ServerAccepter serverAccepter;
    private boolean inServerMode = false;
    private boolean inClientMode = false;
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
	
	public void updateNewClient(ObjectOutputStream oos){
		// send oos a command to clear its screen
		// cycle through shape list and send add command for all shapes
		Iterator iter = shapeList.iterator();
		while(iter.hasNext()){
			DShapeModel[] models = {(DShapeModel)iter.next()};
			OutputStream memStream = new ByteArrayOutputStream();
	        XMLEncoder xmlOut = new XMLEncoder(
		            new BufferedOutputStream(memStream));
	        xmlOut.writeObject(models);
	        xmlOut.close();
	        String xmlString = memStream.toString();
	        
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
		//System.out.println("Sending command: " + command);
		//System.out.println("Sending model: " + model.toString());
		OutputStream memStream = new ByteArrayOutputStream();
        XMLEncoder xmlOut = new XMLEncoder(
	            new BufferedOutputStream(memStream));
        DShapeModel[] models = {model};
        xmlOut.writeObject(models);
        xmlOut.close();
        String xmlString = memStream.toString();
        //System.out.println("XML encoded DSM: " + xmlString);
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
    // Connect to the server, loop getting messages
        public void run() {
            try {
                // make connection to the server name/port
                Socket toServer = new Socket(name, port);
                // get input stream to read from server and wrap in object input stream
                ObjectInputStream in = new ObjectInputStream(toServer.getInputStream());
                System.out.println("client: connected!");
                ref.statusL.setText("In Client Mode");
                inClientMode = true;
                ref.activateClientState();
                // we could do this if we wanted to write to server in addition
                // to reading
                // out = new ObjectOutputStream(toServer.getOutputStream());
                while (true) {
                    // Get the xml string, decode to a Message object.
                    // Blocks in readObject(), waiting for server to send something.
                    String command = (String) in.readObject();
                    String xmlString = (String) in.readObject();
                    //System.out.println(xmlString);
                    DShapeModel[] dShapeArray = null;
                    XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xmlString.getBytes()));
                    dShapeArray = (DShapeModel[]) decoder.readObject();
                    DShapeModel model = dShapeArray[0];
                    //System.out.println("Command received: " + command);
                    //System.out.println("ShapeModel received: " + model.toString());
                    invokeToGUI(command, model);
                }
            }
            catch (Exception ex) { // IOException and ClassNotFoundException
               ex.printStackTrace();
               ref.statusL.setText("");
            }
            // Could null out client ptr.
            // Note that exception breaks out of the while loop,
            // thus ending the thread.
       }
   }

	//Adds an object stream to the list of outputs
	// (this and sendToOutputs() are synchronzied to avoid conflicts)
	public synchronized void addOutput(ObjectOutputStream out) {
	    outputs.add(out);
	}
	
	public void invokeToGUI(String command, DShapeModel model) {
		//System.out.println("GUIInvoked: " + command + " " + model.toString());
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
            	if(command.equals("add")){
            		ref.shapeModelList.add(model);
            		ref.addShape(model);
            	} else if (command.equals("delete")){
            		
            	} else if (command.equals("change")){
            		DShapeModel myModel = findMyModel(model);
            		if(myModel != null){
            			updateMyModel(myModel, model);
            		} else {
            			System.out.println("No matching model found in client");
            		}
            		
            	} else if (command.equals("front")){
            		
            	} else if (command.equals("back")){
            		
            	} else {
            		System.out.println("Error: command not recognized");
            	}
                //ref.statusL.setText("Client receive");
                //sendLocal(temp);
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
	
	public void updateMyModel(DShapeModel myModel, DShapeModel newModel){
		myModel.setX(newModel.getX());
		myModel.setY(newModel.getY());
		myModel.setHeight(newModel.getHeight());
		myModel.setWidth(newModel.getWidth());
		myModel.setColor(newModel.getColor());
		myModel.setID(newModel.getID());
	}
	
	private void tagCurrentShapes(){
		Iterator iter = shapeList.iterator();
		while(iter.hasNext()){
			DShapeModel dsm = (DShapeModel)iter.next();
			dsm.setID(shapeCounter++);
		}
	}
	
}


