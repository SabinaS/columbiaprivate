import org.opencv.core.Core;


public class WhereDescriptions {

	public void run(){
		//TODO
	}
	
	public boolean isNorth(Building s,Building t){
		boolean isNorth = false;
		//TODO
		return isNorth; 
	}
	
	public boolean isSouth(Building s,Building t){
		boolean isSouth = false;
		//TODO
		return isSouth; 
	}
	
	public boolean isEast(Building s, Building t){
		boolean isEast = false;
		//TODO
		return isEast; 
	}
	
	public boolean isWest(Building s, Building t){
		boolean isEwst = false;
		//TODO
		return isEwst; 
	}
	
	public boolean isNear(Building s, Building t){
		boolean isNear = false;
		//TODO
		return isNear; 
	}
	
	public static void main( String[] args )
	{
		  System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		  WhereDescriptions whereDescriptions = new WhereDescriptions(); 
		  whereDescriptions.run(); 
	}
}
