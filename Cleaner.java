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
	
	// board for game
	private Board newBoard;
	
	// get all of the images
	private Image earthCleanerImage;
	private Image earthCleanerImageScaled;
	private Image oceanCleanerImage;
	private Image oceanCleanerImageScaled;
	
	private Image trashImage;
	private Image scaledTrashImage;
	
	private Image plasticTrashImage;
	private Image scaledPlasticTrashImage;
	private Image glassTrashImage;
	private Image scaledGlassTrashImage;
	private Image aluminumTrashImage;
	private Image scaledAluminumTrashImage;
	private Image paperTrashImage;
	private Image scaledPaperTrashImage;
	
	// amount of trash cleaner must pick up
	public int trashRequirement;
	public int trashCollectedType;
	
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
        ImageIcon myEarthCleanerImage = new ImageIcon(this.getClass().getResource("/garbageManImage.gif"));
        earthCleanerImage = myEarthCleanerImage.getImage();
        earthCleanerImageScaled = earthCleanerImage.getScaledInstance(cleanerSize, cleanerSize, Image.SCALE_SMOOTH);
        
        // OCEAN CLEANER IMAGE
        ImageIcon myOceanCleanerImage = new ImageIcon(this.getClass().getResource("/waterCleanerImage.png"));
        oceanCleanerImage = myOceanCleanerImage.getImage();
        oceanCleanerImageScaled = oceanCleanerImage.getScaledInstance(cleanerSize, cleanerSize, Image.SCALE_SMOOTH);
        
        // TRASH PLASTIC
        ImageIcon myTrashImagePlastic = new ImageIcon(this.getClass().getResource("/plasticTrash.png"));
        plasticTrashImage = myTrashImagePlastic.getImage();
        scaledPlasticTrashImage = plasticTrashImage.getScaledInstance(trashSize, trashSize, Image.SCALE_SMOOTH);
        
        // TRASH GLASS
        ImageIcon myTrashImageGlass = new ImageIcon(this.getClass().getResource("/glassTrash.png"));
        glassTrashImage = myTrashImageGlass.getImage();
        scaledGlassTrashImage = glassTrashImage.getScaledInstance(trashSize, trashSize, Image.SCALE_SMOOTH);
        
        // TRASH ALUMINUM
        ImageIcon myTrashImageAluminum = new ImageIcon(this.getClass().getResource("/canTrash.png"));
        aluminumTrashImage = myTrashImageAluminum.getImage();
        scaledAluminumTrashImage = aluminumTrashImage.getScaledInstance(trashSize, trashSize, Image.SCALE_SMOOTH);
        
        // TRASH PAPER
        ImageIcon myTrashImagePaper = new ImageIcon(this.getClass().getResource("/paperTrash.png"));
        paperTrashImage = myTrashImagePaper.getImage();
        scaledPaperTrashImage = paperTrashImage.getScaledInstance(trashSize, trashSize, Image.SCALE_SMOOTH);
        
        // TRASH IMAGE
        ImageIcon myTrashImage = new ImageIcon(this.getClass().getResource("/trashImage.png"));
        trashImage = myTrashImage.getImage();
        scaledTrashImage = trashImage.getScaledInstance(trashSize, trashSize, Image.SCALE_SMOOTH);
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
		
		// if any error occurs and want to stop game
		if(event.getKeyCode() == KeyEvent.VK_Q)
			newBoard.gameOver();
		
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
		trashCollectedType = newBoard.trash.getTrashType(); // gets the trash type
		
		return newBoard.trash.getTrashBounds().intersects(getCleanerBounds());
	}
	
	/*
	 * trash recycled
	 */
	public boolean trashRecycled()
	{
		if(cleanerLength == 2)
		{
			for(int recycleBinType = 0; recycleBinType < 4; recycleBinType++)
			{
				if(newBoard.trash.getRecycleBounds(recycleBinType).intersects(getCleanerBounds()) && 
						recycleBinType == trashCollectedType)
					return true;
			}
		}
		
		return false;
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
		boolean trashMadeCollected = false;
		
		if(cleanerLength == 1)
		{
			for(int trashElementCollected = 0; trashElementCollected < newBoard.trashMan.producedTrash; trashElementCollected++)
			{
				// if cleaner hits any trash
				if(newBoard.trashMan.getTrashMadeBounds(trashElementCollected).intersects(getCleanerBounds()))
				{
					trashMadeCollected = true;
					
					// shift list if updated
					newBoard.trashMan.updateTrashList(trashElementCollected);
					break;
				}
			}
		}
		
		return trashMadeCollected;
	}
	
	public boolean trashMadePickedUp()
	{
		if(cleanerLength == 2)
		{
			for(int trashBin = 0; trashBin < 4; trashBin++)
			{
				if(newBoard.trash.getRecycleBounds(trashBin).intersects(getCleanerBounds()))
					return true;
			}
		}
		
		return false;
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
	 * decrement by one
	 */
	public void decreaseCleanerSize()
	{
		// add to end of current cleaner what previous last part of cleaner was
		cleanerXCoordinates.remove(1);
		cleanerYCoordinates.remove(1);
		
		cleanerLength--;
	}
	
	public int getCleanerLength()
	{
		return cleanerLength;
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
		trashCollectedType = -1;
		
		resetCleanerCoordiantes();
	}
	
	public void resetCleanerCoordiantes()
	{
		cleanerXCoordinates.clear();
		cleanerYCoordinates.clear();
		cleanerXCoordinates.add(120);
		cleanerYCoordinates.add(120);
	}
	
	public void paint(Graphics graphicToDraw)
	{	
		// draw cleaner
		if(newBoard.testGamePlayed == false)
			graphicToDraw.drawImage(earthCleanerImageScaled, cleanerHeadX, cleanerHeadY, newBoard);
		else
			graphicToDraw.drawImage(oceanCleanerImageScaled, cleanerHeadX, cleanerHeadY, newBoard);
		
		// draw cargo
		if(cleanerLength == 2)
		{
			switch(trashCollectedType){
				case 0:
					graphicToDraw.drawImage(scaledPlasticTrashImage, cleanerXCoordinates.get(cleanerLength - 1), 
							cleanerYCoordinates.get(cleanerLength - 1), newBoard);
					break;
				case 1:
					graphicToDraw.drawImage(scaledGlassTrashImage, cleanerXCoordinates.get(cleanerLength - 1), 
							cleanerYCoordinates.get(cleanerLength - 1), newBoard);
					break;
				case 2:
					graphicToDraw.drawImage(scaledAluminumTrashImage, cleanerXCoordinates.get(cleanerLength - 1), 
							cleanerYCoordinates.get(cleanerLength - 1), newBoard);
					break;
				case 3:
					graphicToDraw.drawImage(scaledPaperTrashImage, cleanerXCoordinates.get(cleanerLength - 1), 
							cleanerYCoordinates.get(cleanerLength - 1), newBoard);
					break;
				default: // for 'fun' game
					graphicToDraw.drawImage(scaledTrashImage, cleanerXCoordinates.get(cleanerLength - 1), 
							cleanerYCoordinates.get(cleanerLength - 1), newBoard);
					
			}
		}
        
	}
}