import org.opencv.core.Core;

import java.io.*; 
import java.text.DecimalFormat;
import java.util.*; 

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class WhatDescriptions {
	
	private int magic_small = 4000; //TODO Magic number for small area
	private int magic_large = 7000; //TODO Magic number for large area
	private int magic_largest; // Corresponds to the integer value of the building
	private int magic_smallest; //Corresponds to the integer value of the building; 

	public void run(){
		//TODO
		int[][] pixels= new int[275][495];
		pixels = readImage("ass3-labeled.pgm");
		
	}
	
	public static int[][] readImage(String fileName)
	{
		 int WIDTH = 275;
		 int HEIGHT = 495; 
		 int[][] pixels = new int[WIDTH][HEIGHT];
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

	            // read pixels now
	            int a = 0; 
	            int b = 0; 
	                for (int c = 0; c < WIDTH; c++){
	                    for (int r = 0; r < HEIGHT; r++){
	                    	//int x = in2.readUnsignedByte(); 
	                    	//System.out.println("x : " + x); 
	                    	int pix = in2.readUnsignedByte(); 
	                    	pixels[c][r] = pix; 
	                    	a++; 
	                    	System.out.println("pix: " + pix); 
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
	
	public boolean isSmall(Building s){
		boolean isSmallBool = false;
		int area = s.getArea(); //TODO get area from pixels
		if(area<magic_small){
			isSmallBool= true;
		}
		return isSmallBool; 
	}
	
	public boolean isMedium(Building s){
		boolean isMediumBool = false;
		int area = s.getArea(); //TODO get area from pixels
		if(area<magic_large && area>magic_small){
			isMediumBool= true;
		}
		return isMediumBool; 
	}
	
	public boolean isLarge(Building s){
		boolean isLargeBool = false;
		int area = s.getArea(); //TODO get area from pixels
		if(area<magic_small){
			isLargeBool= true;
		}
		return isLargeBool; 
	}
	
	public int getSmallest(Building[] buildings){
		//TODO
		int smallest = buildings[0].getArea(); // Corresponds to the integer value of the building
		//Look through all buildings and get smallest area
		for(Building s : buildings){
			if(s.getArea() < smallest){
				smallest = s.getArea(); //TODO
			}
		}
		
		return smallest; 
	}
	
	public int getLargest(Building[] buildings){
		//TODO
		int largest = buildings[0].getArea(); // Corresponds to the integer value of the building
		//Look through all buildings and get smallest area
		for(Building s : buildings){
			if(s.getArea() > largest){
				largest = s.getArea(); //TODO
			}
		}
		
		return largest;  
	}
	
	public boolean isRectangle(Building s){
		boolean isRectangle = true;
		int width = s.getWidth();
		int height = s.getHeight();
		
		for(each x in height){
			for(each y in width){
				if(width == filled){
					continue;
				}
				else{
					isRectangle = false; 
				}
			}
		}
		
		return isRectangle; 
	}
	
	public boolean isSquare(){
		boolean isSquare = false;
		if(isRectangle()){
			if(building.height == building.width){
				isSquare = true; 
			}
		}
		
		return isSquare; 
	}
	
	public boolean isNonRectangle(){
		boolean isNonRectangle = false; 
		if(!isSquare() && !isRectangle()){
			isNonRectangle = true; 
		}
		return isNonRectangle; 
	}
	
	public boolean hasSimpleBoundary(){
		boolean hasSimple = false;
		//TODO
		return hasSimple; 
	}
	
	public boolean hasJaggedBoundary(){
		boolean hasJagged = false;
		//TODO
		return hasJagged; 
	}
	
	public boolean isIShaped(){
		boolean isIShaped = false;
		//TODO
		return isIShaped; 
	}
	
	public boolean isLShaped(){
		boolean isLShaped = false;
		//TODO
		return isLShaped; 
	}
	
	public boolean isSymmetricEastWest(){
		boolean isSymmetricEW = false;
		//TODO
		return isSymmetricEW; 
	}
	
	public boolean isSymmetricNorthSouth(){
		boolean isSymmetricNS = false;
		//TODO
		return isSymmetricNS; 
	}
	
	public boolean isOrientedEastWest(){
		boolean isOrientedEW = false;
		//TODO
		return isOrientedEW; 
	}
	
	public boolean isOrientedNorthSouth(){
		boolean isOrientedNS = false;
		//TODO
		return isOrientedNS; 
	}
	
	public boolean isLocatedCentrally(){
		boolean isLocatedCentrally = false;
		//TODO
		return isLocatedCentrally;
	}
	
	public boolean isLocatedOnBorder(){
		boolean isLocatedOnBorder = false;
		//TODO
		return isLocatedOnBorder;
	}
	
	public boolean isNorthernMost(){
		boolean isNorthernMost = false;
		//TODO
		return isNorthernMost;
	}
	
	public boolean isSouthernMost(){
		boolean isSouthernMost = false;
		//TODO
		return isSouthernMost;
	}
	
	public boolean isEasternMost(){
		boolean isEasternMost = false;
		//TODO
		return isEasternMost;
	}
	
	public boolean isWesternMost(){
		boolean isWesternMost = false;
		//TODOv v v
		return isWesternMost;
	}
	
	public static void main( String[] args )
	{
		  System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		  WhatDescriptions whatDescriptions = new WhatDescriptions(); 
		  whatDescriptions.run(); 
	}
}
