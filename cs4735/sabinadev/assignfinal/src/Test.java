import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Test
{
   public static void main( String[] args )
   {
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
      Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
      System.out.println( "mat = " + mat.dump() );
      Test test = new Test();
      test.run("/ass3-campus.jpg");
	
   }
   
	public void run(String filename){
		//System.out.println("fileeeeeeeeeeeeee: " + filename); 
		Mat image = Highgui.imread(getClass().getResource(filename).getPath());
		System.out.println( "mat = " + image.dump() );
		Mat imageHSV = new Mat(image.size(), Core.DEPTH_MASK_8U);
		Highgui.imwrite("stupid.jpg", imageHSV);
	    Mat imageThresh = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	    Mat imageCanny = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
		System.out.println("this"); 
	}
}