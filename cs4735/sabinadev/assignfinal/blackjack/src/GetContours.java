import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;


public class GetContours {

	public void run(){
		
		int a = 550;
		int b = 780;
		int max_y = 2300; 
		boolean c = isSameY(a,b,max_y);
		System.out.println("isSameX : " + c); 
		
		// Read the image
		String filename = "Images/playing_card_3.png"; 
		Mat image = Highgui.imread(getClass().getResource(filename).getPath());
		
		// Apply the thresh and canny
		Mat imageHSV = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageThresh = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	    Mat imageCanny = new Mat(image.size(), Core.DEPTH_MASK_ALL);
		System.out.println("dims " + image.dims()); 
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    //Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    //Imgproc.adaptiveThreshold(imageHSV, imageThresh, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
	    Imgproc.threshold(imageHSV, imageThresh, 100,255, Imgproc.THRESH_BINARY);
	    Imgproc.Canny(image, imageCanny, 100, 200); 
	    Highgui.imwrite("Edges.jpg",imageCanny);
	    
	    // Get the contours
	    List<MatOfPoint> contours = getContours(image, imageThresh);
	    
	    // Get moments
	    List<Moments> moments = getMoments(contours, image); 
	    
	   // Reduce the number of moments
	    int actualMoments = getReducedMoments(moments); 
	    
		// Return the card number
	    int cardNumber = getCardNumber(actualMoments); 
	}


	private int getCardNumber(int actualMoments) {
		int extraContours = 5; 
		int cardNumber = actualMoments - extraContours; 
		return cardNumber;
	}


	public List<MatOfPoint> getContours(Mat image, Mat imageThresh){
		
		// Get the contours 
	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); 
	    Mat hierarchy = new Mat();
	    Imgproc.findContours(imageThresh, contours, hierarchy, Imgproc.RETR_CCOMP,Imgproc.CHAIN_APPROX_SIMPLE);
	    System.out.println("Contour size: " + contours.size());
	    

	    //Mat drawing = new Mat(image.size(), Core.DEPTH_MASK_8U); 
	    
	    //Create a copy of the image
	    Mat mask = Mat.zeros(image.rows(),image.cols(),image.type());
	    // Draw the contours
	    for( int i = 0; i< contours.size(); i++ )
	    {
	        Scalar color = new Scalar( 0,0,255);
	        Imgproc.drawContours(mask, contours, i, color, 1);
	        //Imgproc.drawContours(mask, contours, -1, new Scalar(0,0,255));
	        //System.out.println("contourArea: " + Imgproc.contourArea(contours.get(i)));
	        String iname = "contours" + Integer.toString(i) + ".jpg"; 
	        Highgui.imwrite(iname,mask); 
	    }   
	    Highgui.imwrite("Contours.jpg",mask); 
	    
	    String dump = hierarchy.dump();
		System.out.println("hierarchy: " + hierarchy.get(0, 0).getClass()); 
		System.out.println("size " + hierarchy.size()); 
		System.out.println("dump " + dump); 
		
	    return contours; 
	}
	
	public List<Moments> getMoments(List<MatOfPoint> contours, Mat image){
	    List<Moments> mu = new ArrayList<Moments>(contours.size());
	    for (int i = 0; i < contours.size(); i++) {
	        mu.add(i, Imgproc.moments(contours.get(i), false));
	        Moments p = mu.get(i);
	        int x = (int) (p.get_m10() / p.get_m00());
	        int y = (int) (p.get_m01() / p.get_m00());
	        System.out.println("x " + x + " -- y " + y); 
	        // Draw moment on the original image
	        Core.circle(image, new Point(x, y), 10, new Scalar(255,0,0), 1); 
	    }
	    Highgui.imwrite("Moments.jpg",image);
	    
	    return mu; 
	}
	
	
	public int getReducedMoments(List<Moments> moments) {
		int momentCount = moments.size(); 
		List<Moments> mu = moments;
		int max_x = 0;
		int max_y = 0;
		HashMap<Integer, Integer> sameValues = new HashMap<Integer, Integer>(); 
		ArrayList<HashMap> groups = new ArrayList<HashMap>(); 
		
		int numFound = 0; // how many of the same moments were found
		int maxSame = 0;  // the max amount of same moment found
		for (int i = 0; i < moments.size(); i++) {
			numFound = 0; 
	        Moments p = mu.get(i);
	        int x = (int) (p.get_m10() / p.get_m00());
	        int y = (int) (p.get_m01() / p.get_m00());
	        System.out.println("x " + x + " -- y " + y);
	       
	        if(x>max_x){
	        	max_x = x;
	        }
	        if(y>max_y){
	        	max_y = y; 
	        }
	        
	        for(int j=0; j< moments.size(); j++){
	        	 Moments p2 = mu.get(j);
	 	        int x2 = (int) (p2.get_m10() / p2.get_m00());
	 	        int y2 = (int) (p2.get_m01() / p2.get_m00());
	 	        
	 	        if(i!=j){
		 	        if(isSameX(x,x2) && isSameY(y, y2, max_y)){
		 	        	numFound++; 
		 	        	if(numFound >maxSame){
		 	        		maxSame = numFound; 
		 	        	}
		 	        	System.out.println("sameX: " + x + " " + x2 + "sameY: " + y + " " + y2 + " " +numFound); 
		 	        }
	        	}
	        }
	    }//end outter for
		
		// Check for extra contours 
		int extraMoments = (maxSame-1)*2; 
		if(extraMoments != 1){
		momentCount = momentCount - extraMoments; 
		}
		System.out.println("momentCount: " + momentCount); 
		
		return 0;
	}
	
	public boolean isSameX(int x1, int x2){
		boolean isSame = false; 
		int pixelDifference = 6; 
		if(x1>=(x2-pixelDifference) && x1<=(x2+pixelDifference)){
			isSame = true; 
		}
		if(x2>=(x1-pixelDifference) && x2<=(x1+pixelDifference)){
			isSame = true; 
		}
		
		return isSame; 
	}
	
	public boolean isSameY(int y1, int y2, int max_y){
		boolean isSame = false;
		double maxDifference = (double)max_y * 0.12; //diff of 12%
		if(y1>=(y2-maxDifference) && y1<=(y2+maxDifference)){
			isSame = true; 
		}
		if(y2>=(y1-maxDifference) && y2<=(y1+maxDifference)){
			isSame = true; 
		}
		return isSame; 
	}
	
	public static void main( String[] args )
	{
		  System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		  GetContours getContours = new GetContours(); 
		  getContours.run(); 
	}
}
