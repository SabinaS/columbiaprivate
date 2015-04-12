import org.opencv.core.Core;


public class WhatDescriptions {
	
	private int magic_small; //TODO Magic number for small area
	private int magic_large; //TODO Magic number for large area
	private int magic_medium; //TODO Magic number for medium area
	private int magic_largest; // Corresponds to the integer value of the building
	private int magic_smallest; //Corresponds to the integer value of the building; 

	public void run(){
		//TODO
	}
	
	public boolean isSmall(){
		boolean isSmallBool = false;
		int area; //TODO get area from pixels
		if(area<magic_small){
			isSmallBool= true;
		}
		return isSmallBool; 
	}
	
	public boolean isMedium(){
		boolean isMediumBool = false;
		int area; //TODO get area from pixels
		if(area<magic_small){
			isMediumBool= true;
		}
		return isMediumBool; 
	}
	
	public boolean isLarge(){
		boolean isLargeBool = false;
		int area; //TODO get area from pixels
		if(area<magic_small){
			isLargeBool= true;
		}
		return isLargeBool; 
	}
	
	public boolean getSmallest(){
		//TODO
		int smallest; // Corresponds to the integer value of the building
		//Look through all buildings and get smallest area
		for(each building){
			if(building.area < smallest){
				smallest = building.area; //TODO
			}
		}
		
		return smallest; 
	}
	
	public boolean getLargest(){
		//TODO
		int largest; // Corresponds to the integer value of the building
		//Look through all buildings and get smallest area
		for(each building){
			if(building.area > largest){
				largest = building.area; //TODO
			}
		}
		
		return largest; 
	}
	
	public boolean isSquare(){
		boolean isSquare = false; 
		if(building.height == building.width){
			isSquare = true; 
		}
		
		return isSquare; 
	}
	
	public boolean isRectangle(){
		boolean isRectangle = false;
		if(building.height != building.width){
			isRectangle = true; 
		}
		
		return isRectangle; 
	}
	
	public boolean isNonRectangle(){
		boolean isNonRectangle = false; 
		if(!isSquare() && !isRectangle()){
			isNonRectangle = true; 
		}
		return isNonRectangle; 
	}
	
	public static void main( String[] args )
	{
		  System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		  WhatDescriptions whatDescriptions = new WhatDescriptions(); 
		  whatDescriptions.run(); 
	}
}
