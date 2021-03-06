package pixLab.classes;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.util.List; // resolves problem with java.awt.List and java.util.List

/**
 * A class that represents a picture. This class inherits from SimplePicture and allows the student
 * to add functionality to the Picture class.
 * 
 * @author Barbara Ericson ericson@cc.gatech.edu
 */
public class Picture extends SimplePicture
{
	///////////////////// constructors //////////////////////////////////

	/**
	 * Constructor that takes no arguments
	 */
	public Picture()
	{
		/*
		 * not needed but use it to show students the implicit call to super() child constructors always
		 * call a parent constructor
		 */
		super();
	}

	/**
	 * Constructor that takes a file name and creates the picture
	 * 
	 * @param fileName
	 *            the name of the file to create the picture from
	 */
	public Picture(String fileName)
	{
		// let the parent class handle this fileName
		super(fileName);
	}

	/**
	 * Constructor that takes the width and height
	 * 
	 * @param height
	 *            the height of the desired picture
	 * @param width
	 *            the width of the desired picture
	 */
	public Picture(int height, int width)
	{
		// let the parent class handle this width and height
		super(width, height);
	}

	/**
	 * Constructor that takes a picture and creates a copy of that picture
	 * 
	 * @param copyPicture
	 *            the picture to copy
	 */
	public Picture(Picture copyPicture)
	{
		// let the parent class do the copy
		super(copyPicture);
	}

	/**
	 * Constructor that takes a buffered image
	 * 
	 * @param image
	 *            the buffered image to use
	 */
	public Picture(BufferedImage image)
	{
		super(image);
	}

	////////////////////// methods ///////////////////////////////////////

	/**
	 * Method to return a string with information about this picture.
	 * 
	 * @return a string with information about the picture such as fileName, height and width.
	 */
	public String toString()
	{
		String output = "Picture, filename " + getFileName() + " height " + getHeight() + " width " + getWidth();
		return output;

	}

	/** Method to set the blue to 0 */
	public void zeroBlue()
	{
		Pixel[][] pixels = this.getPixels2D();
		for (Pixel[] rowArray : pixels)
		{
			for (Pixel pixelObj : rowArray)
			{
				pixelObj.setBlue(0);
			}
		}
	}

	/**
	 * Method that mirrors the picture around a vertical mirror in the center of the picture from left
	 * to right
	 */
	public void mirrorVertical()
	{
		Pixel[][] pixels = this.getPixels2D();
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		int width = pixels[0].length;
		for (int row = 0; row < pixels.length; row++)
		{
			for (int col = 0; col < width / 2; col++)
			{
				leftPixel = pixels[row][col];
				rightPixel = pixels[row][width - 1 - col];
				rightPixel.setColor(leftPixel.getColor());
			}
		}
	}

	/** Mirror just part of a picture of a temple */
	public void mirrorTemple()
	{
		int mirrorPoint = 276;
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		int count = 0;
		Pixel[][] pixels = this.getPixels2D();

		// loop through the rows
		for (int row = 27; row < 57; row++)
		{
			// loop from 13 to just before the mirror point
			for (int col = 0; col < 455; col++)
			{

				leftPixel = pixels[row][col];
				rightPixel = pixels[row][mirrorPoint - col + mirrorPoint];
				rightPixel.setColor(leftPixel.getColor());
			}
		}
		
	}

	/**
	 * copy from the passed fromPic to the specified startRow and startCol in the current picture
	 * 
	 * @param fromPic
	 *            the picture to copy from
	 * @param startRow
	 *            the start row to copy to
	 * @param startCol
	 *            the start col to copy to
	 */
	public void copy(Picture fromPic, int startRow, int startCol)
	{
		Pixel fromPixel = null;
		Pixel toPixel = null;
		Pixel[][] toPixels = this.getPixels2D();
		Pixel[][] fromPixels = fromPic.getPixels2D();
		for (int fromRow = 0, toRow = startRow; fromRow < fromPixels.length && toRow < toPixels.length; fromRow++, toRow++)
		{
			for (int fromCol = 0, toCol = startCol; fromCol < fromPixels[0].length && toCol < toPixels[0].length; fromCol++, toCol++)
			{
				fromPixel = fromPixels[fromRow][fromCol];
				toPixel = toPixels[toRow][toCol];
				toPixel.setColor(fromPixel.getColor());
			}
		}
	}

	public void chickenFilter(int startRow, int startCol)
	{
		Pixel fromPixel = null;
		Pixel toPixel = null;
		Picture chicken = new Picture("chicken nugget.png");
		Pixel[][] toPixels = this.getPixels2D(); // The base layer of the picture.
		Pixel[][] fromPixels = chicken.getPixels2D(); // The layer we are adding to the picture.

		int fromRow = 0;
		for (int toRow = startRow; fromRow < fromPixels.length && toRow < toPixels.length; toRow++)
		{
			int fromCol = 0;
			for (int toCol = startCol; fromCol < fromPixels[0].length && toCol < toPixels[0].length; toCol++)
			{
				fromPixel = fromPixels[fromRow][fromCol];
				// int transparentLevel = fromPixel.getAlpha();
				toPixel = toPixels[toRow][toCol];
				if (!fromPixel.isTransparent())
				{
					toPixel.setColor(fromPixel.getColor());
				}
				fromCol++;
			}
			fromRow++;
		}

	}

	/** Method to create a collage of several pictures */
	public void createCollage()
	{
		Picture flower1 = new Picture("flower1.jpg");
		Picture flower2 = new Picture("flower2.jpg");
		this.copy(flower1, 0, 0);
		this.copy(flower2, 100, 0);
		this.copy(flower1, 200, 0);
		Picture flowerNoBlue = new Picture(flower2);
		flowerNoBlue.zeroBlue();
		this.copy(flowerNoBlue, 300, 0);
		this.copy(flower1, 400, 0);
		this.copy(flower2, 500, 0);
		this.mirrorVertical();
		this.write("collage.jpg");
	}

