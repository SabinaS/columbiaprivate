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
		
		 //float newSim = (colorSim+textureSim)/2; 
		 float newSim = ((float)0.25*textureSim) + ((float)0.75*colorSim); 
		 //System.out.println("c " + colorSim + " t " + textureSim + " n " + newSim); 
		 //float newSim = 1; 
		 float linearSum = (newSim*textureSim) + ((1-newSim)*colorSim); 
		 //System.out.println("c " + colorSim + " t " + textureSim + " n " + linearSum); 
		 
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
		}//end for
		
		//now go through the entire list and form bigger clusters
		while(list.size() > 7){ 
			//System.out.println("nsize: " + list.size()); 
			for(int a = 0; a < list.size(); a++){
				if((list.size() ==7) || (list.size() < 7)){
					break;
				}else{
					for(int b = 0; b < list.size(); b++){
						if(b==a){
							continue;
						}else{
							String[] farthest = new String[2];
							farthest = getFarthest(list.get(a), list.get(b)); 
							list.get(a).add(farthest[1]); 
							if(list.get(b).size() ==1){
								list.remove(b); 
							}else{
								list.get(b).remove(farthest[1]); 
							}
						}
					}
				}
				//System.out.println("sizeee: " + list.size()); 
			}//end for
		}//end while
		//System.out.println("size: " + list.size()); 
		for(int c = 0; c < list.size(); c++){
			List<String> clusters = new ArrayList<String>(); 
			clusters = list.get(c);
			for(String s: clusters){
				System.out.println("String: " + s); 
			}
			System.out.println("This is the end of the cluster."); 
		}

	}
	
	public String[] getFarthest(List<String> cluster1, List<String> cluster2){
		String[] farthest = new String[2];
		float minDist = 1000; //random value
		int count = 0; 
		if((cluster1.size() == 1) && (cluster2.size() == 1)){
			farthest[0] = cluster1.get(0);
			farthest[1] = cluster2.get(0); 
		}else{
			for(int i = 0; i < cluster1.size(); i++){
				for(int j = 0; j < cluster2.size(); j++){
					float linearSum = combineColorAndTexture(cluster1.get(i), cluster2.get(j));
					float localDist = 1 - linearSum; 
					//System.out.println("localD " + localDist); 
					if(localDist < minDist){
						farthest[0] = cluster1.get(i);
						//System.out.println("a " + farthest[0]); 
						farthest[1] = cluster2.get(j); 
						//System.out.println("b " + farthest[1]); 
						minDist = localDist; 
					}
					count++; 
					//System.out.println("count " + count); 
				}
			}
		}//end else 
		return farthest; 
	}
	
	/*
	 * Cluster images based on "single link"
	 */
	public void clusterSingleLink(){
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
		}//end for
		
		//now go through the entire list and form bigger clusters
		while(list.size() > 7){ 
			//System.out.println("nsize: " + list.size()); 
			for(int a = 0; a < list.size(); a++){
				if((list.size() ==7) || (list.size() < 7)){
					break;
				}else{
					for(int b = 0; b < list.size(); b++){
						if(b==a){
							continue;
						}else{
							String[] farthest = new String[2];
							farthest = getFarthest(list.get(a), list.get(b)); 
							list.get(a).add(farthest[1]); 
							if(list.get(b).size() ==1){
								list.remove(b); 
							}else{
								list.get(b).remove(farthest[1]); 
							}
						}
					}
				}
				//System.out.println("sizeee: " + list.size()); 
			}//end for
		}//end while
		//System.out.println("size: " + list.size()); 
		for(int c = 0; c < list.size(); c++){
			List<String> clusters = new ArrayList<String>(); 
			clusters = list.get(c);
			for(String s: clusters){
				System.out.println("String: " + s); 
			}
			System.out.println("This is the end of the cluster."); 
		}
	}
	
	public static void main( String[] args ){
	      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	      CombineAndCluster cc = new CombineAndCluster(); 
	      cc.clusterCompleteLink(); 
	}
}
