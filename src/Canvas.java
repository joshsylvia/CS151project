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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import java.util.prefs.BackingStoreException;

import javax.sound.sampled.Line;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel; 

public class Canvas extends JPanel implements Serializable {
	
	
	private JPanel whiteBoard1;
	static String topLeft;
	static String topRight;
	static String bottomLeft;
	static String bottomRight;
	
	static JTextField textField;
	static String item;
	static ArrayList<DShape> shapes = new ArrayList<DShape>();
	String column_names[]= {"X","Y","Width","Height"};
	DefaultTableModel table_model = new DefaultTableModel(column_names ,0);
	JTable table = new JTable(table_model);
	JButton RectButton, OvalButton , LineButton , TextButton, setColor ;
	JLabel text = new JLabel("Add ");
	
    static DShape selectedShape;
    static int clickedX;
    static int clickedY;
    static int currentW = 50;

    static int diffInX;
    static int diffInY;
    
    boolean isKnobClicked = false;
    static Rectangle knobClicked;
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

		whiteBoard1 = new whiteBoard1();
		setSize(new Dimension(800,400));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		whiteBoard1.setPreferredSize(new Dimension(400, 400));
		whiteBoard1.setBackground(Color.WHITE);
		
		add(whiteBoard1);
		addShapeButtons();
		add(setColor);
		