	/**
	 * Method to show large changes in color
	 * 
	 * @param edgeDist
	 *            the distance for finding edges
	 */
	public void edgeDetection(int edgeDist)
	{
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		Pixel[][] pixels = this.getPixels2D();
		Color rightColor = null;
		for (int row = 0; row < pixels.length; row++)
		{
			for (int col = 0; col < pixels[0].length - 1; col++)
			{
				leftPixel = pixels[row][col];
				rightPixel = pixels[row][col + 1];
				rightColor = rightPixel.getColor();
				if (leftPixel.colorDistance(rightColor) > edgeDist)
					leftPixel.setColor(Color.BLACK);
				else
					leftPixel.setColor(Color.WHITE);
			}
		}
	}

	/*
	 * Main method for testing - each class in Java can have a main method
	 */
	public static void main(String[] args)
	{
		Picture beach = new Picture("beach.jpg");
		beach.explore();
		beach.zeroBlue();
		beach.explore();
	}

	public void glitchPhoto(int pixelShift)
	{
		Picture sourcePic = new Picture(this.getFileName());
		Pixel[][] newPixels = sourcePic.getPixels2D();
		Pixel[][] oldPixels = this.getPixels2D();
		for (int col = pixelShift; col < oldPixels.length + pixelShift; col++)
		{
			for (int row = 0; row < oldPixels[0].length; row++)
			{
				oldPixels [row][col%oldPixels.length].setRed(newPixels[row][col-pixelShift].getRed());
				oldPixels [row][col%oldPixels.length].setGreen(newPixels[row][col-pixelShift].getGreen());
				oldPixels [row][col%oldPixels.length].setBlue(newPixels[row][col-pixelShift].getBlue());
			}
		}
    
	}
	
//	public void glitchColor()
//	{
//		Pixel[][] pixels = this.getPixels2D();
//		int shiftAmount = (int) (.33 * pixels[0].length);
//		int width = pixels[0].length;
//		
//		for (int row = 0; row < pixels.length; row++)
//		{
//			Color [] currentColors = new Color[pixels[0].length];
//			
//			for (int col = 0; col < pixels[row].length; col++)
//			{
//				currentColors[col] = pixels[row][col].getColor();
//			}
//			
//			for (int col = 0; col < pixels[0].length; col++)
//			{
//				pixels[row][col].setColor(currentColors[(col + shiftAmount) % width]);
//			}
//		}
//	}
	
	public void glitchColor(int redShift, int greenShift, int blueShift)
	{
		Picture sourcePic = new Picture(this.getFileName());
		Pixel[][] newPixels = sourcePic.getPixels2D();
		Pixel[][] oldPixels = this.getPixels2D();
		for (int col = 0; col < oldPixels.length; col++)
		{
			for (int row = 0; row < oldPixels[0].length; row++)
			{
				oldPixels [row][col%oldPixels.length].setRed(newPixels[row][(col+redShift)%oldPixels.length].getRed());
				oldPixels [row][col%oldPixels.length].setGreen(newPixels[row][(col+greenShift)%oldPixels.length].getGreen());
				oldPixels [row][col%oldPixels.length].setBlue(newPixels[row][(col+blueShift)%oldPixels.length].getBlue());
			}
		}
	}
	
	public void copyRegion(int red, int blue)
	{
		Picture sourcePic = new Picture(this.getFileName());
		Pixel[][] newRegion = sourcePic.getPixels2D();
		Pixel[][] oldRegion = this.getPixels2D();
		for (int col = 32; col < oldRegion.length; col++)
		{
			for (int row = 32; row < oldRegion.length; row++)
			{
				oldRegion [row][col%oldRegion.length].setRed(newRegion[row][(col+red)%oldRegion.length].getRed());
				oldRegion [row][col%oldRegion.length].setBlue(newRegion[row][(col+blue)%oldRegion.length].getBlue());
			}
		}
	}
	public void addMessage(String message, int xPos, int yPos, int color)
	 {
	   // get a graphics context to use to draw on the buffered image
	   Graphics2D graphics2d = bufferedImage.createGraphics();
	   
	   // set the font to Helvetica bold style and size 16
	   graphics2d.setFont(new Font("Helvetica",Font.BOLD,16));
	   
	   // draw the message
	   graphics2d.drawString(message,xPos,yPos);
	   
	   if (color > 100)
	   {
		   if(color < 99)
		   {
			   graphics2d.setColor(Color.GREEN);
		   }
		   graphics2d.setColor(Color.blue);
	   }
	}

	public int getAlpha() {


	    int alpha = (value >> 24) & 0xff;
	    
	    return alpha;
	  }

	  public static int getRed(int value)
	  {
	    int red = (value >> 16) & 0xff;
	    return red;
	  }


	  public int getGreen(int value)
	  {
	    int green = (value >> 8) & 0xff;
	    return green;
	  }

	  public int getBlue(int value)
	  {
	    int blue = value & 0xff;
	    return blue;
	  }
	  
	  
	public void bobFilter()
	{
	Picture sourcePic = new Picture(this.getFileName());
	Pixel[][] newRegion = sourcePic.getPixels2D();
	Pixel[][] oldRegion = this.getPixels2D();
	for (int col = 0; col < oldRegion.length; col++)
	{
		for (int row = 0; row < oldRegion.length; row++)
		{
			 if (getAlpha() == 0 && getRed() == 255 && getGreen() == 0 && getBlue() == 0)
			 {
				 
			 }
			 }
		}
	}


} // this } is the end of class Picture, put all new methods before this
