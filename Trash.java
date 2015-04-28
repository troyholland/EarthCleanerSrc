//environment game trash class
//only for single trash

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Rectangle;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Trash 
{
	public int trashToPickup;
	
	// location of trash
	public int trashX;
	public int trashY;
	public int trashType; // holds type of trash
	
	public List<Integer>recycleBinsXCoordinates = new ArrayList<Integer>();
	public List<Integer>recycleBinsYCoordinates = new ArrayList<Integer>();
	
	public List<Integer>treesMadeXCoordinates = new ArrayList<Integer>();
	public List<Integer>treesMadeYCoordinates = new ArrayList<Integer>();
	public int treesMade;
	
	private static final int trashSize = 40;
	private static final int recycleSize = 40;
	private static final int treeSize = 40;
	
	// recycle images
	private Image plasticImage;
	private Image scaledPlasticImage;
	private Image glassImage;
	private Image scaledGlassImage;
	private Image aluminumImage;
	private Image scaledAluminumImage;
	private Image paperImage;
	private Image scaledPaperImage;
	
	// trees image
	private Image treeImage;
	private Image scaledTreeImage;
	
	// trash images
	private Image plasticTrashImage;
	private Image scaledPlasticTrashImage;
	private Image glassTrashImage;
	private Image scaledGlassTrashImage;
	private Image aluminumTrashImage;
	private Image scaledAluminumTrashImage;
	private Image paperTrashImage;
	private Image scaledPaperTrashImage;
	
	// board for game
	private Board newBoard;
	
	public Trash(Board board)
	{
		trashToPickup = 1;
		
		// initial location at (40, 40)
		trashX = 400;
		trashY = 320;
		
		this.newBoard = board;
		
		// set up recycle bins coordinates
		recycleBinsXCoordinates.add(newBoard.gameWidth - 3*recycleSize); // plastic
		recycleBinsYCoordinates.add(2*recycleSize);
		recycleBinsXCoordinates.add(2*recycleSize); // glass
		recycleBinsYCoordinates.add(2*recycleSize);
		recycleBinsXCoordinates.add(newBoard.gameWidth - 3*recycleSize); // paper
		recycleBinsYCoordinates.add(newBoard.gameHeight - 3*recycleSize);
		recycleBinsXCoordinates.add(2*recycleSize); // aluminum
		recycleBinsYCoordinates.add(newBoard.gameHeight - 3*recycleSize);
		
		treesMade = 0;

        // RECYCLE PLASTIC
        ImageIcon myRecycleImagePlastic = new ImageIcon(this.getClass().getResource("/recyclePlastic.png"));
        plasticImage = myRecycleImagePlastic.getImage();
        scaledPlasticImage = plasticImage.getScaledInstance(recycleSize, recycleSize, Image.SCALE_SMOOTH);
        
        // RECYCLE GLASS
        ImageIcon myRecycleImageGlass = new ImageIcon(this.getClass().getResource("/recycleGlass.png"));
        glassImage = myRecycleImageGlass.getImage();
        scaledGlassImage = glassImage.getScaledInstance(recycleSize, recycleSize, Image.SCALE_SMOOTH);
        
        // RECYCLE ALUMINUM
        ImageIcon myRecycleImageAluminum = new ImageIcon(this.getClass().getResource("/recycleAluminum.png"));
        aluminumImage = myRecycleImageAluminum.getImage();
        scaledAluminumImage = aluminumImage.getScaledInstance(recycleSize, recycleSize, Image.SCALE_SMOOTH);
        
        // RECYCLE PAPER
        ImageIcon myRecycleImagePaper = new ImageIcon(this.getClass().getResource("/recyclePaper.png"));
        paperImage = myRecycleImagePaper.getImage();
        scaledPaperImage = paperImage.getScaledInstance(recycleSize, recycleSize, Image.SCALE_SMOOTH);
        
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
        
        // TREE IMAGE
        ImageIcon myTreeImage = new ImageIcon(this.getClass().getResource("/treeImage.png"));
        treeImage = myTreeImage.getImage();
        scaledTreeImage = treeImage.getScaledInstance(treeSize, treeSize, Image.SCALE_SMOOTH);
	}
	
	/*
	 * remove trash when picked up
	 */
	public void removeTrash()
	{
		trashToPickup = 0;
	}
	
	/*
	 *  check to see if conflict with location
	 */
	public boolean checkTreeLocation(int xCoord, int yCoord)
	{
		boolean sameAsTree = false;
		
		// number of trees
        for(int size = 0; size < treesMade; size++)
        {
        	if(xCoord == treesMadeXCoordinates.get(size) && yCoord == treesMadeYCoordinates.get(size))
        		sameAsTree = true;
        }
		
		return sameAsTree;
	}
	
	/*
	 * when trash recycled, plant tree
	 */
	public void plantTree()
	{
		int treeX;
		int treeY;
		
		boolean notUniqueTree = false;
		
		Random randomNum = new Random();
		
		// get random location
		treeX = (randomNum.nextInt((newBoard.gameWidth / treeSize) - 1) + 1)*treeSize;
		treeY = (randomNum.nextInt((newBoard.gameHeight / treeSize) - 1) + 1)*treeSize;
		
		for(int size = 0; size < treesMade; size++)
		{
			// if trash is first placed on spot where earthCleaner already is
			if((treeX == treesMadeXCoordinates.get(size) && treeY == treesMadeYCoordinates.get(size)) || 
					checkRecycleBinLocation(treeX, treeY) == true)
				notUniqueTree = true;
		}
		
		if(notUniqueTree)
			plantTree();
		else
		{
			treesMadeXCoordinates.add(treeX);
			treesMadeYCoordinates.add(treeY);
			
			treesMade++;
		}
	}
	
	// check to see if conflict with location
	public boolean checkRecycleBinLocation(int xCoord, int yCoord)
	{
		boolean sameAsRecycleBin = false;
		
		// number of bins
        for(int size = 0; size < 4; size++)
        {
        	if(xCoord == recycleBinsXCoordinates.get(size) && yCoord == recycleBinsYCoordinates.get(size))
        		sameAsRecycleBin = true;
        }
		
		
		return sameAsRecycleBin;
	}
	
	/*
	 *  get trash's random location
	 */
	public void getRandomTrashLocation()
	{
		trashToPickup = 1;
		
		Random randomLocation = new Random();
		Random randomTrashType = new Random();
		
		// get random location
		trashX = (randomLocation.nextInt((newBoard.gameWidth / trashSize) - 1) + 1)*trashSize;
		trashY = (randomLocation.nextInt((newBoard.gameHeight / trashSize) - 1) + 1)*trashSize;
		
		trashType = randomTrashType.nextInt((4) + 0); // plastic = 0, glass = 1, aluminum = 2, paper = 3
		
		for(int size = 0; size < newBoard.earthCleaner.cleanerLength; size++)
		{
			// if trash is first placed on spot where earthCleaner already is
			if((trashX == newBoard.earthCleaner.cleanerHeadX &&
					trashY == newBoard.earthCleaner.cleanerHeadY) || checkRecycleBinLocation(trashX, trashY) == true)
				getRandomTrashLocation(); // do again
		}
	}
	
	public int getTrashType()
	{
		return trashType;
	}
	
	/*
	 *  for location of singular trash
	 */
	public Rectangle getTrashBounds()
	{
		return new Rectangle(trashX, trashY, trashSize, trashSize);
	}
	
	/*
	 *  for location of recycle bin
	 */
	public Rectangle getRecycleBounds(int recycleType)
	{
		return new Rectangle(recycleBinsXCoordinates.get(recycleType), recycleBinsYCoordinates.get(recycleType), recycleSize, recycleSize);
	}
	
	/*
	 * gets rid of trees after done
	 */
	public void resetTrees()
	{
		treesMade = 0;
	}
	
	/*
	 *  draw the trash
	 */
	public void paint(Graphics trashPickupGraphic)
	{	
		// draw trash when not being carried
		if(trashToPickup == 1)
		{
			switch(trashType){
			case 0:
				trashPickupGraphic.drawImage(scaledPlasticTrashImage, trashX, trashY, newBoard);
				break;
			case 1:
				trashPickupGraphic.drawImage(scaledGlassTrashImage, trashX, trashY, newBoard);
				break;
			case 2:
				trashPickupGraphic.drawImage(scaledAluminumTrashImage, trashX, trashY, newBoard);
				break;
			case 3:
				trashPickupGraphic.drawImage(scaledPaperTrashImage, trashX, trashY, newBoard);
				break;
			}
		}
		
		// bins
        trashPickupGraphic.drawImage(scaledPlasticImage, recycleBinsXCoordinates.get(0), recycleBinsYCoordinates.get(0),
        		newBoard); // plastic
        trashPickupGraphic.drawImage(scaledGlassImage, recycleBinsXCoordinates.get(1), recycleBinsYCoordinates.get(1),
    			newBoard); // glass
        trashPickupGraphic.drawImage(scaledAluminumImage, recycleBinsXCoordinates.get(2), recycleBinsYCoordinates.get(2),
    			newBoard); // aluminum
        trashPickupGraphic.drawImage(scaledPaperImage, recycleBinsXCoordinates.get(3), recycleBinsYCoordinates.get(3),
    			newBoard); // paper
        
        // draw the trees
        for(int size = 0; size < treesMade; size++)
        {
        	trashPickupGraphic.drawImage(scaledTreeImage, treesMadeXCoordinates.get(size), treesMadeYCoordinates.get(size),
        			newBoard);
        }
	}
	
}
