import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class TextureMatching {
	
	private float least4simsum = 0;
	private float most4simsum= 3; 
	String[] least4sim = new String[4];
	String[] most4sim = new String[4]; 
	
	public void run(){
		
		for(int i = 1; i< 41; i++){
			String filename = "";
			if(i<10){
				filename = "i0" + Integer.toString(i) + ".ppm";
			}else{
				filename = "i" + Integer.toString(i) + ".ppm";
			}
			RGBPixel[][] originalImage= new RGBPixel[89][60];
			originalImage = readImage(filename);
			RGBPixel[][] bwImage = generateBWImage(originalImage); 
			RGBPixel[][] lapImage = createLaplacianImage(bwImage); 
			int[] histogram = generateHistogram(lapImage); 
			String[] threeMostSimilar = getThreeMostLeastSimilar(histogram, filename, true); 
			String[] threeLeastSimilar = getThreeMostLeastSimilar(histogram, filename, false); 
			
			System.out.println("The three most similar images to " + filename + " are:"); 
			for(String s : threeMostSimilar){
				System.out.println(s); 
			}
			
			System.out.println("The three least similar images to " + filename + " are:"); 
			for(String s : threeLeastSimilar){
				System.out.println(s); 
			}
		}
		getFourTopBottomSimilar(true); 
		System.out.println("The 4 most similar images out of the whole are:");
		for(String s: most4sim){
			System.out.println(s); 
		}
		
		getFourTopBottomSimilar(false); 
		System.out.println("The 4 least similar images out of the whole are:");
		for(String s: least4sim){
			System.out.println(s); 
		}
		
		/*RGBPixel[][] pixels= new RGBPixel[89][60];
		pixels = readImage("i18.ppm");
		int[] histogram = generateHistogram(pixels); 
		RGBPixel[][] pixels2= new RGBPixel[89][60];
		pixels2 = readImage("i20.ppm");
		int[] histogram2 = generateHistogram(pixels2); 
		float com = compareHistograms(histogram, histogram2); 
		System.out.println("con " + com);*/
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
	public RGBPixel[][] generateBWImage(RGBPixel[][] originalImage) {
		RGBPixel[][] bwImage = new RGBPixel[89][60];
		RGBPixel bwpixel = new RGBPixel(0,0,0); 
		for(RGBPixel[] row: bwImage){
			Arrays.fill(row, bwpixel); 
		}
		
		for(int c = 0; c <89; c++){
			for(int r = 0; r < 60; r++){
				RGBPixel oldPixel = originalImage[c][r];
				int rVal = oldPixel.getRed();
				int gVal = oldPixel.getGreen();
				int bVal = oldPixel.getBlue();
				int newPixelVal = (rVal + gVal + bVal)/3;
				//System.out.println("newVal: " + newPixelVal);
				//System.out.println("greenVal: " + gVal);
				
				RGBPixel newPixel = new RGBPixel(0,0,0);  
				newPixel.setRed(newPixelVal);
				newPixel.setGreen(newPixelVal);
				newPixel.setBlue(newPixelVal); 
				bwImage[c][r] = newPixel; 
			}
		}
		//System.out.println("test: " + bwImage[10][10].getRed() + " " + bwImage[10][10].getGreen() + " " + bwImage[10][10].getBlue()); 
		return bwImage;
	}
	
	/*
	 * Generates a 1D histogram for the Laplacian
	 * image taken in. 
	 */
	public int[] generateHistogram(RGBPixel[][] lapImage){
		int[] histogram = new int[512];
		for(int i = 0; i< 512; i++){
			histogram[i] = 0; 
		}
		
		for(int c = 0; c < 89; c++){
			for(int r = 0; r < 60; r++){
				int pixVal = lapImage[c][r].getRed(); //doesn't matter which; all the same
				//System.out.println("lap: " + pixVal); 
				int newPixVal = (pixVal / 8)+255; //because 4096/512 = 8
												//but since -2048 to 2048, need offset by 255
				//System.out.println("new: " + newPixVal); 
				histogram[newPixVal] = histogram[newPixVal] + 1; //increment that value
																//whenever we see it
			}
		}
		return histogram; 
	}
	
	/*
	 * Create a Laplacian image for each image
	 * passed in, where each pixel represents the 
	 * edginess of its surroundings and itself
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
				//System.out.println("old val: " + oldPixelVal); 
				/* Again, doesn't matter which RGB we read. 
				 * They're all the same
				 */
				int newPixelVal = getNewPixelVal(c, r, oldPixels, oldPixelVal); 
				
				//System.out.println("lap: " + newPixelVal); 
				RGBPixel newLapPixel = new RGBPixel(newPixelVal, newPixelVal, newPixelVal);
				newLapPixels[c][r] = newLapPixel; 
			}
		}
		return newLapPixels; 
	}
	
	/*
	 * Returns a new pixel value, which is 8 times the old
	 * pixel value minus the sum of the eight surrounding pixels,
	 * or x if the old pixel lies on an edge; 
	 * 88 and 59 are hardcoded.
	 * Could easily be fixed. 
	 */
	public int getNewPixelVal(int c, int r, RGBPixel[][] oldPixels, int oldPixelVal){
		int newPixelVal = 0; 
		int multiplier = 0; 
		int sum = 0;
		int greaterCol = c+1;
		int lesserCol = c-1;
		int greaterRow = r+1;
		int lesserRow = r-1;
		
		if((c==0) && (r==0)){
			//3
			multiplier = 3; 
			sum = oldPixels[greaterCol][r].getRed() + 
					oldPixels[c][greaterRow].getRed() + 
					oldPixels[greaterCol][greaterRow].getRed();
		}else if((c==0) && (r!=0) && (r!=59)){
			//5
			multiplier = 5; 
			sum = oldPixels[c][lesserRow].getRed() + 
					oldPixels[greaterCol][lesserRow].getRed() + 
					oldPixels[greaterCol][r].getRed() + 
					oldPixels[c][greaterRow].getRed() + 
					oldPixels[greaterCol][greaterRow].getRed();
		}else if((r==0) && (c!=0) && (c!=88)){
			//5
			multiplier = 5; 
			sum = oldPixels[lesserCol][r].getRed() + 
					oldPixels[lesserCol][greaterRow].getRed() + 
					oldPixels[c][greaterRow].getRed() + 
					oldPixels[greaterCol][greaterRow].getRed() + 
					oldPixels[greaterCol][r].getRed();
		}else if((c==88) && (r!=59) && (r!=0)){
			//5
			multiplier = 5; 
			sum = oldPixels[c][lesserRow].getRed() + 
					oldPixels[lesserCol][lesserRow].getRed() + 
					oldPixels[lesserCol][r].getRed() + 
					oldPixels[lesserCol][greaterRow].getRed() + 
					oldPixels[c][greaterRow].getRed();
		}else if((c==0) && (r==59)){
			//3
			multiplier = 3; 
			sum =  oldPixels[c][lesserRow].getRed() + 
					oldPixels[greaterCol][lesserRow].getRed() + 
					oldPixels[greaterCol][r].getRed();  
		}else if((r==0) && (c==88)){
			//3
			multiplier = 3; 
			sum  = oldPixels[lesserCol][r].getRed() + 
					oldPixels[lesserCol][greaterRow].getRed() + 
					oldPixels[c][greaterRow].getRed(); 
		}else if((r==59) && (c!=88) && (c!=0)){
			//5
			multiplier = 5; 
			sum = oldPixels[lesserCol][r].getRed() + 
					oldPixels[lesserCol][lesserRow].getRed() + 
					oldPixels[c][lesserRow].getRed() + 
					oldPixels[greaterCol][lesserRow].getRed() + 
					oldPixels[greaterCol][r].getRed();
		}else if((r==59) && (c==88)){
			//3
			multiplier = 3; 
			sum = oldPixels[lesserCol][r].getRed() + 
					oldPixels[lesserCol][lesserRow].getRed() + 
					oldPixels[c][lesserRow].getRed();
		}else{
			//8
			multiplier = 8; 
			sum = oldPixels[lesserCol][lesserRow].getRed() + 
					oldPixels[c][lesserRow].getRed() + 
					oldPixels[greaterCol][greaterRow].getRed() + 
					oldPixels[lesserCol][r].getRed() + 
					oldPixels[greaterCol][r].getRed() + 
					oldPixels[lesserCol][greaterRow].getRed() + 
					oldPixels[c][greaterRow].getRed() + 
					oldPixels[greaterCol][greaterRow].getRed();
		}
		//System.out.println("oldPixelVal: " + oldPixelVal);
		//System.out.println("mult: " + multiplier); 
		//System.out.println("sum: " + sum); 
		newPixelVal = (oldPixelVal*multiplier) - sum; 
		return newPixelVal; 
	}
	
	/*
	 * Compare two given images using the normalized
	 * L1 comparison, using edginess distribution. 
	 */
	public float compareHistograms(int[] histogram1, int[] histogram2){
		//TODO
		int width = 89;
		int height = 60; 
		int goodPixelsImage1 = (width*height) - histogram1[0]; 
		int goodPixelsImage2 = (width*height) - histogram2[0]; 
		int twoN = goodPixelsImage1 + goodPixelsImage2; 
		int global_color_distance = 0; 
		
		for(int b = 1; b<512; b++){
			int local_color_distance = Math.abs(histogram1[b] - histogram2[b]);
			//System.out.println("local: " + histogram1[b] + " " + histogram2[b] + " " + local_color_distance); 
			global_color_distance = global_color_distance + local_color_distance; 

		}//inner for
		float normalization = (float)global_color_distance/twoN; 
		//System.out.println("norm: " + normalization);
		//System.out.println("good: " + twoN  + " global: " + global_color_distance); 
		return normalization; 
	}
	
	/*
	 * Takes in target image histogram and target filename, and boolean most
	 * and uses compareHistory() outputs the three most or least similar
	 * images to the target image. 
	 */
	public String[] getThreeMostLeastSimilar(int[] originalHistogram, String originalFileName, boolean most){
		Map<String, Float> histoCompares = new HashMap<String, Float>(); 
		int counter = 0; 
		for(int i =1; i< 41; i++){
			String filename = "";
			if(i<10){
				filename = "i0" + Integer.toString(i) + ".ppm";
			}else{
				filename = "i" + Integer.toString(i) + ".ppm"; 
			}
			if(filename.equals(originalFileName)){
				continue; 
			}
			RGBPixel[][] pixels = readImage(filename);
			RGBPixel[][] bwImage = generateBWImage(pixels); //file comparing against
			RGBPixel[][] lapImage = createLaplacianImage(bwImage); 
			int[] tempHistogram = generateHistogram(lapImage);
			float normalize = compareHistograms(originalHistogram, tempHistogram); 
			//System.out.println("normnew: " + normalize); 
			histoCompares.put(filename, normalize); 
			//System.out.println("filename: " + filename); 
			counter++; 
		}
		//System.out.println("counter: " + counter); 
		// Sort the hashmap
		ValueComparator bvc =  new ValueComparator(histoCompares);
	    TreeMap<String,Float> sorted_map = new TreeMap<String,Float>(bvc);
	    sorted_map.putAll(histoCompares);
	    NavigableSet nset = sorted_map.descendingKeySet();
	    Iterator<String> iterator = nset.descendingIterator(); //names in descending order
		
	    String[] imagesToReturn = new String[3]; 
		
		//get the 3 most similar
		//else get 3 least similar
		//System.out.println("size: " + sorted_map.size()); 
		if(most){
			int count=0;
			iterator = nset.iterator(); //names in ascending order
			while (iterator.hasNext()) {
				if(count>2){
					break; 
				}else{
					String i = iterator.next();
					imagesToReturn[count] = i; 
					//System.out.println("Most Sim: " + i); 
				}
				count++; 
			}
		}else{
			int count=0;
			while (iterator.hasNext()) {
				if(count>2){
					break; 
				}else{
					String i = iterator.next();
					imagesToReturn[count] = i; 
					//System.out.println("Least Sim: " + i); 
				}
				count++; 
			}
		}
		
		return imagesToReturn; 
	}
	

	/*
	 * Outputs the four most or least similar images out of the 40
	 */
	public void getFourTopBottomSimilar(boolean most){
		//for each image, get the 3 most/least similar
		for(int i = 1; i < 41; i++){
			float local_least4 = 0; 
			float local_most4 = 0;
			String[] threeMostSim = new String[3];
			String[] threeLeastSim = new String[3];
			//get 3 more similar to that file
			//get the distance for each of those
			//compute the local 4's
			String filename = "";
			if(i<10){
				filename = "i0" + Integer.toString(i) + ".ppm";
			}else{
				filename = "i" + Integer.toString(i) + ".ppm"; 
			}
					
			if(most){
				RGBPixel[][] originalImage = readImage(filename);
				RGBPixel[][] bwImage = generateBWImage(originalImage); 
				RGBPixel[][] lapImage = createLaplacianImage(bwImage); 
				int[] histogram = generateHistogram(lapImage); 
				threeMostSim = getThreeMostLeastSimilar(histogram, filename, true); 
				for(String s: threeMostSim){
					RGBPixel[][] originalImage2 = readImage(s);
					RGBPixel[][] bwImage2 = generateBWImage(originalImage2); 
					RGBPixel[][] lapImage2 = createLaplacianImage(bwImage2); 
					int[] histogram2 = generateHistogram(lapImage2);
					float normalization = compareHistograms(histogram, histogram2); 
					local_most4 = local_most4 + normalization; 
				}
				if(local_most4 < most4simsum){
					//System.out.println("most " + local_most4); 
					most4simsum = local_most4; 
					most4sim[0] = filename; 
					//System.out.println("size: " + threeMostSim.length); 
					for(int j = 0; j<3; j++){
						most4sim[j+1] = threeMostSim[j]; 
						//System.out.println("mmm: " + most4sim[j+1]); 
					}
					
				}
				//System.out.println("local_m: " + local_most4); 
			}else{
				RGBPixel[][] originalImage = readImage(filename);
				RGBPixel[][] bwImage = generateBWImage(originalImage); 
				RGBPixel[][] lapImage = createLaplacianImage(bwImage); 
				int[] histogram = generateHistogram(lapImage); 
				threeLeastSim = getThreeMostLeastSimilar(histogram, filename, true); 
				for(String s: threeLeastSim){
					RGBPixel[][] originalImage2 = readImage(s);
					RGBPixel[][] bwImage2 = generateBWImage(originalImage2); 
					RGBPixel[][] lapImage2 = createLaplacianImage(bwImage2); 
					int[] histogram2 = generateHistogram(lapImage2);
					float normalization = compareHistograms(histogram, histogram2); 
					local_least4 = local_least4 + normalization; 
				}
				if(local_least4 > least4simsum){
					//System.out.println("least " + local_least4); 
					least4simsum = local_least4; 
					least4sim[0] = filename; 
					for(int j = 0; j<3; j++){
						least4sim[j+1] = threeLeastSim[j]; 
						//System.out.println("lll: " + least4sim[j+1]); 
					}
					
				}
				//System.out.println("local_l: " + local_least4); 
			}//end else
		}//end for
	}
	

	
	public static void main( String[] args ){
	      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	      TextureMatching textureMatching = new TextureMatching(); 
	      textureMatching.run(); 
	}
}
