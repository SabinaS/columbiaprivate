import java.io.*; 
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
		//TODO
		// Read in the image
		//Mat src;  
		//src = Highgui.imread(getClass().getResource("Images/i15.jpg").getPath());
		RGBPixel[][] pixels= new RGBPixel[89][60];
		pixels = readImage("i15.ppm"); 
		for(int i = 0; i< 89; i++){
			for(int j = 0; j<60; j++){
				//System.out.println(pixels[i][j].getBlue()); 
			}
		}
		generateHistogram(pixels); 
		
	}
	
   /*
    * Takes in an image and generates a 3D array for the RGB
    * values. Then based on the RGB value, creates a 3D
    * color histogram. 
    */
	public void generateHistogram(RGBPixel[][] pixels){
		int[][][] histogram = new int[8][8][8]; //int array auto filled with 0's
		for(int a = 0; a < 8; a++){
			for(int b = 0; b < 8; b++){
				for(int c = 0; c < 8; c++){
					histogram[a][b][c] = 0; 
				}
			}
		}
		
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
		System.out.println("random: " + histogram[6][2][2]); 
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
	
	public static void main( String[] args )
   {
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
      ColorMatching colorMatching = new ColorMatching(); 
      colorMatching.run(); 
   }
}