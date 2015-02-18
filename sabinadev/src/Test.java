import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;

/*
 * Created to test opencv
 */

class Test {

	public static void main(String[] args) {
		java.io.File file = new java.io.File("src/Images/");
		//long filelen = file.length();
		long length = folderSize(file); 
		System.out.println("This many files in folder: " + length);
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