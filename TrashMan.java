// environment game trashMan class
// functionality for 'fun' game

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class TrashMan 
{
	// holds coordinates of trash made
	public List<Integer>trashMadeXCoordinates = new ArrayList<Integer>();
	public List<Integer>trashMadeYCoordinates = new ArrayList<Integer>();
	
	// holds coordinates of garbageBins
	public List<Integer>garbageBinsXCoordinates = new ArrayList<Integer>();
	public List<Integer>garbageBinsYCoordinates = new ArrayList<Integer>();
	
	// location of trashMan
	public int trashManX;
	public int trashManY;
	
	private static final int trashManSize = 40;
	private static final int trashSize = 40;
	private static final int garbageBinSize = 40;
	private static final int movesTillDrop = 15;
	
	// board for game
	private Board newBoard;
	
	// images to load
	private Image trashImage;
	private Image scaledTrashImage;
	private Image trashManImage;
	private Image scaledTrashManImage;
	private Image garbageBinImage;
	private Image scaledGarbageBinImage;
	
	// for producing trash
	public int movesForTrash;
	public int producedTrash;
	
	public TrashMan(Board board)
	{
		this.newBoard = board;
		
		// start at (280, 280)
		trashManX = 280;
		trashManY = 280;
		
		// set up garbageBin bins coordinates
		garbageBinsXCoordinates.add(newBoard.gameWidth - 3*garbageBinSize);
		garbageBinsYCoordinates.add(2*garbageBinSize);
		garbageBinsXCoordinates.add(2*garbageBinSize);
		garbageBinsYCoordinates.add(2*garbageBinSize);
		garbageBinsXCoordinates.add(newBoard.gameWidth - 3*garbageBinSize);
		garbageBinsYCoordinates.add(newBoard.gameHeight - 3*garbageBinSize);
		garbageBinsXCoordinates.add(2*garbageBinSize);
		garbageBinsYCoordinates.add(newBoard.gameHeight - 3*garbageBinSize);
		
		producedTrash = 0;
		movesForTrash = 0;
		
        // TRASHMAN IMAGE
        ImageIcon myTrashManImage = new ImageIcon(this.getClass().getResource("/trashGuy.png"));
        trashManImage = myTrashManImage.getImage();
        scaledTrashManImage = trashManImage.getScaledInstance(trashManSize, trashManSize, Image.SCALE_SMOOTH);
        
        // TRASH IMAGE
        ImageIcon myTrashImage = new ImageIcon(this.getClass().getResource("/trashImage.png"));
        trashImage = myTrashImage.getImage();
        scaledTrashImage = trashImage.getScaledInstance(trashSize, trashSize, Image.SCALE_SMOOTH);
        
        // GARBAGE BIN IMAGE
        ImageIcon myGarbageBinImage = new ImageIcon(this.getClass().getResource("/garbageBinImage.png"));
        garbageBinImage = myGarbageBinImage.getImage();
        scaledGarbageBinImage = garbageBinImage.getScaledInstance(garbageBinSize, garbageBinSize, Image.SCALE_SMOOTH);
	}
	
	/*
	 *  randomly move trashMan
	 */
	public void moveRandomTrashMan()
	{		
		// get random value
		Random randomNum = new Random();
		int randomNumber = randomNum.nextInt(5) + 1;
		
		// if trashMan is on/near the edge
		if(trashManX == (newBoard.gameWidth - trashManSize) || trashManX == (newBoard.gameWidth - 2*trashManSize))
			trashManX -= trashManSize;
		else if(trashManX == 0 || trashManX == 40)
			trashManX += trashManSize;
		else if(trashManY == (newBoard.gameHeight - trashManSize) || trashManY == (newBoard.gameHeight - 2*trashManSize))
			trashManY -= trashManSize;
		else if(trashManY == 0 || trashManY == 40)
			trashManY += trashManSize;
		// else randomly move him
		else
		{
			switch(randomNumber){
				case 1:
					trashManX += trashManSize;
					break;
				case 2:
					trashManX -= trashManSize;
					break;
				case 3:
					trashManY += trashManSize;
					break;
				case 4:
					trashManY -= trashManSize;
					break;
			}
		}
		
		// see how many moves he's at
		movesForTrash = movesUntilTrash(movesForTrash);
	}
	
	/*
	 *  add trash after 15 moves
	 */
	public int movesUntilTrash(int movesMade)
	{
		// only add trash if location is unique
		boolean newTrashUnique = true;
		
		// if hit 15
		if(movesMade == movesTillDrop)
		{
			// go through all previous trash produced
			for(int size = 0; size < producedTrash; size++)
			{
				// check if already present at that location
				if(trashManX == trashMadeXCoordinates.get(size) &&
						trashManY == trashMadeYCoordinates.get(size))
					newTrashUnique = false;
			}
			
			// if unique trash location and does not overlap with garbage bin
			if(newTrashUnique && checkGarbageBinLocation(trashManX, trashManY) == false)
			{
				// at 10th move add trash where trashMan is located
				trashMadeXCoordinates.add(trashManX);
				trashMadeYCoordinates.add(trashManY);
				
				producedTrash++;
				
				movesMade = 0;
			}
		}
		else
			movesMade++;
		
		return movesMade;
	}
	
	/*
	 * update trashList and remove an element
	 */
	public void updateTrashList(int trashElementToRemove)
	{
		if(producedTrash != 1)
		{
			// move items in list
			for(int size = trashElementToRemove; size < producedTrash - 1; size++)
			{
				trashMadeXCoordinates.set(size, trashMadeXCoordinates.get(size+1));
				trashMadeYCoordinates.set(size, trashMadeYCoordinates.get(size+1));
			}
		}
		
		// update amount of trash and array
		producedTrash--;
		
		trashMadeXCoordinates.remove(producedTrash);
		trashMadeYCoordinates.remove(producedTrash);
	}
	
	/*
	 *  check to see if conflict with location
	 */
	public boolean checkGarbageBinLocation(int xCoord, int yCoord)
	{
		boolean sameAsGarbageBin = false;
		
		// number of bins
        for(int size = 0; size < 4; size++)
        {
        	if(xCoord == garbageBinsXCoordinates.get(size) && yCoord == garbageBinsYCoordinates.get(size))
        		sameAsGarbageBin = true;
        }
		
		return sameAsGarbageBin;
	}
	
	/*
	 * get bounds for trash dropped
	 */
	public Rectangle getTrashMadeBounds(int size)
	{
		return new Rectangle(trashMadeXCoordinates.get(size), trashMadeYCoordinates.get(size), trashSize, trashSize);
	}
	
	/*
	 * get trashMan's bounds
	 */
	public Rectangle getTrashManBounds()
	{
		return new Rectangle(trashManX, trashManY, trashManSize, trashManSize);
	}
	
	/*
	 * for game over call
	 */
	public void resetTrashProduced()
	{
		producedTrash = 0;
		movesForTrash = 0;
		trashManX = 280;
		trashManY = 280;
		trashMadeXCoordinates.clear();
		trashMadeYCoordinates.clear();
	}
		
	// paint the graphics
	public void paint(Graphics trashGraphics)
	{	
		// trash
		for(int size = 0; size < producedTrash; size++)
			trashGraphics.drawImage(scaledTrashImage, trashMadeXCoordinates.get(size), trashMadeYCoordinates.get(size),
					newBoard);
		
		// trash man
        trashGraphics.drawImage(scaledTrashManImage, trashManX, trashManY, newBoard);
        
        // garbage bins
        for(int size = 0; size < 4; size++)
        	trashGraphics.drawImage(scaledGarbageBinImage, garbageBinsXCoordinates.get(size), garbageBinsYCoordinates.get(size),
					newBoard);
        
	}
	
}
