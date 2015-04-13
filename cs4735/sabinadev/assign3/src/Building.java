public class Building {

	int height;
	int width;
	int centerOfMassX;
	int centerOfMassY;
	int area;
	int buildingNumber;
	String buildingName; 
	
	public Building(){
		//TODO
	}
	
	public void setHeight(int newHeight){
		height = newHeight; 
	}
	
	public int getHeight(){
		return height; 
	}
	
	public void setWidth(int newWidth){
		width = newWidth; 
	}
	
	public int getWidth(){
		return width; 
	}
	
	public void setCenterOfMassX(int newCenterOfMassX){
		centerOfMassX = newCenterOfMassX;
	}
	
	public int getCenterOfMassX(){
		return centerOfMassX;
	}
	
	public void setCenterOfMassY(int newCenterOfMassY){
		centerOfMassY = newCenterOfMassY;
	}
	
	public int getCenterOfMassY(){
		return centerOfMassY;
	}
	
	public void setArea(int newArea){
		area = newArea; 
	}
	
	public int getArea(){
		return area; 
	}
	
	public void setBuildingNumber(int newBuildingNumber){
		buildingNumber = newBuildingNumber; 
	}
	
	public int getBuildingNumber(){
		return buildingNumber; 
	}
	
	public void setBuildingName(String newBuildingName){
		buildingName = newBuildingName; 
	}
	
	public String getBuildingName(){
		return buildingName; 
	}
}