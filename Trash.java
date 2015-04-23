//environment game trash class
//only for single trash

import java.awt.Graphics;
import java.util.Random;
import java.awt.Rectangle;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Trash 
{
	// location of food
	public int trashX;
	public int trashY;
	
	private static final int trashSize = 40;
	
	// image information
	private Image trashImage;
	private Image scaledTrashImage;
	
	// board for game
	private Board newBoard;
	
	public Trash(Board board)
	{
		// initial location at (40, 40)
		trashX = 40;
		trashY = 40;
		
		this.newBoard = board;
		
		// load image
        ImageIcon myImage = new ImageIcon("/Users/Troy/Desktop/workspace2/EnvironmentGame/trashImage.png");
        trashImage = myImage.getImage();
        scaledTrashImage = trashImage.getScaledInstance(trashSize, trashSize, Image.SCALE_FAST);
	}
	
	/*
	 *  get trash's random location
	 */
	public void getRandomTrashLocation()
	{
		Random randomNum = new Random();
		
		// get random location
		trashX = (randomNum.nextInt((newBoard.gameWidth / trashSize) - 1) + 1)*trashSize;
		trashY = (randomNum.nextInt((newBoard.gameHeight / trashSize) - 1) + 1)*trashSize;
		
		for(int size = 0; size < newBoard.earthCleaner.cleanerLength; size++)
		{
			// if trash is first placed on spot where earthCleaner already is
			if(trashX == newBoard.earthCleaner.cleanerXCoordinates.get(size) &&
					trashY == newBoard.earthCleaner.cleanerYCoordinates.get(size))
				getRandomTrashLocation(); // do again
		}
	}
	
	/*
	 *  for location of singular trash
	 */
	public Rectangle getTrashBounds()
	{
		return new Rectangle(trashX, trashY, trashSize, trashSize);
	}
	
	/*
	 *  draw the trash
	 */
	public void paint(Graphics trashGraphic)
	{	
        trashGraphic.drawImage(scaledTrashImage, trashX, trashY, newBoard);
	}
	
}
