import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import org.opencv.imgproc.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint; 
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.awt.image.DataBufferByte;

/*
 * Read in an image, reduce it, get its 'what' and 'where',
 * and determine whether it's combination is correct or not
 */ 

class ReduceData {
	// image_description should contain "what", "x_where" and "y_where" 
	Map<String, String> image_description = new HashMap<String, String>(); 
	
	
	public void run(String filename) {
		System.out.println("\nRunning ReduceData");
		BufferedImage image = null; 
		Mat binaryMat = null; 
		
		try {
	         image = ImageIO.read(getClass().getResource(filename)); 

		} catch (Exception e) {
	         System.out.println("Errors: " + e.getMessage());
	    }
		// Create a binary of the image
		binaryMat = createBinary(image); 

		// Determine the center of mass of isSkin field
		double[] centerOfMass = determineCenterOfMass(filename); 
		
		// Determine Position of skin
		int[] dimensions = {3000, 2590};
		determineWhere(dimensions, centerOfMass);

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
	
	         File ouptut = new File("Grayscale.jpg");
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
	 * Determines the center of mass, (x, y),
	 * of the binary field of bits
	 */
	public double[] determineCenterOfMass(String filename) {
		Mat hu = new Mat(); 
		
		// Find the contours 
		Mat image = getMat(filename); 
	    Mat imageHSV = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageBlurr = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageThresh = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	    Mat imageCanny = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    Imgproc.adaptiveThreshold(imageBlurr, imageThresh, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
	    Imgproc.Canny(imageBlurr, imageCanny, 100, 200); 
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

	    // Get the moments 
	    List<Moments> moments = new ArrayList<Moments>(contours.size());
	    for (int i = 0; i < contours.size(); i++) {
	        moments.add(i, Imgproc.moments(contours.get(i), false));
		    // Get hu moments
		    //Imgproc.HuMoments(moments.get(i), hu); 
	    }
	    System.out.println("Size of moments: " + moments.size());
	    Imgproc.HuMoments(moments.get(0), hu); 
	    System.out.println("hu size: " + hu.total());
	    double buff[] = new double[(int) (hu.total() * hu.channels())];
	    hu.get(0, 0, buff);
	    System.out.println("Buff values: " + buff[0] + " " + buff[1] + " " + buff[2]);
	    
	    // Determine the "what"
	    if((int)buff[2]>100){
	    	//System.out.println(buff[2] + " > 100");
	    	image_description.put("what", "palm"); 
	    }else{
	    	//System.out.println(buff[2] + " < 100");
	    	image_description.put("what", "fist"); 
	    }
	    System.out.println("What: " + image_description.get("what")); 
	    
	   // Compute the Center of Mass
		double[] centerOfMass = new double[2*contours.size()]; 
	    List<MatOfPoint2f> mc = new ArrayList<MatOfPoint2f>(contours.size()); 
	    int x = 0; 
	    for( int i = 0; i < contours.size(); i++ ){
	        //mc.add(( moments.get(i).get_m10()/moments.get(i).get_m00(), moments.get(i).get_m01()/moments.get(i).get_m00() ));
	        double x_coor =  moments.get(i).get_m10()/moments.get(i).get_m00(); 
	        double y_coor = moments.get(i).get_m01()/moments.get(i).get_m00(); 
	        centerOfMass[x] = x_coor;
	        centerOfMass[x+1] = y_coor; 
	        //System.out.println("Moments: " + "x " + x_coor + " y " + y_coor); 
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
		int left_bound = (int)(x_middle-(0.2*x_middle));
		int right_bound = (int)(x_middle+(0.2*x_middle));
		
		if(centerOfMass[0]<left_bound){
			image_description.put("x-where", "left");
		}else if(centerOfMass[0] > right_bound){
			image_description.put("x-where", "right");
		}else{
			image_description.put("x-where", "center");
		}
		
		// Compute y-location
		int y_middle = (int)dimensions[1]/2; 
		int upper_bound = (int)(y_middle-(0.1*y_middle));
		int lower_bound = (int)(y_middle+(0.1*y_middle)); 
		if(centerOfMass[1] < upper_bound){
			image_description.put("y-where", "upper");
			//System.out.println("Upper: " + centerOfMass[1]);
		}else if(centerOfMass[1] > lower_bound){
			image_description.put("y-where", "lower");
			//System.out.println("Lower: " + centerOfMass[1]);
		}else{
			image_description.put("y-where", "center");
			//System.out.println("Lower: " + centerOfMass[1]);
		}
		
		System.out.println("x-where: " + image_description.get("x-where")); 
		System.out.println("y-where: " + image_description.get("y-where")); 
	}
	
	public Mat getMat(String filename){
		Mat image = Highgui.imread(getClass().getResource(filename).getPath());
		return image; 
	}
}

class DefineGrammar {
	public void run(){
		Map<String,Map<String,String>> imageMap = new HashMap<String,Map<String, String>>();
		ArrayList<String> filesToSearch = defineGrammar(); 
		int size = filesToSearch.size(); 
		for(int i =0; i < size; i++){
			String filename = filesToSearch.get(i); 
			ReduceData reduceData = new ReduceData(); 
			reduceData.run(filename); 
			imageMap.put(filename, reduceData.image_description); 
		}
		String imageToRead = ""; 
		if(imageMap.get("/Test2/image0.jpg").get("what").equals("palm")){
			if(imageMap.get("/Test2/image1.jpg").get("what").equals("fist") 
					&& imageMap.get("/Test2/image1.jpg").get("x-where").equals("left")
					&& imageMap.get("/Test2/image1.jpg").get("y-where").equals("lower")){
				if(imageMap.get("/Test2/image2.jpg").get("what").equals("fist") 
						&& imageMap.get("/Test2/image2.jpg").get("x-where").equals("left")
						&& imageMap.get("/Test2/image2.jpg").get("y-where").equals("upper")){
					if(imageMap.get("/Test2/image3.jpg").get("what").equals("palm") 
						&& imageMap.get("/Test2/image3.jpg").get("x-where").equals("left")
						&& imageMap.get("/Test2/image3.jpg").get("y-where").equals("lower")){
						System.out.println("Password unlocked! HOOORAYYYY"); 
					}else{
						System.out.println("Need lower left palm"); 
					}
				}else{
					System.out.println("Need upper left fist"); 
				}
			}else{
				System.out.println("Need upper right fist"); 
			}
		}else{
			System.out.println("Need to start with palm!"); 
		}
		
	}
	public ArrayList<String> defineGrammar(){
		java.io.File file = new java.io.File("src/Test2/");
		long length = folderSize(file); 
		System.out.println("This many files in folder: " + length);
		ArrayList<String> filesToSearch = new ArrayList<String>(); 
		for(int i = 0; i<length; i++){
			String filename = "/Test2/image" + Integer.toString(i) + ".jpg"; 
			filesToSearch.add(filename); 
			System.out.println("filename " + filename); 
		}
		return filesToSearch; 
	}
	
	public static long folderSize(File directory) {
	    long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += 1; 
	        else
	            length += folderSize(file);
	    }
	    return length;
	}
	
}

public class DataReduction {
	public static void main(String[] args) {
		 // Load the library
		System.out.println("Hello, OpenCV");

		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		new DefineGrammar().run();
		System.out.println("After library.");
	}

}