import java.awt.List;
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
	public void run() {
		System.out.println("\nRunning ReduceData");
		BufferedImage image = null; 
		Mat binaryMat = null; 
		
		try {
	         image = ImageIO.read(getClass().getResource("/FIST-CENTER-RIGHT.JPG")); 

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
		determineCenterOfMass(); 
		
		// Test if skin is palm or first 
		// TODO
		
		// Determine Position of skin
		// TODO

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
	public void determineCenterOfMass() {
		int[] centerOfMass = null;
		
		// Find the contours 
		Mat image = getMat(); 
	    Mat imageHSV = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageBlurr = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageA = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);

	    Highgui.imwrite("test1.jpg",imageBlurr);

	    ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();    
	    Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    //Imgproc.drawContours(imageBlurr, contours, 1, new Scalar(0,0,255));
	    System.out.println("Contour size: " + contours.size());
	    
	    
	    for(int i=0; i< contours.size();i++){
	        //System.out.println(Imgproc.contourArea(contours.get(i)));
	        if (Imgproc.contourArea(contours.get(i)) > 50 ){
	            Rect rect = Imgproc.boundingRect(contours.get(i));
	            //System.out.println(rect.height);
	            if (rect.height > 28){
	            //System.out.println(rect.x +","+rect.y+","+rect.height+","+rect.width);
	            Core.rectangle(image, new Point(rect.x,rect.height), new Point(rect.y,rect.width),new Scalar(0,0,255));
	            }
	        }
	    }
	    Highgui.imwrite("contours.jpg",image);

	    // Get the moments 
		//return centerOfMass;
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
	public void determineWhere(){
		// TODO
	}
	
	public Mat getMat(){
		Mat image = Highgui.imread(getClass().getResource("/fist-grayscale.jpg").getPath());
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