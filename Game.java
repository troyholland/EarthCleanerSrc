//environment game Game class
//don't think this would be the applet class
//applet class would be main one and would create an instance of this one

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
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
	private JPanel instructionPanelEarth;
	private JPanel instructionPanelOcean;
	
	private JPanel summaryPage;
	
	/*
	 * set up window and menu
	 */
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
		
		JButton startGame = new JButton("Let's Clean the Earth!");
		JButton instructions = new JButton("Go Here First!");
		startGame.setFont(new Font("Comic Sans", Font.BOLD, 12));
		instructions.setFont(new Font("Comic Sans", Font.BOLD, 12));
		startGame.addActionListener(new menuButtonListener());
		instructions.addActionListener(new menuButtonListener());
		
		// add to panel
		buttonPanel.add(instructions);
		buttonPanel.add(startGame);
		
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
			if(gameChoice == "Let's Clean the Earth!")
			{
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
			else if(gameChoice == "Go Here First!")
			{	
				// set up
				if(newBoard.testGamePlayed == false)
					setUpInstructionsPageEarth();
				else
					setUpInstructionsPageOcean();
				
				validate();
				repaint();
			}
		}
	}
	
	/*
	 *  instructions page test
	 */
	public void setUpInstructionsPageEarth()
	{
		// remove playing board
		remove(newBoard);
		
		// entire instruction panel
		instructionPanelEarth = new JPanel();
		instructionPanelEarth.setBackground(Color.WHITE);
		instructionPanelEarth.setLayout(new BorderLayout());
		
		// labels that hold instruction info
		// if, else for all of this, depending on if it is test or fun game
		JLabel titleLabel = new JLabel("<html>Clean the Earth!</html>");
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		
		// game instructions
		JPanel listOfInstructionsPanel = new JPanel();
		listOfInstructionsPanel.setOpaque(false);
		listOfInstructionsPanel.setLayout(new BoxLayout(listOfInstructionsPanel, BoxLayout.PAGE_AXIS));
		
		JLabel instLabel1= new JLabel("<html><br>INSTRUCTIONS:</html>");
		JLabel instLabel2= new JLabel("<html>1) You are the Earth Cleaner! Clean the Earth!</html>");
		JLabel instLabel3= new JLabel("<html><br>2) Your job is to pick up trash and correctly recycle it according to the trash type"
				+ " and recycling bin.</html>");
		JLabel instLabel4= new JLabel("<html><br>3) You will be asked to collect and sort plastic, glass, aluminum, and paper trash.</html>");
		JLabel instLabel5= new JLabel("<html><br>4) Trees will be planted as you recycle and help the earth!</html>");
		JLabel instLabel6= new JLabel("<html><br>5) The trash requirement you must reach is displayed on the top of the screen.</html>");
		JLabel instLabel7= new JLabel("<html><br>6) Just remember, <B>most things you throw away can be recycled!!<B></html>");
		
		// font edits
		instLabel1.setFont(new Font("Verdana", Font.ITALIC, 22));
		instLabel2.setFont(new Font("Verdana", Font.PLAIN, 18));
		instLabel3.setFont(new Font("Verdana", Font.PLAIN, 18));
		instLabel4.setFont(new Font("Verdana", Font.PLAIN, 18));
		instLabel5.setFont(new Font("Verdana", Font.PLAIN, 18));
		instLabel6.setFont(new Font("Verdana", Font.PLAIN, 18));
		instLabel7.setFont(new Font("Verdana", Font.PLAIN, 18));
		
		listOfInstructionsPanel.add(instLabel1);
		listOfInstructionsPanel.add(instLabel2);
		listOfInstructionsPanel.add(instLabel3);
		listOfInstructionsPanel.add(instLabel4);
		listOfInstructionsPanel.add(instLabel5);
		listOfInstructionsPanel.add(instLabel6);
		listOfInstructionsPanel.add(instLabel7);
		
		// back button to return to main menu
		JPanel backButtonPanel = new JPanel();
		backButtonPanel.setBackground(Color.WHITE);
		JButton back = new JButton("Back");
		back.setFont(new Font("Helvetica", Font.BOLD, 12));
		back.setForeground(Color.BLACK);
		back.addActionListener(new backButtonListener());
		backButtonPanel.add(back);
		
		// information / facts on page
		JPanel listOfInformationPanel = new JPanel();
		listOfInformationPanel.setOpaque(false);
		listOfInformationPanel.setLayout(new BoxLayout(listOfInformationPanel, BoxLayout.PAGE_AXIS));
		
        ImageIcon myEarthImage = new ImageIcon(this.getClass().getResource("/earthImage.png"));
        JLabel infoImage1 = new JLabel(myEarthImage);
        JLabel infoImage2 = new JLabel(myEarthImage);
        
        JLabel infoLabel1 = new JLabel("<html><br><B> Fast Fact: </B>\"In the United States<br> alone, there are 4 million plastic <br> bottles used every hour. "
        		+ "And of those <br>4 million plastic bottles roughly 25 <br>percent of them get recycled.\" </html>");
		infoLabel1.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		JLabel infoLabel2 = new JLabel("<html><br><B> Fast Fact: </B>\"Every ton of paper that<br> is recycled saves 17 trees, 463 <br>gallons of oil, and saves 5 "
				+ "yards <br>of landfill space.\"</html>");
		infoLabel2.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		
        listOfInformationPanel.add(titleLabel);
        listOfInformationPanel.add(infoImage1);
        listOfInformationPanel.add(infoLabel1);
        listOfInformationPanel.add(infoImage2);
        listOfInformationPanel.add(infoLabel2);
        
		// add to panel
		instructionPanelEarth.add(listOfInformationPanel, BorderLayout.LINE_START);
		instructionPanelEarth.add(listOfInstructionsPanel, BorderLayout.CENTER);
		instructionPanelEarth.add(backButtonPanel, BorderLayout.PAGE_END);

		// add panel
		add(instructionPanelEarth);
	}
	
	/*
	 *  instructions page fun
	 */
	public void setUpInstructionsPageOcean()
	{
		// remove playing board
		remove(newBoard);
		
		// entire instruction panel
		instructionPanelOcean = new JPanel();
		instructionPanelOcean.setBackground(Color.WHITE);
		instructionPanelOcean.setLayout(new BorderLayout());
		
		// labels that hold instruction info
		// if, else for all of this, depending on if it is test or fun game
		JLabel titleLabel = new JLabel("<html>Clean the Earth!</html>");
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		
		// game instructions
		JPanel listOfInstructionsPanel = new JPanel();
		listOfInstructionsPanel.setOpaque(false);
		listOfInstructionsPanel.setLayout(new BoxLayout(listOfInstructionsPanel, BoxLayout.PAGE_AXIS));
		
		JLabel instLabel1= new JLabel("<html><br>INSTRUCTIONS:</html>");
		JLabel instLabel2= new JLabel("<html>1) You are the Ocean Cleaner! Save the Ocean from the Polluter!</html>");
		JLabel instLabel3= new JLabel("<html><br>2) Your job is to pick up trash as the Polluter pollutes and carry it (one by one) to any garbage bin.</html>");
		JLabel instLabel4= new JLabel("<html><br>3) However, if at any time, the ocean is polluted with <B>10</B> pieces of trash, you lose!</html>");
		JLabel instLabel5= new JLabel("<html><br>4) On yeah, don't let him get you!</html>");
		JLabel instLabel6= new JLabel("<html><br>5) You must reach 10! Your score is displayed at the top.</html>");
		JLabel instLabel7= new JLabel("<html><br>6) Just remember, <B>we can't let the Polluters win and pollute the earth! We must work to keep it clean!</B></html>");
		
		// font edits
		instLabel1.setFont(new Font("Verdana", Font.ITALIC, 22));
		instLabel2.setFont(new Font("Verdana", Font.PLAIN, 18));
		instLabel3.setFont(new Font("Verdana", Font.PLAIN, 18));
		instLabel4.setFont(new Font("Verdana", Font.PLAIN, 18));
		instLabel5.setFont(new Font("Verdana", Font.PLAIN, 18));
		instLabel6.setFont(new Font("Verdana", Font.PLAIN, 18));
		instLabel7.setFont(new Font("Verdana", Font.PLAIN, 18));
		
		listOfInstructionsPanel.add(instLabel1);
		listOfInstructionsPanel.add(instLabel2);
		listOfInstructionsPanel.add(instLabel3);
		listOfInstructionsPanel.add(instLabel4);
		listOfInstructionsPanel.add(instLabel5);
		listOfInstructionsPanel.add(instLabel6);
		listOfInstructionsPanel.add(instLabel7);
		
		// back button to return to main menu
		JPanel backButtonPanel = new JPanel();
		backButtonPanel.setBackground(Color.WHITE);
		JButton back = new JButton("Back");
		back.setFont(new Font("Helvetica", Font.BOLD, 12));
		back.setForeground(Color.BLACK);
		back.addActionListener(new backButtonListener());
		backButtonPanel.add(back);
		
		// information / facts on page
		JPanel listOfInformationPanel = new JPanel();
		listOfInformationPanel.setOpaque(false);
		listOfInformationPanel.setLayout(new BoxLayout(listOfInformationPanel, BoxLayout.PAGE_AXIS));
		
        ImageIcon myEarthImage = new ImageIcon(this.getClass().getResource("/earthImage.png"));
        JLabel infoImage1 = new JLabel(myEarthImage);
        JLabel infoImage2 = new JLabel(myEarthImage);
        
        JLabel infoLabel1 = new JLabel("<html><br><B> Fast Fact: </B>\"The Great Pacific Garbage<br> Patch is located in the North Pacific Gyre <br>off the coast of California and is the<br> largest ocean garbage"
        		+ " site in the world. <br>This floating mass of plastic is twice the size <br>of Texas, with plastic pieces outnumbering <br>sea life six to one.\" </html>");
		infoLabel1.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		JLabel infoLabel2 = new JLabel("<html><br><B> Fast Fact: </B>\"One million sea birds and <br>100,000 marine mammals are killed<br> annually from plastic in our oceans.\"</html>");
		infoLabel2.setFont(new Font("Helvetica", Font.ROMAN_BASELINE, 14));
		
        listOfInformationPanel.add(titleLabel);
        listOfInformationPanel.add(infoImage1);
        listOfInformationPanel.add(infoLabel1);
        listOfInformationPanel.add(infoImage2);
        listOfInformationPanel.add(infoLabel2);
        
		// add to panel
		instructionPanelOcean.add(listOfInformationPanel, BorderLayout.LINE_START);
		instructionPanelOcean.add(listOfInstructionsPanel, BorderLayout.CENTER);
		instructionPanelOcean.add(backButtonPanel, BorderLayout.PAGE_END);

		// add panel
		add(instructionPanelOcean);
	}
	
	/*
	 *  back button
	 */
	class backButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			// remove instructions information
			if(newBoard.testGamePlayed == false)
				remove(instructionPanelEarth);
			else
				remove(instructionPanelOcean);
			
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
	public void gameOverEarth()
	{
		String gameOverMessage = "YAY! YOU'VE HELPED TO CLEAN THE EARTH!";
		JOptionPane.showMessageDialog(this, gameOverMessage, "REQUIREMENT COMPLETE", JOptionPane.PLAIN_MESSAGE);
		
		// game has now ended, so, set to false
		newBoard.setGameStarted(false);
		
		nextScreen(); // for mine
		
		// open up next page
		//appletMain.gameToOcean() // for group
		
		//remakeGame();
	}
	
	/*
	 *  if fun game over, create pop up window
	 */
	public void gameOverOcean(int trashCollectedScore)
	{
		String gameOverMessage = "NO! HE GOT THE BETTER OF YOU!";
		JOptionPane.showMessageDialog(this, gameOverMessage, "ARGH!", JOptionPane.PLAIN_MESSAGE);
		
		// game has now ended, so, set to false
		newBoard.setGameStarted(false);
		
		if(trashCollectedScore >= 20)
		{
			//appletMain.gameToSum() // for group
		}
		else
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
}
