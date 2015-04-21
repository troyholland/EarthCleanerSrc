//environment game Game class

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Game extends JFrame 
{
	private static final int frameWidth = 250;
	private static final int frameHeight = 250;
	
	private String gameChoice; // new game or instructions
	
	// where game will be played
	private Board newBoard;
	
	// holds main menu information
	private JPanel buttonPanel;
	private JPanel instructionPanel;
	
	// set up board and call init
	public Game(int difficulty)
	{
		gameChoice = "";
		
		try{
			// board to played on
			newBoard = new Board(this, difficulty);

			initGUI();
		}
		catch(InterruptedException e)
		{
			System.out.println("error");
		}
	}
	
	/*
	 * set up window and menu
	 */
	public void initGUI() throws InterruptedException
	{
		// create and add main menu buttons to JFrame
		setUpMenu();
		
		// setup JFrame
		setTitle("__Earth_Cleaner__");
		setSize(frameWidth, frameHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
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
		
		// add buttons and board to JFrame
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
				pack();
				
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
				setUpInstructionsPage();
				
				validate();
				repaint();
			}
		}
	}
	
	/*
	 *  instructions page
	 */
	public void setUpInstructionsPage()
	{
		// remove playing board
		remove(newBoard);
		
		// entire instruction panel
		instructionPanel = new JPanel();
		instructionPanel.setBackground(Color.WHITE);
		instructionPanel.setLayout(new BorderLayout());
		
		// labels that hold instruction info
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
	 *  if game over, create pop up window
	 */
	public void gameOver()
	{
		String gameOverMessage = "YAY! YOU'VE HELPED TO CLEAN THE EARTH!";
		JOptionPane.showMessageDialog(this, gameOverMessage, "REQUIRMENT COMPLETE", JOptionPane.YES_NO_OPTION);
		
		// game has now ended
		newBoard.setGameStarted(false);
		
		nextScreen(); // get info to advance screens
	}
	
	/*
	 * 	game is finished and get user to advance pages
	 */
	public void nextScreen()
	{
		remove(newBoard);
		
		JPanel summaryPage = new JPanel();
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
		
		resetGame(); // resets information
	}
	
	/*
	 *  continue button
	 */
	class continueButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// calls some function to generate next page
		}
	}
	
	/*
	 *  remake game so when you return it is present
	 *  called when you want to play fun game
	 */
	public void remakeGame()
	{
		// add original menu options
		add(newBoard, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);
	}
	
	/*
	 *  return to main menu and reset game
	 */
	public void resetGame()
	{
		// reset game information
		newBoard.earthCleaner.resetCleaner(120, 120, 1, 0, 1);
		
		validate();
		repaint();
	}
	
	public static void main(String [] args) throws InterruptedException
	{
		// event dispatch thread
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Game game = new Game(25);
            }
        });
	}
}
