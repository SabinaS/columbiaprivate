import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class CombineAndCluster {
	
	/*
	 * Combine the measure of color similarity (C) and texture 
	 * similarity (T), using linear sum: sum = r*T + (1-r)*C.
	 * The value of r ranges form 0 to 1. s
	 */
	public void combineColorAndTexture(){
		
	}
	
	/*
	 * Cluster images based on "complete link"
	 */
	public void clusterCompleteLink(){
		
	}
	
	/*
	 * Cluster images based on "single link"
	 */
	public void clusterSingleLink(){
		
	}
	
	public static void main( String[] args ){
	      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	      Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
	      System.out.println( "mat = " + mat.dump() );
	}
}
