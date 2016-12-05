package fmotm.game.world;

public class Camera {
	private float x;
	private float y;
	private float tileScale;
	private int screenResolution_x;
	private int screenResolution_y;
	
	public Camera(float x, float y, float tileScale, int screenResolution_x, int screenResolution_y) {
		super();
		this.x = x;
		this.y = y;
		this.tileScale = tileScale;
		this.screenResolution_x = screenResolution_x;
		this.screenResolution_y = screenResolution_y;
	}
	
	// Returns the in-world min x value of display
	public float getX() {
		return x;
	}
	
	// Returns the in-world max x value of display
	public float getMaxX() {
		return x + (screenResolution_x / tileScale);
	}
	
	// Returns the in-world min y value of display
	public float getY() {
		return y;
	}
	
	// Returns the in-world max y value of display
	public float getMaxY() {
		return y + (screenResolution_y / tileScale);
	}
	
	// Returns the ratio of tile display to 1
	public float getTileScale() {
		return tileScale;
	}
	
	// Returns the number of pixels wide the screen is
	public int getScreenResolution_x() {
		return screenResolution_x;
	}
	
	// Returns the number of pixels tall the screen is
	public int getScreenResolution_y() {
		return screenResolution_y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setTileScale(float tileScale) {
		this.tileScale = tileScale;
	}
	public void setScreenResolution_x(int screenResolution_x) {
		this.screenResolution_x = screenResolution_x;
	}
	public void setScreenResolution_y(int screenResolution_y) {
		this.screenResolution_y = screenResolution_y;
	}
}
