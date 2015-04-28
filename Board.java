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
	
	// elements of the game
	private Game newGame;
	public Cleaner earthCleaner;
	public Trash trash;
	public TrashMan trashMan;
	
	// images for game background
	private Image backgroundImageEarth;
	private Image scaledBackgroundImageEarth;
	private Image backgroundImageOcean;
	private Image scaledBackgroundImageOcean;
	
	// holds information for trash on screen
	private JLabel trashLeft; // test
	private JLabel trashCollected; // fun
	
	// user will press 'New Game' to start game
	public boolean gameStarted = false;
	
	// for checking to see if test or fun game
	public boolean testGamePlayed = false;
	
	// fun game score
	private int trashCollectedScore;
	
	public Board(Game game, int difficulty)
	{
		trashCollectedScore = 0;
		
		// set up components of game and tie to the board
		this.newGame = game;
		this.earthCleaner = new Cleaner(this, difficulty);
		this.trashMan = new TrashMan(this);
		this.trash = new Trash(this);
		
		// set up board and add panel that counts score on top
		setPreferredSize(new Dimension(gameWidth, gameHeight));

		// in 'test' game display this
		trashLeft = new JLabel("TRASH LEFT: " + earthCleaner.trashRequirement);
		trashLeft.setForeground(Color.WHITE);
		add(trashLeft);

		// in 'fun' game display this
		trashCollected = new JLabel("TRASH COLLECTED: " + trashCollectedScore);
		trashCollected.setForeground(Color.WHITE);
		
		initListeners();
		
		// create background image
        ImageIcon myBackgroundImageEarth = new ImageIcon(this.getClass().getResource("/environmentBack.jpg"));
        backgroundImageEarth = myBackgroundImageEarth.getImage();
        scaledBackgroundImageEarth = backgroundImageEarth.getScaledInstance(gameWidth, gameHeight, Image.SCALE_FAST);
        
		// create background image
        ImageIcon myBackgroundImageOcean = new ImageIcon(this.getClass().getResource("/oceanBackground.jpg"));
        backgroundImageOcean = myBackgroundImageOcean.getImage();
        scaledBackgroundImageOcean = backgroundImageOcean.getScaledInstance(gameWidth, gameHeight, Image.SCALE_FAST);
        
        validate();
        repaint();
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
			{
				earthCleaner.keyReleased();
			}
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
	 * started a new game (pressed 'start game')
	 ************************************************/
	public void play() throws InterruptedException
	{	
		if(testGamePlayed == false)
			trash.getRandomTrashLocation();
		
		// user has pressed "Start Game" on menu
		while(gameStarted)
		{
			// the 'earth' game
			if(testGamePlayed == false)
			{
				earthCleaner.edgeContinue();
				
				repaint();
				
				if(earthCleaner.trashCollected() && earthCleaner.getCleanerLength() == 1)
				{
					earthCleaner.increaseCleanerSize();
					trash.removeTrash();
				}
				
				if(earthCleaner.trashRecycled())
				{
					earthCleaner.decrementTrashRequirement();
					earthCleaner.decreaseCleanerSize();
					trash.getRandomTrashLocation();
					trash.plantTree();
					
					// done when all trash is picked up
					if(earthCleaner.trashRequirement == 0)
						gameOver();
					
					trashLeft.setText("TRASH LEFT: " + earthCleaner.trashRequirement);
				}
				
				//repaint();
				
				Thread.sleep(80);
			}
			// the 'ocean' game
			else if(testGamePlayed == true)
			{
				// show score
				trashCollected.setText("TRASH COLLECTED: " + trashCollectedScore);
				
				// move cleaner and trashMan
				trashMan.moveRandomTrashMan(); // adds trash every 15 moves
				
				repaint();
				
				// allows for move from edge to edge
				earthCleaner.edgeContinue();
	
				// if trash collected
				if(earthCleaner.trashMadeCollected())
					earthCleaner.increaseCleanerSize();
				
				if(earthCleaner.trashMadePickedUp())
				{
					trashCollectedScore++;
					earthCleaner.decreaseCleanerSize();
					trashCollected.setText("TRASH COLLECTED: " + trashCollectedScore);
				}
				
				// run into trashMan
				if(earthCleaner.trashManCollision())
					gameOver();
				
				// if trash is > 4
				if(trashMan.producedTrash > 9)
					gameOver();
				
				//repaint(); // repaint after move
				
				Thread.sleep(80);
			}
		}
		
		// for fun level
		remove(trashLeft);
		add(trashCollected);
	}
	
	/*
	 *  to manipulate setGameStarted variable
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
		if(testGamePlayed)
			newGame.gameOverOcean(trashCollectedScore);
		else
			newGame.gameOverEarth();
		
		trashCollectedScore = 0;
		
		// game has been played at least once
		testGamePlayed = true;
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
		
		if(testGamePlayed == false)
			trash.paint(my2DGraphic);
		
		// only add trashMan once 'fun' game started
		if(testGamePlayed)
			trashMan.paint(my2DGraphic);
	}
	/*
	 * for background
	 */
	@Override
	protected void paintComponent(Graphics background) 
	{
	    super.paintComponent(background);
	    if(testGamePlayed == false)
	        background.drawImage(scaledBackgroundImageEarth, 0, 0, null);
	    else
	    	background.drawImage(scaledBackgroundImageOcean, 0, 0, null);
	}
	
}