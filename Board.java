//environment game Board class

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class Board extends JPanel
{
	// made public and non-static for testing purposes
	public final int gameWidth = 720;
	public final int gameHeight = 520;
	
	// elements of game
	Cleaner earthCleaner;
	Trash trash;
	
	private Image backgroundImage;
	private Image scaledBackgroundImage;
	
	// holds information for trash left and collected (test and fun)
	private JLabel trashLeft;
	private JLabel trashCollected;
	
	private int trashCollectedScore;
	
	private Game newGame;
	
	// user will press 'New Game' to start game
	boolean gameStarted = false;
	
	// if user has already played, then we know it's the 'fun' round
	boolean testGamePlayed;

	/*
	 *  adding earthCleaner and trash to game
	 */
	public Board(Game game, int difficulty)
	{
		trashCollectedScore = 0;
		
		// set up components of game and tie board to game
		this.newGame = game;
		earthCleaner = new Cleaner(this, difficulty);
		trash = new Trash(this);
		
		// set up board and add panel that counts score on top
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(gameWidth, gameHeight));

		if(testGamePlayed == false)
		{
			trashLeft = new JLabel("TRASH LEFT: " + earthCleaner.trashRequirement);
			trashLeft.setForeground(Color.WHITE);
			add(trashLeft);
		}
		else
		{
			trashCollected = new JLabel("TRASH COLLECTED: " + trashCollectedScore);
			trashCollected.setForeground(Color.WHITE);
			add(trashCollected);
		}
		
		initListeners();
		
        ImageIcon myBackgroundImage = new ImageIcon("/Users/Troy/Desktop/workspace2/EnvironmentGame/environmentBack.jpg");
        backgroundImage = myBackgroundImage.getImage();
        scaledBackgroundImage = backgroundImage.getScaledInstance(gameWidth, gameHeight, Image.SCALE_FAST);
	}
	
	/*
	 *  gets user input from keys
	 */
	public void initListeners()
	{
		KeyListener listener = new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent event)
			{}
			@Override
			public void keyReleased(KeyEvent event)
			{}
			@Override
			public void keyPressed(KeyEvent event)
			{	
				earthCleaner.keyPressed(event);
			}
		};
		setFocusable(true);
		addKeyListener(listener);
	}

	/************************************************
	 * Main Method:
	 * -play the game
	 * -while loop that begins only if user has
	 * started a new game
	 ************************************************/
	public void play() throws InterruptedException
	{	
		trash.getRandomTrashLocation(); // place first piece of trash
		
		// user has pressed "StartGame" on menu
		while(gameStarted)
		{
			if(testGamePlayed == false)
			{
				earthCleaner.moveCleaner();
				repaint();
				
				earthCleaner.edgeContinue();
	
				if(earthCleaner.trashCollected())
				{
					earthCleaner.increaseCleanerSize();
					earthCleaner.decrementTrashRequirement();
					
					if(earthCleaner.trashRequirement == 0)
						gameOver();
					
					trash.getRandomTrashLocation();
					
					trashLeft.setText("TRASH LEFT: " + earthCleaner.trashRequirement);
				}
				
				Thread.sleep(90);
			}
			else if(testGamePlayed == true)
			{
				earthCleaner.moveCleaner();
				repaint();
				
				earthCleaner.edgeContinue();
	
				if(earthCleaner.trashCollected())
				{
					trashCollectedScore++;
					earthCleaner.increaseCleanerSize();
					trash.getRandomTrashLocation();
					
					trashCollected.setText("TRASH COLLECTED: " + trashCollectedScore);
				}
				
				Thread.sleep(90);
			}
		}
	}
	
	/*
	 *  to manipulate private variable
	 */
	public void setGameStarted(boolean hasGameStarted)
	{
		gameStarted = hasGameStarted;
	}
	
	/*
	 *  resets score and distance to allow for new game
	 */
	public void gameOver()
	{
		testGamePlayed = true; // game has been played for the first time
		
		newGame.gameOver();
	}
	
	/*
	 * 	override paint
	 */
	@Override
	public void paint(Graphics myGraphic)
	{
		super.paint(myGraphic);
		Graphics2D my2DGraphic = (Graphics2D) myGraphic;
		my2DGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		earthCleaner.paint(my2DGraphic);
		trash.paint(my2DGraphic);
	}
	
	//http://stackoverflow.com/questions/19125707/simplest-way-to-set-image-as-jpanel-background
	@Override
	protected void paintComponent(Graphics background) 
	{
	    super.paintComponent(background);
	        background.drawImage(scaledBackgroundImage, 0, 0, null);
	}
	
}