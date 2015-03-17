import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class CombineAndCluster {
	
	/*
	 * Combine the measure of color similarity (C) and texture 
	 * similarity (T), using linear sum: sum = r*T + (1-r)*C.
	 * The value of r ranges form 0 to 1. s
	 */
	public float combineColorAndTexture(String filename1, String filename2){
		//measure color similarity
		ColorMatching colorMatching = new ColorMatching(); 
		
		RGBPixel[][] pixels1= new RGBPixel[89][60];
		pixels1 = colorMatching.readImage(filename1); 
		int[][][] histogram1 = colorMatching.generateHistogram(pixels1); 
		
		RGBPixel[][] pixels2= new RGBPixel[89][60];
		pixels2 = colorMatching.readImage(filename2); 
		int[][][] histogram2 = colorMatching.generateHistogram(pixels2); 
		
		float colorSim = colorMatching.compareHistograms(histogram1, histogram2); 
		
		//measure texture similarity
		 TextureMatching textureMatching = new TextureMatching(); 
		
		 RGBPixel[][] originalImage1 = new RGBPixel[89][60];
		 originalImage1 = textureMatching.readImage(filename1);
		 RGBPixel[][] bwImage1 = textureMatching.generateBWImage(originalImage1); 
		 RGBPixel[][] lapImage1 = textureMatching.createLaplacianImage(bwImage1); 
		 int[] textHistogram1 = textureMatching.generateHistogram(lapImage1); 
		 
		 RGBPixel[][] originalImage2 = new RGBPixel[89][60];
		 originalImage2 = textureMatching.readImage(filename2);
		 RGBPixel[][] bwImage2 = textureMatching.generateBWImage(originalImage2); 
		 RGBPixel[][] lapImage2 = textureMatching.createLaplacianImage(bwImage2); 
		 int[] textHistogram2 = textureMatching.generateHistogram(lapImage2); 
		 
		 float textureSim = textureMatching.compareHistograms(textHistogram1, textHistogram2);
		
		 float newSim = (colorSim+textureSim)/2;  
		 float linearSum = (newSim*textureSim) + ((1-newSim)*colorSim); 
		 
		 return linearSum; 
	}
	
	/*
	 * Cluster images based on "complete link"
	 */
	public void clusterCompleteLink(){
		List<List<String>> list = new ArrayList<List<String>>();
		
		//create the initial 40 clusters 
		for(int i = 1; i< 41; i++){
			String filename = "";
			if(i<10){
				filename = "i0" + Integer.toString(i) + ".ppm";
			}else{
				filename = "i" + Integer.toString(i) + ".ppm";
			}
			//for each file, create a new arraylist called cluster
			List<String> cluster = new ArrayList<String>();
			//add the original filename to that cluster
			cluster.add(filename); 
			//add that cluster to the list
			list.add(cluster);
		}
		
		//now go through the entire


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
