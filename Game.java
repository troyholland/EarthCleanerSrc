//environment game Game class
//don't think this would be the applet class
//applet class would be main one and would create an instance of this one

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Game extends JPanel
{
	// start game or instructions
	private String gameChoice;
	
	// where game will be played
	private Board newBoard;
	private AppletMain appletMain;
	
	// holds main menu information
	private JPanel buttonPanel;
	private JPanel instructionPanel;
	
	private JPanel summaryPage;
	
	/*
	 * set up window and menu
	 */
	// set up board and call init
	public Game(int difficulty, AppletMain myApplet)
	{
		gameChoice = "";
		
		// board to played on
		this.newBoard = new Board(this, difficulty);
		this.appletMain = myApplet;
		
		// set up menu for game
		setUpMenu();
	}
	
	/************************************************
	 * Main Method:
	 * -either directs user to instructions page OR
	 * begins the new game
	 ************************************************/
	/*
	 *  main menu
	 */
	public void setUpMenu()
	{
		// game's JPanel layout 
		setLayout(new BorderLayout());
		
		// create and add main menu buttons to JFrame
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setBackground(Color.WHITE);
		
		JButton startGame = new JButton("Clean the Earth!");
		JButton instructions = new JButton("How Do You Clean It?");
		startGame.setFont(new Font("Comic Sans", Font.BOLD, 12));
		instructions.setFont(new Font("Comic Sans", Font.BOLD, 12));
		startGame.addActionListener(new menuButtonListener());
		instructions.addActionListener(new menuButtonListener());
		
		// add to panel
		buttonPanel.add(startGame);
		buttonPanel.add(instructions);
		
		// add buttons and board to JPanel
		add(newBoard, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);
	}
	
	/*
	 *  listens for button input
	 */
	class menuButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			JButton thisButton = (JButton) event.getSource();
			gameChoice = thisButton.getText();

			// remove main menu buttons so we can update the frame
			remove(buttonPanel);
			
			// updates frame
			validate();
			repaint();
			
			// starts new game
			if(gameChoice == "Clean the Earth!")
			{
				// fits board to screen
				//pack();
				
				// required for .play() to start
				newBoard.setGameStarted(true);
				
				// must start a new thread to run the game as the window updates
			    Thread gameThread = new Thread() {
			        public void run() {
						try{
							newBoard.play();
						}
						catch(InterruptedException e)
						{
							System.out.println("error");
						}
			        }
			    };
			    gameThread.start();
			}
			// goes to instructions page
			else if(gameChoice == "How Do You Clean It?")
			{	
				// set up
				if(newBoard.testGamePlayed == false)
					setUpInstructionsPageTest();
				else
					setUpInstructionsPageFun();
				
				validate();
				repaint();
			}
		}
	}
	
	/*
	 *  instructions page test
	 */
	public void setUpInstructionsPageTest()
	{
		// remove playing board
		remove(newBoard);
		
		// entire instruction panel
		instructionPanel = new JPanel();
		instructionPanel.setBackground(Color.WHITE);
		instructionPanel.setLayout(new BorderLayout());
		
		// labels that hold instruction info
		// if, else for all of this, depending on if it is test or fun game
		JLabel titleLabel = new JLabel("<html>Clean the earth!</html>");
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel centerInstLabel = new JLabel("<html>INSTRUCTIONS:<br>1) Game begins with an 'Earth Cleaner'"
				+ " of size one<br>2) Collect randomly placed trash<br>3) Move with the arrow keys<br>"
				+ "4) Every five pieces of trash collected translates into one tree<br>"
				+ "5) Your environmental requirement is shown at the top of the screen</html>");
		centerInstLabel.setFont(new Font("Helvetica", Font.BOLD, 22));
		
		// set colors of fonts
		titleLabel.setForeground(Color.BLACK);
		centerInstLabel.setForeground(Color.BLACK);
		
		// back button to return to main menu
		JPanel backButtonPanel = new JPanel();
		backButtonPanel.setBackground(Color.WHITE);
		JButton back = new JButton("Back");
		back.setFont(new Font("Helvetica", Font.BOLD, 12));
		back.setForeground(Color.BLACK);
		back.addActionListener(new backButtonListener());
		backButtonPanel.add(back);
		
		// add to panel
		instructionPanel.add(titleLabel, BorderLayout.PAGE_START);
		instructionPanel.add(centerInstLabel, BorderLayout.CENTER);
		instructionPanel.add(backButtonPanel, BorderLayout.PAGE_END);

		// add panel to frame
		add(instructionPanel);
	}
	
	/*
	 *  instructions page fun
	 */
	public void setUpInstructionsPageFun()
	{
		// remove playing board
		remove(newBoard);
		
		// entire instruction panel
		instructionPanel = new JPanel();
		instructionPanel.setBackground(Color.WHITE);
		instructionPanel.setLayout(new BorderLayout());
		
		// labels that hold instruction info
		// if, else for all of this, depending on if it is test or fun game
		JLabel titleLabel = new JLabel("<html>Save the Earth!</html>");
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel centerInstLabel = new JLabel("<html>INSTRUCTIONS:<br>1) You control earth's destiny!"
				+ "<br>2) The trash man wants to pollute the earth!<br>3) Move with the arrow keys<br>"
				+ "4) Every 10 moves, the trash man will drop a piece of trash<br>"
				+ "5) You must collect these pieces of trash before they accumulate to 5 total!"
				+ "<br>6) Don't run into the trash man!</html>");
		centerInstLabel.setFont(new Font("Helvetica", Font.BOLD, 22));
		
		// set colors of fonts
		titleLabel.setForeground(Color.BLACK);
		centerInstLabel.setForeground(Color.BLACK);
		
		// back button to return to main menu
		JPanel backButtonPanel = new JPanel();
		backButtonPanel.setBackground(Color.WHITE);
		JButton back = new JButton("Back");
		back.setFont(new Font("Helvetica", Font.BOLD, 12));
		back.setForeground(Color.BLACK);
		back.addActionListener(new backButtonListener());
		backButtonPanel.add(back);
		
		// add to panel
		instructionPanel.add(titleLabel, BorderLayout.PAGE_START);
		instructionPanel.add(centerInstLabel, BorderLayout.CENTER);
		instructionPanel.add(backButtonPanel, BorderLayout.PAGE_END);

		// add panel to frame
		add(instructionPanel);
	}
	
	/*
	 *  back button
	 */
	class backButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// remove instructions information
			remove(instructionPanel);
			
			// add original buttons and board and to frame
			add(newBoard, BorderLayout.CENTER);
			add(buttonPanel, BorderLayout.PAGE_END);
			
			validate();
			repaint();
		}
	}
	
	/*
	 *  if test game over, create pop up window
	 */
	public void gameOverTest()
	{
		String gameOverMessage = "YAY! YOU'VE HELPED TO CLEAN THE EARTH!";
		JOptionPane.showMessageDialog(this, gameOverMessage, "REQUIREMENT COMPLETE", JOptionPane.YES_NO_OPTION);
		
		// game has now ended, so, set to false
		newBoard.setGameStarted(false);
		
		//nextScreen(); // get info to advance screens
		
		// open up next page
		//appletMain.gameToSum()
	}
	
	/*
	 *  if fun game over, create pop up window
	 */
	public void gameOverFun()
	{
		String gameOverMessage = "NO! HE GOT THE BETTER OF YOU!";
		JOptionPane.showMessageDialog(this, gameOverMessage, "ARGH!", JOptionPane.YES_NO_OPTION);
		
		// game has now ended, so, set to false
		newBoard.setGameStarted(false);
		
		//nextScreen(); // get info to advance screens
		
		remakeGame();
	}
	
	/*
	 * 	game is finished and get user to advance pages
	 */
	public void nextScreen()
	{
		remove(newBoard); // remove the game board
		
		summaryPage = new JPanel();
		summaryPage.setBackground(Color.WHITE);
		summaryPage.setLayout(new BorderLayout());
		
		JLabel titleSummary = new JLabel("<html>You've Cleaned the Earth!</html>");
		titleSummary.setFont(new Font("Helvetica", Font.BOLD, 32));
		titleSummary.setForeground(Color.BLACK);
		titleSummary.setHorizontalAlignment(JLabel.CENTER);
		
		// back button to return to main menu
		JPanel continueButtonPanel = new JPanel();
		continueButtonPanel.setBackground(Color.WHITE);
		JButton continueButton = new JButton("Continue");
		continueButton.setFont(new Font("Helvetica", Font.BOLD, 16));
		continueButton.addActionListener(new continueButtonListener());
		continueButtonPanel.add(continueButton);
		
		summaryPage.add(titleSummary, BorderLayout.CENTER);
		summaryPage.add(continueButtonPanel, BorderLayout.SOUTH);
		
		add(summaryPage);
		
		validate();
		repaint();
		
		//resetGame(); // resets information
	}
	
	/*
	 *  continue button
	 */
	class continueButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			remove(summaryPage);
			add(newBoard, BorderLayout.CENTER);
			remakeGame();
		}
	}
	
	/*
	 *  re-make game so when you return it is present
	 *  called when you want to play fun game**
	 */
	public void remakeGame()
	{
		resetGame();
		
		// add original buttons
		add(buttonPanel, BorderLayout.PAGE_END);
		
		validate();
		repaint();
	}
	
	/*
	 *  return to main menu and reset game
	 */
	public void resetGame()
	{
		// reset game information
		newBoard.earthCleaner.resetCleaner(120, 120, 1, 0, 1);
		newBoard.trashMan.resetTrashProduced();
	}
	
	/*public static void main(String [] args) throws InterruptedException
	{
		// event dispatch thread
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Game game = new Game(25);
            }
        });
	}*/
}
