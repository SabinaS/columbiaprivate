import java.io.*; 
import java.text.DecimalFormat;
import java.util.*; 

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
			RGBPixel[][] pixels= new RGBPixel[89][60];
			pixels = readImage(filename);
			int[][][] histogram = generateHistogram(pixels); 
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
		
	}
	
   /*
    * Takes in an array of RGBPixels and based on 
    * the RGB value, creates a 3D color histogram. 
    */
	public int[][][] generateHistogram(RGBPixel[][] pixels){
		int[][][] histogram = new int[8][8][8]; //int array auto filled with 0's
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				for(int c = 0; c < 8; c++){
					histogram[a][b][c] = 0; 
				}
			}
		}//outer for
		
		for(int c = 0; c< 89; c++){
			for(int r=0; r<60; r++){
				RGBPixel pix = pixels[c][r]; 
				int newRed = pix.getRed()/32;
				//System.out.println("red " + newRed); 
				int newGreen = pix.getGreen()/32;
				//System.out.println("green: " + newGreen);
				int newBlue = pix.getBlue()/32; 
				//System.out.println("blue: " + newBlue); 
				if((newRed == 0) && (newGreen == 0) && (newBlue ==0)){
					break;
				}else{
					histogram[newRed][newGreen][newBlue] = histogram[newRed][newGreen][newBlue] + 1; 
					//System.out.println("Histo: " + histogram[newRed][newGreen][newBlue]); 
				}
			}
		}
		//System.out.println("random: " + histogram[1][1][2]); 
		return histogram; 
	}
	
	public static RGBPixel[][] readImage(String fileName)
	{
		 RGBPixel[][] pixels = new RGBPixel[89][60];
		 RGBPixel pixel = new RGBPixel(0,0,0); 
		 for(RGBPixel[] row: pixels){
			 Arrays.fill(row, pixel); 
		 }
		 int depth,width,height;
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
	            //System.out.println("line: " + line);
	            in2.skip((line+"\n").getBytes().length);
	            do {
	                line = in.readLine();
	                in2.skip((line+"\n").getBytes().length);
	                //System.out.println("saw #"); 
	            } while (line.charAt(0) == '#');

	            // the current line has dimensions
	            st = new StringTokenizer(line);
	            width = Integer.parseInt(st.nextToken());
	            height = Integer.parseInt(st.nextToken());

	            // next line has pixel depth
	            line = in.readLine();
	            //System.out.println("line2 " + line); 
	            in2.skip((line+"\n").getBytes().length);
	            st = new StringTokenizer(line);
	            depth = Integer.parseInt(st.nextToken());
	            //System.out.println("depth: " + depth); 

	            // read pixels now
	            int a = 0; 
	            int b = 0; 
	                for (int c = 0; c < 89; c++){
	                    for (int r = 0; r < 60; r++){
	                    	//int x = in2.readUnsignedByte(); 
	                    	//System.out.println("x : " + x); 
	                    	int redVal = in2.readUnsignedByte();
	                    	int greenVal = in2.readUnsignedByte();
	                    	int blueVal = in2.readUnsignedByte(); 
	                    	RGBPixel pix = new RGBPixel(redVal, greenVal, blueVal); 
	                    	/*pixels[c][r].setRed(redVal);
	                    	pixels[c][r].setGreen(greenVal);
	                    	pixels[c][r].setBlue(blueVal); */
	                    	pixels[c][r] = pix; 
	                    	a++; 
	                    	//System.out.println("red: " + greenVal); 
	                    	//System.out.println("get " + pixels[c][r].getRed()); 
	                    }
	                    b++; 
	                }//outer for
	                //System.out.println("a: " + a); 
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
	 * Takes in two histograms and using the normalized L1
	 * comparison, compares the histograms and determines 
	 * a value between 0 and 1 inclusive of how similar they are. 
	 */
	public float compareHistograms(int [][][] histogram1, int [][][] histogram2){
		int width = 89;
		int height = 60; 
		/* The good pixels are the total pixels minus any black pixels */
		int goodPixelsImage1 = (width*height) - histogram1[0][0][0]; 
		int goodPixelsImage2 = (width*height) - histogram2[0][0][0]; 
		int twoN = goodPixelsImage1 + goodPixelsImage2; 
		int global_color_distance = 0; 

		for(int r = 0; r<8; r++){
			for(int g = 0; g<8; g++){
				for(int b = 0; b<8; b++){
					if((r==0) && (g==0) && (b==0)){
						break;
					}else{
						int local_color_distance = Math.abs(histogram1[r][g][b] - histogram2[r][g][b]);
						//System.out.println("local: " + histogram1[r][g][b] + " " + histogram2[r][g][b] + " " + local_color_distance); 
						global_color_distance = global_color_distance + local_color_distance; 
					}//else
				}//inner for
			}//middle for
		}//outer for
		//System.out.println("global: " + global_color_distance); 
		float normalization = (float)global_color_distance/twoN; 
		//System.out.println("norm: " + normalization);
		return normalization; 
	}
	
	/*
	 * Takes in target image histogram and a list of histograms 
	 * and using compareHistograms() outputs the three most or 
	 * least similar images to the target images, based on the 
	 * boolean most
	 */
	public String[] getThreeMostLeastSimilar(int[][][] originalHistogram, String originalFileName, boolean most){
		//TODO
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
			RGBPixel[][] tempPixels = readImage(filename); 
			int[][][] tempHistogram = generateHistogram(tempPixels);
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
	 * Takes in target image histogram and a list of histograms 
	 * and using compareHistograms() outputs the three most similar
	 * images to the target images. 
	 */
	public void getThreeMostSimilar(){
		//Don't need
	}
	
	/*
	 * Takes in target image histogram and a list of histograms 
	 * and using compareHistograms() outputs the three least similar
	 * images to the target images. 
	 */
	public void getThreeLeastSimilar(){
		//Don't need
	}
	
	/*
	 * Outputs the four most similar images out of the 40
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
				RGBPixel[][] pixels = new RGBPixel[89][60]; 
				pixels = readImage(filename); 
				int[][][] histogram = generateHistogram(pixels); 
				threeMostSim = getThreeMostLeastSimilar(histogram, filename, true); 
				for(String s: threeMostSim){
					RGBPixel[][] pixels2 = new RGBPixel[89][60]; 
					pixels2 = readImage(s); 
					int[][][] histogram2 = generateHistogram(pixels2);
					float normalization = compareHistograms(histogram, histogram2); 
					local_most4 = local_most4 + normalization; 
				}
				if(local_most4 < most4simsum){
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
				RGBPixel[][] pixels = new RGBPixel[89][60]; 
				pixels = readImage(filename); 
				int[][][] histogram = generateHistogram(pixels); 
				threeLeastSim = getThreeMostLeastSimilar(histogram, filename, true); 
				for(String s: threeLeastSim){
					RGBPixel[][] pixels2 = new RGBPixel[89][60]; 
					pixels2 = readImage(s); 
					int[][][] histogram2 = generateHistogram(pixels2);
					float normalization = compareHistograms(histogram, histogram2); 
					local_least4 = local_least4 + normalization; 
				}
				if(local_least4 > least4simsum){
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
	
	/*
	 * Outputs the four least similar images out of the 40
	 */
	public String[] getFourTopSimilar(){
		return most4sim;
	}
	
	/*
	 * Outputs the four least similar images out of the 40
	 */
	public String[] getFourBottomSimilar(){
		return least4sim; 
	}
	public static void main( String[] args )
   {
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
      ColorMatching colorMatching = new ColorMatching(); 
      colorMatching.run(); 
   }
}