		setUpFontChooser();
		setUpMoveButtons();
		setUpTable();		

	}
	
	
	private void setUpTable() {

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(300, 200));
        scrollPane.createHorizontalScrollBar();
		table.setGridColor(Color.blue);
		add(scrollPane);
		
	}

	
	public void addRowToTable( int x, int y, int width , int height){
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{x ,  y, width , height});
		
	}
	
	
	public void removeRowFromTable(){
		
		((DefaultTableModel)table.getModel()).removeRow(1);
	}
	
	private void setUpMoveButtons() {
		
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		JButton moveFront = new JButton("Move To Front");
		moveFront.addActionListener(moveListener);
		JButton moveBack = new JButton("Move To Back");
		moveBack.addActionListener(moveListener);
		JButton removeShape = new JButton("Remove Shape");
		removeShape.addActionListener(moveListener);
		jp.add(moveFront);
		jp.add(moveBack);
		jp.add(removeShape);
	
		this.add(jp);
	}

	private void setUpFontChooser() {

		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
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
		this.add(jp);
	
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
		
		this.add(container );
		add(setColor);

	}
	
	class whiteBoard1 extends JPanel  {
	
	whiteBoard1() {
		setPreferredSize(new Dimension(400,400));
		setBackground(Color.WHITE);
		CanvasMouseHandler handler = new CanvasMouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
	}
	
	@Override
	 public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        for(DShape shape: shapes){
	            
	            if(shape instanceof DRect){
	                DRect rectangle = new DRect(shape.model);
	                
	                rectangle.draw(g);
	                
	            }
	            else if(shape instanceof DOval){
	                DOval oval = new DOval(shape.model);
	                oval.draw(g);
	            }
	            else if(shape instanceof DLine){
	            	DLine line = new DLine(shape.model);
	            	line.draw(g);
	            }
	            else if(shape instanceof DText){
	            	DText text = new DText(shape.model);
	            	text.draw(g);
	            }
	        }
	    }
    class CanvasMouseHandler extends MouseAdapter {

        public void mousePressed(MouseEvent e){
            clickedX = e.getX();
            clickedY = e.getY();
                       
            DShape clicked = shapeContains(e.getPoint());
                   
            if(selectedShape != null){
                knobClicked = knobContains(selectedShape, e.getPoint());
                currentW = selectedShape.model.getWidth();
                
                if(knobClicked != null)    isKnobClicked = true;
                
            }
             
            if(clicked != null){
                setSelectedShape(clicked);

                repaint();
            }
            else {
                selectedShape.isSelected = false;
                setSelectedShape(null);

                repaint();
            }
            
        }

        public void setSelectedShape(DShape clicked) {

            if(selectedShape != clicked){
                if(selectedShape != null){
                 selectedShape.isSelected = false;
                }
                selectedShape = clicked;
                if(selectedShape != null){
                	selectedShape.isSelected = true;
                }

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
                    if(knobClicked.equals(selectedShape.model.knob[0])){
                        knobClicked = new Rectangle(e.getX(), e.getY(), 9, 9);
                        resizingUpdate(e.getX(), e.getY(), currentWidth - changeX, currentHeight - changeY);
                    }
                    else if(knobClicked.equals(selectedShape.model.knob[1])){
                        knobClicked = new Rectangle(e.getX() - 9, e.getY(), 9, 9);
                        resizingUpdate(currentX, e.getY(), changeX, currentHeight - changeY);                        
                    }
                    else if(knobClicked.equals(selectedShape.model.knob[2])){
                        knobClicked = new Rectangle(e.getX(), e.getY() - 9, 9, 9);
                        resizingUpdate(e.getX(), currentY, currentWidth - changeX, changeY);
                    }else{
                        knobClicked = new Rectangle(e.getX() - 9, e.getY() - 9, 9, 9);
                        resizingUpdate(currentX, currentY, changeX, changeY);
                    }
                }
                         
                else {
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


                    repaint();

                }
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
			//  System.out.println ( button.getText() );
			String text = button.getText();

			if(text.equalsIgnoreCase("rect")){
				// actions for drawing the rectangle here. 
				DShape  ds = new DRect(new DRectModel()) ;
				addShape(ds);
				
			}else if (text.equalsIgnoreCase("oval")){
				// actions for drawing the rectangle here. 
				addShape(new DOval(new DOvalModel()));
			    

			}else if (text.equalsIgnoreCase("line")){
				// actions for drawing the rectangle here. 
				addShape(new DLine(new DLineModel()));
			    
			}else if (text.equalsIgnoreCase("text")){
				// actions for drawing the rectangle here. 
				addShape(new DText(new DTextModel()));
			   
			}

		}
	};

	ActionListener colorListener = new ActionListener() {
		public void actionPerformed(ActionEvent actionEvent) {
			Color initialBackground = setColor.getBackground();
			selectedBackground = JColorChooser.showDialog(null, "ladimer", initialBackground);
			
			if (selectedBackground != null) {
				System.out.println(selectedBackground.toString());
				// set the color here
				if (selectedShape != null ) {
					selectedShape.model.setColor(selectedBackground);
				} else {
					// set color for next objext that is added
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
			//  System.out.println ( button.getText() );
			String text = button.getText();

			if(text.equalsIgnoreCase("move to front")){
				// actions for move to front here. 
				
				
			}else if (text.equalsIgnoreCase("move to back")){
				// actions for move to back here. 
				
			}else if (text.equalsIgnoreCase("remove shape")){
				// actions for remove shape here. 
				removeRowFromTable();
				
			}
			
		}
	};
	

	public void addShape(DShape shape) {	
	
		shapes.add(shape);
		addRowToTable( shape.getModel().getX() , shape.getModel().getY(),shape.getModel().getWidth(), shape.getModel().getHeight() );
	    repaint();

	}
	
    public void resizingUpdate(int x, int y, int width, int height){
        selectedShape.model.setX(x);
        selectedShape.model.setY(y);
        selectedShape.model.setWidth(width);
        selectedShape.model.setHeight(height);
        selectedShape.model.updateRect();
        setNewCoordinates(selectedShape.model.getX(), selectedShape.model.getY(), selectedShape.model.getWidth(), selectedShape.model.getHeight());
       // updateRow(shapes.indexOf(selectedShape), DShapeModel.getShape(selectedShape), selectedShape.model.getX(), selectedShape.model.getY(), selectedShape.model.getWidth(), selectedShape.model.getHeight());
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
        for(int i = shapes.size() - 1; i >= 0; i--){
            DShape r = shapes.get(i);
            if(r.model.getShapeRectangle().contains(p)) {
                getSelectedShapeCoords(i);
                return r;
            }
        }
        return null;
    }
    public void getSelectedShapeCoords(int i) {
        int shapeWidth = shapes.get(i).model.getWidth();
        int shapeHeight = shapes.get(i).model.getHeight();

        int selectedTopLeftX = shapes.get(i).model.getX();
        int selectedTopLeftY = shapes.get(i).model.getY();

        setNewCoordinates(selectedTopLeftX, selectedTopLeftY, shapeWidth, shapeHeight);
    }
    
    public void setNewCoordinates(int x, int y, int width, int height) {
        topLeft = x + "," + y;
        topRight = (x + width) + "," + y;
        bottomLeft = x + "," + (y + height);
        bottomRight = (x + width) + "," + (y + height);
    }
    
	
}
