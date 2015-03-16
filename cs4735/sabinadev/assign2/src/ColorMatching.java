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
	public void run(){
		// Read in the image
		RGBPixel[][] pixels1= new RGBPixel[89][60];
		RGBPixel[][] pixels2= new RGBPixel[89][60];
		pixels1 = readImage("i06.ppm"); 
		pixels2 = readImage("i07.ppm"); 
		for(int i = 0; i< 89; i++){
			for(int j = 0; j<60; j++){
				//System.out.println(pixels1[i][j].getBlue()); 
			}
		}
		int[][][] histogram1 = generateHistogram(pixels1); 
		int[][][] histogram2 = generateHistogram(pixels2); 
		
		float normalization = compareHistograms(histogram1, histogram2); 
		System.out.println("norm: " + normalization); 
		
		String [] threeMostSim= getThreeMostLeastSimilar(histogram1, "i06.ppm", false, 40); 
		
	}
	
   /*
    * Takes in an image and generates a 3D array for the RGB
    * values. Then based on the RGB value, creates a 3D
    * color histogram. 
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
		System.out.println("random: " + histogram[1][1][2]); 
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
	            System.out.println("line: " + line);
	            in2.skip((line+"\n").getBytes().length);
	            do {
	                line = in.readLine();
	                in2.skip((line+"\n").getBytes().length);
	                System.out.println("saw #"); 
	            } while (line.charAt(0) == '#');

	            // the current line has dimensions
	            st = new StringTokenizer(line);
	            width = Integer.parseInt(st.nextToken());
	            height = Integer.parseInt(st.nextToken());

	            // next line has pixel depth
	            line = in.readLine();
	            System.out.println("line2 " + line); 
	            in2.skip((line+"\n").getBytes().length);
	            st = new StringTokenizer(line);
	            depth = Integer.parseInt(st.nextToken());
	            System.out.println("depth: " + depth); 

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
	                System.out.println("a: " + a); 
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
		System.out.println("twoN: " + twoN);
		System.out.println("global: " + global_color_distance); 
		float normalization = (float)global_color_distance/twoN; 
		System.out.println("norm: " + normalization);
		return normalization; 
	}
	
	/*
	 * Takes in target image histogram and a list of histograms 
	 * and using compareHistograms() outputs the three most or 
	 * least similar images to the target images, based on the 
	 * boolean most
	 */
	public String[] getThreeMostLeastSimilar(int[][][] originalHistogram, String originalFileName, boolean most, int max){
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
			System.out.println("normnew: " + normalize); 
			histoCompares.put(filename, normalize); 
			System.out.println("filename: " + filename); 
			counter++; 
		}
		System.out.println("counter: " + counter); 
		// Sort the hashmap
		ValueComparator bvc =  new ValueComparator(histoCompares);
	    TreeMap<String,Float> sorted_map = new TreeMap<String,Float>(bvc);
	    sorted_map.putAll(histoCompares);
	    NavigableSet nset = sorted_map.descendingKeySet();
	    Iterator<String> iterator = nset.descendingIterator(); //names in descending order
	    iterator = nset.iterator();
		
	    String[] imagesToReturn = new String[3]; 
		
		//get the 3 most similar
		//else get 3 least similar
		System.out.println("size: " + sorted_map.size()); 
		if(most){
			int count=0;
			for (Map.Entry<String,Float> entry: histoCompares.entrySet()) {
			     if (count >239) break;
			     //imagesToReturn[count] = entry.getKey(); 
			     System.out.println("Most Sim: " + entry.getKey() + " " + entry.getValue()); 
			     //target.put(entry.getKey(), entry.getValue());
			     count++;
			}
		}else{
			int count=0;
			while (iterator.hasNext()) {
				if(count>39){
					break; 
				}else{
					String i = iterator.next();
					//imagesToReturn[counter] = i; 
					System.out.println("Least Sim: " + i); 
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
		//TODO
	}
	
	/*
	 * Takes in target image histogram and a list of histograms 
	 * and using compareHistograms() outputs the three least similar
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
	
	public static void main( String[] args )
   {
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
      ColorMatching colorMatching = new ColorMatching(); 
      colorMatching.run(); 
   }
}