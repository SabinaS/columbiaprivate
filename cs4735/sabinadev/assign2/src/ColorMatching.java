import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import quicktime.app.image.ImageViewer;

public class ColorMatching
{
   /*
    * Takes in an image and generates a 3D array for the RGB
    * values. Then based on the RGB value, creates a 3D
    * color histogram. 
    */
	//public void generateHistogram(){
	public void run() throws IOException{
		//TODO
		// Read in the image
		//Mat src;  
		//src = Highgui.imread(getClass().getResource("Images/i15.jpg").getPath());
		RGBPixel[][] pixels= new RGBPixel[89][60];
		pixels = readSImage("i15.ppm"); 
		for(int i = 0; i< 10; i++){
			for(int j = 0; j<10; j++){
				//System.out.println(pixels[i][j].redVal); 
			}
		}
		
	}
	
	public static RGBPixel[][] readSImage(String fName) throws IOException
	{
		System.out.println("this"); 
		File theFile = new File(fName);
		System.out.println("file"); 
		Scanner scan = new Scanner(theFile);
		System.out.println("scanner"); 
		int i=0, j=0;
		int tokenCounter = 0;
		int cols = 0;
		int rows = 0;
		StringTokenizer tokens = null;
		
		outloop: 
		while (scan.hasNextLine())
		{
			System.out.println("after"); 
			String aLine = scan.nextLine();
			if (aLine.charAt(0) == '#'){
				System.out.println("saw #"); 
				continue;
			}
			// process the line:
			tokens = new StringTokenizer(aLine, " ");
			while (tokens.hasMoreTokens())
			{
				tokenCounter++;
				String aToken = tokens.nextToken();
				if (tokenCounter == 1) 
				{
					// expect that aToken is "P6"
					if (!aToken.equals("P6")) System.out.println("Expected P6 magic number, got " + aToken);
				}
				if (tokenCounter == 2)
				{
					// expect that aToken is the number of columns
					cols = Integer.parseInt(aToken);
				}
				if (tokenCounter == 3)
				{
					// expect that aToken is the number of rows
					rows = Integer.parseInt(aToken);
				}
				if (tokenCounter == 4)
				{
					// expect that aToken is the maximum value
					// ignore
					break outloop;
				}//inner if
			}//inner while	
		}//outer while

		RGBPixel outImage[][] = new RGBPixel[rows][cols];

		// still worry if more tokens in previously read line

		int channelCount = 0;
		int redVal = 0, blueVal = 0, greenVal = 0;
		while (scan.hasNextLine())
		{
			String aLine = scan.nextLine();
			if (aLine.charAt(0) == '#')
				continue;

			// process the line:
			tokens = new StringTokenizer(aLine, " ");
			while (tokens.hasMoreTokens())
			{
				String aToken = tokens.nextToken();
				if (channelCount == 0) 
				{
					redVal = Integer.parseInt(aToken);
					channelCount++;
				}
				else
				if (channelCount == 1) 
				{
					// expect that aToken is the number of columns
					greenVal = Integer.parseInt(aToken);
					channelCount++;
				}
				else
				if (channelCount == 2) 
				{
					// expect that aToken is the number of rows
					blueVal = Integer.parseInt(aToken);
					outImage[i][j] = new RGBPixel(redVal, greenVal, blueVal);
					j++;
					if (j >= cols)
					{
						i++;
						j = 0;
					}
					channelCount = 0;
		
				}//inner if
			}//inner while		
		}//outer while
/*
		// possible that there's still some tokens on the line already read
		while (tokens.hasMoreTokens())
		{
			int redVal = Integer.parseInt(tokens.nextToken());
			int greenVal = Integer.parseInt(tokens.nextToken());
			int blueVal = Integer.parseInt(tokens.nextToken());
			outImage[i][j] = new RGBPixel(redVal, greenVal, blueVal);
			j++;
			if (j >= cols)
			{
				i++;
				j = 0;
			}
		}

		while (scan.hasNextLine())
		{
			String line = scan.nextLine();
			StringTokenizer tokens2 = new StringTokenizer(line, " ");
			while (tokens2.hasMoreTokens())
			{
				int redVal = Integer.parseInt(tokens2.nextToken());
				int greenVal = Integer.parseInt(tokens2.nextToken());
				int blueVal = Integer.parseInt(tokens2.nextToken());
				outImage[i][j] = new RGBPixel(redVal, greenVal, blueVal);
				j++;
				if (j >= cols)
				{
					i++;
					j = 0;
				}
			}
		}

*/

		return outImage;
	}

	
	public void readImage(String filename){
		//Read in image line by line
		//starting from line 5, break the line into 89*3 sections, 
		//with each section corresponding to a byte
		
		//
	}
	/*
	 * Takes in two histograms and using the normalized L1
	 * comparison, compares the histograms and determines 
	 * a value between 0 and 1 inclusive of how similar they are. 
	 */
	public void compareHistograms(){
		//TODO
	}
	
	/*
	 * Takes in target image histogram and a list of histograms 
	 * and using compareHistory() outputs the three most similar
	 * images to the target images. 
	 */
	public void getThreeMostSimilar(){
		//TODO
	}
	
	/*
	 * Takes in target image histogram and a list of histograms 
	 * and using compareHistory() outputs the three least similar
	 * images to the target images. 
	 */
	public void getThreeLeastSimilar(){
		//TODO
	}
	
	/*
	 * Outputs the four most similar images out of the 40
	 */
	public void getFourTopSimilar(){
		//TODO
	}
	
	/*
	 * Outputs the four least similar images out of the 40
	 */
	public void getFourBottomSimilar(){
		//TODO
	}
	
	public static void main( String[] args ) throws IOException
   {
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
      ColorMatching colorMatching = new ColorMatching(); 
      colorMatching.run(); 
   }
}