import java.util.ArrayList;
import java.util.Vector;

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
   /*
    * Takes in an image and generates a 3D array for the RGB
    * values. Then based on the RGB value, creates a 3D
    * color histogram. 
    */
	//public void generateHistogram(){
	public void run(){
		//TODO
		// Read in the image
		String filename = "Images/i16.ppm";
		Mat image = Highgui.imread(getClass().getResource(filename).getPath());
		Highgui.imwrite("histoimage.jpg", image);
		Mat b_hist = new Mat();
		Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
		//int histSize = 256;
		//float range[] = {0, 255};
		
		ArrayList<Mat> bgr_planes = new ArrayList<>(); 
		Core.split(image, bgr_planes);
		MatOfInt histSize = new MatOfInt(256); 
		MatOfFloat histRange = new MatOfFloat(0, 256);
	    boolean accumulate = false;

		Imgproc.calcHist(bgr_planes, new MatOfInt(0), new Mat(), b_hist, histSize, histRange, accumulate);

	    int hist_w = 89;
	    int hist_h = 60;
	    long bin_w;
	    bin_w = Math.round((double) (hist_w / 256));

	    Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC1);
	    Core.normalize(b_hist, b_hist, 3, histImage.rows(), Core.NORM_MINMAX);
	    
	    for (int i = 1; i < 256; i++) {         


	        Core.line(histImage, new Point(bin_w * (i - 1),hist_h- Math.round(b_hist.get( i-1,0)[0])), 
	                new Point(bin_w * (i), hist_h-Math.round(Math.round(b_hist.get(i, 0)[0]))),
	                new  Scalar(255, 0, 0), 2, 8, 0);

	    }

	    Highgui.imwrite("histogram.jpg", histImage);
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