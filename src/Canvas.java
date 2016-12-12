import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import java.util.prefs.BackingStoreException;

import javax.sound.sampled.Line;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel; 

public class Canvas extends JPanel implements Serializable {
	final boolean DEBUG_MODE = false;
	int shapeCounter = 1;
	JPanel controlPanel;
	JPanel whiteBoard1;
	static String topLeft;
	static String topRight;
	static String bottomLeft;
	static String bottomRight;
	
	static JTextField textField;
	static String item;
	ArrayList<DShape> shapes = new ArrayList<DShape>();
	ArrayList<DShapeModel> shapeModelList = new ArrayList<DShapeModel>();
	String column_names[]= {"X","Y","Width","Height"};
	DefaultTableModel table_model = new DefaultTableModel(column_names ,0);
	JTable table = new JTable(table_model);
	JButton RectButton, OvalButton , LineButton , TextButton, setColor ;
	JLabel text = new JLabel("Add ");
	JButton moveFront;
	JButton moveBack;
	JButton removeShape;
    int selectedRow = -1;
	// For Save/open
	FileMonster fileOps;
	JPanel saveOpenPanel;
	JButton saveB, openB, saveImgB;
		
	// For Server
	ServerMonster serverOps;
	JPanel serverPanel;
	JButton server, client;
	JLabel statusL;
		

	Color cColor; 
    DShape selectedShape;
    int clickedX;
    int clickedY;
    int currentW = 50;

    int diffInX;
    int diffInY;
    
    boolean isKnobClicked = false;
    Rectangle knobClicked;
    boolean differenceCalculated = false;
	
	private JComboBox fontComboBox  ;
	JLabel fontTesterLabel = new JLabel("This is a sample");
	
	Font selectedFont ; 
	Color selectedBackground;
	
	JPanel tablePanel = new JPanel();
	JTextField xCoordinate =  new JTextField("X"); 
	JTextField yCoordinate =  new JTextField("Y"); 
	JTextField width =  new JTextField("Width"); 
	JTextField height =  new JTextField("Height"); 
	JPanel p = new JPanel() ;
	
	
	public Canvas (){
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.setPreferredSize(new Dimension(400,400));
		this.setLayout(new BorderLayout());
		fileOps = new FileMonster(this);
		serverOps = new ServerMonster(this);
		cColor = Color.LIGHT_GRAY;
		whiteBoard1 = new WhiteBoard1();
		
		addShapeButtons();
		controlPanel.add(setColor);
		setUpFontChooser();
		setUpMoveButtons();
		setUpTable();		
		setUpSaveOpen();
		setUpServer();
		add(controlPanel, BorderLayout.WEST);
		add(whiteBoard1, BorderLayout.CENTER);
	}
	
