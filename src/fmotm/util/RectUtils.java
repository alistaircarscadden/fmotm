package fmotm.util;

import static java.lang.Math.min;
import static java.lang.Math.max;

import org.newdawn.slick.geom.Rectangle;

public class RectUtils {

	private RectUtils() {
	}
	
	public static Rectangle intersection2D(float x0, float y0, float x0w, float y0h, float x1, float y1, float x1w, float y1h) {
		float[] int_x = intersection1D(x0, x0w, x1, x1w);
		if(int_x == null) return null;
		
		float[] int_y = intersection1D(y0, y0h, y1, y1h);
		if(int_y == null) return null;
		
		return new Rectangle(int_x[0], int_y[0], int_x[1], int_y[1]);
	}
	
	public static float[] intersection1D(float x0, float x0w, float x1, float x1w) {
		if(x0 + x0w >= x1 && x0 <= x1 + x1w) {
			float int_left = max(x0, x1);
			float int_width = min(x0 + x0w, x1 + x1w) - int_left;
			
			return new float[]{int_left, int_width};
		} else {
			return null;
		}
	}

}
