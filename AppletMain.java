//the main file

import javax.swing.JApplet; 
import javax.swing.SwingUtilities;

public class AppletMain extends JApplet {

	private static final int frameWidth = 720;
	private static final int frameHeight = 520;
	
	// where game will be played
	private Game newGame;
	
	// required method for JApplet
	// executed upon creation
	public void init()
	{
		try {
			 SwingUtilities.invokeAndWait(new Runnable() {
			 public void run() {
				 	// would create game under some condition...like user presses 'next' button
					newGame = new Game(10);
					
					// add Game to JApplet
					add(newGame);
			 	}
			 	
			 });
			 } 
		catch (Exception e) {
				 System.err.println("error");
			 }					
		
		// set up JApplet
		setSize(frameWidth, frameHeight);
		setVisible(true);
	}
	
}