	private void setUpSaveOpen(){
		saveOpenPanel = new JPanel();
		saveOpenPanel.setLayout(new FlowLayout());
		saveB = new JButton("Save");
		openB = new JButton("Open");
		saveImgB = new JButton("Save Image");
		saveOpenPanel.add(saveB);
		saveOpenPanel.add(openB);
		saveOpenPanel.add(saveImgB);
		
		openB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String fName = JOptionPane.showInputDialog("File Name", null);
                if (fName != null) {
                    File f = new File(fName);
                    fileOps.open(f);
                }
            }
        });
		
		saveB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String fName = JOptionPane.showInputDialog("File Name", null);
                if (fName != null) {
                    File f = new File(fName);
                    fileOps.save(f);
                }
            }
        });
		
		saveImgB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String fName = JOptionPane.showInputDialog("File Name", null);
                if (fName != null) {
                	fName += ".png";
                    File f = new File(fName);
                    fileOps.saveImage(f);
                }
            }
        });
		controlPanel.add(saveOpenPanel);
	}
	
	private void setUpServer(){
		//Container pane = new Container();
		serverPanel = new JPanel();
		serverPanel.setLayout(new BoxLayout(serverPanel, BoxLayout.X_AXIS));
		JPanel serverBP = new JPanel();
		serverBP.setLayout(new FlowLayout());
		server = new JButton("Server Mode");
		client = new JButton("Client Mode");
		serverBP.add(server);
		serverBP.add(client);
		serverPanel.add(serverBP);
		JPanel statusPane = new JPanel();
		statusPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		statusL = new JLabel("");
		statusL.setHorizontalAlignment(JLabel.RIGHT);
		statusPane.add(statusL);
		serverPanel.add(statusPane);
		server.addActionListener( new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	             serverOps.becomeServer();
	          }
	      });
		
		client.addActionListener( new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	             serverOps.becomeClient();
	          }
	      });
		controlPanel.add(serverPanel);
	}
	
	private void setUpTable() {

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(300, 200));
        scrollPane.createHorizontalScrollBar();
		table.setGridColor(Color.blue);
		controlPanel.add(scrollPane);
		
	}

	
	public void addRowToTable( int x, int y, int width , int height){
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{x ,  y, width , height});
		
	}
	
	
	public void removeRowFromTable( int index){
		
		
		((DefaultTableModel)table.getModel()).removeRow(index);
		//selectedRow = -1 ;
		System.out.println("row is : " +index);
		
	}
	
	private void setUpMoveButtons() {
		
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		moveFront = new JButton("Move To Front");
		moveFront.addActionListener(moveListener);
		moveBack = new JButton("Move To Back");
		moveBack.addActionListener(moveListener);
		removeShape = new JButton("Remove Shape");
		removeShape.addActionListener(moveListener);
		jp.add(moveFront);
		jp.add(moveBack);
		jp.add(removeShape);
	
		controlPanel.add(jp);
	}

	private void setUpFontChooser() {

		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		jp.setPreferredSize(new Dimension(400, 50));
		GraphicsEnvironment gEnv = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		String envfonts[] = gEnv.getAvailableFontFamilyNames();
		Vector vector = new Vector();
		for (int i = 1; i < envfonts.length; i++) {
			vector.addElement(envfonts[i]);
		}
		fontComboBox = new JComboBox(vector);
		fontComboBox.addActionListener(fontListener);


		textField =  new JTextField(10);
		textField.setSize(new Dimension(200, 100));

		jp.add(textField); 
		jp.add(fontComboBox);
		jp.add(fontTesterLabel);
		controlPanel.add(jp);
	
	}

	public void addShapeButtons(){

		JPanel container = new JPanel();
		//commment the line below to get rid of gray background.
		//container.setBackground(Color.LIGHT_GRAY);
		container.setLayout(new FlowLayout());

		text.setBackground(Color.lightGray);
		text.setForeground(Color.BLUE);

		RectButton = new JButton("Rect");
		RectButton.addActionListener( shapeListener);
		OvalButton = new JButton("Oval");
		OvalButton.addActionListener( shapeListener);
		LineButton = new JButton("Line");
		LineButton.addActionListener( shapeListener);
		TextButton = new JButton("Text");
		TextButton.addActionListener( shapeListener);

		setColor = new JButton("Set Color");
		setColor.addActionListener(colorListener);

		container.add(text);
		container.add(RectButton);
		container.add(OvalButton);
		container.add(LineButton);
		container.add(TextButton);
		container.add(setColor);
		
		controlPanel.add(container );
		controlPanel.add(setColor);

	}
	
	class WhiteBoard1 extends JPanel  {
		int width = 400;
		int height = 400;
	WhiteBoard1() {
		setPreferredSize(new Dimension(400,400));
		setBackground(Color.WHITE);
		CanvasMouseHandler handler = new CanvasMouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
	}
	
	public int getWidth(){
    	return width;
    }
	
    public int getHeight(){
    	return height;
    }
    
	@Override
	 public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        shapes.clear();
	        DShape newShape = null;
	        for(DShapeModel shape: shapeModelList){
	            
	            if(shape instanceof DRectModel){
	                newShape = new DRect(shape);  
	            }
	            else if(shape instanceof DOvalModel){
	            	newShape = new DOval(shape);
	            }
	            else if(shape instanceof DLineModel){
	            	newShape = new DLine(shape);
	            }
	            else if(shape instanceof DTextModel){
	            	newShape = new DText(shape);
	            }
	            newShape.draw(g);
	            shapes.add(newShape);
	        }
	        
	    }
    class CanvasMouseHandler extends MouseAdapter {


        public void mousePressed(MouseEvent e){
        	clickedX = e.getX();

            clickedY = e.getY();
            DShape clicked = shapeContains(e.getPoint());
            selectRowFrmTable( clicked.model.getX() , clickedY);
            
            if(selectedShape != null){
            	selectRowFrmTable( selectedShape.model.getX() , clickedY);
                knobClicked = knobContains(selectedShape, e.getPoint());
                currentW = selectedShape.model.getWidth();             
                if(knobClicked != null)
                	isKnobClicked = true;   
            }
             
            if(clicked != null){
            	
            	if(selectedShape != null){
            		selectRowFrmTable( selectedShape.model.getX() , clickedY);
            		selectedShape.model.setIsSelected(false);
            		selectedShape = null;
            	}
                setSelectedShape(clicked);
                repaint();
            } else {
            	if(selectedShape != null){
            		selectedShape.model.setIsSelected(false);
                    selectedShape = null;
                    repaint();
            	}
            	
            }            
        }

		 private void updateTableForXY(int x, int y , int h ,int w ) {
	            table.setValueAt(x, selectedRow, 0); //value, r, c
	            table.setValueAt(y, selectedRow, 1); //value, r, c
	            table.setValueAt(w, selectedRow, 2); //value, r, c
	            table.setValueAt(h, selectedRow, 3); //value, r, c
				
			}
		
        private int selectRowFrmTable(int clickedX, int clickedY) {
		
        	for (int i = table.getRowCount() - 1; i >= 0; --i) {
                for (int j = table.getColumnCount() - 1; j >= 0; --j) {
                    if (table.getValueAt(i, j).equals(clickedX)      ) {
                       System.out.println("row found");
                       selectedRow = i ;
                      // removeRowFromTable(selectedRow);
//                       table.setValueAt(clickedX, i, 0); //value, r, c
//                       table.setValueAt(clickedY, i, 1); //value, r, c
                       return i;
                        
                    }
                }
            }
        	
        	
        	
			return -1;
		}
        
        public void setSelectedShape(DShape clicked) {

            if(selectedShape != clicked){
            	if(selectedShape != null){
                	selectedShape.model.setIsSelected(false);
                }
                selectedShape = clicked;
                selectedShape.model.setIsSelected(true);
                
                // why is this here?
//                if(selectedShape != null){
//                	clicked.model.setIsSelected(true);
//                	cColor = selectedShape.model.getColor();
//                }

            }
            
        }   

        public void mouseDragged(MouseEvent e) {

            if(selectedShape != null){
                
                int currentWidth = selectedShape.model.getWidth();
                int currentHeight = selectedShape.model.getHeight();
                int currentX = selectedShape.model.getX();
                int currentY = selectedShape.model.getY();
                int changeX = e.getX() - currentX;
                int changeY = e.getY() - currentY;
                
                if(isKnobClicked){
                    if(knobClicked.equals(DShapeModel.knob[0])){
                        knobClicked = new Rectangle(e.getX(), e.getY(), 9, 9);
                        if(DEBUG_MODE){
                        	System.out.println("Sending to resizingUpdate: x: " + 
                                    e.getX() + ", y: " + e.getY() + ", width: " + (currentWidth - changeX) +
                                    ", Height: " + (currentHeight - changeY));
                        }
                        resizingUpdate(e.getX(), e.getY(), currentWidth - changeX, currentHeight - changeY);
                    }
                    else if(knobClicked.equals(DShapeModel.knob[1])){
                        knobClicked = new Rectangle(e.getX() - 9, e.getY(), 9, 9);
                        if(DEBUG_MODE){
                        	System.out.println("Sending to resizingUpdate: x: " +
                        			currentX + ", y: " + e.getY() + ", width: " + (changeX) +
                                    ", Height: " + (currentHeight - changeY));
                        }
                                
                        resizingUpdate(currentX, e.getY(), changeX, currentHeight - changeY);                        
                    }
                    else if(knobClicked.equals(DShapeModel.knob[2])){
                        knobClicked = new Rectangle(e.getX(), e.getY() - 9, 9, 9);
                        if(DEBUG_MODE){
                        	System.out.println("Sending to resizingUpdate: x: " +
                        			e.getX() + ", y: " + currentY + ", width: " + (currentWidth - changeX) +
                                    ", Height: " + (changeY));
                        }
                                
                                
                        resizingUpdate(e.getX(), currentY, currentWidth - changeX, changeY);
                    }else{
                        knobClicked = new Rectangle(e.getX() - 9, e.getY() - 9, 9, 9);
                        if(DEBUG_MODE){
                        	System.out.println("Sending to resizingUpdate: x: " + 
                        			 currentX + ", y: " + currentY + ", width: " + (changeX) +
                                     ", Height: " + (changeY));                        
                        }
                        resizingUpdate(currentX, currentY, changeX, changeY);
                    }
                }  else {
                    if (!differenceCalculated) {
                        diffInX = clickedX - selectedShape.model.getX();
                        diffInY = clickedY - selectedShape.model.getY();
                    }
                    differenceCalculated = true;

                    int newX = e.getX() - diffInX;
                    int newY = e.getY() - diffInY;

                    selectedShape.model.setX(newX);
                    selectedShape.model.setY(newY);
                    selectedShape.model.setShapeRectangle();
                    resizingUpdate(newX, newY, currentWidth, currentHeight);
                    repaint();

                }
                updateTableForXY( selectedShape.getModel().getX() , selectedShape.getModel().getY(),
                		selectedShape.getModel().getHeight(), selectedShape.getModel().getWidth() );
          
            }
        }
   
                
  
        public void mouseReleased(MouseEvent e) {
            isKnobClicked = false;
        }
    }
	
	}
	
	//Action Listeners 
	ActionListener shapeListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			String text = button.getText();
			DShapeModel dsm = null;

			if(text.equalsIgnoreCase("rect")){
				dsm = new DRectModel();
				dsm.register(serverOps);
				addShape(dsm);
				
			}else if (text.equalsIgnoreCase("oval")){
				dsm = new DOvalModel();
				dsm.register(serverOps);
				addShape(dsm);
			   
			}else if (text.equalsIgnoreCase("line")){
				dsm = new DLineModel();
				dsm.register(serverOps);
				addShape(dsm);
			    
			}else if (text.equalsIgnoreCase("text")){
				dsm = new DTextModel();
				dsm.register(serverOps);
				addShape(dsm); 
			}
		}
	};

	ActionListener colorListener = new ActionListener() {
		public void actionPerformed(ActionEvent actionEvent) {
			Color initialBackground = setColor.getBackground();
			selectedBackground = JColorChooser.showDialog(null, "choose a color", initialBackground);
			
			if (selectedBackground != null) {
				System.out.println(selectedBackground.toString());
				if (selectedShape != null ) {
					selectedShape.model.setColor(selectedBackground);
					cColor = selectedBackground; 
					repaint();
				} else {
					cColor = selectedBackground; 
				}
			}
			
		}
	};

	
	ActionListener fontListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox source = (JComboBox) e.getSource();
		    item = (String) source.getSelectedItem();
		    selectedFont = new Font(item, Font.PLAIN, 12);
		    fontTesterLabel.setFont(selectedFont);
			
		}
	};
	
	ActionListener moveListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			String text = button.getText();

			if(text.equalsIgnoreCase("move to front")){
				if (selectedShape != null ) {
		            DShape shape = selectedShape;
		            DShapeModel temp = shape.model;
		            shapes.remove(selectedShape);
		            shapeModelList.remove(temp);
		            shapes.add(shapes.size(), shape);
		            shapeModelList.add(temp);
		            serverOps.move("front", temp);
				}
				repaint();
				
			}else if (text.equalsIgnoreCase("move to back")){
				if (selectedShape != null ) {				
					DShape shape = selectedShape;
					DShapeModel temp = shape.model;
					shapes.remove(selectedShape);
					shapeModelList.remove(temp);
					shapes.add(0, shape);
					shapeModelList.add(0, temp);
					serverOps.move("back", temp);
				} 
		        repaint();
		        
			}else if (text.equalsIgnoreCase("remove shape")){
				if(selectedShape != null){
					shapes.remove(selectedShape);
					selectedShape.model.unRegister(serverOps);
					serverOps.remove(selectedShape.model);
					shapeModelList.remove(selectedShape.model);
					selectedShape.model.setIsSelected(false);
					selectedShape = null;
					if(selectedRow != -1){
						removeRowFromTable(selectedRow); 
					}
				}
				//removeRowFromTable(selectedRow); 
				repaint();
				// needs more work remove last one throws error.
			}
			
		}
	};
	
	public void clearCanvas(){
		shapes.clear();
		shapeModelList.clear();
		repaint();
		//how do we empty the table?
	}

	public void addShape(DShape shape) {
		if(shape.getModel().getColor().equals(Color.GRAY))
			shape.getModel().setColor(cColor);
		if (shape.getModel() instanceof DTextModel)
			shape.getModel().setText((String)textField.getText());
		shapes.add(shape);
		addRowToTable( shape.getModel().getX() , shape.getModel().getY(),shape.getModel().getWidth(), shape.getModel().getHeight() );
		repaint();
	}
	
	public void addShape(DShapeModel dsm){
		shapeModelList.add(dsm);
		serverOps.addShape(dsm);
		if(dsm instanceof DRectModel){
			DShape  ds = new DRect(dsm);
			addShape(ds);
		} else if (dsm instanceof DOvalModel){
			DShape  ds = new DOval(dsm);
			addShape(ds);
		} else if (dsm instanceof DLineModel){
			DShape  ds = new DLine(dsm);
			addShape(ds);
		} else if (dsm instanceof DTextModel){
			DShape  ds = new DText(dsm);
			addShape(ds);
		} else {
			System.out.println("Error adding shape");
		}
		
	}
	
    public void resizingUpdate(int x, int y, int width, int height){
        selectedShape.model.setX(x);
        selectedShape.model.setY(y);
        selectedShape.model.setWidth(width);
        selectedShape.model.setHeight(height);
        selectedShape.model.updateRect();
        setNewCoordinates(selectedShape.model.getX(), 
        		selectedShape.model.getY(), 
        		selectedShape.model.getWidth(), 
        		selectedShape.model.getHeight());
       // updateRow
        repaint();
    }
    
    public Rectangle knobContains(DShape shape, Point p){
        for(int i = 0; i < shape.model.knob.length ; i++){
            Rectangle r = shape.model.knob[i];
            if(r.contains(p)){
                return r;
            }
        }
        return null;
    }
    
    public DShape shapeContains(Point p){
    	if(DEBUG_MODE){
    		System.out.println("In shapeContains with point: " + p.toString());
    	}
    	for(int i = shapes.size() - 1; i >= 0; i--){
            DShape r = shapes.get(i);
            if(r.model.getShapeRectangle().contains(p)) {
            	if(DEBUG_MODE){
            		System.out.println("shape found at index " + i + ": " + 
            	r.toString());
            	}
                getSelectedShapeCoords(i);
                return r;
            }
        }
    	if(DEBUG_MODE){
    		System.out.println("No shape found at point");
    	}
        return null;
    }
    
    public void getSelectedShapeCoords(int i) {
        int shapeWidth = shapes.get(i).model.getWidth();
        int shapeHeight = shapes.get(i).model.getHeight();
        int selectedTopLeftX = shapes.get(i).model.getX();
        int selectedTopLeftY = shapes.get(i).model.getY();
        if(DEBUG_MODE){
        	System.out.println("In getSelectedShapeCoords, shapeWidth: " +
        			shapeWidth + ", shapeHeight: " + shapeHeight + " X: " + selectedTopLeftX +
        	        ", Y: " + selectedTopLeftY + "");
        }
        
        setNewCoordinates(selectedTopLeftX, selectedTopLeftY, shapeWidth, shapeHeight);
    }
    
    public void setNewCoordinates(int x, int y, int width, int height) {
    	topLeft = x + "," + y;
        topRight = (x + width) + "," + y;
        bottomLeft = x + "," + (y + height);
        bottomRight = (x + width) + "," + (y + height);
        
    }
    
    public void activateServerState(){
    	openB.setEnabled(false);
    }
    
	public void activateClientState(){
		moveFront.setEnabled(false);
		moveBack.setEnabled(false);
		removeShape.setEnabled(false);
		RectButton.setEnabled(false);
		OvalButton.setEnabled(false);
		LineButton.setEnabled(false);
		TextButton.setEnabled(false);
		setColor.setEnabled(false);
		openB.setEnabled(false);
		MouseListener[] wBML = whiteBoard1.getMouseListeners();
		whiteBoard1.removeMouseListener(wBML[0]);
		MouseMotionListener[] wBMML = whiteBoard1.getMouseMotionListeners();
		whiteBoard1.removeMouseMotionListener(wBMML[0]);
		clearCanvas();
	}
}
