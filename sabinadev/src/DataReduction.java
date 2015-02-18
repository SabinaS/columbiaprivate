import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import org.opencv.imgproc.*;
import org.opencv.highgui.*; 
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint; 
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;

import java.awt.image.DataBufferByte;

/*
 * Read in an image, reduce it, get its 'what' and 'where',
 * and determine whether it's combination is correct or not
 */ 

class ReduceData {
	// image_description should contain "what", "x_where" and "y_where" 
	Map<String, String> image_description = new HashMap<String, String>(); 
	
	public void run() {
		System.out.println("\nRunning ReduceData");
		BufferedImage image = null; 
		Mat binaryMat = null; 
		
		try {
	         image = ImageIO.read(getClass().getResource("/PALM-CENTER-RIGHT.JPG")); 

		} catch (Exception e) {
	         System.out.println("Errors: " + e.getMessage());
	    }
		// Create a binary of the image
		binaryMat = createBinary(image); 

		// Determine if it's skin
		// TODO
		// isSkin();
		
		// Create an array of isSkin field

		// Determine the center of mass of isSkin field
		double[] centerOfMass = determineCenterOfMass(); 
		
		// Test if skin is palm or first 
		// TODO
		
		// Determine Position of skin
		int[] dimensions = {381, 457};
		determineWhere(dimensions, centerOfMass);

		// Save the visualized detection.
		String filename = "faceDetection.png";
		// System.out.println(String.format("Writing %s", filename));
		// Highgui.imwrite(filename, image);
	}
	
	/*
	 * Create a binary of the image read in
	 */
	public Mat createBinary(BufferedImage image){
		// Method params
		byte[] data = null; 
		byte[] data1 = null; 
		Mat mat = null; 
		Mat mat1 = null; 
		
		// Convert to Grayscale 
		try	{
			 data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	         mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
	         mat.put(0, 0, data);
	
	         mat1 = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC1);
	         Imgproc.cvtColor(mat, mat1, Imgproc.COLOR_RGB2GRAY);
	
	         data1 = new byte[mat1.rows() * mat1.cols() * (int)(mat1.elemSize())];
	         mat1.get(0, 0, data1);
	         BufferedImage image1 = new BufferedImage(mat1.cols(),mat1.rows(), BufferedImage.TYPE_BYTE_GRAY);
	         image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);
	
	         File ouptut = new File("fist-grayscale.jpg");
	         ImageIO.write(image1, "jpg", ouptut);
		} catch (Exception e) {
	         System.out.println("Errors: " + e.getMessage());
	    }
		
		// Apply binary threshold 
		try{
	         Imgproc.threshold(mat1,mat1,127,255,Imgproc.THRESH_BINARY);
	         Highgui.imwrite("ThreshBinary.jpg", mat1);
	         
	    }catch (Exception e) {
	         System.out.println("error: " + e.getMessage());
	    }
		
		return mat1; 
	}

	/*
	 * Determines if the bit passed in is skin or background
	 */
	public boolean isSkin() {
		boolean isSkin = false;

		if (isSkin) {
			isSkin = true;
		}
		return isSkin;
	}

	/*
	 * Determines the center of mass, (x, y),
	 * of the binary field of isSkin() bits
	 */
	public double[] determineCenterOfMass() {
		double[] centerOfMass = new double[10]; 
		
		// Find the contours 
		Mat image = getMat(); 
	    Mat imageHSV = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageBlurr = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageThresh = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	    Mat imageCanny = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    Imgproc.adaptiveThreshold(imageBlurr, imageThresh, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
	    Imgproc.Canny(imageBlurr, imageCanny, 100, 200); 
	    Highgui.imwrite("edges.jpg",imageCanny);
	    
	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
	    Imgproc.findContours(imageCanny, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    System.out.println("Contour size: " + contours.size());

	    // Draw the contours
	    //Mat drawing = new Mat(image.size(), Core.DEPTH_MASK_8U); 
	    Mat mask = Mat.zeros(image.rows(),image.cols(),image.type());
	    for( int i = 0; i< contours.size(); i++ )
	    {
	        Scalar color = new Scalar( 0,0,255);
	        //Imgproc.drawContours(drawing, contours, i, color, 1);
	        Imgproc.drawContours(mask, contours, -1, new Scalar(0,0,255));
	        System.out.println("contourArea: " + Imgproc.contourArea(contours.get(i)));

	    }   
	    Highgui.imwrite("contours.jpg",mask);

	    // Get the moments 
	    List<Moments> moments = new ArrayList<Moments>(contours.size());
	    for (int i = 0; i < contours.size(); i++) {
	        moments.add(i, Imgproc.moments(contours.get(i), false));
	    }
	    System.out.println("Size of moments: " + moments.size());
	    
	   //Compute the Center of Mass
	    List<MatOfPoint2f> mc = new ArrayList<MatOfPoint2f>(contours.size()); 
	    int x = 0; 
	    for( int i = 0; i < contours.size(); i++ ){
	        //mc.add(( moments.get(i).get_m10()/moments.get(i).get_m00(), moments.get(i).get_m01()/moments.get(i).get_m00() ));
	        double x_coor =  moments.get(i).get_m10()/moments.get(i).get_m00(); 
	        double y_coor = moments.get(i).get_m01()/moments.get(i).get_m00(); 
	        centerOfMass[x] = x_coor;
	        centerOfMass[x+1] = y_coor; 
	        System.out.println("Moments: " + x_coor + " " + y_coor); 
	        x = x+2; 
	    }
		return centerOfMass;
	}
	
	/*
	 * Determine if the isSkin field represents
	 * a palm or fist 
	 */
	public void determineWhat(){
		// TODO
	}
	
	/*
	 * Determine the position of the skin
	 */
	public void determineWhere(int[] dimensions, double[] centerOfMass){
		// Compute x-location
		int x_middle = (int)dimensions[0]/2; 
		int left_bound = (int)(x_middle-(0.05*x_middle));
		int right_bound = (int)(x_middle+(0.05*x_middle));
		
		if(centerOfMass[0]<left_bound){
			image_description.put("x-where", "left");
		}else if(centerOfMass[0] > right_bound){
			image_description.put("x-where", "right");
		}else{
			image_description.put("x-where", "center");
		}
		
		// Compute y-location
		int y_middle = (int)dimensions[1]/2; 
		int upper_bound = (int)(y_middle-(0.05*y_middle));
		int lower_bound = (int)(y_middle+(0.05*y_middle)); 
		if(centerOfMass[1]<upper_bound){
			image_description.put("y-where", "upper");
		}else if(centerOfMass[0] > lower_bound){
			image_description.put("y-where", "lower");
		}else{
			image_description.put("y-where", "center");
		}
		
		System.out.println("x-where: " + image_description.get("x-where")); 
		System.out.println("y-where: " + image_description.get("y-where")); 
	}
	
	public Mat getMat(){
		Mat image = Highgui.imread(getClass().getResource("/palm.jpg").getPath());
		return image; 
	}
}

class DefineGrammar {
	// TODO: define the grammar
}

public class DataReduction {
	public static void main(String[] args) {
		 // Load the library
		System.out.println("Hello, OpenCV");

		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new ReduceData().run();
		System.out.println("After library.");
	}

}