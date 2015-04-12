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
	
	public boolean isRectangle(){
		boolean isRectangle = true;
		int width = building.getWidth();
		int height = building.getHeight();
		
		for(each x in height){
			for(each y in width){
				if(width == filled){
					continue;
				}
				else{
					isRectangle = false; 
				}
			}
		}
		
		return isRectangle; 
	}
	
	public boolean isSquare(){
		boolean isSquare = false;
		if(isRectangle()){
			if(building.height == building.width){
				isSquare = true; 
			}
		}
		
		return isSquare; 
	}
	
	public boolean isNonRectangle(){
		boolean isNonRectangle = false; 
		if(!isSquare() && !isRectangle()){
			isNonRectangle = true; 
		}
		return isNonRectangle; 
	}
	
	public boolean hasSimpleBoundary(){
		boolean hasSimple = false;
		//TODO
		return hasSimple; 
	}
	
	public boolean hasJaggedBoundary(){
		boolean hasJagged = false;
		//TODO
		return hasJagged; 
	}
	
	public boolean isIShaped(){
		boolean isIShaped = false;
		//TODO
		return isIShaped; 
	}
	
	public boolean isLShaped(){
		boolean isLShaped = false;
		//TODO
		return isLShaped; 
	}
	
	public boolean isSymmetricEastWest(){
		boolean isSymmetricEW = false;
		//TODO
		return isSymmetricEW; 
	}
	
	public boolean isSymmetricNorthSouth(){
		boolean isSymmetricNS = false;
		//TODO
		return isSymmetricNS; 
	}
	
	public boolean isOrientedEastWest(){
		boolean isOrientedEW = false;
		//TODO
		return isOrientedEW; 
	}
	
	public boolean isOrientedNorthSouth(){
		boolean isOrientedNS = false;
		//TODO
		return isOrientedNS; 
	}
	
	public boolean isLocatedCentrally(){
		boolean isLocatedCentrally = false;
		//TODO
		return isLocatedCentrally;
	}
	
	public boolean isLocatedOnBorder(){
		boolean isLocatedOnBorder = false;
		//TODO
		return isLocatedOnBorder;
	}
	
	public boolean isNorthernMost(){
		boolean isNorthernMost = false;
		//TODO
		return isNorthernMost;
	}
	
	public boolean isSouthernMost(){
		boolean isSouthernMost = false;
		//TODO
		return isSouthernMost;
	}
	
	public boolean isEasternMost(){
		boolean isEasternMost = false;
		//TODO
		return isEasternMost;
	}
	
	public boolean isWesternMost(){
		boolean isWesternMost = false;
		//TODOv v v
		return isWesternMost;
	}
	
	public static void main( String[] args )
	{
		  System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		  WhatDescriptions whatDescriptions = new WhatDescriptions(); 
		  whatDescriptions.run(); 
	}
}
