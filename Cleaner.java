//environmental game Cleaner class

import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Cleaner 
{
	//holds the coordinates of the cleaner
	public List<Integer>cleanerXCoordinates = new ArrayList<Integer>();
	public List<Integer>cleanerYCoordinates = new ArrayList<Integer>();
	
	// starting point and size of cleaner
	// all public for unit tests
	public int cleanerHeadX;
	public int cleanerHeadY;
	public int cleanerLength;
	
	//cleaner will start moving left to right as game begins
	public int cleanerXDirection;
	public int cleanerYDirection;
	
	//the length of each part of the cleaner
	private static final int cleanerSize = 40;
	private static final int trashSize = 40;
	private static final int treeSize = 40;
	
	// board for game
	private Board newBoard;
	
	// get all of the images
	private Image earthCleanerImage;
	private Image earthCleanerImageScaled;
	private Image trashImage;
	private Image scaledTrashImage;
	private Image treeImage;
	private Image scaledTreeImage;
	
	// amount of trash cleaner must pick up
	public int trashRequirement;
	
	// constructor
	public Cleaner(Board board, int difficulty)
	{
		// initialize cleaner information
		cleanerHeadX = 120;
		cleanerHeadY = 120;
		cleanerLength = 1;
		cleanerXDirection = 1;
		cleanerYDirection = 0;
		
		this.newBoard = board;
		this.trashRequirement = difficulty;
		
		// initial size of 1 @ (120, 120)
		cleanerXCoordinates.add(120);
		cleanerYCoordinates.add(120);
		
		// EARTH CLEANER IMAGE
        ImageIcon myEarthCleanerImage = new ImageIcon("/Users/Troy/Desktop/workspace2/EnvironmentGame/garbageManImage.gif");
        earthCleanerImage = myEarthCleanerImage.getImage();
        earthCleanerImageScaled = earthCleanerImage.getScaledInstance(cleanerSize, cleanerSize, Image.SCALE_FAST);
        
        // TRASH IMAGE
        ImageIcon myTrashImage = new ImageIcon("/Users/Troy/Desktop/workspace2/EnvironmentGame/trashImage.png");
        trashImage = myTrashImage.getImage();
        scaledTrashImage = trashImage.getScaledInstance(trashSize, trashSize, Image.SCALE_FAST);
        
        // TREE IMAGE
        ImageIcon myTreeImage = new ImageIcon("/Users/Troy/Desktop/workspace2/EnvironmentGame/treeImage.png");
        treeImage = myTreeImage.getImage();
        scaledTreeImage = treeImage.getScaledInstance(treeSize, treeSize, Image.SCALE_FAST);
	}
	
	/************************************************
	 * Main Cleaner Methods:
	 * - > move
	 * - > pick up garbage
	 ************************************************/
	/*
	 * gets key input (arrows)
	 */
	public void keyPressed(KeyEvent event)
	{		
		// right
		if(event.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			// not going left then asked to go right (backwards)
			if(cleanerXDirection != -1)
			{
				cleanerXDirection = 1;
				cleanerYDirection = 0;
			}
		}
		//left
		if(event.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if(cleanerXDirection != 1)
			{
				cleanerXDirection = -1;
				cleanerYDirection = 0;
			}
		}
		// up
		if(event.getKeyCode() == KeyEvent.VK_UP)
		{
			if(cleanerYDirection != 1)
			{
				cleanerYDirection = -1;
				cleanerXDirection = 0;
			}
		}
		// down
		if(event.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if(cleanerYDirection != -1)
			{
				cleanerYDirection = 1;
				cleanerXDirection = 0;
			}
		}
	}
	/*
	 *  process of moving the cleaner
	 */
	public void moveCleaner()
	{		
		// update body of earthCleaner FIRST, will do head after
		for(int size = cleanerLength - 1; size > 0; size--)
		{
			cleanerXCoordinates.set(size, cleanerXCoordinates.get(size - 1));
			cleanerYCoordinates.set(size, cleanerYCoordinates.get(size - 1));
		}
		
		// based on key pressed, change head position (position 0)
		// position of character in 'fun' mode
		if(cleanerXDirection == 1)
			cleanerXCoordinates.set(0, cleanerXCoordinates.get(0) + cleanerSize);
		if(cleanerXDirection == -1)
			cleanerXCoordinates.set(0, cleanerXCoordinates.get(0) - cleanerSize);
		if(cleanerYDirection == 1)
			cleanerYCoordinates.set(0, cleanerYCoordinates.get(0) + cleanerSize);
		if(cleanerYDirection == -1)
			cleanerYCoordinates.set(0, cleanerYCoordinates.get(0) - cleanerSize);
		
		// update head information
		cleanerHeadX = cleanerXCoordinates.get(0);
		cleanerHeadY = cleanerYCoordinates.get(0);
	}
	
	/*
	 * if earthCleaner hits any part of window's edge, continue from window's opposite side
	 */
	public void edgeContinue()
	{
		// get head location
		cleanerHeadX = cleanerXCoordinates.get(0);
		cleanerHeadY = cleanerYCoordinates.get(0);

		// if head hits any part of edge then cleaner moves to opposite edge
		if(cleanerHeadX == newBoard.gameWidth)
		{
			cleanerXCoordinates.set(0, 0);
			cleanerHeadX = 0;
			cleanerYDirection = 0;
		}
		else if(cleanerHeadX == -40)
		{
			cleanerXCoordinates.set(0, newBoard.gameWidth - 40);
			cleanerHeadX = newBoard.gameWidth - 40;
			cleanerYDirection = 0;
		}
		else if(cleanerHeadY == newBoard.gameHeight)
		{
			cleanerYCoordinates.set(0, 0);
			cleanerHeadY = 0;
			cleanerXDirection = 0;
		}
		else if(cleanerHeadY == -40)
		{
			cleanerYCoordinates.set(0, newBoard.gameHeight - 40);
			cleanerHeadY = newBoard.gameHeight - 40;
			cleanerXDirection = 0;
		}
	}
	
	/*
	 *  check if trash intersects with cleaner's head
	 */
	public boolean trashCollected()
	{
		return newBoard.trash.getTrashBounds().intersects(getCleanerBounds());
	}
	
	/*
	 * collide with trash man
	 */
	public boolean trashManCollision()
	{
		return newBoard.trashMan.getTrashManBounds().intersects(getCleanerBounds());
	}
	
	/*
	 * collide with trash made from trash guy
	 */
	public boolean trashMadeCollected()
	{
		int trashElementCollected = 0;
		boolean trashMadeCollision = false;
		
		for(trashElementCollected = 0; trashElementCollected < newBoard.trashMan.producedTrash; trashElementCollected++)
		{
			// if cleaner hits any trash
			if(newBoard.trashMan.getTrashMadeBounds(trashElementCollected).intersects(getCleanerBounds()))
			{
				trashMadeCollision = true;
				
				// shift list if updated
				newBoard.trashMan.updateTrashList(trashElementCollected);
				break;
			}
		}
		
		return trashMadeCollision;
	}

	/*
	 *  allows us to check for collisions between objects
	 */
	public Rectangle getCleanerBounds()
	{
		return new Rectangle(cleanerHeadX, cleanerHeadY, cleanerSize, cleanerSize);
	}
	
	/*
	 *  increment by one
	 */
	public void increaseCleanerSize()
	{
		cleanerLength++;
		
		// add to end of current cleaner what previous last part of cleaner was
		cleanerXCoordinates.add(cleanerXCoordinates.get(cleanerLength-2));
		cleanerYCoordinates.add(cleanerYCoordinates.get(cleanerLength-2));
	}
	
	/*
	 *  decrement by one
	 */
	public void decrementTrashRequirement()
	{
		trashRequirement--;
	}
	
	/*
	 *  for reseting the game
	 */
	public void resetCleaner(int headX, int headY, int xDirection, int yDirection, int size)
	{
		cleanerHeadX = headX;
		cleanerHeadY = headY;
		cleanerXDirection = xDirection;
		cleanerYDirection = yDirection;
		cleanerLength = size;
		
		resetCleanerCoordiantes();
	}
	
	public void resetCleanerCoordiantes()
	{
		cleanerXCoordinates.clear();
		cleanerYCoordinates.clear();
		cleanerXCoordinates.add(120);
		cleanerYCoordinates.add(120);
	}
	
	/*
	 * paints cleaner, garbage, and trees, if necessary
	 */
	public void paint(Graphics graphicToDraw)
	{	
		int garbageCollected = cleanerLength - 1;
        int treesToDraw =  garbageCollected / 5;
        int garbageToDraw = garbageCollected - (treesToDraw * 5);
        
		graphicToDraw.drawImage(earthCleanerImageScaled, cleanerHeadX, cleanerHeadY, newBoard);
        
		if(treesToDraw > 0)
		{
			for(int size = 1; size < treesToDraw + 1; size++)
			{
				graphicToDraw.drawImage(scaledTreeImage, cleanerXCoordinates.get(size), cleanerYCoordinates.get(size), 
						newBoard);
			}
		}
		
		for(int size = treesToDraw + 1; size < treesToDraw + 1 + garbageToDraw; size++)
		{
			graphicToDraw.drawImage(scaledTrashImage, cleanerXCoordinates.get(size), cleanerYCoordinates.get(size), 
					newBoard);
		}
	}
}