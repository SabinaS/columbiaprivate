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
		String filename = "Images/playing_card_5.jpg"; 
		Mat image = Highgui.imread(getClass().getResource(filename).getPath());
		
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
			    
	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); 
	    Mat hierarchy = new Mat();
	    Imgproc.findContours(imageThresh, contours, hierarchy, Imgproc.RETR_CCOMP,Imgproc.CHAIN_APPROX_SIMPLE);
	    System.out.println("Contour size: " + contours.size());
	    
	    // Get the moments 
	    List<Moments> moments = new ArrayList<Moments>(contours.size());
	    for (int i = 0; i < contours.size(); i++) {
	        moments.add(i, Imgproc.moments(contours.get(i), false));
		    // Get hu moments
		    //Imgproc.HuMoments(moments.get(i), hu); 
	    }
	    System.out.println("Size of moments: " + moments.size());
	    Mat hu =  new Mat();
	    Imgproc.HuMoments(moments.get(0), hu); 
	    System.out.println("hu size: " + hu.total());
	    double buff[] = new double[(int) (hu.total() * hu.channels())];
	    hu.get(0, 0, buff);
	    System.out.println("Buff values: " + buff[0] + " " + buff[1] + " " + buff[2]);
	    
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
	    String dump = hierarchy.dump();
		System.out.println("hierarchy: " + hierarchy.get(0, 0).getClass()); 
		System.out.println("size " + hierarchy.size()); 
		System.out.println("dump " + dump); 
		
	}
	
	
	
	public static void main( String[] args )
	{
		  System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		  GetContours getContours = new GetContours(); 
		  getContours.run(); 
	}
}
