import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

public class Whiteboard extends JFrame{

	
	static ArrayList<DShape> shapes = new ArrayList<DShape>();
	
	public Whiteboard(String title, JPanel controls ){

		super( title );       
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    setSize( 430, 800 );
	    setLayout( new BorderLayout() ); 
	   // setContentPane(getContentPane());
	    
		//whiteBoard.setBackground(Color.lightGray);
	    add(controls, BorderLayout.WEST);
	 //   add(canvas, BorderLayout.EAST);
	    
	}
	
	public static void addShape(DShape shape){
		//whiteBoard.add(shape);
		shapes.add(shape);
		
	}
	
	public static void main(String[] args) {

		Canvas canvas = new Canvas();
		Whiteboard board = new Whiteboard("WhiteBoard" , canvas  );
		board.setVisible(true);
	}
}
