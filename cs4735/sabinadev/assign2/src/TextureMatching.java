import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class TextureMatching {

	/*
	 * Take in a colored image as input.
	 * Convert that image into black and white.
	 */
	public static void convertToBW(){
		//TODO
	}
	
	/*
	 * Measure the grayscale texture similarity, using 
	 * edginess distribution. 
	 */
	public static void measureGrayscaleTextureSimilar(){
		// TODO create Laplacian image for each image
		// TODO use the normalized L1 comparison to compare
		// texture similarity
	}
	
	/*
	 * Create a Laplacian image for each image
	 * passed in. 
	 */
	public void createLaplacianImage(){
		//TODO
	}
	
	/*
	 * Compare two given images using the normalized
	 * L1 comparison. 
	 */
	public void l1Compairson(){
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
	
	public static void main( String[] args ){
	      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	      Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
	      System.out.println( "mat = " + mat.dump() );
	}
}
