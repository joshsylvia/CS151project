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
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class Canvas extends JPanel {
	
	
	private JPanel whiteBoard1;// = new JPanel();
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
	
	private JComboBox fontComboBox  ;
	JLabel fontTesterLabel = new JLabel("This is a sample");
	Font selectedFont ;
	Color selectedBackground;
	JPanel tablePanel = new JPanel();
	JTextField xCoordinate =  new JTextField("X"); 
	JTextField yCoordinate =  new JTextField("Y"); 
	JTextField width =  new JTextField("Width"); 
	JTextField height =  new JTextField("Height"); 
	

	//400 x 400
	JPanel p = new JPanel() ;
	public Canvas (){

		whiteBoard1 = new whiteBoard1();
		setSize(new Dimension(800,400));
		setBackground(Color.BLUE);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		whiteBoard1.setPreferredSize(new Dimension(400, 400));
		whiteBoard1.setBackground(Color.WHITE);
		
		add(whiteBoard1);
		addShapeButtons();
		add(setColor);
		
		setUpFontChooser();
		setUpMoveButtons();
		setUpTable();
		//this is a test.
		addRowToTable(10 , 10 , 111, 58 );
		addRowToTable(15 , 16 , 121, 60 );
		addRowToTable(14 , 11 , 111, 58 );
		
	}
	
	
	private void setUpTable() {

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(300, 200));
		table.setPreferredSize(new Dimension(300, 200));
		table.setGridColor(Color.blue);
		//table.setTableHeader( table_model );
		
		tablePanel.add(scrollPane);
		add(tablePanel);
	}

	
	
	public void addRowToTable( int x, int y, int width , int height){
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.addRow(new Object[]{x ,  y, width , height});
		
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
	
	class whiteBoard1 extends JPanel {
	
	whiteBoard1() {
		setPreferredSize(new Dimension(400,400));
		setBackground(Color.WHITE);
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
				shapes.add(new DRect(new DRectModel()));
			    repaint();
				
			}else if (text.equalsIgnoreCase("oval")){
				// actions for drawing the rectangle here. 
				shapes.add(new DOval(new DOvalModel()));
			    repaint();

			}else if (text.equalsIgnoreCase("line")){
				// actions for drawing the rectangle here. 
				shapes.add(new DLine(new DLineModel()));
			    repaint();
			}else if (text.equalsIgnoreCase("text")){
				// actions for drawing the rectangle here. 
				shapes.add(new DText(new DTextModel()));
			    repaint();
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

			}
		}
	};

	
	ActionListener fontListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox source = (JComboBox) e.getSource();
		    String item = (String) source.getSelectedItem();
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
				
			}
			
		}
	};
	
	// Page 3, paragraph 3
	public void addShape(DShape shape) {	
	
		//Whiteboard.addShape(shape);

	}
}
