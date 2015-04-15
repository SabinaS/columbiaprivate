import org.opencv.core.Core;

import java.io.*; 
import java.text.DecimalFormat;
import java.util.*; 

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class WhatDescriptions {
	
	private int magic_small = 582; //TODO Magic number for small area
	private int magic_large = 4400; //TODO Magic number for large area
	private int magic_largest; // Corresponds to the integer value of the building
	private int magic_smallest; //Corresponds to the integer value of the building; 
	private int smallest;
	private int largest; 

	public void run(){
		//TODO
		int WIDTH = 275;
		int HEIGHT = 495; 
		int[][] pixels= new int[WIDTH][HEIGHT];
		pixels = readImage("ass3-labeled.pgm", WIDTH, HEIGHT);
		Building[] buildingList = new Building[28]; // 0-27, but will only count 1-27
		HashMap<Integer, ArrayList<String>> buildingDescriptions = new HashMap<>(); 
		
		//Initializing the buildings
		for(int a = 0; a<28; a++){
			Building b = new Building(a, 0); 
			buildingList[a] = b; 
		}
		
		//We start the height at 4 because the first 4 values are extraneous 
		for(int i = 0; i < WIDTH; i++){
			for(int j = 0; j < HEIGHT; j++){
				int pixelVal = pixels[i][j];
				if(!((i==0) && ((j==0) || (j==1) || (j==2) || (j==3)))){
					//System.out.println("pixelVal " + pixelVal + " " + i + " "+ j); 
					//Increment the area for each pixel belonging to that building
					buildingList[pixelVal].setArea(buildingList[pixelVal].getArea() +1); 
				}
			}
		}
		smallest = 10000; 
		largest = 0; 
		//Adding the area, and whether the building is small, medium or large to the descriptions 
		for(int c = 1; c < 28; c++){
			//System.out.println("area: " + buildingList[c].getArea());
			ArrayList<String> descr = new ArrayList<>();
			String areaSentence = "with area " + Integer.toString(buildingList[c].getArea()); 
			descr.add(areaSentence); 
			
			if(isSmall(buildingList[c])){
				String smallSentence = "is a small building";
				descr.add(smallSentence); 
			}
			if(isMedium(buildingList[c])){
				String mediumSentence = "is a medium building";
				descr.add(mediumSentence); 
			}
			if(isLarge(buildingList[c])){
				String largeSentence = "is a large building";
				descr.add(largeSentence); 
			}
			if(buildingList[c].getArea() < smallest){
				smallest = buildingList[c].getArea();
				magic_smallest = c; //the number of the smallest building
			}
			if(buildingList[c].getArea() > largest){
				largest = buildingList[c].getArea();
				magic_largest = c; //the number of the largest building
			}
			buildingDescriptions.put(c, descr); 	
		}
		
		//Adding smallest and largest description
		ArrayList<String> tempDescr = buildingDescriptions.get(magic_smallest);
		String smallestSentence = "is the smallest building";
		tempDescr.add(smallestSentence);
		buildingDescriptions.put(magic_smallest, tempDescr); 
		
		ArrayList<String> tempLargeDescr = buildingDescriptions.get(magic_largest);
		String largestSentence = "is the largest building";
		tempLargeDescr.add(largestSentence);
		buildingDescriptions.put(magic_largest, tempLargeDescr); 
		
		
		//Adding the building names
		//TODO
		
		//Test
		for(int d = 1; d < 28; d++){
			Building b = buildingList[d]; 
			ArrayList<String> descr = buildingDescriptions.get(d); 
			System.out.println("building num: " + b.getBuildingNumber());
			for(String s: descr){
				System.out.println(s); 
			}
		}
		
		determineCenterOfMass("ass3-campus.jpg"); 
		
		
		
	}//end run
	
	public static int[][] readImage(String fileName, int newWidth, int newHeight)
	{
		 int WIDTH = newWidth;
		 int HEIGHT = newHeight; 
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
	                    	if(a<100){
	                    		//System.out.println("pix: " + pix); 
	                    	}
	                    }
	                    b++; 
	            }//outer for
	            //System.out.println("a: " + a); 
	            //System.out.println("b: " + b); 
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
	
	public void determineCenterOfMass(String filename){
		Mat hu = new Mat(); 
		
		// Find the contours 
		Mat image = getMat(filename); 
	    Mat imageHSV = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageBlurr = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageThresh = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	    Mat imageCanny = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    //Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    Imgproc.adaptiveThreshold(imageHSV, imageThresh, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
	    Imgproc.Canny(image, imageCanny, 100, 200); 
	    //Imgproc.Canny(imageHSV, imageCanny, 100, 200); 
	    Highgui.imwrite("Edges.jpg",imageCanny);
	    
	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
	    Imgproc.findContours(imageCanny, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    System.out.println("Contour size: " + contours.size());

	    // Draw the contours
	    //Mat drawing = new Mat(image.size(), Core.DEPTH_MASK_8U); 
	    Mat mask = Mat.zeros(image.rows(),image.cols(),image.type());
	    for( int i = 0; i< contours.size(); i++ )
	    {
	        //Scalar color = new Scalar( 0,0,255);
	        //Imgproc.drawContours(drawing, contours, i, color, 1);
	        Imgproc.drawContours(mask, contours, -1, new Scalar(0,0,255));
	        //System.out.println("contourArea: " + Imgproc.contourArea(contours.get(i)));

	    }   
	    Highgui.imwrite("Contours.jpg",mask);
	    
	    //Find the moments
	    System.out.println("contour size: " + contours.size()); 
	    List<Moments> mu = new ArrayList<Moments>(contours.size());
	    for (int i = 0; i < contours.size(); i++) {
	        mu.add(i, Imgproc.moments(contours.get(i), false));
	        Moments p = mu.get(i);
	        int x = (int) (p.get_m10() / p.get_m00());
	        int y = (int) (p.get_m01() / p.get_m00());
	        System.out.println("moments: " + i + " x: "+ x + " y: "+ y); 
	        Core.circle(image, new Point(x, y), 4, new Scalar(255,49,0,255));
	    }
	}
	
	public Mat getMat(String filename){
		System.out.println("fileeeeeeeeeeeeee: " + filename); 
		Mat image = Highgui.imread(getClass().getResource(filename).getPath());
		return image; 
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
		if(area>magic_large){
			isLargeBool= true;
		}
		return isLargeBool; 
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
