import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Whiteboard extends JFrame{

	public Whiteboard(String title, JPanel controls , JPanel whiteBoard){
		
		super( title );       
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    setSize( 800, 400 );
	    setLayout( new BorderLayout() ); 
	   // setContentPane(getContentPane());
	    add(controls, BorderLayout.WEST);
	    add(whiteBoard, BorderLayout.EAST);
	    
	}
	
	public static void main(String[] args) {
		
		JPanel whiteBoard = new JPanel();
		whiteBoard.setPreferredSize(new Dimension(400, 400));
		whiteBoard.setBackground(Color.lightGray);
		Canvas canvas = new Canvas();
		Whiteboard board = new Whiteboard("WhiteBoard" , canvas , whiteBoard );
		board.setVisible(true);
	}
}
