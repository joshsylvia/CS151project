import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Whiteboard extends JFrame{

	public Whiteboard(String title, JPanel panel ){
		
		super( title );       
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    setSize( 800, 400 );
	    setLayout( new BorderLayout() ); 
	   // setContentPane(getContentPane());
	    add(panel, BorderLayout.WEST);
	    
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Canvas canvas = new Canvas();
		Whiteboard board = new Whiteboard("WhiteBoard" , canvas  );
		board.setVisible(true);
	}
}
