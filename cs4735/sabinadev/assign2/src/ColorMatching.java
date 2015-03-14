import java.util.ArrayList;
import java.util.List;
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
		Mat src;  
		src = Highgui.imread(getClass().getResource("Images/i15.ppm").getPath());
		Highgui.imwrite("histoimage.jpg", src);
		List<Mat> rgb_planes = new ArrayList<Mat>(); 
		Core.split(src, rgb_planes);
		
		MatOfInt histSize = new MatOfInt(256);
	    final MatOfFloat histRange = new MatOfFloat(0f, 256f);
	    boolean accumulate = false;
		Mat r_hist = new Mat();
		Mat g_hist = new Mat();
		Mat b_hist = new Mat(); 
		
		List<Mat> r_plane = new ArrayList<Mat>();
		r_plane.add(rgb_planes.get(0)); 
		List<Mat> g_plane = new ArrayList<Mat>();
		g_plane.add(rgb_planes.get(1)); 
		List<Mat> b_plane = new ArrayList<Mat>();
		b_plane.add(rgb_planes.get(2)); 
		
		Imgproc.calcHist(r_plane, new MatOfInt(0), new Mat(), r_hist, histSize, histRange, accumulate);
		Imgproc.calcHist(g_plane, new MatOfInt(0), new Mat(), g_hist, histSize, histRange, accumulate);
		Imgproc.calcHist(b_plane, new MatOfInt(0), new Mat(), b_hist, histSize, histRange, accumulate);

		int hist_w = 89;
		int hist_h = 60;
		long bin_w = Math.round((double) hist_w / 256); 
		
		Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC1);
		Core.normalize(r_hist, r_hist, 3, histImage.rows(), Core.NORM_MINMAX);
		Core.normalize(g_hist, g_hist, 3, histImage.rows(), Core.NORM_MINMAX);
		Core.normalize(b_hist, b_hist, 3, histImage.rows(), Core.NORM_MINMAX);
		
		for (int i = 1; i < 256; i++) {
	        Point p1 = new Point(bin_w * (i - 1), hist_h - Math.round(r_hist.get(i - 1, 0)[0]));
	        Point p2 = new Point(bin_w * (i), hist_h - Math.round(r_hist.get(i, 0)[0]));
	        Core.line(histImage, p1, p2, new Scalar(255, 0, 0), 2, 8, 0);

	        Point p3 = new Point(bin_w * (i - 1), hist_h - Math.round(g_hist.get(i - 1, 0)[0]));
	        Point p4 = new Point(bin_w * (i), hist_h - Math.round(g_hist.get(i, 0)[0]));
	        Core.line(histImage, p3, p4, new Scalar(0, 255, 0), 2, 8, 0);

	        Point p5 = new Point(bin_w * (i - 1), hist_h - Math.round(b_hist.get(i - 1, 0)[0]));
	        Point p6 = new Point(bin_w * (i), hist_h - Math.round(b_hist.get(i, 0)[0]));
	        Core.line(histImage, p5, p6, new Scalar(0, 0, 255), 2, 8, 0);

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