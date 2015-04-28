import java.util.ArrayList;
import java.util.List;

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
		
		// Find the contours 
		String filename = "playing_card_3.jpg"; 
		Mat image = Highgui.imread(getClass().getResource(filename).getPath());
		
		Mat imageHSV = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageThresh = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	    Mat imageCanny = new Mat(image.size(), Core.DEPTH_MASK_ALL);
			
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    //Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    //Imgproc.adaptiveThreshold(imageHSV, imageThresh, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
	    Imgproc.threshold(imageHSV, imageThresh, 100,255, Imgproc.THRESH_BINARY);
	    Imgproc.Canny(image, imageCanny, 100, 200); 
	    Highgui.imwrite("Edges.jpg",imageCanny);
			    
	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
	    Imgproc.findContours(imageThresh, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    System.out.println("Contour size: " + contours.size());

	    // Draw the contours
	    //Mat drawing = new Mat(image.size(), Core.DEPTH_MASK_8U); 
	    Mat mask = Mat.zeros(image.rows(),image.cols(),image.type());
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
		
		
		
	}
	
	
	
	public static void main( String[] args )
	{
		  System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		  GetContours getContours = new GetContours(); 
		  getContours.run(); 
	}
}
