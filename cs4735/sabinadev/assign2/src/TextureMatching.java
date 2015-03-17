import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class TextureMatching {
	
	public void run(){
		for(int i = 15; i < 16; i++){
			String filename = "";
			if(i<10){
				filename = "i0" + Integer.toString(i) + ".ppm";
			}else{
				filename = "i" + Integer.toString(i) + ".ppm"; 
			}
			System.out.println("filename: " + filename); 
			RGBPixel[][] originalImage = readImage(filename);
			RGBPixel[][] bwImage = createBWImage(originalImage); 
			RGBPixel[][] lapImage = createLaplacianImage(bwImage); 
			
 		}
	}

	public static RGBPixel[][] readImage(String fileName)
	{
		 RGBPixel[][] pixels = new RGBPixel[89][60];
		 RGBPixel pixel = new RGBPixel(0,0,0); 
		 for(RGBPixel[] row: pixels){
			 Arrays.fill(row, pixel); 
		 }
		 String line;
		 StringTokenizer st;
		 
		 try {
	            BufferedReader in =
	              new BufferedReader(new InputStreamReader(
	                new BufferedInputStream(
	                  new FileInputStream(fileName))));

	            DataInputStream in2 =
	              new DataInputStream(
	                new BufferedInputStream(
	                  new FileInputStream(fileName)));

	            // read PPM image header

	            // skip comments
	            line = in.readLine();
	            in2.skip((line+"\n").getBytes().length);
	            do {
	                line = in.readLine();
	                in2.skip((line+"\n").getBytes().length);
	            } while (line.charAt(0) == '#');

	            // the current line has dimensions
	            st = new StringTokenizer(line);
	            int width = Integer.parseInt(st.nextToken());
	            int height = Integer.parseInt(st.nextToken());

	            // next line has pixel depth
	            line = in.readLine(); 
	            in2.skip((line+"\n").getBytes().length);
	            st = new StringTokenizer(line);
	            int depth = Integer.parseInt(st.nextToken());

	            // read pixels now
	            int a = 0; 
	            int b = 0; 
	                for (int c = 0; c < 89; c++){
	                    for (int r = 0; r < 60; r++){
	                    	int redVal = in2.readUnsignedByte();
	                    	int greenVal = in2.readUnsignedByte();
	                    	int blueVal = in2.readUnsignedByte(); 
	                    	RGBPixel pix = new RGBPixel(redVal, greenVal, blueVal); 
	                    	pixels[c][r] = pix;  
	                    } 
	                }//outer for
	              
	            in.close();
	            in2.close();
	        } catch(ArrayIndexOutOfBoundsException e) {
	            System.out.println("Error: image in "+fileName+" too big");
	        } catch(FileNotFoundException e) {
	            System.out.println("Error: file "+fileName+" not found");
	        } catch(IOException e) {
	            System.out.println("Error: end of stream encountered when reading "+fileName);
	        }
		return pixels;
	}
	
	/*
	 * Take in a colored image as RGBPIxel[][] as input.
	 * Convert that image into black and white by using
	 * (R+G+B)/3	
	 */
	public RGBPixel[][] createBWImage(RGBPixel[][] originalImage) {
		RGBPixel[][] bwImage = new RGBPixel[89][60];
		RGBPixel bwpixel = new RGBPixel(0,0,0); 
		for(RGBPixel[] row: bwImage){
			Arrays.fill(row, bwpixel); 
		}
		
		for(int c = 0; c <89; c++){
			for(int r = 0; r < 60; r++){
				RGBPixel pixel = originalImage[c][r];
				int rVal = pixel.getRed();
				int gVal = pixel.getGreen();
				int bVal = pixel.getBlue();
				int newPixelVal = (rVal + gVal + bVal)/3;
				//System.out.println("redVal: " + rVal);
				//System.out.println("greenVal: " + gVal);
				
				RGBPixel newPixel = bwImage[c][r]; 
				newPixel.setRed(newPixelVal);
				newPixel.setGreen(newPixelVal);
				newPixel.setBlue(newPixelVal); 
			}
		}
		//System.out.println("test: " + bwImage[10][10].getRed() + " " + bwImage[10][10].getGreen() + " " + bwImage[10][10].getBlue()); 
		return bwImage;
	}
	
	/*
	 * Measure the grayscale texture similarity, using 
	 * edginess distribution. 
	 */
	public static void createHistogram(){
		// TODO create Laplacian image for each image
		// TODO use the normalized L1 comparison to compare
		// texture similarity
	}
	
	/*
	 * Create a Laplacian image for each image
	 * passed in. 
	 */
	public RGBPixel[][] createLaplacianImage(RGBPixel[][] oldPixels){
		RGBPixel[][] newLapPixels = new RGBPixel[89][60];
		RGBPixel lappixel = new RGBPixel(0,0,0); 
		for(RGBPixel[] row: newLapPixels){
			Arrays.fill(row, lappixel); 
		}
		
		for(int c = 0; c < 89; c++){
			for(int r = 0; r < 60; r++){
				int oldPixelVal = oldPixels[c][r].getRed(); // Doesn't matter which we read; they're all the same
				int eightTimesOld = oldPixelVal * 8; 
				
				/* Again, doesn't matter which RGB we read. 
				 * They're all the same
				 */
				int sum = getSum(c, r, oldPixels); 
				
				int newPixelVal = eightTimesOld - sum; 
				System.out.println("lap: " + newPixelVal); 
				RGBPixel newLapPixel = new RGBPixel(newPixelVal, newPixelVal, newPixelVal);
				newLapPixels[c][r] = newLapPixel; 
			}
		}
		return newLapPixels; 
	}
	
	/*
	 * 88 and 59 are hardcoded.
	 * Could easily be fixed. 
	 */
	public int getSum(int c, int r, RGBPixel[][] oldPixels){
		int sum = 0;
		int greaterCol = c+1;
		int lesserCol = c-1;
		int greaterRow = r+1;
		int lesserRow = r-1;
		
		if((c==0) && (r==0)){
			//3
			sum = oldPixels[greaterCol][r].getRed() + 
					oldPixels[c][greaterRow].getRed() + 
					oldPixels[greaterCol][greaterRow].getRed();
		}else if((c==0) && (r!=0) && (r!=59)){
			//5
			sum = oldPixels[c][lesserRow].getRed() + 
					oldPixels[greaterCol][lesserRow].getRed() + 
					oldPixels[greaterCol][r].getRed() + 
					oldPixels[c][greaterRow].getRed() + 
					oldPixels[greaterCol][greaterRow].getRed();
		}else if((r==0) && (c!=0) && (c!=88)){
			//5
			sum = oldPixels[lesserCol][r].getRed() + 
					oldPixels[lesserCol][greaterRow].getRed() + 
					oldPixels[c][greaterRow].getRed() + 
					oldPixels[greaterCol][greaterRow].getRed() + 
					oldPixels[greaterCol][r].getRed();
		}else if((c==88) && (r!=59) && (r!=0)){
			//5
			sum = oldPixels[c][lesserRow].getRed() + 
					oldPixels[lesserCol][lesserRow].getRed() + 
					oldPixels[lesserCol][r].getRed() + 
					oldPixels[lesserCol][greaterRow].getRed() + 
					oldPixels[c][greaterRow].getRed();
		}else if((c==0) && (r==59)){
			//3
			sum =  oldPixels[c][lesserRow].getRed() + 
					oldPixels[greaterCol][lesserRow].getRed() + 
					oldPixels[greaterCol][r].getRed();  
		}else if((r==0) && (c==88)){
			//3
			sum  = oldPixels[lesserCol][r].getRed() + 
					oldPixels[lesserCol][greaterRow].getRed() + 
					oldPixels[c][greaterRow].getRed(); 
		}else if((r==59) && (c!=88) && (c!=0)){
			//5
			sum = oldPixels[lesserCol][r].getRed() + 
					oldPixels[lesserCol][lesserRow].getRed() + 
					oldPixels[c][lesserRow].getRed() + 
					oldPixels[greaterCol][lesserRow].getRed() + 
					oldPixels[greaterCol][r].getRed();
		}else if((r==59) && (c==88)){
			//3
			sum = oldPixels[lesserCol][r].getRed() + 
					oldPixels[lesserCol][lesserRow].getRed() + 
					oldPixels[c][lesserRow].getRed();
		}else{
			sum = oldPixels[lesserCol][lesserRow].getRed() + 
					oldPixels[c][lesserRow].getRed() + 
					oldPixels[greaterCol][greaterRow].getRed() + 
					oldPixels[lesserCol][r].getRed() + 
					oldPixels[greaterCol][r].getRed() + 
					oldPixels[lesserCol][greaterRow].getRed() + 
					oldPixels[c][greaterRow].getRed() + 
					oldPixels[greaterCol][greaterRow].getRed();
		}
		
		return sum; 
	}
	
	/*
	 * Compare two given images using the normalized
	 * L1 comparison, using edginess distribution. 
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
	
	public static void main( String[] args ){
	      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	      TextureMatching textureMatching = new TextureMatching(); 
	      textureMatching.run(); 
	}
}
