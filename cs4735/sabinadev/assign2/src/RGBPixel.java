
public class RGBPixel {
	int redVal;
	int blueVal;
	int greenVal;
	
	public RGBPixel(int red, int blue, int green){
		redVal = red;
		blueVal = blue;
		greenVal = green;
	}
	
	public void setRed(int red){
		redVal = red;
	}
	public void setGreen(int green){
		greenVal = green;
	}
	public void setBlue(int blue){
		blueVal = blue;
	}
	
	public int getRed(){
		return redVal;
	}
	public int getGreen(){
		return greenVal;
	}
	public int getBlue(){
		return blueVal; 
	}
}
