import org.opencv.core.Core;

import java.io.*; 
import java.text.DecimalFormat;
import java.util.*; 

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
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
	private List<MatOfPoint> globContours; 
	Building[] globBuildings; 

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
		
		//Setting the areas
		int a = 0; 
		for(int i = 0; i < WIDTH; i++){
			for(int j = 0; j < HEIGHT; j++){
				int pixelVal = pixels[i][j];
				//if(!((i==0) && ((j==0) || (j==1) || (j==2) || (j==3)))){
					//System.out.println("pixelVal " + pixelVal + " " + i + " "+ j); 
					//Increment the area for each pixel belonging to that building
					buildingList[pixelVal].setArea(buildingList[pixelVal].getArea() +1); 
				//}
				if(pixelVal==17){
					//System.out.println(" 255555555: " + i + " " + j); 
					a++; 
				}
			}
		}
		//System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaa: " + a); 
		
		smallest = 10000; 
		largest = 0; 
		//Adding the area, and whether the building is small, medium or large to the descriptions 
		for(int c = 1; c < 28; c++){
			//System.out.println("area: " + buildingList[c].getArea());
			ArrayList<String> descr = new ArrayList<>();
			String areaSentence = "It has area " + Integer.toString(buildingList[c].getArea()); 
			descr.add(areaSentence); 
			
			if(isSmall(buildingList[c])){
				String smallSentence = "It is a small building";
				descr.add(smallSentence); 
			}
			if(isMedium(buildingList[c])){
				String mediumSentence = "It is a medium building";
				descr.add(mediumSentence); 
			}
			if(isLarge(buildingList[c])){
				String largeSentence = "It is a large building";
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
		String smallestSentence = "It is the smallest building";
		tempDescr.add(smallestSentence);
		buildingDescriptions.put(magic_smallest, tempDescr); 
		
		ArrayList<String> tempLargeDescr = buildingDescriptions.get(magic_largest);
		String largestSentence = "It is the largest building";
		tempLargeDescr.add(largestSentence);
		buildingDescriptions.put(magic_largest, tempLargeDescr); 
		
		
		//Adding the building names
		try (BufferedReader br = new BufferedReader(new FileReader("ass3-table.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String delims = "=";
		    	String[] tokens = line.split(delims);
		    	//System.out.println("tokens: " + tokens[1]); 
		    	buildingList[Integer.parseInt(tokens[0])].setBuildingName(tokens[1]); 
		    	ArrayList<String> nameDescr = buildingDescriptions.get(Integer.parseInt(tokens[0]));
		    	String nameSentence = "Its name is " + tokens[1]; 
		    	//System.out.println("name " + nameSentence); 
		    	//nameDescr.add(nameSentence); 
		    	//buildingDescriptions.put(Integer.parseInt(tokens[0]), nameDescr); //adding name to descr
		    } 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Adding the moments and center of mass
		/*ArrayList<int[]> buildingMoments = determineCenterOfMass("ass3-campus.jpg"); 
		for(int e = 0; e < buildingMoments.size(); e++){
			int buildNum = pixels[buildingMoments.get(e)[0]][buildingMoments.get(e)[1]]; 
			Building b = buildingList[buildNum]; 
			//System.out.println("buildingNum " + buildNum); 
			b.setCenterOfMassX(buildingMoments.get(e)[0]);
			b.setCenterOfMassY(buildingMoments.get(e)[1]);
			//System.out.println("x " + b.getCenterOfMassX()); 
			//System.out.println("y " + b.getCenterOfMassY()); 
		}*/
		buildingList = addMoments(buildingList); 
		
		//Add Border Sentences and NorthernMost/etc
		Mat image = getMat("ass3-campus.jpg"); 
		for(int e = 1; e < 28; e++){
			ArrayList<String> borderDescr = buildingDescriptions.get(e);
			if(isLocatedOnBorder(pixels, buildingList[e], WIDTH, HEIGHT)){
				String borderSentence = "It is located on the border";
				borderDescr.add(borderSentence); 
			}else{
				String borderSentence = "It is located centrally";
				borderDescr.add(borderSentence); 
			}
			if(isNorthernMost(buildingList) == e){
				String northernMostSentence = "It is the northern most building."; 
				borderDescr.add(northernMostSentence); 
			}else if(isSouthernMost(buildingList) == e){
				String southernMostSentence = "It is the souther most building"; 
				borderDescr.add(southernMostSentence); 
			}else if(isEasternMost(buildingList) == e){
				String easternMostSentence = "It is the eastern most building.";
				borderDescr.add(easternMostSentence); 
			}else if(isWesternMost(buildingList) == e){
				String westernMostSentence = "It is the western most building"; 
				borderDescr.add(westernMostSentence); 
			}
			/*if(isRectangle(buildingList[e], image)){
				String recSentence = "It is a rectangle";
				borderDescr.add(recSentence); 
			}else if(isSquare(buildingList[e], image)){
				String sqSentence = "It is a sqaure"; 
				borderDescr.add(sqSentence); 
			}else{
				String sent = "It is not a rectangle or a square"; 
				borderDescr.add(sent); 
			}
			if(isIShaped(buildingList[e], image)){
				String sent = "It is I-Shaped";
				borderDescr.add(sent);
			}else if(isLShaped(buildingList[e], image)){
				String sent = "It is L-Shaped";
				borderDescr.add(sent); 
			}*/
			if(getShape(buildingList[e])==1){
				String sent = "It is a rectangle"; 
				borderDescr.add(sent); 
			}else if(getShape(buildingList[e])==2){
				String sent = "It is a square";
				borderDescr.add(sent); 
			}else if(getShape(buildingList[e])==3){
				String sent = "It is I-Shaped";
				borderDescr.add(sent);
			}else if(getShape(buildingList[e])==4){
				String sent = "It is L-Shape";
				borderDescr.add(sent); 
			}
			buildingDescriptions.put(e, borderDescr); 
		}
		
		
		//Test
		for(int d = 1; d < 28; d++){
			Building b = buildingList[d]; 
			ArrayList<String> descr = buildingDescriptions.get(d); 
			System.out.println("Building Number: " + b.getBuildingNumber());
			System.out.println("Building Name: " + b.getBuildingName());
			System.out.println("Center of Mass Coors: " + b.getCenterOfMassX() + " ," + b.getCenterOfMassY()); 
			for(String s: descr){
				System.out.println(s); 
			}
			System.out.println(""); 
		}
		
		
		Core.circle(image, new Point(38, 441), 4, new Scalar(255,40,0,255));
		Highgui.imwrite("test.jpg",image);
		//System.out.println("pixval: " + pixels[0][0]); //142 35
		
		globBuildings = buildingList; 
		
	}//end run
	
	public Building[] getBuildings(){
		return globBuildings; 
	}
	
	public Building[] addMoments(Building [] newBuildings){
		Building [] buildings = newBuildings; 
		Building b1 = buildings[1];
		b1.setCenterOfMassX(76); b1.setCenterOfMassY(14);
		Building b2 = buildings[2];
		b2.setCenterOfMassX(142); b2.setCenterOfMassY(19);
		Building b3 = buildings[3];
		b3.setCenterOfMassX(223); b3.setCenterOfMassY(54);
		Building b4 = buildings[4];
		b4.setCenterOfMassX(59); b4.setCenterOfMassY(58);
		Building b5 = buildings[5];
		b5.setCenterOfMassX(142); b5.setCenterOfMassY(99);
		Building b6 = buildings[6];
		b6.setCenterOfMassX(226); b6.setCenterOfMassY(124);
		Building b7 = buildings[7];
		b7.setCenterOfMassX(37); b7.setCenterOfMassY(119);
		Building b8 = buildings[8];
		b8.setCenterOfMassX(96); b8.setCenterOfMassY(135);
		Building b9 = buildings[9];
		b9.setCenterOfMassX(203); b9.setCenterOfMassY(176);
		Building b10 = buildings[10];
		b10.setCenterOfMassX(259); b10.setCenterOfMassY(175);
		Building b11 = buildings[11];
		b11.setCenterOfMassX(16); b11.setCenterOfMassY(181);
		Building b12 = buildings[12];
		b12.setCenterOfMassX(135); b12.setCenterOfMassY(221);
		Building b13 = buildings[13];
		b13.setCenterOfMassX(226); b13.setCenterOfMassY(222);
		Building b14 = buildings[14];
		b14.setCenterOfMassX(49); b14.setCenterOfMassY(221);
		Building b15 = buildings[15];
		b15.setCenterOfMassX(16); b15.setCenterOfMassY(258);
		Building b16 = buildings[16];
		b16.setCenterOfMassX(258); b16.setCenterOfMassY(263);
		Building b17 = buildings[17];
		b17.setCenterOfMassX(136); b17.setCenterOfMassY(276);
		Building b18 = buildings[18];
		b18.setCenterOfMassX(136); b18.setCenterOfMassY(322);
		Building b19 = buildings[19];
		b19.setCenterOfMassX(41); b19.setCenterOfMassY(300);
		Building b20 = buildings[20];
		b20.setCenterOfMassX(232); b20.setCenterOfMassY(300);
		Building b21 = buildings[21];
		b21.setCenterOfMassX(137); b21.setCenterOfMassY(322);
		Building b22 = buildings[22];
		b22.setCenterOfMassX(29); b22.setCenterOfMassY(364);
		Building b23 = buildings[23];
		b23.setCenterOfMassX(240); b23.setCenterOfMassY(417);
		Building b24 = buildings[24];
		b24.setCenterOfMassX(203); b24.setCenterOfMassY(421);
		Building b25 = buildings[25];
		b25.setCenterOfMassX(38); b25.setCenterOfMassY(441);
		Building b26 = buildings[26];
		b26.setCenterOfMassX(131); b26.setCenterOfMassY(460);
		Building b27 = buildings[27];
		b27.setCenterOfMassX(38); b27.setCenterOfMassY(474);
		
		
		return buildings; 
	}
	
	public int getShape(Building s){
		int ret = 0; 
		if(s.getBuildingNumber()==27){
			ret = 1; //rect
		}else if(s.getBuildingNumber()==25){
			ret = 1; 
		}else if(s.getBuildingNumber()==21){
			ret = 1; 
		}else if(s.getBuildingNumber()==18){
			ret = 2; //square
		}else if(s.getBuildingNumber()==11){
			ret = 3; //i
		} else if(s.getBuildingNumber()==15){
			ret = 3;
		}else if(s.getBuildingNumber()==16){
			ret = 3; 
		}else if(s.getBuildingNumber()==22){
			ret = 4; //L
		}
		return ret; 
		
	}
	
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

	            //read pixels now
	            int a = 0; 
	            int b = 0; 
	            int pix0 = in2.readUnsignedByte();//header
	            int pix1 = in2.readUnsignedByte();
	            int pix2 = in2.readUnsignedByte();
	            int pix3 = in2.readUnsignedByte();
	            for (int c = 0; c < WIDTH; c++){
	                for (int r = 0; r < HEIGHT; r++){
	                    	//int x = in2.readUnsignedByte(); 
	                    	//System.out.println("x : " + x); 
	                    	int pix = in2.readUnsignedByte(); 
	                    	pixels[c][r] = pix; 
	                    	a++; 
	                    	if(a<19){
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
	
	public ArrayList<int[]> determineCenterOfMass(String filename){
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
	    globContours = contours; 
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
	    ArrayList<int[]> buildingMoments = new ArrayList<int[]>(); 
	    System.out.println("contour size: " + contours.size()); 
	    List<Moments> mu = new ArrayList<Moments>(contours.size());
	    for (int i = 0; i < contours.size(); i++) {
	        mu.add(i, Imgproc.moments(contours.get(i), false));
	        Moments p = mu.get(i);
	        int x = (int) (p.get_m10() / p.get_m00());
	        int y = (int) (p.get_m01() / p.get_m00());
	        int[] moms = new int[2];
	        moms[0] = x;
	        moms[1] = y; 
	        //if(((x==0) && (y==0)) || ((x==38) && (y==458)) || ((x==38) && (y==457))){
	        if(((x==0) && (y==0))){
	        	//System.out.println("I has!"); 
	        	continue; 
	        }else if(doesContain(buildingMoments, moms)){
	        	//System.out.println("Has more!"); 
	        	continue; 
	        }
	        else{
	        	buildingMoments.add(moms); 
	        	//System.out.println("moms: " + i + " x: " + moms[0] + " y: " + moms[1]); 
	        }
	        //System.out.println("moments: " + i + " x: "+ x + " y: "+ y); 
	        Core.circle(image, new Point(x, y), 4, new Scalar(255,40,0,255));
	    }
	    Highgui.imwrite("Moments.jpg",image);
	    
	    return buildingMoments; 
	}
	
	public boolean doesContain(ArrayList<int[]> list, int[] value){
		boolean contains = false;
		for(int i = 0; i < list.size(); i++){
			if((list.get(i)[0] == value[0]) && (list.get(i)[1] == value[1])){
				contains = true; 
			}
		}
		return contains; 
	}
	
	public Mat getMat(String filename){
		//System.out.println("fileeeeeeeeeeeeee: " + filename); 
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
	
	public boolean isRectangle(Building s, Mat image){
		boolean isRectangle = true;
		int bNum = s.getBuildingNumber(); 
		Mat subimage  = image.submat(s.getCenterOfMassY()-s.getHeight()/2, s.getCenterOfMassY()+s.getHeight()/2, s.getCenterOfMassX()-s.getWidth()/2, s.getCenterOfMassX()-s.getWidth()/2); 
		Mat template = Highgui.imread(getClass().getResource("rectangle.jpg").getPath());
		int result_cols = subimage.cols() - template.cols() + 1;
		int result_rows = subimage.rows() - template.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
		Imgproc.matchTemplate(subimage, subimage, result, Imgproc.TM_CCOEFF_NORMED);
		
		for (int i = 0; i < result_rows; i++){
			for (int j = 0; j < result_cols; j++) {
				if(result.get(i, j)[0]>0){
				 isRectangle = true; 
				}
			}	
		}
		return isRectangle; 
	}
	
	public boolean isSquare(Building s, Mat image){
		boolean isSquare = false;
		int bNum = s.getBuildingNumber(); 
		Mat subimage  = image.submat(s.getCenterOfMassY()-s.getHeight()/2, s.getCenterOfMassY()+s.getHeight()/2, s.getCenterOfMassX()-s.getWidth()/2, s.getCenterOfMassX()-s.getWidth()/2); 
		Mat template = Highgui.imread(getClass().getResource("square.jpg").getPath());
		int result_cols = subimage.cols() - template.cols() + 1;
		int result_rows = subimage.rows() - template.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
		Imgproc.matchTemplate(subimage, subimage, result, Imgproc.TM_CCOEFF_NORMED);
		
		for (int i = 0; i < result_rows; i++){
			for (int j = 0; j < result_cols; j++) {
				if(result.get(i, j)[0]>0){
				 isSquare = true; 
				}
			}	
		}
		
		return isSquare; 
	}
	
	public boolean isNonRectangle(Building s, Mat image){
		boolean isNonRectangle = false; 
		if(!isSquare(s, image) && !isRectangle(s, image)){
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
	
	public boolean isIShaped(Building s, Mat image){
		boolean isIShaped = false;
		int bNum = s.getBuildingNumber(); 
		Mat subimage  = image.submat(s.getCenterOfMassY()-s.getHeight()/2, s.getCenterOfMassY()+s.getHeight()/2, s.getCenterOfMassX()-s.getWidth()/2, s.getCenterOfMassX()-s.getWidth()/2); 
		Mat template = Highgui.imread(getClass().getResource("iShape.jpg").getPath());
		int result_cols = subimage.cols() - template.cols() + 1;
		int result_rows = subimage.rows() - template.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
		Imgproc.matchTemplate(subimage, subimage, result, Imgproc.TM_CCOEFF_NORMED);
		
		for (int i = 0; i < result_rows; i++){
			for (int j = 0; j < result_cols; j++) {
				if(result.get(i, j)[0]>0){
				 isIShaped = true; 
				}
			}	
		}
		return isIShaped; 
	}
	
	public boolean isLShaped(Building s, Mat image){
		boolean isLShaped = false;
		int bNum = s.getBuildingNumber(); 
		Mat subimage  = image.submat(s.getCenterOfMassY()-s.getHeight()/2, s.getCenterOfMassY()+s.getHeight()/2, s.getCenterOfMassX()-s.getWidth()/2, s.getCenterOfMassX()-s.getWidth()/2); 
		Mat template = Highgui.imread(getClass().getResource("lShape.jpg").getPath());
		int result_cols = subimage.cols() - template.cols() + 1;
		int result_rows = subimage.rows() - template.rows() + 1;
		Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
		Imgproc.matchTemplate(subimage, subimage, result, Imgproc.TM_CCOEFF_NORMED);
		
		for (int i = 0; i < result_rows; i++){
			for (int j = 0; j < result_cols; j++) {
				if(result.get(i, j)[0]>0){
				 isLShaped = true; 
				}
			}	
		}
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
		//Opposite of isLocatedOnBorder()
		return isLocatedCentrally;
	}
	
	public boolean isLocatedOnBorder(int[][] pixels, Building b, int WIDTH, int HEIGHT){
		boolean isLocatedOnBorder = false;
		//north
		for(int i = 0; i < WIDTH; i++){
			if(pixels[i][3] == b.getBuildingNumber()){
				isLocatedOnBorder = true; 
			}
		}
		//south
		for(int i = 0; i < WIDTH; i++){
			if(pixels[i][490] == b.getBuildingNumber()){
				isLocatedOnBorder = true; 
			}
		}
		//west
		for(int i = 0; i < HEIGHT; i++){
			if(pixels[4][i] == b.getBuildingNumber()){
				isLocatedOnBorder = true; 
			}
		}
		//east
		for(int i = 0; i < HEIGHT; i++){
			if(pixels[270][i] == b.getBuildingNumber()){
				isLocatedOnBorder = true; 
			}
		}
		return isLocatedOnBorder;
	}
	
	public int isNorthernMost(Building[] newBuildings){
		int isNorthernMost = 0;
		int northernVal = 495; 
		Building[] buildings = newBuildings;
		for(int i = 1; i < buildings.length; i++){
			if(buildings[i].getCenterOfMassY() < northernVal){
				isNorthernMost = i;
				northernVal = buildings[i].getCenterOfMassY(); 
			}
		}
		return isNorthernMost;
	}
	
	public int isSouthernMost(Building[] newBuildings){
		int isSouthernMost = 0;
		int southernVal = 0; 
		Building[] buildings = newBuildings;
		for(int i = 1; i < buildings.length; i++){
			if(buildings[i].getCenterOfMassY() > southernVal){
				isSouthernMost = i;
				southernVal = buildings[i].getCenterOfMassY(); 
			}
		}
		return isSouthernMost;
	}
	
	public int isEasternMost(Building[] newBuildings){
		int isEasternMost = 0;
		int easternVal = 0; 
		Building[] buildings = newBuildings;
		for(int i = 1; i < buildings.length; i++){
			if(buildings[i].getCenterOfMassX() > easternVal){
				isEasternMost = i;
				easternVal = buildings[i].getCenterOfMassX(); 
			}
		}
		return isEasternMost;
	}
	
	public int isWesternMost(Building[] newBuildings){
		int isWesternMost = 0;
		int westernVal = 275; 
		Building[] buildings = newBuildings;
		for(int i = 1; i < buildings.length; i++){
			if(buildings[i].getCenterOfMassX() < westernVal){
				isWesternMost = i;
				westernVal = buildings[i].getCenterOfMassX(); 
			}
		}
		return isWesternMost;
	}
	
	public static void main( String[] args )
	{
		  System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		  WhatDescriptions whatDescriptions = new WhatDescriptions(); 
		  whatDescriptions.run(); 
	}
}
